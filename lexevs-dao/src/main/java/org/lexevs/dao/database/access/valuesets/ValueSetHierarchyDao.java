package org.lexevs.dao.database.access.valuesets;

import java.util.List;

import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;
import org.lexevs.dao.database.access.association.model.DefinedNode;
import org.lexevs.dao.database.access.association.model.Node;
import org.lexevs.dao.database.access.association.model.VSHierarchyNode;
import org.lexevs.dao.database.access.association.model.graphdb.GraphDbTriple;

public interface ValueSetHierarchyDao extends LexGridSchemaVersionAwareDao {

	/**
	 * Highly specialized for an asserted value set hierarchy such as that found in the 
	 * NCIthesaurus.  Nodes are designed to carry data elements specific to this need.
	 * 
	 * @param codingSchemeId - database assigned guid for coding scheme
	 * @param code - entity code that defines value set
	 * @param associationName - database assigned guid of relation from top node to values 
	 * @param propertyOne - source designation
	 * @param propertyTwo - property name for publish or not flag
	 * @param canPublish - publish-able flag value
	 * @param start - paging cursor
	 * @param pagesize - paging result set size
	 * @return List<VSHierarchyNode> - returns results as hierarchy specific object list
	 */
	List<VSHierarchyNode> getAllVSTriplesTrOfVSNode(String codingSchemeId, String code,
			String associationName, String propertyOne, String propertyTwo, String canPublish, int start, int pagesize);

	/**
	 * Getting any values that are defined as value set members by a specified association
	 * and a "can publish" flag
	 * 
	 * @param codingSchemeId - database assigned guid for coding scheme
	 * @param associationGuid - database assigned guid of relation from top node to value
	 * @param publishName - property name for publish or not flag
	 * @param canPublish - publish-able flag value
	 * @param start - paging cursor
	 * @param pagesize - paging result set size
	 * @return List<DefinedNode> list of all value set entities as minimal entity representation object
	 */
	List<DefinedNode> getAllVSTriples(String codingSchemeId, String associationGuid, String publishName,
			String canPublish, int start, int pagesize);
	
	

}
