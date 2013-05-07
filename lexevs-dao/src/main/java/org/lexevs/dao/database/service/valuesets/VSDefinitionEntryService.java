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
package org.lexevs.dao.database.service.valuesets;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.valueSets.DefinitionEntry;

/**
 * The Interface VSDefinitionEntryService.
 */
public interface VSDefinitionEntryService {

	/**
	 * Insert vs definition entry.
	 * 
	 * @param valueSetDefinitionURI the value set definition uri
	 * @param defEntry the def entry
	 */
	public void insertVSDefinitionEntry(String valueSetDefinitionURI, DefinitionEntry defEntry);

	/**
	 * Removes the vs definition entry.
	 * 
	 * @param valueSetDefinitionURI the value set definition uri
	 * @param defEntry the def entry
	 */
	public void removeVSDefinitionEntry(String valueSetDefinitionURI, DefinitionEntry defEntry);
	
	/**
	 * Update vs definition entry.
	 * 
	 * @param valueSetDefinitionURI the value set definition uri
	 * @param defEntry the def entry
	 */
	public void updateVSDefinitionEntry(String valueSetDefinitionURI, DefinitionEntry defEntry);
	
	/**
	 * Update vs definition entry versionable changes.
	 * 
	 * @param valueSetDefinitionURI the value set definition uri
	 * @param defEntry the def entry
	 */
	public void updateVSDefinitionEntryVersionableChanges(String valueSetDefinitionURI, DefinitionEntry defEntry);
	
	/**
	 * Revise.
	 * 
	 * @param valueSetDefinitionURI the value set definition uri
	 * @param defEntry the def entry
	 * 
	 * @throws LBException the LB exception
	 */
	public void revise(String valueSetDefinitionURI, DefinitionEntry defEntry) throws LBException;
	
	/**
	 * Resolve definition entry by revision.
	 * 
	 * @param valueSetDefURI the value set def uri
	 * @param ruleOrder the rule order
	 * @param revisionId the revision id
	 * 
	 * @return the definition entry
	 * 
	 * @throws LBRevisionException the LB revision exception
	 */
	public DefinitionEntry resolveDefinitionEntryByRevision(
			String valueSetDefURI, String ruleOrder, String revisionId)
			throws LBRevisionException;
}