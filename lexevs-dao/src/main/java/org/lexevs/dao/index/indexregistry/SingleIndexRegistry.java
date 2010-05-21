package org.lexevs.dao.index.indexregistry;

import org.lexevs.dao.index.lucenesupport.LuceneIndexTemplate;

public class SingleIndexRegistry implements IndexRegistry {
	
	private static String DEFAULT_SINGLE_INDEX_NAME = "commonIndex";
	
	public String indexName = DEFAULT_SINGLE_INDEX_NAME;
	public LuceneIndexTemplate luceneIndexTemplate;

	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.indexregistry.IndexRegistry#getIndexName(java.lang.String, java.lang.String)
	 */
	public String registerCodingSchemeIndex(String codingSchemeUri, String version) {
		return indexName;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.indexregistry.IndexRegistry#getLuceneIndexTemplate(java.lang.String, java.lang.String)
	 */
	public LuceneIndexTemplate getLuceneIndexTemplate(String codingSchemeUri, String version) {
		return luceneIndexTemplate;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public LuceneIndexTemplate getLuceneIndexTemplate() {
		return luceneIndexTemplate;
	}

	public void setLuceneIndexTemplate(LuceneIndexTemplate luceneIndexTemplate) {
		this.luceneIndexTemplate = luceneIndexTemplate;
	}
}
