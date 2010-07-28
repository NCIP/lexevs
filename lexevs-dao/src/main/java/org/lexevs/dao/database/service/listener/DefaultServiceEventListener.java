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
package org.lexevs.dao.database.service.listener;
import org.lexevs.dao.database.service.event.DatabaseServiceEventListener;
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
 * The listener interface for receiving defaultServiceEvent events.
 * The class that is interested in processing a defaultServiceEvent
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addDefaultServiceEventListener<code> method. When
 * the defaultServiceEvent event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see DefaultServiceEventEvent
 */
public class DefaultServiceEventListener implements DatabaseServiceEventListener {
	
	private boolean isActive = true;

	@Override
	public boolean onEntityReviseEvent(EntityReviseEvent reviseEvent) {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.event.DatabaseServiceEventListener#onCodingSchemeUpdate(org.lexevs.dao.database.service.event.codingscheme.CodingSchemeUpdateEvent)
	 */
	public boolean onCodingSchemeUpdate(CodingSchemeUpdateEvent event) {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.event.DatabaseServiceEventListener#onCodingSchemeInsert(org.lexevs.dao.database.service.event.codingscheme.CodingSchemeInsertEvent)
	 */
	public boolean onPreCodingSchemeInsert(PreCodingSchemeInsertEvent event) throws CodingSchemeAlreadyLoadedException {
		return true;
	}
	
	public boolean onPostCodingSchemeInsert(PostCodingSchemeInsertEvent event) {
		return true;
	}
	
	public boolean onEntityUpdate(EntityUpdateEvent event) {
		return true;
	}

	@Override
	public boolean onPostPropertyInsert(PropertyUpdateEvent event) {
		return true;
	}

	@Override
	public boolean onPropertyUpdate(PropertyUpdateEvent event) {
		return true;
	}

	@Override
	public boolean onPostPropertyRemove(PropertyUpdateEvent event) {
		return true;
	}
	
	@Override
	public boolean onPreEntityInsert(EntityInsertOrRemoveEvent entityInsertEvent) {
		return true;
	}
	
	public boolean onPostEntityInsert(EntityInsertOrRemoveEvent entityInsertEvent) {
		return true;
	}

	@Override
	public boolean onPostEntityRemove(EntityInsertOrRemoveEvent entityRemoveEvent) {
		return true;
	}

	@Override
	public boolean onPreEntityRemove(EntityInsertOrRemoveEvent entityRemoveEvent) {
		return true;
	}

	@Override
	public boolean onPostBatchEntityInsert(EntityBatchInsertEvent event) {
		return true;
	}

	@Override
	public boolean onPreBatchEntityInsert(EntityBatchInsertEvent event) {
		return true;
	}

	@Override
	public boolean isActive() {
		return this.isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}
