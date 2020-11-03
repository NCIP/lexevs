/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package edu.mayo.informatics.lexgrid.convert.directConversions.owlapi;

import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.custom.concepts.EntityFactory;
import org.LexGrid.relations.AssociationEntity;
import org.LexGrid.relations.AssociationPredicate;

/**
 * Association class in lg model is spit to AssociationPredicate 
 * and AssociationEntity. AssociationWrapper holds AssociationPredicate
 * & AssociationEntity, and it's only used in package
 * edu.mayo.informatics.lexgrid.convert
 * 
 * 
 * @author Zonghui Lian
 *
 */
public class AssociationWrapper {
    private AssociationPredicate ap;
    private AssociationEntity ae;
    private String relationsContainerName;
    private boolean inverseTransitive;
    
    public AssociationWrapper(){
        ap = new AssociationPredicate();
        ae = EntityFactory.createAssociation();
    }
    
    public AssociationPredicate getAssociationPredicate() {
        return ap;
    }
    
    public AssociationEntity getAssociationEntity() {
        return ae;
    }
    
    public void setAssociationPredicate(AssociationPredicate in) {
        ap = in;
    }
    
    public void setAssociationEntity(AssociationEntity in) {
        ae = in;
    }
    
    public void setAssociationName(String associationName) {
        ap.setAssociationName(associationName);
    }
    
    public void setEntityCode(String entityCode) {
        ae.setEntityCode(entityCode);
    }
    
    public void setEntityCodeNamespace(String entityCodeNamespace) {
        ae.setEntityCodeNamespace(entityCodeNamespace);
    }
    
    public void setForwardName(String forwardName) {
        ae.setForwardName(forwardName);
    }
    
    public void setEntityDescription(String description) {
        EntityDescription ed = new EntityDescription();
        ed.setContent(description);
        ae.setEntityDescription(ed);
    }
    
    public void setReverseName(String reverseName) {
        ae.setReverseName(reverseName);
    }
    
    public void setIsTransitive(Boolean b) {
        ae.setIsTransitive(b);
    }
    
    public void setIsNavigable(Boolean b) {
        ae.setIsNavigable(b);
    }
    
    public void addProperty(Property p) {
        ae.addProperty(p);
    }

    public void setRelationsContainerName(String relationsContainerName) {
        this.relationsContainerName = relationsContainerName;
    }

    public String getRelationsContainerName() {
        return relationsContainerName;
    }

    public boolean isInverseTransitive() {
        return inverseTransitive;
    }

    public void setInverseTransitive(boolean inverseTransitive) {
        this.inverseTransitive = inverseTransitive;
    }
}