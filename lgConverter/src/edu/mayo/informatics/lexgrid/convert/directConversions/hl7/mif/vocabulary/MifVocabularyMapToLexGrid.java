package edu.mayo.informatics.lexgrid.convert.directConversions.hl7.mif.vocabulary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.commonTypes.types.EntityTypes;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.custom.concepts.EntityFactory;
import org.LexGrid.custom.relations.RelationsUtil;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedContainerName;
import org.LexGrid.naming.SupportedHierarchy;
import org.LexGrid.naming.SupportedLanguage;
import org.LexGrid.naming.SupportedProperty;
import org.LexGrid.naming.SupportedPropertyQualifier;
import org.LexGrid.naming.SupportedSource;
import org.LexGrid.relations.AssociationEntity;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.LexGrid.util.sql.DBUtility;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.commons.lang.StringUtils;

import edu.mayo.informatics.lexgrid.convert.directConversions.hl7.HL72LGConstants;

public class MifVocabularyMapToLexGrid {

    private LgMessageDirectorIF messages_;
    private Hashtable<Integer, Entity> internalIdToEntityHash;
    private LoaderPreferences loaderPrefs;
    
    private MifVocabularyModel mifVocabularyModel;
    
    
    public MifVocabularyMapToLexGrid() {
        super();
    }

    public MifVocabularyMapToLexGrid(LgMessageDirectorIF messages_, MifVocabularyModel mifVocabularyModel) {
        super();
        this.messages_ = messages_;
        this.mifVocabularyModel = mifVocabularyModel;
    }
    
    
    
