package org.lexevs.dao.index.service.entity;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.concepts.Entity;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.lexevs.dao.index.access.IndexDaoManager;
import org.lexevs.dao.index.indexer.IndexCreator;

public class LuceneEntityIndexService implements EntityIndexService {
	
	private IndexDaoManager indexDaoManager;
	
	private IndexCreator indexCreator;

	public void createIndex(AbsoluteCodingSchemeVersionReference reference) {
		indexCreator.index(reference);
	}

	public void removeIndexForEntity(String codingSchemeUri,
			String codingSchemeVersion, Entity entity) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	public void updateIndexForEntity(String codingSchemeUri,
			String codingSchemeVersion, Entity entity) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}
	
	public List<ScoreDoc> query(AbsoluteCodingSchemeVersionReference reference, List<? extends Query> combinedQueries, List<? extends Query> individualQueries){
		return indexDaoManager.getEntityDao(reference.getCodingSchemeURN(), reference.getCodingSchemeVersion()).
			query(reference, combinedQueries, individualQueries);
	}

	public void setIndexDaoManager(IndexDaoManager indexDaoManager) {
		this.indexDaoManager = indexDaoManager;
	}

	public IndexDaoManager getIndexDaoManager() {
		return indexDaoManager;
	}

	public Document getDocumentById(
			AbsoluteCodingSchemeVersionReference reference, int documentId) {
		return indexDaoManager.getEntityDao(reference.getCodingSchemeURN(), reference.getCodingSchemeVersion()).getDocumentById(reference, documentId);
	}

	public void dropIndex(AbsoluteCodingSchemeVersionReference reference) {
		indexDaoManager.getEntityDao(
				reference.getCodingSchemeURN(), 
				reference.getCodingSchemeVersion()).deleteDocumentsOfCodingScheme(reference);
	}

	public IndexCreator getIndexCreator() {
		return indexCreator;
	}

	public void setIndexCreator(IndexCreator indexCreator) {
		this.indexCreator = indexCreator;
	}
}
