package org.LexGrid.LexBIG.Impl.pagedgraph.root;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery;

public interface RootsResolver {
    
    public enum ResolveDirection {FORWARD, BACKWARD}
    
    public List<ConceptReference> resolveRoots(
            String codingSchemeUri, 
            String codingSchemeVersion, 
            ResolveDirection direction,
            GraphQuery query);
    
    public boolean isRootOrTail(ConceptReference ref);
    
}
