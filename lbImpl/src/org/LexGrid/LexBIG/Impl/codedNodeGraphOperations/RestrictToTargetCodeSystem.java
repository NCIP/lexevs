
package org.LexGrid.LexBIG.Impl.codedNodeGraphOperations;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.interfaces.CodeSystemRestriction;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.interfaces.Operation;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.interfaces.Restriction;

/**
 * Holder for the RestrictToCodeSystem call on CodedNodeGraph.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class RestrictToTargetCodeSystem extends CodeSystemRestriction implements Operation, Restriction {
    private static final long serialVersionUID = 6510009347956978563L;

    public RestrictToTargetCodeSystem(String codeSystem) throws LBParameterException {
        super(codeSystem);
    }
}