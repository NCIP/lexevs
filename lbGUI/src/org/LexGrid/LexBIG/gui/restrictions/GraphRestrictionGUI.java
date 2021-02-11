
package org.LexGrid.LexBIG.gui.restrictions;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.gui.DialogHandler;
import org.LexGrid.LexBIG.gui.LB_GUI;
import org.LexGrid.LexBIG.gui.Utility;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Shell to let you add and edit restrictions.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class GraphRestrictionGUI {
	private static Logger log = Logger.getLogger("LB_GUI_LOGGER");
	LB_GUI lb_gui_;
	DialogHandler dialog_;
	Restriction restriction_;

	private StackLayout restrictionStack_;
	private Composite restrictionStackComposite_;
	private Composite havingAssociations;
	private Composite havingDirectionalNames;
	private Composite restrictToCodes_;
	private Composite restrictToSourceCodes_;
	private Composite restrictToTargetCodes_;
	private Combo restrictionTypesSelection_;

	private List associations_;
	private List directionalNames_;
	private List associationNamesQualifiers_,associationDirectionalNamesQualifiers_;
	private Text associationNamesQualifierValue_,associationDirectionalNamesQualifierValue_;

	private Text restrictToCodesText_, restrictToSourceCodesText_,
			restrictToTargetCodesText_;

	public GraphRestrictionGUI(LB_GUI lb_gui, Restriction restriction) {
		lb_gui_ = lb_gui;
		Shell shell = new Shell(lb_gui_.getShell(), SWT.APPLICATION_MODAL
				| SWT.DIALOG_TRIM | SWT.RESIZE);
		shell.setSize(400, 300);

		dialog_ = new DialogHandler(shell);

		shell.setText("Configure LexBIG");

		restriction_ = restriction;

		init(shell);

		if (restriction_ != null) {
			restrictionTypesSelection_.setEnabled(false);

			if (restriction_ instanceof Association) {
				restrictionTypesSelection_.select(0);

				Association as = (Association) restriction_;

				for (int i = 0; i < associations_.getItemCount(); i++) {
					for (int j = 0; j < as.associations.length; j++) {
						if (associations_.getItem(i).equals(as.associations[j])) {
							associations_.select(i);
							break;
						}
					}
				}

				for (int i = 0; i < associationNamesQualifiers_.getItemCount(); i++) {
					for (int j = 0; j < as.associationQualifiers.length; j++) {
						if (associationNamesQualifiers_.getItem(i).equals(
								as.associationQualifiers[j])) {
						    associationNamesQualifiers_.select(i);
							break;
						}
					}
				}
				associationNamesQualifierValue_
						.setText(as.associationQualifierValue);

				restrictionStack_.topControl = havingAssociations;
			} else if (restriction_ instanceof DirectionalName) {
				restrictionTypesSelection_.select(0);

				DirectionalName as = (DirectionalName) restriction_;

				for (int i = 0; i < directionalNames_.getItemCount(); i++) {
					for (int j = 0; j < as.directionalNames.length; j++) {
						if (directionalNames_.getItem(i).equals(
								as.directionalNames[j])) {
							directionalNames_.select(i);
							break;
						}
					}
				}

				for (int i = 0; i < associationDirectionalNamesQualifiers_.getItemCount(); i++) {
					for (int j = 0; j < as.associationQualifiers.length; j++) {
						if (associationDirectionalNamesQualifiers_.getItem(i).equals(
								as.associationQualifiers[j])) {
						    associationDirectionalNamesQualifiers_.select(i);
							break;
						}
					}
				}
				associationDirectionalNamesQualifierValue_
						.setText(as.associationQualifierValue);

				restrictionStack_.topControl = havingDirectionalNames;
			} else if (restriction_ instanceof CodeSystem) {
				CodeSystem cs = (CodeSystem) restriction_;
				switch (cs.type) {
				case 0:
					restrictToCodesText_.setText(cs.codeSystem);
					restrictionTypesSelection_.select(1);
					restrictionStack_.topControl = restrictToCodes_;
					break;

				case 1:
					restrictToSourceCodesText_.setText(cs.codeSystem);
					restrictionTypesSelection_.select(2);
					restrictionStack_.topControl = restrictToSourceCodes_;
					break;

				case 2:
					restrictToTargetCodesText_.setText(cs.codeSystem);
					restrictionTypesSelection_.select(3);
					restrictionStack_.topControl = restrictToTargetCodes_;
					break;

				default:
					break;
				}

			}

			restrictionStackComposite_.layout();
			((Composite) restrictionStack_.topControl).layout();

		}
		shell.open();

	}

	private void init(final Shell shell) {
		shell.setLayout(new GridLayout());

		Composite restrictionType = new Composite(shell, SWT.NONE);
		restrictionType.setLayout(new GridLayout(2, false));
		restrictionType.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Utility.makeLabel(restrictionType, "Restriction Type");

		restrictionTypesSelection_ = new Combo(restrictionType, SWT.READ_ONLY);
		restrictionTypesSelection_.setItems(new String[] {
				"Restrict to Associations", "Restrict to DirectionalNames",
				"Restrict to Code System", "Restrict to Source Code System",
				"Restrict to Target Code System" });
		restrictionTypesSelection_.select(0);

		// create a stack of 4 composites (one for each restriction type)

		restrictionStackComposite_ = new Composite(shell, SWT.BORDER);
		restrictionStackComposite_.setLayoutData(new GridData(
				GridData.FILL_BOTH));
		restrictionStack_ = new StackLayout();
		restrictionStackComposite_.setLayout(restrictionStack_);

		// composite for restriction 1

		havingAssociations = new Composite(restrictionStackComposite_, SWT.NONE);
		havingAssociations.setLayout(new GridLayout(2, false));
		Utility.makeLabel(havingAssociations, "Associations");
		associations_ = new List(havingAssociations, SWT.MULTI | SWT.BORDER
				| SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.verticalSpan = 2;
		associations_.setLayoutData(gd);
		try {
			associations_.setItems(lb_gui_.getSelectedCodeSet()
					.getSupportedAssociations(lb_gui_.getLbs()));
		} catch (LBException e) {
			log.error("Problem setting the supported associations", e);
		}

		// filler label to help with layout issues
		Utility.makeLabel(havingAssociations, "");

		Utility.makeLabel(havingAssociations, "Association Qualifiers");
		associationNamesQualifiers_ = new List(havingAssociations, SWT.MULTI
				| SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		gd = new GridData(GridData.FILL_BOTH);
		gd.verticalSpan = 1;
		associationNamesQualifiers_.setLayoutData(gd);
		try {
		    associationNamesQualifiers_.setItems(lb_gui_.getSelectedCodeSet()
					.getSupportedAssociationQualifiers(lb_gui_.getLbs()));
		} catch (LBException e) {
			log
					.error(
							"Problem getting the supported association qualifiers",
							e);
		}

		Utility.makeLabel(havingAssociations, "Association Qualifier Value");
		associationNamesQualifierValue_ = Utility
				.makeText(havingAssociations, "", 1);
		associationNamesQualifierValue_.setEditable(true);

		// filler label to help with layout issues
		Utility.makeLabel(havingAssociations, "");

		// composite for restriction 2

		havingDirectionalNames = new Composite(restrictionStackComposite_,
				SWT.NONE);
		havingDirectionalNames.setLayout(new GridLayout(2, false));
		Utility.makeLabel(havingDirectionalNames, "Directional Names");
		directionalNames_ = new List(havingDirectionalNames, SWT.MULTI
				| SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd2 = new GridData(GridData.FILL_BOTH);
		gd2.verticalSpan = 2;
		directionalNames_.setLayoutData(gd2);
		try {
			directionalNames_.setItems(lb_gui_.getSelectedCodeSet()
					.getAssociationForwardAndReverseNames(lb_gui_.getLbs()));
		} catch (LBException e) {
			log.error("Problem setting the supported associations", e);
		}

		// filler label to help with layout issues
		Utility.makeLabel(havingDirectionalNames, "");

		Utility.makeLabel(havingDirectionalNames, "Association Qualifiers");
		associationDirectionalNamesQualifiers_ = new List(havingDirectionalNames, SWT.MULTI
				| SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		gd2 = new GridData(GridData.FILL_BOTH);
		gd2.verticalSpan = 1;
		associationDirectionalNamesQualifiers_.setLayoutData(gd2);
		try {
		    associationDirectionalNamesQualifiers_.setItems(lb_gui_.getSelectedCodeSet()
					.getSupportedAssociationQualifiers(lb_gui_.getLbs()));
		} catch (LBException e) {
			log
					.error(
							"Problem getting the supported association qualifiers",
							e);
		}

		Utility
				.makeLabel(havingDirectionalNames,
						"Association Qualifier Value");
		associationDirectionalNamesQualifierValue_ = Utility.makeText(havingDirectionalNames,
				"", 1);
		associationDirectionalNamesQualifierValue_.setEditable(true);

		// filler label to help with layout issues
		Utility.makeLabel(havingDirectionalNames, "");

		// composite for restriction 3

		restrictToCodes_ = new Composite(restrictionStackComposite_, SWT.NONE);
		restrictToCodes_.setLayout(new GridLayout(2, false));

		Utility.makeLabel(restrictToCodes_, "Restrict to code system");
		restrictToCodesText_ = Utility.makeText(restrictToCodes_);

		// composite for restriction 4

		restrictToSourceCodes_ = new Composite(restrictionStackComposite_,
				SWT.NONE);
		restrictToSourceCodes_.setLayout(new GridLayout(2, false));

		Utility.makeLabel(restrictToSourceCodes_,
				"Restrict to source code system");
		restrictToSourceCodesText_ = Utility.makeText(restrictToSourceCodes_,
				"Comma seperated list", 1);

		// composite for restriction 5

		restrictToTargetCodes_ = new Composite(restrictionStackComposite_,
				SWT.NONE);
		restrictToTargetCodes_.setLayout(new GridLayout(2, false));

		Utility.makeLabel(restrictToTargetCodes_,
				"Restrict to target code system");
		restrictToTargetCodesText_ = Utility.makeText(restrictToTargetCodes_,
				"Comma seperated list", 1);

		restrictionStack_.topControl = havingAssociations;
		restrictionTypesSelection_
				.addSelectionListener(new SelectionListener() {

					public void widgetSelected(SelectionEvent arg0) {
						int i = restrictionTypesSelection_.getSelectionIndex();
						switch (i) {
						case 0:
							restrictionStack_.topControl = havingAssociations;
							break;
						case 1:
							restrictionStack_.topControl = havingDirectionalNames;
							break;

						case 2:
							restrictionStack_.topControl = restrictToCodes_;
							break;

						case 3:
							restrictionStack_.topControl = restrictToSourceCodes_;
							break;

						case 4:
							restrictionStack_.topControl = restrictToTargetCodes_;
							break;

						}
						restrictionStackComposite_.layout();
						((Composite) restrictionStack_.topControl).layout();
					}

					public void widgetDefaultSelected(SelectionEvent arg0) {
						//
					}

				});

		Composite okCancel = new Composite(shell, SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		okCancel.setLayoutData(gd);
		okCancel.setLayout(new GridLayout(2, false));

		Button ok = new Button(okCancel, SWT.PUSH);
		ok.setText("Ok");
		gd = new GridData(GridData.GRAB_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_END);
		gd.widthHint = 70;
		ok.setLayoutData(gd);

		ok.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {

				int i = restrictionTypesSelection_.getSelectionIndex();
				Restriction restriction = null;
				switch (i) {
				case 0:
					// restrict to associations....
					if (associations_.getSelection().length == 0) {
						dialog_.showError("Invalid Entry",
								"Association is required");
						associations_.forceFocus();
						return;
					}
					Association a;
					if (restriction_ == null) {
						a = new Association();
					} else {
						a = (Association) restriction_;
					}
					a.associations = associations_.getSelection();
					a.associationQualifiers = associationNamesQualifiers_
							.getSelection();
					a.associationQualifierValue = associationNamesQualifierValue_
							.getText();
					restriction = a;
					break;

				case 1:
					// restrict to directional name....
					if (directionalNames_.getSelection().length == 0) {
						dialog_.showError("Invalid Entry",
								"Directional name is required");
						directionalNames_.forceFocus();
						return;
					}
					DirectionalName d;
					if (restriction_ == null) {
						d = new DirectionalName();
					} else {
						d = (DirectionalName) restriction_;
					}
					d.directionalNames = directionalNames_.getSelection();
					d.associationQualifiers = associationDirectionalNamesQualifiers_
							.getSelection();
					d.associationQualifierValue = associationDirectionalNamesQualifierValue_
							.getText();
					restriction = d;
					break;

				case 2:
					// restrict to codes
					if (restrictToCodesText_.getText().length() == 0) {
						dialog_.showError("Invalid Entry",
								"Restriction code(s) are required");
						restrictToCodesText_.forceFocus();
						return;
					}
					CodeSystem cs;
					if (restriction_ == null) {
						cs = new CodeSystem(CodeSystem.CODE_SYSTEM);
					} else {
						cs = (CodeSystem) restriction_;
					}
					cs.codeSystem = restrictToCodesText_.getText();

					restriction = cs;
					break;

				case 3:
					// restrict to source codes
					if (restrictToSourceCodesText_.getText().length() == 0) {
						dialog_.showError("Invalid Entry",
								"Restriction code(s) are required");
						restrictToSourceCodesText_.forceFocus();
						return;
					}
					CodeSystem scs;
					if (restriction_ == null) {
						scs = new CodeSystem(CodeSystem.SOURCE_CODE_SYSTEM);
					} else {
						scs = (CodeSystem) restriction_;
					}
					scs.codeSystem = restrictToSourceCodesText_.getText();

					restriction = scs;
					break;

				case 4:
					// restrict to target codes
					if (restrictToTargetCodesText_.getText().length() == 0) {
						dialog_.showError("Invalid Entry",
								"Restriction code(s) are required");
						restrictToTargetCodesText_.forceFocus();
						return;
					}
					CodeSystem tcs;
					if (restriction_ == null) {
						tcs = new CodeSystem(CodeSystem.TARGET_CODE_SYSTEM);
					} else {
						tcs = (CodeSystem) restriction_;
					}
					tcs.codeSystem = restrictToTargetCodesText_.getText();

					restriction = tcs;
					break;

				}
				if (restriction_ == null) {
					lb_gui_.getSelectedCodeSet().restrictions.add(restriction);
				}
				lb_gui_.updateRestrictionsView();
				shell.dispose();

			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				//
			}

		});

		Button cancel = new Button(okCancel, SWT.PUSH);
		cancel.setText("Cancel");
		gd = new GridData(GridData.HORIZONTAL_ALIGN_END);
		gd.widthHint = 70;
		cancel.setLayoutData(gd);

		cancel.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				shell.dispose();
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				//
			}

		});

	}

	public Restriction getRestriction() {
		return restriction_;
	}

}