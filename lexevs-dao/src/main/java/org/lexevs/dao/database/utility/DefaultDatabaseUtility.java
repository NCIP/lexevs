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
package org.lexevs.dao.database.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.lexevs.dao.database.constants.DatabaseConstants;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;

/**
 * Spring JDBC implementation of DefaultDatabaseUtility.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultDatabaseUtility extends JdbcDaoSupport implements DatabaseUtility {
	
	/* (non-Javadoc)
	 * @see org.LexGrid.persistence.database.DatabaseUtility#dropDatabase(java.lang.String)
	 */
	public void dropDatabase(String databaseName) {
		String dropString = "drop table ";
		getJdbcTemplate().execute(dropString + databaseName);	
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.persistence.database.DatabaseUtility#executeScript(org.springframework.core.io.Resource)
	 */
	public void executeScript(Resource creationScript) throws Exception {
		String script = convertResourceToString(creationScript);
		this.executeScript(script);
	}
	
	public void executeScript(Resource creationScript, String prefix) throws Exception {
		String script = convertResourceToString(creationScript);
		this.executeScript(
				adjustForPrefix(script,prefix));
	}
	
	/* (non-Javadoc)
	 * @see org.LexGrid.persistence.database.DatabaseUtility#executeScript(java.lang.String)
	 */
	public void executeScript(String script, String prefix) throws Exception {	
		script = adjustForPrefix(script, prefix);
		doExecuteScript(script);
	}
	
	public void executeScript(String script) throws Exception {	
		doExecuteScript(script);
	}
	
	/**
	 * Convert resource to string.
	 * 
	 * @param resource the resource
	 * 
	 * @return the string
	 */
	public static String convertResourceToString(Resource resource) {

		InputStream is = null;
		try {
			is = resource.getInputStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				line = line + "\n";
				sb.append(line);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}
	

	/**
	 * Adjust for prefix.
	 * 
	 * @param script the script
	 * 
	 * @return the string
	 */
	protected String adjustForPrefix(String script, String prefix){
		return script.replaceAll(DatabaseConstants.PREFIX_PLACEHOLDER, prefix);
	}
	
	protected String adjustForIndividualTablePrefix(String script, String individualTablePrefix){
		return script.replaceAll(DatabaseConstants.MULTIPLE_TABLES_PREFIX_PLACEHOLDER, individualTablePrefix);
	}
	
	/**
	 * Do execute script.
	 * 
	 * @param scriptResource the script resource
	 */
	private void doExecuteScript(final String scriptResource) {
		if (scriptResource == null){
			return;
		}
		TransactionTemplate transactionTemplate = new TransactionTemplate(new DataSourceTransactionManager(getDataSource()));
		transactionTemplate.execute(new TransactionCallback() {

			@SuppressWarnings("unchecked")
			public Object doInTransaction(TransactionStatus status) {
				JdbcTemplate jdbcTemplate = getJdbcTemplate();
				String[] scripts;
				try {
					scripts = StringUtils.delimitedListToStringArray(stripComments(IOUtils.readLines(new StringReader(scriptResource))), ";");
				}
				catch (IOException e) {
					throw new BeanInitializationException("Cannot load script from [" + scriptResource + "]", e);
				}
				for (int i = 0; i < scripts.length; i++) {
					String script = scripts[i].trim();
					if (StringUtils.hasText(script)) {
						try {
							jdbcTemplate.execute(script);
						}
						catch (DataAccessException e) {	
								throw e;
						}
					}
				}
				return null;
			}

		});

	}

	/**
	 * Strip comments.
	 * 
	 * @param list the list
	 * 
	 * @return the string
	 */
	private String stripComments(List<String> list) {
		StringBuffer buffer = new StringBuffer();
		for (String line : list) {
			if (!line.startsWith("//") && !line.startsWith("--")) {
				buffer.append(line + "\n");
			}
		}
		return buffer.toString();
	}

	public void truncateTable(String tableName)
			throws Exception {
		String sql = "TRUNCATE table " + tableName;
		getJdbcTemplate().execute(sql);
	}

	public boolean doesTableExist(String tableName) {
		String sql = "SELECT * FROM " + tableName;

		try {
			getJdbcTemplate().execute(sql);
			return true;

		} catch (Throwable e) {
			return false;
		}
	}

}
