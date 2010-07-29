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
	@Transactional(rollbackFor=Exception.class)
	public void loadSystemRelease(SystemRelease systemRelease, Boolean indexNewCodingScheme) throws LBRevisionException {

		if (systemRelease == null) {
			return;
		}
		
		RevisionDao revisionDao = this.getDaoManager().getRevisionDao();
		String revisionId = LEXGRID_GENERATED_REVISION + revisionDao.getNewRevisionId();
		String releaseURI = systemRelease.getReleaseURI();
		
		/* 1. Insert system release entry.*/
		SystemReleaseDao sysReleaseDao = this.getDaoManager()
				.getSystemReleaseDao();
		sysReleaseDao.insertSystemReleaseEntry(systemRelease);
		
		/* 2. Insert revision entry.*/
		CodingSchemes codingSchemes = systemRelease.getCodingSchemes();
		ValueSetDefinitions valueSetDefinitions = systemRelease
				.getValueSetDefinitions();
		PickListDefinitions pickLists = systemRelease.getPickListDefinitions();
		
		if (codingSchemes != null || valueSetDefinitions != null
				|| pickLists != null) {

			Revision revision = new Revision();
			revision.setRevisionId(revisionId);
			revisionDao.insertRevisionEntry(revision, releaseURI);
		}
		
		/* 3. Add coding schemes.*/
		if (codingSchemes != null) {
			CodingScheme[] codingSchemeList = codingSchemes.getCodingScheme();

			for (int i = 0; i < codingSchemeList.length; i++) {
				
				codingSchemeList[i].setEntryState(getEntryState(revisionId));

				try {
					codingSchemeService.revise(codingSchemeList[i], releaseURI, indexNewCodingScheme);
				} catch (LBException e) {
					super.getLogger().error(
							"Error occured while revising the codingScheme: "
									+ e.getMessage());
				}
			}
		}

		/* 4. Add valueset definitions.*/
		if (valueSetDefinitions != null) {

			ValueSetDefinition[] valueSetDefList = valueSetDefinitions
					.getValueSetDefinition();
			Mappings vsMapping = valueSetDefinitions.getMappings();

			for (int i = 0; i < valueSetDefList.length; i++) {

				valueSetDefList[i].setEntryState(getEntryState(revisionId));

				try {
					valueSetDefinitionService.revise(valueSetDefList[i],
							vsMapping, releaseURI);
				} catch (LBException e) {
					super.getLogger().error(
							"Error occured while revising the valueSetDefinition : "
									+ e.getMessage());
				}
			}
		}

		/* 5. Add picklist definitions.*/
		if (pickLists != null) {
			PickListDefinition[] pickListDefList = pickLists
					.getPickListDefinition();
			Mappings plMappings = pickLists.getMappings();

			for (int i = 0; i < pickListDefList.length; i++) {

				pickListDefList[i].setEntryState(getEntryState(revisionId));

				try {
					pickListDefinitionService.revise(pickListDefList[i],
							plMappings, releaseURI);
				} catch (LBException e) {
					super.getLogger().error(
							"Error occured while revising the pickListDefinition : "
									+ e.getMessage());
				}
			}
		}
		
		/* 6. Process revisions.*/
		EditHistory editHistory = systemRelease.getEditHistory();
		if (editHistory != null) {
			Revision[] revisionList = editHistory.getRevision();
			
			if (revisionList != null && revisionList.length != 0) {
				for (int i = 0; i < revisionList.length; i++) {

					loadRevision(revisionList[i], systemRelease.getReleaseURI(), indexNewCodingScheme);
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
	@Transactional(rollbackFor=Exception.class)
	public void loadRevision(Revision revision, String releaseURI, Boolean indexNewCodingScheme) throws LBRevisionException {

		if (revision == null)
			return;

		ChangedEntry[] changedEntry = revision.getChangedEntry();

		if (changedEntry != null && changedEntry.length != 0) {
			
			RevisionDao revisionDao = this.getDaoManager().getRevisionDao();
			revisionDao.insertRevisionEntry(revision, releaseURI);
			
			for (int j = 0; j < changedEntry.length; j++) {

				ChangedEntry cEntry = changedEntry[j];

				if (cEntry != null) {

					// Process CodingScheme revisions
					try {
						CodingScheme codingScheme = cEntry
								.getChangedCodingSchemeEntry();
						if (codingScheme != null) {
							codingSchemeService
									.revise(codingScheme, releaseURI, indexNewCodingScheme);
							continue;
						}
					} catch (LBException e) {
						super.getLogger().error(
								"Error occured while revising the codingScheme: " 
										+ e.getMessage());
						throw new LBRevisionException("Error occured while revising the codingScheme: " 
										+ e.getMessage());
					}

					// Process ValueSet Definition revisions
					try {
						ValueSetDefinition valueSetDefinition = cEntry
								.getChangedValueSetDefinitionEntry();
						if (valueSetDefinition != null) {
							valueSetDefinitionService.revise(
									valueSetDefinition, null, releaseURI);
							continue;
						}
					} catch (LBException e) {
						super.getLogger().error(
								"Error occured while revising the valueSetDefinition : "
										+ e.getMessage());
						throw new LBRevisionException("Error occured while revising the valueSetDefinition : "
										+ e.getMessage());
					}

					// Process PickList Definition revisions
					try {
						PickListDefinition pickListDef = cEntry
								.getChangedPickListDefinitionEntry();
						if (pickListDef != null) {

							pickListDefinitionService.revise(pickListDef, null,
									releaseURI);
							continue;
						}
					} catch (LBException e) {
						super.getLogger().error(
								"Error occured while revising the pickListDefinition : "
										+ e.getMessage());
						throw new LBRevisionException("Error occured while revising the pickListDefinition : "
								+ e.getMessage());
					}
				}
			}
		}
	}

	@Transactional(rollbackFor=Exception.class)
	public void loadRevision(Versionable versionable, String releaseURI, Boolean indexNewCodingScheme)
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

		loadRevision(revision, releaseURI, indexNewCodingScheme);
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

	private EntryState getEntryState(String revisionId) {
		EntryState entryState = new EntryState();
		
		entryState.setChangeType(ChangeType.NEW);
		entryState.setContainingRevision(revisionId);
		entryState.setRelativeOrder(0L);
		return entryState;
	}

	@Override
	public SystemRelease getSystemReleaseMetadataById(String systemReleaseId) {
		SystemReleaseDao sysReleaseDao = this.getDaoManager().getSystemReleaseDao();
		return sysReleaseDao.getSystemReleaseMetadataById(systemReleaseId);
	}

	@Override
	public SystemRelease getSystemReleaseMetadataByUri(String systemReleaseUri) {
		SystemReleaseDao sysReleaseDao = this.getDaoManager().getSystemReleaseDao();
		return sysReleaseDao.getSystemReleaseMetadataByUri(systemReleaseUri);
	}

	@Override
	public String insertSystemReleaseMetadata(SystemRelease systemRelease) {
		SystemReleaseDao sysReleaseDao = this.getDaoManager().getSystemReleaseDao();
		return sysReleaseDao.insertSystemReleaseEntry(systemRelease);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexevs.dao.database.service.version.AuthoringService#removeRevisionRecordbyId(java.lang.String)
	 */
	@Override
	public boolean removeRevisionRecordbyId(String revisionId) throws LBException {
		RevisionDao revisionDao = this.getDaoManager().getRevisionDao();
		return revisionDao.removeRevisionById(revisionId);
	}
}
