
package org.lexevs.dao.database.spring;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Generates a Spring Application Context. Uses a Properties Object (instead of a Properties file)
 * to set Property Placeholders.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DynamicPropertyApplicationContext extends AbstractXmlApplicationContext {

	/** The config resources. */
	private Resource[] configResources;
	
	/**
	 * Instantiates a new dynamic property application context.
	 * 
	 * @param configLocation the config location
	 * @param properties the properties
	 * 
	 * @throws BeansException the beans exception
	 */
	public DynamicPropertyApplicationContext(String configLocation, Properties properties)
			throws BeansException {
		super(null);	

		this.configResources = new Resource[1];
		
		this.configResources[0] = new ClassPathResource(configLocation);

		PropertyPlaceholderConfigurer beanConfigurer = new PropertyPlaceholderConfigurer();
		beanConfigurer.setOrder(Ordered.HIGHEST_PRECEDENCE);
		beanConfigurer.setIgnoreUnresolvablePlaceholders(true);
		beanConfigurer.setProperties(properties);

		this.addBeanFactoryPostProcessor(beanConfigurer);
		refresh();
	}

	/* (non-Javadoc)
	 * @see org.springframework.context.support.AbstractXmlApplicationContext#getConfigResources()
	 */
	public Resource[] getConfigResources() {
		return configResources;
	}
}