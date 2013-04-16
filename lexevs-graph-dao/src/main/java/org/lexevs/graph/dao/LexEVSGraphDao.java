package org.lexevs.graph.dao;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;

public interface LexEVSGraphDao {
	
	public List<ConceptReference> getTransitiveClosureForNodes( List<ConceptReference> nodes, List<String> associationNames);
	public List<ConceptReference> getAllIncomingRelationsForNode( List<ConceptReference> nodes, List<String> associationNames);
	

}
