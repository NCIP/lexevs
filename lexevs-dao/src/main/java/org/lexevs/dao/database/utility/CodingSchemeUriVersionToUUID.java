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
package org.lexevs.dao.database.utility;

import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;

/**
 * The Class CodingSchemeUriVersionToUUID.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CodingSchemeUriVersionToUUID implements CodingSchemeIdMapper {
	
	/** The coding scheme dao. */
	private CodingSchemeDao codingSchemeDao;

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.utility.CodingSchemeIdMapper#mapCodingSchemeUriAndVersionToUUID(java.lang.String, java.lang.String)
	 */
	public String mapCodingSchemeUriAndVersionToUUID(String codingSchemeUri, String codingSchemeVersion){
		return codingSchemeDao.getCodingSchemeUIdByUriAndVersion(codingSchemeUri, codingSchemeVersion);
	}

	/**
	 * Gets the coding scheme dao.
	 * 
	 * @return the coding scheme dao
	 */
	public CodingSchemeDao getCodingSchemeDao() {
		return codingSchemeDao;
	}

	/**
	 * Sets the coding scheme dao.
	 * 
	 * @param codingSchemeDao the new coding scheme dao
	 */
	public void setCodingSchemeDao(CodingSchemeDao codingSchemeDao) {
		this.codingSchemeDao = codingSchemeDao;
	}
}
