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

import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.dao.database.service.event.association.AssociationBatchInsertEvent;
import org.lexevs.dao.database.service.event.codingscheme.CodingSchemeUpdateEvent;
import org.lexevs.dao.database.service.event.codingscheme.PostCodingSchemeInsertEvent;
import org.lexevs.dao.database.service.event.codingscheme.PreCodingSchemeInsertEvent;
import org.lexevs.dao.database.service.event.entity.EntityBatchInsertEvent;
import org.lexevs.dao.database.service.event.entity.EntityInsertOrRemoveEvent;
import org.lexevs.dao.database.service.event.entity.EntityUpdateEvent;
import org.lexevs.dao.database.service.event.property.PropertyUpdateEvent;
import org.lexevs.dao.database.service.event.registry.ListenerRegistry;
import org.lexevs.dao.database.service.event.revision.EntityReviseEvent;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;

/**
 * The Class DatabaseServiceEventSupport.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DatabaseServiceEventSupport {

	private ListenerRegistry listenerRegistry;

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
			for(DatabaseServiceEventListener listener : this.listenerRegistry.getRegisteredListeners()){
				listener.onCodingSchemeUpdate(
						new CodingSchemeUpdateEvent(
								revisionId,
								entryStateId,
								originalCodingScheme,
								updatedCodingScheme));
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
			for(DatabaseServiceEventListener listener : this.listenerRegistry.getRegisteredListeners()){
				listener.onPreCodingSchemeInsert(
						new PreCodingSchemeInsertEvent(
								codingScheme));
			}
	}
	
	protected void firePostCodingSchemeInsertEvent(
			CodingScheme codingScheme) throws CodingSchemeAlreadyLoadedException{
			for(DatabaseServiceEventListener listener : this.listenerRegistry.getRegisteredListeners()){
				listener.onPostCodingSchemeInsert(
						new PostCodingSchemeInsertEvent(
								codingScheme));
		}
	}
	
	protected void firePreEntityInsertEvent(EntityInsertOrRemoveEvent entityInsertEvent){
			for(DatabaseServiceEventListener listener : this.listenerRegistry.getRegisteredListeners()){
				listener.onPreEntityInsert(entityInsertEvent);
			}
	}
	
	protected void firePostEntityInsertEvent(EntityInsertOrRemoveEvent entityInsertEvent){
			for(DatabaseServiceEventListener listener : this.listenerRegistry.getRegisteredListeners()){
				listener.onPostEntityInsert(entityInsertEvent);
			}
	}
	
	protected void firePreBatchEntityInsertEvent(EntityBatchInsertEvent entityInsertEvent){
			for(DatabaseServiceEventListener listener : this.listenerRegistry.getRegisteredListeners()){
				listener.onPreBatchEntityInsert(entityInsertEvent);
			}
	}
	
	protected void firePostBatchEntityInsertEvent(EntityBatchInsertEvent entityInsertEvent){
			for(DatabaseServiceEventListener listener : this.listenerRegistry.getRegisteredListeners()){
				listener.onPostBatchEntityInsert(entityInsertEvent);
			}
	}
	
	protected void firePreBatchAssociationInsertEvent(AssociationBatchInsertEvent assocInsertEvent){
			for(DatabaseServiceEventListener listener : this.listenerRegistry.getRegisteredListeners()){
				listener.onPreBatchAssociationInsert(assocInsertEvent);
			}
	}

	protected void firePostPropertyInsertEvent(PropertyUpdateEvent propertyInsertEvent){
		for(DatabaseServiceEventListener listener : this.listenerRegistry.getRegisteredListeners()){
			listener.onPostPropertyInsert(propertyInsertEvent);
		}
	}
	
	protected void firePropertyUpdateEvent(
			PropertyUpdateEvent event) {
			for(DatabaseServiceEventListener listener : this.listenerRegistry.getRegisteredListeners()){
				listener.onPropertyUpdate(event);
			}
	}
	
	protected void firePostPropertyRemoveEvent(PropertyUpdateEvent propertyRemoveEvent){
		for(DatabaseServiceEventListener listener : this.listenerRegistry.getRegisteredListeners()){
			listener.onPostPropertyRemove(propertyRemoveEvent);
		}
	}

	protected void fireEntityUpdateEvent(EntityUpdateEvent entityUpdateEvent) {
			for(DatabaseServiceEventListener listener : this.listenerRegistry.getRegisteredListeners()){
				listener.onEntityUpdate(entityUpdateEvent);
			}
	}
	
	protected void firePreEntityRemoveEvent(EntityInsertOrRemoveEvent entityRemoveEvent){
			for(DatabaseServiceEventListener listener : this.listenerRegistry.getRegisteredListeners()){
				listener.onPreEntityRemove(entityRemoveEvent);
			}
	}
	
	protected void firePostEntityRemoveEvent(EntityInsertOrRemoveEvent entityRemoveEvent){
			for(DatabaseServiceEventListener listener : this.listenerRegistry.getRegisteredListeners()){
				listener.onPostEntityRemove(entityRemoveEvent);
			}
	}
	
	protected void fireEntityReviseEvent(EntityReviseEvent entityReviseEvent){
		for(DatabaseServiceEventListener listener : this.listenerRegistry.getRegisteredListeners()){
			listener.onEntityReviseEvent(entityReviseEvent);
		}
}

	public void setListenerRegistry(ListenerRegistry listenerRegistry) {
		this.listenerRegistry = listenerRegistry;
	}

	public ListenerRegistry getListenerRegistry() {
		return listenerRegistry;
	}
}
