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
package org.LexGrid.LexBIG.gui;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Utility.ObjectToString;
import org.LexGrid.LexBIG.gui.edit.CodingSchemeEditDialog;
import org.LexGrid.LexBIG.gui.edit.EntityEditDialog;
import org.LexGrid.LexBIG.gui.edit.ItemUpdateListener;
import org.LexGrid.codingSchemes.CodingScheme;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;

/**
 * Class for displaying code system details.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class CodeSystemDetails {
	private Shell shell_;
	private StyledText results_;
	private LB_GUI lbGui;

	public CodeSystemDetails(Shell parent, LB_GUI lbGui, CodingScheme codeSystemDetails) {
		shell_ = new Shell(parent.getDisplay());
		shell_.setText("Code System Viewer");
		shell_.setSize(700, 550);
		shell_.setImage(new Image(shell_.getDisplay(), this.getClass()
				.getResourceAsStream("/icons/icon.gif")));

		this.lbGui = lbGui;
		
		buildComponents(codeSystemDetails);

		shell_.setVisible(true);

		results_.setText(ObjectToString.toString(codeSystemDetails));
	}

	public void buildComponents(final CodingScheme codeSystemDetails) {
		shell_.setLayout(new GridLayout(1, true));

		// results area

		results_ = new StyledText(shell_, SWT.WRAP | SWT.BORDER | SWT.MULTI
				| SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL);
		results_.setLayoutData(new GridData(GridData.FILL_BOTH));
		
//		Button editButton = new Button(shell_, SWT.BUTTON1);
//        editButton
//                .setText("Edit Coding Scheme");
//        editButton.addSelectionListener(new SelectionListener() {
//
//            public void widgetDefaultSelected(SelectionEvent arg0) {
//                //
//            }

//            public void widgetSelected(SelectionEvent arg0) {
//                CodingSchemeEditDialog editDialog;
//                try {
//                    editDialog = new CodingSchemeEditDialog(lbGui, null, false, "Edit CodingScheme", shell_, codeSystemDetails, new DialogHandler(shell_));
//                } catch (Exception e) {
//                   throw new RuntimeException(e);
//                }
//                
//                editDialog.addItemUpdateListener(new ItemUpdateListener<CodingScheme>() {
//
//                    public void onItemUpdate(CodingScheme item) {
//                        results_.setText(ObjectToString.toString(item));
//                        lbGui.refreshCodingSchemeList();
//                    }
//                    
//                });
//                editDialog.open();
//            }  
//        });
	}
}