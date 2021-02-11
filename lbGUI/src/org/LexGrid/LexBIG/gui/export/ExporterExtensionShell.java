
package org.LexGrid.LexBIG.gui.export;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.Arrays;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Export.Exporter;
import org.LexGrid.LexBIG.Extensions.Load.options.MultiValueOption;
import org.LexGrid.LexBIG.Extensions.Load.options.Option;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.LexBIG.Extensions.Load.options.URIOption;
import org.LexGrid.LexBIG.Impl.exporters.LexGridExport;
import org.LexGrid.LexBIG.gui.DialogHandler;
import org.LexGrid.LexBIG.gui.LB_GUI;
import org.LexGrid.LexBIG.gui.LB_VSD_GUI;
import org.LexGrid.LexBIG.gui.LoadExportBaseShell;
import org.LexGrid.LexBIG.gui.Utility;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.lexgrid.valuesets.dto.ResolvedValueSetCodedNodeSet;
import org.springframework.util.StringUtils;

/**
 * The Class ExporterExtensionShell.
 * 
 * @author <a href="mailto:turk.michael@mayo.edu">Michael Turk</a>
 */
public class ExporterExtensionShell extends LoadExportBaseShell {
    private Group options_;
    private Text file_;
    private boolean exportVSResolve_ = false;
    private List csrCombo_;
    private LexGridExport lgExporter_ = null;
	/**
	 * Instantiates a new loader extension shell.
	 * 
	 * @param lb_gui the lb_gui
	 * @param loader the loader
	 */
	public ExporterExtensionShell(LB_GUI lb_gui, Exporter exporter, AbsoluteCodingSchemeVersionReference acsvr) {
		super(lb_gui);
		try {
			Shell shell = new Shell(lb_gui_.getShell().getDisplay());
			shell.setSize(500, 600);
		
			shell.setImage(new Image(shell.getDisplay(), this.getClass()
					.getResourceAsStream("/icons/load.gif")));

			dialog_ = new DialogHandler(shell);
			
			shell.setText(exporter.getName());

			init(shell, exporter);
			
			buildGUI(shell, exporter, acsvr);

			shell.open();

			shell.addShellListener(shellListener);
		} catch (Exception e) {
			dialog_.showError("Unexpected Error", e.toString());
		}

	}
	
	/**
     * Instantiates a new loader extension shell for exporting value set definition
     * 
     * @param lb_vsd_gui the lb_vsd_gui
     * @param loader the loader
     */
    public ExporterExtensionShell(LB_VSD_GUI lb_vsd_gui, Exporter exporter, URI valueSetDefinitionURI, boolean exportVSResolv) {
        super(lb_vsd_gui);
        try {
            this.exportVSResolve_ = exportVSResolv;
            lgExporter_ = (LexGridExport) exporter;
            if (exportVSResolv)
                lgExporter_.getOptions().setIsResourceUriFolder(true);
            
            Shell shell = new Shell(lb_vd_gui_.getShell().getDisplay());
            shell.setSize(500, 600);
        
            shell.setImage(new Image(shell.getDisplay(), this.getClass()
                    .getResourceAsStream("/icons/load.gif")));

            dialog_ = new DialogHandler(shell);
            
            shell.setText(exporter.getName());

            init(shell, exporter);
            
            buildVSDGUI(shell, valueSetDefinitionURI);

            shell.open();

            shell.addShellListener(shellListener);
        } catch (Exception e) {
            dialog_.showError("Unexpected Error", e.toString());
        }

    }
    
    /**
     * Instantiates a new loader extension shell for exporting pick list definition.
     * 
     * @param lb_vsd_gui the lb_vsd_gui
     * @param loader the loader
     */
    public ExporterExtensionShell(LB_VSD_GUI lb_vsd_gui, Exporter exporter, String pickListId) {
        super(lb_vsd_gui);
        try {
            Shell shell = new Shell(lb_vd_gui_.getShell().getDisplay());
            shell.setSize(500, 600);
        
            shell.setImage(new Image(shell.getDisplay(), this.getClass()
                    .getResourceAsStream("/icons/load.gif")));

            dialog_ = new DialogHandler(shell);
            
            shell.setText(exporter.getName());

            init(shell, exporter);
            
            buildPLDGUI(shell, exporter, pickListId);

            shell.open();

            shell.addShellListener(shellListener);
        } catch (Exception e) {
            dialog_.showError("Unexpected Error", e.toString());
        }

    }
	
