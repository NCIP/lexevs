
package org.LexGrid.LexBIG.gui.valueSetsView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.LexGrid.LexBIG.gui.DialogHandler;
import org.LexGrid.LexBIG.gui.LB_VSD_GUI;
import org.LexGrid.LexBIG.gui.Utility;
import org.LexGrid.LexBIG.gui.ValueSetDefinitionDetails;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

/**
 * Class for displaying property details.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class PropertyView {
    private Shell shell_;
//    private ValueSetDefinition vd_ = null;
    @SuppressWarnings("unused")
    private Group propertyMetaDataGp_, propertySourceAndContextGp_, propertyRevisionGp_, buttonsGp_;
    @SuppressWarnings("unused")
    private Text  propertyIdTxt_, propertyNameTxt_, propertyTypeTxt_, languageTxt_, propertyValueTxt_, statusTxt_, ownerTxt_, 
        effDateTxt_, expDateTxt_, changeTypeTxt_, revDateTxt_, changeAgentTxt_, sourceTxt_, contextTxt_;
    @SuppressWarnings("unused")
    private StyledText changeInst;
    private Composite propertyComposite_, propertyQualComposite_;
    private Button editButton_, removeButton_, saveButton_, closeButton_, sourceAddButton_, sourceRemoveButton_,
        contextAddButton_, contextRemoveButton_, 
        propertyQualAddButton_, propertyQualEditButton_, propertyQualRemoveButton_;
    @SuppressWarnings("unused")
    private Combo isActiveCombo_, changeTypeCombo_;
    private TableViewer propertyQualTV_;
    private boolean changesMade = false;
    private LB_VSD_GUI lb_vsd_gui_;
    private Color redColor_;
    private Property property_;
    private Property oldProperty_;
    private DialogHandler errorHandler;
    private java.util.List<Source> sourceList = new ArrayList<Source>();
    private java.util.List<String> contextList = new ArrayList<String>();
    private ValueSetDefinitionDetails vsdDetails_;
    
    public PropertyView(LB_VSD_GUI lb_vsd_gui, ValueSetDefinitionDetails vsdDetails, Shell parent, ValueSetDefinition vd, Property property) {
        this.lb_vsd_gui_ = lb_vsd_gui;
        
        property_ = property;
        shell_ = new Shell(parent.getDisplay());
        vsdDetails_ = vsdDetails;
        oldProperty_ = property;
        
        Device device = Display.getCurrent ();
        redColor_ = new Color (device, 255, 0, 0);
        
        errorHandler = new DialogHandler(shell_);
        shell_.setText("Property Details ");
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
        
        
        buildPropertyComposite(topBottom);        

        SashForm leftRightBottom = new SashForm(topBottom, SWT.HORIZONTAL);
        leftRightBottom.SASH_WIDTH = 5;
        buildPropertyQualifierConposite(leftRightBottom);
        
        // disable all the text fields
        if (property != null)
        {
            disableTextFields();
            enablePropertyQualButtons();
        }
        else
        {
            enableTextFields();
            disablePropertyQualButtons();
        }
        
        shell_.open();
    }
    
    private void buildPropertyQualifierConposite(Composite holder){
        
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 30;
        gd.verticalSpan = 12;
        propertyQualComposite_ = new Composite(holder, SWT.BORDER);
        
        propertyQualComposite_.setLayout(new GridLayout(1, false));
        propertyQualComposite_.setLayoutData(gd);

        Utility.makeBoldLabel(propertyQualComposite_, 2,
                GridData.HORIZONTAL_ALIGN_CENTER, "Available Property Qualifiers");
        
        
        setUpProeprtyQualGp();
     }
    
    private void setUpProeprtyQualGp(){
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.verticalSpan = 12;
        gd.horizontalSpan = 6;
        
        Group group = new Group(propertyQualComposite_, SWT.NONE);
        group.setLayoutData(gd);

        GridLayout layout = new GridLayout(2, false);
        group.setLayout(layout);
        
        gd = new GridData(GridData.FILL_BOTH);
        gd.verticalSpan = 11;
        gd.horizontalSpan = 1;
        propertyQualTV_ = new TableViewer(group, SWT.BORDER
                | SWT.SINGLE | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
        
        propertyQualTV_.getTable().setLayoutData(gd);

        propertyQualTV_.setContentProvider(new PropertyQualifierContentProvider(lb_vsd_gui_, property_));
        PropertyQualifierLabelProvider lp = new PropertyQualifierLabelProvider();
        propertyQualTV_.setLabelProvider(lp);

        propertyQualTV_.setUseHashlookup(true);
        propertyQualTV_.getTable().setHeaderVisible(true);
        propertyQualTV_.getTable().setLayoutData(gd);
        propertyQualTV_.getTable().setLinesVisible(true);

        lp.setupColumns(propertyQualTV_.getTable());
        propertyQualTV_.setInput("");
        propertyQualTV_
                .addSelectionChangedListener(new ISelectionChangedListener() {

                    public void selectionChanged(SelectionChangedEvent arg0) {
//                        updateButtonStates();
                    }

                });
 //
        
        gd = new GridData(GridData.END);
        gd.verticalSpan = 1;
        propertyQualAddButton_ = Utility.makeButton("Add",
                group, GridData.END
                        | GridData.FILL_HORIZONTAL);
        propertyQualAddButton_.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                new PropertyQualifierView(PropertyView.this, shell_, null);
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }

        });
        propertyQualAddButton_.setLayoutData(gd);
        
        layout = new GridLayout(1, false);
        
        gd = new GridData(GridData.END);
        gd.verticalSpan = 1;
        gd.horizontalSpan = 1;
        propertyQualRemoveButton_ = Utility.makeButton("Remove",
                group, GridData.END
                        | GridData.FILL_HORIZONTAL);
        propertyQualRemoveButton_.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                TableItem[] temp = propertyQualTV_.getTable().getSelection();
                if (temp == null ) {
                    errorHandler.showError("No Property Qualifier selected",
                            "You must select a Property Qualifier first.");
                    return;
                }

                MessageBox messageBox = new MessageBox(shell_,
                        SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                messageBox.setText("Remove Property");
                messageBox.setMessage("Do you really want to remove the selected Property Qualifier?");
                if (messageBox.open() == SWT.YES) {
                    PropertyQualifier propertyQualToRemove = ((PropertyQualifier)temp[0].getData());
                    removePropertyQualifier(propertyQualToRemove);
                    refreshPropertyQualList();
                    setChangesMade(true);
                }
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }

        });
        propertyQualRemoveButton_.setLayoutData(gd);
        
        gd = new GridData(GridData.END);
        gd.verticalSpan = 1;
        gd.horizontalSpan = 1;
        propertyQualEditButton_ = Utility.makeButton("Edit",
                group, GridData.END
                        | GridData.FILL_HORIZONTAL);
        propertyQualEditButton_.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                TableItem[] temp = propertyQualTV_.getTable().getSelection();
                if (temp.length < 1)
                {
                    errorHandler.showError("No Property Qualifier has been selected",
                    "You must select a Property Qualifier to edit.");
                    return;
                }
                new PropertyQualifierView(PropertyView.this, shell_, ((PropertyQualifier) temp[0].getData()));
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }

        });
        propertyQualEditButton_.setLayoutData(gd);
    }
    
    private void setUpPropertyMetaDataGp(){
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.verticalSpan = 2;  
        gd.horizontalSpan = 3;
        
        propertyMetaDataGp_ = new Group(propertyComposite_, SWT.NONE);
        propertyMetaDataGp_.setText("Property meta data");
        propertyMetaDataGp_.setLayoutData(gd);

        GridLayout layout = new GridLayout(3, true);
        propertyMetaDataGp_.setLayout(layout);
        
        // Property ID
        Label label = new Label(propertyMetaDataGp_, SWT.BEGINNING);
        label.setText("Property ID : ");
        gd = new GridData();
        label.setLayoutData(gd);
        
        propertyIdTxt_ = new Text(propertyMetaDataGp_, SWT.BORDER | SWT.FILL);
        if (property_ != null)
            propertyIdTxt_.setText(property_.getPropertyId() == null ? "" : property_.getPropertyId());
        gd = new GridData();
        gd.horizontalAlignment = GridData.FILL;
        gd.horizontalSpan = 2;
        propertyIdTxt_.setLayoutData(gd);
        Font font = new Font(shell_.getDisplay(),"Arial",8,SWT.BOLD | SWT.ITALIC);
        propertyIdTxt_.setFont(font);
        propertyIdTxt_.addListener(SWT.Verify, new Listener() {
            
            public void handleEvent(Event arg0) {
                String propertyId = propertyIdTxt_.getText();
                if (StringUtils.isNotEmpty(propertyId))
                {
                    String originalPropertyId = "";
                    if (property_ != null && property_.getPropertyId() != null)
                        originalPropertyId = property_.getPropertyId();
                    if (!originalPropertyId.equalsIgnoreCase(propertyId))
                        setChangesMade(true);
                }
            }
        });
        
        // Property Name
        label = new Label(propertyMetaDataGp_, SWT.BEGINNING);
        label.setText("Property Name : ");
        gd = new GridData();
        label.setLayoutData(gd);
        
        propertyNameTxt_ = new Text(propertyMetaDataGp_, SWT.BORDER | SWT.FILL);
        if (property_ != null)
            propertyNameTxt_.setText(property_.getPropertyName());
        gd = new GridData();
        gd.horizontalAlignment = GridData.FILL;
        gd.horizontalSpan = 2;
        propertyNameTxt_.setLayoutData(gd);
        propertyNameTxt_.addListener(SWT.Verify, new Listener() {
            
            public void handleEvent(Event arg0) {
                String propertyName = propertyNameTxt_.getText();                
                if (StringUtils.isNotEmpty(propertyName))
                {
                    if (property_ != null) 
                    {
                        if (!property_.getPropertyName().equalsIgnoreCase(propertyName))
                            setChangesMade(true);                        
                    }
                    else
                        setChangesMade(true);
                }
            }
        });
        
        // Property Type
        label = new Label(propertyMetaDataGp_, SWT.BEGINNING);
        label.setText("Property Type : ");
        gd = new GridData();
        label.setLayoutData(gd);

        propertyTypeTxt_ = new Text(propertyMetaDataGp_, SWT.BORDER | SWT.FILL);
        if (property_ != null)
            propertyTypeTxt_.setText(property_.getPropertyType() == null ? "" : property_.getPropertyType());
        gd = new GridData();
        gd.horizontalAlignment = GridData.FILL;
        gd.horizontalSpan = 2;
        propertyTypeTxt_.setLayoutData(gd);
        propertyTypeTxt_.addListener(SWT.Verify, new Listener() {
            
            public void handleEvent(Event arg0) {
                String propertyType = propertyTypeTxt_.getText();
                
                if (StringUtils.isNotEmpty(propertyType))
                {
                    if (property_ != null && property_.getPropertyType() != null)
                    {
                        if (!property_.getPropertyType().equalsIgnoreCase(propertyType))
                            setChangesMade(true);
                    }
                    else
                        setChangesMade(true);
                }
            }
        });
        
        // Language
        label = new Label(propertyMetaDataGp_, SWT.BEGINNING);
        label.setText("Language : ");

        languageTxt_ = new Text(propertyMetaDataGp_, SWT.BORDER | SWT.FILL);
        if (property_ != null)
            languageTxt_.setText(property_.getLanguage() == null ? "" : property_.getLanguage());
        gd = new GridData();
        gd.horizontalAlignment = GridData.FILL;  
        gd.horizontalSpan = 2;
        languageTxt_.setLayoutData(gd);     
        languageTxt_.addListener(SWT.Verify, new Listener() {
            
            public void handleEvent(Event arg0) {
                String language = languageTxt_.getText();
                
                if (StringUtils.isNotEmpty(language))
                {
                    if (property_ != null && property_.getLanguage() != null)
                    {
                        if (!property_.getLanguage().equalsIgnoreCase(language))
                            setChangesMade(true);
                    }
                    else
                        setChangesMade(true);
                }
            }
        });
        
        // Property Value
        label = new Label(propertyMetaDataGp_, SWT.BEGINNING);
        label.setText("Property Value : ");

        propertyValueTxt_ = new Text(propertyMetaDataGp_, SWT.BORDER | SWT.FILL);
        if (property_ != null)
            propertyValueTxt_.setText(property_.getValue() == null ? "" : property_.getValue().getContent() == null ? "" : property_.getValue().getContent());
        gd = new GridData();
        gd.horizontalAlignment = GridData.FILL;  
        gd.horizontalSpan = 2;
        propertyValueTxt_.setLayoutData(gd);     
        propertyValueTxt_.addListener(SWT.Verify, new Listener() {
            
            public void handleEvent(Event arg0) {
                String propertyValue = propertyValueTxt_.getText();
                
                if (StringUtils.isNotEmpty(propertyValue))
                {
                    if (property_ != null && property_.getValue() != null 
                            && property_.getValue().getContent() != null)
                    {
                            if (!property_.getValue().getContent().equalsIgnoreCase(propertyValue))
                                setChangesMade(false);
                    }
                    else
                        setChangesMade(true);
                }
            }
        });
        
        // isActive
        label = new Label(propertyMetaDataGp_, SWT.BEGINNING);
        label.setText("Is Active : ");
        
        isActiveCombo_ = new Combo(propertyMetaDataGp_, SWT.BORDER | SWT.FILL);
        isActiveCombo_.setItems(new String[] { Boolean.TRUE.toString() , Boolean.FALSE.toString() });
        isActiveCombo_.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        if (property_ != null)
            isActiveCombo_.setText(String.valueOf(property_.isIsActive()));
        gd = new GridData();
        gd.horizontalAlignment = GridData.FILL;   
        gd.horizontalSpan = 2;
        isActiveCombo_.setLayoutData(gd);
        isActiveCombo_.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent arg0) {
                Boolean isActive = Boolean.valueOf(isActiveCombo_.getText());
                
                if (property_ != null)
                {
                    if (!property_.getIsActive() == isActive)
                        setChangesMade(true);
                }
                else
                    setChangesMade(true);
            }
            
            public void widgetDefaultSelected(SelectionEvent arg0) {
                // TODO Auto-generated method stub
                
            }
        });
        
        // Status
        label = new Label(propertyMetaDataGp_, SWT.BEGINNING);
        label.setText("Status : ");

        statusTxt_ = new Text(propertyMetaDataGp_, SWT.BORDER | SWT.FILL);
        if (property_ != null)
            statusTxt_.setText(property_.getStatus() == null ? "" : property_.getStatus());
        gd = new GridData();
        gd.horizontalAlignment = GridData.FILL;  
        gd.horizontalSpan = 2;
        statusTxt_.setLayoutData(gd);  
        statusTxt_.addListener(SWT.Verify, new Listener() {
            
            public void handleEvent(Event arg0) {
                String status = statusTxt_.getText();
                if (StringUtils.isNotEmpty(status))
                {
                    if (property_ != null && property_.getStatus() != null)
                    { 
                        if (!property_.getStatus().equalsIgnoreCase(status))
                            setChangesMade(true);
                    }
                    else
                        setChangesMade(true);
                }
            }
        });
        
        
        // owner
        label = new Label(propertyMetaDataGp_, SWT.BEGINNING);
        label.setText("Owner : ");

        ownerTxt_ = new Text(propertyMetaDataGp_, SWT.BORDER | SWT.BEGINNING);
        if (property_ != null)
            ownerTxt_.setText(property_.getOwner() == null ? "" : property_.getOwner());
        gd = new GridData();
        gd.horizontalAlignment = GridData.FILL;
        gd.horizontalSpan = 2;
        ownerTxt_.setLayoutData(gd); 
        ownerTxt_.addListener(SWT.Verify, new Listener() {
            
            public void handleEvent(Event arg0) {
                String owner = ownerTxt_.getText();
                
                if (StringUtils.isNotEmpty(owner))
                {
                    if (property_ != null && property_.getOwner() != null)
                    { 
                        if (!property_.getOwner().equalsIgnoreCase(owner))
                            setChangesMade(true);
                    }
                    else
                        setChangesMade(true);
                }
            }
        });
        
        // Effective Date
        label = new Label(propertyMetaDataGp_, SWT.BEGINNING);
        label.setText("Effective Date (MM/dd/YYYY): ");

        effDateTxt_ = new Text(propertyMetaDataGp_, SWT.BORDER | SWT.BEGINNING);
        if (property_ != null)
            effDateTxt_.setText(property_.getEffectiveDate() == null ? "" : property_.getEffectiveDate().toString());
        gd = new GridData();
        gd.horizontalAlignment = GridData.FILL;
        gd.horizontalSpan = 2;
        effDateTxt_.setLayoutData(gd); 
        effDateTxt_.addListener(SWT.Verify, new Listener() {
            
            @SuppressWarnings("deprecation")
            public void handleEvent(Event arg0) {
                String effDate = effDateTxt_.getText();
                
                if (StringUtils.isNotEmpty(effDate))
                {
                    if (property_ != null && property_.getEffectiveDate() != null)
                    {
                        if (property_.getEffectiveDate().compareTo(new Date(effDate)) != 0)
                            setChangesMade(true);
                    }
                    else
                        setChangesMade(true);
                }
            }
        });
        
        // Expiration Date
        label = new Label(propertyMetaDataGp_, SWT.BEGINNING);
        label.setText("Expiration Date (MM/dd/YYYY): ");

        expDateTxt_ = new Text(propertyMetaDataGp_, SWT.BORDER | SWT.BEGINNING);
        if (property_ != null)
            expDateTxt_.setText(property_.getExpirationDate() == null ? "" : property_.getExpirationDate().toString());
        gd = new GridData();
        gd.horizontalAlignment = GridData.FILL;
        gd.horizontalSpan = 2;
        expDateTxt_.setLayoutData(gd); 
        expDateTxt_.addListener(SWT.Verify, new Listener() {
            
            @SuppressWarnings("deprecation")
            public void handleEvent(Event arg0) {
                String expDate = expDateTxt_.getText();
                
                if (StringUtils.isNotEmpty(expDate))
                {
                    if (property_ != null && property_.getExpirationDate() != null)
                    {
                        if (property_.getExpirationDate().compareTo(new Date(expDate)) != 0)
                            setChangesMade(true);
                    }
                    else
                        setChangesMade(true);
                }
            }
        });        
    }
    
    private void setUpPropertySourceAndContextGp(){
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.verticalSpan = 3;        
        
        propertySourceAndContextGp_ = new Group(propertyComposite_, SWT.NONE);
        propertySourceAndContextGp_.setText("Source and Context");
        propertySourceAndContextGp_.setLayoutData(gd);

        GridLayout layout = new GridLayout(1, true);
        propertySourceAndContextGp_.setLayout(layout);
        
        // Source
        Label srcLabel = new Label(propertySourceAndContextGp_, SWT.FILL | SWT.BEGINNING | SWT.HORIZONTAL);
        srcLabel.setText("Source:");
        gd = new GridData();
        gd.verticalSpan = 3;
        gd.horizontalSpan = 1;
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        srcLabel.setLayoutData(gd);
        
        sourceTxt_ = new Text(propertySourceAndContextGp_, SWT.BORDER | SWT.BEGINNING);
        sourceTxt_.setText("");
        gd = new GridData();
        gd.verticalSpan = 2;
        gd.horizontalAlignment = GridData.FILL;
        sourceTxt_.setLayoutData(gd);
        
        sourceAddButton_ = Utility.makeButton("Add",
                propertySourceAndContextGp_, SWT.NONE);
        gd = new GridData();
        gd.horizontalAlignment = GridData.END;
        gd.verticalAlignment = GridData.END;
        gd.verticalSpan = 1;
        sourceAddButton_.setLayoutData(gd);
        final List sourceCombo_ = new List(propertySourceAndContextGp_, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
        
        sourceAddButton_.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent arg0) {
                if (sourceTxt_.getText() != null && sourceTxt_.getText().length() > 0)
                {
                    sourceCombo_.add(sourceTxt_.getText());
                    Source src = new Source();
                    src.setContent(sourceTxt_.getText());                    
                    sourceList.add(src);
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
        
        if (property_ != null)
        {
            Source[] sources = property_.getSource();
            
            for (Source source : sources)
            {
                sourceCombo_.add(source.getContent());
                sourceList.add(source);
            }
        }
        
        sourceRemoveButton_ = Utility.makeButton("remove",
                propertySourceAndContextGp_, GridData.VERTICAL_ALIGN_BEGINNING
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
        
        // Context
        Label contextLabel = new Label(propertySourceAndContextGp_, SWT.NONE);
        contextLabel.setText("Realm or Context:");
        gd = new GridData();
        gd.horizontalAlignment = GridData.FILL;
        gd.verticalSpan = 3;
        contextLabel.setLayoutData(gd);
        
        contextTxt_ = new Text(propertySourceAndContextGp_, SWT.BORDER | SWT.BEGINNING);
        contextTxt_.setText("");
        gd = new GridData();
        gd.horizontalAlignment = GridData.FILL;
        gd.verticalSpan = 2;
        contextTxt_.setLayoutData(gd);
        
        contextAddButton_ = Utility.makeButton("Add",
                propertySourceAndContextGp_, GridData.VERTICAL_ALIGN_END
                        | GridData.FILL_HORIZONTAL);
        gd = new GridData();
        gd.horizontalAlignment = GridData.END;
        gd.verticalSpan = 1;
        contextAddButton_.setLayoutData(gd);
        final List contextCombo_ = new List(propertySourceAndContextGp_, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);

        contextAddButton_.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent arg0) {
                if (contextTxt_.getText() != null && contextTxt_.getText().length() > 0)
                {
                    contextCombo_.add(contextTxt_.getText());
                    contextList.add(contextTxt_.getText());
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
        
        if (property_ != null)
        {
            String[] contexts = property_.getUsageContext();
            
            for (String context : contexts)
            {
                contextCombo_.add(context);
                contextList.add(context);
            }
        }
        
        contextRemoveButton_ = Utility.makeButton("remove",
                propertySourceAndContextGp_, GridData.VERTICAL_ALIGN_BEGINNING
                        | GridData.FILL_HORIZONTAL
                        | GridData.HORIZONTAL_ALIGN_CENTER);
        gd = new GridData();
        gd.horizontalAlignment = GridData.END;
        gd.verticalSpan = 1;
        contextRemoveButton_.setLayoutData(gd);
        
        contextRemoveButton_.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                int[] selectedItems = contextCombo_.getSelectionIndices();
                
                MessageBox messageBox = new MessageBox(shell_,
                        SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                messageBox.setText("Remove Context?");
                messageBox
                        .setMessage("Do you really want to remove the selected Usage Context?");
                if (messageBox.open() == SWT.YES) {
                    contextCombo_.remove(selectedItems);
                    for (String ctx : contextList)
                    {
                        for (int loopIndex = 0; loopIndex < selectedItems.length; loopIndex++)
                        {
                          if (ctx.equals(selectedItems[loopIndex]))
                          {
                              contextList.remove(ctx);
                          }
                        }                        
                    }
                    setChangesMade(true);
                }  
                
                if (messageBox.open() == SWT.YES) {
                    contextCombo_.remove(selectedItems);
                    setChangesMade(true);
                }                
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }

        });
        
    }
    
//    private void setUpVSDRevisionGp(){
//        GridData gd = new GridData(GridData.FILL_BOTH);
//        gd.verticalSpan = 2;        
//        
//        propertyRevisionGp_ = new Group(propertyComposite_, SWT.NONE);
//        propertyRevisionGp_.setText("Revision");
//        propertyRevisionGp_.setLayoutData(gd);
//
//        GridLayout layout = new GridLayout(2, false);
//        propertyRevisionGp_.setLayout(layout);
//        
//        EntryState es = null;
//        if (property_ != null)
//            es = property_.getEntryState();
//        
//        // revision id
//        String currRev = "R_Curr";
//        String prevRev = "R_prev";
//        java.util.List<String> prevRevisions = new ArrayList<String>();
//        
//        if (property_ != null)
//        {
//            if (property_.getEntryState() != null)
//            {
//                currRev = property_.getEntryState().getContainingRevision() == null ? "" : property_.getEntryState().getContainingRevision();
//                prevRev = property_.getEntryState().getPrevRevision() == null ? "" : property_.getEntryState().getPrevRevision();
//            }
//        }
//        
//        new Label(propertyRevisionGp_, SWT.NONE).setText("Revision Ids:");
//        final List revList = new List(propertyRevisionGp_, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
//        gd = new GridData(GridData.FILL, GridData.CENTER, true, false);
//        gd.horizontalSpan = 5;
//        gd.verticalSpan = 1;
//        revList.setLayoutData(gd);
//        
//        revList.add(currRev);
//        revList.add(prevRev);
//        for (String rev : prevRevisions)
//        {
//            revList.add(rev);
//        }
//        revList.addSelectionListener(new SelectionListener() {
//
//            public void widgetSelected(SelectionEvent arg0) {
//                int[] selectedItems = revList.getSelectionIndices();
//                String outString = "";
//                for (int loopIndex = 0; loopIndex < selectedItems.length; loopIndex++)
//                {
//                  outString += selectedItems[loopIndex] + " ";
//                  System.out.println("Selected Items: " + outString + " - " + revList.getItem(Integer.parseInt(outString.trim())));
//                }
//              }
//
//              public void widgetDefaultSelected(SelectionEvent event) {
//                int[] selectedItems = revList.getSelectionIndices();
//                String outString = "";
//                for (int loopIndex = 0; loopIndex < selectedItems.length; loopIndex++)
//                  outString += selectedItems[loopIndex] + " ";
//                System.out.println("Selected Items: " + outString);
//              }
//
//        });
//        
//        // Change Type
//        ChangeType changeType = null;
//        
//        if (es != null)
//            changeType = es.getChangeType();
//        
//        Label label = new Label(propertyRevisionGp_, SWT.BEGINNING);
//        label.setText("Change Type : ");
//
//        changeTypeTxt_ = new Text(propertyRevisionGp_, SWT.BORDER | SWT.BEGINNING);
//        changeTypeTxt_.setText(changeType == null ? "" : changeType.name());
//        gd = new GridData();
//        gd.horizontalAlignment = GridData.FILL;
//        changeTypeTxt_.setLayoutData(gd); 
//        
//        
//        // revision date
//        String revDateStr = "";
//        label = new Label(propertyRevisionGp_, SWT.BEGINNING);
//        label.setText("Revision Date : ");
//
//        revDateTxt_ = new Text(propertyRevisionGp_, SWT.BORDER | SWT.BEGINNING);
//        revDateTxt_.setText(revDateStr);
//        gd = new GridData();
//        gd.horizontalAlignment = GridData.FILL;
//        revDateTxt_.setLayoutData(gd);
//        
//        // change agent
//        String changeAgentStr = "";
//        label = new Label(propertyRevisionGp_, SWT.BEGINNING);
//        label.setText("Change Agent : ");
//
//        changeAgentTxt_ = new Text(propertyRevisionGp_, SWT.BORDER | SWT.BEGINNING);
//        changeAgentTxt_.setText(changeAgentStr);
//        gd = new GridData();
//        gd.horizontalAlignment = GridData.FILL;
//        changeAgentTxt_.setLayoutData(gd);
//        
//        // change instruction
//        String changeInstStr = "asddddddddddddddddddddddddddddd\n ddddddddddddddddddd\n" +
//                "asddddddddddddddddddddddddddddddddddddd\n ddddddddd" +
//                "asdddddddddddddddddddddddddddddddddddd\n dddddddddddd" +
//                "asdasdas dasdasdasd\n" +
//                "asdasd asdasd";
//        label = new Label(propertyRevisionGp_, SWT.BEGINNING);
//        label.setText("Change Instructions : ");
//
//        changeInst = new StyledText(propertyRevisionGp_, SWT.BORDER |  SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
//        gd = new GridData();
//        gd.horizontalSpan = 10;
//        gd.verticalSpan = 1;
//        changeInst.setLayoutData(gd);  
//        changeInst.setWordWrap(true);
//        changeInst.setAlignment(SWT.LEFT);
//        changeInst.setJustify(true);
//        changeInst.setOrientation(SWT.CENTER);
//        changeInst.setText(changeInstStr);
//    }
    
    private void setUpPropertyButtonsGp(){
        GridData gd = new GridData(GridData.FILL, GridData.CENTER, true, true);
        gd.horizontalIndent = 5;
        
        buttonsGp_ = new Group(propertyComposite_, SWT.NONE);
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
                
                enableTextFields();
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
                
                MessageBox messageBox = new MessageBox(shell_,
                        SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                messageBox.setText("Remove Property");
                messageBox.setMessage("Do you really want to remove this Property?");
                if (messageBox.open() == SWT.YES) {
                    vsdDetails_.removeVSDProperty(property_);
                    vsdDetails_.setChangesMade(true);
                    vsdDetails_.refreshVSDPropertyList();
                    shell_.close();
                    shell_.dispose();
                }
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                //
            }

        });
        
        saveButton_ = Utility.makeButton("ADD",
                buttonsGp_, GridData.VERTICAL_ALIGN_BEGINNING
                        | GridData.FILL_HORIZONTAL);
        saveButton_.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                if (isChangesMade())
                {
                    if (StringUtils.isEmpty(propertyNameTxt_.getText()))
                    {
                            errorHandler.showError("Invalid Data", 
                                    "Property Name can not be empty");
                            propertyNameTxt_.setFocus();
                    }
                    else
                    {
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
                            buildProperty();
                            vsdDetails_.updateVSDProperty(oldProperty_, property_);
                            vsdDetails_.refreshVSDPropertyList();
                            vsdDetails_.setChangesMade(true);
                            errorHandler.showInfo("Changes Saved", "Changes to Property Saved");
                            enablePropertyQualButtons();
                            setChangesMade(false);
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
                    shell_.close();
                    shell_.dispose();
                }
                else
                {
                    MessageBox messageBox = new MessageBox(shell_,
                            SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                    messageBox.setText("Changes have been made?");
                    messageBox.setMessage("Changes have been made. Ignore them?");
                    if (messageBox.open() == SWT.YES) {
                        shell_.close();
                        shell_.dispose();
                    }
                }
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                //
            }

        });
    }
    
    private void buildPropertyComposite(Composite holder) {
        propertyComposite_ = new Composite(holder, SWT.BORDER);

        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        gd.verticalSpan = 2;
        
        propertyComposite_.setLayout(new GridLayout(6, false));
        propertyComposite_.setLayoutData(gd);

        // group 1 for property meta data
        setUpPropertyMetaDataGp();
        
        // group 2 for source and context
        setUpPropertySourceAndContextGp();
        
        // group 3 for source and context
//        setUpPropertyQualifierGp();
        
        // group 4 for revision
//        setUpVSDRevisionGp();
        
        // group 5 for buttons
        setUpPropertyButtonsGp();
        
    }
    
    private void disableTextFields(){
        propertyIdTxt_.setEditable(false);
        propertyNameTxt_.setEditable(false);
        propertyValueTxt_.setEditable(false);
        propertyTypeTxt_.setEditable(false);
        languageTxt_.setEditable(false);
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
//        changeTypeTxt_.setEditable(false);
        
        saveButton_.setEnabled(false);
    }
    
    private void enableTextFields(){
        propertyNameTxt_.setEditable(true);
        propertyTypeTxt_.setEditable(true);
        propertyValueTxt_.setEditable(true);
        languageTxt_.setEditable(true);
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
//        changeTypeTxt_.setEditable(true);
        
        saveButton_.setEnabled(true);
    }
    
    private void disablePropertyQualButtons(){
        propertyQualAddButton_.setEnabled(false);
        propertyQualEditButton_.setEnabled(false);
        propertyQualRemoveButton_.setEnabled(false);
    }
    
    private void enablePropertyQualButtons(){
        propertyQualAddButton_.setEnabled(true);
        propertyQualEditButton_.setEnabled(true);
        propertyQualRemoveButton_.setEnabled(true);
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
    }
    
    public void refreshPropertyQualList(){
        shell_.getDisplay().syncExec(new Runnable() {
            public void run() {
                propertyQualTV_.setContentProvider(new PropertyQualifierContentProvider(lb_vsd_gui_, property_));
                propertyQualTV_.setInput("");
            }
        });
    }
    
    public void addPropertyQualifier(PropertyQualifier propertyQualToAdd){
        property_.addPropertyQualifier(propertyQualToAdd);
    }
    
    public void removePropertyQualifier(PropertyQualifier propertyQualToRemove){
        property_.removePropertyQualifier(propertyQualToRemove);
    }
    
    public void updatePropertyQualifier(PropertyQualifier oldPropertyQual, PropertyQualifier newPropertyQual){
        removePropertyQualifier(oldPropertyQual);
        addPropertyQualifier(newPropertyQual);
    }
    
    @SuppressWarnings("deprecation")
    private void buildProperty(){
        if (property_ == null)
            property_ = new Property();
        
        property_.setPropertyId(propertyIdTxt_.getText());
        property_.setPropertyName(propertyNameTxt_.getText());
        property_.setIsActive(Boolean.valueOf(isActiveCombo_.getText()));
        property_.setStatus(statusTxt_.getText());
        org.LexGrid.commonTypes.Text text = new org.LexGrid.commonTypes.Text();
        text.setContent(propertyValueTxt_.getText());
        property_.setValue(text);
        property_.setLanguage(languageTxt_.getText());
        property_.setOwner(ownerTxt_.getText());
        property_.setEffectiveDate(StringUtils.isNotEmpty(effDateTxt_.getText()) ? new Date(effDateTxt_.getText()) : null);
        property_.setExpirationDate(StringUtils.isNotEmpty(expDateTxt_.getText()) ? new Date(expDateTxt_.getText()) : null);
        property_.setSource(sourceList);
        property_.setUsageContext(contextList);        
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
}