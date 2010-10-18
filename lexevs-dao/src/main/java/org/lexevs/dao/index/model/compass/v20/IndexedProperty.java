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
package org.lexevs.dao.index.model.compass.v20;

import java.util.ArrayList;
import java.util.List;

import org.compass.annotations.Reverse;
import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableMetaData;
import org.compass.annotations.SearchableMetaDatas;
import org.compass.annotations.SearchableProperty;
import org.lexevs.dao.index.model.IndexableResource;

/**
 * The Class IndexedProperty.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Searchable
public class IndexedProperty implements IndexableResource<Object>{
	
	/** The ALIAS. */
	public static String ALIAS = "IndexedProperty";
	
	/** The id. */
	@SearchableId
	private String id;
	
	/** The entity code. */
	@SearchableProperty 
	private String entityCode;
	
	/** The entity code namespace. */
	@SearchableProperty 
	private String entityCodeNamespace;
	
	/** The entity description. */
	@SearchableProperty 
	private String entityDescription;
	
	/** The property type. */
	@SearchableProperty
	private String propertyType;
	
	/** The property name. */
	@SearchableProperty
	private String propertyName;
	
	/** The value. */
	@SearchableProperty
	@SearchableMetaDatas(value={
			@SearchableMetaData(name = "literalValue"), 
			@SearchableMetaData(name = "reverseLiteralValue", reverse=Reverse.STRING)
	})
	private String value;
	
	/** The all property qualifiers. */
	private List<String> allPropertyQualifiers = new ArrayList<String>();
	
	/** The all sources. */
	private List<String> allSources = new ArrayList<String>();

	/** The all usage contexts. */
	private List<String> allUsageContexts = new ArrayList<String>();
	
	/** The all property names. */
	@SearchableProperty
	private List<String> allPropertyNames = new ArrayList<String>();
	
	/** The all property types. */
	private List<String> allPropertyTypes = new ArrayList<String>();
	
	/** The property qualifiers. */
	private List<String> propertyQualifiers = new ArrayList<String>();
	
	/** The sources. */
	private List<String> sources = new ArrayList<String>();

	/** The usage contexts. */
	private List<String> usageContexts = new ArrayList<String>();
	
	/** The score. */
	private float score;
	

	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.model.IndexableResource#getResultValue()
	 */
	public Object getResultValue() {
		return null;
		/*
		CodeToReturn codeToReturn = new CodeToReturn();
		codeToReturn.setCode(this.getEntityCode());
		codeToReturn.setEntityDescription(this.getEntityDescription());
		codeToReturn.setNamespace(this.getEntityCodeNamespace());
		
		return codeToReturn;
		*/
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the entity code.
	 * 
	 * @return the entity code
	 */
	public String getEntityCode() {
		return entityCode;
	}

	/**
	 * Sets the entity code.
	 * 
	 * @param entityCode the new entity code
	 */
	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}

	/**
	 * Gets the entity code namespace.
	 * 
	 * @return the entity code namespace
	 */
	public String getEntityCodeNamespace() {
		return entityCodeNamespace;
	}

	/**
	 * Sets the entity code namespace.
	 * 
	 * @param entityCodeNamespace the new entity code namespace
	 */
	public void setEntityCodeNamespace(String entityCodeNamespace) {
		this.entityCodeNamespace = entityCodeNamespace;
	}

	/**
	 * Gets the entity description.
	 * 
	 * @return the entity description
	 */
	public String getEntityDescription() {
		return entityDescription;
	}

	/**
	 * Sets the entity description.
	 * 
	 * @param entityDescription the new entity description
	 */
	public void setEntityDescription(String entityDescription) {
		this.entityDescription = entityDescription;
	}

	/**
	 * Gets the property type.
	 * 
	 * @return the property type
	 */
	public String getPropertyType() {
		return propertyType;
	}

	/**
	 * Sets the property type.
	 * 
	 * @param propertyType the new property type
	 */
	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

	/**
	 * Gets the property name.
	 * 
	 * @return the property name
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * Sets the property name.
	 * 
	 * @param propertyName the new property name
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 * 
	 * @param value the new value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Gets the all property qualifiers.
	 * 
	 * @return the all property qualifiers
	 */
	public List<String> getAllPropertyQualifiers() {
		return allPropertyQualifiers;
	}

	/**
	 * Sets the all property qualifiers.
	 * 
	 * @param allPropertyQualifiers the new all property qualifiers
	 */
	public void setAllPropertyQualifiers(List<String> allPropertyQualifiers) {
		this.allPropertyQualifiers = allPropertyQualifiers;
	}

	/**
	 * Gets the all sources.
	 * 
	 * @return the all sources
	 */
	public List<String> getAllSources() {
		return allSources;
	}

	/**
	 * Sets the all sources.
	 * 
	 * @param allSources the new all sources
	 */
	public void setAllSources(List<String> allSources) {
		this.allSources = allSources;
	}

	/**
	 * Gets the all usage contexts.
	 * 
	 * @return the all usage contexts
	 */
	public List<String> getAllUsageContexts() {
		return allUsageContexts;
	}

	/**
	 * Sets the all usage contexts.
	 * 
	 * @param allUsageContexts the new all usage contexts
	 */
	public void setAllUsageContexts(List<String> allUsageContexts) {
		this.allUsageContexts = allUsageContexts;
	}

	/**
	 * Gets the all property names.
	 * 
	 * @return the all property names
	 */
	public List<String> getAllPropertyNames() {
		return allPropertyNames;
	}

	/**
	 * Sets the all property names.
	 * 
	 * @param allPropertyNames the new all property names
	 */
	public void setAllPropertyNames(List<String> allPropertyNames) {
		this.allPropertyNames = allPropertyNames;
	}

	/**
	 * Gets the all property types.
	 * 
	 * @return the all property types
	 */
	public List<String> getAllPropertyTypes() {
		return allPropertyTypes;
	}

	/**
	 * Sets the all property types.
	 * 
	 * @param allPropertyTypes the new all property types
	 */
	public void setAllPropertyTypes(List<String> allPropertyTypes) {
		this.allPropertyTypes = allPropertyTypes;
	}

	/**
	 * Gets the property qualifiers.
	 * 
	 * @return the property qualifiers
	 */
	public List<String> getPropertyQualifiers() {
		return propertyQualifiers;
	}

	/**
	 * Sets the property qualifiers.
	 * 
	 * @param propertyQualifiers the new property qualifiers
	 */
	public void setPropertyQualifiers(List<String> propertyQualifiers) {
		this.propertyQualifiers = propertyQualifiers;
	}

	/**
	 * Gets the sources.
	 * 
	 * @return the sources
	 */
	public List<String> getSources() {
		return sources;
	}

	/**
	 * Sets the sources.
	 * 
	 * @param sources the new sources
	 */
	public void setSources(List<String> sources) {
		this.sources = sources;
	}

	/**
	 * Gets the usage contexts.
	 * 
	 * @return the usage contexts
	 */
	public List<String> getUsageContexts() {
		return usageContexts;
	}

	/**
	 * Sets the usage contexts.
	 * 
	 * @param usageContexts the new usage contexts
	 */
	public void setUsageContexts(List<String> usageContexts) {
		this.usageContexts = usageContexts;
	}

	/**
	 * Sets the score.
	 * 
	 * @param score the new score
	 */
	public void setScore(float score) {
		this.score = score;
	}

	/**
	 * Gets the score.
	 * 
	 * @return the score
	 */
	public float getScore() {
		return score;
	}
}