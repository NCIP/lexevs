package org.lexevs.system;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DefaultResourceManagerFactory implements ResourceManagerFactory {

	private static ApplicationContext context = new ClassPathXmlApplicationContext("lexevsResource.xml");
	
	/* (non-Javadoc)
	 * @see org.lexevs.system.ResourceManagerFactoryI#getResourceManager()
	 */
	public ResourceManager getResourceManager(){
		return (ResourceManager) context.getBean("resourceManager");
	}

}
