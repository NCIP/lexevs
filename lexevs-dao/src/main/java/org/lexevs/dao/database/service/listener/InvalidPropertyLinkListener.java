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

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.PropertyLink;


/**
 * The listener interface for receiving invalidPropertyLink events.
 * The class that is interested in processing a invalidPropertyLink
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addInvalidPropertyLinkListener<code> method. When
 * the invalidPropertyLink event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see InvalidPropertyLinkEvent
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class InvalidPropertyLinkListener extends AbstractPreEntityInsertValidatingListener{

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.listener.AbstractPreEntityInsertValidatingListener#doValidate(java.lang.String, java.lang.String, org.LexGrid.concepts.Entity)
	 */
	protected boolean doValidate(String uri, String version, Entity entity) {
		
		Property[] properties = entity.getProperty();
		List<PropertyLink> propLinkList = entity.getPropertyLinkAsReference();
		List<PropertyLink> validList = new ArrayList<PropertyLink>();

		for(PropertyLink propLink : propLinkList) {
			boolean srcFlag = false, tgtFlag = false;
			for (Property property : properties) {
				if (srcFlag == false && property.getPropertyId().equalsIgnoreCase(propLink.getSourceProperty()))
					srcFlag = true;
				if (tgtFlag == false && property.getPropertyId().equalsIgnoreCase(propLink.getTargetProperty()))
					tgtFlag = true;
			}
			if (srcFlag == true && tgtFlag == true)
				validList.add(propLink);
		}

		entity.setPropertyLink(validList);

		return true;
	}
}