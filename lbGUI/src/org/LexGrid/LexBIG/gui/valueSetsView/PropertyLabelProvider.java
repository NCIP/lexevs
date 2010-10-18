/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.LexGrid.LexBIG.gui.valueSetsView;

import org.LexGrid.commonTypes.Property;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * Label provider for the Value Set Definition Property SWT table view.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class PropertyLabelProvider implements ITableLabelProvider {

    public Image getColumnImage(Object arg0, int arg1) {
        return null;
    }

    public String getColumnText(Object arg0, int col) {
        if (arg0 instanceof Property) {
            Property property = (Property) arg0;
            try {
                switch (col) {
                case 0:
                    return property.getPropertyId() == null ? "" : property.getPropertyId();

                case 1:
                    return property.getPropertyName() == null ? "" : property.getPropertyName();

                case 2:
                    return property.getPropertyType() == null ? "" : property.getPropertyType();
                    
                case 3:
                    return property.getLanguage() == null ? "" : property.getLanguage();
                    
                case 4:
                    return property.getValue() == null ? "" : property.getValue().getContent();
                    
                case 5:
                    return property.getStatus() == null ? "" : property.getStatus();
                    
                case 6:
                    return property.getIsActive() == null ? "" : property.getIsActive().toString();

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
        tc.setText("Property Id");
        tc.setWidth(240);

        tc = new TableColumn(table, SWT.LEFT, 1);
        tc.setText("Property Name");
        tc.setWidth(240);

        tc = new TableColumn(table, SWT.LEFT, 2);
        tc.setText("Property Type");
        tc.setWidth(240); 
        
        tc = new TableColumn(table, SWT.LEFT, 3);
        tc.setText("Language");
        tc.setWidth(240); 
        
        tc = new TableColumn(table, SWT.LEFT, 4);
        tc.setText("Property Value");
        tc.setWidth(240); 
        
        tc = new TableColumn(table, SWT.LEFT, 5);
        tc.setText("Status");
        tc.setWidth(180); 
        
        tc = new TableColumn(table, SWT.LEFT, 6);
        tc.setText("Is Active");
        tc.setWidth(180); 
    }

}