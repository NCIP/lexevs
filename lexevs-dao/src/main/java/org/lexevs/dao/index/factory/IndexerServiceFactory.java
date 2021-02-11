
package org.lexevs.dao.index.factory;

import org.lexevs.dao.indexer.api.IndexerService;
import org.lexevs.system.constants.SystemVariables;
import org.springframework.beans.factory.FactoryBean;

/**
 * A factory for creating IndexerService objects.
 */
public class IndexerServiceFactory implements FactoryBean {

	/** The system variables. */
	private SystemVariables systemVariables;
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		return new IndexerService(systemVariables.getAutoLoadIndexLocation());
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		return IndexerService.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}

	/**
	 * Sets the system variables.
	 * 
	 * @param systemVariables the new system variables
	 */
	public void setSystemVariables(SystemVariables systemVariables) {
		this.systemVariables = systemVariables;
	}

	/**
	 * Gets the system variables.
	 * 
	 * @return the system variables
	 */
	public SystemVariables getSystemVariables() {
		return systemVariables;
	}

}