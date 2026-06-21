
package org.lexevs.dao.database.access.versions;

import java.util.List;

import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.SystemRelease;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;
import org.lexevs.dao.database.ibatis.versions.parameter.InsertEntryStateBean;
import org.lexevs.dao.database.inserter.Inserter;
import org.mybatis.spring.SqlSessionTemplate;

/**
 * The Interface VersionsDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface VersionsDao extends LexGridSchemaVersionAwareDao {
	
	public enum EntryStateType {
		/** The CODINGSCHEME. */
		CODINGSCHEME,
		/** The ENTITY. */
		ENTITY,
		/** The PROPERTY*/
		PROPERTY,
		/** The RELATION*/
		RELATION,
		/** The ENTITYASSNSTOENTITY*/
		ENTITYASSNSTOENTITY,
		/** The ENTITYASSNSTODATA*/
		ENTITYASSNSTODATA,
		/** VALUESETDEFINITION */
		VALUESETDEFINITION,
		/** VALUESETDEFINITIONENTRY */	
		VALUESETDEFINITIONENTRY,
		/** PICKLISTDEFINITION */
		PICKLISTDEFINITION,
		/** PICKLISTENTRYNODE */
		PICKLISTENTRYNODE
	}
	
	public String getPreviousRevisionIdFromGivenRevisionIdForEntry(
			String codingSchemeUid, 
			String entityUid,
			String currentRevisionId) ;

	public EntryState getEntryStateByEntryUidAndRevisionId(
			String codingSchemeUId,
			String entryUId, 
			String revisionId);

	public String insertEntryState(
			String codingSchemeUId,
			String entryUId,
			EntryStateType entryType,
			String previousEntryStateUId,
			EntryState entryState);

	public void insertEntryState( 
			String codingSchemeUId,
			String entryStateUId,
			String entryUId, 
			EntryStateType entryType, 
			String previousEntryStateUId,
			EntryState entryState);
	
	public void insertEntryState(
			String codingSchemeUId,
			String entryStateUId,
			String entryUId, 
			EntryStateType entryType, 
			String previousEntryStateUId,
			EntryState entryState,
			SqlSessionTemplate session);
	
	public void updatePreviousEntryStateUIds(String codingSchemeUId,
			String entityUId, String prevEntryStateUId, String newEntryStateUId);
	
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

	public void deleteAllEntryStateOfCodingScheme(String codingSchemeUId);

	public void deleteAllEntryStateOfEntity(String codingSchemeUId,
			String entityUId);
	
	public void deleteAllEntryStateEntriesByEntryUId(String codingSchemeUId,
			String entryUId);

	public void deleteAllEntryStateOfRelation(String codingSchemeUId,
			String relationUId);


	void insertEntryStateMybatisBatch(String codingSchemeUId, List<InsertEntryStateBean> bean);
}