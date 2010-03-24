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
package edu.mayo.informatics.lexgrid.convert.emfConversions.radlex;

import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Comment;
import org.LexGrid.concepts.Concept;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.naming.SupportedProperty;
import org.LexGrid.naming.SupportedRepresentationalForm;
import org.LexGrid.naming.SupportedSource;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationEntity;
import org.LexGrid.relations.AssociationData;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.LexGrid.custom.concepts.EntityFactory;
import org.LexGrid.custom.relations.RelationsUtil;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.model.ValueType;
import edu.stanford.smi.protege.model.framestore.ReferenceImpl;

/*
 * @author <A HREF="mailto:dks02@mayo.edu">Deepak Sharma</A> @author <A
 * HREF="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala</A>
 * 
 * @version subversion $Revision: 2917 $ checked in on $Date: 2006-06-19
 *          15:52:21 +0000 (Mon, 19 Jun 2006) $
 */

public class RadLex2EMFDynamicMapHolders {
    private Vector conceptList_ = new Vector();

    private Vector properties_ = null;

    private Vector associations_ = null;

    private Vector sources_ = null;

    private Vector representationalForms_ = null;

    private Vector associationsAliases_ = null;

    private Vector relations_ = new Vector();

    private Hashtable associationHash_ = new Hashtable();
    private Hashtable associationEntityHash_ = new Hashtable();
    
    private Hashtable associationRevHash_ = new Hashtable();

    private long propertyCounter = 0;

    private KnowledgeBase kb_;

    /** ****************************** */

    private AssociationPredicate hasSubTypeAssocClass_ = null;
    private AssociationEntity hasSubTypeAssocEntityClass_ = null;

    private List firstRelation_ = null;

    private Relations allRelations_ = null;

//    private List allAssociations_ = null;

    private Entities allConcepts_ = null;

//    private List allCodedEntries_ = null;

    private LgMessageDirectorIF messages_ = null;
    
    private Hashtable<String, Concept> attributeConcepts = new Hashtable<String, Concept>();
    private Hashtable<String, org.LexGrid.concepts.Instance> attributeInstances = new Hashtable<String, org.LexGrid.concepts.Instance>();

    /** ****************************** */

