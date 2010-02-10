package org.lexevs.system.constants;

import org.lexevs.system.ResourceManager;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;

public class SystemVariablesFactory implements InitializingBean, FactoryBean {

	private SystemVariables systemVariables;
	
	public void afterPropertiesSet() throws Exception {
		ClassPathResource configFile = new ClassPathResource(SystemVariables.CONFIG_FILE_NAME);
		if(configFile.exists()){
			System.setProperty("LG_CONFIG_FILE", configFile.getFile().getAbsolutePath());
			systemVariables = ResourceManager.instance().getSystemVariables();
		} else {
			systemVariables = ResourceManager.instance().getSystemVariables();
			System.setProperty("LG_CONFIG_FILE", systemVariables.getConfigFileLocation());
		}
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

}
