package org.lexevs.dao.database.access.valuesets;

import java.util.List;

import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;
import org.lexevs.dao.database.access.association.model.Node;
import org.lexevs.dao.database.access.association.model.graphdb.GraphDbTriple;

public interface ValueSetHierarchyDao extends LexGridSchemaVersionAwareDao {

	List<Node> getAllVSTriplesTrOfVSNode(String codingSchemeId, String code,
			String associationName, int start, int pagesize);
	
	

}
