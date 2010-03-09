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
package edu.mayo.informatics.lexgrid.convert.emfConversions.obo1_2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Comment;
import org.LexGrid.concepts.Concept;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.custom.concepts.EntityFactory;
import org.LexGrid.custom.relations.RelationsUtil;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.naming.SupportedAssociationQualifier;
import org.LexGrid.naming.SupportedHierarchy;
import org.LexGrid.naming.SupportedProperty;
import org.LexGrid.naming.SupportedSource;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.LexGrid.util.SimpleMemUsageReporter;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.commons.lang.StringUtils;

import edu.mayo.informatics.resourcereader.obo.OBOAbbreviation;
import edu.mayo.informatics.resourcereader.obo.OBOAbbreviations;
import edu.mayo.informatics.resourcereader.obo.OBOContents;
import edu.mayo.informatics.resourcereader.obo.OBODbxref;
import edu.mayo.informatics.resourcereader.obo.OBORelation;
import edu.mayo.informatics.resourcereader.obo.OBORelations;
import edu.mayo.informatics.resourcereader.obo.OBOResourceReader;
import edu.mayo.informatics.resourcereader.obo.OBOSynonym;
import edu.mayo.informatics.resourcereader.obo.OBOTerm;
import edu.mayo.informatics.resourcereader.obo.OBOTerms;

/**
 * OBO To EMF Implementation.
 * 
 * @author <A HREF="mailto:kanjamala.pradip@mayo.edu"> Pradip Kanjamala</A>
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @author <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer</A>
 * @author <A HREF="mailto:kevin.peterson@mayo.edu">Kevin Peterson</A>
 * @version subversion $Revision: 2917 $ checked in on $Date: 2006-08-16
 *          21:49:43 +0000 (Wed, 16 Aug 2006) $
 */
public class OBO2EMFDynamicMapHolders {
    private LgMessageDirectorIF messages_;
    private Hashtable<String, String> properties_ = null;
    private Hashtable<String, String> sources_ = null;
    private Hashtable<String, String> supportedAssociationHashtable = null;
    private Hashtable<String, AssociationPredicate> associationHash_ = new Hashtable<String, AssociationPredicate>();
    private List<AssociationPredicate> assocPredicateList = null;

    private Entities allConcepts_ = null;
    private List<Entity> codedEntriesList = null;
    CodingScheme cs;

    private long propertyCounter = 0;
    private long anonymousCounter = 0;
    private Map<String, AssociationSource> assocName_SrcCodeStr2assocSrcMap = null; // association+source

    // name
    // to
    // association
    // instances

    public OBO2EMFDynamicMapHolders(LgMessageDirectorIF messages) {
        messages_ = messages;
    }

    public boolean prepareCSClass(OBOResourceReader oboReader, CodingScheme csclass) {
        boolean success = true;
        this.cs = csclass;
        try {
            properties_ = OBO2EMFStaticMapHolders.getFixedProperties();
            assocName_SrcCodeStr2assocSrcMap = new HashMap<String, AssociationSource>();

            sources_ = OBO2EMFStaticMapHolders.getFixedSources();
            supportedAssociationHashtable = OBO2EMFStaticMapHolders.getFixedAssociations();
            OBOContents contents = oboReader.getContents(false, false);
            OBORelations relations = contents.getOBORelations();

            allConcepts_ = csclass.getEntities();

            if (allConcepts_ == null) {
                allConcepts_ = new Entities();
                csclass.setEntities(allConcepts_);
            }

            codedEntriesList = allConcepts_.getEntityAsReference();

            // Creating the relation instance
            List<Relations> relationsList = csclass.getRelationsAsReference();

            Relations allRelations = new Relations();
            allRelations.setContainerName(SQLTableConstants.TBLCOLVAL_DC_RELATIONS);

            relationsList.add(allRelations);
            assocPredicateList = allRelations.getAssociationPredicateAsReference();

            OBOTerms terms = contents.getOBOTerms();
            Collection<OBOTerm> termList = terms.getAllMembers();

            if ((termList != null) && (!termList.isEmpty())) {
                try {
                    long busyCtr = 0;
                    SimpleMemUsageReporter.snapshot();
                    for (OBOTerm oboTerm : termList) {
                        storeConceptAndRelations(oboReader, oboTerm, csclass);
                        if ((++busyCtr) % 5000 == 0) {
                            messages_.busy();
                            messages_.debug("Processed " + busyCtr + " OBO Terms to EMF Objects");
                        }

                    }
                    messages_.debug("Finished processing. Totally processed " + busyCtr + " OBO Terms to EMF Objects");

                } catch (Exception e) {
                    messages_.fatalAndThrowException("Failed while loading terms and relationships! \n"
                            + e.getMessage());
                }
            }

        } catch (Exception e) {
            success = false;
            messages_.error("Unexpected Error", e);
        }

        return success;
    }

