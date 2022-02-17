
package edu.mayo.informatics.lexgrid.convert.directConversions.hl7;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;
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
import org.LexGrid.custom.concepts.EntityFactory;
import org.LexGrid.custom.relations.RelationsUtil;
import org.LexGrid.util.sql.DBUtility;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.lexevs.logging.messaging.impl.CachingMessageDirectorImpl;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.mayo.informatics.lexgrid.convert.utility.loaderPreferences.PreferenceLoaderConstants;

/**
 * This is the Main EMF conversion driver.  The virtually all of the conversion is driven from this
 * class. 
 * 
 * @author <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer</A>
 * @author <A HREF="mailto:stancl.craig@mayo.edu">Craig Stancl</A>
 * @author <A HREF="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala</A>
 *
 */
public class HL7MapToLexGrid {
    private LgMessageDirectorIF messages_;
    private String accessConnectionString;
    private String driver;
    private Hashtable<Integer, Entity> internalIdToEntityHash;
    private LoaderPreferences loaderPrefs;

    public HL7MapToLexGrid(String database, String driver, LgMessageDirectorIF lg_messages) {

        this.messages_ = new CachingMessageDirectorImpl(lg_messages);
        accessConnectionString = database;
        this.driver = driver;
        internalIdToEntityHash = new Hashtable<Integer, Entity>();
    }

    void initRun(CodingScheme csclass) {

        try {

            loadCodingScheme(csclass);
            loadAssociationEntityAndSupportedMaps(csclass);
            loadConcepts(csclass);
            loadPresentations();
            loadDefinitions();
            loadConceptProperties();
            loadRelations(csclass, driver, internalIdToEntityHash);

            // Load Metadata if the path has been specified in the Loader
            // Preferences
            if (loaderPrefs != null && loaderPrefs.getXMLMetadataFilePath() != null) {
                loadMetaData(csclass, accessConnectionString, driver);
            } else {
                messages_.info("No metadata file path was specified in the Loader Preferences, not loading Metadata.");
            }

        } catch (Exception e) {
            messages_.error("Failed to connect to RIM Database, check connection values");
            e.printStackTrace();
        } finally {

        }
    }