    private void loadCodingScheme(CodingScheme csclass) {

        try {
            messages_.info("Loading coding scheme information for HL7 MIF Vocabulary");
            String name = mifVocabularyModel.getName();
            csclass.setCodingSchemeName(name);
            csclass.setCodingSchemeURI(mifVocabularyModel.getXmlns());
            csclass.setFormalName(mifVocabularyModel.getTitle());
            EntityDescription enDesc = new EntityDescription();
            enDesc.setContent(mifVocabularyModel.getTitle());
            csclass.setEntityDescription(enDesc);
            csclass.setDefaultLanguage(MifVocabulary2LGConstants.DEFAULT_LANGUAGE_EN); // "en"
            csclass.setRepresentsVersion(mifVocabularyModel.getCombinedId());
            Text txt = new Text();
            txt.setContent(MifVocabulary2LGConstants.DEFAULT_COPYRIGHT);   

            csclass.setCopyright(txt);
            csclass.setMappings(new Mappings());

            // Add SupportedCodingScheme and SupportedLanguage Mappings
            SupportedCodingScheme scs = new SupportedCodingScheme();
            scs.setLocalId(csclass.getCodingSchemeName());
            scs.setUri(csclass.getCodingSchemeURI());
            csclass.getMappings().addSupportedCodingScheme(scs);

            SupportedLanguage lang = new SupportedLanguage();
            lang.setLocalId(MifVocabulary2LGConstants.DEFAULT_LANGUAGE_EN);  // "en"
            csclass.getMappings().addSupportedLanguage(lang);
           
            // Add SupportedHierarchy Mappings
            SupportedHierarchy hierarchy = new SupportedHierarchy();
            hierarchy.setLocalId(MifVocabulary2LGConstants.ASSOCIATION_HAS_SUBTYPE);
            ArrayList<String> list = new ArrayList<String>();
            list.add(MifVocabulary2LGConstants.ASSOCIATION_HAS_SUBTYPE);
            hierarchy.setAssociationNames(list);
            hierarchy.setRootCode(MifVocabulary2LGConstants.DEFAULT_ROOT_NODE);
            hierarchy.setIsForwardNavigable(true);
            csclass.getMappings().addSupportedHierarchy(hierarchy);
            
            hierarchy = new SupportedHierarchy();
            hierarchy.setLocalId(MifVocabulary2LGConstants.ASSOCIATION_IS_A);
            list = new ArrayList<String>();
            list.add(MifVocabulary2LGConstants.ASSOCIATION_HAS_SUBTYPE);
            hierarchy.setAssociationNames(list);
            hierarchy.setRootCode(MifVocabulary2LGConstants.DEFAULT_ROOT_NODE);
            hierarchy.setIsForwardNavigable(true);
            csclass.getMappings().addSupportedHierarchy(hierarchy);

        } catch (Exception e) {
            messages_.error("Failed while preparing LexGrid CodingScheme class and its SupportedCodingScheme, " 
                    + "SupportedLanguage and SupportedHierarchy mappings from parsed HL7 MIF Vocabulary data.", e);
            e.printStackTrace();
        } 
    }

    
    private void loadAssociationEntityAndSupportedMaps(CodingScheme csclass) {
        Relations relations = null;
        Entities concepts = csclass.getEntities();
        if (concepts == null) {
            concepts = new Entities();
            csclass.setEntities(concepts);
        }

        try {
            messages_.info("Loading AssociationEntityAndSupportedMaps for HL7 MIF Vocabulary");
            
            // Pre-Load the supported associations 
            relations = new Relations();
            relations.setContainerName("relations");
            csclass.addRelations(relations);
            SupportedContainerName scn= new SupportedContainerName();
            scn.setLocalId("relations");
            csclass.getMappings().addSupportedContainerName(scn);
            
            Set<String> scrKeySet = mifVocabularyModel.getSupportedConceptRelationshipsMap().keySet();
            for (String scRelationshipName : scrKeySet) {
                MifSupportedConceptRelationship scRelationship = mifVocabularyModel.getSupportedConceptRelationshipsMap().get(scRelationshipName);
                String association_name = scRelationshipName;
                
                SupportedAssociation sa = new SupportedAssociation();
                sa.setLocalId(association_name);
                sa.setEntityCode(association_name);
                sa.setEntityCodeNamespace(csclass.getCodingSchemeName());                
                csclass.getMappings().addSupportedAssociation(sa);

                AssociationPredicate parent_assoc = new AssociationPredicate();
                parent_assoc.setAssociationName(association_name);

                AssociationEntity parent_assocEntity = EntityFactory.createAssociation();
                parent_assocEntity.setEntityCode(association_name);
                parent_assocEntity.setForwardName(association_name);
                parent_assocEntity.setEntityCodeNamespace(csclass.getCodingSchemeName());

                concepts.addEntity(parent_assocEntity);

                RelationsUtil.subsume(relations, parent_assoc); 
                                    
                if (scRelationship.getTransitivity() != null && scRelationship.getTransitivity().equalsIgnoreCase("transitive")) {
                    parent_assocEntity.setIsTransitive(true);                    
                }
            }


            // Pre-load all the supported properties (supported concept properties)
            Set<String> scPropKeySet = mifVocabularyModel.getSupportedConceptPropertiesMap().keySet();
            for (String scPropName : scPropKeySet) {
                SupportedProperty sp = new SupportedProperty();
                sp.setLocalId(scPropName);
                if (!Arrays.asList(csclass.getMappings().getSupportedProperty()).contains(sp))
                    csclass.getMappings().addSupportedProperty(sp);
            }

            // Pre-load the supported property qualifier source-code
            String propertyQualifier = "source-code";
            SupportedPropertyQualifier spq = new SupportedPropertyQualifier();
            spq.setLocalId(propertyQualifier);
            if (!Arrays.asList(csclass.getMappings().getSupportedPropertyQualifier()).contains(spq))
                csclass.getMappings().addSupportedPropertyQualifier(spq);

            // Pre-load all the supported sources
            List<MifCodeSystem> codeSystemList = mifVocabularyModel.getCodeSystems();
            for (MifCodeSystem codeSystem : codeSystemList) {
                SupportedSource ss = new SupportedSource();
                ss.setLocalId(codeSystem.getName());
                if (!Arrays.asList(csclass.getMappings().getSupportedSource()).contains(ss))
                    csclass.getMappings().addSupportedSource(ss);
            }
        } catch (Exception e) {
            messages_.error("Failed while preparing AssociationEntityAndSupportedMaps for HL7 MIF Vocabulary.", e);
            e.printStackTrace();
        } 
    }

