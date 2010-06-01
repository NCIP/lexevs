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
package edu.mayo.informatics.lexgrid.convert.swt;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.varia.LevelRangeFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.exceptions.UnexpectedError;
import edu.mayo.informatics.lexgrid.convert.formats.CombinationOptions;
import edu.mayo.informatics.lexgrid.convert.formats.InputFormatSWTInterface;
import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.formats.OptionHolder;
import edu.mayo.informatics.lexgrid.convert.formats.OutputFormatSWTInterface;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.swt.LexGridDelimitedTextSWT;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.swt.LexGridLDAPSWT;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.swt.LexGridSQLLiteSWT;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.swt.LexGridSQLSWT;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.swt.LexGridXMLSWT;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.swt.NCIMetaThesaurusSQLSWT;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.swt.NCIOwlSWT;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.swt.NCIThesaurusHistoryFileSWT;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.swt.OBOSWT;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.swt.OwlSWT;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.swt.ProtegeFramesSWT;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.swt.RRFFilesSWT;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.swt.SemNetFilesSWT;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.swt.SnodentSQLSWT;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.swt.UMLSSQLSWT;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.DeleteLexGridTerminology;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.IndexLexGridDatabase;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.swt.ComputeTransitiveExpansionTableSWT;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.swt.DeleteLexGridTerminologySWT;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.swt.IndexLexGridDatabaseSWT;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.swt.LexGridLDAPSWTOut;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.swt.LexGridSQLLiteSWTOut;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.swt.LexGridSQLSWTOut;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.swt.LexGridXMLSWTOut;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.swt.OBOSWTOut;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.swt.RegisterLexGridTerminologySWT;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.swt.SQLSWTOut;
import edu.mayo.informatics.lexgrid.convert.utility.Constants;

/**
 * Main class for SWT Conversion GUI. This is the big Kahuna.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 7198 $ checked in on $Date: 2008-02-15
 *          18:08:36 +0000 (Fri, 15 Feb 2008) $
 */
public class Converter {

    private static Logger log = Logger.getLogger("convert.gui");

    protected Shell shell_;

    private InputFormatSWTInterface[] inputFormats;
    private Hashtable outputFormats;
    private Composite[] inputComposites;
    private StackLayout stepStack, inputDetailsStack, outputDetailsStack;
    private Composite inputComposite, inputTypesHolder, outputComposite, outputTypesHolder, convertComposite,
            optionsComposite, terminologyChoiceComposite;

    private InputFormatSWTInterface selectedInputFormat;
    private OutputFormatSWTInterface selectedOutputFormat;
    private OptionHolder currentOptions;

    Group conversionOptionsGrp, terminologyChoiceGrp;
    private DialogHandler errorHandler;

    private StyledText conversionOutput;
    private ConverterActionListener actionListener_;
    private String[] selectedTerminologies_;
    private LogViewer logViewer_;

    public Converter() {
        try {
            Appender temp = new FileAppender(new PatternLayout(
                    "%-5p [%t] [%c] (%F:%L): %m\t%d{dd MMM yyyy HH:mm:ss,SSS}\n"), "convert error log.log", false);
            LevelRangeFilter tempFilter = new LevelRangeFilter();
            tempFilter.setLevelMin(Level.WARN);
            temp.addFilter(tempFilter);
            Logger.getRootLogger().addAppender(temp);
            Logger.getRootLogger().addAppender(
                    new FileAppender(new PatternLayout("%-5p [%t] [%c] (%F:%L): %m\t%d{dd MMM yyyy HH:mm:ss,SSS}\n"),
                            "convert debug log.log", false));
        } catch (IOException e) {
            log.error("problem configuring file loggers", e);
        }

        Display display = new Display();
        shell_ = new Shell(display);
        shell_.setText("LexGrid Conversion Toolkit " + Constants.version);
        init();
        shell_.setImage(new Image(display, this.getClass().getResourceAsStream("icons/convert.gif")));
        try {
            logViewer_ = new LogViewer(shell_);
        } catch (Exception e) {
            errorHandler.showError("Initialize Error", "Problem initializing the log file viewer");
        }

        shell_.open();
        try {
            while (!shell_.isDisposed()) {
                if (!display.readAndDispatch())
                    display.sleep();
            }
        } catch (RuntimeException e) {
            log.error("Something really bad happened in the GUI - exiting.", e);
            errorHandler.showError("BAD ERROR", "Something really bad happened in the GUI - exiting.");
            e.printStackTrace();
        }
        display.dispose();
        System.exit(0);
    }

