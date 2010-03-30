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
package org.lexevs.system.service;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.system.constants.SystemVariables;
import org.lexevs.system.utility.MyClassLoader;

/**
 * The Interface SystemResourceService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface SystemResourceService {
	
	/**
	 * Gets the class loader.
	 * 
	 * @return the class loader
	 */
	public MyClassLoader getClassLoader();

	/**
	 * Creates the new tables for load.
	 * 
	 * @return the string
	 */
	public String createNewTablesForLoad();
	
	/**
	 * Adds the coding scheme resource to system.
	 * 
	 * @param uri the uri
	 * @param version the version
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public void addCodingSchemeResourceToSystem(String uri, String version) throws LBParameterException;
	
	/**
	 * Adds the coding scheme resource to system.
	 * 
	 * @param codingScheme the coding scheme
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public void addCodingSchemeResourceToSystem(CodingScheme codingScheme) throws LBParameterException;
	
	/**
	 * Removes the coding scheme resource from system.
	 * 
	 * @param uri the uri
	 * @param version the version
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public void removeCodingSchemeResourceFromSystem(String uri, String version) throws LBParameterException;
	
	/**
	 * Removes the non coding scheme resource from system.
	 * 
	 * @param uri the uri
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public void removeNonCodingSchemeResourceFromSystem(String uri) throws LBParameterException;;
	
	/**
	 * Gets the internal version string for tag.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param tag the tag
	 * 
	 * @return the internal version string for tag
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public String getInternalVersionStringForTag(String codingSchemeName, String tag) throws LBParameterException ;
	
	/**
	 * Gets the internal coding scheme name for user coding scheme name.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param version the version
	 * 
	 * @return the internal coding scheme name for user coding scheme name
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public String getInternalCodingSchemeNameForUserCodingSchemeName(String codingSchemeName, String version) throws LBParameterException ;
	
	/**
	 * Gets the uri for user coding scheme name.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * 
	 * @return the uri for user coding scheme name
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public String getUriForUserCodingSchemeName(String codingSchemeName) throws LBParameterException ;

	/**
	 * Contains coding scheme resource.
	 * 
	 * @param uri the uri
	 * @param version the version
	 * 
	 * @return true, if successful
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public boolean containsCodingSchemeResource(String uri, String version) throws LBParameterException;
	
	/**
	 * Contains non coding scheme resource.
	 * 
	 * @param uri the uri
	 * 
	 * @return true, if successful
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public boolean containsNonCodingSchemeResource(String uri) throws LBParameterException;
	
	/**
	 * Update coding scheme resource tag.
	 * 
	 * @param codingScheme the coding scheme
	 * @param newTag the new tag
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public void updateCodingSchemeResourceTag(AbsoluteCodingSchemeVersionReference codingScheme, String newTag) throws LBParameterException;

	/**
	 * Update non coding scheme resource tag.
	 * 
	 * @param uri the uri
	 * @param newTag the new tag
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public void updateNonCodingSchemeResourceTag(String uri, String newTag) throws LBParameterException;
	
	/**
	 * Update coding scheme resource status.
	 * 
	 * @param codingScheme the coding scheme
	 * @param status the status
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public void updateCodingSchemeResourceStatus(AbsoluteCodingSchemeVersionReference codingScheme, CodingSchemeVersionStatus status) throws LBParameterException;
	
	/**
	 * Update non coding scheme resource status.
	 * 
	 * @param uri the uri
	 * @param status the status
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public void updateNonCodingSchemeResourceStatus(String uri, CodingSchemeVersionStatus status) throws LBParameterException;

	public SystemVariables getSystemVariables();
}


