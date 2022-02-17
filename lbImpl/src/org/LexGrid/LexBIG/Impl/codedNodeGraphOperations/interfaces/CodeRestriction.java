
package org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.interfaces;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;

/**
 * Interface for a code restriction on a CodedNodeGraph
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public abstract class CodeRestriction implements Operation, Restriction {
    /**
     * 
     */
    private static final long serialVersionUID = -487846892517093703L;
    protected CodedNodeSet codedNodeSetCodes_;
    protected ConceptReference[] conceptRefCodes_ = null;

    public ConceptReference[] getCodes() throws LBInvocationException, LBParameterException {
        if (conceptRefCodes_ == null) {
            // then it is has a codedNodeSet I need to process. Processing is
            // done here,
            // incase it is expensive - this way it is part of the final
            // resolve.
            LocalNameList properties = new LocalNameList();
            properties.addEntry("non-existant-property"); // I dont need any
                                                          // properties back -
                                                          // best way to
            // get none.
            ResolvedConceptReferenceList list = codedNodeSetCodes_.resolveToList(null, properties, null, 0);

            ResolvedConceptReference[] items = list.getResolvedConceptReference();

            conceptRefCodes_ = new ConceptReference[items.length];

            for (int i = 0; i < items.length; i++) {
                conceptRefCodes_[i] = (ConceptReference) items[i];
            }
        }

        return this.conceptRefCodes_;
    }

}