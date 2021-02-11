
package org.lexevs.dao.database.schemaversion;

import org.lexevs.dao.database.access.tablemetadata.TableMetadataDao;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * A factory for creating LexGridSchemaVersion objects.
 */
public class LexGridSchemaVersionFactory implements FactoryBean, InitializingBean{

	/** The table metadata dao. */
	private TableMetadataDao tableMetadataDao;

	/** The version. */
	private LexGridSchemaVersion version;
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		return version;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	@SuppressWarnings("unchecked")
	public Class getObjectType() {
		return LexGridSchemaVersion.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		version = LexGridSchemaVersion.parseStringToVersion(tableMetadataDao.getVersion());	
	}

	/**
	 * Gets the table metadata dao.
	 * 
	 * @return the table metadata dao
	 */
	public TableMetadataDao getTableMetadataDao() {
		return tableMetadataDao;
	}

	/**
	 * Sets the table metadata dao.
	 * 
	 * @param tableMetadataDao the new table metadata dao
	 */
	public void setTableMetadataDao(TableMetadataDao tableMetadataDao) {
		this.tableMetadataDao = tableMetadataDao;
	}
}