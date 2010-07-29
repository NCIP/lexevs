package org.lexevs.dao.database.service.association;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.versions.types.ChangeType;
import org.lexevs.dao.database.access.association.AssociationTargetDao;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.access.versions.VersionsDao.EntryStateType;
import org.lexevs.dao.database.service.RevisableAbstractDatabaseService;
import org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId;
import org.lexevs.dao.database.service.error.DatabaseErrorIdentifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

public class VersionableEventAssociationTargetService
	extends RevisableAbstractDatabaseService<AssociationTarget,CodingSchemeUriVersionBasedEntryId> implements AssociationTargetService {

	@Override
	protected AssociationTarget addDependentAttributesByRevisionId(
			CodingSchemeUriVersionBasedEntryId id, 
			String entryUid,
			AssociationTarget entry, String revisionId) {
		return entry;
	}

	@Override
	protected void doInsertDependentChanges(
			CodingSchemeUriVersionBasedEntryId id,
			AssociationTarget revisedEntry) throws LBException {
		//
	}

	@Override
	protected boolean entryStateExists(
			CodingSchemeUriVersionBasedEntryId id,
			String entryStateUid) {
		String codingSchemeUid = this.getCodingSchemeUid(id);
		
		return this.getAssociationTargetDao(id).entryStateExists(codingSchemeUid, entryStateUid);
	}

	@Override
	protected AssociationTarget getCurrentEntry(
			CodingSchemeUriVersionBasedEntryId id, 
			String entryUId) {
		String codingSchemeUid = this.getCodingSchemeUid(id);
		
		AssociationTargetDao associationTargetDao = this.getAssociationTargetDao(id);

		return validateAssociationSource(
				associationTargetDao.getTripleByUid(codingSchemeUid, entryUId));
	}

	@Override
	protected String getCurrentEntryStateUid(
			CodingSchemeUriVersionBasedEntryId id, 
			String entryUid) {
		String codingSchemeUid = this.getCodingSchemeUid(id);
		
		return this.getAssociationTargetDao(id).getEntryStateUId(codingSchemeUid, entryUid);
	}

	@Override
	protected String getEntryUid(
			CodingSchemeUriVersionBasedEntryId id,
			AssociationTarget entry) {
		String codingSchemeUid = this.getCodingSchemeUid(id);
	
		String tripleId = 
			this.getAssociationTargetDao(id).
				getAssociationTargetUId(
						codingSchemeUid, 
						entry.getAssociationInstanceId());
		
		return tripleId;
	}

	@Override
	protected AssociationTarget getHistoryEntryByRevisionId(
			CodingSchemeUriVersionBasedEntryId id, 
			String entryUid,
			String revisionId) {
		String codingSchemeUid = this.getCodingSchemeUid(id);
		
		AssociationTargetDao associationTargetDao = this.getAssociationTargetDao(id);

		return validateAssociationSource(
				associationTargetDao.getHistoryTripleByRevision(codingSchemeUid, entryUid, revisionId));
	}

	@Override
	protected String getLatestRevisionId(
			CodingSchemeUriVersionBasedEntryId id,
			String entryUId) {
		String codingSchemeUid = this.getCodingSchemeUid(id);
		
		AssociationTargetDao associationTargetDao = this.getAssociationTargetDao(id);
		
		return associationTargetDao.getLatestRevision(codingSchemeUid, entryUId);
	}

	@Override
	protected void insertIntoHistory(
			CodingSchemeUriVersionBasedEntryId id,
			AssociationTarget currentEntry, 
			String entryUId) {
		String codingSchemeUid = this.getCodingSchemeUid(id);

		String tripleUid = 
			this.getAssociationTargetDao(id).
				getAssociationTargetUId(
						codingSchemeUid, 
						currentEntry.getAssociationInstanceId());
		
		Boolean assnQualExist = currentEntry.getAssociationQualificationCount() > 0 ? true
				: false;
		Boolean contextExist = currentEntry.getUsageContextCount() > 0 ? true : false;
		
		this.getAssociationTargetDao(id).
			insertHistoryAssociationTarget(
					codingSchemeUid, 
					tripleUid, 
					assnQualExist, 
					contextExist);
	}

	@Override
	protected String updateEntryVersionableAttributes(
			CodingSchemeUriVersionBasedEntryId id, 
			String entryUId,
			AssociationTarget revisedEntity) {
		String codingSchemeUid = this.getCodingSchemeUid(id);
		
		AssociationTargetDao associationTargetDao = this.getAssociationTargetDao(id);
		
		return 
			associationTargetDao.updateVersionableChanges(codingSchemeUid, entryUId, revisedEntity);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public AssociationTarget resolveAssociationTargetByRevision(
			String codingSchemeUri,
			String version, 
			String relationContainerName,
			String associationPredicateName, 
			String associationInstanceId, 
			String revisionId) throws LBRevisionException {
		CodingSchemeUriVersionBasedEntryId id = new CodingSchemeUriVersionBasedEntryId(codingSchemeUri, version);
		
		String codingSchemeUid = this.getCodingSchemeUId(codingSchemeUri, version);
		
		AssociationTargetDao associationTargetDao = this.getAssociationTargetDao(codingSchemeUri, version);
		
		String tripleUid = associationTargetDao.getAssociationTargetUId(codingSchemeUid, associationInstanceId);
		
		return this.resolveEntryByRevision(id, tripleUid, revisionId);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public AssociationTarget getAssociationTarget(
			String codingSchemeUri, 
			String version,
			String relationContainerName, 
			String associationPredicateName,
			String associationInstanceId) {
		String codingSchemeUid = this.getCodingSchemeUId(codingSchemeUri, version);
		
		AssociationTargetDao associationTargetDao = this.getAssociationTargetDao(codingSchemeUri, version);
		
		String tripleUid = associationTargetDao.getAssociationTargetUId(codingSchemeUid, associationInstanceId);
		
		return this.validateAssociationSource(
				associationTargetDao.getTripleByUid(codingSchemeUid, tripleUid));
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	@DatabaseErrorIdentifier(errorCode=INSERT_ASSOCIATIONTARGET_ERROR)
	public void insertAssociationTarget(
			String codingSchemeUri, 
			String version,
			String relationContainerName, 
			String associationPredicateName,
			String sourceEntityCode,
			String sourceEntityCodeNamespace,
			AssociationTarget target) {

		String codingSchemeUId = this.getCodingSchemeUId(codingSchemeUri,
				version);

			String associationPredicateUId = this.getDaoManager()
				.getAssociationDao(codingSchemeUri, version)
				.getAssociationPredicateUIdByContainerName(codingSchemeUId,
					relationContainerName, associationPredicateName);

			this.getDaoManager().getAssociationTargetDao(codingSchemeUri, version)
				.insertAssociationTarget(
						codingSchemeUId,
						associationPredicateUId, 
						sourceEntityCode, 
						sourceEntityCodeNamespace,
						target);

	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	@DatabaseErrorIdentifier(errorCode=UPDATE_ASSOCIATIONTARGET_ERROR)
	public void updateAssociationTarget(
			String codingSchemeUri, 
			String version,
			final AssociationTarget target) {

		final String codingSchemeUId = this.getCodingSchemeUId(codingSchemeUri,
				version);

		final AssociationTargetDao associationTargetDao = this.getDaoManager()
			.getAssociationTargetDao(codingSchemeUri, version);
		final String associationTargetUId = associationTargetDao
			.getAssociationTargetUId(codingSchemeUId, target
				.getAssociationInstanceId());

		try {
			this.updateEntry(
					new CodingSchemeUriVersionBasedEntryId(codingSchemeUri, version),
					target, 
					EntryStateType.ENTITYASSNSTOENTITY,
					new UpdateTemplate() {

						@Override
						public String update() {
							return associationTargetDao.updateAssociationTarget(
									codingSchemeUId, 
									associationTargetUId, 
									target);
						}

					});
		} catch (LBException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	@DatabaseErrorIdentifier(errorCode=REMOVE_ASSOCIATIONTARGET_ERROR)
	public void removeAssociationTarget(String codingSchemeUri, String version, AssociationTarget target) {

		CodingSchemeDao codingSchemeDao = getDaoManager().getCodingSchemeDao(
				codingSchemeUri, version);

		AssociationTargetDao associationTargetDao = this.getDaoManager()
				.getAssociationTargetDao(codingSchemeUri, version);

		VersionsDao versionsDao = getDaoManager().getVersionsDao(
				codingSchemeUri, version);

		String codingSchemeUId = codingSchemeDao
				.getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);

		String associationTargetUId = associationTargetDao
				.getAssociationTargetUId(codingSchemeUId, target
						.getAssociationInstanceId());

		/* 1. Delete all entry state entries of association target. */
		versionsDao.deleteAllEntryStateEntriesByEntryUId(codingSchemeUId,
				associationTargetUId);

		/* 2. Delete all association multi attributes of association target. */
		associationTargetDao
				.deleteAssociationMultiAttribsByAssociationTargetUId(
						codingSchemeUId, associationTargetUId);

		/* 3. Delete association target. */
		associationTargetDao.deleteAssnTargetByUId(codingSchemeUId,
				associationTargetUId);
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void revise(
			String codingSchemeUri, 
			String version,
			String relationContainerName, 
			String associationPredicateName,
			AssociationSource source,
			AssociationTarget target)
			throws LBException {
		
		this.revise(
				codingSchemeUri, 
				version, 
				relationContainerName, 
				associationPredicateName, 
				source.getSourceEntityCode(), 
				source.getSourceEntityCodeNamespace(), 
				target);
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
			AssociationTarget target)
			throws LBException {
		CodingSchemeUriVersionBasedEntryId id = new CodingSchemeUriVersionBasedEntryId(codingSchemeUri, version);

		String entryUId = this.getEntryUid(id, target);
		
		if (validRevision(id, target)) {

			ChangeType changeType = target.getEntryState().getChangeType();
			
			if (changeType == ChangeType.NEW) {

				this.insertAssociationTarget(
						codingSchemeUri, 
						version,
						relationContainerName, 
						associationPredicateName,
						sourceEntityCode, 
						sourceEntityCodeNamespace,
						target);
			} else if (changeType == ChangeType.REMOVE) {

				this.removeAssociationTarget(codingSchemeUri, version,
						target);
			} else if (changeType == ChangeType.MODIFY) {

				this.updateAssociationTarget(
						codingSchemeUri, 
						version, 
						target);
			} else if (changeType == ChangeType.VERSIONABLE) {

				this.updateEntryVersionableAttributes(id, entryUId, target);
			}
		}
	}
	
	private AssociationTarget validateAssociationSource(AssociationSource source) {
		if(source == null || source.getTargetCount() == 0) {
			return null;
		}
		Assert.state(source.getTargetCount() <= 1);
		
		return source.getTarget(0);
	}

	private AssociationTargetDao getAssociationTargetDao(String uri, String version) {
		return this.getDaoManager().getAssociationTargetDao(uri, version);
	}
	
	private AssociationTargetDao getAssociationTargetDao(CodingSchemeUriVersionBasedEntryId id) {
		return this.getAssociationTargetDao(id.getCodingSchemeUri(), id.getCodingSchemeVersion());
	}	
}
