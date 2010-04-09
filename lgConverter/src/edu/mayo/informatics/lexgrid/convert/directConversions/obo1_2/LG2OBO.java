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
import java.util.List;

import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.custom.concepts.EntitiesUtil;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.naming.SupportedAssociationQualifier;
import org.LexGrid.relations.AssociationEntity;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.custom.relations.RelationsUtil;
import org.apache.commons.lang.StringUtils;
import org.lexevs.logging.LoggerFactory;

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
            if (en.getEntityCode()!= null && en.getEntityCode().trim().length()>0
                    && en instanceof AssociationEntity == false){
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
        str += "name: " + codedEntry.getEntityDescription().getContent() + lineReturn;

        // OBO def
        property = EntitiesUtil.resolveProperty(codedEntry, OBO2LGConstants.PROPERTY_DEFINITION);
        if (property != null) {
            str += "def: \"" + property.getValue().getContent() + "\"" + generateSource(property) + lineReturn;
        }
        // OBO comment
        property = EntitiesUtil.resolveProperty(codedEntry, OBO2LGConstants.PROPERTY_COMMENT);
        if (property != null) {
            str += "comment: " + property.getValue().getContent() + lineReturn;
        }
        // OBO xref
        List<Property> propertyList = EntitiesUtil.resolveProperties(codedEntry, OBO2LGConstants.PROPERTY_XREF);
        for (Property p : propertyList) {
            str += "xref: " + p.getValue().getContent() + lineReturn;
        }
        
        // OBO subset
        str += generateSubset(codedEntry);
        // OBO synonym
        str += generateSynonyms(codedEntry);

        if (codedEntry.getIsActive() != null && !codedEntry.getIsActive().booleanValue()) {
            str += "is_obsolete: true " + lineReturn;
        }

        str += generateRelations(codedEntry);
        str += lineReturn;
        return str;
    }

    String generateSynonyms(Entity codedEntry) {
        String str = "";
        List<Presentation> list = EntitiesUtil.getNonPreferredPresentation(codedEntry);
        for (Presentation presentation : list) {
            if (presentation.getDegreeOfFidelity() != null)
                str += "synonym: " + "\"" + presentation.getValue().getContent() + "\"" + " " + presentation.getDegreeOfFidelity()
                    + generateSource(presentation);
            else
                str += "synonym: " + "\"" + presentation.getValue().getContent() + "\"";
            str += lineReturn;
        }
        return str;

    }

    String generateSource(Property property) {
        String str = "";
        List<Source> sources = Arrays.asList(property.getSource());
        for(Source source : sources) {
            if (str.length() == 0) {
                str = " [";
            }
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
        List<Property> list = EntitiesUtil.resolveProperties(codedEntry, OBO2LGConstants.PROPERTY_SUBSET);
        for (Property property: list){
            str += "subset: " + property.getValue().getContent();
            str += lineReturn;
        }
        return str;

    }

    boolean isBuiltInRelationName(String relation_name) {
        List<String> built_in_relationNames = Arrays.asList(OBO2LGConstants.BUILT_IN_ASSOCIATIONS);
        return built_in_relationNames.contains(relation_name);
    }

    String generateRelations(Entity codedEntry) {
        String str = "";
        str += generateIsARelation(codedEntry);

        for (Relations relations: codingScheme.getRelations()) {
            for (AssociationPredicate ap : relations.getAssociationPredicate()){
                String as_name = ap.getAssociationName();
                if (isBuiltInRelationName(as_name)) {
                    str += generateBuiltInRelation(codedEntry, as_name);
                } else {
                    str += generateNonBuiltInRelation(codedEntry, as_name);
                }
            }
        }
        return str;

    }

    String generateBuiltInRelation(Entity codedEntry, String association_name) {
        String str = "";
        List<AssociationPredicate> association_list = RelationsUtil.resolveAssociationPredicates(codingScheme, association_name);
        List<AssociationSource> aSrcs = RelationsUtil.resolveRelationSources(codedEntry, association_list);
        
        for(AssociationSource src : aSrcs){
            for(AssociationTarget tgt: src.getTarget()) {
                if (tgt != null && !"@@".equals(tgt.getTargetEntityCode())) {
                    str += association_name + ": " + tgt.getTargetEntityCode() + lineReturn;
                }
            }
        }
        
        return str;
    }

    String generateNonBuiltInRelation(Entity codedEntry, String association_name) {
        String str = "";
        List<AssociationPredicate> association_list = RelationsUtil.resolveAssociationPredicates(codingScheme, association_name);
        List<AssociationSource> srcList = RelationsUtil.resolveRelationSources(codedEntry, association_list);
        
        for (AssociationSource src : srcList) {
            for (AssociationTarget tgt : src.getTarget()) {
                if (tgt != null && !"@@".equals(tgt.getTargetEntityCode())) {
                    str += "relationship: " + association_name + " " + tgt.getTargetEntityCode() + lineReturn;
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
        List<AssociationPredicate> association_list = RelationsUtil.resolveAssociationPredicates(codingScheme, OBO2LGConstants.ASSOCIATION_HASSUBTYPE);
        List<AssociationSource> sources = RelationsUtil.resolveRelationTargets(codedEntry, association_list);
        for(AssociationSource as: sources) {
            str += OBO2LGConstants.ASSOCIATION_ISA + ": " + as.getSourceEntityCode() + lineReturn;
        }
        return str;
    }

    String generateTypeDef() {
        String str = "";

        for (Relations relations : codingScheme.getRelations()){
            for (AssociationPredicate ap : relations.getAssociationPredicate()) {
                String code = ap.getAssociationName();
                if (!isBuiltInRelationName(code) && !code.equalsIgnoreCase("-multi-assn-@-root-")) {
                    str += "[Typedef]" + lineReturn;
                    str += "id: " + code + lineReturn;
                    str += "name: " + code + lineReturn;
                    str += lineReturn;
                }
            }
        }

        return str;
    }

    String println(String str) {
        return str + " \n";
    }
    
    public static void main(String[] args) {
//        CodingScheme cs = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().
//        getCodingSchemeService().getCodingSchemeByUriAndVersion(codingSchemeUri, codingSchemeVersion);
        CodingScheme cs = new CodingScheme();
        cs.setFormalName("formalName");
        cs.setCodingSchemeName("codingSchemeName");
        cs.setCodingSchemeURI("codingSchemeURI");
        cs.setRepresentsVersion("representsVersion");
        EntityDescription ed = new EntityDescription();
        ed.setContent("enttiy description");
        cs.setEntityDescription(ed);
        Text t = new Text();
        t.setContent("copy right");
        cs.setCopyright(t);
        cs.setDefaultLanguage("english");
        
        //new entity 1
        Entity entity = new Entity();
        entity.setEntityCode("entityCode1");
        entity.setEntityCodeNamespace("entityCodeNamespace");
        entity.addEntityType(SQLTableConstants.TBL_CONCEPT);
        EntityDescription enDesc = new EntityDescription();
        enDesc.setContent("entityDescription");
        entity.setEntityDescription(enDesc);
        entity.setIsActive(true);
        
        Presentation presentation = new Presentation();
        presentation.setPropertyType(SQLTableConstants.TBLCOLVAL_PRESENTATION);
        presentation.setPropertyName("presentation propertyName");
        presentation.setIsPreferred(false);
        Text value = new Text();
        value.setContent("presentation value");
        presentation.setValue(value);
        
        entity.addPresentation(presentation);
        
        
        cs.setEntities(new Entities());
        cs.getEntities().addEntity(entity);
        
        //new entity 2
        Entity entity2 = new Entity();
        entity2.setEntityCode("entityCode2");
        entity2.setEntityCodeNamespace("entityCodeNamespace");
        entity2.addEntityType(SQLTableConstants.TBL_CONCEPT);
        enDesc = new EntityDescription();
        enDesc.setContent("entityDescription");
        entity2.setEntityDescription(enDesc);
        entity2.setIsActive(false);
        
        presentation = new Presentation();
        presentation.setPropertyType(SQLTableConstants.TBLCOLVAL_PRESENTATION);
        presentation.setPropertyName("presentation propertyName");
        presentation.setIsPreferred(true);
        value = new Text();
        value.setContent("presentation value");
        presentation.setValue(value);
        
        entity2.addPresentation(presentation);
        
        cs.getEntities().addEntity(entity2);
        
        // relation
        Relations relations = new Relations();
        relations.setContainerName(cs.getCodingSchemeName()+ " relation");
        AssociationPredicate ap = new AssociationPredicate();
        ap.setAssociationName("hasSubtype");
        AssociationSource as = new AssociationSource();
        as.setSourceEntityCode("entityCode1");
        as.setSourceEntityCodeNamespace("entityCodeNamespace");
        AssociationTarget at = new AssociationTarget();
        at.setTargetEntityCode("entityCode2");
        at.setTargetEntityCodeNamespace("entityCodeNamespace");
        as.addTarget(at);
        ap.addSource(as);
        relations.addAssociationPredicate(ap);
        
        // relation 2
        ap = new AssociationPredicate();
        ap.setAssociationName("is_a");
        as = new AssociationSource();
        as.setSourceEntityCode("entityCode1");
        as.setSourceEntityCodeNamespace("entityCodeNamespace");
        at = new AssociationTarget();
        at.setTargetEntityCode("entityCode2");
        at.setTargetEntityCodeNamespace("entityCodeNamespace");
        as.addTarget(at);
        ap.addSource(as);
        
        relations.addAssociationPredicate(ap);
        
        cs.addRelations(relations);
        
        LG2OBO lg2Obo = new LG2OBO(cs, LoggerFactory.getLogger());
        File file = new File("2obo.obo");
        try {
            lg2Obo.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

}