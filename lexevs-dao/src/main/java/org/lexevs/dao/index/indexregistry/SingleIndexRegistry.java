/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
<<<<<<< .mine
 */
package org.lexevs.dao.index.indexregistry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.Filter;
import org.apache.lucene.store.FSDirectory;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.dao.index.factory.IndexLocationFactory;
import org.lexevs.dao.index.lucenesupport.BaseLuceneIndexTemplate;
import org.lexevs.dao.index.lucenesupport.LuceneDirectoryCreator;
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
		
	public String singleIndexName = IndexLocationFactory.DEFAULT_SINGLE_INDEX_NAME;
	
	private LuceneIndexTemplate luceneIndexTemplate;
	
	private LuceneIndexTemplate searchLuceneIndexTemplate;

	private SystemVariables systemVariables;
	
	private SystemResourceService systemResourceService;
	
	private MetaData metaData;
	
	private Map<String,LuceneIndexTemplate> luceneIndexNameToTemplateMap = new HashMap<String,LuceneIndexTemplate>();
	
	private Map<String,LuceneIndexTemplate> multiCodingSchemeKeyToTemplateMap = new HashMap<String,LuceneIndexTemplate>();
	
	private Map<String,NamedDirectory> luceneIndexNameToDirctoryMap = new HashMap<String,NamedDirectory>();
	
	private Map<CodingSchemeUriVersionPair,String> luceneCodingSchemeToIndexNameMap = 
		new HashMap<CodingSchemeUriVersionPair,String>();
	
	private Map<String,Filter> codingSchemeFilterMap = new HashMap<String,Filter>();
	
	private Map<String,Filter> boundaryDocFilterMap = new HashMap<String,Filter>();
	
	private LuceneDirectoryCreator luceneDirectoryCreator;
	
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
	public void unRegisterCodingSchemeIndex(String codingSchemeUri, String version) {
		CodingSchemeUriVersionPair key = new CodingSchemeUriVersionPair(codingSchemeUri,version);
		String indexName = 
			this.luceneCodingSchemeToIndexNameMap.get(key);
		
		if(!StringUtils.isBlank(indexName)) {

			if(! indexName.equals(singleIndexName)) {
				luceneIndexNameToTemplateMap.remove(indexName);
			}

			luceneCodingSchemeToIndexNameMap.remove(key);

			String mapKey = DaoUtility.createKey(codingSchemeUri, version);
			
			this.boundaryDocFilterMap.remove(mapKey);
			this.codingSchemeFilterMap.remove(mapKey);
		}
	}
	
	@Override
	public void destroyIndex(String indexName) {
		Set<Entry<CodingSchemeUriVersionPair, String>> entrySet = new HashSet<Entry<CodingSchemeUriVersionPair, String>>(
				this.luceneCodingSchemeToIndexNameMap.entrySet());
		
		for(Entry<CodingSchemeUriVersionPair, String> entry : entrySet) {
			if(entry.getValue().equals(indexName)) {
				NamedDirectory namedDirectory = this.luceneIndexNameToDirctoryMap.get(entry.getValue());
				namedDirectory.remove();
				
				CodingSchemeUriVersionPair key = entry.getKey();
				this.unRegisterCodingSchemeIndex(
						key.uri, 
						key.version);
			}
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

					if(! this.luceneIndexNameToDirctoryMap.containsKey(indexName)) {
						NamedDirectory dir = this.createIndexDirectory(indexName);
						this.luceneIndexNameToDirctoryMap.put(indexName, dir);
					}
					
					directories.add(
							this.luceneIndexNameToDirctoryMap.get(indexName));
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
	
	@Override
	public String getCommonIndexName() {
		return this.getSingleIndexName();
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

	public Map<String, Filter> getCodingSchemeFilterMap() {
		return codingSchemeFilterMap;
	}

	public void setCodingSchemeFilterMap(Map<String, Filter> codingSchemeFilterMap) {
		this.codingSchemeFilterMap = codingSchemeFilterMap;
	}

	public Map<String, Filter> getBoundaryDocFilterMap() {
		return boundaryDocFilterMap;
	}

	public void setBoundaryDocFilterMap(Map<String, Filter> boundaryDocFilterMap) {
		this.boundaryDocFilterMap = boundaryDocFilterMap;
	}

	public void setLuceneDirectoryCreator(LuceneDirectoryCreator luceneDirectoryCreator) {
		this.luceneDirectoryCreator = luceneDirectoryCreator;
	}

	public LuceneDirectoryCreator getLuceneDirectoryCreator() {
		return luceneDirectoryCreator;
	}

	public LuceneIndexTemplate getSearchLuceneIndexTemplate() {
		return searchLuceneIndexTemplate;
	}

	public void setSearchLuceneIndexTemplate(LuceneIndexTemplate searchLuceneIndexTemplate) {
		this.searchLuceneIndexTemplate = searchLuceneIndexTemplate;
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