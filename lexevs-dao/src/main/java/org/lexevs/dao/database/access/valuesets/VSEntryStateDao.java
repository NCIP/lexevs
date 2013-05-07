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

import org.LexGrid.versions.EntryState;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;

/**
 * The Interface VSEntryStateDao.
 * 
 * @author <a href="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</a>
 */
public interface VSEntryStateDao extends LexGridSchemaVersionAwareDao {

	/**
	 * Gets the entry state by UID.
	 * 
	 * @param entryStateId the entry state UID
	 * 
	 * @return the entry state
	 */
	public EntryState getEntryStateByUId(String entryStateUId);

	/**
	 * Update entry state.
	 * 
	 * @param entryStateId the entry state UID
	 * @param entryState the entry state
	 */
	public void updateEntryState(String entryStateUId, EntryState entryState);

	/**
	 * Insert entry state.
	 * 
	 * @param entryStateUId
	 * @param entryUId the entry id
	 * @param entryType the entry type
	 * @param previousEntryStateUId the previous entry state id
	 * @param entryState the entry state
	 */
	public void insertEntryState( String entryStateUId,
			String entryUId, String entryType, String previousEntryStateUId,
			EntryState entryState);	
	
	/**
	 * Insert entry state.
	 * 
	 * @param entryUId the entry resource UID
	 * @param entryType the entry type
	 * @param previousEntryStateUId the previous entry state UID
	 * @param entryState the entry state
	 */
	public String insertEntryState(
			String entryUId,
			String entryType,
			String previousEntryStateUId,
			EntryState entryState);
			
	public void deleteAllEntryStatesOfVsPropertiesByParentUId(
			String parentUId, String parentType);

	public void deleteAllEntryStatesOfValueSetDefinitionByUId(
			String valueSetDefGuid);	
	
	public void deleteAllEntryStateEntriesByEntryUId(String entryUId);

	public void deleteAllEntryStatesOfPickListDefinitionByUId(
			String pickListUId);

	public void deleteAllEntryStatesOfPLEntryNodeByUId(
			String pickListEntryNodeUId);
	
	public void deleteAllEntryStateByEntryUIdAndType(
			String entryGuid, String entryType);
}