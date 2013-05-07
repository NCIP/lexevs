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

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.lexevs.dao.database.inserter.BatchInserter;
import org.lexevs.logging.LoggerFactory;

import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.ibatis.sqlmap.engine.execution.BatchResult;

/**
 * The Class SqlMapExecutorBatchInserter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SqlMapExecutorBatchInserter implements BatchInserter {
	
	/** The sql map executor. */
	private SqlMapExecutor sqlMapExecutor;
	
	/**
	 * Instantiates a new sql map executor batch inserter.
	 * 
	 * @param sqlMapExecutor the sql map executor
	 */
	public SqlMapExecutorBatchInserter(SqlMapExecutor sqlMapExecutor){
		this.sqlMapExecutor = sqlMapExecutor;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.ibatis.batch.BatchInserter#executeBatch()
	 */
	public void executeBatch() {
		try {
			List<BatchResult> list = sqlMapExecutor.executeBatchDetailed();
			
			if(CollectionUtils.isNotEmpty(list)) {

				float totalInserts = 0;

				float batches = list.size();

				for(BatchResult result : list) {
					if(result.getUpdateCounts() != null) {
						totalInserts += result.getUpdateCounts().length;
					}
				}

				//Some db drivers won't report batch statistics -- so only report if
				//something is there.
				if( totalInserts > 0 && batches > 0) {
					LoggerFactory.getLogger().debug("\nBatch Insert Results:\n" + 
							" -Batches: " + batches + "\n" +
							" -Inserts: " + totalInserts + "\n" +
							" " + (1 - (batches/totalInserts) + " Batch Efficiency (1.0 is best)" ));
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.ibatis.batch.BatchInserter#startBatch()
	 */
	public void startBatch() {
		try {
			sqlMapExecutor.startBatch();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.ibatis.batch.Inserter#insert(java.lang.String, java.lang.Object)
	 */
	public void insert(String sql, Object parameter) {
		try {
			sqlMapExecutor.insert(sql, parameter);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets the sql map executor.
	 * 
	 * @return the sql map executor
	 */
	public SqlMapExecutor getSqlMapExecutor() {
		return sqlMapExecutor;
	}

	/**
	 * Sets the sql map executor.
	 * 
	 * @param sqlMapExecutor the new sql map executor
	 */
	public void setSqlMapExecutor(SqlMapExecutor sqlMapExecutor) {
		this.sqlMapExecutor = sqlMapExecutor;
	}
}