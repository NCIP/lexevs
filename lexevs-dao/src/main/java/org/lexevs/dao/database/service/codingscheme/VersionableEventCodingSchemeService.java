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

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.URIMap;
import org.LexGrid.relations.Relations;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.lexevs.dao.database.access.association.AssociationDao;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.access.property.PropertyDao;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.lexevs.dao.database.service.association.AssociationService;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.dao.database.service.error.DatabaseErrorIdentifier;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;
import org.lexevs.dao.database.service.property.PropertyService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * The Class VersionableEventCodingSchemeService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class VersionableEventCodingSchemeService extends AbstractDatabaseService implements CodingSchemeService {
	
	private PropertyService propertyService = null;
	private EntityService entityService = null;
	private AssociationService associationService = null;
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.codingscheme.CodingSchemeService#getCodingSchemeByUriAndVersion(java.lang.String, java.lang.String)
	 */
	@Transactional
	public CodingScheme getCodingSchemeByUriAndVersion(String uri,
			String version) {
		return this.getDaoManager().getCodingSchemeDao(uri, version).
			getCodingSchemeByUriAndVersion(uri, version);
	}
	
	@Transactional
	public CodingScheme getCompleteCodingScheme(String codingSchemeUri,
			String codingSchemeVersion) {
		CodingScheme codingScheme = this.getDaoManager().getCodingSchemeDao(codingSchemeUri, codingSchemeVersion).
			getCodingSchemeByUriAndVersion(codingSchemeUri, codingSchemeVersion);
		
		String codingSchemeId = this.getDaoManager().getCodingSchemeDao(codingSchemeUri, codingSchemeVersion).
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, codingSchemeVersion);
		
		codingScheme.setEntities(new Entities());
		
		for(Entity entity : 
			this.getDaoManager().getEntityDao(codingSchemeUri, codingSchemeVersion).
			getAllEntitiesOfCodingScheme(codingSchemeId, 0, -1)){
			codingScheme.getEntities().addEntity(entity);
		}
		
		//TODO add Relations
		
		return codingScheme;		
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
	public void removeCodingScheme(String uri, String version) {
		CodingSchemeDao csDao = 
			this.getDaoManager().getCodingSchemeDao(uri, version);
		PropertyDao propertyDao =
			this.getDaoManager().getPropertyDao(uri, version);
		
		AssociationDao assocDao = 
			this.getDaoManager().getAssociationDao(uri, version);
		
		String codingSchemeUId = csDao.getCodingSchemeUIdByUriAndVersion(uri, version);
		
		propertyDao.deleteAllEntityPropertiesOfCodingScheme(codingSchemeUId);
		assocDao.deleteAssociationQualificationsByCodingSchemeId(codingSchemeUId);
		
		csDao.removeCodingSchemeByUId(codingSchemeUId);	
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.codingscheme.CodingSchemeService#insertCodingScheme(org.LexGrid.codingSchemes.CodingScheme)
	 */
	@Transactional
	@DatabaseErrorIdentifier(errorCode=INSERT_CODINGSCHEME_ERROR)
	public void insertCodingScheme(CodingScheme scheme, String releaseURI)
			throws CodingSchemeAlreadyLoadedException {
		this.firePreCodingSchemeInsertEvent(scheme);

		CodingSchemeDao codingSchemeDao = this.getDaoManager()
				.getCurrentCodingSchemeDao();

		String releaseUId = this.getDaoManager().getSystemReleaseDao()
				.getSystemReleaseUIdByUri(releaseURI);

		codingSchemeDao.insertCodingScheme(scheme, releaseUId, true);

		this.firePostCodingSchemeInsertEvent(scheme);
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
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, codingSchemeVersion);
		getDaoManager().getCodingSchemeDao(codingSchemeUri, codingSchemeVersion).
			insertURIMap(codingSchemeId, uriMap);
	}
	
	@Transactional
	public void updateURIMap(
			String codingSchemeUri, 
			String codingSchemeVersion,
			URIMap uriMap){
		Assert.hasText(uriMap.getLocalId());
		
		CodingSchemeDao codingSchemeDao = getDaoManager().getCodingSchemeDao(codingSchemeUri, codingSchemeVersion);
		String codingSchemeId = codingSchemeDao.
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, codingSchemeVersion);
		getDaoManager().getCodingSchemeDao(codingSchemeUri, codingSchemeVersion).
			insertOrUpdateURIMap(codingSchemeId, uriMap);
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.codingscheme.CodingSchemeService#updateCodingScheme(java.lang.String, java.lang.String, org.LexGrid.codingSchemes.CodingScheme)
	 */
	@Transactional
	@Override
	public void updateCodingScheme(
			CodingScheme codingScheme) throws LBException {
		
		String codingSchemeUri = codingScheme.getCodingSchemeURI();
		String codingSchemeVersion = codingScheme.getRepresentsVersion();
		
		CodingSchemeDao codingSchemeDao = getDaoManager().getCodingSchemeDao(codingSchemeUri, codingSchemeVersion);
		
		VersionsDao versionsDao = getDaoManager().getVersionsDao(codingSchemeUri, codingSchemeVersion);
		
		String codingSchemeUId = codingSchemeDao.
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, codingSchemeVersion);
		
//		if( codingSchemeDao.codingSchemeExists(codingSchemeUId) ) {
			
			String prevEntryStateUId = codingSchemeDao.insertHistoryCodingScheme(codingSchemeUId);
			
			codingSchemeDao.
				updateCodingScheme(codingSchemeUId, codingScheme);	
			
			versionsDao.insertEntryState(codingSchemeUId, "CodingScheme",
					prevEntryStateUId, codingScheme.getEntryState());
//		}
		
		this.insertDependentChanges(codingScheme);
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
	@Transactional
	public <T extends URIMap> boolean validatedSupportedAttribute(
			String codingSchemeUri, String codingSchemeVersion, String localId,
			Class<T> attributeClass) {
		CodingSchemeDao codingSchemeDao = getDaoManager().getCodingSchemeDao(codingSchemeUri, codingSchemeVersion);
		
		String codingSchemeId = codingSchemeDao.
		getCodingSchemeUIdByUriAndVersion(codingSchemeUri, codingSchemeVersion);
		
		return codingSchemeDao.validateSupportedAttribute(codingSchemeId, localId, attributeClass);
	}


	@Override
	public void revise(CodingScheme revisedCodingScheme, String releaseURI) throws LBException {
		
		if(  revisedCodingScheme == null) 
			throw new LBParameterException("CodingScheme is null.");
		
		EntryState entryState = revisedCodingScheme.getEntryState();

		if (entryState == null) {
			throw new LBRevisionException("EntryState can't be null.");
		}

		String revisionId = entryState.getContainingRevision();
		ChangeType changeType = entryState.getChangeType();

		if (revisionId != null && changeType != null) {

			if (changeType == ChangeType.NEW) {

				this.insertCodingScheme(revisedCodingScheme, releaseURI);
			} else if (changeType == ChangeType.REMOVE) {

				this.removeCodingScheme(revisedCodingScheme);
			} else if (changeType == ChangeType.MODIFY) {

				this.updateCodingScheme(revisedCodingScheme);
			} else if (changeType == ChangeType.DEPENDENT) {
				
				this.insertDependentChanges(revisedCodingScheme);
			} else if (changeType == ChangeType.VERSIONABLE) {

				this.insertVersionableChanges(revisedCodingScheme);
			}
		}
	}

	@Override
	public void insertVersionableChanges(CodingScheme codingScheme) throws LBException {
		
		String codingSchemeUri = codingScheme.getCodingSchemeURI();
		String codingSchemeVersion = codingScheme.getRepresentsVersion();
		
		CodingSchemeDao codingSchemeDao = getDaoManager().getCodingSchemeDao(codingSchemeUri, codingSchemeVersion);
		
		VersionsDao versionsDao = getDaoManager().getVersionsDao(codingSchemeUri, codingSchemeVersion);
		
		String codingSchemeUId = codingSchemeDao.
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, codingSchemeVersion);
		
//		if( codingSchemeDao.codingSchemeExists(codingSchemeUId) ) {
			
			String prevEntryStateUId = codingSchemeDao.insertHistoryCodingScheme(codingSchemeUId);
			
			codingSchemeDao.
				updateCodingSchemeVersionableAttrib(codingSchemeUId, codingScheme);	
			
			versionsDao.insertEntryState(codingSchemeUId, "CodingScheme",
					prevEntryStateUId, codingScheme.getEntryState());
//		}
		
		this.insertDependentChanges(codingScheme);
	}
	
	@Override
	public void insertDependentChanges(CodingScheme codingScheme)
			throws LBException {

		String codingSchemeUri = codingScheme.getCodingSchemeURI();
		String version = codingScheme.getRepresentsVersion();

//		propertyService.revise(codingSchemeUri, version, codingScheme);
		
		if (codingScheme.getEntities() != null) {

			Entity[] entityList = codingScheme.getEntities().getEntity();

			for (int i = 0; i < entityList.length; i++) {
				entityService.revise(codingSchemeUri, version, entityList[i]);
			}
		}

		if (codingScheme.getRelations() != null) {

			Relations[] relationList = codingScheme.getRelations();

			for (int i = 0; i < relationList.length; i++) {
				associationService.reviseRelation(codingSchemeUri, version,
						relationList[i]);
			}
		}
	}

	@Override
	public void removeCodingScheme(CodingScheme codingScheme) {

		String codingSchemeUri = codingScheme.getCodingSchemeURI();
		String version = codingScheme.getRepresentsVersion();
		
		removeCodingScheme(codingSchemeUri, version);
	}

	public EntityService getEntityService() {
		return entityService;
	}

	public void setEntityService(EntityService entityService) {
		this.entityService = entityService;
	}

	public AssociationService getAssociationService() {
		return associationService;
	}

	public void setAssociationService(AssociationService associationService) {
		this.associationService = associationService;
	}

	public PropertyService getPropertyService() {
		return propertyService;
	}

	public void setPropertyService(PropertyService propertyService) {
		this.propertyService = propertyService;
	}
}
