package org.lexevs.dao.database.service;

import org.lexevs.dao.database.service.association.AssociationService;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.dao.database.service.picklist.PickListService;
import org.lexevs.dao.database.service.property.PropertyService;
import org.lexevs.dao.database.service.valuedomain.ValueDomainService;

public class DatabaseServiceManager {
	
	private CodingSchemeService codingSchemeService;
	
	private EntityService entityService;
	
	private PropertyService propertyService;
	
	private AssociationService associationService;
	
	private PickListService pickListService;
	
	private ValueDomainService valueDomainService;

	public void setCodingSchemeService(CodingSchemeService codingSchemeService) {
		this.codingSchemeService = codingSchemeService;
	}

	public CodingSchemeService getCodingSchemeService() {
		return codingSchemeService;
	}

	public void setEntityService(EntityService entityService) {
		this.entityService = entityService;
	}

	public EntityService getEntityService() {
		return entityService;
	}

	public PropertyService getPropertyService() {
		return propertyService;
	}

	public void setPropertyService(PropertyService propertyService) {
		this.propertyService = propertyService;
	}

	public void setAssociationService(AssociationService associationService) {
		this.associationService = associationService;
	}

	public AssociationService getAssociationService() {
		return associationService;
	}

	public PickListService getPickListService() {
		return pickListService;
	}

	public void setPickListService(PickListService pickListService) {
		this.pickListService = pickListService;
	}

	public ValueDomainService getValueDomainService() {
		return valueDomainService;
	}

	public void setValueDomainService(ValueDomainService valueDomainService) {
		this.valueDomainService = valueDomainService;
	}
	
	
}


