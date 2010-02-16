package org.lexevs.registry.setup;

import org.lexevs.dao.database.operation.LexEvsDatabaseOperations;
import org.lexevs.dao.database.utility.DatabaseUtility;
import org.lexevs.system.constants.SystemVariables;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

public class LexEvsSchemaInstallationSetup  implements InitializingBean {
	
	private SystemVariables systemVariables;
	private Resource registryCreateScript;
	private DatabaseUtility databaseUtility;
	
	private boolean isLexGridSchemaInstalled;

	public void afterPropertiesSet() throws Exception {
		String prefix = this.getSystemVariables().getAutoLoadDBPrefix();
		if(!isLexGridSchemaInstalled){
				this.getDatabaseUtility().executeScript(
						registryCreateScript, prefix);
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

	public DatabaseUtility getDatabaseUtility() {
		return databaseUtility;
	}

	public void setDatabaseUtility(DatabaseUtility databaseUtility) {
		this.databaseUtility = databaseUtility;
	}

	public Resource getRegistryCreateScript() {
		return registryCreateScript;
	}

	public void setRegistryCreateScript(Resource registryCreateScript) {
		this.registryCreateScript = registryCreateScript;
	}
}
