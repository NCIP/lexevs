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

import org.LexGrid.commonTypes.Property;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.access.property.PropertyDao.PropertyType;
import org.lexevs.dao.database.access.property.batch.PropertyBatchInsertItem;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexgrid.loader.wrappers.CodingSchemeUriVersionPair;
import org.lexgrid.loader.wrappers.ParentIdHolder;

public class EntityPropertyWriter extends AbstractParentIdHolderWriter<Property>{

	@Override
	public void doWrite(final CodingSchemeUriVersionPair codingSchemeId,
			List<ParentIdHolder<Property>> items) {
		final List<PropertyBatchInsertItem> batchList = new ArrayList<PropertyBatchInsertItem>();
		
		for(ParentIdHolder<Property> holder : items){
			batchList.add(new PropertyBatchInsertItem(holder.getParentId(), holder.getItem()));
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
					
					daoManager.getPropertyDao(
							codingSchemeId.getUri(), 
							codingSchemeId.getVersion()).
						insertBatchProperties(codingSchemeIdInDb, PropertyType.ENTITY, batchList);
					
					return null;
				}	
			});
	}
}