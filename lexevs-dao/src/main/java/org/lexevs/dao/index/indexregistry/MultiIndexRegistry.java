package org.lexevs.dao.index.indexregistry;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.Filter;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.dao.index.lucenesupport.BaseLuceneIndexTemplate;
import org.lexevs.dao.index.lucenesupport.LuceneDirectoryCreator;
import org.lexevs.dao.index.lucenesupport.LuceneDirectoryFactory.NamedDirectory;
import org.lexevs.dao.index.lucenesupport.LuceneIndexTemplate;
import org.lexevs.dao.index.lucenesupport.MultiBaseLuceneIndexTemplate;
import org.lexevs.dao.indexer.utility.CodingSchemeMetaData;
import org.lexevs.dao.indexer.utility.ConcurrentMetaData;
import org.lexevs.system.constants.SystemVariables;
import org.lexevs.system.model.LocalCodingScheme;
import org.lexevs.system.service.SystemResourceService;
import org.springframework.beans.factory.InitializingBean;

public class MultiIndexRegistry implements IndexRegistry, InitializingBean {

	private SystemVariables systemVariables;
	
	//Wired to DelegatingSystemResourceService
	private SystemResourceService systemResourceService;
	
	//Wired to BaseLuceneIndexTemplate
	private LuceneIndexTemplate luceneIndexTemplate;	

	//Wired to DefaultLuceneDirectoryCreator
	private LuceneDirectoryCreator luceneDirectoryCreator;
	
	private ConcurrentMetaData concurrentMetaData;
	
	private Map<String,LuceneIndexTemplate> luceneIndexNameToTemplateMap = new HashMap<String,LuceneIndexTemplate>();
	
	private Map<String,LuceneIndexTemplate> multiCodingSchemeKeyToTemplateMap = new HashMap<String,LuceneIndexTemplate>();
	
	private Map<String,NamedDirectory> luceneIndexNameToDirctoryMap = new HashMap<String,NamedDirectory>();
	
	private Map<CodingSchemeUriVersionPair,String> luceneCodingSchemeToIndexNameMap = 
		new HashMap<CodingSchemeUriVersionPair,String>();
	
	private Map<String,Filter> codingSchemeFilterMap = new HashMap<String,Filter>();

	
	public void setCodingSchemeFilterMap(Map<String, Filter> codingSchemeFilterMap) {
		this.codingSchemeFilterMap = codingSchemeFilterMap;
	}

	public MultiIndexRegistry() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void registerCodingSchemeIndex(String codingSchemeUri,
			String version, String indexName) {
		LuceneIndexTemplate template = this.createLuceneIndexTemplate(indexName);
		luceneIndexNameToTemplateMap.put(indexName, template);
		luceneCodingSchemeToIndexNameMap.put(new CodingSchemeUriVersionPair(codingSchemeUri,version), indexName);

	}

	@Override
	public void unRegisterCodingSchemeIndex(String codingSchemeUri,
			String version) {
		CodingSchemeUriVersionPair key = new CodingSchemeUriVersionPair(codingSchemeUri,version);
		String indexName = 
			this.luceneCodingSchemeToIndexNameMap.get(key);
		
		if(!StringUtils.isBlank(indexName)) {
			luceneCodingSchemeToIndexNameMap.remove(key);
			String mapKey = DaoUtility.createKey(codingSchemeUri, version);
			this.codingSchemeFilterMap.remove(mapKey);
		}
	}
	
	protected NamedDirectory createIndexDirectory(String indexName) {
		String baseIndexPath = systemVariables.getAutoLoadIndexLocation();
		NamedDirectory namedDirectory = luceneDirectoryCreator.getDirectory(indexName, new File(baseIndexPath));
		luceneIndexNameToDirctoryMap.put(indexName, namedDirectory);
		return namedDirectory;
	}
	
	protected LuceneIndexTemplate createLuceneIndexTemplate(String indexName) {
		NamedDirectory directory = this.createIndexDirectory(indexName);
		
		return new BaseLuceneIndexTemplate(directory);
	}

	@Override
	public LuceneIndexTemplate getLuceneIndexTemplate(String codingSchemeUri,
			String version) {
		CodingSchemeUriVersionPair key = new CodingSchemeUriVersionPair(codingSchemeUri,version);
		if(! this.luceneCodingSchemeToIndexNameMap.containsKey(key)) {
			this.autoRegisterIndex(codingSchemeUri, version);
		}
		String indexName = luceneCodingSchemeToIndexNameMap.get(key);
		
		return this.luceneIndexNameToTemplateMap.get(indexName);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		File indexDir = new File(systemVariables.getAutoLoadIndexLocation());
		for (File f : indexDir.listFiles()) {
			if (f.exists() && f.isDirectory()) {
				luceneIndexNameToTemplateMap.put(f.getName(),
						buildTemplateForIndexName(f.getName()));
			}
		}
	}
	
	private LuceneIndexTemplate buildTemplateForIndexName(String name) {
		BaseLuceneIndexTemplate baseTemplate = new BaseLuceneIndexTemplate();
		for(CodingSchemeMetaData md : ConcurrentMetaData.getInstance().getCodingSchemeList()){
			if(md.getIndexDirectoryName().equals(name)){
				new BaseLuceneIndexTemplate(md.getDirectory());
				break;
			}
		}
		return baseTemplate;
	}

