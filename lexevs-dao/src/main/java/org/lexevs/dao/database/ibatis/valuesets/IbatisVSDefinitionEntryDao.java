package org.lexevs.dao.database.ibatis.valuesets;

import java.util.List;

import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.lexevs.dao.database.access.valuesets.VSDefinitionEntryDao;
import org.lexevs.dao.database.access.valuesets.VSEntryStateDao;
import org.lexevs.dao.database.access.valuesets.VSPropertyDao;
import org.lexevs.dao.database.access.valuesets.VSPropertyDao.ReferenceType;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTuple;
import org.lexevs.dao.database.ibatis.valuesets.parameter.InsertOrUpdateDefinitionEntryBean;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;

public class IbatisVSDefinitionEntryDao extends AbstractIbatisDao implements
		VSDefinitionEntryDao {

	private VSEntryStateDao vsEntryStateDao = null;
	
	private VSPropertyDao vsPropertyDao = null;
	
	/** The supported datebase version. */
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");

	private static String VSDEFINITIONENTRY_NAMESPACE = "vsDefinitionEntry.";
	
	private static String INSERT_DEFINITION_ENTRY_SQL = VSDEFINITIONENTRY_NAMESPACE + "insertDefinitionEntry";
	
	private static String REMOVE_DEFINITION_ENTRY_BY_UID_SQL = VSDEFINITIONENTRY_NAMESPACE + "deleteDefinitionEntryByUId";
	
	private static String GET_DEFINITION_ENTRY_UID_SQL = VSDEFINITIONENTRY_NAMESPACE + "getDefinitionEntryUId";
	
	private static String GET_DEFINITION_ENTRY_ATTRIBUTES_BY_UID_SQL = VSDEFINITIONENTRY_NAMESPACE + "getDefinitionEntryAttribByUId";
	
	private static String UPDATE_DEFINITION_ENTRY_ATTRIBUTES_BY_UID_SQL = VSDEFINITIONENTRY_NAMESPACE + "updateDefinitionEntryAttribByUId";
	
	private static String UPDATE_DEFINITION_ENTRY_VER_ATTRIBUTES_BY_UID_SQL = VSDEFINITIONENTRY_NAMESPACE + "updateDefinitionEntryVerAttribByUId";
	
	private static String GET_DEFINITION_ENTRY_LATEST_REVISION_ID_BY_UID = VSDEFINITIONENTRY_NAMESPACE + "getDefinitionEntryLatestRevisionIdByUId";
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.AbstractBaseDao#doGetSupportedLgSchemaVersions()
	 */
	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createList(LexGridSchemaVersion.class, supportedDatebaseVersion);
	}
	
	@Override
	public String insertDefinitionEntry(String valueSetDefinitionUId,
			DefinitionEntry vsdEntry) {
		String vsdEntryGuid = this.createUniqueId();
		
		InsertOrUpdateDefinitionEntryBean vsdEntryBean = new InsertOrUpdateDefinitionEntryBean();
		vsdEntryBean.setUId(vsdEntryGuid);
		vsdEntryBean.setDefinitionEntry(vsdEntry);
		vsdEntryBean.setValueSetDefUId(valueSetDefinitionUId);
		
		if (vsdEntry.getCodingSchemeReference() != null)
		{
			vsdEntryBean.setCodingSchemeReference(vsdEntry.getCodingSchemeReference().getCodingScheme());
		}
		
		else if (vsdEntry.getValueSetDefinitionReference() != null)
		{
			vsdEntryBean.setValueSetDefReference(vsdEntry.getValueSetDefinitionReference().getValueSetDefinitionURI());
		}
		else if (vsdEntry.getEntityReference() != null)
		{
			vsdEntryBean.setEntityCode(vsdEntry.getEntityReference().getEntityCode());
			vsdEntryBean.setEntityCodeNamespace(vsdEntry.getEntityReference().getEntityCodeNamespace());
			vsdEntryBean.setLeafOnly(vsdEntry.getEntityReference().getLeafOnly());
			vsdEntryBean.setReferenceAssociation(vsdEntry.getEntityReference().getReferenceAssociation());
			vsdEntryBean.setTargetToSource(vsdEntry.getEntityReference().getTargetToSource());
			vsdEntryBean.setTransitiveClosure(vsdEntry.getEntityReference().getTransitiveClosure());
		}
		else if (vsdEntry.getPropertyReference() != null)
		{
			vsdEntryBean.setPropertyName(vsdEntry.getPropertyReference().getPropertyName());
			vsdEntryBean.setPropertyRefCodingScheme(vsdEntry.getPropertyReference().getCodingScheme());
			if (vsdEntry.getPropertyReference().getPropertyMatchValue() != null)
			{
				vsdEntryBean.setPropertyMatchValue(vsdEntry.getPropertyReference().getPropertyMatchValue().getContent());
				vsdEntryBean.setFormat(vsdEntry.getPropertyReference().getPropertyMatchValue().getDataType());
				vsdEntryBean.setMatchAlgorithm(vsdEntry.getPropertyReference().getPropertyMatchValue().getMatchAlgorithm());
			}
		}			
		
		// insert into vsdEntry table
		this.getSqlMapClientTemplate().insert(INSERT_DEFINITION_ENTRY_SQL, vsdEntryBean);
		
		return vsdEntryGuid;
	}

	@Override
	public void deleteDefinitionEntry(String vsDefinitionEntryUId) {

		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		// remove definition entries
		this.getSqlMapClientTemplate().delete(REMOVE_DEFINITION_ENTRY_BY_UID_SQL, new PrefixedParameter(prefix, vsDefinitionEntryUId));
	}

	@Override
	public String getDefinitionEntryUId(String valueSetDefinitionURI,
			String ruleOrder) {
		
		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		String vsDefinitionUId = (String) this.getSqlMapClientTemplate()
				.queryForObject(
						GET_DEFINITION_ENTRY_UID_SQL,
						new PrefixedParameterTuple(prefix,
								valueSetDefinitionURI, ruleOrder));
		
		return vsDefinitionUId;
	}

	@Override
	public String insertHistoryDefinitionEntry(String valueSetDefUId, String vsDefinitionUId,
			DefinitionEntry defEntry) {

		String historyPrefix = this.getPrefixResolver().resolveHistoryPrefix();
		String prefix = this.getPrefixResolver().resolveDefaultPrefix();

		InsertOrUpdateDefinitionEntryBean definitionEntryData = (InsertOrUpdateDefinitionEntryBean) this.getSqlMapClientTemplate()
				.queryForObject(GET_DEFINITION_ENTRY_ATTRIBUTES_BY_UID_SQL,
						new PrefixedParameter(prefix, vsDefinitionUId));
		
		definitionEntryData.setPrefix(historyPrefix);
		
		this.getNonBatchTemplateInserter().insert(INSERT_DEFINITION_ENTRY_SQL, definitionEntryData);
		
		if (!vsEntryStateExists(prefix, definitionEntryData.getEntryStateUId())) {

			EntryState entryState = new EntryState();

			entryState.setChangeType(ChangeType.NEW);
			entryState.setRelativeOrder(0L);

			vsEntryStateDao
					.insertEntryState(definitionEntryData.getEntryStateUId(),
							definitionEntryData.getUId(), ReferenceType.DEFINITIONENTRY.name(), null,
							entryState);
		}
		
		return definitionEntryData.getEntryStateUId();
	}

	@Override
	public String updateDefinitionEntry(String vsDefinitionUId,
			DefinitionEntry defEntry) {
		
		String entryStateUId = this.createUniqueId();
		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		InsertOrUpdateDefinitionEntryBean bean = new InsertOrUpdateDefinitionEntryBean();
		bean.setPrefix(prefix);
		bean.setUId(vsDefinitionUId);
		bean.setEntryStateUId(entryStateUId);
		bean.setDefinitionEntry(defEntry);
		
		this.getSqlMapClientTemplate().update(UPDATE_DEFINITION_ENTRY_ATTRIBUTES_BY_UID_SQL, bean);
		
		return entryStateUId;
	}

	@Override
	public String updateDefinitionEntryVersionableAttrib(
			String vsDefinitionUId, DefinitionEntry defEntry) {
		
		String entryStateUId = this.createUniqueId();
		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		InsertOrUpdateDefinitionEntryBean bean = new InsertOrUpdateDefinitionEntryBean();
		bean.setPrefix(prefix);
		bean.setUId(vsDefinitionUId);
		bean.setEntryStateUId(entryStateUId);
		bean.setDefinitionEntry(defEntry);
		
		this.getSqlMapClientTemplate().update(UPDATE_DEFINITION_ENTRY_VER_ATTRIBUTES_BY_UID_SQL, bean);
		
		return entryStateUId;
	}

	/**
	 * @return the vsEntryStateDao
	 */
	public VSEntryStateDao getVsEntryStateDao() {
		return vsEntryStateDao;
	}

	/**
	 * @param vsEntryStateDao the vsEntryStateDao to set
	 */
	public void setVsEntryStateDao(VSEntryStateDao vsEntryStateDao) {
		this.vsEntryStateDao = vsEntryStateDao;
	}

	@Override
	public String getLatestRevision(String vsDefEntryUId) {
		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		return (String) this.getSqlMapClientTemplate().queryForObject(
				GET_DEFINITION_ENTRY_LATEST_REVISION_ID_BY_UID, 
				new PrefixedParameter(prefix, vsDefEntryUId));		// TODO Auto-generated method stub
	}

	/**
	 * @return the vsPropertyDao
	 */
	public VSPropertyDao getVsPropertyDao() {
		return vsPropertyDao;
	}

	/**
	 * @param vsPropertyDao the vsPropertyDao to set
	 */
	public void setVsPropertyDao(VSPropertyDao vsPropertyDao) {
		this.vsPropertyDao = vsPropertyDao;
	}
}
