package org.lexevs.dao.database.sqlimplementedmethods;

import org.lexevs.dao.database.access.AbstractBaseDao;
import org.lexevs.system.ResourceManager;

public abstract class AbstraceSqlImplementedMethodsDao extends AbstractBaseDao{
	protected static String KEY_SEPERATOR = "[:]";
	
	private ResourceManager resourceManager;
	private SQLImplementedMethodsDao sqlImplementedMethodsDao;
	
	public ResourceManager getResourceManager() {
		return resourceManager;
	}
	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}
	public SQLImplementedMethodsDao getSqlImplementedMethodsDao() {
		return sqlImplementedMethodsDao;
	}
	public void setSqlImplementedMethodsDao(
			SQLImplementedMethodsDao sqlImplementedMethodsDao) {
		this.sqlImplementedMethodsDao = sqlImplementedMethodsDao;
	}
}
