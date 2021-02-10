
package org.LexGrid.LexBIG.gui.valueSetsView;

import org.LexGrid.LexBIG.gui.LB_VSD_GUI;
import org.LexGrid.LexBIG.gui.displayResults.VDDisplayFilterResult;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ValueSetDefConceptDomainFilter {
    private Shell shell_;
    private LB_VSD_GUI lb_vsd_gui_;
    
    public ValueSetDefConceptDomainFilter(LB_VSD_GUI lb_vsd_gui, Shell parent) {
        shell_ = new Shell(parent.getDisplay());
        shell_.setText("Concept Domain Filter");
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 6;
        shell_.setLayout(gridLayout);
        shell_.setSize(1600, 800);
        
        Rectangle bds = shell_.getDisplay().getBounds();

        Point p = shell_.getSize();

        int nLeft = (bds.width - p.x) / 2;
        int nTop = (bds.height - p.y) / 2;

        shell_.setBounds(nLeft, nTop, p.x, p.y);
        
        lb_vsd_gui_ = lb_vsd_gui;
        
        Label label = new Label(shell_, SWT.NONE);
        label.setText("Concept Domain : ");
        GridData gridData = new GridData(GridData.FILL, GridData.BEGINNING, true, true);
        gridData.horizontalSpan = 1;
        gridData.verticalSpan = 4;
        label.setLayoutData(gridData);
        
        final Text conceptDomainText = new Text(shell_, SWT.SINGLE | SWT.BORDER);
        gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
        gridData.horizontalSpan = 5;
        gridData.verticalSpan = 4;
        conceptDomainText.setLayoutData(gridData);
        conceptDomainText.setFocus();
        
        Button cancelButton = new Button(shell_, SWT.PUSH);
        cancelButton.setText("CANCEL");
        gridData = new GridData(GridData.END, GridData.CENTER, false, false);
        gridData.verticalIndent = 25;
        gridData.horizontalSpan = 2;
        cancelButton.setLayoutData(gridData);
        cancelButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                shell_.close();
                shell_.dispose();
            }
        });
        
        Button filterButton = new Button(shell_, SWT.PUSH);
        filterButton.setText("Filter");
        gridData = new GridData(GridData.END, GridData.CENTER, false, false);
        gridData.verticalIndent = 25;
        gridData.horizontalSpan = 2;
        filterButton.setLayoutData(gridData);
        filterButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                displayResult(conceptDomainText.getText());
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
    
    private void displayResult(String conceptDomain){
        java.util.List<String> uris = lb_vsd_gui_.getValueSetDefinitionService().getValueSetDefinitionURIsWithConceptDomain(conceptDomain, null);
        new VDDisplayFilterResult(lb_vsd_gui_, uris);
    }
}