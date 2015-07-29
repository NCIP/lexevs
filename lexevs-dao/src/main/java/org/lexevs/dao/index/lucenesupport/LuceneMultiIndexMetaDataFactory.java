package org.lexevs.dao.index.lucenesupport;

import java.io.File;

import org.lexevs.dao.indexer.utility.CodingSchemeMetaData;
import org.lexevs.dao.indexer.utility.ConcurrentMetaData;
import org.lexevs.dao.indexer.utility.MetaData;
import org.lexevs.system.constants.SystemVariables;
import org.springframework.beans.factory.FactoryBean;

public class LuceneMultiIndexMetaDataFactory implements FactoryBean<CodingSchemeMetaData> {
	
	private SystemVariables systemVariables;
	private ConcurrentMetaData codingSchemes;

	@Override
	public CodingSchemeMetaData getObject() throws Exception {
//		return new MetaData(new File(systemVariables.getAutoLoadIndexLocation()));
		return null;
	}

	@Override
	public Class getObjectType() {
		return CodingSchemeMetaData.class;
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
