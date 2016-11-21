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

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Properties;
import java.util.prefs.Preferences;

import org.LexGrid.LexBIG.DataModel.Collections.ExtensionDescriptionList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ModuleDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Extensions.Load.Loader;
import org.LexGrid.LexBIG.History.HistoryService;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.exporters.OwlRdfExporterImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexBIG.gui.codeSet.CodeSet;
import org.LexGrid.LexBIG.gui.codeSet.CodedNodeGraph;
import org.LexGrid.LexBIG.gui.codeSet.CodedNodeSet;
import org.LexGrid.LexBIG.gui.codeSystemView.CodeSystemContentProvider;
import org.LexGrid.LexBIG.gui.codeSystemView.CodeSystemLabelProvider;
import org.LexGrid.LexBIG.gui.config.CleanUp;
import org.LexGrid.LexBIG.gui.config.Configure;
import org.LexGrid.LexBIG.gui.displayResults.DisplayCodedNodeSet;
import org.LexGrid.LexBIG.gui.export.ExporterExtensionShell;
import org.LexGrid.LexBIG.gui.export.OBOExport;
import org.LexGrid.LexBIG.gui.history.HistorySearch;
import org.LexGrid.LexBIG.gui.load.LoaderExtensionShell;
import org.LexGrid.LexBIG.gui.load.ManifestLoader;
import org.LexGrid.LexBIG.gui.load.PostProcessorLauncher;
import org.LexGrid.LexBIG.gui.load.ReIndexerLoader;
import org.LexGrid.LexBIG.gui.load.ResolvedValueSetLoader;
import org.LexGrid.LexBIG.gui.logging.LogViewer;
import org.LexGrid.LexBIG.gui.restrictions.GraphRestrictionGUI;
import org.LexGrid.LexBIG.gui.restrictions.RestrictionGUI;
import org.LexGrid.LexBIG.gui.sortOptions.SortOptions;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.system.constants.SystemVariables;
import org.lexevs.system.service.SystemResourceService;
import org.lexevs.system.utility.PropertiesUtility;
import org.lexgrid.loader.ResolvedValueSetDefinitionLoaderImpl;

