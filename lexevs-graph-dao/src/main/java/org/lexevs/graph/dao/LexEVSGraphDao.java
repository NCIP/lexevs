package org.lexevs.graph.dao;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;

public interface LexEVSGraphDao {
	
	public List<ConceptReference> getTransitiveClosureForNode( ConceptReference nodes, String associationName);
	public List<ConceptReference> getAllIncomingRelationsForNode( ConceptReference node, String associationName);

}