    public void storeConceptAndRelations(OBOResourceReader oboReader, OBOTerm oboTerm, CodingScheme csclass) {
        try {
            // OBO Term should not be null and either one of ID or term name
            // should be there.
            OBOContents contents = oboReader.getContents(false, false);
            if ((oboTerm != null)
                    && ((!OBO2EMFUtils.isNull(oboTerm.getId())) || (!OBO2EMFUtils.isNull(oboTerm.getName())))) {
                propertyCounter = 0;

                Concept concept = EntityFactory.createConcept();
                concept.setEntityCodeNamespace(csclass.getCodingSchemeName());

                if (!OBO2EMFUtils.isNull(oboTerm.getId()))
                    concept.setEntityCode(oboTerm.getId());
                else
                    concept.setEntityCode(oboTerm.getName());

                String termDesc = "";

                if (!OBO2EMFUtils.isNull(oboTerm.getName()))
                    termDesc = oboTerm.getName();
                else
                    termDesc = oboTerm.getId();

                EntityDescription ed = new EntityDescription();
                ed.setContent(termDesc);
                concept.setEntityDescription(ed);

                if (oboTerm.isObsolete()) {
                    concept.setIsActive(new Boolean(false));
                }
                Presentation tp = new Presentation();
                Text txt = new Text();
                txt.setContent(termDesc);
                tp.setValue(txt);
                tp.setIsPreferred(new Boolean(true));
                tp.setPropertyName(OBO2EMFConstants.PROPERTY_TEXTPRESENTATION);

                String preferredPresetationID = OBO2EMFConstants.PROPERTY_ID_PREFIX + (++propertyCounter);
                tp.setPropertyId(preferredPresetationID);
                concept.getPresentationAsReference().add(tp);

                if (!OBO2EMFUtils.isNull(oboTerm.getComment())) {
                    Comment comment = new Comment();
                    txt = new Text();
                    txt.setContent((String) OBO2EMFUtils.removeInvalidXMLCharacters(oboTerm.getComment(), null));
                    comment.setValue(txt);
                    comment.setPropertyName(OBO2EMFConstants.PROPERTY_COMMENT);
                    comment.setPropertyId(OBO2EMFConstants.PROPERTY_ID_PREFIX + (++propertyCounter));
                    concept.getCommentAsReference().add(comment);
                }

                if (!OBO2EMFUtils.isNull(oboTerm.getDefinition())) {
                    Definition defn = new Definition();
                    txt = new Text();
                    txt.setContent((String) OBO2EMFUtils.removeInvalidXMLCharacters(oboTerm.getDefinition(), null));
                    defn.setValue(txt);
                    defn.setPropertyName(OBO2EMFConstants.PROPERTY_DEFINITION);
                    defn.setPropertyId(OBO2EMFConstants.PROPERTY_ID_PREFIX + (++propertyCounter));
                    concept.getDefinitionAsReference().add(defn);

                    OBOAbbreviations abbreviations = contents.getOBOAbbreviations();
                    addOBODbxrefAsSource(defn, oboTerm.getDefinitionSources(), abbreviations);

                }

                addPropertyAttribute(oboTerm.getSubset(), concept, OBO2EMFConstants.PROPERTY_SUBSET);
                Vector<String> dbxref_src_vector = OBODbxref.getSourceAndSubRefAsVector(oboTerm.getDbXrefs());
                addPropertyAttribute(dbxref_src_vector, concept, "xref");
                // Add Synonyms as presentations
                Vector<OBOSynonym> synonyms = oboTerm.getSynonyms();

                if ((synonyms != null) && (!synonyms.isEmpty())) {
                    for (OBOSynonym synonym : synonyms) {
                        Presentation ip = new Presentation();
                        txt = new Text();
                        txt.setContent((String) synonym.getText());
                        ip.setValue(txt);
                        ip.setPropertyName(OBO2EMFConstants.PROPERTY_SYNONYM);
                        String synPresID = OBO2EMFConstants.PROPERTY_ID_PREFIX + (++propertyCounter);
                        ip.setPropertyId(synPresID);
                        concept.getPresentationAsReference().add(ip);

                        String scope = synonym.getScope();
                        if (scope != null && scope.length() > 0)
                            ip.setDegreeOfFidelity(scope);

                        // Add source information
                        Vector<OBODbxref> dbxref = OBODbxref.parse(synonym.getDbxref());
                        OBOAbbreviations abbreviations = contents.getOBOAbbreviations();
                        addOBODbxrefAsSource(ip, dbxref, abbreviations);

                    }
                }

                Vector<String> vec_altIds = oboTerm.getAltIds();

                if ((vec_altIds != null) && (!vec_altIds.isEmpty())) {
                    for (String altId : vec_altIds) {

                        if (StringUtils.isNotBlank(altId)) {

                            Property emfProp = new Property();
                            emfProp.setPropertyName(OBO2EMFConstants.PROPERTY_ALTID);
                            String prop_id = OBO2EMFConstants.PROPERTY_ID_PREFIX + (++propertyCounter);
                            emfProp.setPropertyId(prop_id);

                            txt = new Text();
                            txt.setContent(altId);
                            emfProp.setValue(txt);

                            concept.getPropertyAsReference().add(emfProp);

                        }
                    }
                }

                String created_by = oboTerm.getCreated_by();
                if (StringUtils.isNotBlank(created_by)) {
                    addPropertyAttribute(created_by, concept, OBO2EMFConstants.PROPERTY_CREATED_BY);

                }

                String creation_date = oboTerm.getCreation_date();
                if (StringUtils.isNotBlank(creation_date)) {
                    addPropertyAttribute(creation_date, concept, OBO2EMFConstants.PROPERTY_CREATION_DATE);
                }

                Hashtable<String, Vector<String>> relationships = oboTerm.getRelationships();
                if (relationships != null) {
                    for (Enumeration<String> e = relationships.keys(); e.hasMoreElements();) {
                        String relName = e.nextElement();
                        OBORelations relations = contents.getOBORelations();
                        OBORelation relation = relations.getMemberById(relName);
                        Vector<String> targets = relationships.get(relName);
                        if (targets != null) {
                            addAssociationAttribute(concept, relation, targets);
                        }
                    }
                }

                codedEntriesList.add(concept);

            }
        }

        catch (Exception e) {
            e.printStackTrace();
            messages_.info("Failed to store concept " + oboTerm.getName() + "(" + oboTerm.getId() + ")");
        }
    }

