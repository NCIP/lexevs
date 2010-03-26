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

import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.access.entity.EntityDao;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexevs.dao.database.service.event.entity.EntityUpdateEvent;
import org.lexevs.locator.LexEvsServiceLocator;

/**
 * The listener interface for receiving historyTableReplicating events.
 * The class that is interested in processing a historyTableReplicating
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addHistoryTableReplicatingListener<code> method. When
 * the historyTableReplicating event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see HistoryTableReplicatingEvent
 */
public class HistoryTableReplicatingListener extends DefaultServiceEventListener {
	
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.listener.DefaultServiceEventListener#onCodingSchemeUpdate(org.lexevs.dao.database.service.event.codingscheme.CodingSchemeUpdateEvent)
	 */
	@Override
	public boolean onEntityUpdate(final EntityUpdateEvent event) {
		DaoCallbackService callbackService = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getDaoCallbackService();
		callbackService.executeInDaoLayer(new DaoCallback<Object>(){

			@Override
			public Object execute(DaoManager daoManager) {
				String uri = event.getCodingSchemeUri();
				String version = event.getCodingSchemeVersion();
				
				CodingSchemeDao codingSchemeDao = daoManager.getCodingSchemeDao(uri, version);
				String codingSchemeId = codingSchemeDao.getCodingSchemeIdByUriAndVersion(uri, version);
					
				EntityDao entityDao = daoManager.getEntityDao(uri, version);
				String entityId = entityDao.getEntityId(
						codingSchemeId, 
						event.getOriginalEntity().getEntityCode(),
						event.getOriginalEntity().getEntityCodeNamespace());
				
				
				entityDao.insertHistoryEntity(codingSchemeId, entityId, event.getOriginalEntity());
				return null;
			}
			
		});
		
		
		return true;
	}

	
}
