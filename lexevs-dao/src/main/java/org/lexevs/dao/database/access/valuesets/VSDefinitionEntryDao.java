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
package org.lexevs.dao.database.access.valuesets;

import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.valueSets.DefinitionEntry;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;

public interface VSDefinitionEntryDao extends LexGridSchemaVersionAwareDao {

/**
	 * Insert value set definition entry.
	 * 
	 * @param valueSetDefinitionGuid the value set definition GUID
	 * @param definitionEntry the Value Set definitionEntry
	 * 
	 * @return the string
	 */
public String insertDefinitionEntry(String valueSetDefinitionUId,
			DefinitionEntry vsdEntry);
	
	public void deleteDefinitionEntry(String vsDefinitionEntryUId);

	public String getDefinitionEntryUId(String valueSetDefinitionURI,
			String ruleOrder);

	public String insertHistoryDefinitionEntry(String valueSetDefUId,
			String vsDefinitionUId, DefinitionEntry defEntry);

	public String updateDefinitionEntry(String vsDefinitionUId,
			DefinitionEntry defEntry);

	public String updateDefinitionEntryVersionableAttrib(
			String vsDefinitionUId, DefinitionEntry defEntry);

	public String getLatestRevision(String vsDefEntryUId);

	public DefinitionEntry resolveDefinitionEntryByRevision(
			String valueSetDefURI, String ruleOrder, String revisionId)
			throws LBRevisionException;

}