package org.lexevs.dao.index.indexregistry;

import org.lexevs.dao.index.lucenesupport.LuceneIndexTemplate;

public interface IndexRegistry {

	public String registerCodingSchemeIndex(String codingSchemeUri, String version);

	public LuceneIndexTemplate getLuceneIndexTemplate(
			String codingSchemeUri, String version);

}