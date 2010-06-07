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
