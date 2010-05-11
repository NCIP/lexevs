package org.lexevs.dao.database.access.association;

import org.LexGrid.relations.AssociationData;
import org.LexGrid.relations.AssociationSource;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;

public interface AssociationDataDao extends LexGridSchemaVersionAwareDao {

	public String insertAssociationData(String codingSchemeUId,
			String associationPredicateUId, AssociationSource source,
			AssociationData data);

	public String getAssociationDataUId(String codingSchemeUId,
			String associationInstanceId);

	public String insertHistoryAssociationData(String codingSchemeUId,
			String associationDataUId, Boolean assnQualExist,
			Boolean contextExist);

	public String updateAssociationData(String codingSchemeUId,
			String associationDataUId, AssociationSource source,
			AssociationData data);

	public void deleteAssociationData(String codingSchemeUId,
			String associationDataUId);

	public String updateVersionableChanges(String codingSchemeUId,
			String associationDataUId, AssociationSource source,
			AssociationData data);

	public void deleteAllAssocQualsByAssocDataUId(String codingSchemeUId,
			String associationDataUId);

	public String getLatestRevision(String csUId, String assocDataUId);

}
