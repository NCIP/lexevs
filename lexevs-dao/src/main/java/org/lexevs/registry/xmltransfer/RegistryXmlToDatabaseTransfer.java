
package org.lexevs.registry.xmltransfer;

import java.io.File;
import java.util.List;

import org.lexevs.logging.AbstractLoggingBean;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;
import org.lexevs.system.constants.SystemVariables;
import org.springframework.beans.factory.InitializingBean;

/**
 * The Class RegistryXmlToDatabaseTransfer.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class RegistryXmlToDatabaseTransfer extends AbstractLoggingBean implements InitializingBean {
	
	/** The system variables. */
	private SystemVariables systemVariables;
	
	/** The database registry. */
	private Registry databaseRegistry;
	
	/** The xml registry. */
	private Registry xmlRegistry;
	
	//use 'LexGridSchemaCheckFactory'
	/** The is lex grid schema installed. */
	private boolean isLexGridSchemaInstalled;

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {

		//check if flag is set in systemVariables
		//TODO: ...
		boolean isMigrateOnstartup = systemVariables.isMigrateOnStartupEnabled();
		
		
		if( (!isLexGridSchemaInstalled) && isMigrateOnstartup) {
			this.getLogger().info("Migrating XML Registry content to the Database Registry.");
			
			List<RegistryEntry> xmlEntries = xmlRegistry.getAllRegistryEntries();
			for(RegistryEntry entry : xmlEntries) {
				this.getLogger().info(" - Migrating: " + entry.getResourceUri());
				databaseRegistry.addNewItem(entry);
			}
			deleteRegistryXmlFile();
		}
	}
	
	/**
	 * Delete registry xml file exist.
	 */
	protected void deleteRegistryXmlFile(){
		File registry = new File(systemVariables.getAutoLoadRegistryPath());
		registry.delete();
	}
	
	/**
	 * Does registry xml file exist.
	 * 
	 * @return true, if successful
	 */
	protected boolean doesRegistryXmlFileExist(){
		File registry = new File(systemVariables.getAutoLoadRegistryPath());
		return registry.exists();
	}

	/**
	 * Gets the system variables.
	 * 
	 * @return the system variables
	 */
	public SystemVariables getSystemVariables() {
		return systemVariables;
	}

	/**
	 * Sets the system variables.
	 * 
	 * @param systemVariables the new system variables
	 */
	public void setSystemVariables(SystemVariables systemVariables) {
		this.systemVariables = systemVariables;
	}

	/**
	 * Gets the database registry.
	 * 
	 * @return the database registry
	 */
	public Registry getDatabaseRegistry() {
		return databaseRegistry;
	}

	/**
	 * Sets the database registry.
	 * 
	 * @param databaseRegistry the new database registry
	 */
	public void setDatabaseRegistry(Registry databaseRegistry) {
		this.databaseRegistry = databaseRegistry;
	}

	/**
	 * Gets the xml registry.
	 * 
	 * @return the xml registry
	 */
	public Registry getXmlRegistry() {
		return xmlRegistry;
	}

	/**
	 * Sets the xml registry.
	 * 
	 * @param xmlRegistry the new xml registry
	 */
	public void setXmlRegistry(Registry xmlRegistry) {
		this.xmlRegistry = xmlRegistry;
	}

	/**
	 * Checks if is lex grid schema installed.
	 * 
	 * @return true, if is lex grid schema installed
	 */
	public boolean isLexGridSchemaInstalled() {
		return isLexGridSchemaInstalled;
	}

	/**
	 * Sets the lex grid schema installed.
	 * 
	 * @param isLexGridSchemaInstalled the new lex grid schema installed
	 */
	public void setLexGridSchemaInstalled(boolean isLexGridSchemaInstalled) {
		this.isLexGridSchemaInstalled = isLexGridSchemaInstalled;
	}	
}