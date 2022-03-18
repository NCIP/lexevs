
package org.LexGrid.LexBIG.gui.displayResults;

import java.awt.event.MouseEvent;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;

import prefuse.controls.FocusControl;
import prefuse.visual.VisualItem;

/**
 * Focus Listener for the graph to allow me to back select to a swt list..
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class VDMyFocusControl extends FocusControl {
    VDDisplayCodedNodeSet dcns_;

    public VDMyFocusControl(int clicks, String act, VDDisplayCodedNodeSet dcns) {
        super(clicks, act);
        dcns_ = dcns;
    }

    /*
     * (non-Javadoc)
     * 
     * @see prefuse.controls.FocusControl#itemClicked(prefuse.visual.VisualItem,
     * java.awt.event.MouseEvent)
     */
    @Override
    public void itemClicked(VisualItem item, MouseEvent e) {
        super.itemClicked(item, e);

        ResolvedConceptReference rcr = (ResolvedConceptReference) item
                .get("RCR");
        if (rcr != null) {
            dcns_.graphItemSelected(rcr);
        }
    }
}