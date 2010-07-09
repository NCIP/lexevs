package org.lexevs.dao.database.service.relation;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.relations.AssociationData;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.lexevs.dao.database.access.association.AssociationDao;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.access.property.PropertyDao;
import org.lexevs.dao.database.access.property.PropertyDao.PropertyType;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.access.versions.VersionsDao.EntryStateType;
import org.lexevs.dao.database.service.RevisableAbstractDatabaseService;
import org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId;
import org.lexevs.dao.database.service.association.AssociationDataService;
import org.lexevs.dao.database.service.association.AssociationTargetService;
import org.lexevs.dao.database.service.error.DatabaseErrorIdentifier;
import org.lexevs.dao.database.service.property.PropertyService;
import org.springframework.transaction.annotation.Transactional;

public class VersionableEventRelationService extends RevisableAbstractDatabaseService<Relations,CodingSchemeUriVersionBasedEntryId> implements RelationService {

	private PropertyService propertyService = null;
	private AssociationTargetService assocTargetService = null;
	private AssociationDataService assocDataService = null;

	@Override
	protected Relations addDependentAttributesByRevisionId(
			CodingSchemeUriVersionBasedEntryId id, String entryUid,
			Relations entry) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	@Override
	protected void doInsertDependentChanges(
			CodingSchemeUriVersionBasedEntryId id, Relations revisedEntry)
			throws LBException {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	@Override
	protected boolean entryStateExists(CodingSchemeUriVersionBasedEntryId id,
			String entryStateUid) {
		String codingSchemeUid = this.getCodingSchemeUid(id);
		
		return 
			this.getAssociationDao(id).entryStateExists(codingSchemeUid, entryStateUid);
	}

	@Override
	protected Relations getCurrentEntry(CodingSchemeUriVersionBasedEntryId id,
			String entryUId) {
		String codingSchemeUid = this.getCodingSchemeUid(id);
		
		return 
			this.getAssociationDao(id).getRelationsByUId(codingSchemeUid, entryUId);
	}

	@Override
	protected String getCurrentEntryStateUid(
			CodingSchemeUriVersionBasedEntryId id, String entryUid) {
		String codingSchemeUid = this.getCodingSchemeUid(id);

		return 
			this.getAssociationDao(id).getRelationEntryStateUId(codingSchemeUid, entryUid);
	}

	@Override
	protected String getEntryUid(CodingSchemeUriVersionBasedEntryId id,
			Relations entry) {
		String codingSchemeUid = this.getCodingSchemeUid(id);
		
		return 
			this.getAssociationDao(id).getRelationUId(codingSchemeUid, entry.getContainerName());
	}

	@Override
	protected Relations getHistoryEntryByRevisionId(
			CodingSchemeUriVersionBasedEntryId id, String entryUid,
			String revisionId) {
		String codingSchemeUid = this.getCodingSchemeUid(id);
		
		return 
			this.getAssociationDao(id).getHistoryRelationByRevisionId(codingSchemeUid, entryUid, revisionId);
	}

	@Override
	protected String getLatestRevisionId(CodingSchemeUriVersionBasedEntryId id,
			String entryUId) {
		String codingSchemeUid = this.getCodingSchemeUid(id);

		return 
			this.getAssociationDao(id).getRelationLatestRevision(codingSchemeUid, entryUId);
	}

	@Override
	protected void insertIntoHistory(CodingSchemeUriVersionBasedEntryId id,
			Relations currentEntry, String entryUId) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	@Override
	protected String updateEntityVersionableAttributes(
			CodingSchemeUriVersionBasedEntryId id, String entryUId,
			Relations revisedEntity) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.association.AssociationService#insertRelation(java.lang.String, java.lang.String, org.LexGrid.relations.Relations)
	 */
	@Transactional
	@DatabaseErrorIdentifier(errorCode=INSERT_RELATION_ERROR)
	public void insertRelation(String codingSchemeUri, String version,
			Relations relation) {
		String codingSchemeUId = this.getDaoManager().getCodingSchemeDao(codingSchemeUri, version).
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
		
		this.getDaoManager().getAssociationDao(codingSchemeUri, version)
		.insertRelations(
				codingSchemeUId, 
				relation, 
				true);
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	@DatabaseErrorIdentifier(errorCode=UPDATE_RELATION_ERROR)
	public void updateRelation(String codingSchemeUri, String version,
			Relations relation) throws LBException {

		AssociationDao associationDao = this.getDaoManager().getAssociationDao(
				codingSchemeUri, version);
		
		VersionsDao versionsDao = getDaoManager().getVersionsDao(codingSchemeUri, version);
		
		String codingSchemeUId = this.getDaoManager().getCodingSchemeDao(
				codingSchemeUri, version).getCodingSchemeUIdByUriAndVersion(
				codingSchemeUri, version);

		String relationUId = associationDao.getRelationUId(codingSchemeUId,
				relation.getContainerName());

		/* 1. insert current relation data into history. */
		String prevEntryStateUId = associationDao.insertHistoryRelation(
				codingSchemeUId, relationUId, relation);		
		
		/* 2. update the attributes of the relation. */
		String entryStateUId = associationDao.updateRelation(codingSchemeUId, relationUId, relation);
		
		/* 3. register entrystate details for the entity.*/
		versionsDao.insertEntryState(
				codingSchemeUId,
				entryStateUId, relationUId,
				EntryStateType.RELATION,
				prevEntryStateUId, 
				relation.getEntryState());
		
		/* 4. apply dependent changes for the entity.*/			
		this.insertRelationDependentChanges(codingSchemeUri, version, relation);
	}
	
	@Override
	@DatabaseErrorIdentifier(errorCode=REMOVE_RELATION_ERROR)
	@Transactional
	public void removeRelation(String codingSchemeUri, String version,
			Relations relation) {

		CodingSchemeDao codingSchemeDao = getDaoManager().getCodingSchemeDao(codingSchemeUri, version);
		
		AssociationDao associationDao = this.getDaoManager().getAssociationDao(
				codingSchemeUri, version);
		
		PropertyDao propertyDao = getDaoManager().getPropertyDao(codingSchemeUri, version);
		
		VersionsDao versionsDao = getDaoManager().getVersionsDao(codingSchemeUri, version);
		
		String codingSchemeUId = codingSchemeDao.
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
		
		String relationUId = associationDao.getRelationUId(codingSchemeUId, relation.getContainerName());
		
		/* 1. Delete all entry state entries of relation. */
		versionsDao.deleteAllEntryStateOfRelation(codingSchemeUId, relationUId);
		
		/* 2. Delete all entity association qualifications for the relation. */
		associationDao.deleteAssociationQualificationsByRelationUId(codingSchemeUId, relationUId);
		
		/* 3. Delete all relation properties. */
		propertyDao.deleteAllPropertiesOfParent(codingSchemeUId, relationUId, PropertyType.RELATION);
		
		/* 4. Delete the relation. */
		associationDao.removeRelationByUId(codingSchemeUId, relationUId);
	}
	
	@Override
	@DatabaseErrorIdentifier(errorCode=INSERT_RELATION_DEPENDENT_CHANGES_ERROR)
	@Transactional(rollbackFor=Exception.class)
	public void insertRelationDependentChanges(String codingSchemeUri,
			String version, Relations relation) throws LBException {

		CodingSchemeDao codingSchemeDao = getDaoManager().getCodingSchemeDao(codingSchemeUri, version);
		
		AssociationDao associationDao = this.getDaoManager().getAssociationDao(
				codingSchemeUri, version);
		
		VersionsDao versionsDao = getDaoManager().getVersionsDao(codingSchemeUri, version);

		String codingSchemeUId = codingSchemeDao
				.getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);

		String relationUId = associationDao.getRelationUId(codingSchemeUId,
				relation.getContainerName());
		
		if (relation.getEntryState().getChangeType() == ChangeType.DEPENDENT) {
			doAddRelationDependentEntry(relation, associationDao, versionsDao,
					codingSchemeUId, relationUId);
		}
		
		if (relation.getProperties() != null) {
			Property[] propertyList = relation.getProperties().getProperty();

			for (int i = 0; i < propertyList.length; i++) {
				
				propertyService.reviseRelationProperty(codingSchemeUri,
						version, relation.getContainerName(), propertyList[i]);
			}
		}
		
		AssociationPredicate[] assocPredicateList = relation.getAssociationPredicate();
		
		for (int i = 0; i < assocPredicateList.length; i++) {

			AssociationSource[] assocSourceList = assocPredicateList[i]
					.getSource();

			for (int j = 0; j < assocSourceList.length; j++) {

				AssociationTarget[] assocTarget = assocSourceList[j]
						.getTarget();

				for (int k = 0; k < assocTarget.length; k++) {

					if (associationDao
							.getAssociationPredicateUIdByContainerUId(
									codingSchemeUId, relationUId,
									assocPredicateList[i].getAssociationName()) == null) {
						
						associationDao.insertAssociationPredicate(
								codingSchemeUId, relationUId,
								assocPredicateList[i], false);
					}
					
					assocTargetService.revise(codingSchemeUri, version,
							relation.getContainerName(), assocPredicateList[i]
									.getAssociationName(), assocSourceList[j],
							assocTarget[k]);
				}

				AssociationData[] assocData = assocSourceList[j]
						.getTargetData();

				for (int k = 0; k < assocData.length; k++) {

					if (associationDao
							.getAssociationPredicateUIdByContainerUId(
									codingSchemeUId, relationUId,
									assocPredicateList[i].getAssociationName()) == null) {

						associationDao.insertAssociationPredicate(codingSchemeUId,
								relationUId, assocPredicateList[i], false);
					}
					
					assocDataService.revise(codingSchemeUri, version, relation
							.getContainerName(), assocPredicateList[i]
							.getAssociationName(), assocSourceList[j],
							assocData[k]);
				}
			}
		}
	}

	@Override
	@DatabaseErrorIdentifier(errorCode=INSERT_RELATION_VERSIONABLE_CHANGES_ERROR)
	@Transactional(rollbackFor=Exception.class)
	public void insertRelationVersionableChanges(String codingSchemeUri,
			String version, Relations relation) throws LBException {

		AssociationDao associationDao = this.getDaoManager().getAssociationDao(
				codingSchemeUri, version);
		
		VersionsDao versionsDao = getDaoManager().getVersionsDao(codingSchemeUri, version);
		
		String codingSchemeUId = this.getDaoManager().getCodingSchemeDao(
				codingSchemeUri, version).getCodingSchemeUIdByUriAndVersion(
				codingSchemeUri, version);

		String relationUId = associationDao.getRelationUId(codingSchemeUId,
				relation.getContainerName());

		/* 1. insert current relation data into history. */
		String prevEntryStateUId = associationDao.insertHistoryRelation(
				codingSchemeUId, relationUId, relation);		
		
		/* 2. update the versionable attributes of the relation. */
		String entryStateUId = associationDao.updateRelationVersionableChanges(codingSchemeUId, relationUId, relation);
		
		/* 3. register entrystate details for the entity.*/
		versionsDao.insertEntryState(
				codingSchemeUId,
				entryStateUId, 
				relationUId,
				EntryStateType.RELATION, 
				prevEntryStateUId, 
				relation
				.getEntryState());
		
		/* 4. apply dependent changes for the entity.*/
		this.insertRelationDependentChanges(codingSchemeUri, version, relation);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void revise(String codingSchemeUri, String version,
			Relations relation) throws LBException {

		if (validRevision(new CodingSchemeUriVersionBasedEntryId(codingSchemeUri, version), relation)) {
			ChangeType changeType = relation.getEntryState().getChangeType();

			if (changeType == ChangeType.NEW) {

				this.insertRelation(codingSchemeUri, version, relation);
			} else if (changeType == ChangeType.REMOVE) {

				this.removeRelation(codingSchemeUri, version, relation);
			} else if (changeType == ChangeType.MODIFY) {

				this.updateRelation(codingSchemeUri, version, relation);
			} else if (changeType == ChangeType.DEPENDENT) {

				this.insertRelationDependentChanges(codingSchemeUri, version,
						relation);
			} else if (changeType == ChangeType.VERSIONABLE) {

				this.insertRelationVersionableChanges(codingSchemeUri, version,
						relation);
			}
		}
	}
	
	/**
	 * @return the propertyService
	 */
	public PropertyService getPropertyService() {
		return propertyService;
	}

	/**
	 * @param propertyService the propertyService to set
	 */
	public void setPropertyService(PropertyService propertyService) {
		this.propertyService = propertyService;
	}

	/**
	 * @return the assocTargetService
	 */
	public AssociationTargetService getAssocTargetService() {
		return assocTargetService;
	}

	/**
	 * @param assocTargetService the assocTargetService to set
	 */
	public void setAssocTargetService(AssociationTargetService assocTargetService) {
		this.assocTargetService = assocTargetService;
	}

	/**
	 * @return the assocDataService
	 */
	public AssociationDataService getAssocDataService() {
		return assocDataService;
	}

	/**
	 * @param assocDataService the assocDataService to set
	 */
	public void setAssocDataService(AssociationDataService assocDataService) {
		this.assocDataService = assocDataService;
	}

	private boolean doAddRelationDependentEntry(Relations relation,
			AssociationDao associationDao, VersionsDao versionsDao,
			String codingSchemeUId, String relationUId) {
		String prevEntryStateUId = associationDao.getRelationEntryStateUId(codingSchemeUId, relationUId);
	
		if( !associationDao.entryStateExists(codingSchemeUId, prevEntryStateUId)) {
			EntryState entryState = new EntryState();
	
			entryState.setChangeType(ChangeType.NEW);
			entryState.setRelativeOrder(0L);
	
			versionsDao.insertEntryState(
					codingSchemeUId,
					prevEntryStateUId,
					relationUId, 
					EntryStateType.RELATION, 
					null, 
					entryState);	
		}
		
		String entryStateUId = versionsDao.insertEntryState(
				codingSchemeUId,
				relationUId,
				EntryStateType.RELATION,
				prevEntryStateUId, 
				relation.getEntryState());
		
		associationDao.updateRelationEntryStateUId(codingSchemeUId, relationUId, entryStateUId);
		
		return true;
	}
	
	private AssociationDao getAssociationDao(CodingSchemeUriVersionBasedEntryId id) {
		return this.getDaoManager().getAssociationDao(id.getCodingSchemeUri(), id.getCodingSchemeVersion());
	}
}
