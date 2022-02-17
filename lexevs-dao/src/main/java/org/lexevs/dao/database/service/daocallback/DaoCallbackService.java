
package org.lexevs.dao.database.service.daocallback;

import org.lexevs.dao.database.access.DaoManager;

/**
 * The Interface DatabaseService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface DaoCallbackService {

	/**
	 * Execute in dao layer.
	 * 
	 * @param daoCallback the dao callback
	 * 
	 * @return the t
	 */
	public <T> T executeInDaoLayer(DaoCallback<T> daoCallback);
	
	/**
	 * The Interface DaoCallback.
	 * 
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public interface DaoCallback<T> {
		
		/**
		 * Execute.
		 * 
		 * @param daoManager the dao manager
		 * 
		 * @return the t
		 */
		public T execute(DaoManager daoManager);
	}
}