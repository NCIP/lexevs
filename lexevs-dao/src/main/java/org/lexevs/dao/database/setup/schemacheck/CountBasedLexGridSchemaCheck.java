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
package org.lexevs.dao.database.setup.schemacheck;

import javax.sql.DataSource;

import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.lexevs.dao.database.prefix.PrefixResolver;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * The Class CountBasedLexGridSchemaCheck.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CountBasedLexGridSchemaCheck extends AbstractLesGridSchemaCheck {
	
	/** The check table name. */
	private String checkTableName = "registry";

	/**
	 * Instantiates a new count based lex grid schema check.
	 */
	public CountBasedLexGridSchemaCheck() {
		super();
	}

	/**
	 * Instantiates a new count based lex grid schema check.
	 * 
	 * @param dataSource the data source
	 * @param prefixResolver the prefix resolver
	 */
	public CountBasedLexGridSchemaCheck(DataSource dataSource,
			PrefixResolver prefixResolver) {
		super(dataSource, prefixResolver);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.setup.schemacheck.AbstractLesGridSchemaCheck#checkResult(org.springframework.jdbc.core.JdbcTemplate, java.lang.String)
	 */
	@Override
	protected boolean checkResult(JdbcTemplate template, String sql) {
		try{
			template.execute(sql);
			return true;
		} catch(Throwable t){
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.setup.schemacheck.AbstractLesGridSchemaCheck#getDbCheckSql()
	 */
	@Override
	protected String getDbCheckSql() {
		return "SELECT count(*) from " + this.getPrefixResolver().resolveDefaultPrefix() + checkTableName;
	}

	/**
	 * Sets the check table name.
	 * 
	 * @param checkTableName the new check table name
	 */
	public void setCheckTableName(String checkTableName) {
		this.checkTableName = checkTableName;
	}

	/**
	 * Gets the check table name.
	 * 
	 * @return the check table name
	 */
	public String getCheckTableName() {
		return checkTableName;
	}
}
