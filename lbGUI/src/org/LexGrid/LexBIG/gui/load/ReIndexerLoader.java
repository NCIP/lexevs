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
package org.LexGrid.LexBIG.gui.load;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Impl.loaders.IndexLoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.ProcessRunner;
import org.LexGrid.LexBIG.Utility.logging.StatusReporter;
import org.LexGrid.LexBIG.gui.DialogHandler;
import org.LexGrid.LexBIG.gui.LB_GUI;
import org.LexGrid.LexBIG.gui.LoadExportBaseShell;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class ReIndexerLoader extends LoadExportBaseShell {
	public ReIndexerLoader(LB_GUI lb_gui,
			AbsoluteCodingSchemeVersionReference ref) {
		super(lb_gui);
		try {
			Shell shell = new Shell(lb_gui_.getShell().getDisplay());
			shell.setSize(500, 400);
			shell.setImage(new Image(shell.getDisplay(), this.getClass()
					.getResourceAsStream("/icons/load.gif")));

			dialog_ = new DialogHandler(shell);

			shell.setText("Reindexer");
			
			ProcessRunner processRunner = new IndexLoaderImpl();

			buildGUI(shell, processRunner.runProcess(ref, null));

			shell.open();

			shell.addShellListener(shellListener);

			
		} catch (Exception e) {
			dialog_.showError("Unexpected Error", e.toString());
		}

	}

	private void buildGUI(Shell shell, StatusReporter reporter) {
		shell.setLayout(new GridLayout());
		Composite status = getStatusComposite(shell, reporter);
		GridData gd = new GridData(GridData.FILL_BOTH);
		status.setLayoutData(gd);
	}
}