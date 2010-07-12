package org.lexevs.dao.database.service.association;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.relations.AssociationData;
import org.LexGrid.relations.AssociationSource;

public interface AssociationDataService {
	public static final String INSERT_ASSOCIATIONDATA_ERROR = "INSERT-ASSOCIATIONDATA-ERROR";
	public static final String UPDATE_ASSOCIATIONDATA_ERROR = "UPDATE-ASSOCIATIONDATA-ERROR";
	public static final String REMOVE_ASSOCIATIONDATA_ERROR = "REMOVE-ASSOCIATIONDATA-ERROR";
	public static final String INSERT_ASSOCIATIONDATA_VERSIONABLE_CHANGES_ERROR = "INSERT-ASSOCIATIONDATA-VERSIONABLE-CHANGES-ERROR";

	public void insertAssociationData(
			String codingSchemeUri, 
			String version,
			String relationContainerName, 
			String associationPredicateName,
			String sourceEntityCode,
			String sourceEntityCodeNamespace,
			AssociationData data);

	public void updateAssociationData(
			String codingSchemeUri, 
			String version,
			AssociationData data);

	public void removeAssociationData(
			String codingSchemeUri, 
			String version,
			AssociationData data);

	public void revise(
			String codingSchemeUri, 
			String version, 
			String relationContainerName, 
			String associationPredicateName,
			String sourceEntityCode,
			String sourceEntityCodeNamespace,
			AssociationData data) throws LBException;

	public AssociationData resolveAssociationDataByRevision(
			String codingSchemeUri,
			String version, 
			String relationContainerName,
			String associationPredicateName, 
			String associationInstanceId,
			String revisionId) throws LBRevisionException;

	public void revise(
			String codingSchemeUri, 
			String version,
			String relationContainerName, 
			String associationPredicateName,
			AssociationSource source, 
			AssociationData data) throws LBException;
}
