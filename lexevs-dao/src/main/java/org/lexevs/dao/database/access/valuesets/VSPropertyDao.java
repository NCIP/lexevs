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
package org.lexevs.dao.database.access.valuesets;

import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;

/**
 * The Interface VSPropertyDao.
 * 
 * @author <a href="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</a>
 */
public interface VSPropertyDao extends LexGridSchemaVersionAwareDao {
	
	/**
	 * The Enum ReferenceType.
	 * 
	 * @author <a href="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</a>
	 */
	public enum ReferenceType {
		/** The VALUESETDEFINITION. */
		VALUESETDEFINITION,
		/** The DEFINITIONENTRY. */
		DEFINITIONENTRY,
		/** The PICKLISTDEFINITION. */
		PICKLISTDEFINITION,
		/** The PICKLISTENTRY. */
		PICKLISTENTRY}
	
	/**
	 * Gets the all properties of parent.
	 * 
	 * @param parentGuid the parent GUID
	 * @param type the type
	 * 
	 * @return the all properties of parent
	 */
	public List<Property> getAllPropertiesOfParent(String parentGuid, ReferenceType type);
	
	public List<Property> getAllHistoryPropertiesOfParentByRevisionGuid(String parentGuid, String revisionGuid, ReferenceType type);


	/**
	 * Insert property qualifier.
	 * 
	 * @param propertyGuid the property GUID
	 * @param qualifier the qualifier
	 */
	public void insertPropertyQualifier(String propertyGuid, PropertyQualifier qualifier);
	
	/**
	 * Insert property.
	 * 
	 * @param parentGuid the parent GUID
	 * @param type the type
	 * @param property the property
	 * 
	 * @return the string
	 */
	public String insertProperty(String parentGuid, ReferenceType type, Property property);
	
	public String insertHistoryProperty(String parentGuid, String propertyGuid, ReferenceType type, Property property);
	
	/**
	 * Insert property source.
	 * 
	 * @param propertyGuid the property GUID
	 * @param source the source
	 */
	public void insertPropertySource(String propertyGuid, Source source);
	
	/**
	 * Insert property usage context.
	 * 
	 * @param propertyGuid the property GUID
	 * @param usageContext the usage context
	 */
	public void insertPropertyUsageContext(String propertyGuid, String usageContext);
	
	/**
	 * Delete all DefinitionEntity properties of value set definition.
	 * 
	 * @param valueSetDefinitionURI the URI of value set definition
	 */
	public void deleteAllDefinitionEntityPropertiesOfValueSetDefinition(
			String valueSetDefinitionURI);
	
	/**
	 * Delete all PickListEntity properties of pick list definition.
	 * 
	 * @param pickListId the id of pick list definition
	 */
	public void deleteAllPickListEntityPropertiesOfPickListDefinition(
			String pickListId);
	
	/**
	 * Delete all properties of a value set definition.
	 * 
	 * @param valueSetDefinitionURI the URI of value set definition
	 */
	public void deleteAllValueSetDefinitionProperties(
			String valueSetDefinitionURI);
	
	
	/**
	 * Delete all properties of a pick list definition.
	 * 
	 * @param pickListId the id of pick list definition
	 */
	public void deleteAllPickListDefinitionProperties(
			String pickListId);
	
	/**
	 * Update property.
	 * 
	 * @param parentGuid the parent GUID
	 * @param propertyGuid the property GUID
	 * @param type the type
	 * @param property the property
	 */
	public String updateProperty(
			String parentGuid,
			String propertyGuid,
			ReferenceType type,
			Property property);

	String getPropertyGuidFromParentGuidAndPropertyId(String parentGuid,
			String propertyId);

	public void deletePropertyByUId(String propertyUId);

	public String updateVersionableAttributes(String valueSetDefUId,
			String propertyUId, ReferenceType valuesetdefinition,
			Property property);
	
	public void deleteAllPickListEntryNodeProperties(String pickListEntryNodeUId);

	public String getLatestRevision(String propertyUId);
}

