
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
public class GNodeComparator implements Comparator<Object> {
    ArrayList<SortOption> algorithms_;

    public GNodeComparator(SortOptionList sortAlgoritms) {
        // only grab the algoritms that are supported here.
        algorithms_ = new ArrayList<SortOption>();
        if (sortAlgoritms != null) {
            for (int i = 0; i < sortAlgoritms.getEntryCount(); i++) {
                String temp = sortAlgoritms.getEntry(i).getExtensionName();
                if (temp.equalsIgnoreCase("code") || temp.equalsIgnoreCase("codeSystem")) {
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
        GNode a = (GNode) o1;
        GNode b = (GNode) o2;

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

    private int doCompare(GNode a, GNode b, int algorithmIndex) {
        SortOption algorithm = algorithms_.get(algorithmIndex);
        int reverse = 1;
        if (algorithm.getAscending() != null && !algorithm.getAscending().booleanValue()) {
            reverse = -1;
        }
        if (algorithm.getExtensionName().equalsIgnoreCase("code")) {
            return compareByCode(a, b) * reverse;
        } else if (algorithm.getExtensionName().equalsIgnoreCase("codeSystem")) {
            return compareByUrn(a, b) * reverse;
        } else {
            return 0;
        }
    }

    private int compareByCode(GNode a, GNode b) {
        return a.getCode().compareToIgnoreCase(b.getCode());
    }

    private int compareByUrn(GNode a, GNode b) {
        return a.getCodeSystem().compareToIgnoreCase(b.getCodeSystem());
    }
}