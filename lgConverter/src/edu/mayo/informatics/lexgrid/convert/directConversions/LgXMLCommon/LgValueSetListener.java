
package edu.mayo.informatics.lexgrid.convert.directConversions.LgXMLCommon;


import java.util.Arrays;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.castor.xml.UnmarshalListener;
import org.mayo.edu.lgModel.LexGridBase;

public class LgValueSetListener implements UnmarshalListener {
    
    private int nentities = 0;
    private int nassociations = 0;
    int modCount = 0;
//    private static final int mod = 10;
    
//    private boolean isValueSetDefinitionLoaded = false;
//    private boolean isRevisionLoaded = false;
    private boolean isPropertiesPresent = false;
    private AssociationPredicate currentPredicate = new AssociationPredicate();
//    private Revision revision = new Revision();
    private ValueSetDefinition[] valueSetDefinitions = null;

    private XMLDaoServiceAdaptor serviceAdaptor = null;
    private LgMessageDirectorIF messages_;

    


    public LgValueSetListener() {
        super();
        serviceAdaptor = new XMLDaoServiceAdaptor();
    }
    
    public LgValueSetListener(LgMessageDirectorIF messages) {
        super();
        serviceAdaptor = new XMLDaoServiceAdaptor();
        messages_ = messages;
    }
    /**
     * @return
     */
    int getNentities() {
        return nentities;
    }
    
    public ValueSetDefinition[] getValueSetDefinitions() {
        return valueSetDefinitions;
    }

    public void setValueSetDefinitions(ValueSetDefinition[] valueSetDefinitions) {
        this.valueSetDefinitions = valueSetDefinitions;
    }

    /**
     * @return
     */
    int getNassociations() {
        return nassociations;
    }

    /**
     * @return
     */
   boolean isPropertiesPresent() {
        return isPropertiesPresent;
    }

    /**
     * @param isPropertiesPresent
     */
    void setPropertiesPresent(boolean isPropertiesPresent) {
        this.isPropertiesPresent = isPropertiesPresent;
    }

    /**
     * @param e
     * @return
     */
    boolean isPredicateLoaded(AssociationPredicate e) {
        if (currentPredicate.equals(e))
            return true;
        else {
            currentPredicate = e;
            return false;
        }
    }

    /* (non-Javadoc)
     * @see org.castor.xml.UnmarshalListener#initialized(java.lang.Object, java.lang.Object)
     */
    public void initialized(Object target, Object parent) {
        if (target != null && target instanceof LexGridBase)
            ((LexGridBase) target).setParent(parent);
        else
            messages_.error(target.getClass().getName() + " is not an instance of LexGridBase");
    }

    /* (non-Javadoc)
     * @see org.castor.xml.UnmarshalListener#attributesProcessed(java.lang.Object, java.lang.Object)
     */
    public void attributesProcessed(Object target, Object parent) {

    }

    /* (non-Javadoc)
     * @see org.castor.xml.UnmarshalListener#unmarshalled(java.lang.Object, java.lang.Object)
     */
    public void unmarshalled(Object target, Object parent) {

//        messages_.debug("Unmarshalled target: "
//                + (target != null ? target.getClass().getSimpleName() : "target is null"));
//        messages_.debug("parent of Unmarshalled target: "
//                + (parent != null ? parent.getClass().getSimpleName() : "parent is null"));

        if (target instanceof ValueSetDefinition && parent == null) {
            if (getValueSetDefinitions() == null || getValueSetDefinitions().length == 0)
                setValueSetDefinitions(new ValueSetDefinition[] {(ValueSetDefinition) target});
            else
            {
                List<ValueSetDefinition> vsdList = Arrays.asList(getValueSetDefinitions());
                vsdList.add((ValueSetDefinition) target); 
                setValueSetDefinitions((ValueSetDefinition[]) vsdList.toArray());
            }
            try {
                LexGridElementProcessor.processValueSetDefinition(serviceAdaptor, target, parent);
            } catch (LBException e) {
                messages_.error("Error processing value set from XML", e);
                e.printStackTrace();
            }
        }
    }

    /* (non-Javadoc)
     * @see org.castor.xml.UnmarshalListener#fieldAdded(java.lang.String, java.lang.Object, java.lang.Object)
     */
    public void fieldAdded(String fieldName, Object parent, Object child) {

//        messages_.debug("fieldName:" + fieldName);
//        messages_.debug("parent: " + parent.getClass().getSimpleName());
//        messages_.debug("child: " + child.getClass().getSimpleName());
        
//        if (!isPropertiesPresent && UnMarshallingLogic.isValueSetMappings(parent, child)) {
//            LexGridElementProcessor.processValueSetMappings(serviceAdaptor, parent, child);
//            isValueSetDefinitionLoaded = true;
//        }
//        if (!isValueSetDefinitionLoaded && UnMarshallingLogic.isValueSetProperties(parent, child)) {
//            LexGridElementProcessor.processCodingSchemeMetadata(serviceAdaptor, parent, child, this.codingSchemeManifest);
//            isValueSetDefinitionLoaded = true;
//        }
//        if (UnMarshallingLogic.isCodingSchemeEntity(parent, child)) {
//            LexGridElementProcessor.processCodingSchemeEntity(serviceAdaptor, parent, child);
//            nentities++;
//            if(nentities%10== 9){  
//                modCount = modCount + 10;
//                messages_.info("Entities Loaded: " + modCount);}
//        } else if (UnMarshallingLogic.isValueSetDefinitionEntry(parent, child)) {
//            LexGridElementProcessor.removeEntitiesContainer(parent);
//            modCount = 0;
//        } else if (UnMarshallingLogic.isCodingSchemeAssociation(parent, child)) {
//            LexGridElementProcessor.processCodingSchemeAssociation(this
//                    .isPredicateLoaded((AssociationPredicate) parent), serviceAdaptor, parent, child);
//            nassociations++;
//            if(nassociations%10 == 9){  
//                modCount = modCount + 10;
//                messages_.info("Associations Loaded: " + modCount);}
//        }
        

        
    }
}