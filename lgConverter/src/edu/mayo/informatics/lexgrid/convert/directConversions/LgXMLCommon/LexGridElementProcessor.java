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

import java.util.ArrayList;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.Mappings;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.LexGrid.valueSets.PickListDefinition;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.SystemRelease;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;

/**
 * @author  <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer </A>
 *
 */
public class LexGridElementProcessor {
    
    private static ArrayList<CodingScheme> codingSchemes = new ArrayList<CodingScheme>();
    private static  CodingScheme[] cs = null;
    


    public static CodingScheme[] setAndRetrieveCodingSchemes() {
        cs = new CodingScheme[codingSchemes.size()];
        for (int i = 0; i < codingSchemes.size(); i++) {
            cs[i] = codingSchemes.get(i);
        }
        return cs;
    }

    /**
     * @param service
     * @param parent
     * @param child
     */
    public static void processCodingSchemeMetadata(XMLDaoServiceAdaptor service, Object parent, Object child) {
        CodingScheme scheme = (CodingScheme) parent;
        try {
            codingSchemes.add(scheme);
            service.storeCodingScheme(scheme);
        } catch (CodingSchemeAlreadyLoadedException e) {
            e.printStackTrace();
        } catch (LBRevisionException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param service
     * @param parent
     * @param child
     */
    public static void processCodingSchemeEntity(XMLDaoServiceAdaptor service, Object parent, Object child) {
        Entity e = (Entity) child;
        Entities entities = (Entities) parent;
        CodingScheme c = (CodingScheme) entities.getParent();
        service.storeEntity(e, c);
        entities.removeEntity(e);
    }

    /**
     * @param isPredicateLoaded
     * @param service
     * @param parent
     * @param child
     */
    public static void processCodingSchemeAssociation(boolean isPredicateLoaded, XMLDaoServiceAdaptor service,
            Object parent, Object child) {
        AssociationPredicate a = (AssociationPredicate) parent;
        Relations relations = (Relations) a.getParent();
        CodingScheme cs = (CodingScheme) relations.getParent();
        service.storeRelation(cs.getCodingSchemeURI(), cs.getRepresentsVersion(), relations);
        if (!isPredicateLoaded) {
            service.storeAssociationPredicate(cs.getCodingSchemeURI(), cs.getRepresentsVersion(), relations
                    .getContainerName(), (AssociationPredicate) parent);
        } else {
            service.storeAssociation(cs.getCodingSchemeURI(), cs.getRepresentsVersion(), relations.getContainerName(),
                    a.getAssociationName(), (AssociationSource) child);
        }
        a.removeSource((AssociationSource) child);
    }

    /**
     * @param parent
     */
    public static void removeEntitiesContainer(Object parent) {
        ((CodingScheme) parent).setEntities(null);
    }

    /**
     * @param service
     * @param parent
     * @param child
     * @throws LBRevisionException 
     */
    public static void processRevisionMetadata(XMLDaoServiceAdaptor service, Revision revision) throws LBRevisionException {
       service.storeRevisionMetaData(revision);
    }

    /**
     * @param parent
     * @return
     * @throws LBRevisionException 
     */
    public static void processSystemReleaseMetadata(XMLDaoServiceAdaptor service, Object parent) throws LBRevisionException {
        service.storeSystemRelease((SystemRelease)parent);
    }

    /**
     * @param service
     * @param parent
     * @param child
     * @param mappings
     * @param systemReleaseURI
     * @throws LBException 
     */
    public static void processValueSet(XMLDaoServiceAdaptor service, Object parent, Object child, Mappings mappings,
            String systemReleaseURI) throws LBException {
        ValueSetDefinition valueSet = (ValueSetDefinition) child;
        service.storeValueSet(valueSet, systemReleaseURI, mappings);
    }

    public static void processValueSetDefinition(XMLDaoServiceAdaptor service, Object parent, Object child) throws LBException {
        ValueSetDefinition valueSet = (ValueSetDefinition) parent;
        service.storeValueSetDefinition(valueSet);
    }

    public static void processPickListDefinition(XMLDaoServiceAdaptor service, Object parent, Object child) throws LBException {
        PickListDefinition pickList = (PickListDefinition) parent;
        service.storePickListDefinition(pickList);
    }
    /**
     * @param service
     * @param parent
     * @param child
     * @return
     */
    public static Mappings processValueSetMappings(XMLDaoServiceAdaptor service, Object parent, Object child) {
        Mappings mappings = (Mappings) child;
        return mappings;
    }

    /**
     * @param service
     * @param parent
     * @param child
     * @return
     */
    public static Mappings processPickListMappings(XMLDaoServiceAdaptor service, Object parent, Object child) {
        Mappings mappings = (Mappings) child;
        return mappings;
    }

    /**
     * @param service
     * @param child
     * @param mappings
     * @param systemReleaseURI
     * @throws LBException 
     * @throws LBParameterException 
     */
    public static void processPickListDefinition(XMLDaoServiceAdaptor service, Object child, Mappings mappings,
            String systemReleaseURI) throws LBParameterException, LBException {
        PickListDefinition picklist = (PickListDefinition) child;
        service.storePickList(picklist, systemReleaseURI, mappings);
    }

    public static void processCodingSchemeMetadataRevision(XMLDaoServiceAdaptor service, Object parent,
            Object child) {
        CodingScheme scheme = (CodingScheme)parent;
        codingSchemes.add(scheme);
       service.storeCodingSchemeRevision(scheme);
        
    }
    
    /**
     * @param service
     * @param parent
     * @param child
     */
    public static void processCodingSchemeEntityRevision(XMLDaoServiceAdaptor service, Object parent, Object child) {
        Entity e = (Entity) child;
        Entities entities = (Entities) parent;
        CodingScheme c = (CodingScheme) entities.getParent();
        service.storeEntityRevision(e, c);
        entities.removeEntity(e);
    }
    
    public static void processCodingSchemeAssociationRevision(boolean isPredicateLoaded, XMLDaoServiceAdaptor service,
            Object parent, Object child) {
        AssociationSource source = (AssociationSource) parent;
        AssociationPredicate a = (AssociationPredicate)source.getParent();
        Relations relations = (Relations) a.getParent();
        CodingScheme cs = (CodingScheme) relations.getParent();
        service.storeRelationsRevision(cs.getCodingSchemeURI(), cs.getRepresentsVersion(), relations);
        if (!isPredicateLoaded) {
            service.storeAssociationPredicate(cs.getCodingSchemeURI(), cs.getRepresentsVersion(), relations
                    .getContainerName(), a);
        } else {
            service.storeAssociationRevision(cs.getCodingSchemeURI(), cs.getRepresentsVersion(), relations.getContainerName(),
                    a.getAssociationName(), (AssociationSource)parent, (AssociationTarget)child);
        }
        a.removeSource((AssociationSource) parent);
    }

    public static void processCodingSchemePropertyRevision(XMLDaoServiceAdaptor service, Object parent, Object child) {
        Property p = (Property)child;
        Properties props = (Properties)parent;
        CodingScheme c = (CodingScheme)props.getParent();
        service.storeCodingSchemePropertyRevision(p, c);
        
    }

    public static void processValueSetDefinitionRevision(XMLDaoServiceAdaptor service,
            Object child) {
        ValueSetDefinition vsDefinition = (ValueSetDefinition)child;
      service.storeValueSetDefinitionRevision(vsDefinition);
        
    }

    public static void processPickListtDefinitionRevision(XMLDaoServiceAdaptor service,
            Object child) {
        PickListDefinition plDefinition = (PickListDefinition)child;
       service.storePickListDefinitionRevision(plDefinition);
        
    }


}
