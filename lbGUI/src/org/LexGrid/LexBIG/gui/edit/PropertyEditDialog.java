
package org.LexGrid.LexBIG.gui.edit;

import java.util.HashMap;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.gui.DialogHandler;
import org.LexGrid.LexBIG.gui.LB_GUI;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Presentation;
import org.apache.commons.lang.BooleanUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
//import org.lexevs.cts2.LexEvsCTS2Impl;
//import org.lexevs.cts2.author.CodeSystemAuthoringOperation;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.locator.LexEvsServiceLocator;
import org.springframework.beans.BeanUtils;

public class PropertyEditDialog extends AbstractEditDialog<ResolvedConceptReference>{
    
    private Map<PropertyType,Integer> propertyIndexMap = 
        new HashMap<PropertyType,Integer>();
 
    private ResolvedConceptReference reference;
    private Property property;
 
    private Combo propertyType;
    
    private static String PROPERTY_TEXT_KEY = "propertyTextKey";
    private static String PROPERTY_FORMAT_KEY = "propertyFormatKey";
    private static String PROPERTY_NAME_KEY = "propertyNameKey";
    private static String MATCH_IF_NO_CONTEXT_KEY = "matchIfNoContextKey";
    private static String IS_PREFERRED_KEY = "isPreferredKey";
    
//    private CodeSystemAuthoringOperation codeSystemAuthoringOperation = 
//        LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
    
    public PropertyEditDialog(
            LB_GUI lbGui,
            String revisionId,
            boolean defaultEnableRevisionEditing,
            String title, 
            Shell parent, 
            DialogHandler dialogHandler,
            ResolvedConceptReference ref,
            Property prop) throws Exception {
        super(lbGui, revisionId, defaultEnableRevisionEditing, title, parent, dialogHandler);
        this.reference = ref;
        this.property = prop;
    }
    
    @Override
    protected void initComponents(Composite composite) {
        composite.setLayout(new GridLayout(2, false));
        
        GridData gridData = new GridData(GridData.FILL_BOTH);
        composite.setLayoutData(gridData);
        
        super.addTextBox(
                PROPERTY_TEXT_KEY, 
                composite, 
                "Property Text: ",
                property.getValue().getContent());
          
        super.addTextBox(
                PROPERTY_FORMAT_KEY, 
                composite, 
                "Property Format: ",
                property.getValue().getDataType());
       
   
        Label propertyTypeLabel = new Label(composite, SWT.NONE);
        propertyTypeLabel.setText("Property Type: ");
        propertyType = super.comboBoxFactory(composite);
        propertyType.setTextLimit(100);
       
        int index = 0;
        for(PropertyType type : DaoUtility.propertyTypeToStringMap.keySet()) {
            propertyType.add(
                    type.toString(), 
                    index);
            propertyIndexMap.put(type, index);
            index++;
        }
        
        PropertyType propType = DaoUtility.propertyStringToTypeMap.get(
                this.property.getPropertyType());
        propertyType.select(
                propertyIndexMap.get(propType));
        
        propertyType.addSelectionListener(new SelectionListener(){

            public void widgetDefaultSelected(SelectionEvent arg0) {
               //
            }

            public void widgetSelected(SelectionEvent arg0) {
               PropertyType selection =
                   DaoUtility.propertyStringToTypeMap.get(propertyType.getItem(propertyType.getSelectionIndex()));

               if(selection.equals(PropertyType.PRESENTATION)) {
                   getCheckBox(IS_PREFERRED_KEY).setEnabled(true);
                   getCheckBox(MATCH_IF_NO_CONTEXT_KEY).setEnabled(true);
               } else {
                   getCheckBox(IS_PREFERRED_KEY).setEnabled(false);
                   getCheckBox(MATCH_IF_NO_CONTEXT_KEY).setEnabled(false);
               }
            }    
        });
        
        super.addTextBox(PROPERTY_NAME_KEY, composite, "Property Name: ", property.getPropertyName());
        
        boolean isPresentation = (property instanceof Presentation);
        
        Button isPrefTextBox = super.addCheckBox(IS_PREFERRED_KEY, composite, "Is Preferred: ", false);
        Button matchIfNoContextBox = super.addCheckBox(MATCH_IF_NO_CONTEXT_KEY, composite, "Match If No Context: ", false);
        
        if(isPresentation) {
            Presentation pres = (Presentation)property;
            isPrefTextBox.setSelection(BooleanUtils.toBoolean(pres.getIsPreferred()));
            matchIfNoContextBox.setSelection(BooleanUtils.toBoolean(pres.getMatchIfNoContext()));
           
        } else {
            isPrefTextBox.setEnabled(false);
            matchIfNoContextBox.setEnabled(false);
        }
    }

//    @Override
//    protected ResolvedConceptReference updateItem() {
//        property.getValue().setContent(super.getTextBoxContent(PROPERTY_TEXT_KEY));
//        property.getValue().setDataType(super.getTextBoxContent(PROPERTY_FORMAT_KEY));
//        property.setPropertyName(super.getTextBoxContent(PROPERTY_NAME_KEY));
//        
//        PropertyType propType = 
//                DaoUtility.propertyStringToTypeMap.get(
//                        propertyType.getItem(
//                                propertyType.getSelectionIndex()));
//        
//        property.setPropertyType(DaoUtility.propertyTypeToStringMap.get(propType));
//        
//        if( !(property instanceof Presentation) && propType.equals(PropertyType.PRESENTATION)) {
//            Presentation pres = new Presentation();
//            try {
//                BeanUtils.copyProperties(property, pres);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            } 
//            property = pres;
//        }
//        
//        if(property instanceof Presentation) {
//            ((Presentation)property).setIsPreferred(super.getCheckBoxContent(IS_PREFERRED_KEY));
//            ((Presentation)property).setMatchIfNoContext(super.getCheckBoxContent(MATCH_IF_NO_CONTEXT_KEY));
//        }
//
//        try {
//            
//            codeSystemAuthoringOperation.updateConceptProperty(
//                    reference.getCodingSchemeURI(), 
//                    reference.getCodingSchemeVersion(), 
//                    reference.getEntity().getEntityCode(),
//                    reference.getEntity().getEntityCodeNamespace(),
//                    property,
//                    this.buildRevisionInfo());
//
//
//            org.LexGrid.LexBIG.LexBIGService.CodedNodeSet cns = this.getLbGui().getLbs().getCodingSchemeConcepts(
//                    reference.getCodingSchemeURI(), 
//                    Constructors.createCodingSchemeVersionOrTagFromVersion(reference.getCodingSchemeVersion()));
//
//            ConceptReferenceList list = new ConceptReferenceList();
//            list.addConceptReference(reference);
//
//            return cns.restrictToCodes(list).resolve(null, null, null).next();
//        } catch(Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
}