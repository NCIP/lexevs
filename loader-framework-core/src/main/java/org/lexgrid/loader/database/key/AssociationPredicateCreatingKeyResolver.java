
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