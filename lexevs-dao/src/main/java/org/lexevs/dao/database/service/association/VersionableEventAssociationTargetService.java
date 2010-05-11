package org.lexevs.dao.database.service.association;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.lexevs.dao.database.access.association.AssociationTargetDao;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.access.versions.VersionsDao.EntryStateType;
import org.lexevs.dao.database.constants.classifier.property.EntryStateTypeClassifier;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.springframework.batch.classify.Classifier;

public class VersionableEventAssociationTargetService extends
		AbstractDatabaseService implements AssociationTargetService {

	private Classifier<EntryStateType, String> entryStateTypeClassifier = new EntryStateTypeClassifier();

	@Override
	public void insertAssociationTarget(String codingSchemeUri, String version,
			String relationContainerName, String associationPredicateName,
			AssociationSource source, AssociationTarget target) {

		String codingSchemeUId = this.getCodingSchemeUId(codingSchemeUri,
				version);

		String associationPredicateUId = this.getDaoManager()
				.getAssociationDao(codingSchemeUri, version)
				.getAssociationPredicateUIdByContainerName(codingSchemeUId,
						relationContainerName, associationPredicateName);

		this.getDaoManager().getAssociationTargetDao(codingSchemeUri, version)
				.insertAssociationTarget(codingSchemeUId,
						associationPredicateUId, source, target);
	}

	@Override
	public void updateAssociationTarget(String codingSchemeUri, String version,
			AssociationSource source, AssociationTarget target) {

		String codingSchemeUId = this.getCodingSchemeUId(codingSchemeUri,
				version);

		AssociationTargetDao associationTargetDao = this.getDaoManager()
				.getAssociationTargetDao(codingSchemeUri, version);

		VersionsDao versionsDao = getDaoManager().getVersionsDao(
				codingSchemeUri, version);

		String associationTargetUId = associationTargetDao
				.getAssociationTargetUId(codingSchemeUId, target
						.getAssociationInstanceId());

		Boolean assnQualExist = target.getAssociationQualificationCount() > 0 ? true
				: false;
		Boolean contextExist = target.getUsageContextCount() > 0 ? true : false;

		/* 1. insert current association target into history. */
		String prevEntryStateUId = associationTargetDao
				.insertHistoryAssociationTarget(codingSchemeUId,
						associationTargetUId, assnQualExist, contextExist);

		/* 2. update the attributes of the association target. */
		String entryStateUId = associationTargetDao.updateAssociationTarget(
				codingSchemeUId, associationTargetUId, source, target);

		/* 3. register entrystate details for the association target. */
		versionsDao.insertEntryState(entryStateUId, associationTargetUId,
				entryStateTypeClassifier
						.classify(EntryStateType.ENTITYASSNSTOENTITY),
				prevEntryStateUId, target.getEntryState());
	}

	@Override
	public void removeAssociationTarget(String codingSchemeUri, String version,
			AssociationSource source, AssociationTarget target) {

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

		/* 2. Delete all association qualifications of association target. */
		associationTargetDao
				.deleteAssociationQualificationsByAssociationTargetUId(
						codingSchemeUId, associationTargetUId);

		/* 3. Delete association target. */
		associationTargetDao.deleteAssnTargetByUId(codingSchemeUId,
				associationTargetUId);
	}

	@Override
	public void insertAssociationTargetVersionableChanges(
			String codingSchemeUri, String version, AssociationSource source,
			AssociationTarget target) {

		String codingSchemeUId = this.getCodingSchemeUId(codingSchemeUri,
				version);

		AssociationTargetDao associationTargetDao = this.getDaoManager()
				.getAssociationTargetDao(codingSchemeUri, version);

		VersionsDao versionsDao = getDaoManager().getVersionsDao(
				codingSchemeUri, version);

		String associationTargetUId = associationTargetDao
				.getAssociationTargetUId(codingSchemeUId, target
						.getAssociationInstanceId());

		Boolean assnQualExist = target.getAssociationQualificationCount() > 0 ? true
				: false;
		Boolean contextExist = target.getUsageContextCount() > 0 ? true : false;

		/* 1. insert current association target into history. */
		String prevEntryStateUId = associationTargetDao
				.insertHistoryAssociationTarget(codingSchemeUId,
						associationTargetUId, assnQualExist, contextExist);

		/* 2. update the versionable attributes of the association target. */
		String entryStateUId = associationTargetDao.updateVersionableChanges(
				codingSchemeUId, associationTargetUId, source, target);

		/* 3. register entrystate details for the association target. */
		versionsDao.insertEntryState(entryStateUId, associationTargetUId,
				entryStateTypeClassifier
						.classify(EntryStateType.ENTITYASSNSTOENTITY),
				prevEntryStateUId, target.getEntryState());
	}

	@Override
	public void revise(String codingSchemeUri, String version,
			String relationContainerName, String associationPredicateName,
			AssociationSource source, AssociationTarget target)
			throws LBException {

		if (target == null)
			throw new LBParameterException(
					"Association entity object is not supplied.");

		EntryState entryState = target.getEntryState();

		if (entryState == null) {
			throw new LBRevisionException("EntryState can't be null.");
		}

		String revisionId = entryState.getContainingRevision();
		ChangeType changeType = entryState.getChangeType();

		String csUId = this.getDaoManager().getCodingSchemeDao(codingSchemeUri,
				version).getCodingSchemeUIdByUriAndVersion(codingSchemeUri,
				version);

		AssociationTargetDao assocTargetDao = this.getDaoManager()
				.getAssociationTargetDao(codingSchemeUri, version);
		
		String targetUId = assocTargetDao.getAssociationTargetUId(csUId, target
				.getAssociationInstanceId());

		String targetLatestRevisionId = assocTargetDao.getLatestRevision(csUId,
				targetUId);

		if (revisionId != null && changeType != null) {

			if (changeType == ChangeType.NEW) {
				if (entryState.getPrevRevision() != null) {
					throw new LBRevisionException(
							"Changes of type NEW are not allowed to have previous revisions.");
				}
			} else if (targetUId == null) {
				throw new LBRevisionException(
						"The association target being revised doesn't exist.");
			} else if (entryState.getPrevRevision() == null) {
				throw new LBRevisionException(
						"All changes of type other than NEW should have previous revisions.");
			} else if (targetLatestRevisionId != null
					&& !targetLatestRevisionId.equalsIgnoreCase(entryState
							.getPrevRevision())) {
				throw new LBRevisionException(
						"Revision source is not in sync with the database revisions. Previous revision id does not match with the latest revision id of the association target."
								+ "Please update the authoring instance with all the revisions and regenerate the source.");
			}

			if (changeType == ChangeType.NEW) {

				this.insertAssociationTarget(codingSchemeUri, version,
						relationContainerName, associationPredicateName,
						source, target);
			} else if (changeType == ChangeType.REMOVE) {

				this.removeAssociationTarget(codingSchemeUri, version, source,
						target);
			} else if (changeType == ChangeType.MODIFY) {

				this.updateAssociationTarget(codingSchemeUri, version, source,
						target);
			} else if (changeType == ChangeType.VERSIONABLE) {

				this.insertAssociationTargetVersionableChanges(codingSchemeUri,
						version, source, target);
			}
		}
	}
}
