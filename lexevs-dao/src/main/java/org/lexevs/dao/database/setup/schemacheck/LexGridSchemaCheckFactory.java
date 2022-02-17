
package org.lexevs.dao.database.setup.schemacheck;

import javax.sql.DataSource;

import org.lexevs.dao.database.type.DatabaseType;
import org.lexevs.system.constants.SystemVariables;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * A factory for creating LexGridSchemaCheck objects.
 */
public class LexGridSchemaCheckFactory implements FactoryBean, InitializingBean {

	/** The database type. */
	private DatabaseType databaseType;
	
	/** The is schema loaded. */
	private boolean isSchemaLoaded;
	
	/** The data source. */
	private DataSource dataSource;
	
	/** The prefix resolver. */
	private SystemVariables systemVariables;
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		return isSchemaLoaded;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		LexGridSchemaCheck schemaCheck = new CountBasedLexGridSchemaCheck(dataSource, systemVariables);
		
		Assert.notNull(schemaCheck);
		
		isSchemaLoaded = schemaCheck.isCommonLexGridSchemaInstalled();
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		return boolean.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}

	/**
	 * Sets the database type.
	 * 
	 * @param databaseType the new database type
	 */
	public void setDatabaseType(DatabaseType databaseType) {
		this.databaseType = databaseType;
	}

	/**
	 * Gets the database type.
	 * 
	 * @return the database type
	 */
	public DatabaseType getDatabaseType() {
		return databaseType;
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

	/**
	 * Sets the prefix resolver.
	 * 
	 * @param prefixResolver the new prefix resolver
	 */

	public void setSystemVariables(SystemVariables systemVariables) {
		this.systemVariables = systemVariables;
	}

	public SystemVariables getSystemVariables() {
		return systemVariables;
	}
}