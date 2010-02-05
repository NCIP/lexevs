package org.lexevs.dao.database.access.association;

import org.LexGrid.relations.Association;

public interface AssociationDao {

	public void insert(String codingScheme, String version, Association record);

}
