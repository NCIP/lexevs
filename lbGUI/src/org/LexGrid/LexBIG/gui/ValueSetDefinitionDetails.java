
package org.LexGrid.LexBIG.gui;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Utility.VSDObjectToString;
import org.LexGrid.LexBIG.gui.displayResults.TextContent;
import org.LexGrid.LexBIG.gui.valueSetsView.CodingSchemeReferenceContentProvider;
import org.LexGrid.LexBIG.gui.valueSetsView.CodingSchemeReferenceLabelProvider;
import org.LexGrid.LexBIG.gui.valueSetsView.CodingSchemeReferenceView;
import org.LexGrid.LexBIG.gui.valueSetsView.EntityReferenceContentProvider;
import org.LexGrid.LexBIG.gui.valueSetsView.EntityReferenceLabelProvider;
import org.LexGrid.LexBIG.gui.valueSetsView.EntityReferenceView;
import org.LexGrid.LexBIG.gui.valueSetsView.PropertyContentProvider;
import org.LexGrid.LexBIG.gui.valueSetsView.PropertyLabelProvider;
import org.LexGrid.LexBIG.gui.valueSetsView.PropertyReferenceContentProvider;
import org.LexGrid.LexBIG.gui.valueSetsView.PropertyReferenceLabelProvider;
import org.LexGrid.LexBIG.gui.valueSetsView.PropertyReferenceView;
import org.LexGrid.LexBIG.gui.valueSetsView.PropertyView;
import org.LexGrid.LexBIG.gui.valueSetsView.SupportedAttributesContentProvider;
import org.LexGrid.LexBIG.gui.valueSetsView.SupportedAttributesLabelProvider;
import org.LexGrid.LexBIG.gui.valueSetsView.ValueSetDefIsEntityInValueSetFilter;
import org.LexGrid.LexBIG.gui.valueSetsView.ValueSetDefIsSubSet;
import org.LexGrid.LexBIG.gui.valueSetsView.ValueSetDefReferenceContentProvider;
import org.LexGrid.LexBIG.gui.valueSetsView.ValueSetDefReferenceLabelProvider;
import org.LexGrid.LexBIG.gui.valueSetsView.ValueSetDefReferenceView;
import org.LexGrid.LexBIG.gui.valueSetsView.ValueSetDefResolveCSFilter;
import org.LexGrid.LexBIG.gui.valueSetsView.ValueSetDefTermFilter;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.naming.Mappings;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.PickListDefinition;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
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
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

