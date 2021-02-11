
package org.LexGrid.LexBIG.gui.config;

import java.util.Properties;

import org.LexGrid.LexBIG.gui.DialogHandler;
import org.LexGrid.LexBIG.gui.LB_GUI;
import org.LexGrid.LexBIG.gui.LB_VSD_GUI;
import org.LexGrid.LexBIG.gui.Utility;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

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
			eraseLogsAfter_, smtpServer_, emailTo_, relativePathBase_,
            logChange_, dbURL_, dbDriver_;

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
		maxConnectionsPerDB.setEditable(false);

		Utility.makeLabel(generalOptions, "Cache Size");
		cacheSize_ = Utility.makeText(generalOptions);
		cacheSize_.setEditable(false);
		
		Utility.makeLabel(generalOptions, "Iterator max idle time");
		iteratorIdleTime_ = Utility.makeText(generalOptions);
		iteratorIdleTime_.setEditable(false);
		
		Utility.makeLabel(generalOptions, "Max Result Size");
		maxResultSize_ = Utility.makeText(generalOptions);
		maxResultSize_.setEditable(false);

		Utility.makeLabel(generalOptions, "Relative Base Path");
		relativePathBase_ = Utility.makeText(generalOptions, 3);
		relativePathBase_.setEditable(false);

		Utility.makeLabel(generalOptions, "Registry file location");
		registryFile_ = Utility.makeText(generalOptions, 3);
		registryFile_.setEditable(false);

		Utility.makeLabel(generalOptions, "Index file location");
		indexLocation_ = Utility.makeText(generalOptions, 3);
		indexLocation_.setEditable(false);

		Utility.makeLabel(generalOptions, "Jar file location");
		jarFileLocation_ = Utility.makeText(generalOptions, 3);
		jarFileLocation_.setEditable(false);

		Group databaseOptions = new Group(currentOptions, SWT.None);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		databaseOptions.setLayoutData(gd);
		databaseOptions.setText("Database Options");

		databaseOptions.setLayout(new GridLayout(4, false));

		Utility.makeLabel(databaseOptions, "DB URL");
		dbURL_ = Utility.makeText(databaseOptions);
		dbURL_.setEditable(false);

		Utility.makeLabel(databaseOptions, "DB Driver");
		dbDriver_ = Utility.makeText(databaseOptions);
		dbDriver_.setEditable(false);

		Utility.makeLabel(databaseOptions, "DB Prefix");
		dbPrefix_ = Utility.makeText(databaseOptions);
		dbPrefix_.setEditable(false);

		Utility.makeLabel(databaseOptions, "DB Paramaters");
		dbParam_ = Utility.makeText(databaseOptions);
		dbParam_.setEditable(false);

		Utility.makeLabel(databaseOptions, "DB User");
		dbUser_ = Utility.makeText(databaseOptions);
		dbUser_.setEditable(false);

		Utility.makeLabel(databaseOptions, "DB Password");
		dbPassword_ = Utility.makeText(databaseOptions);
		dbPassword_.setEditable(false);

		Group loggingOptions = new Group(currentOptions, SWT.None);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		loggingOptions.setLayoutData(gd);
		loggingOptions.setText("Logging Options");

		loggingOptions.setLayout(new GridLayout(4, false));

		Utility.makeLabel(loggingOptions, "Log File Location");
		logLocation_ = Utility.makeText(loggingOptions, 3);
		logLocation_.setEditable(false);

		Utility.makeLabel(loggingOptions, "");
		debugEnabled_ = new Button(loggingOptions, SWT.CHECK);
		gd = new GridData();
		gd.horizontalSpan = 1;
		debugEnabled_.setLayoutData(gd);
		debugEnabled_.setText("Debug Logging Enabled");
		debugEnabled_.setEnabled(false);

		apiLoggingEnabled_ = new Button(loggingOptions, SWT.CHECK);
		gd = new GridData();
		gd.horizontalSpan = 2;
		apiLoggingEnabled_.setLayoutData(gd);
		apiLoggingEnabled_.setText("API Logging Enabled");
		apiLoggingEnabled_.setEnabled(false);

		Utility.makeLabel(loggingOptions, "Log Change");
		logChange_ = Utility.makeText(loggingOptions);
		logChange_.setEditable(false);

		Utility.makeLabel(loggingOptions, "Erase Logs After");
		eraseLogsAfter_ = Utility.makeText(loggingOptions);
		eraseLogsAfter_.setEditable(false);

		Utility.makeLabel(loggingOptions, "");
		emailErrors_ = new Button(loggingOptions, SWT.CHECK);
		emailErrors_.setText("Email Errors");
		gd = new GridData();
		gd.horizontalSpan = 3;
		emailErrors_.setLayoutData(gd);
		emailErrors_.setEnabled(false);
			
		Utility.makeLabel(loggingOptions, "SMTP Server");
		smtpServer_ = Utility.makeText(loggingOptions);
		smtpServer_.setEditable(false);

		Utility.makeLabel(loggingOptions, "Email To");
		emailTo_ = Utility.makeText(loggingOptions);
		emailTo_.setEditable(false);

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