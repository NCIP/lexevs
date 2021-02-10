
package org.lexevs.dao.database.service.association;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.relations.AssociationData;
import org.LexGrid.relations.AssociationSource;

/**
 * The Interface AssociationDataService.
 */
public interface AssociationDataService {
	
	/** The Constant INSERT_ASSOCIATIONDATA_ERROR. */
	public static final String INSERT_ASSOCIATIONDATA_ERROR = "INSERT-ASSOCIATIONDATA-ERROR";
	
	/** The Constant UPDATE_ASSOCIATIONDATA_ERROR. */
	public static final String UPDATE_ASSOCIATIONDATA_ERROR = "UPDATE-ASSOCIATIONDATA-ERROR";
	
	/** The Constant REMOVE_ASSOCIATIONDATA_ERROR. */
	public static final String REMOVE_ASSOCIATIONDATA_ERROR = "REMOVE-ASSOCIATIONDATA-ERROR";
	
	/** The Constant INSERT_ASSOCIATIONDATA_VERSIONABLE_CHANGES_ERROR. */
	public static final String INSERT_ASSOCIATIONDATA_VERSIONABLE_CHANGES_ERROR = "INSERT-ASSOCIATIONDATA-VERSIONABLE-CHANGES-ERROR";

	/**
	 * Insert association data.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param relationContainerName the relation container name
	 * @param associationPredicateName the association predicate name
	 * @param sourceEntityCode the source entity code
	 * @param sourceEntityCodeNamespace the source entity code namespace
	 * @param data the data
	 */
	public void insertAssociationData(
			String codingSchemeUri, 
			String version,
			String relationContainerName, 
			String associationPredicateName,
			String sourceEntityCode,
			String sourceEntityCodeNamespace,
			AssociationData data);

	/**
	 * Update association data.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param data the data
	 */
	public void updateAssociationData(
			String codingSchemeUri, 
			String version,
			AssociationData data);

	/**
	 * Removes the association data.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param data the data
	 */
	public void removeAssociationData(
			String codingSchemeUri, 
			String version,
			AssociationData data);

	/**
	 * Revise.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param relationContainerName the relation container name
	 * @param associationPredicateName the association predicate name
	 * @param sourceEntityCode the source entity code
	 * @param sourceEntityCodeNamespace the source entity code namespace
	 * @param data the data
	 * 
	 * @throws LBException the LB exception
	 */
	public void revise(
			String codingSchemeUri, 
			String version, 
			String relationContainerName, 
			String associationPredicateName,
			String sourceEntityCode,
			String sourceEntityCodeNamespace,
			AssociationData data) throws LBException;

	/**
	 * Resolve association data by revision.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param relationContainerName the relation container name
	 * @param associationPredicateName the association predicate name
	 * @param associationInstanceId the association instance id
	 * @param revisionId the revision id
	 * 
	 * @return the association data
	 * 
	 * @throws LBRevisionException the LB revision exception
	 */
	public AssociationData resolveAssociationDataByRevision(
			String codingSchemeUri,
			String version, 
			String relationContainerName,
			String associationPredicateName, 
			String associationInstanceId,
			String revisionId) throws LBRevisionException;

	/**
	 * Revise.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param relationContainerName the relation container name
	 * @param associationPredicateName the association predicate name
	 * @param source the source
	 * @param data the data
	 * 
	 * @throws LBException the LB exception
	 */
	public void revise(
			String codingSchemeUri, 
			String version,
			String relationContainerName, 
			String associationPredicateName,
			AssociationSource source, 
			AssociationData data) throws LBException;
}