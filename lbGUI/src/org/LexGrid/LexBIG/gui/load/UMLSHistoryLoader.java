
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

public class UMLSHistoryLoader extends LoadExportBaseShell {

	public UMLSHistoryLoader(LB_GUI lb_gui) {
		super(lb_gui);
		try {
			Shell shell = new Shell(lb_gui_.getShell().getDisplay());
			shell.setSize(500, 400);
			shell.setImage(new Image(shell.getDisplay(), this.getClass()
					.getResourceAsStream("/icons/load.gif")));

			dialog_ = new DialogHandler(shell);

			org.LexGrid.LexBIG.Extensions.Load.UMLSHistoryLoader loader = (org.LexGrid.LexBIG.Extensions.Load.UMLSHistoryLoader) lb_gui_
					.getLbs().getServiceManager(null).getLoader(
							"NCIMetaHistoryLoader");

			shell.setText(loader.getName());

			buildGUI(shell, loader);

			shell.open();

			shell.addShellListener(shellListener);
		} catch (Exception e) {
			dialog_.showError("Unexpected Error", e.toString());
		}
	}

	private void buildGUI(Shell shell,
			final org.LexGrid.LexBIG.Extensions.Load.UMLSHistoryLoader loader) {
		Group options = new Group(shell, SWT.NONE);
		options.setText("Load Options");
		shell.setLayout(new GridLayout());

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		options.setLayoutData(gd);

		GridLayout layout = new GridLayout(3, false);
		options.setLayout(layout);

		Label folderLabel = new Label(options, SWT.NONE);
		folderLabel.setText("Source folder");

		final Text file = new Text(options, SWT.BORDER);
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
		file.setLayoutData(gd);

		Button folderChooseButton = Utility
				.getFolderChooseButton(options, file);
		gd = new GridData(SWT.CENTER);
		gd.widthHint = 60;
		folderChooseButton.setLayoutData(gd);

		Label appendLabel = new Label(options, SWT.None);
		appendLabel.setText("Append to existing history");
		appendLabel
				.setToolTipText("History will be replaced if you do not check this option");

		final Button append = new Button(options, SWT.CHECK);
		append.setSelection(false);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		append.setLayoutData(gd);

		final Button load = new Button(options, SWT.PUSH);
		load.setText("Load");
		gd = new GridData(GridData.HORIZONTAL_ALIGN_END);
		gd.widthHint = 60;
		load.setLayoutData(gd);

		load.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {

				URI uri = null;

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

				try {

					setLoading(true);
					loader.load(uri, append.getSelection(), false, true);
					load.setEnabled(false);
				} catch (LBException e) {
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