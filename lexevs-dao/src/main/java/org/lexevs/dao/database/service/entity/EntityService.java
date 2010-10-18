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
package org.lexevs.dao.database.service.entity;

import java.sql.Date;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.concepts.Entity;
import org.LexGrid.relations.AssociationEntity;

/**
 * The Interface EntityService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface EntityService {
	
	public static final String INSERT_ENTITY_ERROR = "INSERT-ENTITY-ERROR";
	public static final String INSERT_BATCH_ENTITY_ERROR = "INSERT-BATCH-ENTITY-ERROR";
	public static final String UPDATE_ENTITY_ERROR = "UPDATE-ENTITY-ERROR";
	public static final String REMOVE_ENTITY_ERROR = "REMOVE-ENTITY-ERROR";
	public static final String INSERT_ENTITY_VERSIONABLE_CHANGES_ERROR = "INSERT-ENTITY-VERSIONABLE-CHANGES-ERROR";
	public static final String INSERT_ENTITY_DEPENDENT_CHANGES_ERROR = "INSERT-ENTITY-DEPENDENT-CHANGES-ERROR";
	
	/**
	 * Insert entity.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param entity the entity
	 */
	public void insertEntity(
			String codingSchemeUri, 
			String version, 
			Entity entity);
	
	/**
	 * Gets the entity count.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * 
	 * @return the entity count
	 */
	public int getEntityCount(
			String codingSchemeUri, 
			String version);
	
	/**
	 * Gets the entity.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param entityCode the entity code
	 * @param entityCodeNamespace the entity code namespace
	 * 
	 * @return the entity
	 */
	public Entity getEntity(
			String codingSchemeUri, 
			String version, 
			String entityCode,
			String entityCodeNamespace);
	
	public Entity getEntity(
			String codingSchemeUri, 
			String version, 
			String entityCode,
			String entityCodeNamespace,
			List<String> propertyNames,
			List<String> propertyTypes);
	
	public AssociationEntity getAssociationEntity(
			String codingSchemeUri, 
			String version, 
			String entityCode,
			String entityCodeNamespace);
	
	/**
	 * Gets the entities.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param start the start
	 * @param pageSize the page size
	 * 
	 * @return the entities
	 */
	public List<? extends Entity> getEntities(
			String codingSchemeUri, 
			String version, 
			int start,
			int pageSize);
	
	public ResolvedConceptReference getResolvedCodedNodeReference(
			String codingSchemeUri, 
			String version, 
			String entityCode,
			String entityCodeNamespace,
			boolean resolve, 
			List<String> propertyNames,
			List<String> propertyTypes);

	/**
	 * Insert batch entities.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param entities the entities
	 */
	public void insertBatchEntities(
			String codingSchemeUri, 
			String version,
			List<? extends Entity> entities);
	
	/**
	 * Update entity.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param version the version
	 * @param enityCode the enity code
	 * @param entityCodeNamespace the entity code namespace
	 * @param entity the entity
	 * @throws LBException 
	 */
	public void updateEntity(
			String codingSchemeUri, 
			String version, 
			Entity entity) throws LBException;
	
	public void removeEntity(
			String codingSchemeUri, 
			String version,
			Entity entity) throws LBException;
	
	public void revise(String codingSchemeUri, String version,
			Entity revisedEntity) throws LBException;
	
	public Entity resolveEntityByRevision(String codingSchemeURI,
			String version, String entityCode, String entityCodeNamespace, String revisionId) throws LBRevisionException;
	
	public Entity resolveEntityByDate(String codingSchemeURI,
			String version, String entityCode, String entityCodeNamespace,
			Date date) throws LBRevisionException;

	public EntityDescription getEntityDescription(String codingSchemeURI, String version,
			String code, String codeNamespace);
}