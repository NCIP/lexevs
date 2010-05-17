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
import org.lexevs.dao.database.service.error.DatabaseErrorIdentifier;
import org.springframework.batch.classify.Classifier;

public class VersionableEventAssociationTargetService extends
		AbstractDatabaseService implements AssociationTargetService {

	private Classifier<EntryStateType, String> entryStateTypeClassifier = new EntryStateTypeClassifier();

	@Override
	@DatabaseErrorIdentifier(errorCode=INSERT_ASSOCIATIONTARGET_ERROR)
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
	@DatabaseErrorIdentifier(errorCode=UPDATE_ASSOCIATIONTARGET_ERROR)
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
	@DatabaseErrorIdentifier(errorCode=REMOVE_ASSOCIATIONTARGET_ERROR)
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

		/* 2. Delete all association multi attributes of association target. */
		associationTargetDao
				.deleteAssociationMultiAttribsByAssociationTargetUId(
						codingSchemeUId, associationTargetUId);

		/* 3. Delete association target. */
		associationTargetDao.deleteAssnTargetByUId(codingSchemeUId,
				associationTargetUId);
	}

	@Override
	@DatabaseErrorIdentifier(errorCode=INSERT_ASSOCIATIONTARGET_VERSIONABLE_CHANGES_ERROR)
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

		if (validRevision(codingSchemeUri, version, relationContainerName,
				associationPredicateName, source, target)) {

			ChangeType changeType = target.getEntryState().getChangeType();
			
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

	private boolean validRevision(String codingSchemeUri, String version,
			String relationContainerName, String associationPredicateName,
			AssociationSource source, AssociationTarget target) throws LBException {
		
		if (target == null)
			throw new LBParameterException(
					"Association entity object is not supplied.");

		EntryState entryState = target.getEntryState();

		if (entryState == null) {
			throw new LBRevisionException("EntryState can't be null.");
		}

		ChangeType changeType = entryState.getChangeType();
		
		if (changeType == ChangeType.NEW) {
			if (entryState.getPrevRevision() != null) {
				throw new LBRevisionException(
						"Changes of type NEW are not allowed to have previous revisions.");
			}
		} else {
			
			String csUId = this.getDaoManager().getCodingSchemeDao(codingSchemeUri,
					version).getCodingSchemeUIdByUriAndVersion(codingSchemeUri,
					version);

			AssociationTargetDao assocTargetDao = this.getDaoManager()
					.getAssociationTargetDao(codingSchemeUri, version);
			
			String assocPredicateUId = this.getDaoManager().getAssociationDao(codingSchemeUri, version)
					.getAssociationPredicateUIdByContainerName(csUId,
							relationContainerName, associationPredicateName);
			
			if (assocPredicateUId == null) {
				throw new LBRevisionException(
						"The association triple being revised doesn't exist.");
			}
			
			String targetUId = assocTargetDao.getAssociationTargetUId(csUId, target
					.getAssociationInstanceId());

			if (targetUId == null) {
				throw new LBRevisionException(
						"The association target being revised doesn't exist.");
			} 
			
			String targetLatestRevisionId = assocTargetDao.getLatestRevision(csUId,
					targetUId);
			
			if (entryState.getPrevRevision() == null && targetLatestRevisionId != null) {
				throw new LBRevisionException(
						"All changes of type other than NEW should have previous revisions.");
			} else if (targetLatestRevisionId != null
					&& !targetLatestRevisionId.equalsIgnoreCase(entryState
							.getPrevRevision())) {
				throw new LBRevisionException(
						"Revision source is not in sync with the database revisions. Previous revision id does not match with the latest revision id of the association target."
								+ "Please update the authoring instance with all the revisions and regenerate the source.");
			}
		}
		
		return true;
	}
}
