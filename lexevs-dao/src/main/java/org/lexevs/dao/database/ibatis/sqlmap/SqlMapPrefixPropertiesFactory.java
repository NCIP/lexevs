
package org.lexevs.dao.database.ibatis.sqlmap;

import java.util.Properties;

import org.lexevs.system.constants.SystemVariables;
import org.springframework.beans.factory.FactoryBean;


public class SqlMapPrefixPropertiesFactory implements FactoryBean<Properties> {

	private SystemVariables systemVariables;
	
	private String prefixKey = "defaultPrefix";
	
	@Override
	public Properties getObject() throws Exception {
		Properties props = new Properties();
		props.setProperty(prefixKey, systemVariables.getAutoLoadDBPrefix());
		return props;
	}

	@Override
	public Class<Properties> getObjectType() {
		return Properties.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public String getPrefixKey() {
		return prefixKey;
	}

	public void setPrefixKey(String prefixKey) {
		this.prefixKey = prefixKey;
	}

	public void setSystemVariables(SystemVariables systemVariables) {
		this.systemVariables = systemVariables;
	}

	public SystemVariables getSystemVariables() {
		return systemVariables;
	}
}