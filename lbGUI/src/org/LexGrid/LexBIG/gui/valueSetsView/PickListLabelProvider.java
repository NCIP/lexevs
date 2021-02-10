
package org.LexGrid.LexBIG.gui.valueSetsView;

import org.LexGrid.valueSets.PickListDefinition;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * Label provider for the Pick List SWT table view.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class PickListLabelProvider implements ITableLabelProvider {

    public Image getColumnImage(Object arg0, int arg1) {
        return null;
    }

    public String getColumnText(Object arg0, int col) {
        if (arg0 instanceof PickListDefinition) {
            PickListDefinition plDef = (PickListDefinition) arg0;
            try {
                switch (col) {
                case 0:
                    return plDef.getPickListId();

                case 1:
                    return plDef.getRepresentsValueSetDefinition();

                case 2:
                    return plDef.getDefaultEntityCodeNamespace();

                case 3:
                    return String.valueOf(plDef.isCompleteSet());
                    
                case 4:
                    return (plDef.getIsActive().toString());
                    
                case 5:
                    return (plDef.getStatus());

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
        tc.setText("Pick List Id");
        tc.setWidth(320);
//        tc.addListener(SWT.Selection, SortListenerFactory.getListener(SortListenerFactory.STRING_COMPARATOR));
        
        tc = new TableColumn(table, SWT.LEFT, 1);
        tc.setText("Represents Value Domain");
        tc.setWidth(320);
//        tc.addListener(SWT.Selection, SortListenerFactory.getListener(SortListenerFactory.STRING_COMPARATOR));

        tc = new TableColumn(table, SWT.LEFT, 2);
        tc.setText("Default EntityCode Namespace");
        tc.setWidth(240);
//        tc.addListener(SWT.Selection, SortListenerFactory.getListener(SortListenerFactory.STRING_COMPARATOR));

        tc = new TableColumn(table, SWT.LEFT, 3);
        tc.setText("Complete Domain");
        tc.setWidth(80);
//        tc.addListener(SWT.Selection, SortListenerFactory.getListener(SortListenerFactory.STRING_COMPARATOR));
        
        tc = new TableColumn(table, SWT.LEFT, 4);
        tc.setText("Active");
        tc.setWidth(80); 
//        tc.addListener(SWT.Selection, SortListenerFactory.getListener(SortListenerFactory.STRING_COMPARATOR));
        
        tc = new TableColumn(table, SWT.LEFT, 5);
        tc.setText("Status");
        tc.setWidth(80);
//        tc.addListener(SWT.Selection, SortListenerFactory.getListener(SortListenerFactory.STRING_COMPARATOR));
    }    
}