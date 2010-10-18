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
package org.lexevs.dao.database.scheme;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.apache.commons.lang.ClassUtils;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.system.utility.MyClassLoader;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * A factory for creating PersistenceScheme objects.
 */
public class PersistenceSchemeFactory implements FactoryBean, InitializingBean {

	/** The current persistence scheme. */
	private LexGridSchemaVersion currentPersistenceScheme;
	
	/** The persistence schemes. */
	private List<PersistenceScheme> persistenceSchemes;
	
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
				logger.warn("Extension: " + ed.getName() + " cannot be loaded, " +
						"class: " + ed.getExtensionClass() + " could not be found.");
				continue;
			}

			if(ClassUtils.isAssignable(extensionBaseClass, PersistenceScheme.class)){
				this.persistenceSchemes.add(
						(PersistenceScheme)
						Class.forName(ed.getExtensionClass(), true, myClassLoader).newInstance()
				);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	@Override
	public Object getObject() throws Exception {
		return this.getPersistenceScheme(currentPersistenceScheme);
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	@Override
	public Class<?> getObjectType() {
		return PersistenceScheme.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	@Override
	public boolean isSingleton() {
		return true;
	}
	
	/**
	 * Gets the persistence scheme.
	 * 
	 * @param version the version
	 * 
	 * @return the persistence scheme
	 */
	private PersistenceScheme getPersistenceScheme(LexGridSchemaVersion version) {
		for(PersistenceScheme scheme : this.persistenceSchemes) {
			if(scheme.getLexGridSchemaVersion().isEqualVersion(version)) {
				return scheme;
			}
		}
		
		throw new RuntimeException("No Persistence Scheme found for: " + version);
	}

	/**
	 * Gets the current persistence scheme.
	 * 
	 * @return the current persistence scheme
	 */
	public LexGridSchemaVersion getCurrentPersistenceScheme() {
		return currentPersistenceScheme;
	}

	/**
	 * Sets the current persistence scheme.
	 * 
	 * @param currentPersistenceScheme the new current persistence scheme
	 */
	public void setCurrentPersistenceScheme(
			LexGridSchemaVersion currentPersistenceScheme) {
		this.currentPersistenceScheme = currentPersistenceScheme;
	}

	/**
	 * Sets the persistence schemes.
	 * 
	 * @param persistenceSchemes the new persistence schemes
	 */
	public void setPersistenceSchemes(List<PersistenceScheme> persistenceSchemes) {
		this.persistenceSchemes = persistenceSchemes;
	}

	/**
	 * Gets the persistence schemes.
	 * 
	 * @return the persistence schemes
	 */
	public List<PersistenceScheme> getPersistenceSchemes() {
		return persistenceSchemes;
	}

	public void setMyClassLoader(MyClassLoader myClassLoader) {
		this.myClassLoader = myClassLoader;
	}

	public MyClassLoader getMyClassLoader() {
		return myClassLoader;
	}

	public LgLoggerIF getLogger() {
		return logger;
	}

	public void setLogger(LgLoggerIF logger) {
		this.logger = logger;
	}
}