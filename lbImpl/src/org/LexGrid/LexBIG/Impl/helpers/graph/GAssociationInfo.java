
package org.LexGrid.LexBIG.Impl.helpers.graph;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.annotations.LgClientSideSafe;
import org.lexevs.system.ResourceManager;

/**
 * AssociationInfo object to assist in building the graph. The information in
 * this class gets reused by all of the Association Instances as appropriate.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class GAssociationInfo {
    private String codeSystem_;
    private String name_;
    private String urn_; // this urn is from the codingSchemeSupportedAttributes
                         // table - it (sometimes)
    // has the concept code on the end of it.
    private String forwardName_;
    private String reverseName_;
    private Boolean isNavigable_;
    private ConceptReference reference_;

    public GAssociationInfo(String codeSystem, String urn, String name, String forwardName, String reverseName,
            Boolean isNavigable) {
        this.codeSystem_ = codeSystem;
        this.urn_ = urn;
        this.name_ = name;
        this.forwardName_ = forwardName;
        this.reverseName_ = reverseName;
        this.isNavigable_ = isNavigable;
    }

    @LgClientSideSafe
    public static String getKey(String codeSystem, String name) {
        return codeSystem + "[:]" + name;
    }

    @LgClientSideSafe
    public String getKey() {
        return codeSystem_ + "[:]" + name_;
    }

    @LgClientSideSafe
    public String getForwardName() {
        return this.forwardName_;
    }

    @LgClientSideSafe
    public void setForwardName(String forwardName) {
        this.forwardName_ = forwardName;
    }

    @LgClientSideSafe
    public Boolean getIsNavigable() {
        return this.isNavigable_;
    }

    @LgClientSideSafe
    public void setIsNavigable(Boolean isNavigable) {
        this.isNavigable_ = isNavigable;
    }

    @LgClientSideSafe
    public String getName() {
        return this.name_;
    }

    @LgClientSideSafe
    public void setName(String name) {
        this.name_ = name;
    }

    @LgClientSideSafe
    public String getReverseName() {
        return this.reverseName_;
    }

    @LgClientSideSafe
    public void setReverseName(String reverseName) {
        this.reverseName_ = reverseName;
    }

    @LgClientSideSafe
    public ConceptReference getAssociationReference() {
        if (reference_ == null) {
            String temp2 = null;
            reference_ = new ConceptReference();
            reference_.setCodingSchemeName(urn_);

            if (urn_ != null) {
                // if the URN is of the form "urn:oid:2.2.2:code I want to put
                // the portion after the third colon in as the code.
                if (urn_.toLowerCase().startsWith("urn:oid:")) {
                    String temp = urn_.substring("urn:oid:".length());
                    int pos = temp.indexOf(':');
                    if (pos != -1 && temp.length() > pos) {
                        temp2 = temp.substring(pos + 1, temp.length());
                        reference_.setCodingSchemeName(urn_.substring(0, urn_.length() - (temp2.length() + 1)));
                    }
                }

                reference_
                        .setCodingSchemeName(ResourceManager.instance()
                                .getExternalCodingSchemeNameForUserCodingSchemeNameOrId(
                                        reference_.getCodingSchemeName(), null));
            }

            reference_.setCode(temp2);
        }
        return reference_;
    }
}