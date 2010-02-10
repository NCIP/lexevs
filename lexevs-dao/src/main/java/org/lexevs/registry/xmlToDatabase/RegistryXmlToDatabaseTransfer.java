package org.lexevs.registry.xmlToDatabase;

import java.io.File;

import org.lexevs.registry.service.Registry;
import org.lexevs.registry.service.XmlRegistry;
import org.lexevs.registry.service.Registry.DBEntry;
import org.lexevs.registry.service.Registry.HistoryEntry;
import org.lexevs.system.ResourceManager;
import org.springframework.beans.factory.InitializingBean;

public class RegistryXmlToDatabaseTransfer implements InitializingBean {

	public void afterPropertiesSet() throws Exception {
		Registry datbaseRegistry = ResourceManager.instance().getRegistry();
	
		File registryXml = new File(ResourceManager.instance().getSystemVariables().getAutoLoadRegistryPath());
		
		if(registryXml.exists()){
			int dbEntries = ResourceManager.instance().getRegistry().getDBEntries().length;
			int historyEntries = ResourceManager.instance().getRegistry().getHistoryEntries().length;
			
			if(dbEntries == 0 && historyEntries == 0){
				XmlRegistry xmlRegistry = new XmlRegistry(registryXml.getAbsolutePath());
				for(DBEntry dbEntry : xmlRegistry.getDBEntries()){
					datbaseRegistry.addNewItem(
							dbEntry.urn, 
							dbEntry.version, 
							dbEntry.status, 
							dbEntry.dbURL, 
							dbEntry.tag, 
							dbEntry.dbName, 
							dbEntry.prefix);
				}	
				
				for(HistoryEntry dbEntry : xmlRegistry.getHistoryEntries()){
					datbaseRegistry.addNewHistory(
							dbEntry.urn, 
							dbEntry.dbURL, 
							dbEntry.dbName, 
							dbEntry.prefix);
				}	
			}
		}
		
	}
}
