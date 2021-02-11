
package edu.mayo.informatics.lexgrid.convert.directConversions.LgXMLCommon;

import java.util.ArrayList;
import java.util.HashMap;

import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.relations.Relations;
import org.LexGrid.relations.AssociationData;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.valueSets.PickListDefinition;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.versions.Revision;
import org.castor.xml.UnmarshalListener;
import org.mayo.edu.lgModel.LexGridBase;

/**
 * @author  <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer </A>
 * Listener for Unmarshalling a Revision of a LexGrid XML representation of
 * a picklist, value set or coding scheme. 
 */
public class LgRevisionListener implements UnmarshalListener {
    
    private int nentities = 0;
    private int nassociations = 0;
    int modCount = 0;
    private static final int mod = 10;
    CodingScheme cs = null;
    Entity currentEntity = null;
    ArrayList<Property> entityProperties = new ArrayList<Property>();
    
    private boolean isCodingSchemeLoaded = false;
    private boolean isRevisionLoaded = false;
    private boolean isPropertiesPresent = false;
    private int lastMetaDataType;
    
    private AssociationPredicate currentPredicate = new AssociationPredicate();
    private Revision revision = new Revision();
    private CodingScheme[] codingSchemes = null;

    private XMLDaoServiceAdaptor serviceAdaptor = null;
    private LgMessageDirectorIF messages_;

    


    public LgRevisionListener() {
        super();
        serviceAdaptor = new XMLDaoServiceAdaptor();
    }
    
    public LgRevisionListener(LgMessageDirectorIF messages) {
        super();
        serviceAdaptor = new XMLDaoServiceAdaptor();
        messages_ = messages;
    }
    
    public int getLastMetaDataType() {
        return lastMetaDataType;
    }

    public void setLastMetaDataType(int lastMetaDataType) {
        this.lastMetaDataType = lastMetaDataType;
    }

    /**
     * @return
     */
    int getNentities() {
        return nentities;
    }
    
    public CodingScheme[] getCodingSchemes() {
        return codingSchemes;
    }

    public void setCodingSchemes(CodingScheme[] codingSchemes) {
        this.codingSchemes = codingSchemes;
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
//        if(predicateMetadata.contains(e))
//            return true;
//        else
//        {
//            predicateMetadata.add(e);
//            return false;
//        }
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
        
//                messages_.debug("Unmarshalled target: "
//                + (target != null ? target.getClass().getSimpleName() : "target is null"));
//                messages_.debug("parent of Unmarshalled target: "
//                + (parent != null ? parent.getClass().getSimpleName() : "parent is null"));
        
        if(target instanceof Revision && parent == null){
            setCodingSchemes(LexGridElementProcessor.setAndRetrieveCodingSchemes());
           messages_.info("Entity Count: " + nentities);
           messages_.info("Association Count: " + nassociations);
        }
        if(target instanceof Entity  && parent instanceof Entities){
            Entities entities = (Entities)parent;
            CodingScheme cs = (CodingScheme)entities.getParent();
            LexGridElementProcessor.processCodingSchemeEntityRevision(serviceAdaptor, parent, target);
            for (Property o :entityProperties ){
                LexGridElementProcessor.processEntityPropertyRevision(serviceAdaptor, cs, target, o);
            }
            entityProperties.clear();
        nentities++;
        if(nentities%mod == mod-1){  
            modCount = modCount + mod;
            messages_.info("Entities Loaded: " + modCount);}
        }
        if(target instanceof ValueSetDefinition){
            LexGridElementProcessor.processValueSetDefinitionRevision(serviceAdaptor,target);
        }
        if(target instanceof PickListDefinition){
            LexGridElementProcessor.processPickListtDefinitionRevision(serviceAdaptor, target);
        }
        
    }

    /* (non-Javadoc)
     * @see org.castor.xml.UnmarshalListener#fieldAdded(java.lang.String, java.lang.Object, java.lang.Object)
     */
    public void fieldAdded(String fieldName, Object parent, Object child) {

//        messages_.debug("fieldName:" + fieldName);
//        messages_.debug("parent: " + parent.getClass().getSimpleName());
//        messages_.debug("child: " + child.getClass().getSimpleName());
        
        if (!isRevisionLoaded && UnMarshallingLogic.isRevisionWithLastChild(lastMetaDataType, parent, child)) {
            revision = (Revision)parent;
            try {
                LexGridElementProcessor.processRevisionMetadata(serviceAdaptor, revision);
            } catch (LBRevisionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            isRevisionLoaded = true;
        }
        if (!isPropertiesPresent && UnMarshallingLogic.isCodingSchemeMappings(parent, child)) {
            

            LexGridElementProcessor.processCodingSchemeMetadataRevision(serviceAdaptor, parent, child);
            cs = (CodingScheme)parent;
            isCodingSchemeLoaded = true;
        }
        if (!isCodingSchemeLoaded && UnMarshallingLogic.isCodingSchemeProperties(parent, child)) {

            LexGridElementProcessor.processCodingSchemeMetadataRevision(serviceAdaptor, parent, child);
            cs = (CodingScheme)parent;
            isCodingSchemeLoaded = true;
        }
        if(isCodingSchemeLoaded && UnMarshallingLogic.isCodingSchemePropertiesRevision(parent, child)){
            LexGridElementProcessor.processCodingSchemePropertyRevision(serviceAdaptor, cs, parent, child);
        }
        
        if (isCodingSchemeLoaded && UnMarshallingLogic.isCodingSchemeEntity(parent, child)) {
            LexGridElementProcessor.processCodingSchemeEntityRevision(serviceAdaptor, parent, child);
            nentities++;
            if(nentities%mod == mod-1){  
                modCount = modCount + mod;
                messages_.info("Entities Loaded: " + modCount);}
        } 
        
        if(UnMarshallingLogic.isCodingSchemeEntityProperty(parent, child)){
            entityProperties.add((Property)child);
        }
        if(UnMarshallingLogic.isCodingSchemeAssociationSource(parent,child)){
            AssociationSource source = (AssociationSource)parent;
            AssociationPredicate predicate = (AssociationPredicate) source.getParent();
            LexGridElementProcessor.processCodingSchemeAssociationRevision(isPredicateLoaded(predicate), serviceAdaptor, source, child);
          nassociations++;
          if(nassociations%mod == mod-1){  
              modCount = modCount + mod;
              messages_.info("Associations Loaded: " + modCount);}
        }
        if(UnMarshallingLogic.isCodingSchemeAssociationData(parent, child)){
            AssociationSource source = (AssociationSource)parent;
            AssociationData data = (AssociationData)child;
            LexGridElementProcessor.processAssociationData(serviceAdaptor,source, data);
        }

    }
   
}