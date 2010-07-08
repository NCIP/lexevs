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

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.types.PropertyTypes;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.SupportedProperty;
import org.LexGrid.naming.URIMap;
import org.LexGrid.relations.Relations;
import org.LexGrid.versions.types.ChangeType;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.access.association.AssociationDao;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.access.property.PropertyDao;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.access.versions.VersionsDao.EntryStateType;
import org.lexevs.dao.database.service.RevisableAbstractDatabaseService;
import org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.dao.database.service.error.DatabaseErrorIdentifier;
import org.lexevs.dao.database.service.error.ErrorHandlingService;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;
import org.lexevs.dao.database.service.property.PropertyService;
import org.lexevs.dao.database.service.relation.RelationService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * The Class VersionableEventCodingSchemeService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@ErrorHandlingService
public class VersionableEventCodingSchemeService extends RevisableAbstractDatabaseService<CodingScheme,CodingSchemeUriVersionBasedEntryId> implements CodingSchemeService {
	
	private PropertyService propertyService = null;
	private EntityService entityService = null;
	private RelationService relationService = null;

	@Override
	protected String getCurrentEntryStateUid(
			CodingSchemeUriVersionBasedEntryId id,
			String entryUid) {
		String codingSchemeUri = id.getCodingSchemeUri();
		String version = id.getCodingSchemeVersion();
		return this.getDaoManager().
			getCodingSchemeDao(codingSchemeUri, version).getEntryStateUId(entryUid);
	}

	@Override
	protected CodingScheme addDependentAttributesByRevisionId(
			CodingSchemeUriVersionBasedEntryId id, String entryUid,
			CodingScheme entry) {
		String uri = id.getCodingSchemeUri();
		String version = id.getCodingSchemeVersion();
	
		List<Property> properties = 
			propertyService.resolvePropertiesOfCodingSchemeByRevision(uri, version, entry.getEntryState().getContainingRevision());
		
		entry.setProperties(new Properties());
		entry.getProperties().setProperty(properties);
		
		return entry;	
	}

	@Override
	protected CodingScheme getHistoryEntryByRevisionId(
			CodingSchemeUriVersionBasedEntryId id, String entryUid,
			String revisionId) {
		CodingSchemeDao codingSchemeDao = this.getCodingSchemeDao(id);
		
		String codingSchemeUid = this.getCodingSchemeUId(id.getCodingSchemeUri(), id.getCodingSchemeVersion());
		
		return codingSchemeDao.getHistoryCodingSchemeByRevision(codingSchemeUid, revisionId);
	}

	@Override
	protected String getLatestRevisionId(CodingSchemeUriVersionBasedEntryId id,
			String entryUId) {
		CodingSchemeDao codingSchemeDao = this.getCodingSchemeDao(id);
		
		String codingSchemeUid = this.getCodingSchemeUId(id.getCodingSchemeUri(), id.getCodingSchemeVersion());
		
		return codingSchemeDao.getLatestRevision(codingSchemeUid);
	}

	@Override
	protected boolean entryStateExists(CodingSchemeUriVersionBasedEntryId id,
			String entryStateUid) {
		String codingSchemeUri = id.getCodingSchemeUri();
		String version = id.getCodingSchemeVersion();
		
		String codingSchemeUid = this.getDaoManager().
			getCodingSchemeDao(codingSchemeUri, version).
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
		
		return this.getDaoManager().
			getCodingSchemeDao(codingSchemeUri, version).getEntryStateUId(codingSchemeUid).equals(entryStateUid);
	}
	
