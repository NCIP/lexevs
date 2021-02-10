
package org.LexGrid.LexBIG.Impl.pagedgraph.root;

import java.io.Serializable;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery;

public interface RootsResolver extends Serializable {
    
    public enum ResolveDirection {FORWARD, BACKWARD}
    
    public List<ConceptReference> resolveRoots(
            String codingSchemeUri, 
            String codingSchemeVersion, 
            String relationsContainerName,
            ResolveDirection direction,
            GraphQuery query,
            SortOptionList sortOptionList);
    
    public boolean isRootOrTail(ConceptReference ref);

    public List<ConceptReference> resolveRoots(
            String codingSchemeUri, 
            String version, 
            String containerName,
            ResolveDirection direction, 
            GraphQuery graphQuery, 
            SortOptionList sortOptionList,
            int currentPosition, 
            int pageSize);
    
}