/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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

import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * The Class SqlMapExecutorBatchInserter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SqlMapExecutorBatchInserter implements IbatisBatchInserter {
	
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
	 * @see org.lexevs.dao.database.ibatis.batch.IbatisBatchInserter#executeBatch()
	 */
	public void executeBatch() {
		try {
			sqlMapExecutor.executeBatch();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.ibatis.batch.IbatisBatchInserter#startBatch()
	 */
	public void startBatch() {
		try {
			sqlMapExecutor.startBatch();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.ibatis.batch.IbatisInserter#insert(java.lang.String, java.lang.Object)
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
