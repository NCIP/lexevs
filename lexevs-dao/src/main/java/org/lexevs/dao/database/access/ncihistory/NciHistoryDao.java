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
package org.lexevs.dao.database.access.ncihistory;

import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.NCIHistory.NCIChangeEvent;
import org.LexGrid.versions.CodingSchemeVersion;
import org.LexGrid.versions.SystemRelease;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;

public interface NciHistoryDao extends LexGridSchemaVersionAwareDao {

	public List<SystemRelease> getBaseLines(String codingSchemeUid, Date releasedAfter, Date releasedBefore);

	public SystemRelease getEarliestBaseLine(String codingSchemeUid);

	public SystemRelease getLatestBaseLine(String codingSchemeUid);

	public SystemRelease getSystemReleaseForReleaseUri(String codingSchemeUid, String releaseURN);

	public List<NCIChangeEvent> getEditActionList(String codingSchemeUid, String conceptCode, Date date);

	public List<NCIChangeEvent> getEditActionList(String codingSchemeUid, String conceptCode, Date beginDate, Date endDate);

	public List<NCIChangeEvent> getEditActionList(String codingSchemeUid, String conceptCode, String releaseURN);

	public CodingSchemeVersion getConceptCreationVersion(String codingSchemeUid, String conceptCode);

	public List<CodingSchemeVersion> getConceptChangeVersions(String codingSchemeUid, String conceptCode,
			Date beginDate, Date endDate);

	public List<NCIChangeEvent> getDescendants(String codingSchemeUid, String conceptCode);

	public List<NCIChangeEvent> getAncestors(String codingSchemeUid, String conceptCode);

	public void insertSystemRelease(String codingSchemeUid, SystemRelease systemRelease);
}