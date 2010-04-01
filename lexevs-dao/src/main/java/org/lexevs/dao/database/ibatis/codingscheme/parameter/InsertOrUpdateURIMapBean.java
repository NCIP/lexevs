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

import org.LexGrid.naming.URIMap;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

/**
 * The Class InsertURIMapBean.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class InsertOrUpdateURIMapBean extends IdableParameterBean{

	/** The coding scheme id. */
	private String codingSchemeId;
	
	/** The supported attribute tag. */
	private String supportedAttributeTag;
	
	/** The uri map. */
	private URIMap uriMap;
	
	/**
	 * Sets the supported attribute tag.
	 * 
	 * @param supportedAttributeTag the new supported attribute tag
	 */
	public void setSupportedAttributeTag(String supportedAttributeTag) {
		this.supportedAttributeTag = supportedAttributeTag;
	}

	/**
	 * Gets the supported attribute tag.
	 * 
	 * @return the supported attribute tag
	 */
	public String getSupportedAttributeTag() {
		return supportedAttributeTag;
	}

	/**
	 * Sets the uri map.
	 * 
	 * @param uriMap the new uri map
	 */
	public void setUriMap(URIMap uriMap) {
		this.uriMap = uriMap;
	}

	/**
	 * Gets the uri map.
	 * 
	 * @return the uri map
	 */
	public URIMap getUriMap() {
		return uriMap;
	}

	/**
	 * Sets the coding scheme id.
	 * 
	 * @param codingSchemeId the new coding scheme id
	 */
	public void setCodingSchemeId(String codingSchemeId) {
		this.codingSchemeId = codingSchemeId;
	}

	/**
	 * Gets the coding scheme id.
	 * 
	 * @return the coding scheme id
	 */
	public String getCodingSchemeId() {
		return codingSchemeId;
	}
}
