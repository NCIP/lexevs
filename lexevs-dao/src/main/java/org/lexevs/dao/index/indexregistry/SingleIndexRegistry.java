package org.lexevs.dao.index.indexregistry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.dao.index.lucenesupport.BaseLuceneIndexTemplate;
import org.lexevs.dao.index.lucenesupport.LuceneDirectoryFactory;
import org.lexevs.dao.index.lucenesupport.LuceneIndexTemplate;
import org.lexevs.dao.index.lucenesupport.MultiBaseLuceneIndexTemplate;
import org.lexevs.dao.index.lucenesupport.LuceneDirectoryFactory.NamedDirectory;
import org.lexevs.system.constants.SystemVariables;
import org.lexevs.system.model.LocalCodingScheme;
import org.lexevs.system.service.SystemResourceService;
import org.springframework.beans.factory.InitializingBean;

import edu.mayo.informatics.indexer.api.exceptions.InternalErrorException;
import edu.mayo.informatics.indexer.utility.MetaData;

public class SingleIndexRegistry implements IndexRegistry, InitializingBean {
	
	public static String DEFAULT_SINGLE_INDEX_NAME = "commonIndex";
	
	public String singleIndexName = DEFAULT_SINGLE_INDEX_NAME;
	
	private LuceneIndexTemplate luceneIndexTemplate;
	
	private SystemVariables systemVariables;
	
	private SystemResourceService systemResourceService;
	
	private MetaData metaData;
	
	private Map<String,LuceneIndexTemplate> luceneIndexNameToTemplateMap = new HashMap<String,LuceneIndexTemplate>();
	
	private Map<String,LuceneIndexTemplate> multiCodingSchemeKeyToTemplateMap = new HashMap<String,LuceneIndexTemplate>();
	
	private Map<String,Directory> luceneIndexNameToDirctoryMap = new HashMap<String,Directory>();
	
	private Map<CodingSchemeUriVersionPair,String> luceneCodingSchemeToIndexNameMap = 
		new HashMap<CodingSchemeUriVersionPair,String>();
	
	@Override
	public void afterPropertiesSet() throws Exception {
		luceneIndexNameToTemplateMap.put(singleIndexName, luceneIndexTemplate);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.indexregistry.IndexRegistry#getIndexName(java.lang.String, java.lang.String)
	 */
	public void registerCodingSchemeIndex(String codingSchemeUri, String version, String indexName) {
		if(!indexName.equals(singleIndexName)) {
			LuceneIndexTemplate template = this.createLuceneIndexTemplate(indexName);
			
			luceneIndexNameToTemplateMap.put(indexName, template);
		}
		
		luceneCodingSchemeToIndexNameMap.put(new CodingSchemeUriVersionPair(codingSchemeUri,version), indexName);
	}
	
	protected Directory createIndexDirectory(String indexName) {
		String baseIndexPath = systemVariables.getAutoLoadIndexLocation();
		try {
			FSDirectory directory =
				FSDirectory.getDirectory(baseIndexPath + File.separator + indexName);
			
			LuceneDirectoryFactory.initIndexDirectory(directory, directory.getFile());
			
			luceneIndexNameToDirctoryMap.put(indexName, directory);
			
			return directory;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected LuceneIndexTemplate createLuceneIndexTemplate(String indexName) {
		Directory directory = this.createIndexDirectory(indexName);
		
		return new BaseLuceneIndexTemplate(new NamedDirectory(directory, indexName));
	}
	
	@Override
	public void unRegisterCodingSchemeIndex(String codingSchemeUri, String version) {
		CodingSchemeUriVersionPair key = new CodingSchemeUriVersionPair(codingSchemeUri,version);
		String indexName = 
			this.luceneCodingSchemeToIndexNameMap.get(key);
		
		if(!StringUtils.isBlank(indexName)) {

			if(! indexName.equals(singleIndexName)) {
				luceneIndexNameToTemplateMap.remove(indexName);
			}

			luceneCodingSchemeToIndexNameMap.remove(key);

		}
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.indexregistry.IndexRegistry#getLuceneIndexTemplate(java.lang.String, java.lang.String)
	 */
	public LuceneIndexTemplate getLuceneIndexTemplate(String codingSchemeUri, String version) {
		CodingSchemeUriVersionPair key = new CodingSchemeUriVersionPair(codingSchemeUri,version);
		if(! this.luceneCodingSchemeToIndexNameMap.containsKey(key)) {
			this.autoRegisterIndex(codingSchemeUri, version);
		}
		String indexName = luceneCodingSchemeToIndexNameMap.get(key);
		
		return this.luceneIndexNameToTemplateMap.get(indexName);
	}
	
	public LuceneIndexTemplate getCommonLuceneIndexTemplate() {
		return luceneIndexTemplate;
	}
	
	@Override
	public LuceneIndexTemplate getCommonLuceneIndexTemplate(
			List<AbsoluteCodingSchemeVersionReference> codingSchemes) {
		if(this.systemVariables.getIsSingleIndex()) {
			return this.luceneIndexTemplate;
		} else {

			String key = DaoUtility.createKey(codingSchemes);

			if(! this.multiCodingSchemeKeyToTemplateMap.containsKey(key)) {

				List<NamedDirectory> directories = new ArrayList<NamedDirectory>();

				for(AbsoluteCodingSchemeVersionReference ref : codingSchemes) {
					String uri = ref.getCodingSchemeURN();
					String version = ref.getCodingSchemeVersion();

					String indexName = this.getCodingSchemeIndexName(uri, version);

					directories.add(
							new NamedDirectory(
									this.luceneIndexNameToDirctoryMap.get(indexName),
									indexName));
				}

				this.multiCodingSchemeKeyToTemplateMap.put(key, new MultiBaseLuceneIndexTemplate(directories));
			}

			return this.multiCodingSchemeKeyToTemplateMap.get(key);
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
			String indexName = metaData.getIndexMetaDataValue(lcs.getKey());
			
			if(StringUtils.isBlank(indexName)) {
				throw new RuntimeException(
						"Cannot autoregister index for CodingScheme: " + codingSchemeUri + " Version: " + version + ".\n" +
						"The Lucene index for this CodingScheme may have been dropped, or indexing may have failed. " +
						"Reindexing may be needed.");
			}

			this.registerCodingSchemeIndex(codingSchemeUri, version, indexName);
			
		} catch (InternalErrorException e) {
			throw new RuntimeException(e);
		}
	}

	public MetaData getMetaData() {
		return metaData;
	}

	public void setMetaData(MetaData metaData) {
		this.metaData = metaData;
	}

	public String getCodingSchemeIndexName(String codingSchemeUri, String version) {
		return this.getLuceneIndexTemplate(codingSchemeUri, version).getIndexName();
	}

	public String getSingleIndexName() {
		return singleIndexName;
	}

	public void setSingleIndexName(String singleIndexName) {
		this.singleIndexName = singleIndexName;
	}

	public LuceneIndexTemplate getLuceneIndexTemplate() {
		return luceneIndexTemplate;
	}

	public void setLuceneIndexTemplate(LuceneIndexTemplate luceneIndexTemplate) {
		this.luceneIndexTemplate = luceneIndexTemplate;
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