    private void addOBODbxrefAsSource(Property prop, Vector<OBODbxref> srcVector, OBOAbbreviations abbreviations) {
        if ((srcVector != null) && (srcVector.size() > 0)) {
            for (int i = 0; i < srcVector.size(); i++) {
                OBODbxref dbxref = (OBODbxref) srcVector.elementAt(i);
                String src = (String) dbxref.getSource();
                OBOAbbreviation abb = null;
                if (!OBO2EMFUtils.isNull(src)) {
                    String srcVal = src;

                    if (abbreviations != null) {
                        abb = getAbbreviationByCodeOrURL(abbreviations, src);
                        if ((abb != null) && (!OBO2EMFUtils.isNull(abb.getAbbreviation())))
                            srcVal = abb.getAbbreviation();
                    }

                    Source s = new Source();
                    s.setContent(srcVal);
                    s.setSubRef(dbxref.getSubrefAndDescription());
                    prop.getSourceAsReference().add(s);

                    if (abb == null) {
                        abb = new OBOAbbreviation();
                        abb.abbreviation = srcVal;
                        abb.genericURL = srcVal;
                    }
                    addSource(abb);
                }
            }
        }
    }

    public OBOAbbreviation getAbbreviationByCodeOrURL(OBOAbbreviations abbreviations, String src) {
        OBOAbbreviation abb = null;
        abb = abbreviations.getMemberByName(src);
        if (abb == null)
            abb = abbreviations.getOBOAbbreviationByURL(src);

        return abb;
    }

