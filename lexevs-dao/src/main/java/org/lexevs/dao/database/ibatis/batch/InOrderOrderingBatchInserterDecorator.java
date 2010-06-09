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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.lexevs.dao.database.inserter.BatchInserter;

/**
 * The Class OrderingBatchInserterDecorator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class InOrderOrderingBatchInserterDecorator implements BatchInserter {

	/** The delegate. */
	private BatchInserter delegate;
	
	private BatchOrderClassifier batchOrderClassifier;
	
	/** The statement map. */
	private Map<Integer,Map<String,List<Object>>> statementMap = new LinkedHashMap<Integer,Map<String,List<Object>>>();
	/**
	 * Instantiates a new ordering batch inserter decorator.
	 * 
	 * @param delegate the delegate
	 */
	public InOrderOrderingBatchInserterDecorator(BatchInserter delegate){
		this(delegate, new BatchOrderClassifier());
	}
	
	public InOrderOrderingBatchInserterDecorator(
			BatchInserter delegate,
			BatchOrderClassifier batchOrderClassifier){
		this.delegate = delegate;
		this.batchOrderClassifier = batchOrderClassifier;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.ibatis.batch.BatchInserter#executeBatch()
	 */
	@Override
	public void executeBatch() {
		
		for(Integer group : statementMap.keySet()) {
			for(String sql : statementMap.get(group).keySet()){
				for(Object insertObj : statementMap.get(group).get(sql)) {
					delegate.insert(sql, insertObj);
				}
			}
		}
		delegate.executeBatch();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.ibatis.batch.BatchInserter#startBatch()
	 */
	@Override
	public void startBatch() {
		delegate.startBatch();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.ibatis.batch.Inserter#insert(java.lang.String, java.lang.Object)
	 */
	@Override
	public void insert(String sql, Object parameter) {
		Integer groupNumber = this.batchOrderClassifier.classify(sql);
		if(! this.statementMap.containsKey(groupNumber)) {
			this.statementMap.put(groupNumber, new HashMap<String,List<Object>>() );
		}
		
		Map<String,List<Object>> groupMap = this.statementMap.get(groupNumber);
		
		if(!groupMap.containsKey(sql)) {
			groupMap.put(sql, new ArrayList<Object>() );
		}
		
		groupMap.get(sql).add(parameter);
	}
}
