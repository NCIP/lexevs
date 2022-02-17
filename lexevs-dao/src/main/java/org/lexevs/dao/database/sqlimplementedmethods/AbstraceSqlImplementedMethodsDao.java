
package org.lexevs.dao.database.sqlimplementedmethods;

import org.lexevs.dao.database.access.AbstractBaseDao;
import org.lexevs.system.ResourceManager;

/**
 * The Class AbstraceSqlImplementedMethodsDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstraceSqlImplementedMethodsDao extends AbstractBaseDao{
	
	/** The KE y_ seperator. */
	protected static String KEY_SEPERATOR = "[:]";
	
	/** The resource manager. */
	private ResourceManager resourceManager;
	
	/** The sql implemented methods dao. */
	private SQLImplementedMethodsDao sqlImplementedMethodsDao;
	
	/**
	 * Gets the resource manager.
	 * 
	 * @return the resource manager
	 */
	public ResourceManager getResourceManager() {
		return resourceManager;
	}
	
	/**
	 * Sets the resource manager.
	 * 
	 * @param resourceManager the new resource manager
	 */
	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}
	
	/**
	 * Gets the sql implemented methods dao.
	 * 
	 * @return the sql implemented methods dao
	 */
	public SQLImplementedMethodsDao getSqlImplementedMethodsDao() {
		return sqlImplementedMethodsDao;
	}
	
	/**
	 * Sets the sql implemented methods dao.
	 * 
	 * @param sqlImplementedMethodsDao the new sql implemented methods dao
	 */
	public void setSqlImplementedMethodsDao(
			SQLImplementedMethodsDao sqlImplementedMethodsDao) {
		this.sqlImplementedMethodsDao = sqlImplementedMethodsDao;
	}
}