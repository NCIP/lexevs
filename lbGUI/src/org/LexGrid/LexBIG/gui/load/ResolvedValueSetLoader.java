/*
 * Copyright: (c) 2004-2013 Mayo Foundation for Medical Education and 
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
 *      http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.LexGrid.LexBIG.gui.load;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.gui.DialogHandler;
import org.LexGrid.LexBIG.gui.LB_GUI;
import org.LexGrid.LexBIG.gui.LB_VSD_GUI;
import org.LexGrid.LexBIG.gui.LoadExportBaseShell;
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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.lexgrid.loader.ResolvedValueSetDefinitionLoaderImpl;
import org.lexgrid.resolvedvalueset.LexEVSResolvedValueSetService;
import org.lexgrid.resolvedvalueset.impl.LexEVSResolvedValueSetServiceImpl;

public class ResolvedValueSetLoader extends LoadExportBaseShell {

    private List<CodingScheme> resolvedValueSets = null;
    
    public ResolvedValueSetLoader(LB_GUI lb_gui) {
        super(lb_gui);

        try {
            Shell shell = new Shell(lb_gui_.getShell().getDisplay());
            shell.setSize(500, 500);
            shell.setImage(new Image(shell.getDisplay(), this.getClass()
                    .getResourceAsStream("/icons/load.gif")));

            dialog_ = new DialogHandler(shell);

            LexEVSResolvedValueSetService resolvedValueSetService = new LexEVSResolvedValueSetServiceImpl();
            resolvedValueSets = resolvedValueSetService.listAllResolvedValueSets();
              
            ResolvedValueSetDefinitionLoaderImpl loader = (ResolvedValueSetDefinitionLoaderImpl) lb_gui_.getLbs()
                    .getServiceManager(null).getLoader(ResolvedValueSetDefinitionLoaderImpl.NAME);

            shell.setText(loader.getName());

            buildGUI(shell, loader);

            shell.open();

            shell.addShellListener(shellListener);
        } catch (Exception e) {
            dialog_.showError("Unexpected Error", e.toString());
        }    
    }

    private void buildGUI(Shell shell, final ResolvedValueSetDefinitionLoaderImpl loader) throws LBInvocationException {

        Group options = new Group(shell, SWT.NONE);
        options.setText("Load Options");
        shell.setLayout(new GridLayout());

        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        options.setLayoutData(gd);

        GridLayout layout = new GridLayout(1, false);
        options.setLayout(layout);
        
        Group groupUri = new Group(options, SWT.NONE);
        groupUri.setLayout(new GridLayout(2, false));
        groupUri.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Label fileLabel = new Label(groupUri, SWT.NONE);
        fileLabel.setText("Value Set URL");

        final Text file = new Text(groupUri, SWT.BORDER);
        gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
        file.setLayoutData(gd);

        Group groupCSdrop = new Group(options, SWT.NONE);
        groupCSdrop.setLayout(new GridLayout(2, false));
        GridData gdDrop = new GridData(GridData.GRAB_HORIZONTAL);
        groupCSdrop.setLayoutData(gdDrop);
        
        Composite group3 = new Composite(groupCSdrop, SWT.NONE);
        
        RowLayout rlo = new RowLayout(SWT.WRAP);
        rlo.marginWidth = 0;
        group3.setLayout(rlo);
           
        Label textLabel = new Label(group3, SWT.NONE);
        textLabel.setText("Coding Scheme Versions to Resolve Against:");
        textLabel.setToolTipText("Helps to insure the resolution " +
        		"takes place against a particular coding scheme version");
        CodingSchemeRenderingList schemes = LexBIGServiceImpl.defaultInstance().getSupportedCodingSchemes();
        List<CodingSchemeRendering> renderings = Arrays.asList(schemes.getCodingSchemeRendering());       

        //Get a list ready to add to       
        Group group4 = new Group(options, SWT.NONE);
        Label listLabel = new Label(group4, SWT.NONE);
        listLabel.setText("Target resolution schemes");
        final org.eclipse.swt.widgets.List multi = 
                new org.eclipse.swt.widgets.List(group4, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
        group4.setLayout(new GridLayout(1, false));
        GridData gdCSadd = new GridData(GridData.GRAB_HORIZONTAL);
        gdCSadd.horizontalSpan = 2;
        gdCSadd.horizontalAlignment = SWT.FILL;
        gdCSadd.grabExcessHorizontalSpace = true;
        multi.setLayoutData(gdCSadd);
        group4.setLayoutData(gdCSadd);
        

        final HashMap<String, AbsoluteCodingSchemeVersionReference> map = new HashMap<String, AbsoluteCodingSchemeVersionReference>();
        
        if(CollectionUtils.isNotEmpty(renderings)) {
            final Combo comboDropDown = new Combo(group3, SWT.DROP_DOWN | SWT.BORDER);
            
            comboDropDown.setToolTipText("Helps to insure the resolution " +
                    "takes place against a particular coding scheme version");
            
            for(CodingSchemeRendering pickListItem : renderings) {
                
                // don't add resolved value sets to the dropdown.
                if (!isResolvedValueSet(resolvedValueSets, pickListItem)) {
                    String schemeVersion = pickListItem.getCodingSchemeSummary().getFormalName() 
                            + "-" +  pickListItem.getCodingSchemeSummary().getRepresentsVersion();
                    AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
                    ref.setCodingSchemeURN(pickListItem.getCodingSchemeSummary().getCodingSchemeURI());
                    ref.setCodingSchemeVersion(pickListItem.getCodingSchemeSummary().getRepresentsVersion());
                    map.put(schemeVersion, ref);
                    comboDropDown.add(schemeVersion);
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
                 multi.add(option);
                }  
            });
        } else {

        final Text text = new Text(group3, SWT.BORDER);
        RowData textGd = new RowData();
        textGd.width = 25;
        text.setLayoutData(textGd);
        
        text.setToolTipText("Insures the resolution " +
                "takes place against a particular coding scheme version in the service");
        
        text.addModifyListener(new ModifyListener(){
            public void modifyText(ModifyEvent event) {
              multi.add( text.getText());      
            }  
        });
        }
        
        final Button load = new Button(options, SWT.PUSH);
        load.setText("Load");
        gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_END);
        gd.horizontalSpan = 3;
        gd.widthHint = 60;
        load.setLayoutData(gd);
        load.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                AbsoluteCodingSchemeVersionReferenceList metaURI = new AbsoluteCodingSchemeVersionReferenceList();
                for (String s: multi.getItems()){
                    metaURI.addAbsoluteCodingSchemeVersionReference(map.get(s));
                }
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
    }
    
    /**
     * Compare the CodingSchemeRendering codingSchemeURI to all of the codingSchemeURI of the List<CodingScheme>.  
     * If the codingSchemeURI is found, then true;
     * @param resolvedValueSets List<CodingScheme>
     * @param cs CodingSchemeRendering
     * @return true if the codingSchemeURI of the CodingSchemeRendering is found in the List<CodingScheme>.
     */
    private boolean isResolvedValueSet(List<CodingScheme> resolvedValueSets, CodingSchemeRendering cs) {
        String resolvedValueSetURI;
        boolean isResolvedValueSet = false;
        
        // loop through resolved value sets
        for(CodingScheme resolvedValueSet: resolvedValueSets){
            resolvedValueSetURI = resolvedValueSet.getCodingSchemeURI();
            
            // search for a coding scheme that has the same uri as the resolved value set.
            // if one is found, set to true;
            if (cs.getCodingSchemeSummary().getCodingSchemeURI().equals(resolvedValueSetURI)) {
                isResolvedValueSet = true;
                break;
            }

        } 
        return isResolvedValueSet;
    }

}
