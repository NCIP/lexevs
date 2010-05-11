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
package org.lexevs.dao.database.ibatis.property.parameter;

import org.LexGrid.commonTypes.Property;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

/**
 * The Class InsertPropertyBean.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class InsertOrUpdatePropertyBean extends IdableParameterBean {

	/** The property. */
	private Property property;
	
	/** The entity id. */
	private String parentUId;
	
	/** The reference type. */
	private String parentType;
	
	private Boolean isPreferred;
	
	private Boolean matchIfNoContext;
	
	private String degreeOfFidelity;
	
	private String representationalForm;

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
	
	/**
	 * Gets the entity id.
	 * 
	 * @return the entity id
	 */
	public String getParentUId() {
		return parentUId;
	}
	
	/**
	 * Sets the parent id.
	 * 
	 * @param parentUId the new entity id
	 */
	public void setParentUId(String parentUId) {
		this.parentUId = parentUId;
	}
	
	/**
	 * Sets the parent type.
	 * 
	 * @param parentType the new reference type
	 */
	public void setParentType(String parentType) {
		this.parentType = parentType;
	}
	
	/**
	 * Gets the reference type.
	 * 
	 * @return the reference type
	 */
	public String getParentType() {
		return parentType;
	}

	/**
	 * @return the isPreferred
	 */
	public Boolean getIsPreferred() {
		return isPreferred;
	}

	/**
	 * @param isPreferred the isPreferred to set
	 */
	public void setIsPreferred(Boolean isPreferred) {
		this.isPreferred = isPreferred;
	}

	/**
	 * @return the representationalForm
	 */
	public String getRepresentationalForm() {
		return representationalForm;
	}

	/**
	 * @param representationalForm the representationalForm to set
	 */
	public void setRepresentationalForm(String representationalForm) {
		this.representationalForm = representationalForm;
	}

	/**
	 * @return the matchIfNoContext
	 */
	public Boolean getMatchIfNoContext() {
		return matchIfNoContext;
	}

	/**
	 * @param matchIfNoContext the matchIfNoContext to set
	 */
	public void setMatchIfNoContext(Boolean matchIfNoContext) {
		this.matchIfNoContext = matchIfNoContext;
	}

	/**
	 * @return the degreeOfFidelity
	 */
	public String getDegreeOfFidelity() {
		return degreeOfFidelity;
	}

	/**
	 * @param degreeOfFidelity the degreeOfFidelity to set
	 */
	public void setDegreeOfFidelity(String degreeOfFidelity) {
		this.degreeOfFidelity = degreeOfFidelity;
	}
}

