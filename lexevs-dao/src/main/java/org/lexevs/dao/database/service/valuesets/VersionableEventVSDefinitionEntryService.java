
package org.lexevs.dao.database.service.valuesets;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.lexevs.dao.database.access.valuesets.VSDefinitionEntryDao;
import org.lexevs.dao.database.access.valuesets.VSPropertyDao.ReferenceType;
import org.lexevs.dao.database.service.AbstractDatabaseService;

/**
 * The Class VersionableEventVSDefinitionEntryService.
 */
public class VersionableEventVSDefinitionEntryService extends AbstractDatabaseService implements VSDefinitionEntryService {

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.VSDefinitionEntryService#insertVSDefinitionEntry(java.lang.String, org.LexGrid.valueSets.DefinitionEntry)
	 */
	@Override
	public void insertVSDefinitionEntry(String valueSetDefinitionURI,
			DefinitionEntry defEntry) {

		String valueSetDefUId = this.getDaoManager()
				.getCurrentValueSetDefinitionDao()
				.getGuidFromvalueSetDefinitionURI(valueSetDefinitionURI);

		this.getDaoManager().getCurrentVSDefinitionEntryDao()
				.insertDefinitionEntry(valueSetDefUId, defEntry);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.VSDefinitionEntryService#removeVSDefinitionEntry(java.lang.String, org.LexGrid.valueSets.DefinitionEntry)
	 */
	@Override
	public void removeVSDefinitionEntry(String valueSetDefinitionURI,
			DefinitionEntry defEntry) {
		
		VSDefinitionEntryDao vsDefinitionEntryDao = this.getDaoManager()
				.getCurrentVSDefinitionEntryDao();

		String vsDefinitionUId = vsDefinitionEntryDao.getDefinitionEntryUId(
				valueSetDefinitionURI, defEntry.getRuleOrder().toString());

		/* 1. Delete all definition entry entry states. */
		this.getDaoManager().getCurrentVsEntryStateDao()
				.deleteAllEntryStateEntriesByEntryUId(vsDefinitionUId);
		
		/* 2. Delete the definition entry. */
		vsDefinitionEntryDao.deleteDefinitionEntry(vsDefinitionUId);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.VSDefinitionEntryService#updateVSDefinitionEntry(java.lang.String, org.LexGrid.valueSets.DefinitionEntry)
	 */
	@Override
	public void updateVSDefinitionEntry(String valueSetDefinitionURI,
			DefinitionEntry defEntry) {

		VSDefinitionEntryDao vsDefinitionEntryDao = this.getDaoManager()
				.getCurrentVSDefinitionEntryDao();

		String vsDefinitionUId = vsDefinitionEntryDao.getDefinitionEntryUId(
				valueSetDefinitionURI, defEntry.getRuleOrder().toString());

		String prevEntryStateUId = vsDefinitionEntryDao.insertHistoryDefinitionEntry(vsDefinitionUId, vsDefinitionUId, defEntry);
		
		String entryStateUId = vsDefinitionEntryDao.updateDefinitionEntry(vsDefinitionUId, defEntry);
		
		this.getDaoManager().getCurrentVsEntryStateDao().insertEntryState(
				entryStateUId, vsDefinitionUId,
				ReferenceType.DEFINITIONENTRY.name(), prevEntryStateUId,
				defEntry.getEntryState());
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.VSDefinitionEntryService#updateVSDefinitionEntryVersionableChanges(java.lang.String, org.LexGrid.valueSets.DefinitionEntry)
	 */
	@Override
	public void updateVSDefinitionEntryVersionableChanges(
			String valueSetDefinitionURI, DefinitionEntry defEntry) {

		VSDefinitionEntryDao vsDefinitionEntryDao = this.getDaoManager()
				.getCurrentVSDefinitionEntryDao();

		String vsDefinitionUId = vsDefinitionEntryDao.getDefinitionEntryUId(
				valueSetDefinitionURI, defEntry.getRuleOrder().toString());

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

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.VSDefinitionEntryService#revise(java.lang.String, org.LexGrid.valueSets.DefinitionEntry)
	 */
	@Override
	public void revise(String valueSetDefinitionURI, DefinitionEntry defEntry) throws LBException {

		if( validRevision(valueSetDefinitionURI, defEntry)) {
			
			ChangeType changeType = defEntry.getEntryState().getChangeType();
			
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

	/**
	 * Valid revision.
	 * 
	 * @param valueSetDefinitionURI the value set definition uri
	 * @param defEntry the def entry
	 * 
	 * @return true, if successful
	 * 
	 * @throws LBException the LB exception
	 */
	private boolean validRevision(String valueSetDefinitionURI,
			DefinitionEntry defEntry) throws LBException {
		
		if(  defEntry == null) 
			throw new LBParameterException("definition entry is null.");
		
		EntryState entryState = defEntry.getEntryState();

		if (entryState == null) {
			throw new LBRevisionException("EntryState can't be null.");
		}

		ChangeType changeType = entryState.getChangeType();

		VSDefinitionEntryDao entryDao = this.getDaoManager()
				.getCurrentVSDefinitionEntryDao();

		String vsDefEntryUId = entryDao.getDefinitionEntryUId(
				valueSetDefinitionURI, defEntry.getRuleOrder().toString());
		
		if (changeType == ChangeType.NEW) {
			if (entryState.getPrevRevision() != null) {
				throw new LBRevisionException(
						"Changes of type NEW are not allowed to have previous revisions.");
			}
			
			if (vsDefEntryUId != null) {
				throw new LBRevisionException(
						"The vsDefinitionEntry being added already exist. Change the rule order (increment) and try again.");
			}
			
		} else {

			if (vsDefEntryUId == null) {
				throw new LBRevisionException(
						"The vsDefinitionEntry being revised doesn't exist.");
			}

			String vsEntryLatestRevisionId = entryDao.getLatestRevision(vsDefEntryUId);
			
			String currentRevision = entryState.getContainingRevision();
			String prevRevision = entryState.getPrevRevision();
			
			if (entryState.getPrevRevision() == null
					&& vsEntryLatestRevisionId != null
					&& !vsEntryLatestRevisionId.equals(currentRevision)) {
				throw new LBRevisionException(
						"All changes of type other than NEW should have previous revisions.");
			} else if (vsEntryLatestRevisionId != null
					&& !vsEntryLatestRevisionId.equals(currentRevision)
					&& !vsEntryLatestRevisionId.equals(prevRevision)) {
				throw new LBRevisionException(
						"Revision source is not in sync with the database revisions. " +
						"Previous revision id does not match with the latest revision id of the vsDefinitionEntry."
								+ "Please update the authoring instance with all the revisions and regenerate the source.");
			}
		}
		
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.VSDefinitionEntryService#resolveDefinitionEntryByRevision(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public DefinitionEntry resolveDefinitionEntryByRevision(
			String valueSetDefURI, String ruleOrder, String revisionId)
			throws LBRevisionException {
		VSDefinitionEntryDao vsDefinitionEntryDao = this.getDaoManager()
				.getCurrentVSDefinitionEntryDao();

		return vsDefinitionEntryDao.resolveDefinitionEntryByRevision(valueSetDefURI,
				ruleOrder, revisionId);
	}
}