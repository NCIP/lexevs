package org.lexevs.dao.database.operation.transitivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.relations.AssociationEntity;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.commons.collections.map.LRUMap;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.access.association.AssociationDao;
import org.lexevs.dao.database.access.association.model.Triple;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;

public class DefaultTransitivityBuilder {
	
	private DatabaseServiceManager databaseServiceManager;
	
	private LgLoggerIF logger;
/*
	 public void computeTransitivityTable(String codingSchemeUri, String version) throws SQLException {
	        Connection conn = getConnection();
	        try {
	            
	          
	            List<String> transitiveAssociations = this.getTransitiveAssociationPredicateIds(codingSchemeUri, version);


	            PreparedStatement getAllRelations = conn.prepareStatement("Select " + stc_.sourceCSIdOrEntityCodeNS + ", "
	                    + stc_.sourceEntityCodeOrId + ", " + stc_.targetCSIdOrEntityCodeNS + ", "
	                    + stc_.targetEntityCodeOrId + " from "
	                    + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY) + " where "
	                    + stc_.codingSchemeNameOrId + " = ? and " + stc_.containerNameOrContainerDC + " = ? and "
	                    + stc_.entityCodeOrAssociationId + " = ?");

	            PreparedStatement getTargetsOfSource = conn.prepareStatement("SELECT " + stc_.targetCSIdOrEntityCodeNS
	                    + ", " + stc_.targetEntityCodeOrId + " FROM "
	                    + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY) + " WHERE "
	                    + stc_.codingSchemeNameOrId + " = ? and " + stc_.containerNameOrContainerDC + " = ? and "
	                    + stc_.entityCodeOrAssociationId + " = ? and " + stc_.sourceCSIdOrEntityCodeNS + " = ? and "
	                    + stc_.sourceEntityCodeOrId + " = ?");

	            PreparedStatement getSourceCodes = conn.prepareStatement("SELECT Distinct " + stc_.sourceCSIdOrEntityCodeNS
	                    + ", " + stc_.sourceEntityCodeOrId + " FROM "
	                    + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY) + " WHERE "
	                    + stc_.codingSchemeNameOrId + " = ? and " + stc_.containerNameOrContainerDC + " = ? and "
	                    + stc_.entityCodeOrAssociationId + " = ?");
	            try {
	                for (String associationPredicateId : transitiveAssociations) {
	                    // make a hashset the holds the entire current set of
	                    // relations.
	                   
	                                      
	                    String sourceECNS = null;
	                    String sourceEC = null;
	                    String targetECNS = null;
	                    String targetEC = null;
	          
	                    LRUMap insertedCache = new LRUMap(50000);
	                    for(Triple triple : this.getTriples(codingSchemeUri, version, associationPredicateId, start, pageSize)) {
	                        sourceECNS = triple.getSourceEntityNamespace();
	                        sourceEC = triple.getSourceEntityCode();
	                        targetECNS = triple.getTargetEntityNamespace();
	                        targetEC = triple.getTargetEntityCode();
	                        
	                        if (!sourceEC.equals("@") && !targetEC.equals("@@"))
	                        {
	                            StringTriple sourceCode = new StringTriple();                            
	                            sourceCode.a = sourceECNS;
	                            sourceCode.c = sourceEC;
	                            StringTriple targetCode = new StringTriple();                            
	                            targetCode.a = targetECNS;
	                            targetCode.c = targetEC;
	                            
	                            insertIntoTransitiveClosure(
	                            		codingSchemeUri, 
	                            		version, 
	                            		sourceEC, 
	                            		sourceECNS,
	                            		targetEC, 
	                            		targetECNS,
	                            		associationPredicateId, 
	                            		insertedCache);
	                        }
	                    }  

	                    // get the unique source codes for this relationship - and
	                    // get all of the codes.
	                    logger.info("ComputeTransitive - Processing " + associationPredicateId);
	                    getSourceCodes.setString(1, codingScheme);
	                    getSourceCodes.setString(2, transitiveAssociations.get(i).a);
	                    getSourceCodes.setString(3, transitiveAssociations.get(i).b);

	                    results = getSourceCodes.executeQuery();

	                    ArrayList<StringTriple> sourceCodes = new ArrayList<StringTriple>();
	                    sourceECNS = null;
	                    sourceEC = null;
	                    targetECNS = null;
	                    targetEC = null;
	                    while (results.next()) {
	                        sourceECNS = results.getString(stc_.sourceCSIdOrEntityCodeNS);
	                        sourceEC = results.getString(stc_.sourceEntityCodeOrId);
	                        if (!sourceEC.equals("@"))
	                        {
	                            StringTriple temp = new StringTriple();
	                            
	                            temp.a = sourceECNS;
	                            temp.c = sourceEC;
	                            sourceCodes.add(temp);
	                        }
	                    }
	                    results.close();

	                    // Now I have all of the top source codes for this
	                    // relationship. Need to recurse down the
	                    // tree
	                    // adding nodes to the transitive table as necessary.

	                    for (int j = 0; j < sourceCodes.size(); j++) {
	                        getTargetsOfSource.setString(1, codingScheme);
	                        getTargetsOfSource.setString(2, transitiveAssociations.get(i).a);
	                        getTargetsOfSource.setString(3, transitiveAssociations.get(i).b);
	                        getTargetsOfSource.setString(4, sourceCodes.get(j).a);
	                        getTargetsOfSource.setString(5, sourceCodes.get(j).c);

	                        results = getTargetsOfSource.executeQuery();
	                        ArrayList<StringTriple> targetCodes = new ArrayList<StringTriple>();
	                        sourceECNS = null;
	                        sourceEC = null;
	                        targetECNS = null;
	                        targetEC = null;
	                        while (results.next()) {
	                            targetECNS = results.getString(stc_.targetCSIdOrEntityCodeNS);
	                            targetEC = results.getString(stc_.targetEntityCodeOrId);
	                            if (!targetEC.equals("@@"))
	                            {
	                                StringTriple temp = new StringTriple();
	                                temp.a = targetECNS;
	                                temp.c = targetEC;    
	                                targetCodes.add(temp);
	                            }
	                        }
	                        results.close();

	                        processTransitive(codingScheme,  transitiveAssociations.get(i),
	                                 sourceCodes.get(j), targetCodes, getTargetsOfSource, 
	                                insertIntoTransitive, insertedCache);
	                    }

	                }
	            } finally {
	                getAllRelations.close();
	                insertIntoTransitive.close();
	                getTargetsOfSource.close();
	                getSourceCodes.close();
	            }
	        } finally {
	            returnConnection(conn);
	        }
	    }

	    private void processTransitive(String codingScheme, StringTriple association, StringTriple sourceCode,
	            ArrayList<StringTriple> targetCodes, PreparedStatement getTargetsOfSource, 
	            PreparedStatement insertIntoTransitive, LRUMap insertedCache) throws SQLException {
	        // The next target of each of the passed in targetCodes needs to be
	        // added to the transitive table.

	        for (int i = 0; i < targetCodes.size(); i++) {

	            getTargetsOfSource.setString(1, codingScheme);
	            getTargetsOfSource.setString(2, association.a);
	            getTargetsOfSource.setString(3, association.b);
	            getTargetsOfSource.setString(4, targetCodes.get(i).a);
	            getTargetsOfSource.setString(5, targetCodes.get(i).c);

	            ArrayList<StringTriple> targetTargets = new ArrayList<StringTriple>();
	            String targetECNS = null;
	            String targetEC = null;
	            ResultSet results = getTargetsOfSource.executeQuery();
	            while (results.next()) {
	                targetECNS = results.getString(stc_.targetCSIdOrEntityCodeNS);
	                targetEC =  results.getString(stc_.targetEntityCodeOrId);
	                if (!targetEC.equals("@@"))
	                {
	                    StringTriple temp = new StringTriple();
	                    temp.a = targetECNS;
	                    temp.c = targetEC;
	    
	                    targetTargets.add(temp);
	                }
	            }
	            results.close();

	            // need to add an entry for the source code to each of these target
	            // codes (if it doesn't already
	            // exist, and if there isn't an entry in the regular table already

	            for (int j = 0; j < targetTargets.size(); j++) {
	                if (sourceCode.a.equals(targetTargets.get(j).a)
	                        && sourceCode.c.equals(targetTargets.get(j).c)) {
	                    // if they equal each other, there is something wrong with
	                    // the code system. But I don't
	                    // want to fail.. so skip it.

	                    continue;
	                }
	                
	                boolean iInserted = insertIntoTransitiveClosure(codingScheme, insertIntoTransitive, association, sourceCode, targetTargets.get(j), insertedCache);               
	                if (!iInserted) {
	                    // If I didn't insert it into the transitive table, it was
	                    // already there
	                    // or unnecessary. No need to do the recursion below, so
	                    // remove it.
	                    targetTargets.remove(j);
	                    j--;
	                }
	            }

	            // Now, need to recurse.
	            while (targetTargets.size() > 0) {
	                if (sourceCode.a.equals( targetTargets.get(0).a)
	                // && sourceCode.b.equals((
	                // targetTargets.get(0)).b)
	                        && sourceCode.c.equals( targetTargets.get(0).c)) {
	                    // if they equal each other, there is something wrong with
	                    // the code system. But I don't
	                    // want to fail.. so skip it.
	                    targetTargets.remove(0);
	                    continue;
	                }

	                // need to pass in an array list - put the current item in one.
	                ArrayList<StringTriple> temp = new ArrayList<StringTriple>();
	                temp.add(targetTargets.get(0));
	                // remove it, since we will be done with it after this.
	                targetTargets.remove(0);
	                processTransitive(codingScheme, association, sourceCode, temp, getTargetsOfSource, 
	                        insertIntoTransitive, insertedCache);
	            }
	        }
	    }
	    
	    private boolean insertIntoTransitiveClosure(String codingSchemeUri, String codingSchemeVersion,
	            StringTriple association, StringTriple sourceCode, StringTriple targetCode, LRUMap insertedCache) {
	        String key = sourceCode.a + ":" + sourceCode.c + ":" + targetCode.a + ":" + targetCode.c;

	        boolean iInserted = false;

	        if (!insertedCache.containsKey(key)) {
	            // if it is not loaded in the main table, or already loaded
	            // in the transitive table
	            try {
	                
	                insertedCache.put(key, null);
	                iInserted = true;
	            } catch (SQLException e) {
	            	logger.debug(e.getMessage());
	                // assume an exception means that it is a duplicate
	                // error. ignore.
	                // cheaper to do this (in theory) than check ahead of
	                // time - duplicates should
	                // be abnormal, not the rule. And we have a cache now.
	            }
	            
	        }
	        return iInserted;
	    }
	    
	    protected void insertIntoTransitiveClosure(
	    		final String codingSchemeUri, 
	    		final String version, 
	    		final String sourceCode,
	    		final String sourceNamespace,
	    		final String targetCode, 
	    		final String targetNamespace, 
	    		final String associationPredicateId,
	    		Map cache) {
	    	this.databaseServiceManager.getDaoCallbackService().executeInDaoLayer(new DaoCallback<Object>(){

				@Override
				public Object execute(DaoManager daoManager) {
					String codingSchemeId = daoManager.getCodingSchemeDao(codingSchemeUri, version).
						getCodingSchemeIdByUriAndVersion(codingSchemeUri, version);
					
					daoManager.getAssociationDao(codingSchemeUri, version).
						insertIntoTransitiveClosure(
								codingSchemeId, 
								associationPredicateId, 
								sourceCode, 
								sourceNamespace, 
								targetCode, 
								targetNamespace);
					return null;
				}
	    		
	    	});
	    }

	 

	    private class StringTriple {
		    String a;
		    String b;
		    String c;
		}
	    
	    protected List<Triple> getTriples(
	    		final String codingSchemeUri,
	    		final String version,
	    		final String associationPredicateId, 
	    		final int start, 
	    		final int pageSize){
	    	return databaseServiceManager.getDaoCallbackService().executeInDaoLayer(new DaoCallback<List<Triple>>(){

				@Override
				public List<Triple> execute(DaoManager daoManager) {
					String codingSchemeId = daoManager.getCodingSchemeDao(codingSchemeUri, version).
						getCodingSchemeIdByUriAndVersion(codingSchemeUri, version);
					
					return daoManager.getAssociationDao(codingSchemeUri, version).
						getAllTriplesOfCodingScheme(codingSchemeId, associationPredicateId, start, pageSize);
				}
	    		
	    	});
	    }
	    
	    protected List<String> getTransitiveAssociationPredicateIds(
	    		final String codingSchemeUri, 
	    		final String version){
	    	
	    	return databaseServiceManager.getDaoCallbackService().executeInDaoLayer(new DaoCallback<List<String>>(){

				@Override
				public List<String> execute(DaoManager daoManager) {
					List<String> transitivePredicateIds = new ArrayList<String>();
					
					CodingSchemeDao codingSchemeDao = daoManager.getCodingSchemeDao(codingSchemeUri, version);
					AssociationDao associationDao = daoManager.getAssociationDao(codingSchemeUri, version);			
					
					String codingSchemeId = codingSchemeDao.
						getCodingSchemeIdByUriAndVersion(codingSchemeUri, version);
					
					List<String> relationsIds = associationDao
						.getRelationsIdsForCodingSchemeId(codingSchemeId);
				
					for(String relationsId : relationsIds) {
						List<String> associatinPredicateIds = 
							associationDao.getAssociationPredicateIdsForRelationsId(codingSchemeId, relationsId);
						
						for(String associationPredicateId : associatinPredicateIds) {
							AssociationEntity associationEntity = associationDao.
								getAssociationEntityForAssociationPredicateId(
										codingSchemeId, 
										relationsId, 
										associationPredicateId);
							
							if(associationEntity != null && associationEntity.getIsTransitive()) {
								transitivePredicateIds.add(associationPredicateId);
							}
						}
					}
					
					return transitivePredicateIds;
				}
	    	});
	    }
	    */
}
