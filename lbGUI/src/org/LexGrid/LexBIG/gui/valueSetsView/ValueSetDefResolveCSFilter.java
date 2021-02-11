
package org.LexGrid.LexBIG.gui.valueSetsView;

import java.net.URI;
import java.net.URISyntaxException;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.gui.LB_VSD_GUI;
import org.LexGrid.valueSets.ValueSetDefinition;
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
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

public class ValueSetDefResolveCSFilter {
    private Shell shell_;
    private LB_VSD_GUI lb_vsd_gui_;
    private String vsdURIOrPLDid_;
    private ValueSetDefinition vsd_;
    private Shell parent_;
    private boolean resolvingVSD_;
    private boolean resolvingPLD_;
    
    public ValueSetDefResolveCSFilter(LB_VSD_GUI lb_vsd_gui,ValueSetDefinition vsd, Shell parent, boolean resolvingVSD, boolean resolvingPLD) {
        vsd_ = vsd;
        lb_vsd_gui_ = lb_vsd_gui;
        parent_ = parent;
        resolvingVSD_ = resolvingVSD;
        resolvingPLD_ = resolvingPLD;
        display();
    }
    public ValueSetDefResolveCSFilter(LB_VSD_GUI lb_vsd_gui, String vsdURIOrPLDid, Shell parent, boolean resolvingVSD, boolean resolvingPLD) {
        lb_vsd_gui_ = lb_vsd_gui;
        parent_ = parent;
        vsdURIOrPLDid_ = vsdURIOrPLDid;    
        resolvingVSD_ = resolvingVSD;
        resolvingPLD_ = resolvingPLD;
        display();
    }
    
    private void display(){
        shell_ = new Shell(parent_.getDisplay());
        shell_.setText("Select Coding Scheme and version");
        shell_.setLayout(new GridLayout(1, true));
        shell_.setSize(1800, 1000);
        Rectangle bds = shell_.getDisplay().getBounds();
        Point p = shell_.getSize();
        int nLeft = (bds.width - p.x) / 2;
        int nTop = (bds.height - p.y) / 2;
        shell_.setBounds(nLeft, nTop, p.x, p.y);
        
        CodingSchemeRenderingList csrList = null;
        try {
            csrList = lb_vsd_gui_.getLbs().getSupportedCodingSchemes();
        } catch (LBInvocationException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        Label csrLabel = new Label(shell_, SWT.CENTER);
        csrLabel.setText("Select Coding Scheme And Version : ");
        GridData gridData = new GridData(GridData.FILL, GridData.END, true, false);
        gridData.horizontalIndent = 3;
        gridData.verticalSpan = 2;
        gridData.verticalIndent = 5;
        csrLabel.setLayoutData(gridData);
        
        final List csrCombo = new List(shell_, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
        gridData = new GridData(GridData.FILL, GridData.BEGINNING, true, false);
        gridData.verticalSpan = 5;
        gridData.verticalIndent = 5;
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
        filterButton.setText("    Resolve    ");
        gridData = new GridData(GridData.CENTER, GridData.END, false, false);
        gridData.verticalIndent = 10;
        filterButton.setLayoutData(gridData);
        filterButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                try {
                    String[] selectedItems = csrCombo.getSelection();
                    AbsoluteCodingSchemeVersionReferenceList acsvrList = new AbsoluteCodingSchemeVersionReferenceList();
                    for (int loopIndex = 0; loopIndex < selectedItems.length; loopIndex++)
                    {
                        String[] csrString = selectedItems[loopIndex].toString().split(":");
                        String csName = csrString[0];                        
                        String csVersion = csrString[1];
                        
                        AbsoluteCodingSchemeVersionReference acsvr = new AbsoluteCodingSchemeVersionReference();
                        acsvr.setCodingSchemeURN(csName);
                        acsvr.setCodingSchemeVersion(csVersion);
                        acsvrList.addAbsoluteCodingSchemeVersionReference(acsvr);                        
                    }
                    
                    if (resolvingVSD_)
                    {
                        if (vsd_ != null)
                            lb_vsd_gui_.resolveValueSetDef(vsd_, acsvrList, "");
                        else
                            lb_vsd_gui_.resolveValueSetDef(new URI(vsdURIOrPLDid_),acsvrList, "");
                    } 
                    else if (resolvingPLD_)
                    {
                        lb_vsd_gui_.resolvePickList(vsdURIOrPLDid_, acsvrList);
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