    void loadCodingScheme(CodingScheme csclass) {
        Connection c = null;
        try {
            messages_.info("Loading coding scheme information");
            c = DBUtility.connectToDatabase(accessConnectionString, driver, null, null);
            PreparedStatement getCodingSchemeInfo = c
                    .prepareStatement("SELECT modelID, name, versionNumber, description FROM Model");
            ResultSet results = getCodingSchemeInfo.executeQuery();
            results.next();
            String name = results.getString("modelID");
            csclass.setCodingSchemeName(name);
            csclass.setCodingSchemeURI(HL72LGConstants.DEFAULT_URN);
            csclass.setFormalName(results.getString("name"));
            EntityDescription enDesc = new EntityDescription();
            enDesc.setContent(results.getString("description"));
            csclass.setEntityDescription(enDesc);
            csclass.setDefaultLanguage(HL72LGConstants.DEFAULT_LANGUAGE_EN);
            String version = results.getString("versionNumber");
            if (version != null && version.length() > 0)
                csclass.setRepresentsVersion(version);
            else {
                csclass.setRepresentsVersion(SQLTableConstants.TBLCOLVAL_MISSING);
            }
            Text txt = new Text();
            txt.setContent("copyrightNotice goes here");

            csclass.setCopyright(txt);
            csclass.setMappings(new Mappings());

            // Add SupportedCodingScheme and SupportedLanguage Mappings
            SupportedCodingScheme scs = new SupportedCodingScheme();
            scs.setLocalId(csclass.getCodingSchemeName());
            scs.setUri(csclass.getCodingSchemeURI());
            csclass.getMappings().addSupportedCodingScheme(scs);

            SupportedLanguage lang = new SupportedLanguage();
            lang.setLocalId(HL72LGConstants.DEFAULT_LANGUAGE_EN);
            csclass.getMappings().addSupportedLanguage(lang);
           
            // Add SupportedHierarchy Mappings
            SupportedHierarchy hierarchy = new SupportedHierarchy();
            hierarchy.setLocalId(HL72LGConstants.ASSOCIATION_HAS_SUBTYPE);
            ArrayList<String> list = new ArrayList<String>();
            list.add(HL72LGConstants.ASSOCIATION_HAS_SUBTYPE);
            hierarchy.setAssociationNames(list);
            hierarchy.setRootCode(HL72LGConstants.DEFAULT_ROOT_NODE);
            hierarchy.setIsForwardNavigable(true);
            csclass.getMappings().addSupportedHierarchy(hierarchy);
            
            hierarchy = new SupportedHierarchy();
            hierarchy.setLocalId(HL72LGConstants.ASSOCIATION_IS_A);
            list = new ArrayList<String>();
            list.add(HL72LGConstants.ASSOCIATION_HAS_SUBTYPE);
            hierarchy.setAssociationNames(list);
            hierarchy.setRootCode(HL72LGConstants.DEFAULT_ROOT_NODE);
            hierarchy.setIsForwardNavigable(true);
            csclass.getMappings().addSupportedHierarchy(hierarchy);
            results.close();

        } catch (Exception e) {
            messages_.error("Failed while preparing HL7 Coding Scheme Class", e);
            e.printStackTrace();
        } finally {
            try {
                c.close();
            } catch (SQLException e) {
                messages_.debug("An error occurred while closing the MS Access connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    
    void loadAssociationEntityAndSupportedMaps(CodingScheme csclass) {
        Connection con = null;
        Relations relations = null;
        Entities concepts = csclass.getEntities();
        if (concepts == null) {
            concepts = new Entities();
            csclass.setEntities(concepts);
        }

        try {
            messages_.info("Loading AssociationEntityAndSupportedMaps");
            con = DBUtility.connectToDatabase(accessConnectionString, driver, null, null);
            // Pre-Load the supported associations -- this will just be the
            // hasSubtype association for 99% of the contained coding schemes.
            relations = new Relations();
            relations.setContainerName("relations");
            csclass.addRelations(relations);
            SupportedContainerName scn= new SupportedContainerName();
            scn.setLocalId("relations");
            csclass.getMappings().addSupportedContainerName(scn);

            PreparedStatement getAssociations_ps = con
                    .prepareStatement("SELECT distinct(relationCode) FROM VCS_concept_relationship");
            ResultSet association_results = getAssociations_ps.executeQuery();

            // This assures that the single other association (smaller_than)
            // will be supported and provides future support for associations
            // other than hasSubtype
            while (association_results.next()) {
                String association_name = association_results.getString("relationCode");
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
                                                                                                                                                                                            
                if (association_name.equals("hasSubtype")) {                 
                    parent_assocEntity.setIsTransitive(true);
                }
            }

            association_results.close();

            // Pre-load all the supported properties
            PreparedStatement getProperties_ps = con
                    .prepareStatement("SELECT distinct(propertyCode) FROM VCS_concept_property");
            ResultSet property_results = getProperties_ps.executeQuery();

            String property = null;
            while (property_results.next()) {
                property = property_results.getString("propertyCode");
                SupportedProperty sp = new SupportedProperty();
                sp.setLocalId(property);
                if (!Arrays.asList(csclass.getMappings().getSupportedProperty()).contains(sp))
                    csclass.getMappings().addSupportedProperty(sp);
            }
            property_results.close();

            // Pre-load the supported property qualifier source-code
            String propertyQualifier = "source-code";
            SupportedPropertyQualifier spq = new SupportedPropertyQualifier();
            spq.setLocalId(propertyQualifier);
            if (!Arrays.asList(csclass.getMappings().getSupportedPropertyQualifier()).contains(spq))
                csclass.getMappings().addSupportedPropertyQualifier(spq);

            // Pre-load all the supported sources
            PreparedStatement getSources_ps = con.prepareStatement("SELECT distinct(codeSystemName) FROM VCS_code_system");
            ResultSet source_results = getSources_ps.executeQuery();

            String source = null;
            while (source_results.next()) {
                source = source_results.getString("codeSystemName");
                SupportedSource ss = new SupportedSource();

                ss.setLocalId(source);
                if (!Arrays.asList(csclass.getMappings().getSupportedSource()).contains(ss))
                    csclass.getMappings().addSupportedSource(ss);
            }
            source_results.close();
        } catch (Exception e) {
            messages_.error("Failed while preparing HL7 concepts.", e);
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                messages_.debug("An error occurred while closing the MS Access connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    
    void loadConcepts(CodingScheme csclass) {

        Connection c = null;
        ResultSet results = null;

        Entities concepts = csclass.getEntities();
        if (concepts == null) {
            concepts = new Entities();
            csclass.setEntities(concepts);
        }

        try {
            messages_.info("Loading concepts");
            c = DBUtility.connectToDatabase(accessConnectionString, driver, null, null);

            // Create the artificial top nodes, the @ node. Persist the top nodes to a list
            // Create a method do handle them as concepts.
            loadArtificialTopNodes(csclass, concepts, c);

            // Pre-load some concept data into hash tables but eliminate the
            // code system scheme.
            PreparedStatement getConcept_ps = c.prepareStatement("SELECT internalId, conceptCode2, "
                    + SQLTableConstants.TBLCOL_CONCEPTSTATUS
                    + " FROM VCS_concept_code_xref WHERE codeSystemId2 <> ? and codeInstance <> ?");

            getConcept_ps.setString(1, HL72LGConstants.CODE_SYSTEM_OID);
            // We want only the default case which happens to be uppercase.
            // Ignore the lower case code
            getConcept_ps.setInt(2, 1);
            results = getConcept_ps.executeQuery();
            while (results.next()) {
                int internalId = results.getInt("internalId");
                String conceptCode = Integer.toString(internalId) + ":" + results.getString("conceptCode2");
                String status = results.getString(SQLTableConstants.TBLCOL_CONCEPTSTATUS);
                Entity concept = new Entity();
                concept.setEntityCode(conceptCode);
                concept.setEntityType(new String[] { EntityTypes.CONCEPT.toString() });
                concept.setEntityCodeNamespace(csclass.getCodingSchemeName());
                internalIdToEntityHash.put(Integer.valueOf(internalId), concept);
                concepts.addEntity(concept);

            }
            results.close();

        } catch (Exception e) {
            messages_.error("Failed while preparing HL7 concepts.", e);
            e.printStackTrace();
        } finally {
            try {
                c.close();
            } catch (SQLException e) {
                messages_.debug("An error occurred while closing the MS Access connection: " + e.getMessage());
                e.printStackTrace();
            }
        }

        // process the concept data
        messages_.info("Processing " + internalIdToEntityHash.size() + " concepts...");
        csclass.setApproxNumConcepts(new Long(concepts.getEntity().length));

    }

    
    
    void loadArtificialTopNodes(CodingScheme csclass, Entities concepts, Connection c) {
        messages_.info("Processing code systems into top nodes");
        ResultSet topNode_results = null;
        try {
            // Create an "@" top node.
            Entity rootNode = new Entity();

            // Create and set the concept code for "@"
            String topNodeDesignation = "@";
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
                    .resolveAssociationPredicates(csclass, HL72LGConstants.ASSOCIATION_HAS_SUBTYPE).get(0);
            ai = RelationsUtil.subsume(parent_assoc, ai);

            // Get all the code systems except the
            // legacy code system coding scheme in HL7
            PreparedStatement getArtificialTopNodeData = c
                    .prepareStatement("select cs.*, code.internalId FROM VCS_code_system AS cs "+
                                      "LEFT JOIN  VCS_concept_code_xref AS code "+
                                      "ON cs.codeSystemName= code.conceptCode2 " +
                                      "where (code.codeSystemId2 = ? OR code.codeSystemId2 is null) AND cs.codesystemid <> ?");
            getArtificialTopNodeData.setString(1, HL72LGConstants.CODE_SYSTEM_OID);
            getArtificialTopNodeData.setString(2, HL72LGConstants.CODE_SYSTEM_OID);
            ResultSet dataResults = getArtificialTopNodeData.executeQuery();
            while (dataResults.next()) {
                Entity topNode = new Entity();
                String nodeName = dataResults.getString("codeSystemName");
                String entityDescription = dataResults.getString("fullName");
                String oid = dataResults.getString("codeSystemId");
                String def = dataResults.getString("description");
                String internalId= dataResults.getString("internalId");
                if (StringUtils.isNotBlank(def)) {
                    int begin = def.lastIndexOf("<p>");
                    int end = def.lastIndexOf("</p>");
                    if (begin > -1) {
                        if (begin + 3 < end)
                            def = def.substring(begin + 3, end);
                    }
                }
               
                if (StringUtils.isNotBlank(internalId)) {
                    topNode.setEntityCode(internalId + ":" + nodeName);                   
                } else {
                    topNode.setEntityCode(nodeName + ":" + nodeName);
                }
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
                p.setPropertyName(HL72LGConstants.PROPERTY_PRINTNAME);
                p.setPropertyId("T1");
                p.setLanguage(HL72LGConstants.DEFAULT_LANGUAGE_EN);
                topNode.addPresentation(p);

                // Set definition
                if (StringUtils.isNotBlank(def)) {
                    Definition definition = new Definition();
                    Text defText = new Text();
                    defText.setContent(def);
                    definition.setValue(defText);
                    definition.setPropertyName(HL72LGConstants.PROPERTY_DEFINITION);
                    definition.setPropertyId("D1");
                    definition.setIsActive(Boolean.TRUE);
                    definition.setIsPreferred(Boolean.TRUE);
                    definition.setLanguage(HL72LGConstants.DEFAULT_LANGUAGE_EN);
                    topNode.addDefinition(definition);
                }
                
                topNode.addEntityType( EntityTypes.CONCEPT.toString());
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
                PreparedStatement getSystemCodes = c
                        .prepareStatement("SELECT DISTINCT (internalId), conceptCode2 FROM VCS_concept_code_xref WHERE codeSystemId2 =? AND codeSystemId2 <> ?");
                getSystemCodes.setString(1, oid);
                getSystemCodes.setString(2, HL72LGConstants.CODE_SYSTEM_OID);
                ResultSet systemCodes = getSystemCodes.executeQuery();
                while (systemCodes.next()) {
                    String testCode = systemCodes.getString("internalId");
                    if (testCode != null)
                        topNodes.add(testCode);
                }
                if (systemCodes != null)
                    systemCodes.close();
                if (getSystemCodes != null)
                    getSystemCodes.close();

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
            dataResults.close();
            messages_.info("Top node processing complete");
        } catch (Exception e) {
            messages_.error("Top node processing failed", e);
            e.printStackTrace();
        }

    }

    void loadPresentations() {
        ResultSet designationResults = null;
        Connection con = null;
        try {
            messages_.info("Loading all presentations");
            con = DBUtility.connectToDatabase(accessConnectionString, driver, null, null);
            // Pull source code system name and designation
            PreparedStatement designation_ps = con.prepareStatement("SELECT DISTINCT a.internalId, a.designation, "
                    + "a.language, a.preferredForLanguage, b.codeSystemId2, c.codeSystemName "
                    + "FROM VCS_concept_designation AS a, VCS_concept_code_xref AS b, " + "VCS_code_system AS c "
                    + "WHERE a.internalId=b.internalId AND c.codeSystemid=b.codeSystemId2  AND b.codeSystemId2 <> ? ");

            designation_ps.setString(1, HL72LGConstants.CODE_SYSTEM_OID);
            designationResults = designation_ps.executeQuery();

            if (designationResults.next()) {
                do {
                    int internalId = designationResults.getInt("internalId");
                    Entity concept = this.internalIdToEntityHash.get(Integer.valueOf(internalId));
                    if (concept == null) {
                        System.out.println("Concept of InternalId " + internalId + " not found");
                        messages_.info("Concept of InternalId " + internalId + " not found");
                    } else {
                        // Determine the source coding scheme concept code
                        String conceptCodeFull = concept.getEntityCode();
                        int colonPosition = conceptCodeFull.lastIndexOf(":");
                        String conceptCode = conceptCodeFull.substring(colonPosition + 1);
                        String sourceCodeSystemName = designationResults.getString("codeSystemName");
                        String entityDescription = designationResults.getString("designation");

                        Presentation p = new Presentation();
                        Text txt = new Text();
                        txt.setContent((String) entityDescription);
                        p.setValue(txt);

                        if (designationResults.getString("preferredForLanguage").equals("1")) {
                            p.setIsPreferred(Boolean.TRUE);
                            EntityDescription ed = new EntityDescription();
                            ed.setContent(entityDescription);
                            concept.setEntityDescription(ed);
                        } else { // Designation is not preferred for language,
                                 // set
                                 // to false
                            p.setIsPreferred(Boolean.FALSE);
                        }
                        p.setPropertyName(HL72LGConstants.PROPERTY_PRINTNAME);
                        p.setPropertyId("T" + concept.getPresentationCount());
                        String propertyLanguage = designationResults.getString("language");
                        if (StringUtils.isBlank(propertyLanguage)) {
                            p.setLanguage(HL72LGConstants.DEFAULT_LANGUAGE_EN);
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

                    }

                } while (designationResults.next());
            }
        } catch (Exception e) {
            messages_.error("Failed to load HL7 Concept presentation", e);
            e.printStackTrace();
        } finally {
            try {
                designationResults.close();
                con.close();
            } catch (SQLException e) {
                messages_.debug("An error occurred while closing the MS Access connection: " + e.getMessage());
                e.printStackTrace();
            }
        }

    }

    void loadDefinitions() {
        ResultSet desResults = null;
        Connection con = null;
        try {
            messages_.info("Loading all definitions");
            con = DBUtility.connectToDatabase(accessConnectionString, driver, null, null);
            PreparedStatement descriptions_ps = con
                    .prepareStatement("SELECT des.internalId, des.description, des.language "
                            + "FROM VCS_concept_description as des,  VCS_concept_code_xref AS code "
                            + " WHERE des.internalId= code.internalId AND  code.codeSystemId2 <> ? AND code.codeInstance <> ?");
            descriptions_ps.setString(1, HL72LGConstants.CODE_SYSTEM_OID);
            descriptions_ps.setInt(2, 1);
            desResults = descriptions_ps.executeQuery();

            String description = null;
            while (desResults.next()) {
                int internalId = desResults.getInt("internalId");
                Entity concept = this.internalIdToEntityHash.get(Integer.valueOf(internalId));
                if (concept == null) {
                    System.out.println("Concept of InternalId " + internalId + " not found");
                    messages_.info("Concept of InternalId " + internalId + " not found");
                } else {
                    // removes HTML tags from description text.
                    description = desResults.getString("description");
                    description = description.replaceAll("</?[A-Z]+\\b[^>]*>", "");
                    description = description.replaceAll("</?[a-z]+\\b[^>]*>", "");
                    if (StringUtils.isNotBlank(description)) {

                        Definition def = new Definition();
                        Text txt = new Text();
                        txt.setContent((String) description);
                        def.setValue(txt);
                        def.setIsPreferred(Boolean.TRUE);
                        def.setPropertyName(HL72LGConstants.PROPERTY_DEFINITION);
                        String propertyLanguage = desResults.getString("language");
                        if (StringUtils.isBlank(propertyLanguage)) {
                            def.setLanguage(HL72LGConstants.DEFAULT_LANGUAGE_EN);
                        } else {
                            def.setLanguage(propertyLanguage);
                        }                   

                        def.setPropertyId("D" + concept.getDefinitionCount());
                        concept.addDefinition(def);

                    }
                }

            }
        } catch (Exception e) {
            messages_.error("Failed to load HL7 Concept definitions", e);
            e.printStackTrace();
        } finally {
            try {

                desResults.close();
                con.close();
            } catch (SQLException e) {
                messages_.debug("An error occurred while closing the MS Access connection: " + e.getMessage());
                e.printStackTrace();
            }
        }

    }




    void loadConceptProperties() {
        ResultSet prop_results = null;
        Connection con = null;
        try {
            messages_.info("Loading all concept properties");
            con = DBUtility.connectToDatabase(accessConnectionString, driver, null, null);
            PreparedStatement properties_ps = con
                    .prepareStatement("SELECT p.internalId, p.propertyCode, p.propertyValue, p.language "
                            + "FROM VCS_concept_property AS p,  VCS_concept_code_xref AS code "
                            + " WHERE p.internalId= code.internalId AND  code.codeSystemId2 <> ? ");
            properties_ps.setString(1, HL72LGConstants.CODE_SYSTEM_OID);
            prop_results = properties_ps.executeQuery();

            while (prop_results.next()) {
                int internalId = prop_results.getInt("internalId");
                Entity concept = this.internalIdToEntityHash.get(Integer.valueOf(internalId));
                if (concept == null) {
                    System.out.println("Concept of InternalId " + internalId + " not found");
                    messages_.info("Concept of InternalId " + internalId + " not found");
                } else {
                    Property cp = new Property();
                    String propertyLanguage = prop_results.getString("language");
                    if (StringUtils.isBlank(propertyLanguage)) {
                        cp.setLanguage(HL72LGConstants.DEFAULT_LANGUAGE_EN);
                    } else {
                        cp.setLanguage(propertyLanguage);
                    }
                    String property = prop_results.getString("propertyCode");
                    cp.setPropertyName(property);
                    cp.setPropertyId("P" + concept.getProperty().length);
                    Text txt = new Text();
                    txt.setContent((String) prop_results.getString("propertyValue"));
                    cp.setValue(txt);
                    concept.addProperty(cp);
                }
            }

        } catch (Exception e) {
            messages_.error("Problem processing concept properties", e);
            e.printStackTrace();
        } finally {
            try {
                prop_results.close();
                con.close();
            } catch (SQLException e) {
                messages_.debug("An error occurred while closing the MS Access connection: " + e.getMessage());
                e.printStackTrace();
            }
        }

    }

    void loadRelations(CodingScheme csclass, String driver, Hashtable<Integer, Entity> internalId2codeMap) {
        Connection connection = null;
        ResultSet association_results = null;
        try {
            connection = DBUtility.connectToDatabase(accessConnectionString, driver, null, null);
            PreparedStatement relationship_ps = connection
                    .prepareStatement("SELECT distinct relationCode, sourceInternalId, targetInternalId FROM VCS_concept_relationship");
            association_results = relationship_ps.executeQuery();

            while (association_results.next()) {
                int source = association_results.getInt("sourceInternalId");
                int target = association_results.getInt("targetInternalId");
                String association = association_results.getString("relationCode");
                AssociationPredicate parent_association = (AssociationPredicate) RelationsUtil
                        .resolveAssociationPredicates(csclass, association).get(0);
                Entity sourceEntity = internalId2codeMap.get(Integer.valueOf(source));
                Entity targetEntity = internalId2codeMap.get(Integer.valueOf(target));

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
            }
        } catch (SQLException e) {
            messages_.error("Failed while getting HL7 relations from the RIM database", e);
            e.printStackTrace();
        } catch (Exception e) {
            messages_.debug("Error occurred while connecting to the RIM database. " + accessConnectionString + driver);
            e.printStackTrace();
        } finally {
            try {
                association_results.close();
                connection.close();
            } catch (SQLException e) {
                messages_.debug("An error occurred while closing the MS Access connections.");
                e.printStackTrace();
            }
        }
    }

    void loadMetaData(CodingScheme csclass, String connectionString, String driver) {

        messages_.info("Loading individual coding scheme metadata");
        Connection c = null;

        // create the filename of the metadata file to be created
        String filename = loaderPrefs.getXMLMetadataFilePath() + "/"
                + PreferenceLoaderConstants.META_HL7_METADATA_FILE_NAME;

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(filename);

            OutputFormat of = new OutputFormat("XML", "ISO-8859-1", true);
            of.setIndent(1);
            of.setIndenting(true);
            XMLSerializer serializer = new XMLSerializer(fos, of);

            // SAX2.0 ContentHandler.
            ContentHandler hd = serializer.asContentHandler();
            hd.startDocument();

            // Element attributes.
            AttributesImpl atts = new AttributesImpl();

            // CODINGSCHEMES tag.
            hd.startElement("", "", "codingSchemes", atts);

            String defaultLanguage = "en";
            String isNative = "0";
            String codeSystemId;
            String codeSystemType;
            String codeSystemName;
            String fullName;
            String description;
            String releaseId;
            String copyrightNotice;
            Integer approximateNumberofConcepts;

            try {
                c = DBUtility.connectToDatabase(accessConnectionString, driver, null, null);

                PreparedStatement getCodingSchemeMetaData = c
                        .prepareStatement("SELECT b.codeSystemid, b.codeSystemType, b.codeSystemName, "
                                + "b.fullName, b.description, b.releaseId, b.copyrightNotice "
                                + "FROM VCS_code_system AS b");
                ResultSet codingSchemeMetaData = getCodingSchemeMetaData.executeQuery();

                PreparedStatement getCodingSchemeConceptCount = c
                        .prepareStatement("SELECT VCS_concept_code_xref.codeSystemId2, "
                                + "COUNT (VCS_concept_code_xref.codeSystemId2) as conceptcount "
                                + "FROM VCS_concept_code_xref " + "GROUP BY VCS_concept_code_xref.codeSystemId2;");

                ResultSet codingSchemeConceptCount = getCodingSchemeConceptCount.executeQuery();

                Hashtable<String, Integer> conceptCountList = new Hashtable<String, Integer>();

                while (codingSchemeConceptCount.next()) {

                    String codingSchemeId = codingSchemeConceptCount.getString("codeSystemId2");
                    String codingSchemeCount = codingSchemeConceptCount.getString("conceptcount");

                    conceptCountList.put(new String(codingSchemeId), new Integer(codingSchemeCount));
                }

                codingSchemeConceptCount.close();

                int codeSchemeCounter = 0;

                while (codingSchemeMetaData.next()) {
                    codeSystemId = codingSchemeMetaData.getString("codeSystemid");
                    if (codeSystemId == null) {
                        codeSystemId = SQLTableConstants.TBLCOLVAL_MISSING;
                    }

                    codeSystemType = codingSchemeMetaData.getString("codeSystemType");
                    if (codeSystemType == null) {
                        codeSystemType = SQLTableConstants.TBLCOLVAL_MISSING;
                    }

                    codeSystemName = codingSchemeMetaData.getString("codeSystemName");
                    if (codeSystemName == null) {
                        codeSystemName = SQLTableConstants.TBLCOLVAL_MISSING;
                    }

                    fullName = codingSchemeMetaData.getString("fullName");
                    if (fullName == null) {
                        fullName = SQLTableConstants.TBLCOLVAL_MISSING;
                    }

                    description = codingSchemeMetaData.getString("description");
                    if (description == null) {
                        description = SQLTableConstants.TBLCOLVAL_MISSING;
                    }
                    // The description contains HTML tags. We try to remove
                    // them.
                    else {
                        int begin = description.lastIndexOf("<p>");
                        int end = description.lastIndexOf("</p>");
                        if (begin > -1) {
                            if (begin + 3 < end)
                                description = description.substring(begin + 3, end);
                        }
                    }

                    releaseId = codingSchemeMetaData.getString("releaseId");
                    if (releaseId == null) {
                        releaseId = SQLTableConstants.TBLCOLVAL_MISSING;
                    }

                    copyrightNotice = codingSchemeMetaData.getString("copyrightNotice");
                    if (copyrightNotice == null) {
                        copyrightNotice = SQLTableConstants.TBLCOLVAL_MISSING;
                    }

                    approximateNumberofConcepts = ((Integer) conceptCountList.get(codeSystemId));
                    if (approximateNumberofConcepts == null) {
                        approximateNumberofConcepts = new Integer(0);
                    }

                    // Begin codingScheme element
                    atts.clear();
                    atts.addAttribute("", "", SQLTableConstants.TBLCOL_CODINGSCHEMENAME, "CDATA", codeSystemName);
                    // May want to change to _fullName
                    atts.addAttribute("", "", SQLTableConstants.TBLCOL_FORMALNAME, "CDATA", fullName);
                    atts.addAttribute("", "", SQLTableConstants.TBLCOL_CODINGSCHEMEURI, "CDATA", codeSystemId);
                    atts.addAttribute("", "", SQLTableConstants.TBLCOL_DEFAULTLANGUAGE, "CDATA", defaultLanguage);
                    atts.addAttribute("", "", SQLTableConstants.TBLCOL_REPRESENTSVERSION, "CDATA", releaseId);
                    atts.addAttribute("", "", SQLTableConstants.TBLCOL_ISNATIVE, "CDATA", isNative);
                    atts.addAttribute("", "", SQLTableConstants.TBLCOL_APPROXNUMCONCEPTS, "CDATA",
                            approximateNumberofConcepts.toString());
                    hd.startElement("", "", SQLTableConstants.TBLCOL_CODINGSCHEME, atts);

                    // localname
                    atts.clear();
                    hd.startElement("", "", "localName", atts);
                    hd.characters(codeSystemName.toCharArray(), 0, codeSystemName.length());
                    hd.endElement("", "", SQLTableConstants.TBLCOLVAL_LOCALNAME);

                    // entityDescription
                    atts.clear();
                    hd.startElement("", "", SQLTableConstants.TBLCOL_ENTITYDESCRIPTION, atts);
                    hd.characters(description.toCharArray(), 0, description.length());
                    hd.endElement("", "", SQLTableConstants.TBLCOL_ENTITYDESCRIPTION);

                    // copyright
                    atts.clear();
                    hd.startElement("", "", SQLTableConstants.TBLCOL_COPYRIGHT, atts);
                    hd.characters(copyrightNotice.toCharArray(), 0, copyrightNotice.length());
                    hd.endElement("", "", SQLTableConstants.TBLCOL_COPYRIGHT);

                    // May need to include as Property
                    // // CodingScheme
                    // atts.clear();
                    // hd.startElement("","","CodingScheme",atts);
                    // hd.characters(_codeSystemType.toCharArray(),0,_codeSystemType.length());
                    // hd.endElement("","","CodingScheme");

                    // End codingScheme element
                    hd.endElement("", "", SQLTableConstants.TBLCOL_CODINGSCHEME);

                    codeSchemeCounter++;

                } // End while there are result rows to process

                getCodingSchemeConceptCount.close();

                hd.endElement("", "", "codingSchemes");
                hd.endDocument();
                fos.close();

            } catch (Exception e) {
                messages_.error("Failed while preparing HL7 Code System MetaData.", e);
                e.printStackTrace();
            } finally {
                try {
                    c.close();
                } catch (SQLException e) {
                    messages_.debug("An error occurred while closing the MS Access connection: " + e.getMessage());
                    e.printStackTrace();
                }
            }

        } catch (FileNotFoundException fnfe) {
            messages_.debug("Loader Preferences file was not found, file: " + filename);
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            messages_.debug("IOException, file: " + filename);
            ioe.printStackTrace();
        } catch (SAXException saxe) {
            messages_.debug("SAXException, file: " + filename);
            saxe.printStackTrace();
        }

    }

    public void setLoaderPrefs(LoaderPreferences loaderPrefs) {
        this.loaderPrefs = loaderPrefs;
    }

    public String getIntValue(String code) {
        char[] chars = code.toCharArray();

        StringBuffer buffer = new StringBuffer();
        for (char c : chars) {
            int integer = Character.getNumericValue(c);
            buffer.append(String.valueOf(integer));
        }
        return buffer.toString();
    }

}