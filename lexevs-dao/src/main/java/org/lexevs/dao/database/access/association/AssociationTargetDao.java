
package org.lexevs.dao.database.access.association;

import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;
import org.mybatis.spring.SqlSessionTemplate;

public interface AssociationTargetDao extends LexGridSchemaVersionAwareDao {

	public String insertAssociationTarget(String codingSchemeUId, String associationPredicateUId, AssociationSource source, AssociationTarget target);

	public String insertAssociationTarget(
			String codingSchemeUId, 
			String associationPredicateUId, 
			String sourceEntityCode, 
			String sourceEntityCodeNamespace,
			AssociationTarget target);

	public String updateAssociationTarget(String codingSchemeUId, String associationTargetUId, AssociationTarget target);

	public String updateVersionableChanges(String codingSchemeUId, String associationTargetUId, AssociationTarget target);

	public String getAssociationTargetUId(String codingSchemeUId, String associationInstanceId);
	
	public AssociationSource getTripleByUid(String codingSchemeUId, String tripleUid);

	public String insertHistoryAssociationTarget(String codingSchemeUId,
			String assnEntityTripleUId, Boolean assnQualExists, Boolean contextExists);

	public void deleteAssnTargetByUId(String codingSchemeUId,
			String associationTargetUId);

	public String getLatestRevision(String csUId, String targetUId);

	public void deleteAssociationMultiAttribsByAssociationTargetUId(
			String codingSchemeUId, String associationTargetUId);

	public String insertAssociationTarget(String codingSchemeUId,
			String associationPredicateUId, AssociationSource source,
			AssociationTarget target, SqlSessionTemplate session);

	public boolean entryStateExists(String codingSchemeUId, String entryStateUId);
	
	public String getEntryStateUId(String codingSchemeUId, String associationTargetUid);
	
	public AssociationSource getHistoryTripleByRevision(
			String codingSchemeUId, 
			String tripleUid, 
			String revisionId);
}