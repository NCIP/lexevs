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
package org.lexevs.dao.database.access.entity;

import java.util.List;

import org.LexGrid.concepts.Entity;
import org.LexGrid.relations.AssociationEntity;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;


/**
 * The Interface EntityDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface EntityDao extends LexGridSchemaVersionAwareDao {
	
	/**
	 * Insert batch entities.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param entities the entities
	 */
	public void insertBatchEntities(
			String codingSchemeId, List<? extends Entity> entities,
			boolean cascade);
	
	/**
	 * Gets the entity by code and namespace.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param entityCode the entity code
	 * @param entityCodeNamespace the entity code namespace
	 * 
	 * @return the entity by code and namespace
	 */
	public Entity getEntityByCodeAndNamespace(String codingSchemeId, String entityCode, String entityCodeNamespace);
	
	public Entity getEntityById(String codingSchemeId, String entityId);
	
	public Entity getHistoryEntityByRevision(String codingSchemeId, String entityId, String revisionId);
	
	/**
	 * Gets the entity id.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param entityCode the entity code
	 * @param entityCodeNamespace the entity code namespace
	 * 
	 * @return the entity id
	 */
	public String getEntityId(String codingSchemeId, String entityCode, String entityCodeNamespace);
	
	/**
	 * Insert entity.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param entity the entity
	 * 
	 * @return the string
	 */
	public String insertEntity(String codingSchemeId, Entity entity, boolean cascade);
	
	/**
	 * Insert history entity.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param entity the entity
	 * 
	 * @return the string
	 */
	public String insertHistoryEntity(String codingSchemeId, String entityId, Entity entity);
	
	/**
	 * Update entity.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param entity the entity
	 */
	public void updateEntity(String codingSchemeId, Entity entity);
	
	public void updateEntity(String codingSchemeId,
			AssociationEntity entity);
	
	/**
	 * Gets the entity count.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * 
	 * @return the entity count
	 */
	public int getEntityCount(String codingSchemeId);
	
	/**
	 * Gets the all entities of coding scheme.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param start the start
	 * @param pageSize the page size
	 * 
	 * @return the all entities of coding scheme
	 */
	public List<? extends Entity> getAllEntitiesOfCodingScheme(String codingSchemeId, int start, int pageSize);

	public String insertEntity(String codingSchemeId, AssociationEntity entity, boolean cascade);

}
