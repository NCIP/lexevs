
package org.lexevs.dao.database.service.relation;

import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.commonTypes.Properties;
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

/**
 * The Class VersionableEventRelationService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class VersionableEventRelationService extends RevisableAbstractDatabaseService<Relations,CodingSchemeUriVersionBasedEntryId> implements RelationService {

	/** The property service. */
	private PropertyService propertyService = null;
	
	/** The assoc target service. */
	private AssociationTargetService assocTargetService = null;
	
	/** The assoc data service. */
	private AssociationDataService assocDataService = null;

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.RevisableAbstractDatabaseService#addDependentAttributesByRevisionId(org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId, java.lang.String, org.LexGrid.commonTypes.Versionable, java.lang.String)
	 */
	@Override
	protected Relations addDependentAttributesByRevisionId(
			CodingSchemeUriVersionBasedEntryId id, String entryUid,
			Relations entry, String revisionId) {
		entry.setProperties(new Properties());
		
		List<Property> properties = 
			propertyService.resolvePropertiesOfRelationByRevision(
					id.getCodingSchemeUri(), 
					id.getCodingSchemeVersion(), 
					entry.getContainerName(), 
					revisionId);
		
		//TODO: Get dependent association source/targets
		
		entry.getProperties().setProperty(properties);
		
		return entry;
	
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.RevisableAbstractDatabaseService#doInsertDependentChanges(org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId, org.LexGrid.commonTypes.Versionable)
	 */
	@Override
	protected void doInsertDependentChanges(
			CodingSchemeUriVersionBasedEntryId id, Relations revisedEntry)
		throws LBException {
		
		CodingSchemeDao codingSchemeDao = getDaoManager().getCodingSchemeDao(
				id.getCodingSchemeUri(), id.getCodingSchemeVersion());

		AssociationDao associationDao = this.getDaoManager().getAssociationDao(
				id.getCodingSchemeUri(), id.getCodingSchemeVersion());

		String codingSchemeUId = codingSchemeDao
				.getCodingSchemeUIdByUriAndVersion(id.getCodingSchemeUri(), id
						.getCodingSchemeVersion());

		String relationUId = associationDao.getRelationUId(codingSchemeUId,
				revisedEntry.getContainerName());
		
		if (revisedEntry.getEntryState().getChangeType() == ChangeType.DEPENDENT) {
			doAddRelationDependentEntry(id, revisedEntry);
		}
		
		if(revisedEntry.getProperties() != null && revisedEntry.getProperties().getProperty() != null) {
			for(Property property : revisedEntry.getProperties().getProperty()) {
				propertyService.reviseRelationProperty(
						id.getCodingSchemeUri(), 
						id.getCodingSchemeVersion(), 
						revisedEntry.getContainerName(), 
						property);
			}
		}
		
		AssociationPredicate[] assocPredicateList = revisedEntry.getAssociationPredicate();
		
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
					
					assocTargetService.revise(id.getCodingSchemeUri(), id.getCodingSchemeVersion(),
							revisedEntry.getContainerName(), assocPredicateList[i]
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
					
					assocDataService.revise(id.getCodingSchemeUri(), id.getCodingSchemeVersion(), revisedEntry
							.getContainerName(), assocPredicateList[i]
							.getAssociationName(), assocSourceList[j],
							assocData[k]);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.RevisableAbstractDatabaseService#entryStateExists(org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId, java.lang.String)
	 */
	@Override
	protected boolean entryStateExists(CodingSchemeUriVersionBasedEntryId id,
			String entryStateUid) {
		String codingSchemeUid = this.getCodingSchemeUid(id);
		
		return 
			this.getAssociationDao(id).entryStateExists(codingSchemeUid, entryStateUid);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.RevisableAbstractDatabaseService#getCurrentEntry(org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId, java.lang.String)
	 */
	@Override
	protected Relations getCurrentEntry(CodingSchemeUriVersionBasedEntryId id,
			String entryUId) {
		String codingSchemeUid = this.getCodingSchemeUid(id);
		
		return 
			this.getAssociationDao(id).getRelationsByUId(codingSchemeUid, entryUId, true);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.RevisableAbstractDatabaseService#getCurrentEntryStateUid(org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId, java.lang.String)
	 */
	@Override
	protected String getCurrentEntryStateUid(
			CodingSchemeUriVersionBasedEntryId id, String entryUid) {
		String codingSchemeUid = this.getCodingSchemeUid(id);

		return 
			this.getAssociationDao(id).getRelationEntryStateUId(codingSchemeUid, entryUid);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.RevisableAbstractDatabaseService#getEntryUid(org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId, org.LexGrid.commonTypes.Versionable)
	 */
	@Override
	protected String getEntryUid(CodingSchemeUriVersionBasedEntryId id,
			Relations entry) {
		String codingSchemeUid = this.getCodingSchemeUid(id);
		
		return 
			this.getAssociationDao(id).getRelationUId(codingSchemeUid, entry.getContainerName());
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.RevisableAbstractDatabaseService#getHistoryEntryByRevisionId(org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId, java.lang.String, java.lang.String)
	 */
	@Override
	protected Relations getHistoryEntryByRevisionId(
			CodingSchemeUriVersionBasedEntryId id, String entryUid,
			String revisionId) {
		String codingSchemeUid = this.getCodingSchemeUid(id);
		
		return 
			this.getAssociationDao(id).getHistoryRelationByRevisionId(codingSchemeUid, entryUid, revisionId);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.RevisableAbstractDatabaseService#getLatestRevisionId(org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId, java.lang.String)
	 */
	@Override
	protected String getLatestRevisionId(CodingSchemeUriVersionBasedEntryId id,
			String entryUId) {
		String codingSchemeUid = this.getCodingSchemeUid(id);

		return 
			this.getAssociationDao(id).getRelationLatestRevision(codingSchemeUid, entryUId);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.RevisableAbstractDatabaseService#insertIntoHistory(org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId, org.LexGrid.commonTypes.Versionable, java.lang.String)
	 */
	@Override
	protected void insertIntoHistory(CodingSchemeUriVersionBasedEntryId id,
			Relations currentEntry, String entryUId) {
		String codingSchemeUid = this.getCodingSchemeUid(id);
		
		this.getAssociationDao(id).insertHistoryRelation(codingSchemeUid, entryUId, currentEntry);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.RevisableAbstractDatabaseService#updateEntryVersionableAttributes(org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId, java.lang.String, org.LexGrid.commonTypes.Versionable)
	 */
	@Override
	protected String updateEntryVersionableAttributes(
			CodingSchemeUriVersionBasedEntryId id, String entryUId,
			Relations revisedEntity) {
		String codingSchemeUid = this.getCodingSchemeUid(id);
		
		return 
			this.getAssociationDao(id).updateRelationVersionableChanges(codingSchemeUid, entryUId, revisedEntity);
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.relation.RelationService#resolveRelationsByRevision(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override 
	@Transactional(rollbackFor=Exception.class)
	public Relations resolveRelationsByRevision(
			String codingSchemeURI,
			String version, 
			String relationsName,
			String revisionId) throws LBRevisionException {
		CodingSchemeUriVersionBasedEntryId id = 
			new CodingSchemeUriVersionBasedEntryId(
					codingSchemeURI, version);
		
		AssociationDao associationDao = this.getAssociationDao(id);
		
		String codingSchemeUId = this.getCodingSchemeUid(id);
		
		String relationsUid = associationDao.getRelationUId(codingSchemeUId, relationsName);
		
		return this.resolveEntryByRevision(
						id,	 
						relationsUid, 
						revisionId);
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
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.relation.RelationService#updateRelation(java.lang.String, java.lang.String, org.LexGrid.relations.Relations)
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	@DatabaseErrorIdentifier(errorCode=UPDATE_RELATION_ERROR)
	public void updateRelation(String codingSchemeUri, String version,
			final Relations relation) throws LBException {

		final AssociationDao associationDao = this.getDaoManager().getAssociationDao(
				codingSchemeUri, version);
			
		final String codingSchemeUId = this.getDaoManager().getCodingSchemeDao(
				codingSchemeUri, version).getCodingSchemeUIdByUriAndVersion(
				codingSchemeUri, version);

		final String relationUId = associationDao.getRelationUId(codingSchemeUId,
				relation.getContainerName());

		this.updateEntry(
				new CodingSchemeUriVersionBasedEntryId(codingSchemeUri, version), 
				relation, EntryStateType.RELATION, new UpdateTemplate() {

					@Override
					public String update() {
						return associationDao.updateRelation(codingSchemeUId, relationUId, relation);
					}
				});
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.relation.RelationService#removeRelation(java.lang.String, java.lang.String, org.LexGrid.relations.Relations)
	 */
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

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.relation.RelationService#revise(java.lang.String, java.lang.String, org.LexGrid.relations.Relations)
	 */
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

				this.insertDependentChanges(
						new CodingSchemeUriVersionBasedEntryId(codingSchemeUri, version),
						relation,
						EntryStateType.RELATION);
			} else if (changeType == ChangeType.VERSIONABLE) {

				this.insertVersionableChanges(
						new CodingSchemeUriVersionBasedEntryId(codingSchemeUri, version),
						relation,
						EntryStateType.RELATION);
			}
		}
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

	/**
	 * Gets the assoc target service.
	 * 
	 * @return the assocTargetService
	 */
	public AssociationTargetService getAssocTargetService() {
		return assocTargetService;
	}

	/**
	 * Sets the assoc target service.
	 * 
	 * @param assocTargetService the assocTargetService to set
	 */
	public void setAssocTargetService(AssociationTargetService assocTargetService) {
		this.assocTargetService = assocTargetService;
	}

	/**
	 * Gets the assoc data service.
	 * 
	 * @return the assocDataService
	 */
	public AssociationDataService getAssocDataService() {
		return assocDataService;
	}

	/**
	 * Sets the assoc data service.
	 * 
	 * @param assocDataService the assocDataService to set
	 */
	public void setAssocDataService(AssociationDataService assocDataService) {
		this.assocDataService = assocDataService;
	}
	
	/**
	 * Gets the association dao.
	 * 
	 * @param id the id
	 * 
	 * @return the association dao
	 */
	private AssociationDao getAssociationDao(CodingSchemeUriVersionBasedEntryId id) {
		return this.getDaoManager().getAssociationDao(id.getCodingSchemeUri(), id.getCodingSchemeVersion());
	}

	/**
	 * Do add relation dependent entry.
	 * 
	 * @param id the id
	 * @param revisedEntry the revised entry
	 * 
	 * @return true, if successful
	 */
	private boolean doAddRelationDependentEntry(CodingSchemeUriVersionBasedEntryId id, Relations revisedEntry) {
		
		String codingSchemeUri = id.getCodingSchemeUri();
		String version = id.getCodingSchemeVersion();
		
		CodingSchemeDao codingSchemeDao = getDaoManager().getCodingSchemeDao(codingSchemeUri, version);
		
		AssociationDao associationDao = this.getDaoManager().getAssociationDao(
				codingSchemeUri, version);
		
		VersionsDao versionsDao = getDaoManager().getVersionsDao(codingSchemeUri, version);
	
		String codingSchemeUId = codingSchemeDao
				.getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
	
		String relationUId = associationDao.getRelationUId(codingSchemeUId,
				revisedEntry.getContainerName());
		
		String prevEntryStateUId = associationDao.getRelationEntryStateUId(
				codingSchemeUId, relationUId);
	
		if (!associationDao
				.entryStateExists(codingSchemeUId, prevEntryStateUId)) {
			EntryState entryState = new EntryState();
	
			entryState.setChangeType(ChangeType.NEW);
			entryState.setRelativeOrder(0L);
	
			versionsDao.insertEntryState(codingSchemeUId, prevEntryStateUId,
					relationUId, EntryStateType.RELATION, null, entryState);
		}
	
		String entryStateUId = versionsDao.insertEntryState(codingSchemeUId,
				relationUId, EntryStateType.RELATION, prevEntryStateUId,
				revisedEntry.getEntryState());
	
		associationDao.updateRelationEntryStateUId(codingSchemeUId,
				relationUId, entryStateUId);
	
		return true;
	}
}