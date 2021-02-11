
package org.lexevs.dao.index.factory;

import java.io.File;

import org.lexevs.system.constants.SystemVariables;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class MetadataIndexLocationFactory implements FactoryBean {
	
	private SystemVariables systemVariables;

	@Override
	public Object getObject() throws Exception {

		return new FileSystemResource(systemVariables.getAutoLoadIndexLocation() +
			File.separator + SystemVariables.getMetaDataIndexName());
	}

	@Override
	public Class<Resource> getObjectType() {
		return Resource.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public SystemVariables getSystemVariables() {
		return systemVariables;
	}

	public void setSystemVariables(SystemVariables systemVariables) {
		this.systemVariables = systemVariables;
	}
}