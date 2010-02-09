package org.lexevs.dao.database.prefix;

import org.springframework.beans.factory.FactoryBean;

public class PrefixFactory implements FactoryBean {

	private PrefixResolver prefixResolver;
	
	public Object getObject() throws Exception {
		return prefixResolver.resolvePrefix();
	}

	public Class getObjectType() {
		return String.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public void setPrefixResolver(PrefixResolver prefixResolver) {
		this.prefixResolver = prefixResolver;
	}

	public PrefixResolver getPrefixResolver() {
		return prefixResolver;
	}

}
