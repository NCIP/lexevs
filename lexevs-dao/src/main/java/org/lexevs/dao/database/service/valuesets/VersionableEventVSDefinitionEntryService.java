package org.lexevs.dao.database.service.valuesets;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.lexevs.dao.database.access.valuesets.VSDefinitionEntryDao;
import org.lexevs.dao.database.access.valuesets.ValueSetDefinitionDao;
import org.lexevs.dao.database.access.valuesets.VSPropertyDao.ReferenceType;
import org.lexevs.dao.database.service.AbstractDatabaseService;

public class VersionableEventVSDefinitionEntryService extends AbstractDatabaseService implements VSDefinitionEntryService {

	@Override
	public void insertVSDefinitionEntry(String valueSetDefinitionURI,
			DefinitionEntry defEntry) {

		String valueSetDefUId = this.getDaoManager()
				.getCurrentValueSetDefinitionDao()
				.getGuidFromvalueSetDefinitionURI(valueSetDefinitionURI);

		this.getDaoManager().getCurrentVSDefinitionEntryDao()
				.insertDefinitionEntry(valueSetDefUId, defEntry);
	}

	@Override
	public void removeVSDefinitionEntry(String valueSetDefinitionURI,
			DefinitionEntry defEntry) {
		
		VSDefinitionEntryDao vsDefinitionEntryDao = this.getDaoManager()
				.getCurrentVSDefinitionEntryDao();

		String valueSetDefUId = this.getDaoManager()
				.getCurrentValueSetDefinitionDao()
				.getGuidFromvalueSetDefinitionURI(valueSetDefinitionURI);

		String vsDefinitionUId = vsDefinitionEntryDao.getDefinitionEntryUId(
				valueSetDefUId, defEntry.getRuleOrder().toString());

		/* 1. Delete all definition entry entry states. */
		this.getDaoManager().getCurrentVsEntryStateDao()
				.deleteAllEntryStateEntriesByEntryUId(vsDefinitionUId);
		
		/* 2. Delete the definition entry. */
		vsDefinitionEntryDao.deleteDefinitionEntry(vsDefinitionUId);
	}

	@Override
	public void updateVSDefinitionEntry(String valueSetDefinitionURI,
			DefinitionEntry defEntry) {

		VSDefinitionEntryDao vsDefinitionEntryDao = this.getDaoManager()
				.getCurrentVSDefinitionEntryDao();

		String valueSetDefUId = this.getDaoManager()
				.getCurrentValueSetDefinitionDao()
				.getGuidFromvalueSetDefinitionURI(valueSetDefinitionURI);

		String vsDefinitionUId = vsDefinitionEntryDao.getDefinitionEntryUId(
				valueSetDefUId, defEntry.getRuleOrder().toString());

		String prevEntryStateUId = vsDefinitionEntryDao.insertHistoryDefinitionEntry(vsDefinitionUId, vsDefinitionUId, defEntry);
		
		String entryStateUId = vsDefinitionEntryDao.updateDefinitionEntry(vsDefinitionUId, defEntry);
		
		this.getDaoManager().getCurrentVsEntryStateDao().insertEntryState(
				entryStateUId, vsDefinitionUId,
				ReferenceType.DEFINITIONENTRY.name(), prevEntryStateUId,
				defEntry.getEntryState());
	}

	@Override
	public void updateVSDefinitionEntryVersionableChanges(
			String valueSetDefinitionURI, DefinitionEntry defEntry) {

		VSDefinitionEntryDao vsDefinitionEntryDao = this.getDaoManager()
				.getCurrentVSDefinitionEntryDao();

		String valueSetDefUId = this.getDaoManager()
				.getCurrentValueSetDefinitionDao()
				.getGuidFromvalueSetDefinitionURI(valueSetDefinitionURI);

		String vsDefinitionUId = vsDefinitionEntryDao.getDefinitionEntryUId(
				valueSetDefUId, defEntry.getRuleOrder().toString());

		String prevEntryStateUId = vsDefinitionEntryDao
				.insertHistoryDefinitionEntry(vsDefinitionUId, vsDefinitionUId,
						defEntry);

		String entryStateUId = vsDefinitionEntryDao.updateDefinitionEntryVersionableAttrib(
				vsDefinitionUId, defEntry);

		this.getDaoManager().getCurrentVsEntryStateDao().insertEntryState(
				entryStateUId, vsDefinitionUId,
				ReferenceType.DEFINITIONENTRY.name(), prevEntryStateUId,
				defEntry.getEntryState());
	}

	@Override
	public void revise(String valueSetDefinitionURI, DefinitionEntry defEntry) throws LBException {

		if(  defEntry == null) 
			throw new LBParameterException("definition entry is null.");
		
		EntryState entryState = defEntry.getEntryState();

		if (entryState == null) {
			throw new LBRevisionException("EntryState can't be null.");
		}

		String revisionId = entryState.getContainingRevision();
		ChangeType changeType = entryState.getChangeType();

		VSDefinitionEntryDao entryDao = this.getDaoManager().getCurrentVSDefinitionEntryDao();
		
		String vsDefEntryUId = entryDao.getDefinitionEntryUId(
						valueSetDefinitionURI, defEntry.getRuleOrder().toString());

		String vsEntryLatestRevisionId = entryDao.getLatestRevision(vsDefEntryUId);
		
		if (revisionId != null && changeType != null) {

			if (changeType == ChangeType.NEW) {
				if (entryState.getPrevRevision() != null) {
					throw new LBRevisionException(
							"Changes of type NEW are not allowed to have previous revisions.");
				}
			} else if (vsDefEntryUId == null) {
				throw new LBRevisionException(
						"The vsDefinitionEntry being revised doesn't exist.");
			} else if (entryState.getPrevRevision() == null) {
				throw new LBRevisionException(
						"All changes of type other than NEW should have previous revisions.");
			} else if (vsEntryLatestRevisionId != null
					&& !vsEntryLatestRevisionId.equalsIgnoreCase(entryState
							.getPrevRevision())) {
				throw new LBRevisionException(
						"Revision source is not in sync with the database revisions. Previous revision id does not match with the latest revision id of the vsDefinitionEntry."
								+ "Please update the authoring instance with all the revisions and regenerate the source.");
			}
			
			if (changeType == ChangeType.NEW) {

				this.insertVSDefinitionEntry(valueSetDefinitionURI, defEntry);
			} else if (changeType == ChangeType.REMOVE) {

				this.removeVSDefinitionEntry(valueSetDefinitionURI, defEntry);
			} else if (changeType == ChangeType.MODIFY) {

				this.updateVSDefinitionEntry(valueSetDefinitionURI, defEntry);
			} else if (changeType == ChangeType.VERSIONABLE) {

				this.updateVSDefinitionEntryVersionableChanges(valueSetDefinitionURI, defEntry);
			}
		}
	}
}
