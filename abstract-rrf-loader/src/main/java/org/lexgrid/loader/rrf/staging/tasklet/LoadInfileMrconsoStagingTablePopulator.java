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
package org.lexgrid.loader.rrf.staging.tasklet;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * The Class LoadInfileMrconsoStagingTablePopulator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LoadInfileMrconsoStagingTablePopulator extends JdbcDaoSupport implements Tasklet {

	/** The Constant tableNamePlaceHolder. */
	private static final String tableNamePlaceHolder = "@TABLE_NAME";
	
	/** The table name. */
	private String tableName;
	
	/** The rrf dir. */
	private String rrfDir;
	
	/** The rrf file. */
	private String rrfFile;
	
	/** The load mrconso staging my sql. */
	private  String loadMrconsoStagingMySql = "load data local infile ? into table " + tableNamePlaceHolder + " fields terminated by '|'";
	
	/**
	 * Populate staging table.
	 * 
	 * @throws Exception the exception
	 */
	protected void populateStagingTable() throws Exception {
		String sql = loadMrconsoStagingMySql.replace(tableNamePlaceHolder, tableName);
		getJdbcTemplate().execute(sql, new PreparedStatementCallback() {
			public Object doInPreparedStatement(PreparedStatement ps)
					throws SQLException, DataAccessException {
				ps.setString(1, 
						adjustFilePath(rrfDir) + "/" + rrfFile);
				return ps.execute();
			}
		});			
	}
	
	private String adjustFilePath(String filePath){
		String filePrefix = "file:";
		if(!rrfDir.startsWith(filePrefix)){
			throw new RuntimeException("Cannot load :" + filePath + " by Load File Infile.");
		}
		return filePath.replaceFirst(filePrefix, "");
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.core.step.tasklet.Tasklet#execute(org.springframework.batch.core.StepContribution, org.springframework.batch.core.scope.context.ChunkContext)
	 */
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		this.populateStagingTable();
		return RepeatStatus.FINISHED;
	}

	/**
	 * Gets the table name.
	 * 
	 * @return the table name
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * Sets the table name.
	 * 
	 * @param tableName the new table name
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * Gets the rrf dir.
	 * 
	 * @return the rrf dir
	 */
	public String getRrfDir() {
		return rrfDir;
	}

	/**
	 * Sets the rrf dir.
	 * 
	 * @param rrfDir the new rrf dir
	 */
	public void setRrfDir(String rrfDir) {
		this.rrfDir = rrfDir;
	}

	/**
	 * Gets the rrf file.
	 * 
	 * @return the rrf file
	 */
	public String getRrfFile() {
		return rrfFile;
	}

	/**
	 * Sets the rrf file.
	 * 
	 * @param rrfFile the new rrf file
	 */
	public void setRrfFile(String rrfFile) {
		this.rrfFile = rrfFile;
	}
}
