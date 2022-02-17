
package org.LexGrid.LexBIG.gui.load;

import java.io.File;
import java.net.URI;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Load.MetaData_Loader;
import org.LexGrid.LexBIG.Impl.loaders.HL7LoaderImpl;
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

import edu.mayo.informatics.resourcereader.core.StringUtils;

/**
 * GUI that allows loading HL7 RIM into LexBIG.
 * 
 * @author <A HREF="mailto:bauer.scott@mayo.edu">Scott Bauer</A>
 * @author <A HREF="mailto:stancl.craig@mayo.edu">Craig Stancl</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class HL7RIMLoader extends LoadExportBaseShell {

	public HL7RIMLoader(LB_GUI lb_gui) {
		super(lb_gui);

		try {
			Shell shell = new Shell(lb_gui_.getShell().getDisplay());
			shell.setSize(500, 400);
			shell.setImage(new Image(shell.getDisplay(), this.getClass()
					.getResourceAsStream("/icons/load.gif")));

			dialog_ = new DialogHandler(shell);

			HL7LoaderImpl loader = (HL7LoaderImpl) lb_gui_.getLbs()
					.getServiceManager(null).getLoader("HL7Loader");

			MetaData_Loader mdLoader = (MetaData_Loader) lb_gui_.getLbs()
					.getServiceManager(null).getLoader("MetaDataLoader");

			shell.setText(loader.getName());

			buildGUI(shell, loader, mdLoader);

			shell.open();

			shell.addShellListener(shellListener);
		} catch (Exception e) {
			dialog_.showError("Unexpected Error", e.toString());
		}

	}

	private void buildGUI(Shell shell, final HL7LoaderImpl loader,
			final MetaData_Loader mdLoader) {
		Group options = new Group(shell, SWT.NONE);
		options.setText("Load Options");
		shell.setLayout(new GridLayout());

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		options.setLayoutData(gd);

		GridLayout layout = new GridLayout(3, false);
		options.setLayout(layout);

		Label fileLabel = new Label(options, SWT.NONE);
		fileLabel.setText("HL7 RIM database file");

		final Text file = new Text(options, SWT.BORDER);
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
		file.setLayoutData(gd);

		Button fileChooseButton = Utility
				.getFileChooseButton(options, file, new String[] { "*.mdb" },
						new String[] { "MS ACCESS database" });
		gd = new GridData(SWT.CENTER);
		gd.widthHint = 60;
		fileChooseButton.setLayoutData(gd);

		// LoaderPreference support
		Label paramFileLabel = new Label(options, SWT.NONE);
		paramFileLabel.setText("HL7 preference file (optional)");

		final Text loaderPreferenceFile = new Text(options, SWT.BORDER);
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
		loaderPreferenceFile.setLayoutData(gd);

		Button paramFileChooseButton = Utility.getFileChooseButton(options,
				loaderPreferenceFile, new String[] { "*.xml" },
				new String[] { "HL7 preferences (xml)" });
		gd = new GridData(SWT.CENTER);
		gd.widthHint = 60;
		paramFileChooseButton.setLayoutData(gd);

		// Manifest file update
		paramFileLabel = new Label(options, SWT.NONE);
		paramFileLabel.setText("Manifest file (optional)");

		final Text paramFile = new Text(options, SWT.BORDER);
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
		paramFile.setLayoutData(gd);

		paramFileChooseButton = Utility.getFileChooseButton(options,
				paramFile, new String[] { "*.xml" },
				new String[] { "Manifest file (xml)" });
		gd = new GridData(SWT.CENTER);
		gd.widthHint = 60;
		paramFileChooseButton.setLayoutData(gd);

		final Button load = new Button(options, SWT.PUSH);
		load.setText("Load");
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_END);
		gd.horizontalSpan = 3;
		gd.widthHint = 60;
		load.setLayoutData(gd);

		load.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {

				URI hl7Uri = null;
				// is this a local file?
				File theFile = new File(file.getText());

				if (theFile.exists()) {
					hl7Uri = theFile.toURI();
				} else {
					// is it a valid URI (like http://something)
					try {
						hl7Uri = new URI(file.getText());
						hl7Uri.toURL().openConnection();
					} catch (Exception e) {
						dialog_.showError("Path Error",
								"No file could be located at this location");
						return;
					}
				}

				// Check the manifest file
				URI paramUri = null;
				File theParamFile = new File(paramFile.getText());

				if (theParamFile.exists()) {
					paramUri = theParamFile.toURI();
				} else {
					// is it a valid URI (like http://something)
					try {
						if (!StringUtils.isNull(paramFile.getText())) {
							paramUri = new URI(paramFile.getText());
							paramUri.toURL().openConnection();
						}
					} catch (Exception e) {
						dialog_.showError("Path Error",
								"No file could be located at this location");
						return;
					}
				}

				URI loaderPreferenceUri = null;
				try {
					loaderPreferenceUri = Utility
							.getAndVerifyURIFromTextField(loaderPreferenceFile);
				} catch (Exception e) {
					dialog_.showError("Could not find Loader Preference file.",
							e.toString());
					return;
				}

				// Create the metadata XML file if it doesn't exist
				String dbPath = hl7Uri.getPath();

				// remove the leading / if it exists
				if (hl7Uri.getPath().startsWith("/")) {
					dbPath = hl7Uri.getPath().substring(1);
				}

				// Find the location of the file extension
				int sep = dbPath.lastIndexOf(".");

				// Create the new file name for the metadata
				String dbfullPath = dbPath.substring(0, sep);

				String metadataFileName = dbfullPath + "_metadata.xml";
				try {
					setLoading(true);

					// HL7 Metadata URI location
					// NOTE: Metadata Load now happens in BaseLoader.java
					loader.setMetaDataFileLocation(metadataFileName);

					// Update for manifest
					loader.setCodingSchemeManifestURI(paramUri);

					// set the Loader Preferences if the URI is not null
					if (loaderPreferenceUri != null) {
						loader.setLoaderPreferences(loaderPreferenceUri);
					}

					loader.load(hl7Uri.getPath().substring(1), false, true);
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