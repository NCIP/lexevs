package org.lexevs.dao.database.service.association;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.relations.AssociationData;
import org.LexGrid.relations.AssociationSource;

public interface AssociationDataService {

	public void insertAssociationData(String codingSchemeUri, String version,
			String relationContainerName, String associationPredicateName,
			AssociationSource source, AssociationData data);

	public void updateAssociationData(String codingSchemeUri, String version,
			AssociationSource source, AssociationData data);

	public void removeAssociationData(String codingSchemeUri, String version,
			AssociationSource source, AssociationData data);

	public void insertAssociationDataVersionableChanges(String codingSchemeUri,
			String version, AssociationSource source, AssociationData data);

	public void revise(String codingSchemeUri, String version, 
			String relationContainerName, String associationPredicateName,
			AssociationSource source, AssociationData data) throws LBException;
}
