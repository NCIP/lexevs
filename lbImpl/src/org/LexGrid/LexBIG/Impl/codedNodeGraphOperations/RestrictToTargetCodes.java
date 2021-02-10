
package org.LexGrid.LexBIG.Impl.codedNodeGraphOperations;

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.interfaces.CodeRestriction;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.interfaces.Operation;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.interfaces.Restriction;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;

/**
 * Holder for the RestrictToTargetCodes call on CodedNodeGraph.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class RestrictToTargetCodes extends CodeRestriction implements Operation, Restriction {
    private static final long serialVersionUID = -2196241976342891897L;

    public RestrictToTargetCodes(CodedNodeSet codes) {
        codedNodeSetCodes_ = codes;
    }

    public RestrictToTargetCodes(ConceptReference code) throws LBParameterException {
        if (code == null || code.getCodingSchemeName() == null || code.getConceptCode() == null
                || code.getCodingSchemeName().length() == 0 || code.getConceptCode().length() == 0) {
            throw new LBParameterException("You did not provide a code to restrict to");
        }
        conceptRefCodes_ = new ConceptReference[] { code };
    }

    public RestrictToTargetCodes(ConceptReferenceList codes) throws LBParameterException {
        if (codes == null || codes.getConceptReference() == null || codes.getConceptReference().length == 0) {
            throw new LBParameterException("You did not provide a code to restrict to");
        }
        conceptRefCodes_ = codes.getConceptReference();
    }

}