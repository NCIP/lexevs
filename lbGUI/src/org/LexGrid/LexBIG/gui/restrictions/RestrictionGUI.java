
package org.LexGrid.LexBIG.gui.restrictions;

import java.util.ArrayList;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.ActiveOption;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.AnonymousOption;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
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
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class RestrictionGUI {
	private static Logger log = Logger.getLogger("LB_GUI_LOGGER");
	LB_GUI lb_gui_;
	DialogHandler dialog_;
	private Restriction restriction_;

	private Text matchingDesignationMatchText_;
	private Combo matchingDesignationsMatchAlgorithm_;
	private Combo matchingDesignationsMatchLanguage_;
	private Combo matchingDesignationsSearchOption_;
	private Text codeRestriction_;
	private List havingPropertyList_;
	private List havingPropertyTypeList_;
	private List havingSourceList_;
	private List havingUsageContextList_;
	private Combo havingPropertyQualifierName_;
	private Text havingPropertyQualifierValue_;
	private Text propertyMatchText_;
	private List propertyMatchList_;
	private List propertyTypeList_;
	private List propertyMatchSourceList_;
	private List propertyMatchUsageContextList_;
	private Combo propertyMatchLanguage_;
	private Combo propertyMatchAlgorithm_;
	private Combo propertyMatchPropertyQualifierName_;
	private Text propertyMatchPropertyQualifierValue_;
	private Text statusText;
	private Combo statusActive;
	private Combo anonymousCombo;
	private StackLayout restrictionStack_;
	private Composite restrictionStackComposite_;
	private Composite matchingDesignationsComposite_;
	private Composite matchingPropertiesComposite_;
	private Composite matchingCodesComposite_;
	private Composite havingPropertiesComposite_;
	private Composite statusComposite_;
	private Composite anonymousComposite_;
	private Combo restrictionTypesSelection_;

	public RestrictionGUI(LB_GUI lb_gui, Restriction restriction) {
		lb_gui_ = lb_gui;
		Shell shell = new Shell(lb_gui_.getShell(), SWT.APPLICATION_MODAL
				| SWT.DIALOG_TRIM | SWT.RESIZE);
		shell.setSize(450, 500);

		shell.setImage(lb_gui_.getShell().getImage());

		dialog_ = new DialogHandler(shell);

		shell.setText("Configure Restriction");

		restriction_ = restriction;

		init(shell);

		if (restriction_ != null) {
			restrictionTypesSelection_.setEnabled(false);

			if (restriction_ instanceof MatchingDesignation) {
				restrictionTypesSelection_.select(0);
				MatchingDesignation md = (MatchingDesignation) restriction_;
				matchingDesignationMatchText_.setText(md.matchText);
				matchingDesignationsMatchAlgorithm_.setText(md.matchAlgorithm);
				matchingDesignationsMatchLanguage_.setText(md.language);
				matchingDesignationsSearchOption_
						.setText(md.searchDesignationOption.toString());
				restrictionStack_.topControl = matchingDesignationsComposite_;
			} else if (restriction_ instanceof MatchingProperties) {
				restrictionTypesSelection_.select(1);
				MatchingProperties mp = (MatchingProperties) restriction_;
				propertyMatchText_.setText(mp.matchText);
				propertyMatchAlgorithm_.setText(mp.matchAlgorithm);
				propertyMatchLanguage_.setText(mp.language);

				for (int i = 0; i < propertyMatchList_.getItemCount(); i++) {
					for (int j = 0; j < mp.properties.size(); j++) {
						if (propertyMatchList_.getItem(i).equals(
								mp.properties.get(j))) {
							propertyMatchList_.select(i);
							break;
						}
					}
				}
				for (int i = 0; i < propertyTypeList_.getItemCount(); i++) {
					for (int j = 0; j < mp.propertyTypes.size(); j++) {
						if (propertyTypeList_.getItem(i).equals(
								mp.propertyTypes.get(j).name())) {
							propertyTypeList_.select(i);
							break;
						}
					}
				}
				for (int i = 0; i < propertyMatchSourceList_.getItemCount(); i++) {
					for (int j = 0; j < mp.sources.size(); j++) {
						if (propertyMatchSourceList_.getItem(i).equals(
								mp.sources.get(j))) {
							propertyMatchSourceList_.select(i);
							break;
						}
					}
				}
				for (int i = 0; i < propertyMatchUsageContextList_
						.getItemCount(); i++) {
					for (int j = 0; j < mp.usageContexts.size(); j++) {
						if (propertyMatchUsageContextList_.getItem(i).equals(
								mp.usageContexts.get(j))) {
							propertyMatchUsageContextList_.select(i);
							break;
						}
					}
				}

				propertyMatchPropertyQualifierName_
						.setText(mp.propertyQualifier);
				propertyMatchPropertyQualifierValue_
						.setText(mp.propertyQualifierValue);

				restrictionStack_.topControl = matchingPropertiesComposite_;
			} else if (restriction_ instanceof MatchingCode) {
				restrictionTypesSelection_.select(2);
				MatchingCode mc = (MatchingCode) restriction_;
				codeRestriction_.setText(mc.codes);
				restrictionStack_.topControl = matchingCodesComposite_;
			} else if (restriction_ instanceof HavingProperties) {
				restrictionTypesSelection_.select(3);
				HavingProperties hp = (HavingProperties) restriction_;
				for (int i = 0; i < havingPropertyList_.getItemCount(); i++) {
					for (int j = 0; j < hp.properties.size(); j++) {
						if (havingPropertyList_.getItem(i).equals(
								hp.properties.get(j))) {
							havingPropertyList_.select(i);
							break;
						}
					}
				}

				for (int i = 0; i < havingPropertyTypeList_.getItemCount(); i++) {
					for (int j = 0; j < hp.propertyTypes.size(); j++) {
						if (havingPropertyTypeList_.getItem(i).equals(
								hp.propertyTypes.get(j).name())) {
							havingPropertyTypeList_.select(i);
							break;
						}
					}
				}

				for (int i = 0; i < havingSourceList_.getItemCount(); i++) {
					for (int j = 0; j < hp.sources.size(); j++) {
						if (havingSourceList_.getItem(i).equals(
								hp.sources.get(j))) {
							havingSourceList_.select(i);
							break;
						}
					}
				}

				for (int i = 0; i < havingUsageContextList_.getItemCount(); i++) {
					for (int j = 0; j < hp.usageContexts.size(); j++) {
						if (havingUsageContextList_.getItem(i).equals(
								hp.usageContexts.get(j))) {
							havingUsageContextList_.select(i);
							break;
						}
					}
				}

				havingPropertyQualifierName_.setText(hp.propertyQualifier);
				havingPropertyQualifierValue_
						.setText(hp.propertyQualifierValue);

				restrictionStack_.topControl = havingPropertiesComposite_;
			} else if (restriction_ instanceof Status) {
				restrictionTypesSelection_.select(4);
				Status s = (Status) restriction_;
				statusText.setText(s.conceptStatus);
				statusActive.setText(s.activeOption.toString());
				restrictionStack_.topControl = statusComposite_;
			} else if (restriction_ instanceof Anonymous) {
                restrictionTypesSelection_.select(5);
                Anonymous a = (Anonymous) restriction_;
                anonymousCombo.setText(a.anonymousOption.toString());
                restrictionStack_.topControl = anonymousComposite_;
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
				"Restrict to Matching Designations",
				"Restrict to Matching Properties", "Restrict to Codes",
				"Restrict to Properties", "Restrict to Status", "Restrict to Anonymous" });
		
		restrictionTypesSelection_.select(0);

		// create a stack of 5 composites (one for each restriction type)

		restrictionStackComposite_ = new Composite(shell, SWT.BORDER);
		restrictionStackComposite_.setLayoutData(new GridData(
				GridData.FILL_BOTH));
		restrictionStack_ = new StackLayout();
		restrictionStackComposite_.setLayout(restrictionStack_);

		// composite for restriction 1

		matchingDesignationsComposite_ = new Composite(
				restrictionStackComposite_, SWT.NONE);
		matchingDesignationsComposite_.setLayout(new GridLayout(2, false));

		Utility.makeLabel(matchingDesignationsComposite_, "Match Text");
		matchingDesignationMatchText_ = Utility
				.makeText(matchingDesignationsComposite_);

		Utility.makeLabel(matchingDesignationsComposite_, "Match Algorithm");
		matchingDesignationsMatchAlgorithm_ = new Combo(
				matchingDesignationsComposite_, SWT.READ_ONLY);
		matchingDesignationsMatchAlgorithm_.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));
		matchingDesignationsMatchAlgorithm_.setItems(lb_gui_
				.getSupportedMatchAlgorithms());

		matchingDesignationsMatchAlgorithm_.setVisibleItemCount(10);
		matchingDesignationsMatchAlgorithm_.select(0);

		Utility.makeLabel(matchingDesignationsComposite_, "Match Language");
		matchingDesignationsMatchLanguage_ = new Combo(
				matchingDesignationsComposite_, SWT.READ_ONLY);
		matchingDesignationsMatchLanguage_.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));
		try {
			matchingDesignationsMatchLanguage_.setItems(lb_gui_
					.getSelectedCodeSet().getSupportedLanguages(
							lb_gui_.getLbs()));
			matchingDesignationsMatchLanguage_.add("", 0);
		} catch (LBException e) {
			log.error("Problem getting the supported languages", e);
		}

		Utility.makeLabel(matchingDesignationsComposite_, "Designation Type");
		matchingDesignationsSearchOption_ = new Combo(
				matchingDesignationsComposite_, SWT.READ_ONLY);
		matchingDesignationsSearchOption_.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));
		matchingDesignationsSearchOption_.setItems(new String[] {
				SearchDesignationOption.ALL.toString(),
				SearchDesignationOption.NON_PREFERRED_ONLY.toString(),
				SearchDesignationOption.PREFERRED_ONLY.toString() });

		matchingDesignationsSearchOption_.select(0);

		// composite for restriction 2

		matchingPropertiesComposite_ = new Composite(
				restrictionStackComposite_, SWT.NONE);
		matchingPropertiesComposite_.setLayout(new GridLayout(2, false));

		Utility.makeLabel(matchingPropertiesComposite_, "Match Text");
		propertyMatchText_ = Utility.makeText(matchingPropertiesComposite_);

		Utility.makeLabel(matchingPropertiesComposite_, "Match Properties");
		propertyMatchList_ = new List(matchingPropertiesComposite_, SWT.MULTI
				| SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.minimumHeight = 50;
		propertyMatchList_.setLayoutData(gd);
		try {
			propertyMatchList_.setItems(lb_gui_.getSelectedCodeSet()
					.getSupportedProperties(lb_gui_.getLbs()));
		} catch (LBException e) {
			log.error("Problem getting the supported properties", e);
		}

		Utility.makeLabel(matchingPropertiesComposite_, "Property Types");
		propertyTypeList_ = new List(matchingPropertiesComposite_, SWT.MULTI
				| SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.minimumHeight = 60;
		propertyTypeList_.setLayoutData(gd);

		propertyTypeList_.setItems(new String[] { PropertyType.COMMENT.name(),
				PropertyType.DEFINITION.name(), PropertyType.GENERIC.name(),
				PropertyType.PRESENTATION.name() });

		Utility.makeLabel(matchingPropertiesComposite_, "Sources");
		propertyMatchSourceList_ = new List(matchingPropertiesComposite_,
				SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		gd = new GridData(GridData.FILL_BOTH);
		gd.minimumHeight = 50;
		propertyMatchSourceList_.setLayoutData(gd);
		try {
			propertyMatchSourceList_.setItems(lb_gui_.getSelectedCodeSet()
					.getSupportedSources(lb_gui_.getLbs()));
		} catch (LBException e) {
			log.error("Problem getting the supported properties", e);
		}

		Utility.makeLabel(matchingPropertiesComposite_, "Usage Contexts");
		propertyMatchUsageContextList_ = new List(matchingPropertiesComposite_,
				SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		gd = new GridData(GridData.FILL_BOTH);
		gd.minimumHeight = 50;
		propertyMatchUsageContextList_.setLayoutData(gd);
		try {
			propertyMatchUsageContextList_.setItems(lb_gui_
					.getSelectedCodeSet()
					.getSupportedContexts(lb_gui_.getLbs()));
		} catch (LBException e) {
			log.error("Problem getting the supported properties", e);
		}

		Utility.makeLabel(matchingPropertiesComposite_, "Match Algorithm");
		propertyMatchAlgorithm_ = new Combo(matchingPropertiesComposite_,
				SWT.READ_ONLY);
		propertyMatchAlgorithm_.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));
		propertyMatchAlgorithm_.setItems(lb_gui_.getSupportedMatchAlgorithms());

		propertyMatchAlgorithm_.setVisibleItemCount(10);
		propertyMatchAlgorithm_.select(0);

		Utility.makeLabel(matchingPropertiesComposite_, "Match Language");
		propertyMatchLanguage_ = new Combo(matchingPropertiesComposite_,
				SWT.SINGLE);
		propertyMatchLanguage_.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));
		try {
			propertyMatchLanguage_.setItems(lb_gui_.getSelectedCodeSet()
					.getSupportedLanguages(lb_gui_.getLbs()));
			propertyMatchLanguage_.add("", 0);
		} catch (LBException e) {
			log.error("Problem getting the supported languages", e);
		}

		Utility.makeLabel(matchingPropertiesComposite_, "Property Qualifier");
		propertyMatchPropertyQualifierName_ = new Combo(
				matchingPropertiesComposite_, SWT.SINGLE | SWT.READ_ONLY);
		propertyMatchPropertyQualifierName_.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));
		try {
			propertyMatchPropertyQualifierName_.setItems(lb_gui_
					.getSelectedCodeSet().getSupportedPropertyQualifiers(
							lb_gui_.getLbs()));
		} catch (LBException e) {
			log.error("Problem getting the supported property qualifiers", e);
		}

		Utility.makeLabel(matchingPropertiesComposite_,
				"Property Qualifier Value");
		propertyMatchPropertyQualifierValue_ = Utility.makeText(
				matchingPropertiesComposite_, "", 1);

		// composite for restriction 3
		matchingCodesComposite_ = new Composite(restrictionStackComposite_,
				SWT.NONE);
		matchingCodesComposite_.setLayout(new GridLayout(2, false));

		Utility.makeLabel(matchingCodesComposite_, "Concept Codes");
		codeRestriction_ = Utility.makeText(matchingCodesComposite_,
				"Comma seperated list", 1);

		// composite for restriction 4
		havingPropertiesComposite_ = new Composite(restrictionStackComposite_,
				SWT.NONE);
		havingPropertiesComposite_.setLayout(new GridLayout(2, false));

		Utility.makeLabel(havingPropertiesComposite_, "Match Properties");
		havingPropertyList_ = new List(havingPropertiesComposite_, SWT.MULTI
				| SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		gd = new GridData(GridData.FILL_BOTH);
		gd.minimumHeight = 50;
		havingPropertyList_.setLayoutData(gd);
		try {
			havingPropertyList_.setItems(lb_gui_.getSelectedCodeSet()
					.getSupportedProperties(lb_gui_.getLbs()));
		} catch (LBException e) {
			log.error("Problem getting the supported properties", e);
		}

		Utility.makeLabel(havingPropertiesComposite_, "Property Types");
		havingPropertyTypeList_ = new List(havingPropertiesComposite_,
				SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.minimumHeight = 60;
		havingPropertyTypeList_.setLayoutData(gd);

		havingPropertyTypeList_.setItems(new String[] {
				PropertyType.COMMENT.name(), PropertyType.DEFINITION.name(),
				PropertyType.GENERIC.name(), 
				PropertyType.PRESENTATION.name() });

		Utility.makeLabel(havingPropertiesComposite_, "Sources");
		havingSourceList_ = new List(havingPropertiesComposite_, SWT.MULTI
				| SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		gd = new GridData(GridData.FILL_BOTH);
		gd.minimumHeight = 50;
		havingSourceList_.setLayoutData(gd);
		try {
			havingSourceList_.setItems(lb_gui_.getSelectedCodeSet()
					.getSupportedSources(lb_gui_.getLbs()));
		} catch (LBException e) {
			log.error("Problem getting the supported properties", e);
		}

		Utility.makeLabel(havingPropertiesComposite_, "Usage Contexts");
		havingUsageContextList_ = new List(havingPropertiesComposite_,
				SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		gd = new GridData(GridData.FILL_BOTH);
		gd.minimumHeight = 50;
		havingUsageContextList_.setLayoutData(gd);
		try {
			havingUsageContextList_.setItems(lb_gui_.getSelectedCodeSet()
					.getSupportedContexts(lb_gui_.getLbs()));
		} catch (LBException e) {
			log.error("Problem getting the supported properties", e);
		}

		Utility.makeLabel(havingPropertiesComposite_, "Property Qualifier");
		havingPropertyQualifierName_ = new Combo(havingPropertiesComposite_,
				SWT.SINGLE | SWT.READ_ONLY);
		havingPropertyQualifierName_.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));
		try {
			havingPropertyQualifierName_.setItems(lb_gui_.getSelectedCodeSet()
					.getSupportedPropertyQualifiers(lb_gui_.getLbs()));
		} catch (LBException e) {
			log.error("Problem getting the supported property qualifiers", e);
		}

		Utility.makeLabel(havingPropertiesComposite_,
				"Property Qualifier Value");
		havingPropertyQualifierValue_ = Utility.makeText(
				havingPropertiesComposite_, "", 1);

		// composite for restriction 5
		statusComposite_ = new Composite(restrictionStackComposite_, SWT.NONE);
		statusComposite_.setLayout(new GridLayout(2, false));

		Utility.makeLabel(statusComposite_, "Status");
		statusText = Utility.makeText(statusComposite_, "Comma seperated list",
				1);

		Utility.makeLabel(statusComposite_, "Active State");
		statusActive = new Combo(statusComposite_, SWT.READ_ONLY);
		statusActive.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		statusActive.setItems(new String[] {
				ActiveOption.ACTIVE_ONLY.toString(),
				ActiveOption.INACTIVE_ONLY.toString(),
				ActiveOption.ALL.toString() });

		statusActive.select(0);
		
		// composite for restriction 6
        anonymousComposite_ = new Composite(restrictionStackComposite_, SWT.NONE);
        anonymousComposite_.setLayout(new GridLayout(2, false));
        Utility.makeLabel(anonymousComposite_, "Anonymous");
        anonymousCombo = new Combo(anonymousComposite_, SWT.READ_ONLY);
        anonymousCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        anonymousCombo.setItems(new String[] {
                AnonymousOption.ANONYMOUS_ONLY.toString(),
                AnonymousOption.NON_ANONYMOUS_ONLY.toString(),
                AnonymousOption.ALL.toString() });

        statusActive.select(0);

		// end of composite creation

		restrictionStack_.topControl = matchingDesignationsComposite_;
		restrictionTypesSelection_
				.addSelectionListener(new SelectionListener() {

					public void widgetSelected(SelectionEvent arg0) {
						int i = restrictionTypesSelection_.getSelectionIndex();
						switch (i) {
						case 0:
							restrictionStack_.topControl = matchingDesignationsComposite_;
							break;

						case 1:
							restrictionStack_.topControl = matchingPropertiesComposite_;
							break;

						case 2:
							restrictionStack_.topControl = matchingCodesComposite_;
							break;

						case 3:
							restrictionStack_.topControl = havingPropertiesComposite_;
							break;
						case 4:
							restrictionStack_.topControl = statusComposite_;
							break;
						case 5:
                            restrictionStack_.topControl = anonymousComposite_;
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
					// restrict to matching designations....
					if (matchingDesignationMatchText_.getText().length() == 0) {
						dialog_.showError("Invalid Entry",
								"Match Text is required");
						matchingDesignationMatchText_.forceFocus();
						return;
					}
					MatchingDesignation md;
					if (restriction_ == null) {
						md = new MatchingDesignation();
					} else {
						md = (MatchingDesignation) restriction_;
					}
					md.matchText = matchingDesignationMatchText_.getText();
					md.matchAlgorithm = matchingDesignationsMatchAlgorithm_
							.getText();
					md.language = matchingDesignationsMatchLanguage_.getText();

					if (matchingDesignationsSearchOption_.getText().equals(
							SearchDesignationOption.ALL.toString())) {
						md.searchDesignationOption = SearchDesignationOption.ALL;
					} else if (matchingDesignationsSearchOption_.getText()
							.equals(
									SearchDesignationOption.PREFERRED_ONLY
											.toString())) {
						md.searchDesignationOption = SearchDesignationOption.PREFERRED_ONLY;
					} else if (matchingDesignationsSearchOption_.getText()
							.equals(
									SearchDesignationOption.NON_PREFERRED_ONLY
											.toString())) {
						md.searchDesignationOption = SearchDesignationOption.NON_PREFERRED_ONLY;
					}

					restriction = md;
					break;

				case 1:
					// restrict to matching properties
					if (propertyMatchText_.getText().length() == 0) {
						dialog_.showError("Invalid Entry",
								"Match Text is required");
						propertyMatchText_.forceFocus();
						return;
					}
					MatchingProperties mp;
					if (restriction_ == null) {
						mp = new MatchingProperties();
					} else {
						mp = (MatchingProperties) restriction_;
					}
					mp.matchText = propertyMatchText_.getText();
					mp.matchAlgorithm = propertyMatchAlgorithm_.getText();
					mp.language = propertyMatchLanguage_.getText();

					String[] items = propertyMatchList_.getSelection();
					String[] propertyTypes = propertyTypeList_.getSelection();
					if (items.length == 0 && propertyTypes.length == 0) {
						dialog_
								.showError("Invalid Entry",
										"Select one or more properties or property types");
						propertyMatchList_.forceFocus();
						return;
					}
					mp.properties = new ArrayList<String>();
					for (int j = 0; j < items.length; j++) {
						mp.properties.add(items[j]);
					}

					mp.propertyTypes = new ArrayList<PropertyType>();
					for (int j = 0; j < propertyTypes.length; j++) {
						PropertyType pt = null;
						if (propertyTypes[j]
								.equals(PropertyType.COMMENT.name())) {
							pt = PropertyType.COMMENT;
						} else if (propertyTypes[j]
								.equals(PropertyType.DEFINITION.name())) {
							pt = PropertyType.DEFINITION;
						} else if (propertyTypes[j].equals(PropertyType.GENERIC
								.name())) {
							pt = PropertyType.GENERIC;
						} else if (propertyTypes[j]
								.equals(PropertyType.PRESENTATION.name())) {
							pt = PropertyType.PRESENTATION;
						}
						mp.propertyTypes.add(pt);
					}

					items = propertyMatchSourceList_.getSelection();
					mp.sources = new ArrayList<String>();
					for (int j = 0; j < items.length; j++) {
						mp.sources.add(items[j]);
					}

					items = propertyMatchUsageContextList_.getSelection();
					mp.usageContexts = new ArrayList<String>();
					for (int j = 0; j < items.length; j++) {
						mp.usageContexts.add(items[j]);
					}

					mp.propertyQualifier = propertyMatchPropertyQualifierName_
							.getText();
					mp.propertyQualifierValue = propertyMatchPropertyQualifierValue_
							.getText();

					restriction = mp;
					break;

				case 2:
					// restrict to codes
					if (codeRestriction_.getText().length() == 0) {
						dialog_.showError("Invalid Entry",
								"At least one code is required");
						codeRestriction_.forceFocus();
						return;
					}
					MatchingCode mc;
					if (restriction_ == null) {
						mc = new MatchingCode();
					} else {
						mc = (MatchingCode) restriction_;
					}

					mc.codes = codeRestriction_.getText();
					restriction = mc;

					break;

				case 3:
					// restrict to having properties
					HavingProperties hp;
					if (restriction_ == null) {
						hp = new HavingProperties();
					} else {
						hp = (HavingProperties) restriction_;
					}

					items = havingPropertyList_.getSelection();
					String[] havingPropertyTypes = havingPropertyTypeList_
							.getSelection();
					if (items.length == 0 && havingPropertyTypes.length == 0) {
						dialog_.showError("Invalid Entry",
								"Select one or more properties");
						havingPropertyList_.forceFocus();
						return;
					}

					hp.propertyTypes = new ArrayList<PropertyType>();
					for (int j = 0; j < havingPropertyTypes.length; j++) {
						PropertyType pt = null;
						if (havingPropertyTypes[j].equals(PropertyType.COMMENT
								.name())) {
							pt = PropertyType.COMMENT;
						} else if (havingPropertyTypes[j]
								.equals(PropertyType.DEFINITION.name())) {
							pt = PropertyType.DEFINITION;
						} else if (havingPropertyTypes[j]
								.equals(PropertyType.GENERIC.name())) {
							pt = PropertyType.GENERIC;
						} else if (havingPropertyTypes[j]
								.equals(PropertyType.PRESENTATION.name())) {
							pt = PropertyType.PRESENTATION;
						}
						hp.propertyTypes.add(pt);
					}

					hp.properties = new ArrayList<String>();
					for (int j = 0; j < items.length; j++) {
						hp.properties.add(items[j]);
					}
					items = havingSourceList_.getSelection();
					hp.sources = new ArrayList<String>();
					for (int j = 0; j < items.length; j++) {
						hp.sources.add(items[j]);
					}
					items = havingUsageContextList_.getSelection();
					hp.usageContexts = new ArrayList<String>();
					for (int j = 0; j < items.length; j++) {
						hp.usageContexts.add(items[j]);
					}

					hp.propertyQualifier = havingPropertyQualifierName_
							.getText();
					hp.propertyQualifierValue = havingPropertyQualifierValue_
							.getText();

					restriction = hp;

					break;
				case 4:
					// restrict to status
					Status s;
					if (restriction_ == null) {
						s = new Status();
					} else {
						s = (Status) restriction_;
					}

					s.conceptStatus = statusText.getText();
					String temp = statusActive.getText();
					if (temp.equals(ActiveOption.ACTIVE_ONLY.toString())) {
						s.activeOption = ActiveOption.ACTIVE_ONLY;
					} else if (temp.equals(ActiveOption.ALL.toString())) {
						s.activeOption = ActiveOption.ALL;
					} else if (temp.equals(ActiveOption.INACTIVE_ONLY
							.toString())) {
						s.activeOption = ActiveOption.INACTIVE_ONLY;
					}
					restriction = s;

					break;
				
			case 5:
                // restrict to status
                Anonymous a;
                if (restriction_ == null) {
                    a = new Anonymous();
                } else {
                    a = (Anonymous) restriction_;
                }

                temp = anonymousCombo.getText();
                if (temp.equals(AnonymousOption.ANONYMOUS_ONLY.toString())) {
                    a.anonymousOption = AnonymousOption.ANONYMOUS_ONLY;
                } else if (temp.equals(AnonymousOption.ALL.toString())) {
                    a.anonymousOption = AnonymousOption.ALL;
                } else if (temp.equals(AnonymousOption.NON_ANONYMOUS_ONLY
                        .toString())) {
                    a.anonymousOption = AnonymousOption.NON_ANONYMOUS_ONLY;
                }
                restriction = a;

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
}