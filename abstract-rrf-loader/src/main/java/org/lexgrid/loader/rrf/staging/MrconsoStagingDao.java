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
package org.lexgrid.loader.rrf.staging;

import java.util.List;

import org.lexgrid.loader.rrf.staging.model.CodeSabPair;
import org.lexgrid.loader.wrappers.CodeCodingSchemePair;

/**
 * The Interface MrconsoStagingDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface MrconsoStagingDao {

	/**
	 * Gets the code and sab.
	 * 
	 * @param cui the cui
	 * @param aui the aui
	 * 
	 * @return the code and sab
	 */
	public CodeSabPair getCodeAndSab(String cui, String aui);
	
	/**
	 * Gets the code and coding scheme.
	 * 
	 * @param cui the cui
	 * @param aui the aui
	 * 
	 * @return the code and coding scheme
	 */
	public CodeCodingSchemePair getCodeAndCodingScheme(String cui, String aui);
	
	/**
	 * Gets the codes.
	 * 
	 * @param cui the cui
	 * @param sab the sab
	 * 
	 * @return the codes
	 */
	public List<String> getCodes(String cui, String sab);
	
	/**
	 * Gets the code from aui.
	 * 
	 * @param aui the aui
	 * 
	 * @return the code from aui
	 */
	public String getCodeFromAui(String aui);
	
	/**
	 * Gets the cui from aui and sab.
	 * 
	 * @param aui the aui
	 * @param sab the sab
	 * 
	 * @return the cui from aui and sab
	 */
	public String getCuiFromAuiAndSab(String aui, String sab);
	
	public String getCuiFromAui(String aui);
	
	public List<String> getCuisFromCode(String code);
	
	public List<String> getCodesFromCui(String cui);
}