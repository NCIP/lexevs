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
package org.lexevs.dao.database.access.versions;

import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.SystemRelease;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;

/**
 * The Interface VersionsDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface VersionsDao extends LexGridSchemaVersionAwareDao {
	
	/**
	 * Gets the entry state by id.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param codingSchemeVersion the coding scheme version
	 * @param entryStateId the entry state id
	 * 
	 * @return the entry state by id
	 */
	public EntryState getEntryStateById(String codingSchemeName, String codingSchemeVersion, String entryStateId);

	/**
	 * Update entry state.
	 * 
	 * @param entryStateId the entry state id
	 * @param entryState the entry state
	 */
	public void updateEntryState(String entryStateId, EntryState entryState);

	/**
	 * Insert entry state.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param entryStateId the entry state id
	 * @param entryId the entry id
	 * @param entryType the entry type
	 * @param previousEntryStateId the previous entry state id
	 * @param entryState the entry state
	 */
	public void insertEntryState(
			String codingSchemeId,
			String entryStateId, 
			String entryId,
			String entryType,
			String previousEntryStateId,
			EntryState entryState);
	
	/**
	 * Insert revision.
	 * 
	 * @param revision the revision
	 */
	public void insertRevision(Revision revision);
	
	/**
	 * Insert system release.
	 * 
	 * @param systemRelease the system release
	 */
	public void insertSystemRelease(SystemRelease systemRelease);
	
	/**
	 * Gets the system release id by uri.
	 * 
	 * @param systemReleaseUri the system release uri
	 * 
	 * @return the system release id by uri
	 */
	public String getSystemReleaseIdByUri(String systemReleaseUri);
}
