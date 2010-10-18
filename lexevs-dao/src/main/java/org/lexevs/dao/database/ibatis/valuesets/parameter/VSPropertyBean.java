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
package org.lexevs.dao.database.ibatis.valuesets.parameter;

import org.LexGrid.commonTypes.Property;

public class VSPropertyBean extends org.LexGrid.commonTypes.Versionable 
implements java.io.Serializable{

/**
	 * 
	 */
private static final long serialVersionUID = 1L;
	
	private String vsPropertyGuid;
	
	private Property property;

	/**
	 * @return the vsPropertyGuid
	 */
	public String getVsPropertyGuid() {
		return vsPropertyGuid;
	}

	/**
	 * @param vsPropertyGuid the vsPropertyGuid to set
	 */
	public void setVsPropertyGuid(String vsPropertyGuid) {
		this.vsPropertyGuid = vsPropertyGuid;
	}

	/**
	 * @return the property
	 */
	public Property getProperty() {
		return property;
	}

	/**
	 * @param property the property to set
	 */
	public void setProperty(Property property) {
		this.property = property;
	}
}