package org.lexevs.dao.index.lucenesupport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.lexevs.dao.index.lucenesupport.LuceneDirectoryFactory.NamedDirectory;
import org.lexevs.dao.indexer.utility.CodingSchemeMetaData;
import org.lexevs.dao.indexer.utility.ConcurrentMetaData;
import org.lexevs.dao.indexer.utility.Utility;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry.ResourceType;
import org.lexevs.system.constants.SystemVariables;
import org.springframework.beans.factory.FactoryBean;

public class MultiNamedDirectoryFactory implements FactoryBean<List<NamedDirectory>>{

	private SystemVariables systemVariables;
	private LuceneDirectoryCreator directoryCreator;
	private ConcurrentMetaData concurrentMetaData;
	private LuceneMultiDirectoryFactory multiDirectoryFactory;
	
	@Override
	public List<NamedDirectory> getObject() throws Exception {
		List<RegistryEntry> registeredSchemes = LexEvsServiceLocator.getInstance().getRegistry().getAllRegistryEntriesOfType(ResourceType.CODING_SCHEME);
		List<NamedDirectory> namedDirectories = new ArrayList<NamedDirectory>();
		File indexDir = new File(systemVariables.getAutoLoadIndexLocation());
		for (File f : indexDir.listFiles()) {
			if (f.exists() && f.isDirectory()) {
				
				for(RegistryEntry re: registeredSchemes){
				    if(fileMatch(re,f)){
					concurrentMetaData.add(reconciliateDbToIndex(re, f, namedDirectories, directoryCreator));
				    }
				}
			}
		}
		return namedDirectories;
	}

	private CodingSchemeMetaData reconciliateDbToIndex(
			RegistryEntry re, 
			File f, 
			List<NamedDirectory> namedDirectories, 
			LuceneDirectoryCreator directoryCreator) throws LBParameterException, IOException {
		CodingSchemeMetaData csMetaData = null;
		    	csMetaData = new CodingSchemeMetaData(re.getResourceUri(), re.getResourceVersion(), 
		    			LexEvsServiceLocator.getInstance().getSystemResourceService().
		    			getInternalCodingSchemeNameForUserCodingSchemeName(re.getResourceUri(),re.getResourceVersion()),
		    		    makeNewDirectoryIfNone(re));
		return csMetaData;
	}

	private NamedDirectory makeNewDirectoryIfNone(RegistryEntry re) throws IOException {
		AbsoluteCodingSchemeVersionReference reference = new AbsoluteCodingSchemeVersionReference();
		reference.setCodingSchemeURN(re.getResourceUri());
		reference.setCodingSchemeVersion(re.getResourceVersion());
		return multiDirectoryFactory.getNamedDirectory(Utility.getIndexName(reference));
	}

	private boolean fileMatch(RegistryEntry re, File f) {
		AbsoluteCodingSchemeVersionReference reference = new AbsoluteCodingSchemeVersionReference();
		reference.setCodingSchemeURN(re.getResourceUri());
		reference.setCodingSchemeVersion(re.getResourceVersion());
		if(Utility.getIndexName(reference).equals(f.getName())){
			return true;
		}
		return false;
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
