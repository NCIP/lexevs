
package org.LexGrid.LexBIG.Impl.codedNodeGraphOperations;

import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.interfaces.Operation;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.annotations.LgClientSideSafe;

/**
 * Holder for a Intersection operation on a graph.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class Intersection implements Operation {
    private static final long serialVersionUID = 7531611545705614323L;
    private CodedNodeGraph graph_;

    public Intersection(CodedNodeGraph graph) {
        graph_ = graph;
    }

    @LgClientSideSafe
    public CodedNodeGraph getGraph() {
        return this.graph_;
    }

}