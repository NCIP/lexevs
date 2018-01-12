package org.lexevs.dao.index.service.search;

import java.util.List;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.concepts.Entity;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.lexevs.dao.index.access.IndexDaoManager;
import org.lexevs.dao.index.indexer.EntityIndexer;
import org.lexevs.dao.index.indexer.IndexCreator;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;
import org.lexevs.dao.index.indexer.IndexCreator.IndexOption;
import org.lexevs.dao.indexer.utility.ConcurrentMetaData;

public class SourceAssertedValueSetSearchIndexService implements SearchIndexService {

	private IndexCreator indexCreator;
	private IndexDaoManager indexDaoManager;
	private ConcurrentMetaData concurrentMetaData;
	private EntityIndexer entityIndexer;

	@Override
	public void updateIndexForEntity(String codingSchemeUri, String codingSchemeVersion, Entity entity) {
		this.deleteEntityFromIndex(codingSchemeUri, codingSchemeVersion, entity);
		this.addEntityToIndex(codingSchemeUri, codingSchemeVersion, entity);
	}

	@Override
	public void addEntityToIndex(String codingSchemeUri, String codingSchemeVersion, Entity entity) {
		List<Document> docs = 
				entityIndexer.indexEntity(codingSchemeUri, codingSchemeVersion, entity);
			
			indexDaoManager.getSearchDao().
				addDocuments(codingSchemeUri, codingSchemeVersion, docs, entityIndexer.getAnalyzer());
	}

	@Override
	public void deleteEntityFromIndex(String codingSchemeUri, String codingSchemeVersion, Entity entity) {

		Term term = new Term(
					LuceneLoaderCode.CODING_SCHEME_URI_VERSION_CODE_NAMESPACE_KEY_FIELD, 
					LuceneLoaderCode.
						createCodingSchemeUriVersionCodeNamespaceKey(
							codingSchemeUri, 
							codingSchemeVersion, 
							entity.getEntityCode(), 
							entity.getEntityCodeNamespace()));
		
		indexDaoManager.getValueSetEntityDao(codingSchemeUri, codingSchemeVersion).
		deleteDocuments(codingSchemeUri, codingSchemeVersion, new TermQuery(term));
	}

	@Override
	public void dropIndex(AbsoluteCodingSchemeVersionReference reference) {
		String codingSchemeUri = reference.getCodingSchemeURN();
		String codingSchemeVersion = reference.getCodingSchemeVersion();
		
		Term term = new Term(
			LuceneLoaderCode.CODING_SCHEME_URI_VERSION_KEY_FIELD,
			LuceneLoaderCode.createCodingSchemeUriVersionKey(
					codingSchemeUri, codingSchemeVersion));
		
		indexDaoManager.getSearchDao().
			deleteDocuments(
				codingSchemeUri, 
				codingSchemeVersion, 
				new TermQuery(term));
	}

	@Override
	public boolean doesIndexExist(AbsoluteCodingSchemeVersionReference reference) {
		if (null != indexDaoManager
				.getValueSetEntityDao(reference.getCodingSchemeURN(), reference.getCodingSchemeVersion())
				.getLuceneIndexTemplate().getIndexName()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<ScoreDoc> query(Set<AbsoluteCodingSchemeVersionReference> codeSystemToInclude, Query query) {
		return this.indexDaoManager.getValueSetEntityDao(null, null).query(query);
	}

	@Override
	public Document getById(int id) {
		return this.indexDaoManager.getValueSetEntityDao(null, null).getById(id);
	}

	@Override
	public Analyzer getAnalyzer() {
		return this.entityIndexer.getAnalyzer();
	}

	@Override
	public void createIndex(AbsoluteCodingSchemeVersionReference ref) {
		if(ref == null) {throw new RuntimeException("CodingScheme Reference cannot be null");}
		indexCreator.index(ref, IndexOption.SEARCH);
	}

	@Override
	public Document getById(Set<AbsoluteCodingSchemeVersionReference> codeSystemsToInclude, int doc) {
		System.out.println("Getting a document id using a coding scheme reference is not implemented for asserted value sets");
		return null;
	}

	public IndexCreator getIndexCreator() {
		return indexCreator;
	}

	public void setIndexCreator(IndexCreator indexCreator) {
		this.indexCreator = indexCreator;
	}

	public IndexDaoManager getIndexDaoManager() {
		return indexDaoManager;
	}

	public void setIndexDaoManager(IndexDaoManager indexDaoManager) {
		this.indexDaoManager = indexDaoManager;
	}

	public ConcurrentMetaData getConcurrentMetaData() {
		return concurrentMetaData;
	}

	public void setConcurrentMetaData(ConcurrentMetaData concurrentMetaData) {
		this.concurrentMetaData = concurrentMetaData;
	}

	public EntityIndexer getEntityIndexer() {
		return entityIndexer;
	}

	public void setEntityIndexer(EntityIndexer entityIndexer) {
		this.entityIndexer = entityIndexer;
	}

}
