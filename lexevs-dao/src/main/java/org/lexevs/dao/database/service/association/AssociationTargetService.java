
package org.lexevs.dao.database.service.association;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;

/**
 * The Interface AssociationTargetService.
 */
public interface AssociationTargetService {
	
	/** The Constant INSERT_ASSOCIATIONTARGET_ERROR. */
	public static final String INSERT_ASSOCIATIONTARGET_ERROR = "INSERT-ASSOCIATIONTARGET-ERROR";
	
	/** The Constant UPDATE_ASSOCIATIONTARGET_ERROR. */
	public static final String UPDATE_ASSOCIATIONTARGET_ERROR = "UPDATE-ASSOCIATIONTARGET-ERROR";
	
	/** The Constant REMOVE_ASSOCIATIONTARGET_ERROR. */
	public static final String REMOVE_ASSOCIATIONTARGET_ERROR = "REMOVE-ASSOCIATIONTARGET-ERROR";
	
	/** The Constant INSERT_ASSOCIATIONTARGET_VERSIONABLE_CHANGES_ERROR. */
	public static final String INSERT_ASSOCIATIONTARGET_VERSIONABLE_CHANGES_ERROR = "INSERT-ASSOCIATIONTARGET-VERSIONABLE-CHANGES-ERROR";
	
	/**
	 * Gets the association target.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param relationContainerName the relation container name
	 * @param associationPredicateName the association predicate name
	 * @param associationInstanceId the association instance id
	 * 
	 * @return the association target
	 */
	public AssociationTarget getAssociationTarget(
			String codingSchemeUri, 
			String version,
			String relationContainerName, 
			String associationPredicateName,
			String associationInstanceId);
	
	/**
	 * Resolve association target by revision.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param relationContainerName the relation container name
	 * @param associationPredicateName the association predicate name
	 * @param associationInstanceId the association instance id
	 * @param revisionId the revision id
	 * 
	 * @return the association target
	 * 
	 * @throws LBRevisionException the LB revision exception
	 */
	public AssociationTarget resolveAssociationTargetByRevision(
			String codingSchemeUri, 
			String version,
			String relationContainerName, 
			String associationPredicateName,
			String associationInstanceId,
			String revisionId) throws LBRevisionException;

	/**
	 * Insert association target.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param relationContainerName the relation container name
	 * @param associationPredicateName the association predicate name
	 * @param sourceEntityCode the source entity code
	 * @param sourceEntityCodeNamespace the source entity code namespace
	 * @param target the target
	 */
	public void insertAssociationTarget(
			String codingSchemeUri, 
			String version,
			String relationContainerName, 
			String associationPredicateName,
			String sourceEntityCode,
			String sourceEntityCodeNamespace,
			AssociationTarget target);

	/**
	 * Update association target.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param source the source
	 */
	public void updateAssociationTarget(
			String codingSchemeUri,
			String version, 
			AssociationTarget source);

	/**
	 * Removes the association target.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param target the target
	 */
	public void removeAssociationTarget(
			String codingSchemeUri,
			String version, 
			AssociationTarget target);

	/**
	 * Revise.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param relationContainerName the relation container name
	 * @param associationPredicateName the association predicate name
	 * @param sourceEntityCode the source entity code
	 * @param sourceEntityCodeNamespace the source entity code namespace
	 * @param target the target
	 * 
	 * @throws LBException the LB exception
	 */
	public void revise(String codingSchemeUri, 
			String version, 
			String relationContainerName,
			String associationPredicateName, 
			String sourceEntityCode,
			String sourceEntityCodeNamespace,
			AssociationTarget target)
			throws LBException;

	/**
	 * Revise.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param relationContainerName the relation container name
	 * @param associationPredicateName the association predicate name
	 * @param source the source
	 * @param target the target
	 * 
	 * @throws LBException the LB exception
	 */
	public void revise(
			String codingSchemeUri, 
			String version,
			String relationContainerName, 
			String associationPredicateName,
			AssociationSource source, 
			AssociationTarget target)
			throws LBException;
}