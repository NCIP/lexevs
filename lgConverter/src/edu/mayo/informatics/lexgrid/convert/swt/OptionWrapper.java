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
package edu.mayo.informatics.lexgrid.convert.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import edu.mayo.informatics.lexgrid.convert.formats.Option;

/**
 * A Wrapper for Options which will show them in SWT.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 7198 $ checked in on $Date: 2008-02-15
 *          18:08:36 +0000 (Fri, 15 Feb 2008) $
 */
public class OptionWrapper {
    private Option baseOption_;

    public OptionWrapper(Option baseOption, Composite parent) {
        baseOption_ = baseOption;
        if (baseOption_.getOptionValue() instanceof String) {
            final Text temp = new Text(parent, SWT.BORDER);
            temp.setText((String) baseOption_.getOptionValue());
            temp.setToolTipText(baseOption_.getOptionDescription());

            // listener to put the changes back into the base option
            temp.addModifyListener(new ModifyListener() {
                public void modifyText(ModifyEvent arg0) {
                    baseOption_.setOptionValue(temp.getText());
                }

            });
        } else if (baseOption_.getOptionValue() instanceof Boolean) {
            final Button temp = new Button(parent, SWT.CHECK);
            temp.setSelection(((Boolean) baseOption_.getOptionValue()).booleanValue());
            temp.setToolTipText(baseOption_.getOptionDescription());

            // listener to update the base option whenever the gui changes.
            temp.addSelectionListener(new SelectionListener() {

                public void widgetDefaultSelected(SelectionEvent arg0) {
                }

                public void widgetSelected(SelectionEvent arg0) {
                    baseOption_.setOptionValue(new Boolean(temp.getSelection()));
                }

            });
        } else {
            SWTUtility.makeLabel("Don't know how to display that option yet", parent, SWT.NONE);
        }
    }

    public Object getOptionValue() {
        return baseOption_.getOptionValue();
    }

}