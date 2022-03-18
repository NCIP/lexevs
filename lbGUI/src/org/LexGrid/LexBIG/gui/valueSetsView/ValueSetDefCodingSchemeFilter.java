
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

public class ValueSetDefCodingSchemeFilter {
    private Shell shell_;
    private LB_VSD_GUI lb_vsd_gui_;
    
    public ValueSetDefCodingSchemeFilter(LB_VSD_GUI lb_vsd_gui, Shell parent) {
        shell_ = new Shell(parent.getDisplay());
        shell_.setText("Coding Scheme Filter");
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 5;
        shell_.setLayout(gridLayout);
        shell_.setSize(800, 600);
        Rectangle bds = shell_.getDisplay().getBounds();

        Point p = shell_.getSize();

        int nLeft = (bds.width - p.x) / 2;
        int nTop = (bds.height - p.y) / 2;

        shell_.setBounds(nLeft, nTop, p.x, p.y);
        
        lb_vsd_gui_ = lb_vsd_gui;
        
        new Label(shell_, SWT.NONE).setText("Coding Scheme : ");
        
        final Text codingSchemeText = new Text(shell_, SWT.SINGLE | SWT.BORDER);
        GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData.horizontalSpan = 5;
        gridData.verticalSpan = 4;
        codingSchemeText.setLayoutData(gridData);
        codingSchemeText.setFocus();
        
        Button cancelButton = new Button(shell_, SWT.PUSH);
        cancelButton.setText("CANCEL");
        gridData = new GridData(GridData.END, GridData.CENTER, false, false);
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
        gridData.horizontalSpan = 1;
        filterButton.setLayoutData(gridData);
        filterButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                displayResult(codingSchemeText.getText());
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
    
    private void displayResult(String codingScheme){
        java.util.List<String> uris = lb_vsd_gui_.getValueSetDefinitionService().getValueSetDefinitionURIsWithCodingScheme(codingScheme, null);
        new VDDisplayFilterResult(lb_vsd_gui_, uris);
    }
}