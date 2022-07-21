
package org.lexevs.dao.database.ibatis.valuesets;

import java.util.List;

import org.LexGrid.versions.EntryState;
import org.lexevs.dao.database.access.valuesets.VSEntryStateDao;
import org.lexevs.dao.database.access.valuesets.VSPropertyDao.ReferenceType;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTuple;
import org.lexevs.dao.database.ibatis.revision.IbatisRevisionDao;
import org.lexevs.dao.database.ibatis.versions.parameter.InsertEntryStateBean;
import org.lexevs.dao.database.inserter.Inserter;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;
import org.mybatis.spring.SqlSessionTemplate;

/**
 * The Class IbatisVSEntryStateDao manages entrystate data to/fro database.
 * 
 * @author <a href="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</a>
 */
public class IbatisVSEntryStateDao extends AbstractIbatisDao implements VSEntryStateDao {
	
	/** The supported datebase version. */
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");
	
	/** The VERSION s_ namespace. */
	public static String VERSIONS_NAMESPACE = "VSEntryState.";
	
	/** The INSER t_ entr y_ stat e_ sql. */
	public static String INSERT_ENTRY_STATE_SQL = VERSIONS_NAMESPACE + "insertEntryState";
	
	/** The GE t_ entr y_ stat e_ b y_ i d_ sql. */
	public static String GET_ENTRY_STATE_BY_ID_SQL = VERSIONS_NAMESPACE + "getEntryStateByUId";

	private static String DELETE_ALL_VSPROPERTIES_ENTRYSTATE_BY_PARENTUID = VERSIONS_NAMESPACE + "deleteAllVSPropertiesEntryStateByParentUId";
	
	private static String DELETE_ALL_ENTRYSTATE_ENTRIES_BY_ENTRY_UID = VERSIONS_NAMESPACE + "deleteAllEntrySateEntriesByEntryUId";
	
	private static String DELETE_ALL_DEFINITIONENTRY_ENTRYSTATE_OF_VALUESET_DEFINITION = VERSIONS_NAMESPACE + "deleteAllDefinitionEntryEntrySateEntriesByUId";
	
	private static String DELETE_ALL_PLENTRY_PROPERTY_ENTRYSTATE_ENTRIES_OF_PL_DEFINITION = VERSIONS_NAMESPACE + "deleteAllPLEntryPropsEntrySateEntriesOfPLDefinition";

	private static String DELETE_ALL_PLENTRY_ENTRYSTATE_ENTRIES_OF_PL_DEFINITION = VERSIONS_NAMESPACE + "deleteAllPLEntryEntrySateEntriesOfPLDefinition";

	private static String DELETE_ALL_PL_DEFINITION_ENTRYSTATES = VERSIONS_NAMESPACE + "deleteAllPLDefinitionEntryStates";
	
	private static String DELETE_ENTRYSTATE_BY_ENTRYGUID_AND_TYPE = VERSIONS_NAMESPACE + "deleteVSEntryStatesByEntryGuidAndType";

	/** ibatis revision dao*/
	private IbatisRevisionDao ibatisRevisionDao = null;
	

	public EntryState getEntryStateByUId(String entryStateUId) {
		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		return (EntryState) this.getSqlSessionTemplate().selectOne(GET_ENTRY_STATE_BY_ID_SQL, 
			new PrefixedParameter(prefix, entryStateUId));
	}
	
	public void updateEntryState(String id, EntryState entryState) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Insert entry state.
	 * 
	 * @param prefix the prefix
	 * @param entryStateId the entry state id
	 * @param entryId the entry id
	 * @param entryType the entry type
	 * @param previousEntryStateId the previous entry state id
	 * @param entryState the entry state
	 * @param sqlSessionTemplate the ibatis inserter
	 */
	public void insertEntryState(String prefix, String entryStateId,
			String entryId, String entryType, String previousEntryStateId,
			EntryState entryState, SqlSessionTemplate sqlSessionTemplate){
		
		if(entryState == null){
			return;
		}
		
		InsertEntryStateBean esBean = buildInsertEntryStateBean(
				prefix,
				entryStateId, 
				entryId,
				entryType,
				previousEntryStateId,
				entryState);
		
		if (esBean == null)
			return;
		
		sqlSessionTemplate.insert(INSERT_ENTRY_STATE_SQL, esBean);	
		
	}
	
	public String insertEntryState(
			String entryUId, String entryType, String previousEntryStateUId,
			EntryState entryState) {
		
		String entryStateUId = this.createUniqueId();
		
		this.insertEntryState(
				this.getPrefixResolver().resolveDefaultPrefix(), 
				entryStateUId, 
				entryUId, 
				entryType, 
				previousEntryStateUId, 
				entryState, 
				this.getSqlSessionTemplate());
		
		return entryStateUId;
	}
	
	public void insertEntryState( String entryStateUId,
			String entryUId, String entryType, String previousEntryStateUId,
			EntryState entryState) {
		this.insertEntryState(
				this.getPrefixResolver().resolveDefaultPrefix(), 
				entryStateUId, 
				entryUId, 
				entryType, 
				previousEntryStateUId, 
				entryState, 
				this.getSqlSessionTemplate());
	}
	
