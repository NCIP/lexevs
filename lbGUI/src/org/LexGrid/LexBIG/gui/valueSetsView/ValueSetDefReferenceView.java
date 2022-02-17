
package org.LexGrid.LexBIG.gui.valueSetsView;

import java.util.List;

import org.LexGrid.LexBIG.gui.DialogHandler;
import org.LexGrid.LexBIG.gui.LB_VSD_GUI;
import org.LexGrid.LexBIG.gui.ValueSetDefinitionDetails;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.ValueSetDefinitionReference;
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

public class ValueSetDefReferenceView {
    private Shell shell_;
    private DialogHandler errorHandler_;
    private Long ruleOrderValue_ = 0L;
    private DefinitionOperator defOpValue_ = DefinitionOperator.OR;
    private String valueSetDefURIValue_ = "";
    private ValueSetDefinitionReference vsdr_ = null;
    private boolean changesMade_ = false;
    private DefinitionEntry changedDefEntry_ = null;
    private ValueSetDefinitionDetails vsdDetails_ = null;
    
    public ValueSetDefReferenceView(LB_VSD_GUI lb_vsd_gui, ValueSetDefinitionDetails vsdDetails, Shell parent, ValueSetDefinition vd, final DefinitionEntry defEntry) {
        shell_ = new Shell(parent.getDisplay());
        shell_.setText("Value Set Definition Reference");
        GridLayout gridLayout = new GridLayout(3, true);
        gridLayout.numColumns = 3;
        shell_.setLayout(gridLayout);
        shell_.setSize(1200, 800);
        Rectangle bds = shell_.getDisplay().getBounds();
        Point p = shell_.getSize();
        int nLeft = (bds.width - p.x) / 2;
        int nTop = (bds.height - p.y) / 2;
        shell_.setBounds(nLeft, nTop, p.x, p.y);
        errorHandler_ = new DialogHandler(shell_);
        vsdDetails_ = vsdDetails;
        
        List<String> uris = lb_vsd_gui.getValueSetDefinitionService().listValueSetDefinitionURIs();
        
        if (defEntry != null)
        {
            vsdr_ = defEntry.getValueSetDefinitionReference();
        }
        
        if (vsdr_ != null)
        {
            ruleOrderValue_ = defEntry.getRuleOrder();
            defOpValue_ = defEntry.getOperator();
            valueSetDefURIValue_ = vsdr_.getValueSetDefinitionURI();
        }
        
        new Label(shell_, SWT.NONE).setText("Rule Order : ");        
        final Text ruleOrderText = new Text(shell_, SWT.SINGLE | SWT.BORDER);
        GridData gridData = new GridData(GridData.FILL, GridData.BEGINNING, true, false);
        gridData.horizontalSpan = 2;
        ruleOrderText.setLayoutData(gridData);
        ruleOrderText.setFocus();
        ruleOrderText.setText(ruleOrderValue_.toString());
        ruleOrderText.addListener(SWT.Modify, new Listener() {
            public void handleEvent(Event arg0) {
                if (!ruleOrderText.getText().equalsIgnoreCase(ruleOrderValue_.toString()))
                    setChangesMade(true);
            }
        });
        
        new Label(shell_, SWT.NONE).setText("Operator : ");        
        final Combo operatorCombo = new Combo(shell_, SWT.NONE);
        operatorCombo.setItems(new String[] { DefinitionOperator.OR.name(), DefinitionOperator.AND.name(), DefinitionOperator.SUBTRACT.name()});
        operatorCombo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        operatorCombo.setText(defOpValue_.name());
        gridData = new GridData(GridData.FILL, GridData.BEGINNING, true, false);
        gridData.horizontalSpan = 2;        
        operatorCombo.setLayoutData(gridData);
        operatorCombo.addListener(SWT.Modify, new Listener() {
            public void handleEvent(Event arg0) {
                if (!operatorCombo.getText().equalsIgnoreCase(defOpValue_.toString()))
                    setChangesMade(true);
            }
        });
        
        new Label(shell_, SWT.NONE).setText("Value Set Definition URI : ");
        
        final Combo valueSetDefURICombo = new Combo(shell_, SWT.SINGLE | SWT.BORDER);
        gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData.horizontalSpan = 2;
        valueSetDefURICombo.setLayoutData(gridData);
        valueSetDefURICombo.setFocus();
        valueSetDefURICombo.setText(valueSetDefURIValue_);
        valueSetDefURICombo.addListener(SWT.Modify, new Listener() {
            public void handleEvent(Event arg0) {
                if (!valueSetDefURICombo.getText().equalsIgnoreCase(valueSetDefURIValue_))
                    setChangesMade(true);
            }
        });
        if (uris != null)
            valueSetDefURICombo.setItems((String[])uris.toArray(new String[0]));
        
        if (StringUtils.isNotEmpty(valueSetDefURIValue_))
            valueSetDefURICombo.setText(valueSetDefURIValue_);
        
        
        Button cancelButton = new Button(shell_, SWT.PUSH);
        cancelButton.setText("CANCEL");
        gridData = new GridData(GridData.CENTER, GridData.END, false, false);
        gridData.verticalIndent = 25;
        gridData.horizontalIndent = 50;
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
        gridData = new GridData(GridData.CENTER, GridData.END, false, false);
        gridData.verticalIndent = 25;
        gridData.horizontalSpan = 1;
        okButton.setLayoutData(gridData);
        okButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                if (StringUtils.isEmpty(valueSetDefURICombo.getText()))
                {
                    errorHandler_.showError("Invalid data",
                    "Value Set Definition URI can not be empty.");
                    return;
                }
                
                if (StringUtils.isEmpty(ruleOrderText.getText()))
                {
                    errorHandler_.showError("Invalid data",
                    "Rule Order can not be empty.");
                    return;
                }
                if (StringUtils.isEmpty(operatorCombo.getText()))
                {
                    errorHandler_.showError("Invalid data",
                    "Operator can not be empty.");
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
                    changedDefEntry_ = new DefinitionEntry();
                    changedDefEntry_.setRuleOrder(Long.valueOf(ruleOrderText.getText()));
                    changedDefEntry_.setOperator(DefinitionOperator.valueOf(operatorCombo.getText()));
                    ValueSetDefinitionReference changedVSDRef = new ValueSetDefinitionReference();
                    changedVSDRef.setValueSetDefinitionURI(valueSetDefURICombo.getText());
                    changedDefEntry_.setValueSetDefinitionReference(changedVSDRef);
                    
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
        return changesMade_;
    }

    /**
     * @param changesMade the changesMade to set
     */
    public void setChangesMade(boolean changesMade) {
        this.changesMade_ = changesMade;
    }
}