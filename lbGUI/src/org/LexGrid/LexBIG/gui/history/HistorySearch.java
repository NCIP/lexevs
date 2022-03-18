
package org.LexGrid.LexBIG.gui.history;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeVersionList;
import org.LexGrid.LexBIG.DataModel.Collections.NCIChangeEventList;
import org.LexGrid.LexBIG.DataModel.Collections.SystemReleaseList;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.SystemReleaseDetail;
import org.LexGrid.LexBIG.History.HistoryService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ObjectToString;
import org.LexGrid.LexBIG.gui.Utility;
import org.LexGrid.versions.CodingSchemeVersion;
import org.LexGrid.versions.SystemRelease;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

/**
 * Class for searching and viewing history information.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class HistorySearch {
	private Shell shell_;
	private HistoryService hs_;
	private String codeSystem_;
	private StyledText results_;
	private Text releaseURI_;
	private Text releasedAfter_;
	private Text releasedBefore_;
	private Text dacConcept_;
	private Text eaURI_;
	private Text eaConcept1_;
	private Text eaConcept2_;
	private Text eaBeginDate_;
	private Text eaEndDate_;
	private Text eaConcept3_;
	private Text eaCSVersion_;

	public HistorySearch(Shell parent, HistoryService hs, String codeSystem) {
		hs_ = hs;
		codeSystem_ = codeSystem;
		shell_ = new Shell(parent.getDisplay());
		shell_.setText("History Viewer");
		shell_.setSize(700, 550);
		shell_.setImage(new Image(shell_.getDisplay(), this.getClass()
				.getResourceAsStream("/icons/icon.gif")));

		buildComponents();

		shell_.setVisible(true);
	}

	public void buildComponents() {
		shell_.setLayout(new GridLayout(1, true));

		TabFolder tabFolder = new TabFolder(shell_, SWT.NONE);
		tabFolder.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// baseline tab
		TabItem baseLineItem = new TabItem(tabFolder, SWT.NULL);
		baseLineItem.setText("Baselines");
		Composite baseLineComposite = new Composite(tabFolder, SWT.NONE);
		baseLineComposite.setLayout(new GridLayout(7, false));
		baseLineItem.setControl(baseLineComposite);

		Utility.makeLabel(baseLineComposite, "Released After");
		releasedAfter_ = new Text(baseLineComposite, SWT.BORDER);
		releasedAfter_.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Utility.makeLabel(baseLineComposite, "Released Before");
		releasedBefore_ = new Text(baseLineComposite, SWT.BORDER);
		releasedBefore_.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Button gb = Utility.makeButton("Get Baselines", baseLineComposite,
				GridData.CENTER);
		gb.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				try {
					SystemReleaseList sr = hs_.getBaselines(
							parseDate(releasedAfter_.getText()),
							parseDate(releasedBefore_.getText()));
					showResult("Results of Get Baselines", sr);
				} catch (Exception e) {
					showError(e);
				}
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				// noop
			}

		});

		Button geb = Utility.makeButton("Get Earliest Baseline",
				baseLineComposite, GridData.CENTER);
		geb.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				try {
					SystemRelease sr = hs_.getEarliestBaseline();
					showResult("Results of Get Earliest Baseline", sr);
				} catch (Exception e) {
					showError(e);
				}
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				// noop
			}

		});
		Button glb = Utility.makeButton("Get Latest Baseline",
				baseLineComposite, GridData.CENTER);
		glb.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				try {
					SystemRelease sr = hs_.getLatestBaseline();
					showResult("Results of Get Latest Baseline", sr);
				} catch (Exception e) {
					showError(e);
				}
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				// noop
			}

		});

		// row 2
		Utility.makeSeperator(baseLineComposite, 7);

		Utility.makeLabel(baseLineComposite, "Release URI");

		releaseURI_ = new Text(baseLineComposite, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		releaseURI_.setLayoutData(gd);
		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalSpan = 2;
		Button gsr = Utility.makeButton("Get System Release",
				baseLineComposite, gd);
		gsr.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				try {
					SystemReleaseDetail srd = hs_.getSystemRelease(new URI(
							releaseURI_.getText()));
					showResult("Results of Get System Release - "
							+ releaseURI_.getText(), srd);
				} catch (Exception e) {
					showError(e);
				}
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				// noop
			}

		});

		// decendants / ancestors / creation tab

		TabItem dacItem = new TabItem(tabFolder, SWT.NULL);
		dacItem.setText("Descendants / Ancestors / Creation");
		Composite dacComposite = new Composite(tabFolder, SWT.NONE);
		dacItem.setControl(dacComposite);

		dacComposite.setLayout(new GridLayout(6, false));
		Utility.makeLabel(dacComposite, "Concept");
		dacConcept_ = new Text(dacComposite, SWT.BORDER);
		dacConcept_.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Button gdb = Utility.makeButton("Get Decendants", dacComposite,
				GridData.CENTER);
		gdb.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				try {
					NCIChangeEventList ncel = hs_.getDescendants(Constructors
							.createConceptReference(dacConcept_.getText(),
									codeSystem_));
					showResult("Results of Get Descendants", ncel);
				} catch (Exception e) {
					showError(e);
				}
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				// noop
			}
		});

		Button gab = Utility.makeButton("Get Ancestors", dacComposite,
				GridData.CENTER);
		gab.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				try {
					NCIChangeEventList ncel = hs_.getAncestors(Constructors
							.createConceptReference(dacConcept_.getText(),
									codeSystem_));
					showResult("Results of Get Ancestors", ncel);
				} catch (Exception e) {
					showError(e);
				}
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				// noop
			}
		});

		Button gcvb = Utility.makeButton("Get Creation Version", dacComposite,
				GridData.CENTER);
		gcvb.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				try {
					CodingSchemeVersion ncel = hs_
							.getConceptCreationVersion(Constructors
									.createConceptReference(dacConcept_
											.getText(), codeSystem_));
					showResult("Results of Get Concept Creation Version", ncel);
				} catch (Exception e) {
					showError(e);
				}
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				// noop
			}
		});

		Button gchvb = Utility.makeButton("Get Change Versions", dacComposite,
				GridData.CENTER);
		gchvb.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				try {
					CodingSchemeVersionList ncel = hs_
							.getConceptChangeVersions(Constructors
									.createConceptReference(dacConcept_
											.getText(), codeSystem_), null,
									null);
					showResult("Results of Get Concept Change Versions", ncel);
				} catch (Exception e) {
					showError(e);
				}
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				// noop
			}
		});

		// edit actions tab

		TabItem editActionsItem = new TabItem(tabFolder, SWT.NULL);
		editActionsItem.setText("Edit Actions");
		Composite editActionsComposite = new Composite(tabFolder, SWT.NONE);
		editActionsItem.setControl(editActionsComposite);

		editActionsComposite.setLayout(new GridLayout(7, false));
		Utility.makeLabel(editActionsComposite, "Concept");
		eaConcept1_ = new Text(editActionsComposite, SWT.BORDER);
		eaConcept1_.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Utility.makeLabel(editActionsComposite, "Release URI");
		eaURI_ = new Text(editActionsComposite, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		eaURI_.setLayoutData(gd);
		Button geab = Utility.makeButton("Get Edit Actions",
				editActionsComposite, GridData.CENTER);
		geab.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				try {
					NCIChangeEventList ncel = hs_.getEditActionList(
							Constructors.createConceptReference(eaConcept1_
									.getText(), codeSystem_), new URI(eaURI_
									.getText()));
					showResult("Results of Get Edit Action List", ncel);
				} catch (Exception e) {
					showError(e);
				}
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				// noop
			}
		});

		// row 2
		Utility.makeSeperator(editActionsComposite, 7);
		Utility.makeLabel(editActionsComposite, "Concept");
		eaConcept2_ = new Text(editActionsComposite, SWT.BORDER);
		eaConcept2_.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Utility.makeLabel(editActionsComposite, "Begin Date");
		eaBeginDate_ = new Text(editActionsComposite, SWT.BORDER);
		eaBeginDate_.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Utility.makeLabel(editActionsComposite, "End Date");
		eaEndDate_ = new Text(editActionsComposite, SWT.BORDER | SWT.FILL);
		eaEndDate_.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Button geab2 = Utility.makeButton("Get Edit Actions",
				editActionsComposite, GridData.CENTER);
		geab2.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				try {
					NCIChangeEventList ncel = hs_.getEditActionList(
							Constructors.createConceptReference(eaConcept2_
									.getText(), codeSystem_),
							parseDate(eaBeginDate_.getText()),
							parseDate(eaEndDate_.getText()));
					showResult("Results of Get Edit Action List", ncel);
				} catch (Exception e) {
					showError(e);
				}
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				// noop
			}
		});

		// row 3
		Utility.makeSeperator(editActionsComposite, 7);
		Utility.makeLabel(editActionsComposite, "Concept");
		eaConcept3_ = new Text(editActionsComposite, SWT.BORDER);
		eaConcept3_.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Utility.makeLabel(editActionsComposite, "Scheme Version");
		eaCSVersion_ = new Text(editActionsComposite, SWT.BORDER);
		eaCSVersion_.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Utility.makeLabel(editActionsComposite, "");
		Utility.makeLabel(editActionsComposite, "");
		Button geab3 = Utility.makeButton("Get Edit Actions",
				editActionsComposite, GridData.CENTER);
		geab3.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				try {
					CodingSchemeVersion csv = new CodingSchemeVersion();
					csv.setVersion(eaCSVersion_.getText());
					NCIChangeEventList ncel = hs_.getEditActionList(
							Constructors.createConceptReference(eaConcept3_
									.getText(), codeSystem_), csv);
					showResult("Results of Get Edit Action List", ncel);
				} catch (Exception e) {
					showError(e);
				}
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				// noop
			}
		});

		// results area

		results_ = new StyledText(shell_, SWT.WRAP | SWT.BORDER | SWT.MULTI
				| SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL);
		results_.setLayoutData(new GridData(GridData.FILL_BOTH));
	}

	private void showResult(String methodLabel, Object result) {
		results_
				.setText(methodLabel + "\n\n" + ObjectToString.toString(result));
	}

	private void showError(Exception e) {
		results_
				.setText("An error occurred while executing the requested operation\n."
						+ e.toString());

	}

	private Date parseDate(String date) throws ParseException {
		if (date == null || date.length() == 0) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat();
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			// see if tacking a time on the end makes it parse
			try {
				return sdf.parse(date + " 12:00 am");
			} catch (ParseException e1) {
				// didn't fix it - throw the origional error.
				throw e;
			}
		}
	}

}