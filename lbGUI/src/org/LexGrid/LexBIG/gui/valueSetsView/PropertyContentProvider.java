
package org.LexGrid.LexBIG.gui.valueSetsView;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.gui.LB_VSD_GUI;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * Content provider for the Value Set Definition Property SWT Table.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class PropertyContentProvider implements IStructuredContentProvider {
    private LB_VSD_GUI lbVDGui_;
    private static Logger log = LogManager.getLogger("LB_VSGUI_LOGGER");
    private Property[] currentPropertyRenderings_ = null;
    private ValueSetDefinition vsd_;

    public PropertyContentProvider(LB_VSD_GUI lbVDGui, ValueSetDefinition vsd) {
        lbVDGui_ = lbVDGui;
        vsd_ = vsd;
    }

    public Object[] getElements(Object arg0) {
        try {
            return getProperties();
        } catch (LBInvocationException e) {
            log.error("Unexpected Error", e);
            return new String[] {};
        } catch (URISyntaxException e) {
            log.error("Unexpected Error", e);
            return new String[] {};
        }
    }

    public void dispose() {
        // do nothing
    }

    public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
        currentPropertyRenderings_ = null;
    }

    private Property[] getProperties()
            throws LBInvocationException, URISyntaxException {
        if (currentPropertyRenderings_ == null) {
            if (lbVDGui_.getValueSetDefinitionService() != null) {
                List<Property> props = new ArrayList<Property>();
                if (vsd_ != null && vsd_.getProperties() != null)
                    props = vsd_.getProperties().getPropertyAsReference();
                if (props.size() > 0)
                {
                    currentPropertyRenderings_ = new Property[props.size()];
                    for (int i = 0; i < props.size(); i++)
                    {
                        currentPropertyRenderings_[i] = props.get(i);
                    }
                } else {
                    currentPropertyRenderings_ = new Property[] {};
                }
            Arrays.sort(currentPropertyRenderings_,
                        new PropertyRenderingComparator());
            } else {
                currentPropertyRenderings_ = new Property[] {};
            }

        }
        return currentPropertyRenderings_;
    }

    private class PropertyRenderingComparator implements Comparator<Property> {
        public int compare(Property o1, Property o2) {
            return (String.valueOf(o1.getPropertyId()).compareToIgnoreCase(String.valueOf(o2.getPropertyId())));
        }
    }

}