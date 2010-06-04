package org.lexevs.dao.database.service.ncihistory;

import org.LexGrid.versions.SystemRelease;

public interface NciHistoryService {

	public void insertSystemRelease(String codingSchemeUri, String codingSchemeVersion, SystemRelease systemRelease);

}
