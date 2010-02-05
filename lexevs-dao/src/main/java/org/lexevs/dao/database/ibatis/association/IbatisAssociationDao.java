package org.lexevs.dao.database.ibatis.association;

import java.util.List;

import org.LexGrid.relations.Association;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.junit.Assert;
import org.lexevs.dao.database.access.association.AssociationDao;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.association.parameter.InsertAssociationQualificationOrUsageContextBean;
import org.lexevs.dao.database.ibatis.association.parameter.InsertAssociationSourceBean;
import org.lexevs.dao.database.ibatis.association.parameter.InsertRelationsBean;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;

public class IbatisAssociationDao extends AbstractIbatisDao implements AssociationDao {

	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");
	
	private static String INSERT_RELATIONS_SQL = "insertRelations";
	private static String INSERT_ENTITY_ASSNS_TO_ENTITY_SQL = "insertEntityAssnsToEntity";
	private static String INSERT_ASSOCIATION_QUAL_OR_CONTEXT_SQL = "insertAssociationQualificationOrUsageContext";
	
	private VersionsDao versionsDao;	

	public String getAssociationPredicateId(String codingSchemeId,
			String relationContainerName, String associationPredicateName) {
		return null;
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

	public String insertAssociation(String codingScheme, String version,
			Association association) {
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


}
