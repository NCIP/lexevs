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
package org.lexevs.dao.database.ibatis.valuesets.parameter;

import java.util.List;

import org.LexGrid.valueSets.ValueSetDefinition;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

/**
 * The Class InsertValueSetDefinitionBean.
 * 
 * @author <a href="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</a>
 */
public class InsertValueSetDefinitionBean extends IdableParameterBean {

	/** The value set definition. */
	private ValueSetDefinition valueSetDefinition;
	
	/** The system release id. */
	private String systemReleaseUId;
	
	private List<InsertOrUpdateValueSetsMultiAttribBean> vsMultiAttribList = null;

	/**
	 * Sets the value set definition.
	 * 
	 * @param valueSetDefinition the new value set definition
	 */
	public void setValueSetDefinition(ValueSetDefinition valueSetDefinition) {
		this.valueSetDefinition = valueSetDefinition;
	}

	/**
	 * Gets the value Set Definition.
	 * 
	 * @return the valueSetDefinition
	 */
	public ValueSetDefinition getValueSetDefinition() {
		return valueSetDefinition;
	}

	/**
	 * Sets the system release id.
	 * 
	 * @param systemReleaseUId the new system release id
	 */
	public void setSystemReleaseUId(String systemReleaseUId) {
		this.systemReleaseUId = systemReleaseUId;
	}

	/**
	 * Gets the system release id.
	 * 
	 * @return the system release id
	 */
	public String getSystemReleaseUId() {
		return systemReleaseUId;
	}

	/**
	 * @return the vsMultiAttribList
	 */
	public List<InsertOrUpdateValueSetsMultiAttribBean> getVsMultiAttribList() {
		return vsMultiAttribList;
	}

	/**
	 * @param vsMultiAttribList the vsMultiAttribList to set
	 */
	public void setVsMultiAttribList(
			List<InsertOrUpdateValueSetsMultiAttribBean> vsMultiAttribList) {
		this.vsMultiAttribList = vsMultiAttribList;
	}
}

