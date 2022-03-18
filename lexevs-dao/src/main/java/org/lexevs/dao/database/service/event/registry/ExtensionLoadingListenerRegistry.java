
package org.lexevs.dao.database.service.event.registry;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.apache.commons.lang.ClassUtils;
import org.lexevs.dao.database.service.event.DatabaseServiceEventListener;
import org.lexevs.system.utility.MyClassLoader;
import org.springframework.beans.factory.InitializingBean;

/**
 * The Class ExtensionLoadingListenerRegistry.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ExtensionLoadingListenerRegistry extends BaseListenerRegistry implements InitializingBean {
	
	/** The my class loader. */
	private MyClassLoader myClassLoader;
	
	/** The logger. */
	private LgLoggerIF logger;

	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		for(ExtensionDescription ed :
			myClassLoader.getExtensionDescriptions()){

			Class<?> extensionBaseClass;
			try {
				extensionBaseClass = Class.forName(ed.getExtensionBaseClass(), true, myClassLoader);
			} catch (ClassNotFoundException e1) {
				getLogger().warn("Extension: " + ed.getName() + " cannot be loaded, " +
						"class: " + ed.getExtensionClass() + " could not be found.");
				continue;
			}

			if(ClassUtils.isAssignable(extensionBaseClass, DatabaseServiceEventListener.class)){
				this.registerListener(
						(DatabaseServiceEventListener)
						Class.forName(ed.getExtensionClass(), true, myClassLoader).newInstance()
						);
			}
		}
	}

	/**
	 * Gets the my class loader.
	 * 
	 * @return the my class loader
	 */
	public MyClassLoader getMyClassLoader() {
		return myClassLoader;
	}


	/**
	 * Sets the my class loader.
	 * 
	 * @param myClassLoader the new my class loader
	 */
	public void setMyClassLoader(MyClassLoader myClassLoader) {
		this.myClassLoader = myClassLoader;
	}


	/**
	 * Gets the logger.
	 * 
	 * @return the logger
	 */
	public LgLoggerIF getLogger() {
		return logger;
	}


	/**
	 * Sets the logger.
	 * 
	 * @param logger the new logger
	 */
	public void setLogger(LgLoggerIF logger) {
		this.logger = logger;
	}
}