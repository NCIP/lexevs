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
package org.lexevs.cts2.admin.export;

import java.net.URI;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.Exceptions.LBException;

public interface ValueSetExportOperation {

/**
	 * Export Value Set Definition to LexGrid canonical XML format.
	 * 
	 * @param valueSetDefinitionURI
	 * 			value set definition URI
	 * @param valueSetDefinitionVersion
	 * 			value set definition version
	 * @param xmlFullPathName
	 * 			File location (including file name *.xml) to save the definition
	 * @param overwrite
	 * 			True: to override the existing file.
	 * @param failOnAllErrors
	 * 			True: stops exporting if any error.
	 * @throws LBException
	 */
public void exportValueSetDefinition(URI valueSetDefinitionURI, String valueSetDefinitionVersion, String xmlFullPathName, boolean overwrite, boolean failOnAllErrors) throws LBException;
	
	/**
	 * Exports contents of the Value Set Definition using the exporter specified.
	 * 
	 * @param valueSetDefinitionURI 
	 * 			URI of the Value Set Definition to be exported
	 * @param valueSetDefinitionVersion 
	 * 			version of the Value Set Definition to be exported
	 * @param exportDestination 
	 * 			Destination path information for the exported file.
	 * @param exporter 
	 * 			Name of the exporter to use. Use getSupportedExporterNames to get all the exporters supported by this instance of CTS2. 
	 * @return URI of destination if successfully exported.
	 * @throws LBException
	 */
	public URI exportValueSetContents(URI valueSetDefinitionURI, String valueSetDefinitionVersion, URI exportDestination, String exporter) throws LBException;
	
	/**
	 * Exports contents of Value Set Definition as Code System in LexGrid canonical XML format.
	 * 
	 * @param valueSetDefinitionURI
	 * 			value set definition URI
	 * @param valueSetDefinitionVersion
	 * 			value set definition version
	 * @param exportDestination
	 * 			Location (path to the folder withOUT the file name) to save the definition
	 * @param csVersionList 
	 * 			A list of coding scheme URI's and versions to be used.  These will be used only if they are present in
	 * 			the service.  If absent, the most recent version will be used instead.
	 * @param csVersionTag 
	 * 			the tag (e.g "devel", "production", ...) to be used to reconcile coding schemes when more than one is present.
     * @param overwrite
	 * 			True: to override the existing file.
	 * @param failOnAllErrors
	 * 			True: stops exporting if any error.
	 * @return URI of destination if successfully exported.
	 * @throws LBException
	 */
	public URI exportValueSetContents(URI valueSetDefinitionURI, String valueSetDefinitionVersion,  
			URI exportDestination, AbsoluteCodingSchemeVersionReferenceList csVersionList,
            String csVersionTag, boolean overwrite, boolean failOnAllErrors) throws LBException;
	
	/**
	 * Returns list of Exporter names supported by this LexEVS instance.
	 *  
	 * @return List of supported Exporter names
	 * @throws LBException
	 */
	public List<String> getSupportedExporterNames() throws LBException;
}