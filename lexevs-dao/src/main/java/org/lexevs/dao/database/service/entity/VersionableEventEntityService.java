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
package org.lexevs.dao.database.service.entity;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.LexGrid.relations.AssociationEntity;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.access.entity.EntityDao;
import org.lexevs.dao.database.access.property.PropertyDao;
import org.lexevs.dao.database.access.property.PropertyDao.PropertyType;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.access.versions.VersionsDao.EntryStateType;
import org.lexevs.dao.database.constants.classifier.property.EntryStateTypeClassifier;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.lexevs.dao.database.service.error.DatabaseErrorIdentifier;
import org.lexevs.dao.database.service.error.ErrorHandlingService;
import org.lexevs.dao.database.service.event.entity.EntityInsertEvent;
import org.lexevs.dao.database.service.event.entity.EntityUpdateEvent;
import org.lexevs.dao.database.service.property.PropertyService;
import org.springframework.batch.classify.Classifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * The Class VersionableEventEntityService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@ErrorHandlingService
public class VersionableEventEntityService extends AbstractDatabaseService implements EntityService {

	private PropertyService propertyService = null;
	private Classifier<EntryStateType, String> entryStateTypeClassifier = new EntryStateTypeClassifier();
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.entity.EntityService#insertEntity(java.lang.String, java.lang.String, org.LexGrid.concepts.Entity)
	 */
	@Transactional
	@DatabaseErrorIdentifier(errorCode=INSERT_ENTITY_ERROR)
	public void insertEntity(String codingSchemeUri, String version,
			Entity entity) {
		this.firePreEntityInsertEvent(new EntityInsertEvent(codingSchemeUri, version, entity));	
		String codingSchemeUId = this.getDaoManager().
			getCodingSchemeDao(codingSchemeUri, version).
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
		
		this.getDaoManager().
			getEntityDao(codingSchemeUri, version).
				insertEntity(codingSchemeUId, entity, true);
		this.firePostEntityInsertEvent(new EntityInsertEvent(codingSchemeUri, version, entity));
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.entity.EntityService#insertBatchEntities(java.lang.String, java.lang.String, java.util.List)
	 */
	@Transactional
	@DatabaseErrorIdentifier(errorCode=INSERT_BATCH_ENTITY_ERROR)
	public void insertBatchEntities(String codingSchemeUri, String version,
			List<? extends Entity> entities) {
		this.firePreEntityInsertEvent(new EntityInsertEvent(codingSchemeUri, version, entities));
		String codingSchemeUId = this.getDaoManager().
			getCodingSchemeDao(codingSchemeUri, version).
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
		
		this.getDaoManager().getEntityDao(codingSchemeUri, version).
			insertBatchEntities(codingSchemeUId, entities, true);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.entity.EntityService#updateEntity(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.LexGrid.concepts.Entity)
	 */
	@Transactional
	@DatabaseErrorIdentifier(errorCode=UPDATE_ENTITY_ERROR)
	public void updateEntity(
			String codingSchemeUri, 
			String version,
			Entity entity) throws LBException {
		
		CodingSchemeDao codingSchemeDao = getDaoManager().getCodingSchemeDao(codingSchemeUri, version);
		
		EntityDao entityDao = getDaoManager().getEntityDao(codingSchemeUri, version);
		
		VersionsDao versionsDao = getDaoManager().getVersionsDao(codingSchemeUri, version);
		
		String codingSchemeUId = codingSchemeDao.
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
		
		String entityUId = entityDao.getEntityUId(codingSchemeUId, entity.getEntityCode(), entity.getEntityCodeNamespace());
		
		/* 1. insert current entity data into history.*/	
		String prevEntryStateUId = entityDao.insertHistoryEntity(codingSchemeUId, entityUId, entity);
		
		/* 2. update the attributes of the entity. */
		String entryStateUId = entityDao.updateEntity(codingSchemeUId, entityUId, entity);
		
		/* 3. update search (lucene) indexes */
		

		/* 4. register entrystate details for the entity.*/
		versionsDao.insertEntryState(entryStateUId, entityUId,
				entryStateTypeClassifier.classify(EntryStateType.ENTITY),
				prevEntryStateUId, entity.getEntryState());
		
		/* 5. apply dependent changes for the entity.*/
		this.insertDependentChanges(codingSchemeUri, version, entity);
	}
	
	@Transactional
	@DatabaseErrorIdentifier(errorCode=UPDATE_ENTITY_ERROR)
	public void updateEntity(
			String codingSchemeUri, 
			String version,
			AssociationEntity entity) {
		Assert.hasText(entity.getEntityCode(), "An Entity Code is required to be populated to Updated an Entity.");
		Assert.hasText(entity.getEntityCodeNamespace(), "An Entity Code Namespace is required to be populated to Updated an Entity.");
		
		String codingSchemeId = this.getDaoManager().
			getCodingSchemeDao(codingSchemeUri, version).
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
		
		Entity originalEntity = this.getEntity(codingSchemeUri, version, entity.getEntityCode(), entity.getEntityCodeNamespace());
		
		this.getDaoManager().getEntityDao(codingSchemeUri, version).updateEntity(codingSchemeId, entity);
		
		this.fireEntityUpdateEvent(new EntityUpdateEvent(codingSchemeUri, version, originalEntity, entity));
	}

	@DatabaseErrorIdentifier(errorCode=REMOVE_ENTITY_ERROR)
	public void removeEntity(String codingSchemeUri, String version,
			Entity revisedEntity) {

		CodingSchemeDao codingSchemeDao = getDaoManager().getCodingSchemeDao(codingSchemeUri, version);
		
		PropertyDao propertyDao = getDaoManager().getPropertyDao(codingSchemeUri, version);
		
		EntityDao entityDao = getDaoManager().getEntityDao(codingSchemeUri, version);
		
		VersionsDao versionsDao = getDaoManager().getVersionsDao(codingSchemeUri, version);
		
		String entityCode = revisedEntity.getEntityCode();
		String entityCodeNamespace = revisedEntity.getEntityCodeNamespace();
		
		String codingSchemeUId = codingSchemeDao.
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
		
		String entityUId = entityDao.getEntityUId(codingSchemeUId, entityCode, entityCodeNamespace);
		
		if (entityDao.entityInUse(codingSchemeUId, entityCode, entityCodeNamespace)) {
			
			this.getLogger().warn("REMOVE FAILED: The entity '" + entityCode 
					+ "' is in use. Hence can't be removed from the coding scheme.");
			return;
		}
		
		/*1. Delete all entry states of entity. */
		versionsDao.deleteAllEntryStateOfEntity(codingSchemeUId, entityUId);
		
		/*1. Remove all entity properties. */
		propertyDao.deleteAllPropertiesOfParent(codingSchemeUId, entityUId, PropertyType.ENTITY);
		
		/*2. Remove the entity. */
		entityDao.removeEntityByUId(codingSchemeUId, entityUId);
		
		/*3. Remove search (lucene) indexes. */
		
	}
	@DatabaseErrorIdentifier(errorCode=INSERT_ENTITY_VERSIONABLE_CHANGES_ERROR)
	public void insertVersionableChanges(String codingSchemeUri,
			String version, Entity revisedEntity) throws LBException {
		
		CodingSchemeDao codingSchemeDao = getDaoManager().getCodingSchemeDao(codingSchemeUri, version);
		
		VersionsDao versionsDao = getDaoManager().getVersionsDao(codingSchemeUri, version);
		
		EntityDao entityDao = getDaoManager().getEntityDao(codingSchemeUri, version);
		
		String codingSchemeUId = codingSchemeDao.
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
		
		String entityUId = entityDao.getEntityUId(codingSchemeUId, revisedEntity.getEntityCode(), revisedEntity.getEntityCodeNamespace());
		
		/* 1. insert current entity data into history.*/	
		String prevEntryStateUId = entityDao.insertHistoryEntity(codingSchemeUId, entityUId, revisedEntity);
		
		/* 2. update the versionable attributes of the entity. */
		String entryStateUId = entityDao.
			updateEntityVersionableAttrib(codingSchemeUId, entityUId, revisedEntity);	
		
		/* 3. register entrystate details for the entity.*/
		versionsDao.insertEntryState(entryStateUId, entityUId,
				entryStateTypeClassifier.classify(EntryStateType.ENTITY),
				prevEntryStateUId, revisedEntity.getEntryState());
		
		/* 4. apply dependent changes for the entity.*/
		this.insertDependentChanges(codingSchemeUri, version, revisedEntity);
	}
	@DatabaseErrorIdentifier(errorCode=INSERT_ENTITY_DEPENDENT_CHANGES_ERROR)
	public void insertDependentChanges(String codingSchemeUri, String version,
			Entity revisedEntity) throws LBException {

		Property[] entityProperties = revisedEntity.getAllProperties();
		
		CodingSchemeDao codingSchemeDao = getDaoManager().getCodingSchemeDao(codingSchemeUri, version);
		
		VersionsDao versionsDao = getDaoManager().getVersionsDao(codingSchemeUri, version);
		
		EntityDao entityDao = getDaoManager().getEntityDao(codingSchemeUri, version);
		
		if (revisedEntity.getEntryState().getChangeType() == ChangeType.DEPENDENT) {
			
			String codingSchemeUId = codingSchemeDao
					.getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);

			String entityUId = entityDao.getEntityUId(codingSchemeUId,
					revisedEntity.getEntityCode(), revisedEntity
							.getEntityCodeNamespace());

			String prevEntryStateUId = entityDao.getEntryStateUId(
					codingSchemeUId, entityUId);

			if (!entityDao.entryStateExists(codingSchemeUId, prevEntryStateUId)) {
				EntryState entryState = new EntryState();

				entryState.setChangeType(ChangeType.NEW);
				entryState.setRelativeOrder(0L);

				versionsDao.insertEntryState(prevEntryStateUId, entityUId,
						entryStateTypeClassifier
								.classify(EntryStateType.ENTITY), null,
						entryState);
			}

			String entryStateUId = versionsDao.insertEntryState(
					codingSchemeUId, entryStateTypeClassifier
							.classify(EntryStateType.ENTITY),
					prevEntryStateUId, revisedEntity.getEntryState());

			entityDao.updateEntryStateUId(codingSchemeUId, entityUId,
					entryStateUId);

		}
		
		for (int i = 0; i < entityProperties.length; i++) {
			propertyService.reviseEntityProperty(codingSchemeUri, version,
					revisedEntity.getEntityCode(), revisedEntity
							.getEntityCodeNamespace(), entityProperties[i]);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.entity.EntityService#getEntity(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Transactional
	public Entity getEntity(String codingSchemeUri, String version,
			String entityCode, String entityCodeNamespace) {
		String codingSchemeId = this.getDaoManager().
			getCodingSchemeDao(codingSchemeUri, version).
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
		
		return this.getDaoManager().
			getEntityDao(codingSchemeUri, version).getEntityByCodeAndNamespace(codingSchemeId, entityCode, entityCodeNamespace);
	}
	
	@Transactional
	public AssociationEntity getAssociationEntity(String codingSchemeUri, String version,
			String entityCode, String entityCodeNamespace) {
		
		String codingSchemeId = this.getDaoManager().
			getCodingSchemeDao(codingSchemeUri, version).
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
		
		return this.getDaoManager().
			getEntityDao(codingSchemeUri, version).getAssociationEntityByCodeAndNamespace(codingSchemeId, entityCode, entityCodeNamespace);
	}

	@Override
	@Transactional
	public ResolvedConceptReference getResolvedCodedNodeReference(
			String codingSchemeUri, String version, String entityCode,
			String entityCodeNamespace) {
		String codingSchemeId = this.getDaoManager().
		getCodingSchemeDao(codingSchemeUri, version).
		getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);

		return this.getDaoManager().
			getEntityDao(codingSchemeUri, version).
				getResolvedCodedNodeReferenceByCodeAndNamespace(codingSchemeId, entityCode, entityCodeNamespace);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.entity.EntityService#getEntities(java.lang.String, java.lang.String, int, int)
	 */
	@Transactional
	public List<? extends Entity> getEntities(String codingSchemeUri, String version,
			int start, int pageSize) {
		String codingSchemeId = this.getDaoManager().
			getCodingSchemeDao(codingSchemeUri, version).
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
		
		List<? extends Entity> entities = this.getDaoManager().getEntityDao(codingSchemeUri, version).
			getAllEntitiesOfCodingScheme(codingSchemeId, start, pageSize);
		
		return entities;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.entity.EntityService#getEntityCount(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional
	public int getEntityCount(String codingSchemeUri, String version) {
		String codingSchemeId = this.getDaoManager()
			.getCurrentCodingSchemeDao().
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
		return this.getDaoManager().getCurrentEntityDao().getEntityCount(codingSchemeId);
	}
	
	@Transactional
	@Override
	public void revise(String codingSchemeUri, String version, Entity entity) throws LBException {

		if (validRevision(codingSchemeUri, version, entity)) {

			ChangeType changeType = entity.getEntryState().getChangeType();
			
			if (changeType == ChangeType.NEW) {

				this.insertEntity(codingSchemeUri, version, entity);
			} else if (changeType == ChangeType.REMOVE) {

				this.removeEntity(codingSchemeUri, version, entity);
			} else if (changeType == ChangeType.MODIFY) {

				this.updateEntity(codingSchemeUri, version, entity);
			} else if (changeType == ChangeType.DEPENDENT) {
				
				this.insertDependentChanges(codingSchemeUri, version, entity);
			} else if (changeType == ChangeType.VERSIONABLE) {

				this.insertVersionableChanges(codingSchemeUri, version, entity);
			}
		}
	}

	public PropertyService getPropertyService() {
		return propertyService;
	}

	public void setPropertyService(PropertyService propertyService) {
		this.propertyService = propertyService;
	}

	private boolean validRevision(String codingSchemeUri, String version, Entity entity) throws LBException {
		
		String csUId = null;
		EntityDao entityDao = null;
		String entityUId = null;
		
		if( entity == null) 
			throw new LBParameterException("Entity object is not supplied.");
		
		EntryState entryState = entity.getEntryState();
		
		if (entryState == null) {
			throw new LBRevisionException("EntryState can't be null.");
		}
		
		try {
			csUId = this.getDaoManager().getCodingSchemeDao(codingSchemeUri,
					version).getCodingSchemeUIdByUriAndVersion(codingSchemeUri,
					version);
		} catch (Exception e) {
			throw new LBRevisionException(
					"The coding scheme to which the entity belongs to doesn't exist.");
		}
		
		try {
			entityDao = this.getDaoManager().getEntityDao(
					codingSchemeUri, version);
	
			entityUId = entityDao.getEntityUId(csUId, entity
					.getEntityCode(), entity.getEntityCodeNamespace());
		} catch (Exception e) {
			//do nothing.
		}
		
		ChangeType changeType = entryState.getChangeType();
	
		if (changeType == ChangeType.NEW) {
			if (entryState.getPrevRevision() != null) {
				throw new LBRevisionException(
						"Changes of type NEW are not allowed to have previous revisions.");
			}
			
			if (entityUId != null) {
				throw new LBRevisionException(
						"The entity being added already exist.");
			}
		} else {
			
			if (entityUId == null) {
				throw new LBRevisionException(
						"The entity being revised doesn't exist.");
			} 
			
			String entityLatestRevisionId = entityDao.getLatestRevision(csUId,
					entityUId);

			String currentRevision = entryState.getContainingRevision();
			String prevRevision = entryState.getPrevRevision();
			
			if (entryState.getPrevRevision() == null
					&& entityLatestRevisionId != null
					&& !entityLatestRevisionId.equals(currentRevision)) {
				throw new LBRevisionException(
						"All changes of type other than NEW should have previous revisions.");
			} else if (entityLatestRevisionId != null 
					&& !entityLatestRevisionId.equals(currentRevision)
					&& !entityLatestRevisionId.equalsIgnoreCase(prevRevision)) {
				throw new LBRevisionException(
						"Revision source is not in sync with the database revisions. "
								+ "Previous revision id does not match with the latest revision id of the entity. "
								+ "Please update the authoring instance with all the revisions and regenerate the source.");
			}
		} 
		
		return true;
	}
}
