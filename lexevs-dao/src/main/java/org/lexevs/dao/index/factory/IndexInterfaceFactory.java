package org.lexevs.dao.index.factory;

import org.lexevs.dao.index.connection.IndexInterface;
import org.lexevs.system.constants.SystemVariables;
import org.springframework.beans.factory.FactoryBean;

public class IndexInterfaceFactory implements FactoryBean {

	private SystemVariables systemVariables;

	public Object getObject() throws Exception {
		return new IndexInterface(systemVariables.getAutoLoadIndexLocation());
	}

	public Class getObjectType() {
		return IndexInterface.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public void setSystemVariables(SystemVariables systemVariables) {
		this.systemVariables = systemVariables;
	}

	public SystemVariables getSystemVariables() {
		return systemVariables;
	}
}
