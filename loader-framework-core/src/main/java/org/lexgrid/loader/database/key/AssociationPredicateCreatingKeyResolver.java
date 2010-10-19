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
package org.lexgrid.loader.database.key;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.LexGrid.relations.AssociationPredicate;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.access.association.AssociationDao;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;

public class AssociationPredicateCreatingKeyResolver implements AssociationPredicateKeyResolver{

	private DatabaseServiceManager databaseServiceManager;

	private Map<String,String> associationPrediateIdMap = Collections.synchronizedMap(new HashMap<String,String>());

	public synchronized String resolveKey(
			final String codingSchemeUri, 
			final String version, 
			final String relationContainerName,
			final String associationName) {

		if(!associationPrediateIdMap.containsKey(associationName)) {
			databaseServiceManager.getDaoCallbackService().executeInDaoLayer(new DaoCallback<Object>(){

				public Object execute(DaoManager daoManager) {
					CodingSchemeDao codingSchemedao = daoManager.getCodingSchemeDao(codingSchemeUri, version);
					AssociationDao associationDao = daoManager.getAssociationDao(codingSchemeUri, version);

					String codingSchemeUid = 
						codingSchemedao.
						getCodingSchemeUIdByUriAndVersion(
								codingSchemeUri, version);

					String relationUid = associationDao.getRelationUId(codingSchemeUid, relationContainerName);
					
					String associationPredicateUid = associationDao.getAssociationPredicateUIdByContainerUId(
							codingSchemeUid, 
							relationUid, 
							associationName);
					
					if(StringUtils.isBlank(associationPredicateUid)) {
						associationPredicateUid = associationDao.
							insertAssociationPredicate(
								codingSchemeUid, 
								relationUid, 
								buildDefaultAssociationPredicate(associationName),
								false);
					}

					associationPrediateIdMap.put(associationName, associationPredicateUid);

					return null;
				}
			});
		}

		return associationPrediateIdMap.get(associationName);
	}
	
	protected AssociationPredicate buildDefaultAssociationPredicate(String name){
		AssociationPredicate predicate = new AssociationPredicate();
		predicate.setAssociationName(name);
		return predicate;
	}

	public void setDatabaseServiceManager(DatabaseServiceManager databaseServiceManager) {
		this.databaseServiceManager = databaseServiceManager;
	}

	public DatabaseServiceManager getDatabaseServiceManager() {
		return databaseServiceManager;
	}

}