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
package org.lexevs.dao.database.access.ncihistory;

import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.NCIHistory.NCIChangeEvent;
import org.LexGrid.versions.CodingSchemeVersion;
import org.LexGrid.versions.SystemRelease;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;

public interface NciHistoryDao extends LexGridSchemaVersionAwareDao {

	public List<SystemRelease> getBaseLines(String codingSchemeUri, Date releasedAfter, Date releasedBefore);
	
	public String getSystemReleaseUidForDate(String codingSchemeUri, Date editDate);

	public SystemRelease getEarliestBaseLine(String codingSchemeUri);

	public SystemRelease getLatestBaseLine(String codingSchemeUri);

	public SystemRelease getSystemReleaseForReleaseUri(String codingSchemeUri, String releaseURN);

	public List<NCIChangeEvent> getEditActionList(String codingSchemeUri, String conceptCode, Date date);

	public List<NCIChangeEvent> getEditActionList(String codingSchemeUri, String conceptCode, Date beginDate, Date endDate);

	public List<NCIChangeEvent> getEditActionList(String codingSchemeUri, String conceptCode, String releaseURN);

	public CodingSchemeVersion getConceptCreateVersion(String codingSchemeUri, String conceptCode);

	public List<CodingSchemeVersion> getConceptChangeVersions(String codingSchemeUri, String conceptCode,
			Date beginDate, Date endDate);

	public List<NCIChangeEvent> getDescendants(String codingSchemeUri, String conceptCode);

	public List<NCIChangeEvent> getAncestors(String codingSchemeUri, String conceptCode);

	public void insertSystemRelease(String codingSchemeUri, SystemRelease systemRelease);
	
	public void insertNciChangeEvent(String releaseUid, NCIChangeEvent changeEvent);

	public SystemRelease getSystemReleaseForReleaseUid(String codingSchemeUri,
			String releaseUid);
	
	public void removeNciHistory(String codingSchemeUri);

	public List<String> getCodeListForVersion(String currentVersion);

	public Date getDateForVersion(String currentVersion);

	public List<String> getVersionsForDateRange(String previousDate, String currentDate);

	public void insertNciChangeEventBatch(String codingSchemeUri, List<NCIChangeEvent> changeEvents);

}