
package org.LexGrid.LexBIG.gui.logging;

import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;
import org.apache.log4j.varia.LevelRangeFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.lexevs.system.constants.SystemVariables;

/**
 * A window to view the log4j log output.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 * @version subversion $Revision: 1052 $ checked in on $Date: 2006-01-30
 *          11:42:24 +0000 (Mon, 30 Jan 2006) $
 */
public class LogViewer {
	private static Logger log_ = Logger.getLogger("LB_GUI_LOGGER");
	CustomStringWriter writer_;
	LogViewerOptions logViewerOptions_;
	protected Log[] logs_ = new Log[] {
			new Log("ROOT LOGGER", "Log everything", true, "WARN"),
			new Log("LB_GUI_LOGGER", "LexGUI Errors.", true, "INFO"),
			new Log("LB_VSGUI_LOGGER", "LexVSGUI Errors.", true, "INFO"),
			new Log("<special>", "LexGrid Logging.", true, "INFO"),
			new Log("LB_LOAD_LOGGER", "LexGrid Loading messages", true, "DEBUG"),
			new Log(
					"org.LexGrid.util.sql.sqlReconnect.WrappedPreparedStatement",
					"SQL Statements", false, "DEBUG") };

	private Shell shell_;

	public LogViewer(Shell parent) throws Exception {
		shell_ = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE);
		shell_.setText("LexBIG Log");
		shell_.setSize(600, 500);
		shell_.setImage(new Image(shell_.getDisplay(), this.getClass()
				.getResourceAsStream("/icons/log.gif")));

		shell_.addListener(SWT.Close, new Listener() {
			public void handleEvent(Event arg0) {
				arg0.doit = false;
				shell_.setVisible(false);
			}
		});

		buildComponents();
		writer_ = new CustomStringWriter(styledTextLog_);
		LogManager.getRootLogger().setLevel(Level.DEBUG);
		configureLogs();
	}

	@SuppressWarnings("unchecked")
	public void removeOpenLoggers() {
		// Enumeration<Logger> logEnum = LogManager.getCurrentLoggers();
		// while (logEnum.hasMoreElements())
		// {
		// ((Logger) logEnum.nextElement()).removeAllAppenders();
		// }
		if (!SystemVariables.isDebugEnabled()) {
			LogManager.getRootLogger().removeAllAppenders();
		}
	}

	public void configureLogs() {
		// removeOpenLoggers();
		SystemVariables.debugEnableOverrideRemove();
		for (int i = 0; i < logs_.length; i++) {
			if (logs_[i].enabled) {
				if ((logs_[i].log.equals("ROOT LOGGER") || logs_[i].log
						.equals("<special>"))
						&& logs_[i].level.equals("DEBUG")) {
					SystemVariables.debugEnableOverride();
				}
				if (logs_[i].log.equals("ROOT LOGGER")) {
					setupLogger(LogManager.getRootLogger(), logs_[i].level);
				} else if (logs_[i].log.equals("<special>")) {
					// If they specify the LexFOO loggers - set up all 5 at the
					// appropriate level.

					setupLogger(LogManager.getLogger("LB_FATAL_LOGGER"),
							logs_[i].level);
					setupLogger(LogManager.getLogger("LB_ERROR_LOGGER"),
							logs_[i].level);
					setupLogger(LogManager.getLogger("LB_WARN_LOGGER"),
							logs_[i].level);
					setupLogger(LogManager.getLogger("LB_INFO_LOGGER"),
							logs_[i].level);
					setupLogger(LogManager.getLogger("LB_DEBUG_LOGGER"),
							logs_[i].level);
				} else {
					setupLogger(LogManager.getLogger(logs_[i].log),
							logs_[i].level);
				}
			}
		}
		log_.info("Logger reconfigured");
	}

	private void setupLogger(Logger logger, String level) {

		logger.setAdditivity(true);
		logger.setLevel(Level.toLevel(level));
		Appender appender = new WriterAppender(
				new PatternLayout("%p %c - %m%n"), writer_);
		LevelRangeFilter tempFilter = new LevelRangeFilter();
		tempFilter.setLevelMin(Level.toLevel(level));
		appender.addFilter(tempFilter);
		logger.addAppender(appender);
	}

	StyledText styledTextLog_;

	Button configure_;
	Button clear_;

	private void buildComponents() {
		GridLayout layout = new GridLayout(2, true);
		shell_.setLayout(layout);

		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;

		styledTextLog_ = new StyledText(shell_, SWT.WRAP | SWT.V_SCROLL
				| SWT.BORDER);
		styledTextLog_.setEditable(false);
		styledTextLog_.setLayoutData(gd);

		gd = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);

		configure_ = new Button(shell_, SWT.PUSH);
		configure_.setText("Configure");
		configure_.setLayoutData(gd);
		configure_.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			public void widgetSelected(SelectionEvent arg0) {
				if (logViewerOptions_ == null) {
					try {
						logViewerOptions_ = new LogViewerOptions(
								LogViewer.this, shell_);
					} catch (Exception e1) {
						log_.error("Problem creating log configurator", e1);
					}
				}
				logViewerOptions_.setVisible(true);
			}

		});

		clear_ = new Button(shell_, SWT.PUSH);
		clear_.setText("Clear");
		clear_.setLayoutData(gd);
		clear_.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent arg0) {

			}

			public void widgetSelected(SelectionEvent arg0) {
				writer_.getBuffer().setLength(0);
				styledTextLog_.setText("");
			}

		});
	}

	public class Log {
		public String description;
		public String log;
		public boolean enabled;
		public String level;

		public Log(String log, String description) {
			this.log = log;
			this.description = description;
			this.enabled = false;
			this.level = "DEBUG";
		}

		public Log(String log, String description, boolean enabled) {
			this.log = log;
			this.description = description;
			this.enabled = enabled;
			this.level = "DEBUG";
		}

		public Log(String log, String description, boolean enabled, String level) {
			this.log = log;
			this.description = description;
			this.enabled = enabled;
			this.level = level;
		}
	}

	public void setVisible(boolean visible) {
		shell_.setVisible(visible);
	}
}