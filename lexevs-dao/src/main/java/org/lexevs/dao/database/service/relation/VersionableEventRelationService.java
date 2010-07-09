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
import org.LexGrid.versions.types.ChangeType;
import org.lexevs.dao.database.access.association.AssociationDao;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.access.versions.VersionsDao.EntryStateType;
import org.lexevs.dao.database.service.RevisableAbstractDatabaseService;
import org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId;
import org.lexevs.dao.database.service.association.AssociationDataService;
import org.lexevs.dao.database.service.association.AssociationTargetService;
import org.lexevs.dao.database.service.error.DatabaseErrorIdentifier;
import org.lexevs.dao.database.service.property.PropertyService;
import org.lexevs.dao.database.utility.DaoUtility;
import org.springframework.transaction.annotation.Transactional;

public class VersionableEventRelationService extends RevisableAbstractDatabaseService<Relations,CodingSchemeUriVersionBasedEntryId> implements RelationService {

	private PropertyService propertyService = null;
	private AssociationTargetService assocTargetService = null;
	private AssociationDataService assocDataService = null;

	@Override
	protected Relations addDependentAttributesByRevisionId(
			CodingSchemeUriVersionBasedEntryId id, String entryUid,
			Relations entry) {
		entry.setProperties(new Properties());
		
		List<Property> properties = 
			propertyService.resolvePropertiesOfRelationByRevision(
					id.getCodingSchemeUri(), 
					id.getCodingSchemeVersion(), 
					entry.getContainerName(), 
					entry.getEntryState().getContainingRevision());
		
		//TODO: Get dependent association source/targets
		
		entry.getProperties().setProperty(properties);
		
		return entry;
	
	}

	@Override
	protected void doInsertDependentChanges(
			CodingSchemeUriVersionBasedEntryId id, Relations revisedEntry)
		throws LBException {
		
		if(revisedEntry.getProperties() != null && revisedEntry.getProperties().getProperty() != null) {
			for(Property property : revisedEntry.getProperties().getProperty()) {
				propertyService.reviseRelationProperty(
						id.getCodingSchemeUri(), 
						id.getCodingSchemeVersion(), 
						revisedEntry.getContainerName(), 
						property);
			}
		}

		for(AssociationPredicate predicate : DaoUtility.emptyIterableIfNull(revisedEntry.getAssociationPredicate())) {
			for(AssociationSource source : DaoUtility.emptyIterableIfNull(predicate.getSource())) {
				for(AssociationData data : DaoUtility.emptyIterableIfNull(source.getTargetData())) {
					assocDataService.revise(
							id.getCodingSchemeUri(), 
							id.getCodingSchemeVersion(), 
							revisedEntry.getContainerName(), 
							predicate.getAssociationName(), 
							source, 
							data);
				}
				
				for(AssociationTarget target : DaoUtility.emptyIterableIfNull(source.getTarget())) {
					assocTargetService.
					revise(
							id.getCodingSchemeUri(), 
							id.getCodingSchemeVersion(), 
							revisedEntry.getContainerName(), 
							predicate.getAssociationName(), 
							source, 
							target);
				}
			}
		}
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
		String codingSchemeUid = this.getCodingSchemeUid(id);
		
		this.getAssociationDao(id).insertHistoryRelation(codingSchemeUid, entryUId, currentEntry);
	}

	@Override
	protected String updateEntityVersionableAttributes(
			CodingSchemeUriVersionBasedEntryId id, String entryUId,
			Relations revisedEntity) {
		String codingSchemeUid = this.getCodingSchemeUid(id);
		
		return 
			this.getAssociationDao(id).updateRelationVersionableChanges(codingSchemeUid, entryUId, revisedEntity);
	}
	
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
	
	@Override
	@DatabaseErrorIdentifier(errorCode=REMOVE_RELATION_ERROR)
	@Transactional
	public void removeRelation(String codingSchemeUri, String version,
			Relations relation) {

		CodingSchemeDao codingSchemeDao = getDaoManager().getCodingSchemeDao(codingSchemeUri, version);
		
		final AssociationDao associationDao = this.getDaoManager().getAssociationDao(
				codingSchemeUri, version);
		
		final String codingSchemeUId = codingSchemeDao.
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
		
		final String relationUId = associationDao.getRelationUId(codingSchemeUId, relation.getContainerName());
		
		try {
			this.removeEntry(
					new CodingSchemeUriVersionBasedEntryId(codingSchemeUri, version), 
					relation, EntryStateType.RELATION, new DeleteTemplate() {

						@Override
						public void delete() {
							associationDao.removeRelationByUId(codingSchemeUId, relationUId);
						}
					});
		} catch (LBException e) {
			throw new RuntimeException(e);
		}
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
	
	private AssociationDao getAssociationDao(CodingSchemeUriVersionBasedEntryId id) {
		return this.getDaoManager().getAssociationDao(id.getCodingSchemeUri(), id.getCodingSchemeVersion());
	}
}
