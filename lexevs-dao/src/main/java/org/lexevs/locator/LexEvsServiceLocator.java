package org.lexevs.locator;

import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.registry.service.Registry;
import org.lexevs.system.ResourceManager;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class LexEvsServiceLocator {
	
	private static LexEvsServiceLocator serviceLocator;
	
	private static String BEAN_NAME = "lexEvsServiceLocator";
	private static String CONTEXT_FILE = "lexevsDao.xml";

	private DatabaseServiceManager serviceManager;
	private ResourceManager resourceManager;
	private Registry registry;
	
	public static synchronized LexEvsServiceLocator getInstance(){
		if(serviceLocator == null){
			serviceLocator = (LexEvsServiceLocator) new ClassPathXmlApplicationContext(CONTEXT_FILE).getBean(BEAN_NAME);
		}
		return serviceLocator;
	}
	
	public DatabaseServiceManager getServiceManager() {
		return serviceManager;
	}
	public void setServiceManager(DatabaseServiceManager serviceManager) {
		this.serviceManager = serviceManager;
	}
	public ResourceManager getResourceManager() {
		return resourceManager;
	}
	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}
	public Registry getRegistry() {
		return registry;
	}
	public void setRegistry(Registry registry) {
		this.registry = registry;
	}
	
	public void setApplicationContext(ApplicationContext applicationContext)
	throws BeansException {

		serviceLocator = (LexEvsServiceLocator) applicationContext.getBean(BEAN_NAME);	
	}
}
