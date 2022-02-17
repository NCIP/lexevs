
package org.LexGrid.LexBIG.Impl.codedNodeGraphOperations;

import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.interfaces.Operation;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.annotations.LgClientSideSafe;

/**
 * Holder for a Union operation on the graph
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class Union implements Operation {
    private static final long serialVersionUID = -8823362091621747123L;
    private CodedNodeGraph graph_;

    public Union(CodedNodeGraph graph) {
        graph_ = graph;
    }

    @LgClientSideSafe
    public CodedNodeGraph getGraph() {
        return this.graph_;
    }

}