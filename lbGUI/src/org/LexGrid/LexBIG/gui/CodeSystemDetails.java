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

import org.LexGrid.LexBIG.Utility.ObjectToString;
import org.LexGrid.codingSchemes.CodingScheme;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

/**
 * Class for displaying code system details.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class CodeSystemDetails {
	private Shell shell_;
	private StyledText codeSystemResults_;
	private StyledText metaDataResults_;

	public CodeSystemDetails(Shell parent, LB_GUI lbGui, CodingScheme codeSystemDetails, String mdResults) {
		shell_ = new Shell(parent.getDisplay());
		shell_.setText("Code System Viewer");
		shell_.setSize(700, 550);
		shell_.setImage(new Image(shell_.getDisplay(), this.getClass()
				.getResourceAsStream("/icons/icon.gif")));
		
		buildComponents(codeSystemDetails, mdResults);

		shell_.setVisible(true);
		codeSystemResults_.setText(ObjectToString.toString(codeSystemDetails));
		metaDataResults_.setText(mdResults != null? mdResults: "This scheme has no user defined Metadata");
		
        StyleRange styleRange = new StyleRange();
        styleRange.start = 0;
        styleRange.length = 14;
        styleRange.fontStyle = SWT.BOLD;
        metaDataResults_.setStyleRange(styleRange);
	}

	public void buildComponents(final CodingScheme codeSystemDetails, final String mdResults) {
	    shell_.setLayout(new FillLayout());
	    final TabFolder tabFolder = new TabFolder(shell_, SWT.NONE);

	    TabItem one = new TabItem(tabFolder, SWT.NONE);
	    one.setText("CODE SYSTEM METADATA");
	    one.setToolTipText("MetaData asserted by this code system");
	    one.setControl(getTabOneControl(tabFolder));

	    TabItem two = new TabItem(tabFolder, SWT.NONE);
	    two.setText("USER DEFINED METADATA");
	    two.setToolTipText("Metadata authored by the code system user");
	    two.setControl(getTabTwoControl(tabFolder ));
	}
	
    private Control getTabOneControl(TabFolder tabFolder) {
        codeSystemResults_ = new StyledText(tabFolder,
                SWT.RESIZE | SWT.BORDER | SWT.MULTI | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL);
        return codeSystemResults_;
    }

    private Control getTabTwoControl(TabFolder tabFolder) {
        metaDataResults_ = new StyledText(tabFolder,
                SWT.RESIZE | SWT.BORDER | SWT.MULTI | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL);
        return metaDataResults_;
    }
}