package org.lexevs.dao.index.lucenesupport;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.lexevs.dao.index.lucenesupport.LuceneDirectoryFactory.NamedDirectory;
import org.lexevs.dao.indexer.utility.CodingSchemeMetaData;
import org.lexevs.dao.indexer.utility.ConcurrentMetaData;
import org.lexevs.dao.indexer.utility.Utility;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.system.constants.SystemVariables;
import org.springframework.beans.factory.FactoryBean;

public class MultiNamedDirectoryFactory implements FactoryBean<ConcurrentMetaData>{

	private SystemVariables systemVariables;
	private LuceneDirectoryCreator directoryCreator;
	private ConcurrentMetaData concurrentMetaData;
	
	@Override
	public ConcurrentMetaData getObject() throws Exception {
//		List<RegistryEntry> registeredSchemes = LexEvsServiceLocator.getInstance().getRegistry().getAllRegistryEntriesOfType(ResourceType.CODING_SCHEME);
//		List<NamedDirectory> namedDirectories = new ArrayList<NamedDirectory>();
//		File indexDir = new File(systemVariables.getAutoLoadIndexLocation());
//		for (File f : indexDir.listFiles()) {
//			if (f.exists() && f.isDirectory()) {
//				
//				for(RegistryEntry re: registeredSchemes){
//				    if(fileMatch(re,f)){
//					concurrentMetaData.add(reconciliateDbToIndex(re, f, namedDirectories, directoryCreator));
//				    }
//				}
//			}
//		}
		return concurrentMetaData;
	}

	private CodingSchemeMetaData reconciliateDbToIndex(
			RegistryEntry re, 
			File f, 
			List<NamedDirectory> namedDirectories, 
			LuceneDirectoryCreator directoryCreator) throws LBParameterException, IOException {
		CodingSchemeMetaData csMetaData = null;
		    	csMetaData = new CodingSchemeMetaData(re.getResourceUri(), re.getResourceVersion(), 
//		    			LexEvsServiceLocator.getInstance().getSystemResourceService().
//		    			getInternalCodingSchemeNameForUserCodingSchemeName(re.getResourceUri(),re.getResourceVersion()),
		    			"Placeholdername",
		    		    makeNewDirectoryIfNone(re));
		    	
		return csMetaData;
	}

	private NamedDirectory makeNewDirectoryIfNone(RegistryEntry re) throws IOException {
		LuceneMultiDirectoryFactory multiDirectoryFactory = new LuceneMultiDirectoryFactory();
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

	public SystemVariables getSystemVariables() {
		return systemVariables;
	}

	public void setSystemVariables(SystemVariables systemVariables) {
		this.systemVariables = systemVariables;
	}

	public LuceneDirectoryCreator getDirectoryCreator() {
		return directoryCreator;
	}

	public void setDirectoryCreator(LuceneDirectoryCreator directoryCreator) {
		this.directoryCreator = directoryCreator;
	}

	public ConcurrentMetaData getConcurrentMetaData() {
		return concurrentMetaData;
	}

	public void setConcurrentMetaData(ConcurrentMetaData concurrentMetaData) {
		this.concurrentMetaData = concurrentMetaData;
	}


}
