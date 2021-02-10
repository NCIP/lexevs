
package org.LexGrid.LexBIG.gui.displayResults;

import java.util.Iterator;

import prefuse.action.filter.FisheyeTreeFilter;
import prefuse.util.PrefuseLib;
import prefuse.visual.VisualItem;

/**
 * Overrides behavior of the standard FisheyeTreeFilter to maintain visibility
 * of all node connections.
 */
public class VDGraphViewFilter extends FisheyeTreeFilter {
    VDGraphView gv_ = null;

    public VDGraphViewFilter(String group, int depth, VDGraphView view) {
        super(group, depth);
        gv_ = view;
    }

    @SuppressWarnings("unchecked")
    public void run(double d) {
        super.run(d);
        // Superclass will make these items invisible; undo here...
        if (gv_.dcns.getShowSecondaryRelInGraph())
            for (Iterator<VisualItem> items = m_vis.items(m_group); items
                    .hasNext();) {
                VisualItem item = items.next();
                if (item.getDOI() == -1.7976931348623157E+308D)
                    PrefuseLib.updateVisible(item, true);
            }
    }
}