    private void addPropertyAttribute(String value, Concept concept, String propertyName) {
        if (OBO2EMFUtils.isNull(value))
            return;

        Collection<String> values = new ArrayList<String>();
        values.add(value);
        addPropertyAttribute(values, concept, propertyName);
    }

    private String addPropertyAttribute(Collection<String> values, Concept concept, String propertyName) {
        String propertyID = null;
        try {
            if ((values != null) && (!values.isEmpty())) {
                if (!properties_.contains(propertyName)) {
                    properties_.put(propertyName, propertyName);
                }

                for (String value : values) {
                    if (!OBO2EMFUtils.isNull(value)) {
                        Property pc = new Property();
                        pc.setPropertyName(propertyName);
                        propertyID = OBO2EMFConstants.PROPERTY_ID_PREFIX + (++propertyCounter);
                        pc.setPropertyId(propertyID);
                        Text txt = new Text();
                        txt.setContent((String) value);
                        pc.setValue(txt);
                        concept.getPropertyAsReference().add(pc);
                    }
                }
            }
        } catch (Exception e) {
            messages_.info("Failed while storing value for property: " + propertyName);
        }

        return propertyID;
    }

    private boolean processSpecialAssociationLikeOWL(Concept concept, String relation_name, Collection<String> targets) {
        boolean processed = false;
        List<String> special_relationNames = Arrays.asList(OBO2EMFConstants.BUILT_IN_SPECIAL_ASSOCIATIONS);
        if (special_relationNames.contains(relation_name)) {
            processed = true;

            Concept anon_eq = createAnonymousConcept("(" + relation_name + ")");
            addAssociation(concept, "equivalentClass", null, null, null, anon_eq);

            Iterator<String> itr = targets.iterator();
            String target1 = "";
            String target2 = "";
            if (itr.hasNext()) {
                target1 = itr.next();
                processAnonListTarget(anon_eq, target1);

            }
            if (itr.hasNext()) {
                target2 = itr.next();
                processAnonListTarget(anon_eq, target2);
            }

        }
        return processed;
    }

    void processAnonListTarget(Concept anon_concept, String target) {
        String[] result = target.split("\\s");
        if (result.length == 1) {
            addAssociation(anon_concept, "hasElement", null, null, null, result[0]);
        }
        if (result.length == 2) {
            String entityDescription = "(Restriction)\nsomeValuesFrom : " + result[1] + "\nonProperty : " + result[0];
            Concept anon_restriction = createAnonymousConcept(entityDescription);
            addAssociation(anon_concept, "hasElement", null, null, null, anon_restriction);
            addAssociation(anon_restriction, result[0], result[0], null, "someValuesFrom", result[1]);

        }

    }

