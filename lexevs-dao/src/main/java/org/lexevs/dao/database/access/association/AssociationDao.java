package org.lexevs.dao.database.access.association;

import org.LexGrid.relations.Association;
import org.LexGrid.relations.AssociationSource;

public interface AssociationDao {

	public String insertAssociation(String codingScheme, String version, Association association);
	
	public Association getAssociationBy(String codingScheme, String version, Association association);
	
	public String insertAssociationSource(String codingScheme, String version, AssociationSource source);

}
