
package org.lexevs.dao.database.service.association;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.relations.AssociationData;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.versions.types.ChangeType;
import org.lexevs.dao.database.access.association.AssociationDataDao;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.access.versions.VersionsDao.EntryStateType;
import org.lexevs.dao.database.service.RevisableAbstractDatabaseService;
import org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId;
import org.lexevs.dao.database.service.error.DatabaseErrorIdentifier;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class VersionableEventAssociationDataService.
 */
public class VersionableEventAssociationDataService extends
	RevisableAbstractDatabaseService<AssociationData,CodingSchemeUriVersionBasedEntryId> implements AssociationDataService {

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.RevisableAbstractDatabaseService#addDependentAttributesByRevisionId(org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId, java.lang.String, org.LexGrid.commonTypes.Versionable, java.lang.String)
	 */
	@Override
	protected AssociationData addDependentAttributesByRevisionId(
			CodingSchemeUriVersionBasedEntryId id, String entryUid,
			AssociationData entry, String revisionId) {
		return entry;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.RevisableAbstractDatabaseService#doInsertDependentChanges(org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId, org.LexGrid.commonTypes.Versionable)
	 */
	@Override
	protected void doInsertDependentChanges(
			CodingSchemeUriVersionBasedEntryId id,
			AssociationData revisedEntry) throws LBException {
		//
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.RevisableAbstractDatabaseService#entryStateExists(org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId, java.lang.String)
	 */
	@Override
	protected boolean entryStateExists(
			CodingSchemeUriVersionBasedEntryId id,
			String entryStateUid) {
		String codingSchemeUId = this.getCodingSchemeUid(id);

		return getAssociationDataDao(id).entryStateExists(codingSchemeUId, entryStateUid);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.RevisableAbstractDatabaseService#getCurrentEntry(org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId, java.lang.String)
	 */
	@Override
	protected AssociationData getCurrentEntry(
			CodingSchemeUriVersionBasedEntryId id, 
			String entryUId) {
		String codingSchemeUId = this.getCodingSchemeUid(id);

		return getAssociationDataDao(id).getAssociationDataByUid(codingSchemeUId, entryUId);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.RevisableAbstractDatabaseService#getCurrentEntryStateUid(org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId, java.lang.String)
	 */
	@Override
	protected String getCurrentEntryStateUid(
			CodingSchemeUriVersionBasedEntryId id, String entryUid) {
		String codingSchemeUId = this.getCodingSchemeUid(id);

		return getAssociationDataDao(id).getEntryStateUId(codingSchemeUId, entryUid);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.RevisableAbstractDatabaseService#getEntryUid(org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId, org.LexGrid.commonTypes.Versionable)
	 */
	@Override
	protected String getEntryUid(CodingSchemeUriVersionBasedEntryId id,
			AssociationData entry) {
		String codingSchemeUId = this.getCodingSchemeUid(id);

		return getAssociationDataDao(id).getAssociationDataUId(codingSchemeUId, entry.getAssociationInstanceId());
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.RevisableAbstractDatabaseService#getHistoryEntryByRevisionId(org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId, java.lang.String, java.lang.String)
	 */
	@Override
	protected AssociationData getHistoryEntryByRevisionId(
			CodingSchemeUriVersionBasedEntryId id, String entryUid,
			String revisionId) {
		String codingSchemeUId = this.getCodingSchemeUid(id);

		return getAssociationDataDao(id).getHistoryAssociationDataByRevision(codingSchemeUId, entryUid, revisionId);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.RevisableAbstractDatabaseService#getLatestRevisionId(org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId, java.lang.String)
	 */
	@Override
	protected String getLatestRevisionId(CodingSchemeUriVersionBasedEntryId id,
			String entryUId) {
		String codingSchemeUId = this.getCodingSchemeUid(id);

		return getAssociationDataDao(id).getLatestRevision(codingSchemeUId, entryUId);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.RevisableAbstractDatabaseService#insertIntoHistory(org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId, org.LexGrid.commonTypes.Versionable, java.lang.String)
	 */
	@Override
	protected void insertIntoHistory(
			CodingSchemeUriVersionBasedEntryId id,
			AssociationData currentEntry, 
			String entryUId) {
		String codingSchemeUId = this.getCodingSchemeUid(id);

		getAssociationDataDao(id).insertHistoryAssociationData(
				codingSchemeUId, 
				entryUId, 
				currentEntry.getAssociationQualificationCount() > 0, 
				currentEntry.getUsageContextCount() > 0);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.RevisableAbstractDatabaseService#updateEntryVersionableAttributes(org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId, java.lang.String, org.LexGrid.commonTypes.Versionable)
	 */
	@Override
	protected String updateEntryVersionableAttributes(
			CodingSchemeUriVersionBasedEntryId id, 
			String entryUId,
			AssociationData revisedEntity) {
		String codingSchemeUId = this.getCodingSchemeUid(id);

		return getAssociationDataDao(id).updateVersionableChanges(
				codingSchemeUId, 
				entryUId,  
				revisedEntity);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.association.AssociationDataService#insertAssociationData(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.LexGrid.relations.AssociationData)
	 */
	@Override
	@DatabaseErrorIdentifier(errorCode=INSERT_ASSOCIATIONDATA_ERROR)
	@Transactional(rollbackFor=Exception.class)
	public void insertAssociationData(
			String codingSchemeUri, 
			String version,
			String relationContainerName, 
			String associationPredicateName,
			String sourceEntityCode,
			String sourceEntityCodeNamespace,
			AssociationData data) {

		String codingSchemeUId = this.getCodingSchemeUId(codingSchemeUri,
				version);

		String associationPredicateUId = this.getDaoManager()
				.getAssociationDao(codingSchemeUri, version)
				.getAssociationPredicateUIdByContainerName(codingSchemeUId,
						relationContainerName, associationPredicateName);

		this.getDaoManager().getAssociationDataDao(codingSchemeUri, version)
				.insertAssociationData(
						codingSchemeUId,
						associationPredicateUId, sourceEntityCode, sourceEntityCodeNamespace, data);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.association.AssociationDataService#updateAssociationData(java.lang.String, java.lang.String, org.LexGrid.relations.AssociationData)
	 */
	@Override
	@DatabaseErrorIdentifier(errorCode=UPDATE_ASSOCIATIONDATA_ERROR)
	@Transactional(rollbackFor=Exception.class)
	public void updateAssociationData(
			String codingSchemeUri, 
			String version,
			final AssociationData data) {
	
		final String codingSchemeUId = this.getCodingSchemeUId(codingSchemeUri,
				version);
	
		final AssociationDataDao associationDataDao = this.getDaoManager()
				.getAssociationDataDao(codingSchemeUri, version);
	
		final String associationDataUId = associationDataDao
				.getAssociationDataUId(codingSchemeUId, data.getAssociationInstanceId());
			
		try {
			this.updateEntry(
					new CodingSchemeUriVersionBasedEntryId(codingSchemeUri, version), 
					data, 
					EntryStateType.ENTITYASSNSTODATA, 
					new UpdateTemplate() {

						@Override
						public String update() {
							return associationDataDao
								.updateAssociationData(codingSchemeUId, associationDataUId, data);
						}
						
					});
		} catch (LBException e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.association.AssociationDataService#removeAssociationData(java.lang.String, java.lang.String, org.LexGrid.relations.AssociationData)
	 */
	@Override
	@DatabaseErrorIdentifier(errorCode=REMOVE_ASSOCIATIONDATA_ERROR)
	@Transactional(rollbackFor=Exception.class)
	public void removeAssociationData(
			String codingSchemeUri, 
			String version,
			AssociationData data) {

		CodingSchemeDao codingSchemeDao = getDaoManager().getCodingSchemeDao(codingSchemeUri, version);
		
		AssociationDataDao associationDataDao = this.getDaoManager()
				.getAssociationDataDao(codingSchemeUri, version);

		VersionsDao versionsDao = getDaoManager().getVersionsDao(
				codingSchemeUri, version);

		String codingSchemeUId = codingSchemeDao.
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
		
		String associationDataUId = associationDataDao
				.getAssociationDataUId(codingSchemeUId, data.getAssociationInstanceId());
		
		/* 1. Delete all entry state entries of association data. */
		versionsDao.deleteAllEntryStateEntriesByEntryUId(codingSchemeUId, associationDataUId);
		
		/* 2. Delete all association qualifications of association data. */
		associationDataDao.deleteAllAssocQualsByAssocDataUId(codingSchemeUId, associationDataUId);
		
		/* 3. Delete association data. */
		associationDataDao.deleteAssociationData(codingSchemeUId, associationDataUId);
	}

	/**
	 * Gets the association data dao.
	 * 
	 * @param id the id
	 * 
	 * @return the association data dao
	 */
	private AssociationDataDao getAssociationDataDao(CodingSchemeUriVersionBasedEntryId id) {
		return this.getAssociationDataDao(id.getCodingSchemeUri(), id.getCodingSchemeVersion());
	}	
	
	/**
	 * Gets the association data dao.
	 * 
	 * @param uri the uri
	 * @param version the version
	 * 
	 * @return the association data dao
	 */
	private AssociationDataDao getAssociationDataDao(String uri, String version) {
		return this.getDaoManager().getAssociationDataDao(uri, version);
	}	
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.association.AssociationDataService#revise(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.LexGrid.relations.AssociationSource, org.LexGrid.relations.AssociationData)
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void revise(
			String codingSchemeUri, 
			String version, 
			String relationContainerName,
			String associationPredicateName,
			AssociationSource source,
			AssociationData data) throws LBException {
		
		this.revise(
				codingSchemeUri, 
				version, 
				relationContainerName, 
				associationPredicateName, 
				source.getSourceEntityCode(), 
				source.getSourceEntityCodeNamespace(), 
				data);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.association.AssociationDataService#revise(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.LexGrid.relations.AssociationData)
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void revise(
			String codingSchemeUri, 
			String version, 
			String relationContainerName,
			String associationPredicateName,
			String sourceEntityCode,
			String sourceEntityCodeNamespace,
			AssociationData data) throws LBException {
		CodingSchemeUriVersionBasedEntryId id = new CodingSchemeUriVersionBasedEntryId(codingSchemeUri, version);

		if (validRevision(new CodingSchemeUriVersionBasedEntryId(codingSchemeUri, version), data)) {
			
			ChangeType changeType = data.getEntryState().getChangeType();
			
			if (changeType == ChangeType.NEW) {

				this.insertAssociationData(
						codingSchemeUri, 
						version,
						relationContainerName, 
						associationPredicateName,
						sourceEntityCode,
						sourceEntityCodeNamespace,
						data);
			} else if (changeType == ChangeType.REMOVE) {

				this.removeAssociationData(
						codingSchemeUri, 
						version,
						data);
			} else if (changeType == ChangeType.MODIFY) {

				this.updateAssociationData(
						codingSchemeUri, 
						version,
						data);
			} else if (changeType == ChangeType.VERSIONABLE) {

				this.insertVersionableChanges(id, data, EntryStateType.ENTITYASSNSTODATA);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.association.AssociationDataService#resolveAssociationDataByRevision(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public AssociationData resolveAssociationDataByRevision(
			String codingSchemeUri,
			String version, 
			String relationContainerName,
			String associationPredicateName, 
			String associationInstanceId, 
			String revisionId) throws LBRevisionException {
		CodingSchemeUriVersionBasedEntryId id = new CodingSchemeUriVersionBasedEntryId(codingSchemeUri, version);
		
		String codingSchemeUid = this.getCodingSchemeUId(codingSchemeUri, version);
		
		AssociationDataDao associationDataDao = this.getAssociationDataDao(codingSchemeUri, version);
		
		String associationDataUid = associationDataDao.getAssociationDataUId(codingSchemeUid, associationInstanceId);
		
		return this.resolveEntryByRevision(id, associationDataUid, revisionId);
	}
}