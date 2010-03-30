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
package org.lexevs.registry.utility;

import java.sql.Timestamp;
import java.util.Date;

import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.dao.database.constants.DatabaseConstants;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry.ResourceType;

/**
 * The Class RegistryUtility.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class RegistryUtility {

	/**
	 * Coding scheme to registry entry.
	 * 
	 * @param codingScheme the coding scheme
	 * 
	 * @return the registry entry
	 */
	public static RegistryEntry codingSchemeToRegistryEntry(CodingScheme codingScheme){
		RegistryEntry entry = new RegistryEntry();
		entry.setResourceType(ResourceType.CODING_SCHEME);
		entry.setDbSchemaDescription(DatabaseConstants.CURRENT_LEXGRID_SCHEMA_DESCRIPTION);
		entry.setDbSchemaVersion(DatabaseConstants.CURRENT_LEXGRID_SCHEMA_VERSION);
		entry.setResourceUri(codingScheme.getCodingSchemeURI());
		entry.setResourceVersion(codingScheme.getRepresentsVersion());
		entry.setLastUpdateDate(new Timestamp(new Date().getTime()));
		
		//TODO: Fix this for multiple sets of tables
		entry.setPrefix("");
		
		return entry;
	}
	
	/**
	 * Coding scheme to registry entry.
	 * 
	 * @param uri the uri
	 * @param version the version
	 * 
	 * @return the registry entry
	 */
	public static RegistryEntry codingSchemeToRegistryEntry(String uri, String version){
		RegistryEntry entry = new RegistryEntry();
		entry.setResourceType(ResourceType.CODING_SCHEME);
		entry.setDbSchemaDescription(DatabaseConstants.CURRENT_LEXGRID_SCHEMA_DESCRIPTION);
		entry.setDbSchemaVersion(DatabaseConstants.CURRENT_LEXGRID_SCHEMA_VERSION);
		entry.setResourceUri(uri);
		entry.setResourceVersion(version);
		entry.setLastUpdateDate(new Timestamp(new Date().getTime()));
		
		return entry;
	}
}
