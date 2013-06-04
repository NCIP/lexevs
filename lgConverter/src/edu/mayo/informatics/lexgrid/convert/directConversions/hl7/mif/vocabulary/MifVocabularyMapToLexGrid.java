package edu.mayo.informatics.lexgrid.convert.directConversions.hl7.mif.vocabulary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
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
import org.apache.commons.lang.StringUtils;

public class MifVocabularyMapToLexGrid {

    private LgMessageDirectorIF messages_;
//    private Hashtable<Integer, Entity> internalIdToEntityHash;
    private Hashtable<String, Entity> codeAndInternalIdToEntityHash;
//    private LoaderPreferences loaderPrefs;
    
    private MifVocabularyModel mifVocabularyModel;
    
    
    public MifVocabularyMapToLexGrid() {
        super();
    }

    public MifVocabularyMapToLexGrid(LgMessageDirectorIF messages_, MifVocabularyModel mifVocabularyModel) {
        super();
        this.messages_ = messages_;
        this.mifVocabularyModel = mifVocabularyModel;
        codeAndInternalIdToEntityHash = new Hashtable<String, Entity>();
    }
    
    public void initRun(CodingScheme csclass) {

        try {

            loadCodingScheme(csclass);
            loadAssociationEntityAndSupportedMaps(csclass);
            // loadConcepts method includes the Concept's presentation and properties
            loadConcepts(csclass);
            loadConceptRelations(csclass, codeAndInternalIdToEntityHash);

        } catch (Exception e) {
            messages_.error("Failed to load HL7 MIF Vocabulary do to problems mapping parsed XML data into LexGrid objects");
            e.printStackTrace();
        } 
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
            
            // Pre-Load the supported associations to be used for the artificial top nodes
            //    Note:  these associations have already been set for the CodingScheme.Mapping.SupportedAssociation list as a part
            //       of the loadCodingScheme method
            AssociationPredicate parent_assoc = new AssociationPredicate();
            parent_assoc.setAssociationName(MifVocabulary2LGConstants.ASSOCIATION_HAS_SUBTYPE);
            AssociationEntity parent_assocEntity = EntityFactory.createAssociation();
            parent_assocEntity.setEntityCode(MifVocabulary2LGConstants.ASSOCIATION_HAS_SUBTYPE);
            parent_assocEntity.setForwardName(MifVocabulary2LGConstants.ASSOCIATION_HAS_SUBTYPE);
            parent_assocEntity.setEntityCodeNamespace(csclass.getCodingSchemeName());
            parent_assocEntity.setIsTransitive(true);                    

            concepts.addEntity(parent_assocEntity);

            RelationsUtil.subsume(relations, parent_assoc); 
            
            parent_assoc = new AssociationPredicate();
            parent_assoc.setAssociationName(MifVocabulary2LGConstants.ASSOCIATION_IS_A);
            parent_assocEntity = EntityFactory.createAssociation();
            parent_assocEntity.setEntityCode(MifVocabulary2LGConstants.ASSOCIATION_IS_A);
            parent_assocEntity.setForwardName(MifVocabulary2LGConstants.ASSOCIATION_IS_A);
            parent_assocEntity.setEntityCodeNamespace(csclass.getCodingSchemeName());
            parent_assocEntity.setIsTransitive(true);                    

            concepts.addEntity(parent_assocEntity);

            RelationsUtil.subsume(relations, parent_assoc); 
            
            // Get the list of all distinct <supportedConceptRelationships> elements found during the parsing phase and add them
            // to the codingScheme's relations
            Set<String> scrKeySet = mifVocabularyModel.getSupportedConceptRelationshipsMap().keySet();
            for (String scRelationshipName : scrKeySet) {
                MifSupportedConceptRelationship scRelationship = mifVocabularyModel.getSupportedConceptRelationshipsMap().get(scRelationshipName);
                String association_name = scRelationshipName;
                
                SupportedAssociation sa = new SupportedAssociation();
                sa.setLocalId(association_name);
                sa.setEntityCode(association_name);
                sa.setEntityCodeNamespace(csclass.getCodingSchemeName());                
                csclass.getMappings().addSupportedAssociation(sa);

                parent_assoc = new AssociationPredicate();
                parent_assoc.setAssociationName(association_name);

                parent_assocEntity = EntityFactory.createAssociation();
                parent_assocEntity.setEntityCode(association_name);
                parent_assocEntity.setForwardName(association_name);
                parent_assocEntity.setEntityCodeNamespace(csclass.getCodingSchemeName());
                if (scRelationship.getTransitivity() != null && scRelationship.getTransitivity().equalsIgnoreCase("transitive")) {
                    parent_assocEntity.setIsTransitive(true);                    
                }

                concepts.addEntity(parent_assocEntity);

                RelationsUtil.subsume(relations, parent_assoc); 
                                    
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
            for (MifCodeSystem mifCodeSystem : codeSystemList) {
                List<MifConcept> conceptList = mifCodeSystem.getCodeSystemVersions().get(0).getConcepts();
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
//                    int internalId = Integer.parseInt(internalIdStr);
                    
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
                        
                        // *** Set concept's presentation ***
                        String sourceCodeSystemName = mifCodeSystem.getName();
                        MifPrintName mifPrintName = mifConcept.getPrintName();
//                        String entityDescription = designationResults.getString("designation");
                        String entityDescription = mifPrintName.getText(); 
                        Presentation p = new Presentation();
                        Text txt = new Text();
                        txt.setContent((String) entityDescription);
                        p.setValue(txt);
                        // Note only the last <printName> having the preferredForLanguage = "true" - count will be 1
                        p.setIsPreferred(Boolean.TRUE);
                        EntityDescription ed = new EntityDescription();
                        ed.setContent(entityDescription);
                        concept.setEntityDescription(ed);
                        p.setPropertyName(MifVocabulary2LGConstants.PROPERTY_PRINTNAME);
                        p.setPropertyId("T" + "1");
//                        p.setPropertyId("T" + concept.getPresentationCount());
                        String propertyLanguage = mifPrintName.getLanguage();
//                        String propertyLanguage = designationResults.getString("language");
                        if (StringUtils.isBlank(propertyLanguage)) {
                            p.setLanguage(MifVocabulary2LGConstants.DEFAULT_LANGUAGE_EN);
                        } else {
                            p.setLanguage(propertyLanguage);
                        }                               

                        // Set the Qualifier
                        PropertyQualifier propQual = new PropertyQualifier();
                        String tag = "source-code";
                        propQual.setPropertyQualifierName(tag);
                        txt = new Text();
                        txt.setContent((String) conceptCode);
                        propQual.setValue(txt);
                        p.addPropertyQualifier(propQual);

                        // Set the Source
                        Source s = new Source();
                        s.setContent(sourceCodeSystemName);
                        p.addSource(s);

                        concept.addPresentation(p);
                        // *** End block - Set concept's presentation ***
                                               
                        // *** Set the concept's properties ***
                        for (MifConceptProperty mifConceptProperty : mifConceptProperties) {
                            Property cp = new Property();
                            cp.setLanguage(MifVocabulary2LGConstants.DEFAULT_LANGUAGE_EN);
                            String property = mifConceptProperty.getName();
                            cp.setPropertyName(property);
                            cp.setPropertyId("P" + concept.getProperty().length);
                            txt = new Text();
                            txt.setContent(mifConceptProperty.getValue());
                            cp.setValue(txt);
                            concept.addProperty(cp);
                        }                        
                        // *** End block - Set the concept's properties ***
                                             
                        
                        // TODO might not be able to use multiple codes??? Cannot use concept's internalId as key by itself for multiples
                        //  in hashtable if concept has multiple codes - will overlay previous put entry.
//                        internalIdToEntityHash.put(Integer.valueOf(internalId), concept);
                        codeAndInternalIdToEntityHash.put(conceptCode, concept);
                        concepts.addEntity(concept);                        
                    }                   
                }
            }

        } catch (Exception e) {
            messages_.error("Failed while preparing concepts for HL7 MIF Vocabulary.", e);
            e.printStackTrace();
        } 

        // process the concept data
        messages_.info("Processing " + codeAndInternalIdToEntityHash.size() + " concepts...");
        csclass.setApproxNumConcepts(new Long(concepts.getEntity().length));

    }

    
    private void loadArtificialTopNodes(CodingScheme csclass, Entities concepts) {
        
        messages_.info("Processing code systems into top nodes");
        try {
            // Create an "@" top node.
            Entity rootNode = new Entity();

            // Create and set the concept code for "@"
            String topNodeDesignation = MifVocabulary2LGConstants.DEFAULT_ROOT_NODE;
            rootNode.setEntityCode(topNodeDesignation);
            rootNode.setEntityCodeNamespace(csclass.getCodingSchemeName());
            rootNode.setIsAnonymous(Boolean.TRUE);
            EntityDescription enDesc = new EntityDescription();
            enDesc.setContent("Root node for HL7 MIF Vocabulary subclass relations.");
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
            
            for (MifCodeSystem mifCodeSystem : codeSystemList) {
//            while (dataResults.next()) {
                
                Entity topNode = new Entity();
//                String nodeName = dataResults.getString("codeSystemName");
                String nodeName = mifCodeSystem.getName();
//                String entityDescription = dataResults.getString("fullName");
                String entityDescription = mifCodeSystem.getTitle();
//                String oid = dataResults.getString("codeSystemId");
                String oid = mifCodeSystem.getCodeSystemId();
                String def = "";  // Annotation containing description is not being parsed from XML source file
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
                // TODO It appears original code below was using distinct internalId's of HL7 concept as the topNode for the codeSystem. Need
                //   to do the same and consider multiple codes for a concept scenario?
                List<MifConcept> mifConcepts = mifCodeSystem.getCodeSystemVersions().get(0).getConcepts();
                for (MifConcept mifConcept : mifConcepts) {
                    
                    MifConceptProperty mifInternalIdProperty = null;
                    List<MifConceptProperty> mifConceptProperties = mifConcept.getConceptProperties();
                    for (MifConceptProperty mifCP : mifConceptProperties) {
                        if (mifCP.getName().equals("internalId")) {
                            mifInternalIdProperty = mifCP;
                            break;
                        }
                    }
                    String internalIdStr = mifInternalIdProperty.getValue();
//                    int internalId = Integer.parseInt(internalIdStr);
                    
                    List<MifConceptCode> mifConceptCodes = mifConcept.getConceptCodes();
                    String code = null;
                    for (MifConceptCode mifConceptCode : mifConceptCodes) {
                        code = mifConceptCode.getCode();
                        topNodes.add(code + ":" + internalIdStr);
                    }                                    
                }
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
                // TODO For parsed data, all codeSystems should be topNodes for their concepts, right? And none of the concepts and
                //  their codes should be removed given the parsed data is different than how the RIM database approach is set up.
//                ArrayList<String> nodesToRemove = new ArrayList<String>();
//                PreparedStatement checkForTopNode = null;
//                for (int i = 0; i < topNodes.size(); i++) {
//                    checkForTopNode = c
//                            .prepareStatement("SELECT targetInternalId FROM VCS_concept_relationship WHERE targetInternalId =?");
//                    checkForTopNode.setString(1, (String) topNodes.get(i));
//                    topNode_results = checkForTopNode.executeQuery();
//                    if (topNode_results.next()) {
//                        if (topNodes.get(i).equals(topNode_results.getString(1))) {
//                            nodesToRemove.add(topNodes.get(i));
//                        }
//                    }
//                    if (topNode_results != null)
//                        topNode_results.close();
//                    if (checkForTopNode != null)
//                        checkForTopNode.close();
//                }
//
//                for (int i = 0; i < nodesToRemove.size(); i++) {
//                    topNodes.remove(nodesToRemove.get(i));
//                }
                
                // Get the full concept code for each.
                // TODO Concatenation logic below already done in above iteration of codes for a concept
//                PreparedStatement getconceptSuffix = null;
//                ResultSet conceptCode = null;
//                for (int i = 0; i < topNodes.size(); i++) {
//                    getconceptSuffix = c
//                            .prepareStatement("SELECT conceptCode2 FROM VCS_concept_code_xref where internalId = ?");
//
//                    getconceptSuffix.setString(1, (String) topNodes.get(i));
//                    conceptCode = getconceptSuffix.executeQuery();
//                    conceptCode.next();
//                    topNodes.set(i, topNodes.get(i) + ":" + conceptCode.getString(1));
//                    if (conceptCode != null)
//                        conceptCode.close();
//                    if (getconceptSuffix != null)
//                        getconceptSuffix.close();
//                }

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
                        messages_.error("Failed while processing HL7 MIF Vocabulary psuedo top node hierarchy", e);
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

    private void loadConceptRelations(CodingScheme csclass, Hashtable<String, Entity> codeAndInternalIdToEntity) {

        List<MifCodeSystem> codeSystemList = mifVocabularyModel.getCodeSystems();
        for (MifCodeSystem mifCodeSystem : codeSystemList) {
            List<MifConcept> mifConceptList = mifCodeSystem.getCodeSystemVersions().get(0).getConcepts();
            // It is assumed every concept has a single "internalId" conceptProperty
            MifConceptProperty mifInternalIdProperty = null;
            for (MifConcept mifConcept : mifConceptList) {
                List<MifConceptProperty> mifConceptProperties = mifConcept.getConceptProperties();
                for (MifConceptProperty mifCP : mifConceptProperties) {
                    if (mifCP.getName().equals("internalId")) {
                        mifInternalIdProperty = mifCP;
                        break;
                    }
                }
                String internalIdStr = mifInternalIdProperty.getValue();

                List<MifConceptCode> mifConceptCodes = mifConcept.getConceptCodes();
                for (MifConceptCode mifConceptCode : mifConceptCodes) {

                    String sourceConceptCode = mifConceptCode.getCode() + ":" + internalIdStr;

                    
//            PreparedStatement relationship_ps = c
//                   .prepareStatement("SELECT distinct relationCode, sourceInternalId, targetInternalId FROM VCS_concept_relationship");
                    List<MifConceptRelationship> mifConceptRelationships = mifConcept.getConceptRelationships();
                    for (MifConceptRelationship mifConceptRelationship : mifConceptRelationships) {

                        Entity sourceEntity = (Entity) codeAndInternalIdToEntity.get(sourceConceptCode);

                        String targetConceptCode = mifConceptRelationship.getTargetConceptCode();
                        String targetCodeSystemId = mifConceptRelationship.getTargetCodeSystemId();
                        // TODO Remove println's below
                        System.out.println("DEBUG: loadConceptRelations() - sourceConcept code is " + sourceConceptCode);
                        System.out.println("DEBUG: loadConceptRelations() - targetConcept code is " + targetConceptCode);
                        String targetConceptCodeKey = null;
                        if (targetCodeSystemId == null) {
                            // Need to find targetConceptCode among the concepts for this codeset in order to grab its Entity.
                            targetConceptCodeKey = getKeyValueGivenCodeAndConceptsList(mifConceptList, targetConceptCode);
                        } else {
                            // Need to find targetConceptCode among the concepts for a different codeset in order to grab its Entity.
                            targetConceptCodeKey = getKeyValueGivenCodeAndCodeSystem(targetConceptCode, targetCodeSystemId);                            
                        }
                        System.out.println("DEBUG: loadConceptRelations() - targetConceptCodeKey is " + targetConceptCodeKey);
                        Entity targetEntity = (Entity) codeAndInternalIdToEntity.get(targetConceptCodeKey);

                        String association = mifConceptRelationship.getRelationshipName();

                        AssociationPredicate parent_association = (AssociationPredicate) RelationsUtil
                                .resolveAssociationPredicates(csclass, association).get(0);
                        
                        if (sourceEntity != null && targetEntity != null) {
                            AssociationSource ai = new AssociationSource();
                            ai.setSourceEntityCode(sourceEntity.getEntityCode());
                            ai.setSourceEntityCodeNamespace(csclass.getCodingSchemeName());
                            ai = RelationsUtil.subsume(parent_association, ai);

                            AssociationTarget at = new AssociationTarget();
                            at.setTargetEntityCode(targetEntity.getEntityCode());
                            at.setTargetEntityCodeNamespace(csclass.getCodingSchemeName());
                            at = RelationsUtil.subsume(ai, at);
                        }
                    } // end brace - for (MifConceptRelationship mifConceptRelationship : mifConceptRelationships)

                } // end brace - for (MifConceptCode mifConceptCode : mifConceptCodes)

            } // end brace - for (MifConcept mifConcept : conceptList)

        } // end brace - for (MifCodeSystem mifCodeSystem : codeSystemList)
    }
    
    
    private String getKeyValueGivenCodeAndConceptsList(List<MifConcept> mifConcepts, String targetCode) {
        
        String returnValue = null;
        
        outerloop:
        for (MifConcept mifConcept : mifConcepts) {
            MifConceptProperty mifInternalIdProperty = null;
            List<MifConceptProperty> mifConceptProperties = mifConcept.getConceptProperties();
            for (MifConceptProperty mifCP : mifConceptProperties) {
                if (mifCP.getName().equals("internalId")) {
                    mifInternalIdProperty = mifCP;
                    break;
                }
            }
            String internalIdStr = mifInternalIdProperty.getValue();
            
            List<MifConceptCode> mifConceptCodes = mifConcept.getConceptCodes();
            for (MifConceptCode mifConceptCode : mifConceptCodes) {
                // codes are case sensitive
                if (mifConceptCode.getCode().equals(targetCode)) {
                    returnValue = targetCode + ":" + internalIdStr;
                    break outerloop;
                }
            }            
        }
        
        return returnValue;
    }
    
    private String getKeyValueGivenCodeAndCodeSystem(String targetConceptCode, String targetCodeSystemId) {
        
        String returnValue = null;
        
        // Get the MifCodeSystem objects having the passed in targetCodeSystemId value
        List<MifCodeSystem> mifCodeSystems = mifVocabularyModel.getCodeSystems();
        MifCodeSystem targetCodeSystem = null;
        for (MifCodeSystem mifCodeSystem : mifCodeSystems) {
            if (mifCodeSystem.getCodeSystemId().equals(targetCodeSystemId)) {
                targetCodeSystem = mifCodeSystem;
                break;
            }
        }
        
        // Get the list of MifConcepts for the found MifCodeSystem and find the code for the target
        List<MifConcept> mifConcepts = targetCodeSystem.getCodeSystemVersions().get(0).getConcepts();
        
        outerLoop:
        for (MifConcept mifConcept : mifConcepts) {
            List<MifConceptCode> mifConceptCodes = mifConcept.getConceptCodes();
            for (MifConceptCode mifConceptCode : mifConceptCodes) {
                if (mifConceptCode.getCode().equals(targetConceptCode)) {
                    // get the internalId concept property value 
                    MifConceptProperty mifInternalIdProperty = null;
                    List<MifConceptProperty> mifConceptProperties = mifConcept.getConceptProperties();
                    for (MifConceptProperty mifCP : mifConceptProperties) {
                        if (mifCP.getName().equals("internalId")) {
                            mifInternalIdProperty = mifCP;
                            break;
                        }
                    }
                    returnValue = targetConceptCode + ":" + mifInternalIdProperty.getValue();
                    break outerLoop;
                }
            }
        }
                
        return returnValue;
    }
    
}
