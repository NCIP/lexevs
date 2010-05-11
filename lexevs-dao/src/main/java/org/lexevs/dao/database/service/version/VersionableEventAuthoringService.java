/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package org.lexevs.dao.database.service.version;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.codingSchemes.CodingSchemes;
import org.LexGrid.commonTypes.Versionable;
import org.LexGrid.naming.Mappings;
import org.LexGrid.valueSets.PickListDefinition;
import org.LexGrid.valueSets.PickListDefinitions;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.ValueSetDefinitions;
import org.LexGrid.versions.ChangedEntry;
import org.LexGrid.versions.EditHistory;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.SystemRelease;
import org.LexGrid.versions.types.ChangeType;
import org.lexevs.dao.database.access.revision.RevisionDao;
import org.lexevs.dao.database.access.systemRelease.SystemReleaseDao;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.service.valuesets.PickListDefinitionService;
import org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class VersionableEventVersionService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class VersionableEventAuthoringService extends AbstractDatabaseService
		implements AuthoringService {

	private CodingSchemeService codingSchemeService = null;

	private ValueSetDefinitionService valueSetDefinitionService = null;

	private PickListDefinitionService pickListDefinitionService = null;

	public static final String LEXGRID_GENERATED_REVISION = "autoGen-";
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lexevs.dao.database.service.version.VersionService#insertSystemRelease
	 * (org.LexGrid.versions.SystemRelease)
	 */
	@Override
	public void loadSystemRelease(SystemRelease systemRelease) throws LBRevisionException {

		if (systemRelease == null) {
			return;
		}

		/* 1. Insert system release entry.*/
		SystemReleaseDao sysReleaseDao = this.getDaoManager()
				.getSystemReleaseDao();
		sysReleaseDao.insertSystemReleaseEntry(systemRelease);
		
		/* 2. Insert revision entry.*/
		RevisionDao revisionDao = this.getDaoManager().getRevisionDao();
		Revision revision = new Revision();
		revision.setRevisionId(LEXGRID_GENERATED_REVISION + revisionDao.getNewRevisionId());

		/* 3. Include all coding schemes into revision.*/
		CodingSchemes codingSchemes = systemRelease.getCodingSchemes();
		if (codingSchemes != null) {
			CodingScheme[] codingSchemeList = codingSchemes.getCodingScheme();

			for (int i = 0; i < codingSchemeList.length; i++) {
				
				if( codingSchemeList[i].getEntryState() == null ) {
					EntryState entryState = new EntryState();
					
					entryState.setChangeType(ChangeType.NEW);
					entryState.setContainingRevision(revision.getRevisionId());
					entryState.setRelativeOrder(0L);
					
					codingSchemeList[i].setEntryState(entryState);
				}
				ChangedEntry changeEntry = new ChangedEntry();
				changeEntry.setChangedCodingSchemeEntry(codingSchemeList[i]);
				revision.addChangedEntry(changeEntry);
			}
		}

		/* 4. Include all valuesets into revision.*/
		ValueSetDefinitions valueSetDefinitions = systemRelease
				.getValueSetDefinitions();
		if (valueSetDefinitions != null) {
			
			ValueSetDefinition[] valueSetDefList = valueSetDefinitions.getValueSetDefinition();
			
			for (int i = 0; i < valueSetDefList.length; i++) {
				
				if( valueSetDefList[i].getEntryState() == null ) {
					EntryState entryState = new EntryState();
					
					entryState.setChangeType(ChangeType.NEW);
					entryState.setContainingRevision(revision.getRevisionId());
					entryState.setRelativeOrder(0L);
					
					valueSetDefList[i].setEntryState(entryState);
				}
				
				ChangedEntry changeEntry = new ChangedEntry();
				changeEntry.setChangedValueSetDefinitionEntry(valueSetDefList[i]);
				revision.addChangedEntry(changeEntry);
			}
		}

		/* 5. Include all picklists into revision.*/
		PickListDefinitions pickLists = systemRelease.getPickListDefinitions();
		if (pickLists != null) {
			PickListDefinition[] pickListDefList = pickLists
					.getPickListDefinition();
			Mappings mappings = pickLists.getMappings();
			
			for (int i = 0; i < pickListDefList.length; i++) {
				
				if( pickListDefList[i].getEntryState() == null ) {
					EntryState entryState = new EntryState();
					
					entryState.setChangeType(ChangeType.NEW);
					entryState.setContainingRevision(revision.getRevisionId());
					entryState.setRelativeOrder(0L);
					
					pickListDefList[i].setEntryState(entryState);
				}
				
				ChangedEntry changeEntry = new ChangedEntry();
				changeEntry.setChangedPickListDefinitionEntry(pickListDefList[i]);
				
				revision.addChangedEntry(changeEntry);
			}
		}

		/* 6. Load revision. */
		loadRevision(revision, systemRelease.getReleaseURI());
		
		/* 7. Process revisions.*/
		EditHistory editHistory = systemRelease.getEditHistory();
		if (editHistory != null) {
			Revision[] revisionList = editHistory.getRevision();
			
			if (revisionList != null && revisionList.length != 0) {
				for (int i = 0; i < revisionList.length; i++) {

					loadRevision(revisionList[i], systemRelease.getReleaseURI());
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lexevs.dao.database.service.version.VersionService#revise(org.LexGrid
	 * .versions.Revision)
	 */
	@Override
	@Transactional  
	public void loadRevision(Revision revision, String releaseURI) throws LBRevisionException {

		if (revision == null)
			return;

		RevisionDao revisionDao = this.getDaoManager().getRevisionDao();
		revisionDao.insertRevisionEntry(revision, releaseURI);

		ChangedEntry[] changedEntry = revision.getChangedEntry();

		if (changedEntry != null && changedEntry.length != 0) {
			for (int j = 0; j < changedEntry.length; j++) {

				ChangedEntry cEntry = changedEntry[j];

				if (cEntry != null) {

					// Process CodingScheme revisions
					try {
						CodingScheme codingScheme = cEntry
								.getChangedCodingSchemeEntry();
						if (codingScheme != null) {
							codingSchemeService
									.revise(codingScheme, releaseURI);
							continue;
						}
					} catch (LBException e) {
						super.getLogger().error(
								"Error occured while revising the codingScheme: " 
										+ e.getMessage());
					}

					// Process ValueDomain Definition revisions
					try {
						ValueSetDefinition valueSetDefinition = cEntry
								.getChangedValueSetDefinitionEntry();
						if (valueSetDefinition != null) {
							valueSetDefinitionService.revise(
									valueSetDefinition, releaseURI);
							continue;
						}
					} catch (LBException e) {
						super.getLogger().error(
								"Error occured while revising the valueSetDefinition : "
										+ e.getMessage());
					}

					// Process PickList Definition revisions
					try {
						PickListDefinition pickListDef = cEntry
								.getChangedPickListDefinitionEntry();
						if (pickListDef != null) {

							pickListDefinitionService.revise(pickListDef,
									releaseURI);
							continue;
						}
					} catch (LBException e) {
						super.getLogger().error(
								"Error occured while revising the pickListDefinition : "
										+ e.getMessage());
					}
				}
			}
		}
	}

	public void loadRevision(Versionable versionable, String releaseURI)
			throws LBRevisionException {

		RevisionDao revisionDao = this.getDaoManager().getRevisionDao();
		Revision revision = new Revision();
		revision.setRevisionId(LEXGRID_GENERATED_REVISION + revisionDao.getNewRevisionId());

		ChangedEntry changeEntry = new ChangedEntry();

		if( versionable.getEntryState() == null ) {
			EntryState entryState = new EntryState();
			
			entryState.setChangeType(ChangeType.NEW);
			entryState.setContainingRevision(revision.getRevisionId());
			entryState.setRelativeOrder(0L);
			
			versionable.setEntryState(entryState);
		}
		
		if (versionable instanceof CodingScheme) {

			changeEntry.setChangedCodingSchemeEntry((CodingScheme) versionable);

		} else if (versionable instanceof ValueSetDefinition) {
			changeEntry
					.setChangedValueSetDefinitionEntry((ValueSetDefinition) versionable);
		} else if (versionable instanceof PickListDefinition) {
			changeEntry
					.setChangedPickListDefinitionEntry((PickListDefinition) versionable);
		}

		revision.addChangedEntry(changeEntry);

		loadRevision(revision, releaseURI);
	}
	
	/**
	 * @return the codingSchemeService
	 */
	public CodingSchemeService getCodingSchemeService() {
		return codingSchemeService;
	}

	/**
	 * @param codingSchemeService
	 */
	public void setCodingSchemeService(CodingSchemeService codingSchemeService) {
		this.codingSchemeService = codingSchemeService;
	}

	/**
	 * @return the valueSetDefinitionService
	 */
	public ValueSetDefinitionService getValueSetDefinitionService() {
		return valueSetDefinitionService;
	}

	/**
	 * @param valueSetDefinitionService
	 *            the valueSetDefinitionService to set
	 */
	public void setValueSetDefinitionService(
			ValueSetDefinitionService valueSetDefinitionService) {
		this.valueSetDefinitionService = valueSetDefinitionService;
	}

	/**
	 * @return the pickListDefinitionService
	 */
	public PickListDefinitionService getPickListDefinitionService() {
		return pickListDefinitionService;
	}

	/**
	 * @param pickListDefinitionService
	 *            the pickListDefinitionService to set
	 */
	public void setPickListDefinitionService(
			PickListDefinitionService pickListDefinitionService) {
		this.pickListDefinitionService = pickListDefinitionService;
	}
}
