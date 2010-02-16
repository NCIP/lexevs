package org.lexevs.registry.setup;

import org.lexevs.dao.database.operation.LexEvsDatabaseOperations;
import org.lexevs.dao.database.prefix.PrefixResolver;
import org.lexevs.system.constants.SystemVariables;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

public class LexEvsSchemaInstallationSetup  implements InitializingBean {
	
	private SystemVariables systemVariables;
	private PrefixResolver prefixResolver;
	private Resource registryCreateScript;
	private LexEvsDatabaseOperations lexEvsDatabaseOperations;
	
	private boolean isLexGridSchemaInstalled;

	public void afterPropertiesSet() throws Exception {
		String prefix = prefixResolver.resolvePrefix();
		if(!isLexGridSchemaInstalled){
				this.getLexEvsDatabaseOperations().getDatabaseUtility().executeScript(
						registryCreateScript, prefix);
				if(this.systemVariables.isSingleTableMode()){
					lexEvsDatabaseOperations.createTables(prefix);
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

	public Resource getRegistryCreateScript() {
		return registryCreateScript;
	}

	public void setRegistryCreateScript(Resource registryCreateScript) {
		this.registryCreateScript = registryCreateScript;
	}

	public void setPrefixResolver(PrefixResolver prefixResolver) {
		this.prefixResolver = prefixResolver;
	}

	public PrefixResolver getPrefixResolver() {
		return prefixResolver;
	}
}
