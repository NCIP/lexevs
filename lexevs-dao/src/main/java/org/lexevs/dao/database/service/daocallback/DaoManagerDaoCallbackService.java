
package org.lexevs.dao.database.service.daocallback;

import org.lexevs.dao.database.access.DaoManager;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class DaoManagerDaoCallbackService.
 */
public class DaoManagerDaoCallbackService implements DaoCallbackService {

/** The dao manager. */
private DaoManager daoManager;
	
	/**
	 * Execute in dao layer.
	 * 
	 * @param daoCallback the dao callback
	 * 
	 * @return the t
	 */
	@Transactional
	public <T> T executeInDaoLayer(DaoCallback<T> daoCallback){
		return daoCallback.execute(daoManager);
	}

	/**
	 * Gets the dao manager.
	 * 
	 * @return the dao manager
	 */
	public DaoManager getDaoManager() {
		return daoManager;
	}

	/**
	 * Sets the dao manager.
	 * 
	 * @param daoManager the new dao manager
	 */
	public void setDaoManager(DaoManager daoManager) {
		this.daoManager = daoManager;
	}
}