package org.lexevs.dao.database.service.ncihistory;

import org.LexGrid.versions.SystemRelease;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.springframework.transaction.annotation.Transactional;

public class VersionableEventNciHistoryService extends AbstractDatabaseService implements NciHistoryService {

	@Override
	@Transactional
	public void insertSystemRelease(String codingSchemeUri,
			String codingSchemeVersion, SystemRelease systemRelease) {
		String codingSchemeUid = this.getDaoManager().
		getCodingSchemeDao(
				codingSchemeUri, codingSchemeVersion).
				getCodingSchemeUIdByUriAndVersion(
						codingSchemeUri, 
						codingSchemeVersion);
		
		this.getDaoManager().getNciHistoryDao(
				codingSchemeUri, codingSchemeVersion).
					insertSystemRelease(codingSchemeUid, systemRelease);
	}
}
