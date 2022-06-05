package actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import actions.views.ReportView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import services.ReportService;

public class TopAction extends ActionBase {
	/**
	 * TOP Pageに関する処理を行うAction Class
	 */
	private ReportService service;

	/**
	 * index　method 実行
	 */
	@Override
	public void process() throws ServletException, IOException {
		service = new ReportService();

		//method 実行
		invoke();

		service.close();
	}

	/**
	 * 一覧画面表示
	 */
	public void index() throws ServletException, IOException {
		//Sessionからログイン中の従業員情報を取得
		EmployeeView loginEmployee = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

		//Login中の従業員が作成した日報データを、指定されたページ数の一覧画面に表示する分取得する
		int page = getPage();
		List<ReportView> reports = service.getMinePerPage(loginEmployee, page);

		//ログイン中の従業員が作成した日報データの件数を取得
		long myReportsCount = service.countAllMine(loginEmployee);

		putRequestScope(AttributeConst.REPORTS, reports); //取得した日報データ
		putRequestScope(AttributeConst.REP_COUNT, myReportsCount); //ログイン中の従業員が作成した日報の数
		putRequestScope(AttributeConst.PAGE, page); //ページ数
		putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //１ページに表示するレコードの数


		//SessionにFlushメッセージが設定されている場合はrequestScopeに移し替えSessionから削除
		String flush = getSessionScope(AttributeConst.FLUSH);
		if(flush != null) {
			putRequestScope(AttributeConst.FLUSH, flush);
		removeSessionScope(AttributeConst.FLUSH);
	}

	//一覧画面表示
	forward(ForwardConst.FW_TOP_INDEX);

	}

}
