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

public class DuplicatePropertyIdListener extends AbstractPreEntityInsertValidatingListener {
	@Override
	protected boolean doValidate(String uri, String version, Entity entity) {
		Property[] props = entity.getProperty();
		List<Property> validList = new ArrayList<Property>();
		List<String> propIdList = new ArrayList<String>();

		for (Property prop : props) {
			if (!propIdList.contains(prop.getPropertyId().toLowerCase())) {
				validList.add(prop);
				propIdList.add(prop.getPropertyId().toLowerCase());
			}
		}
		entity.setProperty(validList);

		return true;
	}
}