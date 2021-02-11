
package org.LexGrid.LexBIG.gui.load;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.LogEntry;
import org.LexGrid.LexBIG.DataModel.Core.types.LogLevel;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Impl.loaders.MessageDirector;
import org.LexGrid.LexBIG.Utility.logging.CachingMessageDirectorIF;
import org.LexGrid.LexBIG.Utility.logging.StatusReporter;
import org.LexGrid.LexBIG.gui.DialogHandler;
import org.LexGrid.LexBIG.gui.LB_GUI;
import org.LexGrid.LexBIG.gui.LoadExportBaseShell;
import org.LexGrid.LexBIG.gui.Utility;
import org.LexGrid.LexOnt.CodingSchemeManifest;
import org.LexGrid.util.SimpleMemUsageReporter;
import org.LexGrid.util.SimpleMemUsageReporter.Snapshot;
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
import org.lexevs.logging.messaging.impl.CachingMessageDirectorImpl;

import edu.mayo.informatics.lexgrid.convert.exceptions.LgConvertException;
import edu.mayo.informatics.lexgrid.convert.utility.ManifestUtil;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

public class ManifestLoader extends LoadExportBaseShell implements StatusReporter {
    
    AbsoluteCodingSchemeVersionReference acsvr;
    private CachingMessageDirectorIF md_;
    private LoadStatus status_;
    private static String name = "Manifest_Loader";
    protected boolean inUse = false;


    public ManifestLoader(LB_GUI lb_gui, AbsoluteCodingSchemeVersionReference acsvr){
        this(lb_gui);
        this.acsvr = acsvr;
    }
    
	public ManifestLoader(LB_GUI lb_gui) {

		super(lb_gui);
		try {
		    
		       status_ = new LoadStatus();

		        status_.setState(ProcessState.PROCESSING);
		        status_.setStartTime(new Date(System.currentTimeMillis()));
		        md_ = new CachingMessageDirectorImpl( new MessageDirector(getName(), status_));
			Shell shell = new Shell(lb_gui_.getShell().getDisplay());
			shell.setSize(500, 400);
			shell.setImage(new Image(shell.getDisplay(), this.getClass()
					.getResourceAsStream("/icons/load.gif")));

			dialog_ = new DialogHandler(shell);

			shell.setText("Manifest Loader");

			buildGUI(shell);

			shell.open();

			shell.addShellListener(shellListener);
		} catch (Exception e) {
			dialog_.showError("Unexpected Error", e.toString());
		}
	}

	private void buildGUI(Shell shell) {

		Group options = new Group(shell, SWT.NONE);
		options.setText("Load Options");
		shell.setLayout(new GridLayout());

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		options.setLayoutData(gd);

		GridLayout layout = new GridLayout(3, false);
		options.setLayout(layout);

		Label fileLabel = new Label(options, SWT.NONE);
		fileLabel.setText("Manifest XML file");

		final Text file = new Text(options, SWT.BORDER);
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
		file.setLayoutData(gd);

		Button fileChooseButton = Utility.getFileChooseButton(options, file,
				new String[] { "*.xml" },
				new String[] { "Manifest file (xml)" });

		gd = new GridData(SWT.CENTER);
		gd.widthHint = 60;
		fileChooseButton.setLayoutData(gd);

		final Button load = new Button(options, SWT.PUSH);
		load.setText("Load");
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_END);
		gd.horizontalSpan = 3;
		gd.widthHint = 60;
		load.setLayoutData(gd);

		load.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
				URI uri = null;
				String codingSchemeURI = acsvr.getCodingSchemeURN();
				String codingSchemeVersion = acsvr.getCodingSchemeVersion();

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

				try {

					URNVersionPair pair = new URNVersionPair(codingSchemeURI,codingSchemeVersion);
					setLoading(true);
					ManifestUtil manifestUtil = new ManifestUtil();
					CodingSchemeManifest manifest = manifestUtil.getManifest(uri);
					if(manifest == null){
					    throw new LBException("there was a problem loading the manifest file at: " + uri.toString());
					}
					manifestUtil.applyManifest(manifest, pair);
					load.setEnabled(false);
                    md_.info("After Manifest Load");
                    Snapshot snap = SimpleMemUsageReporter.snapshot();
                    md_.info("Read Time : " + SimpleMemUsageReporter.formatTimeDiff(snap.getTimeDelta(null))
                            + " Heap Usage: " + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsage())
                            + " Heap Delta:" + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsageDelta(null)));

                    status_.setState(ProcessState.COMPLETED);
                    md_.info("Load process completed without error");
                    status_.setEndTime(new Date(System.currentTimeMillis()));
                    inUse = false;
		
                } catch (LgConvertException e) {
                    md_.error(e.getMessage());
                    status_.setState(ProcessState.FAILED);
                    dialog_.showError("Conversion Error", "Problem loading the Manifest");

                } catch (LBException e) {
                    status_.setState(ProcessState.FAILED);
                    md_.error(e.getMessage());
                    dialog_.showError("Invocation Error", e.getMessage());

                }

			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				// 
			}

		});

		Composite status = getStatusComposite(shell, this);
		gd = new GridData(GridData.FILL_BOTH);
		status.setLayoutData(gd);

	}
    public LoadStatus getStatus() {
        return status_;
    }
    
    public void setStatus(LoadStatus status){
        status_ = status;
    }

    public LogEntry[] getLog(LogLevel level) {
        if (md_ == null) {
            return new LogEntry[] {};
        }
        return md_.getLog(level);
    }
    public boolean isInUse() {
        return inUse;
    }
    protected void setInUse() throws LBInvocationException {
        if (inUse) {
            throw new LBInvocationException(
                    "This loader is already in use.  Construct a new loader to do two operations at the same time", "");
        }
        inUse = true;
    }
    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        ManifestLoader.name = name;
    }

}