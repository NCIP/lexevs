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

import java.sql.Date;
import java.sql.Timestamp;
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
import org.lexevs.dao.database.access.revision.RevisionDao;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.access.versions.VersionsDao.EntryStateType;
import org.lexevs.dao.database.service.RevisableAbstractDatabaseService;
import org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId;
import org.lexevs.dao.database.service.error.DatabaseErrorIdentifier;
import org.lexevs.dao.database.service.error.ErrorHandlingService;
import org.lexevs.dao.database.service.event.entity.EntityBatchInsertEvent;
import org.lexevs.dao.database.service.event.entity.EntityInsertOrRemoveEvent;
import org.lexevs.dao.database.service.event.entity.EntityUpdateEvent;
import org.lexevs.dao.database.service.property.PropertyService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * The Class VersionableEventEntityService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@ErrorHandlingService
public class VersionableEventEntityService extends RevisableAbstractDatabaseService<Entity,CodingSchemeUriVersionBasedEntryId> implements EntityService {

	private PropertyService propertyService = null;

	@Override
	protected void doInsertDependentChanges(
			CodingSchemeUriVersionBasedEntryId id, Entity revisedEntry)
			throws LBException {
		String codingSchemeUri = id.getCodingSchemeUri();
		String version = id.getCodingSchemeVersion();
		
		Property[] entityProperties = revisedEntry.getAllProperties();
		for (int i = 0; i < entityProperties.length; i++) {
			propertyService.reviseEntityProperty(codingSchemeUri, version,
					revisedEntry.getEntityCode(), revisedEntry
					.getEntityCodeNamespace(), entityProperties[i]);
		}
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
			getEntityDao(codingSchemeUri, version).entryStateExists(codingSchemeUid, entryStateUid);
	}



	@Override
	protected Entity getCurrentEntry(CodingSchemeUriVersionBasedEntryId id,
			String entryUId) {
		String codingSchemeUri = id.getCodingSchemeUri();
		String version = id.getCodingSchemeVersion();
		
		String codingSchemeUid = this.getDaoManager().
			getCodingSchemeDao(codingSchemeUri, version).
				getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
		
		return this.getDaoManager().getEntityDao(codingSchemeUri, version).getEntityByUId(codingSchemeUid, entryUId);
	}



	@Override
	protected String getCurrentEntryStateUid(
			CodingSchemeUriVersionBasedEntryId id, String entryUid) {
		String codingSchemeUri = id.getCodingSchemeUri();
		String version = id.getCodingSchemeVersion();
		
		String codingSchemeUid = this.getDaoManager().
			getCodingSchemeDao(codingSchemeUri, version).
				getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
	
		return this.getDaoManager().
			getEntityDao(codingSchemeUri, version).getEntryStateUId(codingSchemeUid, entryUid);
	}



	@Override
	protected String getEntryUid(
			CodingSchemeUriVersionBasedEntryId id,
			Entity entry) {
		String codingSchemeUri = id.getCodingSchemeUri();
		String version = id.getCodingSchemeVersion();
		
		String codingSchemeUid = this.getDaoManager().
			getCodingSchemeDao(codingSchemeUri, version).
				getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
		
		EntityDao entityDao = this.getDaoManager().
			getEntityDao(codingSchemeUri, version);
	
		String entryUid = 
			entityDao.getEntityUId(codingSchemeUid, entry.getEntityCode(), entry.getEntityCodeNamespace());
	
		return entryUid;
	}

	@Override
	protected void insertIntoHistory(CodingSchemeUriVersionBasedEntryId id,
			Entity currentEntry, String entryUId) {
		String codingSchemeUri = id.getCodingSchemeUri();
		String version = id.getCodingSchemeVersion();
		
		String codingSchemeUid = this.getDaoManager().
			getCodingSchemeDao(codingSchemeUri, version).
				getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
		
		EntityDao entityDao = this.getDaoManager().getEntityDao(codingSchemeUri, version);
		
		entityDao.insertHistoryEntity(codingSchemeUid, entryUId, currentEntry);
	}

	@Override
	protected String updateEntryVersionableAttributes(
			CodingSchemeUriVersionBasedEntryId id, String entryUId,
			Entity revisedEntity) {
		String codingSchemeUri = id.getCodingSchemeUri();
		String version = id.getCodingSchemeVersion();
		
		String codingSchemeUid = this.getDaoManager().
			getCodingSchemeDao(codingSchemeUri, version).
				getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
		
		EntityDao entityDao = this.getDaoManager().getEntityDao(codingSchemeUri, version);
		
		return entityDao.
			updateEntityVersionableAttrib(codingSchemeUid, entryUId, revisedEntity);	
	}
	
	

