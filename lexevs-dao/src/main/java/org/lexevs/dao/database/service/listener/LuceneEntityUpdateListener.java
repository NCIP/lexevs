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

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.dao.database.service.event.entity.EntityUpdateEvent;
import org.lexevs.dao.index.service.IndexServiceManager;
import org.lexevs.dao.index.service.entity.EntityIndexService;
import org.lexevs.dao.index.service.search.SourceAssertedValueSetSearchIndexService;
import org.lexevs.locator.LexEvsServiceLocator;

/**
 * The listener interface for receiving luceneEntityUpdate events.
 * The class that is interested in processing a luceneEntityUpdate
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addLuceneEntityUpdateListener<code> method. When
 * the luceneEntityUpdate event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see LuceneEntityUpdateEvent
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LuceneEntityUpdateListener extends DefaultServiceEventListener {

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.listener.DefaultServiceEventListener#onEntityUpdate(org.lexevs.dao.database.service.event.entity.EntityUpdateEvent)
	 */
	@Override
	public boolean onEntityUpdate(EntityUpdateEvent event) {
		IndexServiceManager indexServiceManager = LexEvsServiceLocator.getInstance().getIndexServiceManager();
		EntityIndexService entityIndexService = indexServiceManager.getEntityIndexService();
		SourceAssertedValueSetSearchIndexService vsIndexSvc = indexServiceManager.
				getAssertedValueSetIndexService();
		
		AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
		ref.setCodingSchemeURN(event.getCodingSchemeUri());
		ref.setCodingSchemeVersion(event.getCodingSchemeVersion());

		if (entityIndexService.doesIndexExist(ref)) {
			EntityService entityService = LexEvsServiceLocator.getInstance()
					.getDatabaseServiceManager().getEntityService();
			
			Entity updatedEntity = entityService.getEntity(event
					.getCodingSchemeUri(), event.getCodingSchemeVersion(),
					event.getOriginalEntity().getEntityCode(), event
							.getOriginalEntity().getEntityCodeNamespace());

			entityIndexService.updateIndexForEntity(event.getCodingSchemeUri(),
					event.getCodingSchemeVersion(), updatedEntity);
			
		}
		
		
		return true;
	}
}