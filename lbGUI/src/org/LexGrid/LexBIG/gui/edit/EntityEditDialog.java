
package org.LexGrid.LexBIG.gui.edit;

import java.util.UUID;

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.LexGrid.LexBIG.gui.DialogHandler;
import org.LexGrid.LexBIG.gui.LB_GUI;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Comment;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
//import org.lexevs.cts2.LexEvsCTS2Impl;
//import org.lexevs.cts2.author.CodeSystemAuthoringOperation;
//import org.lexevs.cts2.core.update.RevisionInfo;
import org.lexevs.dao.database.utility.DaoUtility;

public class EntityEditDialog extends AbstractEditDialog<ResolvedConceptReference> {
    
    private ResolvedConceptReference resolvedConceptReference;
    private static String ENTITY_DESCRIPTION_KEY = "entityDescriptionKey";
    private static String IS_DEFINED_KEY = "isDefinedKey";
    private static String IS_ANONYMOUS_KEY = "isAnonymousKey";
    
    private Combo propertyCombo;

//    private CodeSystemAuthoringOperation codeSystemAuthoringOperation = 
//        LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
    
    public EntityEditDialog(
            LB_GUI lbGui,
            String revisionId,
            boolean defaultEnableRevisionEditing,
            String title, 
            Shell parent, 
            DialogHandler dialogHandler, 
            ResolvedConceptReference resolvedConceptReference) throws Exception {
        super(lbGui, revisionId, defaultEnableRevisionEditing, title, parent, dialogHandler);
        if(resolvedConceptReference.getEntity() == null){
            resolvedConceptReference.setEntity(ServiceUtility.resolveConceptReference(resolvedConceptReference));
        }
        this.resolvedConceptReference = resolvedConceptReference;
    }

    protected void initComponents(Composite composite) {
        Entity entity = resolvedConceptReference.getEntity();
        
        composite.setLayout(new GridLayout(2, false));
        
        GridData gridData = new GridData(GridData.FILL_BOTH);
        composite.setLayoutData(gridData);
       
        super.addTextBox(
                ENTITY_DESCRIPTION_KEY, 
                composite,
                "Entity Description: ",
                entity.getEntityDescription().getContent());
   
        super.addCheckBox(IS_ANONYMOUS_KEY, composite, "Is Anonymous: ", entity.getIsAnonymous());    
        super.addCheckBox(IS_DEFINED_KEY, composite, "Is Defined: ", entity.getIsDefined());    

        Button editPropertyButton = new Button(composite, SWT.BUTTON1);
        editPropertyButton.setText("Edit Property");
        
        propertyCombo = new Combo(composite, SWT.DROP_DOWN);
        propertyCombo.setTextLimit(100);
        propertyCombo.setText("Select a Property...");
        propertyCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        this.refreshPropertyComboBox(resolvedConceptReference);
        
        editPropertyButton.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent arg0) {
               //
            }

            public void widgetSelected(SelectionEvent arg0) {
                if(propertyCombo.getSelectionIndex() > -1) {
                    try {
                        PropertyEditDialog propEditDialog = 
                            new PropertyEditDialog(
                                    getLbGui(),
                                    getRevisionId(),
                                    getRevisionEditing(),
                                    "Edit Property",
                                    getShell(),
                                    getDialogHandler(),
                                    resolvedConceptReference,
                                    (Property)propertyCombo.getData(propertyCombo.getText()));

                        propEditDialog.addItemUpdateListener(EntityEditDialog.this);

                        propEditDialog.open();          
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    getDialogHandler().showError("Error", "Please select a Property.");   
                }
            }
        });

    }
    
    protected void refreshPropertyComboBox(ResolvedConceptReference ref) {
        propertyCombo.removeAll();
        propertyCombo.setText("Select a Property...");
        
        Entity entity = ref.getEntity();
        
        for(Property prop : entity.getAllProperties()) {
            if(StringUtils.isNotBlank(prop.getPropertyId())) {
                
                String displayText = 
                    "ID: " + prop.getPropertyId() +
                    " Type: " + DaoUtility.propertyStringToTypeMap.get(prop.getPropertyType()).toString() +
                    " Value: " + prop.getValue().getContent();
                propertyCombo.add(displayText);
                
                propertyCombo.setData(displayText, prop);
            }
        }
    }

    protected Property getPropertyById(Entity entity, String propId) {
        for(Property prop : entity.getAllProperties()) {
            if(prop.getPropertyId().equals(propId)) {
                return prop;
            }
        }
        throw new RuntimeException("No Property found with Prop Id: " + propId);
    }

//    @Override
//    protected ResolvedConceptReference updateItem() {
//        Entity entity = resolvedConceptReference.getEntity();
//        
//        entity = (Entity) SerializationUtils.clone(entity);
//        entity.setPresentation(new Presentation[0]);
//        entity.setComment(new Comment[0]);
//        entity.setDefinition(new Definition[0]);
//        entity.setProperty(new Property[0]);
//
//        String entityDescriptionText = super.getTextBoxContent(ENTITY_DESCRIPTION_KEY);
//        boolean isAnonymous = super.getCheckBoxContent(IS_ANONYMOUS_KEY);
//        boolean isDefined = super.getCheckBoxContent(IS_DEFINED_KEY);
//
//        entity.getEntityDescription().setContent(entityDescriptionText);
//        
//        entity.setIsDefined(isAnonymous);
//        entity.setIsAnonymous(isDefined);
//
//        try {
//            codeSystemAuthoringOperation.updateConcept(
//                    resolvedConceptReference.getCodingSchemeURI(), 
//                    resolvedConceptReference.getCodingSchemeVersion(), 
//                    entity, 
//                    this.buildRevisionInfo());
//          
//            org.LexGrid.LexBIG.LexBIGService.CodedNodeSet cns = this.getLbGui().getLbs().getCodingSchemeConcepts(
//                    resolvedConceptReference.getCodingSchemeURI(), 
//                    Constructors.createCodingSchemeVersionOrTagFromVersion(resolvedConceptReference.getCodingSchemeVersion()));
//
//            ConceptReferenceList list = new ConceptReferenceList();
//            list.addConceptReference(resolvedConceptReference);
//
//            return cns.restrictToCodes(list).resolve(null, null, null).next();
//        } catch (LBException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @Override
    public void onItemUpdate(ResolvedConceptReference item) {
        this.refreshPropertyComboBox(item);
        super.onItemUpdate(item);
    }
    
    
}