/**
 * This is the GUI application for the LexBIG project.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class LB_GUI {
	private static Logger log = Logger.getLogger("LB_GUI_LOGGER");
	protected Shell shell_;
	private DialogHandler errorHandler;
	private Composite codeSystemComposite_, setComposite_,
			restrictionComposite_;
	private TableViewer codeSystemsTV_;
	private Properties currentProperties_;
	private List codeSetsList_, restrictionsList_;
	private Button union, intersection, diff, remove, addRestriction,
			editRestriction, removeRestriction, restrictToCodes,
			restrictToSourceCodes, restrictToTargetCodes, lgExport; // mct
	private StackLayout codeSetSummaryStack;
	private Composite codeSetSummary, codeGraphSummary,
			noCodeSetChoosenSummary, codeSetOrGraphSummary;

	Label fullCodeSetDescription, fullCodeSetGraphDescription;
	Button resolveForward, resolveBackward;
	Text resolveDepth_, graphFocus_, resolveMax_;
	Combo graphFocusCS_, graphFocusNS_, relationName_;
	MenuItem enableAdmin_, loadItem_, exportItem_, cleanUpItem_;
	Button changeTag_, activate_, deactivate_, removeCodeSystem_,
			removeCodeSystemHistory_, removeCodeSystemMetadata_, rebuildIndex_, loadManifest_;

	private ArrayList<CodeSet> codeSets;
	private LogViewer logViewer_;
	private LexBIGService lbs_;

	private boolean isAdminEnabled = true;
	
	public static int MAX_PAGE_SIZE = 10;

	public static void main(String[] args) throws LBInvocationException {
		new LB_GUI(args);
	}

	/**
	 * Instantiates a new GUI with the given command arguments and default
	 * LexBIGService.
	 * 
	 * @param args
	 *            Recognized arguments: -d, Disables admin options.
	 * @throws LBInvocationException
	 */
	public LB_GUI(String[] args) throws LBInvocationException {
		this(args, null);
	}

	/**
	 * Instantiates a new GUI with the given command arguments and the given
	 * LexBIGService.
	 * 
	 * @param args
	 *            Recognized arguments: -d, Disables admin options.
	 * @param service
	 *            The LexBIGService to invoke for GUI functions.
	 * @throws LBInvocationException
	 */
	public LB_GUI(String[] args, LexBIGService service)
			throws LBInvocationException {
		isAdminEnabled = !(ArrayUtils.contains(args, "-d") || ArrayUtils
				.contains(args, "-D"));
		lbs_ = service != null ? service : LexBIGServiceImpl.defaultInstance();
		Display display = new Display();
		shell_ = new Shell(display);
		shell_.setText("LexBIG  " + Constants.version);
		init();

		setSizeFromPreviousRun();
		// shell_.setMaximized(true);
		shell_.open();
		try {
			while (!shell_.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
		} catch (RuntimeException e) {
			System.err.println(e);
			e.printStackTrace();
			errorHandler.showError("ERROR",
					"Unhandled error, see log for details - exiting.");

		}
		display.dispose();
		System.exit(0);
	}

	private void setSizeFromPreviousRun() {
		Preferences p = Preferences.systemNodeForPackage(this.getClass());
		int height = p.getInt("console_height", -1);
		int width = p.getInt("console_width", -1);
		if (height < 100 || width < 100) {
			shell_.setMaximized(true);
			return;
		}

		shell_.setSize(width, height);

		int locX = p.getInt("console_loc_x", 0);
		int locY = p.getInt("console_loc_y", 0);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		if (locX < 0 || locX > d.width - 200) {
			locX = 0;
		}
		if (locY < 0 || locY > d.height - 150) {
			locY = 0;
		}
		shell_.setLocation(locX, locY);
	}

	private void init() throws LBInvocationException {
		try {
			logViewer_ = new LogViewer(shell_);
		} catch (Exception e1) {
			log.error("There was a problem starting the log viewer", e1);
			System.err.println(e1);
		}
		codeSets = new ArrayList<CodeSet>();

		shell_.addShellListener(new ShellAdapter() {
			public void shellClosed(ShellEvent e) {
				/*
				 * Save the size and location of the main window.
				 */
				int width = shell_.getSize().x;
				int height = shell_.getSize().y;
				int locX = shell_.getLocation().x;
				int locY = shell_.getLocation().y;

				Preferences p = Preferences.systemNodeForPackage(LB_GUI.this
						.getClass());
				p.putInt("console_width", width);
				p.putInt("console_height", height);
				p.putInt("console_loc_x", locX);
				p.putInt("console_loc_y", locY);
			}
		});
		GridLayout layout = new GridLayout(1, true);
		shell_.setLayout(layout);

		shell_.setImage(new Image(shell_.getDisplay(), this.getClass()
				.getResourceAsStream("/icons/icon.gif")));

		errorHandler = new DialogHandler(shell_);

		SashForm topBottom = new SashForm(shell_, SWT.VERTICAL);
		topBottom.SASH_WIDTH = 5;
		topBottom.setLayout(new GridLayout());
		GridData gd = new GridData(GridData.FILL_BOTH);
		topBottom.setLayoutData(gd);
		topBottom.setVisible(true);

		// topBottom.setWeights(new int[] {5, 5});
		buildCodeSystemComposite(topBottom);

		SashForm leftRightBottom = new SashForm(topBottom, SWT.HORIZONTAL);
		leftRightBottom.SASH_WIDTH = 5;

		buildSetComposite(leftRightBottom);
		buildRestrictionComposite(leftRightBottom);

		buildMenus();

		PropertiesUtility.systemVariable = "LG_CONFIG_FILE";
		String filePath = PropertiesUtility.locatePropFile(
				"config/" + SystemVariables.CONFIG_FILE_NAME, SystemResourceService.class.getName());
		if (filePath != null) {
			File file = new File(filePath);
			if (file.exists()) {
				refreshCodingSchemeList();
				try {
					PropertiesUtility.propertiesLocationKey = "CONFIG_FILE_LOCATION";
					currentProperties_ = PropertiesUtility
							.loadPropertiesFromFileOrURL(file.getAbsolutePath());
				} catch (IOException e) {
					log.error("Unexpected Error", e);
				}
			}
		} else {
			new Configure(LB_GUI.this, currentProperties_);
		}

	}

	private void buildCodeSystemComposite(Composite holder) {
		codeSystemComposite_ = new Composite(holder, SWT.BORDER);

		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		codeSystemComposite_.setLayout(new GridLayout(2, false));
		codeSystemComposite_.setLayoutData(gd);

		Utility.makeBoldLabel(codeSystemComposite_, 2,
				GridData.HORIZONTAL_ALIGN_CENTER, "Available Code Systems");

		gd = new GridData(GridData.FILL_BOTH);
		gd.verticalSpan = 14;
		codeSystemsTV_ = new TableViewer(codeSystemComposite_, SWT.BORDER
				| SWT.SINGLE | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		codeSystemsTV_.getTable().setLayoutData(gd);

		codeSystemsTV_.setContentProvider(new CodeSystemContentProvider(this));
		CodeSystemLabelProvider cslp = new CodeSystemLabelProvider();
		codeSystemsTV_.setLabelProvider(cslp);

		codeSystemsTV_.setUseHashlookup(true);
		codeSystemsTV_.getTable().setHeaderVisible(true);
		codeSystemsTV_.getTable().setLayoutData(gd);
		codeSystemsTV_.getTable().setLinesVisible(true);

		cslp.setupColumns(codeSystemsTV_.getTable());
		codeSystemsTV_.setInput("");

		codeSystemsTV_
				.addSelectionChangedListener(new ISelectionChangedListener() {

					public void selectionChanged(SelectionChangedEvent arg0) {
						updateButtonStates();
					}

				});

		codeSystemsTV_.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent arg0) {
				try {
					displayCodeSystemDetails();
				} catch (LBException e) {
					errorHandler.showError("Resolve Problem", e.toString());
				}
			}

		});

		Button getCodeSet = Utility.makeButton("Get Code Set",
				codeSystemComposite_, GridData.VERTICAL_ALIGN_BEGINNING
						| GridData.FILL_HORIZONTAL);
		getCodeSet.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {

				CodingSchemeRendering csr = getSelectedCodeSystemRendering();

				if (csr == null) {
					errorHandler.showError("No coding scheme selected",
							"You must select a coding scheme first.");
					return;
				}

				codeSets.add(new CodedNodeSet(csr));
				codeSetsList_.add(codeSets.get(codeSets.size() - 1).toString());

			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				// 
			}

		});

		Button getCodeGraph = Utility.makeButton("Get Code Graph",
				codeSystemComposite_, GridData.VERTICAL_ALIGN_BEGINNING
						| GridData.FILL_HORIZONTAL);
		getCodeGraph.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {

				CodingSchemeRendering csr = getSelectedCodeSystemRendering();

				if (csr == null) {
					errorHandler.showError("No coding scheme selected",
							"You must select a coding scheme first.");
					return;
				}

				codeSets.add(new CodedNodeGraph(csr));
				codeSetsList_.add(codeSets.get(codeSets.size() - 1).toString());

			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				// 
			}

		});

		Button getHistory = Utility.makeButton("Get History",
				codeSystemComposite_, GridData.VERTICAL_ALIGN_BEGINNING
						| GridData.FILL_HORIZONTAL);
		getHistory.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {

				CodingSchemeRendering csr = getSelectedCodeSystemRendering();

				if (csr == null) {
					errorHandler.showError("No coding scheme selected",
							"You must select a coding scheme first.");
					return;
				}

				String urn = csr.getCodingSchemeSummary().getCodingSchemeURI();
				try {
					HistoryService hs = lbs_.getHistoryService(urn);
					new HistorySearch(LB_GUI.this.shell_, hs, urn);
				} catch (LBException e) {
					errorHandler
							.showError("History not available",
									"History is not available for the selected code system.");
					return;
				}

			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				// 
			}

		});

		Button refresh = Utility.makeButton("Refresh", codeSystemComposite_,
				GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL);
		refresh.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				try {
				    LexEvsServiceLocator.getInstance().getSystemResourceService().refresh();
					LB_GUI.this.refreshCodingSchemeList();
				} catch (Exception e) {
					errorHandler.showError("Error during refresh", e
							.getMessage());
				}
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				// 
			}

		});
		
		gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		Label separator = new Label(codeSystemComposite_, SWT.SEPARATOR
				| SWT.HORIZONTAL);
		separator.setLayoutData(gd);

		if (isAdminEnabled) {// begin Admin enabled conditional

			loadManifest_ = Utility.makeButton("Load Manifest", codeSystemComposite_,
					GridData.VERTICAL_ALIGN_BEGINNING
							| GridData.FILL_HORIZONTAL);
			loadManifest_.setEnabled(false);
			loadManifest_.addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent arg0) {
		
						AbsoluteCodingSchemeVersionReference acsvr = getSelectedCodeSystem();
						if (acsvr == null) {
							errorHandler.showError("No coding scheme selected",
									"You must select a coding scheme first.");
							return;
						}
						
		        new ManifestLoader(LB_GUI.this, acsvr);
	

				}
				public void widgetDefaultSelected(SelectionEvent arg0) {
					// 
				}

			});
			
			changeTag_ = Utility.makeButton("Change Tag", codeSystemComposite_,
                    GridData.VERTICAL_ALIGN_BEGINNING
                            | GridData.FILL_HORIZONTAL);
            changeTag_.setEnabled(false);
            changeTag_.addSelectionListener(new SelectionListener() {

                public void widgetSelected(SelectionEvent arg0) {
                    try {
                        AbsoluteCodingSchemeVersionReference acsvr = getSelectedCodeSystem();

                        if (acsvr == null) {
                            errorHandler.showError("No coding scheme selected",
                                    "You must select a coding scheme first.");
                            return;
                        }

                        InputDialog id = new InputDialog(LB_GUI.this.shell_,
                                "Change Tag", "Please enter the new tag", "",
                                null);
                        id.setBlockOnOpen(true);
                        id.open();
                        if (id.getReturnCode() == Dialog.OK) {
                            LB_GUI.this.lbs_.getServiceManager(null)
                                    .setVersionTag(acsvr, id.getValue());
                            LB_GUI.this.refreshCodingSchemeList();
                        }

                    } catch (LBException e) {
                        errorHandler.showError("Error executing change tag", e
                                .getMessage());
                    }
                }
                public void widgetDefaultSelected(SelectionEvent arg0) {
                    // 
                }

            });
			
			activate_ = Utility.makeButton("Activate", codeSystemComposite_,
					GridData.VERTICAL_ALIGN_BEGINNING
							| GridData.FILL_HORIZONTAL);
			activate_.setEnabled(false);

			activate_.addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent arg0) {
					try {
						AbsoluteCodingSchemeVersionReference acsvr = getSelectedCodeSystem();

						if (acsvr == null) {
							errorHandler.showError("No coding scheme selected",
									"You must select a coding scheme first.");
							return;
						}
						LB_GUI.this.lbs_.getServiceManager(null)
								.activateCodingSchemeVersion(acsvr);
						LB_GUI.this.refreshCodingSchemeList();
					} catch (LBException e) {
						errorHandler.showError("Error executing activate", e
								.getMessage());
					}

				}

				public void widgetDefaultSelected(SelectionEvent arg0) {
					// 
				}

			});

			// TODO - why won't webstart connect to network servers?

			deactivate_ = Utility.makeButton("Deactivate",
					codeSystemComposite_, GridData.VERTICAL_ALIGN_BEGINNING
							| GridData.FILL_HORIZONTAL);
			deactivate_.setEnabled(false);
			deactivate_.addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent arg0) {
					try {
						AbsoluteCodingSchemeVersionReference acsvr = getSelectedCodeSystem();

						if (acsvr == null) {
							errorHandler.showError("No coding scheme selected",
									"You must select a coding scheme first.");
							return;
						}
						// TODO [feature] support dated deactivations
						LB_GUI.this.lbs_.getServiceManager(null)
								.deactivateCodingSchemeVersion(acsvr, null);
						LB_GUI.this.refreshCodingSchemeList();
					} catch (LBException e) {
						errorHandler.showError("Error executing deactivate", e
								.getMessage());
					}

				}

				public void widgetDefaultSelected(SelectionEvent arg0) {
					//
				}

			});

			removeCodeSystem_ = Utility.makeButton("Remove",
					codeSystemComposite_, GridData.VERTICAL_ALIGN_BEGINNING
							| GridData.FILL_HORIZONTAL);
			removeCodeSystem_.setEnabled(false);
			removeCodeSystem_.addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent arg0) {
					try {
						AbsoluteCodingSchemeVersionReference acsvr = getSelectedCodeSystem();

						if (acsvr == null) {
							errorHandler.showError("No coding scheme selected",
									"You must select a coding scheme first.");
							return;
						}

						MessageBox messageBox = new MessageBox(shell_,
								SWT.ICON_QUESTION | SWT.YES | SWT.NO);
						messageBox.setText("Remove code system?");
						messageBox
								.setMessage("Do you really want to remove the selected code system?");
						if (messageBox.open() == SWT.YES) {
							LB_GUI.this.lbs_.getServiceManager(null)
									.removeCodingSchemeVersion(acsvr);
							LB_GUI.this.refreshCodingSchemeList();
						}
					} catch (LBException e) {
						errorHandler.showError("Error executing remove", e
								.getMessage());
					}
				}

				public void widgetDefaultSelected(SelectionEvent arg0) {
					//
				}

			});

			removeCodeSystemHistory_ = Utility.makeButton("Remove History",
					codeSystemComposite_, GridData.VERTICAL_ALIGN_BEGINNING
							| GridData.FILL_HORIZONTAL);
			removeCodeSystemHistory_.setEnabled(false);
			removeCodeSystemHistory_
					.addSelectionListener(new SelectionListener() {

						public void widgetSelected(SelectionEvent arg0) {
							try {
								AbsoluteCodingSchemeVersionReference acsvr = getSelectedCodeSystem();

								if (acsvr == null) {
									errorHandler
											.showError(
													"No coding scheme selected",
													"You must select a coding scheme first.");
									return;
								}

								MessageBox messageBox = new MessageBox(shell_,
										SWT.ICON_QUESTION | SWT.YES | SWT.NO);
								messageBox
										.setText("Remove code system history?");
								messageBox
										.setMessage("Do you really want to remove the history for the selected code system?");
								if (messageBox.open() == SWT.YES) {
									LB_GUI.this.lbs_.getServiceManager(null)
											.removeHistoryService(
													acsvr.getCodingSchemeURN());
									LB_GUI.this.refreshCodingSchemeList();
								}
							} catch (LBException e) {
								errorHandler.showError(
										"Error executing remove", e
												.getMessage());
							}
						}

						public void widgetDefaultSelected(SelectionEvent arg0) {
							//
						}

					});

			removeCodeSystemMetadata_ = Utility.makeButton("Remove Metadata",
					codeSystemComposite_, GridData.VERTICAL_ALIGN_BEGINNING
							| GridData.FILL_HORIZONTAL);
			removeCodeSystemMetadata_.setEnabled(false);
			removeCodeSystemMetadata_
					.addSelectionListener(new SelectionListener() {
						public void widgetSelected(SelectionEvent arg0) {
							try {
								AbsoluteCodingSchemeVersionReference acsvr = getSelectedCodeSystem();
								if (acsvr == null) {
									errorHandler
											.showError(
													"No coding scheme selected",
													"You must select a coding scheme first.");
									return;
								}
								if (!MessageDialog
										.openQuestion(
												LB_GUI.this.shell_,
												"Remove Metadata?",
												"Do you really want to remove optional metadata loaded for the selected code system?"))
									return;
								LB_GUI.this.lbs_.getServiceManager(null)
										.removeCodingSchemeVersionMetaData(
												acsvr);
							} catch (LBException e) {
								errorHandler.showError(
										"Error executing remove", e
												.getMessage());
							}
						}

						public void widgetDefaultSelected(SelectionEvent arg0) {
							//
						}
					});

			rebuildIndex_ = Utility.makeButton("Rebuild Index",
					codeSystemComposite_, GridData.VERTICAL_ALIGN_BEGINNING
							| GridData.FILL_HORIZONTAL);
			rebuildIndex_.setEnabled(false);
			rebuildIndex_.addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent arg0) {
					AbsoluteCodingSchemeVersionReference acsvr = getSelectedCodeSystem();

					if (acsvr == null) {
					    if (acsvr == null) {
                            errorHandler.showError("No coding scheme selected",
                                    "You must select a coding scheme first.");
                            return;
						}
					} else {
						boolean ans = MessageDialog.openQuestion(
								LB_GUI.this.shell_, "Reindex Code System?",
								"Do you really want to reindex the code system "
										+ acsvr.getCodingSchemeURN() + "?");
						if (!ans) {
							return;
						}
					}
					new ReIndexerLoader(LB_GUI.this, acsvr);
				}

				public void widgetDefaultSelected(SelectionEvent arg0) {
					//
				}

			});

			
			Button runPostProcessor = Utility.makeButton("Run Post Processor",
                    codeSystemComposite_, GridData.VERTICAL_ALIGN_BEGINNING
                            | GridData.FILL_HORIZONTAL);
			
			runPostProcessor.setEnabled(true);
			
			runPostProcessor.addSelectionListener(new SelectionListener() {

                public void widgetSelected(SelectionEvent arg0) {
                    AbsoluteCodingSchemeVersionReference acsvr = getSelectedCodeSystem();
                    
                    if (acsvr == null) {
                        errorHandler.showError("No coding scheme selected",
                                "You must select a coding scheme first.");
                        return;
                    }

                    new PostProcessorLauncher(LB_GUI.this, acsvr);
                }

                public void widgetDefaultSelected(SelectionEvent arg0) {
                    //
                }
            });

		}// end Admin enabled conditional
	}

	private AbsoluteCodingSchemeVersionReference getSelectedCodeSystem() {
		TableItem[] temp = codeSystemsTV_.getTable().getSelection();
		if (temp.length == 1) {
			return ConvenienceMethods
					.createAbsoluteCodingSchemeVersionReference(((CodingSchemeRendering) temp[0]
							.getData()).getCodingSchemeSummary());
		}
		return null;
	}

	private CodingSchemeRendering getSelectedCodeSystemRendering() {
		TableItem[] temp = codeSystemsTV_.getTable().getSelection();
		if (temp.length == 1) {
			return ((CodingSchemeRendering) temp[0].getData());
		}
		return null;
	}

	private CodingSchemeVersionStatus getSelectedCodeSystemActiveState() {
		TableItem[] temp = codeSystemsTV_.getTable().getSelection();
		if (temp.length == 1) {
			return ((CodingSchemeRendering) temp[0].getData())
					.getRenderingDetail().getVersionStatus() == null ? CodingSchemeVersionStatus.INACTIVE
					: ((CodingSchemeRendering) temp[0].getData())
							.getRenderingDetail().getVersionStatus();
		} else {
			return null;
		}
	}

	public void refreshCodingSchemeList() {
		shell_.getDisplay().syncExec(new Runnable() {
			public void run() {
				AbsoluteCodingSchemeVersionReference temp = getSelectedCodeSystem();
				codeSystemsTV_.setInput("");
				// reselect previous selection
				if (temp != null) {
					TableItem[] ti = codeSystemsTV_.getTable().getItems();
					for (int i = 0; i < ti.length; i++) {
						if (((CodingSchemeRendering) ti[i].getData())
								.getCodingSchemeSummary().getCodingSchemeURI()
								.equals(temp.getCodingSchemeURN())
								&& ((CodingSchemeRendering) ti[i].getData())
										.getCodingSchemeSummary()
										.getRepresentsVersion().equals(
												temp.getCodingSchemeVersion())) {
							codeSystemsTV_.getTable().select(i);
							updateButtonStates();
							break;
						}
					}
				}
			}
		});
	}

	private void buildSetComposite(Composite holder) {
		setComposite_ = new Composite(holder, SWT.BORDER);
		setComposite_.setLayout(new GridLayout(2, false));

		Utility.makeBoldLabel(setComposite_, 2,
				GridData.HORIZONTAL_ALIGN_CENTER,
				"Selected CodedNodeSets and CodedNodeGraphs");

		codeSetsList_ = new List(setComposite_, SWT.MULTI | SWT.BORDER
				| SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		codeSetsList_.setLayoutData(gd);
		codeSetsList_.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				int[] selectedItems = codeSetsList_.getSelectionIndices();

				if (selectedItems.length > 2) {
					while (selectedItems.length > 2) {
						codeSetsList_.deselect(selectedItems[0]);
						selectedItems = codeSetsList_.getSelectionIndices();
					}
					errorHandler.showWarning("Too many items selected",
							"You can only select two items at a time.");
				}

				if (selectedItems.length == 0) {
					union.setEnabled(false);
					intersection.setEnabled(false);
					diff.setEnabled(false);
					remove.setEnabled(false);
					restrictToCodes.setEnabled(false);
					restrictToSourceCodes.setEnabled(false);
					restrictToTargetCodes.setEnabled(false);

				} else if (selectedItems.length == 1) {
					union.setEnabled(false);
					intersection.setEnabled(false);
					diff.setEnabled(false);
					remove.setEnabled(true);
					restrictToCodes.setEnabled(false);
					restrictToSourceCodes.setEnabled(false);
					restrictToTargetCodes.setEnabled(false);
					lgExport.setEnabled(true); // mct

				} else if (selectedItems.length == 2) {
					if (codeSets.get(selectedItems[0]) instanceof CodedNodeGraph
							&& codeSets.get(selectedItems[1]) instanceof CodedNodeGraph) {
						// can't diff to graphs
						union.setEnabled(true);
						intersection.setEnabled(true);
						diff.setEnabled(false);
						restrictToCodes.setEnabled(false);
						restrictToSourceCodes.setEnabled(false);
						restrictToTargetCodes.setEnabled(false);
					}

					else if (codeSets.get(selectedItems[0]) instanceof CodedNodeSet
							&& codeSets.get(selectedItems[1]) instanceof CodedNodeSet) {
						union.setEnabled(true);
						intersection.setEnabled(true);
						diff.setEnabled(true);
						restrictToCodes.setEnabled(false);
						restrictToSourceCodes.setEnabled(false);
						restrictToTargetCodes.setEnabled(false);
					}

					else if ((codeSets.get(selectedItems[0]) instanceof CodedNodeSet && codeSets
							.get(selectedItems[1]) instanceof CodedNodeGraph)
							|| (codeSets.get(selectedItems[1]) instanceof CodedNodeSet && codeSets
									.get(selectedItems[0]) instanceof CodedNodeGraph)) {
						union.setEnabled(false);
						intersection.setEnabled(false);
						diff.setEnabled(false);
						restrictToCodes.setEnabled(true);
						restrictToSourceCodes.setEnabled(true);
						restrictToTargetCodes.setEnabled(true);
						lgExport.setEnabled(true); // mct
					}

					remove.setEnabled(false);

				}
				updateRestrictionsView();
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				// 
			}

		});

		Composite buttons = new Composite(setComposite_, SWT.BORDER);
		buttons.setLayout(new GridLayout());
		gd = new GridData(GridData.FILL_VERTICAL);
		buttons.setLayoutData(gd);

		union = Utility.makeButton("Union", buttons, GridData.FILL_HORIZONTAL);
		union.setEnabled(false);
		union.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				// union can't be pressed unless two code sets (or two code
				// graphs) are selected

				int[] selection = codeSetsList_.getSelectionIndices();
				if (codeSets.get(selection[0]) instanceof CodedNodeSet) {
					codeSets.add(new CodedNodeSet((CodedNodeSet) codeSets
							.get(selection[0]), (CodedNodeSet) codeSets
							.get(selection[1]), CodeSet.UNION));
					codeSetsList_.add(codeSets.get(codeSets.size() - 1)
							.toString());
				} else {
					codeSets.add(new CodedNodeGraph((CodedNodeGraph) codeSets
							.get(selection[0]), (CodedNodeGraph) codeSets
							.get(selection[1]), CodeSet.UNION));
					codeSetsList_.add(codeSets.get(codeSets.size() - 1)
							.toString());
				}

			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				// 
			}

		});

		intersection = Utility.makeButton("Intersection", buttons,
				GridData.FILL_HORIZONTAL);
		intersection.setEnabled(false);
		intersection.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				// intersection can't be pressed unless two code sets (or two
				// code graphs) are selected

				int[] selection = codeSetsList_.getSelectionIndices();
				if (codeSets.get(selection[0]) instanceof CodedNodeSet) {
					codeSets.add(new CodedNodeSet((CodedNodeSet) codeSets
							.get(selection[0]), (CodedNodeSet) codeSets
							.get(selection[1]), CodeSet.INTERSECTION));
					codeSetsList_.add(codeSets.get(codeSets.size() - 1)
							.toString());
				} else {
					codeSets.add(new CodedNodeGraph((CodedNodeGraph) codeSets
							.get(selection[0]), (CodedNodeGraph) codeSets
							.get(selection[1]), CodeSet.INTERSECTION));
					codeSetsList_.add(codeSets.get(codeSets.size() - 1)
							.toString());
				}

			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				// 
			}

		});
		diff = Utility.makeButton("Difference", buttons,
				GridData.FILL_HORIZONTAL);
		diff.setEnabled(false);
		diff.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				// difference can't be pressed unless two code sets (or two code
				// graphs) are selected

				int[] selection = codeSetsList_.getSelectionIndices();
				if (codeSets.get(selection[0]) instanceof CodedNodeSet) {
					codeSets.add(new CodedNodeSet((CodedNodeSet) codeSets
							.get(selection[0]), (CodedNodeSet) codeSets
							.get(selection[1]), CodeSet.DIFFERENCE));
					codeSetsList_.add(codeSets.get(codeSets.size() - 1)
							.toString());
				} else {
					// diff isn't allowed on graphs.
				}

			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				// 
			}

		});
		Utility.makeLabel(buttons, "");

		restrictToCodes = Utility.makeButton("Restrict to Codes", buttons,
				GridData.FILL_HORIZONTAL);
		restrictToCodes.setEnabled(false);
		restrictToCodes.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				// this button can only be pressed if there is a code set and a
				// code graph selected
				int[] selection = codeSetsList_.getSelectionIndices();
				CodedNodeSet cs;
				CodedNodeGraph cg;
				if (codeSets.get(selection[0]) instanceof CodedNodeSet) {
					cs = (CodedNodeSet) codeSets.get(selection[0]);
					cg = (CodedNodeGraph) codeSets.get(selection[1]);
				} else {
					cs = (CodedNodeSet) codeSets.get(selection[1]);
					cg = (CodedNodeGraph) codeSets.get(selection[0]);
				}
				codeSets.add(new CodedNodeGraph(cg, cs,
						CodeSet.RESTRICT_TO_CODES));
				codeSetsList_.add(codeSets.get(codeSets.size() - 1).toString());
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				//
			}

		});

		restrictToSourceCodes = Utility.makeButton("Rst to Source Codes",
				buttons, GridData.FILL_HORIZONTAL);
		restrictToSourceCodes.setEnabled(false);
		restrictToSourceCodes.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				// this button can only be pressed if there is a code set and a
				// code graph selected
				int[] selection = codeSetsList_.getSelectionIndices();
				CodedNodeSet cs;
				CodedNodeGraph cg;
				if (codeSets.get(selection[0]) instanceof CodedNodeSet) {
					cs = (CodedNodeSet) codeSets.get(selection[0]);
					cg = (CodedNodeGraph) codeSets.get(selection[1]);
				} else {
					cs = (CodedNodeSet) codeSets.get(selection[1]);
					cg = (CodedNodeGraph) codeSets.get(selection[0]);
				}
				codeSets.add(new CodedNodeGraph(cg, cs,
						CodeSet.RESTRICT_TO_SOURCE_CODES));
				codeSetsList_.add(codeSets.get(codeSets.size() - 1).toString());
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				//
			}

		});

		restrictToTargetCodes = Utility.makeButton("Rst to Target Codes",
				buttons, GridData.FILL_HORIZONTAL);
		restrictToTargetCodes.setEnabled(false);
		restrictToTargetCodes.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				// this button can only be pressed if there is a code set and a
				// code graph selected
				int[] selection = codeSetsList_.getSelectionIndices();
				CodedNodeSet cs;
				CodedNodeGraph cg;
				if (codeSets.get(selection[0]) instanceof CodedNodeSet) {
					cs = (CodedNodeSet) codeSets.get(selection[0]);
					cg = (CodedNodeGraph) codeSets.get(selection[1]);
				} else {
					cs = (CodedNodeSet) codeSets.get(selection[1]);
					cg = (CodedNodeGraph) codeSets.get(selection[0]);
				}
				codeSets.add(new CodedNodeGraph(cg, cs,
						CodeSet.RESTRICT_TO_TARGET_CODES));
				codeSetsList_.add(codeSets.get(codeSets.size() - 1).toString());
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				//
			}

		});

		Utility.makeLabel(buttons, "");

		remove = Utility
				.makeButton("Remove", buttons, GridData.FILL_HORIZONTAL);
		remove.setEnabled(false);
		remove.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				int[] selectedItems = codeSetsList_.getSelectionIndices();
				for (int i = selectedItems.length - 1; i >= 0; i--) {
					codeSetsList_.remove(selectedItems[i]);
					codeSets.remove(selectedItems[i]);
				}
				updateRestrictionsView();
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				//
			}

		});
		
		// mct: XML Export button
		Utility.makeLabel(buttons, "");
        lgExport = Utility
        .makeButton("LgExport", buttons, GridData.FILL_HORIZONTAL);
        lgExport.setEnabled(false);
        lgExport.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                int[] selection = codeSetsList_.getSelectionIndices();
                if (selection.length > 1) {
                    while (selection.length > 1) {
                        codeSetsList_.deselect(selection[0]);
                        selection = codeSetsList_.getSelectionIndices();
                    }
                    errorHandler.showWarning("Too many items selected",
                            "You can only select one item at a time.");
                }
                                
                CodedNodeSet cns;
                CodedNodeGraph cng;
                if (codeSets.get(selection[0]) instanceof CodedNodeSet) {
                    cns = (CodedNodeSet) codeSets.get(selection[0]);
                    cng = null;
                } else {
                    cns = null;
                    cng = (CodedNodeGraph) codeSets.get(selection[0]);
                }
                System.out.println("LgExport button pressed.");
                
                AbsoluteCodingSchemeVersionReference acsvr = getSelectedCodeSystem();
                org.LexGrid.LexBIG.Impl.exporters.LexGridExport exporter;
                try {
                    exporter = (org.LexGrid.LexBIG.Impl.exporters.LexGridExport)lbs_.getServiceManager(null).getExporter(org.LexGrid.LexBIG.Impl.exporters.LexGridExport.name);
                } catch (LBException e) {
                    throw new RuntimeException(e);
                }
                try {
                    if(cng != null) { 
                        exporter.setCng(cng.getRealCodedNodeGraph(getLbs()));
                    } else {
                        exporter.setCng(null);
                    }
                    
                    if(cns != null) {
                        exporter.setCns(cns.getRealCodedNodeSet(getLbs()));
                    } else {
                        exporter.setCns(null);
                    }
                    
                } catch (LBException e) {
                    e.printStackTrace();
                }
                
                new ExporterExtensionShell(LB_GUI.this, exporter, acsvr);
                
                
                // need to get a file output dialog
