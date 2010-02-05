/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package edu.mayo.informatics.lexgrid.convert.swt.formatPanels;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import edu.mayo.informatics.lexgrid.convert.swt.DialogHandler;

/**
 * A SWT Composite that collects information necessary for getting a folder
 * location.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 787 $ checked in on $Date: 2005-12-12 13:53:09
 *          -0600 (Mon, 12 Dec 2005) $
 */
public class IndexerConfigComposite extends Composite {
    Text folderName;
    Text indexName_;
    Button normEnabled_;
    Text normFile_;
    Label normLabel;
    Button choose2;

    public IndexerConfigComposite(Composite parent, int style, String description, DialogHandler errorHandler) {
        super(parent, style);
        this.setLayout(new GridLayout(1, true));

        Group group = new Group(this, SWT.NONE);
        group.setText(description);
        group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        group.setLayout(new GridLayout(3, false));

        Label fileLabel = new Label(group, SWT.NONE);
        fileLabel.setText("Index Location:  ");
        fileLabel.setToolTipText("The LexGrid Index will be written into this folder");

        GridData gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER);
        folderName = new Text(group, SWT.SINGLE | SWT.BORDER);
        folderName.setLayoutData(gd);
        folderName.setToolTipText("The LexGrid Index will be written into this folder");

        Button choose = new Button(group, SWT.PUSH);
        choose.setText("Browse...");
        choose.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent arg0) {
            }

            public void widgetSelected(SelectionEvent arg0) {
                DirectoryDialog fileDialog = new DirectoryDialog(IndexerConfigComposite.this.getShell());
                fileDialog.setText("Please select a folder");
                String file = fileDialog.open();
                if (file != null) {
                    folderName.setText(file);
                }
            }

        });

        Label nameLabel = new Label(group, SWT.NONE);
        nameLabel.setText("Index Name:  ");
        nameLabel.setToolTipText("The name to use for this LexGrid index");

        gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER);
        indexName_ = new Text(group, SWT.SINGLE | SWT.BORDER);
        indexName_.setLayoutData(gd);
        indexName_.setToolTipText("The name to use for this LexGrid index");

        // empty space fillers
        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);

        gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER);
        gd.horizontalSpan = 2;
        normEnabled_ = new Button(group, SWT.CHECK);
        normEnabled_.setText("Build Normalized Index");
        normEnabled_.setToolTipText("Check this to enable the construction of a normalized index.");
        normEnabled_.setLayoutData(gd);
        normEnabled_.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent arg0) {
            }

            public void widgetSelected(SelectionEvent arg0) {
                if (normEnabled_.getSelection()) {
                    normLabel.setEnabled(true);
                    normFile_.setEnabled(true);
                    choose2.setEnabled(true);
                } else {
                    normLabel.setEnabled(false);
                    normFile_.setEnabled(false);
                    choose2.setEnabled(false);
                }
            }
        });

        normLabel = new Label(group, SWT.NONE);
        normLabel.setText("Norm Config File:  ");
        normLabel.setToolTipText("The configuration file for LVG Norm");
        normLabel.setEnabled(false);

        gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER);
        normFile_ = new Text(group, SWT.SINGLE | SWT.BORDER);
        normFile_.setLayoutData(gd);
        normFile_.setToolTipText("The configuration file for LVG Norm");
        normFile_.setEnabled(false);

        choose2 = new Button(group, SWT.PUSH);
        choose2.setText("Browse...");
        choose2.setEnabled(false);
        choose2.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent arg0) {
            }

            public void widgetSelected(SelectionEvent arg0) {
                FileDialog fileDialog = new FileDialog(IndexerConfigComposite.this.getShell());
                fileDialog.setText("Please select the LVG Norm Config File");
                String file = fileDialog.open();
                if (file != null) {
                    normFile_.setText(file);
                }
            }

        });

    }

    public String getFolderSelection() {
        return folderName.getText();
    }

    public void setFolderSelection(String folder) {
        folderName.setText(folder);
    }

    public String getIndexName() {
        return indexName_.getText();
    }

    public void setIndexName(String indexName) {
        indexName_.setText(indexName);
    }

    public String getNormFile() {
        return normFile_.getText();
    }

    public void setNormFile(String normFile) {
        normFile_.setText(normFile);
    }

    public boolean isNormEnabled() {
        return normEnabled_.getSelection();
    }

    public void setNormEnabled(boolean enabled) {
        normEnabled_.setSelection(enabled);
    }
}