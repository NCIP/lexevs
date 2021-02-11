
package org.LexGrid.LexBIG.Impl.helpers.graph;

import java.util.ArrayList;
import java.util.Comparator;

import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.SortOption;
import org.LexGrid.annotations.LgClientSideSafe;

/**
 * Class to assist in sorting scored concept codes.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
@Deprecated
public class GAssociationComparator implements Comparator<Object> {
    ArrayList<SortOption> algorithms_;

    public GAssociationComparator(SortOptionList sortAlgoritms) {
        // only grab the algoritms that are supported here.
        algorithms_ = new ArrayList<SortOption>();
        if (sortAlgoritms != null) {
            for (int i = 0; i < sortAlgoritms.getEntryCount(); i++) {
                String temp = sortAlgoritms.getEntry(i).getExtensionName();
                if (temp.equalsIgnoreCase("associationName") || temp.equalsIgnoreCase("associationForwardName")
                        || temp.equalsIgnoreCase("associationReverseName")
                        || temp.equalsIgnoreCase("associationChildCount")) {
                    algorithms_.add(sortAlgoritms.getEntry(i));
                }
            }
        }
    }

    @LgClientSideSafe
    public boolean willSort() {
        return algorithms_.size() > 0;
    }

    @LgClientSideSafe
    public int compare(Object o1, Object o2) {
        GAssociation a = (GAssociation) o1;
        GAssociation b = (GAssociation) o2;

        int i = 0;
        while (i < algorithms_.size()) {
            int result = doCompare(a, b, i);
            if (result != 0) {
                // items are not equal, don't need to step down to the
                // additional algorithms.
                return result;
            }

            i++;
        }
        // If we get here, that means none of the algorithms found a difference
        // between two items.

        return 0;
    }

    @LgClientSideSafe
    private int doCompare(GAssociation a, GAssociation b, int algorithmIndex) {
        SortOption algorithm = algorithms_.get(algorithmIndex);

        int reverse = 1;
        if (algorithm.getAscending() != null && !algorithm.getAscending().booleanValue()) {
            reverse = -1;
        }

        if (algorithm.getExtensionName().equalsIgnoreCase("associationName")) {
            return compareByName(a, b) * reverse;
        } else if (algorithm.getExtensionName().equalsIgnoreCase("associationForwardName")) {
            return compareByForwardName(a, b) * reverse;
        } else if (algorithm.getExtensionName().equalsIgnoreCase("associationReverseName")) {
            return compareByReverseName(a, b) * reverse;
        } else if (algorithm.getExtensionName().equalsIgnoreCase("associationChildCount")) {
            return compareByNumChildren(a, b) * reverse;
        } else {
            return 0;
        }
    }

    private int compareByNumChildren(GAssociation a, GAssociation b) {
        return ((Integer) a.getChildCount()).compareTo(((Integer) b.getChildCount())) * -1;
    }

    private int compareByName(GAssociation a, GAssociation b) {
        return a.getAssociationInfo().getName().compareToIgnoreCase(b.getAssociationInfo().getName());
    }

    private int compareByForwardName(GAssociation a, GAssociation b) {
        return a.getAssociationInfo().getForwardName().compareToIgnoreCase(b.getAssociationInfo().getForwardName());
    }

    private int compareByReverseName(GAssociation a, GAssociation b) {
        return a.getAssociationInfo().getReverseName().compareToIgnoreCase(b.getAssociationInfo().getReverseName());
    }
}