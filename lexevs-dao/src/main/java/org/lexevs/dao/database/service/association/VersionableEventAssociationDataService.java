package org.lexevs.dao.database.service.association;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.relations.AssociationData;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.lexevs.dao.database.access.association.AssociationDataDao;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.access.versions.VersionsDao.EntryStateType;
import org.lexevs.dao.database.constants.classifier.property.EntryStateTypeClassifier;
import org.lexevs.dao.database.service.AbstractDatabaseService;

public class VersionableEventAssociationDataService extends
		AbstractDatabaseService implements AssociationDataService {

	private EntryStateTypeClassifier entryStateClassifier = new EntryStateTypeClassifier();
	
	@Override
	public void insertAssociationData(String codingSchemeUri, String version,
			String relationContainerName, String associationPredicateName,
			AssociationSource source, AssociationData data) {

		String codingSchemeUId = this.getCodingSchemeUId(codingSchemeUri,
				version);

		String associationPredicateUId = this.getDaoManager()
				.getAssociationDao(codingSchemeUri, version)
				.getAssociationPredicateUIdByContainerName(codingSchemeUId,
						relationContainerName, associationPredicateName);

		this.getDaoManager().getAssociationDataDao(codingSchemeUri, version)
				.insertAssociationData(codingSchemeUId,
						associationPredicateUId, source, data);
	}

	@Override
	public void updateAssociationData(String codingSchemeUri, String version,
			AssociationSource source, AssociationData data) {
	
		String codingSchemeUId = this.getCodingSchemeUId(codingSchemeUri,
				version);
	
		AssociationDataDao associationDataDao = this.getDaoManager()
				.getAssociationDataDao(codingSchemeUri, version);
	
		VersionsDao versionsDao = getDaoManager().getVersionsDao(
				codingSchemeUri, version);
	
		String associationDataUId = associationDataDao
				.getAssociationDataUId(codingSchemeUId, data.getAssociationInstanceId());
	
		Boolean assnQualExist = data.getAssociationQualificationCount() > 0 ? true : false;
		Boolean contextExist = data.getUsageContextCount() > 0 ? true : false;
		
		/* 1. insert current association data into history. */
		String prevEntryStateUId = associationDataDao
				.insertHistoryAssociationData(codingSchemeUId, associationDataUId, assnQualExist, contextExist);
	
		/* 2. update the attributes of the association data. */
		String entryStateUId = associationDataDao
				.updateAssociationData(codingSchemeUId, associationDataUId, source, data);
	
		/* 3. register entrystate details for the association data. */
		versionsDao
				.insertEntryState(entryStateUId, associationDataUId,
						entryStateClassifier
								.classify(EntryStateType.ENTITYASSNSTODATA),
						prevEntryStateUId, data.getEntryState());
	}

	@Override
	public void removeAssociationData(String codingSchemeUri, String version,
			AssociationSource source, AssociationData data) {

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
		
		//remove entryStates?
	}

	@Override
	public void insertAssociationDataVersionableChanges(String codingSchemeUri,
			String version, AssociationSource source, AssociationData data) {

		String codingSchemeUId = this.getCodingSchemeUId(codingSchemeUri,
				version);

		AssociationDataDao associationDataDao = this.getDaoManager()
				.getAssociationDataDao(codingSchemeUri, version);

		VersionsDao versionsDao = getDaoManager().getVersionsDao(
				codingSchemeUri, version);

		String associationDataUId = associationDataDao
				.getAssociationDataUId(codingSchemeUId, data.getAssociationInstanceId());

		Boolean assnQualExist = data.getAssociationQualificationCount() > 0 ? true : false;
		Boolean contextExist = data.getUsageContextCount() > 0 ? true : false;
		
		/* 1. insert current association data into history. */
		String prevEntryStateUId = associationDataDao
				.insertHistoryAssociationData(codingSchemeUId, associationDataUId, assnQualExist, contextExist);

		/* 2. update the versionable attributes of the association data. */
		String entryStateUId = associationDataDao
				.updateVersionableChanges(codingSchemeUId, associationDataUId, source, data);

		/* 3. register entrystate details for the association data. */
		versionsDao
				.insertEntryState(entryStateUId, associationDataUId,
						entryStateClassifier
								.classify(EntryStateType.ENTITYASSNSTODATA),
						prevEntryStateUId, data.getEntryState());
	}

	@Override
	public void revise(String codingSchemeUri, String version, 
			String relationContainerName, String associationPredicateName,
			AssociationSource source, AssociationData data) throws LBException {

		if (data == null)
			throw new LBParameterException(
					"Association data object is not supplied.");

		EntryState entryState = data.getEntryState();

		if (entryState == null) {
			throw new LBRevisionException("EntryState can't be null.");
		}

		String revisionId = entryState.getContainingRevision();
		ChangeType changeType = entryState.getChangeType();

		String csUId = this.getDaoManager().getCodingSchemeDao(codingSchemeUri,
				version).getCodingSchemeUIdByUriAndVersion(codingSchemeUri,
				version);

		AssociationDataDao assocDataDao = this.getDaoManager()
				.getAssociationDataDao(codingSchemeUri, version);

		String assocDataUId = assocDataDao.getAssociationDataUId(csUId, data
				.getAssociationInstanceId());

		String assocDataLatestRevisionId = assocDataDao.getLatestRevision(csUId,
				assocDataUId);
		
		if (revisionId != null && changeType != null) {

			if (changeType == ChangeType.NEW) {
				if (entryState.getPrevRevision() != null) {
					throw new LBRevisionException(
							"Changes of type NEW are not allowed to have previous revisions.");
				}
			} else if (assocDataUId == null) {
				throw new LBRevisionException(
						"The association data being revised doesn't exist.");
			} else if (entryState.getPrevRevision() == null) {
				throw new LBRevisionException(
						"All changes of type other than NEW should have previous revisions.");
			} else if (assocDataLatestRevisionId != null
					&& !assocDataLatestRevisionId.equalsIgnoreCase(entryState
							.getPrevRevision())) {
				throw new LBRevisionException(
						"Revision source is not in sync with the database revisions. Previous revision id does not match with the latest revision id of the association data."
								+ "Please update the authoring instance with all the revisions and regenerate the source.");
			}
			
			if (changeType == ChangeType.NEW) {

				this.insertAssociationData(codingSchemeUri, version,
						relationContainerName, associationPredicateName,
						source, data);
			} else if (changeType == ChangeType.REMOVE) {

				this.removeAssociationData(codingSchemeUri, version,
						source, data);
			} else if (changeType == ChangeType.MODIFY) {

				this.updateAssociationData(codingSchemeUri, version,
						source, data);
			} else if (changeType == ChangeType.VERSIONABLE) {

				this.insertAssociationDataVersionableChanges(codingSchemeUri,
						version, source, data);
			}
		}
	}
}
