
package org.lexevs.dao.database.setup.schemacheck;

import javax.sql.DataSource;

import org.lexevs.system.constants.SystemVariables;
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
			SystemVariables systemVariables) {
		super(dataSource, systemVariables);
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
		return "SELECT count(*) from " + this.getSystemVariables().getAutoLoadDBPrefix() + checkTableName;
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