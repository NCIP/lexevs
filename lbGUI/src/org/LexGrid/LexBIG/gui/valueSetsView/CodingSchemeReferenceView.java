
package org.LexGrid.LexBIG.gui.valueSetsView;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.gui.DialogHandler;
import org.LexGrid.LexBIG.gui.LB_VSD_GUI;
import org.LexGrid.LexBIG.gui.ValueSetDefinitionDetails;
import org.LexGrid.valueSets.CodingSchemeReference;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.types.DefinitionOperator;
import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class CodingSchemeReferenceView {
    private Shell shell_;
    private DialogHandler errorHandler_;
    private Long ruleOrderValue = 0L;
    private DefinitionOperator defOpValue = DefinitionOperator.OR;
    private String codingSchemeValue = "";
    private CodingSchemeReference csr = null;
    private boolean changesMade = false;
    private Combo csCombo_, operator_;
    private Text ruleOrder_;
    private DefinitionEntry defEntry_;
    private ValueSetDefinitionDetails vsdDetails_;
    
    public CodingSchemeReferenceView(LB_VSD_GUI lb_vsd_gui, ValueSetDefinitionDetails vsdDetails, Shell parent, ValueSetDefinition vd, final DefinitionEntry defEntry) {
        shell_ = new Shell(parent.getDisplay());
        shell_.setText("Coding Scheme Reference");
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        shell_.setLayout(gridLayout);
        shell_.setSize(1400, 800);
        Rectangle bds = shell_.getDisplay().getBounds();
        Point p = shell_.getSize();
        int nLeft = (bds.width - p.x) / 2;
        int nTop = (bds.height - p.y) / 2;
        shell_.setBounds(nLeft, nTop, p.x, p.y);
        errorHandler_ = new DialogHandler(shell_);
        defEntry_ = defEntry;
        vsdDetails_ = vsdDetails;
        
        if (defEntry != null)
        {
            csr = defEntry.getCodingSchemeReference();
        }
        
        if (csr != null)
        {
            ruleOrderValue = defEntry.getRuleOrder();
            defOpValue = defEntry.getOperator();
            codingSchemeValue = csr.getCodingScheme();
        }
        
        new Label(shell_, SWT.NONE).setText("Rule Order : ");        
        ruleOrder_ = new Text(shell_, SWT.SINGLE | SWT.BORDER);
        GridData gridData = new GridData(GridData.FILL, GridData.BEGINNING, true, false);
        gridData.horizontalSpan = 2;
        ruleOrder_.setLayoutData(gridData);
        ruleOrder_.setFocus();
        ruleOrder_.setText(ruleOrderValue.toString());
        ruleOrder_.addListener(SWT.Modify, new Listener() {
            public void handleEvent(Event arg0) {
                if (!ruleOrder_.getText().equalsIgnoreCase(ruleOrderValue.toString()))
                    setChangesMade(true);
            }
        });
        
        new Label(shell_, SWT.NONE).setText("Operator : ");        
        operator_ = new Combo(shell_, SWT.NONE);
        operator_.setItems(new String[] { DefinitionOperator.OR.name(), DefinitionOperator.AND.name(), DefinitionOperator.SUBTRACT.name()});
        operator_.setText(defOpValue.name());
        gridData = new GridData(GridData.FILL, GridData.BEGINNING, true, false);
        gridData.horizontalSpan = 2;        
        operator_.setLayoutData(gridData);
        operator_.addListener(SWT.Modify, new Listener() {
            public void handleEvent(Event arg0) {
                if (!operator_.getText().equalsIgnoreCase(defOpValue.toString()))
                    setChangesMade(true);
            }
        });
        
        new Label(shell_, SWT.NONE).setText("Coding Scheme : ");
        
        csCombo_ = new Combo(shell_, SWT.BORDER | SWT.FILL);
        csCombo_.setText(codingSchemeValue);
        try {
            if (lb_vsd_gui.getLbs() != null && lb_vsd_gui.getLbs().getSupportedCodingSchemes() != null)
            {
                CodingSchemeRenderingList csrList = lb_vsd_gui.getLbs().getSupportedCodingSchemes();
            
                if (csrList != null)
                {
                    for (int i = 0; i < csrList.getCodingSchemeRenderingCount(); i++)
                    {
                        CodingSchemeRendering csr = csrList.getCodingSchemeRendering(i);
                        if (StringUtils.isNotBlank(csr.getCodingSchemeSummary().getLocalName()))
                            csCombo_.add(csr.getCodingSchemeSummary().getLocalName());
                    }
                }
            }
        } catch (LBInvocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;    
        gridData.horizontalSpan = 2;
        csCombo_.setLayoutData(gridData);
        csCombo_.addListener(SWT.Selection, new Listener() {
            
            @Override
            public void handleEvent(Event arg0) {
                String csName = csCombo_.getText();
                
                if (StringUtils.isNotEmpty(csName))
                {
                    if (!csName.equalsIgnoreCase(codingSchemeValue))
                        setChangesMade(true);
                }
                
            }
        });
        
        Button cancelButton = new Button(shell_, SWT.PUSH);
        cancelButton.setText("CANCEL");
        gridData = new GridData(GridData.END, GridData.END, false, false);
        gridData.horizontalSpan = 1;
        gridData.horizontalIndent = 1;
        cancelButton.setLayoutData(gridData);
        cancelButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                shell_.close();
                shell_.dispose();
            }
        });
        
        Button okButton = new Button(shell_, SWT.PUSH);
        okButton.setText("ADD");
        gridData = new GridData(GridData.END, GridData.END, false, false);
        gridData.horizontalSpan = 1;
        okButton.setLayoutData(gridData);
        okButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event){
                if (StringUtils.isEmpty(operator_.getText()))
                {
                    errorHandler_.showError("Invalid data",
                    "Operator can not be empty.");
                    return;
                }
                if (StringUtils.isEmpty(ruleOrder_.getText()))
                {
                    errorHandler_.showError("Invalid data",
                    "Rule Order can not be empty.");
                    return;
                }
                if (StringUtils.isEmpty(csCombo_.getText()))
                {
                    errorHandler_.showError("Invalid data",
                    "Coding Scheme can not be empty.");
                    return;
                }
                if (!isChangesMade())
                {
                    errorHandler_.showInfo("No Changes", "No changes have been made");
                    shell_.close();
                    shell_.dispose();
                }
                else
                {   
                    if (defEntry_ == null)
                            defEntry_ = new DefinitionEntry();
                    defEntry_.setOperator(DefinitionOperator.valueOf(operator_.getText()));
                    defEntry_.setRuleOrder(Long.valueOf(ruleOrder_.getText()));
                    CodingSchemeReference csRef = new CodingSchemeReference();
                    csRef.setCodingScheme(csCombo_.getText());
                    defEntry_.setCodingSchemeReference(csRef);
                    if (defEntry == null) // Adding new definition entry
                    {
                        vsdDetails_.addDefinitionEntry(defEntry_);
                    }
                    else // updating definitionEntry
                    {
                        vsdDetails_.updateDefinitionEntry(defEntry, defEntry_);
                    }
                    
                    vsdDetails_.setChangesMade(true);
                    vsdDetails_.refreshDefinitionEntryList();
                    
                    shell_.close();
                    shell_.dispose();
                }
            }
        });
        
        shell_.addDisposeListener(new DisposeListener() {
            
            public void widgetDisposed(DisposeEvent arg0) {
                // TODO Auto-generated method stub
                
            }
        });
        
        shell_.pack();
        shell_.open();
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
}