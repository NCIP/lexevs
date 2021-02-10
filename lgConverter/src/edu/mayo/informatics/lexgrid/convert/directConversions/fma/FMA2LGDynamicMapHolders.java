
package edu.mayo.informatics.lexgrid.convert.directConversions.fma;

import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Comment;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.types.EntityTypes;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
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
import org.LexGrid.custom.relations.RelationsUtil;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Reference;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.model.ValueType;

/*
 * @author <A HREF="mailto:dks02@mayo.edu">Deepak Sharma</A> 
 * @author <A HREF="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala</A>
 * 
 * @version subversion $Revision: 2917 $ checked in on $Date: 2006-06-19
 *          15:52:21 +0000 (Mon, 19 Jun 2006) $
 */

public class FMA2LGDynamicMapHolders {
    private Vector conceptList_ = new Vector();
    // private Vector codedEntries_ = new Vector();

    private Vector properties_ = null;

    private Vector associations_ = null;
    private Vector sources_ = null;
    private Vector representationalForms_ = null;
    private Vector associationsAliases_ = null;
    private Vector relations_ = new Vector();

    private Hashtable associationPredicateHash_ = new Hashtable();
    private Hashtable associationEntityHash_ = new Hashtable();

    private long propertyCounter = 0;

    private KnowledgeBase kb_;

    /** ****************************** */

    private AssociationPredicate hasSubTypeAssocClass_ = null;
    private AssociationEntity hasSubTypeAssocEntityClass_ = null;
    private Relations allRelations_ = null;
    private Entities allConcepts_ = null;

