
package org.LexGrid.LexBIG.gui.edit;

import java.util.UUID;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.gui.DialogHandler;
import org.LexGrid.LexBIG.gui.LB_GUI;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.versions.ChangedEntry;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.types.ChangeType;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
//import org.lexevs.cts2.LexEvsCTS2Impl;
//import org.lexevs.cts2.author.CodeSystemAuthoringOperation;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.locator.LexEvsServiceLocator;

public class CodingSchemeEditDialog extends AbstractEditDialog<CodingScheme>{
    
    private CodingScheme codingScheme;
    
    private static String COPYRIGHT_TEXT_KEY = "copyright";
    private static String FORMAL_NAME_KEY = "formalName";
    private static String NUM_OF_CONCEPTS_KEY = "approxNumConcepts";
    
//    private CodeSystemAuthoringOperation codeSystemAuthoringOperation = 
//        LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
    
    public CodingSchemeEditDialog(
            LB_GUI lbGui,
            String revisionId,
            boolean defaultEnableRevisionEditing,
            String title, 
            Shell parent, 
            CodingScheme codingScheme,
            DialogHandler dialogHandler) throws Exception {
        super(lbGui, revisionId, defaultEnableRevisionEditing, title, parent, dialogHandler);
        this.codingScheme = codingScheme;
    }
    
    @Override
    protected void initComponents(Composite composite) {
        composite.setLayout(new GridLayout(2, false));
        
        GridData gridData = new GridData(GridData.FILL_BOTH);
        composite.setLayoutData(gridData);
        
        String copyright = codingScheme.getCopyright() == null ? "" : codingScheme.getCopyright().getContent();
        super.addTextBox(
                COPYRIGHT_TEXT_KEY, 
                composite, 
                "Copyright: ",
                copyright);
          
        super.addTextBox(
                FORMAL_NAME_KEY, 
                composite, 
                "Formal Name: ",
                codingScheme.getFormalName());
        
        super.addTextBox(
                NUM_OF_CONCEPTS_KEY, 
                composite, 
                "Number of Entities: ",
                String.valueOf(codingScheme.getApproxNumConcepts()));
    }

//    @Override
//    protected CodingScheme updateItem() {
//
//        String numOfConceptsText = super.getTextBoxContent(NUM_OF_CONCEPTS_KEY);
//        long numOfConcepts = Long.parseLong(numOfConceptsText);
//        Text copyRight = DaoUtility.createText(super.getTextBoxContent(COPYRIGHT_TEXT_KEY));
//        String formalName = super.getTextBoxContent(FORMAL_NAME_KEY);
//
//        try {
//            this.codeSystemAuthoringOperation.updateCodeSystem(
//                    this.buildRevisionInfo(), 
//                    this.codingScheme.getCodingSchemeName(), 
//                    this.codingScheme.getCodingSchemeURI(), 
//                    formalName, 
//                    null, 
//                    numOfConcepts, 
//                    this.codingScheme.getRepresentsVersion(), 
//                    null, 
//                    null, 
//                    copyRight, 
//                    null);
//        } catch (LBException e) {
//            this.getDialogHandler().showError("Update Error", e.getMessage());
//        }
//      
//        return LexEvsServiceLocator.getInstance().
//            getDatabaseServiceManager().
//                getCodingSchemeService().
//                    getCodingSchemeByUriAndVersion(
//                            this.codingScheme.getCodingSchemeURI(), 
//                            this.codingScheme.getRepresentsVersion());
//    }
}