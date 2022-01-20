
package org.LexGrid.LexBIG.gui.valueSetsView;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.gui.LB_VSD_GUI;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * Content provider for the Property Qualifier SWT Table.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class PropertyQualifierContentProvider implements IStructuredContentProvider {
    private LB_VSD_GUI lbVDGui_;
    private static Logger log = LogManager.getLogger("LB_VSGUI_LOGGER");
    private PropertyQualifier[] currentPropertyQualifierRenderings_ = null;
    private Property property_;

    public PropertyQualifierContentProvider(LB_VSD_GUI lbVDGui, Property property) {
        lbVDGui_ = lbVDGui;
        property_ = property;
    }

    public Object[] getElements(Object arg0) {
        try {
            return getPropertyQualifiers();
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
        currentPropertyQualifierRenderings_ = null;
    }

    private PropertyQualifier[] getPropertyQualifiers()
            throws LBInvocationException, URISyntaxException {
        if (currentPropertyQualifierRenderings_ == null) {
            if (lbVDGui_.getValueSetDefinitionService() != null) {
                List<PropertyQualifier> propQuals = new ArrayList<PropertyQualifier>();
                if (property_ != null && property_.getPropertyQualifier() != null)
                    propQuals = property_.getPropertyQualifierAsReference();
                if (propQuals.size() > 0)
                {
                    currentPropertyQualifierRenderings_ = new PropertyQualifier[propQuals.size()];
                    for (int i = 0; i < propQuals.size(); i++)
                    {
                        currentPropertyQualifierRenderings_[i] = propQuals.get(i);
                    }
                } else {
                    currentPropertyQualifierRenderings_ = new PropertyQualifier[] {};
                }
            Arrays.sort(currentPropertyQualifierRenderings_,
                        new PropertyRenderingComparator());
            } else {
                currentPropertyQualifierRenderings_ = new PropertyQualifier[] {};
            }

        }
        return currentPropertyQualifierRenderings_;
    }

    private class PropertyRenderingComparator implements Comparator<PropertyQualifier> {
        public int compare(PropertyQualifier o1, PropertyQualifier o2) {            
            return (o1.getPropertyQualifierName().compareToIgnoreCase(o2.getPropertyQualifierName()));
        }
    }

}