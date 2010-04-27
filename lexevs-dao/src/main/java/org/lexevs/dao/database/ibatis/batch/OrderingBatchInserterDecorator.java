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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class OrderingBatchInserterDecorator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class OrderingBatchInserterDecorator implements IbatisBatchInserter {

	/** The delegate. */
	private IbatisBatchInserter delegate;
	
	/** The statement map. */
	private Map<String,List<Object>> statementMap = new HashMap<String,List<Object>>();
	
	/**
	 * Instantiates a new ordering batch inserter decorator.
	 * 
	 * @param delegate the delegate
	 */
	public OrderingBatchInserterDecorator(IbatisBatchInserter delegate){
		this.delegate = delegate;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.ibatis.batch.IbatisBatchInserter#executeBatch()
	 */
	@Override
	public void executeBatch() {
		for(String sql : statementMap.keySet()) {
			for(Object argument : statementMap.get(sql)){
				delegate.insert(sql, argument);
			}
		}
		delegate.executeBatch();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.ibatis.batch.IbatisBatchInserter#startBatch()
	 */
	@Override
	public void startBatch() {
		delegate.startBatch();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.ibatis.batch.IbatisInserter#insert(java.lang.String, java.lang.Object)
	 */
	@Override
	public void insert(String sql, Object parameter) {
		if(!this.statementMap.containsKey(sql)) {
			this.statementMap.put(sql, new ArrayList<Object>());
		}
		
		this.statementMap.get(sql).add(parameter);
	}
}
