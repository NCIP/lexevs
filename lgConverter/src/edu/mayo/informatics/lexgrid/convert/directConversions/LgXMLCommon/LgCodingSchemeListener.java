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

import org.LexGrid.concepts.Entity;
import org.LexGrid.relations.AssociationPredicate;
import org.castor.xml.UnmarshalListener;
import org.mayo.edu.lgModel.LexGridBase;

/**
 * @author  <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer </A>
 *
 */
public class LgCodingSchemeListener implements UnmarshalListener {

    int nentities = 0;
    int nassociations = 0;
    boolean isCodingSchemeLoaded = false;
    boolean isConceptLoaded = false;
    boolean isAssociationLoaded = false;
    boolean isPropertiesPresent = false;
    Entity currentEntity = new Entity();
    AssociationPredicate currentPredicate = new AssociationPredicate();

    XMLDaoServiceAdaptor serviceAdaptor = null;

    /**
     * Constructor initializes service adaptor
     */
    public LgCodingSchemeListener() {
        super();
        serviceAdaptor = new XMLDaoServiceAdaptor();
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
            System.out.println(target.getClass().getName() + " is not an instance of LexGridBase");
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
        
        //TODO Debugging code.  Remove before shipping
        System.out.println("Unmarshalled target: "
                + (target != null ? target.getClass().getSimpleName() : "target is null"));
        System.out.println("parent of Unmarshalled target: "
                + (parent != null ? parent.getClass().getSimpleName() : "parent is null"));
    }

    /* (non-Javadoc)
     * @see org.castor.xml.UnmarshalListener#fieldAdded(java.lang.String, java.lang.Object, java.lang.Object)
     */
    public void fieldAdded(String fieldName, Object parent, Object child) {

        //TODO Debugging code.  Remove before shipping
        System.out.println("fieldName:" + fieldName);
        System.out.println("parent: " + parent.getClass().getSimpleName());
        System.out.println("child: " + child.getClass().getSimpleName());
        
        if (!isPropertiesPresent && UnMarshallingLogic.isCodingSchemeMappings(parent, child)) {
            LexGridElementProcessor.processCodingSchemeMetadata(serviceAdaptor, parent, child);
            isCodingSchemeLoaded = true;
        }
        if (!isCodingSchemeLoaded && UnMarshallingLogic.isCodingSchemeProperties(parent, child)) {
            LexGridElementProcessor.processCodingSchemeMetadata(serviceAdaptor, parent, child);
            isCodingSchemeLoaded = true;
        }
        if (UnMarshallingLogic.isCodingSchemeEntity(parent, child)) {
            LexGridElementProcessor.processCodingSchemeEntity(serviceAdaptor, parent, child);
            nentities++;
        } else if (UnMarshallingLogic.isCodingSchemeEntities(parent, child)) {
            LexGridElementProcessor.removeEntitiesContainer(parent);
        } else if (UnMarshallingLogic.isCodingSchemeAssociation(parent, child)) {
            LexGridElementProcessor.processCodingSchemeAssociation(this
                    .isPredicateLoaded((AssociationPredicate) parent), serviceAdaptor, parent, child);
            nassociations++;
        }
    }

}
