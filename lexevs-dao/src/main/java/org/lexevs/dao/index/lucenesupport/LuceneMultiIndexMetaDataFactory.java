package org.lexevs.dao.index.lucenesupport;

import org.lexevs.dao.indexer.utility.CodingSchemeMetaData;
import org.lexevs.dao.indexer.utility.ConcurrentMetaData;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.FileSystemResource;

public class LuceneMultiIndexMetaDataFactory implements FactoryBean<ConcurrentMetaData> {


	@Override
	public ConcurrentMetaData getObject() throws Exception {
		return ConcurrentMetaData.getInstance();
	}

	@Override
	public Class getObjectType() {
		return CodingSchemeMetaData.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
