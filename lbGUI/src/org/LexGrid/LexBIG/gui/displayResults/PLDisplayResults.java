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

import java.util.ArrayList;
import java.util.HashSet;

import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.gui.DialogHandler;
import org.LexGrid.LexBIG.gui.LB_VSD_GUI;
import org.LexGrid.LexBIG.gui.Utility;
import org.LexGrid.LexBIG.gui.plugin.PluginRetriever;
import org.LexGrid.LexBIG.gui.plugin.TabbedContent;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Presentation;
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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.lexgrid.valuesets.dto.ResolvedPickListEntry;
import org.lexgrid.valuesets.dto.ResolvedPickListEntryList;

import prefuse.Constants;

/**
 * This class displays the results of a CodedNodeSet query in a shell.
 * 
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 * @author <A HREF="mailto:leisch.jason@mayo.edu">Jason Leisch</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class PLDisplayResults {
    private static Logger log = LogManager.getLogger("LexBIG.GUI");
    private LB_VSD_GUI lb_gui_;
    private DialogHandler dialog_;
    private Shell shell_;
    private LBException resolveException_;
    private ProgressBar progressBar_;
    private Composite resultsComposite_;
    private StackLayout stack_;
    private ResolvedConceptReference[] graphRcr_;
    private ArrayList<ResolvedPickListEntry> displayedResults_;
    private HashSet<String> displayedResultsCodes_;
    private Table displayedCodeList_;
    private static String FETCHING_MORE = "Getting Results...";
    private int fetchSize = 50;
    private StyledText codeDetails_;
    private Label busyResolvingLabel_;
    private Button showSecondaryRel_button;
    private boolean showSecondaryRel_selected = false;
    private Button showCodes_button;
    private boolean showCodes_selected = true;
    private boolean graphMode_ = false;
    private VDGraphView primaryGraph;
    private ConceptTreeView primaryTree;
    private final boolean twoGraphs;
    private TabFolder tabs;
    private ResolvedPickListEntryList pleList_;
    /*
     * The subsetGraph/Tree are used only in the Coded Node Graph case (ie, not
     * the "Coded Node Set" case). They will show the coded node set, so that
     * the Coded Node Graph shows all 4 views.
     */
    private ConceptTreeView secondaryTree;
    private Object currentControl;

    public PLDisplayResults(LB_VSD_GUI lb_gui, ResolvedPickListEntryList pleList,
            SortOptionList sortOptions, boolean flatDisplay,
            boolean resolveForward, boolean resolveBackward, int resolveDepth,
            int maxToReturn, ConceptReference graphFocus) {
        this.twoGraphs = !flatDisplay;
        preInit(lb_gui);

//        resolveGraph(pleList, sortOptions, flatDisplay, resolveForward,
//                resolveBackward, resolveDepth, maxToReturn, graphFocus);
    }

    public PLDisplayResults(LB_VSD_GUI lb_gui, ResolvedPickListEntryList pleList,
            SortOptionList sortOptions) {
        this.twoGraphs = false;
        preInit(lb_gui);

        resolvePL(pleList, sortOptions);

    }

    private void preInit(LB_VSD_GUI lb_gui) {
        lb_gui_ = lb_gui;
        shell_ = new Shell(lb_gui_.getShell().getDisplay());
        shell_.setSize(640, 480);

        dialog_ = new DialogHandler(shell_);

        shell_.setText("Result Browser");

        init();

        shell_.open();

        shell_.addShellListener(new ShellAdapter() {
            public void shellClosed(ShellEvent arg0) {
                if (pleList_ != null) {
                    pleList_.removeAllResolvedPickListEntry();
                    pleList_ = null;
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

        // Right-hand controls ...
        // The panels on the right side will be "sash"-ed as well, to allow for
        // optimal viewing of the graph with lower resolutions
        SashForm rightTopBottom = new SashForm(leftRight, SWT.VERTICAL);

        rightTopBottom.setLayoutData(new GridData(GridData.FILL_BOTH));
        rightTopBottom.setLayout(new GridLayout());

        // Add html-capable form for display of selected item details ...
        codeDetails_ = new StyledText(rightTopBottom, SWT.WRAP | SWT.BORDER
                | SWT.MULTI | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL);

        /*
         * In the lower right corner is a tabbed form with two tabs, along with
         * two checkboxes.
         * 
         * The first tab is the graph view. The 2nd tab is the new tree view.
         */
        Composite lowerRight = new Composite(rightTopBottom, SWT.NONE);
        lowerRight.setLayout(new GridLayout(1, false));

        buildTabFolder(lowerRight);
        addUserTabs();

        // Add checkboxes ...
        Composite graphChoices = new Composite(lowerRight, SWT.NONE);
        graphChoices.setLayout(new GridLayout(1, false));
        graphChoices.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        leftRight.setWeights(new int[] { 30, 70 });
        rightTopBottom.setWeights(new int[] { 30, 70 });
        stack_.topControl = busyComposite;
    }

    private void addUserTabs() {
        PluginRetriever pr = new PluginRetriever();
        TabbedContent[] plugins = pr.getTabbedContentPlugins();
        for (TabbedContent tc : plugins) {
            try {
                addContentTab(tc);
            } catch (Exception e) {
                log.error("Unexpected Error from plugin during initialization",
                        e);
            }
        }
    }

    private void buildTabFolder(final Composite parentComposite) {
        tabs = new TabFolder(parentComposite, SWT.TOP);
        tabs.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                currentControl = tabs.getItem(tabs.getSelectionIndex())
                        .getData();
            }
        });
        tabs.setLayoutData(new GridData(GridData.FILL_BOTH
                | GridData.GRAB_VERTICAL));
    }

    public TabItem addContentTab(TabbedContent control) {
        control.setParentComposite(tabs);
        TabItem newTab = new TabItem(tabs, SWT.NONE);
        newTab.setData(control);
        newTab.setText(control.getTabName());
        newTab.setControl(control.getControl());
        return newTab;
    }

    private void resolvePL(ResolvedPickListEntryList pleList, SortOptionList sortOptions) {
        Resolver resolver = new Resolver(pleList, this, sortOptions);

        shell_.setCursor(shell_.getDisplay().getSystemCursor(SWT.CURSOR_WAIT));

        new Thread(resolver).start();
    }

    private class Resolver implements Runnable {
        private PLDisplayResults caller_;
        private SortOptionList sortOptions_;

        public Resolver(ResolvedPickListEntryList pleList, PLDisplayResults caller,
                SortOptionList sortOptions) {
            pleList_ = pleList;
            caller_ = caller;
            sortOptions_ = sortOptions;
        }

        public void run() {
//            try {
//                PLDisplayResults.this.codeIterator_ = cns_.resolve(
//                        sortOptions_, null, null);
//
//            } catch (LBException e) {
//                caller_.resolveException_ = e;
//            }

            caller_.resolveFinished(true);
        }
    }
    
    private void resolveFinished(final boolean forward) {
        shell_.getDisplay().syncExec(new Runnable() {
            public void run() {
                try {
                    if (resolveException_ != null) {
                        throw resolveException_;
                    }

//                    ResolvedConceptReference[] codes = null;
                    ResolvedPickListEntry[] codes = pleList_.getResolvedPickListEntry();
                    
                    displayedResults_ = new ArrayList<ResolvedPickListEntry>();
                    displayedResultsCodes_ = new HashSet<String>();

                    for (int i = 0; i < codes.length; i++) {
                        addCodeToDisplayedResults(codes[i]);
                    }

                    boolean hasNextTemp = false;
                    int numRemainingTemp = -1;

//                    if (codeIterator_ != null && codes.length > 0) {
//                        numRemainingTemp = codeIterator_.numberRemaining();
//                        hasNextTemp = codeIterator_.hasNext();
//                        if (!hasNextTemp) {
//                            codeIterator_.release();
//                            codeIterator_ = null;
//                        }
//                    }

                    if (hasNextTemp && !twoGraphs) {
                        TableItem ti = new TableItem(displayedCodeList_,
                                SWT.NONE);
                        ti.setText("Fetch More Results (" + numRemainingTemp
                                + " remaining)...");
                    }
                    stack_.topControl = resultsComposite_;
                    shell_.layout();
                    shell_.setCursor(null);
                } catch (final LBException e) {
                    e.printStackTrace();
                    log.error("Unexpected Error", e);
                    busyResolvingLabel_
                            .setText("Sorry, an error occured while resolving the codes.  See the log for full details.  \n\n"
                                    + e.getMessage());
                    busyResolvingLabel_.getParent().layout();

                    progressBar_.setVisible(false);
                    shell_.setCursor(null);
                }
            }
        });
    }
    
    private final SelectionListener codeListSelectionListener = new SelectionListener() {
        public void widgetDefaultSelected(SelectionEvent e) {
        }

        public void widgetSelected(SelectionEvent e) {
            int index = displayedCodeList_.getSelectionIndex();
            if (index == -1) {
                return;
            }
            if (index == displayedResults_.size()) {
                if (displayedCodeList_.getItem(index).equals(FETCHING_MORE)) {
                    // in the process of fetching more... do nothing.
                } else {
                    // this means that they clicked on the "fetch more results"
                    // item.
                    TableItem ti = displayedCodeList_.getItem(index);
                    ti.setText(FETCHING_MORE);
//                    resolveMoreResults();
                }
                codeDetails_.setText("More results are being retrieved.");

            } else {
                ResolvedPickListEntry rple = displayedResults_.get(index);
                displayConceptDetails(rple);
                addOrUpdateDisplayedResults(rple, -1, SWT.COLOR_BLUE, -1, -1,
                        SWT.COLOR_BLUE, -1, false);
//                TabItem[] userTabs = tabs.getItems();
//                for (TabItem userTab : userTabs) {
//                    Object c = userTab.getData();
//                    if (c instanceof TabbedContent) {
//                        TabbedContent tc = (TabbedContent) c;
//                        try {
//                            tc.conceptSelected(rple);
//                        } catch (Exception ex) {
//                            log
//                                    .error(
//                                            "Unexpected Error from plugin during conceptSelected",
//                                            ex);
//                        }
//                    }
//                }
            }
        }
    };
    
    protected void displayConceptDetails(ResolvedPickListEntry rple) {
        try {
            final StringBuffer text = new StringBuffer();
            text.append("<b>Coding Scheme:</b> " + rple.getEntityCodeNamespace() + "\n");
            text.append("<b>Entity Code:</b> " + rple.getEntityCode() + "\n");
            text.append("<b>Pick Text:</b> " + rple.getPickText() + "\n");
            text.append("<b>Property Id:</b> " + rple.getPropertyId() + "\n");
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

//    private void resolveMoreResults() {
//        new Thread(new ResolveMore()).start();
//    }

//    private class ResolveMore implements Runnable {
//
//        public void run() {
//            try {
//                if (codeIterator_ != null && codeIterator_.hasNext()) {
//                    final ResolvedConceptReference[] codes = codeIterator_
//                            .next(fetchSize).getResolvedConceptReference();
//                    // gui update code
//                    shell_.getDisplay().syncExec(new Runnable() {
//                        public void run() {
//                            // remove the - FETCHING_MORE item.
//                            displayedCodeList_.remove(displayedCodeList_
//                                    .getItemCount() - 1);
//                        }
//                    });
//
//                    for (int i = 0; i < codes.length; i++) {
//                        addCodeToDisplayedResults(codes[i]);
//                    }
//                    final int numRemaining = codeIterator_.numberRemaining();
//                    final boolean hasNext = codeIterator_.hasNext();
//                    if (!hasNext) {
//                        codeIterator_.release();
//                        codeIterator_ = null;
//                    }
//
//                    // gui update code
//                    shell_.getDisplay().syncExec(new Runnable() {
//                        public void run() {
//                            if (hasNext) {
//                                TableItem ti = new TableItem(
//                                        displayedCodeList_, SWT.NONE);
//                                ti.setText("Fetching More Results ("
//                                        + numRemaining + " remaining)...");
//                            }
//                        }
//                    });
//
//                }
//            } catch (LBException e) {
//                dialog_.showError("Unexpected Error",
//                        "There was an unexpected problem while getting more results - "
//                                + e.toString());
//            }
//        }
//
//    }

    /*
     * If absent, adds the given reference to the maintained results and list
     * control.
     */
    private void addCodeToDisplayedResults(final ResolvedPickListEntry rple) {
        addOrUpdateDisplayedResults(rple, -1, -1, -1, SWT.NONE, -1, -1, true);
    }

    /**
     * If absent, adds the given reference to the maintained results and list
     * control using the specified style settings. If present, alters the
     * existing list entry according to the given style settings.
     * 
     * @param rcr
     *            The resolved concept reference to display.
     * @param fontStyle
     *            The style to use for the associated list item; -1 for default
     *            (new item) or no change (existing item).
     * @param addFontStyle
     *            The font style if a list item is added; -1 for default.
     * @param addForeground
     *            The text color if a list item is added; -1 for default.
     * @param addBackground
     *            The area color if a list item is added; -1 for default.
     * @param updFontStyle
     *            The font style if a list item is updated; -1 for default.
     * @param updForeground
     *            The text color if a list item is updated; -1 for default.
     * @param updBackground
     *            The area color if a list item is updated; -1 for default.
     * @param replace
     *            True if an existing item should be removed (if present) and
     *            re-introduced at list end; false to maintain the current list
     *            item. If replaced, attributes from the old node are carried
     *            forward unless overridden by update parameters.
     */
    private void addOrUpdateDisplayedResults(
            final ResolvedPickListEntry rple, int addFontStyle,
            int addForeground, int addBackground, int updFontStyle,
            int updForeground, int updBackground, boolean replace) {
        int oldPos = -1;
        int newPos = -1;
        String qualifiedCode = new StringBuffer().append(
                rple.getEntityCodeNamespace()).append(':').append(
                rple.getEntityCode()).append(':').append(rple.getPickText()).append(':').append(rple.getPropertyId())
                .toString();

        // Determine the position to alter, if item is new or the style has
        // changed.
        // Item is new?
        if (!(displayedResultsCodes_.contains(qualifiedCode))) {
            displayedResultsCodes_.add(qualifiedCode);
            displayedResults_.add(rple);
            newPos = displayedResults_.size() - 1;
        }

        // Item is not new, do we need to alter the existing item?
        else if (replace || updFontStyle >= 0 || updForeground >= 0
                || updBackground >= 0) {
            for (int i = 0; i < displayedResults_.size(); i++) {
                ResolvedPickListEntry ref = displayedResults_.get(i);
                if (ref.getEntityCode().equals(rple.getEntityCode())
                        && (ref.getEntityCodeNamespace() == null || ref
                                .getEntityCodeNamespace().equals(
                                        rple.getEntityCodeNamespace())
                                        && (ref.getPickText() == null || 
                                                ref.getPickText().equals(
                                                        rple.getPickText())))
                        && (ref.getPropertyId() == null || ref
                                .getPropertyId().equals(
                                        rple.getPropertyId()))) {
                    oldPos = i;
                    break;
                }
            }
            if (oldPos >= 0) {
                if (replace) {
                    displayedResults_.remove(oldPos).getEntityCode();
                    displayedResults_.add(rple);
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
                    ti.setText(new StringBuffer().append(rple.getEntityCode())
                            .append(" - ").append(
                                    rple.getPickText() != null ? (rple
                                            .getPickText() != null ? rple
                                            .getPickText() : "") : "")
                            .toString());
                }
            });
        }
    }

    /*
     * When a graph node is selected, I want to find it in the code list, and
     * select it there.
     */
    int loop;

    protected void graphItemSelected(final ResolvedPickListEntry rple) {
        // deselect current item ...
        shell_.getDisplay().syncExec(new Runnable() {
            public void run() {
                displayedCodeList_.deselectAll();
            }
        });

        // ensure the new item is in the list ...
        addOrUpdateDisplayedResults(rple, SWT.ITALIC, SWT.COLOR_BLUE, -1,
                SWT.NONE, SWT.COLOR_BLUE, -1, false);

        // attempt to find and select the matching item ...
        for (loop = 0; loop < displayedResults_.size(); loop++) {
            ResolvedPickListEntry cur = displayedResults_.get(loop);

            if (cur.getEntityCodeNamespace().equals(rple.getEntityCodeNamespace())
                    && cur.getEntityCode().equals(rple.getEntityCode())
                    && cur.getPickText().equals(rple.getPickText())) {
                shell_.getDisplay().syncExec(new Runnable() {
                    public void run() {
                        displayedCodeList_.select(loop);
                        displayedCodeList_.showSelection();
                    }
                });
                break;
            }
        }

        // update the details view
        displayConceptDetails(rple);
    }

    /**
     * Indicates whether codes should be displayed with text in the graph.
     */
    public boolean getShowCodesInGraph() {
        return showCodes_selected;
    }

    /**
     * Indicates whether edges should be drawn between secondary nodes.
     */
    public boolean getShowSecondaryRelInGraph() {
        return showSecondaryRel_selected;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
//        if (codeIterator_ != null) {
//            codeIterator_.release();
//            codeIterator_ = null;
//        }
    }
}