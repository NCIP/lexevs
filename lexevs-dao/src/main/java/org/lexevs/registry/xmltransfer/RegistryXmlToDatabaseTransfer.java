package org.lexevs.registry.xmltransfer;

import java.io.File;
import java.util.List;

import org.lexevs.logging.LoggingBean;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;
import org.lexevs.system.constants.SystemVariables;
import org.springframework.beans.factory.InitializingBean;

public class RegistryXmlToDatabaseTransfer extends LoggingBean implements InitializingBean {
	
	private SystemVariables systemVariables;
	
	private Registry databaseRegistry;
	private Registry xmlRegistry;
	
	//use 'LexGridSchemaCheckFactory'
	private boolean isLexGridSchemaInstalled;

	public void afterPropertiesSet() throws Exception {
		//check if flag is set in systemVariables
		//TODO: ...
		boolean isMigrateOnstartup = false;
		
		
		if( (!isLexGridSchemaInstalled) && isMigrateOnstartup) {
			this.getLogger().info("Migrating XML Registry content to the Database Registry.");
			
			List<RegistryEntry> xmlEntries = xmlRegistry.getAllRegistryEntries();
			for(RegistryEntry entry : xmlEntries) {
				this.getLogger().info(" - Migrating: " + entry.getResourceUri());
				databaseRegistry.addNewItem(entry);
			}
			deleteRegistryXmlFileExist();
		}
	}
	
	protected void deleteRegistryXmlFileExist(){
		File registry = new File(systemVariables.getAutoLoadRegistryPath());
		registry.delete();
	}
	
	protected boolean doesRegistryXmlFileExist(){
		File registry = new File(systemVariables.getAutoLoadRegistryPath());
		return registry.exists();
	}

	public SystemVariables getSystemVariables() {
		return systemVariables;
	}

	public void setSystemVariables(SystemVariables systemVariables) {
		this.systemVariables = systemVariables;
	}

	public Registry getDatabaseRegistry() {
		return databaseRegistry;
	}

	public void setDatabaseRegistry(Registry databaseRegistry) {
		this.databaseRegistry = databaseRegistry;
	}

	public Registry getXmlRegistry() {
		return xmlRegistry;
	}

	public void setXmlRegistry(Registry xmlRegistry) {
		this.xmlRegistry = xmlRegistry;
	}

	public boolean isLexGridSchemaInstalled() {
		return isLexGridSchemaInstalled;
	}

	public void setLexGridSchemaInstalled(boolean isLexGridSchemaInstalled) {
		this.isLexGridSchemaInstalled = isLexGridSchemaInstalled;
	}	
}
