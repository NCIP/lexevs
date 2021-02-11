
package org.lexevs.dao.index.indexer;

import java.util.List;

import org.LexGrid.concepts.Entity;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.lexevs.dao.index.version.LexEvsIndexFormatVersion;

public interface EntityIndexer {
	
	public List<Document> indexEntity(
			String codingSchemeUri, 
			String codingSchemeVersion,
			Entity entity);
	
	public Analyzer getAnalyzer();
	
	public LexEvsIndexFormatVersion getIndexerFormatVersion();
	
}