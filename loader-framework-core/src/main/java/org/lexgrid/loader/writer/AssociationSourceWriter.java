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
package org.lexgrid.loader.writer;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.relations.AssociationSource;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.access.association.batch.AssociationSourceBatchInsertItem;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexgrid.loader.wrappers.CodingSchemeUriVersionPair;
import org.lexgrid.loader.wrappers.ParentIdHolder;

public class AssociationSourceWriter extends AbstractParentIdHolderWriter<AssociationSource>{

	@Override
	public void doWrite(final CodingSchemeUriVersionPair codingSchemeId,
			List<ParentIdHolder<AssociationSource>> items) {
		
		final List<AssociationSourceBatchInsertItem> batch = 
			new ArrayList<AssociationSourceBatchInsertItem>();
		
		for(ParentIdHolder<AssociationSource> holder : items){
			batch.add(new AssociationSourceBatchInsertItem(
					holder.getParentId(), holder.getItem()));
		}
		this.getDatabaseServiceManager().getDaoCallbackService().
			executeInDaoLayer(new DaoCallback<Object>(){

			public Object execute(DaoManager daoManager) {
				String codingSchemeIdInDb = daoManager.getCodingSchemeDao(
						codingSchemeId.getUri(), 
						codingSchemeId.getVersion()).
						getCodingSchemeUIdByUriAndVersion(
								codingSchemeId.getUri(), 
								codingSchemeId.getVersion());
				daoManager.getAssociationDao(
						codingSchemeId.getUri(), 
						codingSchemeId.getVersion())
							.insertBatchAssociationSources(codingSchemeIdInDb, batch);
				return null;
			}
		});	
	}
}