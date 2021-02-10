
package edu.mayo.informatics.lexgrid.convert.directConversions.LgXMLCommon;

import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.LexOnt.CodingSchemeManifest;
import org.LexGrid.concepts.Entity;
import org.LexGrid.relations.AssociationPredicate;
import org.castor.xml.UnmarshalListener;
import org.mayo.edu.lgModel.LexGridBase;

/**
 * @author  <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer </A>
 *
 * Listener for unmarshalling a coding scheme element from LexGrid XML.
 */
public class LgCodingSchemeListener implements UnmarshalListener {

    int nentities = 0;
    int nassociations = 0;
    int modCount = 0;
    private static final int mod = 10;
    
    boolean isCodingSchemeLoaded = false;
    boolean isConceptLoaded = false;
    boolean isAssociationLoaded = false;
    boolean isPropertiesPresent = false;
    Entity currentEntity = new Entity();
    AssociationPredicate currentPredicate = new AssociationPredicate();
    
    private CodingSchemeManifest codingSchemeManifest;

    XMLDaoServiceAdaptor serviceAdaptor = null;
    LgMessageDirectorIF messages_;
    /**
     * Constructor initializes service adaptor
     */
    public LgCodingSchemeListener() {
        super();
        serviceAdaptor = new XMLDaoServiceAdaptor();
    }
    public LgCodingSchemeListener(LgMessageDirectorIF messages) {
        super();
        serviceAdaptor = new XMLDaoServiceAdaptor();
        messages_ = messages;
    }
    
    public LgCodingSchemeListener(LgMessageDirectorIF messages, CodingSchemeManifest codingSchemeManifest) {
        super();
        serviceAdaptor = new XMLDaoServiceAdaptor();
        messages_ = messages;
        this.codingSchemeManifest = codingSchemeManifest;
    }
    /**
     * @return
     */
    int getNentities() {
        return nentities;
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
    public boolean isPropertiesPresent() {
        return isPropertiesPresent;
    }

    /**
     * @param isPropertiesPresent
     */
    public void setPropertiesPresent(boolean isPropertiesPresent) {
        this.isPropertiesPresent = isPropertiesPresent;
    }

    /**
     * @param e
     * @return
     */
    private boolean isPredicateLoaded(AssociationPredicate e) {
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
    }

    /* (non-Javadoc)
     * @see org.castor.xml.UnmarshalListener#fieldAdded(java.lang.String, java.lang.Object, java.lang.Object)
     */
    public void fieldAdded(String fieldName, Object parent, Object child) {

//        messages_.debug("fieldName:" + fieldName);
//        messages_.debug("parent: " + parent.getClass().getSimpleName());
//        messages_.debug("child: " + child.getClass().getSimpleName());
        
        if (!isPropertiesPresent && UnMarshallingLogic.isCodingSchemeMappings(parent, child)) {
            LexGridElementProcessor.processCodingSchemeMetadata(serviceAdaptor, parent, child, this.codingSchemeManifest);
            isCodingSchemeLoaded = true;
        }
        if (!isCodingSchemeLoaded && UnMarshallingLogic.isCodingSchemeProperties(parent, child)) {
            LexGridElementProcessor.processCodingSchemeMetadata(serviceAdaptor, parent, child, this.codingSchemeManifest);
            isCodingSchemeLoaded = true;
        }
        if (UnMarshallingLogic.isCodingSchemeEntity(parent, child)) {
            LexGridElementProcessor.processCodingSchemeEntity(serviceAdaptor, parent, child);
            nentities++;
            if(nentities%10== 9){  
                modCount = modCount + 10;
                messages_.info("Entities Loaded: " + modCount);}
        } else if (UnMarshallingLogic.isCodingSchemeEntities(parent, child)) {
            LexGridElementProcessor.removeEntitiesContainer(parent);
            modCount = 0;
        } else if (UnMarshallingLogic.isCodingSchemeAssociation(parent, child)) {
            LexGridElementProcessor.processCodingSchemeAssociation(this
                    .isPredicateLoaded((AssociationPredicate) parent), serviceAdaptor, parent, child);
            nassociations++;
            if(nassociations%10 == 9){  
                modCount = modCount + 10;
                messages_.info("Associations Loaded: " + modCount);}
        }
    }

}