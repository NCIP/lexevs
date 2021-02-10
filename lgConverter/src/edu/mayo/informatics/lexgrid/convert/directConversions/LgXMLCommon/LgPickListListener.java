
package edu.mayo.informatics.lexgrid.convert.directConversions.LgXMLCommon;


import java.util.Arrays;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.valueSets.PickListDefinition;
import org.castor.xml.UnmarshalListener;
import org.mayo.edu.lgModel.LexGridBase;

public class LgPickListListener implements UnmarshalListener {
    
    private int nentities = 0;
    private int nassociations = 0;
    int modCount = 0;
    

    private boolean isPropertiesPresent = false;
    private AssociationPredicate currentPredicate = new AssociationPredicate();
    private PickListDefinition[] plds = null;

    private XMLDaoServiceAdaptor serviceAdaptor = null;
    private LgMessageDirectorIF messages_;

    


    public LgPickListListener() {
        super();
        serviceAdaptor = new XMLDaoServiceAdaptor();
    }
    
    public LgPickListListener(LgMessageDirectorIF messages) {
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
    
    public PickListDefinition[] getPickListDefinitions() {
        return plds;
    }

    public void setPickListDefinitions(PickListDefinition[] pickListDefinitions) {
        this.plds = pickListDefinitions;
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

        if (target instanceof PickListDefinition && parent == null) {
            if (getPickListDefinitions() == null || getPickListDefinitions().length == 0)
                setPickListDefinitions(new PickListDefinition[] {(PickListDefinition) target});
            else
            {
                List<PickListDefinition> pldList = Arrays.asList(getPickListDefinitions());
                pldList.add((PickListDefinition) target); 
                setPickListDefinitions((PickListDefinition[]) pldList.toArray());
            }
            try {
                LexGridElementProcessor.processPickListDefinition(serviceAdaptor, target, parent);
            } catch (LBException e) {
                messages_.error("Error processing pick list from XML", e);
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
        
        
    }
}