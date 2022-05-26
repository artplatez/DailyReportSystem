package models.validators;

import java.util.ArrayList;
import java.util.List;

import actions.views.EmployeeView;
import constants.MessageConst;


public class EmployeeValidator {
	/**
	 * 종업원 인스턴스의 각항목에 대해 validation실행
	 * @param service 불러올 항목의 서비스 클래스의 인스턴스
	 * @param ev EmployeeView의 인스턴스
	 * @param codeDuplicateCheckFlag 사원번호의 중복체크를 실행할지 말지 (실행:true 안함:false)
	 * @param passwordCheckFlag 패스워드의 입력체크를 실행할지말지
	 * @return 에러의 리스트
	 */
	public static List<String> validate(
			EmployeeService service, EmployeeView ev, Boolean codeDuplicateCheckFlag, Boolean passwordCheckFlag) {
		List<String> errors = new ArrayList<String>();

		String codeError = validateCode(service, ev.getCode(), codeDuplicateCheckFlag);
		if(!codeError.equals("")) {
			errors.add(codeError);

		}
		String nameError= validateName(ev.getName());
		if(!nameError.equals("")) {
			errors.add(nameError);
		}
		String passError = validatePassword(ev.getPassword(), passwordCheckFlag);
		if(!passError.equals("")) {
			errors.add(passError);

		}
		return errors;
	}

	/**
	 * 사원번호의 입력체크를 하고 에러메세지를 반환
	 * @param service EmployeeService의 Instance
	 * @param code 사원번호
	 * @param codeDuplicateCheckFlag 사원번호의 중복체크를 실시할지 말지
	 * @return 에러메세지
	 */

	private static String validateCode(EmployeeService service, String code, Boolean codeDuplicateCheckFlag) {

		//입력치가 없으면 에러메세지 반환
		if (code == null || code.equals("")) {
			return MessageConst.E_NOEMP_CODE.getMessage();

		}
		if (codeDuplicateCheckFlag) {
			//사원번호의 중복체크 실시
			long employeesCount = isDuplicateEmployee(service, code);

			//동일사번이 이미 등록되어있는 경우 에러메세지 반환
			if (employeesCount > 0) {
				return MessageConst.E_EMP_CODE_EXIST.getMessage();

			}
		}
		//에러가 없을 시 빈칸을 반환
		return "";

	}

	  /**
     * @param service EmployeeServiceのインスタンス
     * @param code 社員番号
     * @return 従業員テーブルに登録されている同一社員番号のデータの件数
     */

	private static long isDuplicateEmployee(EmployeeService service, String code) {
		long employeesCount = service.countByCode(code);
		return employeesCount;
	}
	/**
     * 氏名に入力値があるかをチェックし、入力値がなければエラーメッセージを返却
     * @param name 氏名
     * @return エラーメッセージ
     */
	private static String validateName(String name) {
		if(name == null || name.equals("")) {
			return MessageConst.E_NONAME.getMessage();
		}
		return "";
	}
	 /**
     * パスワードの入力チェックを行い、エラーメッセージを返却
     * @param password パスワード
     * @param passwordCheckFlag パスワードの入力チェックを実施するかどうか(実施する:true 実施しない:false)
     * @return エラーメッセージ
     */
	private static String validatePassword(String password, Boolean passwordCheckFlag) {
		//입력체크실시 동시에 입력값이 없을 시 에러메세지 반환
		if (passwordCheckFlag && (password == null || password.equals(""))) {
			return MessageConst.E_NOPASSWORD.getMessage();

		}
		return "";
	}

}
