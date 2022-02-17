
package org.lexevs.cache;

import java.io.File;

import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.LoggerFactory;
import org.lexevs.system.constants.SystemVariables;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class CacheConfigLocationFactory implements FactoryBean<Resource> {

	private SystemVariables systemVariables;

	private String cacheConfigFileName = "lexevsCacheConfig.xml";
	private String defaultClasspathCacheConfigFilePath = "ehcache/lexevsCacheConfig.xml";
	
	@Override
	public Resource getObject() throws Exception {
		
		if(systemVariables == null) {
			systemVariables = LexEvsServiceLocator.getInstance().getSystemResourceService().getSystemVariables();
		}
		
		Resource resource = new FileSystemResource(systemVariables.getConfigFileLocation() 
				+ File.separator + ".." + File.separator + this.cacheConfigFileName);
		
		if(!resource.exists()){
			LoggerFactory.getLogger().debug("No user defined Cache Settings available, using defaults.");
			
			resource = new ClassPathResource(defaultClasspathCacheConfigFilePath);
		} else {
			LoggerFactory.getLogger().debug("Found user defined Cache Settings at: " + resource.getFile().getPath());
		}
		
		return resource;
	}

	@Override
	public Class<Resource> getObjectType() {
		return Resource.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
	
	public SystemVariables getSystemVariables() {
		return systemVariables;
	}

	public void setSystemVariables(SystemVariables systemVariables) {
		this.systemVariables = systemVariables;
	}
}