package org.lexevs.dao.database.service.association;

import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.Relations;
import org.lexevs.dao.database.service.DatabaseService;

public interface AssociationService extends DatabaseService {
	
	public void insertRelation(String codingSchemeUri, String version, Relations relation);
	
	public void insertAssociationSource(String codingSchemeUri, 
			String version, 
			String relationContainerName,
			String associationPredicateName,
			AssociationSource source);
}
