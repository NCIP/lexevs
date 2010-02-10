package org.lexevs.dao.database.access.association;

import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.Relations;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;

public interface AssociationDao extends LexGridSchemaVersionAwareDao {
	
	public String insertRelations(String codingSchemeId,
			Relations relations);

	public String insertAssociationPredicate(String codingScheme, String version, AssociationPredicate associationPredicate);
	
	public void insertAssociationQualifier(
			String codingSchemeId, 
			String associatableInstanceId, AssociationQualification qualifier);
	
	public String getAssociationPredicateId(String codingSchemeId, String relationContainerName, String associationPredicateName);
	
	public void insertAssociationSource(String codingSchemeId, String associationPredicateId, AssociationSource source);

	public void insertRelations(String codingSchemeName, String version,  Relations relations);

}
