package org.lexevs.dao.database.setup.schemacheck;

import javax.sql.DataSource;

import org.lexevs.dao.database.type.DatabaseType;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class LexGridSchemaCheckFactory implements FactoryBean, InitializingBean {

	private DatabaseType databaseType;
	
	private boolean isSchemaLoaded;
	private DataSource dataSource;
	
	public Object getObject() throws Exception {
		return isSchemaLoaded;
	}
	
	public void afterPropertiesSet() throws Exception {
		LexGridSchemaCheck schemaCheck = null;
		if(databaseType.equals(DatabaseType.HSQL)){
			schemaCheck = new CountBasedLexGridSchemaCheck(dataSource);
		}
		
		if(databaseType.equals(DatabaseType.MYSQL)){
			schemaCheck = new CountBasedLexGridSchemaCheck(dataSource);
		}
		
		Assert.notNull(schemaCheck);
		
		isSchemaLoaded = schemaCheck.isLgSchemaInstalled();
	}

	public Class getObjectType() {
		return boolean.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public void setDatabaseType(DatabaseType databaseType) {
		this.databaseType = databaseType;
	}

	public DatabaseType getDatabaseType() {
		return databaseType;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public DataSource getDataSource() {
		return dataSource;
	}
}
