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
package org.lexevs.dao.database.service.codingscheme;

import java.util.Arrays;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.URIMap;
import org.LexGrid.relations.Relations;
import org.LexGrid.versions.EntryState;
import org.lexevs.dao.database.access.association.AssociationDao;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.access.entity.EntityDao;
import org.lexevs.dao.database.access.property.PropertyDao;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class VersionableEventCodingSchemeService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class VersionableEventCodingSchemeService extends AbstractDatabaseService implements CodingSchemeService {
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.codingscheme.CodingSchemeService#getCodingSchemeByUriAndVersion(java.lang.String, java.lang.String)
	 */
	@Transactional
	public CodingScheme getCodingSchemeByUriAndVersion(String uri,
			String version) {
		return this.getDaoManager().getCodingSchemeDao(uri, version).
			getCodingSchemeByUriAndVersion(uri, version);
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.codingscheme.CodingSchemeService#getCodingSchemeSummaryByUriAndVersion(java.lang.String, java.lang.String)
	 */
	@Transactional
	public CodingSchemeSummary getCodingSchemeSummaryByUriAndVersion(
			String uri, String version) {
		return this.getDaoManager().getCodingSchemeDao(uri, version).
			getCodingSchemeSummaryByUriAndVersion(uri, version);
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.codingscheme.CodingSchemeService#destroyCodingScheme(java.lang.String, java.lang.String)
	 */
	@Transactional
	public void destroyCodingScheme(String uri, String version) {
		CodingSchemeDao csDao = 
			this.getDaoManager().getCodingSchemeDao(uri, version);
		PropertyDao propertyDao =
			this.getDaoManager().getPropertyDao(uri, version);
		String codingSchemeId = csDao.getCodingSchemeIdByUriAndVersion(uri, version);
		
		propertyDao.deleteAllEntityPropertiesOfCodingScheme(codingSchemeId);
		
		csDao.deleteCodingSchemeById(codingSchemeId);	
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.codingscheme.CodingSchemeService#insertCodingScheme(org.LexGrid.codingSchemes.CodingScheme)
	 */
	@Transactional
	public void insertCodingScheme(CodingScheme scheme) throws CodingSchemeAlreadyLoadedException {
		CodingSchemeDao codingSchemeDao = 
			this.getDaoManager().getCurrentCodingSchemeDao();
		
		EntityDao entityDao = 
			this.getDaoManager().getCurrentEntityDao();
		
		AssociationDao associationDao = 
			this.getDaoManager().getCurrentAssociationDao();
		
		String codingSchemeId = codingSchemeDao.insertCodingScheme(scheme);
		
		this.fireCodingSchemeInsertEvent(scheme);
		
		if(scheme.getEntities() != null) {
			entityDao.insertBatchEntities(codingSchemeId,
					Arrays.asList(scheme.getEntities().getEntity()));
		}
		
		if(scheme.getRelations() != null) {
			for(Relations relation : scheme.getRelations()) {
				associationDao.insertRelations(codingSchemeId, relation);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.codingscheme.CodingSchemeService#insertURIMap(java.lang.String, java.lang.String, org.LexGrid.naming.URIMap)
	 */
	@Transactional
	public void insertURIMap(
			String codingSchemeUri, 
			String codingSchemeVersion,
			URIMap uriMap){
		CodingSchemeDao codingSchemeDao = getDaoManager().getCodingSchemeDao(codingSchemeUri, codingSchemeVersion);
		String codingSchemeId = codingSchemeDao.
			getCodingSchemeIdByUriAndVersion(codingSchemeUri, codingSchemeVersion);
		getDaoManager().getCodingSchemeDao(codingSchemeUri, codingSchemeVersion).
			insertURIMap(codingSchemeId, uriMap);
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.codingscheme.CodingSchemeService#updateCodingScheme(java.lang.String, java.lang.String, org.LexGrid.codingSchemes.CodingScheme)
	 */
	@Transactional
	public void updateCodingScheme(
			String codingSchemeUri,
			String codingSchemeVersion,
			CodingScheme codingScheme) {
		CodingSchemeDao codingSchemeDao = getDaoManager().getCodingSchemeDao(codingSchemeUri, codingSchemeVersion);
		
		String codingSchemeId = codingSchemeDao.
			getCodingSchemeIdByUriAndVersion(codingSchemeUri, codingSchemeVersion);
		
		codingSchemeDao.
			updateCodingScheme(codingSchemeId, codingScheme);	
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.codingscheme.CodingSchemeService#updateCodingSchemeEntryState(org.LexGrid.codingSchemes.CodingScheme, org.LexGrid.versions.EntryState)
	 */
	@Transactional
	public void updateCodingSchemeEntryState(CodingScheme codingScheme,
			EntryState entryState) {
		// TODO Auto-generated method stub
		
	}
	

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.codingscheme.CodingSchemeService#validatedSupportedAttribute(java.lang.String, java.lang.String, java.lang.String, java.lang.Class)
	 */
	@Override
	public <T extends URIMap> boolean validatedSupportedAttribute(
			String codingSchemeUri, String codingSchemeVersion, String localId,
			Class<T> attributeClass) {
		CodingSchemeDao codingSchemeDao = getDaoManager().getCodingSchemeDao(codingSchemeUri, codingSchemeVersion);
		
		String codingSchemeId = codingSchemeDao.
		getCodingSchemeIdByUriAndVersion(codingSchemeUri, codingSchemeVersion);
		
		return codingSchemeDao.validateSupportedAttribute(codingSchemeId, localId, attributeClass);
	}
}
