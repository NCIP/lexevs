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

import org.lexevs.dao.database.service.event.codingscheme.CodingSchemeUpdateEvent;
import org.lexevs.dao.database.service.event.codingscheme.PostCodingSchemeInsertEvent;
import org.lexevs.dao.database.service.event.codingscheme.PreCodingSchemeInsertEvent;
import org.lexevs.dao.database.service.event.entity.EntityBatchInsertEvent;
import org.lexevs.dao.database.service.event.entity.EntityInsertOrRemoveEvent;
import org.lexevs.dao.database.service.event.entity.EntityUpdateEvent;
import org.lexevs.dao.database.service.event.property.PropertyUpdateEvent;
import org.lexevs.dao.database.service.event.revision.EntityReviseEvent;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;

/**
 * The listener interface for receiving databaseServiceEvent events.
 * The class that is interested in processing a databaseServiceEvent
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addDatabaseServiceEventListener<code> method. When
 * the databaseServiceEvent event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see DatabaseServiceEventEvent
 */
public interface DatabaseServiceEventListener {
	
	public boolean isActive();
	
	public void setActive(boolean isActive);

	/**
	 * On coding scheme update.
	 * 
	 * @param event the event
	 * 
	 * @return true, if successful
	 */
	public boolean onCodingSchemeUpdate(CodingSchemeUpdateEvent event);
	
	/**
	 * On coding scheme insert.
	 * 
	 * @param event the event
	 * 
	 * @return true, if successful
	 * 
	 * @throws CodingSchemeAlreadyLoadedException the coding scheme already loaded exception
	 */
	public boolean onPreCodingSchemeInsert(PreCodingSchemeInsertEvent event) throws CodingSchemeAlreadyLoadedException;
	
	public boolean onPostCodingSchemeInsert(PostCodingSchemeInsertEvent event);
	
	public boolean onEntityUpdate(EntityUpdateEvent event);
	
	public boolean onPropertyUpdate(PropertyUpdateEvent event);
	
	public boolean onPreEntityInsert(EntityInsertOrRemoveEvent event);
	
	public boolean onPostEntityInsert(EntityInsertOrRemoveEvent event);
	
	public boolean onPreBatchEntityInsert(EntityBatchInsertEvent event);
	
	public boolean onPostBatchEntityInsert(EntityBatchInsertEvent event);

	public boolean onPreEntityRemove(EntityInsertOrRemoveEvent entityRemoveEvent);
	
	public boolean onPostEntityRemove(EntityInsertOrRemoveEvent entityRemoveEvent);
	
	public boolean onEntityReviseEvent(EntityReviseEvent reviseEvent);
}
