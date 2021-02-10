
package org.LexGrid.LexBIG.Impl.pagedgraph.paging;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.Impl.pagedgraph.root.NullFocusRootsResolver;
import org.LexGrid.LexBIG.Impl.pagedgraph.root.RootsResolver;
import org.LexGrid.LexBIG.Impl.pagedgraph.root.RootsResolver.ResolveDirection;
import org.LexGrid.annotations.LgClientSideSafe;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery;
import org.lexevs.paging.AbstractPageableIterator;

/**
 * The Class AssociatedConceptIterator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@LgClientSideSafe
public class RootConceptReferenceIterator extends AbstractPageableIterator<ConceptReference> {

    private static final long serialVersionUID = -7463384030723777372L;

    private RootsResolver rootsResolver = new NullFocusRootsResolver();
    
    private String codingSchemeUri;
    private String version;
    private String containerName;
    private ResolveDirection direction;
    private GraphQuery graphQuery;
    private SortOptionList sortOptionList;
    
    public RootConceptReferenceIterator(
            String codingSchemeUri,
            String version,
            String containerName,
            ResolveDirection direction,
            GraphQuery graphQuery,
            SortOptionList sortOptionList) {
        this.codingSchemeUri = codingSchemeUri;
        this.version = version;
        this.containerName = containerName;
        this.direction = direction;
        this.graphQuery = graphQuery;
        this.sortOptionList = sortOptionList;
    }
    
    @Override
    protected List<? extends ConceptReference> doPage(int currentPosition, int pageSize) {
        return  
            rootsResolver.resolveRoots(
                    codingSchemeUri, 
                    version,
                    containerName,
                    direction, 
                    graphQuery, 
                    sortOptionList,
                    currentPosition, 
                    pageSize);
    }
}