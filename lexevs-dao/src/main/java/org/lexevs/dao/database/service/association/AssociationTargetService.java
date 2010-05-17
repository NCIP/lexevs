package org.lexevs.dao.database.service.association;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;

public interface AssociationTargetService {
	public static final String INSERT_ASSOCIATIONTARGET_ERROR = "INSERT-ASSOCIATIONTARGET-ERROR";
	public static final String UPDATE_ASSOCIATIONTARGET_ERROR = "UPDATE-ASSOCIATIONTARGET-ERROR";
	public static final String REMOVE_ASSOCIATIONTARGET_ERROR = "REMOVE-ASSOCIATIONTARGET-ERROR";
	public static final String INSERT_ASSOCIATIONTARGET_VERSIONABLE_CHANGES_ERROR = "INSERT-ASSOCIATIONTARGET-VERSIONABLE-CHANGES-ERROR";
	

	public void insertAssociationTarget(String codingSchemeUri, String version,
			String relationContainerName, String associationPredicateName,
			AssociationSource source, AssociationTarget target);

	public void updateAssociationTarget(String codingSchemeUri,
			String version, AssociationSource source, AssociationTarget target);

	public void removeAssociationTarget(String codingSchemeUri,
			String version, AssociationSource source, AssociationTarget target);

	public void insertAssociationTargetVersionableChanges(
			String codingSchemeUri, String version, AssociationSource source,
			AssociationTarget target);

	public void revise(String codingSchemeUri, String relationContainerName,
			String associationPredicateName, String version,
			AssociationSource source, AssociationTarget target)
			throws LBException;
}
