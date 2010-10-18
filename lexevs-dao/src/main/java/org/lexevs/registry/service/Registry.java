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
package org.lexevs.registry.service;

import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.lexevs.registry.model.RegistryEntry;

/**
 * The Interface Registry.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface Registry {
	
	/**
	 * The Enum ResourceType.
	 * 
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public enum ResourceType {
		/** The CODIN g_ scheme. */
		CODING_SCHEME, 
		/** The VALU e_ domain. */
		VALUESET_DEFINITION, 
		/** The PICKLIST. */
		PICKLIST_DEFINITION, 
		/** The NC i_ history. */
		NCI_HISTORY,
		CONCEPT_DOMAIN,
		USAGE_CONTEXT
	}
	
	/**
	 * The Enum KnownTags.
	 * 
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public enum KnownTags {
			/** The PRODUCTION. */
			PRODUCTION};

	/**
	 * Gets the entries for uri.
	 * 
	 * @param uri the uri
	 * 
	 * @return the entries for uri
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public List<RegistryEntry> getEntriesForUri(String uri)
			throws LBParameterException;

	/**
	 * Gets the coding scheme entry.
	 * 
	 * @param codingScheme the coding scheme
	 * 
	 * @return the coding scheme entry
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public RegistryEntry getCodingSchemeEntry(AbsoluteCodingSchemeVersionReference codingScheme)
			throws LBParameterException;
	
	/**
	 * Gets the non coding scheme entry.
	 * 
	 * @param uri the uri
	 * 
	 * @return the non coding scheme entry
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public RegistryEntry getNonCodingSchemeEntry(String uri)
		throws LBParameterException;
	
	/**
	 * Contains coding scheme entry.
	 * 
	 * @param codingScheme the coding scheme
	 * 
	 * @return true, if successful
	 */
	public boolean containsCodingSchemeEntry(AbsoluteCodingSchemeVersionReference codingScheme);
	
	/**
	 * Contains non coding scheme entry.
	 * 
	 * @param uri the uri
	 * 
	 * @return true, if successful
	 */
	public boolean containsNonCodingSchemeEntry(String uri);

	/**
	 * Adds the new item.
	 * 
	 * @param entry the entry
	 * 
	 * @throws Exception the exception
	 */
	public void addNewItem(RegistryEntry entry)
			throws Exception;

	/**
	 * Gets the all registry entries.
	 * 
	 * @return the all registry entries
	 */
	public List<RegistryEntry> getAllRegistryEntries();
	
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
	 * @param uri the resource uri
	 * @return the all registry entries of type and URI
	 */
	public List<RegistryEntry> getAllRegistryEntriesOfTypeAndURI(ResourceType type, String uri);
	
	/**
	 * Gets the all registry entries of type, URI and version.
	 * 
	 * @param type the resource type
	 * @param uri the resource uri
	 * @param version the verion
	 * @return the all registry entries of type and URI
	 */
	public List<RegistryEntry> getAllRegistryEntriesOfTypeURIAndVersion(ResourceType type, String uri, String version);

	/**
	 * Gets the last update time.
	 * 
	 * @return the last update time
	 */
	public Date getLastUpdateTime();

	/**
	 * Removes the entry.
	 * 
	 * @param entry the entry
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public void removeEntry(RegistryEntry entry) throws LBParameterException;
	
	/**
	 * Update entry.
	 * 
	 * @param entry the entry
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public void updateEntry(RegistryEntry entry) throws LBParameterException;
	
	/**
	 * Gets the next db identifier.
	 * 
	 * @return the next db identifier
	 * 
	 * @throws LBInvocationException the LB invocation exception
	 */
	public String getNextDBIdentifier() throws LBInvocationException;

	/**
	 * Gets the next history identifier.
	 * 
	 * @return the next history identifier
	 * 
	 * @throws LBInvocationException the LB invocation exception
	 */
	public String getNextHistoryIdentifier() throws LBInvocationException;

}