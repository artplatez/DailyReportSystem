package services;

import javax.persistence.EntityManager;

import utils.DBUtil;

/**
 * DB접속에 관련된 공동처리를 실행
 *
 */

public class ServiceBase {
	/**
	 * Entity Manager Instance
	 */
	protected EntityManager em = DBUtil.createEntityManager();

	//Entity manager Close

	public void close() {
		if (em.isOpen()) {
			em.close();
		}
	}

}
