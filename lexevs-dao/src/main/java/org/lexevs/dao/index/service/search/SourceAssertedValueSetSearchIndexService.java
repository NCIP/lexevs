package org.lexevs.dao.index.service.search;

import java.util.List;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.annotations.LgAdminFunction;
import org.LexGrid.concepts.Entity;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.lexevs.dao.index.access.IndexDaoManager;
import org.lexevs.dao.index.indexer.AssertedValueSetEntityIndexer;
import org.lexevs.dao.index.indexer.EntityIndexer;
import org.lexevs.dao.index.indexer.IndexCreator;
import org.lexevs.dao.index.indexer.IndexCreator.IndexOption;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;
import org.lexevs.dao.index.lucene.v2013.search.ValueSetEntityDao;
import org.lexevs.dao.indexer.utility.ConcurrentMetaData;
import org.lexevs.dao.indexer.utility.Utility;

public class SourceAssertedValueSetSearchIndexService implements AssertedValueSetSearchIndexService {

	private IndexCreator indexCreator;
	private IndexDaoManager indexDaoManager;
	private ConcurrentMetaData concurrentMetaData;
	private EntityIndexer entityIndexer;


	@Override
	@LgAdminFunction
	public void updateIndexForEntity(String codingSchemeUri, String codingSchemeVersion, Entity entity) {
		throw new UnsupportedOperationException(
				"value set URI and Name are required for value set indexing update operation");
	}
	
	@Override
	@LgAdminFunction
	public void addEntityToIndex(String codingSchemeUri, String codingSchemeVersion, Entity entity) {
		throw new UnsupportedOperationException(
				"value set URI and Name are required for value set indexing add operation");
	}
	
	@Override
	@LgAdminFunction
	public void deleteEntityFromIndex(
			String codingSchemeUri,
			String codingSchemeVersion, 
			Entity entity) {
		throw new UnsupportedOperationException(
				"value set URI and Name are required for value set indexing delete operation");
	}
	
	@Override
	@LgAdminFunction
	public void updateIndexForEntity(String codingSchemeUri, String codingSchemeVersion, String vsURI, String vsName, Entity entity) {
		this.deleteEntityFromIndex(codingSchemeUri, codingSchemeVersion, vsURI, vsName, entity);
		this.addEntityToIndex(codingSchemeUri, codingSchemeVersion,vsURI, vsName, entity);
	}
	
	@Override
	@LgAdminFunction
	public void addEntityToIndex(String codingSchemeUri, String codingSchemeVersion,
			String vsURI, String vsName, Entity entity) {
		AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
		ref.setCodingSchemeURN(codingSchemeUri);
		ref.setCodingSchemeVersion(codingSchemeVersion);
		String codingSchemeName = null;
		try {
			codingSchemeName = Utility.getIndexName(ref);
		} catch (LBParameterException e) {
			 throw new RuntimeException("Index Name for value set index source could not be resolved");
		}
		List<Document> docs = 
				((AssertedValueSetEntityIndexer)entityIndexer).indexEntity(codingSchemeName, codingSchemeUri, 
						codingSchemeVersion, vsURI, vsName, entity);
			
			indexDaoManager.getValueSetEntityDao(codingSchemeUri, codingSchemeVersion).
				addDocuments(codingSchemeUri, codingSchemeVersion, docs, entityIndexer.getAnalyzer());
	}	

	@Override
	@LgAdminFunction
	public void deleteEntityFromIndex(
			String codingSchemeUri, String codingSchemeVersion, 
			String vsURI, String vsName, Entity entity) {

		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		Term uniqueIdTerm = new Term(
					LuceneLoaderCode.CODING_SCHEME_URI_VERSION_CODE_NAMESPACE_KEY_FIELD, 
					LuceneLoaderCode.
						createCodingSchemeUriVersionCodeNamespaceKey(
							codingSchemeUri, 
							codingSchemeVersion, 
							entity.getEntityCode(), 
							entity.getEntityCodeNamespace()));
		Term codingSchemeUriTerm = new Term(LuceneLoaderCode.CODING_SCHEME_ID_FIELD, vsURI);
		builder.add(new TermQuery(uniqueIdTerm), Occur.MUST);
		builder.add(new TermQuery(codingSchemeUriTerm), Occur.MUST);
		
		indexDaoManager.getValueSetEntityDao(codingSchemeUri, codingSchemeVersion).
		deleteDocuments(codingSchemeUri, codingSchemeVersion, builder.build());
	}

	@Override
	@LgAdminFunction
	public void dropIndex(AbsoluteCodingSchemeVersionReference reference) {
		String codingSchemeUri = reference.getCodingSchemeURN();
		String codingSchemeVersion = reference.getCodingSchemeVersion();
		
		Term term = new Term(
			LuceneLoaderCode.CODING_SCHEME_URI_VERSION_KEY_FIELD,
			LuceneLoaderCode.createCodingSchemeUriVersionKey(
					codingSchemeUri, codingSchemeVersion));
		
		indexDaoManager.getValueSetEntityDao(codingSchemeUri, codingSchemeVersion).
			deleteDocuments(
				codingSchemeUri, 
				codingSchemeVersion, 
				new TermQuery(term));
	}
	

	@LgAdminFunction
	public void dropIndexForAllValueSets(String codingSchemeUri, String codingSchemeVersion){
		
		indexDaoManager.getValueSetEntityDao(codingSchemeUri, codingSchemeVersion).
		deleteDocuments(
			codingSchemeUri, 
			codingSchemeVersion, 
			new MatchAllDocsQuery());
		
	}

	@Override
	public boolean doesIndexExist(AbsoluteCodingSchemeVersionReference reference) {
		ValueSetEntityDao vsDao = indexDaoManager
				.getValueSetEntityDao(reference.getCodingSchemeURN(), reference.getCodingSchemeVersion());
		if (null == vsDao
				.getLuceneIndexTemplate().getIndexName() || ((ValueSetEntityDao)vsDao).maxDocs() == 0) {

			return false;
		} else {
			return true;
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
	@LgAdminFunction
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
