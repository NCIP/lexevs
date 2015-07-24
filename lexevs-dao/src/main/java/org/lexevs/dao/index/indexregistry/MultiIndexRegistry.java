package org.lexevs.dao.index.indexregistry;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.apache.lucene.search.Filter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.dao.index.factory.IndexLocationFactory;
import org.lexevs.dao.index.indexregistry.SingleIndexRegistry.CodingSchemeUriVersionPair;
import org.lexevs.dao.index.lucenesupport.BaseLuceneIndexTemplate;
import org.lexevs.dao.index.lucenesupport.LuceneDirectoryCreator;
import org.lexevs.dao.index.lucenesupport.LuceneIndexTemplate;
import org.lexevs.dao.index.lucenesupport.LuceneDirectoryFactory.NamedDirectory;
import org.lexevs.dao.indexer.utility.MetaData;
import org.lexevs.system.constants.SystemVariables;
import org.lexevs.system.model.LocalCodingScheme;
import org.lexevs.system.service.SystemResourceService;
import org.springframework.beans.factory.InitializingBean;

public class MultiIndexRegistry implements IndexRegistry, InitializingBean {

	//TODO make any needed adjustments for a multi-Index implementation
	private SystemVariables systemVariables;
	
	//Wired to LuceneIndexMetadataFactory
	private MetaData metaData;
	
	//Wired to DelegatingSystemResourceService
	private SystemResourceService systemResourceService;
	
	//Wired to BaseLuceneIndexTemplate
	private LuceneIndexTemplate luceneIndexTemplate;
	
	//Wired to BaseLuceneIndexTemplate
	private LuceneIndexTemplate searchLuceneIndexTemplate;

	//Wired to DefaultLuceneDirectoryCreator
	private LuceneDirectoryCreator luceneDirectoryCreator;
	
	
	private Map<String,LuceneIndexTemplate> luceneIndexNameToTemplateMap = new HashMap<String,LuceneIndexTemplate>();
	
	private Map<String,LuceneIndexTemplate> multiCodingSchemeKeyToTemplateMap = new HashMap<String,LuceneIndexTemplate>();
	
	private Map<String,NamedDirectory> luceneIndexNameToDirctoryMap = new HashMap<String,NamedDirectory>();
	
	private Map<CodingSchemeUriVersionPair,String> luceneCodingSchemeToIndexNameMap = 
		new HashMap<CodingSchemeUriVersionPair,String>();
	
	private Map<String,Filter> codingSchemeFilterMap = new HashMap<String,Filter>();
	

	private String singleIndexName;
	

	
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LuceneIndexTemplate getCommonLuceneIndexTemplate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LuceneIndexTemplate getCommonLuceneIndexTemplate(
			List<AbsoluteCodingSchemeVersionReference> codingSchemes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LuceneIndexTemplate getSearchLuceneIndexTemplate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void destroyIndex(String indexName) {
		// TODO Auto-generated method stub

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

	@Override
	public void afterPropertiesSet() throws Exception {
		File indexDir = new File(systemVariables.getAutoLoadIndexLocation());
		for (File f : indexDir.listFiles()) {
			if (f.exists() && f.isDirectory()) {
				luceneIndexNameToTemplateMap.put(f.getName(),
						luceneIndexTemplate);
			}
		}
		
	}
	
	
	protected void autoRegisterIndex(String codingSchemeUri, String version) {
		String codingSchemeName;
		try {
			codingSchemeName = systemResourceService.getInternalCodingSchemeNameForUserCodingSchemeName(codingSchemeUri, version);
		} catch (LBParameterException e) {
			throw new RuntimeException(e);
		}
		
		LocalCodingScheme lcs = LocalCodingScheme.getLocalCodingScheme(codingSchemeName, version);
		try {
//TODO these seem to be query dependencies. We'll try to fix them later. The local map might be the place to deal with this but I think we
// should look at how lucene handles this first.
//			String indexName = metaData.getIndexMetaDataValue(lcs.getKey());
			
//			if(StringUtils.isBlank(indexName)) {
//				throw new RuntimeException(
//						"Cannot autoregister index for CodingScheme: " + codingSchemeUri + " Version: " + version + ".\n" +
//						"The Lucene index for this CodingScheme may have been dropped, or indexing may have failed. " +
//						"Reindexing may be needed.");
//			}

//			this.registerCodingSchemeIndex(codingSchemeUri, version, indexName);
			
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
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

}
