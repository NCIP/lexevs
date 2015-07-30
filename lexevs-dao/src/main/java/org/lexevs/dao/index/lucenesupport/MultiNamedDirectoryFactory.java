package org.lexevs.dao.index.lucenesupport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.lexevs.dao.index.lucenesupport.LuceneDirectoryFactory.NamedDirectory;
import org.lexevs.dao.indexer.utility.ConcurrentMetaData;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.service.Registry.ResourceType;
import org.lexevs.system.constants.SystemVariables;
import org.springframework.beans.factory.FactoryBean;

public class MultiNamedDirectoryFactory implements FactoryBean {

	private SystemVariables systemVariables;
	private LuceneDirectoryCreator directoryCreator;
	private LuceneMultiIndexMetaDataFactory luceneMultiIndexMetaDataFactory;
	private ConcurrentMetaData concurrentMetaData;
	
	@Override
	public Object getObject() throws Exception {
		
		
		LexEvsServiceLocator.getInstance().getRegistry().getAllRegistryEntriesOfType(ResourceType.CODING_SCHEME);
		
		//LexEvsServiceLocator.getInstance().getResourceManager().getExternalCodingSchemeNameForUserCodingSchemeNameOrId(codingScheme, version);
		
		//ConcurrentMetaData concurrentMetaData = luceneMultiIndexMetaDataFactory.getObject();
		
		List<NamedDirectory> namedDirectories = new ArrayList<NamedDirectory>();
		
		File indexDir = new File(systemVariables.getAutoLoadIndexLocation());
		for (File f : indexDir.listFiles()) {
			if (f.exists() && f.isDirectory()) {
				
				//CodingSchemeMetaData codingSchemeMetaData = new CodingSchemeMetaData(codingSchemeUri, codingSchemeVersion, directory);
				
				namedDirectories.add(directoryCreator.getDirectory(f.getName(), f));
			}
		}

		return namedDirectories;
	}

	@Override
	public Class<?> getObjectType() {

		return List.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
