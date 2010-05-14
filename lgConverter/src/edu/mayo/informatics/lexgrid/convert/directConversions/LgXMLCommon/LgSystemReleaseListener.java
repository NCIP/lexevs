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

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.codingSchemes.CodingSchemes;
import org.LexGrid.naming.Mappings;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.valueSets.PickListDefinition;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.versions.SystemRelease;
import org.castor.xml.UnmarshalListener;
import org.mayo.edu.lgModel.LexGridBase;

/**
 * @author  <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer </A>
 * Listener for loading a system release element and its contained 
 * coding schemes, picklists, value sets and revision.
 */
public class LgSystemReleaseListener implements UnmarshalListener {

    private int nentities = 0;
    private int nassociations = 0;
    private int modCount = 0;
    private boolean isCodingSchemeLoaded = false;
    private boolean isPropertiesPresent = false;
    private boolean isSystemReleaseSet = false;
    private Mappings currentPickListMappings = new Mappings();
    private Mappings currentValueSetMappings;
    private SystemRelease systemRelease = new SystemRelease();
    private AssociationPredicate currentPredicate = new AssociationPredicate();
    private XMLDaoServiceAdaptor serviceAdaptor = null;
    private CodingScheme[] codingSchemes = null;
    private LgMessageDirectorIF messages_;
    private static final int mod = 10;
    /**
     * 
     */
    public LgSystemReleaseListener() {
        super();
        serviceAdaptor = new XMLDaoServiceAdaptor();

    }
    
    /**
     * @param messages
     */
    public LgSystemReleaseListener(LgMessageDirectorIF messages) {
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

    public void attributesProcessed(Object target, Object parent) {

    }

    /* (non-Javadoc)
     * @see org.castor.xml.UnmarshalListener#unmarshalled(java.lang.Object, java.lang.Object)
     */
    public void unmarshalled(Object target, Object parent) {
        
//       messages_.debug("Unmarshalled target: "
//                + (target != null ? target.getClass().getSimpleName() : "target is null"));
//       messages_.debug("parent of Unmarshalled target: "
//                + (parent != null ? parent.getClass().getSimpleName() : "parent is null"));
        
        if(target instanceof CodingScheme && parent instanceof CodingSchemes){
            setCodingSchemes(LexGridElementProcessor.setAndRetrieveCodingSchemes());
        }
    }


    public CodingScheme[] getCodingSchemes() {
        return codingSchemes;
    }

    public void setCodingSchemes(CodingScheme[] codingSchemes) {
        this.codingSchemes = codingSchemes;
    }

    /* (non-Javadoc)
     * @see org.castor.xml.UnmarshalListener#fieldAdded(java.lang.String, java.lang.Object, java.lang.Object)
     */
    public void fieldAdded(String fieldName, Object parent, Object child) {

//        messages_.debug("fieldName:" + fieldName);
//        messages_.debug("parent: " + parent.getClass().getSimpleName());
//        messages_.debug("child: " + child.getClass().getSimpleName());
        
        if (!isSystemReleaseSet && UnMarshallingLogic.isSytemRelease(parent, child)) {
           try {
            LexGridElementProcessor.processSystemReleaseMetadata(serviceAdaptor, parent);
        } catch (LBRevisionException e) {
            messages_.error("System release element reading and writing has failed.", e);
            e.printStackTrace();
        }
            isSystemReleaseSet = true;
        }
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
            if(nentities%mod == mod-1){  
                modCount = modCount + mod;
                messages_.info("Entities Loaded: " + modCount);}
        } else if (UnMarshallingLogic.isCodingSchemeEntities(parent, child)) {
            LexGridElementProcessor.removeEntitiesContainer(parent);
            modCount = 0;
        } else if (UnMarshallingLogic.isCodingSchemeAssociation(parent, child)) {
            LexGridElementProcessor.processCodingSchemeAssociation(this
                    .isPredicateLoaded((AssociationPredicate) parent), serviceAdaptor, parent, child);
            nassociations++;
            if(nassociations%mod == mod-1){  
                modCount = modCount + mod;
                messages_.info("Associations Loaded: " + modCount);}
        }
        if (UnMarshallingLogic.isValueSetMappings(parent, child)) {
            currentValueSetMappings = LexGridElementProcessor.processValueSetMappings(serviceAdaptor, parent, child);
        }
        if (UnMarshallingLogic.isValueSet(parent, child)) {
            String vsdURI = ((ValueSetDefinition) child).getValueSetDefinitionURI();
            messages_.info("Loading value set definition uri : " + vsdURI);
            try {
                LexGridElementProcessor.processValueSet(serviceAdaptor, parent, child, currentValueSetMappings,
                        systemRelease.getReleaseURI());
            } catch (LBException e) {
                messages_.error("Error loading VSD : " + vsdURI, e);
                e.printStackTrace();                
            }
        }
        if (UnMarshallingLogic.isPickListMappings(parent, child)) {
            currentPickListMappings = LexGridElementProcessor.processPickListMappings(serviceAdaptor, parent, child);
        }
        if (UnMarshallingLogic.isPickListDefinition(parent, child)) {
            String pickListId = ((PickListDefinition) child).getPickListId();
            try {                
                messages_.info("Loading pick list definition id : " + pickListId);
                LexGridElementProcessor.processPickListDefinition(serviceAdaptor, child, currentPickListMappings,
                        systemRelease.getReleaseURI());
            } catch (LBParameterException e) {
                messages_.error("Error loading PickList : " + pickListId, e);
                e.printStackTrace();  
            } catch (LBException e) {
                messages_.error("Error loading PickList : " + pickListId, e);
                e.printStackTrace();  
            }
        }

    }

    /**
     * @return the messages_
     */
    public LgMessageDirectorIF getMessages_() {
        return messages_;
    }

    /**
     * @param messages the messages_ to set
     */
    public void setMessages_(LgMessageDirectorIF messages) {
        messages_ = messages;
    }

}
