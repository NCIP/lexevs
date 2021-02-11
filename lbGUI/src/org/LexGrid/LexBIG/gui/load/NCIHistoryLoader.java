
package org.LexGrid.LexBIG.gui.load;

import java.io.File;
import java.net.URI;

import org.LexGrid.LexBIG.Exceptions.LBException;
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

/**
 * GUI that allows loading NCI History files into LexBIG.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class NCIHistoryLoader extends LoadExportBaseShell {
	public NCIHistoryLoader(LB_GUI lb_gui) {
		super(lb_gui);
		try {
			Shell shell = new Shell(lb_gui_.getShell().getDisplay());
			shell.setSize(500, 400);
			shell.setImage(new Image(shell.getDisplay(), this.getClass()
					.getResourceAsStream("/icons/load.gif")));

			dialog_ = new DialogHandler(shell);

			org.LexGrid.LexBIG.Extensions.Load.NCIHistoryLoader loader = (org.LexGrid.LexBIG.Extensions.Load.NCIHistoryLoader) lb_gui_
					.getLbs().getServiceManager(null).getLoader(
							"NCIThesaurusHistoryLoader");

			shell.setText(loader.getName());

			buildGUI(shell, loader);

			shell.open();

			shell.addShellListener(shellListener);
		} catch (Exception e) {
			dialog_.showError("Unexpected Error", e.toString());
		}

	}

	private void buildGUI(Shell shell,
			final org.LexGrid.LexBIG.Extensions.Load.NCIHistoryLoader loader) {
		Group options = new Group(shell, SWT.NONE);
		options.setText("Load Options");
		shell.setLayout(new GridLayout());

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		options.setLayoutData(gd);

		GridLayout layout = new GridLayout(3, false);
		options.setLayout(layout);

		Label fileLabel = new Label(options, SWT.NONE);
		fileLabel.setText("History source file");

		final Text file = new Text(options, SWT.BORDER);
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
		file.setLayoutData(gd);

		Button fileChooseButton = Utility.getFileChooseButton(options, file,
				new String[] { "*.txt" },
				new String[] { "NCI History text file (txt)" });
		gd = new GridData(SWT.CENTER);
		gd.widthHint = 60;
		fileChooseButton.setLayoutData(gd);

		Label versionsFileLabel = new Label(options, SWT.NONE);
		versionsFileLabel.setText("Versions file");

		final Text versionsFile = new Text(options, SWT.BORDER);
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
		versionsFile.setLayoutData(gd);

		Button versionsFileChooseButton = Utility.getFileChooseButton(options,
				versionsFile, new String[] { "*.txt" },
				new String[] { "Versions text file (txt)" });
		gd = new GridData(SWT.CENTER);
		gd.widthHint = 60;
		versionsFileChooseButton.setLayoutData(gd);

		Label appendLabel = new Label(options, SWT.None);
		appendLabel.setText("Append to existing history");
		appendLabel
				.setToolTipText("History will be replaced if you do not check this option");

		final Button append = new Button(options, SWT.CHECK);
		append.setSelection(false);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		append.setLayoutData(gd);

		final Button load = new Button(options, SWT.PUSH);
		load.setText("Load");
		gd = new GridData(GridData.CENTER);
		gd.widthHint = 60;
		load.setLayoutData(gd);

		load.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {

				URI uri = null;
				URI versionsURI = null;

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
						dialog_
								.showError("Path Error",
										"No file could be located at this file location");
						return;
					}
				}

				// check the versions file
				theFile = new File(versionsFile.getText());

				if (theFile.exists()) {
					versionsURI = theFile.toURI();
				} else {
					// is it a valid URI (like http://something)
					try {
						versionsURI = new URI(file.getText());
						versionsURI.toURL().openConnection();
					} catch (Exception e) {
						dialog_
								.showError("Path Error",
										"No file could be located at your versions file location.");
						return;
					}
				}

				try {
					setLoading(true);
					loader.load(uri, versionsURI, append.getSelection(), false, true);
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