package org.lexevs.dao.database.ibatis.sqlmap;

import java.util.Properties;

import org.lexevs.dao.database.prefix.PrefixResolver;
import org.springframework.beans.factory.FactoryBean;


public class SqlMapPrefixPropertiesFactory implements FactoryBean {

	private PrefixResolver prefixResolver;
	
	private String prefixKey = "defaultPrefix";
	
	@Override
	public Object getObject() throws Exception {
		Properties props = new Properties();
		props.setProperty(prefixKey, prefixResolver.resolveDefaultPrefix());
		return props;
	}

	@Override
	public Class getObjectType() {
		return Properties.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public PrefixResolver getPrefixResolver() {
		return prefixResolver;
	}

	public void setPrefixResolver(PrefixResolver prefixResolver) {
		this.prefixResolver = prefixResolver;
	}

	public String getPrefixKey() {
		return prefixKey;
	}

	public void setPrefixKey(String prefixKey) {
		this.prefixKey = prefixKey;
	}
}
