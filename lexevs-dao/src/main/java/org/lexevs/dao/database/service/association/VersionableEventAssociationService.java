package org.lexevs.dao.database.service.association;

import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.Relations;
import org.lexevs.dao.database.service.AbstractDatabaseService;

public class VersionableEventAssociationService extends AbstractDatabaseService implements AssociationService{
	
	public void insertAssociationSource(String codingSchemeUri, 
			String version, 
			String relationContainerName,
			String associationPredicateName,
			AssociationSource source){
		String codingSchemeId = this.getDaoManager().getCodingSchemeDao(codingSchemeUri, version).
			getCodingSchemeIdByUriAndVersion(codingSchemeUri, version);
		
		String associationPredicateId = this.getDaoManager().getAssociationDao(codingSchemeUri, version).
			getAssociationPredicateId(codingSchemeId, relationContainerName, associationPredicateName);
		
		this.getDaoManager().getAssociationDao(codingSchemeUri, version)
			.insertAssociationSource(codingSchemeId, associationPredicateId, source);
	}

	public String insertRelation(String codingSchemeUri, String version,
			Relations relation) {
		String codingSchemeId = this.getDaoManager().getCodingSchemeDao(codingSchemeUri, version).
			getCodingSchemeIdByUriAndVersion(codingSchemeUri, version);
		return
			this.getDaoManager().getAssociationDao(codingSchemeUri, version).insertRelations(codingSchemeId, relation);
	}
}
