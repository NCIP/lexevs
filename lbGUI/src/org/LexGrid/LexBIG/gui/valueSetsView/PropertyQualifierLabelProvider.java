
package org.LexGrid.LexBIG.gui.valueSetsView;

import org.LexGrid.commonTypes.PropertyQualifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * Label provider for the Property Qualifier SWT table view.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class PropertyQualifierLabelProvider implements ITableLabelProvider {

    public Image getColumnImage(Object arg0, int arg1) {
        return null;
    }

    public String getColumnText(Object arg0, int col) {
        if (arg0 instanceof PropertyQualifier) {
            PropertyQualifier propertyQualifier = (PropertyQualifier) arg0;
            try {
                switch (col) {
                case 0:
                    return propertyQualifier.getPropertyQualifierName() == null ? "" : propertyQualifier.getPropertyQualifierName();

                case 1:
                    return propertyQualifier.getPropertyQualifierType() == null ? "" : propertyQualifier.getPropertyQualifierType();

                case 2:
                    return propertyQualifier.getValue() == null ? "" : propertyQualifier.getValue().getContent();

                default:
                    return "";
                }
            } catch (RuntimeException e) {
                return "";
            }
        }
        return "";
    }

    public void addListener(ILabelProviderListener arg0) {
        // do nothing
    }

    public void dispose() {
        // do nothing
    }

    public boolean isLabelProperty(Object arg0, String arg1) {
        return false;
    }

    public void removeListener(ILabelProviderListener arg0) {
        // do nothing
    }

    public void setupColumns(Table table) {
        TableColumn tc = new TableColumn(table, SWT.LEFT, 0);
        tc.setText("Property Qualifier Name");
        tc.setWidth(340);

        tc = new TableColumn(table, SWT.LEFT, 1);
        tc.setText("Property Qualifier Type");
        tc.setWidth(340);
        
        tc = new TableColumn(table, SWT.LEFT, 2);
        tc.setText("Property Qualifier Value");
        tc.setWidth(540);
    }

}