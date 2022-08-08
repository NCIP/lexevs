
package org.lexevs.dao.database.access.association;

import org.LexGrid.relations.AssociationData;
import org.LexGrid.relations.AssociationSource;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;
import org.lexevs.dao.database.inserter.Inserter;
import org.mybatis.spring.SqlSessionTemplate;

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
			String associationDataUId,
			AssociationData data);

	public void deleteAssociationData(String codingSchemeUId,
			String associationDataUId);

	public String updateVersionableChanges(String codingSchemeUId,
			String associationDataUId,
			AssociationData data);

	public void deleteAllAssocQualsByAssocDataUId(String codingSchemeUId,
			String associationDataUId);

	public String getLatestRevision(String csUId, String assocDataUId);
	
	public boolean entryStateExists(String codingSchemeUId, String entryStateUId);

	public String insertAssociationData(String codingSchemeUId,
			String associationPredicateUId, AssociationSource source,
			AssociationData data, SqlSessionTemplate session);

	public AssociationData getAssociationDataByUid(
			String codingSchemeUId,
			String associationDataUid);

	public String getEntryStateUId(String codingSchemeUId, String associationDataUid);

	public AssociationData getHistoryAssociationDataByRevision(
			String codingSchemeUId, 
			String associationDataUid, 
			String revisionId);

	public void insertAssociationData(
			String codingSchemeUId,
			String associationPredicateUId, 
			String sourceEntityCode,
			String sourceEntityCodeNamespace, 
			AssociationData data);

	public AssociationSource getTripleByUid(String codingSchemeUId, String tripleUid);

	String insertMybatisBatchAssociationData(String codingSchemeUId, String associationPredicateUId,
			AssociationSource source, AssociationData data, SqlSessionTemplate session);

}