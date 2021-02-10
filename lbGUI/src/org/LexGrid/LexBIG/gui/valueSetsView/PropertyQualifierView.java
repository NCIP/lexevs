
package org.LexGrid.LexBIG.gui.valueSetsView;

import org.LexGrid.LexBIG.gui.DialogHandler;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class PropertyQualifierView {
    private Shell shell_;
    private DialogHandler errorHandler;
    private String propertyName = "";
    private String propertyType = "";
    private String propertyValue = "";
    private boolean changesMade = false;
    private PropertyView propertyView_;
    private PropertyQualifier propertyQualifier_;
    
    public PropertyQualifierView(PropertyView propertyView, Shell parent, final PropertyQualifier pq) {
        shell_ = new Shell(parent.getDisplay());
        shell_.setText("Property Qualifier");
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        shell_.setLayout(gridLayout);
        shell_.setSize(600, 400);
        errorHandler = new DialogHandler(shell_);
        propertyView_ = propertyView;
        propertyQualifier_ = pq;
        
        if (pq != null)
        {
            propertyName = pq.getPropertyQualifierName();
            propertyType = pq.getPropertyQualifierType() == null ? "" : pq.getPropertyQualifierType();
            propertyValue = pq.getValue() == null ? "" : pq.getValue().getContent() == null ? "" : pq.getValue().getContent();
        }
        
        new Label(shell_, SWT.NONE).setText("Property Qualifier Name : ");        
        final Text pQualNameTxt = new Text(shell_, SWT.SINGLE | SWT.BORDER);
        GridData gridData = new GridData(GridData.FILL, GridData.BEGINNING, true, false);
        gridData.horizontalSpan = 2;
        pQualNameTxt.setLayoutData(gridData);
        pQualNameTxt.setFocus();
        pQualNameTxt.setText(propertyName);
        pQualNameTxt.addListener(SWT.Verify, new Listener() {            
            public void handleEvent(Event arg0) {
                if (pQualNameTxt.getText().equalsIgnoreCase(propertyName))
                    setChangesMade(true);
            }
        });
        
        new Label(shell_, SWT.NONE).setText("Property Qualifier Type : ");        
        final Text pQualTypeTxt = new Text(shell_, SWT.SINGLE | SWT.BORDER);
        gridData = new GridData(GridData.FILL, GridData.BEGINNING, true, false);
        gridData.horizontalSpan = 2;
        pQualTypeTxt.setLayoutData(gridData);
        pQualTypeTxt.setText(propertyType);
        pQualTypeTxt.addListener(SWT.Verify, new Listener() {            
            public void handleEvent(Event arg0) {
                if (pQualTypeTxt.getText().equalsIgnoreCase(propertyType))
                    setChangesMade(true);
            }
        });
        
        new Label(shell_, SWT.NONE).setText("Property Qualifier Value : ");
        
        final Text pQualValueTxt = new Text(shell_, SWT.SINGLE | SWT.BORDER);
        gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData.horizontalSpan = 2;
        pQualValueTxt.setLayoutData(gridData);
        pQualValueTxt.setText(propertyValue);
        pQualValueTxt.addListener(SWT.Verify, new Listener() {            
            public void handleEvent(Event arg0) {
                if (pQualValueTxt.getText().equalsIgnoreCase(propertyValue))
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
                if (StringUtils.isEmpty(pQualNameTxt.getText()))
                {
                    errorHandler.showError("Invalid data",
                        "Property Qualifier Name can not be empty.");
                    return;
                }
                propertyQualifier_ = new PropertyQualifier();
                propertyQualifier_.setPropertyQualifierName(pQualNameTxt.getText());
                propertyQualifier_.setPropertyQualifierType(pQualTypeTxt.getText());
                org.LexGrid.commonTypes.Text text = new org.LexGrid.commonTypes.Text();
                text.setContent(pQualValueTxt.getText());
                propertyQualifier_.setValue(text);
                
                if (pq == null) // Adding new property qualifier
                {
                    propertyView_.addPropertyQualifier(propertyQualifier_);
                }
                else
                {
                    propertyView_.updatePropertyQualifier(pq, propertyQualifier_);
                }
                propertyView_.refreshPropertyQualList();
                propertyView_.setChangesMade(true);
                
                shell_.close();
                shell_.dispose();
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