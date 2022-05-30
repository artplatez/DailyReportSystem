package actions;

import java.io.IOException;

/**
 * 認証に関する処理を行うAction Class
 */

import javax.servlet.ServletException;

import constants.AttributeConst;
import constants.ForwardConst;
import services.EmployeeService;

public class AuthAction extends ActionBase {

	private EmployeeService service;

	/**
	 * methodを実行する
	 */

	@Override
	public void process() throws ServletException, IOException {
		service = new EmployeeService();

		//method実行
		invoke();

		service.close();

	}
	/**
	 * Login画面を表示
	 * @throws ServletException
	 * @throws IOException
	 */
	public void showLogin() throws ServletException, IOException {
		//CSRF対策用トークン設定
		putRequestScope(AttributeConst.TOKEN, getTokenId());

		//SessionにFlush Messageが登録されている場合はRequest Scopeに設定する
		String flush = getSessionScope(AttributeConst.FLUSH);
		if(flush != null ) {
		putRequestScope(AttributeConst.FLUSH, flush);
		removeSessionScope(AttributeConst.FLUSH);
	}
	//ログイン画面を表示
	forward(ForwardConst.FW_LOGIN);

}
}
