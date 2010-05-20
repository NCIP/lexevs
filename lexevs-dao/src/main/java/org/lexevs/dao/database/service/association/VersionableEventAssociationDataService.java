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
import org.lexevs.dao.database.service.error.DatabaseErrorIdentifier;

public class VersionableEventAssociationDataService extends
		AbstractDatabaseService implements AssociationDataService {

	private EntryStateTypeClassifier entryStateClassifier = new EntryStateTypeClassifier();
	
	@Override
	@DatabaseErrorIdentifier(errorCode=INSERT_ASSOCIATIONDATA_ERROR)
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
	@DatabaseErrorIdentifier(errorCode=UPDATE_ASSOCIATIONDATA_ERROR)
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
	@DatabaseErrorIdentifier(errorCode=REMOVE_ASSOCIATIONDATA_ERROR)
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
	}

	@Override
	@DatabaseErrorIdentifier(errorCode=INSERT_ASSOCIATIONDATA_VERSIONABLE_CHANGES_ERROR)
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

		if (validRevision(codingSchemeUri, version, relationContainerName,
				associationPredicateName, source, data)) {
			
			ChangeType changeType = data.getEntryState().getChangeType();
			
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

	private boolean validRevision(String codingSchemeUri, String version,
			String relationContainerName, String associationPredicateName,
			AssociationSource source, AssociationData data) throws LBException {
		
		String csUId = null;
		AssociationDataDao assocDataDao = null;
		String assocDataUId = null;
		
		if (data == null)
			throw new LBParameterException(
					"Association data object is not supplied.");

		EntryState entryState = data.getEntryState();

		if (entryState == null) {
			throw new LBRevisionException("EntryState can't be null.");
		}

		ChangeType changeType = entryState.getChangeType();
		
		try {
			csUId = this.getDaoManager().getCodingSchemeDao(codingSchemeUri,
					version).getCodingSchemeUIdByUriAndVersion(codingSchemeUri,
					version);
		} catch (Exception e) {
			throw new LBRevisionException(
					"The coding scheme to which the association data belongs to doesnt exist.");
		}
		
		try {
			assocDataDao = this.getDaoManager().getAssociationDataDao(
					codingSchemeUri, version);

			assocDataUId = assocDataDao.getAssociationDataUId(csUId, data
					.getAssociationInstanceId());
		} catch (Exception e) {
			//do nothing.
		}

		if (changeType == ChangeType.NEW) {
			if (entryState.getPrevRevision() != null) {
				throw new LBRevisionException(
						"Changes of type NEW are not allowed to have previous revisions.");
			}
			
			if (assocDataUId != null) {
				throw new LBRevisionException(
						"The association data being added already exist.");
			}
			
		} else {

			if (assocDataUId == null) {
				throw new LBRevisionException(
						"The association data being revised doesn't exist.");
			} 

			String assocDataLatestRevisionId = assocDataDao.getLatestRevision(csUId,
					assocDataUId);
			
			String currentRevision = entryState.getContainingRevision();
			String prevRevision = entryState.getPrevRevision();
			
			if (entryState.getPrevRevision() == null
					&& assocDataLatestRevisionId != null
					&& !assocDataLatestRevisionId.equals(currentRevision)) {
				throw new LBRevisionException(
						"All changes of type other than NEW should have previous revisions.");
			} else if (assocDataLatestRevisionId != null
					&& !assocDataLatestRevisionId.equals(currentRevision)
					&& !assocDataLatestRevisionId.equals(prevRevision)) {
				throw new LBRevisionException(
						"Revision source is not in sync with the database revisions. "
								+ "Previous revision id does not match with the latest revision id of the association data."
								+ "Please update the authoring instance with all the revisions and regenerate the source.");
			}
		}
		
		return true;
	}
}
