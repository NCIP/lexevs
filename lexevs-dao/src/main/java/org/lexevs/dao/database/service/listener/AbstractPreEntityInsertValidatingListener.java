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
package org.lexevs.dao.database.service.listener;

import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.service.event.entity.EntityBatchInsertEvent;
import org.lexevs.dao.database.service.event.entity.EntityInsertOrRemoveEvent;


/**
 * The listener interface for receiving abstractPreEntityInsertValidating events.
 * The class that is interested in processing a abstractPreEntityInsertValidating
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addAbstractPreEntityInsertValidatingListener<code> method. When
 * the abstractPreEntityInsertValidating event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see AbstractPreEntityInsertValidatingEvent
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractPreEntityInsertValidatingListener extends DefaultServiceEventListener{

	/**
	 * Do validate.
	 * 
	 * @param uri the uri
	 * @param version the version
	 * @param entity the entity
	 * 
	 * @return true, if successful
	 */
	protected abstract boolean doValidate(String uri, String version, Entity entity);

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.listener.DefaultServiceEventListener#onPreBatchEntityInsert(org.lexevs.dao.database.service.event.entity.EntityBatchInsertEvent)
	 */
	@Override
	public boolean onPreBatchEntityInsert(EntityBatchInsertEvent event) {
		if(event == null || event.getEntities() == null) {return true;}
		
		for(Entity entity : event.getEntities()) {
			this.doValidate(
					event.getCodingSchemeUri(),
					event.getVersion(),
					entity);
		}
		
		return true;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.listener.DefaultServiceEventListener#onPreEntityInsert(org.lexevs.dao.database.service.event.entity.EntityInsertOrRemoveEvent)
	 */
	@Override
	public boolean onPreEntityInsert(EntityInsertOrRemoveEvent event) {
		if(event == null || event.getEntity() == null) {return true;}
	
		return this.doValidate(
				event.getCodingSchemeUri(),
				event.getVersion(),
				event.getEntity());
	}
}