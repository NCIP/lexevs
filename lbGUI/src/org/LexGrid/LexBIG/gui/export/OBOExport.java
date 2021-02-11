
package org.LexGrid.LexBIG.gui.export;

import java.io.File;
import java.net.URI;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Export.OBO_Exporter;
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
 * GUI that allows loading OBO files into LexBIG.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class OBOExport extends LoadExportBaseShell {
	public OBOExport(LB_GUI lb_gui, AbsoluteCodingSchemeVersionReference source) {
		super(lb_gui);
		try {
			Shell shell = new Shell(lb_gui_.getShell().getDisplay());
			shell.setSize(500, 400);
			shell.setImage(new Image(shell.getDisplay(), this.getClass()
					.getResourceAsStream("/icons/load.gif")));

			dialog_ = new DialogHandler(shell);

			OBO_Exporter exporter = (OBO_Exporter) lb_gui_.getLbs()
					.getServiceManager(null).getExporter("OBOExport");

			shell.setText(exporter.getName());

			buildGUI(shell, exporter, source);

			shell.open();

			shell.addShellListener(shellListener);
		} catch (Exception e) {
			dialog_.showError("Unexpected Error", e.toString());
		}

	}

	private void buildGUI(Shell shell, final OBO_Exporter exporter,
			final AbsoluteCodingSchemeVersionReference source) {
		Group options = new Group(shell, SWT.NONE);
		options.setText("Export Options");
		shell.setLayout(new GridLayout());

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		options.setLayoutData(gd);

		GridLayout layout = new GridLayout(5, false);
		options.setLayout(layout);

		Label fileLabel = new Label(options, SWT.NONE);
		fileLabel.setText("Save to file");

		final Text file = new Text(options, SWT.BORDER);
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		file.setLayoutData(gd);

		Button fileChooseButton = Utility.getFileSaveButton(options, file, new String[] {".obo"}, new String[] {".obo"});
		gd = new GridData(SWT.CENTER);
		gd.widthHint = 60;
		fileChooseButton.setLayoutData(gd);

		Utility.makeLabel(options, "Overwrite");

		final Button overwrite = new Button(options, SWT.CHECK);
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		overwrite.setLayoutData(gd);
		overwrite.setSelection(true);

		final Button export = new Button(options, SWT.PUSH);
		export.setText("Export");
		gd = new GridData(GridData.CENTER);
		gd.widthHint = 60;
		export.setLayoutData(gd);

		export.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {

				URI uri = null;
				// is this a local file?
				File theFile = new File(file.getText());

				uri = theFile.toURI();
				
				try {
					exporter.export(source, uri, overwrite.getSelection(), false, true);
					export.setEnabled(false);
				}

				catch (LBException e) {
					dialog_.showError("Loader Error", e.toString());
					export.setEnabled(true);
					return;
				}
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				// 
			}

		});

		Composite status = getStatusComposite(shell, exporter);
		gd = new GridData(GridData.FILL_BOTH);
		status.setLayoutData(gd);

	}

}