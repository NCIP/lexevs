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
import org.lexevs.dao.database.service.event.property.PropertyUpdateEvent;
import org.lexevs.dao.index.service.IndexServiceManager;
import org.lexevs.dao.index.service.entity.EntityIndexService;
import org.lexevs.dao.index.service.search.SourceAssertedValueSetSearchIndexService;
import org.lexevs.locator.LexEvsServiceLocator;

/**
 * The listener interface for receiving luceneEntityPropertyInsert events.
 * The class that is interested in processing a luceneEntityPropertyInsert
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addLuceneEntityPropertyInsertListener<code> method. When
 * the luceneEntityPropertyInsert event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see LuceneEntityPropertyInsertEvent
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LuceneEntityPropertyInsertListener extends
		DefaultServiceEventListener {

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.listener.DefaultServiceEventListener#onPostPropertyInsert(org.lexevs.dao.database.service.event.property.PropertyUpdateEvent)
	 */
	@Override
	public boolean onPostPropertyInsert(PropertyUpdateEvent event) {

		if (event != null && event.getEntity() != null) {

			IndexServiceManager indexServiceManager = LexEvsServiceLocator
					.getInstance().getIndexServiceManager();
			EntityIndexService entityIndexService = indexServiceManager
					.getEntityIndexService();
			SourceAssertedValueSetSearchIndexService vsIndexSvc = indexServiceManager.
					getAssertedValueSetIndexService();

			AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
			ref.setCodingSchemeURN(event.getCodingSchemeUri());
			ref.setCodingSchemeVersion(event.getCodingSchemeVersion());

			if (entityIndexService.doesIndexExist(ref)) {
				entityIndexService.updateIndexForEntity(event
						.getCodingSchemeUri(), event.getCodingSchemeVersion(),
						event.getEntity());
			}
			if(vsIndexSvc.doesIndexExist(ref)){
				vsIndexSvc.updateIndexForEntity(event
						.getCodingSchemeUri(), event.getCodingSchemeVersion(),
						event.getEntity());
			}
		}

		return true;
	}

}