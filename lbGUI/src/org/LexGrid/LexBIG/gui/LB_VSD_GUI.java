
package org.LexGrid.LexBIG.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.prefs.Preferences;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ModuleDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Extensions.Export.Exporter;
import org.LexGrid.LexBIG.Extensions.Load.Loader;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexBIG.gui.codeSet.CodeSet;
import org.LexGrid.LexBIG.gui.config.Configure;
import org.LexGrid.LexBIG.gui.displayResults.PLDisplayResults;
import org.LexGrid.LexBIG.gui.displayResults.VDDisplayCodedNodeSet;
import org.LexGrid.LexBIG.gui.export.ExporterExtensionShell;
import org.LexGrid.LexBIG.gui.load.LoaderExtensionShell;
import org.LexGrid.LexBIG.gui.logging.LogViewer;
import org.LexGrid.LexBIG.gui.valueSetsView.PickListContentProvider;
import org.LexGrid.LexBIG.gui.valueSetsView.PickListLabelProvider;
import org.LexGrid.LexBIG.gui.valueSetsView.ValueSetDefCodingSchemeFilter;
import org.LexGrid.LexBIG.gui.valueSetsView.ValueSetDefConceptDomainFilter;
import org.LexGrid.LexBIG.gui.valueSetsView.ValueSetDefEntityCodeFilter;
import org.LexGrid.LexBIG.gui.valueSetsView.ValueSetDefIsEntityInValueSetFilter;
import org.LexGrid.LexBIG.gui.valueSetsView.ValueSetDefIsSubSet;
import org.LexGrid.LexBIG.gui.valueSetsView.ValueSetDefResolveCSFilter;
import org.LexGrid.LexBIG.gui.valueSetsView.ValueSetDefTermFilter;
import org.LexGrid.LexBIG.gui.valueSetsView.ValueSetDefValueSetDefNameFilter;
import org.LexGrid.LexBIG.gui.valueSetsView.ValueSetDefinitionContentProvider;
import org.LexGrid.LexBIG.gui.valueSetsView.ValueSetDefinitionLabelProvider;
import org.LexGrid.naming.Mappings;
import org.LexGrid.valueSets.PickListDefinition;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.lexevs.system.constants.SystemVariables;
import org.lexevs.system.service.SystemResourceService;
import org.lexevs.system.utility.PropertiesUtility;
import org.lexgrid.valuesets.LexEVSPickListDefinitionServices;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.dto.ResolvedPickListEntryList;
import org.lexgrid.valuesets.dto.ResolvedValueSetCodedNodeSet;
import org.lexgrid.valuesets.dto.ResolvedValueSetDefinition;
import org.lexgrid.valuesets.impl.LexEVSPickListDefinitionServicesImpl;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

