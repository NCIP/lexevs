
package org.LexGrid.LexBIG.Impl.codedNodeGraphOperations;

import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.interfaces.Operation;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.interfaces.Restriction;

/**
 * Holder for the directionalNames call on CodedNodeGraph.
 * 
 * @author <A HREF="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class RestrictToDirectionalNames implements Operation, Restriction {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2978700134481702063L;
    private NameAndValueList directionalNames_;
    private NameAndValueList associationQualifiers_;

    public NameAndValueList getAssociationQualifiers() {
        return this.associationQualifiers_;
    }

    public NameAndValueList getDirectionalNames() {
        return this.directionalNames_;
    }

    public RestrictToDirectionalNames(NameAndValueList directionalNames, NameAndValueList associationQualifiers) {
        directionalNames_ = directionalNames;
        associationQualifiers_ = associationQualifiers;
    }
}