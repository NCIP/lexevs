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
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.types.PropertyTypes;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.SupportedProperty;
import org.LexGrid.naming.URIMap;
import org.LexGrid.relations.Relations;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.lexevs.dao.database.access.association.AssociationDao;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.access.property.PropertyDao;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.access.versions.VersionsDao.EntryStateType;
import org.lexevs.dao.database.constants.classifier.property.EntryStateTypeClassifier;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.dao.database.service.error.DatabaseErrorIdentifier;
import org.lexevs.dao.database.service.error.ErrorHandlingService;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;
import org.lexevs.dao.database.service.property.PropertyService;
import org.lexevs.dao.database.service.relation.RelationService;
import org.lexevs.dao.database.service.version.VersionableEventAuthoringService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.springframework.batch.classify.Classifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * The Class VersionableEventCodingSchemeService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@ErrorHandlingService
public class VersionableEventCodingSchemeService extends AbstractDatabaseService implements CodingSchemeService {
	
	private PropertyService propertyService = null;
	private EntityService entityService = null;
	private RelationService relationService = null;
	private Classifier<EntryStateType, String> entryStateTypeClassifier = new EntryStateTypeClassifier();
	
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

		String releaseUId = this.getDaoManager().getSystemReleaseDao()
				.getSystemReleaseUIdByUri(releaseURI);

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
	@Transactional  
	@Override
	@DatabaseErrorIdentifier(errorCode=UPDATE_CODINGSCHEME_ERROR)
	public void updateCodingScheme(
			CodingScheme codingScheme) throws LBException {
		
		String codingSchemeUri = codingScheme.getCodingSchemeURI();
		String codingSchemeVersion = codingScheme.getRepresentsVersion();

		CodingSchemeDao codingSchemeDao = getDaoManager().getCodingSchemeDao(
				codingSchemeUri, codingSchemeVersion);

		VersionsDao versionsDao = getDaoManager().getVersionsDao(
				codingSchemeUri, codingSchemeVersion);

		String codingSchemeUId = codingSchemeDao
				.getCodingSchemeUIdByUriAndVersion(codingSchemeUri,
						codingSchemeVersion);

		/* 1. insert current coding scheme data into history.*/	
		String prevEntryStateUId = codingSchemeDao
				.insertHistoryCodingScheme(codingSchemeUId);

		/* 2. update the attributes of the codingScheme. */
		String entryStateUId = codingSchemeDao.updateCodingScheme(
				codingSchemeUId, codingScheme);

		/* 3. register entrystate details for the codingScheme.*/
		versionsDao.insertEntryState(entryStateUId, codingSchemeUId,
				entryStateTypeClassifier.classify(EntryStateType.CODINGSCHEME),
				prevEntryStateUId, codingScheme.getEntryState());

		if ((codingScheme.getProperties() != null && codingScheme
				.getProperties().getPropertyCount() != 0)
				|| (codingScheme.getEntities() != null && codingScheme
						.getEntities().getEntityCount() != 0)
				|| (codingScheme.getRelations() != null && codingScheme
						.getRelations().length != 0)) {
			
			this.insertDependentChanges(codingScheme);
		}
		
		this.fireCodingSchemeUpdateEvent(null, null, codingScheme, codingScheme);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.codingscheme.CodingSchemeService#updateCodingSchemeEntryState(org.LexGrid.codingSchemes.CodingScheme, org.LexGrid.versions.EntryState)
	 */
	@Transactional
	@DatabaseErrorIdentifier(errorCode=UPDATE_CODINGSCHEME_ENTRYSTATE_ERROR)
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
		
		if (validRevision(revisedCodingScheme)) {

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

				this.insertDependentChanges(revisedCodingScheme);
			} else if (changeType == ChangeType.VERSIONABLE) {

				this.insertVersionableChanges(revisedCodingScheme);
			}
		}
	}

	@Override
	@DatabaseErrorIdentifier(errorCode=INSERT_CODINGSCHEME_VERSIONABLE_CHANGES_ERROR)
	public void insertVersionableChanges(CodingScheme codingScheme) throws LBException {
		
		String codingSchemeUri = codingScheme.getCodingSchemeURI();
		String codingSchemeVersion = codingScheme.getRepresentsVersion();
		
		CodingSchemeDao codingSchemeDao = getDaoManager().getCodingSchemeDao(codingSchemeUri, codingSchemeVersion);
		
		VersionsDao versionsDao = getDaoManager().getVersionsDao(codingSchemeUri, codingSchemeVersion);
		
		String codingSchemeUId = codingSchemeDao.
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, codingSchemeVersion);
		
			
		String prevEntryStateUId = codingSchemeDao.insertHistoryCodingScheme(codingSchemeUId);
		
		String entryStateUId = codingSchemeDao.
			updateCodingSchemeVersionableAttrib(codingSchemeUId, codingScheme);	
		
		versionsDao.insertEntryState(entryStateUId, codingSchemeUId,
				entryStateTypeClassifier.classify(EntryStateType.CODINGSCHEME),
				prevEntryStateUId, codingScheme.getEntryState());

		if ((codingScheme.getProperties() != null && codingScheme
				.getProperties().getPropertyCount() != 0)
				|| (codingScheme.getEntities() != null && codingScheme
						.getEntities().getEntityCount() != 0)
				|| (codingScheme.getRelations() != null && codingScheme
						.getRelations().length != 0)) {
			
			this.insertDependentChanges(codingScheme);
		}
	}
	
	@Override
	@DatabaseErrorIdentifier(errorCode=INSERT_CODINGSCHEME_DEPENDENT_CHANGES_ERROR)
	public void insertDependentChanges(CodingScheme codingScheme)
			throws LBException {

		String codingSchemeUri = codingScheme.getCodingSchemeURI();
		String version = codingScheme.getRepresentsVersion();

		CodingSchemeDao codingSchemeDao = getDaoManager().getCodingSchemeDao(codingSchemeUri, version);
		
		VersionsDao versionsDao = getDaoManager().getVersionsDao(codingSchemeUri, version);
		
		String codingSchemeUId = codingSchemeDao.
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);

		String prevEntryStateUId = codingSchemeDao.getEntryStateUId(codingSchemeUId);
		
		String entryStateUId = versionsDao.insertEntryState(codingSchemeUId,
				entryStateTypeClassifier.classify(EntryStateType.CODINGSCHEME),
				prevEntryStateUId, codingScheme.getEntryState());
		
		codingSchemeDao.updateEntryStateUId(codingSchemeUId, entryStateUId);
		
		if (codingScheme.getProperties() != null) {
			Property[] propertyList = codingScheme.getProperties()
					.getProperty();

			for (int i = 0; i < propertyList.length; i++) {
				propertyService.reviseCodingSchemeProperty(codingSchemeUri, version,
						propertyList[i]);
			}
		}

		if (codingScheme.getEntities() != null) {

			Entity[] entityList = codingScheme.getEntities().getEntity();

			for (int i = 0; i < entityList.length; i++) {
				entityService.revise(codingSchemeUri, version, entityList[i]);
			}

			Entity[] assocEntityList = codingScheme.getEntities().getAssociationEntity();

			for (int i = 0; i < assocEntityList.length; i++) {
				entityService.revise(codingSchemeUri, version, assocEntityList[i]);
			}
		}

		if (codingScheme.getRelations() != null) {

			Relations[] relationList = codingScheme.getRelations();

			for (int i = 0; i < relationList.length; i++) {
				relationService.revise(codingSchemeUri, version,
						relationList[i]);
			}
		}
	}

	@Override
	@DatabaseErrorIdentifier(errorCode=REMOVE_CODINGSCHEME_ERROR)
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

	public PropertyService getPropertyService() {
		return propertyService;
	}

	public void setPropertyService(PropertyService propertyService) {
		this.propertyService = propertyService;
	}

	@Override
	public List<SupportedProperty> getSupportedPropertyForPropertyType(
			String codingSchemeUri, String codingSchemeVersion, PropertyTypes propertyType) {
		CodingSchemeDao codingSchemeDao = getDaoManager().getCodingSchemeDao(codingSchemeUri, codingSchemeVersion);
		
		String codingSchemeId = codingSchemeDao.
		getCodingSchemeUIdByUriAndVersion(codingSchemeUri, codingSchemeVersion);
		
		return (List<SupportedProperty>) codingSchemeDao.getPropertyUriMapForPropertyType(codingSchemeId, propertyType);
	}

	/**
	 * @return the relationService
	 */
	public RelationService getRelationService() {
		return relationService;
	}

	/**
	 * @param relationService the relationService to set
	 */
	public void setRelationService(RelationService relationService) {
		this.relationService = relationService;
	}

	private boolean validRevision(CodingScheme codingScheme) throws LBException {
		
		String invalid = "Invalid Revision. ";
		
		if (codingScheme == null)
			throw new LBParameterException(invalid + "CodingScheme is null.");
		
		String csURI = codingScheme.getCodingSchemeURI();
		String version = codingScheme.getRepresentsVersion();
		
		EntryState entryState = codingScheme.getEntryState();
		
		if (entryState == null) {
			throw new LBRevisionException(invalid + "EntryState is null.");
		}
		
		ChangeType changeType = entryState.getChangeType();
		
		if( entryState.getContainingRevision() == null ) {
			throw new LBRevisionException(
					invalid + "Revision identifier is null for the versionable object.");
		}
		
		if (changeType == ChangeType.NEW) {
			if (entryState.getPrevRevision() != null) {
				throw new LBRevisionException(
						invalid + "Changes of type NEW are not allowed to have previous revisions.");
			}
			
		} else {
			
			CodingSchemeDao codingSchemeDao = this.getDaoManager()
					.getCodingSchemeDao(csURI, version);
	
			String csUId = codingSchemeDao.getCodingSchemeUIdByUriAndVersion(
					csURI, version);
			
			if (csUId == null) {
				throw new LBRevisionException(invalid +
						"The codingScheme being revised doesn't exist.");
			} 
			
			String csLatestRevId = codingSchemeDao.getLatestRevision(csUId);
			
			if (entryState.getPrevRevision() == null && !csLatestRevId
					.startsWith(VersionableEventAuthoringService.LEXGRID_GENERATED_REVISION)) {
				throw new LBRevisionException(invalid +
						"All changes of type other than NEW should have previous revisions.");
			} else if (!csLatestRevId.equalsIgnoreCase(entryState
					.getPrevRevision())
					&& !csLatestRevId
							.startsWith(VersionableEventAuthoringService.LEXGRID_GENERATED_REVISION)) {
				throw new LBRevisionException(invalid +
						"Revision source is not in sync with the database revisions. "
								+ "Previous revision id does not match with the latest revision id of the coding scheme. "
								+ "Please update the authoring instance with all the revisions and regenerate the source.");
			}
			
			if( entryState.getPrevRevision() == null && csLatestRevId
					.startsWith(VersionableEventAuthoringService.LEXGRID_GENERATED_REVISION)) {
				entryState.setPrevRevision(csLatestRevId);
			}
		}
		
		return true;
	}
}
