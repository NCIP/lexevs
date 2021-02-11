
package org.LexGrid.LexBIG.gui.valueSetsView;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.gui.DialogHandler;
import org.LexGrid.LexBIG.gui.LB_VSD_GUI;
import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

public class ValueSetDefIsSubSet {
    private Shell shell_;
    private LB_VSD_GUI lb_vsd_gui_;
    private DialogHandler errorHandler;
    
    public ValueSetDefIsSubSet(LB_VSD_GUI lb_vsd_gui, String childVSDURI, Shell parent) {
        shell_ = new Shell(parent.getDisplay());
        shell_.setText("Is SubSet");
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 6;
        shell_.setLayout(gridLayout);
        shell_.setSize(1800, 1000);
        Monitor primary = parent.getDisplay().getPrimaryMonitor ();
        Rectangle bounds = primary.getBounds ();
        Rectangle rect = shell_.getBounds ();
        int x = bounds.x + (bounds.width - rect.width) / 2;
        int y = bounds.y + (bounds.height - rect.height) / 2;
        shell_.setLocation (x, y);
        errorHandler = new DialogHandler(shell_);
        lb_vsd_gui_ = lb_vsd_gui;
        
        List<String> uris = lb_vsd_gui_.getValueSetDefinitionService().listValueSetDefinitionURIs();
        
        Label childLabel = new Label(shell_, SWT.NONE);
        childLabel.setText("Child ValueSetDefinition URI : ");
        GridData gridData = new GridData(GridData.FILL, GridData.BEGINNING, true, false);
        gridData.verticalIndent = 25;
        childLabel.setLayoutData(gridData);
        
        final Combo childVSDURICombo = new Combo(shell_, SWT.DROP_DOWN | SWT.BORDER);
        gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData.horizontalSpan = 2;
        gridData.verticalIndent = 25;
        childVSDURICombo.setLayoutData(gridData);
        if (uris != null)
            childVSDURICombo.setItems((String[])uris.toArray(new String[0]));
        
        if (StringUtils.isNotEmpty(childVSDURI))
        {
            childVSDURICombo.setText(childVSDURI);
        }
        
        childVSDURICombo.addKeyListener(new KeyListener() {
            String selectedItem = "";
            public void keyPressed(KeyEvent e) {
                if(childVSDURICombo.getText( ).length( ) > 0)
                {
                    return;
                }
                String key = Character.toString(e.character);
                String[] items = childVSDURICombo.getItems( );
                for(int i =0;i<items.length;i++)
                {
                    if(items[i].toLowerCase( ).startsWith(key.toLowerCase( )))
                    {
                        childVSDURICombo.select(i);
                        selectedItem = items[i];
                        return;
                    }
                }
            }
            public void keyReleased(KeyEvent e) {
                if(selectedItem.length( ) > 0)
                    childVSDURICombo.setText(selectedItem);
                selectedItem = "";
            }
        }); 
        
        Label parentLabel = new Label(shell_, SWT.NONE);
        parentLabel.setText("Parent ValueSetDefinition URI : ");
        gridData = new GridData(GridData.FILL, GridData.BEGINNING, true, false);
        gridData.verticalIndent = 25;
        parentLabel.setLayoutData(gridData);
        
        final Combo parentVSDURICombo = new Combo(shell_, SWT.DROP_DOWN | SWT.BORDER);
        gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData.horizontalSpan = 2;
        gridData.verticalIndent = 25;
        parentVSDURICombo.setLayoutData(gridData);
        
        if (uris != null)
            parentVSDURICombo.setItems((String[])uris.toArray(new String[0]));
        
        parentVSDURICombo.addKeyListener(new KeyListener() {
            String selectedItem = "";
            public void keyPressed(KeyEvent e) {
                if(parentVSDURICombo.getText( ).length( ) > 0)
                {
                    return;
                }
                String key = Character.toString(e.character);
                String[] items = parentVSDURICombo.getItems( );
                for(int i =0;i<items.length;i++)
                {
                    if(items[i].toLowerCase( ).startsWith(key.toLowerCase( )))
                    {
                        parentVSDURICombo.select(i);
                        selectedItem = items[i];
                        return;
                    }
                }
            }
            public void keyReleased(KeyEvent e) {
                if(selectedItem.length( ) > 0)
                    parentVSDURICombo.setText(selectedItem);
                selectedItem = "";
            }
        }); 
        
        CodingSchemeRenderingList csrList = null;
        try {
            csrList = lb_vsd_gui_.getLbs().getSupportedCodingSchemes();
        } catch (LBInvocationException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        Label csrLabel = new Label(shell_, SWT.CENTER);
        csrLabel.setText("Select Coding Scheme And Version : ");
        gridData = new GridData(GridData.FILL, GridData.END, true, false);
        gridData.horizontalSpan = 2;
        gridData.horizontalIndent = 3;
        gridData.verticalSpan = 2;
        gridData.verticalIndent = 25;
        csrLabel.setLayoutData(gridData);
        
        final Combo csrCombo = new Combo(shell_, SWT.DROP_DOWN | SWT.BORDER);
        gridData = new GridData(GridData.FILL, GridData.BEGINNING, true, false);
        gridData.horizontalSpan = 3;
        gridData.verticalSpan = 2;
        gridData.verticalIndent = 25;
        csrCombo.setLayoutData(gridData);
        
        if (csrList != null)
        {
            for (int i = 0; i < csrList.getCodingSchemeRenderingCount(); i++)
            {
                CodingSchemeRendering csr = csrList.getCodingSchemeRendering(i);
                if (csr.getCodingSchemeSummary() != null)
                    csrCombo.add(csr.getCodingSchemeSummary().getFormalName() + ":" + csr.getCodingSchemeSummary().getRepresentsVersion());
            }
        }
        
        Button filterButton = new Button(shell_, SWT.PUSH);
        filterButton.setText("Check");
        gridData = new GridData(GridData.CENTER, GridData.END, true, true);
        gridData.horizontalSpan = 6;
        gridData.verticalIndent = 25;
        filterButton.setLayoutData(gridData);
        filterButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                try {
                    if (StringUtils.isEmpty(childVSDURICombo.getText()))
                    {
                        errorHandler.showError("Invalid Data",
                                "Child Value Set Definition URI can not empty");
                            return;
                    }
                    if (StringUtils.isEmpty(parentVSDURICombo.getText()))
                    {
                        errorHandler.showError("Invalid Data",
                                "Parent Value Set Definition URI can not empty");
                            return;
                    }
                    
                    AbsoluteCodingSchemeVersionReferenceList acsvrList = new AbsoluteCodingSchemeVersionReferenceList();
                    if (StringUtils.isNotEmpty(csrCombo.getText()))
                    {
                        String[] csrString = csrCombo.getText().toString().split(":");
                        String csName = csrString[0];                        
                        String csVersion = csrString[1];
                        
                        AbsoluteCodingSchemeVersionReference acsvr = new AbsoluteCodingSchemeVersionReference();
                        acsvr.setCodingSchemeURN(csName);
                        acsvr.setCodingSchemeVersion(csVersion);
                        acsvrList.addAbsoluteCodingSchemeVersionReference(acsvr);
                    }
                    
                    if (lb_vsd_gui_.getValueSetDefinitionService().isSubSet(new URI(childVSDURICombo.getText()), new URI(parentVSDURICombo.getText()), acsvrList, null))
                    {
                        errorHandler.showInfo("Is Subset",
                            "Value Set Definition URI : " + childVSDURICombo.getText() + " is SubSet of Value Set Definition URI : " + parentVSDURICombo.getText());
                        return;
                    }
                    else
                    {
                        errorHandler.showWarning("Is NOT Subset",
                                "Value Set Definition URI : " + childVSDURICombo.getText() + " is NOT SubSet of Value Set Definition URI : " + parentVSDURICombo.getText());
                        return;
                    }
                } catch (LBException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
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
}