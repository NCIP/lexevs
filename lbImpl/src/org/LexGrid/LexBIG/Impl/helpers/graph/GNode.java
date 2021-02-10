
package org.LexGrid.LexBIG.Impl.helpers.graph;

import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.Impl.dataAccess.SQLImplementedMethods;
import org.LexGrid.LexBIG.Impl.helpers.comparator.ResultComparator;
import org.LexGrid.annotations.LgClientSideSafe;
import org.lexevs.exceptions.MissingResourceException;
import org.lexevs.exceptions.UnexpectedInternalError;

/**
 * A concept code in my graph - helps me build the graph I need to return.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class GNode {
    private String code_;
    private String codeNamespace_;
    private String codeSystem_;
    private String entityDescription_;
    private String[] codeTypes_;
    private String definingCodeSystemURN;
    private String definingCodeSystemVersion_;
    private boolean nodeHasBeenPrinted_ = false;
    private Hashtable<String, GAssociation> sourceAssociations_;
    private Hashtable<String, GAssociation> targetAssociations_;

    protected GNode(GNode referenceNode) {
        code_ = referenceNode.code_;
        codeNamespace_ = referenceNode.codeNamespace_;
        codeSystem_ = referenceNode.codeSystem_;
        entityDescription_ = referenceNode.entityDescription_;
        codeTypes_ = referenceNode.codeTypes_;
        definingCodeSystemURN = referenceNode.definingCodeSystemURN;
        definingCodeSystemVersion_ = referenceNode.definingCodeSystemVersion_;
        nodeHasBeenPrinted_ = referenceNode.nodeHasBeenPrinted_;
        targetAssociations_ = new Hashtable<String, GAssociation>();
        sourceAssociations_ = new Hashtable<String, GAssociation>();
    }

    public GNode(String codeSystem, String codeNamespace, String code, String[] codeTypes,
            String entityDescription, String internalCodeSystemName, String internalVersionString)
            throws MissingResourceException, UnexpectedInternalError {
        codeSystem_ = codeSystem;
        codeNamespace_ = codeNamespace;
        code_ = code;
        codeTypes_ = codeTypes;
        entityDescription_ = entityDescription;
        targetAssociations_ = new Hashtable<String, GAssociation>();
        sourceAssociations_ = new Hashtable<String, GAssociation>();

        // Resolve URN and version information based on the provided internal name/version ...
        definingCodeSystemURN = SQLImplementedMethods.getURNForRelationshipCodingSchemeName(codeSystem_,
                internalCodeSystemName, internalVersionString, true);

        String currentURN = SQLImplementedMethods.getURNForInternalCodingSchemeName(internalCodeSystemName,
                internalVersionString);

        if (currentURN.equals(definingCodeSystemURN)) {
            // This code is from the code system represented by
            // internalCodeSystemName - so I can set the version
            // to the version provided.
            definingCodeSystemVersion_ = internalVersionString;
        } else {
            // The codes are from some other code system - so I don't know what
            // version to use.
            definingCodeSystemVersion_ = null;
        }
    }

    @Override
    @LgClientSideSafe
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof GNode && this.codeSystem_.equals(((GNode) obj).codeSystem_)
                && this.code_.equals(((GNode) obj).code_)
                && this.definingCodeSystemURN.equals(((GNode) obj).definingCodeSystemURN)) {
            return true;
        }

        return false;
    }

    @LgClientSideSafe
    public String getCodeNamespace() {
        return codeNamespace_;
    }

    @LgClientSideSafe
    public String getCodeSystem() {
        return this.codeSystem_;
    }
    
    @LgClientSideSafe
    public String getEntityDescription() {
        return this.entityDescription_;
    }

    @LgClientSideSafe
    public String[] getCodeTypes() {
        if (codeTypes_ == null)
            codeTypes_ = new String[0];
        return codeTypes_;
    }

    @LgClientSideSafe
    public String getCode() {
        return this.code_;
    }

    @LgClientSideSafe
    public String getDefiningCodeSystemURN() {
        return this.definingCodeSystemURN;
    }

    @LgClientSideSafe
    public String getDefiningCodeSystemVersion() {
        return this.definingCodeSystemVersion_;
    }

    @LgClientSideSafe
    public int getIncomingLinkCount() {
        int total = 0;
        Enumeration<GAssociation> sourceAssns = sourceAssociations_.elements();
        while (sourceAssns.hasMoreElements()) {
            GAssociation element = sourceAssns.nextElement();
            total += element.getChildCount();
        }
        return total;
    }

    @LgClientSideSafe
    public String getKey() {
        return getKey(codeSystem_, codeNamespace_, code_); 
    }

    @LgClientSideSafe
    public static String getKey(String codeSystem, String codeNamespace, String conceptCode) {
        StringBuffer sb = new StringBuffer(64);
        if (codeSystem != null)
            sb.append(codeSystem).append("[:]");
        if (codeNamespace != null)
            sb.append(codeNamespace).append("[:]");
        return
            sb.append(conceptCode)
                .toString();
    }

    @LgClientSideSafe
    public GAssociation getSourceAssociation(GAssociationInfo gai) {
        GAssociation association = sourceAssociations_.get(gai.getKey());

        if (association == null) {
            association = new GAssociation(gai);
            sourceAssociations_.put(gai.getKey(), association);
        }
        return association;
    }

    @LgClientSideSafe
    public Enumeration<GAssociation> getSourceAssociations() {
        return this.sourceAssociations_.elements();
    }

    @LgClientSideSafe
    public Enumeration<GAssociation> getSourceAssociations(SortOptionList sortBy) {
        return sort(this.sourceAssociations_, sortBy);
    }

    @LgClientSideSafe
    public GAssociation getTargetAssociation(GAssociationInfo gai) {
        GAssociation association = targetAssociations_.get(gai.getKey());

        if (association == null) {
            association = new GAssociation(gai);
            targetAssociations_.put(gai.getKey(), association);
        }
        return association;
    }

    @LgClientSideSafe
    public Enumeration<GAssociation> getTargetAssociations() {
        return this.targetAssociations_.elements();
    }

    @LgClientSideSafe
    public Enumeration<GAssociation> getTargetAssociations(SortOptionList sortBy) {
        return sort(this.targetAssociations_, sortBy);
    }

    /**
     * Maintain only those links shared with the given node; drop the rest.
     * 
     * @param node
     */
    @LgClientSideSafe
    public void intersectLinks(GNode node) {
        for (Iterator<String> keys = sourceAssociations_.keySet().iterator(); keys.hasNext();) {
            String key = keys.next();
            GAssociation mySourceAssoc = sourceAssociations_.get(key);
            GAssociation otherAssoc = node.getSourceAssociation(mySourceAssoc.getAssociationInfo());
            if (otherAssoc == null)
                sourceAssociations_.remove(key);
            else {
                Collection<GNode> myChildren = mySourceAssoc.getChildren();
                myChildren.retainAll(otherAssoc.getChildren());
                if (myChildren.size() == 0)
                    sourceAssociations_.remove(key);
            }
        }
        for (Iterator<String> keys = targetAssociations_.keySet().iterator(); keys.hasNext();) {
            String key = keys.next();
            GAssociation myTargetAssoc = targetAssociations_.get(key);
            GAssociation otherAssoc = node.getTargetAssociation(myTargetAssoc.getAssociationInfo());
            if (otherAssoc == null)
                targetAssociations_.remove(key);
            else {
                Collection<GNode> myChildren = myTargetAssoc.getChildren();
                myChildren.retainAll(otherAssoc.getChildren());
                if (myChildren.size() == 0)
                    targetAssociations_.remove(key);
            }
        }
    }

    @LgClientSideSafe
    public boolean isChildless() {
        Enumeration<GAssociation> targetAssns = targetAssociations_.elements();
        while (targetAssns.hasMoreElements()) {
            GAssociation element = targetAssns.nextElement();
            if (element.getChildCount() > 0) {
                return false;
            }
        }
        return true;
    }

    @LgClientSideSafe
    public boolean isNodeHasBeenPrinted() {
        return this.nodeHasBeenPrinted_;
    }

    @LgClientSideSafe
    public void removeLinkFrom(GNode node, GAssociation association) {
        this.sourceAssociations_.get(association.getAssociationInfo().getKey()).removeChild(node);
    }

    @LgClientSideSafe
    public void removeLinkTo(GNode node, GAssociation association) {
        this.targetAssociations_.get(association.getAssociationInfo().getKey()).removeChild(node);
    }

    @LgClientSideSafe
    public void setNodeHasBeenPrinted(boolean nodeHasBeenPrinted) {
        this.nodeHasBeenPrinted_ = nodeHasBeenPrinted;
    }

    @LgClientSideSafe
    private Enumeration<GAssociation> sort(Hashtable<String, GAssociation> elements, SortOptionList sortBy) {
        if (ResultComparator.isSortOptionListValid(sortBy)) {
            ResultComparator<GAssociation> compare = new ResultComparator<GAssociation>(sortBy, GAssociation.class);
            GAssociation[] temp = elements.values().toArray(new GAssociation[elements.size()]);
            Arrays.sort(temp, compare);
            // put them back into an object I can get an enumeration out of.
            Vector<GAssociation> temp2 = new Vector<GAssociation>(temp.length);
            for (int i = 0; i < temp.length; i++) {
                temp2.add(temp[i]);
            }
            return temp2.elements();
        }

        // If I didn't need to sort, just return them unsorted.
        return elements.elements();
    }
      
    public String toString() {
        String str = "codeSystem_ = " + codeSystem_ + "\n" + "conceptCode_ = " + code_ + "\n"
                + "definingCodeSystemVersion_ = " + definingCodeSystemVersion_ + "\n" + "definingCodeSystemURN = "
                + definingCodeSystemURN + "\n" + "nodeHasBeenPrinted_ = " + nodeHasBeenPrinted_ + "\n"
                + "targetAssociations_ = " + targetAssociations_ + "\n" + "sourceAssociations_ = "
                + sourceAssociations_ + "\n";
        return str;
    }
}