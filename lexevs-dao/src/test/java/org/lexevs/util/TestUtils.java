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
package org.lexevs.util;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;

public class TestUtils {

	public static boolean entityContainsPropertyWithValue(Entity entity, String propertyValue) {
		for(Property prop : entity.getAllProperties()) {
			if(prop.getValue().getContent().equals(propertyValue)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean entityContainsPropertyWithId(Entity entity, String propertyId) {
		for(Property prop : entity.getAllProperties()) {
			if(prop.getPropertyId().equals(propertyId)) {
				return true;
			}
		}
		return false;
	}

	public static Property getPropertyWithValue(Entity entity, String propertyValue) {
		for(Property prop : entity.getAllProperties()) {
			if(prop.getValue().getContent().equals(propertyValue)) {
				return prop;
			}
		}
		throw new RuntimeException("Property not found");
	}
	
	public static Property getPropertyWithId(Entity entity, String propertyId) {
		for(Property prop : entity.getAllProperties()) {
			if(prop.getPropertyId().equals(propertyId)) {
				return prop;
			}
		}
		throw new RuntimeException("Property not found");
	}
}