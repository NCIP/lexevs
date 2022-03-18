
package org.LexGrid.LexBIG.gui.valueSetsView;

import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.EntityReference;
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
public class EntityReferenceLabelProvider implements ITableLabelProvider {
//    private static SimpleDateFormat formatter = new SimpleDateFormat(
//            "h:mm:ss a 'on' MM/dd/yyyy");

    public Image getColumnImage(Object arg0, int arg1) {
        return null;
    }

    public String getColumnText(Object arg0, int col) {
        if (arg0 instanceof DefinitionEntry) {
            DefinitionEntry defEntry = (DefinitionEntry) arg0;
            EntityReference entityRef = defEntry.getEntityReference();
            if (entityRef != null)
                try {
                    switch (col) {
                    case 0:
                        return defEntry.getRuleOrder().toString();
    
                    case 1:
                        return defEntry.getOperator().name();
    
                    case 2:
                        return entityRef.getEntityCode();
                        
                    case 3:
                        return entityRef.getEntityCodeNamespace();
                        
                    case 4:
                        return entityRef.getReferenceAssociation();
                        
                    case 5:
                        return entityRef.getTransitiveClosure().toString();
                        
                    case 6:
                        return entityRef.getTargetToSource().toString();
                        
                    case 7:
                        return entityRef.getLeafOnly().toString();
    
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
        tc.setText("Entity Code");
        tc.setWidth(240); 
        
        tc = new TableColumn(table, SWT.LEFT, 3);
        tc.setText("Entity Code Namespace");
        tc.setWidth(240); 
        
        tc = new TableColumn(table, SWT.LEFT, 4);
        tc.setText("Reference Association");
        tc.setWidth(240); 
        
        tc = new TableColumn(table, SWT.LEFT, 5);
        tc.setText("Transitive Closure");
        tc.setWidth(120); 
        
        tc = new TableColumn(table, SWT.LEFT, 6);
        tc.setText("Target To Source");
        tc.setWidth(120); 
        
        tc = new TableColumn(table, SWT.LEFT, 7);
        tc.setText("Leaf Only");
        tc.setWidth(120); 
    }

}