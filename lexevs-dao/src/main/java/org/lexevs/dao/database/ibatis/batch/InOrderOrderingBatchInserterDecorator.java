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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class OrderingBatchInserterDecorator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class InOrderOrderingBatchInserterDecorator implements IbatisBatchInserter {

	/** The delegate. */
	private IbatisBatchInserter delegate;
	
	private BatchOrderClassifier batchOrderClassifier;
	
	/** The statement map. */
	private Map<Integer,List<SqlParameterPair>> statementMap = new LinkedHashMap<Integer,List<SqlParameterPair>>();
	/**
	 * Instantiates a new ordering batch inserter decorator.
	 * 
	 * @param delegate the delegate
	 */
	public InOrderOrderingBatchInserterDecorator(IbatisBatchInserter delegate){
		this(delegate, new BatchOrderClassifier());
	}
	
	public InOrderOrderingBatchInserterDecorator(
			IbatisBatchInserter delegate,
			BatchOrderClassifier batchOrderClassifier){
		this.delegate = delegate;
		this.batchOrderClassifier = batchOrderClassifier;
		
		for(Integer groupNumber : this.batchOrderClassifier.getOrderedGroups()) {
			statementMap.put(groupNumber, new ArrayList<SqlParameterPair>());
		}
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.ibatis.batch.IbatisBatchInserter#executeBatch()
	 */
	@Override
	public void executeBatch() {
		
		for(Integer group : statementMap.keySet()) {
			for(SqlParameterPair pair : statementMap.get(group)){
				delegate.insert(pair.getSql(), pair.getParameter());
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
		Integer groupNumber = this.batchOrderClassifier.classify(sql);
		this.statementMap.get(groupNumber).add(
				new SqlParameterPair(sql,parameter));
	}

	private class SqlParameterPair {
		private String sql;
		private Object parameter;
		
		public SqlParameterPair(String sql, Object parameter) {
			super();
			this.sql = sql;
			this.parameter = parameter;
		}
		public String getSql() {
			return sql;
		}
		public void setSql(String sql) {
			this.sql = sql;
		}
		public Object getParameter() {
			return parameter;
		}
		public void setParameter(Object parameter) {
			this.parameter = parameter;
		}
		
		
	}

	

}
