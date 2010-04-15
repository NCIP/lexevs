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
package org.lexevs.dao.database.service.property;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Versionable;
import org.LexGrid.concepts.Entity;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.lexevs.dao.database.access.property.PropertyDao.PropertyType;
import org.lexevs.dao.database.access.property.batch.PropertyBatchInsertItem;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.lexevs.dao.database.service.event.property.PropertyUpdateEvent;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class VersionableEventPropertyService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class VersionableEventPropertyService extends AbstractDatabaseService implements PropertyService{

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.property.PropertyService#insertBatchEntityProperties(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.List)
	 */
	@Transactional
	public void insertBatchEntityProperties(
			String codingSchemeUri, 
			String version,
			String entityCode,
			String entityCodeNamespace,
			List<Property> items) {
		String codingSchemeId = this.getCodingSchemeId(codingSchemeUri, version);
		String entityId = this.getDaoManager().getEntityDao(codingSchemeUri, version).getEntityUId(codingSchemeId, entityCode, entityCodeNamespace);
		
		this.getDaoManager().getPropertyDao(codingSchemeUri, version).
		insertBatchProperties(
				codingSchemeId, 
				PropertyType.ENTITY, 
				this.propertyListToBatchInsertList(entityId, items));
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.property.PropertyService#insertEntityProperty(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.LexGrid.commonTypes.Property)
	 */
	@Transactional
	public void insertEntityProperty(
			String codingSchemeUri, 
			String version, 
			String entityCode, 
			String entityCodeNamespace, 
			Property property) {
		String codingSchemeId = this.getCodingSchemeId(codingSchemeUri, version);
		String entityId = this.getDaoManager().getEntityDao(codingSchemeUri, version).getEntityUId(codingSchemeId, entityCode, entityCodeNamespace);
		
		this.getDaoManager().getPropertyDao(codingSchemeUri, version).
			insertProperty(codingSchemeId, entityId, PropertyType.ENTITY, property);	
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.property.PropertyService#updateEntityProperty(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.LexGrid.commonTypes.Property)
	 */
	@Transactional
	public void updateEntityProperty(String codingSchemeUri, String version,
			String entityCode, String entityCodeNamespace, String propertyId,
			Property property) {
		String codingSchemeId = this.getCodingSchemeId(codingSchemeUri, version);
		String entityId = this.getDaoManager().getEntityDao(codingSchemeUri, version).getEntityUId(codingSchemeId, entityCode, entityCodeNamespace);
		
		this.getDaoManager().
			getPropertyDao(codingSchemeUri, version).
				updateProperty(codingSchemeId, entityId, propertyId, PropertyType.ENTITY, property);
		
		//TODO
		this.firePropertyUpdateEvent(new PropertyUpdateEvent(
			codingSchemeUri,
			version,
			entityCode,
			entityCodeNamespace,
			null,
			property));
	}

	/**
	 * Property list to batch insert list.
	 * 
	 * @param parentId the parent id
	 * @param props the props
	 * 
	 * @return the list< property batch insert item>
	 */
	protected List<PropertyBatchInsertItem> propertyListToBatchInsertList(
			String parentId, List<Property> props){
		List<PropertyBatchInsertItem> returnList = new ArrayList<PropertyBatchInsertItem>();
		for(Property prop : props){
			returnList.add(new PropertyBatchInsertItem(parentId, prop));
		}
		return returnList;
	}
	
	public void revise(String codingSchemeUri, String version, Property property) throws LBException {
		
		if( property == null) 
			throw new LBParameterException("Property object is not supplied.");
		
		EntryState entryState = property.getEntryState();

		if (entryState == null) {
			throw new LBRevisionException("EntryState can't be null.");
		}

		String revisionId = entryState.getContainingRevision();
		ChangeType changeType = entryState.getChangeType();

		if (revisionId != null && changeType != null) {

			/*if (changeType == ChangeType.NEW) {

				this.insertEntity(codingSchemeUri, version, revisedProperty);
			} else if (changeType == ChangeType.REMOVE) {

				this.removeEntity(codingSchemeUri, version, revisedProperty);
			} else if (changeType == ChangeType.MODIFY) {

				this.updateEntity(codingSchemeUri, version, revisedProperty);
			} else if (changeType == ChangeType.DEPENDENT) {
				
				this.insertDependentChanges(codingSchemeUri, version, revisedProperty);
			} else if (changeType == ChangeType.VERSIONABLE) {

				this.insertVersionableChanges(codingSchemeUri, version, revisedProperty);
			}*/
		}
	}
}
