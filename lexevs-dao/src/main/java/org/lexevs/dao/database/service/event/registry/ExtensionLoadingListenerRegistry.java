package org.lexevs.dao.database.service.event.registry;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.apache.commons.lang.ClassUtils;
import org.lexevs.dao.database.service.event.DatabaseServiceEventListener;
import org.lexevs.system.utility.MyClassLoader;
import org.springframework.beans.factory.InitializingBean;

public class ExtensionLoadingListenerRegistry extends BaseListenerRegistry implements InitializingBean {
	
	private MyClassLoader myClassLoader;
	
	private LgLoggerIF logger;

	
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
				this.getDatabaseServiceEventListeners().add(
						(DatabaseServiceEventListener)
						Class.forName(ed.getExtensionClass(), true, myClassLoader).newInstance()
						);
			}
		}
	}

	public MyClassLoader getMyClassLoader() {
		return myClassLoader;
	}


	public void setMyClassLoader(MyClassLoader myClassLoader) {
		this.myClassLoader = myClassLoader;
	}


	public LgLoggerIF getLogger() {
		return logger;
	}


	public void setLogger(LgLoggerIF logger) {
		this.logger = logger;
	}
}
