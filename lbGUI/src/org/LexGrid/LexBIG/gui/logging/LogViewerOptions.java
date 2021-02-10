
package org.LexGrid.LexBIG.gui.logging;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * A dialog to configure what log4j writes to the LogViewer window.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 * @version subversion $Revision: 1052 $ checked in on $Date: 2006-01-30
 *          11:42:24 +0000 (Mon, 30 Jan 2006) $
 */
public class LogViewerOptions {
	LogViewer logViewer_;
	private Shell shell_;

	public LogViewerOptions(LogViewer logViewer, Shell parent) throws Exception {
		logViewer_ = logViewer;
		shell_ = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL
				| SWT.RESIZE | SWT.BORDER);
		shell_.setText("Converter Log Viewer Options");
		shell_.setSize(300, 300);
		shell_.setImage(new Image(shell_.getDisplay(), this.getClass()
				.getResourceAsStream("/icons/config.gif")));

		shell_.addListener(SWT.Close, new Listener() {
			public void handleEvent(Event arg0) {
				arg0.doit = false;
				shell_.setVisible(false);
			}
		});

		buildComponents();
	}

	Button ok_;
	Button cancel_;
	Button help_;
	Button[] loggers_;
	Combo[] loggerLevels_;

	private void buildComponents() {
		GridLayout layout = new GridLayout(3, true);
		shell_.setLayout(layout);

		String[] temp = new String[] { "DEBUG", "INFO", "WARN", "ERROR",
				"FATAL" };

		loggers_ = new Button[logViewer_.logs_.length];
		loggerLevels_ = new Combo[logViewer_.logs_.length];

		for (int i = 0; i < logViewer_.logs_.length; i++) {
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 2;
			loggers_[i] = new Button(shell_, SWT.CHECK);
			loggers_[i].setLayoutData(gd);
			loggers_[i].setText(logViewer_.logs_[i].description);
			loggers_[i].setToolTipText(logViewer_.logs_[i].log);

			gd = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);

			loggerLevels_[i] = new Combo(shell_, SWT.BORDER);
			loggerLevels_[i].setItems(temp);
			loggerLevels_[i].setLayoutData(gd);

			if (logViewer_.logs_[i].log.indexOf("ROOT") != -1) {
				loggers_[i].addSelectionListener(new SelectionListener() {

					public void widgetDefaultSelected(SelectionEvent arg0) {
					}

					public void widgetSelected(SelectionEvent arg0) {
						if (((Button) arg0.getSource()).getSelection()) {
							for (int j = 0; j < loggers_.length; j++) {
								if (logViewer_.logs_[j].log.indexOf("ROOT") == -1) {
									loggers_[j].setSelection(false);
								}
							}
						}
					}

				});

			}
		}
		loggers_[0].setSelection(true);

		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_CENTER
				| GridData.FILL_VERTICAL | GridData.VERTICAL_ALIGN_END);
		gd.widthHint = 80;

		ok_ = new Button(shell_, SWT.PUSH);
		ok_.setText("Ok");
		ok_.setLayoutData(gd);
		ok_.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			public void widgetSelected(SelectionEvent arg0) {
				for (int i = 0; i < logViewer_.logs_.length; i++) {
					logViewer_.logs_[i].enabled = loggers_[i].getSelection();
					logViewer_.logs_[i].level = (String) loggerLevels_[i]
							.getText();
				}
				logViewer_.configureLogs();
				LogViewerOptions.this.setVisible(false);
			}

		});

		cancel_ = new Button(shell_, SWT.PUSH);
		cancel_.setText("Cancel");
		cancel_.setLayoutData(gd);
		cancel_.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			public void widgetSelected(SelectionEvent arg0) {
				LogViewerOptions.this.setVisible(false);
			}

		});

		help_ = new Button(shell_, SWT.PUSH);
		help_.setText("Help");
		help_.setLayoutData(gd);
		help_.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			public void widgetSelected(SelectionEvent arg0) {
				MessageBox messageBox = new MessageBox(shell_,
						SWT.ICON_INFORMATION | SWT.OK);
				messageBox
						.setMessage("The first option, 'Log everything' will catch log messages from all components.\n"
								+ "If you want more detailed logs from just an individual component - enable the"
								+ "\ndesired component and set the level to the verbosity level that you want."
								+ "\n\nAdditionally, two log files are always written to disk:"
								+ "\n'convert debug log.log' - contains everything"
								+ "\n'convert error log.log' - only contains errors."
								+ "\n\n These will be in the folder where you installed the converter.");
				messageBox.setText("About");
				messageBox.open();
			}

		});

	}

	public void setVisible(boolean visible) {
		if (visible) {
			for (int i = 0; i < logViewer_.logs_.length; i++) {
				loggers_[i].setSelection(logViewer_.logs_[i].enabled);
				String level = logViewer_.logs_[i].level;
				int index = 0;
				if (level.equals("INFO")) {
					index = 1;
				} else if (level.equals("WARN")) {
					index = 2;
				} else if (level.equals("ERROR")) {
					index = 3;
				} else if (level.equals("FATAL")) {
					index = 4;
				}

				loggerLevels_[i].select(index);
			}
		}

		shell_.setVisible(visible);
	}

}