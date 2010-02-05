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
package org.lexgrid.loader.data;

import java.lang.reflect.Constructor;

import org.LexGrid.persistence.model.EntityAssnsToEntity;
import org.LexGrid.persistence.model.EntityProperty;
import org.LexGrid.persistence.model.EntityPropertyId;
import org.apache.commons.lang.StringUtils;

/**
 * The Class DataUtils.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DataUtils {

	/**
	 * Deep clone entity property.
	 * 
	 * @param prop the prop
	 * 
	 * @return the entity property
	 * 
	 * @throws Exception the exception
	 */
	public static EntityProperty deepCloneEntityProperty(EntityProperty prop) throws Exception {
		EntityProperty newProp = new EntityProperty();
		EntityPropertyId newPropId = new EntityPropertyId();
		
		newProp.setDegreeOfFidelity(deepCopy(prop.getDegreeOfFidelity()));
		newProp.setEntryStateId(deepCopy(prop.getEntryStateId()));
		newProp.setFormat(deepCopy(prop.getFormat()));
		newProp.setIsActive(deepCopy(prop.getIsActive()));
		newProp.setIsPreferred(deepCopy(prop.getIsPreferred()));
		newProp.setLanguage(deepCopy(prop.getLanguage()));
		newProp.setMatchIfNoContext(deepCopy(prop.getMatchIfNoContext()));
		newProp.setPropertyName(deepCopy(prop.getPropertyName()));
		newProp.setPropertyType(deepCopy(prop.getPropertyType()));
		newProp.setPropertyValue(deepCopy(prop.getPropertyValue()));
		newProp.setRepresentationalForm(deepCopy(prop.getRepresentationalForm()));
		
		newPropId.setCodingSchemeName(deepCopy(prop.getId().getCodingSchemeName()));
		newPropId.setEntityCode(deepCopy(prop.getId().getEntityCode()));
		newPropId.setEntityCodeNamespace(deepCopy(prop.getId().getEntityCodeNamespace()));
		newPropId.setPropertyId(deepCopy(prop.getId().getPropertyId()));
		
		newProp.setId(newPropId);	
		
		return newProp;
	}
	
	
	/**
	 * Deep clone entity assns to entity.
	 * 
	 * @param assoc the assoc
	 * 
	 * @return the entity assns to entity
	 * 
	 * @throws Exception the exception
	 */
	public static EntityAssnsToEntity deepCloneEntityAssnsToEntity(EntityAssnsToEntity assoc) throws Exception {
		EntityAssnsToEntity newAssoc = new EntityAssnsToEntity();
		
		newAssoc.setIsActive(deepCopy(assoc.getIsActive()));
		newAssoc.setIsDefining(deepCopy(assoc.getIsDefining()));
		newAssoc.setIsInferred(deepCopy(assoc.getIsInferred()));
		newAssoc.setMultiAttributesKey(deepCopy(assoc.getMultiAttributesKey()));	
		
		newAssoc.setCodingSchemeName(deepCopy(assoc.getCodingSchemeName()));
		newAssoc.setEntityCode(deepCopy(assoc.getEntityCode()));
		newAssoc.setEntityCodeNamespace(deepCopy(assoc.getEntityCodeNamespace()));
		newAssoc.setContainerName(deepCopy(assoc.getContainerName()));
		newAssoc.setSourceEntityCode(deepCopy(assoc.getSourceEntityCode()));
		newAssoc.setSourceEntityCodeNamespace(deepCopy(assoc.getSourceEntityCodeNamespace()));
		newAssoc.setTargetEntityCode(deepCopy(assoc.getTargetEntityCode()));
		newAssoc.setTargetEntityCodeNamespace(deepCopy(assoc.getTargetEntityCodeNamespace()));
		
		return newAssoc;
	}
	
	
	/**
	 * Makes a deep copy of an Object -- assumes the Object to be copied has a Constructor
	 * that accepts a String
	 * 
	 * Example: String stringCopy = new String(String originalString);.
	 * 
	 * @param value the value
	 * 
	 * @return the T
	 * 
	 * @throws Exception the exception
	 */
	public static <T> T deepCopy(T value) throws Exception {
		if(value == null){
			return null;
		}
		Constructor constructor = value.getClass().getConstructor(String.class);
		return (T)constructor.newInstance(value.toString());	
	}
	
	public static String adjustNonNullValue(String value){
		if(StringUtils.isEmpty(value)){
			return " ";
		} else {
			return value;
		}
	}
}
