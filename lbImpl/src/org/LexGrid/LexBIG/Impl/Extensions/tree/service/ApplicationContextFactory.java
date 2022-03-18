
package org.LexGrid.LexBIG.Impl.Extensions.tree.service;

import java.io.InputStream;
import java.io.Serializable;

import org.lexevs.system.utility.MyClassLoader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.InputStreamResource;

/**
 * A factory for creating ApplicationContext objects.
 */
public class ApplicationContextFactory implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5794966638343867430L;
	
	/** The instance. */
	private static ApplicationContextFactory instance;
	
	/** The context. */
	private ApplicationContext context;

	/**
	 * Gets the single instance of ApplicationContextFactory.
	 * 
	 * @return single instance of ApplicationContextFactory
	 */
	public static synchronized ApplicationContextFactory getInstance() {
		if (instance == null) {
			instance = new ApplicationContextFactory();
		}
		return instance;
	}

	/**
	 * Instantiates a new application context factory.
	 */
	protected ApplicationContextFactory(){
		GenericApplicationContext ctx = new GenericApplicationContext();
		ctx.setClassLoader(MyClassLoader.instance());
		XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(ctx);
		xmlReader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_NONE);
		InputStream stream = MyClassLoader.instance().getResourceAsStream("treeServiceContext.xml");
		xmlReader.loadBeanDefinitions(new InputStreamResource(stream));
		ctx.refresh();

		this.context = ctx;
	}

	/**
	 * Gets the application context.
	 * 
	 * @return the application context
	 */
	public ApplicationContext getApplicationContext(){
		return this.context;
	}
}