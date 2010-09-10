/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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

import java.io.IOException;
import java.util.Properties;

import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.gui.Constants;
import org.LexGrid.LexBIG.gui.DialogHandler;
import org.LexGrid.LexBIG.gui.LB_GUI;
import org.LexGrid.LexBIG.gui.LB_VSD_GUI;
import org.LexGrid.LexBIG.gui.Utility;
import org.LexGrid.util.config.PropertiesUtility;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.lexevs.system.ResourceManager;

/**
 * This GUI allows you to display and edit all of the options from a LexBIG
 * config file.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class Configure {
	DialogHandler dialog_;
	LB_GUI lb_gui_;
	LB_VSD_GUI lb_vd_gui_;

	Button debugEnabled_, emailErrors_, apiLoggingEnabled_;
	Text maxConnectionsPerDB, cacheSize_, iteratorIdleTime_, maxResultSize_,
			registryFile_, indexLocation_, jarFileLocation_, fileLocation_,
			dbPrefix_, dbParam_, dbUser_, dbPassword_, logLocation_,
			eraseLogsAfter_, smtpServer_, emailTo_, relativePathBase_;
	Combo logChange_, dbURL_, dbDriver_;

	public Configure(LB_GUI lb_gui, Properties currentProperties) {
		lb_gui_ = lb_gui;
		Shell shell = new Shell(lb_gui_.getShell(), SWT.APPLICATION_MODAL
				| SWT.DIALOG_TRIM | SWT.RESIZE);
		shell.setSize(640, 480);
		shell.setImage(new Image(shell.getDisplay(), this.getClass()
				.getResourceAsStream("/icons/config.gif")));

		dialog_ = new DialogHandler(shell);

		shell.setText("Configure LexBIG");

		init(shell, currentProperties);

		shell.open();
	}
	
	public Configure(LB_VSD_GUI lb_vd_gui, Properties currentProperties) {
	    lb_vd_gui_ = lb_vd_gui;
        Shell shell = new Shell(lb_vd_gui_.getShell(), SWT.APPLICATION_MODAL
                | SWT.DIALOG_TRIM | SWT.RESIZE);
        shell.setSize(640, 480);
        shell.setImage(new Image(shell.getDisplay(), this.getClass()
                .getResourceAsStream("/icons/config.gif")));

        dialog_ = new DialogHandler(shell);

        shell.setText("Configure LexBIG");

        init(shell, currentProperties);

        shell.open();
    }

	private void init(final Shell shell, Properties currentProperties) {
		shell.setLayout(new GridLayout());

		Group readFromFileG = new Group(shell, SWT.NONE);
		readFromFileG.setText("Read Options From File");

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		readFromFileG.setLayoutData(gd);

		GridLayout layout = new GridLayout(3, false);
		readFromFileG.setLayout(layout);

		final Button fileBrowse = new Button(readFromFileG, SWT.PUSH);
		fileBrowse.setText("Read config file");
		gd = new GridData();
		fileBrowse.setLayoutData(gd);

		fileBrowse.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				FileDialog fileChooser = new FileDialog(fileBrowse.getShell(),
						SWT.OPEN);
				fileChooser.setText("Choose File");
				// fileChooser.setFilterPath(currentDir);
				fileChooser.setFilterExtensions(new String[] { "*.props" });
				fileChooser
						.setFilterNames(new String[] { "LexBIG config file (props)" });
				String filename = fileChooser.open();

				try {
					if (filename != null && filename.length() > 0) {
						fileLocation_.setText(filename);
						Properties props = PropertiesUtility
								.loadPropertiesFromFileOrURL(filename);
						loadValues(props);
					}
				} catch (IOException e) {
					dialog_.showError("Error reading file",
							"There was an error reading the config file - "
									+ e.toString());
				} catch (Exception e) {
					dialog_
							.showError("Invalid properties file",
									"The selected file is not a valid LexBIG properties file.");
				}
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				// 
			}

		});

		Utility.makeLabel(readFromFileG, " Options read from:");
		fileLocation_ = Utility.makeText(readFromFileG);
		fileLocation_.setEnabled(false);

		ScrolledComposite sc = new ScrolledComposite(shell, SWT.V_SCROLL
				| SWT.BORDER);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		gd = new GridData(GridData.FILL_BOTH);
		sc.setLayoutData(gd);

		Composite currentOptions = new Composite(sc, SWT.NONE);
		sc.setContent(currentOptions);

		gd = new GridData(GridData.FILL_BOTH);
		currentOptions.setLayoutData(gd);
		currentOptions.setLayout(new GridLayout(1, false));

		Group generalOptions = new Group(currentOptions, SWT.None);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		generalOptions.setLayoutData(gd);
		generalOptions.setText("General Options");

		generalOptions.setLayout(new GridLayout(4, false));

		Utility.makeLabel(generalOptions, "Max connections per DB");
		maxConnectionsPerDB = Utility.makeText(generalOptions);

		Utility.makeLabel(generalOptions, "Cache Size");
		cacheSize_ = Utility.makeText(generalOptions);

		Utility.makeLabel(generalOptions, "Iterator max idle time");
		iteratorIdleTime_ = Utility.makeText(generalOptions);

		Utility.makeLabel(generalOptions, "Max Result Size");
		maxResultSize_ = Utility.makeText(generalOptions);

		Utility.makeLabel(generalOptions, "Relative Base Path");
		relativePathBase_ = Utility.makeText(generalOptions, 3);

		Utility.makeLabel(generalOptions, "Registry file location");
		registryFile_ = Utility.makeText(generalOptions, 3);

		Utility.makeLabel(generalOptions, "Index file location");
		indexLocation_ = Utility.makeText(generalOptions, 3);

		Utility.makeLabel(generalOptions, "Jar file location");
		jarFileLocation_ = Utility.makeText(generalOptions, 3);

		// Norm has been disabled.
		// Group normOptions = new Group(currentOptions, SWT.None);
		// gd = new GridData(GridData.FILL_HORIZONTAL);
		// normOptions.setLayoutData(gd);
		// normOptions.setText("Normalization Options");
		//
		// normOptions.setLayout(new GridLayout(4, false));
		//
		// Utility.makeLabel(normOptions, "");
		// normQueriesEnabled_ = new Button(normOptions, SWT.CHECK);
		// gd = new GridData();
		// gd.horizontalSpan = 3;
		// normQueriesEnabled_.setLayoutData(gd);
		// normQueriesEnabled_.setText("Normalized Queries Enabled");
		// normQueriesEnabled_.addSelectionListener(new SelectionListener()
		// {
		//
		// public void widgetSelected(SelectionEvent arg0)
		// {
		// normConfigFile_.setEnabled(normQueriesEnabled_.getSelection());
		// }
		//
		// public void widgetDefaultSelected(SelectionEvent arg0)
		// {
		// }
		//
		// });
		//
		// Utility.makeLabel(normOptions, "Norm Config File");
		// normConfigFile_ = Utility.makeText(normOptions, 3);
		// normConfigFile_.setEnabled(false);

		Group databaseOptions = new Group(currentOptions, SWT.None);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		databaseOptions.setLayoutData(gd);
		databaseOptions.setText("Database Options");

		databaseOptions.setLayout(new GridLayout(4, false));

		Utility.makeLabel(databaseOptions, "DB URL");
		dbURL_ = new Combo(databaseOptions, SWT.DROP_DOWN);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		dbURL_.setLayoutData(gd);
		dbURL_.setItems(Constants.singleDBModeServers);

		Utility.makeLabel(databaseOptions, "DB Driver");
		dbDriver_ = new Combo(databaseOptions, SWT.DROP_DOWN);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		dbDriver_.setLayoutData(gd);
		dbDriver_.setItems(Constants.drivers);

		Utility.makeLabel(databaseOptions, "DB Prefix");
		dbPrefix_ = Utility.makeText(databaseOptions);

		Utility.makeLabel(databaseOptions, "DB Paramaters");
		dbParam_ = Utility.makeText(databaseOptions);

		Utility.makeLabel(databaseOptions, "DB User");
		dbUser_ = Utility.makeText(databaseOptions);

		Utility.makeLabel(databaseOptions, "DB Password");
		dbPassword_ = Utility.makeText(databaseOptions);

		Group loggingOptions = new Group(currentOptions, SWT.None);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		loggingOptions.setLayoutData(gd);
		loggingOptions.setText("Logging Options");

		loggingOptions.setLayout(new GridLayout(4, false));

		Utility.makeLabel(loggingOptions, "Log File Location");
		logLocation_ = Utility.makeText(loggingOptions, 3);

		Utility.makeLabel(loggingOptions, "");
		debugEnabled_ = new Button(loggingOptions, SWT.CHECK);
		gd = new GridData();
		gd.horizontalSpan = 1;
		debugEnabled_.setLayoutData(gd);
		debugEnabled_.setText("Debug Logging Enabled");

		apiLoggingEnabled_ = new Button(loggingOptions, SWT.CHECK);
		gd = new GridData();
		gd.horizontalSpan = 2;
		apiLoggingEnabled_.setLayoutData(gd);
		apiLoggingEnabled_.setText("API Logging Enabled");

		Utility.makeLabel(loggingOptions, "Log Change");
		logChange_ = new Combo(loggingOptions, SWT.DROP_DOWN);
		logChange_.setItems(new String[] { "daily", "weekly", "monthly", "1",
				"5", "10", "50" });
		gd = new GridData(GridData.FILL_HORIZONTAL);
		logChange_.setLayoutData(gd);

		Utility.makeLabel(loggingOptions, "Erase Logs After");
		eraseLogsAfter_ = Utility.makeText(loggingOptions);

		Utility.makeLabel(loggingOptions, "");
		emailErrors_ = new Button(loggingOptions, SWT.CHECK);
		emailErrors_.setText("Email Errors");
		gd = new GridData();
		gd.horizontalSpan = 3;
		emailErrors_.setLayoutData(gd);
		emailErrors_.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				if (emailErrors_.getSelection()) {
					smtpServer_.setEnabled(true);
					emailTo_.setEnabled(true);
				} else {
					smtpServer_.setEnabled(false);
					emailTo_.setEnabled(false);
				}
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				//
			}

		});

		Utility.makeLabel(loggingOptions, "SMTP Server");
		smtpServer_ = Utility.makeText(loggingOptions);
		smtpServer_.setEnabled(false);

		Utility.makeLabel(loggingOptions, "Email To");
		emailTo_ = Utility.makeText(loggingOptions);
		emailTo_.setEnabled(false);

		sc.setMinSize(currentOptions.computeSize(SWT.DEFAULT, SWT.DEFAULT));

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
				try {
					Properties props = valuesToProps();
					ResourceManager.reInit(props);
					if (Configure.this.lb_gui_ != null)
					{
            			Configure.this.lb_gui_.getNewLBS();
            			Configure.this.lb_gui_.refreshCodingSchemeList();
            			Configure.this.lb_gui_.setCurrentProperties(props);
					}
					else if (Configure.this.lb_vd_gui_ != null)
					{
					    Configure.this.lb_vd_gui_.getNewLBS();
	                    Configure.this.lb_vd_gui_.refreshValueDomainList();
	                    Configure.this.lb_vd_gui_.setCurrentProperties(props);
					}
					shell.dispose();
				} catch (LBInvocationException e) {
					dialog_.showError("Error configuring LexBIG",
							"There was a problem setting up LexBIG - "
									+ e.toString());
				}
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

		if (currentProperties != null) {
			loadValues(currentProperties);
		}

	}

	private void loadValues(Properties props) {
		if (props.getProperty("CONFIG_FILE_LOCATION") != null) {
			fileLocation_.setText(props.getProperty("CONFIG_FILE_LOCATION"));
		}
		String relPathStart = System.getProperty("LG_BASE_PATH");
		if (relPathStart == null || relPathStart.length() == 0) {
			relPathStart = props.getProperty("LG_BASE_PATH");
			if (relPathStart == null) {
				relPathStart = "";
			}
		}

		relativePathBase_.setText(relPathStart);
		// Norm has been disabled in the implementation.
		// normQueriesEnabled_.setSelection(new Boolean((String)
		// props.get("NORMALIZED_QUERIES_ENABLED")).booleanValue());
		// normConfigFile_.setText((String)
		// props.get("LVG_NORM_CONFIG_FILE_ABSOLUTE"));
		maxConnectionsPerDB.setText((String) props
				.get("MAX_CONNECTIONS_PER_DB"));
		cacheSize_.setText((String) props.get("CACHE_SIZE"));
		iteratorIdleTime_.setText((String) props.get("ITERATOR_IDLE_TIME"));
		maxResultSize_.setText((String) props.get("MAX_RESULT_SIZE"));
		registryFile_.setText((String) props.get("REGISTRY_FILE"));
		indexLocation_.setText((String) props.get("INDEX_LOCATION"));
		jarFileLocation_.setText((String) props.get("JAR_FILE_LOCATION"));
		dbURL_.setText((String) props.get("DB_URL"));
		dbPrefix_.setText((String) props.get("DB_PREFIX"));
		dbParam_.setText((String) props.get("DB_PARAM"));
		dbDriver_.setText((String) props.get("DB_DRIVER"));
		dbUser_.setText((String) props.get("DB_USER"));
		dbPassword_.setText((String) props.get("DB_PASSWORD"));
		logLocation_.setText((String) props.get("LOG_FILE_LOCATION"));
		debugEnabled_.setSelection(new Boolean((String) props
				.get("DEBUG_ENABLED")).booleanValue());
		apiLoggingEnabled_.setSelection(new Boolean((String) props
				.get("API_LOG_ENABLED")).booleanValue());
		logChange_.setText((String) props.get("LOG_CHANGE"));
		eraseLogsAfter_.setText((String) props.get("ERASE_LOGS_AFTER"));
		emailErrors_.setSelection(new Boolean((String) props
				.get("EMAIL_ERRORS")).booleanValue());
		smtpServer_.setText((String) props.get("SMTP_SERVER"));
		emailTo_.setText((String) props.get("EMAIL_TO"));

	}

	private Properties valuesToProps() {
		Properties props = new Properties();
		props.put("CONFIG_FILE_LOCATION", fileLocation_.getText());
		props.put("LG_BASE_PATH", relativePathBase_.getText());
		// Norm has been disabled.
		// props.put("NORMALIZED_QUERIES_ENABLED", new
		// Boolean(normQueriesEnabled_.getSelection()).toString());
		// props.put("LVG_NORM_CONFIG_FILE_ABSOLUTE",
		// normConfigFile_.getText());
		props.put("MAX_CONNECTIONS_PER_DB", maxConnectionsPerDB.getText());
		props.put("CACHE_SIZE", cacheSize_.getText());
		props.put("ITERATOR_IDLE_TIME", iteratorIdleTime_.getText());
		props.put("MAX_RESULT_SIZE", maxResultSize_.getText());
		props.put("REGISTRY_FILE", registryFile_.getText());
		props.put("INDEX_LOCATION", indexLocation_.getText());
		props.put("JAR_FILE_LOCATION", jarFileLocation_.getText());
		props.put("DB_URL", dbURL_.getText());
		props.put("DB_PREFIX", dbPrefix_.getText());
		props.put("DB_PARAM", dbParam_.getText());
		props.put("DB_DRIVER", dbDriver_.getText());
		props.put("DB_USER", dbUser_.getText());
		props.put("DB_PASSWORD", dbPassword_.getText());
		props.put("LOG_FILE_LOCATION", logLocation_.getText());
		props.put("DEBUG_ENABLED", new Boolean(debugEnabled_.getSelection())
				.toString());
		props.put("API_LOG_ENABLED", new Boolean(apiLoggingEnabled_
				.getSelection()).toString());
		props.put("LOG_CHANGE", logChange_.getText());
		props.put("ERASE_LOGS_AFTER", eraseLogsAfter_.getText());
		props.put("EMAIL_ERRORS", new Boolean(emailErrors_.getSelection())
				.toString());
		props.put("SMTP_SERVER", smtpServer_.getText());
		props.put("EMAIL_TO", emailTo_.getText());

		return props;
	}
}