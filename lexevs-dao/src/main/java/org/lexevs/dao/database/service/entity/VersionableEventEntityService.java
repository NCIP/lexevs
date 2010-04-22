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
import org.LexGrid.concepts.Entity;
import org.LexGrid.relations.AssociationEntity;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.apache.commons.lang.ClassUtils;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.access.entity.EntityDao;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.lexevs.dao.database.service.error.DatabaseErrorIdentifier;
import org.lexevs.dao.database.service.event.entity.EntityUpdateEvent;
import org.lexevs.dao.database.service.property.PropertyService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * The Class VersionableEventEntityService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class VersionableEventEntityService extends AbstractDatabaseService implements EntityService {

	private PropertyService propertyService = null;
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.entity.EntityService#insertEntity(java.lang.String, java.lang.String, org.LexGrid.concepts.Entity)
	 */
	@Transactional
	@DatabaseErrorIdentifier(errorCode=INSERT_ENTITY_ERROR)
	public void insertEntity(String codingSchemeUri, String version,
			Entity entity) {
		String codingSchemeId = this.getDaoManager().
			getCodingSchemeDao(codingSchemeUri, version).
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
		
		this.getDaoManager().
			getEntityDao(codingSchemeUri, version).
				insertEntity(codingSchemeId, entity, true);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.entity.EntityService#insertBatchEntities(java.lang.String, java.lang.String, java.util.List)
	 */
	@Transactional
	@DatabaseErrorIdentifier(errorCode=INSERT_BATCH_ENTITY_ERROR)
	public void insertBatchEntities(String codingSchemeUri, String version,
			List<? extends Entity> entities) {
		String codingSchemeId = this.getDaoManager().
			getCodingSchemeDao(codingSchemeUri, version).
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
		
		this.getDaoManager().getEntityDao(codingSchemeUri, version).
			insertBatchEntities(codingSchemeId, entities, true);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.entity.EntityService#updateEntity(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.LexGrid.concepts.Entity)
	 */
	@Transactional
	public void updateEntity(
			String codingSchemeUri, 
			String version,
			Entity entity) {
		
		CodingSchemeDao codingSchemeDao = getDaoManager().getCodingSchemeDao(codingSchemeUri, version);
		
		EntityDao entityDao = getDaoManager().getEntityDao(codingSchemeUri, version);
		
		VersionsDao versionsDao = getDaoManager().getVersionsDao(codingSchemeUri, version);
		
		String codingSchemeUId = codingSchemeDao.
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
		
		String entityUId = entityDao.getEntityUId(codingSchemeUId, entity.getEntityCode(), entity.getEntityCodeNamespace());
		
		/* 1. insert current entity data into history.*/	
		String prevEntryStateUId = entityDao.insertHistoryEntity(codingSchemeUId, entityUId, entity);
		
		/* 2. update the attributes of the entity. */
		entityDao.updateEntity(codingSchemeUId, entity);
		
		/* 3. update search (lucene) indexes */
		

		/* 4. register entrystate details for the entity.*/
		versionsDao.insertEntryState(entityUId, "Entity",
				prevEntryStateUId, entity.getEntryState());
		
		/* 5. apply dependent changes for the entity.*/
		this.insertDependentChanges(codingSchemeUri, version, entity);
	}
	
	@Transactional
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

	public void removeEntity(String codingSchemeUri, String version,
			Entity revisedEntity) {

		CodingSchemeDao codingSchemeDao = getDaoManager().getCodingSchemeDao(codingSchemeUri, version);
		
		VersionsDao versionsDao = getDaoManager().getVersionsDao(codingSchemeUri, version);
		
		EntityDao entityDao = getDaoManager().getEntityDao(codingSchemeUri, version);
		
		String codingSchemeUId = codingSchemeDao.
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
		
		String entityUId = entityDao.getEntityUId(codingSchemeUId, revisedEntity.getEntityCode(), revisedEntity.getEntityCodeNamespace());
		
//		entityDao.
		
	}
	
	public void insertVersionableChanges(String codingSchemeUri,
			String version, Entity revisedEntity) {
		
		CodingSchemeDao codingSchemeDao = getDaoManager().getCodingSchemeDao(codingSchemeUri, version);
		
		VersionsDao versionsDao = getDaoManager().getVersionsDao(codingSchemeUri, version);
		
		EntityDao entityDao = getDaoManager().getEntityDao(codingSchemeUri, version);
		
		String codingSchemeUId = codingSchemeDao.
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
		
		String entityUId = entityDao.getEntityUId(codingSchemeUId, revisedEntity.getEntityCode(), revisedEntity.getEntityCodeNamespace());
		
		/* 1. insert current entity data into history.*/	
		String prevEntryStateUId = entityDao.insertHistoryEntity(codingSchemeUId, entityUId, revisedEntity);
		
		/* 2. update the versionable attributes of the entity. */
		entityDao.
			updateEntityVersionableAttrib(codingSchemeUId, entityUId, revisedEntity);	
		
		/* 3. register entrystate details for the entity.*/
		versionsDao.insertEntryState(entityUId, "Entity",
				prevEntryStateUId, revisedEntity.getEntryState());
		
		/* 4. apply dependent changes for the entity.*/
		this.insertDependentChanges(codingSchemeUri, version, revisedEntity);
	}

	public void insertDependentChanges(String codingSchemeUri, String version,
			Entity revisedEntity) {

//		propertyService.revise(codingSchemeUri, version, revisedEntity);
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
		Entity associationEntity = this.getEntity(codingSchemeUri, version, entityCode, entityCodeNamespace);
		
		if(associationEntity == null) {return null;}
		
		boolean isAssociationEntity = 
			ClassUtils.isAssignable(associationEntity.getClass(), AssociationEntity.class);
		
		if(isAssociationEntity) {
			return (AssociationEntity)associationEntity;
		} else {
			throw new IllegalArgumentException("Code: " + entityCode + " Namespace: " + entityCodeNamespace + " is not an AssociationEntity.");
		}
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
		
		if( entity == null) 
			throw new LBParameterException("Entity object is not supplied.");
		
		EntryState entryState = entity.getEntryState();

		if (entryState == null) {
			throw new LBRevisionException("EntryState can't be null.");
		}

		String revisionId = entryState.getContainingRevision();
		ChangeType changeType = entryState.getChangeType();

		if (revisionId != null && changeType != null) {

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
}
