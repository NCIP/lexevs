
package org.LexGrid.LexBIG.Impl.codedNodeGraphOperations;

import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.interfaces.Operation;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.interfaces.Restriction;
import org.LexGrid.annotations.LgClientSideSafe;

/**
 * Holder for the Associations call on CodedNodeGraph.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class RestrictToAssociations implements Operation, Restriction {
    private static final long serialVersionUID = -2978700134481702063L;
    private NameAndValueList associations_;
    private NameAndValueList associationQualifiers_;

    @LgClientSideSafe
    public NameAndValueList getAssociationQualifiers() {
        return this.associationQualifiers_;
    }

    @LgClientSideSafe
    public NameAndValueList getAssociations() {
        return this.associations_;
    }

    public RestrictToAssociations(NameAndValueList associations, NameAndValueList associationQualifiers) {
        associations_ = associations;
        associationQualifiers_ = associationQualifiers;
    }
}