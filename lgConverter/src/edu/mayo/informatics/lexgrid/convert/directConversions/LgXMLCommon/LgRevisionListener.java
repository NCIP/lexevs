/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *      http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package edu.mayo.informatics.lexgrid.convert.directConversions.LgXMLCommon;

import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationSource;
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
    
    private boolean isCodingSchemeLoaded = false;
    private boolean isRevisionLoaded = false;
    private boolean isPropertiesPresent = false;
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
    }

    /* (non-Javadoc)
     * @see org.castor.xml.UnmarshalListener#fieldAdded(java.lang.String, java.lang.Object, java.lang.Object)
     */
    public void fieldAdded(String fieldName, Object parent, Object child) {

//        messages_.debug("fieldName:" + fieldName);
//        messages_.debug("parent: " + parent.getClass().getSimpleName());
//        messages_.debug("child: " + child.getClass().getSimpleName());
        
        if (!isRevisionLoaded && UnMarshallingLogic.isRevisionWithFirstChild(parent, child)) {
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
            
//            try {
//                LexGridElementProcessor.processRevisionMetadata(serviceAdaptor, revision, (CodingScheme)parent);
//            } catch (LBRevisionException e) {
//                messages_.error("Revision element reading and writing has failed.", e);
//                e.printStackTrace();
//            }
            LexGridElementProcessor.processCodingSchemeMetadataRevision(serviceAdaptor, parent, child);
            isCodingSchemeLoaded = true;
        }
        if (!isCodingSchemeLoaded && UnMarshallingLogic.isCodingSchemeProperties(parent, child)) {
//            try {
//                LexGridElementProcessor.processRevisionMetadata(serviceAdaptor, revision, (CodingScheme)parent);
//            } catch (LBRevisionException e) {
//                messages_.error("Revision element reading and writing has failed.", e);
//                e.printStackTrace();
//            }
            LexGridElementProcessor.processCodingSchemeMetadataRevision(serviceAdaptor, parent, child);
            isCodingSchemeLoaded = true;
        }
//        if(UnMarshallingLogic.isCodingSchemeProperty(serviceAdaptor, parent, child)){
//            LexGridElementProcessor.processCodingSchemePropertyRevision(serviceAdaptor, parent, child);
//        }
        
        if (UnMarshallingLogic.isCodingSchemeEntity(parent, child)) {
            LexGridElementProcessor.processCodingSchemeEntityRevision(serviceAdaptor, parent, child);
            nentities++;
            if(nentities%mod == mod-1){  
                modCount = modCount + mod;
                messages_.info("Entities Loaded: " + modCount);}
         
        } 
        
        if(UnMarshallingLogic.isCodingSchemeAssociationSource(parent,child)){
            AssociationSource source = (AssociationSource)parent;
            AssociationPredicate predicate = (AssociationPredicate) source.getParent();
            LexGridElementProcessor.processCodingSchemeAssociationRevision(isPredicateLoaded(predicate), serviceAdaptor, parent, child);
          nassociations++;
          if(nassociations%mod == mod-1){  
              modCount = modCount + mod;
              messages_.info("Associations Loaded: " + modCount);}
        }
        
          
    }
   
}
