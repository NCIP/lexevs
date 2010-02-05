package org.lexevs.dao.systemresource.constants;

import org.LexGrid.LexBIG.Impl.dataAccess.ResourceManager;
import org.LexGrid.LexBIG.Impl.dataAccess.SystemVariables;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;

public class SystemVariablesFactory implements InitializingBean, FactoryBean {

	private SystemVariables systemVariables;
	
	public void afterPropertiesSet() throws Exception {
		ClassPathResource configFile = new ClassPathResource("lbconfig.props");
		if(configFile.exists()){
			System.setProperty("LG_CONFIG_FILE", configFile.getFile().getAbsolutePath());
		}
		systemVariables = ResourceManager.instance().getSystemVariables();
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
