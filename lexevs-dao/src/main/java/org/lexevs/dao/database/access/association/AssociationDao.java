package org.lexevs.dao.database.access.association;

import java.util.List;

import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.Relations;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;
import org.lexevs.dao.database.access.association.batch.AssociationSourceBatchInsertItem;

public interface AssociationDao extends LexGridSchemaVersionAwareDao {
	
	public String insertAssociationPredicate(String codingSchemeId, String relationId, AssociationPredicate associationPredicate);
	
	public void insertAssociationQualifier(
			String codingSchemeId, 
			String associatableInstanceId, AssociationQualification qualifier);
	
	public String getAssociationPredicateId(String codingSchemeId, String relationContainerId, String associationPredicateName);
	
	public void insertAssociationSource(String codingSchemeId, String associationPredicateId, AssociationSource source);

	public void insertBatchAssociationSources(String codingSchemeId,
			List<AssociationSourceBatchInsertItem> batch);
	
	public void insertBatchAssociationSources(String codingSchemeId, String associationPredicateId,
			List<AssociationSource> batch);
	
	public String insertRelations(String codingSchemeId, Relations relations);
	
	public String getRelationsId(String codingSchemeId, String relationsName);

	public String insertIntoTransitiveClosure(
			String codingSchemeId, 
			String associationPredicateId,
			String sourceEntityCode, 
			String sourceEntityCodeNamesapce, 
			String targetEntityCode, 
			String targetEntityCodeNamespace);
}