    Button previous, next;
    Composite mainArea;

    private void init() {
        actionListener_ = new ConverterActionListener(this);
        GridLayout layout = new GridLayout(2, false);
        shell_.setLayout(layout);

        errorHandler = new DialogHandler(shell_);

        buildMenus();

        // MainArea is a stacked layout which holds input, output, and result
        // screens.
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 2;
        mainArea = new Composite(shell_, SWT.BORDER);
        stepStack = new StackLayout();
        mainArea.setLayoutData(gd);
        mainArea.setLayout(stepStack);

        // put the input, output, and results onto the main area stack.

        buildInputSelection(mainArea);
        buildOutputSelection(mainArea);
        buildConvertComposite(mainArea);

        previous = new Button(shell_, SWT.NONE);
        previous.setText("<- Previous");
        gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        previous.setLayoutData(gd);
        previous.setEnabled(false);
        previous.addSelectionListener(actionListener_);

        next = new Button(shell_, SWT.NONE);
        next.setText("Next ->");
        gd = new GridData(GridData.HORIZONTAL_ALIGN_END);
        next.setLayoutData(gd);
        next.addSelectionListener(actionListener_);

        stepStack.topControl = inputComposite;
        mainArea.layout();

    }

    Combo inputFormat;

    /*
     * Build a composite that allows for input type selection - this also
     * contains a stack of the input paramter composites.
     */
    private void buildInputSelection(Composite holder) {
        inputComposite = new Composite(holder, SWT.NONE);
        inputComposite.setLayout(new GridLayout(3, false));

        inputFormats = new InputFormatSWTInterface[] { new LexGridSQLSWT(), new LexGridLDAPSWT(), new LexGridXMLSWT(),
                new LexGridDelimitedTextSWT(), new LexGridSQLLiteSWT(), new OwlSWT(), new NCIOwlSWT(), new OBOSWT(),
                new ProtegeFramesSWT(), new UMLSSQLSWT(), new NCIMetaThesaurusSQLSWT(), new RRFFilesSWT(),
                new NCIThesaurusHistoryFileSWT(), new SemNetFilesSWT(), new SnodentSQLSWT() };

        GridData gd = new GridData(GridData.BEGINNING);

        Label label = new Label(inputComposite, SWT.CENTER);
        label.setText("Choose the Input Format");
        label.setLayoutData(gd);

        // spacer column
        gd = new GridData(GridData.BEGINNING);
        gd.widthHint = 10;
        new Label(inputComposite, SWT.NONE).setLayoutData(gd);

        gd = new GridData(GridData.BEGINNING);
        inputFormat = new Combo(inputComposite, SWT.DROP_DOWN | SWT.READ_ONLY);
        inputFormat.setLayoutData(gd);

        String[] temp = new String[inputFormats.length];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = inputFormats[i].getDescription();
        }

        inputFormat.setItems(temp);
        inputFormat.setVisibleItemCount(15);
        inputFormat.addSelectionListener(actionListener_);

        inputTypesHolder = new Composite(inputComposite, SWT.NONE);
        gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 3;
        inputTypesHolder.setLayoutData(gd);

        inputDetailsStack = new StackLayout();
        inputTypesHolder.setLayout(inputDetailsStack);

        // +1 for a blank one on top.
        inputComposites = new Composite[inputFormats.length + 1];

        // put an empty one on top at first.
        inputComposites[0] = new Composite(inputTypesHolder, SWT.NONE);

