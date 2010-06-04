package org.lexevs.dao.database.ibatis.ncihistory;

import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.NCIHistory.NCIChangeEvent;
import org.LexGrid.versions.CodingSchemeVersion;
import org.LexGrid.versions.SystemRelease;
import org.lexevs.dao.database.access.ncihistory.NciHistoryDao;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.parameter.SequentialMappedParameterBean;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;

public class IbatisNciHistoryDao extends AbstractIbatisDao implements NciHistoryDao {
	
	private static String NCI_HISTORY_NAMESPACE = "NciHistory.";
	
	private static String GET_BASELINES_SQL = NCI_HISTORY_NAMESPACE + "getBaseLines";
	
	private static String GET_EARLIEST_BASELINE_SQL = NCI_HISTORY_NAMESPACE + "getEarliestBaseLine";
	
	private static String GET_LATEST_BASELINE_SQL = NCI_HISTORY_NAMESPACE + "getLatestBaseLine";
	
	private static String GET_SYSTEMRELEASE_FOR_URI_SQL = NCI_HISTORY_NAMESPACE + "getSystemReleaseForUri";
	
	private static String INSERT_SYSTEM_RELEASE_SQL = NCI_HISTORY_NAMESPACE + "insertSystemRelease";
	
	private static String GET_DECENDANTS_SQL = NCI_HISTORY_NAMESPACE + "getDecendants";
	
	private static String GET_ANCESTORS_SQL = NCI_HISTORY_NAMESPACE + "getAncestors";
	
	/** The supported datebase version. */
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");

	@Override
	public void insertSystemRelease(String codingSchemeUid,
			SystemRelease systemRelease) {
		
		String systemReleaseGuid = this.createUniqueId();
		
		this.getSqlMapClientTemplate().insert(
				INSERT_SYSTEM_RELEASE_SQL, 
				new SequentialMappedParameterBean(systemReleaseGuid,codingSchemeUid, systemRelease));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NCIChangeEvent> getAncestors(String codingSchemeUid, String conceptCode) {
		return this.getSqlMapClientTemplate().queryForList(GET_ANCESTORS_SQL, 
				new SequentialMappedParameterBean(
						codingSchemeUid,
						conceptCode));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SystemRelease> getBaseLines(String codingSchemeUid, Date releasedAfter,
			Date releasedBefore) {
		
		return this.getSqlMapClientTemplate().queryForList(GET_BASELINES_SQL, 
				new SequentialMappedParameterBean(
						codingSchemeUid,
						releasedAfter,
						releasedBefore));
	}

	@Override
	public List<CodingSchemeVersion> getConceptChangeVersions(String urn,
			String conceptCode, Date beginDate, Date endDate) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	@Override
	public CodingSchemeVersion getConceptCreationVersion(String codingSchemeUid,
			String conceptCode) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NCIChangeEvent> getDescendants(String codingSchemeUid, String conceptCode) {
		return this.getSqlMapClientTemplate().queryForList(GET_DECENDANTS_SQL, 
				new SequentialMappedParameterBean(
						codingSchemeUid,
						conceptCode));
	}

	@Override
	public SystemRelease getEarliestBaseLine(String codingSchemeUid) {
		return (SystemRelease) this.getSqlMapClientTemplate().queryForObject(GET_EARLIEST_BASELINE_SQL, 
				new SequentialMappedParameterBean(codingSchemeUid));
	}

	@Override
	public List<NCIChangeEvent> getEditActionList(String codingSchemeUid,
			String conceptCode, Date date) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	@Override
	public List<NCIChangeEvent> getEditActionList(String codingSchemeUid,
			String conceptCode, Date beginDate, Date endDate) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	@Override
	public List<NCIChangeEvent> getEditActionList(String codingSchemeUid,
			String conceptCode, String releaseURN) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	@Override
	public SystemRelease getLatestBaseLine(String codingSchemeUid) {
		return (SystemRelease) this.getSqlMapClientTemplate().queryForObject(GET_LATEST_BASELINE_SQL, 
				new SequentialMappedParameterBean(codingSchemeUid));
	}

	@Override
	public SystemRelease getSystemReleaseForReleaseUri(String codingSchemeUid,
			String releaseURN) {
		return (SystemRelease) this.getSqlMapClientTemplate().queryForObject(GET_SYSTEMRELEASE_FOR_URI_SQL, 
				new SequentialMappedParameterBean(
						codingSchemeUid,
						releaseURN));
	}

	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createNonTypedList(supportedDatebaseVersion);
	}

}
