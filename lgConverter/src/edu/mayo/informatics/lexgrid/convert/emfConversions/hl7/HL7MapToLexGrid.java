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
package edu.mayo.informatics.lexgrid.convert.emfConversions.hl7;

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
import org.LexGrid.concepts.Concept;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.naming.SupportedCodingScheme;
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
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 *
 */
/**
 * @author <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer</A>
 * @author <A HREF="mailto:stancl.craig@mayo.edu">Craig Stancl</A>
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * 
 */
public class HL7MapToLexGrid {
    private LgMessageDirectorIF messages_;
    private String accessConnectionString;
    private String driver;
    private String codingScheme;
    private Hashtable conceptsList;
    private Hashtable internalIdConceptCodeMap;
    private LoaderPreferences loaderPrefs;

    public HL7MapToLexGrid(String currentCodingScheme, String database, String driver, LgMessageDirectorIF lg_messages) {

        this.messages_ = new CachingMessageDirectorImpl(lg_messages);
        accessConnectionString = database;
        this.driver = driver;
        this.codingScheme = currentCodingScheme;
        conceptsList = new Hashtable();
        internalIdConceptCodeMap = new Hashtable();
    }

    void initRun(CodingScheme csclass) {

        try {

            loadCodingScheme(csclass, accessConnectionString, driver);
            loadConcepts(csclass, accessConnectionString, driver);

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

    void loadCodingScheme(CodingScheme csclass, Connection c) {

        try {

            // TODO Load any persisted HL7 rim metadata as coding scheme data
            // here
            // TODO Get the RIM data elements from the Model Table and persist
            // to the
            // emf csclass object as is done below
            // TODO use the statement below later in an iterative loop to get
            // the
            // top node and possibly persist the metadata for each scheme as
            // properties.
            PreparedStatement getCodingSchemeInfo = c.prepareStatement("SELECT codeSystemid, "
                    + "codeSystemType, codeSystemName, fullName, description, "
                    + "releaseId, copyrightNotice FROM VCS_Code_System WHERE codeSystemid = ?");
            getCodingSchemeInfo.setString(1, codingScheme);
            ResultSet results = getCodingSchemeInfo.executeQuery();
            results.next();

            String name = results.getString("codeSystemName");

            csclass.setCodingSchemeName(name);
            csclass.setCodingSchemeURI(results.getString("codeSystemid"));
            csclass.setFormalName(results.getString("FullName"));
            EntityDescription enDesc = new EntityDescription();
            enDesc.setContent(results.getString("description"));
            csclass.setEntityDescription(enDesc);
            csclass.setDefaultLanguage("en");

            String version = results.getString("releaseId");
            if (version != null && version.length() > 0)
                csclass.setRepresentsVersion(version);
            else {
                csclass.setRepresentsVersion(SQLTableConstants.TBLCOLVAL_MISSING);
            }
            Text txt = new Text();
            txt.setContent((String) results.getString("copyrightNotice"));
            csclass.setCopyright(txt);
            csclass.setMappings(new Mappings());

            // Add SupportedCodingScheme and SupportedLanguage Mappings
            SupportedCodingScheme scs = new SupportedCodingScheme();
            scs.setLocalId(csclass.getCodingSchemeName());
            scs.setUri(csclass.getCodingSchemeURI());

            csclass.getMappings().addSupportedCodingScheme(scs);

            SupportedLanguage lang = new SupportedLanguage();
            lang.setLocalId(HL72EMFConstants.DEFAULT_LANGUAGE_EN);
            csclass.getMappings().addSupportedLanguage(lang);

            results.close();
        } catch (Exception e) {
            messages_.error("Failed while preparing HL7 Coding Scheme Class", e);
            e.printStackTrace();
        }
    }

    void loadCodingScheme(CodingScheme csclass, String connectionString, String driver) {
        Connection c = null;
        try {

            c = DBUtility.connectToDatabase(accessConnectionString, driver, null, null);

            PreparedStatement getCodingSchemeInfo = c
                    .prepareStatement("SELECT modelID, name, versionNumber, description FROM Model");
            ResultSet results = getCodingSchemeInfo.executeQuery();
            results.next();
            String name = results.getString("modelID");
            csclass.setCodingSchemeName(name);
            csclass.setCodingSchemeURI(HL72EMFConstants.DEFAULT_URN);
            csclass.setFormalName(results.getString("name"));
            EntityDescription enDesc = new EntityDescription();
            enDesc.setContent(results.getString("description"));
            csclass.setEntityDescription(enDesc);
            csclass.setDefaultLanguage(HL72EMFConstants.DEFAULT_LANGUAGE_EN);
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
            lang.setLocalId(HL72EMFConstants.DEFAULT_LANGUAGE_EN);
            csclass.getMappings().addSupportedLanguage(lang);
            results.close();
            // Add SupportedHierarchy Mappings
            SupportedHierarchy hierarchy = new SupportedHierarchy();
            hierarchy.setLocalId(HL72EMFConstants.DEFAULT_ASSOC);
            ArrayList list = new ArrayList();
            list.add(HL72EMFConstants.DEFAULT_ASSOC);
            hierarchy.setAssociationNames(list);
            hierarchy.setRootCode(HL72EMFConstants.DEFAULT_ROOT_NODE);
            hierarchy.setIsForwardNavigable(true);
            csclass.getMappings().addSupportedHierarchy(hierarchy);

            
//            SupportedProperty sp = NamingFactory.eINSTANCE.createSupportedProperty();
//            sp.setLocalId(value);
//            sp.setValue(value);
//            
//            SupportedPropertyType spt = NamingFactory.eINSTANCE.createSupportedPropertyType();
//            spt.setLocalId();
//            spt.setValue(value);
            
        } catch (Exception e) {
            messages_.error("Failed while preparing HL7 Coding Scheme Class", e);
            e.printStackTrace();
        } finally {
            try {
                c.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    void loadConcepts(CodingScheme csclass, String connectionString, String driver) {

        Connection c = null;
        String association_name = null;
        Relations relations = null;
        AssociationPredicate parent_assoc = null;
        AssociationEntity parent_assocEntity = null;
        AssociationPredicate hasSubtypeAssociation = null;
        AssociationEntity hasSubtypeAssociationEntity = null;
        ResultSet associations = null;
        ResultSet properties = null;
        ResultSet sources = null;
 //       ResultSet topNodes = null;
        ResultSet results = null;
        
        Entities concepts = csclass.getEntities();
        if (concepts == null) {
            concepts = new Entities();
            csclass.setEntities(concepts);
        }
        int num = 0;
        try {
            c = DBUtility.connectToDatabase(accessConnectionString, driver, null, null);
            // Pre-Load the supported associations -- this will just be the
            // hasSubtype association for 99% of the contained coding schemes.
            relations = new Relations();
            relations.setContainerName("relations");
            csclass.addRelations(relations);

            PreparedStatement getAssociations = c
                    .prepareStatement("SELECT distinct(relationCode) FROM VCS_concept_relationship");
            associations = getAssociations.executeQuery();

            // This assures that the single other association (smaller_than)
            // will be supported and provides future support for associations
            // other than hasSubtype
            while (associations.next()) {
                association_name = associations.getString("relationCode");
                SupportedAssociation sa = new SupportedAssociation();
                sa.setLocalId(association_name);
                csclass.getMappings().addSupportedAssociation(sa);

                parent_assoc = new AssociationPredicate();
                parent_assoc.setAssociationName(association_name);
                
                parent_assocEntity = new AssociationEntity();
                parent_assocEntity.setEntityCode(association_name);
                parent_assocEntity.setForwardName(association_name);
                parent_assocEntity.setIsTransitive(true);
                RelationsUtil.subsume(relations, parent_assoc); // whether need to provide the associationEntity?
                messages_.info("Loading association: " + association_name);

                // Save a reference to the hasSubtype association so we can
                // use it to attach the artificial top nodes to the "@" node
                if (association_name.equals("hasSubtype")) {
                    hasSubtypeAssociation = parent_assoc;
                }
            }

            associations.close();

            // Pre-load all the supported properties
            PreparedStatement getProperties = c
                    .prepareStatement("SELECT distinct(propertyCode) FROM VCS_concept_property");
           properties = getProperties.executeQuery();

            String property = null;
            while (properties.next()) {
                property = properties.getString("propertyCode");
                SupportedProperty sp = new SupportedProperty();
                sp.setLocalId(property);
                if (!Arrays.asList(csclass.getMappings().getSupportedProperty()).contains(sp))
                    csclass.getMappings().addSupportedProperty(sp);
            }
            properties.close();

            // Pre-load the supported property qualifier source-code
            String propertyQualifier = "source-code";
            SupportedPropertyQualifier spq = new SupportedPropertyQualifier();
            spq.setLocalId(propertyQualifier);
            if (!Arrays.asList(csclass.getMappings().getSupportedPropertyQualifier()).contains(spq))
                csclass.getMappings().addSupportedPropertyQualifier(spq);

            // Pre-load all the supported sources
            PreparedStatement getSources = c.prepareStatement("SELECT distinct(codeSystemName) FROM VCS_code_system");
            sources = getSources.executeQuery();

            String source = null;
            while (sources.next()) {
                source = sources.getString("codeSystemName");
                SupportedSource ss = new SupportedSource();

                ss.setLocalId(source);
                if (!Arrays.asList(csclass.getMappings().getSupportedSource()).contains(ss))
                    csclass.getMappings().addSupportedSource(ss);
            }
            sources.close();
       

            // Create the artificial top nodes, the @ node.
            // Persist the top nodes to a list
            // Create a method do handle them as concepts.
                loadArtificialTopNodes(csclass, concepts,
                        hasSubtypeAssociation, c);
//            }
            

            // Pre-load some concept data into hash tables but eliminate the code system scheme.
            PreparedStatement getConcept = c.prepareStatement("SELECT internalId, conceptCode2, "
                    + SQLTableConstants.TBLCOL_CONCEPTSTATUS + " FROM VCS_concept_code_xref WHERE codeSystemId2 <> ?");
            getConcept.setString(1, "2.16.840.1.113883.5.22");
            results = getConcept.executeQuery();
            int i = 0;
            while (results.next()) {
                int internalId = results.getInt("internalId");
                String conceptCode = Integer.toString(internalId) + ":" + results.getString("conceptCode2");
                String status = results.getString(SQLTableConstants.TBLCOL_CONCEPTSTATUS);
                conceptsList.put(new Integer(i), new HL7ConceptContainer(Integer.toString(internalId), conceptCode,
                        status));
                internalIdConceptCodeMap.put(new Integer(internalId), conceptCode);
                i++;
            }
            results.close();
        } catch (Exception e) {
            messages_.error("Failed while preparing HL7 concepts.", e);
            e.printStackTrace();
        } finally {
            try {
                
//                associations.close();
//                properties.close();
//                sources.close();
//                results.close();
                c.close();
            } catch (SQLException e) {
                // TODO drop stack trace.
                e.printStackTrace();
            }
        }

        // process the concept data
        messages_.info("Processing concepts");
        for (int j = 0; j < conceptsList.size(); j++) {
            Concept concept = new Concept();
            HL7ConceptContainer conceptContainer = (HL7ConceptContainer) conceptsList.get(new Integer(j));
            String uniqueInternalId = conceptContainer.getInternalId();
            concept.setEntityCode(conceptContainer.getConceptCode());
            loadPresentation(csclass, concept, uniqueInternalId, connectionString, driver);
            loadConceptProperties(csclass, concept, uniqueInternalId, connectionString, driver);
            loadRelations(csclass, concept, uniqueInternalId, parent_assoc, relations, connectionString, driver,
                    internalIdConceptCodeMap);
            concepts.addEntity(concept);
            num++;
            int out = num % 1000;
            if (out == 0)
            messages_.info("Processed " + num + " concepts: ");
        }
        csclass.setApproxNumConcepts(new Long(concepts.getEntity().length));
        messages_.info("Concepts added=" + num);

    }

    void loadArtificialTopNodes(CodingScheme csclass, Entities concepts, AssociationPredicate parent_assoc, Connection c) {
        messages_.info("Processing code systems into top nodes");
        ResultSet isTopNode = null;
        try {
            // Create an "@" top node.
            Concept rootNode = new Concept();

            // Create and set the concept code for "@"
            String topNodeDesignation = "@";
            rootNode.setEntityCode(topNodeDesignation);
            rootNode.setIsAnonymous(Boolean.TRUE);
            EntityDescription enDesc = new EntityDescription();
            enDesc.setContent("Root node for subclass relations.");
            rootNode.setEntityDescription(enDesc);
            concepts.addEntity(rootNode);

            AssociationSource ai = new AssociationSource();
            ai.setSourceEntityCode(rootNode.getEntityCode());
            ai = RelationsUtil.subsume(parent_assoc, ai);
            
            // Get all the code systems except the
            // legacy code system coding scheme in HL7
            PreparedStatement getArtificialTopNodeData = c
                    .prepareStatement("SELECT * FROM VCS_code_system where codeSystemId <> ?");
            getArtificialTopNodeData.setString(1, HL72EMFConstants.CODE_SYSTEM_OID);
            ResultSet dataResults = getArtificialTopNodeData.executeQuery();
            while (dataResults.next()) {
                Entity topNode = new Entity();

                String nodeName = dataResults.getString("codeSystemName");
                String entityDescription = dataResults.getString("fullName");
                String oid = dataResults.getString("codeSystemId");
                String type = dataResults.getString("codeSystemType");
                String def = dataResults.getString("description");
                String releaseId = dataResults.getString("releaseId");
                if (def == null) {
                    def = SQLTableConstants.TBLCOLVAL_MISSING;
                }
                // The description contains HTML tags. We try to remove
                // them.
                else {
                    int begin = def.lastIndexOf("<p>");
                    int end = def.lastIndexOf("</p>");
                    if (begin > -1) {
                        if (begin + 3 < end)
                            def = def.substring(begin + 3, end);
                    }
                }

                // Does it have a code system counterpart?
                // if so get its unique identifier
                PreparedStatement getArtificialTopNodeCode = c
                        .prepareStatement("SELECT DISTINCT (internalId)FROM VCS_concept_code_xref "
                                + "WHERE codeSystemId2 = ? AND conceptCode2 = ?");
                getArtificialTopNodeCode.setString(1, HL72EMFConstants.CODE_SYSTEM_OID);
                getArtificialTopNodeCode.setString(2, nodeName);
                ResultSet codeResults = getArtificialTopNodeCode.executeQuery();
                if (codeResults.next()) {
                    topNode.setEntityCode(codeResults.getString("internalId") + ":" + nodeName);
                } else {
                    topNode.setEntityCode(nodeName + ":" + nodeName);
                }
                if (getArtificialTopNodeCode != null)
                    getArtificialTopNodeCode.close();

                EntityDescription enD = new EntityDescription();
                enD.setContent(entityDescription);
                topNode.setEntityDescription(enD);
                topNode.setIsActive(true);

                // a property example for some of the values we may want to
                // bring into
                // the code system entity. (currently loaded as metadata.)

                // Property property =
                // CommontypesFactory.eINSTANCE.createProperty();
                // Text proptxt = CommontypesFactory.eINSTANCE.createText();
                // proptxt.setValue((String) entityDescription);
                // property.setValue(proptxt);
                // property.setPropertyType(HL72EMFConstants.PROPERTY_TYPE_GENERIC);
                // property.setPropertyName(HL72EMFConstants.PROPERTY_NAME_OID);
                // property.setPropertyId("P1");
                // property.setLanguage(HL72EMFConstants.DEFAULT_LANGUAGE_EN);
                // topNode.getProperty().add(property);

                // Set presentation so it's a full fledged concept
                Presentation p = new Presentation();
                Text txt = new Text();
                txt.setContent((String) entityDescription);
                p.setValue(txt);
                p.setIsPreferred(Boolean.TRUE);
                p.setPropertyName(HL72EMFConstants.PROPERTY_PRINTNAME);
                p.setPropertyId("T1");
                p.setLanguage(HL72EMFConstants.DEFAULT_LANGUAGE_EN);
                topNode.addPresentation(p);

                // Set definition
                if (def != null) {
                    Definition definition = new Definition();
                    Text defText = new Text();
                    defText.setContent(def);
                    definition.setValue(defText);
                    definition.setPropertyName(HL72EMFConstants.PROPERTY_DEFINITION);
                    definition.setPropertyId("D1");
                    definition.setIsActive(Boolean.TRUE);
                    definition.setIsPreferred(Boolean.TRUE);
                    definition.setLanguage(HL72EMFConstants.DEFAULT_LANGUAGE_EN);
                    topNode.addDefinition(definition);
                }

                topNode.addEntityType("Coding Scheme");
                concepts.addEntity(topNode);

                // This coding scheme is attached to an artificial root.
                AssociationTarget at = new AssociationTarget();
                at.setTargetEntityCode(topNode.getEntityCode());
                RelationsUtil.subsume(ai, at);

                // Now find the top nodes of the scheme and subsume them to this
                // scheme's artificial top node.
                // First get a list of all nodes in the scheme.
                // but again exclude the code system nodes.
                ArrayList topNodes = new ArrayList();
                PreparedStatement getSystemCodes = c
                        .prepareStatement("SELECT DISTINCT (internalId), conceptCode2 FROM VCS_concept_code_xref WHERE codeSystemId2 =? AND codeSystemId2 <> ?");
                getSystemCodes.setString(1, oid);
                getSystemCodes.setString(2, HL72EMFConstants.CODE_SYSTEM_OID);
                ResultSet systemCodes = getSystemCodes.executeQuery();
                while (systemCodes.next()) {
                    String testCode = systemCodes.getString("internalId");
                    if (testCode != null)
                        topNodes.add(testCode);
                }

                if (getSystemCodes != null)
                    getSystemCodes.close();

                // Drop any from the list that aren't top nodes.
                ArrayList nodesToRemove = new ArrayList();
                PreparedStatement checkForTopNode = null;
                for (int i = 0; i < topNodes.size(); i++) {
                    checkForTopNode = c
                            .prepareStatement("SELECT targetInternalId FROM VCS_concept_relationship WHERE targetInternalId =?");
                    checkForTopNode.setString(1, (String) topNodes.get(i));
                    isTopNode = checkForTopNode.executeQuery();
                    if (isTopNode.next()) {
                        if (topNodes.get(i).equals(isTopNode.getString(1))) {
                            nodesToRemove.add(topNodes.get(i));
                        }
                    }
                }
                if (checkForTopNode != null)
                    checkForTopNode.close();

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
                }
                if (getconceptSuffix != null)
                    getconceptSuffix.close();

                // For each top node subsume to the current artificial node for
                // the scheme.
                for (int j = 0; j < topNodes.size(); j++) {
                    try {
                        AssociationSource atn = new AssociationSource();
                        atn.setSourceEntityCode(topNode.getEntityCode());
                        atn = RelationsUtil.subsume(parent_assoc, atn);

                        AssociationTarget atopNode = new AssociationTarget();
                        atopNode.setTargetEntityCode((String) topNodes.get(j));
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

    void loadPresentation(CodingScheme csclass, Concept concept, Connection c, String uniqueInternalId) {

        ResultSet edResults = null;
        ResultSet desResults = null;
        try {
            // Pull source code system name and designation
            PreparedStatement getEntityDescription = c
                    .prepareStatement("SELECT DISTINCT b.codeSystemId2, a.designation, "
                            + "a.language,a.preferredForLanguage, c.codeSystemName "
                            + " FROM VCS_concept_designation AS a, VCS_concept_code_xref AS b, "
                            + "VCS_code_system AS c " + "WHERE ((a.internalId = ?) AND "
                            + "(a.internalId=b.internalId) AND (c.codeSystemid=b.codeSystemId2))");

            getEntityDescription.setString(1, uniqueInternalId);
            edResults = getEntityDescription.executeQuery();

            String entityDescription;
            String sourceCodeSystemName;
            String conceptCodeFull;
            String conceptCode;

            // Determine the source coding scheme concept code
            conceptCodeFull = concept.getEntityCode();
            int colonPosition = conceptCodeFull.lastIndexOf(":");
            conceptCode = conceptCodeFull.substring(colonPosition + 1);

            int presentationCount = 1;
            if (edResults.next()) {
                do {

                    sourceCodeSystemName = edResults.getString("codeSystemName");
                    entityDescription = edResults.getString("designation");

                    Presentation p = new Presentation();
                    Text txt = new Text();
                    txt.setContent((String) entityDescription);
                    p.setValue(txt);

                    if (edResults.getString("preferredForLanguage").equals("1")) {
                        p.setIsPreferred(Boolean.TRUE);
                        EntityDescription ed = new EntityDescription();
                        ed.setContent(entityDescription);
                        concept.setEntityDescription(ed);
                    } else { // Designation is not preferred for language, set
                             // to false
                        p.setIsPreferred(Boolean.FALSE);
                    }
                    p.setPropertyName(HL72EMFConstants.PROPERTY_PRINTNAME);
                    p.setPropertyId("T" + presentationCount);
                    p.setLanguage(edResults.getString("language"));

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
                    presentationCount++;

                } while (edResults.next());
            } else { // There are no designations, so specify they are missing
                Presentation p = new Presentation();
                p.setPropertyName(HL72EMFConstants.PROPERTY_PRINTNAME);
                p.setPropertyId("T" + presentationCount);
                p.setLanguage(HL72EMFConstants.DEFAULT_LANGUAGE_EN);
                concept.addPresentation(p);
                entityDescription = HL72EMFConstants.MISSING;
                Text txt = new Text();
                txt.setContent((String) entityDescription);
                p.setValue(txt);
                p.setIsPreferred(Boolean.TRUE);
                EntityDescription ed = new EntityDescription();
                ed.setContent(entityDescription);
                concept.setEntityDescription(ed);

            }
           

          //  Presentation pd = ConceptsFactory.eINSTANCE.createPresentation();
            Definition des = new Definition();
            PreparedStatement getDescriptions = c
                    .prepareStatement("SELECT description, language FROM VCS_concept_description WHERE internalId = ?");
            getDescriptions.setString(1, uniqueInternalId);
            desResults = getDescriptions.executeQuery();

            int definitionCount = 1;
            String description = null;

            if (desResults.next()) {
                // removes HTML tags from description text.
                description = desResults.getString("description");
                description = description.replaceAll("</?[A-Z]+\\b[^>]*>", "");
                description = description.replaceAll("</?[a-z]+\\b[^>]*>", "");
                if (StringUtils.isBlank(description)) {
                    description = HL72EMFConstants.MISSING;
                    messages_.info("Found an empty description on Concept " + conceptCodeFull);
                }

                Text txt = new Text();
                txt.setContent((String) description);
                des.setValue(txt);
                des.setIsPreferred(Boolean.TRUE);
                des.setPropertyName(HL72EMFConstants.PROPERTY_DEFINITION);
                des.setLanguage(desResults.getString("language"));

                des.setPropertyId("D" + definitionCount);
            }

            else {
                Text txt = new Text();
                txt.setContent((String) HL72EMFConstants.MISSING);
                des.setValue(txt);
                des.setIsPreferred(Boolean.TRUE);
                des.setPropertyName(HL72EMFConstants.PROPERTY_DEFINITION);
                des.setLanguage(HL72EMFConstants.DEFAULT_LANGUAGE_EN);

                des.setPropertyId("D" + definitionCount);

            }
            concept.addDefinition(des);
      

        } catch (Exception e) {
            e.printStackTrace();
            messages_.error("Failed to load HL7 Concept presentation", e);

        }
        finally{
            try {
                edResults.close();
                desResults.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
     
        }

    }

    void loadPresentation(CodingScheme csclass, Concept concept, String uniqueInternalId, String connectionString,
            String driver) {
        Connection c = null;
        try {
            c = DBUtility.connectToDatabase(accessConnectionString, driver, null, null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        loadPresentation(csclass, concept, c, uniqueInternalId);
        try {
            c.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    void loadConceptProperties(CodingScheme csclass, Concept concept, Connection c, String uniqueInternalId) {
        ResultSet properties = null;
        try {

            // TODO load this up front with the Relations We may need to do the
            // same with the presentation.
            PreparedStatement getProperties = c
                    .prepareStatement("SELECT propertyCode, propertyValue, language FROM VCS_concept_property WHERE internalId = ?");
            getProperties.setString(1, uniqueInternalId);
           properties = getProperties.executeQuery();

            while (properties.next()) {
                Property cp = new Property();
                String propertyLanguage = properties.getString("language");
                if (propertyLanguage == null || propertyLanguage.length() < 1) {
                    cp.setLanguage(HL72EMFConstants.DEFAULT_LANGUAGE_EN);
                } else {
                    cp.setLanguage(propertyLanguage);
                }
                String property = properties.getString("propertyCode");
                cp.setPropertyName(property);
                cp.setPropertyId("P" + concept.getProperty().length);
                Text txt = new Text();
                txt.setContent((String) properties.getString("propertyValue"));
                cp.setValue(txt);
                concept.addProperty(cp);
            }
         

        } catch (Exception e) {
            messages_.error("Problem processing concept properties", e);
            e.printStackTrace();
        }
        finally{
            try {
                properties.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    void loadConceptProperties(CodingScheme csclass, Concept concept, String uniqueInternalId, String connectionString,
            String driver) {
        Connection c = null;
        try {
            c = DBUtility.connectToDatabase(accessConnectionString, driver, null, null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        loadConceptProperties(csclass, concept, c, uniqueInternalId);
        try {
            c.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    void loadRelations(CodingScheme csclass, Concept concept, Connection c, String uniqueInternalId,
            AssociationPredicate parent_assoc, Relations relations, Hashtable concept2Id) {
        ResultSet associations = null;
        Hashtable associationsList = new Hashtable();
        try {
            PreparedStatement getSourceAssociations = c
                    .prepareStatement("SELECT relationCode, sourceInternalId, targetInternalId FROM VCS_concept_relationship where sourceInternalId = ?");
            getSourceAssociations.setString(1, uniqueInternalId);
           associations = getSourceAssociations.executeQuery();
            int i = 0;
            while (associations.next()) {
                associationsList.put(new Integer(i), new HL7AssocContainer(associations.getString("relationCode"),
                        associations.getInt("sourceInternalId"), associations.getInt("targetInternalId")));
                i++;
            }
          
        } catch (Exception e) {
            messages_.error("Failed while getting HL7 relations from RIM database", e);
            e.printStackTrace();
        }finally{
            try {
                associations.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        for (int j = 0; j < associationsList.size(); j++) {
            HL7AssocContainer assoContainer = (HL7AssocContainer) associationsList.get(new Integer(j));
            int sourceCode = assoContainer.getSourceCode();
            int targetCode = assoContainer.getTargetCode();
            AssociationPredicate parent_association = (AssociationPredicate) RelationsUtil.resolveAssociations(csclass,
                    assoContainer.getAssociation()).get(0);

            // TODO enclose entire association setup in some kind of try catch.
            try {
                AssociationSource ai = new AssociationSource();
                ai.setSourceEntityCode((String) concept2Id.get(new Integer(sourceCode)));
                ai = RelationsUtil.subsume(parent_association, ai);

                AssociationTarget at = new AssociationTarget();
                at.setTargetEntityCode((String) concept2Id.get(new Integer(targetCode)));
                RelationsUtil.subsume(ai, at);
            } catch (Exception e) {
                messages_.error("Failed while processing HL7 association hierarchy", e);
                e.printStackTrace();
            }

        }
    }

    void loadRelations(CodingScheme csclass, Concept concept, String uniqueInternalId, AssociationPredicate association,
            Relations relations, String connectionString, String driver, Hashtable concept2Id) {
        Connection c = null;
        try {
            c = DBUtility.connectToDatabase(accessConnectionString, driver, null, null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        loadRelations(csclass, concept, c, uniqueInternalId, association, relations, concept2Id);
        try {
            c.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    void loadMetaData(CodingScheme csclass, String connectionString, String driver) {

        messages_.info("Loading individual coding scheme metadata");
        Connection c = null;

        // create the filename of the metadata file to be created
        String fileName = PreferenceLoaderConstants.META_HL7_METADATA_FILE_NAME;

        // String filename = getDBLocation() + "_metadata.xml";
        String filename = loaderPrefs.getXMLMetadataFilePath() + "/" + fileName;

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
            String dataMissing = SQLTableConstants.TBLCOLVAL_MISSING;
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

                Hashtable conceptCountList = new Hashtable();

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
                    else{
                    int begin = description.lastIndexOf("<p>");
                    int end = description.lastIndexOf("</p>");
                    if (begin > -1) {
                        if(begin + 3 < end)
                        description = description.substring(begin + 3, end);
                    }
                    }
                    // description =
                    // description.replaceAll("</?[A-Z]+\\b[^>]*>", ""); // CRS
                    // description =
                    // description.replaceAll("</?[a-z]+\\b[^>]*>", ""); // CRS
                    // description = description.replaceAll("&#xd;", "");

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
                    atts.addAttribute("", "", SQLTableConstants.TBLCOL_CODINGSCHEMENAME, "CDATA", codeSystemName); // May
                                                                                                               // want
                                                                                                               // to
                                                                                                               // change
                                                                                                               // to
                                                                                                               // _fullName
                    atts.addAttribute("", "", SQLTableConstants.TBLCOL_FORMALNAME, "CDATA", fullName);
                    // atts.addAttribute("","",SQLTableConstants.TBLCOL_REGISTEREDNAME,"CDATA",
                    // codeSystemId);
                    atts.addAttribute("", "", SQLTableConstants.TBLCOL_CODINGSCHEMEURI, "CDATA", codeSystemId);
                    atts.addAttribute("", "", SQLTableConstants.TBLCOL_DEFAULTLANGUAGE, "CDATA", defaultLanguage);
                    atts.addAttribute("", "", SQLTableConstants.TBLCOL_REPRESENTSVERSION, "CDATA", releaseId);
                    atts.addAttribute("", "", SQLTableConstants.TBLCOL_ISNATIVE, "CDATA", isNative);
                    atts.addAttribute("", "", SQLTableConstants.TBLCOL_APPROXNUMCONCEPTS, "CDATA",
                            approximateNumberofConcepts.toString());
                    // atts.addAttribute("","",SQLTableConstants.TBLCOL_FIRSTRELEASE,"CDATA",dataMissing);
                    // atts.addAttribute("","",SQLTableConstants.TBLCOL_MODIFIEDINRELEASE,"CDATA",dataMissing);
                    // atts.addAttribute("","",SQLTableConstants.TBLCOL_DEPRECATED,"CDATA",
                    // dataMissing);
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
                    // TODO drop stack trace.
                    e.printStackTrace();
                }
            }

            // this is the file not found exception
        } catch (FileNotFoundException e1) {
            // TODO drop stack trace.
            e1.printStackTrace();
        } catch (IOException e) {
            // TODO drop stack trace.
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO drop stack trace.
            e.printStackTrace();
        }

    }

    private String getDBLocation() {

        int dbqLocation = accessConnectionString.lastIndexOf("DBQ");
        String dbFullFilename = accessConnectionString.substring(dbqLocation + 4);
        int sep = dbFullFilename.lastIndexOf(".");
        // strip off the filename extension
        String dbfullPath = dbFullFilename.substring(0, sep);

        return dbfullPath;
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