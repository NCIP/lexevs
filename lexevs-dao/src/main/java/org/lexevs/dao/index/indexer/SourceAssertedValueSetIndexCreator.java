package org.lexevs.dao.index.indexer;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.concepts.Entity;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;
import org.lexevs.dao.database.service.valuesets.AssertedValueSetService;
import org.lexevs.dao.indexer.utility.Utility;

public class SourceAssertedValueSetIndexCreator implements IndexCreator {

	private static final String ASSERTED_VALUE_SET_INDEX_NAME = "SOURCE_ASSERTED_VALUE_SETS";
	private int batchSize = 1000;
	private AssertedValueSetService valueSetService;

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
	
	    valueSetService.init(new AssertedValueSetParameters.
	    		Builder(reference.getCodingSchemeVersion()).
	    		baseValueSetURI(reference.getCodingSchemeURN()).build());
		String indexName;
		try {
			indexName = this.getIndexName(reference);
		} catch (LBParameterException e) {
			throw new RuntimeException("Problems getting coding scheme name. uri = " + 
					reference.getCodingSchemeURN()  + " version = " + reference.getCodingSchemeVersion(), e);
		}
		
		List<? extends Entity> entities = 
				valueSetService.getSourceAssertedValueSetEntitiesForEntityCode(null);
		return null;
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

}
