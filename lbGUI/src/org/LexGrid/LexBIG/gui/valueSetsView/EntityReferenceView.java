
package org.LexGrid.LexBIG.gui.valueSetsView;

import org.LexGrid.LexBIG.gui.DialogHandler;
import org.LexGrid.LexBIG.gui.ValueSetDefinitionDetails;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.EntityReference;
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

public class EntityReferenceView {
    private Shell shell_;
    private DialogHandler errorHandler;
    private Long ruleOrderValue = 0L;
    private DefinitionOperator defOpValue = DefinitionOperator.OR;
    private String entityCodeValue = "";
    private String entityCodeNSValue = "";
    private String refAssociationValue = "";
    private boolean transitiveValue = true;
    private boolean targetToSourceValue = false;
    private boolean leafOnlyValue = false;
    private EntityReference er_ = null;
    private boolean changesMade = false;
    private ValueSetDefinitionDetails vsdDetails_ = null;
    private DefinitionEntry changedDefEntry_ = null;
    
    public EntityReferenceView(ValueSetDefinitionDetails vsdDetails, Shell parent, ValueSetDefinition vd, final DefinitionEntry defEntry) {
        shell_ = new Shell(parent.getDisplay());
        shell_.setText("Entity Reference");
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 4;
        shell_.setLayout(gridLayout);
        shell_.setSize(1200, 400);
        Rectangle bds = shell_.getDisplay().getBounds();
        Point p = shell_.getSize();
        int nLeft = (bds.width - p.x) / 2;
        int nTop = (bds.height - p.y) / 2;
        shell_.setBounds(nLeft, nTop, p.x, p.y);
        
        errorHandler = new DialogHandler(shell_);
        vsdDetails_ = vsdDetails;
        changedDefEntry_ = defEntry;
        
        if (defEntry != null)
        {
            er_ = defEntry.getEntityReference();
        }
        
        if (er_ != null)
        {
            ruleOrderValue = defEntry.getRuleOrder();
            defOpValue = defEntry.getOperator();
            entityCodeValue = er_.getEntityCode();
            entityCodeNSValue = er_.getEntityCodeNamespace() == null ? "" : er_.getEntityCodeNamespace();
            refAssociationValue = er_.getReferenceAssociation() == null ? "" : er_.getReferenceAssociation();
            transitiveValue = er_.getTransitiveClosure();
            targetToSourceValue = er_.getTargetToSource();
            leafOnlyValue = er_.getLeafOnly();
        }
        
        new Label(shell_, SWT.NONE).setText("Rule Order : ");        
        final Text ruleOrder = new Text(shell_, SWT.SINGLE | SWT.BORDER);
        GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData.horizontalSpan = 3;
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
        operator.setText(defOpValue.name());
        gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData.horizontalSpan = 3;        
        operator.setLayoutData(gridData);
        operator.addListener(SWT.Modify, new Listener() {
            public void handleEvent(Event arg0) {
                if (!operator.getText().equalsIgnoreCase(defOpValue.toString()))
                    setChangesMade(true);
            }
        });
        
        new Label(shell_, SWT.NONE).setText("Entity Code : ");
        
        final Text entityCode = new Text(shell_, SWT.SINGLE | SWT.BORDER);
        gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData.horizontalSpan = 3;
        entityCode.setLayoutData(gridData);
        entityCode.setFocus();
        entityCode.setText(entityCodeValue);
        entityCode.addListener(SWT.Modify, new Listener() {
            public void handleEvent(Event arg0) {
                if (!entityCode.getText().equalsIgnoreCase(entityCodeValue))
                    setChangesMade(true);
            }
        });
        
        new Label(shell_, SWT.NONE).setText("Entity Code Namespace : ");
        final Text entityCodeNS = new Text(shell_, SWT.SINGLE | SWT.BORDER);
        gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData.horizontalSpan = 3;
        entityCodeNS.setLayoutData(gridData);
        entityCodeNS.setText(entityCodeNSValue);
        entityCodeNS.addListener(SWT.Modify, new Listener() {
            public void handleEvent(Event arg0) {
                if (!entityCodeNS.getText().equalsIgnoreCase(entityCodeNSValue))
                    setChangesMade(true);
            }
        });
        
        new Label(shell_, SWT.NONE).setText("Reference Association : ");
        final Text refAssn = new Text(shell_, SWT.SINGLE | SWT.BORDER);
        gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData.horizontalSpan = 3;
        refAssn.setLayoutData(gridData);
        refAssn.setText(refAssociationValue);
        refAssn.addListener(SWT.Modify, new Listener() {
            public void handleEvent(Event arg0) {
                if (!refAssn.getText().equalsIgnoreCase(refAssociationValue))
                    setChangesMade(true);
            }
        });
        
        new Label(shell_, SWT.NONE).setText("Transitive Closure : ");        
        final Combo transitive = new Combo(shell_, SWT.NONE);
        transitive.setItems(new String[] { Boolean.TRUE.toString() , Boolean.FALSE.toString() });
        transitive.setText(String.valueOf(transitiveValue));
        gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData.horizontalSpan = 3;        
        transitive.setLayoutData(gridData);
        transitive.addListener(SWT.Modify, new Listener() {
            public void handleEvent(Event arg0) {
                if (!transitive.getText().equalsIgnoreCase(String.valueOf(transitiveValue)))
                    setChangesMade(true);
            }
        });
        
        new Label(shell_, SWT.NONE).setText("Target To Source : ");        
        final Combo targetToSource = new Combo(shell_, SWT.NONE);
        targetToSource.setItems(new String[] { Boolean.TRUE.toString() , Boolean.FALSE.toString() });
        targetToSource.setText(String.valueOf(targetToSourceValue));
        gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData.horizontalSpan = 3;        
        targetToSource.setLayoutData(gridData);
        targetToSource.addListener(SWT.Modify, new Listener() {
            public void handleEvent(Event arg0) {
                if (!targetToSource.getText().equalsIgnoreCase(String.valueOf(targetToSourceValue)))
                    setChangesMade(true);
            }
        });
        
        new Label(shell_, SWT.NONE).setText("Leaf Only : ");        
        final Combo leafOnly = new Combo(shell_, SWT.NONE);
        leafOnly.setItems(new String[] { Boolean.TRUE.toString() , Boolean.FALSE.toString() });
        leafOnly.setText(String.valueOf(leafOnlyValue));
        gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData.horizontalSpan = 3;        
        leafOnly.setLayoutData(gridData);
        leafOnly.addListener(SWT.Modify, new Listener() {
            public void handleEvent(Event arg0) {
                if (!leafOnly.getText().equalsIgnoreCase(String.valueOf(leafOnlyValue)))
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
                if (StringUtils.isEmpty(ruleOrder.getText()))
                {
                    errorHandler.showError("Invalid data",
                    "Rule order can not be empty.");
                    return;
                }
                if (StringUtils.isEmpty(operator.getText()))
                {
                    errorHandler.showError("Invalid data",
                    "Operator can not be empty.");
                    return;
                }
                if (StringUtils.isEmpty(entityCode.getText()))
                {
                    errorHandler.showError("Invalid data",
                    "Entity Code can not be empty.");
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
                    EntityReference changedEntityRef = new EntityReference();
                    changedEntityRef.setEntityCode(entityCode.getText());
                    changedEntityRef.setEntityCodeNamespace(entityCodeNS.getText());
                    changedEntityRef.setReferenceAssociation(refAssn.getText());                        
                    changedEntityRef.setLeafOnly(Boolean.valueOf(leafOnly.getText()));
                    changedEntityRef.setTargetToSource(Boolean.valueOf(targetToSource.getText()));
                    changedEntityRef.setTransitiveClosure(Boolean.valueOf(transitive.getText()));
                    
                    changedDefEntry_.setEntityReference(changedEntityRef);
                    
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