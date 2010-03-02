package org.lexevs.dao.index.factory;

import org.lexevs.system.constants.SystemVariables;
import org.springframework.beans.factory.FactoryBean;

import edu.mayo.informatics.indexer.api.IndexerService;

public class IndexerServiceFactory implements FactoryBean {

	private SystemVariables systemVariables;
	
	public Object getObject() throws Exception {
		return new IndexerService(systemVariables.getAutoLoadIndexLocation());
	}

	public Class getObjectType() {
		return IndexerService.class;
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
