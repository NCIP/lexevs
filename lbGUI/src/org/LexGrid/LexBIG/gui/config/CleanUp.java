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
package org.LexGrid.LexBIG.gui.config;

import java.util.ArrayList;

import org.LexGrid.LexBIG.Impl.dataAccess.CleanUpUtility;
import org.LexGrid.LexBIG.gui.DialogHandler;
import org.LexGrid.LexBIG.gui.LB_GUI;
import org.LexGrid.LexBIG.gui.Utility;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

/**
 * This GUI allows you to run the LexBIG clean up utility.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class CleanUp {
	DialogHandler dialog_;
	LB_GUI lb_gui_;
	Shell shell_;

	Label status_;
	List orphans_;
	Button remove_;

	private static Logger log = Logger.getLogger("LB_GUI_LOGGER");

	public CleanUp(LB_GUI lb_gui) {
		lb_gui_ = lb_gui;
		shell_ = new Shell(lb_gui_.getShell(), SWT.DIALOG_TRIM | SWT.RESIZE);
		shell_.setSize(640, 480);

		dialog_ = new DialogHandler(shell_);

		shell_.setText("Configure LexBIG");

		init(shell_);

		shell_.open();

		search();
	}

	private void search() {
		orphans_.setItems(new String[] { "Searching, please wait..." });
		Thread temp = new Thread(new Searcher());
		temp.start();
	}

	private class Searcher implements Runnable {

		public void run() {
			try {
				final ArrayList<String> temp = new ArrayList<String>();

				String[] indexes = CleanUpUtility.listUnusedIndexes();

				for (int i = 0; i < indexes.length; i++) {
					temp.add("Index: " + indexes[i]);
				}

				String[] dbs = CleanUpUtility.listUnusedDatabases();
				for (int i = 0; i < dbs.length; i++) {
					temp.add("Database: " + dbs[i]);
				}
				
				String[] md = CleanUpUtility.listUnusedMetadata();
			    for (int i = 0; i < md.length; i++) {
                    temp.add("Metadata: " + md[i]);
                }
				
				
				shell_.getDisplay().syncExec(new Runnable() {
					public void run() {
						orphans_
								.setItems(temp.toArray(new String[temp.size()]));
						remove_.setEnabled(true);
					}
				});

			}

			catch (Exception e) {
				log.error("Problem running cleanup search", e);
				dialog_.showError("Unexpected Error", e.toString());
			}

		}

	}

	private void init(final Shell shell) {
		shell.setLayout(new GridLayout());

		status_ = Utility.makeLabel(shell, "Orphaned Resources");
		orphans_ = new List(shell, SWT.MULTI);
		orphans_.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite buttons = new Composite(shell, SWT.NONE);
		buttons.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		buttons.setLayout(new GridLayout(2, false));
		remove_ = Utility.makeButton("Remove Selected", buttons,
				GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_END);
		remove_.setEnabled(false);

		remove_.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				remove_.setEnabled(false);
				String[] items = orphans_.getSelection();
				final int[] selection = orphans_.getSelectionIndices();
				try {
					for (int i = 0; i < items.length; i++) {
						String temp;
						if (items[i].startsWith("Index: ")) {
							temp = items[i].substring("Index: ".length());
							CleanUpUtility.removeUnusedIndex(temp);

						} else if (items[i].startsWith("Database: ")){
							temp = items[i].substring("Database: ".length());
							CleanUpUtility.removeUnusedDatabase(temp);
						}
						else if (items[i].startsWith("Metadata: ")){
                            temp = items[i].substring("Metadata: ".length());
                            CleanUpUtility.removeUnusedMetaData(temp);
                        }

					}
					shell_.getDisplay().syncExec(new Runnable() {
						public void run() {
							orphans_.remove(selection);
						}
					});
				} catch (Exception e) {
					log.error("Problem running cleanup", e);
					dialog_.showError("Unexpected Error", e.toString());
				}

				remove_.setEnabled(true);
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				// nothing
			}

		});

		Button cancel = Utility.makeButton("Cancel", buttons,
				GridData.HORIZONTAL_ALIGN_END);

		cancel.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				CleanUp.this.shell_.dispose();
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				// 
			}

		});
	}
}