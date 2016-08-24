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

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Load.Loader;
import org.LexGrid.LexBIG.Extensions.Load.options.MultiValueOption;
import org.LexGrid.LexBIG.Extensions.Load.options.Option;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.LexBIG.Extensions.Load.options.URIOption;
import org.LexGrid.LexBIG.gui.DialogHandler;
import org.LexGrid.LexBIG.gui.LB_GUI;
import org.LexGrid.LexBIG.gui.LB_VSD_GUI;
import org.LexGrid.LexBIG.gui.LoadExportBaseShell;
import org.LexGrid.LexBIG.gui.Utility;
import org.LexGrid.codingSchemes.CodingScheme;
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
import org.lexgrid.resolvedvalueset.LexEVSResolvedValueSetService;
import org.lexgrid.resolvedvalueset.impl.LexEVSResolvedValueSetServiceImpl;
import org.springframework.util.StringUtils;

/**
 * The Class LoaderExtensionShell.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LoaderExtensionShell extends LoadExportBaseShell {

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
        
        // Get resolved value sets
        LexEVSResolvedValueSetService resolvedValueSetService = new LexEVSResolvedValueSetServiceImpl();
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

}