	@Override
	public CodingScheme resolveCodingSchemeByRevision(String codingSchemeURI,
			String version, String revisionId) throws LBRevisionException {
		String codingSchemeUid = this.getDaoManager().
			getCodingSchemeDao(codingSchemeURI, version).
				getCodingSchemeUIdByUriAndVersion(codingSchemeURI, version);
		
		return super.resolveEntryByRevision(
				new CodingSchemeUriVersionBasedEntryId(codingSchemeURI, version), codingSchemeUid, revisionId);
	}

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
	@DatabaseErrorIdentifier(errorCode=REMOVE_CODINGSCHEME_ERROR)
	public void removeCodingScheme(String uri, String version) {
		
		CodingSchemeDao csDao = this.getDaoManager().getCodingSchemeDao(uri,
				version);
		
		PropertyDao propertyDao = this.getDaoManager().getPropertyDao(uri,
				version);

		VersionsDao versionsDao = this.getDaoManager().getVersionsDao(uri,
				version);

		AssociationDao assocDao = this.getDaoManager().getAssociationDao(uri,
				version);
		
		String codingSchemeUId = csDao.getCodingSchemeUIdByUriAndVersion(uri, version);
		
		/*1. Delete all entry states of coding scheme. */
		versionsDao.deleteAllEntryStateOfCodingScheme(codingSchemeUId);
		
		/*2. Delete all coding scheme properties. */
		propertyDao.deleteAllCodingSchemePropertiesOfCodingScheme(codingSchemeUId);
		
		/*3. Delete all entity properties. */
		propertyDao.deleteAllEntityPropertiesOfCodingScheme(codingSchemeUId);
		
		/*4. Delete all relation properties. */
		propertyDao.deleteAllRelationPropertiesOfCodingScheme(codingSchemeUId);
		
		/*5. Delete all association qualifications. */
		assocDao.deleteAssociationQualificationsByCodingSchemeUId(codingSchemeUId);
		
		/*6. Delete coding scheme entry. */
		csDao.deleteCodingSchemeByUId(codingSchemeUId);
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

		String releaseUId;
		if(StringUtils.isNotBlank(releaseURI)){
			releaseUId = this.getDaoManager().getSystemReleaseDao()
				.getSystemReleaseUIdByUri(releaseURI);
		} else {
			releaseUId = null;
		}

		codingSchemeDao.insertCodingScheme(scheme, releaseUId, true);

		this.firePostCodingSchemeInsertEvent(scheme);
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.codingscheme.CodingSchemeService#insertURIMap(java.lang.String, java.lang.String, org.LexGrid.naming.URIMap)
	 */
	@Transactional
	@DatabaseErrorIdentifier(errorCode=INSERT_CODINGSCHEME_URI_ERROR)
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
	@DatabaseErrorIdentifier(errorCode=UPDATE_CODINGSCHEME_URI_ERROR)
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
	@Override
	@Transactional(rollbackFor=Exception.class)  
	@DatabaseErrorIdentifier(errorCode=UPDATE_CODINGSCHEME_ERROR)
	public void updateCodingScheme(
			final CodingScheme codingScheme) throws LBException {
		final String uri = codingScheme.getCodingSchemeURI();
		final String version = codingScheme.getRepresentsVersion();
		
		CodingSchemeUriVersionBasedEntryId id = 
			new CodingSchemeUriVersionBasedEntryId(codingScheme.getCodingSchemeURI(), codingScheme.getRepresentsVersion());
		
		this.updateEntry(id, codingScheme, EntryStateType.CODINGSCHEME, new UpdateTemplate() {

			@Override
			public String update() {
				CodingSchemeDao codingSchemeDao = 
					getDaoManager().getCodingSchemeDao(uri, version);
				
				String codingSchemeUid = codingSchemeDao.
					getCodingSchemeUIdByUriAndVersion(uri, version);
				
				return codingSchemeDao.updateCodingScheme(
						codingSchemeUid, codingScheme);
			}
		});
		
		this.fireCodingSchemeUpdateEvent(null, null, codingScheme, codingScheme);
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
	@Transactional(rollbackFor=Exception.class)
	public void revise(CodingScheme revisedCodingScheme, String releaseURI) throws LBException {
		String uri = revisedCodingScheme.getCodingSchemeURI();
		String version = revisedCodingScheme.getRepresentsVersion();
		
		if (this.validRevision(new CodingSchemeUriVersionBasedEntryId(uri, version), revisedCodingScheme)) {

			ChangeType changeType = revisedCodingScheme.getEntryState()
					.getChangeType();

			if (changeType == ChangeType.NEW) {

				this.insertCodingScheme(revisedCodingScheme, releaseURI);
			} else if (changeType == ChangeType.REMOVE) {

				LexEvsServiceLocator.getInstance().getSystemResourceService()
						.removeCodingSchemeResourceFromSystem(
								revisedCodingScheme.getCodingSchemeURI(),
								revisedCodingScheme.getRepresentsVersion());
			} else if (changeType == ChangeType.MODIFY) {

				this.updateCodingScheme(revisedCodingScheme);
			} else if (changeType == ChangeType.DEPENDENT) {

				this.insertDependentChanges(new CodingSchemeUriVersionBasedEntryId(uri, version), revisedCodingScheme, EntryStateType.CODINGSCHEME);
			} else if (changeType == ChangeType.VERSIONABLE) {

				this.insertVersionableChanges(new CodingSchemeUriVersionBasedEntryId(uri, version), revisedCodingScheme, EntryStateType.CODINGSCHEME);
			}
		}
	}


	@Override
	@Transactional
	@DatabaseErrorIdentifier(errorCode=REMOVE_CODINGSCHEME_ERROR)
	public void removeCodingScheme(CodingScheme codingScheme) {

		String codingSchemeUri = codingScheme.getCodingSchemeURI();
		String version = codingScheme.getRepresentsVersion();
		
		removeCodingScheme(codingSchemeUri, version);
	}

	@Override
	public List<SupportedProperty> getSupportedPropertyForPropertyType(
			String codingSchemeUri, String codingSchemeVersion, PropertyTypes propertyType) {
		CodingSchemeDao codingSchemeDao = getDaoManager().getCodingSchemeDao(codingSchemeUri, codingSchemeVersion);
		
		String codingSchemeId = codingSchemeDao.
		getCodingSchemeUIdByUriAndVersion(codingSchemeUri, codingSchemeVersion);
		
		return (List<SupportedProperty>) codingSchemeDao.getPropertyUriMapForPropertyType(codingSchemeId, propertyType);
	}

	

	

	@Override
	protected void doInsertDependentChanges(CodingSchemeUriVersionBasedEntryId id, CodingScheme revisedEntry) throws LBException {
		String codingSchemeUri = id.getCodingSchemeUri();
		String version = id.getCodingSchemeVersion();
		
		/* 2. Revise coding scheme properties.*/
		if (revisedEntry.getProperties() != null) {
			Property[] propertyList = revisedEntry.getProperties()
					.getProperty();

			for (int i = 0; i < propertyList.length; i++) {
				propertyService.reviseCodingSchemeProperty(codingSchemeUri, version,
						propertyList[i]);
			}
		}

		/* 3. Revise entities and association entities of coding scheme.*/
		if (revisedEntry.getEntities() != null) {

			Entity[] entityList = revisedEntry.getEntities().getEntity();

			for (int i = 0; i < entityList.length; i++) {
				entityService.revise(codingSchemeUri, version, entityList[i]);
			}

			Entity[] assocEntityList = revisedEntry.getEntities().getAssociationEntity();

			for (int i = 0; i < assocEntityList.length; i++) {
				//entityService.revise(codingSchemeUri, version, assocEntityList[i]);
			}
		}

		/* 4. Revise relations and triples of coding scheme.*/
		if (revisedEntry.getRelations() != null) {

			Relations[] relationList = revisedEntry.getRelations();

			for (int i = 0; i < relationList.length; i++) {
				relationService.revise(codingSchemeUri, version,
						relationList[i]);
			}
		}
	}

	@Override
	protected CodingScheme getCurrentEntry(CodingSchemeUriVersionBasedEntryId id, String entryUId) {
		String codingSchemeUri = id.getCodingSchemeUri();
		String version = id.getCodingSchemeVersion();
		
		return this.getCodingSchemeDao(codingSchemeUri, version).getCodingSchemeByUId(entryUId);
	}

	@Override
	protected String getEntryUid(CodingSchemeUriVersionBasedEntryId id,
			CodingScheme entry) {
		String codingSchemeUri = id.getCodingSchemeUri();
		String version = id.getCodingSchemeVersion();
		
		return this.getCodingSchemeDao(codingSchemeUri, version).
			getCodingSchemeUIdByUriAndVersion(
					entry.getCodingSchemeURI(), 
					entry.getRepresentsVersion());
	}

	@Override
	protected void insertIntoHistory(CodingSchemeUriVersionBasedEntryId id,
			CodingScheme currentEntry, String entryUId) {
		String codingSchemeUri = id.getCodingSchemeUri();
		String version = id.getCodingSchemeVersion();
		
		this.getCodingSchemeDao(codingSchemeUri, version).insertHistoryCodingScheme(entryUId);
	}

	@Override
	protected String updateEntityVersionableAttributes(CodingSchemeUriVersionBasedEntryId id, String entryUId, CodingScheme revisedEntity) {
		String codingSchemeUri = id.getCodingSchemeUri();
		String version = id.getCodingSchemeVersion();
		
		CodingSchemeDao codingSchemeDao = this.getCodingSchemeDao(codingSchemeUri, version);
		
		String codingSchemeUId = codingSchemeDao
			.getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);

		return this.getCodingSchemeDao(codingSchemeUri, version).
			updateCodingSchemeVersionableAttrib(codingSchemeUId, revisedEntity);	
	}
	
	private CodingSchemeDao getCodingSchemeDao(CodingSchemeUriVersionBasedEntryId id) {
		return this.getCodingSchemeDao(id.getCodingSchemeUri(), id.getCodingSchemeVersion());
	}
	
	private CodingSchemeDao getCodingSchemeDao(String uri, String version) {
		return this.getDaoManager().getCodingSchemeDao(uri, version);
	}

	public EntityService getEntityService() {
		return entityService;
	}

	public void setEntityService(EntityService entityService) {
		this.entityService = entityService;
	}

	public PropertyService getPropertyService() {
		return propertyService;
	}

	public void setPropertyService(PropertyService propertyService) {
		this.propertyService = propertyService;
	}

	public RelationService getRelationService() {
		return relationService;
	}

	public void setRelationService(RelationService relationService) {
		this.relationService = relationService;
	}
}
