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
package org.lexevs.dao.database.service;

import org.lexevs.dao.database.service.association.AssociationDataService;
import org.lexevs.dao.database.service.association.AssociationService;
import org.lexevs.dao.database.service.association.AssociationTargetService;
import org.lexevs.dao.database.service.codednodegraph.CodedNodeGraphService;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.dao.database.service.error.ErrorCallbackDatabaseServiceFactory;
import org.lexevs.dao.database.service.error.ErrorCallbackListener;
import org.lexevs.dao.database.service.property.PropertyService;
import org.lexevs.dao.database.service.relation.RelationService;
import org.lexevs.dao.database.service.valuesets.PickListDefinitionService;
import org.lexevs.dao.database.service.valuesets.VSPropertyService;
import org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService;
import org.lexevs.dao.database.service.version.AuthoringService;
//import org.lexevs.dao.database.service.version.AuthoringService;

/**
 * The Class DatabaseServiceManager.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DatabaseServiceManager {
	
	private ErrorCallbackDatabaseServiceFactory errorCallbackDatabaseServiceFactory = 
		new ErrorCallbackDatabaseServiceFactory();
	
	/** The coding scheme service. */
	private CodingSchemeService codingSchemeService;
	
	/** The entity service. */
	private EntityService entityService;
	
	/** The property service. */
	private PropertyService propertyService;
	
	/** The relation service. */
	private RelationService relationService;
	
	/** The association service. */
	private AssociationService associationService;
	
	/** The associationTarget service. */
	private AssociationTargetService associationTargetService;
	
	/** The associationData service. */
	private AssociationDataService associationDataService;
	
	/** The pick list service. */
	private PickListDefinitionService pickListDefinitionService;
	
	/** The value set definition service. */
	private ValueSetDefinitionService valueSetDefinitionService;
	
	/** The vsproperty service. */
	private VSPropertyService vsPropertyService;
	
	/** The authoring service. */
	private AuthoringService authoringService;
	
	private CodedNodeGraphService codedNodeGraphService;
	
	private DaoCallbackService daoCallbackService;
	
	public <T> T wrapServiceForErrorHandling(T service, ErrorCallbackListener errorCallbackListener) {
		return errorCallbackDatabaseServiceFactory.
			getErrorCallbackDatabaseService(service, errorCallbackListener);
	}
	
	/**
	 * Sets the coding scheme service.
	 * 
	 * @param codingSchemeService the new coding scheme service
	 */
	public void setCodingSchemeService(CodingSchemeService codingSchemeService) {
		this.codingSchemeService = codingSchemeService;
	}

	/**
	 * Gets the coding scheme service.
	 * 
	 * @return the coding scheme service
	 */
	public CodingSchemeService getCodingSchemeService() {
		return codingSchemeService;
	}

	/**
	 * Sets the entity service.
	 * 
	 * @param entityService the new entity service
	 */
	public void setEntityService(EntityService entityService) {
		this.entityService = entityService;
	}

	/**
	 * Gets the entity service.
	 * 
	 * @return the entity service
	 */
	public EntityService getEntityService() {
		return entityService;
	}

	/**
	 * Gets the property service.
	 * 
	 * @return the property service
	 */
	public PropertyService getPropertyService() {
		return propertyService;
	}
	
	/**
	 * Sets the property service.
	 * 
	 * @param propertyService the new property service
	 */
	public void setPropertyService(PropertyService propertyService) {
		this.propertyService = propertyService;
	}

	/**
	 * Sets the association service.
	 * 
	 * @param associationService the new association service
	 */
	public void setAssociationService(AssociationService associationService) {
		this.associationService = associationService;
	}
	
	/**
	 * Gets the association service.
	 * 
	 * @return the association service
	 */
	public AssociationService getAssociationService() {
		return associationService;
	}
	
	public void setDaoCallbackService(DaoCallbackService daoCallbackService) {
		this.daoCallbackService = daoCallbackService;
	}

	public DaoCallbackService getDaoCallbackService() {
		return daoCallbackService;
	}
	
	public AuthoringService getAuthoringService() {
		return authoringService;
	}

	public void setAuthoringService(AuthoringService authoringService) {
		this.authoringService = authoringService;
	}

	public void setPickListDefinitionService(PickListDefinitionService pickListDefinitionService) {
		this.pickListDefinitionService = pickListDefinitionService;
	}

	public PickListDefinitionService getPickListDefinitionService() {
		return pickListDefinitionService;
	}

	/**
	 * @return the valueSetDefinitionService
	 */
	public ValueSetDefinitionService getValueSetDefinitionService() {
		return valueSetDefinitionService;
	}

	/**
	 * @param valueSetDefinitionService the valueSetDefinitionService to set
	 */
	public void setValueSetDefinitionService(
			ValueSetDefinitionService valueSetDefinitionService) {
		this.valueSetDefinitionService = valueSetDefinitionService;
	}

	/**
	 * @return the vsPropertyService
	 */
	public VSPropertyService getVsPropertyService() {
		return vsPropertyService;
	}

	/**
	 * @param vsPropertyService the vsPropertyService to set
	 */
	public void setVsPropertyService(VSPropertyService vsPropertyService) {
		this.vsPropertyService = vsPropertyService;
	}

	public void setCodedNodeGraphService(CodedNodeGraphService codedNodeGraphService) {
		this.codedNodeGraphService = codedNodeGraphService;
	}

	public CodedNodeGraphService getCodedNodeGraphService() {
		return codedNodeGraphService;
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

	/**
	 * @return the associationTargetService
	 */
	public AssociationTargetService getAssociationTargetService() {
		return associationTargetService;
	}

	/**
	 * @param associationTargetService the associationTargetService to set
	 */
	public void setAssociationTargetService(
			AssociationTargetService associationTargetService) {
		this.associationTargetService = associationTargetService;
	}

	/**
	 * @return the associationDataService
	 */
	public AssociationDataService getAssociationDataService() {
		return associationDataService;
	}

	/**
	 * @param associationDataService the associationDataService to set
	 */
	public void setAssociationDataService(
			AssociationDataService associationDataService) {
		this.associationDataService = associationDataService;
	}
}


