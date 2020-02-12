/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.commonTypes.EntityDescription;
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
	 * @param codingSchemeUId the coding scheme id
	 * @param entityCode the entity code
	 * @param entityCodeNamespace the entity code namespace
	 * 
	 * @return the entity by code and namespace
	 */
	public Entity getEntityByCodeAndNamespace(String codingSchemeUId, String entityCode, String entityCodeNamespace);
	
	public Entity getEntityByCodeAndNamespace(
			String codingSchemeUId, 
			String entityCode, 
			String entityCodeNamespace, 
			List<String> propertyNames, 
			List<String> propertyTypes);
	
	public AssociationEntity getAssociationEntityByCodeAndNamespace(String codingSchemeUId, String entityCode, String entityCodeNamespace);
	
	public ResolvedConceptReference getResolvedCodedNodeReferenceByCodeAndNamespace(String codingSchemeUId, String entityCode, String entityCodeNamespace);
	
	public Entity getEntityByUId(
			String codingSchemeUId, 
			String entityUId);
	
	public Entity getEntityByUId(
			String codingSchemeUId, 
			String entityUId, 
			List<String> propertyNames, 
			List<String> propertyTypes);
	
	public List<Entity> getEntities(String codingSchemeId, List<String> entityUids);
	
	public List<Entity> getEntities(
			String codingSchemeId, 
			List<String> propertyNames, 
			List<String> propertyTypes, 
			List<String> entityUids);
	
	public Map<String,Entity> getEntitiesWithUidMap(String codingSchemeId,
			List<String> propertyNames, List<String> propertyTypes,
			List<String> entityUids);
	
	public Entity getHistoryEntityByRevision(String codingSchemeUId, String entityUId, String revisionId);
	
	/**
	 * Gets the entity id.
	 * 
	 * @param codingSchemeUId the coding scheme id
	 * @param entityCode the entity code
	 * @param entityCodeNamespace the entity code namespace
	 * 
	 * @return the entity id
	 */
	public String getEntityUId(String codingSchemeUId, String entityCode, String entityCodeNamespace);
		
	/**
	 * Insert entity.
	 * 
	 * @param codingSchemeUId the coding scheme id
	 * @param entity the entity
	 * 
	 * @return the string
	 */
	public String insertEntity(String codingSchemeUId, Entity entity, boolean cascade);
	
	/**
	 * Insert history entity.
	 * 
	 * @param codingSchemeUId the coding scheme id
	 * @param entity the entity
	 * 
	 * @return the string
	 */
	public String insertHistoryEntity(String codingSchemeUId, String entityUId, Entity entity);
	
	/**
	 * Update entity.
	 * 
	 * @param codingSchemeUId the coding scheme id
	 * @param entity the entity
	 */
	public String updateEntity(String codingSchemeUId, String entityUId, Entity entity);
	
	public void updateEntity(String codingSchemeUId,
			AssociationEntity entity);
	
	/**
	 * Gets the entity count.
	 * 
	 * @param codingSchemeUId the coding scheme id
	 * 
	 * @return the entity count
	 */
	public int getEntityCount(String codingSchemeUId);
	
	/**
	 * Gets the all entities of coding scheme.
	 * 
	 * @param codingSchemeUId the coding scheme id
	 * @param start the start
	 * @param pageSize the page size
	 * 
	 * @return the all entities of coding scheme
	 */
	public List<? extends Entity> getAllEntitiesOfCodingScheme(String codingSchemeUId, int start, int pageSize);

	public String updateEntityVersionableAttrib(String codingSchemeUId,
			String entityUId, Entity entity);

	public void removeEntityByUId(String codingSchemeUId, String entityUId);

	public String getLatestRevision(String csUId, String entityUId);

	public boolean entityInUse(String codingSchemeUId, String entityCode, String entityCodeNamespace);

	public String getEntryStateUId(String codingSchemeUId, String entityUId);

	public void updateEntryStateUId(String codingSchemeUId, String entityUId,
			String entryStateUId);

	public boolean entryStateExists(String codingSchemeUId, String entryStateUId);

	public EntityDescription getEntityDescription(String codingSchemeUid, String entityCode, String entityCodeNamespace);

	public List<String> getDistinctEntityNamespacesFromCode(String codingSchemeUId, String entityCode);

	public String getEntityDescriptionAsString(String codingSchemeUid,
			String entityCode, String entityCodeNamespace);
}