    /** ****************************** */
    public boolean processFMA(CodingScheme csclass, KnowledgeBase kb) {
        kb_ = kb;
        boolean success = true;
        try {
            // loadMappings();

            // initialize with static properties
            properties_ = FMA2LGStaticMapHolders.getFixedProperties();

            if (properties_ == null)
                properties_ = new Vector();

            associations_ = FMA2LGStaticMapHolders.getFixedAssociations();
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
            hasSubTypeAssocClass_.setAssociationName(FMA2LGConstants.ASSOCIATION_HASSUBTYPE);
            
            hasSubTypeAssocEntityClass_ = new AssociationEntity();
            hasSubTypeAssocEntityClass_.setEntityCode(FMA2LGConstants.ASSOCIATION_HASSUBTYPE);
            hasSubTypeAssocEntityClass_.setForwardName(FMA2LGConstants.ASSOCIATION_HASSUBTYPE);
            hasSubTypeAssocEntityClass_.setReverseName(FMA2LGConstants.ASSOCIATION_ISA);
            hasSubTypeAssocEntityClass_.setIsTransitive(new Boolean(true));
            allRelations_.addAssociationPredicate(hasSubTypeAssocClass_);

            relations_.add(csclass.getRelations());

            boolean testing = false;

            if (testing) {
                System.out.println("<<<<<<<------TEST MODE----->>>>>>");
                Cls topCls = kb_.getCls("Protein");
                loadConcept(topCls);
                System.out.println("Processing done for all Concept Name Instances at " + (new java.util.Date()));
            } else {
                for (int i = 0; i < FMA2LGConstants.topClasses.length; i++) {
                    System.out.println("Processing Branch -->" + FMA2LGConstants.topClasses[i]);
                    Cls topCls = kb_.getCls(FMA2LGConstants.topClasses[i]);
                    loadConcept(topCls);
                }

                System.out.println("Processing done for all branches at " + (new java.util.Date()));

                System.out.println("Now Processing Concept Name Instances...");

                Cls conNamesCls = kb_.getCls("Concept name");

                if (conNamesCls != null)
                    // loadConceptNameInstances(conNamesCls);

                    System.out.println("Processing done for all Concept Name Instances at " + (new java.util.Date()));
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
                // System.out.println("Loading ConceptCode="+conceptCode);
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
                                        // System.out.println("Processing
                                        // concept=" + c.getName());
                                        loadConcept(c);
                                    }
                                }
                            }
                        }

                        if ((!conceptCode.equals("67361")) && (!conceptCode.equals("Concept name"))
                                && (!conceptCode.equals("ConceptName"))) {
                            classes = kb_.getDirectInstances((Cls) parentObj);

                            if ((classes != null) && (classes.size() > 0)) {
                                addParentChildRelationship(conceptCode, classes);

                                Iterator itr = classes.iterator();
                                while (itr.hasNext()) {
                                    Object obj = itr.next();
                                    if (obj instanceof Instance) {
                                        Instance i = (Instance) obj;
                                        if (!i.getName().startsWith(":")) {
                                            // System.out.println("Processing
                                            // Instance=" + i.getName());
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
    }

    /*
     * private void loadConceptNameInstances(Cls conNamesCls) { if (conNamesCls
     * != null) { String conceptCode = getConceptCodeFromObj(conNamesCls);
     * Collection classes = kb_.getDirectInstances(conNamesCls);
     * 
     * long recsDone = 0;
     * 
     * if ((classes != null) && (classes.size() > 0)) {
     * System.out.println("Total Instances are:" + classes.size()); //
     * addParentChildRelationship(conceptCode, classes);
     * 
     * Iterator itr = classes.iterator(); while (itr.hasNext()) { Object obj =
     * itr.next(); if (obj instanceof Instance) { Instance i = (Instance) obj;
     * if (!i.getName().startsWith(":")) { //
     * System.out.println("Processing Instance=" + // i.getName());
     * loadConcept(i); } }
     * 
     * recsDone++;
     * 
     * if (recsDone % 100 == 0) System.out.println("AT:" + (new
     * java.util.Date()) + " Done so far=" + recsDone);
     * 
     * } } } }
     */

    private boolean storeConcept(Object concept, String conceptCode) {
        boolean stored = false;
        boolean conceptNameInstance = false;
        if ((conceptCode != null) && (!conceptCode.startsWith(":"))) {
            try {
                conceptNameInstance = false;
                Instance cnInst = null;

                if (concept instanceof Instance) {
                    cnInst = ((Instance) concept);

                    try {
                        if ((cnInst != null) && (cnInst.getDirectType().getName().equals("Concept name"))) {
                            conceptNameInstance = true;
                        }
                    } catch (Exception e) {
                    }
                }

                if ((!conceptList_.contains(conceptCode)) && (!conceptNameInstance)) {
                    propertyCounter = 0;
                    conceptList_.add(conceptCode);

                    Entity con = new Entity();
                    con.setEntityType(new String[]{EntityTypes.CONCEPT.toString()});
                    con.setEntityCode(conceptCode);

                    String description = getEntityDescriptionFromObj(concept);
                    if (description != null) {
                        EntityDescription ed = new EntityDescription();
                        ed.setContent(description);
                        con.setEntityDescription(ed);
                        addEntityDescriptionAsPresentation(con);
                    }

                    Comment[] comments = getCommentsFromObj(concept);
                    if (comments != null)
                        for (int i = 0; i < comments.length; i++)
                            con.addComment(comments[i]);

                    Definition[] definitions = getDefinitionsFromObj(concept);
                    if (definitions != null)
                        for (int i = 0; i < definitions.length; i++)
                            con.addDefinition(definitions[i]);

                    processSlots(concept, con, false);

//                    allCodedEntries_.add(con);
                    allConcepts_.addEntity(con);
                    stored = true;
                } else {
                    if (conceptNameInstance) {
                        Collection references = cnInst.getReferences();

                        if ((references != null) && (references.size() > 0)) {
                            Iterator itrt = references.iterator();

                            while (itrt.hasNext()) {
                                Object ref = itrt.next();
                                Reference refO = (Reference) ref;
                                // System.out.println(refO.toString());

                                try {
                                    Cls owner = (Cls) refO.getFrame();

                                    if (owner != null) {
                                        String clsNm = getConceptCodeFromCls(owner);
                                        if ((!clsNm.equals("67361")) && (!clsNm.equals("Concept name"))
                                                && (!clsNm.equals("ConceptName"))) {
                                            Entity con = getConceptFromVector(clsNm);

                                            if (con != null)
                                                processSlots(cnInst, con, true);
                                        }
                                    }
                                } catch (Exception e) {
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Failed while storing concept!");
                e.printStackTrace();
            }
        }

        return stored;
    }

    private void addEntityDescriptionAsPresentation(Entity con) {
        Presentation tp = new Presentation();
        Text txt = new Text();
        txt.setContent(con.getEntityDescription().getContent());

        tp.setValue(txt);
        tp.setPropertyName(FMA2LGConstants.PROPERTY_TEXTPRESENTATION);
        tp.setPropertyId(FMA2LGConstants.PROPERTY_ID_PREFIX + (++propertyCounter));
        con.addPresentation(tp);
    }

    /*
     * private Concept findConcept(String conceptCode) { Concept con = null;
     * 
     * try { con = getConceptFromVector(conceptCode);
     * 
     * if (con == null) { Cls targetCls = kb_.getCls(conceptCode);
     * 
     * if (targetCls != null) con =
     * getConceptFromVector(getConceptCodeFromCls(targetCls)); } } catch
     * (Exception e) {
     * System.out.println("Could not find coded entry for Instance=" +
     * conceptCode); }
     * 
     * return con; }
     */

    private Entity getConceptFromVector(String name) {
        try {
            if (allConcepts_.getEntity() != null) {
                Iterator itr = Arrays.asList(allConcepts_.getEntity()).iterator();
                while (itr.hasNext()) {
                    Object ob = itr.next();

                    if ((ob != null) && (ob instanceof Entity)) {
                        if (((Entity) ob).getEntityCode().equals(name))
                            return ((Entity) ob);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Failed while looking for coded entry for name=" + name);
        }

        return null;
    }

    public int getApproxNumberOfConcepts() {
        int num = 0;
        if (allConcepts_.getEntity() != null) {
            num = allConcepts_.getEntity().length;
        }
        return num;
    }

    private void processSlots(Object concept, Entity con, boolean restricted) {
        Collection slots = null;

        if (concept instanceof Cls) {
            slots = ((Cls) concept).getOwnSlots();
        } else {
            if (concept instanceof Instance)
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
                        String slotType = null;

                        if (restricted) {
                            processRestrictedSlot(concept, con, slot);
                        } else {
                            slotType = getSlotTypeFromSlot(slot);

                            if (!FMA2LGUtils.isNull(slotType)) {
                                boolean hasValue = false;

                                if (concept instanceof Cls) {
                                    hasValue = (((Cls) concept).getOwnSlotValueCount(slot) > 0);
                                } else {
                                    if (concept instanceof Instance)
                                        hasValue = (((Instance) concept).getOwnSlotValueCount(slot) > 0);
                                }

                                if (hasValue)
                                    addSlotDetailsToConcept(concept, con, slot, slotType);
                            }
                        }
                    }
                }
            }

            // if (con.getPresentationCount() == 0)
            // {
            // copyPresentationAttributeFromDesc(concept, con);
            // }
        }
    }

    private String getSlotTypeFromSlot(Slot slot) {
        String type = null;

        if (slot != null) {
            String slotName = slot.getName();

            if ((FMA2LGConstants.SLOT_FMA_CONCEPT_CODE.equals(slotName))
                    || (FMA2LGConstants.SLOT_NAME.equals(slotName))
                    || (FMA2LGConstants.SLOT_DEFINITION.equals(slotName)))
                return type;

            if ((FMA2LGConstants.SLOT_FMA_PREFERRED_NAME.equals(slotName))
                    || (FMA2LGConstants.SLOT_FMA_SYNONYM.equals(slotName))
                    || (FMA2LGConstants.SLOT_FMA_NON_ENG_EQUIV.equals(slotName))
                    || (FMA2LGConstants.SLOT_FMA_OTHER_ENG_EQUIV.equals(slotName))) {
                type = FMA2LGConstants.SLOT_TYPE_PRESENTATION;
            } else {
                ValueType vT = slot.getValueType();

                if ((ValueType.CLS.equals(vT)) || (ValueType.INSTANCE.equals(vT)))
                    type = FMA2LGConstants.SLOT_TYPE_ASSOCIATION;
                else
                    type = FMA2LGConstants.SLOT_TYPE_PROPERTY;
            }
        }

        return type;
    }

    private void processRestrictedSlot(Object concept, Entity con, Slot slot) {

        if ((concept != null) && (con != null) && (slot != null)) {
            String slotName = slot.getName();
            String conceptName = getConceptCodeFromObj(concept);

            try {
                if (("authority".equalsIgnoreCase(slotName)) || ("Language".equalsIgnoreCase(slotName))
                        || ("TA ID".equalsIgnoreCase(slotName))
                        || ("Abbreviation".equalsIgnoreCase(slotName))
                        ||
                        // ("Other Latin
                        // equivalents".equalsIgnoreCase(slotName)) ||
                        ("Latin name (TA)".equalsIgnoreCase(slotName)) || ("Eponymn".equalsIgnoreCase(slotName))
                        || ("Source".equalsIgnoreCase(slotName))) {
                    String value = null;
                    Collection values = null;
                    if (concept instanceof Instance) {
                        values = ((Instance) concept).getOwnSlotValues(slot);
                    } else if (concept instanceof Cls) {
                        values = ((Cls) concept).getOwnSlotValues(slot);
                    }

                    if ((values != null) && (values.size() > 0)) {
                        Iterator itr = values.iterator();

                        while (itr.hasNext()) {
                            String val = null;

                            if ("Eponymn".equalsIgnoreCase(slotName)) {
                                val = ((Boolean) itr.next()).toString();
                            } else
                                val = (String) itr.next();

                            if (val != null) {
                                if (("authority".equalsIgnoreCase(slotName)) || ("Source".equalsIgnoreCase(slotName))) {
                                    value = FMA2LGUtils.toSourceFormat(val);
                                } else
                                    value = val;
                            }

                            // Now we have value now make changes to codeEntry.
                            if (!FMA2LGUtils.isNull(value)) {
                                modifyPresentationAttribute(con, conceptName, slotName, value);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Failed while getting eligible slot values...");
            }
        }
    }

    private void addParentChildRelationship(String parentConceptcode, Collection subclasses) {
        if ((subclasses != null) && (subclasses.size() > 0)) {
            AssociationSource aI = new AssociationSource();
            aI.setSourceEntityCode(parentConceptcode);
            aI = RelationsUtil.subsume(hasSubTypeAssocClass_, aI);
            Iterator itr = subclasses.iterator();
            while (itr.hasNext()) {
                Object o = itr.next();
                String childConceptCode = getConceptCodeFromObj(o);
                if (childConceptCode != null) {
                    AssociationTarget aT = new AssociationTarget();
                    aT.setTargetEntityCode(childConceptCode);
                    RelationsUtil.subsume(aI, aT);
                }
            }
        }
    }

    private void addSlotDetailsToConcept(Object concept, Entity con, Slot slot, String slotType) {
        if (slot == null)
            return;
        String slotName = slot.getName();

        if ((FMA2LGConstants.SLOT_FMA_CONCEPT_CODE.equals(slotName)) || (FMA2LGConstants.SLOT_NAME.equals(slotName))
                || (FMA2LGConstants.SLOT_DEFINITION.equals(slotName)))
            return;

        if (FMA2LGConstants.SLOT_TYPE_PRESENTATION.equals(slotType)) {
            addPresentationAttribute(concept, con, slot);
        } else if (FMA2LGConstants.SLOT_TYPE_PROPERTY.equals(slotType)) {
            addPropertyAttribute(concept, con, slot);
        } else if (FMA2LGConstants.SLOT_TYPE_ASSOCIATION.equals(slotType)) {
            addAssociationAttribute(concept, con, slot);
        }
    }

    private String getConceptNameFromInstance(Instance concept) {
        String conCode = null;

        try {
            if (concept != null) {

                Slot idSlot = kb_.getSlot(FMA2LGConstants.SLOT_FMA_PREFERRED_NAME);
                if (concept.hasOwnSlot(idSlot)) {
                    Instance inst = (Instance) concept.getOwnSlotValue(idSlot);
                    conCode = (String) inst.getOwnSlotValue(kb_.getSlot(FMA2LGConstants.SLOT_NAME));
                } else {
                    try {
                        conCode = (String) concept.getOwnSlotValue(kb_.getSlot(FMA2LGConstants.SLOT_NAME));
                    } catch (Exception e) {
                    }

                    if (FMA2LGUtils.isNull(conCode))
                        conCode = FMA2LGUtils.toNMToken(concept.getName());
                }

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return conCode;
    }

    private String getConceptCodeFromObj(Object conceptObj) {
        if (conceptObj != null) {
            if (conceptObj instanceof Cls)
                return getConceptCodeFromCls((Cls) conceptObj);

            if (conceptObj instanceof Instance)
                return getConceptCodeFromInstance((Instance) conceptObj);
        }

        return null;
    }

    private String getConceptCodeFromCls(Cls concept) {
        String conCode = null;

        try {
            if (concept != null) {
                Slot idSlot = kb_.getSlot(FMA2LGConstants.SLOT_FMA_CONCEPT_CODE);
                if (concept.hasOwnSlot(idSlot)) {
                    conCode = (String) concept.getOwnSlotValue(idSlot);
                } else {
                    idSlot = kb_.getSlot(FMA2LGConstants.SLOT_FMA_PREFERRED_NAME);
                    if (concept.hasOwnSlot(idSlot)) {
                        Instance inst = (Instance) concept.getOwnSlotValue(idSlot);
                        conCode = (String) inst.getOwnSlotValue(kb_.getSlot(FMA2LGConstants.SLOT_NAME));
                    } else {
                        conCode = FMA2LGUtils.toNMToken(concept.getName());
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
                Slot idSlot = kb_.getSlot(FMA2LGConstants.SLOT_FMA_CONCEPT_CODE);
                if (concept.hasOwnSlot(idSlot)) {
                    conCode = (String) concept.getOwnSlotValue(idSlot);
                } else {
                    idSlot = kb_.getSlot(FMA2LGConstants.SLOT_FMA_PREFERRED_NAME);
                    if (concept.hasOwnSlot(idSlot)) {
                        Instance inst = (Instance) concept.getOwnSlotValue(idSlot);
                        conCode = (String) inst.getOwnSlotValue(kb_.getSlot(FMA2LGConstants.SLOT_NAME));
                    } else {
                        try {
                            conCode = (String) concept.getOwnSlotValue(kb_.getSlot(FMA2LGConstants.SLOT_NAME));
                        } catch (Exception e) {
                        }

                        if (FMA2LGUtils.isNull(conCode))
                            conCode = FMA2LGUtils.toNMToken(concept.getName());
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

        return null;
    }

    private String getEntityDescription(Cls concept) {
        String description = null;

        try {
            if (concept != null) {
                Slot idSlot = kb_.getSlot(FMA2LGConstants.SLOT_FMA_PREFERRED_NAME);
                if (concept.hasOwnSlot(idSlot)) {
                    Object io = concept.getOwnSlotValue(idSlot);

                    if ((io != null) && (io instanceof Instance)) {
                        Instance inst = (Instance) io;
                        Slot nameSlot = kb_.getSlot(FMA2LGConstants.SLOT_NAME);

                        if ((inst != null) && (nameSlot != null) && (inst.hasOwnSlot(nameSlot))) {
                            Object o = inst.getOwnSlotValue(nameSlot);

                            if (o != null)
                                description = (String) o;
                        }
                    }
                } else {
                    description = concept.getName();
                }
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
                Slot idSlot = kb_.getSlot(FMA2LGConstants.SLOT_FMA_PREFERRED_NAME);
                if (concept.hasOwnSlot(idSlot)) {
                    Object io = concept.getOwnSlotValue(idSlot);

                    if ((io != null) && (io instanceof Instance)) {
                        Instance inst = (Instance) io;
                        Slot nameSlot = kb_.getSlot(FMA2LGConstants.SLOT_NAME);

                        if ((inst != null) && (nameSlot != null) && (inst.hasOwnSlot(nameSlot))) {
                            Object o = inst.getOwnSlotValue(nameSlot);

                            if (o != null)
                                description = (String) o;
                        }
                    }
                } else {
                    description = concept.getName();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return description;
    }

    private void addPresentationAttribute(Object concept, Entity con, Slot slot) {
        try {
            if ((concept == null) || (slot == null) || (con == null))
                return;

            String slotName = slot.getName();

            String value = getCorrectValueString(concept, slot, null, false);

            if (FMA2LGUtils.isNull(value))
                return;

            boolean foundSome = false;

            // List tps = con.getProperty();
            List tps = Arrays.asList(con.getPresentation());
            if ((tps != null) && (tps.size() > 0)) {
                Iterator itr = tps.iterator();

                while (itr.hasNext()) {
                    Object ob = itr.next();

                    if (ob instanceof Presentation) {
                        if (value.equals(((Presentation) ob).getValue().getContent())) {
                            foundSome = true;
                            if (slotName.equals(FMA2LGConstants.SLOT_FMA_PREFERRED_NAME))
                                ((Presentation) ob).setIsPreferred(new Boolean(true));
                        }
                    }
                }
            }

            if (!foundSome) {
                Presentation tp = new Presentation();
                Text txt = new Text();
                txt.setContent((String) value);
                tp.setValue(txt);

                if (slotName.equals(FMA2LGConstants.SLOT_FMA_PREFERRED_NAME))
                    tp.setIsPreferred(new Boolean(true));

                tp.setPropertyName(FMA2LGConstants.PROPERTY_TEXTPRESENTATION);
                tp.setPropertyId(FMA2LGConstants.PROPERTY_ID_PREFIX + (++propertyCounter));
                con.addPresentation(tp);
            }
        } catch (Exception e) {
            System.out.println("Failed while getting value for textual representation for Slot=" + slot);
        }
    }

    private void modifyPresentationAttribute(Entity con, String conceptName, String slotName, String value) {
        try {
            if ((con == null) || (FMA2LGUtils.isNull(conceptName)) || (FMA2LGUtils.isNull(slotName))
                    || (FMA2LGUtils.isNull(value)))
                return;

            boolean toCreate = true;

            List tps = Arrays.asList(con.getProperty());

            if ((tps != null) && (tps.size() > 0)) {
                Iterator itr = tps.iterator();

                while (itr.hasNext()) {
                    Object ob = itr.next();

                    if (ob instanceof Presentation) {
                        Presentation prs = (Presentation) ob;

                        if ("Latin name (TA)".equalsIgnoreCase(slotName)) {
                            // Go and find a textual representation equal to
                            // value of this slot and set Language as "Latin"
                            // Add one if you don't find one.

                            if ((prs.getValue().getContent().equals(value))
                                    || (prs.getValue().getContent().equals(FMA2LGConstants.UNKNOWN))) {
                                String lng = prs.getLanguage();

                                if ((!FMA2LGUtils.isNull(lng)) && (FMA2LGConstants.LANG_LATIN.equals(lng))) {
                                    Text txt = new Text();
                                    txt.setContent((String) value);
                                    prs.setValue(txt);
                                    toCreate = false;
                                }
                            }
                        } else {
                            if ("Abbreviation".equalsIgnoreCase(slotName)) {
                                if (prs.getValue().getContent().equals(value)) {
                                    String rpf = prs.getRepresentationalForm();

                                    if (FMA2LGUtils.isNull(rpf)) {
                                        prs.setRepresentationalForm(slotName);

                                        if (!representationalForms_.contains(slotName))
                                            representationalForms_.add(slotName);

                                        toCreate = false;
                                    } else {
                                        if (slotName.equals(rpf)) {
                                            toCreate = false;
                                        }
                                    }
                                }
                            } else {
                                if (prs.getValue().getContent().equals(conceptName)) {
                                    if (("authority".equalsIgnoreCase(slotName))
                                            || ("Source".equalsIgnoreCase(slotName))) {
                                        // Go and find a textual representation
                                        // equal with 'conceptName' and set
                                        // source as 'value'
                                        // Add to supported source if not there.
                                        Source s = new Source();
                                        s.setContent(value);
                                        prs.addSource(s);
                                        if (!sources_.contains(value))
                                            sources_.add(value);
                                        toCreate = false;
                                    }

                                    if ("Language".equalsIgnoreCase(slotName)) {
                                        // Go and find a textual representation
                                        // equal with 'conceptName' and set
                                        // Language as 'value'
                                        // If Entry is not there then create one
                                        // with this language.

                                        if (FMA2LGUtils.isNull(prs.getLanguage())) {
                                            prs.setLanguage(value);
                                            toCreate = false;
                                        }
                                    }

                                    if ("TA ID".equalsIgnoreCase(slotName)) {
                                        String lng = prs.getLanguage();

                                        if ((!FMA2LGUtils.isNull(lng)) && (FMA2LGConstants.LANG_LATIN.equals(lng))) {
                                            // Change Propery ID to TA ID if
                                            // language is Latin.
                                            prs.setPropertyId(value);
                                            toCreate = false;
                                        }
                                    }

                                    if ("Eponymn".equalsIgnoreCase(slotName)) {
                                        if ("true".equalsIgnoreCase(value)) {
                                            // Add another entry for
                                            // representational form if not
                                            // already there.
                                            // Add to supported representational
                                            // form if not there.
                                            String rpf2 = prs.getRepresentationalForm();

                                            if (FMA2LGUtils.isNull(rpf2)) {
                                                prs.setRepresentationalForm(slotName);

                                                if (!representationalForms_.contains(slotName))
                                                    representationalForms_.add(slotName);

                                                toCreate = false;
                                            } else {
                                                if (slotName.equals(rpf2)) {
                                                    toCreate = false;
                                                }
                                            }
                                        } else
                                            toCreate = false;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (toCreate) {
                Presentation tp = new Presentation();
                Text txt = new Text();
                txt.setContent((String) conceptName);
                tp.setValue(txt);

                if (("authority".equalsIgnoreCase(slotName)) || ("Source".equalsIgnoreCase(slotName))) {
                    Source s = new Source();
                    s.setContent(value);
                    tp.addSource(s);
                    if (!sources_.contains(value))
                        sources_.add(value);
                }

                if ("Language".equalsIgnoreCase(slotName)) {
                    tp.setLanguage(value);
                }

                if ("TA ID".equalsIgnoreCase(slotName)) {
                    tp.setLanguage(FMA2LGConstants.LANG_LATIN);
                    txt = new Text();
                    txt.setContent((String) FMA2LGConstants.UNKNOWN);
                    tp.setValue(txt);
                    tp.setPropertyId(value);
                } else
                    tp.setPropertyId(FMA2LGUtils.toNMToken(slotName) + (++propertyCounter));

                if ("Latin name (TA)".equalsIgnoreCase(slotName)) {
                    txt = new Text();
                    txt.setContent((String) value);
                    tp.setValue(txt);
                    tp.setLanguage(FMA2LGConstants.LANG_LATIN);
                }

                if ("Abbreviation".equalsIgnoreCase(slotName)) {
                    txt = new Text();
                    txt.setContent((String) value);
                    tp.setValue(txt);
                    tp.setRepresentationalForm(slotName);

                    if (!representationalForms_.contains(slotName))
                        representationalForms_.add(slotName);
                }

                if ("Eponymn".equalsIgnoreCase(slotName)) {
                    tp.setRepresentationalForm(slotName);

                    if (!representationalForms_.contains(slotName))
                        representationalForms_.add(slotName);
                }

                tp.setIsPreferred(new Boolean(false));
                tp.setPropertyName(FMA2LGConstants.PROPERTY_TEXTPRESENTATION);

                con.addProperty(tp);
            }
        } catch (Exception e) {
            System.out.println("Failed while modifying presentation attribute for Cls/Instance=" + conceptName
                    + " for slot=" + slotName + " with value=" + value);
        }
    }

    private void addPropertyAttribute(Object concept, Entity con, Slot slot) {
        try {
            String property = FMA2LGUtils.toNMToken(slot.getName());
            if (!properties_.contains(property))
                properties_.add(property);

            Property pc = new Property();
            pc.setPropertyName(property);
            pc.setPropertyId(FMA2LGConstants.PROPERTY_ID_PREFIX + (++propertyCounter));

            // ValueType vT = slot.getValueType();
            // pc.setDataType(getCorrectDataType(vT));

            String value = getCorrectValueString(concept, slot, null, false);

            if (FMA2LGUtils.isNull(value))
                return;
            Text txt = new Text();
            txt.setContent((String) value);
            pc.setValue(txt);

            con.addProperty(pc);
        } catch (Exception e) {
            System.out.println("Failed while getting value for property for Slot=" + slot.getName());
        }
    }

    private void addAssociationAttribute(Object concept, Entity con, Slot slot) {
        try {
            if ((concept == null) || (slot == null) || (con == null))
                return;

            // String relation = FMA2EMFUtils.toNMToken(slot.getName());

            String relation = slot.getName();
            AssociationPredicate assocClass = null;
            AssociationEntity assocEntityClass = null;
            
            boolean createdNew = false;

            Slot invSlot = null;
            String invSlotName = null;

            try {
                // System.out.println("Processing Relation=" + relation);
                if (associationPredicateHash_.containsKey(relation)) {
                    Object associationHashValue = associationPredicateHash_.get(relation);
                    Object associationEntityHashValue = associationEntityHash_.get(relation);
                    if (!(associationHashValue instanceof AssociationPredicate)) {
                        return;
                    }

                    assocClass = (AssociationPredicate) associationHashValue;
                    assocEntityClass = (AssociationEntity) associationEntityHashValue;
                } else {
                    // Inverse Slot
                    invSlot = kb_.getInverseSlot(slot);
                    if (invSlot != null) {
                        // invSlotName =
                        // FMA2EMFUtils.toNMToken(invSlot.getName());
                        invSlotName = invSlot.getName();
                        if (!FMA2LGUtils.isNull(invSlotName)) {
                            if (relation.compareTo(invSlotName) > 0) {
                                // System.out.println("Inverse Slot=" +
                                // invSlotName + " comes before. so it is a
                                // reverse association. Skipping..");
                                return;
                            }
                        }
                        /*
                         * Remove manufactured relationship names else
                         * invSlotName = relation + "_reverse";
                         */
                    }

                    assocClass = new AssociationPredicate();
                    assocClass.setAssociationName(relation);
                    
                    assocEntityClass = new AssociationEntity();
                    assocEntityClass.setEntityCode(relation);
                    assocEntityClass.setForwardName(relation);
                    assocEntityClass.setReverseName(invSlotName);
                    createdNew = true;
                }
            } catch (Exception e) {
                System.out.println("Error while creating/finding association object for=" + relation);
                e.printStackTrace();
                return;
            }

            int slotCount = 0;

            if (concept instanceof Cls) {
                slotCount = ((Cls) concept).getOwnSlotValueCount(slot);
            } else {
                if (concept instanceof Instance)
                    slotCount = ((Instance) concept).getOwnSlotValueCount(slot);
            }

            if (slotCount > 0) {
                ValueType vT = slot.getValueType();

                AssociationSource aI = new AssociationSource();
                aI.setSourceEntityCode(con.getEntityCode());

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

                        if (!FMA2LGUtils.isNull(stringValue)) {
                            if ((ValueType.CLS.equals(vT)) || (ValueType.INSTANCE.equals(vT))) {
                                AssociationTarget aT = new AssociationTarget();
                                aT.setTargetEntityCode(stringValue);
                                valVector.add(aT);
                            } else {
                                AssociationData aD = new AssociationData();
                                String dataType = getCorrectDataType(vT);
                                // aD.setDataType(dataType);
                                Text txt = new Text();
                                txt.setContent((String) stringValue);
                                txt.setDataType(dataType);
                                aD.setAssociationDataText(txt);
                                valVector.add(aD);
                            }
                        }
                    }
                }

                if (valVector.size() > 0) {
                    if ((ValueType.CLS.equals(vT)) || (ValueType.INSTANCE.equals(vT))) {
                        for (int i = 0; i < valVector.size(); i++)
                            aI.addTarget((AssociationTarget) valVector.elementAt(i));
                    } else {
                        for (int i = 0; i < valVector.size(); i++)
                            aI.addTargetData((AssociationData) valVector.elementAt(i));
                    }

                    assocClass.addSource(aI);

                    // System.out.println("STORING...");

                    // time to store the entries
                    if (createdNew) {
                        allRelations_.addAssociationPredicate(assocClass);

                        // It is time to store all objects
                        if (!associations_.contains(relation))
                            associations_.add(relation);
                        

                        // Setting Association Aliases
                        if (slot.hasOwnSlot(kb_.getSlot("slot synonym"))) {
                            Collection aliases = slot.getOwnSlotValues(kb_.getSlot("slot synonym"));

                            if (aliases != null) {
                                Iterator itr = aliases.iterator();

                                while (itr.hasNext()) {
                                    Object o = itr.next();

                                    if ((o != null) && (o instanceof String)) {
                                        String alias = (String) o;
                                        associationsAliases_.add(relation + FMA2LGConstants.DELIM + alias);
                                    }
                                }
                            }
                        }

                        // Synonyms of inverse slot
                        if ((invSlot != null) && (!FMA2LGUtils.isNull(invSlotName))) {
                            if (invSlot.hasOwnSlot(kb_.getSlot("slot synonym"))) {
                                Collection aliases2 = invSlot.getOwnSlotValues(kb_.getSlot("slot synonym"));

                                if (aliases2 != null) {
                                    Iterator itr = aliases2.iterator();

                                    while (itr.hasNext()) {
                                        Object o = itr.next();

                                        if ((o != null) && (o instanceof String)) {
                                            String alias = (String) o;
                                            associationsAliases_.add(invSlotName + FMA2LGConstants.DELIM + alias);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // just overwrite or restore the value of association class
                    // if it was a new object then it will create a new entry
                    // otherwise it will update it with new entries of source
                    // concepts to
                    // the existing one.

                    associationPredicateHash_.put(relation, assocClass);
                    associationEntityHash_.put(relation, assocEntityClass);

                    if (FMA2LGUtils.isNull(invSlotName)) {
                        invSlotName = relation + "_reverse";
                    }

                    associationPredicateHash_.put(invSlotName, "REVERSE");

                    if (FMA2LGUtils.isNull(assocEntityClass.getReverseName())) {
                        assocEntityClass.setReverseName(invSlotName);
                    }
                }
                // else
                // System.out.println("COULD NOT STORE AS NO VALUES FOUND!!");
            }
        } catch (Exception e) {
            System.out.println("Failed while adding association for Slot=" + slot.getName());
            e.printStackTrace();
        }
    }

    private String getCorrectDataType(ValueType vT) {
        if (ValueType.ANY.equals(vT))
            return FMA2LGConstants.PROT_ANY;

        if (ValueType.BOOLEAN.equals(vT))
            return FMA2LGConstants.PROT_BOOLEAN;

        if (ValueType.CLS.equals(vT))
            return FMA2LGConstants.PROT_CLS;

        if (ValueType.FLOAT.equals(vT))
            return FMA2LGConstants.PROT_FLOAT;

        if (ValueType.INSTANCE.equals(vT))
            return FMA2LGConstants.PROT_INSTANCE;

        if (ValueType.INTEGER.equals(vT))
            return FMA2LGConstants.PROT_INTEGER;

        if (ValueType.STRING.equals(vT))
            return FMA2LGConstants.PROT_STRING;

        if (ValueType.SYMBOL.equals(vT))
            return FMA2LGConstants.PROT_SYMBOL;

        return FMA2LGConstants.PROT_STRING;
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
                    result = ((Cls) valObj).getName();
            }
        }

        if (ValueType.FLOAT.equals(vT)) {
            if (valObj != null)
                result = ((Float) valObj).toString();
        }

        if (ValueType.INSTANCE.equals(vT)) {
            if ((valObj != null) && (valObj instanceof Instance)) {
                if (returnCode)
                    result = getConceptCodeFromObj((Instance) valObj);
                else
                    result = getConceptNameFromInstance((Instance) valObj);
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

        return null;
    }

    private Comment[] getComments(Cls concept) {
        try {
            Slot cSlt = kb_.getSlot(FMA2LGConstants.SLOT_COMMENT);
            String comment = (String) concept.getOwnSlotValue(cSlt);

            if (comment != null) {
                Comment cmt = new Comment();
                Text txt = new Text();
                txt.setContent((String) FMA2LGUtils.removeInvalidXMLCharacters(comment, concept.getName()));
                cmt.setValue(txt);
                cmt.setPropertyName(FMA2LGConstants.PROPERTY_COMMENT);
                cmt.setPropertyId(FMA2LGConstants.PROPERTY_ID_PREFIX + (++propertyCounter));

                return new Comment[] { cmt };
            }
        } catch (Exception ex) {
            System.out.println("Failed while getting value for comment for concept=" + concept.getName());
        }

        return null;
    }

    private Comment[] getComments(Instance concept) {
        try {
            Slot cSlt = kb_.getSlot(FMA2LGConstants.SLOT_COMMENT);
            String comment = (String) concept.getOwnSlotValue(cSlt);

            if (comment != null) {
                Comment cmt = new Comment();
                Text txt = new Text();
                txt.setContent((String) FMA2LGUtils.removeInvalidXMLCharacters(comment, concept.getName()));
                cmt.setValue(txt);
                cmt.setPropertyName(FMA2LGConstants.PROPERTY_COMMENT);
                cmt.setPropertyId(FMA2LGConstants.PROPERTY_ID_PREFIX + (++propertyCounter));

                return new Comment[] { cmt };
            }
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

        return null;
    }

    private Definition[] getDefinitions(Cls concept) {
        try {
            String definition = (String) concept.getOwnSlotValue(kb_.getSlot(FMA2LGConstants.SLOT_DEFINITION));

            if (definition != null) {
                Definition def = new Definition();
                Text txt = new Text();
                txt.setContent((String) FMA2LGUtils.removeInvalidXMLCharacters(definition, concept.getName()));
                def.setValue(txt);
                def.setPropertyName(FMA2LGConstants.PROPERTY_DEFINITION);
                def.setPropertyId(FMA2LGConstants.PROPERTY_ID_PREFIX + (++propertyCounter));

                return new Definition[] { def };
            }
        } catch (Exception e) {
            System.out.println("Failed while getting value for definition for concept=" + concept.getName());
        }

        return null;
    }

    private Definition[] getDefinitions(Instance concept) {
        try {
            String definition = (String) concept.getOwnSlotValue(kb_.getSlot(FMA2LGConstants.SLOT_DEFINITION));

            if (definition != null) {
                Definition def = new Definition();
                Text txt = new Text();
                txt.setContent((String) FMA2LGUtils.removeInvalidXMLCharacters(definition, concept.getName()));
                def.setValue(txt);
                def.setPropertyName(FMA2LGConstants.PROPERTY_DEFINITION);
                def.setPropertyId(FMA2LGConstants.PROPERTY_ID_PREFIX + (++propertyCounter));

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

                    if (!FMA2LGUtils.isNull(prp)) {
                        SupportedProperty suppProp = new SupportedProperty();

                        if ((!FMA2LGConstants.PROPERTY_COMMENT.equals(prp))
                                && (!FMA2LGConstants.PROPERTY_DEFINITION.equals(prp))
                                && (!FMA2LGConstants.PROPERTY_INSTRUCTION.equals(prp))
                                && (!FMA2LGConstants.PROPERTY_TEXTPRESENTATION.equals(prp))) {
                            suppProp.setUri(FMA2LGUtils.getWithFMAURN(prp));
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
                        if (!FMA2LGUtils.isNull(sA)) {
                            SupportedAssociation suppAss = new SupportedAssociation();
                            suppAss.setUri(FMA2LGUtils.getWithFMAURN(sA));
                            suppAss.setLocalId(FMA2LGUtils.toNMToken(sA));
                            csclass.getMappings().addSupportedAssociation(suppAss);
                        }
                    }
                }

                if (assocAliasSize > 0) {
                    for (int j = assocSize; j < total; j++) {
                        String sA2 = (String) associationsAliases_.elementAt(j - assocSize);
                        String[] ali = sA2.split(FMA2LGConstants.DELIM);

                        if ((ali != null) && (ali.length > 1)) {
                            if (!FMA2LGUtils.isNull(ali[1])) {
                                SupportedAssociation suppAss = new SupportedAssociation();
                                suppAss.setUri(FMA2LGUtils.getWithFMAURN(ali[0]));
                                suppAss.setLocalId(FMA2LGUtils.toNMToken(ali[1]));
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

                        if (!FMA2LGUtils.isNull(src)) {
                            SupportedSource suppSrc = new SupportedSource();
                            suppSrc.setUri(FMA2LGUtils.getWithFMAURN(src));
                            suppSrc.setLocalId(FMA2LGUtils.toNMToken(src));
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

                        if (!FMA2LGUtils.isNull(rep)) {
                            SupportedRepresentationalForm suppRpf = new SupportedRepresentationalForm();
                            suppRpf.setUri(FMA2LGUtils.getWithFMAURN(rep));
                            suppRpf.setLocalId(FMA2LGUtils.toNMToken(rep));
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

    /*
     * private void loadMappings() { URI mappingsURI =
     * URI.create("file:///C:/Work/Protege-Projects/mappings.txt"); File
     * mappingFile = new File(mappingsURI);
     * 
     * if ((mappingFile != null) && (mappingFile.exists())) { try { mappings_ =
     * new Properties();
     * 
     * BufferedReader in = new BufferedReader(new FileReader(mappingFile));
     * String str; while ((str = in.readLine()) != null) { if ((str != null) &&
     * (!"".equals(str.trim()))) { mappings_.setProperty(str.split("=")[0],
     * str.split("=")[1]); } } in.close();
     * 
     * mappings_.list(System.out);
     * 
     * } catch (FileNotFoundException e) {
     * System.out.println("Mapping file not found! No properties will be loaded!"
     * ); e.printStackTrace(); } catch (Exception e) {
     * System.out.println("Failed while reading Mapping file!");
     * e.printStackTrace(); } } }
     */
}