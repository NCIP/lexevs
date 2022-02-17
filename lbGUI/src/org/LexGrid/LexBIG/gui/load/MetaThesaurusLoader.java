
package org.LexGrid.LexBIG.gui.load;

import java.net.URI;

import org.LexGrid.LexBIG.Extensions.Load.MetaBatchLoader;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * GUI that allows loading NCI MetaThesaurus files into LexBIG.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class MetaThesaurusLoader extends LoadExportBaseShell {
	public MetaThesaurusLoader(LB_GUI lb_gui) {
		super(lb_gui);
		try {
			Shell shell = new Shell(lb_gui_.getShell().getDisplay());
			shell.setSize(500, 400);
			shell.setImage(new Image(shell.getDisplay(), this.getClass()
					.getResourceAsStream("/icons/load.gif")));

			dialog_ = new DialogHandler(shell);

			MetaBatchLoader loader = (MetaBatchLoader) lb_gui_
					.getLbs().getServiceManager(null).getLoader(
							MetaBatchLoader.NAME);

			shell.setText(loader.getName());

			buildGUI(shell, loader);

			shell.open();

			shell.addShellListener(shellListener);
		} catch (Exception e) {
			dialog_.showError("Unexpected Error", e.toString());
		}

	}

	private void buildGUI(Shell shell, final MetaBatchLoader loader) {
		Group options = new Group(shell, SWT.NONE);
		options.setText("Load Options");
		shell.setLayout(new GridLayout());

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		options.setLayoutData(gd);

		GridLayout layout = new GridLayout(3, false);
		options.setLayout(layout);

		final Text sourceFolder = Utility.createChooseFileDialog(options,
				"Meta RRF source folder", null);
		final Text loaderPreferenceFile = Utility.createChooseFileDialog(
				options, "Meta preference file (optional)", "*.xml");
		final Text manifestFile = Utility.createChooseFileDialog(options,
				"Manifest file (optional)", "*.xml");

		final Button load = new Button(options, SWT.PUSH);
		load.setText("Load");
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_END);
		gd.horizontalSpan = 3;
		gd.widthHint = 60;
		load.setLayoutData(gd);

		load.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {

				URI folderUri = null;
				try {
					folderUri = Utility
							.getAndVerifyURIFromTextField(sourceFolder);
				} catch (Exception e) {
					dialog_.showError("Could not find Source folder.", e
							.toString());
					return;
				}

				URI manifestUri = null;
				try {
					manifestUri = Utility
							.getAndVerifyURIFromTextField(manifestFile);
				} catch (Exception e) {
					dialog_.showError("Could not find Manifest file.", e
							.toString());
					return;
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

				setLoading(true);
				try {
					loader.setCodingSchemeManifestURI(manifestUri);

					// set the Loader Preferences is the URI is not null
					if (loaderPreferenceUri != null) {
						loader.setLoaderPreferences(loaderPreferenceUri);
					}
					try {
                       Thread loaderThread = new Thread(new RunMetaLoader(loader, folderUri));
					   loaderThread.run();   
                    } catch (Exception e) {
                       throw new RuntimeException(e);
                    }
					load.setEnabled(false);
				}

				catch (Exception e) {
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
	
    private class RunMetaLoader implements Runnable {

        private MetaBatchLoader loader;
        private URI path;
        
        public RunMetaLoader(MetaBatchLoader loader, URI path){
            this.loader = loader;
            this.path = path;
        }
        
        public void run() {
            try {
                loader.loadMeta(path);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}