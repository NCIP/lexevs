package org.LexGrid.LexBIG.gui.load;

import java.io.File;
import java.net.URI;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Load.OBO_Loader;
import org.LexGrid.LexBIG.Extensions.Load.ResolvedValueSetDefinitionLoader;
import org.LexGrid.LexBIG.gui.DialogHandler;
import org.LexGrid.LexBIG.gui.LB_GUI;
import org.LexGrid.LexBIG.gui.LB_VSD_GUI;
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
import org.lexgrid.loader.ResolvedValueSetDefinitionLoaderImpl;

public class ResolvedValueSetLoader extends LoadExportBaseShell {

    public ResolvedValueSetLoader(LB_GUI lb_gui) {
        super(lb_gui);
        try {
            Shell shell = new Shell(lb_gui_.getShell().getDisplay());
            shell.setSize(500, 400);
            shell.setImage(new Image(shell.getDisplay(), this.getClass()
                    .getResourceAsStream("/icons/load.gif")));

            dialog_ = new DialogHandler(shell);

            ResolvedValueSetDefinitionLoaderImpl loader = (ResolvedValueSetDefinitionLoaderImpl) lb_gui_.getLbs()
                    .getServiceManager(null).getLoader(ResolvedValueSetDefinitionLoaderImpl.NAME);

            shell.setText(loader.getName());

            buildGUI(shell, loader);

            shell.open();

            shell.addShellListener(shellListener);
        } catch (Exception e) {
            dialog_.showError("Unexpected Error", e.toString());
        }    }

    private void buildGUI(Shell shell, final ResolvedValueSetDefinitionLoaderImpl loader) {
        Group options = new Group(shell, SWT.NONE);
        options.setText("Load Options");
        shell.setLayout(new GridLayout());

        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        options.setLayoutData(gd);

        GridLayout layout = new GridLayout(3, false);
        options.setLayout(layout);

        Label fileLabel = new Label(options, SWT.NONE);
        fileLabel.setText("Value Set URL");

        final Text file = new Text(options, SWT.BORDER);
        gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
        file.setLayoutData(gd);


        final Button load = new Button(options, SWT.PUSH);
        load.setText("Load");
        gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_END);
        gd.horizontalSpan = 3;
        gd.widthHint = 60;
        load.setLayoutData(gd);

        load.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                AbsoluteCodingSchemeVersionReferenceList metaURI = null;



                try {
                    setLoading(true);
                    loader.loadResolvedValueSet(file.getText(),null, metaURI, null);
                    load.setEnabled(false);
                }

                catch (LBException e) {
                    dialog_.showError("Loader Error", e.toString());
                    load.setEnabled(true);
                    setLoading(false);
                    return;
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            private boolean isNull(String str) {
                return ((str == null) || ("".equals(str.trim())) || ("null".equals(str)));
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }

        });

        Composite status = getStatusComposite(shell, loader);
        gd = new GridData(GridData.FILL_BOTH);
        status.setLayoutData(gd);
        
    }

    public ResolvedValueSetLoader(LB_VSD_GUI lb_vd_gui) {
        super(lb_vd_gui);
        // TODO Auto-generated constructor stub
    }

}
