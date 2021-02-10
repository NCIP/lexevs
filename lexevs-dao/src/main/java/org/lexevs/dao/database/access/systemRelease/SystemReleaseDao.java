
package org.lexevs.dao.database.access.systemRelease;

import java.util.List;

import org.LexGrid.versions.SystemRelease;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;

/**
 * The Interface SystemReleaseDao.
 * 
 * @author <a href="mailto:rao.ramachandra@mayo.edu">Ramachandra J S Rao</a>
 */
public interface SystemReleaseDao extends LexGridSchemaVersionAwareDao {

	/**
	 * insert system release entry.
	 * 
	 * @param systemRelease
	 */
	public String insertSystemReleaseEntry(SystemRelease systemRelease);
	
	/**
	 * get system release entry for a given uri.
	 * @param systemReleaseUri
	 * @return
	 */
	public SystemRelease getSystemReleaseMetadataByUri(String systemReleaseUri);
	
	/**
	 * get system release entry for a given unique id.
	 * @param systemReleaseId
	 * @return
	 */
	public SystemRelease getSystemReleaseMetadataById(String systemReleaseId);
	
	/**
	 * get all system release entries.
	 * @return
	 */
	public List<SystemRelease> getAllSystemRelease();

	/**
	 * get system release id by uri.
	 * @param systemReleaseUri
	 * @return
	 */
	public String getSystemReleaseUIdByUri(String systemReleaseUri);
}