/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package org.lexevs.dao.database.service.event;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.apache.commons.lang.ClassUtils;
import org.lexevs.dao.database.service.event.codingscheme.CodingSchemeUpdateEvent;
import org.lexevs.dao.database.service.event.codingscheme.PostCodingSchemeInsertEvent;
import org.lexevs.dao.database.service.event.codingscheme.PreCodingSchemeInsertEvent;
import org.lexevs.dao.database.service.event.entity.EntityInsertOrRemoveEvent;
import org.lexevs.dao.database.service.event.entity.EntityUpdateEvent;
import org.lexevs.dao.database.service.event.property.PropertyUpdateEvent;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;
import org.lexevs.system.utility.MyClassLoader;
import org.springframework.beans.factory.InitializingBean;

/**
 * The Class DatabaseServiceEventSupport.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DatabaseServiceEventSupport implements InitializingBean {

	/** The database service event listeners. */
	private List<DatabaseServiceEventListener> databaseServiceEventListeners = new ArrayList<DatabaseServiceEventListener>();

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
	
	/**
	 * Fire coding scheme update event.
	 * 
	 * @param revisionId the revision id
	 * @param entryStateId the entry state id
	 * @param originalCodingScheme the original coding scheme
	 * @param updatedCodingScheme the updated coding scheme
	 */
	protected void fireCodingSchemeUpdateEvent(
			String revisionId,
			String entryStateId,
			CodingScheme originalCodingScheme,
			CodingScheme updatedCodingScheme){
		if(databaseServiceEventListeners != null){
			for(DatabaseServiceEventListener listener : this.databaseServiceEventListeners){
				listener.onCodingSchemeUpdate(
						new CodingSchemeUpdateEvent(
								revisionId,
								entryStateId,
								originalCodingScheme,
								updatedCodingScheme));
			}
		}
	}
	
	/**
	 * Fire coding scheme insert event.
	 * 
	 * @param codingScheme the coding scheme
	 * 
	 * @throws CodingSchemeAlreadyLoadedException the coding scheme already loaded exception
	 */
	protected void firePreCodingSchemeInsertEvent(
			CodingScheme codingScheme) throws CodingSchemeAlreadyLoadedException{
		if(databaseServiceEventListeners != null){
			for(DatabaseServiceEventListener listener : this.databaseServiceEventListeners){
				listener.onPreCodingSchemeInsert(
						new PreCodingSchemeInsertEvent(
								codingScheme));
			}
		}
	}
	
	protected void firePostCodingSchemeInsertEvent(
			CodingScheme codingScheme) throws CodingSchemeAlreadyLoadedException{
		if(databaseServiceEventListeners != null){
			for(DatabaseServiceEventListener listener : this.databaseServiceEventListeners){
				listener.onPostCodingSchemeInsert(
						new PostCodingSchemeInsertEvent(
								codingScheme));
			}
		}
	}
	
	protected void firePreEntityInsertEvent(EntityInsertOrRemoveEvent entityInsertEvent){
		if(databaseServiceEventListeners != null){
			for(DatabaseServiceEventListener listener : this.databaseServiceEventListeners){
				listener.onPreEntityInsert(entityInsertEvent);
			}
		}	
	}
	
	protected void firePostEntityInsertEvent(EntityInsertOrRemoveEvent entityInsertEvent){
		if(databaseServiceEventListeners != null){
			for(DatabaseServiceEventListener listener : this.databaseServiceEventListeners){
				listener.onPreEntityInsert(entityInsertEvent);
			}
		}	
	}
	
	protected void firePropertyUpdateEvent(
			PropertyUpdateEvent event) {
		if(databaseServiceEventListeners != null){
			for(DatabaseServiceEventListener listener : this.databaseServiceEventListeners){
				listener.onPropertyUpdate(event);
			}
		}
	}
	
	protected void fireEntityUpdateEvent(EntityUpdateEvent entityUpdateEvent) {
		if(databaseServiceEventListeners != null){
			for(DatabaseServiceEventListener listener : this.databaseServiceEventListeners){
				listener.onEntityUpdate(entityUpdateEvent);
			}
		}
	}
	
	protected void firePreEntityRemoveEvent(EntityInsertOrRemoveEvent entityRemoveEvent){
		if(databaseServiceEventListeners != null){
			for(DatabaseServiceEventListener listener : this.databaseServiceEventListeners){
				listener.onPreEntityRemove(entityRemoveEvent);
			}
		}	
	}

	/**
	 * Gets the database service event listeners.
	 * 
	 * @return the database service event listeners
	 */
	public List<DatabaseServiceEventListener> getDatabaseServiceEventListeners() {
		return databaseServiceEventListeners;
	}

	/**
	 * Sets the database service event listeners.
	 * 
	 * @param databaseServiceEventListeners the new database service event listeners
	 */
	public void setDatabaseServiceEventListeners(
			List<DatabaseServiceEventListener> databaseServiceEventListeners) {
		this.databaseServiceEventListeners = databaseServiceEventListeners;
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
