
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
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedNamespace;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ValueSetDefIsEntityInValueSetFilter {
    private Shell shell_;
    private LB_VSD_GUI lb_vsd_gui_;
    private DialogHandler errorHandler;
    
    public ValueSetDefIsEntityInValueSetFilter(LB_VSD_GUI lb_vsd_gui, String vsdURI, Mappings vsdMappings, Shell parent) {
        shell_ = new Shell(parent.getDisplay());
        shell_.setText("Is Entity in Value Set Resolution");
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        shell_.setLayout(gridLayout);
        shell_.setSize(800, 1000);
        Rectangle bds = shell_.getDisplay().getBounds();
        Point p = shell_.getSize();
        int nLeft = (bds.width - p.x) / 2;
        int nTop = (bds.height - p.y) / 2;
        shell_.setBounds(nLeft, nTop, p.x, p.y);
        errorHandler = new DialogHandler(shell_);
        lb_vsd_gui_ = lb_vsd_gui;
        
        List<String> uris = lb_vsd_gui_.getValueSetDefinitionService().listValueSetDefinitionURIs();
        
        Label entityCodeLabel = new Label(shell_, SWT.NONE);
        entityCodeLabel.setText("Entity Code : ");
        GridData gridData = new GridData(GridData.FILL, GridData.BEGINNING, true, false);
        gridData.verticalIndent = 25;
        entityCodeLabel.setLayoutData(gridData);
        
        final Text entityCodeText = new Text(shell_, SWT.SIMPLE | SWT.BORDER);
        gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData.horizontalSpan = 2;
        gridData.verticalIndent = 25;
        entityCodeText.setLayoutData(gridData);
        entityCodeText.setFocus();
        
        Label entityCodeNSLabel = new Label(shell_, SWT.NONE);
        entityCodeNSLabel.setText("Entity Code Namespace: ");
        gridData = new GridData(GridData.FILL, GridData.BEGINNING, true, false);
        gridData.verticalIndent = 25;
        entityCodeNSLabel.setLayoutData(gridData);
        
        final Combo entityCodeNSCombo = new Combo(shell_, SWT.DROP_DOWN | SWT.BORDER);
        gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData.horizontalSpan = 2;
        gridData.verticalIndent = 25;
        entityCodeNSCombo.setLayoutData(gridData);
        if (vsdMappings != null)
        {
            for (SupportedNamespace sn : vsdMappings.getSupportedNamespaceAsReference())
            {
                entityCodeNSCombo.add(sn.getLocalId());
            }            
        }        
        
        Label vsdLabel = new Label(shell_, SWT.NONE);
        vsdLabel.setText("Value Set Definition URI : ");
        gridData = new GridData(GridData.FILL, GridData.BEGINNING, true, false);
        gridData.verticalIndent = 25;
        vsdLabel.setLayoutData(gridData);
        
        final Combo vsdURICombo = new Combo(shell_, SWT.DROP_DOWN | SWT.BORDER);
        gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData.horizontalSpan = 2;
        gridData.verticalIndent = 25;
        vsdURICombo.setLayoutData(gridData);
        
        if (uris != null)
            vsdURICombo.setItems((String[])uris.toArray(new String[0]));
        
        if (StringUtils.isNotEmpty(vsdURI))
        {
            vsdURICombo.setText(vsdURI);
            
        }
        
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
        gridData.verticalIndent = 25;
        csrLabel.setLayoutData(gridData);
        
        final Combo csrCombo = new Combo(shell_, SWT.DROP_DOWN | SWT.BORDER);
        gridData = new GridData(GridData.FILL, GridData.BEGINNING, true, false);
        gridData.horizontalSpan = 2;
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
        gridData.horizontalSpan = 3;
        gridData.verticalIndent = 25;
        filterButton.setLayoutData(gridData);
        filterButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                try {
                    if (StringUtils.isEmpty(entityCodeText.getText()))
                    {
                        errorHandler.showError("Invalid Data",
                                "Entity Code can not empty");
                            return;
                    }
//                    if (StringUtils.isEmpty(entityCodeNSCombo.getText()))
//                    {
//                        errorHandler.showError("Invalid Data",
//                                "Entity Code Namespace can not empty");
//                            return;
//                    }
                    if (StringUtils.isEmpty(vsdURICombo.getText()))
                    {
                        errorHandler.showError("Invalid Data",
                                "Value Set Definition URI can not empty");
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
                    
                    AbsoluteCodingSchemeVersionReference result = lb_vsd_gui_.getValueSetDefinitionService().isEntityInValueSet(entityCodeText.getText(), new URI(entityCodeNSCombo.getText()), new URI(vsdURICombo.getText()), null, acsvrList, null);
                    
                    if (result != null)
                    {
                        errorHandler.showInfo("Valid",
                            "Entity Code : " + entityCodeText.getText() + " is valid part of Value Set Resulution URI : " + vsdURICombo.getText());
                        return;
                    }
                    else
                    {
                        errorHandler.showWarning("NOT Valid",
                                "Entity Code : " + entityCodeText.getText() + " is NOT valid part of Value Set Resulution URI : " + vsdURICombo.getText());
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