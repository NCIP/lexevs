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

import org.LexGrid.emf.codingSchemes.CodingScheme;
import org.LexGrid.emf.commonTypes.CommontypesFactory;
import org.LexGrid.emf.commonTypes.Property;
import org.LexGrid.emf.commonTypes.Source;
import org.LexGrid.emf.commonTypes.Text;
import org.LexGrid.emf.commonTypes.impl.CommontypesFactoryImpl;
import org.LexGrid.emf.concepts.Comment;
import org.LexGrid.emf.concepts.Concept;
import org.LexGrid.emf.concepts.ConceptsFactory;
import org.LexGrid.emf.concepts.Definition;
import org.LexGrid.emf.concepts.Entities;
import org.LexGrid.emf.concepts.Presentation;
import org.LexGrid.emf.concepts.impl.ConceptsFactoryImpl;
import org.LexGrid.emf.naming.NamingFactory;
import org.LexGrid.emf.naming.SupportedAssociation;
import org.LexGrid.emf.naming.SupportedAssociationQualifier;
import org.LexGrid.emf.naming.SupportedHierarchy;
import org.LexGrid.emf.naming.SupportedProperty;
import org.LexGrid.emf.naming.SupportedPropertyLink;
import org.LexGrid.emf.naming.SupportedRepresentationalForm;
import org.LexGrid.emf.naming.SupportedSource;
import org.LexGrid.emf.naming.SupportedStatus;
import org.LexGrid.emf.naming.impl.NamingFactoryImpl;
import org.LexGrid.emf.relations.Association;
import org.LexGrid.emf.relations.AssociationQualification;
import org.LexGrid.emf.relations.AssociationSource;
import org.LexGrid.emf.relations.AssociationTarget;
import org.LexGrid.emf.relations.Relations;
import org.LexGrid.emf.relations.RelationsFactory;
import org.LexGrid.emf.relations.impl.RelationsFactoryImpl;
import org.LexGrid.emf.relations.util.RelationsUtil;
import org.LexGrid.messaging.LgMessageDirectorIF;
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

    private Hashtable properties_ = null;
    private Hashtable propertyLinks_ = null;
    private Hashtable conceptStatus_ = null;
    private Hashtable sources_ = null;
    private Hashtable representationalForms_ = null;
    private Hashtable supportedAssociationHashtable = null;

    private Hashtable associationHash_ = new Hashtable();

    private RelationsFactory relationsFactory = new RelationsFactoryImpl();
    private ConceptsFactory conceptFactory = new ConceptsFactoryImpl();
    private NamingFactory nameFactory = new NamingFactoryImpl();
    private CommontypesFactory commontypesFactory = new CommontypesFactoryImpl();

    private List allAssociations = null;

    private Entities allConcepts_ = null;
    private List codedEntriesList = null;
    CodingScheme csclass_;

    private long propertyCounter = 0;
    private long anonymousCounter = 0;
    private Map<String, AssociationSource> assocSrcQName2emfAI = null; // association+source

    // name
    // to
    // association
    // instances

    public OBO2EMFDynamicMapHolders(LgMessageDirectorIF messages) {
        messages_ = messages;
    }

    public boolean prepareCSClass(OBOResourceReader oboReader, CodingScheme csclass) {
        boolean success = true;
        csclass_ = csclass;
        try {
            properties_ = OBO2EMFStaticMapHolders.getFixedProperties();
            assocSrcQName2emfAI = new HashMap<String, AssociationSource>();
            if (propertyLinks_ == null)
                propertyLinks_ = new Hashtable();

            conceptStatus_ = OBO2EMFStaticMapHolders.getFixedConceptStatus();
            representationalForms_ = OBO2EMFStaticMapHolders.getFixedRepresentationalForms();
            sources_ = OBO2EMFStaticMapHolders.getFixedSources();
            supportedAssociationHashtable = OBO2EMFStaticMapHolders.getFixedAssociations();
            OBOContents contents = oboReader.getContents(false, false);
            OBORelations relations = contents.getOBORelations();

            allConcepts_ = csclass.getEntities();

            if (allConcepts_ == null) {
                allConcepts_ = conceptFactory.createEntities();
                csclass.setEntities(allConcepts_);
            }

            codedEntriesList = allConcepts_.getEntity();

            // Creating the relation instance
            List relationsList;
            relationsList = csclass.getRelations();

            Relations allRelations = relationsFactory.createRelations();
            allRelations.setContainerName(SQLTableConstants.TBLCOLVAL_DC_RELATIONS);
            allRelations.setIsNative(new Boolean(true));

            relationsList.add(allRelations);
            allAssociations = allRelations.getAssociation();

            OBOTerms terms = contents.getOBOTerms();
            Collection termList = terms.getAllMembers();

            if ((termList != null) && (!termList.isEmpty())) {
                try {
                    long busyCtr = 0;
                    SimpleMemUsageReporter.snapshot();
                    for (Iterator items = termList.iterator(); items.hasNext();) {
                        OBOTerm oboTerm = (OBOTerm) items.next();
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

                Concept concept = conceptFactory.createConcept();

                if (!OBO2EMFUtils.isNull(oboTerm.getId()))
                    concept.setEntityCode(oboTerm.getId());
                else
                    concept.setEntityCode(oboTerm.getName());

                String termDesc = "";

                if (!OBO2EMFUtils.isNull(oboTerm.getName()))
                    termDesc = oboTerm.getName();
                else
                    termDesc = oboTerm.getId();

                concept.setEntityDescription(termDesc);

                if (oboTerm.isObsolete()) {
                    concept.setIsActive(new Boolean(false));
                }

                Presentation tp = conceptFactory.createPresentation();
                Text txt = CommontypesFactory.eINSTANCE.createText();
                txt.setValue(termDesc);
                tp.setValue(txt);
                tp.setIsPreferred(new Boolean(true));
                tp.setPropertyName(OBO2EMFConstants.PROPERTY_TEXTPRESENTATION);

                String preferredPresetationID = OBO2EMFConstants.PROPERTY_ID_PREFIX + (++propertyCounter);
                tp.setPropertyId(preferredPresetationID);
                concept.getPresentation().add(tp);

                if (!OBO2EMFUtils.isNull(oboTerm.getComment())) {
                    Comment comment = conceptFactory.createComment();
                    txt = CommontypesFactory.eINSTANCE.createText();
                    txt.setValue((String) OBO2EMFUtils.removeInvalidXMLCharacters(oboTerm.getComment(), null));
                    comment.setValue(txt);
                    comment.setPropertyName(OBO2EMFConstants.PROPERTY_COMMENT);
                    comment.setPropertyId(OBO2EMFConstants.PROPERTY_ID_PREFIX + (++propertyCounter));
                    concept.getComment().add(comment);
                }

                if (!OBO2EMFUtils.isNull(oboTerm.getDefinition())) {
                    Definition defn = conceptFactory.createDefinition();
                    txt = CommontypesFactory.eINSTANCE.createText();
                    txt.setValue((String) OBO2EMFUtils.removeInvalidXMLCharacters(oboTerm.getDefinition(), null));
                    defn.setValue(txt);
                    defn.setPropertyName(OBO2EMFConstants.PROPERTY_DEFINITION);
                    defn.setPropertyId(OBO2EMFConstants.PROPERTY_ID_PREFIX + (++propertyCounter));
                    concept.getDefinition().add(defn);

                    OBOAbbreviations abbreviations = contents.getOBOAbbreviations();
                    addOBODbxrefAsSource(defn, oboTerm.getDefinitionSources(), abbreviations);

                }

                addPropertyAttribute(oboTerm.getSubset(), concept, OBO2EMFConstants.PROPERTY_SUBSET);
                Vector dbxref_src_vector = OBODbxref.getSourceAndSubRefAsVector(oboTerm.getDbXrefs());
                addPropertyAttribute(dbxref_src_vector, concept, "xref");
                // Add Synonyms as presentations
                Vector synonyms = oboTerm.getSynonyms();

                if ((synonyms != null) && (!synonyms.isEmpty())) {
                    for (Iterator sItr = synonyms.iterator(); sItr.hasNext();) {
                        Object synObj = sItr.next();

                        if (synObj instanceof OBOSynonym) {
                            OBOSynonym synonym = (OBOSynonym) synObj;
                            Presentation ip = conceptFactory.createPresentation();
                            txt = CommontypesFactory.eINSTANCE.createText();
                            txt.setValue((String) synonym.getText());
                            ip.setValue(txt);
                            ip.setPropertyName(OBO2EMFConstants.PROPERTY_SYNONYM);
                            String synPresID = OBO2EMFConstants.PROPERTY_ID_PREFIX + (++propertyCounter);
                            ip.setPropertyId(synPresID);
                            concept.getPresentation().add(ip);

                            String scope = synonym.getScope();
                            if (scope != null && scope.length() > 0)
                                ip.setDegreeOfFidelity(scope);

                            // Add source information
                            Vector<OBODbxref> dbxref = OBODbxref.parse(synonym.getDbxref());
                            OBOAbbreviations abbreviations = contents.getOBOAbbreviations();
                            addOBODbxrefAsSource(ip, dbxref, abbreviations);

                        }
                    }
                }

                Vector<String> vec_altIds = oboTerm.getAltIds();

                if ((vec_altIds != null) && (!vec_altIds.isEmpty())) {
                    for (String altId : vec_altIds) {

                        if (StringUtils.isNotBlank(altId)) {

                            Property emfProp = CommontypesFactory.eINSTANCE.createProperty();
                            emfProp.setPropertyName(OBO2EMFConstants.PROPERTY_ALTID);
                            String prop_id = OBO2EMFConstants.PROPERTY_ID_PREFIX + (++propertyCounter);
                            emfProp.setPropertyId(prop_id);

                            txt = CommontypesFactory.eINSTANCE.createText();
                            txt.setValue(altId);
                            emfProp.setValue(txt);

                            concept.getProperty().add(emfProp);

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

                Hashtable relationships = oboTerm.getRelationships();
                if (relationships != null) {
                    for (Enumeration e = relationships.keys(); e.hasMoreElements();) {
                        String relName = e.nextElement().toString();
                        OBORelations relations = contents.getOBORelations();
                        OBORelation relation = relations.getMemberById(relName);
                        Object targetObj = relationships.get(relName);
                        Vector targets = null;
                        if (targetObj != null && targetObj instanceof Vector) {
                            targets = (Vector) targetObj;
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

                    Source s = CommontypesFactory.eINSTANCE.createSource();
                    s.setValue(srcVal);
                    s.setSubRef(dbxref.getSubrefAndDescription());
                    prop.getSource().add(s);

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

        Collection values = new ArrayList();
        values.add(value);
        addPropertyAttribute(values, concept, propertyName);
    }

    private String addPropertyAttribute(Collection values, Concept concept, String propertyName) {
        String propertyID = null;
        try {
            if ((values != null) && (!values.isEmpty())) {
                String propertyNMT = OBO2EMFUtils.toNMToken(propertyName);

                if (!properties_.containsKey(propertyNMT)) {
                    properties_.put(propertyNMT, propertyNMT);
                }

                Iterator itr = values.iterator();
                while (itr.hasNext()) {
                    String value = (String) itr.next();

                    if (!OBO2EMFUtils.isNull(value)) {
                        Property pc = CommontypesFactory.eINSTANCE.createProperty();
                        pc.setPropertyName(propertyNMT);
                        propertyID = OBO2EMFConstants.PROPERTY_ID_PREFIX + (++propertyCounter);
                        pc.setPropertyId(propertyID);
                        Text txt = CommontypesFactory.eINSTANCE.createText();
                        txt.setValue((String) value);
                        pc.setValue(txt);
                        concept.getProperty().add(pc);
                    }
                }
            }
        } catch (Exception e) {
            messages_.info("Failed while storing value for property: " + propertyName);
        }

        return propertyID;
    }

    private boolean processSpecialAssociationLikeOWL(Concept concept, String relation_name, Collection targets) {
        boolean processed = false;
        List special_relationNames = Arrays.asList(OBO2EMFConstants.BUILT_IN_SPECIAL_ASSOCIATIONS);
        if (special_relationNames.contains(relation_name)) {
            processed = true;

            Concept anon_eq = createAnonymousConcept("(" + relation_name + ")");
            addAssociation(concept, "equivalentClass", null, null, null, anon_eq);

            Iterator itr = targets.iterator();
            String target1 = "";
            String target2 = "";
            if (itr.hasNext()) {
                target1 = itr.next().toString();
                processAnonListTarget(anon_eq, target1);

            }
            if (itr.hasNext()) {
                target2 = itr.next().toString();
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
        Concept concept = conceptFactory.createConcept();
        String conceptCode = OBO2EMFConstants.ANONYMOUS_TEXTPRESENTATION + (++anonymousCounter);
        if (entityDescription == null) {
            entityDescription = conceptCode;
        }
        Presentation tp = conceptFactory.createPresentation();
        Text txt = CommontypesFactory.eINSTANCE.createText();
        txt.setValue((String) entityDescription);
        tp.setValue(txt);
        tp.setIsPreferred(new Boolean(true));
        tp.setPropertyName(OBO2EMFConstants.PROPERTY_TEXTPRESENTATION);
        concept.setEntityCode(conceptCode);
        concept.setEntityDescription(entityDescription);
        String preferredPresetationID = OBO2EMFConstants.PROPERTY_ID_PREFIX + anonymousCounter;
        tp.setPropertyId(preferredPresetationID);
        concept.getPresentation().add(tp);
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

        List associationList = RelationsUtil.resolveAssociations(csclass_, association_name);
        Association assocClass;
        if (associationList.isEmpty()) {
            if (!associationHash_.containsKey(association_name)) {
                associationHash_.put(association_name, association_name);
            }
            assocClass = relationsFactory.createAssociation();
            assocClass.setEntityCode(association_name);
            assocClass.setForwardName(association_name);
            RelationsUtil.subsume(getOrCreateRelations(), assocClass);
            if (!supportedAssociationHashtable.containsKey(association_name)) {
                supportedAssociationHashtable.put(association_name, association_name);
            }
        } else {
            assocClass = (Association) associationList.get(0);
        }
        AssociationSource ai = relationsFactory.createAssociationSource();
        ai.setSourceEntityCode(source.getEntityCode());
        ai = RelationsUtil.subsume(assocClass, ai);
        AssociationTarget at = relationsFactory.createAssociationTarget();
        if (association_qualifier != null) {
            AssociationQualification aq = relationsFactory.createAssociationQualification();
            aq.setAssociationQualifier(association_qualifier);
            at.getAssociationQualification().add(aq);
            addSupportedAssociationQualifier(association_qualifier);
        }

        at.setTargetEntityCode(target_code);
        RelationsUtil.subsume(ai, at);

    }

    Relations getOrCreateRelations() {
        Relations relations = null;
        Iterator itr = csclass_.getRelations().iterator();
        while (itr.hasNext()) {
            Relations r = (Relations) itr.next();
            if (r.getContainerName().equals("relations")) {
                relations = r;
                break;
            }
        }
        if (relations == null) {
            relations = relationsFactory.createRelations();
            relations.setContainerName("relations");
            RelationsUtil.subsume(csclass_, relations);
        }
        return relations;
    }

    private void addSupportedAssociationQualifier(String association_qualifier) {

        if (csclass_ == null || association_qualifier == null)
            return;
        Iterator itr = csclass_.getMappings().getSupportedAssociationQualifier().iterator();
        boolean found = false;
        while (itr.hasNext()) {
            SupportedAssociationQualifier saq = (SupportedAssociationQualifier) itr.next();
            if (saq.getLocalId().equals(association_qualifier)) {
                // We already have it added, return
                return;
            }
        }
        SupportedAssociationQualifier qualifier = this.nameFactory.createSupportedAssociationQualifier();
        qualifier.setLocalId(association_qualifier);
        csclass_.getMappings().getSupportedAssociationQualifier().add(qualifier);

    }

    private boolean processSpecialAssociation(Concept concept, String relation_name, Collection targets) {
        boolean processed = false;
        List special_relationNames = Arrays.asList(OBO2EMFConstants.BUILT_IN_SPECIAL_ASSOCIATIONS);
        if (special_relationNames.contains(relation_name)) {
            processed = true;
            Iterator itr = targets.iterator();
            while (itr.hasNext()) {
                String stringValue = itr.next().toString();
                Property cp = commontypesFactory.createProperty();
                cp.setPropertyName(relation_name);
                String propertyID = OBO2EMFConstants.PROPERTY_ID_PREFIX + (++propertyCounter);
                cp.setPropertyId(propertyID);
                Text txt = CommontypesFactory.eINSTANCE.createText();
                txt.setValue((String) stringValue);
                cp.setValue(txt);
                concept.getProperty().add(cp);
            }

        }
        return processed;
    }

    private void addAssociationAttribute(Concept concept, OBORelation oboRelation, Collection targets) {
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
            Association assocClass = null;
            boolean createdNew = false;
            String invSlotName = null;

            try {
                if (associationHash_.containsKey(relation)) {
                    Object associationHashValue = associationHash_.get(relation);
                    if (!(associationHashValue instanceof Association))
                        return;

                    assocClass = (Association) associationHashValue;
                } else { // Inverse Slot
                    invSlotName = oboRelation.getInverseOf();
                    if (invSlotName != null) {
                        invSlotName = OBO2EMFUtils.toNMToken(invSlotName);
                    }

                    // System.out.println("Creating Relation=" + relation);
                    assocClass = relationsFactory.createAssociation();
                    assocClass.setEntityCode(oboRelation.getId());
                    assocClass.setForwardName(oboRelation.getId());

                    if (!OBO2EMFUtils.isNull(invSlotName)) {
                        // Do not put a reserve name of the relation.....the
                        // reverse name of
                        // part_of is not has_part....BioPortal bug: #709
                        // assocClass.setReverseName(invSlotName);
                        // assocClass.setInverse(invSlotName);
                        assocClass.setIsNavigable(new Boolean(true));
                    }
                    /*
                     * We do not manufacture a reverse relation name anymore.
                     * else assocClass.setReverseName("inverseOf_" + relation);
                     */

                    assocClass.setIsTransitive(oboRelation.getIsTransitive());
                    assocClass.setIsSymmetric(oboRelation.getIsSymmetric());
                    assocClass.setIsReflexive(oboRelation.getIsReflexive());
                    assocClass.setIsAntiSymmetric(oboRelation.getIsAntiSymmetric());
                    createdNew = true;
                    associationHash_.put(relation, assocClass);
                }
            } catch (Exception e) {
                messages_.error("Error while creating/finding association object for=" + relation, e);
                return;
            }

            if (targets.size() > 0) {
                AssociationSource ai = getOrCreateAssociationInstance(assocClass, concept);

                Iterator itr = targets.iterator();
                while (itr.hasNext()) {
                    Object o = itr.next();

                    String stringValue = (String) o;

                    if (!OBO2EMFUtils.isNull(stringValue)) {
                        AssociationTarget at = relationsFactory.createAssociationTarget();
                        at.setTargetEntityCode(stringValue);
                        RelationsUtil.subsume(ai, at);
                    }
                }
            }

            if (createdNew) {
                allAssociations.add(assocClass);
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

    private AssociationSource getOrCreateAssociationInstance(Association association, Concept concept) {
        String assocSrcQName = association.getEntityCode() + "::" + concept.getEntityCode();
        if (assocSrcQName2emfAI.containsKey(assocSrcQName)) {
            return assocSrcQName2emfAI.get(assocSrcQName);
        } else {
            AssociationSource ai = relationsFactory.createAssociationSource();
            ai.setSourceEntityCode(concept.getEntityCode());
            assocSrcQName2emfAI.put(assocSrcQName, ai);
            association.getSource().add(ai);
            return ai;
        }
    }

    public void populateSupportedProperties(CodingScheme csclass) {
        if (csclass == null)
            return;

        List suppProps = csclass.getMappings().getSupportedProperty();

        try {
            if (properties_.size() > 0) {
                Iterator kI = properties_.keySet().iterator();

                while (kI.hasNext()) {
                    String prpCode = null;
                    String prp = (String) kI.next();

                    if (!OBO2EMFUtils.isNull(prp)) {
                        prpCode = (String) properties_.get(prp);

                        SupportedProperty suppProp = nameFactory.createSupportedProperty();

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

    public void populateSupportedConceptStatus(CodingScheme csclass) {
        if (csclass == null)
            return;

        List suppProps = csclass.getMappings().getSupportedStatus();

        try {
            if (conceptStatus_.size() > 0) {
                Iterator kI = conceptStatus_.keySet().iterator();

                while (kI.hasNext()) {
                    String prpCode = null;
                    String prp = (String) kI.next();

                    if (!OBO2EMFUtils.isNull(prp)) {
                        prpCode = (String) conceptStatus_.get(prp);

                        SupportedStatus suppCSs = nameFactory.createSupportedStatus();

                        if (!OBO2EMFUtils.isNull(prpCode))
                            suppCSs.setUri(OBO2EMFUtils.getWithOBOURN(prpCode));
                        else
                            suppCSs.setUri(OBO2EMFUtils.getWithOBOURN(prp));

                        suppCSs.setLocalId(prp);
                        suppProps.add(suppCSs);
                    }
                }
            }
        } catch (Exception ex) {
            messages_.info("Failed while getting supported Concept Status!");
        }
    }

    public void populateSupportedAssociations(CodingScheme csclass) {
        try {
            if (csclass == null)
                return;

            List suppAssoc = csclass.getMappings().getSupportedAssociation();
            for (Enumeration e = supportedAssociationHashtable.keys(); e.hasMoreElements();) {
                String prpCode = (String) e.nextElement();

                if (!OBO2EMFUtils.isNull(prpCode)) {
                    SupportedAssociation suppAss = nameFactory.createSupportedAssociation();
                    suppAss.setUri(OBO2EMFUtils.getWithOBOURN(prpCode));
                    // suppAss.setLocalName(OBO2EMFUtils.toNMToken(prpCode));
                    suppAss.setLocalId(prpCode);
                    suppAssoc.add(suppAss);
                }
            }
            SupportedAssociation suppAss = nameFactory.createSupportedAssociation();
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

            List supportedHierList = csclass.getMappings().getSupportedHierarchy();
            org.LexGrid.emf.naming.SupportedHierarchy hier = nameFactory.createSupportedHierarchy();
            hier.setLocalId(OBO2EMFConstants.HIERARCHY_DEFAULT_NAME);
            hier.setIsForwardNavigable(Boolean.valueOf(OBO2EMFConstants.ASSOCIATION_ISA_ISFORWARDNAVIGABLE)
                    .booleanValue());
            hier.setRootCode(OBO2EMFConstants.HIERARCHY_DEFAULT_ROOT);

            if (hier.getAssociationNames() == null) {
                hier.setAssociationNames(new ArrayList());
            }

            for (Enumeration e = supportedAssociationHashtable.keys(); e.hasMoreElements();) {
                String prpCode = (String) e.nextElement();

                if (prpCode.equals(OBO2EMFConstants.ASSOCIATION_DEVELOPS_FROM))
                    hier.getAssociationNames().add(OBO2EMFConstants.ASSOCIATION_DEVELOPS_FROM);
                else if (prpCode.equals(OBO2EMFConstants.ASSOCIATION_ISA))
                    hier.getAssociationNames().add(OBO2EMFConstants.ASSOCIATION_ISA);
                else if (prpCode.equals(OBO2EMFConstants.ASSOCIATION_PART_OF))
                    hier.getAssociationNames().add(OBO2EMFConstants.ASSOCIATION_PART_OF);
                else if (prpCode.equals(OBO2EMFConstants.ASSOCIATION_LOCATED_IN))
                    hier.getAssociationNames().add(OBO2EMFConstants.ASSOCIATION_LOCATED_IN);
                else if (prpCode.equals(OBO2EMFConstants.ASSOCIATION_DERIVES_FROM))
                    hier.getAssociationNames().add(OBO2EMFConstants.ASSOCIATION_DERIVES_FROM);
                else if (prpCode.equals(OBO2EMFConstants.ASSOCIATION_BROADER))
                    hier.getAssociationNames().add(OBO2EMFConstants.ASSOCIATION_BROADER);
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

            hier.getAssociationNames().add("-multi-assn-@-root-");
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

        List suppSrcs = csclass.getMappings().getSupportedSource();
        try {
            if (sources_.size() > 0) {
                Iterator kI = sources_.keySet().iterator();

                while (kI.hasNext()) {
                    String prpCode = null;
                    String prp = (String) kI.next();

                    if (!OBO2EMFUtils.isNull(prp)) {
                        prpCode = (String) sources_.get(prp);

                        SupportedSource suppCSs = nameFactory.createSupportedSource();

                        if (!OBO2EMFUtils.isNull(prpCode)) {
                            // messages_.("prpCode="+prpCode+" prp="+prp);
                            if (!prpCode.startsWith("http"))
                                suppCSs.setUri(OBO2EMFConstants.OBO_URN + OBO2EMFUtils.toNMToken(prpCode));
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

    public void populateSupportedRepresentationalForms(CodingScheme csclass) {
        if (csclass == null)
            return;

        List suppRepf = csclass.getMappings().getSupportedRepresentationalForm();

        try {
            if (representationalForms_.size() > 0) {
                Iterator kI = representationalForms_.keySet().iterator();

                while (kI.hasNext()) {
                    String prpCode = null;
                    String prp = (String) kI.next();

                    if (!OBO2EMFUtils.isNull(prp)) {
                        prpCode = (String) representationalForms_.get(prp);

                        SupportedRepresentationalForm suppRPs = nameFactory.createSupportedRepresentationalForm();

                        if (!OBO2EMFUtils.isNull(prpCode))
                            suppRPs.setUri(OBO2EMFUtils.getWithOBOURN(prpCode));
                        else
                            suppRPs.setUri(OBO2EMFUtils.getWithOBOURN(prp));

                        suppRPs.setLocalId(prp);
                        suppRepf.add(suppRPs);
                    }
                }
            }
        } catch (Exception ex) {
            messages_.error("Failed while getting supported Representational Forms !", ex);
        }
    }

    public void setSupportedHierarchyAssociationsTransitive(CodingScheme csclass) {
        // Holder for all Supported Hierarchy Associations
        // This is a consolidated list of all the associations
        // from all of the supportedHierarchies
        ArrayList supportedHierarchyAssociations = new ArrayList();

        // Get the Supported Hierarchies
        List supportedHierList = csclass.getMappings().getSupportedHierarchy();
        Iterator supportedHierItr = supportedHierList.iterator();
        while (supportedHierItr.hasNext()) {
            SupportedHierarchy hier = (SupportedHierarchy) supportedHierItr.next();

            // For each Supported Hierarchy, get its associations
            List associations = hier.getAssociationNames();
            Iterator associationsItr = associations.iterator();
            while (associationsItr.hasNext()) {
                String currentAssoc = (String) associationsItr.next();
                // Add the associaton (if it hasn't been added already)
                if (!supportedHierarchyAssociations.contains(currentAssoc)) {
                    supportedHierarchyAssociations.add(currentAssoc);
                }
            }
        }

        // Get all of the Associations for this Coding Scheme
        Iterator associationsItr = allAssociations.iterator();
        while (associationsItr.hasNext()) {
            Association currentAssoc = (Association) associationsItr.next();
            // Check to see if the association is part of a Supported Hierarchy
            String currentAssocName = currentAssoc.getEntityCode();
            if (supportedHierarchyAssociations.contains(currentAssocName)) {
                // if so, set isTransitive to true
                currentAssoc.setIsTransitive(Boolean.valueOf(true));
                messages_.info("Setting Hierarchical Association " + currentAssocName + " to Transitive");
            }
        }
    }

    public void populateSupportedPropertyLinks(CodingScheme csclass) {
        if (csclass == null)
            return;

        List suppRepf = csclass.getMappings().getSupportedPropertyLink();

        try {
            if (propertyLinks_.size() > 0) {
                Iterator kI = propertyLinks_.keySet().iterator();

                while (kI.hasNext()) {
                    String prpCode = null;
                    String prp = (String) kI.next();

                    if (!OBO2EMFUtils.isNull(prp)) {
                        prpCode = (String) propertyLinks_.get(prp);

                        SupportedPropertyLink suppPlnks = nameFactory.createSupportedPropertyLink();

                        if (!OBO2EMFUtils.isNull(prpCode))
                            suppPlnks.setUri(OBO2EMFUtils.getWithOBOURN(prpCode));
                        else
                            suppPlnks.setUri(OBO2EMFUtils.getWithOBOURN(prp));

                        suppPlnks.setLocalId(prp);
                        suppRepf.add(suppPlnks);
                    }
                }
            }
        } catch (Exception ex) {
            messages_.error("Failed while getting supported Property Links !", ex);
        }
    }
}