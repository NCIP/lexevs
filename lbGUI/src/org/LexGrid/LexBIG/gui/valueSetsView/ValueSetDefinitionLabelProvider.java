
package org.LexGrid.LexBIG.gui.valueSetsView;

import org.LexGrid.valueSets.ValueSetDefinition;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * Label provider for the Value Set Definition SWT table view.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class ValueSetDefinitionLabelProvider implements ITableLabelProvider {
//    private static SimpleDateFormat formatter = new SimpleDateFormat(
//            "h:mm:ss a 'on' MM/dd/yyyy");

    public Image getColumnImage(Object arg0, int arg1) {
        return null;
    }

    public String getColumnText(Object arg0, int col) {
        if (arg0 instanceof ValueSetDefinition) {
            ValueSetDefinition vdDef = (ValueSetDefinition) arg0;
            try {
                switch (col) {
                case 0:
                    return vdDef.getValueSetDefinitionName();

                case 1:
                    return vdDef.getValueSetDefinitionURI();

                case 2:
                    return vdDef.getDefaultCodingScheme();

                case 3:
                    return vdDef.getConceptDomain();
                    
                case 4:
                    return (vdDef.getIsActive().toString());
                    
                case 5:
                    return (vdDef.getStatus());

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
        tc.setText("Value Domain Name");
        tc.setWidth(340);

        tc = new TableColumn(table, SWT.LEFT, 1);
        tc.setText("Value Domain URI");
        tc.setWidth(340);

        tc = new TableColumn(table, SWT.LEFT, 2);
        tc.setText("Default Coding Scheme");
        tc.setWidth(240);
        
        tc = new TableColumn(table, SWT.LEFT, 3);
        tc.setText("Concept Domain");
        tc.setWidth(240);

        tc = new TableColumn(table, SWT.LEFT, 4);
        tc.setText("Active");
        tc.setWidth(80); 
        
        tc = new TableColumn(table, SWT.LEFT, 5);
        tc.setText("Status");
        tc.setWidth(100); 
    }

}