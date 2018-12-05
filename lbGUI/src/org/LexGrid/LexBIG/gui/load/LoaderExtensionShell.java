/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.LexGrid.LexBIG.gui.load;

import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.Enumeration;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Load.LexGrid_Loader;
import org.LexGrid.LexBIG.Extensions.Load.Loader;
import org.LexGrid.LexBIG.Extensions.Load.MetaBatchLoader;
import org.LexGrid.LexBIG.Extensions.Load.MetaData_Loader;
import org.LexGrid.LexBIG.Extensions.Load.MrMap_Loader;
import org.LexGrid.LexBIG.Extensions.Load.OBO_Loader;
import org.LexGrid.LexBIG.Extensions.Load.OWL2_Loader;
import org.LexGrid.LexBIG.Extensions.Load.UmlsBatchLoader;
import org.LexGrid.LexBIG.Extensions.Load.options.MultiValueOption;
import org.LexGrid.LexBIG.Extensions.Load.options.Option;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.LexBIG.Extensions.Load.options.URIOption;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.gui.DialogHandler;
import org.LexGrid.LexBIG.gui.LB_GUI;
import org.LexGrid.LexBIG.gui.LB_VSD_GUI;
import org.LexGrid.LexBIG.gui.LoadExportBaseShell;
import org.LexGrid.LexBIG.gui.Utility;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;
import org.apache.commons.collections.CollectionUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.system.constants.SystemVariables;
import org.lexgrid.resolvedvalueset.LexEVSResolvedValueSetService;
import org.lexgrid.resolvedvalueset.impl.LexEVSResolvedValueSetServiceImpl;
import org.springframework.util.StringUtils;

