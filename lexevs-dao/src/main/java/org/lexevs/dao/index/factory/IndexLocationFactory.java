package org.lexevs.dao.index.factory;

import java.io.File;

import org.lexevs.system.constants.SystemVariables;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

public class IndexLocationFactory implements FactoryBean {

	private String indexName;
	
	private SystemVariables systemVariables;

	@Override
	public Object getObject() throws Exception {
		Assert.hasText(indexName);

		return new FileSystemResource(systemVariables.getAutoLoadIndexLocation() +
			File.separator + indexName);
	}

	@Override
	public Class getObjectType() {
		return Resource.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public SystemVariables getSystemVariables() {
		return systemVariables;
	}

	public void setSystemVariables(SystemVariables systemVariables) {
		this.systemVariables = systemVariables;
	}
}
