package org.lexevs.dao.database.service.version;

import org.LexGrid.versions.Revision;
import org.LexGrid.versions.SystemRelease;
import org.lexevs.dao.database.service.DatabaseService;

public interface VersionService extends DatabaseService {

	public void insertSystemRelease(SystemRelease systemRelease);
	
	public void revise(Revision revision);
}