        for (int i = 0; i < inputFormats.length; i++) {
            inputComposites[i + 1] = inputFormats[i].createComposite(inputTypesHolder, SWT.NONE, errorHandler);
        }
        inputDetailsStack.topControl = inputComposites[0];
        inputTypesHolder.layout();
    }

    Combo outputFormat;
    Composite blank;

    /*
     * Build a composite that allows for input type selection - this also
     * contains a stack of the input paramter composites.
     */
    private void buildOutputSelection(Composite holder) {
        outputComposite = new Composite(holder, SWT.NONE);
        outputComposite.setLayout(new GridLayout(3, false));

        outputFormats = new Hashtable();
        outputFormats.put(LexGridLDAPSWTOut.description, new LexGridLDAPSWTOut());
        outputFormats.put(LexGridSQLSWTOut.description, new LexGridSQLSWTOut());
        outputFormats.put(LexGridSQLLiteSWTOut.description, new LexGridSQLLiteSWTOut());
        outputFormats.put(LexGridXMLSWTOut.description, new LexGridXMLSWTOut());
        outputFormats.put(DeleteLexGridTerminologySWT.description, new DeleteLexGridTerminologySWT());
        outputFormats.put(RegisterLexGridTerminologySWT.description, new RegisterLexGridTerminologySWT());
        outputFormats.put(IndexLexGridDatabase.description, new IndexLexGridDatabaseSWT());
        outputFormats.put(SQLSWTOut.description, new SQLSWTOut());
        outputFormats.put(ComputeTransitiveExpansionTableSWT.description, new ComputeTransitiveExpansionTableSWT());
        outputFormats.put(OBOSWTOut.description, new OBOSWTOut());

        GridData gd = new GridData(GridData.BEGINNING);

        Label label = new Label(outputComposite, SWT.CENTER);
        label.setText("Choose the Output Format");
        label.setLayoutData(gd);

        // spacer column
        gd = new GridData(GridData.BEGINNING);
        gd.widthHint = 10;
        new Label(outputComposite, SWT.NONE).setLayoutData(gd);

        gd = new GridData(GridData.BEGINNING);
        outputFormat = new Combo(outputComposite, SWT.DROP_DOWN | SWT.READ_ONLY);
        outputFormat.setLayoutData(gd);
        outputFormat.setVisibleItemCount(10);

        outputFormat.addSelectionListener(actionListener_);

        outputTypesHolder = new Composite(outputComposite, SWT.NONE);
        gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 3;
        outputTypesHolder.setLayoutData(gd);

        outputDetailsStack = new StackLayout();
        outputTypesHolder.setLayout(outputDetailsStack);

        // put an empty one on top at first.
        blank = new Composite(outputTypesHolder, SWT.NONE);

        Enumeration e = outputFormats.keys();
        while (e.hasMoreElements()) {
            ((OutputFormatSWTInterface) outputFormats.get(e.nextElement())).createComposite(outputTypesHolder,
                    SWT.NONE, errorHandler);
        }

        outputDetailsStack.topControl = blank;
        outputTypesHolder.layout();
    }

    StyledText inputSummary, outputSummary;

    /*
     * Build a composite that for doing the conversion
     */
    private void buildConvertComposite(Composite holder) {
        convertComposite = new Composite(holder, SWT.NONE);
        convertComposite.setLayout(new GridLayout(2, false));

        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.heightHint = 100;

        Group inputSummaryGrp = new Group(convertComposite, SWT.NONE);
        inputSummaryGrp.setLayout(new FillLayout());
        inputSummaryGrp.setText("Input Summary");
        inputSummaryGrp.setLayoutData(gd);

        inputSummary = new StyledText(inputSummaryGrp, SWT.MULTI | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
        inputSummary.setText("boo");

        Group outputSummaryGrp = new Group(convertComposite, SWT.NONE);
        outputSummaryGrp.setLayout(new FillLayout());
        outputSummaryGrp.setText("Output Summary");
        outputSummaryGrp.setLayoutData(gd);

        outputSummary = new StyledText(outputSummaryGrp, SWT.MULTI | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL
                | SWT.BORDER);

        terminologyChoiceGrp = new Group(convertComposite, SWT.NONE);
        terminologyChoiceGrp.setLayout(new FillLayout());
        terminologyChoiceGrp.setText("Select Source Terminologies");
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.heightHint = 150;
        terminologyChoiceGrp.setLayoutData(gd);

        buildTerminologyChoiceComposite();

        conversionOptionsGrp = new Group(convertComposite, SWT.V_SCROLL | SWT.H_SCROLL);
        conversionOptionsGrp.setLayout(new FillLayout());
        conversionOptionsGrp.setText("Options");
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.heightHint = 150;
        conversionOptionsGrp.setLayoutData(gd);

        buildOptionsComposite();

        Group conversionOutputGrp = new Group(convertComposite, SWT.NONE);
        conversionOutputGrp.setLayout(new FillLayout());
        conversionOutputGrp.setText("Conversion Output");
        gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 2;
        conversionOutputGrp.setLayoutData(gd);

        conversionOutput = new StyledText(conversionOutputGrp, SWT.MULTI | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL
                | SWT.BORDER);
    }

    /*
     * create widgets for options
     */
    private void optionsHelper(Option[] options) {
        for (int i = 0; i < options.length; i++) {
            if (!currentOptions.contains(options[i].getOptionName())) {
                SWTUtility.makeLabel(options[i].getOptionName(), options[i].getOptionDescription(), optionsComposite,
                        SWT.NONE);

                // this call builds the input widget.
                new OptionWrapper(options[i], optionsComposite);
                currentOptions.add(options[i]);
            }
        }
    }

    MenuItem toolsItem;

    /*
     * build the menus
     */
    private void buildMenus() {
        Menu mBar = new Menu(shell_, SWT.BAR);

        // build top menu bar
        MenuItem fileItem = new MenuItem(mBar, SWT.CASCADE);
        fileItem.setText("&File");

        // The tools menu is built dynamically by the selected input type.
        toolsItem = new MenuItem(mBar, SWT.CASCADE);
        toolsItem.setText("&Tools");
        toolsItem.setEnabled(false);

        MenuItem helpItem = new MenuItem(mBar, SWT.CASCADE);
        helpItem.setText("&Help");

        // build file Menu
        Menu fileMenu = new Menu(shell_, SWT.DROP_DOWN);
        fileItem.setMenu(fileMenu);

        MenuItem viewLogItem = new MenuItem(fileMenu, SWT.CASCADE);
        viewLogItem.setText("&View Log File");
        viewLogItem.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event arg0) {
                logViewer_.setVisible(true);
            }
        });

        MenuItem exitItem = new MenuItem(fileMenu, SWT.CASCADE);
        exitItem.setText("&Exit");
        exitItem.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event arg0) {
                shell_.close();
                shell_.dispose();
            }
        });

        // build the Help Menu
        Menu helpMenu = new Menu(shell_, SWT.DROP_DOWN);
        helpItem.setMenu(helpMenu);

        MenuItem aboutItem = new MenuItem(helpMenu, SWT.NONE);
        aboutItem.setText("&About...");
        aboutItem.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event arg0) {
                errorHandler
                        .showInfo(
                                "About LexGrid Converter",
                                "LexGrid Converter "
                                        + Constants.version
                                        + "\nSee http://informatics.mayo.edu/index.php?page=convert\nContact informatics@mayo.edu for additional support.");

            }

        });

        shell_.setMenuBar(mBar);

    }

    /*
     * Do the steps necessary when showPrevious is clicked.
     */
    protected void showPrevious() {
        if (stepStack.topControl.equals(outputComposite)) {
            stepStack.topControl = inputComposite;
            previous.setEnabled(false);

            if (toolsItem.getMenu() != null && !toolsItem.getMenu().isDisposed()) {
                toolsItem.setEnabled(true);
            }
        } else if (stepStack.topControl.equals(convertComposite)) {
            stepStack.topControl = outputComposite;
            next.setText("Next ->");
            next.setEnabled(true);
            shell_.layout();
        }
        mainArea.layout();
    }

    private void preConversionGuiSetup() {
        shell_.getDisplay().syncExec(new Runnable() {
            public void run() {
                conversionOutput.setText("");
                next.setEnabled(false);
                previous.setEnabled(false);
                terminologyChoiceComposite.setEnabled(false);
                optionsComposite.setEnabled(false);
            }
        });
    }

    private void postConversionGuiSetup() {
        shell_.getDisplay().syncExec(new Runnable() {
            public void run() {
                next.setEnabled(true);
                previous.setEnabled(true);
                terminologyChoiceComposite.setEnabled(true);
                optionsComposite.setEnabled(true);

            }
        });
    }

    /*
     * Do the steps necessary when showNext is clicked.
     */
    protected void showNext() {
        if (stepStack.topControl.equals(convertComposite)) {
            try {
                selectedTerminologies_ = null;
                if (terminologies != null) {
                    selectedTerminologies_ = terminologies.getSelection();
                    if (selectedTerminologies_ == null || selectedTerminologies_.length == 0) {
                        errorHandler.showError("Select a terminology", "You must select a terminology to convert");
                        return;
                    } else if (selectedOutputFormat.getDescription().equals(DeleteLexGridTerminology.description)) {
                        MessageBox message = new MessageBox(shell_, SWT.ICON_WARNING | SWT.YES | SWT.NO);
                        message.setText("Really delete terminologies?");
                        message
                                .setMessage("You have choosen to DELETE the selected terminologies from the INPUT server."
                                        + "\nAre you sure?");
                        if (message.open() != SWT.YES) {
                            return;
                        }

                    }

                }

                Thread converter = new Thread(new DoConversion());
                converter.start();
            } catch (Exception e) {
                errorHandler.showError("Error", "An error occurred while running the conversion\n" + e.toString());
            }
        } else {
            if (stepStack.topControl.equals(inputComposite)) {
                if (selectedInputFormat == null) {
                    errorHandler.showError("Choose an import format", "You must choose an import format");
                    return;
                }

                try {
                    String warning = selectedInputFormat.testConnection();

                    if (warning != null && warning.length() > 0) {
                        errorHandler.showWarning("Connection Warning", warning);
                    }
                } catch (ConnectionFailure e) {
                    errorHandler.showError("Invalid Parameters", e.toString());
                    return;
                }

                stepStack.topControl = outputComposite;
                outputFormat.setItems(selectedInputFormat.getSupportedOutputFormats());
                selectedOutputFormat = null;
                outputComposite.layout();
                outputDetailsStack.topControl = blank;
                outputTypesHolder.layout();
                previous.setEnabled(true);
                toolsItem.setEnabled(false);
            } else if (stepStack.topControl.equals(outputComposite)) {
                if (selectedOutputFormat == null) {
                    errorHandler.showError("Choose an ouput format", "You must choose an output format");
                    return;
                }
                try {
                    String warning = selectedOutputFormat.testConnection();

                    if (warning != null && warning.length() > 0) {
                        errorHandler.showWarning("Connection Warning", warning);
                    }
                } catch (ConnectionFailure e) {
                    errorHandler.showError("Invalid Parameters", e.toString());
                    return;
                }
                next.setEnabled(true);
                stepStack.topControl = convertComposite;
                setupConvertCompositeForDisplay();
                next.setText(selectedOutputFormat.getTextForActionButton());
                shell_.layout();
            }
            mainArea.layout();
        }
    }

    /*
     * executed when an input format is selected.
     */
    protected void selectInputFormat() {
        inputDetailsStack.topControl = inputComposites[inputFormat.getSelectionIndex() + 1];
        inputTypesHolder.layout();
        selectedInputFormat = inputFormats[inputFormat.getSelectionIndex()];
        toolsItem.setEnabled(false);
        Menu temp = toolsItem.getMenu();
        if (temp != null) {
            temp.dispose();
        }
        temp = selectedInputFormat.createToolsMenu(shell_, errorHandler);
        if (temp != null) {
            toolsItem.setMenu(temp);
            toolsItem.setEnabled(true);
        }
    }

    /*
     * executed when an output format is selected
     */
    protected void selectOutputFormat() {
        outputDetailsStack.topControl = ((OutputFormatSWTInterface) outputFormats.get(outputFormat.getText()))
                .getComposite();
        outputTypesHolder.layout();
        selectedOutputFormat = (OutputFormatSWTInterface) outputFormats.get(outputFormat.getText());
    }

    /*
     * prepare the convert composite for display (Call before displaying it)
     */
    private void setupConvertCompositeForDisplay() {
        buildOptionsComposite();
        buildTerminologyChoiceComposite();
        inputSummary.setText(selectedInputFormat.getConnectionSummary());
        outputSummary.setText(selectedOutputFormat.getConnectionSummary());
        conversionOutput.setText("");
    }

    List terminologies;

    /*
     * build and populate the terminology choice area.
     */
    private void buildTerminologyChoiceComposite() {
        if (terminologyChoiceComposite != null) {
            terminologyChoiceComposite.dispose();
            terminologies = null;
        }
        terminologyChoiceComposite = new Composite(terminologyChoiceGrp, SWT.NONE);
        terminologyChoiceComposite.setLayout(new GridLayout(1, false));

        if (selectedInputFormat != null) {
            String[] availableTerminologies = new String[] {};
            try {
                availableTerminologies = selectedInputFormat.getAvailableTerminologies();
            } catch (UnexpectedError e) {
                log.error("Problem reading available terminologies", e);
                errorHandler.showError("Problem reading available terminologies",
                        "Could not read the available terminologies from the input server."
                                + "\nThe full error message can be viewed in the log file."
                                + "\n\nYou will have to go back and make changes to the input configuration.");
            } catch (ConnectionFailure e) {
                log.error("Problem reading available terminologies", e);
                errorHandler.showError("Invalid connection information?",
                        "Could not read the available terminologies from the input server."
                                + "\nThe full error message can be viewed in the log file."
                                + "\n\nYou will have to go back and make changes to the input configuration.");
            }

            if (availableTerminologies != null) {
                if (availableTerminologies.length > 0) {

                    terminologies = new List(terminologyChoiceComposite, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL
                            | SWT.V_SCROLL);
                    GridData gd = new GridData(GridData.FILL_BOTH);
                    terminologies.setLayoutData(gd);
                    terminologies.setItems(availableTerminologies);
                } else {
                    SWTUtility.makeLabel("Could not read the available terminologies from the input server."
                            + "\n\nYou will have to go back and make changes to the input configuration.",
                            terminologyChoiceComposite, GridData.FILL_BOTH);
                    next.setEnabled(false);
                }
            } else {
                SWTUtility.makeLabel("All terminologies in the source file will be converted.",
                        terminologyChoiceComposite, GridData.FILL_BOTH);
            }
        }

        terminologyChoiceGrp.layout();
    }

    /*
     * build and populate the options area
     */
    private void buildOptionsComposite() {
        // clear out the options arrayList.
        currentOptions = new OptionHolder();

        if (optionsComposite != null) {
            optionsComposite.dispose();
        }
        optionsComposite = new Composite(conversionOptionsGrp, SWT.NONE);
        optionsComposite.setLayout(new GridLayout(2, false));

        if (selectedInputFormat != null) {
            Option[] options = selectedInputFormat.getOptions();
            optionsHelper(options);
        }

        if (selectedOutputFormat != null) {
            Option[] options = selectedOutputFormat.getOptions();
            optionsHelper(options);
        }

        optionsHelper(CombinationOptions.getOptionsForCombination(selectedInputFormat, selectedOutputFormat));
        optionsHelper(CombinationOptions.getEMFSpecificOptions(selectedInputFormat, selectedOutputFormat,
                currentOptions));

        if (optionsComposite.getChildren().length == 0) {
            SWTUtility.makeLabel("No options are available for this conversion", optionsComposite, GridData.FILL_BOTH);
        }
        conversionOptionsGrp.layout();
    }

    public static void main(String[] args) {
        new Converter();
    }

    private class DoConversion implements Runnable {
        public void run() {

            try {
                preConversionGuiSetup();
                //TODO .. replace EMF with direct conversion
//                ConversionLauncher.startConversion(selectedInputFormat, selectedOutputFormat, selectedTerminologies_,
//                        currentOptions, md);
            } catch (Exception e) {
                errorHandler.showError("Error", "An error occurred while running the conversion\n" + e.toString());
            } catch (Error e) {
                errorHandler.showError("Error", "An error occurred while running the conversion\n" + e.toString());
            } finally {
                postConversionGuiSetup();
            }
        }
    }
}