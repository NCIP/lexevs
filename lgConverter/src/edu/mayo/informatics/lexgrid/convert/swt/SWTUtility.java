/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package edu.mayo.informatics.lexgrid.convert.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * Methods to simplify building SWT layouts.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 7198 $ checked in on $Date: 2008-02-15
 *          18:08:36 +0000 (Fri, 15 Feb 2008) $
 */
public class SWTUtility {
    public static Label makeLabel(String label, Composite parent, int gridLayoutParam) {
        return makeLabel(label, "", parent, gridLayoutParam, -1);
    }

    public static Label makeLabel(String label, String toolTipText, Composite parent, int gridLayoutParam) {
        return makeLabel(label, toolTipText, parent, gridLayoutParam, -1);
    }

    public static Label makeLabel(String label, String toolTipText, Composite parent, int gridLayoutParam, int widthHint) {
        Label temp = new Label(parent, SWT.NONE);
        temp.setText(label);
        temp.setToolTipText(toolTipText);
        GridData gd = new GridData(gridLayoutParam);
        if (widthHint > 0) {
            gd.widthHint = widthHint;
        }
        temp.setLayoutData(gd);
        return temp;

    }

    public static void position(Control composite, int gridDataParam, int horizontalSpan, int widthHint) {
        GridData temp = new GridData(gridDataParam);
        temp.horizontalSpan = horizontalSpan;
        if (widthHint > 0) {
            temp.widthHint = widthHint;
        }
        composite.setLayoutData(temp);
    }

    public static void position(Control composite, int gridDataParam, int horizontalSpan) {
        position(composite, gridDataParam, horizontalSpan, -1);
    }
}