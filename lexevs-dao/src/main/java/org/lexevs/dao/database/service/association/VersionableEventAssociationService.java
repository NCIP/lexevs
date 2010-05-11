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
package org.lexevs.dao.database.service.association;

import java.util.List;

import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationSource;
import org.lexevs.dao.database.access.association.AssociationDao;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.lexevs.dao.database.service.property.PropertyService;
import org.lexevs.dao.database.utility.DaoUtility;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class VersionableEventAssociationService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class VersionableEventAssociationService extends AbstractDatabaseService implements AssociationService{
	
	private PropertyService propertyService = null;
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.association.AssociationService#insertAssociationSource(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.LexGrid.relations.AssociationSource)
	 */
	@Transactional
	public void insertAssociationSource(String codingSchemeUri, 
			String version, 
			String relationContainerName,
			String associationPredicateName,
			AssociationSource source){
		CodingSchemeDao codingSchemeDao = this.getDaoManager().getCodingSchemeDao(codingSchemeUri, version);
		
		String codingSchemeUId = codingSchemeDao.
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
		
		String associationPredicateUId = this.getDaoManager().getAssociationDao(codingSchemeUri, version).
			getAssociationPredicateUIdByContainerName(codingSchemeUId, relationContainerName, associationPredicateName);
		
		this.doInsertAssociationSource(codingSchemeUri, version, codingSchemeUId, associationPredicateUId, 
				DaoUtility.createNonTypedList(source));
	}
	
	/**
	 * Insert association predicate.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param relationsName the relations name
	 * @param predicate the predicate
	 */
	@Transactional
	public void insertAssociationPredicate(
			String codingSchemeUri, String version, String relationsName, AssociationPredicate predicate) {
		String codingSchemeId = this.getDaoManager().getCodingSchemeDao(codingSchemeUri, version).
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
		
		AssociationDao associationDao = this.getDaoManager().getAssociationDao(codingSchemeUri, version);
		String relationId = associationDao.getRelationUId(codingSchemeUri, relationsName);
		
		this.doInsertAssociationPredicate(codingSchemeUri, version, codingSchemeId, relationId, predicate);
	}
	
	/**
	 * Do insert association predicate.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * @param codingSchemeId the coding scheme id
	 * @param relationsId the relations id
	 * @param predicate the predicate
	 */
	protected void doInsertAssociationPredicate(String codingSchemeUri, 
			String codingSchemeVersion, String codingSchemeId, String relationsId, AssociationPredicate predicate) {
		AssociationDao associationDao = this.getDaoManager().getAssociationDao(codingSchemeUri, codingSchemeVersion);
		associationDao.
			insertAssociationPredicate(
					codingSchemeId, 
					relationsId, 
					predicate,
					true);
	}
	
	/**
	 * Do insert association source.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * @param codingSchemeId the coding scheme id
	 * @param predicateId the predicate id
	 * @param sources the sources
	 */
	protected void doInsertAssociationSource(String codingSchemeUri, 
			String codingSchemeVersion, String codingSchemeId, String predicateId, List<AssociationSource> sources) {
		AssociationDao associationDao = this.getDaoManager().getAssociationDao(codingSchemeUri, codingSchemeVersion);
		associationDao.insertBatchAssociationSources(
				codingSchemeId, 
				predicateId, 
				sources);
	}

	/**
	 * @return the propertyService
	 */
	public PropertyService getPropertyService() {
		return propertyService;
	}

	/**
	 * @param propertyService the propertyService to set
	 */
	public void setPropertyService(PropertyService propertyService) {
		this.propertyService = propertyService;
	}
}