    private Concept createAnonymousConcept(String entityDescription) {
        Concept concept = EntityFactory.createConcept();
        String conceptCode = OBO2EMFConstants.ANONYMOUS_TEXTPRESENTATION + (++anonymousCounter);
        if (entityDescription == null) {
            entityDescription = conceptCode;
        }
        Presentation tp = new Presentation();
        Text txt = new Text();
        txt.setContent(entityDescription);
        tp.setValue(txt);
        tp.setIsPreferred(new Boolean(true));
        tp.setPropertyName(OBO2EMFConstants.PROPERTY_TEXTPRESENTATION);
        concept.setEntityCode(conceptCode);
        EntityDescription ed = new EntityDescription();
        ed.setContent(entityDescription);
        concept.setEntityDescription(ed);
        String preferredPresetationID = OBO2EMFConstants.PROPERTY_ID_PREFIX + anonymousCounter;
        tp.setPropertyId(preferredPresetationID);
        concept.getPresentationAsReference().add(tp);
        concept.setIsAnonymous(new Boolean(true));
        codedEntriesList.add(concept);
        return concept;
    }

    private void addAssociation(Concept source, String association_name, String association_forwardname,
            String association_reversename, String association_qualifier, Concept target) {
        addAssociation(source, association_name, association_forwardname, association_reversename,
                association_qualifier, target.getEntityCode());
    }

    private void addAssociation(Concept source, String association_name, String association_forwardname,
            String association_reversename, String association_qualifier, String target_code) {
        if (source == null || association_name == null || target_code == null) {
            return;
        }

        AssociationPredicate assocPredicate;
        if (!associationHash_.containsKey(association_name)) {

            assocPredicate = new AssociationPredicate();
            assocPredicate.setAssociationName(association_name);

            associationHash_.put(association_name, assocPredicate);

            // TODO: Deal with forward name
            // assocPredicate.setForwardName(association_name);
            Relations rel = getOrCreateRelations();
            rel.getAssociationPredicateAsReference().add(assocPredicate);
            if (!supportedAssociationHashtable.containsKey(association_name)) {
                supportedAssociationHashtable.put(association_name, association_name);
            }
        } else {
            assocPredicate = associationHash_.get(association_name);
        }
        AssociationSource as = getOrCreateAssociationInstance(assocPredicate, source);

        AssociationTarget at = new AssociationTarget();
        if (association_qualifier != null) {
            AssociationQualification aq = new AssociationQualification();
            aq.setAssociationQualifier(association_qualifier);
            at.getAssociationQualificationAsReference().add(aq);
            addSupportedAssociationQualifier(association_qualifier);
        }

        at.setTargetEntityCode(target_code);
        at.setTargetEntityCodeNamespace(source.getEntityCodeNamespace());
        RelationsUtil.subsume(as, at);

    }

    Relations getOrCreateRelations() {
        Relations relations = null;

        for (Relations r : cs.getRelations()) {
            if (r.getContainerName().equals("relations")) {
                relations = r;
                break;
            }
        }
        if (relations == null) {
            relations = new Relations();
            relations.setContainerName("relations");
            cs.getRelationsAsReference().add(relations);
        }
        return relations;
    }

    private void addSupportedAssociationQualifier(String association_qualifier) {

        if (cs == null || association_qualifier == null)
            return;
        Iterator<SupportedAssociationQualifier> itr = cs.getMappings().iterateSupportedAssociationQualifier();
        while (itr.hasNext()) {
            SupportedAssociationQualifier saq = itr.next();
            if (saq.getLocalId().equals(association_qualifier)) {
                // We already have it added, return
                return;
            }
        }
        SupportedAssociationQualifier qualifier = new SupportedAssociationQualifier();
        qualifier.setLocalId(association_qualifier);
        cs.getMappings().getSupportedAssociationQualifierAsReference().add(qualifier);

    }

