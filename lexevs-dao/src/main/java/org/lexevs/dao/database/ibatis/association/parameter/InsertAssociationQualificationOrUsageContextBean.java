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
package org.lexevs.dao.database.ibatis.association.parameter;

import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

/**
 * The Class InsertAssociationQualificationOrUsageContextBean.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class InsertAssociationQualificationOrUsageContextBean extends IdableParameterBean {

	/** The association target id. */
	private String referenceUId;
	
	/** The qualifier name. */
	private String qualifierName;
	
	/** The qualifier value. */
	private String qualifierValue;
	
	/**
	 * Gets the qualifier name.
	 * 
	 * @return the qualifier name
	 */
	public String getQualifierName() {
		return qualifierName;
	}
	
	/**
	 * Sets the qualifier name.
	 * 
	 * @param qualifierName the new qualifier name
	 */
	public void setQualifierName(String qualifierName) {
		this.qualifierName = qualifierName;
	}
	
	/**
	 * Gets the qualifier value.
	 * 
	 * @return the qualifier value
	 */
	public String getQualifierValue() {
		return qualifierValue;
	}
	
	/**
	 * Sets the qualifier value.
	 * 
	 * @param qualifierValue the new qualifier value
	 */
	public void setQualifierValue(String qualifierValue) {
		this.qualifierValue = qualifierValue;
	}

	/**
	 * @return the referenceUId
	 */
	public String getReferenceUId() {
		return referenceUId;
	}

	/**
	 * @param referenceUId the referenceUId to set
	 */
	public void setReferenceUId(String referenceUId) {
		this.referenceUId = referenceUId;
	}
	
	
}
