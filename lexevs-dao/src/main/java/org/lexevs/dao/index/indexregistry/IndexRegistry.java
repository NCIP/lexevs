
package org.lexevs.dao.index.indexregistry;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.apache.lucene.search.Filter;
import org.lexevs.dao.index.lucenesupport.LuceneIndexTemplate;

import java.util.List;
import java.util.Map;

public interface IndexRegistry {

	public void registerCodingSchemeIndex(String codingSchemeUri, String version, String indexName);
	
	public void unRegisterCodingSchemeIndex(String codingSchemeUri, String version);

	public LuceneIndexTemplate getLuceneIndexTemplate(
			String codingSchemeUri, String version);
	
	public LuceneIndexTemplate getCommonLuceneIndexTemplate();
	
	public LuceneIndexTemplate getCommonLuceneIndexTemplate(List<AbsoluteCodingSchemeVersionReference> codingSchemes);
	
	public LuceneIndexTemplate getSearchLuceneIndexTemplate();
	
	public void destroyIndex(String indexName);
	
	public Map<String, Filter> getCodingSchemeFilterMap();

	public Map<String, Filter> getBoundaryDocFilterMap();
}