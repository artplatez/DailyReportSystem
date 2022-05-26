package services;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.NoResultException;

import actions.views.EmployeeConverter;
import actions.views.EmployeeView;
import constants.JpaConst;
import models.Employee;
import models.validators.EmployeeValidator;
import utils.EncryptUtil;

/**
 * 종업원 테이블의 조작에 관련한 처리를 실행
 *
 */
public class EmployeeService extends ServiceBase {
	/**
     * 指定されたページ数の一覧画面に表示するデータを取得し、EmployeeViewのリストで返却する
     * @param page ページ数
     * @return 表示するデータのリスト
     */
	public List<EmployeeView> getPerPage(int page){
		List<Employee> employees = em.createNamedQuery(JpaConst.Q_EMP_GET_ALL, Employee.class)
				.setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
				.setMaxResults(JpaConst.ROW_PER_PAGE)
				.getResultList();

		return EmployeeConverter.toViewList(employees);
	}
	/**
     * 従業員テーブルのデータの件数を取得し、返却する
     * @return 従業員テーブルのデータの件数
     */
	public long countAll() {
		long empCount = (long) em.createNamedQuery(JpaConst.Q_EMP_COUNT, Long.class)
				.getSingleResult();
		return empCount;

	}
	 /**
     * 社員番号、パスワードを条件に取得したデータをEmployeeViewのインスタンスで返却する
     * @param code 社員番号
     * @param plainPass パスワード文字列
     * @param pepper pepper文字列
     * @return 取得データのインスタンス 取得できない場合null
     */
	public EmployeeView findOne(String code, String plainPass, String pepper) {
		Employee e = null;
		try {
			//패스워드의 해쉬화
			String pass = EncryptUtil.getPasswordEncrypt(plainPass, pepper);
			//사원번호와 해쉬화한 패스워드를 조건으로, 삭제하지않은 종업원 1건을 습득
			e = em.createNamedQuery(JpaConst.Q_EMP_GET_BY_CODE_AND_PASS, Employee.class)
					.setParameter(JpaConst.JPQL_PARM_CODE, code)
					.setParameter(JpaConst.JPQL_PARM_PASSWORD, pass)
					.getSingleResult();
		} catch (NoResultException ex) {

		}
		return EmployeeConverter.toView(e);

	}
	 /**
     * idを条件に取得したデータをEmployeeViewのインスタンスで返却する
     * @param id
     * @return 取得データのインスタンス
     */
	public EmployeeView findOne(int id) {
		Employee e = findOneInternal(id);
		return EmployeeConverter.toView(e);
	}
	/**
     * 社員番号を条件に該当するデータの件数を取得し、返却する
     * @param code 社員番号
     * @return 該当するデータの件数
     */
	public long countByCode(String code) {
		//지정한 사원번호를 보유한 사원의 건수를 습득
		long employees_count = (long) em.createNamedQuery(JpaConst.Q_EMP_COUNT_RESISTERED_BY_CODE, Long.class)
				.setParameter(JpaConst.JPQL_PARM_CODE, code)
				.getSingleResult();
		return employees_count;
	}
	 /**
     * 画面から入力された従業員の登録内容を元にデータを1件作成し、従業員テーブルに登録する
     * @param ev 画面から入力された従業員の登録内容
     * @param pepper pepper文字列
     * @return バリデーションや登録処理中に発生したエラーのリスト
     */
	public List<String> create(EmployeeView ev, String pepper){
		//패스워드를 해쉬화해서 설정
		String pass = EncryptUtil.getPasswordEncrypt(ev.getPassword(), pepper);
		ev.setPassword(pass);

		//등록날짜,갱신날짜를 현재시각으로 설정
		LocalDateTime now = LocalDateTime.now();
		ev.setCreatedAt(now);
		ev.setUpdatedAt(now);
		//등록내용의validation
		List<String> errors = EmployeeValidator.validate(this, ev, true, true);

		//Validation error가 없다면 데이터등록
		if (errors.size() == 0) {
			create(ev);
		}
		return errors;
	}
	 /**
     * 画面から入力された従業員の更新内容を元にデータを1件作成し、従業員テーブルを更新する
     * @param ev 画面から入力された従業員の登録内容
     * @param pepper pepper文字列
     * @return バリデーションや更新処理中に発生したエラーのリスト
     */
	public List<String> update(EmployeeView ev, String pepper){
		//id를 조건으로 등록된 종업원 정보를 습득
		EmployeeView savedEmp = findOne(ev.getId());

		boolean validateCode = false;
		if (!savedEmp.getCode().equals(ev.getCode())) {
			//사원번호를 갱신할 경우
			//사원번호에 대해 validation실행
			validateCode = true;
			//변경한 후의 사원번호를 설정
			savedEmp.setCode(ev.getCode());
		}
		boolean validatePass = false;
		if (ev.getPassword() != null && !ev.getPassword().equals("")) {
			//패스워드 입력이 있을시

			//패스워드에 대한 validation 실시
			validatePass = true;

			//변경후의 패스워드를 해쉬화 해서 설정
			savedEmp.setPassword(
					EncryptUtil.getPasswordEncrypt(ev.getPassword(),pepper));
		}
		savedEmp.setName(ev.getName());//변경후의 이름을 설정
		savedEmp.setAdminFlag(ev.getAdminFlag());//변경후의 관리자프래그를 설정

		//갱신일자를 현재시간으로 설정
		LocalDateTime today = LocalDateTime.now();
		savedEmp.setUpdatedAt(today);

		//갱신내용에 대해 validation실시
		List<String> errors = EmployeeValidator.validate(this, savedEmp, validateCode, validatePass);

		//validation error없을 시 데이터 갱신
		if (errors.size() == 0) {
			update(savedEmp);
		}

		//에러반환
		return errors;
	}
	/**
     * idを条件に従業員データを論理削除する
     * @param id
     */
	public void destroy(Integer id) {
		//id를 조건으로 등록된 종업원정보 습득
		EmployeeView savedEmp = findOne(id);

		//갱신일자를 현재시간으로 설정
		LocalDateTime today = LocalDateTime.now();
		savedEmp.setUpdatedAt(today);
		//논리삭제플래그를세우기
		savedEmp.setDeleteFlag(JpaConst.EMP_DEL_TRUE);
		//갱신처리실행
		update(savedEmp);
	}
	 /**
     * 社員番号とパスワードを条件に検索し、データが取得できるかどうかで認証結果を返却する
     * @param code 社員番号
     * @param plainPass パスワード
     * @param pepper pepper文字列
     * @return 認証結果を返却す(成功:true 失敗:false)
     */
	public Boolean validateLogin(String code, String plainPass, String pepper) {
		boolean isValidEmployee = false;
		if(code!= null && !code.equals("")&& plainPass != null && !plainPass.equals("")) {
			EmployeeView ev = findOne(code, plainPass, pepper);

			if (ev != null && ev.getId() != null) {
				//데이터 습득 성공시, 증명성공
				isValidEmployee = true;
			}
		}
		return isValidEmployee;
	}
	/**
     * idを条件にデータを1件取得し、Employeeのインスタンスで返却する
     * @param id
     * @return 取得データのインスタンス
     */
	private Employee findOneInternal(int id) {
		Employee e = em.find(Employee.class, id);

		return e;
	}
	  /**
     * 従業員データを1件登録する
     * @param ev 従業員データ
     * @return 登録結果(成功:true 失敗:false)
     */
	private void create(EmployeeView ev) {
		em.getTransaction().begin();
		em.persist(EmployeeConverter.toModel(ev));
		em.getTransaction().commit();
	}
	/**
     * 従業員データを更新する
     * @param ev 画面から入力された従業員の登録内容
     */
	private void update(EmployeeView ev) {
		em.getTransaction().begin();
		Employee e = findOneInternal(ev.getId());
		EmployeeConverter.copyViewToModel(e, ev);
		em.getTransaction().commit();
	}

}
