package org.lexevs.dao.index.lucenesupport;

import java.io.File;

import org.lexevs.system.constants.SystemVariables;
import org.springframework.beans.factory.FactoryBean;

import edu.mayo.informatics.indexer.utility.MetaData;

public class LuceneIndexMetadataFactory implements FactoryBean {
	
	private SystemVariables systemVariables;

	@Override
	public Object getObject() throws Exception {
		return new MetaData(new File(systemVariables.getAutoLoadIndexLocation()));
	}

	@Override
	public Class getObjectType() {
		return MetaData.class;
	}

	@Override
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
