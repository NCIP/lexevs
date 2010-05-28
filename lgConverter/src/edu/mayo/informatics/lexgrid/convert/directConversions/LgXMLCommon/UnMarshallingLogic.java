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

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.Mappings;
import org.LexGrid.relations.AssociationData;
import org.LexGrid.relations.AssociationEntity;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.LexGrid.valueSets.PickListDefinition;
import org.LexGrid.valueSets.PickListDefinitions;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.ValueSetDefinitions;
import org.LexGrid.versions.ChangedEntry;
import org.LexGrid.versions.EditHistory;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.SystemRelease;

/**
 * @author  <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer </A>
 *
 */
public class UnMarshallingLogic {
private static final int CHANGE_AGENT_TYPE = 0;
private static final int CHANGE_INSTRUCTIONS_TYPE = 1;

    /**
     * @param parent
     * @param child
     * @return
     */
    public static boolean isCodingSchemeMappings(Object parent, Object child) {
        return parent instanceof CodingScheme && child instanceof Mappings;
    }

    /**
     * @param parent
     * @param child
     * @return
     */
    public static boolean isCodingSchemeProperties(Object parent, Object child) {
        return parent instanceof CodingScheme && child instanceof Properties;
    }

    /**
     * @param parent
     * @param child
     * @return
     */
    public static boolean isCodingSchemeEntity(Object parent, Object child) {
        return child instanceof Entity && parent instanceof Entities || child instanceof AssociationEntity && parent instanceof Entities;
    }

    /**
     * @param parent
     * @param child
     * @return
     */
    public static boolean isCodingSchemeEntities(Object parent, Object child) {
        return child instanceof Entities && parent instanceof CodingScheme;
    }

    /**
     * @param parent
     * @param child
     * @return
     */
    public static boolean isCodingSchemeAssociation(Object parent, Object child) {
        return child instanceof AssociationSource && parent instanceof AssociationPredicate;
    }
    /**
     * @param parent
     * @param child
     * @return
     */
    public static boolean isCodingSchemeAssociationSource(Object parent, Object child) {
        return child instanceof AssociationTarget && parent instanceof AssociationSource;
    }
    
    public static boolean isCodingSchemeProperty(XMLDaoServiceAdaptor serviceAdaptor, Object parent, Object child) {
        return child instanceof Property && parent instanceof Properties;
       }

    /**
     * @param parent
     * @param child
     * @return
     */
    public static boolean isRevisionWithFirstChild(Object parent, Object child) {
        return parent instanceof Revision && child instanceof Text;
    }

    public static boolean isSystemReleaseRevisionInstance(Object parent, Object child) {
        return (parent instanceof Revision && child instanceof Text) ||
        (parent instanceof EditHistory && child instanceof Revision);
    }
    /**
     * @param parent
     * @param child
     * @return
     */
    public static boolean isSytemRelease(Object parent, Object child) {
        return (parent instanceof SystemRelease && child instanceof EntityDescription );
    }

    /**
     * @param parent
     * @param child
     * @return
     */
    public static boolean isValueSet(Object parent, Object child) {
        return child instanceof ValueSetDefinition && parent instanceof ValueSetDefinitions;
    }
    
    public static boolean isValueSetDefinition(Object parent, Object child) {
        return child instanceof ValueSetDefinition && parent == null;
    }
    /**
     * @param parent
     * @param child
     * @return
     */
    public static boolean isValueSetMappings(Object parent, Object child) {
        return child instanceof Mappings && parent instanceof ValueSetDefinitions;
    }

    /**
     * @param parent
     * @param child
     * @return
     */
    public static boolean isPickListMappings(Object parent, Object child) {
        return child instanceof Mappings && parent instanceof PickListDefinitions;
    }

    /**
     * @param parent
     * @param child
     * @return
     */
    public static boolean isPickListDefinition(Object parent, Object child) {
        return child instanceof PickListDefinition && parent instanceof PickListDefinitions;
    }

    public static boolean isValueSetDefinitionRevision(Object parent, Object child) {
        return child instanceof ValueSetDefinition && parent instanceof ChangedEntry;
    }

    public static boolean isPickListDefinitionRevision(Object parent, Object child) {
    return child instanceof PickListDefinition && parent instanceof ChangedEntry;
    }

    public static boolean isRevisionInstance(Object parent, Object child) {
       return child instanceof Revision && parent instanceof EditHistory;
    }

    public static boolean isRevisionWithLastChild(int lastMetaDataType, Object parent, Object child) {
     if (lastMetaDataType == CHANGE_AGENT_TYPE)
           return child instanceof String && parent instanceof Revision;
       else if (lastMetaDataType == CHANGE_INSTRUCTIONS_TYPE){
           return child instanceof Text && parent instanceof Revision;
       }
         return child instanceof ChangedEntry && parent instanceof Revision;
     
    }
    public static boolean isCodingSchemePropertiesRevision(Object parent, Object child) {
        return parent instanceof Properties && child instanceof Property;
    }
    public static boolean isCodingSchemeAssociationData(Object parent, Object child) {
        return child instanceof AssociationData && parent instanceof AssociationSource;
    }

    public static boolean isCodingSchemeEntityProperty(Object parent, Object child) {
       return child instanceof Property && parent instanceof Entity;
    }
    
    public static boolean isCodingSchemeRelation(Object parent, Object child) {
        return child instanceof EntityDescription && parent instanceof Relations;
    }
    //Looking for an empty predicate just because the model allows it.
    //runs the risk of allowing a large predicate to be populated.
    public static boolean isCodingSchemeRelationWithEmptyPredicate(Object parent, Object child) {
        return parent instanceof AssociationPredicate && child == null;
    }



}