/**
 * This is the GUI application for the Value Set and Pick List Definitions.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class LB_VSD_GUI{
    private static Logger log = Logger.getLogger("LB_VSGUI_LOGGER");
    protected Shell shell_;
    private DialogHandler errorHandler;
    private Composite valueSetDefComposite_, pickListComposite_;
    private TableViewer valueSetDefTV_, pickListTV_;
    private Properties currentProperties_;
    private List codeSetsList_;

    private MenuItem enableAdmin_, loadItem_, exportItem_, queryItem_, createItem_;
    private Button removeValueSetDef_, removePickList_;

    private ArrayList<CodeSet> codeSets;
    private LogViewer logViewer_;
    private LexBIGService lbs_;
    private LexEVSValueSetDefinitionServices vds_;
    private LexEVSPickListDefinitionServices pls_;

    private boolean isAdminEnabled = true;

    public static void main(String[] args) throws LBInvocationException {
        new LB_VSD_GUI(args);
    }

    /**
     * Instantiates a new GUI with the given command arguments and default
     * LexBIGService.
     * 
     * @param args
     *            Recognized arguments: -d, Disables admin options.
     * @throws LBInvocationException
     */
    public LB_VSD_GUI(String[] args) throws LBInvocationException {
        this(args, null);
    }

    /**
     * Instantiates a new GUI with the given command arguments and the given
     * LexBIGService.
     * 
     * @param args
     *            Recognized arguments: -d, Disables admin options.
     * @param service
     *            The LexBIGService to invoke for GUI functions.
     * @throws LBInvocationException
     */
    public LB_VSD_GUI(String[] args, LexBIGService service)
            throws LBInvocationException {
        isAdminEnabled = !(ArrayUtils.contains(args, "-d") || ArrayUtils
                .contains(args, "-D"));
        lbs_ = service != null ? service : LexBIGServiceImpl.defaultInstance();

        Display display = new Display();
        shell_ = new Shell(display);
        shell_.setText("LexBIG Value Sets " + Constants.version);
        init();

        setSizeFromPreviousRun();
        // shell_.setMaximized(true);
        shell_.open();
        try {
            while (!shell_.isDisposed()) {
                if (!display.readAndDispatch())
                    display.sleep();
            }
        } catch (RuntimeException e) {
            System.err.println(e);
            e.printStackTrace();
            errorHandler.showError("ERROR",
                    "Unhandled error, see log for details - exiting.");

        }
        display.dispose();
        System.exit(0);
    }

    private void setSizeFromPreviousRun() {
        Preferences p = Preferences.systemNodeForPackage(this.getClass());
        int height = p.getInt("console_height", -1);
        int width = p.getInt("console_width", -1);
        if (height < 100 || width < 100) {
            shell_.setMaximized(true);
            return;
        }

        shell_.setSize(width, height);

        int locX = p.getInt("console_loc_x", 0);
        int locY = p.getInt("console_loc_y", 0);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        if (locX < 0 || locX > d.width - 200) {
            locX = 0;
        }
        if (locY < 0 || locY > d.height - 150) {
            locY = 0;
        }
        shell_.setLocation(locX, locY);
    }

    private void init() throws LBInvocationException {
        try {
            logViewer_ = new LogViewer(shell_);
        } catch (Exception e1) {
            log.error("There was a problem starting the log viewer", e1);
            System.err.println(e1);
        }
        codeSets = new ArrayList<CodeSet>();
        
        shell_.addShellListener(new ShellAdapter() {
            public void shellClosed(ShellEvent e) {
                /*
                 * Save the size and location of the main window.
                 */
                int width = shell_.getSize().x;
                int height = shell_.getSize().y;
                int locX = shell_.getLocation().x;
                int locY = shell_.getLocation().y;

                Preferences p = Preferences.systemNodeForPackage(LB_VSD_GUI.this
                        .getClass());
                p.putInt("console_width", width);
                p.putInt("console_height", height);
                p.putInt("console_loc_x", locX);
                p.putInt("console_loc_y", locY);
            }
        });
        GridLayout layout = new GridLayout(1, true);
        shell_.setLayout(layout);

        shell_.setImage(new Image(shell_.getDisplay(), this.getClass()
                .getResourceAsStream("/icons/icon.gif")));

        errorHandler = new DialogHandler(shell_);

        SashForm topBottom = new SashForm(shell_, SWT.VERTICAL);
        topBottom.SASH_WIDTH = 5;
        topBottom.setLayout(new GridLayout());
        GridData gd = new GridData(GridData.FILL_BOTH);
        topBottom.setLayoutData(gd);
        topBottom.setVisible(true);

        buildValueSetDefComposite(topBottom);
       
        SashForm leftRightBottom = new SashForm(topBottom, SWT.HORIZONTAL);
        leftRightBottom.SASH_WIDTH = 5;
        gd = new GridData(GridData.FILL_BOTH);
        leftRightBottom.setLayoutData(gd);
        leftRightBottom.setVisible(true);
        buildPickListComposite(leftRightBottom);

        buildMenus();

        PropertiesUtility.systemVariable = "LG_CONFIG_FILE";
        String filePath = PropertiesUtility.locatePropFile(
                "config/" + SystemVariables.CONFIG_FILE_NAME, SystemResourceService.class.getName());
        if (filePath != null) {
            File file = new File(filePath);
            if (file.exists()) {
                refreshValueSetDefList();
                try {
                    PropertiesUtility.propertiesLocationKey = "CONFIG_FILE_LOCATION";
                    currentProperties_ = PropertiesUtility
                            .loadPropertiesFromFileOrURL(file.getAbsolutePath());
                } catch (IOException e) {
                    log.error("Unexpected Error", e);
                }
            }
        } else {
            new Configure(LB_VSD_GUI.this, currentProperties_);
        }

    }

    private void buildValueSetDefComposite(Composite holder) {
        valueSetDefComposite_ = new Composite(holder, SWT.BORDER);

        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 2;
        valueSetDefComposite_.setLayout(new GridLayout(2, false));
        valueSetDefComposite_.setLayoutData(gd);

        Utility.makeBoldLabel(valueSetDefComposite_, 2,
                GridData.HORIZONTAL_ALIGN_CENTER, "Available Value Set Definitions");

        gd = new GridData(GridData.FILL_BOTH);
        gd.verticalSpan = 12;
        valueSetDefTV_ = new TableViewer(valueSetDefComposite_, SWT.BORDER
                | SWT.SINGLE | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
        valueSetDefTV_.getTable().setLayoutData(gd);

        valueSetDefTV_.setContentProvider(new ValueSetDefinitionContentProvider(this));
        ValueSetDefinitionLabelProvider vdlp = new ValueSetDefinitionLabelProvider();
        valueSetDefTV_.setLabelProvider(vdlp);

        valueSetDefTV_.setUseHashlookup(true);
        valueSetDefTV_.getTable().setHeaderVisible(true);
        valueSetDefTV_.getTable().setLayoutData(gd);
        valueSetDefTV_.getTable().setLinesVisible(true);

        vdlp.setupColumns(valueSetDefTV_.getTable());
        valueSetDefTV_.setInput("");

        valueSetDefTV_
                .addSelectionChangedListener(new ISelectionChangedListener() {

                    public void selectionChanged(SelectionChangedEvent arg0) {
//                        updateButtonStates();
                    }

                });

        valueSetDefTV_.addDoubleClickListener(new IDoubleClickListener() {

            public void doubleClick(DoubleClickEvent arg0) {
                try {
                    displayValueSetDefinitionDetails();
                } catch (LBException e) {
                    errorHandler.showError("Resolve Problem", e.toString());
                }
            }

        });

        Button resolveVD = Utility.makeButton("Resolve",
                valueSetDefComposite_, GridData.VERTICAL_ALIGN_BEGINNING
                        | GridData.FILL_HORIZONTAL);
        resolveVD.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {

                URI uri = getSelectedValueSetDef();

                if (uri == null) {
                    errorHandler.showError("No Value Set Definition selected",
                            "You must select a Value Set Definition first.");
                    return;
                }
                new ValueSetDefResolveCSFilter(LB_VSD_GUI.this, uri.toString(), shell_, true, false);
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }

        });
        
        Button displayVDDef = Utility.makeButton("Display Details",
                valueSetDefComposite_, GridData.VERTICAL_ALIGN_BEGINNING
                        | GridData.FILL_HORIZONTAL);
        displayVDDef.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {

                URI uri = getSelectedValueSetDef();

                if (uri == null) {
                    errorHandler.showError("No Value Set Definition selected",
                            "You must select a Value Set Definition first.");
                    return;
                }
                
                try {
                    displayValueSetDefinitionDetails();
                } catch (LBException e) {
                    errorHandler.showError("Resolve Problem", e.toString());
                }
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }

        });

        Button refresh = Utility.makeButton("Refresh", valueSetDefComposite_,
                GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL);
        refresh.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
               LB_VSD_GUI.this.refreshValueSetDefList();
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }

        });
        
        removeValueSetDef_ = Utility.makeButton("Remove",
                valueSetDefComposite_, GridData.VERTICAL_ALIGN_BEGINNING
                        | GridData.FILL_HORIZONTAL);
        removeValueSetDef_.setEnabled(false);
        removeValueSetDef_.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                try {
                    URI uri = getSelectedValueSetDef();

                    if (uri == null) {
                        errorHandler.showError("No value set definition selected",
                                "You must select a value set definition first.");
                        return;
                    }

                    MessageBox messageBox = new MessageBox(shell_,
                            SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                    messageBox.setText("Remove value set definition?");
                    messageBox
                            .setMessage("Do you really want to remove the selected value set definition?");
                    if (messageBox.open() == SWT.YES) {
                        LB_VSD_GUI.this.getValueSetDefinitionService().removeValueSetDefinition(uri);
                        LB_VSD_GUI.this.refreshValueSetDefList();
                    }
                } catch (LBException e) {
                    errorHandler.showError("Error executing remove", e
                            .getMessage());
                } 
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                //
            }

        });

    }
    
    private void buildPickListComposite(Composite holder) {
        pickListComposite_ = new Composite(holder, SWT.BORDER);

        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 2;
        pickListComposite_.setLayout(new GridLayout(2, false));
        pickListComposite_.setLayoutData(gd);

        Utility.makeBoldLabel(pickListComposite_, 2,
                GridData.HORIZONTAL_ALIGN_CENTER, "Available Pick List Definitions");

        gd = new GridData(GridData.FILL_BOTH);
        gd.verticalSpan = 12;
        pickListTV_ = new TableViewer(pickListComposite_, SWT.BORDER
                | SWT.SINGLE | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
        pickListTV_.getTable().setLayoutData(gd);

        pickListTV_.setContentProvider(new PickListContentProvider(this));
        PickListLabelProvider pllp = new PickListLabelProvider();
        pickListTV_.setLabelProvider(pllp);

        pickListTV_.setUseHashlookup(true);
        pickListTV_.getTable().setHeaderVisible(true);
        pickListTV_.getTable().setLayoutData(gd);
        pickListTV_.getTable().setLinesVisible(true);

        pllp.setupColumns(pickListTV_.getTable());
        pickListTV_.setInput("");

        pickListTV_
                .addSelectionChangedListener(new ISelectionChangedListener() {

                    public void selectionChanged(SelectionChangedEvent arg0) {
//                        updateButtonStates();
                    }

                });

        pickListTV_.addDoubleClickListener(new IDoubleClickListener() {

            public void doubleClick(DoubleClickEvent arg0) {
                try {
                    displayPickListDefinitionDetails();
                } catch (LBException e) {
                    errorHandler.showError("Resolve Problem", e.toString());
                }
            }

        });

        Button resolvePL = Utility.makeButton("Resolve Pick List Definition",
                pickListComposite_, GridData.VERTICAL_ALIGN_BEGINNING
                        | GridData.FILL_HORIZONTAL);
        resolvePL.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {

                String pickListId = getSelectedPickList();

                if (pickListId == null) {
                    errorHandler.showError("No Pick List Definition selected",
                            "You must select a Pick List Definition first.");
                    return;
                }
                
                new ValueSetDefResolveCSFilter(LB_VSD_GUI.this, pickListId, shell_, false, true);
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }

        });
        
        Button displayPLDef = Utility.makeButton("Display Definition",
                pickListComposite_, GridData.VERTICAL_ALIGN_BEGINNING
                        | GridData.FILL_HORIZONTAL);
        displayPLDef.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {

                String pickListId = getSelectedPickList();

                if (pickListId == null) {
                    errorHandler.showError("No Pick List Definition selected",
                            "You must select a Pick List Definition first.");
                    return;
                }
                
                try {
                    displayPickListDefinitionDetails();
                } catch (LBException e) {
                    errorHandler.showError("Resolve Problem", e.toString());
                }
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }

        });

        Button refresh = Utility.makeButton("Refresh", pickListComposite_,
                GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL);
        refresh.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                LB_VSD_GUI.this.refreshPickListList();
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }

        });
        
        removePickList_ = Utility.makeButton("Remove",
                pickListComposite_, GridData.VERTICAL_ALIGN_BEGINNING
                        | GridData.FILL_HORIZONTAL);
        removePickList_.setEnabled(false);
        removePickList_.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                try {
                    String pickListId = getSelectedPickList();

                    if (pickListId == null) {
                        errorHandler.showError("No Pick List Definition selected",
                                "You must select a Pick List Definition first.");
                        return;
                    }

                    MessageBox messageBox = new MessageBox(shell_,
                            SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                    messageBox.setText("Remove Pick List Definition?");
                    messageBox
                            .setMessage("Do you really want to remove the selected Pick List Definition?");
                    if (messageBox.open() == SWT.YES) {
                        LB_VSD_GUI.this.getPickListDefinitionService().removePickList(pickListId);
                        LB_VSD_GUI.this.refreshPickListList();
                    }
                } catch (LBException e) {
                    errorHandler.showError("Error executing remove", e
                            .getMessage());
                } 
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                //
            }

        });

    }

    private URI getSelectedValueSetDef() {
        TableItem[] temp = valueSetDefTV_.getTable().getSelection();
        if (temp.length == 1) {
            try {
                return new URI(((ValueSetDefinition) temp[0].getData()).getValueSetDefinitionURI());
            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }
    
    private String getSelectedPickList() {
        TableItem[] temp = pickListTV_.getTable().getSelection();
        
        if (temp.length == 1) {
            return ((PickListDefinition) temp[0].getData()).getPickListId();
        }
        return null;
    }
    
    public void refreshValueSetDefListTemp() {
        shell_.getDisplay().syncExec(new Runnable() {
            public void run() {
                valueSetDefTV_.setContentProvider(new ValueSetDefinitionContentProvider(LB_VSD_GUI.this));
                valueSetDefTV_.setInput("");
            }
        });
    }
    
    public void refreshValueSetDefList() {
        shell_.getDisplay().syncExec(new Runnable() {
            public void run() {
                URI temp;
                temp = getSelectedValueSetDef();
                valueSetDefTV_.setInput("");
                // reselect previous selection
                if (temp != null) {
                    TableItem[] ti = valueSetDefTV_.getTable().getItems();
                    valueSetDefTV_.getTable().select(0);
                    for (int i = 0; i < ti.length; i++) {
                        if (((ValueSetDefinition) ti[i].getData())
                                .getValueSetDefinitionURI()
                                .equals(temp)) {
                            valueSetDefTV_.getTable().select(i);
//                                updateButtonStates();
                            break;
                        }
                    }
                }
            }
        });
    }
    
    public void refreshPickListList() {
        shell_.getDisplay().syncExec(new Runnable() {
            public void run() {
                String pickListId = getSelectedPickList();
                pickListTV_.setInput("");
                // reselect previous selection
                if (pickListId != null) {
                    TableItem[] ti = pickListTV_.getTable().getItems();
                    pickListTV_.getTable().select(0);
                    for (int i = 0; i < ti.length; i++) {
                        if (((PickListDefinition) ti[i].getData())
                                .getPickListId()
                                .equals(pickListId)) {
                            pickListTV_.getTable().select(i);
                            break;
                        }
                    }
                }
            }
        });
    }

    /*
     * build the menus
     */
    private MenuItem configureItem;

    private void buildMenus() {
        Menu mBar = new Menu(shell_, SWT.BAR);

        // build top menu bar
        MenuItem commandItem = new MenuItem(mBar, SWT.CASCADE);
        commandItem.setText("&Commands");

        if (isAdminEnabled) {
            loadItem_ = new MenuItem(mBar, SWT.CASCADE);
            loadItem_.setText("&Load");
            loadItem_.setEnabled(false);
            
            exportItem_ = new MenuItem(mBar, SWT.CASCADE);
            exportItem_.setText("&Export");
            exportItem_.setEnabled(false);
        }

        MenuItem helpItem = new MenuItem(mBar, SWT.CASCADE);
        helpItem.setText("&Help");

        // build file Menu
        Menu commandMenu = new Menu(shell_, SWT.DROP_DOWN);
        commandItem.setMenu(commandMenu);

        configureItem = new MenuItem(commandMenu, SWT.CASCADE);
        configureItem.setText("&Configure");
        configureItem.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                new Configure(LB_VSD_GUI.this, currentProperties_);
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                //
            }

        });
        if (isAdminEnabled) {// start enable admin conditional
            enableAdmin_ = new MenuItem(commandMenu, SWT.CASCADE | SWT.CHECK);
            enableAdmin_.setText("&Enable Admin Options");
            enableAdmin_.setSelection(false);
            enableAdmin_.addSelectionListener(new SelectionListener() {

                public void widgetSelected(SelectionEvent arg0) {
                    if (enableAdmin_.getSelection()) {
                        loadItem_.setEnabled(true);
                        exportItem_.setEnabled(true);
                        removeValueSetDef_.setEnabled(true);
                        removePickList_.setEnabled(true);
                    } else {
                        loadItem_.setEnabled(false);
                        exportItem_.setEnabled(false);
                        removeValueSetDef_.setEnabled(false);
                        removePickList_.setEnabled(false);
                    }

                }

                public void widgetDefaultSelected(SelectionEvent arg0) {
                    //
                }

            });

        }// end Admin enable conditional

        MenuItem viewLogItem = new MenuItem(commandMenu, SWT.CASCADE);
        viewLogItem.setText("&View Log File");
        viewLogItem.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event arg0) {
                logViewer_.setVisible(true);
            }
        });

        MenuItem exitItem = new MenuItem(commandMenu, SWT.CASCADE);
        exitItem.setText("&Exit");
        exitItem.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event arg0) {
                shell_.close();
                shell_.dispose();
            }
        });

        // build the Load Menu

        if (isAdminEnabled) { // begin Admin enable conditional

            Menu loadMenu = new Menu(shell_, SWT.DROP_DOWN);
            loadItem_.setMenu(loadMenu);

            MenuItem loadLexGrid = new MenuItem(loadMenu, SWT.NONE);
            loadLexGrid.setText("Load Value Set Definition - LexGrid XML");
            loadLexGrid.addSelectionListener(new SelectionListener() {

                public void widgetSelected(SelectionEvent arg0) {
                    Loader loader;
                    try {
                        loader = lbs_.getServiceManager(null).getLoader("LexGrid_Loader");
                    } catch (LBException e) {
                        throw new RuntimeException(e);
                    }
                    new LoaderExtensionShell(LB_VSD_GUI.this, loader, true, false);
                }

                public void widgetDefaultSelected(SelectionEvent arg0) {
                    // not used
                }

            });
            
            loadLexGrid = new MenuItem(loadMenu, SWT.NONE);
            loadLexGrid.setText("Load Pick List Definition - LexGrid XML");
            loadLexGrid.addSelectionListener(new SelectionListener() {

                public void widgetSelected(SelectionEvent arg0) {
                    Loader loader;
                    try {
                        loader = lbs_.getServiceManager(null).getLoader("LexGrid_Loader");
                    } catch (LBException e) {
                        throw new RuntimeException(e);
                    }
                    new LoaderExtensionShell(LB_VSD_GUI.this, loader, false, true);
                }

                public void widgetDefaultSelected(SelectionEvent arg0) {
                    // not used
                }

            });
            
         // build the Export Menu

            Menu exportMenu = new Menu(shell_, SWT.DROP_DOWN);
            exportItem_.setMenu(exportMenu);

            MenuItem exportVSDLexGrid = new MenuItem(exportMenu, SWT.NONE);
            exportVSDLexGrid.setText("Value Set Definition as LexGrid XML");
            exportVSDLexGrid.addSelectionListener(new SelectionListener() {
                public void widgetSelected(SelectionEvent arg0) {
                    URI uri = getSelectedValueSetDef();

                    if (uri == null) {
                        errorHandler.showError("No value set selected",
                                "You must select a value set first.");
                        return;
                    }
                    Exporter exporter;
                    try {
                        exporter = lbs_.getServiceManager(null).getExporter(org.LexGrid.LexBIG.Impl.exporters.LexGridExport.name);
                    } catch (LBException e) {
                        throw new RuntimeException(e);
                    }
                    new ExporterExtensionShell(LB_VSD_GUI.this, exporter, uri, false);                    
                }

                public void widgetDefaultSelected(SelectionEvent arg0) {
                    // not used
                }

            });
            
            MenuItem exportVSResolvedLexGrid = new MenuItem(exportMenu, SWT.NONE);
            exportVSResolvedLexGrid.setText("Value Set Resolution as LexGrid XML");
            exportVSResolvedLexGrid.addSelectionListener(new SelectionListener() {
                public void widgetSelected(SelectionEvent arg0) {
                    URI uri = getSelectedValueSetDef();

                    if (uri == null) {
                        errorHandler.showError("No value set selected",
                                "You must select a value set first.");
                        return;
                    }
                    Exporter exporter;
                    try {
                        exporter = lbs_.getServiceManager(null).getExporter(org.LexGrid.LexBIG.Impl.exporters.LexGridExport.name);
                    } catch (LBException e) {
                        throw new RuntimeException(e);
                    }
                    new ExporterExtensionShell(LB_VSD_GUI.this, exporter, uri, true);                    
                }

                public void widgetDefaultSelected(SelectionEvent arg0) {
                    // not used
                }

            });
            
            MenuItem exportPLDLexGrid = new MenuItem(exportMenu, SWT.NONE);
            exportPLDLexGrid.setText("Pick List Definition as LexGrid XML");
            exportPLDLexGrid.addSelectionListener(new SelectionListener() {
                public void widgetSelected(SelectionEvent arg0) {
                    String pickListId = getSelectedPickList();

                    if (pickListId == null) {
                        errorHandler.showError("No pick list selected",
                                "You must select a pick list first.");
                        return;
                    }
                    Exporter exporter;
                    try {
                        exporter = lbs_.getServiceManager(null).getExporter(org.LexGrid.LexBIG.Impl.exporters.LexGridExport.name);
                    } catch (LBException e) {
                        throw new RuntimeException(e);
                    }
                    new ExporterExtensionShell(LB_VSD_GUI.this, exporter, pickListId);                    
                }

                public void widgetDefaultSelected(SelectionEvent arg0) {
                    // not used
                }

            });
            
        }// end Admin enable conditional
        
        // Query menu
        Menu queryMenu = new Menu(shell_, SWT.DROP_DOWN);
        queryItem_ = new MenuItem(mBar, SWT.CASCADE);
        queryItem_.setMenu(queryMenu);
        queryItem_.setText("&Query");

        MenuItem filterByConceptDomain = new MenuItem(queryMenu, SWT.NONE);
        filterByConceptDomain.setText("Value Set Definition by Concept Domain");
        filterByConceptDomain.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                new ValueSetDefConceptDomainFilter(LB_VSD_GUI.this, shell_);
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // not used
            }

        });
        
        MenuItem filterByCodingScheme = new MenuItem(queryMenu, SWT.NONE);
        filterByCodingScheme.setText("Value Set Definition by Coding Scheme");
        filterByCodingScheme.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                new ValueSetDefCodingSchemeFilter(LB_VSD_GUI.this, shell_);
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // not used
            }

        });
        
        MenuItem filterByVSDName = new MenuItem(queryMenu, SWT.NONE);
        filterByVSDName.setText("Value Set Definition by value Set Definition Name");
        filterByVSDName.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                new ValueSetDefValueSetDefNameFilter(LB_VSD_GUI.this, shell_);
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // not used
            }

        });
        
        MenuItem filterByEntityCode = new MenuItem(queryMenu, SWT.NONE);
        filterByEntityCode.setText("Value Set Definition by Entity Code");
        filterByEntityCode.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {                
                new ValueSetDefEntityCodeFilter(LB_VSD_GUI.this, shell_);
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // not used
            }
        });
        
        MenuItem checkForSubset = new MenuItem(queryMenu, SWT.NONE);
        checkForSubset.setText("Value Set Definition is Sub Set");
        checkForSubset.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                URI vsdSelected = getSelectedValueSetDef();
                new ValueSetDefIsSubSet(LB_VSD_GUI.this, vsdSelected == null ? null : vsdSelected.toString(), shell_);
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // not used
            }
        });
        
        MenuItem checkEntityInVSD = new MenuItem(queryMenu, SWT.NONE);
        checkEntityInVSD.setText("Value Set Definition has Entity Code");
        checkEntityInVSD.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                String vsdURI = null;
                ValueSetDefinition selectedVSD = null;
                Mappings vsdMappings = null;
                URI vsdSelected = getSelectedValueSetDef();
                if (vsdSelected != null)
                {
                    vsdURI = vsdSelected.toString();
                    try {
                        selectedVSD = getValueSetDefinitionService().getValueSetDefinition(vsdSelected, null);
                        if (selectedVSD != null)
                            vsdMappings = selectedVSD.getMappings();
                    } catch (LBException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                new ValueSetDefIsEntityInValueSetFilter(LB_VSD_GUI.this, vsdURI, vsdMappings, shell_);
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // not used
            }
        });
        
        MenuItem getVSDEntitiesForTerm = new MenuItem(queryMenu, SWT.NONE);
        getVSDEntitiesForTerm.setText("Value Set Definition for Entity Term");
        getVSDEntitiesForTerm.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                String vsdURI = null;
                URI vsdSelected = getSelectedValueSetDef();
                if (vsdSelected != null)
                {
                    vsdURI = vsdSelected.toString();
                }
                new ValueSetDefTermFilter(LB_VSD_GUI.this, vsdURI, shell_);
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // not used
            }
        });
        
        // Create menu
        Menu createMenu = new Menu(shell_, SWT.DROP_DOWN);
        createItem_ = new MenuItem(mBar, SWT.CASCADE);
        createItem_.setMenu(createMenu);
        createItem_.setText("&Create");

        MenuItem createVSD = new MenuItem(createMenu, SWT.NONE);
        createVSD.setText("Value Set Definition");
        createVSD.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                new ValueSetDefinitionDetails(LB_VSD_GUI.this, shell_, null);
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // not used
            }

        });
        
        // build the Help Menu

        // Using the java Properties class to pull current info
        // from the build.properties file.
        final VersionProperties vProps = new VersionProperties();

        Menu helpMenu = new Menu(shell_, SWT.DROP_DOWN);
        helpItem.setMenu(helpMenu);

        MenuItem aboutItem = new MenuItem(helpMenu, SWT.NONE);
        aboutItem.setText("&About");
        aboutItem.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event arg0) {
                errorHandler
                        .showInfo(
                                "About",
                                "LexBIG GUI Console"
                                        + "\nBased on: "
                                        + vProps.getProduct()
                                        + "\nVersion: "
                                        + vProps.getVersion()
                                        + "\nBuild date: "
                                        + vProps.getDate()
                                        + "\n\nFor questions, please contact the caBIG Vocabulary Knowledge Center (https://cabig-kc.nci.nih.gov/Vocab/forums/).");
            }

        });

        shell_.setMenuBar(mBar);

    }
    
    public LexEVSValueSetDefinitionServices getValueSetDefinitionService(){
        if (vds_ == null) {
            vds_ = LexEVSValueSetDefinitionServicesImpl.defaultInstance();
        }
        return vds_;
    }
    
    public LexEVSPickListDefinitionServices getPickListDefinitionService(){
        if (pls_ == null) {
            pls_ = LexEVSPickListDefinitionServicesImpl.defaultInstance();
        }
        return pls_;
    }
    
    public LexBIGService getLbs() {
        return this.lbs_;
    }

    public void getNewLBS() throws LBInvocationException {
        this.lbs_ = LexBIGServiceImpl.defaultInstance();
    }

    public Shell getShell() {
        return this.shell_;
    }

    public CodeSet getSelectedCodeSet() {
        int index = codeSetsList_.getSelectionIndex();
        if (index >= 0) {
            return codeSets.get(index);
        }
        return null;
    }

    public String[] getSupportedMatchAlgorithms() {
        ModuleDescription[] ed = getLbs().getMatchAlgorithms()
                .getModuleDescription();
        String[] result = new String[ed.length];
        for (int i = 0; i < ed.length; i++) {
            result[i] = ed[i].getName();
        }
        return result;
    }

    /**
     * @param currentProperties
     *            the currentProperties to set
     */
    public void setCurrentProperties(Properties currentProperties) {
        this.currentProperties_ = currentProperties;
    }

    private void displayValueSetDefinitionDetails() throws LBException {
        new ValueSetDefinitionDetails(this, this.shell_, getValueSetDefinitionService().getValueSetDefinition(getSelectedValueSetDef(), null));
    }
    
    private void displayPickListDefinitionDetails() throws LBException {
        new ValueSetDefinitionDetails(this.shell_, getPickListDefinitionService().getPickListDefinitionById(getSelectedPickList()));
    }

    @Deprecated
    public void resolveValueSetDefOld(URI uri) throws LBException {
        ConvenienceMethods cm = new ConvenienceMethods();
        org.LexGrid.LexBIG.LexBIGService.CodedNodeSet finalCNS = null;   
        ResolvedValueSetDefinition rvdDef = getValueSetDefinitionService().resolveValueSetDefinition(uri, null, null, null, null);
        org.LexGrid.LexBIG.LexBIGService.CodedNodeSet cns = null;
        while (rvdDef.getResolvedConceptReferenceIterator().hasNext())
        {
            ResolvedConceptReference rcr = rvdDef.getResolvedConceptReferenceIterator().next();
            cns = cm.createCodedNodeSet(new String[] {rcr.getCode()}, rcr.getCodingSchemeURI(), Constructors.createCodingSchemeVersionOrTag(null, rcr.getCodingSchemeVersion()));
            if (finalCNS == null)
                finalCNS = cns;
            else
                finalCNS = finalCNS.union(cns);
        }

        new VDDisplayCodedNodeSet(this, finalCNS, null);
    }
    
    public void resolveValueSetDef(URI uri) throws LBException {
        if (uri == null)
            return;
        
        ValueSetDefinition vsd = getValueSetDefinitionService().getValueSetDefinition(uri, null);
        if (vsd == null)
            return;
        
        ResolvedValueSetCodedNodeSet rvscns = getValueSetDefinitionService().getCodedNodeSetForValueSetDefinition(uri, null, null, null);
        
        if (rvscns != null)        
            new VDDisplayCodedNodeSet(this, rvscns.getCodedNodeSet(), null);        
       
    }
    
    public void resolveValueSetDef(URI uri, AbsoluteCodingSchemeVersionReferenceList csrList, String revisionId) throws LBException {
        ResolvedValueSetCodedNodeSet resolvedVSDCNS = getValueSetDefinitionService().getCodedNodeSetForValueSetDefinition(uri, null, csrList, null);
        org.LexGrid.LexBIG.LexBIGService.CodedNodeSet finalCNS = null;
        if (resolvedVSDCNS != null)
            finalCNS = resolvedVSDCNS.getCodedNodeSet(); 

        new VDDisplayCodedNodeSet(this, finalCNS, null);
    }
    
    public void resolveValueSetDef(ValueSetDefinition vsd, AbsoluteCodingSchemeVersionReferenceList csrList, String revisionId) throws LBException {
        ConvenienceMethods cm = new ConvenienceMethods();
        org.LexGrid.LexBIG.LexBIGService.CodedNodeSet finalCNS = null; 
        try {
            ResolvedValueSetDefinition rvdDef = getValueSetDefinitionService().resolveValueSetDefinition(vsd, csrList, null, null);
            org.LexGrid.LexBIG.LexBIGService.CodedNodeSet cns = null;
            while (rvdDef.getResolvedConceptReferenceIterator().hasNext())
            {
                ResolvedConceptReference rcr = rvdDef.getResolvedConceptReferenceIterator().next();
                if (rcr.getCodingSchemeURI() != null)
                {
                    cns = cm.createCodedNodeSet(new String[] {rcr.getCode()}, rcr.getCodingSchemeURI(), Constructors.createCodingSchemeVersionOrTag(null, rcr.getCodingSchemeVersion()));
                    if (finalCNS == null)
                        finalCNS = cns;
                    else
                        finalCNS = finalCNS.union(cns);
                }
            }
        } catch (NullPointerException e){
            errorHandler.showError("Error", e.getMessage());
            return;
        }

        new VDDisplayCodedNodeSet(this, finalCNS, null);
    }
    
    public void resolvePickList(String pickListId, AbsoluteCodingSchemeVersionReferenceList csrList) throws LBException {
        ResolvedPickListEntryList pleList = getPickListDefinitionService().resolvePickList(pickListId, true, csrList, null);

        new PLDisplayResults(this, pleList, null);
    }    
}