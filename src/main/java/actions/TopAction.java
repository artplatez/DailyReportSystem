package actions;

import java.io.IOException;

import javax.servlet.ServletException;

import constants.AttributeConst;
import constants.ForwardConst;

public class TopAction extends ActionBase {

	/**
	 * index　method 実行
	 */
	@Override
	public void process() throws ServletException, IOException {
		//method 実行
		invoke();
	}

	/**
	 * 一覧画面表示
	 */
	public void index() throws ServletException, IOException {
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
