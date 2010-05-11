package org.lexevs.dao.database.service.relation;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.relations.Relations;

public interface RelationService {

	/**
	 * Insert relation.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param relation the relation
	 */
	public void insertRelation(String codingSchemeUri, String version, Relations relation);
	
	public void updateRelation(String codingSchemeUri, String version, Relations relation) throws LBException;
	
	public void removeRelation(String codingSchemeUri, String version, Relations relation);
	
	public void insertRelationDependentChanges(String codingSchemeUri, String version, Relations relation) throws LBException;
	
	public void insertRelationVersionableChanges(String codingSchemeUri, String version, Relations relation) throws LBException;

	/**
	 * version API to revise relations.
	 * 
	 * @param codingSchemeUri
	 * @param version
	 * @param relation
	 * @throws LBException
	 */
	public void revise(String codingSchemeUri, String version,
			Relations relation) throws LBException;
}
