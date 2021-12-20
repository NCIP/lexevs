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
package org.LexGrid.LexBIG.gui.displayResults;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.gui.LB_VSD_GUI;
import org.LexGrid.LexBIG.gui.Utility;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.valueSets.CodingSchemeReference;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.EntityReference;
import org.LexGrid.valueSets.PropertyMatchValue;
import org.LexGrid.valueSets.PropertyReference;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.ValueSetDefinitionReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import prefuse.Constants;

/**
 * This class displays the results of a Value Set Definition query in a shell.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class VDDisplayFilterResult {
    private static Logger log = LogManager.getLogger("LexBIG.GUI");
    private LB_VSD_GUI lb_gui_;
    private Shell shell_;
    private LBException resolveException_;
    private ProgressBar progressBar_;
    private Composite resultsComposite_;
    private StackLayout stack_;
    private ListIterator<String> codeIterator_;
    private ArrayList<String> displayedResults_;
    private HashSet<String> displayedResultsCodes_;
    private Table displayedCodeList_;
    private StyledText codeDetails_;
    private Label busyResolvingLabel_;
    private Object currentControl;

    public VDDisplayFilterResult(LB_VSD_GUI lb_gui, List<String> vsdURIs) {
        preInit(lb_gui);

        resolveCodeSet(vsdURIs);
    }

    private void preInit(LB_VSD_GUI lb_gui) {
        lb_gui_ = lb_gui;
        shell_ = new Shell(lb_gui_.getShell().getDisplay());
        shell_.setSize(640, 480);

        shell_.setText("Result Browser");

        init();

        shell_.open();

        shell_.addShellListener(new ShellAdapter() {
            public void shellClosed(ShellEvent arg0) {
                if (codeIterator_ != null) {
                    codeIterator_ = null;
                }
            }
        });

        shell_.getDisplay().addFilter(SWT.KeyUp, new Listener() {

            public void handleEvent(Event event) {
                GraphView currentGraph;
                if (currentControl == null
                        || !(currentControl instanceof GraphView)) {
                    return;
                }
                currentGraph = (GraphView) currentControl;
                if (event.stateMask == SWT.CTRL) {
                    // CTRL 1 through 4
                    if (event.keyCode == 49) {
                        currentGraph
                                .updateOrientation(Constants.ORIENT_LEFT_RIGHT);
                    } else if (event.keyCode == 50) {
                        currentGraph
                                .updateOrientation(Constants.ORIENT_TOP_BOTTOM);
                    } else if (event.keyCode == 51) {
                        currentGraph
                                .updateOrientation(Constants.ORIENT_RIGHT_LEFT);
                    } else if (event.keyCode == 52) {
                        currentGraph
                                .updateOrientation(Constants.ORIENT_BOTTOM_TOP);
                    } else if (event.keyCode == 61) {
                        // ctrl +
                        currentGraph.increaseSpace();
                    } else if (event.keyCode == 45) {
                        // ctrl -
                        currentGraph.decreaseSpace();
                    }
                }
            }

        });

        shell_.setImage(new Image(shell_.getDisplay(), this.getClass()
                .getResourceAsStream("/icons/result.gif")));
    }

    private void init() {
        stack_ = new StackLayout();
        shell_.setLayout(stack_);

        // create the *busy* windows - only displays while we do the initial
        // resolve.
        Composite busyComposite = new Composite(shell_, SWT.NONE);
        busyComposite.setLayout(new GridLayout(1, false));

        busyResolvingLabel_ = Utility.makeWrappingLabel(busyComposite,
                "Resolving the Code Set", 1);
        busyResolvingLabel_.setLayoutData(new GridData(
                GridData.VERTICAL_ALIGN_END | GridData.HORIZONTAL_ALIGN_CENTER
                        | GridData.FILL_BOTH));

        progressBar_ = new ProgressBar(busyComposite, SWT.INDETERMINATE);
        progressBar_.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
                | GridData.VERTICAL_ALIGN_BEGINNING | GridData.GRAB_VERTICAL));

        // create the results window.

        resultsComposite_ = new Composite(shell_, SWT.NONE);
        resultsComposite_.setLayout(new GridLayout(1, false));

        SashForm leftRight = new SashForm(resultsComposite_, SWT.HORIZONTAL);
        leftRight.SASH_WIDTH = 5;

        leftRight.setVisible(true);
        leftRight.setLayout(new GridLayout(1, false));
        leftRight.setLayoutData(new GridData(GridData.FILL_BOTH));

        displayedCodeList_ = new Table(leftRight, SWT.SINGLE | SWT.BORDER
                | SWT.H_SCROLL | SWT.V_SCROLL);
        displayedCodeList_.setLayoutData(new GridData(GridData.FILL_BOTH));
        displayedCodeList_.addSelectionListener(codeListSelectionListener);

        SashForm rightTopBottom = new SashForm(leftRight, SWT.VERTICAL);

        rightTopBottom.setLayoutData(new GridData(GridData.FILL_BOTH));
        rightTopBottom.setLayout(new GridLayout());

        // Add html-capable form for display of selected item details ...
        codeDetails_ = new StyledText(rightTopBottom, SWT.WRAP | SWT.BORDER
                | SWT.MULTI | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL);

        leftRight.setWeights(new int[] { 30, 70 });
        stack_.topControl = busyComposite;
    }

    private final SelectionListener codeListSelectionListener = new SelectionListener() {
        public void widgetDefaultSelected(SelectionEvent e) {
        }

        public void widgetSelected(SelectionEvent e) {
            int index = displayedCodeList_.getSelectionIndex();
            if (index == -1) {
                return;
            }
            String rcr = displayedResults_.get(index);
            displayConceptDetails(rcr);
            addOrUpdateDisplayedResults(rcr, -1, SWT.COLOR_BLUE, -1, -1,
                    SWT.COLOR_BLUE, -1, false);
        }
    };

    private void resolveCodeSet(List<String> vsdURIs) {
        Resolver resolver = new Resolver(vsdURIs, this);

        shell_.setCursor(shell_.getDisplay().getSystemCursor(SWT.CURSOR_WAIT));

        new Thread(resolver).start();
    }
    
    protected void displayConceptDetails(String rcr) {
        try {
            final StringBuffer text = new StringBuffer();
            ValueSetDefinition vsd = lb_gui_.getValueSetDefinitionService().getValueSetDefinition(new URI(rcr), null);
            text.append("<b>Value Set Definition URI :</b> " + rcr + "\n");
            if (vsd != null)
            {
                text.append("<b>Value Set Definition Name :</b> " + vsd.getValueSetDefinitionName() + "\n");
                fieldHelper(text, vsd.getDefaultCodingScheme(), "Default Coding Scheme");
                fieldHelper(text, vsd.getConceptDomain(), "Concept Domain");
                if (vsd.getEntityDescription() != null)
                    fieldHelper(text, vsd.getEntityDescription().getContent(), "Entity Description");
                fieldHelper(text, vsd.getIsActive(), "Is Active");
                if (vsd.getOwner() != null)
                    fieldHelper(text, vsd.getOwner(), "Owner");
                fieldHelper(text, vsd.getStatus(), "Status");
                if (vsd.getEffectiveDate() != null)
                    fieldHelper(text, vsd.getEffectiveDate().toString(),"Effective Date");

                if (vsd.getExpirationDate() != null)
                    fieldHelper(text, vsd.getExpirationDate().toString(), "Expiration Date");
                
                for (Source src : vsd.getSource())
                {
                    fieldHelper(text, src.getContent(), "Source");
                }
                for (String cxt : vsd.getRepresentsRealmOrContext())
                {
                    fieldHelper(text, cxt, "Represents Realm Or Context");
                }
                if (vsd.getProperties() != null)
                {
                    Property[] p = vsd.getProperties().getProperty();
                    presentationPrinter(p, text);
                }
                
                for (DefinitionEntry de : vsd.getDefinitionEntryAsReference())
                {
                    text.append("<b>Value Set Definition Entries :</b> " + "\n");
                    fieldHelper(text, de.getRuleOrder().toString(), "Rule Order");
                    fieldHelper(text, de.getOperator().name(), "Operator");
                    
                    EntityReference er = de.getEntityReference();
                    if (er != null)
                    {
                        fieldHelper(text, er.getEntityCode(), "Entity Reference - Entity Code");
                        fieldHelper(text, er.getEntityCodeNamespace(), "Entity Reference - Entity Code Namespace");
                        fieldHelper(text, er.getReferenceAssociation(), "Entity Reference - Association");
                        fieldHelper(text, er.getTransitiveClosure(), "Entity Reference - Transitve Closure");
                        fieldHelper(text, er.getTargetToSource(), "Entity Reference - Target To Source");
                        fieldHelper(text, er.getLeafOnly(), "Entity Reference - Leaf Only");
                    }
                    CodingSchemeReference csr = de.getCodingSchemeReference();
                    if (csr != null)
                    {
                        fieldHelper(text, csr.getCodingScheme(), "Coding Scheme Reference - Coding Scheme");
                    }
                    ValueSetDefinitionReference vsdr = de.getValueSetDefinitionReference();
                    if (vsdr != null)
                    {
                        fieldHelper(text, vsdr.getValueSetDefinitionURI(), "Value Set Definition Reference - Value Set Definition URI");
                    }
                    PropertyReference pr = de.getPropertyReference();
                    if (pr != null)
                    {
                        fieldHelper(text, pr.getCodingScheme(), "Property Reference - Coding Scheme");
                        fieldHelper(text, pr.getPropertyName(), "Property Reference - Property Name");
                        
                        PropertyMatchValue pmv = pr.getPropertyMatchValue();
                        if (pmv != null)
                        {
                            fieldHelper(text, pmv.getContent(), "Property Reference - Property Match Value");
                            fieldHelper(text, pmv.getMatchAlgorithm(), "Property Reference - Property Match Algorithm");
                        }
                    }
                }
            }

            shell_.getDisplay().syncExec(new Runnable() {
                public void run() {
                    TextContent tc = new TextContent(shell_.getDisplay());
                    tc.setContent(text.toString());
                    codeDetails_.setText(tc.toPlainText());
                    codeDetails_.setStyleRanges(tc.getStyleRanges());
                }
            });

        } catch (RuntimeException e) {
            log.error("Unexpected Error", e);
            shell_.getDisplay().syncExec(new Runnable() {
                public void run() {
                    codeDetails_
                            .setText("Sorry, and unexpected error occurred.  See the log for details.");
                }
            });

        } catch (LBException e) {
            log.error("Unexpected Error", e);
            shell_.getDisplay().syncExec(new Runnable() {
                public void run() {
                    codeDetails_
                            .setText("Sorry, and unexpected error occurred.  See the log for details.");
                }
            });
        } catch (URISyntaxException e) {
            log.error("Unexpected Error", e);
            shell_.getDisplay().syncExec(new Runnable() {
                public void run() {
                    codeDetails_
                            .setText("Sorry, and unexpected error occurred.  See the log for details.");
                }
            });
        }
    }

    private void presentationPrinter(Property[] p, StringBuffer text) {
        if (p != null) {
            for (int i = 0; i < p.length; i++) {
                if (p[i].getValue() != null) {
                    fieldHelper(text, p[i].getValue().getContent(), p[i]
                            .getClass().getSimpleName());
                    fieldHelper(text, p[i].getPropertyName(),
                            "   Property Name");
                    fieldHelper(text, p[i].getPropertyId(), "   Property Id");
                    fieldHelper(text, p[i].getLanguage(), "   Language");

                    if (p[i] instanceof Presentation) {
                        Presentation presentation = (Presentation) p[i];
                        fieldHelper(text, presentation.getIsPreferred(),
                                "   Is Preferred");
                        fieldHelper(text, presentation.getDegreeOfFidelity(),
                                "   Degree Of Fidelity");
                        fieldHelper(text, presentation.getMatchIfNoContext(),
                                "   Match If No Context");
                        fieldHelper(text, presentation
                                .getRepresentationalForm(),
                                "   Representational Form");
                    } else if (p[i] instanceof Definition) {
                        Definition definition = (Definition) p[i];
                        fieldHelper(text, definition.getIsPreferred(),
                                "   Is Preferred");

                    }

                    Source[] s = p[i].getSource();
                    if (s != null) {
                        for (int j = 0; j < s.length; j++) {
                            fieldHelper(text, s[j].getContent()
                                    + " , <b>Role:</b> " + s[j].getRole()
                                    + ", <b>SubRef:</b> " + s[j].getSubRef(),
                                    "   Source");
                        }
                    }

                    String[] uc = p[i].getUsageContext();
                    if (uc != null) {
                        for (int j = 0; j < uc.length; j++) {
                            fieldHelper(text, uc[j], "   Usage Content");
                        }
                    }
                    PropertyQualifier[] pq = p[i].getPropertyQualifier();
                    if (pq != null) {
                        for (int j = 0; j < pq.length; j++) {
                            fieldHelper(text, pq[j].getPropertyQualifierName()
                                    + " , <b>Property Qualifier Content:</b> "
                                    + pq[j].getValue().getContent(),
                                    "   Property Qualifier Id");
                        }
                    }

                }
            }
        }
    }

    private void fieldHelper(StringBuffer text, String field, String fieldName) {
        if (field != null && field.length() > 0) {
            text.append("<b>" + fieldName + ":</b> " + field + "\n");
        }
    }

    private void fieldHelper(StringBuffer text, Boolean field, String fieldName) {
        if (field != null) {
            text.append("<b>" + fieldName + ":</b> " + field.toString() + "\n");
        }
    }

    private void resolveFinished(final boolean forward) {
        shell_.getDisplay().syncExec(new Runnable() {
            public void run() {
                try {
                    if (resolveException_ != null) {
                        throw resolveException_;
                    }

                    displayedResults_ = new ArrayList<String>();
                    displayedResultsCodes_ = new HashSet<String>();
                    
                    while (codeIterator_.hasNext())
                    {
                        String uri = codeIterator_.next();
                        addCodeToDisplayedResults(uri);
                    }

                    stack_.topControl = resultsComposite_;
                    shell_.layout();
                    shell_.setCursor(null);
                } catch (final LBException e) {
                    e.printStackTrace();
                    log.error("Unexpected Error", e);
                    busyResolvingLabel_
                            .setText("Sorry, an error occured while resolving the VSD URI.  See the log for full details.  \n\n"
                                    + e.getMessage());
                    busyResolvingLabel_.getParent().layout();

                    progressBar_.setVisible(false);
                    shell_.setCursor(null);
                }
            }
        });
    }

    private class Resolver implements Runnable {
        private List<String> vsdURIs_;
        private VDDisplayFilterResult caller_;

        public Resolver(List<String> vsdURIs, VDDisplayFilterResult caller) {
            vsdURIs_ = vsdURIs;
            caller_ = caller;
        }

        public void run() {
            if (vsdURIs_ == null)
            {
                caller_.resolveException_ = new LBException("There was a problem resolving Value Set Definition Query, please check the definition for any errors.");
                caller_.resolveFinished(true);
                return;
            }
            
            VDDisplayFilterResult.this.codeIterator_ = vsdURIs_.listIterator();

            caller_.resolveFinished(true);
        }
    }

    /*
     * If absent, adds the given reference to the maintained results and list
     * control.
     */
    private void addCodeToDisplayedResults(final String vsdURI) {
        addOrUpdateDisplayedResults(vsdURI, -1, -1, -1, SWT.NONE, -1, -1, true);
    }

    private void addOrUpdateDisplayedResults(
            final String vsdURI, int addFontStyle,
            int addForeground, int addBackground, int updFontStyle,
            int updForeground, int updBackground, boolean replace) {
        int oldPos = -1;
        int newPos = -1;
        String qualifiedCode = vsdURI;

        // Determine the position to alter, if item is new or the style has
        // changed.
        // Item is new?
        if (!(displayedResultsCodes_.contains(qualifiedCode))) {
            displayedResultsCodes_.add(qualifiedCode);
            displayedResults_.add(vsdURI);
            newPos = displayedResults_.size() - 1;
        }

        // Item is not new, do we need to alter the existing item?
        else if (replace || updFontStyle >= 0 || updForeground >= 0
                || updBackground >= 0) {
            for (int i = 0; i < displayedResults_.size(); i++) {
                String ref = displayedResults_.get(i);
                if (ref.equals(vsdURI)) {
                    oldPos = i;
                    break;
                }
            }
            if (oldPos >= 0) {
                if (replace) {
                    displayedResults_.remove(oldPos);
                    displayedResults_.add(vsdURI);
                    newPos = displayedResults_.size() - 1;
                } else
                    newPos = oldPos;
            }
        }

        // Was the position set to indicate a new or changed item?
        if (newPos >= 0) {
            boolean isAdd = oldPos < 0;
            final int fNewPos = newPos;
            final int fOldPos = oldPos;
            final int fStyle = isAdd ? addFontStyle : updFontStyle;
            final int fFrgnd = isAdd ? addForeground : updForeground;
            final int fBkgnd = isAdd ? addBackground : updBackground;

            shell_.getDisplay().syncExec(new Runnable() {
                public void run() {
                    int fontStyle = fStyle;
                    Color foreColor = null;
                    Color backColor = null;
                    FontData data[] = null;

                    // Preserve old item properties on replace ...
                    if (fOldPos >= 0 && fNewPos != fOldPos) {
                        TableItem tiOld = displayedCodeList_.getItem(fOldPos);
                        if (fontStyle < 0)
                            data = tiOld.getFont().getFontData();
                        if (fFrgnd < 0)
                            foreColor = tiOld.getForeground();
                        if (fBkgnd < 0)
                            backColor = tiOld.getBackground();
                        displayedCodeList_.remove(fOldPos);
                    }

                    // Fetch or create a new item to satisfy the request ...
                    TableItem ti = (fNewPos == fOldPos) ? displayedCodeList_
                            .getItem(fOldPos) : new TableItem(
                            displayedCodeList_, SWT.NONE, fNewPos);

                    // Merge new item properties ...
                    if (fontStyle >= 0) {
                        data = ti.getFont().getFontData();
                        for (int i = 0; i < data.length; i++)
                            data[i].setStyle(data[i].getStyle() | fontStyle);
                        Font newFont = new Font(shell_.getDisplay(), data);
                        ti.setFont(newFont);
                    }
                    if (fFrgnd >= 0)
                        foreColor = shell_.getDisplay().getSystemColor(fFrgnd);
                    if (foreColor != null)
                        ti.setForeground(foreColor);
                    if (fBkgnd >= 0)
                        backColor = shell_.getDisplay().getSystemColor(fBkgnd);
                    if (backColor != null)
                        ti.setBackground(backColor);

                    // Finally, set the text ...
                    ti.setText(vsdURI);
                }
            });
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (codeIterator_ != null) {
            codeIterator_.remove();
            codeIterator_ = null;
        }
    }
}