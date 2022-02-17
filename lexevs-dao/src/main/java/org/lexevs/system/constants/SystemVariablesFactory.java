
package org.lexevs.system.constants;

import org.lexevs.system.ResourceManager;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * A factory for creating SystemVariables objects.
 */
public class SystemVariablesFactory implements InitializingBean, FactoryBean {

	/** The system variables. */
	private SystemVariables systemVariables;
	
	/** The resource manager. */
	private ResourceManager resourceManager;
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		systemVariables = resourceManager.getSystemVariables();
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		return systemVariables;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class<SystemVariables> getObjectType() {
		return SystemVariables.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
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
	 * Gets the resource manager.
	 * 
	 * @return the resource manager
	 */
	public ResourceManager getResourceManager() {
		return resourceManager;
	}

}