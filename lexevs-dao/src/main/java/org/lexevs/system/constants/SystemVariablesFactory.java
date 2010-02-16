package org.lexevs.system.constants;

import org.lexevs.system.ResourceManager;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class SystemVariablesFactory implements InitializingBean, FactoryBean {

	private SystemVariables systemVariables;
	private ResourceManager resourceManager;
	
	public void afterPropertiesSet() throws Exception {
		systemVariables = resourceManager.getSystemVariables();
	}

	public Object getObject() throws Exception {
		return systemVariables;
	}

	public Class<SystemVariables> getObjectType() {
		return SystemVariables.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	public ResourceManager getResourceManager() {
		return resourceManager;
	}

}
