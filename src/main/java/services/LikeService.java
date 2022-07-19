package services;

import java.time.LocalDateTime;
import java.util.List;

import actions.views.EmployeeConverter;
import actions.views.EmployeeView;
import actions.views.LikeConverter;
import actions.views.LikeView;
import actions.views.ReportConverter;
import actions.views.ReportView;
import constants.JpaConst;
import models.Like;

/**
 * 日報テーブルの操作に関する処理を行うクラス
 *
 */
public class LikeService extends ServiceBase {

	/**
	 *
	 * @param report
	 * @param page
	 * @return
	 */

	public List<LikeView> getMinePerPage(ReportView report, int page){
		List<Like> likes = em.createNamedQuery(JpaConst.Q_LIKE_GET_ALL_MINE, Like.class)
				.setParameter(JpaConst.REP_COL_ID, ReportConverter.toModel(report))
				.setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
				.setMaxResults(JpaConst.ROW_PER_PAGE)
				.getResultList();
		return LikeConverter.toViewList(likes);
	}
	/**
	 * 指定した従業員が作成した日報データの件数を取得し、返却する
	 * @param employee
	 * @return 日報データの件数
	 */
	public long countAllMine(ReportView report) {
		long count = (long) em.createNamedQuery(JpaConst.Q_LIKE_COUNT_ALL_MINE, Long.class)
				.setParameter(JpaConst.REP_COL_ID, ReportConverter.toModel(report))
				.getSingleResult();

		return count;
	}

	/**
	 * 指定されたページ数の一覧画面に表示する日報データを取得し、ReportViewのリストで返却する
	 * @param page
	 * @return
	 */
	public List<LikeView> getAllPerPage(int page){
		List<Like> likes = em.createNamedQuery(JpaConst.Q_LIKE_GET_ALL, Like.class)
				.setFirstResult(JpaConst.ROW_PER_PAGE * (page -1))
				.setMaxResults(JpaConst.ROW_PER_PAGE)
				.getResultList();
		return LikeConverter.toViewList(likes);

	}

	/**
	 * 日報テーブルのデータの件数を取得し、返却する
	 * @return データの件数
	 */
	public long countAll() {
		long likes_count = (long) em.createNamedQuery(JpaConst.Q_LIKE_COUNT, Long.class)
				.getSingleResult();
		return likes_count;
	}

	/**
	 * idを条件に取得したデータをReportViewのインスタンスで返却
	 * @param id
	 * @return 取得データのインスタンス
	 */
	public LikeView findOne(int id) {
		return LikeConverter.toView(findOneInternal(id));
	}

	/**
	 * 画面から入力された日報の登録内容を元にデータを1件作成し、日報テーブルに登録
	 * @param rv　日報の登録内容
	 * @return　バリデーションで発生したエラーのリスト
	 */
	public void create(LikeView lv){
			LocalDateTime ldt = LocalDateTime.now();
			lv.setCreatedAt(ldt);
			lv.setUpdatedAt(ldt);
			createInternal(lv);
		}
	/**
	 * 画面から入力された日報の登録内容を元に、日報データを更新する
	 * @param rv　日報の更新内容
	 * @return　バリデーションで発生したエラーのリスト
	 */
	public void update(LikeView lv){
			LocalDateTime ldt = LocalDateTime.now();
			lv.setUpdatedAt(ldt);

			updateInternal(lv);
		}
		//Validationで発生したエラーを返却
	/**
	 * idを条件にデータを1件取得する
	 * @param id
	 * @return 取得データのインスタンス
	 */
	private Like findOneInternal(int id) {
		return em.find(Like.class, id);
	}

	private void createInternal(LikeView lv) {
		em.getTransaction().begin();
		em.persist(LikeConverter.toModel(lv));
		em.getTransaction().commit();
	}
	/**
	 * 日報データを更新する
	 * @param rv 日報データ
	 */

	private void updateInternal(LikeView lv) {
		em.getTransaction().begin();
		Like l = findOneInternal(lv.getId());
		LikeConverter.copyViewToModel(l, lv);
		em.getTransaction().commit();
	}
	public long countAllMine(EmployeeView loginEmployee) {
		long count = (long) em.createNamedQuery(JpaConst.Q_LIKE_COUNT_ALL_MINE, Long.class)
				.setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(loginEmployee))
				.getSingleResult();

		return count;
	}
}
