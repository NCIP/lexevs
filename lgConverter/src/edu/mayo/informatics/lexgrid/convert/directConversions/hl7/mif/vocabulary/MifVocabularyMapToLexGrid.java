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
import org.LexGrid.naming.SupportedNamespace;
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
            // loadConcepts method below includes the Concept's presentation and properties
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
            hierarchy.setLocalId(MifVocabulary2LGConstants.ASSOCIATION_IS_A);
            ArrayList<String> list = new ArrayList<String>();
            list.add(MifVocabulary2LGConstants.ASSOCIATION_HAS_SUBTYPE);
            hierarchy.setAssociationNames(list);
            hierarchy.setRootCode(MifVocabulary2LGConstants.DEFAULT_ROOT_NODE);
            hierarchy.setIsForwardNavigable(true);
            csclass.getMappings().addSupportedHierarchy(hierarchy);
            
            // Add supported Namespace
            SupportedNamespace sn = new SupportedNamespace();
            sn.setContent(MifVocabulary2LGConstants.NAMESPACE);
            sn.setEquivalentCodingScheme(name);
            sn.setLocalId(MifVocabulary2LGConstants.NAMESPACE);
            sn.setUri(csclass.getCodingSchemeURI());
            csclass.getMappings().addSupportedNamespace(sn);

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
            parent_assocEntity.setEntityCodeNamespace(MifVocabulary2LGConstants.NAMESPACE);
            parent_assocEntity.setIsTransitive(true);                    

            concepts.addEntity(parent_assocEntity);

            RelationsUtil.subsume(relations, parent_assoc); 
            
            parent_assoc = new AssociationPredicate();
            parent_assoc.setAssociationName(MifVocabulary2LGConstants.ASSOCIATION_IS_A);
            parent_assocEntity = EntityFactory.createAssociation();
            parent_assocEntity.setEntityCode(MifVocabulary2LGConstants.ASSOCIATION_IS_A);
            parent_assocEntity.setForwardName(MifVocabulary2LGConstants.ASSOCIATION_IS_A);
            parent_assocEntity.setEntityCodeNamespace(MifVocabulary2LGConstants.NAMESPACE);
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
                sa.setEntityCodeNamespace(MifVocabulary2LGConstants.NAMESPACE);                
                csclass.getMappings().addSupportedAssociation(sa);

                parent_assoc = new AssociationPredicate();
                parent_assoc.setAssociationName(association_name);

                parent_assocEntity = EntityFactory.createAssociation();
                parent_assocEntity.setEntityCode(association_name);
                parent_assocEntity.setForwardName(association_name);
                parent_assocEntity.setEntityCodeNamespace(MifVocabulary2LGConstants.NAMESPACE);
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

            List<MifCodeSystem> codeSystemList = mifVocabularyModel.getCodeSystems();
            for (MifCodeSystem mifCodeSystem : codeSystemList) {
                List<MifConcept> conceptList = mifCodeSystem.getCodeSystemVersions().get(0).getConcepts();
                // It is assumed every concept has a single "internalId" conceptProperty
                MifConceptProperty mifInternalIdProperty = null;
                for (MifConcept mifConcept : conceptList) {
                    List<MifConceptProperty> mifConceptProperties = mifConcept.getConceptProperties();
                    for (MifConceptProperty mifCP : mifConceptProperties) {
                        if (mifCP.getName().equals("internalId")) {
                            mifInternalIdProperty = mifCP;
                            break;
                        }
                    }
                    String internalIdStr = mifInternalIdProperty.getValue();
                    
                    //  Note: if a concept has more than one code defined, the current design will create a concept entity for every code using
                    //  the code's code value and the concept's internalId value (concept property common to both) as the LexGrid entity's code value. 
                    
                    List<MifConceptCode> mifConceptCodes = mifConcept.getConceptCodes();
                    for (MifConceptCode mifConceptCode : mifConceptCodes) {
                        
                        String conceptCode = internalIdStr + ":" + mifConceptCode.getCode();
                        String status = mifConceptCode.getStatus();
                        Entity concept = new Entity();
                        concept.setEntityCode(conceptCode);
                        concept.setEntityType(new String[] { EntityTypes.CONCEPT.toString() });
                        concept.setEntityCodeNamespace(MifVocabulary2LGConstants.NAMESPACE);
                        concept.setStatus(status);                        
                        if (status.equalsIgnoreCase("active")) {
                            concept.setIsActive(Boolean.TRUE);
                        } else {
                            concept.setIsActive(Boolean.FALSE);
                        }
                        
                        // *** Set concept's presentation ***
                        String sourceCodeSystemName = mifCodeSystem.getName();
                        String entityDescription = null; 
                        MifPrintName mifPrintName = mifConcept.getPrintName();
                        if (mifPrintName != null && mifPrintName.getText() != null) {
                            entityDescription = mifPrintName.getText(); 
                        } else {
                            entityDescription = mifConceptCode.getCode(); 
                        }                       
                        Presentation p = new Presentation();
                        Text txt = new Text();
                        txt.setContent(entityDescription);
                        p.setValue(txt);
                        // Note only the last <printName> having the preferredForLanguage = "true" - count will be 1
                        p.setIsPreferred(Boolean.TRUE);
                        EntityDescription ed = new EntityDescription();
                        ed.setContent(entityDescription);
                        concept.setEntityDescription(ed);
                        p.setPropertyName(MifVocabulary2LGConstants.PROPERTY_PRINTNAME);
                        p.setPropertyId("T" + "1");
                        String propertyLanguage = mifPrintName.getLanguage();
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

                        // Set definition
                        String def = null;
                        if (mifConcept.getDefinition() != null) {
                            def = mifConcept.getDefinition();
                        } else {
                            def = mifConceptCode.getCode();
                        }                        
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
                            concept.addDefinition(definition);
                        }

                        codeAndInternalIdToEntityHash.put(conceptCode, concept);
                        concepts.addEntity(concept);                        
                    }                   
                }
            }

        } catch (Exception e) {
            messages_.error("Failed while preparing concepts for HL7 MIF Vocabulary.", e);
            e.printStackTrace();
        } 

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
            rootNode.setEntityCodeNamespace(MifVocabulary2LGConstants.NAMESPACE);
            rootNode.setIsAnonymous(Boolean.TRUE);
            EntityDescription enDesc = new EntityDescription();
            enDesc.setContent("Root node for HL7 MIF Vocabulary subclass relations.");
            rootNode.setEntityDescription(enDesc);
            concepts.addEntity(rootNode);

            AssociationSource ai = new AssociationSource();
            ai.setSourceEntityCode(rootNode.getEntityCode());
            ai.setSourceEntityCodeNamespace(MifVocabulary2LGConstants.NAMESPACE);
            AssociationPredicate parent_assoc = (AssociationPredicate) RelationsUtil
                    .resolveAssociationPredicates(csclass, MifVocabulary2LGConstants.ASSOCIATION_HAS_SUBTYPE).get(0);
            ai = RelationsUtil.subsume(parent_assoc, ai);

            // Get all the code systems contained the XML source file which have been parsed into conversion objects and create
            // concept entities to be a parent (hasSubto the concept entities that contain codes in the source XML file.
            List<MifCodeSystem> codeSystemList = mifVocabularyModel.getCodeSystems();
            
            for (MifCodeSystem mifCodeSystem : codeSystemList) {                
                Entity topNode = new Entity();
                String nodeName = mifCodeSystem.getName();
                String entityDescription = mifCodeSystem.getTitle();
                String oid = mifCodeSystem.getCodeSystemId();
                String def = "";
                if (mifCodeSystem.getDescription() != null) {
                    def = mifCodeSystem.getDescription();
                } else {
                    def = mifCodeSystem.getTitle();
                }
               
                topNode.setEntityCode(nodeName + ":" + oid);                   
                topNode.setEntityCodeNamespace(MifVocabulary2LGConstants.NAMESPACE);

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
                at.setTargetEntityCodeNamespace(MifVocabulary2LGConstants.NAMESPACE);
                RelationsUtil.subsume(ai, at);

                // Now find the concept of the codeSystem and subsume them to this
                // artificial top node that is the codeSystem.
                ArrayList<String> topNodes = new ArrayList<String>();
                // Need to use internalId of HL7 concept in conjunction with the conceptCode code value to make it distinct given
                // code values are not unique across the load source file. 
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
                    
                    List<MifConceptCode> mifConceptCodes = mifConcept.getConceptCodes();
                    String code = null;
                    for (MifConceptCode mifConceptCode : mifConceptCodes) {
                        code = mifConceptCode.getCode();
                        topNodes.add(internalIdStr + ":" + code);
                    }                                    
                }
                
                // For each HL7 concept having a code, subsume to the current artificial parent node representing the
                // codeSystem.
                for (int j = 0; j < topNodes.size(); j++) {
                    try {
                        AssociationSource atn = new AssociationSource();
                        atn.setSourceEntityCode(topNode.getEntityCode());
                        atn.setSourceEntityCodeNamespace(MifVocabulary2LGConstants.NAMESPACE);
                        atn = RelationsUtil.subsume(parent_assoc, atn);

                        AssociationTarget atopNode = new AssociationTarget();
                        atopNode.setTargetEntityCode((String) topNodes.get(j));
                        atopNode.setTargetEntityCodeNamespace(MifVocabulary2LGConstants.NAMESPACE);
                        RelationsUtil.subsume(atn, atopNode);
                    } catch (Exception e) {
                        messages_.error("Failed while processing HL7 MIF Vocabulary psuedo top node hierarchy", e);
                        e.printStackTrace();
                    }
                }
            }
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

                    String sourceConceptCode = internalIdStr + ":" + mifConceptCode.getCode();

                    List<MifConceptRelationship> mifConceptRelationships = mifConcept.getConceptRelationships();
                    for (MifConceptRelationship mifConceptRelationship : mifConceptRelationships) {

                        Entity sourceEntity = (Entity) codeAndInternalIdToEntity.get(sourceConceptCode);

                        String targetConceptCode = mifConceptRelationship.getTargetConceptCode();
                        String targetCodeSystemId = mifConceptRelationship.getTargetCodeSystemId();
                        String targetConceptCodeKey = null;
                        if (targetCodeSystemId == null) {
                            // Need to find targetConceptCode among the concepts for this codeset in order to grab its Entity.
                            targetConceptCodeKey = getKeyValueGivenCodeAndConceptsList(mifConceptList, targetConceptCode);
                        } else {
                            // Need to find targetConceptCode among the concepts for a different codeset in order to grab its Entity.
                            targetConceptCodeKey = getKeyValueGivenCodeAndCodeSystem(targetConceptCode, targetCodeSystemId);                            
                        }
                        Entity targetEntity = (Entity) codeAndInternalIdToEntity.get(targetConceptCodeKey);

                        String association = mifConceptRelationship.getRelationshipName();

                        AssociationPredicate parent_association = (AssociationPredicate) RelationsUtil
                                .resolveAssociationPredicates(csclass, association).get(0);
                        
                        if (sourceEntity != null && targetEntity != null) {
                            AssociationSource ai = new AssociationSource();
                            ai.setSourceEntityCode(sourceEntity.getEntityCode());
                            ai.setSourceEntityCodeNamespace(MifVocabulary2LGConstants.NAMESPACE);
                            ai = RelationsUtil.subsume(parent_association, ai);

                            AssociationTarget at = new AssociationTarget();
                            at.setTargetEntityCode(targetEntity.getEntityCode());
                            at.setTargetEntityCodeNamespace(MifVocabulary2LGConstants.NAMESPACE);
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
                    returnValue = internalIdStr + ":" + targetCode;
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
