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
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.codingSchemes.CodingSchemes;
import org.LexGrid.naming.Mappings;
import org.LexGrid.valueSets.PickListDefinition;
import org.LexGrid.valueSets.PickListDefinitions;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.ValueSetDefinitions;
import org.LexGrid.versions.ChangedEntry;
import org.LexGrid.versions.EditHistory;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.SystemRelease;
import org.lexevs.dao.database.access.revision.RevisionDao;
import org.lexevs.dao.database.access.systemRelease.SystemReleaseDao;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;
import org.lexevs.dao.database.service.valuesets.PickListDefinitionService;
import org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService;
import org.lexevs.locator.LexEvsServiceLocator;

/**
 * The Class VersionableEventVersionService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class VersionableEventAuthoringService extends AbstractDatabaseService
		implements AuthoringService {

	private CodingSchemeService codingSchemeService = null;

	public void setCodingSchemeService(CodingSchemeService codingSchemeService) {
		this.codingSchemeService = codingSchemeService;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lexevs.dao.database.service.version.VersionService#insertSystemRelease
	 * (org.LexGrid.versions.SystemRelease)
	 */
	@Override
	public void loadSystemRelease(SystemRelease systemRelease) {
		
		if( systemRelease == null ) {
			return;
		}			
		
		Revision[] revisionList = null;
		
		LexEvsServiceLocator lexEvsServiceLocator = LexEvsServiceLocator.getInstance();
		DatabaseServiceManager databaseServiceManager = lexEvsServiceLocator.getDatabaseServiceManager();
		CodingSchemeService codingSchemeService = databaseServiceManager.getCodingSchemeService();
		ValueSetDefinitionService valueSetDefService = databaseServiceManager.getValueSetDefinitionService();
		PickListDefinitionService pickListDefService = databaseServiceManager.getPickListDefinitionService();

		SystemReleaseDao sysReleaseDao = this.getDaoManager().getSystemReleaseDao();
		sysReleaseDao.insertSystemReleaseEntry(systemRelease);
		
		CodingSchemes codingSchemes = systemRelease.getCodingSchemes();
		if( codingSchemes != null ) {
			CodingScheme[] codingSchemeList = codingSchemes.getCodingScheme();
			
			for (int i = 0; i < codingSchemeList.length; i++) {
				try {
					codingSchemeService.insertCodingScheme(codingSchemeList[i], systemRelease.getReleaseURI());
				} catch (CodingSchemeAlreadyLoadedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		ValueSetDefinitions valueSetDefinitions = systemRelease.getValueSetDefinitions();
		if( valueSetDefinitions != null ) {
				try {
					valueSetDefService.insertValueSetDefinitions(valueSetDefinitions, systemRelease.getReleaseURI());
				} catch (LBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

		PickListDefinitions pickLists = systemRelease.getPickListDefinitions();
		if( pickLists != null ) {
			PickListDefinition[] pickListDefList = pickLists.getPickListDefinition();
			Mappings mappings = pickLists.getMappings();
			for (int i = 0; i < pickListDefList.length; i++) {
				try {
					pickListDefService.insertPickListDefinition(pickListDefList[i], systemRelease.getReleaseURI(), mappings);
				} catch (LBParameterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (LBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		EditHistory editHistory = systemRelease.getEditHistory();
		if( editHistory != null ) {
			revisionList = editHistory.getRevision();

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
	public void loadRevision(Revision revision, String releaseURI) {
		
		if( revision == null )
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
							loadCodingScheme(codingScheme, releaseURI);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					// Process ValueDomain Definition revisions
					try {
						ValueSetDefinition valueSetDefinition = cEntry
								.getChangedValueSetDefinitionEntry();
						if (valueSetDefinition != null) {
							loadValueSetDefinition(valueSetDefinition);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					// Process PickList Definition revisions
					try {
						PickListDefinition pickListDef = cEntry
								.getChangedPickListDefinitionEntry();
						if (pickListDef != null) {

							loadPickListDefinition(pickListDef);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	private void loadCodingScheme(CodingScheme codingScheme, String releaseURI) throws Exception {
		codingSchemeService.revise(codingScheme, releaseURI);
	}

	private void loadValueSetDefinition(ValueSetDefinition valueSetDefinition)
			throws Exception {
		// Method not implemented
	}

	private void loadPickListDefinition(PickListDefinition pickListDef)
			throws Exception {

//		pickListRevisionService_.applyChanges(pickListDef);
	}
}