    public boolean processRadlex(CodingScheme csclass, KnowledgeBase kb, LgMessageDirectorIF messages) {
        kb_ = kb;
        messages_ = messages;
        boolean success = true;
        try {
            // initialize with static properties
            properties_ = RadLex2EMFStaticMapHolders.getFixedProperties();

            if (properties_ == null)
                properties_ = new Vector();

            associations_ = RadLex2EMFStaticMapHolders.getFixedAssociations();
            if (associations_ == null)
                associations_ = new Vector();

            if (sources_ == null)
                sources_ = new Vector();

            if (representationalForms_ == null)
                representationalForms_ = new Vector();

            if (associationsAliases_ == null)
                associationsAliases_ = new Vector();

            allConcepts_ = csclass.getEntities();

            if (allConcepts_ == null) {
                allConcepts_ = new Entities();
                csclass.setEntities(allConcepts_);
            }
            
            // Relations
            allRelations_ = new Relations();
            allRelations_.setContainerName(SQLTableConstants.TBLCOLVAL_DC_RELATIONS);

            // Creating the relation instance
            csclass.addRelations(allRelations_);

            // Add HasSubtype
            hasSubTypeAssocClass_ = new AssociationPredicate();
            hasSubTypeAssocClass_.setAssociationName(RadLex2EMFConstants.ASSOCIATION_HASSUBTYPE);
            
            hasSubTypeAssocEntityClass_ = new AssociationEntity();
            hasSubTypeAssocEntityClass_.setEntityCode(RadLex2EMFConstants.ASSOCIATION_HASSUBTYPE);
            hasSubTypeAssocEntityClass_.setForwardName(RadLex2EMFConstants.ASSOCIATION_HASSUBTYPE);
            hasSubTypeAssocEntityClass_.setReverseName(RadLex2EMFConstants.ASSOCIATION_ISA);
            hasSubTypeAssocEntityClass_.setIsTransitive(new Boolean(true));

            // TODO: Set this Association's original Forward and
            // Reverse (isOriginalRevName) Name flags here
            // 		
            // for hasSubtype it will be false as it is our inbuilt relation
            // for Is_A it will be true as it comes in RadLex file as it is.

            associationHash_.put(RadLex2EMFConstants.ASSOCIATION_HASSUBTYPE, hasSubTypeAssocClass_);
            associationEntityHash_.put(RadLex2EMFConstants.ASSOCIATION_HASSUBTYPE, hasSubTypeAssocEntityClass_);
            associationRevHash_.put(RadLex2EMFConstants.ASSOCIATION_ISA, hasSubTypeAssocClass_);

            allRelations_.addAssociationPredicate(hasSubTypeAssocClass_);

            relations_.add(csclass.getRelations());

            boolean testing = false;

            if (testing) {
                messages_.info("<<<<<<<------TEST MODE----->>>>>>");
                Cls topCls = kb_.getCls("RID0");
                loadConcept(topCls);
                messages_.info("Processing done in test mode!");
            } else {
                for (int i = 0; i < RadLex2EMFConstants.topClasses.length; i++) {
                    // System.out.println("Processing top node -->" +
                    // RadLex2EMFConstants.topClasses[i]);
                    Cls topCls = kb_.getCls(RadLex2EMFConstants.topClasses[i]);
                    loadConcept(topCls);
                }
                
                messages_.info("Processing done!");
            }
        } catch (Exception e) {
            success = false;
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return success;
    }

    private void loadConcept(Object parentObj) {
        if (parentObj != null) {
            if ((parentObj instanceof Cls) || (parentObj instanceof Instance)) {
                String conceptCode = getConceptCodeFromObj(parentObj);
                if (storeConcept(parentObj, conceptCode)) {
                    if (parentObj instanceof Cls) {
                        Collection classes = kb_.getDirectSubclasses((Cls) parentObj);

                        if ((classes != null) && (classes.size() > 0)) {
                            addParentChildRelationship(conceptCode, classes);

                            Iterator itr = classes.iterator();
                            while (itr.hasNext()) {
                                Object obj = itr.next();
                                if (obj instanceof Cls) {
                                    Cls c = (Cls) obj;
                                    if (!c.getName().startsWith(":")) {
                                        loadConcept(c);
                                    }
                                }
                            }
                        }

                        classes = kb_.getDirectInstances((Cls) parentObj);
                        if ((classes != null) && (classes.size() > 0)) {
                            addParentChildRelationship(conceptCode, classes);

                            Iterator itr = classes.iterator();
                            while (itr.hasNext()) {
                                Object obj = itr.next();
                                if (obj instanceof Instance) {
                                    Instance i = (Instance) obj;
                                    if (!i.getName().startsWith(":")) {
                                        loadConcept(i);
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    private boolean storeConcept(Object concept, String conceptCode) 
    {
        boolean stored = false;
        boolean isRadLexSynonymCls = false;
        if ((conceptCode != null) && (!conceptCode.startsWith(":"))) {
            try {
                isRadLexSynonymCls = false;
                Cls rlCls = null;

                if (concept instanceof Cls)
                    rlCls = ((Cls) concept);

                try 
                {
                    if ((rlCls != null) && (rlCls.getDirectType().getName().equals("radlex_synonym")))
                    {
                        isRadLexSynonymCls = true;
                        //return true;
                    }
                } 
                catch (Exception e) 
                {
                }

                try 
                {
                    if ((rlCls != null) && (rlCls.getDirectSuperclassCount() > 0))
                    {
                        boolean isRadlexAttribute = false;
                        Collection superClassesCol = rlCls.getDirectSuperclasses();
                        for (Iterator itr = superClassesCol.iterator(); itr.hasNext();)
                        {
                            Object ob = itr.next();
                            if (ob instanceof Cls)
                            {
                                Cls sup = (Cls) ob;
                                if (sup.getName().equals("RID10310"))
                                {
                                    isRadlexAttribute = true;
                                    //System.out.println("This is attribute");
                                    break;
                                }
                            }
                        }
                        
                        if (isRadlexAttribute)
                        {
                            org.LexGrid.concepts.Instance inst = null;
                            String iCode = getConceptCodeFromObj(rlCls);
                            
                            if (!RadLex2EMFUtils.isNull(iCode))
                            {
                                if (attributeInstances.containsKey(iCode))
                                    inst = attributeInstances.get(iCode);
                                else
                                {
                                    inst = createInstanceFromProtegeCls(rlCls);
                                    attributeInstances.put(iCode, inst);
                                    conceptList_.add(iCode);
                                    allConcepts_.addEntity(inst);
                                }
                            }
                            
                            Collection refs = rlCls.getReferences();
                            for (Iterator itr2 = refs.iterator(); itr2.hasNext();)
                            {
                                Object ob2 = itr2.next();
                                //System.out.println(ob2);
                                ReferenceImpl refI = (ReferenceImpl) ob2;
                                String frmNm = refI.getFrame().getName();
                                
                                if ((frmNm.equals("RID10310"))||(frmNm.equals("radlex_metaclass")))
                                    continue;
                                
                                Slot referredSlot = refI.getSlot();
                                String sltNm = referredSlot.getName();
                                if ((sltNm.startsWith(":"))||
                                        (sltNm.toLowerCase().indexOf("has_subtype") != -1)||
                                        (sltNm.toLowerCase().indexOf("synonym") != -1))
                                    continue;
                                
                                //System.out.println("Eligible for processing: " + sltNm);
                                
                                // creating the class for this slot
                                Concept slotLGConcept = null;
                                if (attributeConcepts.containsKey(sltNm))
                                    slotLGConcept = attributeConcepts.get(sltNm);
                                else
                                {
                                    slotLGConcept = createCoceptFromSlot(referredSlot);
                                    attributeConcepts.put(sltNm, slotLGConcept);
                                    conceptList_.add(slotLGConcept.getEntityCode());
                                    allConcepts_.addEntity(slotLGConcept);
                                    addParentChildRelationship("RID10310", slotLGConcept.getEntityCode());
                                }
                                
                                // TODO: Deepak May 12 2009
                                // Now create instance of SlotLGConcepts with name as
                                // name of the rlCls (the radlex attribute that we are
                                // working with)
                                // and 
                                // add associations between the concept(s) (domain of slot)
                                // and the instances with prefix "has" and slot name to create
                                // association and targets as the instance
                                
                                Collection domains = referredSlot.getDomain();

                                Vector<String> sourceClses = new Vector<String>();
                                for (Iterator itr3 = domains.iterator(); itr3.hasNext();)
                                {
                                    Object ob3 = itr3.next();
                                    
                                    if (ob3 instanceof Cls)
                                    {
                                        String sourceCode = getConceptCodeFromObj(ob3);
                                        
                                        if (!RadLex2EMFUtils.isNull(sourceCode))
                                            sourceClses.addElement(sourceCode);
                                    }
                                }

                                String relation2Inst = "has_" + RadLex2EMFUtils.toNMToken(slotLGConcept.getEntityDescription().getContent());
                                //String relation2Inst = "has " + slotLGConcept.getEntityDescription();
                                
                                if ((sourceClses != null)&&(!sourceClses.isEmpty()))
                                {
                                    if (inst != null)
                                    {
                                        for (int h=0; h < sourceClses.size(); h++)
                                        {
                                            addAssociationBetweenSourcesAndInstances(sourceClses.elementAt(h), inst, relation2Inst);
                                        }
                                    }
                                }
                            }
                            
                            return true;
                        }
                    }
                } 
                catch (Exception e) 
                {
                    
                }
                
                if (!conceptList_.contains(conceptCode)) 
                {
                    Concept con = null;
                    // if (!isRadLexSynonymCls)
                    // {
                    propertyCounter = 0;
                    conceptList_.add(conceptCode);

                    con = EntityFactory.createConcept();
                    con.setEntityCode(conceptCode);

                    String description = getEntityDescriptionFromObj(concept);
                    if (description != null) {
                        EntityDescription enDesc = new EntityDescription(); 
                        enDesc.setContent(description);
                        con.setEntityDescription(enDesc);
                    }

                    Comment[] comments = getCommentsFromObj(concept);
                    if (comments != null)
                        for (int i = 0; i < comments.length; i++)
                            if (comments[i] != null)
                                con.addComment(comments[i]);

                    Definition[] definitions = getDefinitionsFromObj(concept);
                    if (definitions != null)
                        for (int i = 0; i < definitions.length; i++)
                            con.addDefinition(definitions[i]);
                    // }

                    processSlots(concept, con, isRadLexSynonymCls);

                    // if (!isRadLexSynonymCls)
                    allConcepts_.addEntity(con);

                    stored = true;
                }
            } catch (Exception e) {
                System.out.println("Failed while storing concept!");
                e.printStackTrace();
            }
        }

        return stored;
    }

    private Concept createCoceptFromSlot(Slot slot)
    {
        String code = getConceptCodeFromObj(slot);
        Concept con = null;
        propertyCounter = 0;
        conceptList_.add(code);

        con = EntityFactory.createConcept();
        con.setEntityCode(code);

        String description = getEntityDescriptionFromObj(slot);
        if (!RadLex2EMFUtils.isNull(description)) {
            EntityDescription enDesc = new EntityDescription();
            enDesc.setContent(description);
            con.setEntityDescription(enDesc);
        }

        Comment[] comments = getCommentsFromObj(slot);
        if (comments != null)
            for (int i = 0; i < comments.length; i++)
                if (comments[i] != null)
                    con.addComment(comments[i]);

        Definition[] definitions = getDefinitionsFromObj(slot);
        if (definitions != null)
            for (int i = 0; i < definitions.length; i++)
                if (definitions[i] != null)
                    con.addDefinition(definitions[i]);
        
        return con;
    }

    private org.LexGrid.concepts.Instance createInstanceFromProtegeCls(Cls pCls)
    {
        String code = getConceptCodeFromObj(pCls);
        org.LexGrid.concepts.Instance inst = null;
        propertyCounter = 0;
        conceptList_.add(code);

        inst = new org.LexGrid.concepts.Instance();
        inst.setEntityCode(code);

        String description = getEntityDescriptionFromObj(pCls);
        if (!RadLex2EMFUtils.isNull(description)) {
            EntityDescription enDesc = new EntityDescription();
            enDesc.setContent(description);
            inst.setEntityDescription(enDesc);
        }

        Comment[] comments = getCommentsFromObj(pCls);
        if (comments != null)
            for (int i = 0; i < comments.length; i++)
                if (comments[i] != null)
                    inst.addComment(comments[i]);

        Definition[] definitions = getDefinitionsFromObj(pCls);
        if (definitions != null)
            for (int i = 0; i < definitions.length; i++)
                if (definitions[i] != null)
                    inst.addDefinition(definitions[i]);
        
        return inst;
    }

    
    public int getApproxNumberOfConcepts() 
    {
        int num = 0;
        if (allConcepts_ != null) {
            num = allConcepts_.getEntity().length;
        }
        return num;
    }

    private void processSlots(Object concept, Concept con, boolean restricted) {
        Collection slots = null;

        if (concept instanceof Cls) {
            slots = ((Cls) concept).getOwnSlots();
        } else if (concept instanceof Instance) {
            slots = ((Instance) concept).getOwnSlots();
        }

        if (slots != null) {
            Iterator itr = slots.iterator();

            while (itr.hasNext()) {
                Object o = itr.next();

                if (o instanceof Slot) {
                    Slot slot = (Slot) o;
                    String slotName = slot.getName();

                    if (!slotName.startsWith(":")) {
                        String slotType = getSlotTypeFromSlot(slot);

                        if (!RadLex2EMFUtils.isNull(slotType))
                            addSlotDetailsToConcept(concept, con, slot, slotType);
                    }
                }
            }
        }
    }

    private String getSlotTypeFromSlot(Slot slot) 
    {
        String type = null;

        if (slot != null) {
            String slotName = slot.getName();
            
            //if (slotName.indexOf("status") != -1 )
             //   System.out.println("here it is");
            
            if (!toSkipThisSlot(slotName)) 
            {
                if ((RadLex2EMFConstants.SLOT_Radlex_PREFERRED_NAME.equals(slotName))
                 ||(RadLex2EMFConstants.SLOT_Radlex_SYNONYM.equals(slotName))
                 ||(RadLex2EMFConstants.SLOT_Radlex_SYNONYM_OF.equals(slotName))
                ) 
                {
                    type = RadLex2EMFConstants.SLOT_TYPE_PRESENTATION;
                } 
                else 
                {
                    ValueType vT = slot.getValueType();

                    if ((ValueType.CLS.equals(vT)) || (ValueType.INSTANCE.equals(vT)))
                        type = RadLex2EMFConstants.SLOT_TYPE_ASSOCIATION;
                    else
                        type = RadLex2EMFConstants.SLOT_TYPE_PROPERTY;
                }
            }
        }

        return type;
    }

    private void addParentChildRelationship(String parentConceptcode, Collection subclasses) 
    {
        if ((subclasses != null) && (subclasses.size() > 0)) {
            AssociationSource aI = new AssociationSource();
            aI.setSourceEntityCode(parentConceptcode);
            aI = RelationsUtil.subsume(hasSubTypeAssocClass_, aI);
            Iterator itr = subclasses.iterator();
            while (itr.hasNext()) {
                Object o = itr.next();
                /*
                 * if (o instanceof Instance && ((Instance)
                 * o).getDirectType().getName().equals( "radlex_synonym")) {
                 * continue; }
                 */
                String childConceptCode = getConceptCodeFromObj(o);
                if (childConceptCode != null) {
                    AssociationTarget aT = new AssociationTarget();
                    aT.setTargetEntityCode(childConceptCode);
                    RelationsUtil.subsume(aI, aT);
                }
            }
        }
    }
    
    private void addParentChildRelationship(String parentConceptcode, String childConceptCode) 
    {
        try
        {
            if ((RadLex2EMFUtils.isNull(parentConceptcode))||
               (RadLex2EMFUtils.isNull(childConceptCode)))
            {
                return;
            }
            
            AssociationSource aI = new AssociationSource();
            aI.setSourceEntityCode(parentConceptcode);
            aI = RelationsUtil.subsume(hasSubTypeAssocClass_, aI);
            AssociationTarget aT = new AssociationTarget();
            aT.setTargetEntityCode(childConceptCode);
            RelationsUtil.subsume(aI, aT);
        }
        catch(Exception e)
        {
            System.out.println("Failed to add parent child relationship");
            e.printStackTrace();
        }
    }

    private void addSlotDetailsToConcept(Object concept, Concept con, Slot slot, String slotType) {
        if (slot == null)
            return;
        String slotName = slot.getName();

        if (!toSkipThisSlot(slotName)) {
            if (RadLex2EMFConstants.SLOT_TYPE_PRESENTATION.equals(slotType)) {
                addPresentationAttribute(concept, con, slot);
            } else if (RadLex2EMFConstants.SLOT_TYPE_PROPERTY.equals(slotType)) {
                addPropertyAttribute(concept, con, slot);
            } else if (RadLex2EMFConstants.SLOT_TYPE_ASSOCIATION.equals(slotType)) {
                addAssociationAttribute(concept, con, slot);
            }
        }
    }

    private boolean toSkipThisSlot(String slotName) {
        if ((RadLex2EMFConstants.SLOT_Radlex_CONCEPT_CODE.equals(slotName))
                || (RadLex2EMFConstants.SLOT_NAME.equals(slotName))
                || (RadLex2EMFConstants.SLOT_HASSUBTYPE.equals(slotName))
                //|| (RadLex2EMFConstants.SLOT_Radlex_SYNONYM_OF.equals(slotName))
                || (RadLex2EMFConstants.SLOT_DEFINITION.equals(slotName)))
            return true;

        return false;
    }

    private String getConceptCodeFromObj(Object conceptObj) {
        if (conceptObj != null) {
            if (conceptObj instanceof Cls)
                return getConceptCodeFromCls((Cls) conceptObj);

            if (conceptObj instanceof Instance)
                return getConceptCodeFromInstance((Instance) conceptObj);
            
            if (conceptObj instanceof Slot)
                return getConceptCodeFromSlot((Slot) conceptObj);
        }

        return null;
    }

    private String getConceptCodeFromCls(Cls concept) {
        String conCode = null;

        try {
            if (concept != null) {
                Slot idSlot = kb_.getSlot(RadLex2EMFConstants.SLOT_Radlex_CONCEPT_CODE);
                if (concept.hasOwnSlot(idSlot)) {
                    conCode = (String) concept.getOwnSlotValue(idSlot);
                } else {
                    idSlot = kb_.getSlot(RadLex2EMFConstants.SLOT_Radlex_PREFERRED_NAME);
                    if (concept.hasOwnSlot(idSlot)) {
                        Instance inst = (Instance) concept.getOwnSlotValue(idSlot);
                        conCode = (String) inst.getOwnSlotValue(kb_.getSlot(RadLex2EMFConstants.SLOT_NAME));
                    } else {
                        conCode = RadLex2EMFUtils.toNMToken(concept.getName());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return conCode;
    }
    
    private String getConceptCodeFromSlot(Slot concept) 
    {
        String conCode = null;

        try {
            if (concept != null) {
                Slot idSlot = kb_.getSlot(RadLex2EMFConstants.SLOT_Radlex_CONCEPT_CODE);
                if (concept.hasOwnSlot(idSlot)) {
                    conCode = (String) concept.getOwnSlotValue(idSlot);
                } else {
                    idSlot = kb_.getSlot(RadLex2EMFConstants.SLOT_Radlex_PREFERRED_NAME);
                    if (concept.hasOwnSlot(idSlot)) {
                        Instance inst = (Instance) concept.getOwnSlotValue(idSlot);
                        conCode = (String) inst.getOwnSlotValue(kb_.getSlot(RadLex2EMFConstants.SLOT_NAME));
                    } else {
                        conCode = RadLex2EMFUtils.toNMToken(concept.getName());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return conCode;
    }

    private String getConceptCodeFromInstance(Instance concept) {
        String conCode = null;

        try {
            if (concept != null) {
                Slot idSlot = kb_.getSlot(RadLex2EMFConstants.SLOT_Radlex_CONCEPT_CODE);
                if (concept.hasOwnSlot(idSlot)) {
                    conCode = (String) concept.getOwnSlotValue(idSlot);
                } else {
                    idSlot = kb_.getSlot(RadLex2EMFConstants.SLOT_Radlex_PREFERRED_NAME);
                    if (concept.hasOwnSlot(idSlot)) {
                        Instance inst = (Instance) concept.getOwnSlotValue(idSlot);
                        conCode = (String) inst.getOwnSlotValue(kb_.getSlot(RadLex2EMFConstants.SLOT_NAME));
                    } else {
                        try {
                            conCode = (String) concept.getOwnSlotValue(kb_.getSlot(RadLex2EMFConstants.SLOT_NAME));
                        } catch (Exception e) {
                        }

                        if (RadLex2EMFUtils.isNull(conCode))
                            conCode = RadLex2EMFUtils.toNMToken(concept.getName());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return conCode;
    }

    private String getEntityDescriptionFromObj(Object o) {
        if (o instanceof Cls)
            return getEntityDescription((Cls) o);

        if (o instanceof Instance)
            return getEntityDescription((Instance) o);

        if (o instanceof Slot)
            return getEntityDescriptionFromSlot((Slot) o);
        
        return null;
    }

    private String getEntityDescription(Cls concept) {
        String description = null;

        try {
            if (concept != null) {
                Object io = null;
                Slot idSlot = kb_.getSlot(RadLex2EMFConstants.SLOT_Radlex_PREFERRED_NAME);
                if (concept.hasOwnSlot(idSlot))
                    io = concept.getOwnSlotValue(idSlot);
                else {
                    idSlot = kb_.getSlot(RadLex2EMFConstants.SLOT_Radlex_SYNONYMS_SLOT);

                    if (concept.hasOwnSlot(idSlot))
                        io = concept.getOwnSlotValue(idSlot);
                }

                description = (io != null) ? io.toString() : concept.getName();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return description;
    }

    private String getEntityDescriptionFromSlot(Slot concept) {
        String description = null;

        try {
            if (concept != null) {
                Object io = null;
                Slot idSlot = kb_.getSlot(RadLex2EMFConstants.SLOT_Radlex_PREFERRED_NAME);
                if (concept.hasOwnSlot(idSlot))
                    io = concept.getOwnSlotValue(idSlot);
                else {
                    idSlot = kb_.getSlot(RadLex2EMFConstants.SLOT_Radlex_SYNONYMS_SLOT);

                    if (concept.hasOwnSlot(idSlot))
                        io = concept.getOwnSlotValue(idSlot);
                }

                description = (io != null) ? io.toString() : concept.getName();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return description;
    }
    
    private String getEntityDescription(Instance concept) {
        String description = null;

        try {
            if (concept != null) {
                Object io = null;
                Slot idSlot = kb_.getSlot(RadLex2EMFConstants.SLOT_Radlex_PREFERRED_NAME);
                if (concept.hasOwnSlot(idSlot))
                    io = concept.getOwnSlotValue(idSlot);
                else {
                    idSlot = kb_.getSlot(RadLex2EMFConstants.SLOT_Radlex_SYNONYMS_SLOT);

                    if (concept.hasOwnSlot(idSlot))
                        io = concept.getOwnSlotValue(idSlot);
                }

                description = (io != null) ? io.toString() : concept.getName();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return description;
    }

    private void addPresentationToConcept(Concept con, String value, String slotName, String code) {
        boolean foundSome = false;
        List tps = Arrays.asList(con.getPresentation());
        if ((tps != null) && (tps.size() > 0)) {
            Iterator itr = tps.iterator();

            while (itr.hasNext()) {
                Object ob = itr.next();

                if (ob instanceof Presentation) {
                    if (value.equals(((Presentation) ob).getValue().getContent())) {
                        foundSome = true;
                        if (slotName.equals(RadLex2EMFConstants.SLOT_Radlex_PREFERRED_NAME))
                            ((Presentation) ob).setIsPreferred(new Boolean(true));
                    }
                }
            }
        }

        if (!foundSome) 
        {
            Presentation tp = new Presentation();
            Text txt = new Text();
            txt.setContent((String) value);
            tp.setValue(txt);

            if (slotName.equals(RadLex2EMFConstants.SLOT_Radlex_PREFERRED_NAME))
                tp.setIsPreferred(new Boolean(true));
            else
                tp.setIsPreferred(new Boolean(false));

            if (RadLex2EMFUtils.isNull(slotName))
                tp.setPropertyName(RadLex2EMFConstants.PROPERTY_TEXTPRESENTATION);
            else
            {
                if ((slotName.startsWith("Synonym"))||(slotName.startsWith("synonym")))
                    tp.setPropertyName(RadLex2EMFConstants.SLOT_Radlex_SYNONYM);
                else
                    tp.setPropertyName(slotName);
            }
            
            if (RadLex2EMFUtils.isNull(code))
            {
                //TODO: Deepak:Find if the slot's code with RadLex_ID exists and then use its code if present

                tp.setPropertyId(RadLex2EMFConstants.PROPERTY_ID_PREFIX + (++propertyCounter));
            }
            else
                tp.setPropertyId(code);
            
            con.addPresentation(tp);
        }
    }

    private void addPresentationAttribute(Object concept, Concept con, Slot slot) {
        String ConceptName = null;
        try {
            if ((concept == null) || (slot == null) || (con == null))
                return;

            ConceptName = ((Cls) concept).getBrowserText();

            String slotName = slot.getName();

            String value = getCorrectValueString(concept, slot, null, false);
            //String value = getEntityDescriptionFromObj(con);

            if (RadLex2EMFUtils.isNull(value))
                return;

            if ((!RadLex2EMFConstants.SLOT_Radlex_SYNONYM.equals(slotName)) &&
                    (!RadLex2EMFConstants.SLOT_Radlex_SYNONYM_OF.equals(slotName)))
            {
                addPresentationToConcept(con, value, slotName, null);
            } else {
                Collection slotValues = null;
                if (concept instanceof Instance) {
                    slotValues = ((Instance) concept).getOwnSlotValues(slot);
                }

                if ((slotValues != null) && (slotValues.size() > 0)) {
                    Iterator itr = slotValues.iterator();

                    while (itr.hasNext()) {
                        Object o = itr.next();
                        if (o instanceof Instance) {
                            Instance inst = (Instance) o;
                            Slot synonym_slot = kb_.getSlot(RadLex2EMFConstants.SLOT_Radlex_SYNONYMS_SLOT);
                            if (inst.hasOwnSlot(synonym_slot)) 
                            {
                                String code = getConceptCodeFromObj(inst); 
                                try 
                                {
                                    value = inst.getOwnSlotValue(synonym_slot).toString();
                                } catch (Exception e2) 
                                {
                                    value = code;
                                }

                                addPresentationToConcept(con, value, slotName, code);
                                // TODO: (DKS) Add property qualifier or
                                // property link for synonym
                            }
                            else
                            {
                                String code = getConceptCodeFromObj(inst); 
                                try 
                                {
                                    value = getEntityDescription(inst);
                                } catch (Exception e2) 
                                {
                                    value = code;
                                }

                                addPresentationToConcept(con, value, slotName, code);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Failed while getting value for textual representation for Slot=" + slot
                    + " in concept=" + ConceptName);
        }
    }

    private void addPropertyAttribute(Object concept, Concept con, Slot slot) {
        try {
            String property = RadLex2EMFUtils.toNMToken(slot.getName());

            if (!properties_.contains(property))
                properties_.add(property);

            Property pc = new Property();
            pc.setPropertyName(property);
          //TODO: Deepak:Find if the slot's code with RadLex_ID exists and then use its code if present
            pc.setPropertyId(RadLex2EMFConstants.PROPERTY_ID_PREFIX + (++propertyCounter));

            String value = getCorrectValueString(concept, slot, null, false);

            if (RadLex2EMFUtils.isNull(value))
                return;

            Text txt = new Text();
            txt.setContent((String) value);
            pc.setValue(txt);

            con.addProperty(pc);
        } catch (Exception e) {
            System.out.println("Failed while getting value for property for Slot=" + slot.getName());
        }
    }

    private void addAssociationBetweenSourcesAndInstances(String sourceCode, org.LexGrid.concepts.Instance inst, String relation)
    {
        AssociationPredicate assocClass = null;
        AssociationEntity assocEntityClass = null;

        if ((RadLex2EMFUtils.isNull(sourceCode))||
            (RadLex2EMFUtils.isNull(relation))||
            (inst == null))
            return;
        
        boolean createdNew = false;
        
        try 
        {
            // See if this is already processed relation
            if (associationHash_.containsKey(relation)) {
                Object associationHashValue = associationHash_.get(relation);
                Object associationEntityHashValue = associationEntityHash_.get(relation);

                if (!(associationHashValue instanceof AssociationPredicate)) 
                {
                    return;
                }

                assocClass = (AssociationPredicate) associationHashValue;
                assocEntityClass = (AssociationEntity)associationEntityHashValue;
            } 
            else 
            {
                assocClass = new AssociationPredicate();
                assocClass.setAssociationName(relation);
                
                assocEntityClass = new AssociationEntity();
                assocEntityClass.setEntityCode(relation);
                assocEntityClass.setForwardName(relation);
                createdNew = true;
            }
            
            AssociationSource aI = null;
            
            if (!createdNew)
            {
                List asources = Arrays.asList(assocClass.getSource());
                
                if ((asources != null)&&(!asources.isEmpty()))
                {
                    for (Iterator<AssociationSource> itr = asources.iterator(); itr.hasNext();)
                    {
                        AssociationSource src = itr.next();
                        if (src.getSourceEntityCode() == sourceCode)
                        {
                            aI = src;
                            break;
                        }
                    }
                }
            }
            
            if (aI == null)
            {
                aI = new AssociationSource();
                aI.setSourceEntityCode(sourceCode);
            }
            
            AssociationTarget aT = new AssociationTarget();
            aT.setTargetEntityCode(inst.getEntityCode());
            aI.addTarget(aT);
            RelationsUtil.subsume(assocClass, aI);
            
            //System.out.println("Added [" + sourceCode + "] -> [" + relation + "] -> [" + inst.getEntityCode() + "]");
            if (createdNew) 
            {
                allRelations_.addAssociationPredicate(assocClass);

                if (!associations_.contains(relation))
                    associations_.add(relation);

                associationHash_.put(relation, assocClass);
                associationEntityHash_.put(relation, assocEntityClass);
            }
        }
        catch (Exception e) 
        {
            System.out.println("Failed while adding association for Instance=" + inst.getEntityDescription());
            e.printStackTrace();
        }
    }
    
    private void addAssociationAttribute(Object concept, Concept con, Slot slot) {
        try {
            String inverseRelation = null;

            if ((concept == null) || (slot == null) || (con == null))
                return;

            String relation = slot.getName();
            // String relation = RadLex2EMFUtils.toNMToken(slot.getName());

            if (RadLex2EMFUtils.isNull(relation))
                return;

            AssociationPredicate assocClass = null;
            AssociationEntity assocEntityClass = null;
            
            boolean createdNew = false;
            boolean isReverseDirection = false;

            try {
                // See if this is already processed relation
                if (associationHash_.containsKey(relation)) {
                    Object associationHashValue = associationHash_.get(relation);
                    Object associationEntityHashValue = associationEntityHash_.get(relation);

                    if (!(associationHashValue instanceof AssociationPredicate)) {
                        return;
                    }

                    assocClass = (AssociationPredicate) associationHashValue;
                    assocEntityClass = (AssociationEntity) associationEntityHashValue;
                } else {
                    // May be this is a reverse of an already processed relation
                    if (associationRevHash_.containsKey(relation)) {
                        Object associationHashValue = associationRevHash_.get(relation);
                        Object associationEntityHashValue = associationEntityHash_.get(relation);

                        if (!(associationHashValue instanceof AssociationPredicate)) {
                            return;
                        }

                        assocClass = (AssociationPredicate) associationHashValue;
                        assocEntityClass = (AssociationEntity) associationEntityHashValue;
                        isReverseDirection = true;
                    } else {
                        String originalRevName = null;
                        try {
                            Slot invSlot = kb_.getInverseSlot(slot);
                            if (invSlot != null)
                                originalRevName = invSlot.getName();
                        } catch (Exception e) {
                        }

                        // It is a new relation, Lets make an entry into list of
                        // relations
                        // inverseRelation = getIneverseRelationName(slot,
                        // relation);

                        assocClass = new AssociationPredicate();
                        assocClass.setAssociationName(relation);
                        
                        assocEntityClass = new AssociationEntity();
                        assocEntityClass.setEntityCode(relation);
                        assocEntityClass.setForwardName(relation);

                        if (!RadLex2EMFUtils.isNull(originalRevName))
                            assocEntityClass.setReverseName(originalRevName);

                        createdNew = true;
                    }
                }

                int slotCount = 0;

                if (concept instanceof Cls)
                {
                    slotCount = ((Cls) concept).getOwnSlotValueCount(slot);
                    
                    if (slotCount == 0)
                    {
                       Collection svc = ((Cls) concept).getTemplateSlotValues(slot); 
                       slotCount = ((Cls) concept).getTemplateSlotValues(slot).size();
                    }
                }
                else 
                    if (concept instanceof Instance)
                    {
                        slotCount = ((Instance) concept).getOwnSlotValueCount(slot);
                    }
                
                if (slotCount > 0) 
                {
                    ValueType vT = slot.getValueType();

                    Vector valVector = new Vector();
                    Collection slotValues = null;

                    if (concept instanceof Cls) {
                        slotValues = ((Cls) concept).getOwnSlotValues(slot);
                    } else {
                        if (concept instanceof Instance)
                            slotValues = ((Instance) concept).getOwnSlotValues(slot);
                    }

                    if ((slotValues != null) && (slotValues.size() > 0)) {
                        Iterator itr = slotValues.iterator();

                        while (itr.hasNext()) {
                            Object o = itr.next();
                            String stringValue = getCorrectValueString(concept, slot, o, true);

                            if (!RadLex2EMFUtils.isNull(stringValue)) {
                                if ((ValueType.CLS.equals(vT)) || (ValueType.INSTANCE.equals(vT))) {
                                    AssociationTarget aT = new AssociationTarget();
                                    aT.setTargetEntityCode(stringValue);
                                    valVector.add(aT);
                                } else {
                                    AssociationData aD = new AssociationData();
                                    String dataType = getCorrectDataType(vT);
                                    // aD.setDataType(dataType);
                                    // aD.setDataId(dataType);
                                    Text txt = new Text();
                                    txt.setContent(stringValue);
                                    txt.setDataType(dataType);
                                    aD.setAssociationDataText(txt);
                                    valVector.add(aD);
                                }
                            }
                        }
                    }

                    if (valVector.size() > 0) {

                        if (!isReverseDirection) {
                            // Relation is same as forward name
                            AssociationSource aI = new AssociationSource();

                            if ((ValueType.CLS.equals(vT)) || (ValueType.INSTANCE.equals(vT))) {
                                aI.setSourceEntityCode(con.getEntityCode());
                                for (int i = 0; i < valVector.size(); i++)
                                    aI.addTarget((AssociationTarget) valVector.elementAt(i));
                            } else {
                                aI.setSourceEntityCode(con.getEntityCode());
                                for (int i = 0; i < valVector.size(); i++)
                                    aI.addTargetData((AssociationData) valVector.elementAt(i));
                            }

                            

                            // assocClass.getSource().add(aI);
                            RelationsUtil.subsume(assocClass, aI);
                        } else {
                            // Relation is same as reverse name
                            if ((ValueType.CLS.equals(vT)) || (ValueType.INSTANCE.equals(vT))) {
                                for (int i = 0; i < valVector.size(); i++) {
                                    AssociationSource aI = new AssociationSource();
                                    aI.setSourceEntityCode(((AssociationTarget) valVector.elementAt(i))
                                            .getTargetEntityCode());
                                    AssociationTarget aT2 = new AssociationTarget();
                                    aT2.setTargetEntityCode(con.getEntityCode());
                                    RelationsUtil.subsume(aI, aT2);
                                    // assocClass.getSource().add(aI);
                                    RelationsUtil.subsume(assocClass, aI);
                                }
                            }
                        }
                    }
                }

                if (createdNew) {
                    allRelations_.addAssociationPredicate(assocClass);

                    if (!associations_.contains(relation))
                        associations_.add(relation);

                    associationHash_.put(relation, assocClass);
                    associationEntityHash_.put(relation, assocEntityClass);
                    if (inverseRelation != null)
                        associationRevHash_.put(inverseRelation, assocClass);
                }
            } catch (Exception e) {
                System.out.println("Error while creating/finding association object for=" + relation);
                e.printStackTrace();
                return;
            }
        } catch (Exception e) {
            System.out.println("Failed while adding association for Slot=" + slot.getName());
            e.printStackTrace();
        }
    }

    private String getCorrectDataType(ValueType vT) {
        if (ValueType.ANY.equals(vT))
            return RadLex2EMFConstants.PROT_ANY;

        if (ValueType.BOOLEAN.equals(vT))
            return RadLex2EMFConstants.PROT_BOOLEAN;

        if (ValueType.CLS.equals(vT))
            return RadLex2EMFConstants.PROT_CLS;

        if (ValueType.FLOAT.equals(vT))
            return RadLex2EMFConstants.PROT_FLOAT;

        if (ValueType.INSTANCE.equals(vT))
            return RadLex2EMFConstants.PROT_INSTANCE;

        if (ValueType.INTEGER.equals(vT))
            return RadLex2EMFConstants.PROT_INTEGER;

        if (ValueType.STRING.equals(vT))
            return RadLex2EMFConstants.PROT_STRING;

        if (ValueType.SYMBOL.equals(vT))
            return RadLex2EMFConstants.PROT_SYMBOL;

        return RadLex2EMFConstants.PROT_STRING;
    }

    private String getCorrectValueString(Object concept, Slot slt, Object vObj, boolean returnCode) {
        String result = "";
        ValueType vT = slt.getValueType();

        Object valObj = null;

        if (vObj == null) {
            if (concept instanceof Cls)
                valObj = ((Cls) concept).getOwnSlotValue(slt);
            if (concept instanceof Instance)
                valObj = ((Instance) concept).getOwnSlotValue(slt);
        } else
            valObj = vObj;

        if (ValueType.ANY.equals(vT)) {
            if (valObj != null)
                result = valObj.toString();
        }

        if (ValueType.BOOLEAN.equals(vT)) {
            if (valObj != null)
                result = ((Boolean) valObj).toString();
        }

        if (ValueType.CLS.equals(vT)) {
            if ((valObj != null) && (valObj instanceof Cls)) {
                if (returnCode)
                    result = getConceptCodeFromObj((Cls) valObj);
                else
                {
                    result = getEntityDescriptionFromObj(((Cls) valObj));
                    
                    if (RadLex2EMFUtils.isNull(result))
                        result = ((Cls) valObj).getName();
                }
            }
        }

        if (ValueType.FLOAT.equals(vT)) {
            if (valObj != null)
                result = ((Float) valObj).toString();
        }

        if (ValueType.INSTANCE.equals(vT)) {
            if ((valObj != null) && (valObj instanceof Instance)) {
                result = getConceptCodeFromObj((Instance) valObj);
            }
        }

        if (ValueType.INTEGER.equals(vT)) {
            if (valObj != null)
                result = ((Integer) valObj).toString();
        }

        if (ValueType.STRING.equals(vT)) {
            if (valObj != null)
                result = (String) valObj;
        }

        if (ValueType.SYMBOL.equals(vT)) {
            if (valObj != null)
                result = (String) valObj;
        }

        return result;
    }

    private Comment[] getCommentsFromObj(Object o) {
        if (o instanceof Cls)
            return getComments((Cls) o);

        if (o instanceof Instance)
            return getComments((Instance) o);

        if (o instanceof Slot)
            return getCommentsFromSlot((Slot) o);
        
        return null;
    }

    private Comment[] getComments(Cls concept) {
        try {
            Slot cSlt = kb_.getSlot(RadLex2EMFConstants.SLOT_COMMENT);
            String comment1 = (String) concept.getOwnSlotValue(cSlt);

            Slot cmtSlot = kb_.getSlot(RadLex2EMFConstants.SLOT_DOCUMENTATION);
            String comment2 = (String) concept.getOwnSlotValue(cmtSlot);
            
            Comment cmt1 = null;
            
            if (!RadLex2EMFUtils.isNull(comment1)) 
            {
                cmt1 = new Comment();
                Text txt = new Text();
                txt.setContent(RadLex2EMFUtils.removeInvalidXMLCharacters(comment1, concept.getName()));
                cmt1.setValue(txt);
                cmt1.setPropertyName(RadLex2EMFConstants.PROPERTY_COMMENT);
                cmt1.setPropertyId(RadLex2EMFConstants.PROPERTY_ID_PREFIX + (++propertyCounter));
            }
            
            Comment cmt2 = null;
            
            if (!RadLex2EMFUtils.isNull(comment2)) 
            {
                cmt2 = new Comment();
                Text txt = new Text();
                txt.setContent(RadLex2EMFUtils.removeInvalidXMLCharacters(comment2, concept.getName()));
                cmt2.setValue(txt);
                cmt2.setPropertyName(RadLex2EMFConstants.PROPERTY_COMMENT);
                cmt2.setPropertyId(RadLex2EMFConstants.PROPERTY_ID_PREFIX + (++propertyCounter));
            }
            
            return new Comment[] { cmt1, cmt2 };
            
        } catch (Exception ex) {
            System.out.println("Failed while getting value for comment for concept=" + concept.getName());
        }

        return null;
    }

    private Comment[] getCommentsFromSlot(Slot concept) {
        try {
            Slot cSlt = kb_.getSlot(RadLex2EMFConstants.SLOT_COMMENT);
            String comment1 = (String) concept.getOwnSlotValue(cSlt);

            Slot cmtSlot = kb_.getSlot(RadLex2EMFConstants.SLOT_DOCUMENTATION);
            String comment2 = (String) concept.getOwnSlotValue(cmtSlot);
            
            Comment cmt1 = null;
            
            if (!RadLex2EMFUtils.isNull(comment1)) 
            {
                cmt1 = new Comment();
                Text txt = new Text();
                txt.setContent(RadLex2EMFUtils.removeInvalidXMLCharacters(comment1, concept.getName()));
                cmt1.setValue(txt);
                cmt1.setPropertyName(RadLex2EMFConstants.PROPERTY_COMMENT);
                cmt1.setPropertyId(RadLex2EMFConstants.PROPERTY_ID_PREFIX + (++propertyCounter));
            }
            
            Comment cmt2 = null;
            
            if (!RadLex2EMFUtils.isNull(comment2)) 
            {
                cmt2 = new Comment();
                Text txt = new Text();
                txt.setContent(RadLex2EMFUtils.removeInvalidXMLCharacters(comment2, concept.getName()));
                cmt2.setValue(txt);
                cmt2.setPropertyName(RadLex2EMFConstants.PROPERTY_COMMENT);
                cmt2.setPropertyId(RadLex2EMFConstants.PROPERTY_ID_PREFIX + (++propertyCounter));
            }
            
            return new Comment[] { cmt1, cmt2 };
            
        } catch (Exception ex) {
            System.out.println("Failed while getting value for comment for concept=" + concept.getName());
        }

        return null;
    }
    
    private Comment[] getComments(Instance concept) {
        try {
            Slot cSlt = kb_.getSlot(RadLex2EMFConstants.SLOT_COMMENT);
            String comment1 = (String) concept.getOwnSlotValue(cSlt);

            Slot cmtSlot = kb_.getSlot(RadLex2EMFConstants.SLOT_DOCUMENTATION);
            String comment2 = (String) concept.getOwnSlotValue(cmtSlot);
            
            Comment cmt1 = null;
            
            if (!RadLex2EMFUtils.isNull(comment1)) 
            {
                cmt1 = new Comment();
                Text txt = new Text();
                txt.setContent(RadLex2EMFUtils.removeInvalidXMLCharacters(comment1, concept.getName()));
                cmt1.setValue(txt);
                cmt1.setPropertyName(RadLex2EMFConstants.PROPERTY_COMMENT);
                cmt1.setPropertyId(RadLex2EMFConstants.PROPERTY_ID_PREFIX + (++propertyCounter));
            }
            
            Comment cmt2 = null;
            
            if (!RadLex2EMFUtils.isNull(comment2)) 
            {
                cmt2 = new Comment();
                Text txt = new Text();
                txt.setContent(RadLex2EMFUtils.removeInvalidXMLCharacters(comment2, concept.getName()));
                cmt2.setValue(txt);
                cmt2.setPropertyName(RadLex2EMFConstants.PROPERTY_COMMENT);
                cmt2.setPropertyId(RadLex2EMFConstants.PROPERTY_ID_PREFIX + (++propertyCounter));
            }
            
            return new Comment[] { cmt1, cmt2 };
            
        } catch (Exception ex) {
            System.out.println("Failed while getting value for comment for Instance=" + concept.getName());
        }

        return null;
    }

    private Definition[] getDefinitionsFromObj(Object o) {
        if (o instanceof Cls)
            return getDefinitions((Cls) o);

        if (o instanceof Instance)
            return getDefinitions((Instance) o);

        if (o instanceof Instance)
            return getDefinitions((Slot) o);
        
        return null;
    }

    private Definition[] getDefinitions(Cls concept) {
        try {
            String definition = (String) concept.getOwnSlotValue(kb_.getSlot(RadLex2EMFConstants.SLOT_DEFINITION));

            if (definition != null) {
                Definition def = new Definition();
                Text txt = new Text();
                txt.setContent(RadLex2EMFUtils.removeInvalidXMLCharacters(definition, concept.getName()));
                def.setValue(txt);
                def.setPropertyName(RadLex2EMFConstants.PROPERTY_DEFINITION);
                def.setPropertyId(RadLex2EMFConstants.PROPERTY_ID_PREFIX + (++propertyCounter));

                return new Definition[] { def };
            }
        } catch (Exception e) {
            System.out.println("Failed while getting value for definition for concept=" + concept.getName());
        }

        return null;
    }

    private Definition[] getDefinitions(Slot concept) {
        try {
            String definition = (String) concept.getOwnSlotValue(kb_.getSlot(RadLex2EMFConstants.SLOT_DEFINITION));

            if (definition != null) {
                Definition def = new Definition();
                Text txt = new Text();
                txt.setContent(RadLex2EMFUtils.removeInvalidXMLCharacters(definition, concept.getName()));
                def.setValue(txt);
                def.setPropertyName(RadLex2EMFConstants.PROPERTY_DEFINITION);
                def.setPropertyId(RadLex2EMFConstants.PROPERTY_ID_PREFIX + (++propertyCounter));

                return new Definition[] { def };
            }
        } catch (Exception e) {
            System.out.println("Failed while getting value for definition for concept=" + concept.getName());
        }

        return null;
    }
    
    private Definition[] getDefinitions(Instance concept) {
        try {
            String definition = (String) concept.getOwnSlotValue(kb_.getSlot(RadLex2EMFConstants.SLOT_DEFINITION));

            if (definition != null) {
                Definition def = new Definition();
                Text txt = new Text();
                txt.setContent(RadLex2EMFUtils.removeInvalidXMLCharacters(definition, concept.getName()));
                def.setValue(txt);
                def.setPropertyName(RadLex2EMFConstants.PROPERTY_DEFINITION);
                def.setPropertyId(RadLex2EMFConstants.PROPERTY_ID_PREFIX + (++propertyCounter));

                return new Definition[] { def };
            }
        } catch (Exception e) {
            System.out.println("Failed while getting value for definition for Instnace=" + concept.getName());
        }

        return null;
    }

    public void populateSupportedProperties(CodingScheme csclass) {
        if (csclass == null)
            return;

        try {
            if (properties_.size() > 0) {
                for (int i = 0; i < properties_.size(); i++) {
                    String prp = (String) properties_.elementAt(i);

                    if (!RadLex2EMFUtils.isNull(prp)) {
                        SupportedProperty suppProp = new SupportedProperty();

                        if ((!RadLex2EMFConstants.PROPERTY_COMMENT.equals(prp))
                                && (!RadLex2EMFConstants.PROPERTY_DEFINITION.equals(prp))
                                && (!RadLex2EMFConstants.PROPERTY_INSTRUCTION.equals(prp))
                                && (!RadLex2EMFConstants.PROPERTY_TEXTPRESENTATION.equals(prp))) {
                            suppProp.setUri(RadLex2EMFUtils.getWithRadlexURN(prp));
                        }

                        suppProp.setLocalId(prp);
                        csclass.getMappings().addSupportedProperty(suppProp);
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("Failed while getting supported properties!");
        }
    }

    public void populateSupportedAssociations(CodingScheme csclass) {
        try {
            if (csclass == null)
                return;

            int assocSize = associations_.size();
            int assocAliasSize = associationsAliases_.size();
            int total = assocSize + assocAliasSize;

            if (total > 0) {
                if (assocSize > 0) {
                    for (int i = 0; i < assocSize; i++) {
                        String sA = (String) associations_.elementAt(i);
                        if (!RadLex2EMFUtils.isNull(sA)) {
                            SupportedAssociation suppAss = new SupportedAssociation();
                            suppAss.setUri(RadLex2EMFUtils.getWithRadlexURN(sA));
                            suppAss.setLocalId(RadLex2EMFUtils.toNMToken(sA));
                            csclass.getMappings().addSupportedAssociation(suppAss);
                        }
                    }
                }

                if (assocAliasSize > 0) {
                    for (int j = assocSize; j < total; j++) {
                        String sA2 = (String) associationsAliases_.elementAt(j - assocSize);
                        String[] ali = sA2.split(RadLex2EMFConstants.DELIM);

                        if ((ali != null) && (ali.length > 1)) {
                            if (!RadLex2EMFUtils.isNull(ali[1])) {
                                SupportedAssociation suppAss = new SupportedAssociation();
                                suppAss.setUri(RadLex2EMFUtils.getWithRadlexURN(ali[0]));
                                suppAss.setLocalId(RadLex2EMFUtils.toNMToken(ali[1]));
                                csclass.getMappings().addSupportedAssociation(suppAss);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed while getting supported associations!");
        }
    }

    public void populateSupportedSources(CodingScheme csclass) {
        if (csclass == null)
            return;

        try {
            int srcsSize = sources_.size();

            if (srcsSize > 0) {
                if (srcsSize > 0) {
                    for (int i = 0; i < srcsSize; i++) {
                        String src = (String) sources_.elementAt(i);

                        if (!RadLex2EMFUtils.isNull(src)) {
                            SupportedSource suppSrc = new SupportedSource();
                            suppSrc.setUri(RadLex2EMFUtils.getWithRadlexURN(src));
                            suppSrc.setLocalId(RadLex2EMFUtils.toNMToken(src));
                            csclass.getMappings().addSupportedSource(suppSrc);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed while getting supported Sources!");
        }
    }

    public void populateSupportedRepresentationalForms(CodingScheme csclass) {
        if (csclass == null)
            return;

        try {
            int repfSize = representationalForms_.size();

            if (repfSize > 0) {
                if (repfSize > 0) {
                    for (int i = 0; i < repfSize; i++) {
                        String rep = (String) representationalForms_.elementAt(i);

                        if (!RadLex2EMFUtils.isNull(rep)) {
                            SupportedRepresentationalForm suppRpf = new SupportedRepresentationalForm();
                            suppRpf.setUri(RadLex2EMFUtils.getWithRadlexURN(rep));
                            suppRpf.setLocalId(RadLex2EMFUtils.toNMToken(rep));
                            csclass.getMappings().addSupportedRepresentationalForm(suppRpf);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed while getting supported Representational Forms !");
        }
    }

}