	protected void autoRegisterIndex(String codingSchemeUri, String version) {
		String codingSchemeName;
		
		try {
			codingSchemeName = systemResourceService.getInternalCodingSchemeNameForUserCodingSchemeName(codingSchemeUri, version);
			LocalCodingScheme lcs = LocalCodingScheme.getLocalCodingScheme(codingSchemeName, version);
			
			String indexName = concurrentMetaData.getIndexMetaDataValue(lcs.getKey());
			
			if(StringUtils.isBlank(indexName)) {
				throw new RuntimeException(
						"Cannot autoregister index for CodingScheme: " + codingSchemeUri + " Version: " + version + ".\n" +
						"The Lucene index for this CodingScheme may have been dropped, or indexing may have failed. " +
						"Reindexing may be needed.");
			}
			this.registerCodingSchemeIndex(codingSchemeUri, version, indexName);
			
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		} catch (LBParameterException e) {
            throw new RuntimeException("Problems getting coding scheme name. uri = " +
            		codingSchemeUri  + " version = " + version, e);
		}
	}
	protected static class CodingSchemeUriVersionPair {
		private String uri;
		private String version;
		
		private CodingSchemeUriVersionPair(String uri, String version){
			this.uri = uri;
			this.version = version;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((uri == null) ? 0 : uri.hashCode());
			result = prime * result
					+ ((version == null) ? 0 : version.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CodingSchemeUriVersionPair other = (CodingSchemeUriVersionPair) obj;
			if (uri == null) {
				if (other.uri != null)
					return false;
			} else if (!uri.equals(other.uri))
				return false;
			if (version == null) {
				if (other.version != null)
					return false;
			} else if (!version.equals(other.version))
				return false;
			return true;
		}	
	}
	
	@Override
	public LuceneIndexTemplate getCommonLuceneIndexTemplate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LuceneIndexTemplate getCommonLuceneIndexTemplate(
			List<AbsoluteCodingSchemeVersionReference> codingSchemes) {
		return getLuceneIndexTemplate(codingSchemes);
	}

	@Override
	public LuceneIndexTemplate getSearchLuceneIndexTemplate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void destroyIndex(String indexName) {
		//TODO Coordinate with ConcurrentMetaData List? Not sure we should even allow this here.   
		String location = systemVariables.getAutoLoadIndexLocation();
		try {
			Path path = Paths.get(location,indexName);
			FileUtils.deleteDirectory(new File(path.toString()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getCommonIndexName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Filter> getCodingSchemeFilterMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Filter> getBoundaryDocFilterMap() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Boundary Docs no longer in use");
	}
	
	public SystemVariables getSystemVariables() {
		return systemVariables;
	}

	public void setSystemVariables(SystemVariables systemVariables) {
		this.systemVariables = systemVariables;
	}

	public SystemResourceService getSystemResourceService() {
		return systemResourceService;
	}

	public void setSystemResourceService(SystemResourceService systemResourceService) {
		this.systemResourceService = systemResourceService;
	}

	public LuceneIndexTemplate getLuceneIndexTemplate() {
		return luceneIndexTemplate;
	}

	public void setLuceneIndexTemplate(LuceneIndexTemplate luceneIndexTemplate) {
		this.luceneIndexTemplate = luceneIndexTemplate;
	}

	public LuceneDirectoryCreator getLuceneDirectoryCreator() {
		return luceneDirectoryCreator;
	}

	public void setLuceneDirectoryCreator(
			LuceneDirectoryCreator luceneDirectoryCreator) {
		this.luceneDirectoryCreator = luceneDirectoryCreator;
	}

	public ConcurrentMetaData getConcurrentMetaData() {
		return concurrentMetaData;
	}

	public void setConcurrentMetaData(ConcurrentMetaData concurrentMetaData) {
		this.concurrentMetaData = concurrentMetaData;
	}
	
    private LuceneIndexTemplate getLuceneIndexTemplate(
            List<AbsoluteCodingSchemeVersionReference> codingSchemes) {
        List<NamedDirectory> directories = getNamedDirectoriesForCodingSchemes(codingSchemes);
        return new MultiBaseLuceneIndexTemplate(directories);
    }

	private List<NamedDirectory> getNamedDirectoriesForCodingSchemes(
			List<AbsoluteCodingSchemeVersionReference> codingSchemes) {
	    List<NamedDirectory> directories = new ArrayList<NamedDirectory>();
		for(CodingSchemeMetaData csmd: concurrentMetaData.getCodingSchemeList()){
		    for(AbsoluteCodingSchemeVersionReference ref : codingSchemes){
		    if(csmd.getCodingSchemeUri().equals(ref.getCodingSchemeURN()) && 
		            csmd.getCodingSchemeVersion().equals(ref.getCodingSchemeVersion())){
		    directories.add(csmd.getDirectory().getIndexReader().maxDoc() > 0 ? csmd.getDirectory(): csmd.getDirectory().refresh());}
		    }
		}
		return directories;
	}

}
