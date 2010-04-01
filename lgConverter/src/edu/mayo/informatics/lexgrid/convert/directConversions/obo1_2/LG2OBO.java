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
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package edu.mayo.informatics.lexgrid.convert.directConversions.obo1_2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.custom.concepts.EntitiesUtil;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.LexGrid.custom.relations.RelationsUtil;
import org.apache.commons.lang.StringUtils;

/**
 * EMF to OBO Implementation.
 * 
 * @author <A HREF="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala</A>
 * @version subversion $Revision: 2484 $ checked in on $Date: 2006-06-19
 *          15:52:21 +0000 (Mon, 19 Jun 2006) $
 */
public class LG2OBO {
    private LgMessageDirectorIF messages;

    private CodingScheme codingScheme;

    private static final String lineReturn = System.getProperty("line.separator");

    public LG2OBO(CodingScheme codingScheme, LgMessageDirectorIF messages) {
        this.messages = messages;
        this.codingScheme = codingScheme;
    }

    /**
     * Saves the Lexgrid EMF content in obo format to the outFileName
     * 
     * @param outputFileName
     * @throws Exception
     */
    public void save(File outputFile) throws Exception {
        // declared here only to make visible to finally clause; generic
        // reference
        Writer output = null;
        try {
            output = new BufferedWriter(new FileWriter(outputFile));
            output.write(toString());
        } catch (Exception e) {
            messages.fatalAndThrowException("Failed to save the Lexgrid content into OBO format", e);
        } finally {
            // flush and close both "output" and its underlying FileWriter
            if (output != null)
                output.close();
        }

    }

    /**
     * Generate the OBO 1.2 representation of the Lexgrid EMF content
     */
    public String toString() {
        String oboText = generateOBOHeader();
        oboText += generateOBOContent();
        oboText += generateTypeDef();
        return oboText;

    }

    String generateOBOHeader() {
        String str = "format-version: 1.2 " + lineReturn;
        str += "default-namespace: " + codingScheme.getFormalName() + lineReturn;
        str += lineReturn;
        return str;
    }

    String generateOBOContent() {
        String str = "";
        // Try to find an existing target to match
        for (Entity en : codingScheme.getEntities().getEntity()) {
            if (en.getEntityCode()!= null && en.getEntityCode().trim().length()>0){
                String entityCode = en.getEntityCode();
                if (!"@".equals(entityCode) || !"@@".equals(entityCode))
                    str += generateOBOTerm(en);
            }
        }
        return str;
    }

    String generateOBOTerm(Entity codedEntry) {
        Property property;
        String str = "[Term]" + lineReturn;
        // OBO id
        str += "id: " + codedEntry.getEntityCode() + lineReturn;
        // OBO name
        str += "name: " + codedEntry.getEntityDescription() + lineReturn;

        // OBO def
        property = EntitiesUtil.resolveProperty(codedEntry, OBO2EMFConstants.PROPERTY_DEFINITION);
        if (property != null) {
            str += "def: \"" + property.getValue().getContent() + "\"" + generateSource(property) + lineReturn;
        }
        // OBO comment
        property = EntitiesUtil.resolveProperty(codedEntry, OBO2EMFConstants.PROPERTY_COMMENT);
        if (property != null) {
            str += "comment: " + property.getValue().getContent() + lineReturn;
        }
        // OBO subset
        str += generateSubset(codedEntry);
        // OBO synonym
        str += generateSynonyms(codedEntry);

        if (!codedEntry.getIsActive().booleanValue()) {
            str += "is_obsolete: true " + lineReturn;
        }

        str += generateRelations(codedEntry);
        str += lineReturn;
        return str;
    }

    String generateSynonyms(Entity codedEntry) {
        String str = "";
        List list = EntitiesUtil.getNonPreferredPresentation(codedEntry);
        for (Iterator items = list.iterator(); items.hasNext();) {
            Presentation presentation = (Presentation) items.next();
            str += "synonym: " + presentation.getValue().getContent() + " " + presentation.getDegreeOfFidelity()
                    + generateSource(presentation);
            str += lineReturn;
        }
        return str;

    }

    String generateSource(Property property) {
        String str = "";
        List sources = Arrays.asList(property.getSource());
        for (Iterator items = sources.iterator(); items.hasNext();) {
            if (str.length() == 0) {
                str = " [";
            }
            Source source = (Source) items.next();
            str += source.getContent();
            if (StringUtils.isNotBlank(source.getSubRef())) {
                str += ":" + source.getSubRef();
            }
            str += ", ";
        }

        if (StringUtils.isNotBlank(str)) {
            str = str.substring(0, str.lastIndexOf(","));
            str += "] ";
        }
        return str;
    }

