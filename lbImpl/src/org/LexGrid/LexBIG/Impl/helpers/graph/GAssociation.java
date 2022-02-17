
package org.LexGrid.LexBIG.Impl.helpers.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;

import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.Impl.helpers.comparator.ResultComparator;
import org.LexGrid.annotations.LgClientSideSafe;

/**
 * Association object to help in constructing the graph.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class GAssociation {
    private GAssociationInfo associationInfo_;

    private Hashtable<String, GNode> children_;
    private Hashtable<String, NameAndValueList> qualifiers_;
    // Used for child Indicator...if true, it means that when
    // keepLastAssociationLevelUnresolved
    // is true, the GAssociation has children but were not added.
    private boolean unaddedChildrenPresent = false;

    public GAssociation(GAssociationInfo associationInfo) {
        children_ = new Hashtable<String, GNode>();
        qualifiers_ = new Hashtable<String, NameAndValueList>();
        associationInfo_ = associationInfo;
    }

    @LgClientSideSafe
    public GAssociationInfo getAssociationInfo() {
        return this.associationInfo_;
    }

    @LgClientSideSafe
    public void addChild(GNode child, NameAndValueList qualifiers) {
        children_.put(child.getKey(), child);
        if (qualifiers != null && qualifiers.getNameAndValueCount() > 0) {
            qualifiers_.put(child.getKey(), qualifiers);
        }
    }

    @LgClientSideSafe
    public void removeChild(GNode child) {
        children_.remove(child.getKey());
        qualifiers_.remove(child.getKey());
    }

    @LgClientSideSafe
    public int getChildCount() {
        return children_.size();
    }

    /*
     * Adds qualifiers to a qualifier list (removes dupes)
     */
    public void addQualifiers(GNode node, NameAndValueList qualifiersToAdd) {
        if (qualifiersToAdd == null) {
            return;
        }
        NameAndValueList qualifiers = qualifiers_.get(node.getKey());
        if (qualifiers == null) {
            qualifiers_.put(node.getKey(), qualifiersToAdd);
        } else {
            for (int i = 0; i < qualifiersToAdd.getNameAndValueCount(); i++) {
                NameAndValue currentQualifier = qualifiersToAdd.getNameAndValue(i);
                boolean add = true;
                for (int j = 0; j < qualifiers.getNameAndValueCount(); j++) {
                    if (currentQualifier.equals(qualifiers.getNameAndValue(j))) {
                        add = false;
                        break;
                    }
                }
                if (add) {
                    qualifiers.addNameAndValue(currentQualifier);
                }
            }
        }
    }

    @LgClientSideSafe
    public NameAndValueList getQualifier(GNode node) {
        return qualifiers_.get(node.getKey());
    }

    @LgClientSideSafe
    public boolean hasChild(GNode code) {
        return children_.containsKey(code.getKey());
    }

    @LgClientSideSafe
    public GNode getChild(GNode code) {
        return children_.get(code.getKey());
    }

    @LgClientSideSafe
    public Collection<GNode> getChildren(SortOptionList sortBy) {
        if(ResultComparator.isSortOptionListValid(sortBy)){
            ResultComparator<GNode> compare = new ResultComparator<GNode>(sortBy, GNode.class);

            GNode[] temp = children_.values().toArray(new GNode[children_.size()]);
            Arrays.sort(temp, compare);
            // put them back into a collection object to return
            ArrayList<GNode> temp2 = new ArrayList<GNode>(temp.length);
            for (int i = 0; i < temp.length; i++) {
                temp2.add(temp[i]);
            }
            return temp2;
        } else {
            return getChildren();
        }
    }

    @LgClientSideSafe
    public Collection<GNode> getChildren() {
        return children_.values();
    }

    public boolean isUnaddedChildrenPresent() {
        return unaddedChildrenPresent;
    }

    public void setUnaddedChildrenPresent(boolean unaddedChildrenPresent) {
        this.unaddedChildrenPresent = unaddedChildrenPresent;
    }

    public String toString() {
        String str = "children_ = " + children_ + "\n" + "qualifiers_ = " + qualifiers_;
        return str;
    }

}