
package org.lexevs.dao.database.utility;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.support.MetaDataAccessException;

/**
 * A factory for creating DatabaseDialect objects. Because Hibernate cannot automatically
 * detect an Oracle 11g Dialect, we have to do it here.
 */
public class DatabaseValidationQueryFactory implements InitializingBean, FactoryBean {
	
	/** The Constant DEFAULT_VALIDATION_QUERY. */
	private static final String DEFAULT_VALIDATION_QUERY = "SELECT 1";
	
	/** The Constant ORACLE_VALIDATION_QUERY. */
	private static final String ORACLE_VALIDATION_QUERY = "SELECT sysdate from dual";
	
	/** The Constant DB2_VALIDATION_QUERY. */
	private static final String DB2_VALIDATION_QUERY = null;
	
	/** The Constant HSQLDB_VALIDATION_QUERY. */
	private static final String HSQLDB_VALIDATION_QUERY = null;
	
	/** The db url. */
	private String dbUrl;
	
	/** The validation query. */
	private String validationQuery;
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		validationQuery = getValidationQuery();
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		return validationQuery;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		return String.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}
	
	/**
	 * Gets the validation query.
	 * 
	 * @return the validation query
	 * 
	 * @throws MetaDataAccessException the meta data access exception
	 */
	public String getValidationQuery() throws MetaDataAccessException {

		if(dbUrl.toLowerCase().contains("oracle")){
			return ORACLE_VALIDATION_QUERY;
		} else if(dbUrl.toLowerCase().contains("db2")){
			return DB2_VALIDATION_QUERY;
		} else if(dbUrl.toLowerCase().contains("hsqldb")){
			return HSQLDB_VALIDATION_QUERY;
		} else {
			return DEFAULT_VALIDATION_QUERY;
		}
	}

	/**
	 * Gets the db url.
	 * 
	 * @return the db url
	 */
	public String getDbUrl() {
		return dbUrl;
	}

	/**
	 * Sets the db url.
	 * 
	 * @param dbUrl the new db url
	 */
	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}
}