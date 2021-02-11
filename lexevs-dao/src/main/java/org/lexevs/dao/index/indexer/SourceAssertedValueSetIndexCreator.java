
package org.lexevs.dao.index.indexer;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetServices;
import org.apache.lucene.document.Document;
import org.lexevs.dao.database.service.valuesets.AssertedValueSetService;
import org.lexevs.dao.index.access.IndexDaoManager;
import org.lexevs.dao.index.access.search.SearchDao;
import org.lexevs.dao.indexer.utility.Utility;
import org.lexevs.logging.LoggerFactory;

public class SourceAssertedValueSetIndexCreator implements IndexCreator {

	private static final String ASSERTED_VALUE_SET_INDEX_NAME = "SOURCE_ASSERTED_VALUE_SETS";
	private int batchSize = 1000;
	private AssertedValueSetService valueSetService;
	private AssertedValueSetEntityIndexer entityIndexer;
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
	
	@Override
	public String index(AbsoluteCodingSchemeVersionReference reference, EntityIndexerProgressCallback callback,
			boolean onlyRegister, IndexOption option) {
		LgLoggerIF logger = LoggerFactory.getLogger();
		valueSetService.init(new AssertedValueSetParameters.Builder(reference.getCodingSchemeVersion())
				.codingSchemeURI(reference.getCodingSchemeURN()).build());

		SearchDao entityIndexService = indexDaoManager.getValueSetEntityDao(reference.getCodingSchemeURN(),
				reference.getCodingSchemeVersion());

		String indexName;
		try {
			indexName = Utility.getSourceCodingSchemeName(reference);
		} catch (LBParameterException e) {
			throw new RuntimeException("Problems getting coding scheme name. uri = " + reference.getCodingSchemeURN()
					+ " version = " + reference.getCodingSchemeVersion(), e);
		}
		logger.info("Processing entities");
		System.out.println("Processing entities");
		List<String> topNodes = valueSetService.getAllValidValueSetTopNodeCodes();
		List<CodingScheme> valueSets = null;
		List<Document> documents = new ArrayList<Document>();
		for(String s: topNodes) {
		try {
			valueSets = valueSetService.getSourceAssertedValueSetforTopNodeEntityCode(s);
		} catch (LBException e) {
			throw new RuntimeException("Problem getting value sets from top node: " + s
					+ "  " + e);
		}
		
			for (CodingScheme cs : valueSets) {
				Entities entities = cs.getEntities();
				logger.info("Indexing " + entities.getEntityCount() + " entities");
				System.out.println("Indexing " + entities.getEntityCount() + " entities");
				for (Entity entity : entities.getEntityAsReference()) {
					entity = addPropertiesToEntity(entity);
					documents.addAll(entityIndexer.indexEntity(indexName, reference.getCodingSchemeURN(),
							reference.getCodingSchemeVersion(), cs.getCodingSchemeURI(), cs.getCodingSchemeName(),
							entity));
				}
			}
		}

		entityIndexService.addDocuments(indexName, reference.getCodingSchemeVersion(), documents,
				entityIndexer.getAnalyzer());
		return indexName;
	}

	private Entity addPropertiesToEntity(Entity entity) {
		entity.addAnyProperties(valueSetService.getEntityProperties(entity.getEntityCode()));
		return entity;
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