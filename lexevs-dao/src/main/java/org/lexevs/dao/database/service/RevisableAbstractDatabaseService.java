/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.lexevs.dao.database.service;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.commonTypes.Versionable;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.access.versions.VersionsDao.EntryStateType;
import org.lexevs.dao.database.service.version.VersionableEventAuthoringService;
import org.springframework.util.Assert;
import org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId;

/**
 * The Class RevisableAbstractDatabaseService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class RevisableAbstractDatabaseService<T extends Versionable, I extends CodingSchemeUriVersionBasedEntryId> extends AbstractDatabaseService {
	
	
	/**
	 * The Class ParentUidReferencingId.
	 */
	public static class ParentUidReferencingId extends CodingSchemeUriVersionBasedEntryId {

		/** The parent uid. */
		private String parentUid;
		
		/**
		 * Instantiates a new parent uid referencing id.
		 * 
		 * @param codingSchemeUri the coding scheme uri
		 * @param codingSchemeVersion the coding scheme version
		 * @param parentUid the parent uid
		 */
		public ParentUidReferencingId(String codingSchemeUri,
				String codingSchemeVersion, String parentUid) {
			super(codingSchemeUri, codingSchemeVersion);
			this.parentUid = parentUid;
		}

		/**
		 * Gets the parent uid.
		 * 
		 * @return the parent uid
		 */
		public String getParentUid() {
			return parentUid;
		}

		/**
		 * Sets the parent uid.
		 * 
		 * @param parentUid the new parent uid
		 */
		public void setParentUid(String parentUid) {
			this.parentUid = parentUid;
		}
	}
	
	/**
	 * The Class CodingSchemeUriVersionBasedEntryId.
	 * 
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public static class CodingSchemeUriVersionBasedEntryId {
		
		/** The coding scheme uri. */
		String codingSchemeUri;
		
		/** The coding scheme version. */
		String codingSchemeVersion;

		/**
		 * Instantiates a new coding scheme uri version based entry id.
		 * 
		 * @param codingSchemeUri the coding scheme uri
		 * @param codingSchemeVersion the coding scheme version
		 */
		public CodingSchemeUriVersionBasedEntryId(String codingSchemeUri,
				String codingSchemeVersion) {
			super();
			this.codingSchemeUri = codingSchemeUri;
			this.codingSchemeVersion = codingSchemeVersion;
		}
		
		/**
		 * Gets the coding scheme uri.
		 * 
		 * @return the coding scheme uri
		 */
		public String getCodingSchemeUri() {
			return codingSchemeUri;
		}
		
		/**
		 * Sets the coding scheme uri.
		 * 
		 * @param codingSchemeUri the new coding scheme uri
		 */
		public void setCodingSchemeUri(String codingSchemeUri) {
			this.codingSchemeUri = codingSchemeUri;
		}
		
		/**
		 * Gets the coding scheme version.
		 * 
		 * @return the coding scheme version
		 */
		public String getCodingSchemeVersion() {
			return codingSchemeVersion;
		}
		
		/**
		 * Sets the coding scheme version.
		 * 
		 * @param codingSchemeVersion the new coding scheme version
		 */
		public void setCodingSchemeVersion(String codingSchemeVersion) {
			this.codingSchemeVersion = codingSchemeVersion;
		}
	}

	/**
	 * Resolve current entry state uid.
	 * 
	 * @param id the id
	 * @param entryUid the entry uid
	 * @param type the type
	 * 
	 * @return the string
	 */
	protected String resolveCurrentEntryStateUid(I id, String entryUid, EntryStateType type) {
		String codingSchemeUri = id.getCodingSchemeUri();
		String version = id.getCodingSchemeVersion();
		
		CodingSchemeDao codingSchemeDao = this.getDaoManager()
			.getCodingSchemeDao(codingSchemeUri, version);
		
		codingSchemeDao.
				getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
		
		String codingSchemeUid = codingSchemeDao.getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
		
		String entryStateUid = getCurrentEntryStateUid(
				id, entryUid);

		if(StringUtils.isBlank(entryStateUid) || !entryStateExists(id, entryStateUid)) {
			EntryState entryState = new EntryState();

			entryState.setChangeType(ChangeType.NEW);
			entryState.setRelativeOrder(0L);
			
			String containingRevision = codingSchemeDao.getRevisionWhenNew(codingSchemeUid);
			
			entryState.setContainingRevision(containingRevision);

			if(StringUtils.isBlank(entryStateUid)){
				return this.getDaoManager().getVersionsDao(codingSchemeUri, version).
					insertEntryState(
						codingSchemeUid,
						entryUid,
						type, 
						null,
						entryState);
			} else {
				this.getDaoManager().getVersionsDao(codingSchemeUri, version).
				insertEntryState(
						codingSchemeUid,
						entryStateUid,
						entryUid,
						type, 
						null,
						entryState);
				
				return entryStateUid; 
			}
		} else {
			return entryStateUid;
		}
	}
	
	/**
	 * The Interface ChangeDatabaseStateTemplate.
	 * 
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	private interface ChangeDatabaseStateTemplate<I extends CodingSchemeUriVersionBasedEntryId, T extends Versionable> {
		
		/**
		 * Do change.
		 * 
		 * @param id the id
		 * @param entryUid the entry uid
		 * @param revisedEntry the revised entry
		 * @param type the type
		 * 
		 * @return the string
		 */
		String doChange(
				I id, 
				String entryUid,
				T revisedEntry, 
				EntryStateType type);
	}
	
	/**
	 * Make change.
	 * 
	 * @param id the id
	 * @param revisedEntry the revised entry
	 * @param type the type
	 * @param template the template
	 * 
	 * @throws LBException the LB exception
	 */
	protected void makeChange(
			I id, 
			T revisedEntry, 
			EntryStateType type, 
			ChangeDatabaseStateTemplate<I,T> template) throws LBException {
		
		Assert.noNullElements(new Object[] {id, revisedEntry, type, template} );
		
		String codingSchemeUri = id.getCodingSchemeUri();
		String version = id.getCodingSchemeVersion();
		
		CodingSchemeDao codingSchemeDao = getDaoManager().getCodingSchemeDao(codingSchemeUri, version);
		
		VersionsDao versionsDao = getDaoManager().getVersionsDao(codingSchemeUri, version);
		
		String codingSchemeUId = codingSchemeDao.
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
		Assert.notNull(codingSchemeUId);
		
		String entryUId = getEntryUid(id, revisedEntry);
		Assert.notNull(entryUId, "The 'getEntryUid' method failed to produce the current Entry's Uid.");
		
		T currentEntry = getCurrentEntry(id, entryUId);
		Assert.notNull(currentEntry, "The 'getCurrentEntry' method failed to produce the current Entry.");
		
		String currentEntryStateUid = this.resolveCurrentEntryStateUid(id, entryUId, type);
		Assert.notNull(currentEntryStateUid);
		
		if(!this.isChangeTypeDependent(currentEntry) || this.isChangeTypeRemove(revisedEntry)) {
			this.insertIntoHistory(id, currentEntry, entryUId);
		} 
		
		if(this.isChangeTypeRemove(revisedEntry)) {
			currentEntryStateUid = null;
		}
		
		String entryStateUId = template.doChange(
				id, 
				entryUId,
				revisedEntry, 
				type);

		/* register entrystate details for the entry.*/
		if (!this.isChangeTypeDependent(revisedEntry)) {
			versionsDao.insertEntryState(codingSchemeUId, entryStateUId,
					entryUId, type, currentEntryStateUid, revisedEntry
							.getEntryState());
		}
		
		/* apply dependent changes for the entry.*/
		this.doInsertDependentChanges(id, revisedEntry);
	}
	
	/**
	 * Insert versionable changes.
	 * 
	 * @param id the id
	 * @param revisedEntry the revised entry
	 * @param type the type
	 * 
	 * @throws LBException the LB exception
	 */
	protected void insertVersionableChanges(
			I id, 
			T revisedEntry,
			EntryStateType type) throws LBException {

		this.makeChange(id, revisedEntry, type, new ChangeDatabaseStateTemplate<I,T>() {

			@Override
			public String doChange(I id, String entryUid, T revisedEntry, EntryStateType type) {
				return updateEntryVersionableAttributes(id, entryUid, revisedEntry);	
			}
		});
	}

	/**
	 * The Interface UpdateTemplate.
	 * 
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public static interface UpdateTemplate {
		
		/**
		 * Update.
		 * 
		 * @return the string
		 */
		public String update();
	}
	
	/**
	 * Update entry.
	 * 
	 * @param id the id
	 * @param updatedEntry the updated entry
	 * @param type the type
	 * @param updateTemplate the update template
	 * 
	 * @throws LBException the LB exception
	 */
	protected void updateEntry(I id, T updatedEntry, EntryStateType type, final UpdateTemplate updateTemplate) throws LBException {
		
		this.makeChange(id, updatedEntry, type, new ChangeDatabaseStateTemplate<I,T>() {

			@Override
			public String doChange(I id, String entryUid, T revisedEntry, EntryStateType type) {
				return updateTemplate.update();	
			}
		});
	}

	/**
	 * Insert dependent changes.
	 * 
	 * @param id the id
	 * @param revisedEntry the revised entry
	 * @param type the type
	 * 
	 * @throws LBException the LB exception
	 */
	protected void insertDependentChanges(I id,
			T revisedEntry, EntryStateType type) throws LBException {
		this.makeChange(id, revisedEntry, type, new ChangeDatabaseStateTemplate<I,T>() {

			@Override
			public String doChange(I id, String entryUid, T revisedEntry, EntryStateType type) {
				return null;	
			}
		});
	}
	
	/**
	 * Resolve entry by revision.
	 * 
	 * @param id the id
	 * @param entryUid the entry uid
	 * @param revisionId the revision id
	 * 
	 * @return the t
	 * 
	 * @throws LBRevisionException the LB revision exception
	 */
	public T resolveEntryByRevision(
			I id, String entryUid, String revisionId) throws LBRevisionException {
		
		if (entryUid == null) {
			throw new LBRevisionException(
					"Entry "
							+ entryUid
							+ " doesn't exist in lexEVS. "
							+ "Please check the identifying attributes of this entry. Its possible that the given entry "
							+ "has been REMOVEd from the lexEVS system in the past.");
		}
		
		String entryLatestRevisionId = this.getLatestRevisionId(id, entryUid);

		if(StringUtils.equals(revisionId, entryLatestRevisionId) || revisionId == null) {
			return this.getCurrentEntry(id, entryUid);
		} else {
			
			//Check to make sure a Revision with the given Id exists
			if(! this.isValidRevisionId(revisionId)) {
				throw new LBRevisionException(
						"No Revision with Id: "
								+ revisionId
								+ " exists in the system.");
			}
			
			CodingSchemeDao codingSchemeDao = 
				getDaoManager().getCodingSchemeDao(
						id.getCodingSchemeUri(), 
						id.getCodingSchemeVersion());
			
			VersionsDao versionsDao = 
				getDaoManager().getVersionsDao(
						id.getCodingSchemeUri(), 
						id.getCodingSchemeVersion());

			
			String codingSchemeUId = codingSchemeDao.
				getCodingSchemeUIdByUriAndVersion(
						id.getCodingSchemeUri(), 
						id.getCodingSchemeVersion());

			T entry = this.getHistoryEntryByRevisionId(
					id, 
					entryUid, 
					revisionId);

			if(entry == null) {
				String adjustedRevisionId = 
					versionsDao.getPreviousRevisionIdFromGivenRevisionIdForEntry(codingSchemeUId, entryUid, revisionId);
				
				if(StringUtils.equals(adjustedRevisionId, entryLatestRevisionId)) {
					entry = this.getCurrentEntry(id, entryUid);
				} else {
					entry = this.getHistoryEntryByRevisionId(
							id, 
							entryUid, 
							adjustedRevisionId);
				}
				
				if(entry != null) {
					entry.setEntryState(versionsDao.
							getEntryStateByEntryUidAndRevisionId(
									codingSchemeUId, 
									entryUid, 
									adjustedRevisionId));
				}
			}
			
			if(entry != null) {
				return addDependentAttributesByRevisionId(id, entryUid, entry, revisionId);
			} else {
				return null;
			}
		}
	}
	
	/**
	 * Valid revision.
	 * 
	 * @param id the id
	 * @param entry the entry
	 * 
	 * @return true, if successful
	 * 
	 * @throws LBException the LB exception
	 */
	protected boolean validRevision(I id, T entry) throws LBException {
		
		String invalid = "Invalid Revision. ";
		
		if (entry == null)
			throw new LBParameterException(invalid + "Entry is null.");
		
		EntryState entryState = entry.getEntryState();

		if (entryState == null) {
			throw new LBRevisionException(invalid + "EntryState is null.");
		}
		
		if (entryState.getContainingRevision() == null) {
			throw new LBRevisionException(invalid
					+ "Revision identifier is null for the versionable object.");
		}

		ChangeType changeType = entryState.getChangeType();
		String entryUid = null;
		
		try {
			entryUid = this.getEntryUid(id, entry);
		} catch (Exception e) {
			//Do nothing.
		} 
		
		if (changeType == ChangeType.NEW) {
			if (entryState.getPrevRevision() != null) {
				throw new LBRevisionException(
						invalid + "Changes of type NEW are not allowed to have previous revisions.");
			}
			
			if (entryUid != null) {
				throw new LBRevisionException(invalid
						+ "The entry being added already exists.");
			}
		} else {
			
			if (entryUid == null) {
				throw new LBRevisionException(invalid +
						"The entry being revised doesn't exist.");
			} 
			
			String latestRevId = this.getLatestRevisionId(id, entryUid);
			
			String currentRevision = entryState.getContainingRevision();
			
			if (entryState.getPrevRevision() == null && latestRevId != null
					&& !latestRevId.equals(currentRevision)) {
				entryState.setPrevRevision(latestRevId);
			}
			
			String prevRevision = entryState.getPrevRevision();

			if (latestRevId != null
					&& prevRevision != null
					&& !latestRevId.equals(currentRevision)
					&& !latestRevId.equals(prevRevision)
					&& !latestRevId
							.startsWith(VersionableEventAuthoringService.LEXGRID_GENERATED_REVISION)) {
				throw new LBRevisionException(
						invalid
								+ "\n -- Revision source is not in sync with the database revisions. "
								+ "\n -- Previous revision id does not match with the latest revision id of the coding scheme. "
								+ "\n -- Please update the authoring instance with all the revisions and regenerate the source.");
			}
		}
		
		return true;
	}
	
	/**
	 * Adds the dependent attributes by revision id.
	 * 
	 * @param id the id
	 * @param entryUid the entry uid
	 * @param entry the entry
	 * @param revisionId the revision id
	 * 
	 * @return the t
	 */
	protected abstract T addDependentAttributesByRevisionId(I id, String entryUid, T entry, String revisionId);

	/**
	 * Insert into history.
	 * 
	 * @param id the id
	 * @param currentEntry the current entry
	 * @param entryUId the entry u id
	 */
	protected abstract void insertIntoHistory(I id, T currentEntry, String entryUId);

	/**
	 * Do insert dependent changes.
	 * 
	 * @param id the id
	 * @param revisedEntry the revised entry
	 * 
	 * @throws LBException the LB exception
	 */
	protected abstract void doInsertDependentChanges(I id, T revisedEntry) throws LBException;

	/**
	 * Update entity versionable attributes.
	 * 
	 * @param id the id
	 * @param entryUId the entry u id
	 * @param revisedEntity the revised entity
	 * 
	 * @return the string
	 */
	protected abstract String updateEntryVersionableAttributes(I id, String entryUId, T revisedEntity);
	
	/**
	 * Gets the current entry.
	 * 
	 * @param id the id
	 * @param entryUId the entry u id
	 * 
	 * @return the current entry
	 */
	protected abstract T getCurrentEntry(I id, String entryUId);
	
	/**
	 * Gets the history entry by revision id.
	 * 
	 * @param id the id
	 * @param entryUid the entry uid
	 * @param revisionId the revision id
	 * 
	 * @return the history entry by revision id
	 */
	protected abstract T getHistoryEntryByRevisionId(I id, String entryUid, String revisionId);
	
	/**
	 * Gets the latest revision id.
	 * 
	 * @param id the id
	 * @param entryUId the entry u id
	 * 
	 * @return the latest revision id
	 */
	protected abstract String getLatestRevisionId(I id, String entryUId);

	/**
	 * Gets the entry uid.
	 * 
	 * @param id the id
	 * @param entry the entry
	 * 
	 * @return the entry uid
	 */
	protected abstract String getEntryUid(I id, T entry);
	
	/**
	 * Entry state exists.
	 * 
	 * @param id the id
	 * @param entryStateUid the entry state uid
	 * 
	 * @return true, if successful
	 */
	protected abstract boolean entryStateExists(I id, String entryStateUid);
	
	/**
	 * Gets the current entry state uid.
	 * 
	 * @param id the id
	 * @param entryUid the entry uid
	 * 
	 * @return the current entry state uid
	 */
	protected abstract String getCurrentEntryStateUid(I id, String entryUid);
	
	/**
	 * Gets the coding scheme uid.
	 * 
	 * @param id the id
	 * 
	 * @return the coding scheme uid
	 */
	protected String getCodingSchemeUid(CodingSchemeUriVersionBasedEntryId id) {
		String uri = id.getCodingSchemeUri();
		String version = id.getCodingSchemeVersion();
		
		return this.getDaoManager().
			getCodingSchemeDao(uri, version).getCodingSchemeUIdByUriAndVersion(uri, version);
	}
	
	protected boolean isValidRevisionId(String revisionId) {
		String revisionUid = 
			getDaoManager().getRevisionDao().getRevisionUIdById(revisionId);
		
		return ! (revisionUid == null);
	}
}