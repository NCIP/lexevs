/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.lexevs.dao.database.ibatis.batch;

/**
 * The Class IbatisBatchGroupInserterAdapter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class IbatisBatchGroupInserterAdapter implements IbatisBatchGroupInserter{
	
	/** The delegate. */
	private IbatisInserter delegate;
	
	/**
	 * Instantiates a new ibatis batch group inserter adapter.
	 * 
	 * @param delegate the delegate
	 */
	public IbatisBatchGroupInserterAdapter(IbatisInserter delegate){
		this.delegate = delegate;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.ibatis.batch.IbatisBatchGroupInserter#insert(java.lang.String, java.lang.Object, java.lang.String)
	 */
	@Override
	public void insert(String sql, Object parameter, String batchGroup) {
		delegate.insert(sql, parameter);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.ibatis.batch.IbatisBatchInserter#executeBatch()
	 */
	@Override
	public void executeBatch() {
		throw new UnsupportedOperationException("Batch inserts not allowed for IbatisBatchGroupInserterAdapter");
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.ibatis.batch.IbatisBatchInserter#startBatch()
	 */
	@Override
	public void startBatch() {
		throw new UnsupportedOperationException("Batch inserts not allowed for IbatisBatchGroupInserterAdapter");
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.ibatis.batch.IbatisInserter#insert(java.lang.String, java.lang.Object)
	 */
	@Override
	public void insert(String sql, Object parameter) {
		delegate.insert(sql, parameter);
	}
}
