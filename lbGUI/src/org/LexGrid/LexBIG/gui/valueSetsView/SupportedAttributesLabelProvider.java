
package org.LexGrid.LexBIG.gui.valueSetsView;

import java.util.List;

import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedHierarchy;
import org.LexGrid.naming.SupportedNamespace;
import org.LexGrid.naming.SupportedProperty;
import org.LexGrid.naming.SupportedSource;
import org.LexGrid.naming.URIMap;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * Label provider for the Value Set Definition Supported Attributes SWT table view.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class SupportedAttributesLabelProvider implements ITableLabelProvider {

    public Image getColumnImage(Object arg0, int arg1) {
        return null;
    }

    public String getColumnText(Object arg0, int col) {
//        String attribute = "";
        String localId = "";
        String uri = "";
        String assnCS = "";
        String assnNS = "";
        String assnEC = "";
        String csIsImported = "";
        String hAssnName = "";
        String hRootCode = "";
        String hIsForwardNavigable = "";
        String nsEqCS = "";
        String propPropType = "";
        String srcAssmRule = "";
        
        if (arg0 instanceof URIMap) {
            URIMap uriMap = (URIMap) arg0;
            localId = uriMap.getLocalId();
            uri = uriMap.getUri();
            if (uriMap instanceof SupportedAssociation)
            {
                SupportedAssociation suppAssn = (SupportedAssociation) uriMap;
                assnCS = suppAssn.getCodingScheme();
                assnNS = suppAssn.getEntityCodeNamespace();
                assnEC = suppAssn.getEntityCode();
            }
            else if (uriMap instanceof SupportedCodingScheme)
            {
                SupportedCodingScheme suppCS = (SupportedCodingScheme) uriMap;
                csIsImported = String.valueOf(suppCS.getIsImported());
            }
            else if (uriMap instanceof SupportedHierarchy)
            {
                SupportedHierarchy suppH = (SupportedHierarchy) uriMap;
                List<String> assnNames = suppH.getAssociationNamesAsReference();
                for (String assnName : assnNames)
                {
                    hAssnName += assnName + ", ";
                }
                hRootCode = suppH.getRootCode();
                hIsForwardNavigable = String.valueOf(suppH.isIsForwardNavigable());
            }
            else if (uriMap instanceof SupportedNamespace)
            {
                SupportedNamespace suppNS = (SupportedNamespace) uriMap;
                nsEqCS = suppNS.getEquivalentCodingScheme();
            }
            else if (uriMap instanceof SupportedProperty)
            {
                SupportedProperty suppProp = (SupportedProperty) uriMap;
                propPropType = suppProp.getPropertyType() == null? "" : suppProp.getPropertyType().name();
            }
            else if (uriMap instanceof SupportedSource)
            {
                SupportedSource suppS = (SupportedSource) uriMap;
                srcAssmRule = suppS.getAssemblyRule();
            }
            try {
                switch (col) {
                case 0:
                    return uriMap.getClass().getSimpleName();

                case 1:
                    return localId;

                case 2:
                    return uri;
                    
                case 3:
                    return assnCS;
                    
                case 4:
                    return assnNS;
                    
                case 5:
                    return assnEC;
                    
                case 6:
                    return csIsImported;
                    
                case 7:
                    return hAssnName;
                    
                case 8:
                    return hRootCode;
                    
                case 9:
                    return hIsForwardNavigable;
                    
                case 10:
                    return nsEqCS;
                    
                case 11:
                    return propPropType;
                    
                case 12:
                    return srcAssmRule;

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
        tc.setText("Attribute");
        tc.setWidth(240);

        tc = new TableColumn(table, SWT.LEFT, 1);
        tc.setText("Local ID");
        tc.setWidth(240);

        tc = new TableColumn(table, SWT.LEFT, 2);
        tc.setText("URI");
        tc.setWidth(240); 
        
        tc = new TableColumn(table, SWT.LEFT, 3);
        tc.setText("Supported Association Coding Scheme");
        tc.setWidth(240); 
        
        tc = new TableColumn(table, SWT.LEFT, 4);
        tc.setText("Supported Association Namespace");
        tc.setWidth(240); 
        
        tc = new TableColumn(table, SWT.LEFT, 5);
        tc.setText("Supported Association Entity Code");
        tc.setWidth(240); 
        
        tc = new TableColumn(table, SWT.LEFT, 6);
        tc.setText("Supported Coding Scheme Isimported");
        tc.setWidth(240); 
        
        tc = new TableColumn(table, SWT.LEFT, 7);
        tc.setText("Supported Hierarchy Association Name");
        tc.setWidth(240);
        
        tc = new TableColumn(table, SWT.LEFT, 8);
        tc.setText("Supported Hierarchy Root Code");
        tc.setWidth(240);
        
        tc = new TableColumn(table, SWT.LEFT, 9);
        tc.setText("Supported Hierarchy IsForward Navigable");
        tc.setWidth(240);
        
        tc = new TableColumn(table, SWT.LEFT, 10);
        tc.setText("Supported Namespace Equivalent Coding Scheme");
        tc.setWidth(240);
        
        tc = new TableColumn(table, SWT.LEFT, 11);
        tc.setText("Supported Property PropertyType");
        tc.setWidth(240);
        
        tc = new TableColumn(table, SWT.LEFT, 12);
        tc.setText("Supported Source Assembly Rule");
        tc.setWidth(240);
    }

}