package org.lexevs.dao.database.schemaversion;

import org.lexevs.dao.database.access.tablemetadata.TableMetadataDao;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class LexGridSchemaVersionFactory implements FactoryBean, InitializingBean{

	private TableMetadataDao tableMetadataDao;

	private LexGridSchemaVersion version;
	
	public Object getObject() throws Exception {
		return version;
	}

	@SuppressWarnings("unchecked")
	public Class getObjectType() {
		return LexGridSchemaVersion.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public void afterPropertiesSet() throws Exception {
		version = LexGridSchemaVersion.parseStringToVersion(tableMetadataDao.getVersion());	
	}

	public TableMetadataDao getTableMetadataDao() {
		return tableMetadataDao;
	}

	public void setTableMetadataDao(TableMetadataDao tableMetadataDao) {
		this.tableMetadataDao = tableMetadataDao;
	}
}
