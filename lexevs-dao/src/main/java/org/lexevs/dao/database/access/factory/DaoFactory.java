package org.lexevs.dao.database.access.factory;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class DaoFactory<T extends LexGridSchemaVersionAwareDao> implements FactoryBean, InitializingBean {

	private List<T> versionableDaos;
	
	private T daoToReturn;
	
	private LexGridSchemaVersion lexGridSchemaVersion;
	
	public Object getObject() throws Exception {
		return daoToReturn;
	}

	@SuppressWarnings("unchecked")
	public Class getObjectType() {
		return LexGridSchemaVersionAwareDao.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public void afterPropertiesSet() throws Exception {
		List<T> daos = new ArrayList<T>();
		
		for(T dao : versionableDaos){
			if(dao.supportsLgSchemaVersion(lexGridSchemaVersion)){
				daos.add(dao);
			}
		}
		
		Assert.assertTrue("No matching DAO for Database Version: " +
				lexGridSchemaVersion, daos.size() > 0);	
		
		Assert.assertTrue("More than one matching DAO for: " +
				daos.get(0).getClass().getName(), daos.size() < 2);
		
		daoToReturn = daos.get(0);
	}

	public List<T> getVersionableDaos() {
		return versionableDaos;
	}

	public void setVersionableDaos(List<T> versionableDaos) {
		this.versionableDaos = versionableDaos;
	}

	public void setLexGridSchemaVersion(LexGridSchemaVersion lexGridSchemaVersion) {
		this.lexGridSchemaVersion = lexGridSchemaVersion;
	}

	public LexGridSchemaVersion getLexGridSchemaVersion() {
		return lexGridSchemaVersion;
	}


}
