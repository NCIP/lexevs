
package org.lexevs.dao.database.setup.schemacheck;

import javax.sql.DataSource;

import org.lexevs.system.constants.SystemVariables;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * The Class AbstractLesGridSchemaCheck.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractLesGridSchemaCheck implements LexGridSchemaCheck {
	
	/** The data source. */
	private DataSource dataSource;
	
	/** The prefix resolver. */
	private SystemVariables systemVariables;
	
	/**
	 * Instantiates a new abstract les grid schema check.
	 */
	public AbstractLesGridSchemaCheck(){
		super();
	}
	
	/**
	 * Instantiates a new abstract les grid schema check.
	 * 
	 * @param dataSource the data source
	 * @param prefixResolver the prefix resolver
	 */
	public AbstractLesGridSchemaCheck(DataSource dataSource, SystemVariables systemVariables){
		this.dataSource = dataSource;
		this.setSystemVariables(systemVariables);
	}

	/**
	 * Sets the data source.
	 * 
	 * @param dataSource the new data source
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Gets the data source.
	 * 
	 * @return the data source
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.setup.schemacheck.LexGridSchemaCheck#isCommonLexGridSchemaInstalled()
	 */
	public boolean isCommonLexGridSchemaInstalled() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		return checkResult(template,
				getDbCheckSql());
	}
	
	/**
	 * Gets the db check sql.
	 * 
	 * @return the db check sql
	 */
	protected abstract String getDbCheckSql();
	
	/**
	 * Check result.
	 * 
	 * @param template the template
	 * @param sql the sql
	 * 
	 * @return true, if successful
	 */
	protected abstract boolean checkResult(JdbcTemplate template, String sql);

	public void setSystemVariables(SystemVariables systemVariables) {
		this.systemVariables = systemVariables;
	}

	public SystemVariables getSystemVariables() {
		return systemVariables;
	}
}