    private void addAssociationAttribute(Concept concept, OBORelation oboRelation, Collection<String> targets) {
        try {
            if ((oboRelation == null) || (concept == null) || (targets == null))
                return;

            if ((OBO2EMFUtils.isNull(oboRelation.getId())) && (OBO2EMFUtils.isNull(oboRelation.getName())))
                return;

            if (OBO2EMFUtils.isNull(oboRelation.getName()))
                oboRelation.setName(oboRelation.getId());

            if (OBO2EMFUtils.isNull(oboRelation.getId()))
                oboRelation.setId(oboRelation.getName());

            String relation = oboRelation.getId();

            if (processSpecialAssociationLikeOWL(concept, relation, targets))
                return;
            AssociationPredicate assocPredictate = null;
            boolean createdNew = false;

            try {
                if (associationHash_.containsKey(relation)) {
                    assocPredictate = associationHash_.get(relation);

                } else {
                    // System.out.println("Creating Relation=" + relation);
                    assocPredictate = new AssociationPredicate();
                    assocPredictate.setAssociationName(oboRelation.getId());
                    // TODO: Deal with forward name and flags
                    // assocClass.setForwardName(oboRelation.getId());
                    // assocClass.setIsTransitive(oboRelation.getIsTransitive());
                    // assocClass.setIsSymmetric(oboRelation.getIsSymmetric());
                    // assocClass.setIsReflexive(oboRelation.getIsReflexive());
                    // assocClass.setIsAntiSymmetric(oboRelation.getIsAntiSymmetric());
                    createdNew = true;
                    associationHash_.put(relation, assocPredictate);
                }
            } catch (Exception e) {
                messages_.error("Error while creating/finding association object for=" + relation, e);
                return;
            }

            if (targets.size() > 0) {
                AssociationSource as = getOrCreateAssociationInstance(assocPredictate, concept);

                for (String stringValue : targets) {

                    if (!OBO2EMFUtils.isNull(stringValue)) {
                        AssociationTarget at = new AssociationTarget();
                        at.setTargetEntityCode(stringValue);
                        at.setTargetEntityCodeNamespace(concept.getEntityCodeNamespace());
                        RelationsUtil.subsume(as, at);
                    }
                }
            }

            if (createdNew) {
                assocPredicateList.add(assocPredictate);
                // It is time to store all objects
                if (!supportedAssociationHashtable.containsKey(relation)) {
                    supportedAssociationHashtable.put(relation, oboRelation.getName());
                }
            }

        } catch (Exception e) {
            messages_.error("Failed while adding association for " + oboRelation.getName(), e);
            e.printStackTrace();
        }
    }

    private AssociationSource getOrCreateAssociationInstance(AssociationPredicate assocPredicate, Concept concept) {
        String assocNameAndCode = assocPredicate.getAssociationName() + "::" + concept.getEntityCode();
        if (assocName_SrcCodeStr2assocSrcMap.containsKey(assocNameAndCode)) {
            return assocName_SrcCodeStr2assocSrcMap.get(assocNameAndCode);
        } else {
            AssociationSource ai = new AssociationSource();
            ai.setSourceEntityCode(concept.getEntityCode());
            ai.setSourceEntityCodeNamespace(concept.getEntityCodeNamespace());
            assocName_SrcCodeStr2assocSrcMap.put(assocNameAndCode, ai);
            assocPredicate.getSourceAsReference().add(ai);
            return ai;
        }
    }

    public void populateSupportedProperties(CodingScheme csclass) {
        if (csclass == null)
            return;

        List<SupportedProperty> suppProps = csclass.getMappings().getSupportedPropertyAsReference();

        try {
            if (properties_.size() > 0) {
                Iterator<String> kI = properties_.keySet().iterator();

                while (kI.hasNext()) {
                    String prpCode = null;
                    String prp = kI.next();

                    if (!OBO2EMFUtils.isNull(prp)) {
                        prpCode = (String) properties_.get(prp);

                        SupportedProperty suppProp = new SupportedProperty();

                        if (!OBO2EMFUtils.isNull(prpCode))
                            suppProp.setUri(OBO2EMFUtils.getWithOBOURN(prpCode));
                        else
                            suppProp.setUri(OBO2EMFUtils.getWithOBOURN(prp));

                        suppProp.setLocalId(prp);
                        suppProps.add(suppProp);

                    }
                }
            }
        } catch (Exception ex) {
            messages_.info("Failed while getting supported properties!");
        }
    }

