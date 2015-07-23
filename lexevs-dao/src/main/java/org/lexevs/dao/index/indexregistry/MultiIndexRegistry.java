package org.lexevs.dao.index.indexregistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.apache.lucene.search.Filter;
import org.lexevs.dao.index.factory.IndexLocationFactory;
import org.lexevs.dao.index.indexregistry.SingleIndexRegistry.CodingSchemeUriVersionPair;
import org.lexevs.dao.index.lucenesupport.LuceneDirectoryCreator;
import org.lexevs.dao.index.lucenesupport.LuceneIndexTemplate;
import org.lexevs.dao.index.lucenesupport.LuceneDirectoryFactory.NamedDirectory;
import org.lexevs.dao.indexer.utility.MetaData;
import org.lexevs.system.constants.SystemVariables;
import org.lexevs.system.service.SystemResourceService;
import org.springframework.beans.factory.InitializingBean;

public class MultiIndexRegistry implements IndexRegistry, InitializingBean {

	
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
	
	public MultiIndexRegistry() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void registerCodingSchemeIndex(String codingSchemeUri,
			String version, String indexName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unRegisterCodingSchemeIndex(String codingSchemeUri,
			String version) {
		// TODO Auto-generated method stub

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
		return null;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
