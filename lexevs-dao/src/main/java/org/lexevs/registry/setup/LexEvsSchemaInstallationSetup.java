package org.lexevs.registry.setup;

import org.lexevs.dao.database.access.registry.RegistryDao;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations;
import org.lexevs.logging.LoggingBean;
import org.lexevs.system.constants.SystemVariables;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

public class LexEvsSchemaInstallationSetup extends LoggingBean implements InitializingBean {
	
	private SystemVariables systemVariables;
	private LexEvsDatabaseOperations lexEvsDatabaseOperations;
	private RegistryDao registryDao;
	
	private boolean isLexGridSchemaInstalled;

	//@Transactional
	public void afterPropertiesSet() throws Exception {
		this.getLogger().info("Checking for installed LexEVS Database Schema.");

		if(!isLexGridSchemaInstalled){
				this.getLexEvsDatabaseOperations().createCommonTables();
				this.registryDao.initRegistryMetadata();
				if(this.systemVariables.isSingleTableMode()){
					lexEvsDatabaseOperations.createCodingSchemeTables();
				}
		}	
	}

	public void setLexGridSchemaInstalled(boolean isLexGridSchemaInstalled) {
		this.isLexGridSchemaInstalled = isLexGridSchemaInstalled;
	}

	public boolean isLexGridSchemaInstalled() {
		return isLexGridSchemaInstalled;
	}

	public SystemVariables getSystemVariables() {
		return systemVariables;
	}

	public void setSystemVariables(SystemVariables systemVariables) {
		this.systemVariables = systemVariables;
	}

	public LexEvsDatabaseOperations getLexEvsDatabaseOperations() {
		return lexEvsDatabaseOperations;
	}

	public void setLexEvsDatabaseOperations(
			LexEvsDatabaseOperations lexEvsDatabaseOperations) {
		this.lexEvsDatabaseOperations = lexEvsDatabaseOperations;
	}

	public void setRegistryDao(RegistryDao registryDao) {
		this.registryDao = registryDao;
	}

	public RegistryDao getRegistryDao() {
		return registryDao;
	}
}
