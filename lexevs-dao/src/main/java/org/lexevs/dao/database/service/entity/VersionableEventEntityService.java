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

import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.lexevs.dao.database.service.error.DatabaseErrorIdentifier;
import org.lexevs.dao.database.service.event.entity.EntityUpdateEvent;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * The Class VersionableEventEntityService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class VersionableEventEntityService extends AbstractDatabaseService implements EntityService {

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.entity.EntityService#insertEntity(java.lang.String, java.lang.String, org.LexGrid.concepts.Entity)
	 */
	@Transactional
	@DatabaseErrorIdentifier(errorCode=INSERT_ENTITY_ERROR)
	public void insertEntity(String codingSchemeUri, String version,
			Entity entity) {
		String codingSchemeId = this.getDaoManager().
			getCodingSchemeDao(codingSchemeUri, version).
			getCodingSchemeIdByUriAndVersion(codingSchemeUri, version);
		
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
			getCodingSchemeIdByUriAndVersion(codingSchemeUri, version);
		
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
		Assert.hasText(entity.getEntityCode(), "An Entity Code is required to be populated to Updated an Entity.");
		Assert.hasText(entity.getEntityCodeNamespace(), "An Entity Code Namespace is required to be populated to Updated an Entity.");
		
		String codingSchemeId = this.getDaoManager().
			getCodingSchemeDao(codingSchemeUri, version).
			getCodingSchemeIdByUriAndVersion(codingSchemeUri, version);
		
		Entity originalEntity = this.getEntity(codingSchemeUri, version, entity.getEntityCode(), entity.getEntityCodeNamespace());
		
		this.getDaoManager().getEntityDao(codingSchemeUri, version).updateEntity(codingSchemeId, entity);
		
		this.fireEntityUpdateEvent(new EntityUpdateEvent(codingSchemeUri, version, originalEntity, entity));
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.entity.EntityService#getEntity(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Transactional
	public Entity getEntity(String codingSchemeUri, String version,
			String entityCode, String entityCodeNamespace) {
		String codingSchemeId = this.getDaoManager().
			getCodingSchemeDao(codingSchemeUri, version).
			getCodingSchemeIdByUriAndVersion(codingSchemeUri, version);
		
		return this.getDaoManager().
			getEntityDao(codingSchemeUri, version).getEntityByCodeAndNamespace(codingSchemeId, entityCode, entityCodeNamespace);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.entity.EntityService#getEntities(java.lang.String, java.lang.String, int, int)
	 */
	@Transactional
	public List<? extends Entity> getEntities(String codingSchemeUri, String version,
			int start, int pageSize) {
		String codingSchemeId = this.getDaoManager().
			getCodingSchemeDao(codingSchemeUri, version).
			getCodingSchemeIdByUriAndVersion(codingSchemeUri, version);
		
		List<? extends Entity> entities = this.getDaoManager().getEntityDao(codingSchemeUri, version).
			getAllEntitiesOfCodingScheme(codingSchemeId, start, pageSize);
		
		return entities;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.entity.EntityService#getEntityCount(java.lang.String, java.lang.String)
	 */
	@Override
	public int getEntityCount(String codingSchemeUri, String version) {
		String codingSchemeId = this.getDaoManager()
			.getCurrentCodingSchemeDao().
			getCodingSchemeIdByUriAndVersion(codingSchemeUri, version);
		return this.getDaoManager().getCurrentEntityDao().getEntityCount(codingSchemeId);
	}
}
