package org.lexevs.dao.index.lucenesupport;

import org.lexevs.dao.indexer.utility.CodingSchemeMetaData;
import org.lexevs.dao.indexer.utility.ConcurrentMetaData;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.FileSystemResource;

public class LuceneMultiIndexMetaDataFactory implements FactoryBean<ConcurrentMetaData> {

//	private FileSystemResource fileLocation;
//
//	public FileSystemResource getFileLocation() {
//		return fileLocation;
//	}
//
//	public void setFileLocation(FileSystemResource fileLocation) {
//		this.fileLocation = fileLocation;
//	}

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
