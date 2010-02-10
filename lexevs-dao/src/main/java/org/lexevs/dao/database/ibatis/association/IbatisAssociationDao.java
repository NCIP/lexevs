package org.lexevs.dao.database.ibatis.association;

import java.util.List;

import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.junit.Assert;
import org.lexevs.dao.database.access.association.AssociationDao;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.association.parameter.InsertAssociationQualificationOrUsageContextBean;
import org.lexevs.dao.database.ibatis.association.parameter.InsertAssociationSourceBean;
import org.lexevs.dao.database.ibatis.association.parameter.InsertRelationsBean;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTuple;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;

public class IbatisAssociationDao extends AbstractIbatisDao implements AssociationDao {

	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");
	
	private static String INSERT_RELATIONS_SQL = "insertRelations";
	private static String INSERT_ENTITY_ASSNS_TO_ENTITY_SQL = "insertEntityAssnsToEntity";
	private static String INSERT_ASSOCIATION_QUAL_OR_CONTEXT_SQL = "insertAssociationQualificationOrUsageContext";
	private static String GET_ASSOCIATION_INSTANCE_KEY_SQL = "getAccociationInstanceKey";
	
	private VersionsDao versionsDao;
	private CodingSchemeDao codingSchemeDao;

	public String getAssociationPredicateId(String codingSchemeId,
			String relationContainerName, String associationPredicateName) {
		return null;
	}
	
	public void insertRelations(String codingSchemeName, String version,
			Relations relations) {
		String codingSchemeId = codingSchemeDao.getCodingSchemeId(codingSchemeName, version);
		this.insertRelations(
				codingSchemeId, 
				relations);
	}

	public String insertRelations(String codingSchemeId,
			Relations relations) {
		String relationsId = this.createUniqueId();
		String entryStateId = this.createUniqueId();
		
		InsertRelationsBean bean = new InsertRelationsBean();
		bean.setEntryStateId(entryStateId);
		bean.setId(relationsId);
		bean.setCodingSchemeId(codingSchemeId);
		bean.setRelations(relations);
		bean.setPrefix(this.getPrefixResolver().resolvePrefix());
		
		this.getSqlMapClientTemplate().insert(INSERT_RELATIONS_SQL, bean);
		
		return relationsId;
	}

	public String insertAssociationPredicate(String codingScheme, String version,
			AssociationPredicate associationPredicate) {
		// TODO Auto-generated method stub
		return null;
	}

	public void insertAssociationSource(String codingSchemeId,
			String associationPredicateId, AssociationSource source) {
		Assert.assertTrue("Must Insert at least ONE AssociationTarget per AssociationSource.", source.getTarget().length > 0);
		
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		for(AssociationTarget target : source.getTarget()){
			String associationTargetId = this.createUniqueId();
			String entryStateId = this.createUniqueId();
			
			InsertAssociationSourceBean bean = new InsertAssociationSourceBean();
			bean.setPrefix(prefix);
			bean.setAssociationPredicateId(associationPredicateId);
			bean.setAssociationSource(source);
			bean.setAssociationTarget(target);
			bean.setEntryStateId(entryStateId);
			bean.setId(associationTargetId);
			this.getSqlMapClientTemplate().insert(INSERT_ENTITY_ASSNS_TO_ENTITY_SQL, bean);
			
			versionsDao.insertEntryState(codingSchemeId, entryStateId, associationTargetId, "associationSource", null, target.getEntryState());
			
			for(AssociationQualification qual : target.getAssociationQualification()){
				String qualId = this.createUniqueId();
				
				InsertAssociationQualificationOrUsageContextBean qualBean = new InsertAssociationQualificationOrUsageContextBean();
				qualBean.setAssociationTargetId(associationTargetId);
				qualBean.setId(qualId);
				qualBean.setPrefix(prefix);
				qualBean.setQualifierName(qual.getAssociationQualifier());
				qualBean.setQualifierValue(qual.getQualifierText().getContent());
				
				this.getSqlMapClientTemplate().insert(
						INSERT_ASSOCIATION_QUAL_OR_CONTEXT_SQL, 
						qualBean);
			}
			
			for(String context : target.getUsageContext()){
				String contextId = this.createUniqueId();
				
				InsertAssociationQualificationOrUsageContextBean contextBean = new InsertAssociationQualificationOrUsageContextBean();
				contextBean.setAssociationTargetId(associationTargetId);
				contextBean.setId(contextId);
				contextBean.setPrefix(prefix);
				contextBean.setQualifierName(SQLTableConstants.TBLCOLVAL_USAGECONTEXT);
				contextBean.setQualifierValue(context);
				
				this.getSqlMapClientTemplate().insert(
						INSERT_ASSOCIATION_QUAL_OR_CONTEXT_SQL, 
						contextBean);
			}
		}
	}
	

	public void insertAssociationQualifier(String codingSchemeId,
			String associationInstanceId, AssociationQualification qualifier) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		String associationTargetId = this.getKeyForAssociationInstanceId(codingSchemeId, associationInstanceId);
		
		String qualId = this.createUniqueId();
		
		InsertAssociationQualificationOrUsageContextBean contextBean = new InsertAssociationQualificationOrUsageContextBean();
		contextBean.setAssociationTargetId(associationTargetId);
		contextBean.setId(qualId);
		contextBean.setPrefix(prefix);
		contextBean.setQualifierName(qualifier.getAssociationQualifier());
		contextBean.setQualifierValue(qualifier.getQualifierText().getContent());
		
		this.getSqlMapClientTemplate().insert(
				INSERT_ASSOCIATION_QUAL_OR_CONTEXT_SQL, 
				contextBean);
	}
	
	protected String getKeyForAssociationInstanceId(String codingSchemeId, String associationInstanceId){
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeId);
		
		return (String) this.getSqlMapClientTemplate().queryForObject(
				
				GET_ASSOCIATION_INSTANCE_KEY_SQL, 
				new PrefixedParameterTuple(prefix, codingSchemeId, associationInstanceId));
	}
	
	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createList(supportedDatebaseVersion, LexGridSchemaVersion.class);
	}

	public void setVersionsDao(VersionsDao versionsDao) {
		this.versionsDao = versionsDao;
	}

	public VersionsDao getVersionsDao() {
		return versionsDao;
	}

	public void setCodingSchemeDao(CodingSchemeDao codingSchemeDao) {
		this.codingSchemeDao = codingSchemeDao;
	}

	public CodingSchemeDao getCodingSchemeDao() {
		return codingSchemeDao;
	}

	


}
