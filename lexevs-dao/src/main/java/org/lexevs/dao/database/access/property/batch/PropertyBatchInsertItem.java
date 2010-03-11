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
package org.lexevs.dao.database.access.property.batch;

import org.LexGrid.commonTypes.Property;

/**
 * The Class PropertyBatchInsertItem.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PropertyBatchInsertItem {

	/** The parent id. */
	private String parentId;
	
	/** The property. */
	private Property property;
	
	/**
	 * Instantiates a new property batch insert item.
	 */
	public PropertyBatchInsertItem(){
		super();
	}
	
	/**
	 * Instantiates a new property batch insert item.
	 * 
	 * @param parentId the parent id
	 * @param property the property
	 */
	public PropertyBatchInsertItem(String parentId, Property property) {
		super();
		this.parentId = parentId;
		this.property = property;
	}

	/**
	 * Gets the parent id.
	 * 
	 * @return the parent id
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * Sets the parent id.
	 * 
	 * @param parentId the new parent id
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * Gets the property.
	 * 
	 * @return the property
	 */
	public Property getProperty() {
		return property;
	}
	
	/**
	 * Sets the property.
	 * 
	 * @param property the new property
	 */
	public void setProperty(Property property) {
		this.property = property;
	}
}
