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
package org.lexevs.dao.database.ibatis.codingscheme.parameter;

import java.util.List;

import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

/**
 * The Class InsertOrUpdateCodingSchemeBean.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class InsertOrUpdateCodingSchemeBean extends IdableParameterBean{
	
	/** The coding scheme. */
	private CodingScheme codingScheme;
	
	private List<InsertOrUpdateCodingSchemeMultiAttribBean> csMultiAttribList = null;
	
	/** The system release uid*/
	private String releaseUId = null;
	/**
	 * Sets the coding scheme.
	 * 
	 * @param codingScheme the new coding scheme
	 */
	public void setCodingScheme(CodingScheme codingScheme) {
		this.codingScheme = codingScheme;
	}

	/**
	 * Gets the coding scheme.
	 * 
	 * @return the coding scheme
	 */
	public CodingScheme getCodingScheme() {
		return codingScheme;
	}

	public String getReleaseUId() {
		return releaseUId;
	}

	public void setReleaseUId(String releaseUId) {
		this.releaseUId = releaseUId;
	}

	/**
	 * @return the csMultiAttribList
	 */
	public List<InsertOrUpdateCodingSchemeMultiAttribBean> getCsMultiAttribList() {
		return csMultiAttribList;
	}

	/**
	 * @param csMultiAttribList the csMultiAttribList to set
	 */
	public void setCsMultiAttribList(
			List<InsertOrUpdateCodingSchemeMultiAttribBean> csMultiAttribList) {
		this.csMultiAttribList = csMultiAttribList;
	}
}
