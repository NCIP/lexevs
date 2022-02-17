
package org.LexGrid.LexBIG.gui.load;

import java.io.File;
import java.net.URI;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.loaders.OWLLoaderImpl;
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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import edu.mayo.informatics.resourcereader.core.StringUtils;

/**
 * GUI that allows loading generic Owl files into LexBIG.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class OWLLoader extends LoadExportBaseShell {
	public OWLLoader(LB_GUI lb_gui) {
		super(lb_gui);
		try {
			Shell shell = new Shell(lb_gui_.getShell().getDisplay());
			shell.setSize(500, 400);
			shell.setImage(new Image(shell.getDisplay(), this.getClass()
					.getResourceAsStream("/icons/load.gif")));

			dialog_ = new DialogHandler(shell);

			OWLLoaderImpl loader = (OWLLoaderImpl) lb_gui_.getLbs()
					.getServiceManager(null).getLoader("OWLLoader");

			shell.setText(loader.getName());

			buildGUI(shell, loader);

			shell.open();

			shell.addShellListener(shellListener);
		} catch (Exception e) {
			dialog_.showError("Unexpected Error", e.toString());
		}

	}

	private void buildGUI(Shell shell, final OWLLoaderImpl loader) {
		/* ----- Forming the Group ----- */
		Group options = new Group(shell, SWT.NONE);
		options.setText("Load Options");
		shell.setLayout(new GridLayout());

		/* ----- Forming grid data and adding to group ----- */
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		options.setLayoutData(gd);

		/* ----- forming grid layout and adding to the group ----- */
		GridLayout layout = new GridLayout(3, false);
		options.setLayout(layout);

		/* ----- Select input owl file ----- */

		Label fileLabel = new Label(options, SWT.NONE);
		fileLabel.setText("OWL source file");

		final Text file = new Text(options, SWT.BORDER);
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
		file.setLayoutData(gd);

		Button fileChooseButton = Utility.getFileChooseButton(options, file,
				new String[] { "*.owl" }, new String[] { "Owl file (owl)" });
		gd = new GridData(SWT.CENTER);
		gd.widthHint = 60;
		fileChooseButton.setLayoutData(gd);

		/* ----- Select preferences file ----- */

		final Text loaderPreferenceFile = Utility.createChooseFileDialog(
				options, "OWL preference file (optional)", "*.xml");

		loaderPreferenceFile.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));

		/* ----- Select manifest file ----- */

		Label paramFileLabel = new Label(options, SWT.NONE);
		paramFileLabel.setText("Manifest file (optional)");

		final Text paramFile = new Text(options, SWT.BORDER);
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
		paramFile.setLayoutData(gd);

		Button paramFileChooseButton = Utility.getFileChooseButton(options,
				paramFile, new String[] { "*.xml" },
				new String[] { "Manifest file (xml)" });
		gd = new GridData(SWT.CENTER);
		gd.widthHint = 60;
		paramFileChooseButton.setLayoutData(gd);

		/* ----- Select memory profile option ----- */

		Label paramRestrictionTypeLabel = new Label(options, SWT.NONE);
		paramRestrictionTypeLabel.setText("Load Profile");

		final Combo memoryProfile = new Combo(options, SWT.READ_ONLY);

		// Note: Option 0 currently disabled in GUI for simplification.
		// Performance impact is minimal compared to option 1, with more
		// danger of the user running low on memory.
		// memoryProfile.add("0-Fastest: all processing in memory", 0);
		memoryProfile.add("Faster/More memory - holds OWL in memory", 0);
		memoryProfile.add("Slower/Less memory - cache OWL to database", 1);
		// Note: Option 3 currently disabled in GUI for simplification.
		// Performance overhead is significant, and review with Stanford
		// indicates it reflects current limitations of Protege API.
		// memoryProfile.add("3-Slowest: Least memory", 3);

		memoryProfile.select(0);
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
		memoryProfile.setLayoutData(gd);

		/* ----- Button to start the load ----- */

		final Button load = new Button(options, SWT.PUSH);
		load.setText("Load");
		gd = new GridData(SWT.CENTER);
		gd.widthHint = 60;
		load.setLayoutData(gd);

		load.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {

				URI owlUri = null;
				// is this a local file?
				File theFile = new File(file.getText());

				if (theFile.exists()) {
					owlUri = theFile.toURI();
				} else {
					// is it a valid URI (like http://something)
					try {
						owlUri = new URI(file.getText());
						owlUri.toURL().openConnection();
					} catch (Exception e) {
						dialog_.showError("Path Error",
								"No file could be located at this location");
						return;
					}
				}

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

				try {
					// set the Loader Preferences is the URI is not null
					if (loaderPreferenceUri != null) {
						loader.setLoaderPreferences(loaderPreferenceUri);
					}
					setLoading(true);
					loader.load(owlUri, paramUri, memoryProfile.getSelectionIndex() + 1, false, true);
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