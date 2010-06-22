package org.lexevs.dao.database.service.valuesets;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.valueSets.DefinitionEntry;

public interface VSDefinitionEntryService {

	public void insertVSDefinitionEntry(String valueSetDefinitionURI, DefinitionEntry defEntry);

	public void removeVSDefinitionEntry(String valueSetDefinitionURI, DefinitionEntry defEntry);
	
	public void updateVSDefinitionEntry(String valueSetDefinitionURI, DefinitionEntry defEntry);
	
	public void updateVSDefinitionEntryVersionableChanges(String valueSetDefinitionURI, DefinitionEntry defEntry);
	
	public void revise(String valueSetDefinitionURI, DefinitionEntry defEntry) throws LBException;
	
	public DefinitionEntry resolveDefinitionEntryByRevision(
			String valueSetDefURI, String ruleOrder, String revisionId)
			throws LBRevisionException;
}
