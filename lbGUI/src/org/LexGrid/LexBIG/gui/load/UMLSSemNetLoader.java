
package org.LexGrid.LexBIG.gui.load;

import java.io.File;
import java.net.URI;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Load.UMLS_Loader;
import org.LexGrid.LexBIG.Preferences.loader.SemNetLoadPreferences.SemNetLoaderPreferences;
import org.LexGrid.LexBIG.Preferences.loader.SemNetLoadPreferences.types.InheritanceLevelType;
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
 * GUI that allows loading UMLS RRF files into LexBIG.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class UMLSSemNetLoader extends LoadExportBaseShell {
	public UMLSSemNetLoader(LB_GUI lb_gui) {
		super(lb_gui);
		try {
			Shell shell = new Shell(lb_gui_.getShell().getDisplay());
			shell.setSize(500, 400);
			shell.setImage(new Image(shell.getDisplay(), this.getClass()
					.getResourceAsStream("/icons/load.gif")));

			dialog_ = new DialogHandler(shell);

			UMLS_Loader loader = (UMLS_Loader) lb_gui_.getLbs()
					.getServiceManager(null).getLoader("UMLSLoader");

			shell.setText("UMLSSemNetLoader");

			buildGUI(shell, loader);

			shell.open();

			shell.addShellListener(shellListener);
		} catch (Exception e) {
			dialog_.showError("Unexpected Error", e.toString());
		}

	}

	private void buildGUI(Shell shell, final UMLS_Loader loader) {
		Group options = new Group(shell, SWT.NONE);
		options.setText("Load Options");
		shell.setLayout(new GridLayout());

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		options.setLayoutData(gd);

		GridLayout layout = new GridLayout(5, false);
		options.setLayout(layout);

		Label fileLabel = new Label(options, SWT.NONE);
		fileLabel.setText("Source folder");

		final Text file = new Text(options, SWT.BORDER);
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		file.setLayoutData(gd);

		Button fileChooseButton = Utility.getFolderChooseButton(options, file);
		gd = new GridData(GridData.CENTER);
		gd.widthHint = 60;
		fileChooseButton.setLayoutData(gd);

		// Manifest Support
		Label paramFileLabel = new Label(options, SWT.NONE);
		paramFileLabel.setText("Manifest file (optional)");

		final Text paramFile = new Text(options, SWT.BORDER);
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		paramFile.setLayoutData(gd);

		Button paramFileChooseButton = Utility.getFileChooseButton(options,
				paramFile, new String[] { "*.xml" },
				new String[] { "Manifest file (xml)" });

		gd = new GridData(SWT.CENTER);
		gd.widthHint = 60;
		paramFileChooseButton.setLayoutData(gd);
		// End Manifest Support

		Label loadOptionsLabel = new Label(options, SWT.NONE);
		loadOptionsLabel.setText("Load inferred relations");
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		loadOptionsLabel.setLayoutData(gd);

		final Button none = new Button(options, SWT.RADIO);
		none.setText("None");
		none
				.setToolTipText("Associations are extracted only from the source file SRSTR.");
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		none.setLayoutData(gd);

		final Button all = new Button(options, SWT.RADIO);
		all.setText("All");
		all
				.setToolTipText("Associations are extracted only from the source file SRSTRE1.");
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		all.setLayoutData(gd);

		final Button exceptIsA = new Button(options, SWT.RADIO);
		exceptIsA.setText("All except is_a");
		exceptIsA
				.setToolTipText("Associations are extracted from the source file SRSTRE1 everything but ISA relaions"
						+ " and from SRSTR only ISA realions.");
		exceptIsA.setSelection(true);
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		exceptIsA.setLayoutData(gd);
		
		final Button load = new Button(options, SWT.PUSH);
		load.setText("Load");
		gd = new GridData(GridData.CENTER);
		gd.widthHint = 60;
		load.setLayoutData(gd);

		load.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				SemNetLoaderPreferences loaderPrefObj = new SemNetLoaderPreferences();

				// Determine the inheritance level for the loader
				if (none.getSelection())
					loaderPrefObj
							.setInheritanceLevel(InheritanceLevelType.VALUE_0);
				else if (all.getSelection())
					loaderPrefObj
							.setInheritanceLevel(InheritanceLevelType.VALUE_1);
				else if (exceptIsA.getSelection())
					loaderPrefObj
							.setInheritanceLevel(InheritanceLevelType.VALUE_2);

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

				try {
					// set the Loader Preferences (currently contains only
					// inheritance level)
					if (loaderPrefObj != null) {
						loader.setLoaderPreferences(loaderPrefObj);
					} else {

					}
					setLoading(true);
					loader.setCodingSchemeManifestURI(paramUri);
					loader.loadSemnet(uri, false, true);
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