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
import org.LexGrid.commonTypes.Versionable;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.access.versions.VersionsDao.EntryStateType;
import org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId;
import org.springframework.util.Assert;

/**
 * The Class RevisableAbstractDatabaseService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class RevisableAbstractDatabaseService<T extends Versionable, I extends CodingSchemeUriVersionBasedEntryId> extends AbstractDatabaseService {
	
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
		
		String codingSchemeUid = this.getDaoManager()
			.getCodingSchemeDao(codingSchemeUri, version).
				getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
		
		String entryStateUid = getCurrentEntryStateUid(
				id, entryUid);

		if(StringUtils.isBlank(entryStateUid) || !entryStateExists(id, entryStateUid)) {
			EntryState entryState = new EntryState();

			entryState.setChangeType(ChangeType.NEW);
			entryState.setRelativeOrder(0L);

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
		
		if(!this.isChangeTypeDependent(currentEntry)) {
			this.insertIntoHistory(id, currentEntry, entryUId);
		}
		
		String entryStateUId = template.doChange(
				id, 
				entryUId,
				revisedEntry, 
				type);

		/* 3. register entrystate details for the entity.*/
		versionsDao.insertEntryState(
				codingSchemeUId,
				entryStateUId, 
				entryUId,
				type,
				currentEntryStateUid, 
				revisedEntry.getEntryState());
		
		/* 4. apply dependent changes for the entity.*/
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
				return updateEntityVersionableAttributes(id, entryUid, revisedEntry);	
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
				return updateEntityVersionableAttributes(id, entryUid, revisedEntry);	
			}
		});
	}
	
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
	protected abstract String updateEntityVersionableAttributes(I id, String entryUId, T revisedEntity);
	
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
}