	/**
	 * Builds the gui.
	 * 
	 * @param shell the shell
	 * @param loader the loader
	 */
	private void buildGUI(Shell shell, final Exporter exporter, final AbsoluteCodingSchemeVersionReference acsvr) {
	    final Button export = new Button(options_, SWT.PUSH);
	    export.setText("Export");
	    GridData gd = new GridData(GridData.CENTER);
	    gd.widthHint = 60;
	    export.setLayoutData(gd);

	    export.addSelectionListener(new SelectionListener() {

	        public void widgetSelected(SelectionEvent arg0) {

	            URI uri = null;
	            String fileName = file_.getText();	               
	            try {
                    uri = ExporterExtensionShell.string2UriVerifyTrue(fileName);
                    exporter.export(acsvr, uri);
                                        
                } catch (LBResourceUnavailableException e) {
                    e.printStackTrace();
                    dialog_.showError("LBResourceUnavailableException", e.getMessage());
                }
                export.setEnabled(false);
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				// 
			}

		});

		Composite status = getStatusComposite(shell, exporter);
		gd = new GridData(GridData.FILL_BOTH);
		status.setLayoutData(gd);

	}
	
	/**
     * Builds the gui for value set definition.
     * 
     * @param shell the shell
     * @param loader the loader
     */
    private void buildVSDGUI(Shell shell, final URI vsdURI) {
        final Button export = new Button(options_, SWT.PUSH);
        export.setText("Export");
        GridData gd = new GridData(GridData.CENTER);
        gd.widthHint = 60;
        export.setLayoutData(gd);

        export.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {

                URI uri = null;
                String fileName = file_.getText();                 
                try {
                    uri = ExporterExtensionShell.string2UriVerifyFalse(fileName);
                    if (exportVSResolve_)
                    {
                        String[] selectedItems = csrCombo_.getSelection();
                        AbsoluteCodingSchemeVersionReferenceList acsvrList = new AbsoluteCodingSchemeVersionReferenceList();
                        for (int loopIndex = 0; loopIndex < selectedItems.length; loopIndex++)
                        {
                            String[] csrString = selectedItems[loopIndex].toString().split(":");
                            String csName = csrString[0];                        
                            String csVersion = csrString[1];
                            
                            AbsoluteCodingSchemeVersionReference acsvr = new AbsoluteCodingSchemeVersionReference();
                            acsvr.setCodingSchemeURN(csName);
                            acsvr.setCodingSchemeVersion(csVersion);
                            acsvrList.addAbsoluteCodingSchemeVersionReference(acsvr);                        
                        }
                        ResolvedValueSetCodedNodeSet rvscns = lb_vd_gui_.getValueSetDefinitionService().getCodedNodeSetForValueSetDefinition(vsdURI, null, acsvrList, null);
                        lgExporter_.setCns(rvscns.getCodedNodeSet());
                        lgExporter_.exportValueSetResolution(vsdURI, null, uri);
                    }
                    else
                    {
                        lgExporter_.exportValueSetDefinition(vsdURI, null, uri);
                    }
                                        
                } catch (LBResourceUnavailableException e) {
                    e.printStackTrace();
                    dialog_.showError("LBResourceUnavailableException", e.getMessage());
                } catch (LBException e) {
                    e.printStackTrace();
                    dialog_.showError("LBException", e.getMessage());
                }
                export.setEnabled(false);
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }

        });

        Composite status = getStatusComposite(shell, lgExporter_);
        gd = new GridData(GridData.FILL_BOTH);
        status.setLayoutData(gd);

    }
    
    /**
     * Builds the gui for pick list definition.
     * 
     * @param shell the shell
     * @param loader the loader
     */
    private void buildPLDGUI(Shell shell, final Exporter exporter, final String pickListId) {
        final Button export = new Button(options_, SWT.PUSH);
        export.setText("Export");
        GridData gd = new GridData(GridData.CENTER);
        gd.widthHint = 60;
        export.setLayoutData(gd);

        export.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {

                URI uri = null;
                String fileName = file_.getText();                 
                try {
                    uri = ExporterExtensionShell.string2UriVerifyFalse(fileName);
                    exporter.exportPickListDefinition(pickListId, uri);
                                        
                } catch (LBResourceUnavailableException e) {
                    e.printStackTrace();
                    dialog_.showError("LBResourceUnavailableException", e.getMessage());
                }
                export.setEnabled(false);
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }

        });

        Composite status = getStatusComposite(shell, exporter);
        gd = new GridData(GridData.FILL_BOTH);
        status.setLayoutData(gd);
    }
	
	/**
     * Builds the gui.
     * 
     * @param shell the shell
     * @param loader the loader
     */
    private void init(Shell shell, final Exporter exporter) {
        options_ = new Group(shell, SWT.NONE);
        options_.setText("Export Options");
        shell.setLayout(new GridLayout());

        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        options_.setLayoutData(gd);
        
        GridLayout layout = new GridLayout(1, false);
        options_.setLayout(layout);
        
        Group groupUri = new Group(options_, SWT.NONE);
        groupUri.setLayout(new GridLayout(3, false));
        groupUri.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
  
        Label label = new Label(groupUri, SWT.NONE);
        label.setText("URI:");
        
        file_ = new Text(groupUri, SWT.BORDER);
        file_.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL));
        
        OptionHolder optionHolder = exporter.getOptions();
        
        if(optionHolder.isResourceUriFolder()  ) {
            Utility.getFolderChooseButton(groupUri, file_);
        } else {
            Utility.getFileChooseButton(groupUri, file_,
                    optionHolder.getResourceUriAllowedFileTypes().toArray(new String[0]),
                    optionHolder.getResourceUriAllowedFileTypes().toArray(new String[0]));
        }
       
        
        for(final URIOption uriOption : optionHolder.getURIOptions()) {
            Composite group1 = new Composite(options_, SWT.NONE);
            
            group1.setLayout(new GridLayout(3, false));
            group1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            
            Label uriOptionLable = new Label(group1, SWT.NONE);
            uriOptionLable.setText(uriOption.getOptionName() + ":");

            final Text uriOptionFile = new Text(group1, SWT.BORDER);
            uriOptionFile.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL));

            Button uriOptionfileChooseButton = Utility.getFileChooseButton(group1, uriOptionFile,
                    uriOption.getAllowedFileExtensions().toArray(new String[0]), null);
            
            uriOptionfileChooseButton.addSelectionListener(new SelectionListener() {

                public void widgetDefaultSelected(SelectionEvent arg0) {
                    //
                }

                public void widgetSelected(SelectionEvent arg0) {
                    try {
                        uriOption.setOptionValue(Utility.getAndVerifyURIFromTextField(uriOptionFile));
                    } catch (Exception e) {
                      throw new RuntimeException(e);
                    }
                }    
            });
        }
        
        for(final Option<Boolean> boolOption : optionHolder.getBooleanOptions()){
            Composite group2 = new Composite(options_, SWT.NONE);
           
           RowLayout rlo = new RowLayout();
           rlo.marginWidth = 0;
           group2.setLayout(rlo);
           
           final Button button = new Button(group2, SWT.CHECK);
           button.setText(boolOption.getOptionName());
           if(boolOption.getOptionValue() != null){
               button.setSelection(boolOption.getOptionValue());
           }
           button.addSelectionListener(new SelectionListener(){

            public void widgetDefaultSelected(SelectionEvent event) {
              //
            }

            public void widgetSelected(SelectionEvent event) {
                boolOption.setOptionValue(button.getSelection());
            }
               
           });
        }

        for(final Option<String> stringOption : optionHolder.getStringOptions()){
            Composite group3 = new Composite(options_, SWT.NONE);
       
            RowLayout rlo = new RowLayout();
            rlo.marginWidth = 0;
            group3.setLayout(rlo);
               
            Label textLabel = new Label(group3, SWT.NONE);
            textLabel.setText(stringOption.getOptionName() + ":");

            final Text text = new Text(group3, SWT.BORDER);
            
            text.addModifyListener(new ModifyListener(){

                public void modifyText(ModifyEvent event) {
                    stringOption.setOptionValue(text.getText());      
                }  
            });
        }
        
           for(final MultiValueOption<String> stringArrayOption : optionHolder.getStringArrayOptions()){
                Composite group4 = new Composite(options_, SWT.NONE);
           
                RowLayout rlo = new RowLayout();
                rlo.marginWidth = 0;
                group4.setLayout(rlo);
                   
                Label textLabel = new Label(group4, SWT.NONE);
                textLabel.setText(stringArrayOption.getOptionName() + "\n\t(Comma Seperated):");

                final Text text = new Text(group4, SWT.BORDER);
               
                String arrayString =
                    StringUtils.collectionToCommaDelimitedString(stringArrayOption.getOptionValue());
                text.setText(arrayString);

                text.addModifyListener(new ModifyListener(){

                    public void modifyText(ModifyEvent event) {
                        String[] options =  StringUtils.commaDelimitedListToStringArray(text.getText());
                        stringArrayOption.setOptionValue(Arrays.asList(options));
                    }  
                });
            }
           
           if (exportVSResolve_)
           {
               
               CodingSchemeRenderingList csrList = null;
               try {
                   csrList = lb_vd_gui_.getLbs().getSupportedCodingSchemes();
               } catch (LBInvocationException e1) {
                   // TODO Auto-generated catch block
                   e1.printStackTrace();
               }
               Composite group5 = new Composite(options_, SWT.NONE);
               
               GridLayout glo = new GridLayout(2, true);
               group5.setLayout(glo);
               
               Label csrLabel = new Label(group5, SWT.CENTER);
               csrLabel.setText("Select Coding Scheme And Version : ");
               GridData gridData = new GridData(GridData.FILL, GridData.END, true, false);
               gridData.horizontalIndent = 3;
               gridData.verticalSpan = 2;
               gridData.verticalIndent = 5;
               csrLabel.setLayoutData(gridData);
               
               csrCombo_ = new List(group5, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
               gridData = new GridData(GridData.FILL, GridData.BEGINNING, true, false);
               gridData.verticalSpan = 5;
               gridData.verticalIndent = 5;
               csrCombo_.setLayoutData(gridData);
               
               if (csrList != null)
               {
                   for (int i = 0; i < csrList.getCodingSchemeRenderingCount(); i++)
                   {
                       CodingSchemeRendering csr = csrList.getCodingSchemeRendering(i);
                       if (csr.getCodingSchemeSummary() != null)
                           csrCombo_.add(csr.getCodingSchemeSummary().getFormalName() + ":" + csr.getCodingSchemeSummary().getRepresentsVersion());
                   }
               }               
           }
    }
	
    /**
     * Returns a file URI corresponding to the given string.
     * 
     * Copied and modified from org.LexGrid.LexBIG.admin.Util 
     * 
     * The resource described by sResource is expected to exist. 
     * 
     * @param sResource
     * @return java.net.URI
     * @throws org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException
     */
    private static URI string2UriVerifyTrue(String sResource) throws LBResourceUnavailableException {
        String trimmed = sResource.trim();

        try {
            // Resolve to file, treating the string as either a
            // standard file path or URI.
            File f;
            if (!(f = new File(trimmed)).exists()) {
                f = new File(new URI(trimmed.replace(" ", "%20")));
                if (!f.exists())
                    throw new FileNotFoundException();
            } 

            // Accomodate embedded spaces ...
            return new URI(f.toURI().toString().replace(" ", "%20"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new LBResourceUnavailableException("UNABLE TO RESOLVE RESOURCE: " + trimmed);
        }
    }
    
    /**
     * Returns a file URI corresponding to the given string.
     * 
     * The resource described by sResource does not need to exist. 
     * 
     * @param sResource
     * @return java.net.URI
     */
    private static URI string2UriVerifyFalse(String sResource) throws LBResourceUnavailableException {
        String trimmed = sResource.trim();
        File f = new File(trimmed);
        return f.toURI();
    }
    
    
}