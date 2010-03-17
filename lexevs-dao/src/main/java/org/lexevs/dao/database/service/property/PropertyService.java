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
package org.lexevs.dao.database.service.property;

import java.util.List;

import org.LexGrid.commonTypes.Property;

/**
 * The Interface PropertyService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface PropertyService {
	
	/**
	 * Insert entity property.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param entityCode the entity code
	 * @param entityCodeNamespace the entity code namespace
	 * @param property the property
	 */
	public void insertEntityProperty(
			String codingSchemeUri, 
			String version, 
			String entityCode,
			String entityCodeNamespace,
			Property property);
	
	/**
	 * Insert batch entity properties.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param entityCode the entity code
	 * @param entityCodeNamespace the entity code namespace
	 * @param batch the batch
	 */
	public void insertBatchEntityProperties(
			String codingSchemeUri, 
			String version, 
			String entityCode,
			String entityCodeNamespace,
			List<Property> batch);

	
	/**
	 * Update entity property.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param entityCode the entity code
	 * @param entityCodeNamespace the entity code namespace
	 * @param propertyId the property id
	 * @param property the property
	 */
	public void updateEntityProperty(
			String codingSchemeUri, 
			String version, 
			String entityCode,
			String entityCodeNamespace,
			String propertyId,
			Property property);
}
