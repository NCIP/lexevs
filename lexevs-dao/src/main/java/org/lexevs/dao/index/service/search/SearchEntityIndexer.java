package org.lexevs.dao.index.service.search;

import java.util.Arrays;
import java.util.List;

import org.LexGrid.concepts.Entity;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.lexevs.dao.index.indexer.EntityIndexer;
import org.lexevs.dao.index.version.LexEvsIndexFormatVersion;

public class SearchEntityIndexer implements EntityIndexer {
	
	/** The current index version. */
	private String currentIndexVersion = "2013";

	@Override
	public List<Document> indexEntity(
			String codingSchemeUri,
			String codingSchemeVersion, 
			Entity entity) {
		Document document = new Document();
		document.add(new Field("description", 
				entity.getEntityDescription().getContent(),
				Field.Store.YES, 
				Field.Index.ANALYZED));
		
		return Arrays.asList(document);
	}

	@Override
	public Analyzer getAnalyzer() {
		return new SimpleAnalyzer();
	}

	@Override
	public LexEvsIndexFormatVersion getIndexerFormatVersion() {
		return LexEvsIndexFormatVersion.parseStringToVersion(this.currentIndexVersion);
	}

}