	@Override
	protected Entity getHistoryEntryByRevisionId(
			CodingSchemeUriVersionBasedEntryId id, 
			String entryUid,
			String revisionId) {
		String uri = id.getCodingSchemeUri();
		String version = id.getCodingSchemeVersion();

		String codingSchemeUid = this.getCodingSchemeUId(uri, version);
		
		EntityDao entityDao = getDaoManager().getEntityDao(uri,
				version);
		
		return entityDao.getHistoryEntityByRevision(codingSchemeUid, entryUid, revisionId);
	}

	@Override
	protected String getLatestRevisionId(CodingSchemeUriVersionBasedEntryId id,
			String entryUId) {
		String uri = id.getCodingSchemeUri();
		String version = id.getCodingSchemeVersion();
		
		String codingSchemeUid = this.getCodingSchemeUId(uri, version);
		
		EntityDao entityDao = getDaoManager().getEntityDao(uri,
				version);
		
		return entityDao.getLatestRevision(codingSchemeUid, entryUId);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.entity.EntityService#insertEntity(java.lang.String, java.lang.String, org.LexGrid.concepts.Entity)
	 */
	@Transactional
	@DatabaseErrorIdentifier(errorCode=INSERT_ENTITY_ERROR)
	public void insertEntity(String codingSchemeUri, String version,
			Entity entity) {
		this.firePreEntityInsertEvent(new EntityInsertOrRemoveEvent(codingSchemeUri, version, entity));	
		String codingSchemeUId = this.getDaoManager().
			getCodingSchemeDao(codingSchemeUri, version).
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
		
		this.getDaoManager().
			getEntityDao(codingSchemeUri, version).
				insertEntity(codingSchemeUId, entity, true);
		this.firePostEntityInsertEvent(new EntityInsertOrRemoveEvent(codingSchemeUri, version, entity));
	}
	
	

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.entity.EntityService#insertBatchEntities(java.lang.String, java.lang.String, java.util.List)
	 */
	@Transactional
	@DatabaseErrorIdentifier(errorCode=INSERT_BATCH_ENTITY_ERROR)
	public void insertBatchEntities(String codingSchemeUri, String version,
			List<? extends Entity> entities) {
		this.firePreBatchEntityInsertEvent(new EntityBatchInsertEvent(codingSchemeUri, version, entities));	

		String codingSchemeUId = this.getDaoManager().
			getCodingSchemeDao(codingSchemeUri, version).
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
		
		this.getDaoManager().getEntityDao(codingSchemeUri, version).
			insertBatchEntities(codingSchemeUId, entities, true);
		
		this.firePostBatchEntityInsertEvent(new EntityBatchInsertEvent(codingSchemeUri, version, entities));	
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.entity.EntityService#updateEntity(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.LexGrid.concepts.Entity)
	 */
	@Transactional(rollbackFor=Exception.class)
	@DatabaseErrorIdentifier(errorCode=UPDATE_ENTITY_ERROR)
	public void updateEntity(
			final String codingSchemeUri, 
			final String version,
			final Entity entity) throws LBException {
		
		final CodingSchemeUriVersionBasedEntryId id = 
			new CodingSchemeUriVersionBasedEntryId(codingSchemeUri, version);
		
		final EntityDao entityDao = getDaoManager().getEntityDao(codingSchemeUri, version);
		final CodingSchemeDao codingSchemeDao = getDaoManager().getCodingSchemeDao(codingSchemeUri, version);
		
		final String codingSchemeUId = codingSchemeDao.
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
	
		final String entityUid = getEntryUid(id, entity);
		
		Entity currentEntity = this.getCurrentEntry(id, entityUid);
		
		this.updateEntry(id, entity, EntryStateType.ENTITY, new UpdateTemplate() {

			@Override
			public String update() {
				
				entityDao.updateEntity(codingSchemeUId, entityUid, entity);
				String codingSchemeUid = codingSchemeDao.
					getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
				
				return entityDao.updateEntity(
						codingSchemeUid, entityUid, entity);
			}
		});

		this.fireEntityUpdateEvent(new EntityUpdateEvent(
				codingSchemeUri,
				version, 
				currentEntity, 
				entity));
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
	@Transactional
	public void removeEntity(String codingSchemeUri, String version,
			Entity revisedEntity) {
		this.firePreEntityRemoveEvent(new EntityInsertOrRemoveEvent(codingSchemeUri, version, revisedEntity));	

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
		
		this.firePostEntityRemoveEvent(new EntityInsertOrRemoveEvent(codingSchemeUri, version, revisedEntity));	
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.entity.EntityService#getEntity(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Transactional
	public Entity getEntity(String codingSchemeUri, String version,
			String entityCode, String entityCodeNamespace) {
		return this.getEntity(codingSchemeUri, version, entityCode, entityCodeNamespace, null, null);
	}
	
	@Override
	@Transactional
	public Entity getEntity(
			String codingSchemeUri, 
			String version,
			String entityCode, 
			String entityCodeNamespace,
			List<String> propertyNames, 
			List<String> propertyTypes) {
		
		String codingSchemeUid = this.getDaoManager().
		getCodingSchemeDao(codingSchemeUri, version).
		getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
	
	return this.getDaoManager().
		getEntityDao(codingSchemeUri, version).
			getEntityByCodeAndNamespace(
					codingSchemeUid, 
					entityCode, 
					entityCodeNamespace, 
					propertyNames, 
					propertyTypes);
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
			String codingSchemeUri, 
			String version, 
			String entityCode,
			String entityCodeNamespace,
			boolean resolve,
			List<String> propertyNames,
			List<String> propertyTypes) {
		String codingSchemeId = this.getDaoManager().
		getCodingSchemeDao(codingSchemeUri, version).
		getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);

		ResolvedConceptReference ref = this.getDaoManager().
			getEntityDao(codingSchemeUri, version).
				getResolvedCodedNodeReferenceByCodeAndNamespace(codingSchemeId, entityCode, entityCodeNamespace);
		
		if(ref != null && resolve) {
			Entity entity = this.getEntity(
					codingSchemeUri, 
					version, 
					entityCode, 
					entityCodeNamespace, 
					propertyNames, 
					propertyTypes);
			
			ref.setEntity(entity);
		}
		
		return ref;
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
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public void revise(String codingSchemeUri, String version, Entity entity) throws LBException {

		if (this.validRevision(new CodingSchemeUriVersionBasedEntryId(codingSchemeUri, version), entity)) {
			ChangeType changeType = entity.getEntryState().getChangeType();
			
			if (changeType == ChangeType.NEW) {

				this.insertEntity(codingSchemeUri, version, entity);
			} else if (changeType == ChangeType.REMOVE) {

				//this.removeEntity(codingSchemeUri, version, entity);
			} else if (changeType == ChangeType.MODIFY) {

				this.updateEntity(codingSchemeUri, version, entity);
			} else if (changeType == ChangeType.DEPENDENT) {
				
				this.insertDependentChanges(new CodingSchemeUriVersionBasedEntryId(codingSchemeUri, version), entity, EntryStateType.ENTITY);
			} else if (changeType == ChangeType.VERSIONABLE) {

				this.insertVersionableChanges(new CodingSchemeUriVersionBasedEntryId(codingSchemeUri, version), entity, EntryStateType.ENTITY);
			}
		}
	}

	public PropertyService getPropertyService() {
		return propertyService;
	}

	public void setPropertyService(PropertyService propertyService) {
		this.propertyService = propertyService;
	}

	@Override 
	@Transactional(rollbackFor=Exception.class)
	public Entity resolveEntityByRevision(String codingSchemeURI,
			String version, String entityCode, String entityCodeNamespace,
			String revisionId) throws LBRevisionException {

		CodingSchemeDao codingSchemeDao = getDaoManager().getCodingSchemeDao(
				codingSchemeURI, version);

		EntityDao entityDao = getDaoManager().getEntityDao(codingSchemeURI,
				version);

		String codingSchemeUId = codingSchemeDao
				.getCodingSchemeUIdByUriAndVersion(codingSchemeURI, version);
		
		String entityUid = entityDao.getEntityUId(codingSchemeUId, entityCode, entityCodeNamespace);

		return super.resolveEntryByRevision(
				new CodingSchemeUriVersionBasedEntryId(codingSchemeURI, version), entityUid, revisionId);
	}
	
	@Override
	protected Entity addDependentAttributesByRevisionId(
			CodingSchemeUriVersionBasedEntryId id, String entryUid, Entity entry) {
		String codingSchemeUri = id.getCodingSchemeUri();
		String version = id.getCodingSchemeVersion();

		List<Property> properties = this.propertyService.resolvePropertiesOfEntityByRevision(
				codingSchemeUri, 
				version, 
				entry.getEntityCode(), 
				entry.getEntityCodeNamespace(), 
				entry.getEntryState().getContainingRevision());
		
		entry.addAnyProperties(properties);
		
		return entry;
	}

	@Override 
	@Transactional(rollbackFor=Exception.class)
	public Entity resolveEntityByDate(String codingSchemeURI,
			String version, String entityCode, String entityCodeNamespace,
			Date date) throws LBRevisionException {
		
		EntityDao entityDao = getDaoManager().getEntityDao(codingSchemeURI,
				version);
		
		String codingSchemeUid = this.getCodingSchemeUId(codingSchemeURI, version);
		
		String entityUid = entityDao.getEntityUId(codingSchemeUid, entityCode, entityCodeNamespace);
		
		RevisionDao revisionDao = getDaoManager().getRevisionDao();

		String revisionId = revisionDao.getRevisionIdForDate(new Timestamp(date.getTime()));
	
		return super.resolveEntryByRevision(
				new CodingSchemeUriVersionBasedEntryId(codingSchemeURI, version), entityUid, revisionId);
	}
}