    private void loadConcepts(CodingScheme csclass) {

        Connection c = null;
        ResultSet results = null;

        Entities concepts = csclass.getEntities();
        if (concepts == null) {
            concepts = new Entities();
            csclass.setEntities(concepts);
        }

        try {
            messages_.info("Loading concepts for HL7 MIF Vocabulary");

            // Create the artificial top nodes, the @ node. Persist the top nodes to a list
            // Create a method to handle them as concepts.
            loadArtificialTopNodes(csclass, concepts);

            // Pre-load some concept data into hash tables but eliminate the
            // code system scheme.
//            PreparedStatement getConcept_ps = c.prepareStatement("SELECT internalId, conceptCode2, "
//                    + SQLTableConstants.TBLCOL_CONCEPTSTATUS
//                    + " FROM VCS_concept_code_xref WHERE codeSystemId2 <> ? and codeInstance <> ?");

//            getConcept_ps.setString(1, HL72LGConstants.CODE_SYSTEM_OID);
            // We want only the default case which happens to be uppercase.
            // Ignore the lower case code
//            getConcept_ps.setInt(2, 1);
//            results = getConcept_ps.executeQuery();
//            while (results.next()) {
            List<MifCodeSystem> codeSystemList = mifVocabularyModel.getCodeSystems();
            for (MifCodeSystem codeSystem : codeSystemList) {
                List<MifConcept> conceptList = codeSystem.getCodeSystemVersions().get(0).getConcepts();
                // It is assumed every concept has a single "internalId" conceptProperty
                MifConceptProperty mifInternalIdProperty = null;
                for (MifConcept mifConcept : conceptList) {
//                    int internalId = results.getInt("internalId");
                    List<MifConceptProperty> mifConceptProperties = mifConcept.getConceptProperties();
                    for (MifConceptProperty mifCP : mifConceptProperties) {
                        if (mifCP.getName().equals("internalId")) {
                            mifInternalIdProperty = mifCP;
                            break;
                        }
                    }
                    String internalIdStr = mifInternalIdProperty.getValue();
                    int internalId = Integer.parseInt(internalIdStr);
                    
                    // If the concept contains more than one code, get the first "active" code.  If multiple codes and none
                    // are "active", use the first code in the list.
                    //    Or, create a concept entity for each code since original design created a string comprised of the 
                    //    internalId and code's value.
                    
                    List<MifConceptCode> mifConceptCodes = mifConcept.getConceptCodes();
                    for (MifConceptCode mifConceptCode : mifConceptCodes) {
                        
//                        String conceptCode = Integer.toString(internalId) + ":" + results.getString("conceptCode2");
                        String conceptCode = mifConceptCode.getCode() + ":" + internalIdStr;
                        String status = mifConceptCode.getStatus();
//                        String status = results.getString(SQLTableConstants.TBLCOL_CONCEPTSTATUS);
                        Entity concept = new Entity();
                        concept.setEntityCode(conceptCode);
                        concept.setEntityType(new String[] { EntityTypes.CONCEPT.toString() });
                        concept.setEntityCodeNamespace(csclass.getCodingSchemeName());
                        concept.setStatus(status);                        
                        if (status.equalsIgnoreCase("active")) {
                            concept.setIsActive(Boolean.TRUE);
                        } else {
                            concept.setIsActive(Boolean.FALSE);
                        }
                        
                        // TODO might not be able to use multiple codes??? Cannot use concept's internalId as key
                        //  in hashtable if concept has multiple codes - will overlay previous put entry.
                        internalIdToEntityHash.put(Integer.valueOf(internalId), concept);
                        concepts.addEntity(concept);
                        
                    }
                    
                }
            }

        } catch (Exception e) {
            messages_.error("Failed while preparing concepts for HL7 MIF Vocabulary.", e);
            e.printStackTrace();
        } 

        // process the concept data
        messages_.info("Processing " + internalIdToEntityHash.size() + " concepts...");
        csclass.setApproxNumConcepts(new Long(concepts.getEntity().length));

    }

    
    void loadArtificialTopNodes(CodingScheme csclass, Entities concepts) {
        
        Connection c = null;  // TODO remove when done

        messages_.info("Processing code systems into top nodes");
        ResultSet topNode_results = null;
        try {
            // Create an "@" top node.
            Entity rootNode = new Entity();

            // Create and set the concept code for "@"
            String topNodeDesignation = MifVocabulary2LGConstants.DEFAULT_ROOT_NODE;
            rootNode.setEntityCode(topNodeDesignation);
            rootNode.setEntityCodeNamespace(csclass.getCodingSchemeName());
            rootNode.setIsAnonymous(Boolean.TRUE);
            EntityDescription enDesc = new EntityDescription();
            enDesc.setContent("Root node for subclass relations.");
            rootNode.setEntityDescription(enDesc);
            concepts.addEntity(rootNode);

            AssociationSource ai = new AssociationSource();
            ai.setSourceEntityCode(rootNode.getEntityCode());
            ai.setSourceEntityCodeNamespace(csclass.getCodingSchemeName());
            AssociationPredicate parent_assoc = (AssociationPredicate) RelationsUtil
                    .resolveAssociationPredicates(csclass, MifVocabulary2LGConstants.ASSOCIATION_HAS_SUBTYPE).get(0);
            ai = RelationsUtil.subsume(parent_assoc, ai);

            // Get all the code systems except the
            // legacy code system coding scheme in HL7
//            PreparedStatement getArtificialTopNodeData = c
//                    .prepareStatement("select cs.*, code.internalId FROM VCS_code_system AS cs "+
//                                      "LEFT JOIN  VCS_concept_code_xref AS code "+
//                                      "ON cs.codeSystemName= code.conceptCode2 " +
//                                      "where (code.codeSystemId2 = ? OR code.codeSystemId2 is null) AND cs.codesystemid <> ?");
//            getArtificialTopNodeData.setString(1, HL72LGConstants.CODE_SYSTEM_OID);
//            getArtificialTopNodeData.setString(2, HL72LGConstants.CODE_SYSTEM_OID);
//            ResultSet dataResults = getArtificialTopNodeData.executeQuery();
            
            List<MifCodeSystem> codeSystemList = mifVocabularyModel.getCodeSystems();
            
            for (MifCodeSystem codeSystem : codeSystemList) {
//            while (dataResults.next()) {
                
                Entity topNode = new Entity();
//                String nodeName = dataResults.getString("codeSystemName");
                String nodeName = codeSystem.getName();
//                String entityDescription = dataResults.getString("fullName");
                String entityDescription = codeSystem.getTitle();
//                String oid = dataResults.getString("codeSystemId");
                String oid = codeSystem.getCodeSystemId();
                String def = "";  // Not being parsed from XML source file
//                String def = dataResults.getString("description");
//                String internalId= dataResults.getString("internalId");
//                if (StringUtils.isNotBlank(def)) {
//                    int begin = def.lastIndexOf("<p>");
//                    int end = def.lastIndexOf("</p>");
//                    if (begin > -1) {
//                        if (begin + 3 < end)
//                            def = def.substring(begin + 3, end);
//                    }
//                }
               
//                if (StringUtils.isNotBlank(internalId)) {
//                    topNode.setEntityCode(internalId + ":" + nodeName);                   
//                } else {
//                    topNode.setEntityCode(nodeName + ":" + nodeName);
//                }
                topNode.setEntityCode(nodeName + ":" + oid);                   
                topNode.setEntityCodeNamespace(csclass.getCodingSchemeName());

                EntityDescription enD = new EntityDescription();
                enD.setContent(entityDescription);
                topNode.setEntityDescription(enD);
                topNode.setIsActive(true);

                // Set presentation so it's a full fledged concept
                Presentation p = new Presentation();
                Text txt = new Text();
                txt.setContent((String) entityDescription);
                p.setValue(txt);
                p.setIsPreferred(Boolean.TRUE);
                p.setPropertyName(MifVocabulary2LGConstants.PROPERTY_PRINTNAME);
                p.setPropertyId("T1");
                p.setLanguage(MifVocabulary2LGConstants.DEFAULT_LANGUAGE_EN);
                topNode.addPresentation(p);

                // Set definition
                if (StringUtils.isNotBlank(def)) {
                    Definition definition = new Definition();
                    Text defText = new Text();
                    defText.setContent(def);
                    definition.setValue(defText);
                    definition.setPropertyName(MifVocabulary2LGConstants.PROPERTY_DEFINITION);
                    definition.setPropertyId("D1");
                    definition.setIsActive(Boolean.TRUE);
                    definition.setIsPreferred(Boolean.TRUE);
                    definition.setLanguage(MifVocabulary2LGConstants.DEFAULT_LANGUAGE_EN);
                    topNode.addDefinition(definition);
                }
                
                topNode.addEntityType(EntityTypes.CONCEPT.toString());
                concepts.addEntity(topNode);

                // This coding scheme is attached to an artificial root.
                AssociationTarget at = new AssociationTarget();
                at.setTargetEntityCode(topNode.getEntityCode());
                at.setTargetEntityCodeNamespace(csclass.getCodingSchemeName());
                RelationsUtil.subsume(ai, at);

                // Now find the top nodes of the scheme and subsume them to this
                // scheme's artificial top node.
                // First get a list of all nodes in the scheme.
                // but again exclude the code system nodes.
                ArrayList<String> topNodes = new ArrayList<String>();
//                PreparedStatement getSystemCodes = c
//                        .prepareStatement("SELECT DISTINCT (internalId), conceptCode2 FROM VCS_concept_code_xref WHERE codeSystemId2 =? AND codeSystemId2 <> ?");
//                getSystemCodes.setString(1, oid);
//                getSystemCodes.setString(2, HL72LGConstants.CODE_SYSTEM_OID);
//                ResultSet systemCodes = getSystemCodes.executeQuery();
//                while (systemCodes.next()) {
//                    String testCode = systemCodes.getString("internalId");
//                    if (testCode != null)
//                        topNodes.add(testCode);
//                }
//                if (systemCodes != null)
//                    systemCodes.close();
//                if (getSystemCodes != null)
//                    getSystemCodes.close();

                // Drop any from the list that aren't top nodes.
                ArrayList<String> nodesToRemove = new ArrayList<String>();
                PreparedStatement checkForTopNode = null;
                for (int i = 0; i < topNodes.size(); i++) {
                    checkForTopNode = c
                            .prepareStatement("SELECT targetInternalId FROM VCS_concept_relationship WHERE targetInternalId =?");
                    checkForTopNode.setString(1, (String) topNodes.get(i));
                    topNode_results = checkForTopNode.executeQuery();
                    if (topNode_results.next()) {
                        if (topNodes.get(i).equals(topNode_results.getString(1))) {
                            nodesToRemove.add(topNodes.get(i));
                        }
                    }
                    if (topNode_results != null)
                        topNode_results.close();
                    if (checkForTopNode != null)
                        checkForTopNode.close();
                }

                for (int i = 0; i < nodesToRemove.size(); i++) {
                    topNodes.remove(nodesToRemove.get(i));
                }
                // Get the full concept code for each.
                PreparedStatement getconceptSuffix = null;
                ResultSet conceptCode = null;
                for (int i = 0; i < topNodes.size(); i++) {
                    getconceptSuffix = c
                            .prepareStatement("SELECT conceptCode2 FROM VCS_concept_code_xref where internalId = ?");

                    getconceptSuffix.setString(1, (String) topNodes.get(i));
                    conceptCode = getconceptSuffix.executeQuery();
                    conceptCode.next();
                    topNodes.set(i, topNodes.get(i) + ":" + conceptCode.getString(1));
                    if (conceptCode != null)
                        conceptCode.close();
                    if (getconceptSuffix != null)
                        getconceptSuffix.close();
                }

                // For each top node subsume to the current artificial node for
                // the scheme.
                for (int j = 0; j < topNodes.size(); j++) {
                    try {
                        AssociationSource atn = new AssociationSource();
                        atn.setSourceEntityCode(topNode.getEntityCode());
                        atn.setSourceEntityCodeNamespace(csclass.getCodingSchemeName());
                        atn = RelationsUtil.subsume(parent_assoc, atn);

                        AssociationTarget atopNode = new AssociationTarget();
                        atopNode.setTargetEntityCode((String) topNodes.get(j));
                        atopNode.setTargetEntityCodeNamespace(csclass.getCodingSchemeName());
                        RelationsUtil.subsume(atn, atopNode);
                    } catch (Exception e) {
                        messages_.error("Failed while processing HL7 psuedo top node hierarchy", e);
                        e.printStackTrace();
                    }
                }
            }
//            dataResults.close();
            messages_.info("Top node processing complete");
        } catch (Exception e) {
            messages_.error("Top node processing failed", e);
            e.printStackTrace();
        }

    }

    
    
}
