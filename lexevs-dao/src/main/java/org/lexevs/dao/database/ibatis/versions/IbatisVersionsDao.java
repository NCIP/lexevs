package org.lexevs.dao.database.ibatis.versions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.LexGrid.versions.EntryState;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.versions.parameter.InsertEntryStateBean;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;

public class IbatisVersionsDao extends AbstractIbatisDao implements VersionsDao {
	
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");
	
	public static String INSERT_ENTRY_STATE_SQL = "insertEntryState";
	public static String GET_ENTRY_STATE_BY_ID_SQL = "insertEntryState";

	public EntryState getEntryStateById(String codingSchemeName, String codingSchemeVersion, String entryStateId) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeName, codingSchemeVersion);
		return (EntryState) this.getSqlMapClientTemplate().queryForObject(GET_ENTRY_STATE_BY_ID_SQL, 
			new PrefixedParameter(prefix, entryStateId));
	}
	
	public void updateEntryState(String id, EntryState entryState) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("entryStateId", id);
		paramMap.put("entryState", entryState);
		
		this.getSqlMapClientTemplate().update("updateEntryStateById", paramMap);
		
	}
	
	public void insertEntryState(String codingSchemeId, String entryStateId,
			String entryId, String entryType, String previousEntryStateId,
			EntryState entryState) {
		buildInsertEntryStateBean(
				this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId),
				entryStateId, 
				entryId,
				entryType,
				previousEntryStateId,
				entryState);
		
		if(entryState == null){
			return;
		}
		
		this.getSqlMapClientTemplate().update(INSERT_ENTRY_STATE_SQL, 
				buildInsertEntryStateBean(
						this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId),
						entryStateId, 
						entryId,
						entryType,
						previousEntryStateId,
						entryState));	
	}
	
	public void insertEntryState(
			String codingSchemeName,
			String codingSchemeVersion,
			String entryStateId, 
			String entryId,
			String entryType,
			String previousEntryStateId,
			EntryState entryState) {
		
		if(entryState == null){
			return;
		}

		this.getSqlMapClientTemplate().update(INSERT_ENTRY_STATE_SQL, 
				buildInsertEntryStateBean(
						this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeName, codingSchemeVersion),
						entryStateId, 
						entryId,
						entryType,
						previousEntryStateId,
						entryState));
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
		return DaoUtility.createList(supportedDatebaseVersion, LexGridSchemaVersion.class);
	}

}
