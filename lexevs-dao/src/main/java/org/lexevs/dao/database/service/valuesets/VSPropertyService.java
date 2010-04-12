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
package org.lexevs.dao.database.service.valuesets;

import org.LexGrid.commonTypes.Property;

/**
 * The Interface VSPropertyService.
 * 
 * @author <a href="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</a>
 */
public interface VSPropertyService {
	
	/**
	 * Insert value set definition property.
	 * 
	 * @param valueSetDefinitionUri the value set definition URI
	 * @param property the property
	 */
	public void insertValueSetDefinitionProperty(
			String valueSetDefinitionUri, 
			Property property);
	
	/**
	 * Insert pick list definition property.
	 * 
	 * @param pickListId the pick list id
	 * @param property the property
	 */
	public void insertPickListDefinitionProperty(
			String pickListId, 
			Property property);
	
	/**
	 * Insert pick list entry node property.
	 * 
	 * @param pickListId the pick list id
	 * @param pickListEntryNodeId the pick list entry id
	 * @param property the property
	 */
	public void insertPickListEntryNodeProperty(
			String pickListId,
			String pickListEntryNodeId, 
			Property property);
	
	
	/**
	 * Update value set definition property.
	 * 
	 * @param valueSetDefinitionUri the value set definition URI
	 * @param propertyId the property id
	 * @param property the property
	 */
	public void updateValueSetDefinitionProperty(
			String valueSetDefinitionUri, 
			String propertyId,
			Property property);
	
	/**
	 * Update pick list definition property.
	 * 
	 * @param pickListId the pick list id
	 * @param propertyId the property id
	 * @param property the property
	 */
	public void updatePickListDefinitionProperty(
			String pickListId, 
			String propertyId,
			Property property);
	
	/**
	 * Update pick list entry node property.
	 * 
	 * @param pickListId the pick list id
	 * @param pickListEntryNodeId the pick list entry id
	 * @param propertyId the property id
	 * @param property the property
	 */
	public void updatePickListEntryNodeProperty(
			String pickListId,
			String pickListEntryNodeId, 
			String propertyId,
			Property property);
}
