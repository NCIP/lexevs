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

import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.gui.DialogHandler;
import org.LexGrid.LexBIG.gui.LB_VSD_GUI;
import org.LexGrid.LexBIG.gui.Utility;
import org.LexGrid.LexBIG.gui.plugin.PluginRetriever;
import org.LexGrid.LexBIG.gui.plugin.TabbedContent;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.concepts.Comment;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.concepts.PropertyLink;
import org.LexGrid.relations.Relations;
import org.apache.commons.lang.StringUtils;
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
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import prefuse.Constants;
import prefuse.data.Node;

/**
 * This class displays the results of a CodedNodeSet query in a shell.
 * 
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 * @author <A HREF="mailto:leisch.jason@mayo.edu">Jason Leisch</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class VDDisplayCodedNodeSet {
    private static Logger log = LogManager.getLogger("LexBIG.GUI");
    private LB_VSD_GUI lb_gui_;
    private DialogHandler dialog_;
    private Shell shell_;
    private LBException resolveException_;
    private ProgressBar progressBar_;
    private Composite resultsComposite_;
    private StackLayout stack_;
    private ResolvedConceptReferencesIterator codeIterator_;
    private ResolvedConceptReference[] graphRcr_;
    private ArrayList<ResolvedConceptReference> displayedResults_;
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

    /*
     * The subsetGraph/Tree are used only in the Coded Node Graph case (ie, not
     * the "Coded Node Set" case). They will show the coded node set, so that
     * the Coded Node Graph shows all 4 views.
     */
    private VDGraphView secondaryGraph;
    private ConceptTreeView secondaryTree;
    private Object currentControl;

    public VDDisplayCodedNodeSet(LB_VSD_GUI lb_gui, CodedNodeGraph cng,
            SortOptionList sortOptions, boolean flatDisplay,
            boolean resolveForward, boolean resolveBackward, int resolveDepth,
            int maxToReturn, ConceptReference graphFocus) {
        this.twoGraphs = !flatDisplay;
        preInit(lb_gui);

        resolveGraph(cng, sortOptions, flatDisplay, resolveForward,
                resolveBackward, resolveDepth, maxToReturn, graphFocus);
    }

    public VDDisplayCodedNodeSet(LB_VSD_GUI lb_gui, CodedNodeSet cns,
            SortOptionList sortOptions) {
        this.twoGraphs = false;
        preInit(lb_gui);

        resolveCodeSet(cns, sortOptions);

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
                if (codeIterator_ != null) {
                    try {
                        codeIterator_.release();
                    } catch (LBResourceUnavailableException e) {
                        // do nothing.
                    }
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
        buildBottomTabs(tabs);
        addUserTabs();

        // Add checkboxes ...
        Composite graphChoices = new Composite(lowerRight, SWT.NONE);
        graphChoices.setLayout(new GridLayout(1, false));
        graphChoices.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        showCodes_button = new Button(graphChoices, SWT.CHECK);
        showCodes_button.setText("Show &codes");
        showCodes_button.setSelection(showCodes_selected);
        showCodes_button.addSelectionListener(graphingCheckboxListener);

        showSecondaryRel_button = new Button(graphChoices, SWT.CHECK);
        showSecondaryRel_button
                .setText("Show non-hierarchical &relations (graph only)");
        showSecondaryRel_button.setSelection(showSecondaryRel_selected);
        showSecondaryRel_button.addSelectionListener(graphingCheckboxListener);

        // Add graph viewer ...
        // Graph graph = new Graph(true);
        // graph.addNode();

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

    private void buildBottomTabs(final TabFolder tabFolder) {

        primaryGraph = new VDGraphView(tabFolder, VDDisplayCodedNodeSet.this);
        TabItem primaryGraphTab = new TabItem(tabFolder, SWT.NONE);
        primaryGraphTab.setText("Association Graph");
        primaryGraphTab.setControl(primaryGraph.getSwtComposite());
        primaryGraphTab.setData(primaryGraph);

        TabItem primaryTreeTab = new TabItem(tabFolder, SWT.NONE);
        primaryTreeTab.setText("Association Tree");
        primaryTree = new ConceptTreeView(shell_, tabFolder);
        primaryTreeTab.setControl(primaryTree.getTree());
        primaryTree.addSelectionListener(new TreeSelectionListener());

        currentControl = primaryGraph;

        if (twoGraphs) {
            secondaryGraph = new VDGraphView(tabFolder, VDDisplayCodedNodeSet.this);
            TabItem secondaryGraphTab = new TabItem(tabFolder, SWT.NONE);
            secondaryGraphTab.setData(secondaryGraph);
            secondaryGraphTab.setText("Subset Graph");
            secondaryGraphTab.setControl(secondaryGraph.getSwtComposite());
            TabItem secondaryTreeTab = new TabItem(tabFolder, SWT.NONE);
            secondaryTreeTab.setText("Subset Tree");
            secondaryTree = new ConceptTreeView(shell_, tabFolder);
            secondaryTreeTab.setControl(secondaryTree.getTree());
            secondaryTree.addSelectionListener(new TreeSelectionListener());
        }
    }

    /**
     * A SelectionListener to handle the the selection of elements in the tree.
     * When something is selected in the tree, update the corresponding graphs
     * to focus to (if the full graph) the matching node, or to re-root with (if
     * the subsetted graph) the matching node.
     * 
     * Also update the concept list to highlight the matching concept in blue.
     * 
     */
    private class TreeSelectionListener implements SelectionListener {
        public void widgetDefaultSelected(SelectionEvent e) {
            Tree t = (Tree) e.getSource();
            TreeItem ti = t.getSelection()[0];
            if (ti == null) {
                return;
            }
            Object o = ti.getData();
            if (!(o instanceof ResolvedConceptReference)) {
                return;
            }
            ResolvedConceptReference rcr = (ResolvedConceptReference) o;
            displayConceptDetails(rcr);
            updateGraphForConceptSelection(rcr);
            addOrUpdateDisplayedResults(rcr, -1, SWT.COLOR_BLUE, -1, -1,
                    SWT.COLOR_BLUE, -1, false);

        }

        public void widgetSelected(SelectionEvent e) {
        }
    }

    /**
     * A selection listener to handle: - the display of secondary relations -
     * the display of codes on concepts
     */
    private final SelectionListener graphingCheckboxListener = new SelectionListener() {
        public void widgetSelected(SelectionEvent e) {
            if (e.getSource() == showSecondaryRel_button) {
                shell_.getDisplay().syncExec(new Runnable() {
                    public void run() {
                        showSecondaryRel_selected = showSecondaryRel_button
                                .getSelection();
                        primaryGraph.m_vis.run("filter");
                        if (twoGraphs) {
                            secondaryGraph.m_vis.run("filter");
                        }
                    }
                });
            } else if (e.getSource() == showCodes_button) {
                shell_.getDisplay().syncExec(new Runnable() {
                    public void run() {
                        showCodes_selected = showCodes_button.getSelection();
                        primaryGraph.getGraph().refreshNodeNames(
                                showCodes_selected, showSecondaryRel_selected);
                        primaryGraph.m_vis.run("filter");
                        primaryTree.setCodesShown(showCodes_selected);
                        if (twoGraphs) {
                            secondaryGraph.getGraph().refreshNodeNames(
                                    showCodes_selected,
                                    showSecondaryRel_selected);
                            secondaryGraph.m_vis.run("filter");
                            secondaryTree.setCodesShown(showCodes_selected);
                        }
                    }
                });
            }
        }

        public void widgetDefaultSelected(SelectionEvent e) {
        }
    };

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
                    resolveMoreResults();
                }
                codeDetails_.setText("More results are being retrieved.");

            } else {
                ResolvedConceptReference rcr = displayedResults_.get(index);
                displayConceptDetails(rcr);
                updateTreeForConceptSelection(rcr);
                updateGraphForConceptSelection(rcr);
                addOrUpdateDisplayedResults(rcr, -1, SWT.COLOR_BLUE, -1, -1,
                        SWT.COLOR_BLUE, -1, false);
                TabItem[] userTabs = tabs.getItems();
                for (TabItem userTab : userTabs) {
                    Object c = userTab.getData();
                    if (c instanceof TabbedContent) {
                        TabbedContent tc = (TabbedContent) c;
                        try {
                            tc.conceptSelected(rcr);
                        } catch (Exception ex) {
                            log
                                    .error(
                                            "Unexpected Error from plugin during conceptSelected",
                                            ex);
                        }
                    }
                }
            }
        }
    };

    private void updateTreeForConceptSelection(ResolvedConceptReference rcr) {
        if (!graphMode_) {
            return;
        }
        primaryTree.updateTreeForConceptSelection(rcr);
    }

    private void resolveCodeSet(CodedNodeSet cns, SortOptionList sortOptions) {
        Resolver resolver = new Resolver(cns, this, sortOptions);

        shell_.setCursor(shell_.getDisplay().getSystemCursor(SWT.CURSOR_WAIT));

        new Thread(resolver).start();
    }

    private void resolveGraph(CodedNodeGraph cng, SortOptionList sortOptions,
            boolean flat, boolean forward, boolean backward, int depth,
            int maxToReturn, ConceptReference focus) {
        GraphResolver resolver = new GraphResolver(cng, this, sortOptions,
                flat, forward, backward, depth, maxToReturn, focus);

        shell_.setCursor(shell_.getDisplay().getSystemCursor(SWT.CURSOR_WAIT));

        new Thread(resolver).start();
    }

    protected void displayConceptDetails(ResolvedConceptReference rcr) {
        try {
            final StringBuffer text = new StringBuffer();
            text.append("<b>Coding Scheme:</b> " + rcr.getCodingSchemeName()
                    + " - " + rcr.getCodingSchemeURI() + "\n");
            text.append("<b>Entity Code:</b> " + rcr.getConceptCode() + "\n");
            Entity node = rcr.getEntity();
            if (node != null) {
                fieldHelper(text, node.getEntityCodeNamespace(),
                        "Entity Code Namespace");
            }
            if (rcr.getEntityDescription() != null
                    && rcr.getEntityDescription().getContent() != null
                    && rcr.getEntityDescription().getContent().length() > 0) {
                text.append("<b>Entity Description:</b> "
                        + rcr.getEntityDescription().getContent() + "\n");
            }
            if (node != null) {
                for (String entityType : node.getEntityType()) {
                    fieldHelper(text, entityType, "Entity Type");
                }

                if (node.getOwner() != null)
                    fieldHelper(text, node.getOwner(), "Owner");

                fieldHelper(text, node.getStatus(), "Status");
                fieldHelper(text, node.getIsActive(), "Is Active");
                fieldHelper(text, node.getIsAnonymous(), "Is Anonymous");

                if (node.getEffectiveDate() != null)
                    fieldHelper(text, node.getEffectiveDate().toString(),
                            "Effective Date");

                if (node.getExpirationDate() != null)
                    fieldHelper(text, node.getExpirationDate().toString(),
                            "Expiration Date");

                Presentation[] p = node.getPresentation();
                presentationPrinter(p, text);

                Definition[] d = node.getDefinition();
                presentationPrinter(d, text);

                Property[] pr = node.getProperty();
                presentationPrinter(pr, text);

                PropertyLink[] pl = node.getPropertyLink();
                if (pl != null) {
                    for (int i = 0; i < pl.length; i++) {

                        fieldHelper(text, pl[i].getPropertyLink(),
                                "Property Link");
                        fieldHelper(text, pl[i].getSourceProperty(),
                                "   Source Property");
                        fieldHelper(text, pl[i].getTargetProperty(),
                                "   Target Property");
                    }
                }

                Comment[] c = node.getComment();
                presentationPrinter(c, text);

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

    private UpdateSelectionGraphResolver usgr_;

    private void updateGraphForConceptSelection(
            final ResolvedConceptReference rcr) {
        try {
            if (graphMode_) {
                primaryGraph.focusCode(rcr.getConceptCode(), (rcr
                        .getEntityDescription() == null ? "" : rcr
                        .getEntityDescription().getContent()));

                shell_.getDisplay().asyncExec(new Runnable() {
                    public void run() {
                        primaryTree.updateTreeForConceptSelection(rcr);
                    }
                });
                /*
                 * Added for coded node set within the coded node graph
                 */
                Graph temp = new Graph(true);

                temp.addNode("resolving relationships, please wait.");
                secondaryGraph.setGraph(temp);

                if (usgr_ != null) {
                    usgr_.stop = true;
                }
                usgr_ = new UpdateSelectionGraphResolver(rcr, secondaryGraph,
                        secondaryTree);
                Thread resolve = new Thread(usgr_);
                resolve.start();
            } else {
                Graph temp = new Graph(true);

                temp.addNode("resolving relationships, please wait.");
                primaryGraph.setGraph(temp);

                if (usgr_ != null) {
                    usgr_.stop = true;
                }
                usgr_ = new UpdateSelectionGraphResolver(rcr, primaryGraph,
                        primaryTree);
                Thread resolve = new Thread(usgr_);
                resolve.start();

            }
        } catch (Exception e) {
            log.error("Unexpected Error", e);
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

    private void resolveMoreResults() {
        new Thread(new ResolveMore()).start();
    }

    private void resolveFinished(final boolean forward) {
        shell_.getDisplay().syncExec(new Runnable() {
            public void run() {
                try {
                    if (resolveException_ != null) {
                        throw resolveException_;
                    }

                    ResolvedConceptReference[] codes = null;
                    if (graphRcr_ != null) {
                        // had a graph resolution
                        graphMode_ = true;
                        codes = graphRcr_;
                    } else {
                        graphMode_ = false;
                        codes = codeIterator_.next(fetchSize)
                                .getResolvedConceptReference();
                    }
                    displayedResults_ = new ArrayList<ResolvedConceptReference>();
                    displayedResultsCodes_ = new HashSet<String>();
                    Graph graph = null;
                    Node top = null;

                    if (graphMode_) {
                        primaryTree.addConcepts(codes);
                        graph = new Graph(true);
                        if (codes.length > 1) {
                            // if there is more than one code at the top level,
                            // automatically add
                            // the '@' (ultimate root) or '@@' (ultimate end)
                            // node in the graph view.
                            top = graph.addNode(forward ? "@" : "@@");
                        }
                    }

                    for (int i = 0; i < codes.length; i++) {
                        addCodeToDisplayedResults(codes[i]);
                        Node temp = null;
                        if (graph != null) {
                            temp = graph.addNode(codes[i],
                                    getShowCodesInGraph());
                        }

                        if (graph != null && top != null) {
                            graph.addEdge(top, temp);
                        }

                        graphAssociations(graph, temp, codes[i].getSourceOf(),
                                true, true);
                        graphAssociations(graph, temp, codes[i].getTargetOf(),
                                false, true);
                    }

                    primaryGraph.init(graph, "name");

                    if (twoGraphs) {
                        secondaryGraph.init(null, "name");
                    }
                    boolean hasNextTemp = false;
                    int numRemainingTemp = -1;

                    if (codeIterator_ != null && codes.length > 0) {
                        numRemainingTemp = codeIterator_.numberRemaining();
                        hasNextTemp = codeIterator_.hasNext();
                        if (!hasNextTemp) {
                            codeIterator_.release();
                            codeIterator_ = null;
                        }
                    }

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

    private void graphPropertyLinks(Graph graph, Node parentNode,
            ResolvedConceptReference rcr) {
        
        if (rcr == null || rcr.getEntity() == null)
            return;
        
        PropertyLink[] propertyLinks = rcr.getEntity().getPropertyLink();
        for (int i = 0; i < propertyLinks.length; i++) {
            PropertyLink propertyLink = propertyLinks[i];

            String sourcePropertyId = propertyLink.getSourceProperty();
            String targetPropertyId = propertyLink.getTargetProperty();

            String sourceText = "";
            String targetText = "";

            for (int j = 0; j < rcr.getEntity().getPresentation().length; j++) {
                Presentation pres = rcr.getEntity().getPresentation(j);
                String propertyId = pres.getPropertyId();
                if (propertyId.equals(sourcePropertyId)) {
                    sourceText = pres.getValue().getContent();
                }
                if (propertyId.equals(targetPropertyId)) {
                    targetText = pres.getValue().getContent();
                }

            }
            Node sourcePropertyLinkNode = graph.addNode("["
                    + rcr.getConceptCode() + "]" + " - "
                    + propertyLink.getSourceProperty() + " : " + sourceText,
                    rcr.getConceptCode() + propertyLink.getSourceProperty()
                            + "source");
            Node targetPropertyLinkNode = graph.addNode("["
                    + rcr.getConceptCode() + "]" + " - "
                    + propertyLink.getTargetProperty() + " : " + targetText,
                    rcr.getConceptCode() + propertyLink.getTargetProperty()
                            + "target");
            graph.addEdge(sourcePropertyLinkNode, targetPropertyLinkNode,
                    propertyLink.getPropertyLink());
            graph.addEdge(parentNode, sourcePropertyLinkNode, "PropertyLink");
        }
    }

    private void graphAssociations(Graph graph, Node parentNode,
            AssociationList al, boolean down, boolean addToResults) {
        if (al != null && al.getAssociationCount() > 0) {
            Association[] aList = al.getAssociation();
            for (int i = 0; i < aList.length; i++) {
                Association a = aList[i];
                AssociatedConcept[] acList = a.getAssociatedConcepts()
                        .getAssociatedConcept();

                for (int j = 0; j < acList.length; j++) {
                    AssociatedConcept ac = acList[j];
                    if (addToResults)
                        addCodeToDisplayedResults(ac);
                    Node child = graph.addNode(ac, getShowCodesInGraph());

                    // Resolve the core association name ...
                    StringBuffer edgeText = new StringBuffer();
                    if (StringUtils.isNotBlank(a.getDirectionalName())) {
                        edgeText.append(a.getDirectionalName());
                    } else {
                        if (!down)
                            edgeText.append("[R]");
                        edgeText.append(a.getAssociationName());
                    }

                    // Add qualifiers, if available. Format 'assoc:qual(val),
                    // ...'
                    NameAndValueList aqList = ac.getAssociationQualifiers();
                    if (aqList != null && aqList.getNameAndValueCount() > 0) {
                        edgeText.append(':');
                        for (int k = 0; k < aqList.getNameAndValueCount(); k++) {
                            NameAndValue nv = aqList.getNameAndValue(k);
                            if (k > 0)
                                edgeText.append(", ");
                            edgeText.append(nv.getName());
                            if (StringUtils.isNotBlank(nv.getContent()))
                                edgeText.append('(').append(nv.getContent())
                                        .append(')');
                        }
                    }

                    // Limit the text length to protect use of horizontal space
                    graph.addEdge(parentNode, child, StringUtils.abbreviate(
                            edgeText.toString(), 64));

                    if (down) {
                        graphAssociations(graph, child, ac.getSourceOf(), down,
                                addToResults);
                    } else {
                        graphAssociations(graph, child, ac.getTargetOf(), down,
                                addToResults);
                    }
                }
            }
        }
    }

    private class GraphResolver implements Runnable {
        private CodedNodeGraph cng_;
        private VDDisplayCodedNodeSet caller_;
        private SortOptionList sortOptions_;
        private boolean flat_;
        private boolean forward_, backward_;
        private int depth_, maxToReturn_;
        private ConceptReference focus_;

        public GraphResolver(CodedNodeGraph cng, VDDisplayCodedNodeSet caller,
                SortOptionList sortOptions, boolean flat, boolean forward,
                boolean backward, int depth, int maxToReturn,
                ConceptReference graphFocus) {
            cng_ = cng;
            caller_ = caller;
            sortOptions_ = sortOptions;
            flat_ = flat;
            forward_ = forward;
            backward_ = backward;
            depth_ = depth;
            focus_ = graphFocus;
            maxToReturn_ = maxToReturn;

        }

        public void run() {
            try {
                if (!flat_) {
                    graphRcr_ = cng_.resolveAsList(focus_, forward_, backward_,
                            Integer.MAX_VALUE, depth_, null, null,
                            sortOptions_, maxToReturn_)
                            .getResolvedConceptReference();
                    try {
                        CodedNodeSet cns = cng_.toNodeList(focus_, forward_,
                                backward_, depth_, maxToReturn_);
                        VDDisplayCodedNodeSet.this.codeIterator_ = cns.resolve(
                                sortOptions_, null, null, null, false);
                    } catch (Exception e) {
                        VDDisplayCodedNodeSet.this.codeIterator_ = null;
                    }
                } else {
                    CodedNodeSet cns = cng_.toNodeList(focus_, forward_,
                            backward_, depth_, maxToReturn_);
                    VDDisplayCodedNodeSet.this.codeIterator_ = cns.resolve(
                            sortOptions_, null, null, null, false);
                }
            } catch (LBException e) {
                caller_.resolveException_ = e;
            }

            caller_.resolveFinished(forward_);
        }
    }

    private class Resolver implements Runnable {
        private CodedNodeSet cns_;
        private VDDisplayCodedNodeSet caller_;
        private SortOptionList sortOptions_;

        public Resolver(CodedNodeSet cns, VDDisplayCodedNodeSet caller,
                SortOptionList sortOptions) {
            cns_ = cns;
            caller_ = caller;
            sortOptions_ = sortOptions;
        }

        public void run() {
            try {
                if (cns_ == null)
                {
                    caller_.resolveException_ = new LBException("There was a problem resolving Value Set Definition, please check the definition for any errors.");
                    caller_.resolveFinished(true);
                    return;
                }
                
                VDDisplayCodedNodeSet.this.codeIterator_ = cns_.resolve(
                        sortOptions_, null, null, null, true);

            } catch (LBException e) {
                caller_.resolveException_ = e;
            }

            caller_.resolveFinished(true);
        }
    }

    private class UpdateSelectionGraphResolver implements Runnable {

        private ResolvedConceptReference rcr_;
        public boolean stop = false;
        private final VDGraphView graphView;
        private final ConceptTreeView treeView;

        public UpdateSelectionGraphResolver(ResolvedConceptReference rcr,
                final VDGraphView graphView, final ConceptTreeView treeView) {
            this.graphView = graphView;
            this.treeView = treeView;
            rcr_ = rcr;
        }

        public void run() {
            try {
                ResolvedConceptReference[] rcr;
                try {
                    stop = false;

                    LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
                    CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
                    csvt.setVersion(rcr_.getCodingSchemeVersion());

                    CodingScheme cs = lbs.resolveCodingScheme(rcr_
                            .getCodingSchemeName(), csvt);
                    Relations[] relationContainers = cs.getRelations();

                    ResolvedConceptReference[][] allRefs = new ResolvedConceptReference[relationContainers.length][];
                    int totalSize = 0;

                    // Resolve relationships from each container ...
                    for (int i = 0; i < relationContainers.length && !stop; i++) {
                        CodedNodeGraph cng = lb_gui_
                                .getLbs()
                                .getNodeGraph(
                                        rcr_.getCodingSchemeName(),
                                        Constructors
                                                .createCodingSchemeVersionOrTagFromVersion(rcr_
                                                        .getCodingSchemeVersion()),
                                        relationContainers[i]
                                                .getContainerName());
                        ResolvedConceptReferenceList refs = cng.resolveAsList(
                                rcr_, true, true, 0, 1, null, null, null, -1);
                        allRefs[i] = refs.getResolvedConceptReference();
                        totalSize += refs.getResolvedConceptReferenceCount();
                    }

                    // Combine results into single array ...
                    rcr = new ResolvedConceptReference[totalSize];
                    int rcrPos = 0;
                    for (int i = 0; i < allRefs.length && !stop; i++) {
                        ResolvedConceptReference[] refs = allRefs[i];
                        for (int j = 0; j < refs.length; j++)
                            rcr[rcrPos++] = refs[j];
                    }
                } catch (LBParameterException e) {
                    e.printStackTrace();
                    Graph graph = new Graph(true);
                    graph.addNode(e.toString());
                    graphView.setGraph(graph);
                    // primaryGraph.setGraph(graph);
                    return;
                }

                if (!stop) {
                    Graph graph = new Graph(true);
                    treeView.clear();
                    shell_.getDisplay().asyncExec(new Runnable() {
                        public void run() {
                            treeView.getTree().setRedraw(false);
                        }
                    });

                    if (rcr.length == 0) {
                        graph.addNode(rcr_, getShowCodesInGraph());
                    } else {
                        for (int i = 0; i < rcr.length; i++) {
                            // there should only be one concept in this array.
                            // it is the top of the graph.
                            Node top = graph.addNode(rcr[i],
                                    getShowCodesInGraph());
                            String entity = null;
                            if (rcr[i].getEntityDescription() != null) {
                                entity = rcr[i].getEntityDescription()
                                        .getContent();
                                if (entity != null) {
                                    entity = entity.trim();
                                }
                            }
                            Object sourceKey = new Object();
                            Object targetKey = new Object();
                            treeView.addTreeItem(treeView.getTree(), sourceKey,
                                    entity + " as association source");
                            treeView.addTreeItem(treeView.getTree(), targetKey,
                                    entity + " as association target");
                            TreeItem source = treeView
                                    .getKeyedTreeItem(sourceKey);
                            TreeItem target = treeView
                                    .getKeyedTreeItem(targetKey);

                            treeView.addAssociations(source, rcr[i]
                                    .getSourceOf());
                            treeView.addAssociations(target, rcr[i]
                                    .getTargetOf());
                            treeView.expand(source);
                            treeView.expand(target);

                            graphPropertyLinks(graph, top, rcr[i]);
                            graphAssociations(graph, top, rcr[i].getSourceOf(),
                                    true, false);
                            graphAssociations(graph, top, rcr[i].getTargetOf(),
                                    false, false);
                        }
                    }
                    graphView.setGraph(graph);
                    // primaryGraph.setGraph(graph);
                    shell_.getDisplay().asyncExec(new Runnable() {
                        public void run() {
                            treeView.getTree().setRedraw(true);
                        }
                    });
                }

            } catch (LBException e) {
                log.error("problem updating graph", e);
            }

        }
    }

    private class ResolveMore implements Runnable {

        public void run() {
            try {
                if (codeIterator_ != null && codeIterator_.hasNext()) {
                    final ResolvedConceptReference[] codes = codeIterator_
                            .next(fetchSize).getResolvedConceptReference();
                    // gui update code
                    shell_.getDisplay().syncExec(new Runnable() {
                        public void run() {
                            // remove the - FETCHING_MORE item.
                            displayedCodeList_.remove(displayedCodeList_
                                    .getItemCount() - 1);
                        }
                    });

                    for (int i = 0; i < codes.length; i++) {
                        addCodeToDisplayedResults(codes[i]);
                    }
                    final int numRemaining = codeIterator_.numberRemaining();
                    final boolean hasNext = codeIterator_.hasNext();
                    if (!hasNext) {
                        codeIterator_.release();
                        codeIterator_ = null;
                    }

                    // gui update code
                    shell_.getDisplay().syncExec(new Runnable() {
                        public void run() {
                            if (hasNext) {
                                TableItem ti = new TableItem(
                                        displayedCodeList_, SWT.NONE);
                                ti.setText("Fetching More Results ("
                                        + numRemaining + " remaining)...");
                            }
                        }
                    });

                }
            } catch (LBException e) {
                dialog_.showError("Unexpected Error",
                        "There was an unexpected problem while getting more results - "
                                + e.toString());
            }
        }

    }

    /*
     * If absent, adds the given reference to the maintained results and list
     * control.
     */
    private void addCodeToDisplayedResults(final ResolvedConceptReference rcr) {
        addOrUpdateDisplayedResults(rcr, -1, -1, -1, SWT.NONE, -1, -1, true);
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
            final ResolvedConceptReference rcr, int addFontStyle,
            int addForeground, int addBackground, int updFontStyle,
            int updForeground, int updBackground, boolean replace) {
        int oldPos = -1;
        int newPos = -1;
        String qualifiedCode = new StringBuffer().append(
                rcr.getCodingSchemeName()).append(':').append(
                rcr.getConceptCode()).append(':').append(rcr.getCodeNamespace())
                .toString();

        // Determine the position to alter, if item is new or the style has
        // changed.
        // Item is new?
        if (!(displayedResultsCodes_.contains(qualifiedCode))) {
            displayedResultsCodes_.add(qualifiedCode);
            displayedResults_.add(rcr);
            newPos = displayedResults_.size() - 1;
        }

        // Item is not new, do we need to alter the existing item?
        else if (replace || updFontStyle >= 0 || updForeground >= 0
                || updBackground >= 0) {
            for (int i = 0; i < displayedResults_.size(); i++) {
                ResolvedConceptReference ref = displayedResults_.get(i);
                if (ref.getConceptCode().equals(rcr.getConceptCode())
                        && (ref.getCodingSchemeURI() == null || ref
                                .getCodingSchemeURI().equals(
                                        rcr.getCodingSchemeURI())
                                        && (ref.getCodeNamespace() == null || 
                                                ref.getCodeNamespace().equals(
                                                        rcr.getCodeNamespace())))
                        && (ref.getCodingSchemeVersion() == null || ref
                                .getCodingSchemeVersion().equals(
                                        rcr.getCodingSchemeVersion()))) {
                    oldPos = i;
                    break;
                }
            }
            if (oldPos >= 0) {
                if (replace) {
                    displayedResults_.remove(oldPos).getConceptCode();
                    displayedResults_.add(rcr);
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
                    ti.setText(new StringBuffer().append(rcr.getConceptCode())
                            .append(" - ").append(
                                    rcr.getEntityDescription() != null ? (rcr
                                            .getEntityDescription()
                                            .getContent() != null ? rcr
                                            .getEntityDescription()
                                            .getContent() : "") : "")
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

    protected void graphItemSelected(final ResolvedConceptReference rcr) {
        // deselect current item ...
        shell_.getDisplay().syncExec(new Runnable() {
            public void run() {
                displayedCodeList_.deselectAll();
            }
        });

        // ensure the new item is in the list ...
        addOrUpdateDisplayedResults(rcr, SWT.ITALIC, SWT.COLOR_BLUE, -1,
                SWT.NONE, SWT.COLOR_BLUE, -1, false);

        // attempt to find and select the matching item ...
        for (loop = 0; loop < displayedResults_.size(); loop++) {
            ResolvedConceptReference cur = displayedResults_.get(loop);

            if (cur.getCodingSchemeURI().equals(rcr.getCodingSchemeURI())
                    && cur.getConceptCode().equals(rcr.getConceptCode())) {
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
        displayConceptDetails(rcr);
        updateGraphForConceptSelection(rcr);
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
        if (codeIterator_ != null) {
            codeIterator_.release();
            codeIterator_ = null;
        }
    }
}