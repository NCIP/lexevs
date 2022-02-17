
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