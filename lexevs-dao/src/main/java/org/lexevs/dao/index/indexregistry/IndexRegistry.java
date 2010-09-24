package org.lexevs.dao.index.indexregistry;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.lexevs.dao.index.lucenesupport.LuceneIndexTemplate;

public interface IndexRegistry {

	public void registerCodingSchemeIndex(String codingSchemeUri, String version, String indexName);
	
	public void unRegisterCodingSchemeIndex(String codingSchemeUri, String version);

	public LuceneIndexTemplate getLuceneIndexTemplate(
			String codingSchemeUri, String version);
	
	public LuceneIndexTemplate getCommonLuceneIndexTemplate();
	
	public LuceneIndexTemplate getCommonLuceneIndexTemplate(List<AbsoluteCodingSchemeVersionReference> codingSchemes);
}