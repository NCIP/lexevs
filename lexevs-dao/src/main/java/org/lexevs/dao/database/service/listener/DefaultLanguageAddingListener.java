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

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.service.event.entity.EntityBatchInsertEvent;
import org.lexevs.dao.database.service.event.entity.EntityInsertOrRemoveEvent;
import org.lexevs.locator.LexEvsServiceLocator;

public class DefaultLanguageAddingListener extends DefaultServiceEventListener {
	
	@Override
	public boolean onPreBatchEntityInsert(EntityBatchInsertEvent event) {
		if(event == null || event.getEntities() == null) {return true;}
		
		CodingScheme cs = getCodingScheme(
				event.getCodingSchemeUri(), 
				event.getVersion());
		
		for(Entity entity : event.getEntities()) {
			this.addDefaultLanguage(
					cs,
					entity);
		}
		
		return true;
	}

	private CodingScheme getCodingScheme(String uri, String version) {
		CodingScheme cs = 
			LexEvsServiceLocator.getInstance().
				getDatabaseServiceManager().
					getCodingSchemeService().
						getCodingSchemeByUriAndVersion(
								uri, 
								version);
		return cs;
	}

	private boolean addDefaultLanguage(CodingScheme cs, Entity entity) {
		String defaultLanguage = cs.getDefaultLanguage();
		if(StringUtils.isNotBlank(defaultLanguage)) {
			for(Property prop : entity.getAllProperties()) {
				if(StringUtils.isBlank(prop.getLanguage())) {
					prop.setLanguage(defaultLanguage);
				}
			}
		}
		return true;
	}

	@Override
	public boolean onPreEntityInsert(EntityInsertOrRemoveEvent event) {
		if(event == null || event.getEntity() == null) {return true;}
	
		CodingScheme cs = getCodingScheme(
				event.getCodingSchemeUri(), 
				event.getVersion());
		
		return this.addDefaultLanguage(
				cs,
				event.getEntity());
	}
}