/**
 * Class for displaying value sets details.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class ValueSetDefinitionDetails {
    private Shell shell_;
    private StyledText results_;
    private ValueSetDefinition vd_ = null;
    private DialogHandler errorHandler;
    private Group vsdMetaDataGp_, vsdSourceAndContextGp_, vsdRevisionGp_, buttonsGp_;
    private Text  vsdURITxt_, vsdNameTxt_, conceptDomainTxt_, statusTxt_, ownerTxt_, 
        effDateTxt_, expDateTxt_, currRevisionTxt_, prevRevisionTxt_, revDateTxt_, changeAgentTxt_, sourceTxt_, contextTxt_;
    private StyledText changeInst;
    private Composite valueSetsComposite_, definitionEntryComposite_, propertyComposite_, suppAttribComposite_;
    
    private Button editButton_, removeButton_, resolveButton_, saveButton_, closeButton_, sourceAddButton_, sourceRemoveButton_,
        contextAddButton_, contextRemoveButton_, entityRefAddButton_, entityRefRemoveButton_, entityRefEditButton_,
        csRefAddButton_, csRefRemoveButton_, csRefEditButton_, vsdRefAddButton_, vsdRefRemoveButton_, vsdRefEditButton_,
        propRefAddButton_, propRefRemoveButton_, propRefEditButton_, 
        propertyDisplayButton_, propertyAddButton_, propertyEditButton_, propertyRemoveButton_;
    private Combo defaultCSCombo_, isActiveCombo_, changeTypeCombo_;
    private TableViewer csRefTV_, vsdRefTV_, entityRefTV_, propertyRefTV_, vsdPropertyTV_, vsdSuppAttribTV_;
    private MenuItem queryItem_;
    private boolean changesMade = false;
    private LB_VSD_GUI lb_vsd_gui_;
    private Color redColor_;
    private java.util.List<Source> sourceList = new ArrayList<Source>();
    private java.util.List<String> contextList = new ArrayList<String>();
    
    private DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    private boolean changesSaved = false;
    
    public ValueSetDefinitionDetails(LB_VSD_GUI lb_vsd_gui, Shell parent, ValueSetDefinition vd) {
        this.lb_vsd_gui_ = lb_vsd_gui;
        vd_ = vd;
        shell_ = new Shell(parent.getDisplay());
        
        Device device = Display.getCurrent ();
        redColor_ = new Color (device, 255, 0, 0);
        
        errorHandler = new DialogHandler(shell_);
        shell_.setText("Value Set Definition Details " + Constants.version);
           shell_.addShellListener(new ShellAdapter() {
            public void shellClosed(ShellEvent e) {
                /*
                 * Save the size and location of the main window.
                 */
                int width = shell_.getSize().x;
                int height = shell_.getSize().y;
                int locX = shell_.getLocation().x;
                int locY = shell_.getLocation().y;

                Preferences p = Preferences.systemNodeForPackage(this
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

        SashForm topBottom = new SashForm(shell_, SWT.VERTICAL);
        topBottom.SASH_WIDTH = 5;
        topBottom.setLayout(new GridLayout());
        GridData gd = new GridData(GridData.FILL_BOTH);
        topBottom.setLayoutData(gd);
        topBottom.setVisible(true);
        
        
        buildValueSetsComposite(topBottom);
        
        SashForm leftRightBottom = new SashForm(topBottom, SWT.HORIZONTAL);
        leftRightBottom.SASH_WIDTH = 5;
        buildDefinitionEntryConposite(leftRightBottom);
        
        buildMenus();
        
        if (vd != null)
        {
            disableTextFields();
            enableRefButtons();
            enablePropertyButtons();
        }
        else
        {
            resolveButton_.setEnabled(false);
            enableTextFields();
            disableRefButtons();
            disablePropertyButtons();
        }
        
        shell_.open();
    }
    
    private void buildDefinitionEntryConposite(Composite holder){
        
       CTabFolder folder = new CTabFolder(holder, SWT.BORDER);
       folder.setSimple(false);
       folder.setMaximizeVisible(true);
       folder.setMinimizeVisible(true);
       folder.setUnselectedCloseVisible(false);
       //Tab 1 for definition entry
       CTabItem tab1 = new CTabItem(folder, SWT.BORDER);
       tab1.setText("Definition Entries");
//       
       GridData gd = new GridData(GridData.FILL_BOTH);
       gd.horizontalSpan = 30;
       gd.verticalSpan = 12;
       definitionEntryComposite_ = new Composite(folder, SWT.BORDER);
       
       definitionEntryComposite_.setLayout(new GridLayout(1, false));
       definitionEntryComposite_.setLayoutData(gd);

       Utility.makeBoldLabel(definitionEntryComposite_, 2,
               GridData.HORIZONTAL_ALIGN_CENTER, "Available Definition Entries");
       tab1.setControl(definitionEntryComposite_);
       
       // group 1 for coding scheme reference
       setUpCodingSchemeRefGp();
       // group 2 for value set reference
       setUpValueSetDefRefGp();
       // group 3 for entity code reference
       setUpEntityRefGp();
       // group 4 for property match reference
       setUpPropertyRefGp();
       
       //Tab 2 for value set definition properties
       CTabItem tab2 = new CTabItem(folder, SWT.NONE);
       tab2.setText("Properties");
       
       gd = new GridData(GridData.FILL_BOTH);
       gd.horizontalSpan = 30;
       gd.verticalSpan = 12;
       propertyComposite_ = new Composite(folder, SWT.BORDER);
       
       propertyComposite_.setLayout(new GridLayout(1, false));
       propertyComposite_.setLayoutData(gd);

       Utility.makeBoldLabel(propertyComposite_, 2,
               GridData.HORIZONTAL_ALIGN_CENTER, "Available Properties");
       tab2.setControl(propertyComposite_);
       setUpVSDProeprtyGp();
       
       //Tab 3 for value set definition supported attributes
       CTabItem tab3 = new CTabItem(folder, SWT.NONE);
       tab3.setText("SupportedAttributes");
       
       gd = new GridData(GridData.FILL_BOTH);
       gd.horizontalSpan = 30;
       gd.verticalSpan = 12;
       suppAttribComposite_ = new Composite(folder, SWT.BORDER);
       
       suppAttribComposite_.setLayout(new GridLayout(1, false));
       suppAttribComposite_.setLayoutData(gd);

       Utility.makeBoldLabel(suppAttribComposite_, 2,
               GridData.HORIZONTAL_ALIGN_CENTER, "Available Supported Attributes");
       tab3.setControl(suppAttribComposite_);
       setUpVSDSuppAttribGp();
       
       folder.pack();
    }
    
    private void setUpVSDProeprtyGp(){
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.verticalSpan = 12;
        gd.horizontalSpan = 6;
        
        Group group = new Group(propertyComposite_, SWT.NONE);
        group.setText("Properties");
        group.setLayoutData(gd);

        GridLayout layout = new GridLayout(2, false);
        group.setLayout(layout);
        
        gd = new GridData(GridData.FILL_BOTH);
        gd.verticalSpan = 11;
        gd.horizontalSpan = 1;
        vsdPropertyTV_ = new TableViewer(group, SWT.BORDER
                | SWT.SINGLE | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
        
        vsdPropertyTV_.getTable().setLayoutData(gd);

        vsdPropertyTV_.setContentProvider(new PropertyContentProvider(lb_vsd_gui_, vd_));
        PropertyLabelProvider lp = new PropertyLabelProvider();
        vsdPropertyTV_.setLabelProvider(lp);

        vsdPropertyTV_.setUseHashlookup(true);
        vsdPropertyTV_.getTable().setHeaderVisible(true);
        vsdPropertyTV_.getTable().setLayoutData(gd);
        vsdPropertyTV_.getTable().setLinesVisible(true);

        lp.setupColumns(vsdPropertyTV_.getTable());
        vsdPropertyTV_.setInput("");
        vsdPropertyTV_
                .addSelectionChangedListener(new ISelectionChangedListener() {

                    public void selectionChanged(SelectionChangedEvent arg0) {
//                        updateButtonStates();
                    }

                });
 //
        
        gd = new GridData(GridData.END);
        gd.verticalSpan = 1;
        gd.horizontalSpan = 1;
        propertyDisplayButton_ = Utility.makeButton("Display details",
                group, GridData.END
                        | GridData.FILL_HORIZONTAL);
        propertyDisplayButton_.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                TableItem[] temp = vsdPropertyTV_.getTable().getSelection();
                if (temp.length < 1)
                {
                    errorHandler.showError("No Value Set Definition Property has been selected",
                    "You must select a Property to edit.");
                    return;
                }
                new PropertyView(lb_vsd_gui_, ValueSetDefinitionDetails.this, shell_, vd_, ((Property) temp[0].getData()));
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }

        });
        propertyDisplayButton_.setLayoutData(gd);
        
        gd = new GridData(GridData.END);
        gd.verticalSpan = 1;
        propertyAddButton_ = Utility.makeButton("Add",
                group, GridData.END
                        | GridData.FILL_HORIZONTAL);
        propertyAddButton_.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                new PropertyView(lb_vsd_gui_, ValueSetDefinitionDetails.this, shell_, vd_, null);
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }

        });
        propertyAddButton_.setLayoutData(gd);
        
        layout = new GridLayout(1, false);
        
        gd = new GridData(GridData.END);
        gd.verticalSpan = 1;
        gd.horizontalSpan = 1;
        propertyRemoveButton_ = Utility.makeButton("Remove",
                group, GridData.END
                        | GridData.FILL_HORIZONTAL);
        propertyRemoveButton_.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                TableItem[] temp = vsdPropertyTV_.getTable().getSelection();
                if (temp == null ) {
                    errorHandler.showError("No Property selected",
                            "You must select a property first.");
                    return;
                }

                MessageBox messageBox = new MessageBox(shell_,
                        SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                messageBox.setText("Remove Property");
                messageBox.setMessage("Do you really want to remove the selected Property?");
                if (messageBox.open() == SWT.YES) {
                    Property propertyToRemove = ((Property)temp[0].getData());
                    removeVSDProperty(propertyToRemove);
                    refreshVSDPropertyList();
                }
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }

        });
        propertyRemoveButton_.setLayoutData(gd);
        
        gd = new GridData(GridData.END);
        gd.verticalSpan = 1;
        gd.horizontalSpan = 1;
        propertyEditButton_ = Utility.makeButton("Edit",
                group, GridData.END
                        | GridData.FILL_HORIZONTAL);
        propertyEditButton_.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                TableItem[] temp = vsdPropertyTV_.getTable().getSelection();
                if (temp.length < 1)
                {
                    errorHandler.showError("No Value Set Definition Property has been selected",
                    "You must select a Property to edit.");
                    return;
                }
                new PropertyView(lb_vsd_gui_, ValueSetDefinitionDetails.this, shell_, vd_, ((Property) temp[0].getData()));
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }

        });
        propertyEditButton_.setLayoutData(gd);
    }
    
    private void setUpVSDSuppAttribGp(){
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.verticalSpan = 12;
        gd.horizontalSpan = 6;
        
        Group group = new Group(suppAttribComposite_, SWT.NONE);
        group.setText("Supported Attributes");
        group.setLayoutData(gd);

        GridLayout layout = new GridLayout(2, false);
        group.setLayout(layout);
        
        gd = new GridData(GridData.FILL_BOTH);
        gd.verticalSpan = 11;
        gd.horizontalSpan = 1;
        vsdSuppAttribTV_ = new TableViewer(group, SWT.BORDER
                | SWT.SINGLE | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
        
        vsdSuppAttribTV_.getTable().setLayoutData(gd);

        vsdSuppAttribTV_.setContentProvider(new SupportedAttributesContentProvider(lb_vsd_gui_, vd_));
        SupportedAttributesLabelProvider lp = new SupportedAttributesLabelProvider();
        vsdSuppAttribTV_.setLabelProvider(lp);

        vsdSuppAttribTV_.setUseHashlookup(true);
        vsdSuppAttribTV_.getTable().setHeaderVisible(true);
        vsdSuppAttribTV_.getTable().setLayoutData(gd);
        vsdSuppAttribTV_.getTable().setLinesVisible(true);

        lp.setupColumns(vsdSuppAttribTV_.getTable());
        vsdSuppAttribTV_.setInput("");
        vsdSuppAttribTV_
                .addSelectionChangedListener(new ISelectionChangedListener() {

                    public void selectionChanged(SelectionChangedEvent arg0) {
//                        updateButtonStates();
                    }

                });
 //
        
//        gd = new GridData(GridData.END);
//        gd.verticalSpan = 1;
//        gd.horizontalSpan = 1;
//        propertyDisplayButton_ = Utility.makeButton("Display details",
//                group, GridData.END
//                        | GridData.FILL_HORIZONTAL);
//        propertyDisplayButton_.addSelectionListener(new SelectionListener() {
//
//            public void widgetSelected(SelectionEvent arg0) {
//                TableItem[] temp = vsdPropertyTV_.getTable().getSelection();
//                if (temp.length < 1)
//                {
//                    errorHandler.showError("No Value Set Definition Property has been selected",
//                    "You must select a Property to edit.");
//                    return;
//                }
//                new PropertyView(lb_vsd_gui_, ValueSetDefinitionDetails.this, shell_, vd_, ((Property) temp[0].getData()));
//            }
//
//            public void widgetDefaultSelected(SelectionEvent arg0) {
//                // 
//            }
//
//        });
//        propertyDisplayButton_.setLayoutData(gd);
//        
//        gd = new GridData(GridData.END);
//        gd.verticalSpan = 1;
//        propertyAddButton_ = Utility.makeButton("Add",
//                group, GridData.END
//                        | GridData.FILL_HORIZONTAL);
//        propertyAddButton_.addSelectionListener(new SelectionListener() {
//
//            public void widgetSelected(SelectionEvent arg0) {
//                new PropertyView(lb_vsd_gui_, ValueSetDefinitionDetails.this, shell_, vd_, null);
//            }
//
//            public void widgetDefaultSelected(SelectionEvent arg0) {
//                // 
//            }
//
//        });
//        propertyAddButton_.setLayoutData(gd);
//        
//        layout = new GridLayout(1, false);
//        
//        gd = new GridData(GridData.END);
//        gd.verticalSpan = 1;
//        gd.horizontalSpan = 1;
//        propertyRemoveButton_ = Utility.makeButton("Remove",
//                group, GridData.END
//                        | GridData.FILL_HORIZONTAL);
//        propertyRemoveButton_.addSelectionListener(new SelectionListener() {
//
//            public void widgetSelected(SelectionEvent arg0) {
//                TableItem[] temp = vsdPropertyTV_.getTable().getSelection();
//                if (temp == null ) {
//                    errorHandler.showError("No Property selected",
//                            "You must select a property first.");
//                    return;
//                }
//
//                MessageBox messageBox = new MessageBox(shell_,
//                        SWT.ICON_QUESTION | SWT.YES | SWT.NO);
//                messageBox.setText("Remove Property");
//                messageBox.setMessage("Do you really want to remove the selected Property?");
//                if (messageBox.open() == SWT.YES) {
//                    Property propertyToRemove = ((Property)temp[0].getData());
//                    removeVSDProperty(propertyToRemove);
//                    refreshVSDPropertyList();
//                }
//            }
//
//            public void widgetDefaultSelected(SelectionEvent arg0) {
//                // 
//            }
//
//        });
//        propertyRemoveButton_.setLayoutData(gd);
//        
//        gd = new GridData(GridData.END);
//        gd.verticalSpan = 1;
//        gd.horizontalSpan = 1;
//        propertyEditButton_ = Utility.makeButton("Edit",
//                group, GridData.END
//                        | GridData.FILL_HORIZONTAL);
//        propertyEditButton_.addSelectionListener(new SelectionListener() {
//
//            public void widgetSelected(SelectionEvent arg0) {
//                TableItem[] temp = vsdPropertyTV_.getTable().getSelection();
//                if (temp.length < 1)
//                {
//                    errorHandler.showError("No Value Set Definition Property has been selected",
//                    "You must select a Property to edit.");
//                    return;
//                }
//                new PropertyView(lb_vsd_gui_, ValueSetDefinitionDetails.this, shell_, vd_, ((Property) temp[0].getData()));
//            }
//
//            public void widgetDefaultSelected(SelectionEvent arg0) {
//                // 
//            }
//
//        });
//        propertyEditButton_.setLayoutData(gd);
    }
    
    private void setUpEntityRefGp(){
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.verticalSpan = 12;
        gd.horizontalSpan = 6;
        
        Group group = new Group(definitionEntryComposite_, SWT.NONE);
        group.setText("Entity References");
        group.setLayoutData(gd);

        GridLayout layout = new GridLayout(2, false);
        group.setLayout(layout);
        
        gd = new GridData(GridData.FILL_BOTH);
        gd.verticalSpan = 11;
        gd.horizontalSpan = 1;
        entityRefTV_ = new TableViewer(group, SWT.BORDER
                | SWT.SINGLE | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
        
        entityRefTV_.getTable().setLayoutData(gd);

        entityRefTV_.setContentProvider(new EntityReferenceContentProvider(lb_vsd_gui_, vd_));
        EntityReferenceLabelProvider lp = new EntityReferenceLabelProvider();
        entityRefTV_.setLabelProvider(lp);

        entityRefTV_.setUseHashlookup(true);
        entityRefTV_.getTable().setHeaderVisible(true);
        entityRefTV_.getTable().setLayoutData(gd);
        entityRefTV_.getTable().setLinesVisible(true);

        lp.setupColumns(entityRefTV_.getTable());
        entityRefTV_.setInput("");
        entityRefTV_
                .addSelectionChangedListener(new ISelectionChangedListener() {

                    public void selectionChanged(SelectionChangedEvent arg0) {
//                        updateButtonStates();
                    }

                });
 //
        
        gd = new GridData(GridData.END);
        gd.verticalSpan = 1;
        entityRefAddButton_ = Utility.makeButton("Add",
                group, GridData.END
                        | GridData.FILL_HORIZONTAL);
        entityRefAddButton_.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                new EntityReferenceView(ValueSetDefinitionDetails.this, shell_, vd_, null);
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }

        });
        entityRefAddButton_.setLayoutData(gd);
        
        layout = new GridLayout(1, false);
        
        gd = new GridData(GridData.END);
        gd.verticalSpan = 1;
        gd.horizontalSpan = 1;
        entityRefRemoveButton_ = Utility.makeButton("Remove",
                group, GridData.END
                        | GridData.FILL_HORIZONTAL);
        entityRefRemoveButton_.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                TableItem[] temp = entityRefTV_.getTable().getSelection();
                if (temp == null ) {
                    errorHandler.showError("No definition entry selected",
                            "You must select a definition entry first.");
                    return;
                }

                MessageBox messageBox = new MessageBox(shell_,
                        SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                messageBox.setText("Remove definition entry?");
                messageBox.setMessage("Do you really want to remove the selected definition entry?");
                if (messageBox.open() == SWT.YES) {
                    DefinitionEntry defEntryToDelete = ((DefinitionEntry)temp[0].getData());
                    removeDefinitionEntry(defEntryToDelete);
                    refreshEntityRefList();
                }
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }

        });
        entityRefRemoveButton_.setLayoutData(gd);
        
        gd = new GridData(GridData.END);
        gd.verticalSpan = 1;
        gd.horizontalSpan = 1;
        entityRefEditButton_ = Utility.makeButton("Edit",
                group, GridData.END
                        | GridData.FILL_HORIZONTAL);
        entityRefEditButton_.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                TableItem[] temp = entityRefTV_.getTable().getSelection();
                if (temp.length < 1)
                {
                    errorHandler.showError("No Value Set Definition Entry has been selected",
                    "You must select a Definition Entry to edit.");
                    return;
                }
                new EntityReferenceView(ValueSetDefinitionDetails.this, shell_, vd_, ((DefinitionEntry) temp[0].getData()));
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }

        });
        entityRefEditButton_.setLayoutData(gd);
    }
    
    private void setUpCodingSchemeRefGp(){
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.verticalSpan = 12;
        gd.horizontalSpan = 6;
        
        Group group = new Group(definitionEntryComposite_, SWT.NONE);
        group.setText("Coding Scheme References");
        group.setLayoutData(gd);

        GridLayout layout = new GridLayout(2, false);
        group.setLayout(layout);
        
        gd = new GridData(GridData.FILL_BOTH);
        gd.verticalSpan = 11;
        gd.horizontalSpan = 1;
        csRefTV_ = new TableViewer(group, SWT.BORDER
                | SWT.SINGLE | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
        
        csRefTV_.getTable().setLayoutData(gd);

        csRefTV_.setContentProvider(new CodingSchemeReferenceContentProvider(lb_vsd_gui_, vd_));
        CodingSchemeReferenceLabelProvider lp = new CodingSchemeReferenceLabelProvider();
        csRefTV_.setLabelProvider(lp);

        csRefTV_.setUseHashlookup(true);
        csRefTV_.getTable().setHeaderVisible(true);
        csRefTV_.getTable().setLayoutData(gd);
        csRefTV_.getTable().setLinesVisible(true);

        lp.setupColumns(csRefTV_.getTable());
        csRefTV_.setInput("");
        csRefTV_
                .addSelectionChangedListener(new ISelectionChangedListener() {

                    public void selectionChanged(SelectionChangedEvent arg0) {
//                        updateButtonStates();
                    }

                });
 //
        
        gd = new GridData(GridData.END);
        gd.verticalSpan = 1;
        csRefAddButton_ = Utility.makeButton("Add",
                group, GridData.END
                        | GridData.FILL_HORIZONTAL);
        csRefAddButton_.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                new CodingSchemeReferenceView(lb_vsd_gui_, ValueSetDefinitionDetails.this, shell_, vd_, null);
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }

        });
        csRefAddButton_.setLayoutData(gd);
        
        layout = new GridLayout(1, false);
        
        gd = new GridData(GridData.END);
        gd.verticalSpan = 1;
        gd.horizontalSpan = 1;
        
        csRefRemoveButton_ = Utility.makeButton("Remove",
                group, GridData.END
                        | GridData.FILL_HORIZONTAL);
        csRefRemoveButton_.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                TableItem[] temp = csRefTV_.getTable().getSelection();
                if (temp == null ) {
                    errorHandler.showError("No definition entry selected",
                            "You must select a definition entry first.");
                    return;
                }

                MessageBox messageBox = new MessageBox(shell_,
                        SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                messageBox.setText("Remove definition entry?");
                messageBox.setMessage("Do you really want to remove the selected definition entry?");
                if (messageBox.open() == SWT.YES) {
                    DefinitionEntry defEntryToDelete = ((DefinitionEntry)temp[0].getData());
                    removeDefinitionEntry(defEntryToDelete);
                    refreshCodingSchemeRefList();
                }
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }

        });
        csRefRemoveButton_.setLayoutData(gd);
        
        gd = new GridData(GridData.END);
        gd.verticalSpan = 1;
        gd.horizontalSpan = 1;
        
        csRefEditButton_ = Utility.makeButton("Edit",
                group, GridData.END
                        | GridData.FILL_HORIZONTAL);
        csRefEditButton_.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                TableItem[] temp = csRefTV_.getTable().getSelection();
                if (temp.length < 1)
                {
                    errorHandler.showError("No Definition Entry (CodingSchemReference) has been selected",
                    "You must select a Definition Entry(CodingSchemReference) to edit.");
                    return;
                }
                new CodingSchemeReferenceView(lb_vsd_gui_, ValueSetDefinitionDetails.this, shell_, vd_, ((DefinitionEntry) temp[0].getData()));
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }

        });
        csRefEditButton_.setLayoutData(gd);
    }
    
    private void setUpValueSetDefRefGp(){
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.verticalSpan = 12;
        gd.horizontalSpan = 6;
        
        Group group = new Group(definitionEntryComposite_, SWT.NONE);
        group.setText("Value Set Definition References");
        group.setLayoutData(gd);

        GridLayout layout = new GridLayout(2, false);
        group.setLayout(layout);
        
        gd = new GridData(GridData.FILL_BOTH);
        gd.verticalSpan = 11;
        gd.horizontalSpan = 1;
        vsdRefTV_ = new TableViewer(group, SWT.BORDER
                | SWT.SINGLE | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
        
        vsdRefTV_.getTable().setLayoutData(gd);

        vsdRefTV_.setContentProvider(new ValueSetDefReferenceContentProvider(lb_vsd_gui_, vd_));
        ValueSetDefReferenceLabelProvider lp = new ValueSetDefReferenceLabelProvider();
        vsdRefTV_.setLabelProvider(lp);

        vsdRefTV_.setUseHashlookup(true);
        vsdRefTV_.getTable().setHeaderVisible(true);
        vsdRefTV_.getTable().setLayoutData(gd);
        vsdRefTV_.getTable().setLinesVisible(true);

        lp.setupColumns(vsdRefTV_.getTable());
        vsdRefTV_.setInput("");
        vsdRefTV_.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent arg0) {
                // nothing
            }
        });
        
        gd = new GridData(GridData.END);
        gd.verticalSpan = 1;

        vsdRefAddButton_ = Utility.makeButton("Add",
                group, GridData.END
                        | GridData.FILL_HORIZONTAL);
        vsdRefAddButton_.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                new ValueSetDefReferenceView(lb_vsd_gui_, ValueSetDefinitionDetails.this, shell_, vd_, null);
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }

        });
        vsdRefAddButton_.setLayoutData(gd);
        
        layout = new GridLayout(1, false);
        
        gd = new GridData(GridData.END);
        gd.verticalSpan = 1;
        gd.horizontalSpan = 1;
        
        vsdRefRemoveButton_ = Utility.makeButton("Remove",
                group, GridData.END
                        | GridData.FILL_HORIZONTAL);
        vsdRefRemoveButton_.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                TableItem[] temp = vsdRefTV_.getTable().getSelection();
                if (temp == null ) {
                    errorHandler.showError("No definition entry selected",
                            "You must select a definition entry first.");
                    return;
                }

                MessageBox messageBox = new MessageBox(shell_,
                        SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                messageBox.setText("Remove definition entry?");
                messageBox.setMessage("Do you really want to remove the selected definition entry?");
                if (messageBox.open() == SWT.YES) {
                    DefinitionEntry defEntryToDelete = ((DefinitionEntry)temp[0].getData());
                    removeDefinitionEntry(defEntryToDelete);
                    refreshValueSetDefRefList();
                }
                
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }

        });
        vsdRefRemoveButton_.setLayoutData(gd);
        
        gd = new GridData(GridData.END);
        gd.verticalSpan = 1;
        gd.horizontalSpan = 1;
        
        vsdRefEditButton_ = Utility.makeButton("Edit",
                group, GridData.END
                        | GridData.FILL_HORIZONTAL);
        vsdRefEditButton_.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                TableItem[] temp = vsdRefTV_.getTable().getSelection();
                if (temp.length < 1)
                {
                    errorHandler.showError("No Definition Entry (ValueSetDefinition Reference) has been selected",
                    "You must select a Definition Entry(CodingSchemReference) to edit.");
                    return;
                }
                new ValueSetDefReferenceView(lb_vsd_gui_, ValueSetDefinitionDetails.this, shell_, vd_, ((DefinitionEntry) temp[0].getData()));
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }

        });
        vsdRefEditButton_.setLayoutData(gd);
    }
    
    private void setUpPropertyRefGp(){
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.verticalSpan = 12;
        gd.horizontalSpan = 6;
        
        Group group = new Group(definitionEntryComposite_, SWT.NONE);
        group.setText("Property References");
        group.setLayoutData(gd);

        GridLayout layout = new GridLayout(2, false);
        group.setLayout(layout);
        
        gd = new GridData(GridData.FILL_BOTH);
        gd.verticalSpan = 11;
        gd.horizontalSpan = 1;
        propertyRefTV_ = new TableViewer(group, SWT.BORDER
                | SWT.SINGLE | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
        
        propertyRefTV_.getTable().setLayoutData(gd);

        propertyRefTV_.setContentProvider(new PropertyReferenceContentProvider(lb_vsd_gui_, vd_));
        PropertyReferenceLabelProvider lp = new PropertyReferenceLabelProvider();
        propertyRefTV_.setLabelProvider(lp);

        propertyRefTV_.setUseHashlookup(true);
        propertyRefTV_.getTable().setHeaderVisible(true);
        propertyRefTV_.getTable().setLayoutData(gd);
        propertyRefTV_.getTable().setLinesVisible(true);

        lp.setupColumns(propertyRefTV_.getTable());
        propertyRefTV_.setInput("");
        propertyRefTV_
                .addSelectionChangedListener(new ISelectionChangedListener() {

                    public void selectionChanged(SelectionChangedEvent arg0) {
//                        updateButtonStates();
                    }

                });
 //
        
        gd = new GridData(GridData.END);
        gd.verticalSpan = 1;

        propRefAddButton_ = Utility.makeButton("Add",
                group, GridData.END
                        | GridData.FILL_HORIZONTAL);
        propRefAddButton_.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                new PropertyReferenceView(lb_vsd_gui_, ValueSetDefinitionDetails.this, shell_, vd_, null);
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }

        });
        propRefAddButton_.setLayoutData(gd);
        
        layout = new GridLayout(1, false);
        
        gd = new GridData(GridData.END);
        gd.verticalSpan = 1;
        gd.horizontalSpan = 1;
        
        propRefRemoveButton_ = Utility.makeButton("Remove",
                group, GridData.END
                        | GridData.FILL_HORIZONTAL);
        propRefRemoveButton_.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                TableItem[] temp = propertyRefTV_.getTable().getSelection();
                if (temp == null ) {
                    errorHandler.showError("No definition entry selected",
                            "You must select a definition entry first.");
                    return;
                }

                MessageBox messageBox = new MessageBox(shell_,
                        SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                messageBox.setText("Remove definition entry?");
                messageBox.setMessage("Do you really want to remove the selected definition entry?");
                if (messageBox.open() == SWT.YES) {
                    DefinitionEntry defEntryToDelete = ((DefinitionEntry)temp[0].getData());
                    removeDefinitionEntry(defEntryToDelete);
                    refreshPropertyRefList();
                }
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }

        });
        propRefRemoveButton_.setLayoutData(gd);
        
        gd = new GridData(GridData.END);
        gd.verticalSpan = 1;
        gd.horizontalSpan = 1;
        
        propRefEditButton_ = Utility.makeButton("Edit",
                group, GridData.END
                        | GridData.FILL_HORIZONTAL);
        propRefEditButton_.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                TableItem[] temp = propertyRefTV_.getTable().getSelection();
                if (temp.length < 1)
                {
                    errorHandler.showError("No Definition Entry (Property Reference) has been selected",
                    "You must select a Definition Entry(Property Reference) to edit.");
                    return;
                }
                new PropertyReferenceView(lb_vsd_gui_, ValueSetDefinitionDetails.this, shell_, vd_, ((DefinitionEntry) temp[0].getData()));
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }

        });
        propRefEditButton_.setLayoutData(gd);
    }
    
    private void setUpVSDMetaDataGp(){
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.verticalSpan = 2;       
        gd.horizontalSpan = 3;
        
        vsdMetaDataGp_ = new Group(valueSetsComposite_, SWT.FILL);
        vsdMetaDataGp_.setText("Value Set Definition meta data");
        vsdMetaDataGp_.setLayoutData(gd);

        GridLayout layout = new GridLayout(3, true);
        vsdMetaDataGp_.setLayout(layout);
        
        // Value Set Definition URI
        Label label = new Label(vsdMetaDataGp_, SWT.BEGINNING);
        label.setText("Value Set Definition URI : ");
        gd = new GridData();
        gd.horizontalSpan = 1;
        label.setLayoutData(gd);
        
        vsdURITxt_ = new Text(vsdMetaDataGp_, SWT.BORDER | SWT.BEGINNING);
        if (vd_ != null)
            vsdURITxt_.setText(vd_.getValueSetDefinitionURI());
        gd = new GridData();
        gd.horizontalAlignment = GridData.FILL;
        gd.verticalAlignment = GridData.FILL;
        gd.horizontalSpan = 2;
        vsdURITxt_.setLayoutData(gd);
        Font font = new Font(shell_.getDisplay(),"Arial",8,SWT.BOLD | SWT.ITALIC);
        vsdURITxt_.setFont(font);
        vsdURITxt_.addListener(SWT.Verify, new Listener() {
            
            public void handleEvent(Event arg0) {
                if (StringUtils.isNotEmpty(vsdURITxt_.getText()))
                {
                    if (vd_ != null)
                        setChangesMade(true);
                }
            }
        });
        
        // Value Set Definition Name
        label = new Label(vsdMetaDataGp_, SWT.BEGINNING);
        label.setText("Value Set Definition Name : ");
        gd = new GridData();
        gd.verticalSpan = 1;
        label.setLayoutData(gd);
        
        vsdNameTxt_ = new Text(vsdMetaDataGp_, SWT.BORDER | SWT.BEGINNING);
        if (vd_ != null)
            vsdNameTxt_.setText(vd_.getValueSetDefinitionName());
        gd = new GridData();
        gd.horizontalAlignment = GridData.FILL;
        gd.horizontalSpan = 2;
        vsdNameTxt_.setLayoutData(gd);
        vsdNameTxt_.addListener(SWT.Verify, new Listener() {
            
            public void handleEvent(Event arg0) {
                if (StringUtils.isNotEmpty(vsdNameTxt_.getText()))
                {
                    setChangesMade(true);
                    if (vd_ != null && vd_.getValueSetDefinitionName().equalsIgnoreCase(vsdNameTxt_.getText()))
                        setChangesMade(false);
                }
            }
        });
        
        // Default Coding Scheme
        label = new Label(vsdMetaDataGp_, SWT.BEGINNING);
        label.setText("Default Coding Scheme : ");
        gd = new GridData();
        label.setLayoutData(gd);

        defaultCSCombo_ = new Combo(vsdMetaDataGp_, SWT.BORDER | SWT.FILL);
        if (vd_ != null)
            defaultCSCombo_.setText(vd_.getDefaultCodingScheme() == null ? "" : vd_.getDefaultCodingScheme());
        try {
            if (lb_vsd_gui_.getLbs() != null && lb_vsd_gui_.getLbs().getSupportedCodingSchemes() != null)
            {
                CodingSchemeRenderingList csrList = lb_vsd_gui_.getLbs().getSupportedCodingSchemes();
                
                if (csrList != null)
                {
                    for (int i = 0; i < csrList.getCodingSchemeRenderingCount(); i++)
                    {
                        CodingSchemeRendering csr = csrList.getCodingSchemeRendering(i);
                        if (StringUtils.isNotBlank(csr.getCodingSchemeSummary().getLocalName()))
                            defaultCSCombo_.add(csr.getCodingSchemeSummary().getLocalName());
                    }
                }
            }
        } catch (LBInvocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        defaultCSCombo_.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        gd = new GridData();
        gd.horizontalAlignment = GridData.FILL;  
        gd.horizontalSpan = 2;
        defaultCSCombo_.setLayoutData(gd);
        defaultCSCombo_.addListener(SWT.Verify, new Listener() {
            
            public void handleEvent(Event arg0) {
                if (StringUtils.isNotEmpty(defaultCSCombo_.getText()))
                {
                    setChangesMade(true);
                    if (vd_ != null && vd_.getDefaultCodingScheme() != null && vd_.getDefaultCodingScheme().equalsIgnoreCase(defaultCSCombo_.getText()))
                        setChangesMade(false);
                }
            }
        });
        
        // Concept Domain
        label = new Label(vsdMetaDataGp_, SWT.BEGINNING);
        label.setText("Concept Domain : ");

        conceptDomainTxt_ = new Text(vsdMetaDataGp_, SWT.BORDER | SWT.FILL);
        if (vd_ != null)
            conceptDomainTxt_.setText(vd_.getConceptDomain() == null ? "" : vd_.getConceptDomain());
        gd = new GridData();
        gd.horizontalAlignment = GridData.FILL; 
        gd.horizontalSpan = 2;
        conceptDomainTxt_.setLayoutData(gd);     
        conceptDomainTxt_.addListener(SWT.Verify, new Listener() {
            
            public void handleEvent(Event arg0) {
                String cdName = conceptDomainTxt_.getText();
                
                if (StringUtils.isNotEmpty(cdName))
                {
                    setChangesMade(true);
                    if (vd_ != null && vd_.getConceptDomain() != null && vd_.getConceptDomain().equalsIgnoreCase(cdName))
                        setChangesMade(false);
                }
            }
        });
        
        // isActive
        label = new Label(vsdMetaDataGp_, SWT.BEGINNING);
        label.setText("Is Active : ");
        
        isActiveCombo_ = new Combo(vsdMetaDataGp_, SWT.BORDER | SWT.FILL);
        isActiveCombo_.setItems(new String[] { Boolean.TRUE.toString() , Boolean.FALSE.toString() });
        isActiveCombo_.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        if (vd_ != null)
            isActiveCombo_.setText(String.valueOf(vd_.isIsActive()));
        gd = new GridData();
        gd.horizontalAlignment = GridData.FILL;  
        gd.horizontalSpan = 2;
        isActiveCombo_.setLayoutData(gd);
        isActiveCombo_.addListener(SWT.Verify, new Listener() {
            
            public void handleEvent(Event arg0) {
                Boolean isActive = Boolean.valueOf(isActiveCombo_.getText());
                
                setChangesMade(true);
                if (vd_ != null && (vd_.getIsActive() == isActive))
                    setChangesMade(false);
            }
        });
        
        // Status
        label = new Label(vsdMetaDataGp_, SWT.BEGINNING);
        label.setText("Status : ");

        statusTxt_ = new Text(vsdMetaDataGp_, SWT.BORDER | SWT.FILL);
        if (vd_ != null)
            statusTxt_.setText(vd_.getStatus() == null ? "" : vd_.getStatus());
        gd = new GridData();
        gd.horizontalAlignment = GridData.FILL;  
        gd.horizontalSpan = 2;
        statusTxt_.setLayoutData(gd);  
        statusTxt_.addListener(SWT.Verify, new Listener() {
            
            public void handleEvent(Event arg0) {
                String status = statusTxt_.getText();
                
                if (StringUtils.isNotEmpty(status))
                {
                    setChangesMade(true);
                    if (vd_ != null && vd_.getStatus() != null && vd_.getStatus().equalsIgnoreCase(status))
                        setChangesMade(false);
                }
            }
        });
        
        // owner
        label = new Label(vsdMetaDataGp_, SWT.BEGINNING);
        label.setText("Owner : ");

        ownerTxt_ = new Text(vsdMetaDataGp_, SWT.BORDER | SWT.BEGINNING);
        if (vd_ != null)
            ownerTxt_.setText(vd_.getOwner() == null ? "" : vd_.getOwner());
        gd = new GridData();
        gd.horizontalAlignment = GridData.FILL;
        gd.horizontalSpan = 2;
        ownerTxt_.setLayoutData(gd); 
        ownerTxt_.addListener(SWT.Verify, new Listener() {
            
            public void handleEvent(Event arg0) {
                String owner = ownerTxt_.getText();
                if (StringUtils.isNotEmpty(owner))
                {
                    setChangesMade(true);
                    if (vd_ != null && vd_.getOwner() != null && vd_.getOwner().equalsIgnoreCase(owner))
                        setChangesMade(false);
                }
            }
        });
        
        // Effective Date
        label = new Label(vsdMetaDataGp_, SWT.BEGINNING);
        label.setText("Effective Date (MM/dd/YYYY): ");

        effDateTxt_ = new Text(vsdMetaDataGp_, SWT.BORDER | SWT.BEGINNING);
        effDateTxt_.setText("");
        if (vd_ != null)
        {
            if (vd_.getEffectiveDate() != null)
                effDateTxt_.setText(sdf.format(vd_.getEffectiveDate()));
            else
                effDateTxt_.setText("");
        }
        effDateTxt_.addListener(SWT.Verify, new Listener() {
            
            public void handleEvent(Event arg0) {
                String effDate = effDateTxt_.getText();
                if (StringUtils.isNotEmpty(effDate))
                {
                    setChangesMade(true);
                    //TODO compare dates
//                    if (vd_ != null && vd_.getEffectiveDate() != null && vd_.getEffectiveDate()tOwner().equalsIgnoreCase(owner))
//                        setChangesMade(false);
                }
            }
        });
        
        gd = new GridData();
        gd.horizontalAlignment = GridData.FILL;
        gd.horizontalSpan = 2;
        effDateTxt_.setLayoutData(gd);
        
        // Expiration Date
        label = new Label(vsdMetaDataGp_, SWT.BEGINNING);
        label.setText("Expiration Date (MM/dd/YYYY): ");

        expDateTxt_ = new Text(vsdMetaDataGp_, SWT.BORDER | SWT.BEGINNING);
        expDateTxt_.setText("");
        if (vd_ != null)
        {
            if (vd_.getExpirationDate() != null)
                expDateTxt_.setText(sdf.format(vd_.getExpirationDate()));
            else
                expDateTxt_.setText("");
        }
        expDateTxt_.addListener(SWT.Verify, new Listener() {
            
            public void handleEvent(Event arg0) {
                String expDate = expDateTxt_.getText();
                if (StringUtils.isNotEmpty(expDate))
                {
                    setChangesMade(true);
                    //TODO compare dates
//                    if (vd_ != null && vd_.getEffectiveDate() != null && vd_.getEffectiveDate()tOwner().equalsIgnoreCase(owner))
//                        setChangesMade(false);
                }
            }
        });

        gd = new GridData();
        gd.horizontalAlignment = GridData.FILL;
        gd.horizontalSpan = 2;
        expDateTxt_.setLayoutData(gd);
    }
    
    private void setUpVSDSourceAndContextGp(){
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.verticalSpan = 3;        
        
        vsdSourceAndContextGp_ = new Group(valueSetsComposite_, SWT.NONE);
        vsdSourceAndContextGp_.setText("Source and Context");
        vsdSourceAndContextGp_.setLayoutData(gd);

        GridLayout layout = new GridLayout(1, true);
        vsdSourceAndContextGp_.setLayout(layout);
        
        // Source
        Label srcLabel = new Label(vsdSourceAndContextGp_, SWT.FILL | SWT.BEGINNING | SWT.HORIZONTAL);
        srcLabel.setText("Source:");
        gd = new GridData();
        gd.verticalSpan = 3;
        gd.horizontalSpan = 1;
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        srcLabel.setLayoutData(gd);
        
        sourceTxt_ = new Text(vsdSourceAndContextGp_, SWT.BORDER | SWT.BEGINNING);
        sourceTxt_.setText("");
        gd = new GridData();
        gd.verticalSpan = 2;
        gd.horizontalAlignment = GridData.FILL;
        sourceTxt_.setLayoutData(gd);
        
        sourceAddButton_ = Utility.makeButton("Add",
                vsdSourceAndContextGp_, SWT.NONE);
        gd = new GridData();
        gd.horizontalAlignment = GridData.END;
        gd.verticalAlignment = GridData.END;
        gd.verticalSpan = 1;
        sourceAddButton_.setLayoutData(gd);
        final List sourceCombo_ = new List(vsdSourceAndContextGp_, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
        
        sourceAddButton_.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent arg0) {
                if (sourceTxt_.getText() != null && sourceTxt_.getText().length() > 0)
                {
                    sourceCombo_.add(sourceTxt_.getText());
                    Source src = new Source();
                    src.setContent(sourceTxt_.getText());
                    sourceList.add(src);
                    if (vd_ != null)
                        vd_.addSource(src);
                    sourceTxt_.setText("");
                    setChangesMade(true);
                }           
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }
        });
        
        gd = new GridData(GridData.FILL, GridData.BEGINNING, true, false);
        gd.horizontalSpan = 5;
        gd.verticalSpan = 2;
        sourceCombo_.setLayoutData(gd);
        
        if (vd_ != null)
        {
            Source[] sources = vd_.getSource();
            
            for (Source source : sources)
            {
                sourceCombo_.add(source.getContent());
                sourceList.add(source);
            }
        }
        
        sourceRemoveButton_ = Utility.makeButton("remove",
                vsdSourceAndContextGp_, GridData.VERTICAL_ALIGN_BEGINNING
                        | GridData.FILL_HORIZONTAL
                        | GridData.HORIZONTAL_ALIGN_CENTER);
        gd = new GridData();
        gd.horizontalAlignment = GridData.END;
        gd.verticalSpan = 1;
        sourceRemoveButton_.setLayoutData(gd);
        
        sourceRemoveButton_.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent arg0) {
                int[] selectedItems = sourceCombo_.getSelectionIndices();
                
                MessageBox messageBox = new MessageBox(shell_,
                        SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                messageBox.setText("Remove Source");
                messageBox
                        .setMessage("Do you really want to remove the selected Source(s)?");
                if (messageBox.open() == SWT.YES) {
                    sourceCombo_.remove(selectedItems);
                    for (Source src : sourceList)
                    {
                        for (int loopIndex = 0; loopIndex < selectedItems.length; loopIndex++)
                        {
                            if (src.getContent().equals(selectedItems[loopIndex]))
                            {
                                sourceList.remove(src);
                                if (vd_ != null)
                                    vd_.removeSource(src);
                            }
                        }
                        
                    }
                    sourceCombo_.setBackground(redColor_);
                    setChangesMade(true);
                }                
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }
        });
        
        // Context
        Label contextLabel = new Label(vsdSourceAndContextGp_, SWT.NONE);
        contextLabel.setText("Realm or Context:");
        gd = new GridData();
        gd.horizontalAlignment = GridData.FILL;
        gd.verticalSpan = 3;
        contextLabel.setLayoutData(gd);
        
        contextTxt_ = new Text(vsdSourceAndContextGp_, SWT.BORDER | SWT.BEGINNING);
        contextTxt_.setText("");
        gd = new GridData();
        gd.horizontalAlignment = GridData.FILL;
        gd.verticalSpan = 2;
        contextTxt_.setLayoutData(gd);
        
        contextAddButton_ = Utility.makeButton("Add",
                vsdSourceAndContextGp_, GridData.VERTICAL_ALIGN_END
                        | GridData.FILL_HORIZONTAL);
        gd = new GridData();
        gd.horizontalAlignment = GridData.END;
        gd.verticalSpan = 1;
        contextAddButton_.setLayoutData(gd);
        final List contextCombo_ = new List(vsdSourceAndContextGp_, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);

        contextAddButton_.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent arg0) {
                if (contextTxt_.getText() != null && contextTxt_.getText().length() > 0)
                {
                    contextCombo_.add(contextTxt_.getText());
                    contextList.add(contextTxt_.getText());
                    if (vd_ != null)
                        vd_.addRepresentsRealmOrContext(contextTxt_.getText());
                    contextTxt_.setText("");
                    setChangesMade(true);
                }           
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }

        });
        
        gd = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gd.horizontalSpan = 5;
        gd.verticalSpan = 2;
        contextCombo_.setLayoutData(gd);
        
        if (vd_ != null)
        {
            String[] contexts = vd_.getRepresentsRealmOrContext();
            
            for (String context : contexts)
            {
                contextCombo_.add(context);
                contextList.add(context);
            }
        }
        
        contextRemoveButton_ = Utility.makeButton("remove",
                vsdSourceAndContextGp_, GridData.VERTICAL_ALIGN_BEGINNING
                        | GridData.FILL_HORIZONTAL
                        | GridData.HORIZONTAL_ALIGN_CENTER);
        gd = new GridData();
        gd.horizontalAlignment = GridData.END;
        gd.verticalSpan = 1;
        contextRemoveButton_.setLayoutData(gd);
        
        contextRemoveButton_.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                int[] selectedItems = contextCombo_.getSelectionIndices();
                String outString = "";
                for (int loopIndex = 0; loopIndex < selectedItems.length; loopIndex++)
                {
                  outString += selectedItems[loopIndex] + " ";
                }
            
                MessageBox messageBox = new MessageBox(shell_,
                        SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                messageBox.setText("Remove Context?");
                messageBox
                        .setMessage("Do you really want to remove the selected Realm or Context?");
                if (messageBox.open() == SWT.YES) {
                    contextCombo_.remove(selectedItems);
                    for (String ctx : contextList)
                    {
                        for (int loopIndex = 0; loopIndex < selectedItems.length; loopIndex++)
                        {
                          if (ctx.equals(selectedItems[loopIndex]))
                          {
                              contextList.remove(ctx);
                              if (vd_ != null)
                                  vd_.removeRepresentsRealmOrContext(ctx);
                          }
                        }                        
                    }
                    setChangesMade(true);
                }                
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }

        });
        
    }
    
    @SuppressWarnings("unused")
    private void setUpVSDRevisionGp(){
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.verticalSpan = 2;        
        
        vsdRevisionGp_ = new Group(valueSetsComposite_, SWT.NONE);
        vsdRevisionGp_.setText("Revision");
        vsdRevisionGp_.setLayoutData(gd);

        GridLayout layout = new GridLayout(2, false);
        vsdRevisionGp_.setLayout(layout);
        
        EntryState es = null;
        if (vd_ != null)
            es = vd_.getEntryState();
        
        // current revision
        Label label = new Label(vsdRevisionGp_, SWT.BEGINNING);
        label.setText("Current Revision : ");
        
        currRevisionTxt_ = new Text(vsdRevisionGp_, SWT.BORDER | SWT.BEGINNING);
        currRevisionTxt_.setText(es == null ? "" : es.getContainingRevision() == null ? "" : es.getContainingRevision());
        gd = new GridData();
        gd.horizontalAlignment = GridData.FILL;
        currRevisionTxt_.setLayoutData(gd);
        
        // previous revision
        label = new Label(vsdRevisionGp_, SWT.BEGINNING);
        label.setText("Previous Revision : ");
        
        prevRevisionTxt_ = new Text(vsdRevisionGp_, SWT.BORDER | SWT.BEGINNING);
        prevRevisionTxt_.setText(es == null ? "" : es.getPrevRevision() == null ? "" : es.getPrevRevision());
        gd = new GridData();
        gd.horizontalAlignment = GridData.FILL;
        prevRevisionTxt_.setLayoutData(gd);
        
        // all revision id
        String currRev = "R_Curr";
        String prevRev = "R_prev";
        java.util.List<String> prevRevisions = new ArrayList<String>();
        
        if (vd_ != null)
        {
            if (vd_.getEntryState() != null)
            {
                currRev = vd_.getEntryState().getContainingRevision();
                prevRev = vd_.getEntryState().getPrevRevision();
            }
        }
        
        new Label(vsdRevisionGp_, SWT.NONE).setText("All Revision Ids for this Value Set Definition :");
        final List revList = new List(vsdRevisionGp_, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
        gd = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gd.horizontalSpan = 5;
        gd.verticalSpan = 1;
        revList.setLayoutData(gd);
        
        revList.add(currRev);
        revList.add(prevRev);
        for (String rev : prevRevisions)
        {
            revList.add(rev);
        }
        revList.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                int[] selectedItems = revList.getSelectionIndices();
                String outString = "";
                for (int loopIndex = 0; loopIndex < selectedItems.length; loopIndex++)
                {
                  outString += selectedItems[loopIndex] + " ";
                }
              }

              public void widgetDefaultSelected(SelectionEvent event) {
                int[] selectedItems = revList.getSelectionIndices();
                String outString = "";
                for (int loopIndex = 0; loopIndex < selectedItems.length; loopIndex++)
                  outString += selectedItems[loopIndex] + " ";
              }

        });
        
        // Change Type
        ChangeType changeType = null;
        
        if (es != null)
            changeType = es.getChangeType();
        else if (vd_ == null)
            changeType = ChangeType.NEW;
            
        label = new Label(vsdRevisionGp_, SWT.BEGINNING);
        label.setText("Change Type : ");

        changeTypeCombo_ = new Combo(vsdRevisionGp_, SWT.BORDER | SWT.FILL);
        changeTypeCombo_.setItems(new String[] { ChangeType.DEPENDENT.name(), ChangeType.MODIFY.name(), 
                ChangeType.NEW.name(), ChangeType.REMOVE.name(), ChangeType.VERSIONABLE.name() });
        changeTypeCombo_.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        
        changeTypeCombo_.setText(changeType == null ? "" : changeType.name());
        gd = new GridData();
        gd.horizontalAlignment = GridData.FILL;
        changeTypeCombo_.setLayoutData(gd); 
        
        
        // revision date
        String revDateStr = "";
        label = new Label(vsdRevisionGp_, SWT.BEGINNING);
        label.setText("Revision Date : ");

        revDateTxt_ = new Text(vsdRevisionGp_, SWT.BORDER | SWT.BEGINNING);
        revDateTxt_.setText(revDateStr);
        gd = new GridData();
        gd.horizontalAlignment = GridData.FILL;
        revDateTxt_.setLayoutData(gd);
        
        // change agent
        String changeAgentStr = "";
        label = new Label(vsdRevisionGp_, SWT.BEGINNING);
        label.setText("Change Agent : ");

        changeAgentTxt_ = new Text(vsdRevisionGp_, SWT.BORDER | SWT.BEGINNING);
        changeAgentTxt_.setText(changeAgentStr);
        gd = new GridData();
        gd.horizontalAlignment = GridData.FILL;
        changeAgentTxt_.setLayoutData(gd);
        
        // change instruction
        String changeInstStr = "asddddddddddddddddddddddddddddd\n ddddddddddddddddddd\n" +
        		"asddddddddddddddddddddddddddddddddddddd\n ddddddddd" +
        		"asdddddddddddddddddddddddddddddddddddd\n dddddddddddd" +
        		"asdasdas dasdasdasd\n" +
        		"asdasd asdasd";
        label = new Label(vsdRevisionGp_, SWT.BEGINNING);
        label.setText("Change Instructions : ");

        changeInst = new StyledText(vsdRevisionGp_, SWT.BORDER |  SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
        gd = new GridData();
        gd.horizontalSpan = 10;
        gd.verticalSpan = 1;
        changeInst.setLayoutData(gd);  
        changeInst.setWordWrap(true);
        changeInst.setAlignment(SWT.LEFT);
        changeInst.setJustify(true);
        changeInst.setOrientation(SWT.CENTER);
        changeInst.setText(changeInstStr);
    }
    
    private void setUpVSDButtonsGp(){
        GridData gd = new GridData(GridData.FILL, GridData.CENTER, true, true);
        gd.horizontalIndent = 5;
        
        buttonsGp_ = new Group(valueSetsComposite_, SWT.NONE);
        buttonsGp_.setLayoutData(gd);

        GridLayout layout = new GridLayout(1, false);
        buttonsGp_.setLayout(layout);
        
     // edit button
        editButton_ = Utility.makeButton("Edit",
                buttonsGp_, GridData.VERTICAL_ALIGN_BEGINNING
                        | GridData.FILL_HORIZONTAL
                        | GridData.HORIZONTAL_ALIGN_CENTER);
        editButton_.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                // check that edit is being done only on current revision
                // checkrevision();
                
//                contextCombo_.redraw();
//                sourceCombo_.redraw();
                enableTextFields();
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }

        });
        
        resolveButton_ = Utility.makeButton("Resolve",
                buttonsGp_, GridData.VERTICAL_ALIGN_BEGINNING
                        | GridData.FILL_HORIZONTAL);
        resolveButton_.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                
                if (vd_ != null)
                    new ValueSetDefResolveCSFilter(lb_vsd_gui_, vd_, shell_, true, false);
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }

        });
        

        removeButton_ = Utility.makeButton("Remove",
                buttonsGp_, GridData.VERTICAL_ALIGN_BEGINNING
                        | GridData.FILL_HORIZONTAL);
        removeButton_.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                try {
                    // check if the selected revision is current.
                    // checkRevision();
                    URI uri = null;
                    try {
                        if (vd_ != null)
                            uri = new URI(vd_.getValueSetDefinitionURI());
                    } catch (URISyntaxException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

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
                        lb_vsd_gui_.getValueSetDefinitionService().removeValueSetDefinition(uri);
                        lb_vsd_gui_.refreshValueSetDefList();
                        
                        errorHandler.showInfo("Removed", "Selected Value Set Definition has been removed");
                        shell_.dispose();
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
        
        saveButton_ = Utility.makeButton("Save",
                buttonsGp_, GridData.VERTICAL_ALIGN_BEGINNING
                        | GridData.FILL_HORIZONTAL);
        saveButton_.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                if (StringUtils.isEmpty(vsdURITxt_.getText()))
                {
                        errorHandler.showError("Invalid Data", 
                                "Value Set Definition URI can not be empty");
                        vsdURITxt_.setFocus();
                }
                else
                {
                    try {
                        new URI(vsdURITxt_.getText());
                    } catch (URISyntaxException e) {
                        errorHandler.showError("Invalid Data", 
                            "Value Set Definition URI is NOT WELL FORMED");
                        vsdURITxt_.setFocus();
                        return;
                    }
                    
                    boolean allDatesGood = true;
                    if (StringUtils.isNotEmpty(effDateTxt_.getText()))
                    {
                        if (!isDateValid(effDateTxt_.getText()))
                        {
                            allDatesGood = false;
                            errorHandler.showError("Invalid Data", 
                                    "The date provided is in an invalid date or " +
                                    " format. Please enter date in MM/dd/YYYY format.");
                            effDateTxt_.setBackground(redColor_);
                            effDateTxt_.setFocus();
                        }
                    }
                    if (StringUtils.isNotEmpty(expDateTxt_.getText()))   
                    {
                        if (!isDateValid(expDateTxt_.getText()))
                        {
                            allDatesGood = false;
                            errorHandler.showError("Invalid Data", 
                                    "The date provided is in an invalid date or " +
                                    " format. Please enter date in MM/dd/YYYY format.");
                            expDateTxt_.setBackground(redColor_);
                            expDateTxt_.setFocus();
                        }
                    }
                    
                    if (allDatesGood)
                    {
                        buildAndLoadVSD();
                        
                        if (changesSaved)
                        {
                            errorHandler.showInfo("Changes Saved", "Changes to Value Set Definition Saved");
                            enablePropertyButtons();
                            enableRefButtons();
                            refreshSupportedAttribList();
                            refreshDefinitionEntryList();
                            refreshVSDPropertyList();
                            setChangesMade(false);
                            lb_vsd_gui_.refreshValueSetDefList();
                            resolveButton_.setEnabled(true);
                            changesSaved = false;
                        }
                    }
                }
                
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                //
            }

        });
        
        closeButton_ = Utility.makeButton("Close",
                buttonsGp_, GridData.VERTICAL_ALIGN_BEGINNING
                        | GridData.FILL_HORIZONTAL);
        closeButton_.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                if (!isChangesMade())
                {
                    shell_.dispose();
                }
                else
                {
                    MessageBox messageBox = new MessageBox(shell_,
                            SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                    messageBox.setText("Changes have been made?");
                    messageBox
                            .setMessage("Changes have been made. Ignore them?");
                    if (messageBox.open() == SWT.YES) {
                        shell_.dispose();
                    }
                }
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                //
            }

        });
    }
    
    private void buildValueSetsComposite(Composite holder) {
        valueSetsComposite_ = new Composite(holder, SWT.BORDER);

        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        gd.verticalSpan = 2;
        
        valueSetsComposite_.setLayout(new GridLayout(6, false));
        valueSetsComposite_.setLayoutData(gd);

        // group 1 for vsd meta data
        setUpVSDMetaDataGp();
        
        // group 2 for source and context
        setUpVSDSourceAndContextGp();
        
        // group 3 for revision
//        setUpVSDRevisionGp();
        
        // group 4 for buttons
        setUpVSDButtonsGp();
        
    }
    
    public ValueSetDefinitionDetails(Shell parent, PickListDefinition pl) {
        shell_ = new Shell(parent.getDisplay());
        shell_.setText("Pick List Definition Viewer");
        shell_.setSize(700, 550);
        shell_.setImage(new Image(shell_.getDisplay(), this.getClass()
                .getResourceAsStream("/icons/icon.gif")));

        buildComponents();
        
        shell_.setVisible(true);
        
        TextContent tc = new TextContent(shell_.getDisplay());
        tc.setContent(VSDObjectToString.toString(pl));
        results_.setText(tc.toPlainText());
        results_.setStyleRanges(tc.getStyleRanges());
    }

    public void buildComponents() {
        shell_.setLayout(new GridLayout(1, true));

        // results area

        results_ = new StyledText(shell_, SWT.WRAP | SWT.BORDER
                | SWT.MULTI | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL);
        results_.setLayoutData(new GridData(GridData.FILL_BOTH));
    }
    
    private void buildMenus() {
        Menu mBar = new Menu(shell_, SWT.BAR);

        // Query menu
        Menu queryMenu = new Menu(shell_, SWT.DROP_DOWN);
        queryItem_ = new MenuItem(mBar, SWT.CASCADE);
        queryItem_.setMenu(queryMenu);
        queryItem_.setText("&Query");

        MenuItem checkForSubset = new MenuItem(queryMenu, SWT.NONE);
        checkForSubset.setText("Value Set Definition is Sub Set");
        checkForSubset.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                new ValueSetDefIsSubSet(lb_vsd_gui_, vsdURITxt_.getText(), shell_);                
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
                Mappings vsdMappings = null;
                if (vd_ != null)
                {
                    vsdURI = vd_.getValueSetDefinitionURI();
                    vsdMappings = vd_.getMappings();
                }
                else
                {
                    vsdURI = StringUtils.isNotEmpty(vsdURITxt_.getText()) ? vsdURITxt_.getText() : null;
                }
                new ValueSetDefIsEntityInValueSetFilter(lb_vsd_gui_, vsdURI, vsdMappings, shell_);
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
                if (vd_ != null)
                {
                    vsdURI = vd_.getValueSetDefinitionURI();
                }
                else
                {
                    vsdURI = StringUtils.isNotEmpty(vsdURITxt_.getText()) ? vsdURITxt_.getText() : null;
                }
                new ValueSetDefTermFilter(lb_vsd_gui_, vsdURI, shell_);
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // not used
            }
        });        

        shell_.setMenuBar(mBar);

    }
    
    private void disableTextFields(){
        vsdURITxt_.setEditable(false);
        vsdNameTxt_.setEditable(false);
        defaultCSCombo_.setEnabled(false);
        conceptDomainTxt_.setEditable(false);
        statusTxt_.setEditable(false);
        isActiveCombo_.setEnabled(false);
//        changeTypeCombo_.setEnabled(false);
        ownerTxt_.setEditable(false);
        effDateTxt_.setEditable(false);
        expDateTxt_.setEditable(false);
        
        sourceTxt_.setEditable(false);
        sourceAddButton_.setEnabled(false);
        sourceRemoveButton_.setEnabled(false);
        contextTxt_.setEditable(false);
        contextAddButton_.setEnabled(false);
        contextRemoveButton_.setEnabled(false);
        
//        changeAgentTxt_.setEditable(false);
//        revDateTxt_.setEditable(false);
//        currRevisionTxt_.setEditable(false);
//        prevRevisionTxt_.setEditable(false);
        
        saveButton_.setEnabled(false);
//        removeButton_.setEnabled(false);
//        editButton_.setEnabled(false);
    }
    
    private void enableTextFields(){
        vsdNameTxt_.setEditable(true);
        defaultCSCombo_.setEnabled(true);
        conceptDomainTxt_.setEditable(true);
        statusTxt_.setEditable(true);
        isActiveCombo_.setEnabled(true);
//        changeTypeCombo_.setEnabled(true);
        ownerTxt_.setEditable(true);
        effDateTxt_.setEditable(true);
        expDateTxt_.setEditable(true);
        
        sourceTxt_.setEditable(true);
        sourceAddButton_.setEnabled(true);
        sourceRemoveButton_.setEnabled(true);
        contextTxt_.setEditable(true);
        contextAddButton_.setEnabled(true);
        contextRemoveButton_.setEnabled(true);
        
//        changeAgentTxt_.setEditable(true);
//        revDateTxt_.setEditable(true);
//        currRevisionTxt_.setEditable(true);
//        prevRevisionTxt_.setEditable(true);
        
//        removeButton_.setEnabled(false);
//        editButton_.setEnabled(false);
    }
    
    private void disableRefButtons(){
        entityRefAddButton_.setEnabled(false);
        entityRefEditButton_.setEnabled(false);
        entityRefRemoveButton_.setEnabled(false);
        
        csRefAddButton_.setEnabled(false);
        csRefEditButton_.setEnabled(false);
        csRefRemoveButton_.setEnabled(false);
        
        vsdRefAddButton_.setEnabled(false);
        vsdRefEditButton_.setEnabled(false);
        vsdRefRemoveButton_.setEnabled(false);
        
        propRefAddButton_.setEnabled(false);
        propRefEditButton_.setEnabled(false);        
        propRefRemoveButton_.setEnabled(false);
    }
    
    private void enableRefButtons(){
        entityRefAddButton_.setEnabled(true);
        entityRefEditButton_.setEnabled(true);
        entityRefRemoveButton_.setEnabled(true);
        
        csRefAddButton_.setEnabled(true);
        csRefEditButton_.setEnabled(true);
        csRefRemoveButton_.setEnabled(true);
        
        vsdRefAddButton_.setEnabled(true);
        vsdRefEditButton_.setEnabled(true);
        vsdRefRemoveButton_.setEnabled(true);
        
        propRefAddButton_.setEnabled(true);
        propRefEditButton_.setEnabled(true);
        propRefRemoveButton_.setEnabled(true);
    }
    
    private void disablePropertyButtons(){
        propertyAddButton_.setEnabled(false);
        propertyEditButton_.setEnabled(false);
        propertyRemoveButton_.setEnabled(false);
    }
    
    private void enablePropertyButtons(){
        propertyAddButton_.setEnabled(true);
        propertyEditButton_.setEnabled(true);
        propertyRemoveButton_.setEnabled(true);
    }
    
    /**
     * @return the changesMade
     */
    public boolean isChangesMade() {
        return changesMade;
    }

    /**
     * @param changesMade the changesMade to set
     */
    public void setChangesMade(boolean changesMade) {
        this.changesMade = changesMade;
        saveButton_.setEnabled(true);
        removeButton_.setEnabled(true);
        editButton_.setEnabled(true);
    }
    
    @SuppressWarnings("deprecation")
    private void buildAndLoadVSD(){
        if (vd_ == null)
        {
            vd_ = new ValueSetDefinition();
            vd_.setSource(sourceList);
            vd_.setRepresentsRealmOrContext(contextList);
            vd_.setProperties(new Properties());
            vd_.setMappings(new Mappings());
        }
        else
        {
            try {
                lb_vsd_gui_.getValueSetDefinitionService().removeValueSetDefinition(new URI(vd_.getValueSetDefinitionURI()));
            } catch (LBException e) {
                errorHandler.showError("Problem Updating Value Set Definition", 
                        "There was an unexpected problem updating Value Set Definition. : " + e.getMessage());
            } catch (URISyntaxException e) {
                errorHandler.showError("Problem Updating Value Set Definition", 
                        "There was an unexpected problem updating Value Set Definition. : " + e.getMessage());
            }
        }
        vd_.setValueSetDefinitionURI(vsdURITxt_.getText());
        vd_.setValueSetDefinitionName(vsdNameTxt_.getText());
        vd_.setDefaultCodingScheme(defaultCSCombo_.getText());
        vd_.setConceptDomain(conceptDomainTxt_.getText());
        vd_.setIsActive(Boolean.valueOf(isActiveCombo_.getText()));
        vd_.setStatus(statusTxt_.getText());
        vd_.setOwner(ownerTxt_.getText());
        vd_.setEffectiveDate(StringUtils.isNotEmpty(effDateTxt_.getText()) ? new Date(effDateTxt_.getText()) : null);
        vd_.setExpirationDate(StringUtils.isNotEmpty(expDateTxt_.getText()) ? new Date(expDateTxt_.getText()) : null);   
        
        try {
            
            lb_vsd_gui_.getValueSetDefinitionService().loadValueSetDefinition(vd_, null, null);
            changesSaved = true;
        } catch (LBException e) {
            errorHandler.showError("Problem Loading Value Set Definition", 
                    "There was an unexpected problem loading Value Set Definition. : " + e.getMessage());
            changesSaved = false;
        } catch (Exception e) {
            errorHandler.showError("Problem Loading Value Set Definition", 
                    "There was an unexpected problem loading Value Set Definition. : " + e.getMessage());
            changesSaved = false;
        }
    }
    
    private boolean isDateValid(String dateStr){
        String format = "M/d/yyyy";
        String reFormat = Pattern.compile("d+|M+").matcher(Matcher.quoteReplacement(format)).replaceAll("\\\\d{1,2}");
        reFormat = Pattern.compile("y+").matcher(reFormat).replaceAll("\\\\d{4}");
        if ( Pattern.compile(reFormat).matcher(dateStr).matches() ) {

          // date string matches format structure, 
          // - now test it can be converted to a valid date
          SimpleDateFormat sdf = (SimpleDateFormat)DateFormat.getDateInstance();
          sdf.setLenient(false);          
          sdf.applyPattern(format);
          try { 
              sdf.parse(dateStr);
              return true;              
          } catch (ParseException e) { 
              return false;
          }
        } 
        return false;
    }
    
    private void refreshValueSetDefRefList() {
        shell_.getDisplay().syncExec(new Runnable() {
            public void run() {
                vsdRefTV_.setContentProvider(new ValueSetDefReferenceContentProvider(lb_vsd_gui_, vd_));
                vsdRefTV_.setInput("");
            }
        });
    }
    
    private void refreshEntityRefList() {
        shell_.getDisplay().syncExec(new Runnable() {
            public void run() {
                entityRefTV_.setContentProvider(new EntityReferenceContentProvider(lb_vsd_gui_, vd_));
                entityRefTV_.setInput("");
            }
        });
    }    
    
    private void refreshPropertyRefList() {
        shell_.getDisplay().syncExec(new Runnable() {
            public void run() {
                propertyRefTV_.setContentProvider(new PropertyReferenceContentProvider(lb_vsd_gui_, vd_));
                propertyRefTV_.setInput("");
            }
        });
    } 
    
    private void refreshSupportedAttribList() {
        shell_.getDisplay().syncExec(new Runnable() {
            public void run() {
                vsdSuppAttribTV_.setContentProvider(new SupportedAttributesContentProvider(lb_vsd_gui_, vd_));
                vsdSuppAttribTV_.setInput("");
            }
        });
    } 
    
    private void refreshCodingSchemeRefList() {
        shell_.getDisplay().syncExec(new Runnable() {
            public void run() {
                csRefTV_.setContentProvider(new CodingSchemeReferenceContentProvider(lb_vsd_gui_, vd_));
                csRefTV_.setInput("");
            }
        });
    }
    
    public void refreshDefinitionEntryList(){
        refreshCodingSchemeRefList();
        refreshEntityRefList();
        refreshPropertyRefList();
        refreshValueSetDefRefList();
    }
    
    public void refreshVSDPropertyList(){
        shell_.getDisplay().syncExec(new Runnable() {
            public void run() {
                vsdPropertyTV_.setContentProvider(new PropertyContentProvider(lb_vsd_gui_, vd_));
                vsdPropertyTV_.setInput("");
            }
        });
    }
    
    public void addDefinitionEntry(DefinitionEntry defEntryToAdd){
        vd_.addDefinitionEntry(defEntryToAdd);
        setChangesMade(true);
    }
    
    public void removeDefinitionEntry(DefinitionEntry defEntryToRemove){
        vd_.removeDefinitionEntry(defEntryToRemove);
        setChangesMade(true);
    }
    
    public void updateDefinitionEntry(DefinitionEntry oldDefEntry, DefinitionEntry newDefEntry){
        removeDefinitionEntry(oldDefEntry);
        addDefinitionEntry(newDefEntry);
    }   
    
    public void addVSDProperty(Property propertyToAdd){
        vd_.getProperties().addProperty(propertyToAdd);
        setChangesMade(true);
    }
    
    public void removeVSDProperty(Property propertyToRemove){
        if (vd_ != null && vd_.getProperties() != null)
        {
            vd_.getProperties().removeProperty(propertyToRemove);
            setChangesMade(true);
        }
    }
    
    public void updateVSDProperty(Property oldVSDProperty, Property newVSDProperty){
        removeVSDProperty(oldVSDProperty);
        addVSDProperty(newVSDProperty);
    }
}