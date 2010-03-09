package org.lexevs.dao.database.service.association;

import java.util.Arrays;
import java.util.List;

import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.Relations;
import org.lexevs.dao.database.access.association.AssociationDao;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.lexevs.dao.database.utility.DaoUtility;
import org.springframework.transaction.annotation.Transactional;

public class VersionableEventAssociationService extends AbstractDatabaseService implements AssociationService{
	
	@Transactional
	public void insertAssociationSource(String codingSchemeUri, 
			String version, 
			String relationContainerName,
			String associationPredicateName,
			AssociationSource source){
		String codingSchemeId = this.getDaoManager().getCodingSchemeDao(codingSchemeUri, version).
			getCodingSchemeIdByUriAndVersion(codingSchemeUri, version);
		
		String associationPredicateId = this.getDaoManager().getAssociationDao(codingSchemeUri, version).
			getAssociationPredicateId(codingSchemeId, relationContainerName, associationPredicateName);
		
		this.doInsertAssociationSource(codingSchemeUri, codingSchemeUri, codingSchemeId, associationPredicateId, 
				DaoUtility.createList(AssociationSource.class, source));
	}

	@Transactional
	public void insertRelation(String codingSchemeUri, String version,
			Relations relation) {
		String codingSchemeId = this.getDaoManager().getCodingSchemeDao(codingSchemeUri, version).
			getCodingSchemeIdByUriAndVersion(codingSchemeUri, version);
		
		this.doInsertRelation(codingSchemeUri, version, codingSchemeId, relation);
	}
	
	@Transactional
	public void insertAssociationPredicate(
			String codingSchemeUri, String version, String relationsName, AssociationPredicate predicate) {
		String codingSchemeId = this.getDaoManager().getCodingSchemeDao(codingSchemeUri, version).
			getCodingSchemeIdByUriAndVersion(codingSchemeUri, version);
		
		AssociationDao associationDao = this.getDaoManager().getAssociationDao(codingSchemeUri, version);
		String relationId = associationDao.getRelationsId(codingSchemeUri, relationsName);
		
		this.doInsertAssociationPredicate(codingSchemeUri, codingSchemeUri, codingSchemeId, relationId, predicate);
	}
	
	protected void doInsertRelation(String codingSchemeUri, 
			String codingSchemeVersion, String codingSchemeId, Relations relations) {

		String relationsId = this.getDaoManager().getAssociationDao(codingSchemeUri, codingSchemeVersion)
			.insertRelations(codingSchemeId, relations);
		
		for(AssociationPredicate predicate : relations.getAssociationPredicate()) {
			this.doInsertAssociationPredicate(codingSchemeUri, codingSchemeVersion, codingSchemeId, relationsId, predicate);
		}
	}
	
	protected void doInsertAssociationPredicate(String codingSchemeUri, 
			String codingSchemeVersion, String codingSchemeId, String relationsId, AssociationPredicate predicate) {
		AssociationDao associationDao = this.getDaoManager().getAssociationDao(codingSchemeUri, codingSchemeVersion);
		String predicateId = associationDao.insertAssociationPredicate(codingSchemeId, relationsId, predicate);
		
		this.doInsertAssociationSource(codingSchemeUri, codingSchemeVersion, codingSchemeId, predicateId, Arrays.asList(predicate.getSource()));
		
	}
	
	protected void doInsertAssociationSource(String codingSchemeUri, 
			String codingSchemeVersion, String codingSchemeId, String predicateId, List<AssociationSource> sources) {
		AssociationDao associationDao = this.getDaoManager().getAssociationDao(codingSchemeUri, codingSchemeVersion);
		associationDao.insertBatchAssociationSources(codingSchemeId, predicateId, sources);
	}
}
