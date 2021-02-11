
package org.LexGrid.LexBIG.gui.valueSetsView;

import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.ValueSetDefinitionReference;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * Label provider for the Value Set Definition Entry SWT table view.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class ValueSetDefReferenceLabelProvider implements ITableLabelProvider {
//    private static SimpleDateFormat formatter = new SimpleDateFormat(
//            "h:mm:ss a 'on' MM/dd/yyyy");

    public Image getColumnImage(Object arg0, int arg1) {
        return null;
    }

    public String getColumnText(Object arg0, int col) {
        if (arg0 instanceof DefinitionEntry) {
            DefinitionEntry defEntry = (DefinitionEntry) arg0;
            ValueSetDefinitionReference vsdRef = defEntry.getValueSetDefinitionReference();
            if (vsdRef != null && vsdRef.getValueSetDefinitionURI() != null)
                try {
                    switch (col) {
                    case 0:
                        return defEntry.getRuleOrder().toString();
    
                    case 1:
                        return defEntry.getOperator().name();
    
                    case 2:
                        return vsdRef.getValueSetDefinitionURI();
    
                    default:
                        return null;
                    }
                } catch (RuntimeException e) {
                    return null;
                }
        }
        return null;
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
        tc.setText("Rule Order");
        tc.setWidth(120);

        tc = new TableColumn(table, SWT.LEFT, 1);
        tc.setText("Operator");
        tc.setWidth(120);

        tc = new TableColumn(table, SWT.LEFT, 2);
        tc.setText("Value Set Definition URI");
        tc.setWidth(440); 
    }

}