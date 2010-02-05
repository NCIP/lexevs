package org.lexevs.dao.database.access.associatableelement;

import org.LexGrid.relations.AssociationData;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;

public interface AssociationSourceDao {
	
	public AssociationSource findAssociationSourceByAssociationInstanceId(int associationInstanceId);
	
	public void insertAssociationQualifier(String associationInstanceId, AssociationQualification record);
	
	public void insertAssociationTarget(String associationInstanceId, AssociationTarget record);
	
	public void insertAssociationData(String associationInstanceId, AssociationData record);
	
	public void insertAssociationUsageContext(String associationInstanceId, String record);

}
