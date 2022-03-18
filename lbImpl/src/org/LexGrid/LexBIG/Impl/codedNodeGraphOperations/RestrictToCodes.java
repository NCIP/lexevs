
package org.LexGrid.LexBIG.Impl.codedNodeGraphOperations;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.interfaces.CodeRestriction;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.interfaces.Operation;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.interfaces.Restriction;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;

/**
 * Holder for the RestrictToCodes call on CodedNodeGraph.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class RestrictToCodes extends CodeRestriction implements Operation, Restriction {
    private static final long serialVersionUID = -7627334300683655750L;

    public RestrictToCodes(CodedNodeSet codes) {
        codedNodeSetCodes_ = codes;
    }

    public RestrictToCodes(ConceptReference codes) throws LBParameterException {
        if (codes == null || codes.getCodingSchemeName() == null || codes.getConceptCode() == null
                || codes.getCodingSchemeName().length() == 0 || codes.getConceptCode().length() == 0) {
            throw new LBParameterException("You did not provide a code to restrict to");
        }
        conceptRefCodes_ = new ConceptReference[] { codes };
    }

}