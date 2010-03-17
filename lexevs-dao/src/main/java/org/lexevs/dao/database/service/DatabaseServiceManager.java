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

import org.lexevs.dao.database.service.association.AssociationService;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.dao.database.service.picklist.PickListService;
import org.lexevs.dao.database.service.property.PropertyService;
import org.lexevs.dao.database.service.valuedomain.ValueDomainService;

/**
 * The Class DatabaseServiceManager.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DatabaseServiceManager {
	
	/** The coding scheme service. */
	private CodingSchemeService codingSchemeService;
	
	/** The entity service. */
	private EntityService entityService;
	
	/** The property service. */
	private PropertyService propertyService;
	
	/** The association service. */
	private AssociationService associationService;
	
	/** The pick list service. */
	private PickListService pickListService;
	
	/** The value domain service. */
	private ValueDomainService valueDomainService;
	
	private DaoCallbackService daoCallbackService;
	
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

	/**
	 * Gets the pick list service.
	 * 
	 * @return the pick list service
	 */
	public PickListService getPickListService() {
		return pickListService;
	}

	/**
	 * Sets the pick list service.
	 * 
	 * @param pickListService the new pick list service
	 */
	public void setPickListService(PickListService pickListService) {
		this.pickListService = pickListService;
	}

	/**
	 * Gets the value domain service.
	 * 
	 * @return the value domain service
	 */
	public ValueDomainService getValueDomainService() {
		return valueDomainService;
	}

	/**
	 * Sets the value domain service.
	 * 
	 * @param valueDomainService the new value domain service
	 */
	public void setValueDomainService(ValueDomainService valueDomainService) {
		this.valueDomainService = valueDomainService;
	}

	public void setDaoCallbackService(DaoCallbackService daoCallbackService) {
		this.daoCallbackService = daoCallbackService;
	}

	public DaoCallbackService getDaoCallbackService() {
		return daoCallbackService;
	}
	
	
}