    String generateSubset(Entity codedEntry) {
        String str = "";
        List list = EntitiesUtil.resolveProperties(codedEntry, OBO2EMFConstants.PROPERTY_SUBSET);
        for (Iterator items = list.iterator(); items.hasNext();) {
            Property property = (Property) items.next();
            str += "subset: " + property.getValue().getContent();
            str += lineReturn;
        }
        return str;

    }

    boolean isBuiltInRelationName(String relation_name) {
        List built_in_relationNames = Arrays.asList(OBO2EMFConstants.BUILT_IN_ASSOCIATIONS);
        return built_in_relationNames.contains(relation_name);
    }

    String generateRelations(Entity codedEntry) {
        String str = "";
        str += generateIsARelation(codedEntry);

        List associations = RelationsUtil.resolveAssociations(codingScheme);
        for (Iterator items = associations.iterator(); items.hasNext();) {
            Association ac = (Association) items.next();
            String as_name = ac.getEntityCode();
            if (isBuiltInRelationName(as_name)) {
                str += generateBuiltInRelation(codedEntry, as_name);
            } else {
                str += generateNonBuiltInRelation(codedEntry, as_name);
            }
        }

        return str;

    }

    String generateBuiltInRelation(Entity codedEntry, String association_name) {
        String str = "";
        List association_list = RelationsUtil.resolveAssociations(codingScheme, association_name);
        Collection ac_instances = RelationsUtil.resolveRelationSources(codingScheme, codedEntry, association_list);
        for (Iterator items = ac_instances.iterator(); items.hasNext();) {
            AssociationSource ai = (AssociationSource) items.next();
            for (Iterator targets = ai.getTarget().iterator(); targets.hasNext();) {
                AssociationTarget target = (AssociationTarget) targets.next();
                if (target != null && !"@@".equals(target.getTargetEntityCode())) {
                    str += association_name + ": " + target.getTargetEntityCode() + lineReturn;
                }
            }
        }
        return str;
    }

    String generateNonBuiltInRelation(Entity codedEntry, String association_name) {
        String str = "";
        List association_list = RelationsUtil.resolveAssociations(codingScheme, association_name);
        Collection instances = RelationsUtil.resolveRelationSources(codingScheme, codedEntry, association_list);
        for (Iterator items = instances.iterator(); items.hasNext();) {
            AssociationSource ai = (AssociationSource) items.next();
            for (Iterator targets = ai.getTarget().iterator(); targets.hasNext();) {
                AssociationTarget target = (AssociationTarget) targets.next();
                if (target != null && !"@@".equals(target.getTargetEntityCode())) {
                    str += "relationship: " + association_name + " " + target.getTargetEntityCode() + lineReturn;
                }
            }
        }
        return str;
    }

    /**
     * Need special processing to generate the is_a obo relationship because the
     * is_a relationship is defined in LexGrid using hasSubType. entity1 is_a
     * entity2 gets transformed to concept2 hasSubType concept1.
     * 
     * @param codedEntry
     * @return
     */
    String generateIsARelation(Entity codedEntry) {
        String str = "";
        List association_list = RelationsUtil
                .resolveAssociations(codingScheme, OBO2EMFConstants.ASSOCIATION_HASSUBTYPE);
        Collection targets = RelationsUtil.resolveRelationTargets(codingScheme, codedEntry, association_list);
        for (Iterator items = targets.iterator(); items.hasNext();) {
            AssociationTarget at = (AssociationTarget) items.next();
            AssociationSource instance = (AssociationSource) at.getContainer(AssociationSource.class, 0);
            str += OBO2EMFConstants.ASSOCIATION_ISA + ": " + instance.getSourceEntityCode() + lineReturn;

        }
        return str;
    }

    String generateTypeDef() {
        String str = "";

        List containers = RelationsUtil.resolveRelationContainers(codingScheme);
        for (Iterator items = containers.iterator(); items.hasNext();) {
            Relations relations = (Relations) items.next();
            for (Iterator i = relations.getAssociation().iterator(); i.hasNext();) {
                Association association = (Association) i.next();
                String assoc_name = association.getEntityCode();
                if (!isBuiltInRelationName(assoc_name) && !assoc_name.equalsIgnoreCase("-multi-assn-@-root-")) {
                    str += "[Typedef]" + lineReturn;
                    str += "id: " + assoc_name + lineReturn;
                    str += "name: " + assoc_name + lineReturn;
                    str += lineReturn;
                }
            }
        }

        return str;
    }

    String println(String str) {
        return str + " \n";
    }

}