    public void populateSupportedAssociations(CodingScheme csclass) {
        try {
            if (csclass == null)
                return;

            List<SupportedAssociation> suppAssoc = csclass.getMappings().getSupportedAssociationAsReference();
            for (Enumeration<String> e = supportedAssociationHashtable.keys(); e.hasMoreElements();) {
                String prpCode = e.nextElement();

                if (!OBO2EMFUtils.isNull(prpCode)) {
                    SupportedAssociation suppAss = new SupportedAssociation();
                    suppAss.setUri(OBO2EMFUtils.getWithOBOURN(prpCode));
                    // suppAss.setLocalName(OBO2EMFUtils.toNMToken(prpCode));
                    suppAss.setLocalId(prpCode);
                    suppAssoc.add(suppAss);
                }
            }
            SupportedAssociation suppAss = new SupportedAssociation();
            suppAss.setUri(OBO2EMFUtils.getWithOBOURN("-multi-assn-@-root-"));
            // suppAss.setLocalName(OBO2EMFUtils.toNMToken(prpCode));
            suppAss.setLocalId("-multi-assn-@-root-");
            suppAssoc.add(suppAss);
        } catch (Exception ex) {
            messages_.error("Failed while getting supported associations!", ex);
        }
    }

    public void populateSupportedHierarchy(CodingScheme csclass) {
        try {
            if (csclass == null)
                return;

            List<SupportedHierarchy> supportedHierList = csclass.getMappings().getSupportedHierarchyAsReference();
            SupportedHierarchy hier = new SupportedHierarchy();
            hier.setLocalId(OBO2EMFConstants.HIERARCHY_DEFAULT_NAME);
            hier.setIsForwardNavigable(Boolean.valueOf(OBO2EMFConstants.ASSOCIATION_ISA_ISFORWARDNAVIGABLE)
                    .booleanValue());
            hier.setRootCode(OBO2EMFConstants.HIERARCHY_DEFAULT_ROOT);

            if (hier.getAssociationNames() == null) {
                hier.setAssociationNames(new ArrayList<String>());
            }

            for (Enumeration<String> e = supportedAssociationHashtable.keys(); e.hasMoreElements();) {
                String prpCode = (String) e.nextElement();

                if (prpCode.equals(OBO2EMFConstants.ASSOCIATION_DEVELOPS_FROM))
                    hier.getAssociationNamesAsReference().add(OBO2EMFConstants.ASSOCIATION_DEVELOPS_FROM);
                else if (prpCode.equals(OBO2EMFConstants.ASSOCIATION_ISA))
                    hier.getAssociationNamesAsReference().add(OBO2EMFConstants.ASSOCIATION_ISA);
                else if (prpCode.equals(OBO2EMFConstants.ASSOCIATION_PART_OF))
                    hier.getAssociationNamesAsReference().add(OBO2EMFConstants.ASSOCIATION_PART_OF);
                else if (prpCode.equals(OBO2EMFConstants.ASSOCIATION_LOCATED_IN))
                    hier.getAssociationNamesAsReference().add(OBO2EMFConstants.ASSOCIATION_LOCATED_IN);
                else if (prpCode.equals(OBO2EMFConstants.ASSOCIATION_DERIVES_FROM))
                    hier.getAssociationNamesAsReference().add(OBO2EMFConstants.ASSOCIATION_DERIVES_FROM);
                else if (prpCode.equals(OBO2EMFConstants.ASSOCIATION_BROADER))
                    hier.getAssociationNamesAsReference().add(OBO2EMFConstants.ASSOCIATION_BROADER);
            }

            // Add all the associations that are defined as transitive to the
            // hierarchy
            // Get all of the Associations for this Coding Scheme
            // This doesn't work because some ontologies have both part_of and
            // has_part defined as transitive
            // Iterator associationsItr = allAssociations.iterator();
            // while (associationsItr.hasNext()) {
            // Association currentAssoc = (Association) associationsItr.next();
            // String association_name= currentAssoc.getEntityCode();
            // if (currentAssoc.getIsTransitive()!= null &&
            // currentAssoc.getIsTransitive() && !
            // hier.getAssociationNames().contains(association_name)) {
            // hier.getAssociationNames().add(association_name);
            // }
            //                  
            // }

            hier.getAssociationNamesAsReference().add("-multi-assn-@-root-");
            supportedHierList.add(hier);
        } catch (Exception ex) {
            messages_.error("Failed while getting supported associations!", ex);
        }

    }

