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
public class InOrderOrderingBatchInserterDecorator implements IbatisBatchGroupInserter {

	/** The delegate. */
	private IbatisBatchInserter delegate;
	
	/** The statement map. */
	private Map<BatchGroupKey,List<Object>> statementMap = new LinkedHashMap<BatchGroupKey,List<Object>>();
	/**
	 * Instantiates a new ordering batch inserter decorator.
	 * 
	 * @param delegate the delegate
	 */
	public InOrderOrderingBatchInserterDecorator(IbatisBatchInserter delegate){
		this.delegate = delegate;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.ibatis.batch.IbatisBatchInserter#executeBatch()
	 */
	@Override
	public void executeBatch() {
		for(BatchGroupKey key : statementMap.keySet()) {
			for(Object argument : statementMap.get(key)){
				delegate.insert(key.getSql(), argument);
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
		this.insert(sql, parameter, sql);
	}

	@Override
	public void insert(String sql, Object parameter, String batchGroup) {
		BatchGroupKey key = new BatchGroupKey(sql, batchGroup);
		if(!this.statementMap.containsKey(key)) {
			this.statementMap.put(key, new ArrayList<Object>());
		}
		statementMap.get(key).add(parameter);
	}
	
	private class BatchGroupKey {
		private String sql;
		private String batchGroup;
		
		public BatchGroupKey(String sql, String batchGroup) {
			super();
			this.sql = sql;
			this.batchGroup = batchGroup;
		}

		public String getSql() {
			return sql;
		}

		public void setSql(String sql) {
			this.sql = sql;
		}

		public String getBatchGroup() {
			return batchGroup;
		}

		public void setBatchGroup(String batchGroup) {
			this.batchGroup = batchGroup;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ ((batchGroup == null) ? 0 : batchGroup.hashCode());
			result = prime * result + ((sql == null) ? 0 : sql.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			BatchGroupKey other = (BatchGroupKey) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (batchGroup == null) {
				if (other.batchGroup != null)
					return false;
			} else if (!batchGroup.equals(other.batchGroup))
				return false;
			if (sql == null) {
				if (other.sql != null)
					return false;
			} else if (!sql.equals(other.sql))
				return false;
			return true;
		}

		private InOrderOrderingBatchInserterDecorator getOuterType() {
			return InOrderOrderingBatchInserterDecorator.this;
		}
	}
}
