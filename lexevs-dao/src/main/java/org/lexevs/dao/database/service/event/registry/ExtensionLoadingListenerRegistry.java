/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
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
				this.registerListener(
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