	@Override
	public void deleteAllEntryStatesOfVsPropertiesByParentUId(
			String parentUId, String parentType) {

		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		this.getSqlSessionTemplate().delete(
				DELETE_ALL_VSPROPERTIES_ENTRYSTATE_BY_PARENTUID,
				new PrefixedParameterTuple(prefix, parentUId, parentType));
	}

	/**
	 * Builds the insert entry state bean.
	 * 
	 * @param prefix the prefix
	 * @param entryStateUId the entry state id
	 * @param entryUId the entry id
	 * @param entryType the entry type
	 * @param previousEntryStateUId the previous entry state id
	 * @param entryState the entry state
	 * 
	 * @return the insert entry state bean
	 */
	protected InsertEntryStateBean buildInsertEntryStateBean(
			String prefix, 
			String entryStateUId, 
			String entryUId, 
			String entryType,
			String previousEntryStateUId,
			EntryState entryState){
		
		String revisionUId = null;		
		String prevRevisionUId = null;
		
		if (entryState != null) {
			revisionUId = ibatisRevisionDao
					.getRevisionUIdById(entryState.getContainingRevision());
			// commenting it out since a null revision is valid in case of 
			// initial load.
			/*if (revisionUId == null)
			{
				return null;
				
			}*/
			if (entryState.getPrevRevision() != null)
			{
				prevRevisionUId = ibatisRevisionDao
					.getRevisionUIdById(entryState.getPrevRevision());
				/*if (prevRevisionUId == null)
				{
					return null;					
				}*/
			}
		}
		
		InsertEntryStateBean bean = new InsertEntryStateBean();
		bean.setPrefix(prefix);
		bean.setEntryUId(entryUId);
		bean.setEntryState(entryState);
		bean.setEntryType(entryType);
		bean.setEntryStateUId(entryStateUId);
		bean.setPreviousEntryStateUId(previousEntryStateUId);
		bean.setRevisionUId(revisionUId);
		bean.setPrevRevisionUId(prevRevisionUId);
		
		return bean;
	}
	
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createNonTypedList(supportedDatebaseVersion);
	}

	public void setIbatisRevisionDao(IbatisRevisionDao ibatisRevisionDao) {
		this.ibatisRevisionDao = ibatisRevisionDao;
	}

	@Override
	public void deleteAllEntryStatesOfValueSetDefinitionByUId(
			String valueSetDefGuid) {
		
		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		/* 1. Delete all value set definition property entry states. */
		this.deleteAllEntryStatesOfVsPropertiesByParentUId(valueSetDefGuid,
				ReferenceType.VALUESETDEFINITION.name());
		
		/* 2. Delete all vsEntry entry states of value set definition.*/
		this.getSqlSessionTemplate().delete(
				DELETE_ALL_DEFINITIONENTRY_ENTRYSTATE_OF_VALUESET_DEFINITION,
				new PrefixedParameter(prefix, valueSetDefGuid));
		
		/* 3. Delete all value set definition entry states. */
		this.deleteAllEntryStateByEntryUIdAndType(valueSetDefGuid, ReferenceType.VALUESETDEFINITION.name());
	}
	
	@Override
	public void deleteAllEntryStateByEntryUIdAndType(
			String valueSetDefGuid, String entryType) {
		
		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		this.getSqlSessionTemplate().delete(
				DELETE_ENTRYSTATE_BY_ENTRYGUID_AND_TYPE,
				new PrefixedParameterTuple(prefix, valueSetDefGuid, entryType));
	}
	
	public void deleteAllEntryStateEntriesByEntryUId(String entryUId) {

		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		this.getSqlSessionTemplate().delete(
				DELETE_ALL_ENTRYSTATE_ENTRIES_BY_ENTRY_UID,
				new PrefixedParameter(prefix, entryUId));
	}

	@Override
	public void deleteAllEntryStatesOfPickListDefinitionByUId(
			String pickListUId) {

		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		/* 1. Delete pick list definition properties entry states. */
		this.deleteAllEntryStatesOfVsPropertiesByParentUId(pickListUId,
				ReferenceType.PICKLISTDEFINITION.name());
		
		/* 2. Delete all PL Entry properties entry states of the PL definition. */
		this.getSqlSessionTemplate().delete(
				DELETE_ALL_PLENTRY_PROPERTY_ENTRYSTATE_ENTRIES_OF_PL_DEFINITION,
				new PrefixedParameter(prefix, pickListUId));
		
		/* 3. Delete all PL Entry entry states of the PL Definition.*/
		this.getSqlSessionTemplate().delete(DELETE_ALL_PLENTRY_ENTRYSTATE_ENTRIES_OF_PL_DEFINITION,
				new PrefixedParameter(prefix, pickListUId));
		
		/* 4. Delete all entry states of PL definition.*/
		this.getSqlSessionTemplate().delete(DELETE_ALL_PL_DEFINITION_ENTRYSTATES,
				new PrefixedParameter(prefix, pickListUId));
	}

	@Override
	public void deleteAllEntryStatesOfPLEntryNodeByUId(
			String pickListEntryNodeUId) {

		/* 1. Delete all PL entry properties entry states. */
		this.deleteAllEntryStatesOfVsPropertiesByParentUId(
				pickListEntryNodeUId, ReferenceType.PICKLISTENTRY.name());
		
		/* 2. Delete all PL entry entry states. */
		this.deleteAllEntryStateEntriesByEntryUId(pickListEntryNodeUId);
	}
}