//                LexGridExportAlternateLauncher launcher = new LexGridExportAlternateLauncher("asdfA");
//                launcher.go();
                
                
                
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                //
            }

        });
		

	}

	private void buildRestrictionComposite(Composite holder) {
		restrictionComposite_ = new Composite(holder, SWT.BORDER);
		restrictionComposite_.setLayoutData(new GridData(GridData.FILL_BOTH));
		restrictionComposite_.setLayout(new GridLayout(2, false));

		Utility.makeBoldLabel(restrictionComposite_, 2,
				GridData.HORIZONTAL_ALIGN_CENTER | GridData.GRAB_HORIZONTAL,
				"Restrictions");
		fullCodeSetDescription = Utility.makeWrappingLabel(
				restrictionComposite_, "", 2);

		// the restrictions section.

		restrictionsList_ = new List(restrictionComposite_, SWT.SINGLE
				| SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		restrictionsList_.setLayoutData(gd);
		restrictionsList_.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				addRestriction.setEnabled(true);
				if (restrictionsList_.getSelection().length > 0) {
					editRestriction.setEnabled(true);
					removeRestriction.setEnabled(true);
				}
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				// 
			}

		});

		Composite buttons = new Composite(restrictionComposite_, SWT.BORDER);
		buttons.setLayout(new GridLayout());
		gd = new GridData(GridData.FILL_VERTICAL);
		buttons.setLayoutData(gd);

		addRestriction = Utility.makeButton("Add", buttons,
				GridData.FILL_HORIZONTAL);
		addRestriction.setEnabled(false);
		addRestriction.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				if (getSelectedCodeSet() instanceof CodedNodeSet) {
					new RestrictionGUI(LB_GUI.this, null);
				} else {
					new GraphRestrictionGUI(LB_GUI.this, null);
				}
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				//
			}

		});
		editRestriction = Utility.makeButton("Edit", buttons,
				GridData.FILL_HORIZONTAL);
		editRestriction.setEnabled(false);
		editRestriction.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				if (getSelectedCodeSet() instanceof CodedNodeSet) {
					int ri = restrictionsList_.getSelectionIndex();
					new RestrictionGUI(LB_GUI.this,
							getSelectedCodeSet().restrictions.get(ri));
				} else {
					int ri = restrictionsList_.getSelectionIndex();
					new GraphRestrictionGUI(LB_GUI.this,
							getSelectedCodeSet().restrictions.get(ri));
				}
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				//
			}

		});

		removeRestriction = Utility.makeButton("Remove", buttons,
				GridData.FILL_HORIZONTAL);
		removeRestriction.setEnabled(false);
		removeRestriction.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				int ri = restrictionsList_.getSelectionIndex();
				int ci = codeSetsList_.getSelectionIndex();

				codeSets.get(ci).restrictions.remove(ri);
				updateRestrictionsView();
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				//

			}

		});

		// The summary section at the top has a stack - one composite for coded
		// node set, another for
		// coded node graph.
		codeSetOrGraphSummary = new Composite(restrictionComposite_, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		codeSetOrGraphSummary.setLayoutData(gd);

		codeSetSummaryStack = new StackLayout();
		codeSetOrGraphSummary.setLayout(codeSetSummaryStack);

		// code set stack composite
		codeSetSummary = new Composite(codeSetOrGraphSummary, SWT.NONE);
		codeSetSummary.setLayout(new GridLayout(2, false));

		Utility.makeLabel(codeSetSummary, ""); // space filler

		Composite buttonsHolder = new Composite(codeSetSummary, SWT.None);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		buttonsHolder.setLayoutData(gd);
		buttonsHolder.setLayout(new GridLayout(2, false));

		Button setSortOptions = Utility.makeButton("Set Sort Options",
				buttonsHolder, GridData.GRAB_HORIZONTAL
						| GridData.HORIZONTAL_ALIGN_END
						| GridData.GRAB_VERTICAL | GridData.VERTICAL_ALIGN_END);
		setSortOptions.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				int ci = codeSetsList_.getSelectionIndex();
				new SortOptions(LB_GUI.this, ((CodedNodeSet) codeSets.get(ci)));
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				//
			}

		});

		Button resolveCodeSet = Utility.makeButton("Resolve Code Set",
				buttonsHolder, GridData.HORIZONTAL_ALIGN_END
						| GridData.GRAB_VERTICAL | GridData.VERTICAL_ALIGN_END);
		resolveCodeSet.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				try {
					resolveCodeSet();
				} catch (LBException e) {
					errorHandler.showError("Resolve Problem", e.toString());
				}
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				//
			}

		});		
		

		// code graph stack composite
		codeGraphSummary = new Composite(codeSetOrGraphSummary, SWT.NONE);
		codeGraphSummary.setLayout(new GridLayout(4, false));

		Utility.makeLabel(codeGraphSummary, "Relation Container");
		relationName_ = new Combo(codeGraphSummary, SWT.SINGLE | SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		relationName_.setLayoutData(gd);

		relationName_.setToolTipText("Optional");

		relationName_.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				int ci = codeSetsList_.getSelectionIndex();
				((CodedNodeGraph) codeSets.get(ci)).relationName = relationName_
						.getText();
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				//
			}

		});

		Utility.makeLabel(codeGraphSummary, "Focus Code");
		graphFocus_ = Utility.makeText(codeGraphSummary, "Optional", 3);
		graphFocus_.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent arg0) {
				ConceptReference focus = Constructors.createConceptReference(
						graphFocus_.getText(), graphFocusCS_.getText());
				int ci = codeSetsList_.getSelectionIndex();
				((CodedNodeGraph) codeSets.get(ci)).graphFocus = focus;
			}

			public void focusGained(FocusEvent arg0) {
				// noop
			}
		});

		Utility.makeLabel(codeGraphSummary, "Focus Code System");
		graphFocusCS_ = new Combo(codeGraphSummary, SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		graphFocusCS_.setLayoutData(gd);
		graphFocusCS_.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent arg0) {
				ConceptReference focus = Constructors.createConceptReference(
						graphFocus_.getText(), graphFocusCS_.getText());
				int ci = codeSetsList_.getSelectionIndex();
				((CodedNodeGraph) codeSets.get(ci)).graphFocus = focus;
			}

			public void focusGained(FocusEvent arg0) {
				// noop
			}
		});
		
		Utility.makeLabel(codeGraphSummary, "Focus Code Namespace");
        graphFocusNS_ = new Combo(codeGraphSummary, SWT.SINGLE);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 3;
        graphFocusNS_.setLayoutData(gd);
        graphFocusNS_.addFocusListener(new FocusListener() {
            public void focusLost(FocusEvent arg0) {
                String namespace = null;
                if(StringUtils.isNotBlank(graphFocusNS_.getText())){
                    namespace = graphFocusNS_.getText();
                }
                ConceptReference focus = Constructors.createConceptReference(
                        graphFocus_.getText(), namespace, graphFocusCS_.getText() );
                int ci = codeSetsList_.getSelectionIndex();
                ((CodedNodeGraph) codeSets.get(ci)).graphFocus = focus;
            }

            public void focusGained(FocusEvent arg0) {
                // noop
            }
        });

		Utility.makeLabel(codeGraphSummary, "Max Resolve Depth");
		resolveDepth_ = Utility.makeText(codeGraphSummary);
		gd = new GridData(GridData.FILL_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		resolveDepth_.setLayoutData(gd);
		resolveDepth_.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent arg0) {
				String temp = resolveDepth_.getText();
				int depth = -1;

				if (temp.length() > 0) {
					try {
						depth = Integer.parseInt(temp);
					} catch (NumberFormatException e) {
						errorHandler.showError("Invalid Depth",
								"Depth must be a number");
						resolveDepth_.forceFocus();
					}
				}

				int ci = codeSetsList_.getSelectionIndex();
				((CodedNodeGraph) codeSets.get(ci)).resolveDepth = depth;
			}

			public void focusGained(FocusEvent arg0) {
				// noop
			}
		});

		resolveForward = new Button(codeGraphSummary, SWT.CHECK);
		resolveForward.setText("Resolve Forward");
		resolveForward.setSelection(true);
		resolveForward.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				int ci = codeSetsList_.getSelectionIndex();
				((CodedNodeGraph) codeSets.get(ci)).resolveForward = resolveForward
						.getSelection();
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				//
			}

		});

		resolveBackward = new Button(codeGraphSummary, SWT.CHECK);
		resolveBackward.setText("Resolve Backward");
		resolveBackward.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				int ci = codeSetsList_.getSelectionIndex();
				((CodedNodeGraph) codeSets.get(ci)).resolveBackward = resolveBackward
						.getSelection();
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				//
			}

		});

		Utility.makeLabel(codeGraphSummary, "Max To Return");
		resolveMax_ = Utility.makeText(codeGraphSummary);
		gd = new GridData(GridData.FILL_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		resolveMax_.setLayoutData(gd);
		resolveMax_.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent arg0) {
				String temp = resolveMax_.getText();
				int max = -1;

				if (temp.length() > 0) {
					try {
						max = Integer.parseInt(temp);
					} catch (NumberFormatException e) {
						errorHandler.showError("Invalid Max",
								"Max must be a number");
						resolveMax_.forceFocus();
					}
				}

				int ci = codeSetsList_.getSelectionIndex();
				((CodedNodeGraph) codeSets.get(ci)).maxToReturn = max;
			}

			public void focusGained(FocusEvent arg0) {
				// noop
			}
		});

		Composite codeGraphButtonsHolder = new Composite(codeGraphSummary,
				SWT.None);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		codeGraphButtonsHolder.setLayoutData(gd);
		codeGraphButtonsHolder.setLayout(new GridLayout(3, false));

		Button setGraphSortOptions = Utility.makeButton("Set Sort Options",
				codeGraphButtonsHolder, GridData.GRAB_HORIZONTAL
						| GridData.HORIZONTAL_ALIGN_END
						| GridData.GRAB_VERTICAL | GridData.VERTICAL_ALIGN_END);
		setGraphSortOptions.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				int ci = codeSetsList_.getSelectionIndex();
				new SortOptions(LB_GUI.this,
						((CodedNodeGraph) codeSets.get(ci)));
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				//
			}

		});

		Button resolveCodeGraphFlat = Utility.makeButton("Resolve as Set",
				codeGraphButtonsHolder, GridData.HORIZONTAL_ALIGN_END
						| GridData.GRAB_VERTICAL | GridData.VERTICAL_ALIGN_END);
		resolveCodeGraphFlat.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				try {
					resolveCodeGraphFlat();
				} catch (LBException e) {
					errorHandler.showError("Resolve Problem", e.toString());
				}
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				//
			}

		});

		Button resolveCodeGraph = Utility.makeButton("Resolve as Graph",
				codeGraphButtonsHolder, GridData.HORIZONTAL_ALIGN_END
						| GridData.GRAB_VERTICAL | GridData.VERTICAL_ALIGN_END);
		resolveCodeGraph.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				try {
					resolveCodeGraph();
				} catch (LBException e) {
					errorHandler.showError("Resolve Problem", e.toString());
				}
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				//
			}

		});

		// /

		noCodeSetChoosenSummary = new Composite(codeSetOrGraphSummary, SWT.NONE);
		noCodeSetChoosenSummary.setLayout(new GridLayout());

		Utility.makeWrappingLabel(noCodeSetChoosenSummary,
				"You must choose a single Code Set or Graph on the left.", 1);

		codeSetSummaryStack.topControl = noCodeSetChoosenSummary;

	}

	private void updateButtonStates() {
	    CodingSchemeVersionStatus state = LB_GUI.this.getSelectedCodeSystemActiveState();
		if (isAdminEnabled) {
			if (enableAdmin_.getSelection()) {
			    if(state == null) {
                    activate_.setEnabled(false);
                    deactivate_.setEnabled(false);
                } else if (state.equals(CodingSchemeVersionStatus.ACTIVE)) {
					activate_.setEnabled(false);
					deactivate_.setEnabled(true);
				} else if (state.equals(CodingSchemeVersionStatus.INACTIVE)) {
					activate_.setEnabled(true);
					deactivate_.setEnabled(false);
				} else {
					activate_.setEnabled(true);
					deactivate_.setEnabled(true);
				}
			} else {
				activate_.setEnabled(false);
				deactivate_.setEnabled(false);
			}
		}
	}

	public void updateRestrictionsView() {
		restrictionsList_.removeAll();
		editRestriction.setEnabled(false);
		removeRestriction.setEnabled(false);
		int[] selectedItems = codeSetsList_.getSelectionIndices();
		if (selectedItems.length == 1) {
			CodeSet cs = codeSets.get(selectedItems[0]);
			addRestriction.setEnabled(true);

			if (cs instanceof CodedNodeSet) {
				fullCodeSetDescription.setText(((CodedNodeSet) cs)
						.getFullDescription());
				codeSetSummaryStack.topControl = codeSetSummary;
				codeSetSummary.layout();
			} else {
				fullCodeSetDescription.setText(((CodedNodeGraph) cs)
						.getFullDescription());

				try {
					relationName_.setItems(cs.getSupportedRelations(getLbs()));
				} catch (LBException e) {
					log.error("Problem getting the supported relations", e);
				}

				relationName_.setText(((CodedNodeGraph) cs).relationName);
				try {
					graphFocusCS_
							.setItems(cs.getSupportedCodeSystems(getLbs()));
				} catch (LBException e) {
					log.error("Problem getting the supported code systems", e);
				}
				try {
                    graphFocusNS_
                            .setItems(cs.getSupportedNamespaces(getLbs()));
                } catch (LBException e) {
                    log.error("Problem getting the supported code namespaces", e);
                }
				if (((CodedNodeGraph) cs).graphFocus != null) {
				    ConceptReference ref = ((CodedNodeGraph) cs).graphFocus;
                    
				    if(StringUtils.isNotBlank(ref.getCode())) {
    					graphFocus_.setText(((CodedNodeGraph) cs).graphFocus
    							.getConceptCode());
				    }
				    if(StringUtils.isNotBlank(ref.getCodingSchemeName())) {
    					graphFocusCS_.setText(((CodedNodeGraph) cs).graphFocus
    							.getCodingSchemeName());
				    }
				    if(StringUtils.isNotBlank(ref.getCodeNamespace())) {
    					graphFocusNS_.setText(((CodedNodeGraph) cs).graphFocus
                                .getCodeNamespace());
				    }
				} else {
					graphFocus_.setText("");
					graphFocusCS_.setText("");
					graphFocusNS_.setText("");
				}
				resolveForward
						.setSelection(((CodedNodeGraph) cs).resolveForward);
				resolveBackward
						.setSelection(((CodedNodeGraph) cs).resolveBackward);
				resolveDepth_.setText(((CodedNodeGraph) cs).resolveDepth + "");
				resolveMax_.setText(((CodedNodeGraph) cs).maxToReturn + "");

				codeSetSummaryStack.topControl = codeGraphSummary;
				codeGraphSummary.layout();
			}

			if (cs.restrictions != null) {
				for (int i = 0; i < cs.restrictions.size(); i++) {
					restrictionsList_.add(cs.restrictions.get(i).toString());
				}
			}
		} else {
			fullCodeSetDescription.setText("");
			codeSetSummaryStack.topControl = noCodeSetChoosenSummary;
			addRestriction.setEnabled(false);

		}
		codeSetOrGraphSummary.layout();
	}

	/*
	 * build the menus
	 */
	MenuItem configureItem;

	private void buildMenus() {
		Menu mBar = new Menu(shell_, SWT.BAR);

		// build top menu bar
		MenuItem commandItem = new MenuItem(mBar, SWT.CASCADE);
		commandItem.setText("&Commands");

		if (isAdminEnabled) {
			loadItem_ = new MenuItem(mBar, SWT.CASCADE);
			loadItem_.setText("&Load Terminology");
			loadItem_.setEnabled(false);

			exportItem_ = new MenuItem(mBar, SWT.CASCADE);
			exportItem_.setText("&Export Terminology");
			exportItem_.setEnabled(false);
			
		}

		MenuItem helpItem = new MenuItem(mBar, SWT.CASCADE);
		helpItem.setText("&Help");

		// build file Menu
		Menu commandMenu = new Menu(shell_, SWT.DROP_DOWN);
		commandItem.setMenu(commandMenu);

		configureItem = new MenuItem(commandMenu, SWT.CASCADE);
		configureItem.setText("&Configure");
		configureItem.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				new Configure(LB_GUI.this, currentProperties_);
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				//
			}

		});
		if (isAdminEnabled) {// start enable admin conditional
			enableAdmin_ = new MenuItem(commandMenu, SWT.CASCADE | SWT.CHECK);
			enableAdmin_.setText("&Enable Admin Options");
			enableAdmin_.setSelection(false);
			enableAdmin_.addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent arg0) {
					if (enableAdmin_.getSelection()) {
						loadItem_.setEnabled(true);
						exportItem_.setEnabled(true);
						cleanUpItem_.setEnabled(true);
						changeTag_.setEnabled(true);
						removeCodeSystem_.setEnabled(true);
						removeCodeSystemHistory_.setEnabled(true);
						removeCodeSystemMetadata_.setEnabled(true);
						rebuildIndex_.setEnabled(true);
						loadManifest_.setEnabled(true);
						CodingSchemeVersionStatus state = LB_GUI.this
								.getSelectedCodeSystemActiveState();
						if(state == null) {
						    activate_.setEnabled(false);
                            deactivate_.setEnabled(false);
						} else if (state.equals(CodingSchemeVersionStatus.ACTIVE)) {
							activate_.setEnabled(false);
							deactivate_.setEnabled(true);
						} else if (state.equals(CodingSchemeVersionStatus.INACTIVE)) {
							activate_.setEnabled(true);
							deactivate_.setEnabled(false);
						} else {
							activate_.setEnabled(true);
							deactivate_.setEnabled(true);
						}

					} else {
						loadItem_.setEnabled(false);
						exportItem_.setEnabled(false);
						cleanUpItem_.setEnabled(false);
						changeTag_.setEnabled(false);
						removeCodeSystem_.setEnabled(false);
						removeCodeSystemHistory_.setEnabled(false);
						removeCodeSystemMetadata_.setEnabled(false);
						rebuildIndex_.setEnabled(false);
						activate_.setEnabled(false);
						deactivate_.setEnabled(false);
						loadManifest_.setEnabled(true);
					}

				}

				public void widgetDefaultSelected(SelectionEvent arg0) {
					//
				}

			});

		}// end Admin enable conditional

		cleanUpItem_ = new MenuItem(commandMenu, SWT.CASCADE);
		cleanUpItem_.setText("&Clean Up");
		cleanUpItem_.setEnabled(false);
		cleanUpItem_.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				new CleanUp(LB_GUI.this);
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				//
			}

		});

		MenuItem viewLogItem = new MenuItem(commandMenu, SWT.CASCADE);
		viewLogItem.setText("&View Log File");
		viewLogItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event arg0) {
				logViewer_.setVisible(true);
			}
		});

		MenuItem exitItem = new MenuItem(commandMenu, SWT.CASCADE);
		exitItem.setText("&Exit");
		exitItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event arg0) {
				shell_.close();
				shell_.dispose();
			}
		});

		// build the Load Menu

        if (isAdminEnabled) { // begin Admin enable conditional

            Menu loadMenu = new Menu(shell_, SWT.DROP_DOWN);
            loadItem_.setMenu(loadMenu);
            
            try {
                
                ExtensionDescription[] extensionsSorted = 
                        sortNames (lbs_.getServiceManager(null).getExtensionRegistry().getLoadExtensions());

                for (int i = 0; i < extensionsSorted.length; i++) {
                    
                    final ExtensionDescription extension = extensionsSorted[i];

                    if(extension.getName() != "ResolvedValueSetDefinitionLoader"){
                        MenuItem loadItem = new MenuItem(loadMenu, SWT.NONE);
                        loadItem.setText(extension.getName() + " - " + extension.getDescription());
                                                
                        loadItem.addSelectionListener(new SelectionListener() {
    
                            public void widgetSelected(SelectionEvent arg0) {
                                Loader loader;
                                try {
                                    loader = lbs_.getServiceManager(null).getLoader(extension.getName());
                                } catch (LBException e) {
                                    throw new RuntimeException(e);
                                }
                                new LoaderExtensionShell(LB_GUI.this, loader);
                            }
    
                            public void widgetDefaultSelected(SelectionEvent arg0) {
                                // not used
                            }
    
                        });
                    }
                }
                    MenuItem loadRVSItem = new MenuItem(loadMenu, SWT.NONE);
                    loadRVSItem.setText(ResolvedValueSetDefinitionLoaderImpl.NAME + " - " + "Resolves and persists value sets current in the terminology service");
                    loadRVSItem.addSelectionListener(new SelectionListener() {

                        public void widgetSelected(SelectionEvent arg0) {
                            
                            new ResolvedValueSetLoader(LB_GUI.this);
                        }

                        public void widgetDefaultSelected(SelectionEvent arg0) {
                            // not used
                        }

                    });
         

            } catch (LBException e) {
                throw new RuntimeException(e);
            }
			

			// build the Export Menu

			Menu exportMenu = new Menu(shell_, SWT.DROP_DOWN);
			exportItem_.setMenu(exportMenu);

			MenuItem exportLexGrid = new MenuItem(exportMenu, SWT.NONE);
			exportLexGrid.setText("Export as LexGrid XML");
			exportLexGrid.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent arg0) {
					AbsoluteCodingSchemeVersionReference acsvr = getSelectedCodeSystem();

					if (acsvr == null) {
						errorHandler.showError("No coding scheme selected",
								"You must select a coding scheme first.");
						return;
					}
					/*
					Exporter exporter;
                    try {
                        exporter = lbs_.getServiceManager(null).getExporter(org.LexGrid.LexBIG.Impl.exporters.LexGridExport.name);
                    } catch (LBException e) {
                        throw new RuntimeException(e);
                    }
                    new ExporterExtensionShell(LB_GUI.this, exporter, acsvr);
                    */
	                org.LexGrid.LexBIG.Impl.exporters.LexGridExport exporter;
	                try {
	                    exporter = (org.LexGrid.LexBIG.Impl.exporters.LexGridExport)lbs_.getServiceManager(null).getExporter(org.LexGrid.LexBIG.Impl.exporters.LexGridExport.name);
	                } catch (LBException e) {
	                    throw new RuntimeException(e);
	                }
	                try {	                    
	                    // exporter.setSource(acsvr);
	                    
	                    org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph cng = lbs_.getNodeGraph(acsvr.getCodingSchemeURN(), 
	                          Constructors.createCodingSchemeVersionOrTagFromVersion(acsvr.getCodingSchemeVersion()),null);
	                    
//	                    org.LexGrid.LexBIG.LexBIGService.CodedNodeSet cns = lbs_.getCodingSchemeConcepts(acsvr.getCodingSchemeURN(), 
//	                          Constructors.createCodingSchemeVersionOrTagFromVersion(acsvr.getCodingSchemeVersion()) );
	        
                        org.LexGrid.LexBIG.LexBIGService.CodedNodeSet cns = lbs_.getNodeSet(acsvr.getCodingSchemeURN(), 
                    Constructors.createCodingSchemeVersionOrTagFromVersion(acsvr.getCodingSchemeVersion()), null );
	        
	                    
	                    exporter.setCng(cng);
	                    exporter.setCns(cns);
	                    
	                    new ExporterExtensionShell(LB_GUI.this, exporter, acsvr);
	                    
	                } catch (LBException e) {
	                    e.printStackTrace();
	                }
					
				}

				public void widgetDefaultSelected(SelectionEvent arg0) {
					// not used
				}

			});
			
			// Export as OWL/RDF
            MenuItem exportOwlRdf = new MenuItem(exportMenu, SWT.NONE);
            exportOwlRdf.setText("Export as OWL/RDF");
            exportOwlRdf.addSelectionListener(new SelectionListener() {
                public void widgetSelected(SelectionEvent arg0) {
                    AbsoluteCodingSchemeVersionReference acsvr = getSelectedCodeSystem();

                    if (acsvr == null) {
                        errorHandler.showError("No coding scheme selected",
                                "You must select a coding scheme first.");
                        return;
                    }

                    OwlRdfExporterImpl exporter = null;
                    try {
                        exporter = (OwlRdfExporterImpl) lbs_.getServiceManager(null).getExporter(OwlRdfExporterImpl.name);
                    } catch (LBException e) {
                        throw new RuntimeException(e);
                    }
                    new ExporterExtensionShell(LB_GUI.this, exporter, acsvr);
                    
                }

                public void widgetDefaultSelected(SelectionEvent arg0) {
                    // not used
                }

            });
			
			
            // Export as OBO
			MenuItem exportOBO = new MenuItem(exportMenu, SWT.NONE);
			exportOBO.setText("Export as OBO");
			exportOBO.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent arg0) {
					AbsoluteCodingSchemeVersionReference acsvr = getSelectedCodeSystem();

					if (acsvr == null) {
						errorHandler.showError("No coding scheme selected",
								"You must select a coding scheme first.");
						return;
					}

					new OBOExport(LB_GUI.this, acsvr);
				}

				public void widgetDefaultSelected(SelectionEvent arg0) {
					// not used
				}

			});
		}// end Admin enable conditional

		// build the Help Menu

		// Using the java Properties class to pull current info
		// from the build.properties file.
		final VersionProperties vProps = new VersionProperties();

		Menu helpMenu = new Menu(shell_, SWT.DROP_DOWN);
		helpItem.setMenu(helpMenu);

		MenuItem aboutItem = new MenuItem(helpMenu, SWT.NONE);
		aboutItem.setText("&About");
		aboutItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event arg0) {
				errorHandler
						.showInfo(
								"About",
								"LexBIG GUI Console"
										+ "\nBased on: "
										+ vProps.getProduct()
										+ "\nVersion: "
										+ vProps.getVersion()
										+ "\nBuild date: "
										+ vProps.getDate()
										+ "\n\nFor questions, please contact the caBIG Vocabulary Knowledge Center (https://cabig-kc.nci.nih.gov/Vocab/forums/).");
			}

		});

		shell_.setMenuBar(mBar);

	}

	public LexBIGService getLbs() {
		return this.lbs_;
	}

	public void getNewLBS() throws LBInvocationException {
		this.lbs_ = LexBIGServiceImpl.defaultInstance();
	}

	public Shell getShell() {
		return this.shell_;
	}

	public CodeSet getSelectedCodeSet() {
		int index = codeSetsList_.getSelectionIndex();
		if (index >= 0) {
			return codeSets.get(index);
		}
		return null;
	}

	public String[] getSupportedMatchAlgorithms() {
		ModuleDescription[] ed = getLbs().getMatchAlgorithms()
				.getModuleDescription();
		String[] result = new String[ed.length];
		for (int i = 0; i < ed.length; i++) {
			result[i] = ed[i].getName();
		}
		return result;
	}

	/**
	 * @param currentProperties
	 *            the currentProperties to set
	 */
	public void setCurrentProperties(Properties currentProperties) {
		this.currentProperties_ = currentProperties;
	}

	private void displayCodeSystemDetails() throws LBException {
		CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
		csvt.setVersion(getSelectedCodeSystem().getCodingSchemeVersion());
		new CodeSystemDetails(this.shell_, this, getLbs().resolveCodingScheme(
				getSelectedCodeSystem().getCodingSchemeURN(), csvt));
	}

	private void resolveCodeSet() throws LBException {
		CodedNodeSet cnsData = (CodedNodeSet) getSelectedCodeSet();
		org.LexGrid.LexBIG.LexBIGService.CodedNodeSet cns = cnsData
				.getRealCodedNodeSet(getLbs());

		// ok, at this point, things are constructed fairly reasonably. Time to
		// spawn a new window
		// for the results - in a new thread. Any errors after this point get
		// reported to the new window.
		// this is when the 'real' resolve step starts.

		new DisplayCodedNodeSet(this, cns, cnsData.getSortOptions());
	}

	private void resolveCodeGraph() throws LBException {
		CodedNodeGraph cngData = (CodedNodeGraph) getSelectedCodeSet();
		org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph cng = cngData
				.getRealCodedNodeGraph(getLbs());

		// ok, at this point, things are constructed fairly reasonably. Time to
		// spawn a new window
		// for the results - in a new thread. Any errors after this point get
		// reported to the new window.
		// this is when the 'real' resolve step starts.

		new DisplayCodedNodeSet(this, cng, cngData.getSortOptions(), false,
				cngData.resolveForward, cngData.resolveBackward,
				cngData.resolveDepth, cngData.maxToReturn, cngData.graphFocus);
	}

	private void resolveCodeGraphFlat() throws LBException {
		CodedNodeGraph cngData = (CodedNodeGraph) getSelectedCodeSet();
		org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph cng = cngData
				.getRealCodedNodeGraph(getLbs());

		// ok, at this point, things are constructed fairly reasonably. Time to
		// spawn a new window
		// for the results - in a new thread. Any errors after this point get
		// reported to the new window.
		// this is when the 'real' resolve step starts.

		new DisplayCodedNodeSet(this, cng, cngData.getSortOptions(), true,
				cngData.resolveForward, cngData.resolveBackward,
				cngData.resolveDepth, cngData.maxToReturn, cngData.graphFocus);
	}

    private ExtensionDescription[] sortNames(ExtensionDescriptionList descriptions) {

        ArrayList<ExtensionDescription> extensionList = new ArrayList<ExtensionDescription>();

        for (final ExtensionDescription extension : descriptions.getExtensionDescription()) {
            if (extension.getName() != "ResolvedValueSetDefinitionLoader") {
                extensionList.add(extension);
            }
        }

        ExtensionDescription[] extensionArray = extensionList.toArray(new ExtensionDescription[extensionList.size()]);
        Arrays.sort(extensionArray, ExtensionDescriptionNameComparator);
        return extensionArray;
    }
   
   
  public boolean isAdminEnabled() {
     return isAdminEnabled;
  }
        
	    
    private static Comparator<ExtensionDescription> ExtensionDescriptionNameComparator = new Comparator<ExtensionDescription>() {

        // Comparator to compare ExtensionDescription names and sort them
        // alphabetically - ascending.

        public int compare(ExtensionDescription desc1, ExtensionDescription desc2) {

            String description1 = desc1.getName().toUpperCase();
            String description2 = desc2.getName().toUpperCase();

            // ascending order
            return description1.compareTo(description2);
        }

    };
}