    private void addSource(OBOAbbreviation abb) {
        if (abb == null)
            return;
        // Need to uppercase, as we want Poc and POC to be treated as one
        String key = abb.getAbbreviation().toUpperCase().trim();
        if (!sources_.containsKey(key))
            sources_.put(key, abb.getGenericURL());
    }

    public void populateSupportedSources(CodingScheme csclass) {
        if (csclass == null)
            return;

        List<SupportedSource> suppSrcs = csclass.getMappings().getSupportedSourceAsReference();
        try {
            if (sources_.size() > 0) {
                Iterator<String> kI = sources_.keySet().iterator();

                while (kI.hasNext()) {
                    String prpCode = null;
                    String prp = kI.next();

                    if (!OBO2EMFUtils.isNull(prp)) {
                        prpCode = (String) sources_.get(prp);

                        SupportedSource suppCSs = new SupportedSource();

                        if (!OBO2EMFUtils.isNull(prpCode)) {
                            // messages_.("prpCode="+prpCode+" prp="+prp);
                            if (!prpCode.startsWith("http"))
                                suppCSs.setUri(OBO2EMFConstants.OBO_URN + prpCode);
                            else
                                suppCSs.setUri(prpCode);
                        }

                        suppCSs.setLocalId(prp);
                        suppSrcs.add(suppCSs);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            messages_.info("Failed while getting supported Hierarchies!");
        }
    }

    public void setSupportedHierarchyAssociationsTransitive(CodingScheme csclass) {
        // Holder for all Supported Hierarchy Associations
        // This is a consolidated list of all the associations
        // from all of the supportedHierarchies
        ArrayList<String> supportedHierarchyAssociations = new ArrayList<String>();

        // Get the Supported Hierarchies
        List<SupportedHierarchy> supportedHierList = csclass.getMappings().getSupportedHierarchyAsReference();
        Iterator<SupportedHierarchy> supportedHierItr = supportedHierList.iterator();
        while (supportedHierItr.hasNext()) {
            SupportedHierarchy hier = supportedHierItr.next();

            // For each Supported Hierarchy, get its associations
            List<String> associations = hier.getAssociationNamesAsReference();
            Iterator<String> associationsItr = associations.iterator();
            while (associationsItr.hasNext()) {
                String currentAssoc = associationsItr.next();
                // Add the associaton (if it hasn't been added already)
                if (!supportedHierarchyAssociations.contains(currentAssoc)) {
                    supportedHierarchyAssociations.add(currentAssoc);
                }
            }
        }

        // Get all of the Associations for this Coding Scheme

        for (AssociationPredicate currentAssoc : assocPredicateList) {

            // Check to see if the association is part of a Supported Hierarchy
            String currentAssocName = currentAssoc.getAssociationName();
            if (supportedHierarchyAssociations.contains(currentAssocName)) {
                // if so, set isTransitive to true
                // TODO: Deal with flags
                // currentAssoc.setIsTransitive(Boolean.valueOf(true));
                messages_.info("Setting Hierarchical Association " + currentAssocName + " to Transitive");
            }
        }
    }

}