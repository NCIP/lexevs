
package org.lexevs.dao.database.service.relation;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.relations.Relations;

/**
 * The Interface RelationService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface RelationService {
	
	/** The Constant INSERT_RELATION_ERROR. */
	public static final String INSERT_RELATION_ERROR = "INSERT-RELATION-ERROR";
	
	/** The Constant UPDATE_RELATION_ERROR. */
	public static final String UPDATE_RELATION_ERROR = "UPDATE-RELATION-ERROR";
	
	/** The Constant REMOVE_RELATION_ERROR. */
	public static final String REMOVE_RELATION_ERROR = "REMOVE-RELATION-ERROR";
	
	/** The Constant INSERT_RELATION_DEPENDENT_CHANGES_ERROR. */
	public static final String INSERT_RELATION_DEPENDENT_CHANGES_ERROR = "INSERT-RELATION-DEPENDENT-CHANGES-ERROR";
	
	/** The Constant INSERT_RELATION_VERSIONABLE_CHANGES_ERROR. */
	public static final String INSERT_RELATION_VERSIONABLE_CHANGES_ERROR = "INSERT-RELATION-VERSIONABLE-CHANGES-ERROR";

/**
 * Insert relation.
 * 
 * @param codingSchemeUri the coding scheme uri
 * @param version the version
 * @param relation the relation
 */
public void insertRelation(String codingSchemeUri, String version, Relations relation);
	
	/**
	 * Update relation.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param relation the relation
	 * 
	 * @throws LBException the LB exception
	 */
	public void updateRelation(String codingSchemeUri, String version, Relations relation) throws LBException;
	
	/**
	 * Removes the relation.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param relation the relation
	 */
	public void removeRelation(String codingSchemeUri, String version, Relations relation);
	
	/**
	 * version API to revise relations.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param relation the relation
	 * 
	 * @throws LBException the LB exception
	 */
	public void revise(String codingSchemeUri, String version,
			Relations relation) throws LBException;

	/**
	 * Resolve relations by revision.
	 * 
	 * @param codingSchemeURI the coding scheme uri
	 * @param version the version
	 * @param relationsName the relations name
	 * @param revisionId the revision id
	 * 
	 * @return the relations
	 * 
	 * @throws LBRevisionException the LB revision exception
	 */
	public Relations resolveRelationsByRevision(
			String codingSchemeURI,
			String version, 
			String relationsName, 
			String revisionId)
			throws LBRevisionException;
}