/**
 * The Class LoaderExtensionShell.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LoaderExtensionShell extends LoadExportBaseShell {
    String metadataFileStr = null;
    boolean metadataOverwrite = false;
    
    /**
     * Instantiates a new loader extension shell.
     * 
     * @param lb_gui the lb_gui
     * @param loader the loader
     */
    public LoaderExtensionShell(LB_GUI lb_gui, Loader loader) {
        super(lb_gui);
        initializeLBGui(loader);
    }
	
	/**
     * Instantiates a new loader extension shell.
     * 
     * @param lb_gui the lb_gui
     * @param loader the loader
     */
    public LoaderExtensionShell(LB_VSD_GUI lb_vsd_gui, Loader loader, boolean loadingVS, boolean loadingPL) {
        super(lb_vsd_gui);
        initializeLBVSDGui(loader, loadingVS, loadingPL);
    }
    
    private void initializeLBGui(Loader loader) {
        try {
            Shell shell = new Shell(lb_gui_.getShell().getDisplay());
            
            shell.setImage(new Image(shell.getDisplay(), this.getClass()
                    .getResourceAsStream("/icons/load.gif")));

            dialog_ = new DialogHandler(shell);
            
            shell.setText(loader.getName());

            buildGUI(shell, loader);
            shell.pack();
            
            shell.open();
    
            shell.addShellListener(shellListener);
        } catch (Exception e) {
            dialog_.showError("Unexpected Error", e.toString());
        }
    }
    
    private void initializeLBVSDGui(Loader loader, boolean loadingVS, boolean loadingPL) {
        try {
            Shell shell = new Shell(lb_vd_gui_.getShell().getDisplay());
            shell.setSize(500, 600);
        
            shell.setImage(new Image(shell.getDisplay(), this.getClass()
                    .getResourceAsStream("/icons/load.gif")));

            dialog_ = new DialogHandler(shell);
            
            shell.setText(loader.getName());

            buildVSGUI(shell, loader, loadingVS, loadingPL);

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
	private void buildGUI(final Shell shell, final Loader loader) {
	    final Text metadataFile;
        final Button metadataUriChooseButton;
        final Button overwriteButton;
        	    
	    Group options = new Group(shell, SWT.NONE);
	    options.setText("Load Options");
	    shell.setLayout(new GridLayout());
        
	    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
	    options.setLayoutData(gd);
	    
	    GridLayout layout = new GridLayout(1, false);
        options.setLayout(layout);
        
        Group groupUri = new Group(options, SWT.NONE);
        groupUri.setLayout(new GridLayout(3, false));
        groupUri.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        String uriHelp = "The URI of the resource to load.";
  
        Label label = new Label(groupUri, SWT.NONE);
        label.setText("URI:");
        label.setToolTipText(uriHelp);
        
        final Text file = new Text(groupUri, SWT.BORDER);
        file.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL));
        file.setToolTipText(uriHelp);
        
        OptionHolder optionHolder = loader.getOptions();

        final Button uriChooseButton;

        if(optionHolder.isResourceUriFolder()  ) {
            uriChooseButton = Utility.getFolderChooseButton(groupUri, file);
        } else {
            uriChooseButton = Utility.getFileChooseButton(groupUri, file,
                    optionHolder.getResourceUriAllowedFileTypes().toArray(new String[0]),
                    optionHolder.getResourceUriAllowedFileTypes().toArray(new String[0]));
        }
        uriChooseButton.setToolTipText(uriHelp);
        
        
        // get lbconfig properties
        SystemVariables variables = LexEvsServiceLocator.getInstance().getSystemResourceService().getSystemVariables();
        String csTag = variables.getAssertedValueSetCodingSchemeTag();
        String csVersion = variables.getAssertedValueSetVersion();
        String csName = variables.getAssertedValueSetCodingSchemeName();
        String csURI = variables.getAssertedValueSetCodingSchemeURI();
        String hierarchyVSRelation = variables.getAssertedValueSetHierarchyVSRelation();
            
        AssertedValueSetParameters params = null;
        
        // Set the asserted value set params
        // create parameters with tag set
        if (csTag != null) {
            params = new AssertedValueSetParameters.Builder().
                    assertedDefaultHierarchyVSRelation(hierarchyVSRelation).
                    codingSchemeName(csName).
                    codingSchemeURI(csURI).
                    codingSchemeTag(csTag)
                    .build();
        }
        // create parameters with a version set
        else if (csVersion != null && csVersion.length() > 0){
            params = new AssertedValueSetParameters.Builder(csVersion).
                    assertedDefaultHierarchyVSRelation(hierarchyVSRelation).
                    codingSchemeName(csName).
                    codingSchemeURI(csURI)
                    .build();
        }
        // create parameters with NO tag set
        else {
            params = new AssertedValueSetParameters.Builder().
                assertedDefaultHierarchyVSRelation(hierarchyVSRelation).
                codingSchemeName(csName).
                codingSchemeURI(csURI)
                .build();
        }
        
        // Get resolved value sets
        LexEVSResolvedValueSetService resolvedValueSetService = new LexEVSResolvedValueSetServiceImpl(params);
        java.util.List<CodingScheme> resolvedValueSets = null;
        try {
            resolvedValueSets = resolvedValueSetService.listAllResolvedValueSets();
        } catch (LBException e) {
            resolvedValueSets = null;
        }
        
        for(final URIOption uriOption : optionHolder.getURIOptions()) {
            Composite group1 = new Composite(options, SWT.NONE);
            
            group1.setLayout(new GridLayout(3, false));
            group1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            
            Label uriOptionLable = new Label(group1, SWT.NONE);
            uriOptionLable.setText(uriOption.getOptionName() + ":");
            uriOptionLable.setToolTipText(uriOption.getHelpText());

            final Text uriOptionFile = new Text(group1, SWT.BORDER);
            uriOptionFile.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL));
            uriOptionFile.setToolTipText(uriOption.getHelpText());

            Button uriOptionfileChooseButton = Utility.getFileChooseButton(group1, uriOptionFile,
                    uriOption.getAllowedFileExtensions().toArray(new String[0]), null);
            uriOptionfileChooseButton.setToolTipText(uriOption.getHelpText());
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

        
        
        // Determine if the metadata load options should be displayed.
        if (displayMetadataOptions(loader)) {
        
            // Metadata Options
            Group groupMetadata = new Group(options, SWT.NONE);
            groupMetadata.setLayout(new GridLayout(3, false));
            groupMetadata.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            
            String metadataUriHelp = "The URI of the metadata to load.";
      
            Label metadataLabel = new Label(groupMetadata, SWT.NONE);
            metadataLabel.setText("Metadata URI:");
            metadataLabel.setToolTipText(metadataUriHelp);
            
            metadataFile = new Text(groupMetadata, SWT.BORDER);
            metadataFile.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL));
            metadataFile.setToolTipText(metadataUriHelp);
            
            metadataUriChooseButton = Utility.getFileChooseButton(groupMetadata, metadataFile,
                 optionHolder.getResourceUriAllowedFileTypes().toArray(new String[0]),
                 optionHolder.getResourceUriAllowedFileTypes().toArray(new String[0]));
            
            metadataUriChooseButton.setToolTipText(metadataUriHelp);   
                    
            overwriteButton = new Button(groupMetadata, SWT.CHECK);
            overwriteButton.setText("Overwrite");
            overwriteButton.setToolTipText("overwrite If specified, existing metadata for the code system will be erased. " +
                    "Otherwise, new metadata will be appended to existing metadata (if present).");
        }
        else {
            metadataFile = null;
            overwriteButton = null;
        }
        
	    for(final Option<Boolean> boolOption : optionHolder.getBooleanOptions()){
	        Composite group2 = new Composite(options, SWT.NONE);
	       
	       RowLayout rlo = new RowLayout();
           rlo.marginWidth = 0;
           group2.setLayout(rlo);
           
	       final Button button = new Button(group2, SWT.CHECK);
	       button.setText(boolOption.getOptionName());
	       button.setToolTipText(boolOption.getHelpText());
	       
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
	        Composite group3 = new Composite(options, SWT.NONE);
	   
	        RowLayout rlo = new RowLayout();
	        rlo.marginWidth = 0;
	        group3.setLayout(rlo);
	           
	        Label textLabel = new Label(group3, SWT.NONE);
	        textLabel.setText(stringOption.getOptionName() + ":");
	        textLabel.setToolTipText(stringOption.getHelpText());
	        
	        if(CollectionUtils.isNotEmpty(stringOption.getPickList())) {
	            final Combo comboDropDown = new Combo(group3, SWT.DROP_DOWN | SWT.BORDER);
	            
	            comboDropDown.setToolTipText(stringOption.getHelpText());
                
	            for(String pickListItem : stringOption.getPickList()) {
	                	                
	                // Add if it is not a resolved value set
                    if (resolvedValueSets != null && !isResolvedValueSet(resolvedValueSets, pickListItem)){
                        comboDropDown.add(pickListItem);
                    }
                }

	            comboDropDown.addSelectionListener(new SelectionListener(){

                    @Override
                    public void widgetDefaultSelected(SelectionEvent arg0) {
                       //
                    }

                    @Override
                    public void widgetSelected(SelectionEvent event) {
                        String option =  comboDropDown.getItem(comboDropDown.getSelectionIndex());
                        stringOption.setOptionValue(option);
                    }  
                });
            } else {

	        final Text text = new Text(group3, SWT.BORDER);
	        RowData textGd = new RowData();
	        textGd.width = 25;
	        text.setLayoutData(textGd);
	        
	        text.setToolTipText(stringOption.getHelpText());
	        
	        text.addModifyListener(new ModifyListener(){

                public void modifyText(ModifyEvent event) {
                    stringOption.setOptionValue(text.getText());      
                }  
	        });
	    }
	    }
	    
	       for(final Option<Integer> integerOption : optionHolder.getIntegerOptions()){
	            Composite group3 = new Composite(options, SWT.NONE);
	       
	            RowLayout rlo = new RowLayout();
	            rlo.marginWidth = 0;
	            group3.setLayout(rlo);
	               
	            Label textLabel = new Label(group3, SWT.NONE);
	            textLabel.setText(integerOption.getOptionName() + ":");
	            textLabel.setToolTipText(integerOption.getHelpText());

	            final Text text = new Text(group3, SWT.BORDER);
	            text.setToolTipText(integerOption.getHelpText());
	            
	            if(integerOption.getOptionValue() != null){
	                text.setText(integerOption.getOptionValue().toString());
	            }
	            
	            text.addModifyListener(new ModifyListener(){

	                public void modifyText(ModifyEvent event) {
	                    integerOption.setOptionValue(Integer.parseInt(text.getText()));      
	                }  
	            });
	        }
	    	       
	       for(final MultiValueOption<String> stringArrayOption : optionHolder.getStringArrayOptions()){
	            Composite group4 = new Composite(options, SWT.NONE);
	
	            RowLayout rlo = new RowLayout();
	            rlo.marginWidth = 0;
	            group4.setLayout(rlo);
	               
	            Label textLabel = new Label(group4, SWT.NONE);
	            String appendString = CollectionUtils.isNotEmpty(stringArrayOption.getPickList()) ? "" : "\n\t(Comma Seperated):";
	            textLabel.setText(stringArrayOption.getOptionName() + appendString);
	            textLabel.setToolTipText(stringArrayOption.getHelpText());

	            if(CollectionUtils.isNotEmpty(stringArrayOption.getPickList())) {
	                final List multi = new List(group4, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
	                
	                multi.setToolTipText(stringArrayOption.getHelpText());
	                
	                for(String pickListItem : stringArrayOption.getPickList()) {
	                    multi.add(pickListItem);
                    }
	                for(int i=0;i<stringArrayOption.getPickList().size();i++) {
	                    if(stringArrayOption.getOptionValue().contains(    
	                            stringArrayOption.getPickList().get(i))) {
	                        multi.select(i);
	                    }
	                }

	                multi.addSelectionListener(new SelectionListener(){

                        @Override
                        public void widgetDefaultSelected(SelectionEvent arg0) {
                           //
                        }

                        @Override
                        public void widgetSelected(SelectionEvent event) {
                            String[] options =  multi.getSelection();
                            stringArrayOption.setOptionValue(Arrays.asList(options));
                        }  
                    });
	            } else {
	                final Text text = new Text(group4, SWT.BORDER);
	                
	                text.setToolTipText(stringArrayOption.getHelpText());

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
	        }
	      
	    Group groupControlButtons = new Group(options, SWT.NONE);
        groupControlButtons.setLayout(new GridLayout(3, false));
        groupControlButtons.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	    
        final Button load = new Button(groupControlButtons, SWT.PUSH);
        final Button nextLoad = new Button(groupControlButtons, SWT.PUSH);
        final Button close = new Button(groupControlButtons, SWT.PUSH);
        close.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, true, 1, 1));
        
	    load.setText("Load");
	    load.setToolTipText("Start Load Process.");
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
						dialog_.showError("Path Error",
								"No file could be located at this location");
						return;
					}
				}
				
				metadataFileStr = metadataFile != null?  metadataFile.getText(): null;
				metadataOverwrite = overwriteButton != null? overwriteButton.getSelection() : false;

				setLoading(true);
				load.setEnabled(false);
				close.setEnabled(false);
				loader.load(uri);
				
				
				// Create/start a new thread to update the buttons when the load completes.

                ButtonUpdater buttonUpdater = new ButtonUpdater(nextLoad, close, loader);
                Thread t = new Thread(buttonUpdater);
                t.setDaemon(true);
                t.start();  
	        }

			public void widgetDefaultSelected(SelectionEvent arg0) {
				// 
			}

		});
	    
        nextLoad.setText("Next Load");
        nextLoad.setToolTipText("Start a New Load Process.");
        nextLoad.setEnabled(false);
        nextLoad.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {

                Loader newLoader = null;
                try {
                    newLoader = lb_gui_.getLbs().getServiceManager(null).getLoader(loader.getName());
                } catch (LBException e) {
                    e.printStackTrace();
                }
	            if(!isLoading()){
	                
	                // close the current window and create/initialize it again with the same loader
                    shell.dispose();
                    setMonitorLoader(true);
                    initializeLBGui(newLoader);
	            }
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }

        });
        
        close.setText("Close");
        close.setToolTipText("Close this Loader Window.");
        close.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                shell.dispose();
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }

        });

		Composite status = getStatusComposite(shell, loader);
		gd = new GridData(GridData.FILL_BOTH);
		status.setLayoutData(gd);
	}
	
	/**
     * Builds the gui.
     * 
     * @param shell the shell
     * @param loader the loader
     */
    private void buildVSGUI(final Shell shell, final Loader loader, final boolean loadingVS, final boolean loadingPL) {
        Group options = new Group(shell, SWT.NONE);
        options.setText("Load Options");
        shell.setLayout(new GridLayout());

        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        options.setLayoutData(gd);
        
        GridLayout layout = new GridLayout(1, false);
        options.setLayout(layout);
        
        Group groupUri = new Group(options, SWT.NONE);
        groupUri.setLayout(new GridLayout(3, false));
        groupUri.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
  
        Label label = new Label(groupUri, SWT.NONE);
        label.setText("URI:");
        
        final Text file = new Text(groupUri, SWT.BORDER);
        file.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL));
        
        OptionHolder optionHolder = loader.getOptions();

        if(optionHolder.isResourceUriFolder()  ) {
            Utility.getFolderChooseButton(groupUri, file);
        } else {
            Utility.getFileChooseButton(groupUri, file,
                    optionHolder.getResourceUriAllowedFileTypes().toArray(new String[0]),
                    optionHolder.getResourceUriAllowedFileTypes().toArray(new String[0]));
        }
        
        Group groupControlButtons = new Group(options, SWT.NONE);
        groupControlButtons.setLayout(new GridLayout(3, false));
        groupControlButtons.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        final Button load = new Button(groupControlButtons, SWT.PUSH);
        final Button nextLoad = new Button(groupControlButtons, SWT.PUSH);
        final Button close = new Button(groupControlButtons, SWT.PUSH);
        close.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, true, 1, 1));
        
        load.setText("Load");
        load.setToolTipText("Start Load Process.");
        
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
                        dialog_.showError("Path Error",
                                "No file could be located at this location");
                        return;
                    }
                }
      
                setLoading(true);
                load.setEnabled(false);
                close.setEnabled(false);
                loader.load(uri);
                
                // Create/start a new thread to update the buttons when the load completes.
                ButtonUpdater buttonUpdater = new ButtonUpdater(nextLoad, close, loader);
                Thread t = new Thread(buttonUpdater);
                t.setDaemon(true);
                t.start();  
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }

        });
        
        nextLoad.setText("Next Load");
        nextLoad.setToolTipText("Start a New Load Process.");
        nextLoad.setEnabled(false);
        nextLoad.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {

                Loader newLoader = null;
                try {
                    newLoader = lb_vd_gui_.getLbs().getServiceManager(null).getLoader(loader.getName());
                } catch (LBException e) {
                    e.printStackTrace();
                }
                if(!isLoading()){
                    
                    // close the current window and create/initialize it again with the same loader
                    shell.dispose();
                    setMonitorLoader(true);
                    initializeLBVSDGui(newLoader, loadingVS, loadingPL);
                }
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }
        });
        
        close.setText("Close");
        close.setToolTipText("Close this Loader Window.");
        close.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                shell.dispose();
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }

        });

        
        Composite status = null;
        
        if (loadingVS)
            status = getStatusCompositeForValueSets(shell, loader);        
        else if (loadingPL)
                status = getStatusCompositeForPickList(shell, loader);
        
        gd = new GridData(GridData.FILL_BOTH);
        status.setLayoutData(gd);
    }
    
    /**
     * Runnable class to update the buttons when the load finishes.
     */
    protected class ButtonUpdater implements Runnable {

        protected Button nextLoadBtn;
        protected Button closeBtn;
        protected Loader loader;
        
        ButtonUpdater(Button nextLoadButton, Button closeButton, Loader loader){
            nextLoadBtn = nextLoadButton; 
            closeBtn = closeButton;
            this.loader = loader;
        }
        
        @Override
        public void run() {
            while(loader.getStatus() != null && loader.getStatus().getEndTime() == null){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException("Error while waiting for load to complete.", e);
                }
            }
         
            try {
                URI metadataUri = getMetadataURI();
                if (metadataUri != null){
                    
                    // Pause a second then do the metadata load.  Otherwise the logs don't
                    // display the metadata load.
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException("Error while waiting for status log.", e);
                    }
                    
                    LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
                    LexBIGServiceManager lbsm = lbs.getServiceManager(null);
                    MetaData_Loader metadataLoader = (MetaData_Loader) lbsm.getLoader("MetaDataLoader");
                    
                    CodingSchemeSummary css = 
                            getCodingSchemeSummary(loader.getCodingSchemeReferences(), 
                                    lbs.getSupportedCodingSchemes());
                    
                    setStatusMonitor(statusText_, metadataLoader);
                    
                    metadataLoader.loadAuxiliaryData(metadataUri, Constructors.createAbsoluteCodingSchemeVersionReference(css),
                            metadataOverwrite, false, true);  
                }
            }
            catch (Exception e) {
                System.out.println("Metadata loader failed:" + e);
            }
                                   
            
            // Wait an extra 3 seconds to give time for the screen to update completely (XWindows sessions)
            // JIRA issue lexevs-2461
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException("Error while waiting extra time.", e);
            }
            
            // Needs to be called from the SWT Display thread.
            Display.getDefault().syncExec(new Runnable() {
                public void run() {
                    nextLoadBtn.setEnabled(true);
                    closeBtn.setEnabled(true);
                    setLoading(false);
                }
            });
        }
    }
    
    /**
     * Compare the CodingSchemeRendering codingSchemeURI to all of the codingSchemeURI of the List<CodingScheme>.  
     * If the codingSchemeURI is found, then true;
     * @param resolvedValueSets List<CodingScheme>
     * @param uri String
     * @return true if the codingSchemeURI of the CodingSchemeRendering is found in the List<CodingScheme>.
     */
    private boolean isResolvedValueSet(java.util.List<CodingScheme> resolvedValueSets, String uri) {
        String resolvedValueSetURI;
        String resolvedValueSetVersion;
        boolean isResolvedValueSet = false;
        
        // loop through resolved value sets
        for(CodingScheme resolvedValueSet: resolvedValueSets){
            resolvedValueSetURI = resolvedValueSet.getCodingSchemeURI();
            resolvedValueSetVersion = resolvedValueSet.getRepresentsVersion();
            
            // search for a coding scheme that has the same uri as the resolved value set.
            // if one is found, set to true;
            if (uri.equals(resolvedValueSetURI + " - " + resolvedValueSetVersion)) {
                isResolvedValueSet = true;
                break;
            }

        } 
        return isResolvedValueSet;
    }
    
    private CodingSchemeSummary getCodingSchemeSummary(AbsoluteCodingSchemeVersionReference[] refs,
    		CodingSchemeRenderingList schemeList){
    	
    	CodingSchemeSummary css = null;
    	Enumeration<? extends CodingSchemeRendering> schemes = schemeList.enumerateCodingSchemeRendering();
        while (schemes.hasMoreElements() && css == null) {
            CodingSchemeSummary summary = schemes.nextElement().getCodingSchemeSummary();
            
            for (int i = 0; i < refs.length; i++) {
                AbsoluteCodingSchemeVersionReference ref = refs[i];
                                        
                if (ref.getCodingSchemeURN().equalsIgnoreCase(summary.getCodingSchemeURI())
                        && ref.getCodingSchemeVersion().equalsIgnoreCase(summary.getRepresentsVersion())){
                    css = summary;
                    break;
                }
            }
        }
        return css;
    }

    private boolean displayMetadataOptions(Loader loader){
        boolean display = false;
        if (loader.getName().equals(LexGrid_Loader.name) ||
            loader.getName().equals(OWL2_Loader.name) ||   
            loader.getName().equals(MrMap_Loader.name) ||   
            loader.getName().equals(MetaBatchLoader.NAME) ||   
            loader.getName().equals(UmlsBatchLoader.NAME) ||   
            loader.getName().equals(OBO_Loader.name)){
            display = true;
        }
        
        return display;
    }
    
    private URI getMetadataURI() {
        
        URI metadataUri = null;
        if (metadataFileStr != null && metadataFileStr.trim().length() > 0) {
            File metadataFile = new File(metadataFileStr);

            if (metadataFile.exists()) {
                metadataUri = metadataFile.toURI();
            } else {
                // is it a valid URI (like http://something)
                try {
                    metadataUri = new URI(metadataFileStr);
                    metadataUri.toURL().openConnection();
                } catch (Exception e) {
                    System.out.println("Path Error. No metatdata file could be located at this location." + e.getMessage());
                }
            } 
        }
        
        return metadataUri;
    }

    
}