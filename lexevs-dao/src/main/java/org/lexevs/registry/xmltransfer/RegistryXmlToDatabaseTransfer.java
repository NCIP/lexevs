package org.lexevs.registry.xmltransfer;

import java.io.File;

import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;
import org.lexevs.registry.service.XmlRegistry;
import org.lexevs.registry.service.Registry.DBEntry;
import org.lexevs.registry.service.Registry.HistoryEntry;
import org.lexevs.system.ResourceManager;
import org.lexevs.system.constants.SystemVariables;
import org.springframework.beans.factory.InitializingBean;

public class RegistryXmlToDatabaseTransfer implements InitializingBean {
	
	private SystemVariables systemVariables;
	private Registry registry;

	public void afterPropertiesSet() throws Exception {
	
		File registryXml = new File(systemVariables.getAutoLoadRegistryPath());
		
		if(registryXml.exists()){
			int dbEntries = registry.getAllRegistryEntries().size();
			
			if(dbEntries == 0){
				XmlRegistry xmlRegistry = new XmlRegistry(registryXml.getAbsolutePath());
				for(DBEntry dbEntry : xmlRegistry.getDBEntries()){
					registry.addNewItem(RegistryEntry.toRegistryEntry(dbEntry));
				}	
				
				for(HistoryEntry dbEntry : xmlRegistry.getHistoryEntries()){
					registry.addNewItem(RegistryEntry.toRegistryEntry(dbEntry));
				}	
			}
		}	
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

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	public Registry getRegistry() {
		return registry;
	}
	
	
}
