
package org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.interfaces;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.annotations.LgClientSideSafe;

/**
 * Interface for a code system restriction on a codedNodeGraph operation
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public abstract class CodeSystemRestriction implements Restriction, Operation {
    /**
     * 
     */
    private static final long serialVersionUID = 686440611568951158L;
    private String supportedCodeSystemOrURN_;

    public CodeSystemRestriction(String codeSystem) throws LBParameterException {
        if (codeSystem == null || codeSystem.length() == 0) {
            throw new LBParameterException("Missing parameter", "codeSystem");
        }

        supportedCodeSystemOrURN_ = codeSystem;
    }

    @LgClientSideSafe
    public String getSupportedCodeSystemOrURN() {
        return this.supportedCodeSystemOrURN_;
    }

}