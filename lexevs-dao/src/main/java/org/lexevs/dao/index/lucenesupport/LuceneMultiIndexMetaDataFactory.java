package org.lexevs.dao.index.lucenesupport;

import java.io.File;

import org.lexevs.dao.indexer.utility.CodingSchemeMetaData;
import org.lexevs.dao.indexer.utility.ConcurrentMetaData;
import org.lexevs.dao.indexer.utility.MetaData;
import org.lexevs.system.constants.SystemVariables;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.FileSystemResource;

public class LuceneMultiIndexMetaDataFactory implements FactoryBean<ConcurrentMetaData> {
	

	public FileSystemResource getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(FileSystemResource fileLocation) {
		this.fileLocation = fileLocation;
	}

	private FileSystemResource fileLocation;

	@Override
	public ConcurrentMetaData getObject() throws Exception {
		return ConcurrentMetaData.getInstance(fileLocation.getPath());
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
