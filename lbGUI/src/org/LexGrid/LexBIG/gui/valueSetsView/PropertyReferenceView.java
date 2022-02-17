
package org.LexGrid.LexBIG.gui.valueSetsView;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.gui.DialogHandler;
import org.LexGrid.LexBIG.gui.LB_VSD_GUI;
import org.LexGrid.LexBIG.gui.ValueSetDefinitionDetails;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.PropertyMatchValue;
import org.LexGrid.valueSets.PropertyReference;
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

public class PropertyReferenceView {
    private Shell shell_;
    private DialogHandler errorHandler;
    private Long ruleOrderValue = 0L;
    private DefinitionOperator defOpValue = DefinitionOperator.OR;
    private String codingSchemeValue = "";
    private String propertyNameValue = "";
    private String propertyMatchValueValue = "";
    private String matchAlgorithmValue = "";
    private PropertyReference pr = null;
    private boolean changesMade = false;
    private DefinitionEntry changedDefEntry_ = null;
    private ValueSetDefinitionDetails vsdDetails_ = null;
    
    public PropertyReferenceView(LB_VSD_GUI lb_vsd_gui, ValueSetDefinitionDetails vsdDetails, Shell parent, ValueSetDefinition vd, final DefinitionEntry defEntry) {
        shell_ = new Shell(parent.getDisplay());
        shell_.setText("Property Reference");
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        shell_.setLayout(gridLayout);
        shell_.setSize(1200, 800);
        Rectangle bds = shell_.getDisplay().getBounds();
        Point p = shell_.getSize();
        int nLeft = (bds.width - p.x) / 2;
        int nTop = (bds.height - p.y) / 2;
        shell_.setBounds(nLeft, nTop, p.x, p.y);
        errorHandler = new DialogHandler(shell_);
        vsdDetails_ = vsdDetails;
        
        if (defEntry != null)
        {
            pr = defEntry.getPropertyReference();
        }
        
        if (pr != null)
        {
            ruleOrderValue = defEntry.getRuleOrder();
            defOpValue = defEntry.getOperator();
            codingSchemeValue = pr.getCodingScheme();
            propertyNameValue = pr.getPropertyName() == null ? "" : pr.getPropertyName();
            PropertyMatchValue pmv = pr.getPropertyMatchValue();
            if (pmv != null)
            {
                propertyMatchValueValue = pmv.getContent() == null ? "" : pmv.getContent();
                matchAlgorithmValue = pmv.getMatchAlgorithm() == null ? "" : pmv.getMatchAlgorithm();
            }
        }
        
        new Label(shell_, SWT.NONE).setText("Rule Order : ");        
        final Text ruleOrder = new Text(shell_, SWT.SINGLE | SWT.BORDER);
        GridData gridData = new GridData(GridData.FILL, GridData.BEGINNING, true, false);
        gridData.horizontalSpan = 2;
        ruleOrder.setLayoutData(gridData);
        ruleOrder.setFocus();
        ruleOrder.setText(ruleOrderValue.toString());
        ruleOrder.addListener(SWT.Modify, new Listener() {
            public void handleEvent(Event arg0) {
                if (!ruleOrder.getText().equalsIgnoreCase(ruleOrderValue.toString()))
                    setChangesMade(true);
            }
        });
        
        new Label(shell_, SWT.NONE).setText("Operator : ");        
        final Combo operator = new Combo(shell_, SWT.NONE);
        operator.setItems(new String[] { DefinitionOperator.OR.name(), DefinitionOperator.AND.name(), DefinitionOperator.SUBTRACT.name()});
        operator.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        operator.setText(defOpValue.name());
        gridData = new GridData(GridData.FILL, GridData.BEGINNING, true, false);
        gridData.horizontalSpan = 2;        
        operator.setLayoutData(gridData);
        operator.addListener(SWT.Modify, new Listener() {
            public void handleEvent(Event arg0) {
                if (!operator.getText().equalsIgnoreCase(defOpValue.toString()))
                    setChangesMade(true);
            }
        });
        
        new Label(shell_, SWT.NONE).setText("Coding Scheme : ");
        
        final Combo csCombo = new Combo(shell_, SWT.BORDER | SWT.FILL);
        csCombo.setText(codingSchemeValue);
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
                            csCombo.add(csr.getCodingSchemeSummary().getLocalName());
                    }
                }
            }
        } catch (LBInvocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        csCombo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;    
        gridData.horizontalSpan = 2;
        csCombo.setLayoutData(gridData);
        csCombo.addListener(SWT.Selection, new Listener() {
            
            @Override
            public void handleEvent(Event arg0) {
                String csName = csCombo.getText();
                
                if (StringUtils.isNotEmpty(csName))
                {
                    if (!csName.equalsIgnoreCase(codingSchemeValue))
                        setChangesMade(true);
                }
                
            }
        });
        
        new Label(shell_, SWT.NONE).setText("Property Name : ");
        final Text propNameText = new Text(shell_, SWT.SINGLE | SWT.BORDER);
        gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData.horizontalSpan = 2;
        propNameText.setLayoutData(gridData);
        propNameText.setText(propertyNameValue);
        propNameText.addListener(SWT.Modify, new Listener() {
            public void handleEvent(Event arg0) {
                if (!propNameText.getText().equalsIgnoreCase(propertyNameValue))
                    setChangesMade(true);
            }
        });
        
        new Label(shell_, SWT.NONE).setText("Property Match Value : ");
        final Text propValueText = new Text(shell_, SWT.SINGLE | SWT.BORDER);
        gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData.horizontalSpan = 2;
        propValueText.setLayoutData(gridData);
        propValueText.setText(propertyMatchValueValue);
        propValueText.addListener(SWT.Modify, new Listener() {
            public void handleEvent(Event arg0) {
                if (!propValueText.getText().equalsIgnoreCase(propertyMatchValueValue))
                    setChangesMade(true);
            }
        });
        
        new Label(shell_, SWT.NONE).setText("Match Algorithm : ");      
        final Combo matchAlgorithmCombo = new Combo(shell_, SWT.DROP_DOWN | SWT.BORDER);
        gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData.horizontalSpan = 2;
        matchAlgorithmCombo.setLayoutData(gridData);
        matchAlgorithmCombo.setItems(lb_vsd_gui.getSupportedMatchAlgorithms());
        if (StringUtils.isNotEmpty(matchAlgorithmValue))
            matchAlgorithmCombo.setText(matchAlgorithmValue);
        else
            matchAlgorithmCombo.select(0);
        matchAlgorithmCombo.addListener(SWT.Modify, new Listener() {
            public void handleEvent(Event arg0) {
                if (!matchAlgorithmCombo.getText().equalsIgnoreCase(matchAlgorithmValue))
                    setChangesMade(true);
            }
        });
        
        Button cancelButton = new Button(shell_, SWT.PUSH);
        cancelButton.setText("CANCEL");
        gridData = new GridData(GridData.END, GridData.CENTER, false, false);
        gridData.horizontalSpan = 1;
        cancelButton.setLayoutData(gridData);
        cancelButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                shell_.close();
                shell_.dispose();
            }
        });
        
        Button okButton = new Button(shell_, SWT.PUSH);
        okButton.setText("ADD");
        gridData = new GridData(GridData.END, GridData.CENTER, false, false);
        gridData.horizontalSpan = 1;
        okButton.setLayoutData(gridData);
        okButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                if (StringUtils.isEmpty(operator.getText()))
                {
                    errorHandler.showError("Invalid data",
                    "Operator can not be empty.");
                    return;
                }
                
                if (StringUtils.isEmpty(ruleOrder.getText()))
                {
                    errorHandler.showError("Invalid data",
                    "Rule Order can not be empty.");
                    return;
                }
                if (StringUtils.isEmpty(csCombo.getText()))
                {
                    errorHandler.showError("Invalid data",
                    "Coding Scheme can not be empty.");
                    return;
                }
                
                if (!isChangesMade())
                {
                    errorHandler.showInfo("No Changes", "No changes have been made");
                    shell_.close();
                    shell_.dispose();
                }
                else
                {  
                    changedDefEntry_ = new DefinitionEntry();
                    changedDefEntry_.setRuleOrder(Long.valueOf(ruleOrder.getText()));
                    changedDefEntry_.setOperator(DefinitionOperator.valueOf(operator.getText()));
                    PropertyReference changedPropertyRef = new PropertyReference();
                    changedPropertyRef.setCodingScheme(csCombo.getText());
                    changedPropertyRef.setPropertyName(propNameText.getText());
                    PropertyMatchValue pmv = new PropertyMatchValue();
                    pmv.setMatchAlgorithm(matchAlgorithmCombo.getText());
                    pmv.setContent(propValueText.getText());
                    changedPropertyRef.setPropertyMatchValue(pmv);
                    
                    changedDefEntry_.setPropertyReference(changedPropertyRef);
                    
                    if (defEntry == null) // Adding new definition entry
                    { 
                        vsdDetails_.addDefinitionEntry(changedDefEntry_);
                    }
                    else // updating definition entry
                    {
                        vsdDetails_.updateDefinitionEntry(defEntry, changedDefEntry_);
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
    public boolean changesMade() {
        return false;
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