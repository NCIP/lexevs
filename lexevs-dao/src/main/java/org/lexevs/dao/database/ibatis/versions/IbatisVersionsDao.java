package org.lexevs.dao.database.ibatis.versions;

import java.util.List;

import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.SystemRelease;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.batch.IbatisInserter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.versions.parameter.InsertEntryStateBean;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;

public class IbatisVersionsDao extends AbstractIbatisDao implements VersionsDao {
	
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");
	
	public static String VERSIONS_NAMESPACE = "Versions.";
	public static String INSERT_ENTRY_STATE_SQL = VERSIONS_NAMESPACE + "insertEntryState";
	public static String GET_ENTRY_STATE_BY_ID_SQL = VERSIONS_NAMESPACE + "insertEntryState";
	public static String GET_SYSTEM_RELEASE_ID_BY_URI = VERSIONS_NAMESPACE + "getSystemReleaseIdByUri";
	

	@Override
	public String getSystemReleaseIdByUri(String systemReleaseUri) {
		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		return (String) this.getSqlMapClientTemplate().queryForObject(GET_SYSTEM_RELEASE_ID_BY_URI, 
			new PrefixedParameter(prefix, systemReleaseUri));
	}

	public EntryState getEntryStateById(String codingSchemeName, String codingSchemeVersion, String entryStateId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeName, codingSchemeVersion);
		return (EntryState) this.getSqlMapClientTemplate().queryForObject(GET_ENTRY_STATE_BY_ID_SQL, 
			new PrefixedParameter(prefix, entryStateId));
	}
	
	public void updateEntryState(String id, EntryState entryState) {
		
		
	}
	
	public void insertEntryState(String prefix, String entryStateId,
			String entryId, String entryType, String previousEntryStateId,
			EntryState entryState, IbatisInserter ibatisInserter){
		buildInsertEntryStateBean(
				prefix,
				entryStateId, 
				entryId,
				entryType,
				previousEntryStateId,
				entryState);
		
		if(entryState == null){
			return;
		}
		
		ibatisInserter.insert(INSERT_ENTRY_STATE_SQL, 
				buildInsertEntryStateBean(
						prefix,
						entryStateId, 
						entryId,
						entryType,
						previousEntryStateId,
						entryState));	
		
	}
	
	@Override
	public void insertRevision(Revision revision) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	@Override
	public void insertSystemRelease(SystemRelease systemRelease) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}
	
	public void insertEntryState(String codingSchemeId, String entryStateId,
			String entryId, String entryType, String previousEntryStateId,
			EntryState entryState) {
		this.insertEntryState(
				this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId), 
				entryStateId, 
				entryId, 
				entryType, 
				previousEntryStateId, 
				entryState, 
				this.getNonBatchTemplateInserter());
	}
	

	protected InsertEntryStateBean buildInsertEntryStateBean(
			String prefix, 
			String entryStateId, 
			String entryId, 
			String entryType,
			String previousEntryStateId,
			EntryState entryState){
		InsertEntryStateBean bean = new InsertEntryStateBean();
		bean.setPrefix(prefix);
		bean.setEntryId(entryId);
		bean.setEntryState(entryState);
		bean.setEntryType(entryType);
		bean.setEntryStateId(entryStateId);
		bean.setPreviousEntryStateId(previousEntryStateId);
		
		return bean;
	}

	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createList(LexGridSchemaVersion.class, supportedDatebaseVersion);
	}
}
