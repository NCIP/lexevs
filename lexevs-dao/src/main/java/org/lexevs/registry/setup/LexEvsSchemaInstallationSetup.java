package org.lexevs.registry.setup;

import org.lexevs.dao.database.operation.LexEvsDatabaseOperations;
import org.lexevs.dao.database.prefix.PrefixResolver;
import org.lexevs.logging.LoggingBean;
import org.lexevs.system.constants.SystemVariables;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

public class LexEvsSchemaInstallationSetup extends LoggingBean implements InitializingBean {
	
	private SystemVariables systemVariables;
	private LexEvsDatabaseOperations lexEvsDatabaseOperations;
	
	private boolean isLexGridSchemaInstalled;

	public void afterPropertiesSet() throws Exception {
		this.getLogger().info("Checking for installed LexEVS Database Schema.");

		if(!isLexGridSchemaInstalled){
				this.getLexEvsDatabaseOperations().createCommonTables();
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
}
