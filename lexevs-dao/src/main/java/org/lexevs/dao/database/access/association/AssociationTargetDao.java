package org.lexevs.dao.database.access.association;

import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;

public interface AssociationTargetDao extends LexGridSchemaVersionAwareDao {

	public String insertAssociationTarget(String codingSchemeUId, String associationPredicateUId, AssociationSource source, AssociationTarget target);

	public String updateAssociationTarget(String codingSchemeUId, String associationTargetUId, AssociationSource source, AssociationTarget target);

	public String updateVersionableChanges(String codingSchemeUId, String associationTargetUId, AssociationSource source, AssociationTarget target);

	public String getAssociationTargetUId(String codingSchemeUId, String associationInstanceId);

	public String insertHistoryAssociationTarget(String codingSchemeUId,
			String assnEntityTripleUId, Boolean assnQualExists, Boolean contextExists);

	public void deleteAssnTargetByUId(String codingSchemeUId,
			String associationTargetUId);

	public String getLatestRevision(String csUId, String targetUId);

	public void deleteAssociationQualificationsByAssociationTargetUId(
			String codingSchemeUId, String associationTargetUId);

}
