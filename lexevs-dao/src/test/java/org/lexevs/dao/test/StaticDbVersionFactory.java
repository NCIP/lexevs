package org.lexevs.dao.test;

import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class StaticDbVersionFactory implements FactoryBean, InitializingBean {
	
	private String version;
	
	private LexGridSchemaVersion dbVersion;

	public Object getObject() throws Exception {
		return dbVersion;
	}

	@SuppressWarnings("unchecked")
	public Class getObjectType() {
		return LexGridSchemaVersion.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getVersion() {
		return version;
	}

	public void afterPropertiesSet() throws Exception {
		dbVersion = LexGridSchemaVersion.parseStringToVersion(version);
	}
}
