package actions;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

//日報に関する処理を行うActionクラス

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import actions.views.ReportView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import constants.MessageConst;
import services.ReportService;

public class ReportAction extends ActionBase {

	private ReportService service;

	//メッそどを実行

	@Override
	public void process() throws ServletException, IOException {

		service = new ReportService();

		//methodを実行
		invoke();
		service.close();
	}
	/**
	 * 一覧画面を表示する
	 * @throws ServletException
	 * @throws IOException
	 */

	public void index() throws ServletException, IOException {
		//指定されたページ数の一覧画面に表示する日報データを取得
		int page  = getPage();
		List<ReportView> reports = service.getAllPerPage(page);

		//全日報データの件数を取得
		long reportsCount = service.countAll();

		putRequestScope(AttributeConst.REPORTS, reports); //取得した日報データ
		putRequestScope(AttributeConst.REP_COUNT, reportsCount); //全ての日報データの件数
		putRequestScope(AttributeConst.PAGE, page); //ページ数
		putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //１ページに表示するレコードの数

		//セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
		String flush = getSessionScope(AttributeConst.FLUSH);
		if(flush != null) {
			putRequestScope(AttributeConst.FLUSH, flush);
			removeSessionScope(AttributeConst.FLUSH);
		}

		//一覧画面を表示
		forward(ForwardConst.FW_REP_INDEX);
	}
	/**
	 * 新規登録画面を表示
	 * @throws ServletException
	 * @throws IOException
	 */
	public void entryNew() throws ServletException, IOException {
		putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF

		//日報情報の空インスタンスに、日報の日付＝今日の日付を設定
		ReportView rv= new ReportView();
		rv.setReportDate(LocalDate.now());//사전에 오늘의 날짜를 습득해서 수납. 일지작성시 오늘날짜가 자동으로 입력되어있는것이 더 편하기떄문
		putRequestScope(AttributeConst.REPORT, rv); //日付のみ設定ずみの日報インスタンス

		//新規登録画面を表示
		forward(ForwardConst.FW_REP_NEW);
	}
	/**
	 * 新規登録
	 * @throws ServletException
	 * @throws IOException
	 */

	public void create() throws ServletException, IOException {

		//CSRF
		if(checkToken()) {
			//日報の日付が入力されていなければ、今日の日付を設定
			LocalDate day = null;
			if(getRequestParam(AttributeConst.REP_DATE) == null || getRequestParam(AttributeConst.REP_DATE).equals("")) {
				day = LocalDate.now();

			} else {
				day = LocalDate.parse(getRequestParam(AttributeConst.REP_DATE));

			}
			//Sessionからログイン中の従業員情報を取得
			EmployeeView ev= (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

			//Parameterの値を元に日報情報のインスタンスを作成する
			ReportView rv= new ReportView(
					null,
					ev, //ログインしている従業員を、日報作成者として登録
					day,
					getRequestParam(AttributeConst.REP_TITLE),
					getRequestParam(AttributeConst.REP_CONTENT),
					null,
					null);

			//日報情報登録
			List<String> errors = service.create(rv);

			if(errors.size() > 0) {
				//登録中にエラーがあった場合
				putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF
				putRequestScope(AttributeConst.REPORT, rv);//入力された日報情報
				putRequestScope(AttributeConst.ERR, errors); //エラーのリスト

				//新規登録画面を再表示
				forward(ForwardConst.FW_REP_NEW);
			} else {
				//登録中にエラーがなかった場合
				//セッションに登録完了のフラッシュメッセージ設定
				putSessionScope(AttributeConst.FLUSH, MessageConst.I_REGISTERED.getMessage());

				//一覧画面にリダイレクト
				redirect(ForwardConst.ACT_REP, ForwardConst.CMD_INDEX);
			}
		}
	}
/**
 * 一覧画面
 * @throws ServletException
 * @throws IOException
 */
	public void show() throws ServletException, IOException {
		//idを条件に日報データを取得
		ReportView rv= service.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));

		if(rv == null) {
			//該当の日報データが存在しない場合エラー画面を表示
			forward(ForwardConst.FW_ERR_UNKNOWN);

		} else {
			putRequestScope(AttributeConst.REPORT, rv); //取得した日報データ
			//詳細画面表示
			forward(ForwardConst.FW_REP_SHOW);
		}
	}
/**
 * 日報edit
 * @throws ServletException
 * @throws IOException
 */
	public void edit() throws ServletException, IOException {

		//idを条件に日報データを取得
		ReportView rv = service.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));

		//Sessionからログイン中の従業員情報を取得
		EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

		if (rv == null || ev.getId() != rv.getEmployee().getId()) {
			//該当の日報データが存在しない、またはログインしている従業員が日報の作成者でない場合はエラー画面表示
			forward(ForwardConst.FW_ERR_NOTALLOWED);
		}else {
			putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF
			putRequestScope(AttributeConst.REPORT, rv); //取得した日報データ

			//編集画面を表示
			forward(ForwardConst.FW_REP_EDIT);
		}

	}
	/**
	 * update
	 * @throws ServletException
	 * @throws IOException
	 */
	public void update() throws ServletException, IOException {
		//CSRF
		if(checkToken()) {
			//idを条件に日報データを取得する
			ReportView rv= service.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));

			//入力された日報内容を設定
			rv.setReportDate(toLocalDate(getRequestParam(AttributeConst.REP_DATE)));
			rv.setTitle(getRequestParam(AttributeConst.REP_TITLE));
			rv.setContent(getRequestParam(AttributeConst.REP_CONTENT));

			//日報データを更新
			List<String> errors = service.update(rv);

			if(errors.size() > 0) {
				//更新中にエラーが発生した場合
				putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF
				putRequestScope(AttributeConst.REPORT, rv); //入力された内容
				putRequestScope(AttributeConst.ERR, errors); //エラーのリスト

				//編集画面を再表示
				forward(ForwardConst.FW_REP_EDIT);
			}else {
				//更新中にエラーがなかった場合
				//sessionに更新完了のフラッシュメッセージを設定
				putSessionScope(AttributeConst.FLUSH, MessageConst.I_UPDATED.getMessage());

				//一覧画面にリダイレクト
				redirect(ForwardConst.ACT_REP, ForwardConst.CMD_INDEX);
			}

		}
	}

}
