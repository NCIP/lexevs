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

public class VersionableEventAssociationDataService extends
	RevisableAbstractDatabaseService<AssociationData,CodingSchemeUriVersionBasedEntryId> implements AssociationDataService {

	@Override
	protected AssociationData addDependentAttributesByRevisionId(
			CodingSchemeUriVersionBasedEntryId id, String entryUid,
			AssociationData entry, String revisionId) {
		return entry;
	}

	@Override
	protected void doInsertDependentChanges(
			CodingSchemeUriVersionBasedEntryId id,
			AssociationData revisedEntry) throws LBException {
		//
	}

	@Override
	protected boolean entryStateExists(
			CodingSchemeUriVersionBasedEntryId id,
			String entryStateUid) {
		String codingSchemeUId = this.getCodingSchemeUid(id);

		return getAssociationDataDao(id).entryStateExists(codingSchemeUId, entryStateUid);
	}

	@Override
	protected AssociationData getCurrentEntry(
			CodingSchemeUriVersionBasedEntryId id, 
			String entryUId) {
		String codingSchemeUId = this.getCodingSchemeUid(id);

		return getAssociationDataDao(id).getAssociationDataByUid(codingSchemeUId, entryUId);
	}

	@Override
	protected String getCurrentEntryStateUid(
			CodingSchemeUriVersionBasedEntryId id, String entryUid) {
		String codingSchemeUId = this.getCodingSchemeUid(id);

		return getAssociationDataDao(id).getEntryStateUId(codingSchemeUId, entryUid);
	}

	@Override
	protected String getEntryUid(CodingSchemeUriVersionBasedEntryId id,
			AssociationData entry) {
		String codingSchemeUId = this.getCodingSchemeUid(id);

		return getAssociationDataDao(id).getAssociationDataUId(codingSchemeUId, entry.getAssociationInstanceId());
	}

	@Override
	protected AssociationData getHistoryEntryByRevisionId(
			CodingSchemeUriVersionBasedEntryId id, String entryUid,
			String revisionId) {
		String codingSchemeUId = this.getCodingSchemeUid(id);

		return getAssociationDataDao(id).getHistoryAssociationDataByRevision(codingSchemeUId, entryUid, revisionId);
	}

	@Override
	protected String getLatestRevisionId(CodingSchemeUriVersionBasedEntryId id,
			String entryUId) {
		String codingSchemeUId = this.getCodingSchemeUid(id);

		return getAssociationDataDao(id).getLatestRevision(codingSchemeUId, entryUId);
	}

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

	private AssociationDataDao getAssociationDataDao(CodingSchemeUriVersionBasedEntryId id) {
		return this.getAssociationDataDao(id.getCodingSchemeUri(), id.getCodingSchemeVersion());
	}	
	
	private AssociationDataDao getAssociationDataDao(String uri, String version) {
		return this.getDaoManager().getAssociationDataDao(uri, version);
	}	
	
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
