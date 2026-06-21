
package org.lexevs.dao.database.service.association;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.Relations;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.access.association.AssociationDao;
import org.lexevs.dao.database.access.association.AssociationDataDao;
import org.lexevs.dao.database.access.association.AssociationTargetDao;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.ibatis.association.parameter.BatchAssociationInsertBean;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.lexevs.dao.database.service.error.DatabaseErrorIdentifier;
import org.lexevs.dao.database.service.event.association.AssociationBatchInsertEvent;
import org.lexevs.dao.database.service.property.PropertyService;
import org.lexevs.dao.database.utility.DaoUtility;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class VersionableEventAssociationService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class VersionableEventAssociationService extends AbstractDatabaseService implements AssociationService{
	
	/** The property service. */
	private PropertyService propertyService = null;

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.association.AssociationService#getAssociationTripleByAssociationInstanceId(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public AssociationTriple getAssociationTripleByAssociationInstanceId(
			String codingSchemeUri, 
			String version, 
			String associationInstanceId) {
		CodingSchemeDao codingSchemeDao = this.getDaoManager().getCodingSchemeDao(codingSchemeUri, version);
		
		String codingSchemeUId = codingSchemeDao.
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
		
		AssociationTargetDao associationTargetDao = 
			this.getDaoManager().getAssociationTargetDao(codingSchemeUri, version);
		
		AssociationDataDao associationDataDao = 
			this.getDaoManager().getAssociationDataDao(codingSchemeUri, version);
		
		AssociationDao associationDao = 
			this.getDaoManager().getAssociationDao(codingSchemeUri, version);
		
		String associationTargetUid = 
			associationTargetDao.getAssociationTargetUId(codingSchemeUId, associationInstanceId);
		
		AssociationSource associationSource;
		if(StringUtils.isNotBlank(associationTargetUid)) {
			associationSource = associationTargetDao.getTripleByUid(codingSchemeUId, associationTargetUid);
		} else {
			String associationDataUid = associationDataDao.getAssociationDataUId(codingSchemeUId, associationInstanceId);
			associationSource = associationDataDao.getTripleByUid(codingSchemeUId, associationDataUid);
		}
		
		String relationsContainerName = associationDao.
			getRelationsContainerNameForAssociationInstanceId(
					codingSchemeUId, 
					associationInstanceId);
		
		String associationPredicateName = associationDao.
			getAssociationPredicateNameForAssociationInstanceId(
				codingSchemeUId, 
				associationInstanceId);
		
		return new AssociationTriple(associationSource, relationsContainerName, associationPredicateName);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.association.AssociationService#insertAssociationSource(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.LexGrid.relations.AssociationSource)
	 */
	@Transactional
	@DatabaseErrorIdentifier(errorCode=INSERT_ASSOCIATIONSOURCE_ERROR)
	public void insertAssociationSource(String codingSchemeUri, 
			String version, 
			String relationContainerName,
			String associationPredicateName,
			AssociationSource source){
		CodingSchemeDao codingSchemeDao = this.getDaoManager().getCodingSchemeDao(codingSchemeUri, version);
		
		String codingSchemeUId = codingSchemeDao.
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
		
		AssociationDao assocDao = this.getDaoManager().getAssociationDao(codingSchemeUri, version);
		
		String relationsUId = assocDao.getRelationUId(codingSchemeUId, relationContainerName);
		
		String associationPredicateUId = assocDao.
			getAssociationPredicateUIdByContainerName(codingSchemeUId, relationContainerName, associationPredicateName);
		
		Relations relations = assocDao.getRelationsByUId(codingSchemeUId, relationsUId, false);
		
		this.doInsertAssociationSource(codingSchemeUri, version, codingSchemeUId, relations, associationPredicateUId, 
				DaoUtility.createNonTypedList(source));
	}
	
	public void insertAssociationSourceBatch(String codingSchemeUri, 
			String version, 
			List<BatchAssociationInsertBean> sources){
		CodingSchemeDao codingSchemeDao = this.getDaoManager().getCodingSchemeDao(codingSchemeUri, version);
		
		String codingSchemeUId = codingSchemeDao.
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
		
		AssociationDao assocDao = this.getDaoManager().getAssociationDao(codingSchemeUri, version);
		List<BatchAssociationInsertBean> temp = new ArrayList<BatchAssociationInsertBean>();
		for(int i = 0; i < sources.size(); i++) {
		String relationsUId = assocDao.getRelationUId(codingSchemeUId, sources.get(i).getRelationsContainer());
		
		String associationPredicateUId = assocDao.
			getAssociationPredicateUIdByContainerName(
					codingSchemeUId, 
					sources.get(i).getRelationsContainer(), 
					sources.get(i).getAssociationPredicateId());
		
		Relations relations = assocDao.getRelationsByUId(codingSchemeUId, relationsUId, false);
		
		this.runPreInsertionListeners(codingSchemeUri, version, codingSchemeUId, relations, associationPredicateUId, 
				DaoUtility.createNonTypedList(sources.get(i).getSource()));
		temp.add(sources.get(i));
		if(temp.size() >= 50) {
			assocDao.insertMybatisBatchAssociationSources(codingSchemeUId,temp);
			temp.clear();
		}
		}
		if(temp.size() > 0) {
			assocDao.insertMybatisBatchAssociationSources(codingSchemeUId,temp);
			temp.clear();
		}
	}
	
	private void runPreInsertionListeners(String codingSchemeUri, String version, String codingSchemeUId,
			Relations relations, String associationPredicateUId, List<AssociationSource> sources) {
		
		this.firePreBatchAssociationInsertEvent(new AssociationBatchInsertEvent(
				codingSchemeUri, version, relations,sources));

	}

	/**
	 * Insert association predicate.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param relationsName the relations name
	 * @param predicate the predicate
	 */
	@Transactional
	@DatabaseErrorIdentifier(errorCode=INSERT_ASSOCIATIONPREDICATE_ERROR)
	public void insertAssociationPredicate(
			String codingSchemeUri, String version, String relationsName, AssociationPredicate predicate) {
		String codingSchemeId = this.getDaoManager().getCodingSchemeDao(codingSchemeUri, version).
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
		
		AssociationDao associationDao = this.getDaoManager().getAssociationDao(codingSchemeUri, version);
		String relationId = associationDao.getRelationUId(codingSchemeId, relationsName);
		
		this.doInsertAssociationPredicate(codingSchemeUri, version, codingSchemeId, relationId, predicate);
	}
	
	/**
	 * Do insert association predicate.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * @param codingSchemeId the coding scheme id
	 * @param relationsId the relations id
	 * @param predicate the predicate
	 */
	protected void doInsertAssociationPredicate(String codingSchemeUri, 
			String codingSchemeVersion, String codingSchemeId, String relationsId, AssociationPredicate predicate) {
		AssociationDao associationDao = this.getDaoManager().getAssociationDao(codingSchemeUri, codingSchemeVersion);
		associationDao.
			insertAssociationPredicate(
					codingSchemeId, 
					relationsId, 
					predicate,
					true);
	}
	
	/**
	 * Do insert association source.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * @param codingSchemeId the coding scheme id
	 * @param predicateId the predicate id
	 * @param sources the sources
	 * @param relations the relations
	 */
	protected void doInsertAssociationSource(String codingSchemeUri, 
			String codingSchemeVersion, String codingSchemeId, Relations relations, String predicateId, List<AssociationSource> sources) {
		AssociationDao associationDao = this.getDaoManager().getAssociationDao(codingSchemeUri, codingSchemeVersion);

		this.firePreBatchAssociationInsertEvent(new AssociationBatchInsertEvent(
						codingSchemeUri, codingSchemeVersion, relations,
						sources));

		associationDao.insertBatchAssociationSources(codingSchemeId,predicateId, sources);
	}

	/**
	 * Gets the property service.
	 * 
	 * @return the propertyService
	 */
	public PropertyService getPropertyService() {
		return propertyService;
	}

	/**
	 * Sets the property service.
	 * 
	 * @param propertyService the propertyService to set
	 */
	public void setPropertyService(PropertyService propertyService) {
		this.propertyService = propertyService;
	}
}