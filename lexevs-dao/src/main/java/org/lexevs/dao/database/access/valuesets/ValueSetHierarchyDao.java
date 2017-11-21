package org.lexevs.dao.database.access.valuesets;

import java.util.List;

import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;
import org.lexevs.dao.database.access.association.model.DefinedNode;
import org.lexevs.dao.database.access.association.model.Node;
import org.lexevs.dao.database.access.association.model.VSHierarchyNode;
import org.lexevs.dao.database.access.association.model.graphdb.GraphDbTriple;

public interface ValueSetHierarchyDao extends LexGridSchemaVersionAwareDao {

	List<VSHierarchyNode> getAllVSTriplesTrOfVSNode(String codingSchemeId, String code,
			String associationName, String propertyOne, String propertyTwo, String canPublish, int start, int pagesize);

	List<DefinedNode> getAllVSTriples(String codingSchemeId, String associationGuid, String publishName,
			String canPublish, int start, int pagesize);
	
	

}
