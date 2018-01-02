package org.lexevs.dao.index.indexer;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.concepts.Entity;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.dao.database.service.valuesets.AssertedValueSetService;
import org.lexevs.dao.index.access.IndexDaoManager;
import org.lexevs.dao.index.access.entity.EntityDao;
import org.lexevs.dao.indexer.utility.Utility;
import org.apache.lucene.document.Document;

public class SourceAssertedValueSetIndexCreator implements IndexCreator {

	private static final String ASSERTED_VALUE_SET_INDEX_NAME = "SOURCE_ASSERTED_VALUE_SETS";
	private static final int BATCH_SIZE = 1000;
	private int batchSize =1000;
	private AssertedValueSetService valueSetService;
	private AssertedValueSetEntityIndexer entityIndexer;
	private EntityService entityService;
	private IndexDaoManager indexDaoManager;

	@Override
	public String index(AbsoluteCodingSchemeVersionReference reference) {
		return this.index(reference, null, IndexOption.BOTH);
	}

	@Override
	public String index(AbsoluteCodingSchemeVersionReference reference, 
			EntityIndexerProgressCallback callback, boolean onlyRegister) {
		return this.index(reference, callback, onlyRegister, IndexOption.BOTH);
	}

	@Override
	public String index(AbsoluteCodingSchemeVersionReference reference, EntityIndexerProgressCallback callback) {	
		return this.index(reference, callback, IndexOption.BOTH);
	}

	@Override
	public String index(AbsoluteCodingSchemeVersionReference reference, IndexOption option) {
		return this.index(reference, null, option);
	}

	@Override
	public String index(AbsoluteCodingSchemeVersionReference reference,
			EntityIndexerProgressCallback callback, IndexOption option) {
		return this.index(reference, callback, false, option);
	}

//	@Override
//	public String index(AbsoluteCodingSchemeVersionReference reference, EntityIndexerProgressCallback callback,
//			boolean onlyRegister, IndexOption option) {
//	
//	    valueSetService.init(new AssertedValueSetParameters.
//	    		Builder(reference.getCodingSchemeVersion()).
//	    		baseValueSetURI(reference.getCodingSchemeURN()).build());
//	    EntityDao entityIndexService = indexDaoManager.getValueSetEntityDao(reference.getCodingSchemeURN(), reference.getCodingSchemeVersion());
//	    
//		String indexName;
//		try {
//			indexName = this.getIndexName(reference);
//		} catch (LBParameterException e) {
//			throw new RuntimeException("Problems getting coding scheme name. uri = " + 
//					reference.getCodingSchemeURN()  + " version = " + reference.getCodingSchemeVersion(), e);
//		}
//		List<? extends Entity> entities = 
//				valueSetService.getSourceAssertedValueSetEntitiesForEntityCode(null);
//		List<Document> documents = new ArrayList<Document>();
//		for(Entity entity : entities ) {documents.addAll(entityIndexer.indexEntity(indexName,reference.getCodingSchemeURN(), reference.getCodingSchemeVersion(), entity));}
//		
//		entityIndexService.addDocuments(
//				indexName, 
//				reference.getCodingSchemeVersion(), 
//				documents, entityIndexer.getAnalyzer());
//		return indexName;
//	}
	
	@Override
	public String index(AbsoluteCodingSchemeVersionReference reference, EntityIndexerProgressCallback callback,
			boolean onlyRegister, IndexOption option) {
	
	    valueSetService.init(new AssertedValueSetParameters.
	    		Builder(reference.getCodingSchemeVersion()).
	    		baseValueSetURI(reference.getCodingSchemeURN()).build());
	    
	    EntityDao entityIndexService = indexDaoManager.getValueSetEntityDao(reference.getCodingSchemeURN(), reference.getCodingSchemeVersion());
	    
		String indexName;
		try {
			indexName = this.getIndexName(reference);
		} catch (LBParameterException e) {
			throw new RuntimeException("Problems getting coding scheme name. uri = " + 
					reference.getCodingSchemeURN()  + " version = " + reference.getCodingSchemeVersion(), e);
		}
		int position = 0;
		List<Entity> entities = new ArrayList<Entity>();
		for(List<String> entityUids = 
				valueSetService.getSourceAssertedValueSetEntityUidsforPredicateUid( position, batchSize);
				entityUids.size() > 0; 
				entityUids = valueSetService.getSourceAssertedValueSetEntityUidsforPredicateUid(position += batchSize, batchSize)){
		entities.addAll(valueSetService.getEntitiesForUidMap(entityUids));
				}
		List<Document> documents = new ArrayList<Document>();
		for(Entity entity : entities ) {documents.addAll(entityIndexer.indexEntity(indexName,reference.getCodingSchemeURN(), reference.getCodingSchemeVersion(), entity));}
		
		entityIndexService.addDocuments(
				indexName, 
				reference.getCodingSchemeVersion(), 
				documents, entityIndexer.getAnalyzer());
		return indexName;
	}

	private String getIndexName(AbsoluteCodingSchemeVersionReference reference) throws LBParameterException {
		return ASSERTED_VALUE_SET_INDEX_NAME + "_" + Utility.getIndexName(reference);
	}

	public int getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}
	
	public AssertedValueSetService getValueSetService() {
		return valueSetService;
	}

	public void setValueSetService(AssertedValueSetService valueSetService) {
		this.valueSetService = valueSetService;
	}

	public AssertedValueSetEntityIndexer getEntityIndexer() {
		return entityIndexer;
	}

	public void setEntityIndexer(AssertedValueSetEntityIndexer entityIndexer) {
		this.entityIndexer = entityIndexer;
	}

	public IndexDaoManager getIndexDaoManager() {
		return indexDaoManager;
	}

	public void setIndexDaoManager(IndexDaoManager indexDaoManager) {
		this.indexDaoManager = indexDaoManager;
	}

	public static void main(String...args) {
		
	}

}
