
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