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

import java.io.File;
import java.net.URI;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.loaders.RadLexProtegeFramesLoaderImpl;
import org.LexGrid.LexBIG.gui.DialogHandler;
import org.LexGrid.LexBIG.gui.LB_GUI;
import org.LexGrid.LexBIG.gui.LoadExportBaseShell;
import org.LexGrid.LexBIG.gui.Utility;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import edu.mayo.informatics.resourcereader.core.StringUtils;

/**
 * GUI to load the RadLex ProtégéFrames representation to LexBIG.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class RadLexProtegeFramesLoader extends LoadExportBaseShell {
	public RadLexProtegeFramesLoader(LB_GUI lb_gui) {
		super(lb_gui);
		try {
			Shell shell = new Shell(lb_gui_.getShell().getDisplay());
			shell.setSize(500, 400);
			shell.setImage(new Image(shell.getDisplay(), this.getClass()
					.getResourceAsStream("/icons/load.gif")));

			dialog_ = new DialogHandler(shell);

			RadLexProtegeFramesLoaderImpl loader = (RadLexProtegeFramesLoaderImpl) lb_gui_
					.getLbs().getServiceManager(null).getLoader(
					        RadLexProtegeFramesLoaderImpl.name);

			shell.setText(loader.getName());

			buildGUI(shell, loader);

			shell.open();

			shell.addShellListener(shellListener);
		} catch (Exception e) {
			dialog_.showError("Unexpected Error", e.toString());
		}

	}

	private void buildGUI(Shell shell, final RadLexProtegeFramesLoaderImpl loader) {
		Group options = new Group(shell, SWT.NONE);
		options.setText("Load Options");
		shell.setLayout(new GridLayout());

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		options.setLayoutData(gd);

		GridLayout layout = new GridLayout(3, false);
		options.setLayout(layout);

		Label fileLabel = new Label(options, SWT.NONE);
		fileLabel.setText("RadLex project file");

		final Text file = new Text(options, SWT.BORDER);
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
		file.setLayoutData(gd);

		Button fileChooseButton = Utility.getFileChooseButton(options, file,
				new String[] { "*.pprj" },
				new String[] { "Protégé project file (pprj)" });

		gd = new GridData(SWT.CENTER);
		gd.widthHint = 60;
		fileChooseButton.setLayoutData(gd);

		Label paramFileLabel = new Label(options, SWT.NONE);
		paramFileLabel.setText("Manifest file (optional)");

		final Text paramFile = new Text(options, SWT.BORDER);
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
		paramFile.setLayoutData(gd);

		Button paramFileChooseButton = Utility.getFileChooseButton(options,
				paramFile, new String[] { "*.xml" },
				new String[] { "Manifest file (xml)" });

		gd = new GridData(SWT.CENTER);
		gd.widthHint = 60;
		paramFileChooseButton.setLayoutData(gd);

		final Button load = new Button(options, SWT.PUSH);
		load.setText("Load");
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_END);
		gd.horizontalSpan = 3;
		gd.widthHint = 60;
		load.setLayoutData(gd);

		load.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {

				URI uri = null;
				// is this a local file?
				File theFile = new File(file.getText());

				if (theFile.exists()) {
					uri = theFile.toURI();
				} else {
					// is it a valid URI (like http://something)
					try {
						uri = new URI(file.getText());
						uri.toURL().openConnection();
					} catch (Exception e) {
						dialog_.showError("Path Error",
								"No file could be located at this location");
						return;
					}
				}

				URI manifestUri = null;
				File theParamFile = new File(paramFile.getText());

				if (theParamFile.exists()) {
					manifestUri = theParamFile.toURI();
				} else {
					// is it a valid URI (like http://something)
					try {
						if (!StringUtils.isNull(paramFile.getText())) {
							manifestUri = new URI(paramFile.getText());
							manifestUri.toURL().openConnection();
						}
					} catch (Exception e) {
						dialog_.showError("Path Error",
								"No file could be located at this location");
						return;
					}
				}

				try {
					setLoading(true);
					loader.setCodingSchemeManifestURI(manifestUri);
					loader.load(uri, false, true);
					load.setEnabled(false);
				}

				catch (LBException e) {
					dialog_.showError("Loader Error", e.toString());
					load.setEnabled(true);
					setLoading(false);
					return;
				}
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				// 
			}

		});

		Composite status = getStatusComposite(shell, loader);
		gd = new GridData(GridData.FILL_BOTH);
		status.setLayoutData(gd);

	}

}