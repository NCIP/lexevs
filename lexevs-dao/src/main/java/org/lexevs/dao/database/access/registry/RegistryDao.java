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
package org.lexevs.dao.database.access.registry;

import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry.ResourceType;

/**
 * The Interface RegistryDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface RegistryDao {
	
	/**
	 * Gets the all registry entries of type.
	 * 
	 * @param type the type
	 * 
	 * @return the all registry entries of type
	 */
	public List<RegistryEntry> getAllRegistryEntriesOfType(ResourceType type);
	
	/**
	 * Gets the all registry entries of type and URI.
	 * 
	 * @param type the resource type
	 * @param uri the resource URI
	 * 
	 * @return the all registry entries of type and URI
	 */
	public List<RegistryEntry> getAllRegistryEntriesOfTypeAndURI(ResourceType type, String URI);
	
	/**
	 * Gets the all registry entries of type, URI and version.
	 * 
	 * @param type the resource type
	 * @param uri the resource URI
	 * @param version of resource
	 * 
	 * @return the all registry entries of type and URI
	 */
	public List<RegistryEntry> getAllRegistryEntriesOfTypeURIAndVersion(ResourceType type, String URI, String version);
	
	/**
	 * Gets the all registry entries.
	 * 
	 * @return the all registry entries
	 */
	public List<RegistryEntry> getAllRegistryEntries();

	/**
	 * Gets the registry entry for uri and version.
	 * 
	 * @param uri the uri
	 * @param version the version
	 * 
	 * @return the registry entry for uri and version
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public RegistryEntry getRegistryEntryForUriAndVersion(String uri, String version) throws LBParameterException;
	
	/**
	 * Gets the registry entries for uri.
	 * 
	 * @param uri the uri
	 * 
	 * @return the registry entries for uri
	 */
	public List<RegistryEntry> getRegistryEntriesForUri(String uri);
	
	/**
	 * Update last update time.
	 * 
	 * @param lastUpdateTime the last update time
	 */
	public void updateLastUpdateTime(Date lastUpdateTime);
	
	/**
	 * Gets the last used db identifier.
	 * 
	 * @return the last used db identifier
	 */
	public String getLastUsedDbIdentifier();
	
	/**
	 * Inits the registry metadata.
	 */
	public void initRegistryMetadata();
	
	/**
	 * Update last used db identifier.
	 * 
	 * @param databaseIdentifier the database identifier
	 */
	public void updateLastUsedDbIdentifier(String databaseIdentifier);
	
	/**
	 * Gets the last used history identifier.
	 * 
	 * @return the last used history identifier
	 */
	public String getLastUsedHistoryIdentifier();
	
	/**
	 * Gets the last update time.
	 * 
	 * @return the last update time
	 */
	public Date getLastUpdateTime();
	
	/**
	 * Update registry entry.
	 * 
	 * @param entry the entry
	 */
	public void updateRegistryEntry(RegistryEntry entry);
	
	/**
	 * Insert registry entry.
	 * 
	 * @param entry the entry
	 */
	public void insertRegistryEntry(RegistryEntry entry);
	
	/**
	 * Delete registry entry.
	 * 
	 * @param entry the entry
	 */
	public void deleteRegistryEntry(RegistryEntry entry);
}
