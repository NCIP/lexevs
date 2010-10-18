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
package org.lexevs.dao.database.service.ncihistory;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeVersionList;
import org.LexGrid.LexBIG.DataModel.Collections.NCIChangeEventList;
import org.LexGrid.LexBIG.DataModel.Collections.SystemReleaseList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.SystemReleaseDetail;
import org.LexGrid.LexBIG.DataModel.NCIHistory.NCIChangeEvent;
import org.LexGrid.versions.CodingSchemeVersion;
import org.LexGrid.versions.SystemRelease;

public interface NciHistoryService {
	
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");

	public void insertSystemRelease(String codingSchemeUri, SystemRelease systemRelease);
	
	public void insertNCIChangeEvent(String codingSchemeUri, NCIChangeEvent changeEvent);
	
	public SystemReleaseList getBaseLines(String uri, Date releasedAfter, Date releasedBefore);
	
	public SystemRelease getEarliestBaseLine(String uri) ;
	
	public SystemRelease getLatestBaseLine(String uri);
	
	public SystemReleaseDetail getSystemRelease(String uri, URI releaseURN);

	public NCIChangeEventList getEditActionList(String uri, ConceptReference conceptReference,
			Date date);

	public NCIChangeEventList getEditActionList(String uri, ConceptReference conceptReference, Date beginDate,
			Date endDate);

	public NCIChangeEventList getEditActionList(String uri, ConceptReference conceptReference, URI releaseURN);

	public CodingSchemeVersion getConceptCreationVersion(String urn, ConceptReference conceptReference);

	public CodingSchemeVersionList getConceptChangeVersions(String urn, ConceptReference conceptReference,
			Date beginDate, Date endDate);

	public NCIChangeEventList getDescendants(String uri, ConceptReference conceptReference);

	public NCIChangeEventList getAncestors(String uri, ConceptReference conceptReference);
	
	public void removeNciHistory(String codingSchemeUri);
}