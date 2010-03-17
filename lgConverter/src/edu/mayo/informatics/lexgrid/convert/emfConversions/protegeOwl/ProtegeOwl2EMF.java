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
 *      http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package edu.mayo.informatics.lexgrid.convert.emfConversions.protegeOwl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URI;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.LexOnt.CodingSchemeManifest;
import org.LexGrid.LexOnt.CsmfCodingSchemeName;
import org.LexGrid.LexOnt.CsmfCodingSchemeURI;
import org.LexGrid.LexOnt.CsmfMappings;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.types.EntityTypes;
import org.LexGrid.concepts.Concept;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Instance;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.emf.base.util.LgModelUtil;
import org.LexGrid.emf.commonTypes.CommontypesPackage;
import org.LexGrid.emf.concepts.ConceptsPackage;
import org.LexGrid.emf.concepts.util.EntitiesUtil;
import org.LexGrid.custom.relations.RelationsUtil;
import org.LexGrid.naming.Mappings;
import org.LexGrid.relations.AssociationData;
import org.LexGrid.relations.AssociationEntity;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.LexGrid.util.SimpleMemUsageReporter;
import org.LexGrid.util.SimpleMemUsageReporter.Snapshot;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.util.sql.lgTables.SQLTableUtilities;
import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.EClass;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexevs.locator.LexEvsServiceLocator;

import edu.mayo.informatics.lexgrid.convert.emfConversions.EMFSupportedMappings;
import edu.mayo.informatics.lexgrid.convert.exceptions.LgConvertException;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.storage.database.DatabaseKnowledgeBaseFactory;
import edu.stanford.smi.protege.util.PropertyList;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.database.CreateOWLDatabaseFromFileProjectPlugin;
import edu.stanford.smi.protegex.owl.database.OWLDatabaseKnowledgeBaseFactory;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.NamespaceManager;
import edu.stanford.smi.protegex.owl.model.OWLAllDifferent;
import edu.stanford.smi.protegex.owl.model.OWLCardinalityBase;
import edu.stanford.smi.protegex.owl.model.OWLClass;
import edu.stanford.smi.protegex.owl.model.OWLComplementClass;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLEnumeratedClass;
import edu.stanford.smi.protegex.owl.model.OWLHasValue;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNAryLogicalClass;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLNames;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLQuantifierRestriction;
import edu.stanford.smi.protegex.owl.model.OWLRestriction;
import edu.stanford.smi.protegex.owl.model.OWLUnionClass;
import edu.stanford.smi.protegex.owl.model.RDFIndividual;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.RDFSDatatype;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFSNames;

/**
 * This is the main class containing the logic for the conversion from OWL to
 * EMF..
 * 
 * 
 * Last modified on: May 30, 2008 9:52:43 AM
 * 
 * @author Jyotishman Pathak (pathak.jyotishman@mayo.edu)
 * @author Pradip Kanjamala (kanjamala.pradip@mayo.edu)
 * 
 *         For NCI, they want to see the OWL Object Properties that are also
 *         annotation properties loaded as Associations in the association
 *         container
 */
public class ProtegeOwl2EMF {
    /* Define some global variables */
    // Input & output ...
    private URI owlURI_ = null;
    private OWLModel owlModel_ = null;
    private LoaderPreferences loadPrefs_ = null;
    private CodingSchemeManifest manifest_ = null;
    private LgMessageDirectorIF messages_ = null;

    private Connection sqlConnection_ = null;
    private SQLTableUtilities sqlTableUtil_ = null;
    private String dbType_ = null;
    private String dbDriver_ = null, dbUrl_ = null, dbProtegeTempTable_ = null, dbUser_ = null, dbPassword_ = null;
    private StringBuffer whereClause_ = null;

    // Memory profile option ...
    private int memoryProfile_ = ProtegeOwl2EMFConstants.MEMOPT_LEXGRID_DIRECT_DB;

    // Generated EMF objects ...
    private CodingScheme emfScheme_ = null;
    private EMFSupportedMappings emfSupportedMappings_ = null;

    private Relations emfRelationsContainer_Assoc = null;
    private Relations emfRelationsContainer_Roles = null;

    private CodingScheme tempEmfScheme_ = null;
    private Entities tempEmfEntityList_ = null;

    // Shared mapping information ...
    private Map<String, Object> attributeMap_ = null;
    private Map<String, String> owlDatatypeName2label_ = null;
    private Map<String, EClass> owlDatatypeName2emfPropClass_ = null;
    private Map<String, String> owlDatatypeName2emfDatatype_ = null;
    private Map<String, String> owlClassName2Conceptcode_ = new HashMap<String, String>();
    private Map<String, String> entityCode2NameSpace_ = new HashMap<String, String>();
    private Map<String, AssociationSource> emfAssocToAssocSrc_ = new HashMap<String, AssociationSource>();
    private Map<String, String> owlInstanceName2code_ = null;

    // OWL type references
    private RDFResource annotationType_ = null;

    // Cached values and state
    private int conceptCount_ = 0;
    AssociationManager assocManager = null;
    PreferenceManager prefManager = null;
    PropertyComparator propertyComparator;

    // Complex property parser
    private BasicXMLParser bxp;
    
    private DatabaseServiceManager databaseServiceManager;

    /**
     * Create a new instance for conversion.
     * 
     * @param owlURI
     *            The OWL input file.
     * @param manifest
     *            The OWL manifest Object
     * @param messages
     *            Responsible for handling display of program messages to the
     *            user.
     * @param dbDriver
     *            If specified, enables database on Protege load (reduced memory
     *            footprint).
     * @param dbUrl
     *            If specified, enables database on Protege load (reduced memory
     *            footprint).
     * @param dbTable
     *            If specified, enables database on Protege load (reduced memory
     *            footprint).
     * @param dbUser
     *            If specified, enables database on Protege load (reduced memory
     *            footprint).
     * @param dbPassword
     *            If specified, enables database on Protege load (reduced memory
     *            footprint).
     */
    public ProtegeOwl2EMF(URI owlURI, CodingSchemeManifest manifest, LoaderPreferences loadPrefs, int memorySafe,
            LgMessageDirectorIF messages) {
        super();
        owlURI_ = owlURI;
        messages_ = messages;
        this.manifest_ = manifest;
        this.memoryProfile_ = memorySafe;
        this.loadPrefs_ = loadPrefs;
        bxp = new BasicXMLParser();
        databaseServiceManager = LexEvsServiceLocator.getInstance().getDatabaseServiceManager();
    }

    /**
     * Runs the conversion.
     * 
     * @return EMF representation of the coding scheme.
     * @throws LgConvertException
     *             If an error occurs in processing.
     */
    public CodingScheme run() throws LgConvertException {
        // Honor preferences, if provided
        prefManager = new PreferenceManager(loadPrefs_);
        // Set the property comparator
        propertyComparator = new PropertyComparator(prefManager);

        // Load the OWL Java model from source
        initOWLModelFromSource();

        // Create the EMF model
        try {
            // Disable unnecessary notifications
            LgModelUtil.setNotifyRequired(false);

            // Initialize structures referenced during conversion ...
            initSupportedMappings();
            initScheme();
            initSupportedDatatypes();
            assocManager = new AssociationManager(emfSupportedMappings_, emfRelationsContainer_Assoc,
                    emfRelationsContainer_Roles);
            initSupportedDatatypeProperties();
            initSupportedObjectProperties();

            // If we are streaming the LexGrid model to database, write
            // the coding scheme metadata as defined so far.
            if (memoryProfile_ != ProtegeOwl2EMFConstants.MEMOPT_ALL_IN_MEMORY) {
                databaseServiceManager.getCodingSchemeService().insertCodingScheme(emfScheme_);
            }

            // Populate the EMF coding scheme from the OWL model
            initSubtypeRoot();
            processOWL();

            // Apply all supported attributes that have been registered
            // over the course of processing the OWL model ...
            if (emfSupportedMappings_.getSupportedAssociations().size() > 0) {
                String name = EntityTypes.ASSOCIATION.toString();
                
                emfSupportedMappings_.registerSupportedEntityType(name, null, name, false);
            }
            emfSupportedMappings_.applyToCodingScheme(emfScheme_);
            
            if (memoryProfile_ != ProtegeOwl2EMFConstants.MEMOPT_ALL_IN_MEMORY) {
                final String uri = emfScheme_.getCodingSchemeURI();
                final String version = emfScheme_.getRepresentsVersion();
                
                databaseServiceManager.getDaoCallbackService().executeInDaoLayer(new DaoCallback<Object>() {

                    public Object execute(DaoManager daoManager) {
                        String codingSchemeId = 
                            daoManager.getCodingSchemeDao(uri, version).getCodingSchemeIdByUriAndVersion(uri, version);
                        
                        daoManager.getCodingSchemeDao(uri, version).insertMappings(codingSchemeId, emfScheme_.getMappings());
                    }   
                });
            }

            // Register the number of concepts found and return the scheme
            updateApproximateConceptNumber();
            return emfScheme_;
        } catch (Exception e) {
            throw new LgConvertException(e);
        } finally {
            // Restore state and cleanup resources ...
            LgModelUtil.setNotifyRequired(true);
            if (owlModel_ != null) {
                owlModel_.flushCache();
                owlModel_.getJenaModel().close();
            }
        }
    }

    // ////////////////////////////////////////////////
    // //////////// CORE METHODS /////////////////////
    // //////////////////////////////////////////////

    /**
     * Create and populate the EMF representation of the coding scheme.
     * 
     * @throws LgConvertException
     *             If an error occurs in processing.
     */
    protected void processOWL() throws LgConvertException {

        messages_.info("Before OWL Processing");
        Snapshot snap = SimpleMemUsageReporter.snapshot();
        messages_.info("Read Time : " + SimpleMemUsageReporter.formatTimeDiff(snap.getTimeDelta(null))
                + " Heap Usage: " + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsage()) + " Heap Delta:"
                + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsageDelta(null)));

        // Step 1:
        // Iterate through object classes and generate EMF concepts and
        // associations ...
        // Resolve all concepts before working on relations, since we need to
        // ensure all concept codes are correctly resolved (sometimes buried
        // in class definition) before setting source and target references.

        messages_.info("Processing OWL Classes.....");
        processConcepts(snap);

        // Step 2: Process the OWL Object properties. Essentially, determine
        // the domain and ranges for the properties and relationships to other
        // properties.

        messages_.info("Processing OWL Object Properties.....");
        processOWLObjectProperties(snap);

        // Step 3: Process the OWL Datatype properties. Essentially, determine
        // the domain and data ranges for the properties and relationships to
        // other properties.

        messages_.info("Processing OWL Datatype Properties.....");
        processOWLDatatypeProperties(snap);

        // Step 4: Process OWL individuals. Essentially, determine to which
        // classes these instances belong to, as well as, relations between
        // the individuals themselves (e.g., differentFrom)?

        messages_.info("Processing OWL Individuals.....");
        processInstances(snap);
    }

    /**
     * This method is responsible for processing of all the OWL concepts.
     * 
     */
    protected void processConcepts(Snapshot snap) {
        int count = 0;

        // The idea is to iterate through all the OWL classes, and register them
        // as well as find out any associations or restrictions they have to
        // other OWL classes (including anonymous ones).
        messages_.info("Processing concepts: ");
        for (Iterator namedClasses = owlModel_.getUserDefinedRDFSNamedClasses().iterator(); namedClasses.hasNext();) {
            RDFSNamedClass namedClass = (RDFSNamedClass) namedClasses.next();
            resolveConcept(namedClass);
            count++;
            if (count % 5000 == 0) {
                messages_.info("OWL classes processed: " + count);
            }
        }
        messages_.info("Total OWL classes processed: " + count);
        // Now, process all the relationships/associations the
        // concept has with other concepts. Also, process all
        // the restrictions the concept has.
        messages_.info("Concepts converted to EMF");
        snap = SimpleMemUsageReporter.snapshot();
        messages_.info("Read Time : " + SimpleMemUsageReporter.formatTimeDiff(snap.getTimeDelta(null))
                + " Heap Usage: " + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsage()) + " Heap Delta:"
                + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsageDelta(null)));

        messages_.info("Processing concept relationships ...");

        for (Iterator namedClasses = owlModel_.getUserDefinedRDFSNamedClasses().iterator(); namedClasses.hasNext();) {
            RDFSNamedClass namedClass = (RDFSNamedClass) namedClasses.next();
            String emfConceptCode = resolveConceptID(namedClass);
            if (emfConceptCode != null) {
                AssociationSource source = CreateUtils.createAssociationSource(emfConceptCode, entityCode2NameSpace_
                        .get(emfConceptCode));
                resolveEquivalentClassRelations(source, namedClass);
                resolveSubClassOfRelations(source, namedClass);
                resolveDisjointWithRelations(source, namedClass);
                resolveComplementOfRelations(source, namedClass);
                resolveOWLObjectPropertyRelations(source, namedClass);
            }
        }

        // If we found at least one, register the supported entity type.
        if (count > 0) {
            String name = EntityTypes.CONCEPT.name();
            emfSupportedMappings_.registerSupportedEntityType(name, null, name, false);
        }
    }

    /**
     * Process the instance information in the ontology.
     * 
     */
    protected void processInstances(Snapshot snap) {
        snap = SimpleMemUsageReporter.snapshot();
        messages_.info("Read Time : " + SimpleMemUsageReporter.formatTimeDiff(snap.getTimeDelta(null))
                + " Heap Usage: " + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsage()) + " Heap Delta:"
                + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsageDelta(null)));
        messages_.info("Processing OWL Individuals ...");

        int count = 0;
        owlInstanceName2code_ = new HashMap();

        // The idea is to iterate through all the OWL individuals, and register
        // them
        // as well as find out additional associations (e.g,. From)
        for (Iterator individuals = owlModel_.getOWLIndividuals().iterator(); individuals.hasNext();) {
            OWLIndividual individual = (OWLIndividual) individuals.next();            
            Instance emfInstance = resolveIndividual(individual);
            if (emfInstance != null) {
                addEntity(emfInstance);
            }
            if (count % 1000 == 0)
                messages_.info("OWL individuals processed: " + count);
            count++;
        }
        messages_.info("Total OWL individuals processed: " + count);
        // Now, process all the relationships/associations the
        // concept has with other concepts. Also, process all
        // the restrictions the concept has.
        messages_.info("Instances converted to EMF");
        snap = SimpleMemUsageReporter.snapshot();
        messages_.info("Read Time : " + SimpleMemUsageReporter.formatTimeDiff(snap.getTimeDelta(null))
                + " Heap Usage: " + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsage()) + " Heap Delta:"
                + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsageDelta(null)));

        // Pradip: This is not needed anymore...replaced with resolveRdfTypeRelations. We decided
        // to stop using the manufactured relation instanceOf and substitute that with rdf:type
        // Step 1: once the instances have been registered, associate them to
        // specific classes.
//        for (Iterator namedClasses = owlModel_.getUserDefinedOWLNamedClasses().iterator(); namedClasses.hasNext();) {
//            RDFSClass namedClass = (RDFSClass) namedClasses.next();
//
//            String sourceCode = resolveConceptID(namedClass);
//            String nameSpace = getNameSpace(namedClass.getNamespace());
//
//            AssociationSource source = EMFCreateUtils.createAssociationSource(sourceCode, nameSpace);
//
//            for (Iterator instances = namedClass.getInstances(false).iterator(); instances.hasNext();) {
//                RDFResource instance = (RDFResource) instances.next();
//                relateAssocSourceWithRDFResourceTarget(EntityTypes.INSTANCE_LITERAL, assocManager.getRdfType(),
//                        source, instance);
//            }
//
//        }

        // Step 2: for every individual, we also need to have associations
        // with other individuals depending on the definition of the class.
        // Here is an example class:
        // <owl:Class rdf:ID="Country">
        // <owl:DatatypeProperty rdf:ID="hasShortName">
        // <rdfs:domain rdf:resource="#Country"/>
        // <rdfs:range rdf:resource="http://www.w3.org/2001/XMLSchema#string"/>
        // </owl:DatatypeProperty>
        // This is the instance:
        // <Country rdf:ID="America">
        // <hasShortName xml:lang="en">USA</hasShortName>
        // </Country>
        // Thus, we have to associate "America" to "USA" via the association
        // "hasShortName".

        /**
         * UPDATED 05/28/2008: Check the "resolveIndividualProperties" method
         * for this. We will handle them as individualProperties as opposed to
         * associations.
         */
        /** Updated 02/25/2010: Replaced "resolveIndividualProperties" method
         * with resolveEntityProperties.
         */

        // Process the associations (e.g.,
        // rdf:type, DifferentFrom, SameAs, ObjectProperties)
        for (Iterator individuals = owlModel_.getOWLIndividuals().iterator(); individuals.hasNext();) {
            OWLIndividual individual = (OWLIndividual) individuals.next();
            String nameSpace = getNameSpace(individual.getNamespace());
            String emfCode = resolveInstanceID(individual);
            if (emfCode != null) {
                AssociationSource source = CreateUtils.createAssociationSource(emfCode,
                        nameSpace);
                resolveRdfTypeRelations(source, individual);
                resolveDifferentFromRelations(source, individual);
                resolveSameAsRelations(source, individual);
                resolveOWLObjectPropertyRelations(source, individual);
            }
        }

        // Updated on 07/29/2008:
        // Process OWL:AllDifferent, which is not the same
        // as processing OWL:differentFrom. We need a separate logic
        // for this (e.g., Pizza ontology uses OWL:AllDifferent). Since
        // we need to create an association. Cui Tao suggested that we
        // stick in the browser text, and she will do custom processing. So,
        // the AssociationSource will be the browser text, and the
        // associationTarget will be null.
        for (Iterator diffIndi = owlModel_.getOWLAllDifferents().iterator(); diffIndi.hasNext();) {
            OWLAllDifferent allDifferent = (OWLAllDifferent) diffIndi.next();
            String nameSpace = getNameSpace(allDifferent.getNamespace());
            AssociationSource source = CreateUtils.createAssociationSource(allDifferent.getBrowserText(), nameSpace);
            String nameSpaceTarget = getNameSpace(allDifferent.getNamespace());
            AssociationTarget target = CreateUtils.createAssociationTarget(allDifferent.getBrowserText(),
                    nameSpaceTarget);
            relateAssociationSourceTarget(assocManager.getAllDifferent(), source, target);
        }

        // If we found at least one, register the supported entity type.
        if (!owlInstanceName2code_.isEmpty()) {
            String name = EntityTypes.INSTANCE.toString();
            emfSupportedMappings_.registerSupportedEntityType(name, null, name, false);
        }
    } // end of the method.

    /**
     * This method determines the domain and ranges for the OWL Object
     * properties. It also processes different relationships between the
     * properties.
     * 
     */
    protected void processOWLObjectProperties(Snapshot snap) {
        snap = SimpleMemUsageReporter.snapshot();
        messages_.info("Read Time : " + SimpleMemUsageReporter.formatTimeDiff(snap.getTimeDelta(null))
                + " Heap Usage: " + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsage()) + " Heap Delta:"
                + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsageDelta(null)));
        messages_.info("Processing OWL Object Properties ...");

        for (Iterator props = owlModel_.getUserDefinedOWLObjectProperties().iterator(); props.hasNext();) {
            RDFProperty prop = (RDFProperty) props.next();
            // Modified on 01/19/2009 (Jyoti): I think this is what Satya meant
            // when saying that creating Concepts for ObjectProperties should be
            // handled
            // later in the code(Refer to his email dated: 01/14/2009).
            if (prefManager.isProcessConceptsForObjectProperties()) {
                Concept concept = resolveConcept(prop);
            }

            // ///////////////////////////////////
            // /// Process Domain and Ranges /////
            // //////////////////////////////////

            // Get the appropriate association name initialized earlier
            // (initSupportedObjectProperties)
            String propertyName = getRDFResourceLocalName(prop);
            AssociationWrapper emfAssoc = assocManager.getAssociation(propertyName);
            String nameSpace = getNameSpace(prop.getNamespace());
            AssociationSource source = CreateUtils.createAssociationSource(emfAssoc.getAssociationEntity().getEntityCode(), nameSpace);

            // The idea is to create a new association called "domain", whose
            // LHS will be the OWLObjectProperty and RHS will be the domain.
            if (prop.getDomains(false).size() != 0) {
                for (Iterator domains = prop.getDomains(false).iterator(); domains.hasNext();) {
                    RDFResource domain = (RDFResource) domains.next();
                    if (domain instanceof OWLUnionClass) {
                        for (Iterator domainList = ((OWLUnionClass) domain).listOperands(); domainList.hasNext();) {
                            RDFSNamedClass domainClass = (RDFSNamedClass) domainList.next();
                            relateAssocSourceWithRDFResourceTarget(EntityTypes.CONCEPT, assocManager
                                    .getDomain(), source, domainClass);
                        }
                    }// end of OWLUnionClass
                    else {
                        relateAssocSourceWithRDFResourceTarget(EntityTypes.CONCEPT, assocManager.getDomain(),
                                source, domain);
                    }
                }
            }

            // The idea is to create a new association called "range", whose
            // LHS will be the OWLObjectProperty and RHS will be the range.
            if (prop.getRanges(false).size() != 0) {

                for (Iterator ranges = prop.getRanges(false).iterator(); ranges.hasNext();) {
                    RDFResource range = (RDFResource) ranges.next();
                    if (range instanceof OWLUnionClass) {
                        for (Iterator rangeList = ((OWLUnionClass) range).listOperands(); rangeList.hasNext();) {
                            RDFSNamedClass rangeClass = (RDFSNamedClass) rangeList.next();
                            relateAssocSourceWithRDFResourceTarget(EntityTypes.CONCEPT,
                                    assocManager.getRange(), source, rangeClass);
                        }

                    } else { // end of OWLUnionClass
                        relateAssocSourceWithRDFResourceTarget(EntityTypes.CONCEPT, assocManager.getRange(),
                                source, range);
                    }
                }

            }

            // //////////////////////////////////////////////////
            // /// Process Property Hierarchy/Relationships /////
            // /////////////////////////////////////////////////

            // Step 1: process subPropertyOf: here also we create
            // an association between associations.

            for (Iterator superProperties = prop.getSuperproperties(false).iterator(); superProperties.hasNext();) {
                RDFProperty superProp = (RDFProperty) superProperties.next();
                relateAssocSourceWithRDFResourceTarget(EntityTypes.ASSOCIATION,
                        assocManager.getSubPropertyOf(), source, superProp);
            }

            // Step 2: process inverseProperties
            if (prop.getInverseProperty() != null) {
                relateAssocSourceWithRDFResourceTarget(EntityTypes.ASSOCIATION, assocManager.getInverseOf(),
                        source, prop.getInverseProperty());
            }

            // Step 3: process equivalentProperties
            for (Iterator equivProperties = prop.getEquivalentProperties().iterator(); equivProperties.hasNext();) {
                OWLObjectProperty equivalent = (OWLObjectProperty) equivProperties.next();
                relateAssocSourceWithRDFResourceTarget(EntityTypes.ASSOCIATION, assocManager
                        .getEquivalentProperty(), source, equivalent);
            }
        } // end of for.
    }

    /**
     * This method determines the domain and ranges for the OWL Datatype
     * properties. It also processes different relationships between the
     * properties.
     * 
     */
    protected void processOWLDatatypeProperties(Snapshot snap) {
        snap = SimpleMemUsageReporter.snapshot();
        messages_.info("Read Time : " + SimpleMemUsageReporter.formatTimeDiff(snap.getTimeDelta(null))
                + " Heap Usage: " + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsage()) + " Heap Delta:"
                + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsageDelta(null)));
        messages_.info("Processing OWL Datatype Properties ...");

        for (Iterator props = owlModel_.getUserDefinedOWLDatatypeProperties().iterator(); props.hasNext();) {
            OWLDatatypeProperty prop = (OWLDatatypeProperty) props.next();
            // Check if the data type property is an annotation property. We do
            // not treat annotation properties as associations.
            if (prop.isAnnotationProperty()) {
                continue;
            }
            resolveAssociation(prop);
            // ///////////////////////////////////
            // /// Process Domain and Ranges /////
            // //////////////////////////////////

            // Get the appropriate association name initialized earlier
            // (initSupportedObjectProperties)
            String propertyName = getRDFResourceLocalName(prop);
            AssociationWrapper emfAssoc = assocManager.getAssociation(propertyName);
            String nameSpace = getNameSpace(prop.getNamespace());
            AssociationSource source = CreateUtils.createAssociationSource(emfAssoc.getAssociationEntity().getEntityCode(), nameSpace);

            // The idea is to create a new association called "hasDomain", whose
            // LHS will be the OWLDatatyeProperty and RHS will be the
            // RDFSDatatype.
            if (prop.getDomain(false) != null) {
                for (Iterator domains = prop.getDomains(false).iterator(); domains.hasNext();) {
                    RDFResource domain = (RDFResource) domains.next();
                    relateAssocSourceWithRDFResourceTarget(EntityTypes.CONCEPT, assocManager.getDomain(),
                            source, domain);
                }
            }

            if (prop.getRangeDatatype() != null) {
                AssociationData data = CreateUtils.createAssociationTextData(owlDatatypeName2emfDatatype_
                        .get(propertyName));
                relateAssociationSourceData(assocManager.getDatatype(), source, data);
            }

            // //////////////////////////////////////////////////
            // /// Process Property Hierarchy/Relationships /////
            // /////////////////////////////////////////////////

            // Step 1: process subPropertyOf relationships
            for (Iterator superProperties = prop.getSuperproperties(false).iterator(); superProperties.hasNext();) {
                OWLDatatypeProperty superProp = (OWLDatatypeProperty) superProperties.next();
                relateAssocSourceWithRDFResourceTarget(EntityTypes.ASSOCIATION,
                        assocManager.getSubPropertyOf(), source, superProp);
            }

            // Step 2: process inverseProperties
            if (prop.getInverseProperty() != null) {
                relateAssocSourceWithRDFResourceTarget(EntityTypes.ASSOCIATION, assocManager.getInverseOf(),
                        source, prop.getInverseProperty());
            }

            // Step 3: process equivalentProperties
            for (Iterator equivProperties = prop.getEquivalentProperties().iterator(); equivProperties.hasNext();) {
                OWLDatatypeProperty equivalent = (OWLDatatypeProperty) equivProperties.next();
                relateAssocSourceWithRDFResourceTarget(EntityTypes.ASSOCIATION, assocManager
                        .getEquivalentProperty(), source, equivalent);
            }
        } // end of for
    }

    /**
     * Defines an EMF concept and properties based on the given rdf source.
     * 
     * @param rdfResource
     *            The resource to evaluate.
     * @return The resolved concept; null if a new concept was not generated.
     */
    protected Concept resolveConcept(RDFResource rdfResource) {

        String rdfName = getRDFResourceLocalName(rdfResource);
        if (isNoopNamespace(rdfName))
            return null;

        if (owlClassName2Conceptcode_.containsKey(rdfName))
            return null;

        String label = resolveLabel(rdfResource);

        // Create the raw EMF concept and assign label as initial description,
        // which may be overridden later by preferred text.
        Concept emfConcept = new Concept();
        EntityDescription ed = new EntityDescription();
        ed.setContent(label);
        emfConcept.setEntityDescription(ed);
        emfConcept.setEntityCode(rdfName);
        emfConcept.setIsAnonymous(Boolean.FALSE);

        String nameSpace = getNameSpace(rdfResource.getNamespace());
        emfConcept.setEntityCodeNamespace(nameSpace);

        // Is deprecated? If so, mark as inactive.
        if (rdfResource instanceof OWLNamedClass
                && OWLNames.Cls.DEPRECATED_CLASS.equals(rdfResource.getRDFType().getName()))
            emfConcept.setIsActive(Boolean.FALSE);

        // Set the 'isDefined' property.
        if (rdfResource instanceof OWLNamedClass) {
            OWLNamedClass owlNamedClass = (OWLNamedClass) rdfResource;
            emfConcept.setIsDefined(owlNamedClass.isDefinedClass());
        }

        // Resolve all the concept properties and add to entities.
        resolveEntityProperties(emfConcept, rdfResource);
        addEntity(emfConcept);

        // Remember the rdf to code mapping and return.
        owlClassName2Conceptcode_.put(rdfName, emfConcept.getEntityCode());

        return emfConcept;
    }

    /**
     * Defines an EMF concept and properties based on the given rdf source.
     * 
     * @param rdfResource
     *            The resource to evaluate.
     * @return The resolved concept; null if a new concept was not generated.
     */
    protected Entity resolveAssociation(RDFResource rdfResource) {

        String rdfName = getRDFResourceLocalName(rdfResource);
        if (isNoopNamespace(rdfName))
            return null;

        if (owlClassName2Conceptcode_.containsKey(rdfName))
            return null;

        String label = resolveLabel(rdfResource);

        // Create the raw EMF concept and assign label as initial description,
        // which may be overridden later by preferred text.
        Entity emfEntity = new Entity();
        emfEntity.addEntityType(EntityTypes.ASSOCIATION.toString());
        EntityDescription ed = new EntityDescription();
        ed.setContent(label);
        emfEntity.setEntityDescription(ed);
        emfEntity.setEntityCode(rdfName);
        emfEntity.setIsAnonymous(Boolean.FALSE);

        String nameSpace = getNameSpace(rdfResource.getNamespace());
        emfEntity.setEntityCodeNamespace(nameSpace);

        // Is deprecated? If so, mark as inactive.
        if (rdfResource instanceof OWLNamedClass
                && OWLNames.Cls.DEPRECATED_CLASS.equals(rdfResource.getRDFType().getName()))
            emfEntity.setIsActive(Boolean.FALSE);

        // Set the 'isDefined' property.
        if (rdfResource instanceof OWLNamedClass) {
            OWLNamedClass owlNamedClass = (OWLNamedClass) rdfResource;
            emfEntity.setIsDefined(owlNamedClass.isDefinedClass());
        }

        // Resolve all the concept properties and add to entities.
        resolveEntityProperties(emfEntity, rdfResource);
        addEntity(emfEntity);

        // Remember the rdf to code mapping and return.
        owlClassName2Conceptcode_.put(rdfName, emfEntity.getEntityCode());

        return emfEntity;
    }

    /**
     * Defines EMF subClassOf relations based on OWL source.
     * 
     */
    protected void resolveSubClassOfRelations(AssociationSource source, RDFSNamedClass rdfsNamedClass) {
        // Process parent-child (rdfs:subClassOf) relationships
        // Does this concept represent the root of a concept branch that should
        // be centrally linked to the top node for subclass traversal?
        if (isRootNode(rdfsNamedClass)) {
            AssociationTarget target = CreateUtils.createAssociationTarget(ProtegeOwl2EMFConstants.ROOT_CODE, null);
            relateAssociationSourceTarget(assocManager.getSubClassOf(), source, target);
        }

        // Does this concept have any parents?
        Collection superClassCollection = rdfsNamedClass.getSuperclasses(false);
        for (Iterator superClasses = superClassCollection.iterator(); superClasses.hasNext();) {
            RDFResource superClass = (RDFResource) superClasses.next();
            relateAssocSourceWithRDFResourceTarget(EntityTypes.CONCEPT, assocManager.getSubClassOf(), source,
                    superClass);
            if (superClass instanceof OWLRestriction) {
                OWLRestriction restriction = (OWLRestriction) superClass;
                processRestriction(restriction, null, source);
            }
        }
    }

    /**
     * 
     * Defines EMF equivalentClass relations based on OWL source.
     * 
     */
    protected void resolveEquivalentClassRelations(AssociationSource source, RDFSNamedClass rdfsNamedClass) {
        for (Iterator equivClasses = rdfsNamedClass.getEquivalentClasses().iterator(); equivClasses.hasNext();) {
            String emfCode = resolveAnonymousClass((OWLClass) equivClasses.next(), source);
            AssociationTarget target = CreateUtils.createAssociationTarget(emfCode, entityCode2NameSpace_
                    .get(emfCode));
            relateAssociationSourceTarget(assocManager.getEquivalentClass(), source, target);
        }
    }

    /**
     * Defines EMF equivalentClass relations based on OWL source.
     * 
     */
    protected void resolveDisjointWithRelations(AssociationSource source, RDFSNamedClass rdfsNamedClass) {
        if (rdfsNamedClass instanceof OWLNamedClass) {
            OWLNamedClass newOwlClass = null;
            newOwlClass = (OWLNamedClass) rdfsNamedClass;
            for (Iterator disjointClasses = newOwlClass.getDisjointClasses().iterator(); disjointClasses.hasNext();) {
                RDFResource disjointClass = (RDFResource) disjointClasses.next();
                relateAssocSourceWithRDFResourceTarget(EntityTypes.CONCEPT, assocManager.getDisjointWith(),
                        source, disjointClass);
            }
        }
    }

    /**
     * Defines EMF complementOf relations based on OWL source.
     * 
     */
    protected void resolveComplementOfRelations(AssociationSource source, RDFSNamedClass rdfsNamedClass) {
        if (rdfsNamedClass instanceof OWLComplementClass) {
            relateAssocSourceWithRDFResourceTarget(EntityTypes.CONCEPT, assocManager.getComplementOf(), source,
                    rdfsNamedClass);
        }
    }

    /**
     * Defines EMF class RDF properties.
     * 
     */
    protected void resolveOWLObjectPropertyRelations(AssociationSource source, RDFResource rdfResource) {
        for (Iterator rdfProps = rdfResource.getRDFProperties().iterator(); rdfProps.hasNext();) {
            RDFProperty rdfProp = (RDFProperty) rdfProps.next();
            if (rdfProp instanceof OWLObjectProperty) {
                // Lookup the LexGrid association; ignore if this property does
                // not match a defined association ...
                String relationName = getRDFResourceLocalName(rdfProp);
                AssociationWrapper emfAssoc = assocManager.getAssociation(relationName);
                // Determine the targets ...
                Collection propVals = rdfResource.getPropertyValues(rdfProp);
                if (propVals != null) {
                    for (Iterator vals = propVals.iterator(); vals.hasNext();) {
                        Object val = vals.next();
                        if (val instanceof OWLNamedClass) {
                            relateAssocSourceWithRDFResourceTarget(EntityTypes.CONCEPT, emfAssoc, source,
                                    (OWLNamedClass) val);
                        } else if (val instanceof OWLIndividual) {
                            relateAssocSourceWithRDFResourceTarget(EntityTypes.INSTANCE, emfAssoc, source,
                                    (OWLIndividual) val);
                        }
                    }
                }
            }
        }
    }

    /**
     * Defines EMF differentFrom relations based on OWL source.
     * 
     */
    protected void resolveDifferentFromRelations(AssociationSource source, OWLIndividual individual) {

        for (Iterator differentFrom = individual.getDifferentFrom().iterator(); differentFrom.hasNext();) {
            OWLIndividual different = (OWLIndividual) differentFrom.next();
            relateAssocSourceWithRDFResourceTarget(EntityTypes.INSTANCE, assocManager.getDifferentFrom(),
                    source, different);
        }

    }

    /**
     * Defines EMF complementOf relations based on OWL source.
     * 
     */
    protected void resolveSameAsRelations(AssociationSource source, OWLIndividual individual) {
        for (Iterator sameAs = individual.getSameAs().iterator(); sameAs.hasNext();) {
            OWLIndividual same = (OWLIndividual) sameAs.next();
            relateAssocSourceWithRDFResourceTarget(EntityTypes.INSTANCE, assocManager.getSameAs(), source, same);
        }
    }

    /**
     * Defines EMF RDFType relations based on OWL source.
     * 
     */
    protected void resolveRdfTypeRelations(AssociationSource source, OWLIndividual individual) {
        for (Iterator iter = individual.getDirectTypes().iterator(); iter.hasNext();) {
            Object item= iter.next();
            if (item instanceof RDFSClass) {
               RDFSClass typeClass= (RDFSClass) item; 
               relateAssocSourceWithRDFResourceTarget(EntityTypes.CONCEPT, assocManager.getRdfType(), source, typeClass);
            }
        }
    }    
    /**
     * Resolve and assign all property information contained by the given RDF
     * resource to the EMF Entity.
     * 
     * @param emfEntity
     * @param rdfResource
     * 
     *            Last updated: 05/28/2008
     */
    protected void resolveEntityProperties(Entity emfEntity, RDFResource rdfResource) {
        String rdfName = rdfResource.getLocalName();

        // Temporary container for EMF properties.
        // Note: The EMF object does not enforce order. However, the XSD models
        // have required properties to occur in the XML in a specific order.
        // The comparator ensures a compatible order is maintained.
        SortedSet sortedProps = new TreeSet(propertyComparator);
        // Counters to use in property ID assignment to keep things unique
        // and track assigned presentation count.
        int i = 0;
        int presentationCount = 0;

        // Start with properties from RDF-defined property set ...
        for (Iterator props = rdfResource.getRDFProperties().iterator(); props.hasNext();) {
            RDFProperty prop = (RDFProperty) props.next();
            String propName = getRDFResourceLocalName(prop);

            // Do we care about this rdf property?
            if (isNoop(propName) || isNoopNamespace(propName))
                continue;

            // Do we have an EMF class match?
            EClass propClass = (EClass) owlDatatypeName2emfPropClass_.get(propName);
            if (propClass == null)
                continue;

            // Determine the EMF property name and datatype ...
            String emfDType = owlDatatypeName2emfDatatype_.get(propName);
            String emfLabel = owlDatatypeName2label_.get(propName);

            // Interpret RDF property value(s) ...
            for (Iterator values = rdfResource.getPropertyValues(prop).iterator(); values.hasNext();) {
                Object value = values.next();
                String resolvedText = resolveRDFText(rdfResource, value);

                // Special case for handling concept code and status, which are
                // set directly as attributes on the LexGrid concept.
                if (propName.matches(prefManager.getMatchPattern_conceptCode())) {
                    emfEntity.setEntityCode(resolvedText);
                } else if (emfLabel != null && emfLabel.matches(prefManager.getMatchPattern_conceptStatus())) {
                    emfEntity.setStatus(resolvedText);
                    if (resolvedText.matches(prefManager.getMatchPattern_inactiveStatus()))
                        emfEntity.setIsActive(false);
                }
                // Otherwise instantiate a new EMF property and add the new
                // property to the list to eventually add to the concept.
                else {
                    Property newProp = resolveProp(prop, propClass, generatePropertyID(++i), emfLabel, emfDType, prop
                            .getNamespace(), resolvedText);
                    if (newProp.getValue() != null) {
                        sortedProps.add(newProp);
                        if (newProp instanceof Presentation)
                            presentationCount++;
                    }
                }
            }
        }

        // Indicate whether the concept is primitive (no equivalent classes) ...
        Collection values = rdfResource.getPropertyValues(rdfResource.getOWLModel().getOWLEquivalentClassProperty());
        if (values == null || values.isEmpty()) {
            Property emfProp = CreateUtils.createProperty(generatePropertyID(++i), prefManager
                    .getPropertyName_primitive(), "true", emfSupportedMappings_);
            sortedProps.add(emfProp);
        } else {
            Property emfProp = CreateUtils.createProperty(generatePropertyID(++i), prefManager
                    .getPropertyName_primitive(), "false", emfSupportedMappings_);
            sortedProps.add(emfProp);
        }

        // The LexGrid model requires a matching presentation for the entity
        // description. If no presentations exist, manufacture a default to
        // satisfy the requirement. If created, the name of the new property
        // is set to indicate where the value was found. Also support
        // explicit requests for "rdfs:label" as the preferred property.
        boolean generatePreferred = prefManager.getPrioritized_presentation_names().size() == 0
                || ProtegeOwl2EMFConstants.PROPNAME_RDFS_LABEL.equalsIgnoreCase(prefManager
                        .getPrioritized_presentation_names().get(0)) || presentationCount == 0;
        if (generatePreferred) {
            String entityDesc = emfEntity.getEntityDescription().getContent();
            sortedProps.add(CreateUtils.createPresentation(generatePropertyID(++i),
                    rdfName.equals(entityDesc) ? ProtegeOwl2EMFConstants.PROPNAME_RDF_ID
                            : ProtegeOwl2EMFConstants.PROPNAME_RDFS_LABEL, entityDesc, true, emfSupportedMappings_));
        }

        // Track assignment of preferred presentation and definition.
        // For presentation, check to see if preference was set above.
        boolean assignedPreferredPres = generatePreferred;
        boolean assignedPreferredDefn = false;

        // Iterate through properties; stop when complete or if both a preferred
        // presentation and definition have been assigned ...
        for (Iterator props = sortedProps.iterator(); props.hasNext()
                && !(assignedPreferredPres && assignedPreferredDefn);) {
            Object prop = props.next();
            if (!assignedPreferredPres && (prop instanceof Presentation)) {
                // Tag the property
                Presentation pres = (Presentation) prop;
                pres.setIsPreferred(Boolean.TRUE);

                // Entity description on concept should match preferred
                // presentation.
                EntityDescription ed = new EntityDescription();
                ed.setContent(((Presentation) prop).getValue().getContent());
                emfEntity.setEntityDescription(ed);

                // Remember that a preferred presentation was assigned ...
                assignedPreferredPres = true;
            }
            if (!assignedPreferredDefn && (prop instanceof Definition)) {
                // Tag the definition
                ((Definition) prop).setIsPreferred(Boolean.TRUE);

                // Remember that a preferred definition was assigned ...
                assignedPreferredDefn = true;
            }
        }

        // Updated on 05/28/2008: It was decided that we also need to
        // hook all the OWLDatatypeProperties for a particular concept
        // as "Concept Properties". This will assist in the visualization
        // of the concepts in a browser. The idea is that the "concept" is
        // stored as the "Range", and the "PropertyText" corresponds to the
        // "range" of the property. However, note that these
        // OWLDatatypeProperties are also represented in the "Relations"
        // container as "Associations", since they may have relations
        // between themselves: e.g., subPropertyOf relations.
        // OWLNamedClass myOWLNamedClass = (OWLNamedClass) rdfResource;

        // Added on 01/14/2009 by Satya as Concept and its
        // properties can be created for OWLObjectProperty's as well.
        if (rdfResource instanceof RDFSNamedClass) {
            RDFSNamedClass rdfsNamedClass = (RDFSNamedClass) rdfResource;
            for (Iterator classProps = rdfsNamedClass.getAssociatedProperties().iterator(); classProps.hasNext();) {
                RDFProperty prop = (RDFProperty) classProps.next();
                if (prop instanceof OWLDatatypeProperty) {
                    String propertyName = getFromLastIndexOfColonOrHash(prop.getBrowserText());

                    RDFSDatatype range = prop.getRangeDatatype();
                    if (range != null) {
                        String propertyRangeName = getRDFResourceLocalName(range);
                        Property emfProp = CreateUtils.createProperty(generatePropertyID(++i), propertyName,
                                propertyRangeName, emfSupportedMappings_);
                        sortedProps.add(emfProp);
                    }
                }
            }
        }

        // Now add all the sorted properties to the concept.
        for (Iterator emfProps = sortedProps.iterator(); emfProps.hasNext();) {
            Property emfProp = (Property) emfProps.next();
            EntitiesUtil.addProperty(emfEntity, emfProp);
        }

    }

    /**
     * Defines an EMF instance.
     */
    protected Instance resolveIndividual(RDFResource rdfResource) {
        String rdfName = getRDFResourceLocalName(rdfResource);

        if (isNoopNamespace(rdfName))
            return null;

        String label = resolveLabel(rdfResource);

        // Create the raw EMF individual and assign label as initial
        // description,
        // which may be overridden later by preferred text.
        Instance emfInstance = new Instance();
        EntityDescription ed = new EntityDescription();
        ed.setContent(label);
        emfInstance.setEntityDescription(ed);
        emfInstance.setEntityCode(rdfName);
        String nameSpace = getNameSpace(rdfResource.getNamespace());
        emfInstance.setEntityCodeNamespace(nameSpace);

        // Is deprecated? If so, mark as inactive.
        if (rdfResource instanceof OWLIndividual
                && OWLNames.Cls.DEPRECATED_CLASS.equals(rdfResource.getRDFType().getName()))
            emfInstance.setIsActive(Boolean.FALSE);

        // Set the 'isDefined' property.
        if (rdfResource instanceof OWLNamedClass) {
            OWLNamedClass owlNamedClass = (OWLNamedClass) rdfResource;
            emfInstance.setIsDefined(owlNamedClass.isDefinedClass());
        }

        // Updated 05/28/2008: handle the individual OWLObjectProperties
        // and OWLDatatypeProperties. Essentially, both are represented
        // as instanceProperties.
        resolveEntityProperties(emfInstance, rdfResource);

        // Remember the rdf to code mapping and return.
        owlInstanceName2code_.put(rdfName, emfInstance.getEntityCode());
        return emfInstance;
    }

    /**
     * This method associates OWLIndividuals and their properties: OWLDatatype
     * and OWLObject. Essentially, the is to tag each individual with the
     * property so that it helps in visualization and navigation.
     * 
     * @param emfInstance
     * @param rdfResource
     */
    protected void resolveIndividualProperties(Instance emfInstance, RDFResource rdfResource) {

        // Temporary container for EMF properties.
        // Note: The EMF object does not enforce order. However, the XSD models
        // have required properties to occur in the XML in a specific order.
        // The comparator ensures a compatible order is maintained.
        SortedSet sortedProps = new TreeSet(propertyComparator);

        // Counter to use in property ID assignment to keep things unique.
        int i = 0;

        RDFSNamedClass myClass = null;
        for (Iterator userDefinedClasses = owlModel_.getUserDefinedOWLNamedClasses().iterator(); userDefinedClasses
                .hasNext();) {
            myClass = (RDFSNamedClass) userDefinedClasses.next();

            if (rdfResource.hasRDFType(myClass, false)) {
                String className = getRDFResourceLocalName(myClass);

                // Add this information as an instanceProperty.
                Property emfProp = CreateUtils.createProperty(generatePropertyID(++i), "isInstanceOf", className,
                        emfSupportedMappings_);
                sortedProps.add(emfProp);

                break;
            }
        }

        String rdfName = rdfResource.getLocalName();

        int presentationCount = 0;

        // Start with properties from RDF-defined property set ...
        for (Iterator props = rdfResource.getRDFProperties().iterator(); props.hasNext();) {
            RDFProperty prop = (RDFProperty) props.next();
            String propName = getRDFResourceLocalName(prop);
            // Do we care about this rdf property?
            if (isNoop(propName) || isNoopNamespace(propName))
                continue;

            // Do we have an EMF class match?
            EClass propClass = (EClass) owlDatatypeName2emfPropClass_.get(propName);
            if (propClass == null)
                continue;

            // Determine the EMF property name and datatype ...
            String emfDType = owlDatatypeName2emfDatatype_.get(propName);
            String emfLabel = owlDatatypeName2label_.get(propName);

            // Interpret RDF property value(s) ...
            for (Iterator values = rdfResource.getPropertyValues(prop).iterator(); values.hasNext();) {
                Object value = values.next();
                String resolvedText = resolveRDFText(rdfResource, value);

                // Special case for handling concept code and status, which are
                // set directly as attributes on the LexGrid instance.
                if (propName.matches(prefManager.getMatchPattern_conceptCode())) {
                    emfInstance.setEntityCode(resolvedText);
                } else if (emfLabel != null && emfLabel.matches(prefManager.getMatchPattern_conceptStatus())) {
                    emfInstance.setStatus(resolvedText);
                    if (resolvedText.matches(prefManager.getMatchPattern_inactiveStatus()))
                        emfInstance.setIsActive(false);
                }
                // Otherwise instantiate a new EMF property and add the new
                // property to the list to eventually add to the instance.
                else {
                    Property newProp = resolveProp(prop, propClass, generatePropertyID(++i), emfLabel, emfDType, prop
                            .getNamespace(), resolvedText);
                    if (newProp.getValue() != null) {
                        sortedProps.add(newProp);
                        if (newProp instanceof Presentation)
                            presentationCount++;
                    }
                }
            }
        }

        // Indicate whether the instance is primitive (no equivalent classes)
        // ...
        Collection values = rdfResource.getPropertyValues(rdfResource.getOWLModel().getOWLEquivalentClassProperty());
        if (values == null || values.isEmpty()) {
            Property emfProp = CreateUtils.createProperty(generatePropertyID(++i), prefManager
                    .getPropertyName_primitive(), "true", emfSupportedMappings_);
            sortedProps.add(emfProp);
        } else {
            Property emfProp = CreateUtils.createProperty(generatePropertyID(++i), prefManager
                    .getPropertyName_primitive(), "false", emfSupportedMappings_);
            sortedProps.add(emfProp);
        }

        // The LexGrid model requires a matching presentation for the entity
        // description. If no presentations exist, manufacture a default to
        // satisfy the requirement. If created, the name of the new property
        // is set to indicate where the value was found. Also support
        // explicit requests for "rdfs:label" as the preferred property.
        boolean generatePreferred = prefManager.getPrioritized_presentation_names().size() == 0
                || ProtegeOwl2EMFConstants.PROPNAME_RDFS_LABEL.equalsIgnoreCase(prefManager
                        .getPrioritized_presentation_names().get(0)) || presentationCount == 0;
        if (generatePreferred) {
            String entityDesc = emfInstance.getEntityDescription().getContent();
            sortedProps.add(CreateUtils.createPresentation(generatePropertyID(++i),
                    rdfName.equals(entityDesc) ? ProtegeOwl2EMFConstants.PROPNAME_RDF_ID
                            : ProtegeOwl2EMFConstants.PROPNAME_RDFS_LABEL, entityDesc, true, emfSupportedMappings_));
        }

        // Track assignment of preferred presentation and definition.
        // For presentation, check to see if preference was set above.
        boolean assignedPreferredPres = generatePreferred;
        boolean assignedPreferredDefn = false;

        // Iterate through properties; stop when complete or if both a preferred
        // presentation and definition have been assigned ...
        for (Iterator props = sortedProps.iterator(); props.hasNext()
                && !(assignedPreferredPres && assignedPreferredDefn);) {
            Object prop = props.next();
            if (!assignedPreferredPres && (prop instanceof Presentation)) {
                // Tag the property
                Presentation pres = (Presentation) prop;
                pres.setIsPreferred(Boolean.TRUE);
                // Entity description on instance should match preferred
                // presentation.
                EntityDescription ed = new EntityDescription();
                ed.setContent(((Presentation) prop).getValue().getContent());
                emfInstance.setEntityDescription(ed);
                // Remember that a preferred presentation was assigned ...
                assignedPreferredPres = true;
            }
            if (!assignedPreferredDefn && (prop instanceof Definition)) {
                // Tag the definition
                ((Definition) prop).setIsPreferred(Boolean.TRUE);
                // Remember that a preferred definition was assigned ...
                assignedPreferredDefn = true;
            }
        }

        // Now add all the sorted properties to the instance.
        for (Iterator emfProps = sortedProps.iterator(); emfProps.hasNext();) {
            Property emfProp = (Property) emfProps.next();
            EntitiesUtil.addProperty(emfInstance, emfProp);
        }
    }

    /**
     * Instantiate and return a new EMF property based on the provided values.
     * 
     * @param emfClass
     *            The EMF property class to instantiate (presentation, comment,
     *            etc).
     * @param emfID
     *            The identifier to assign to the new property; not null.
     * @param emfLabel
     *            The name or label for the new property; not null.
     * @param emfDType
     *            The data type of the new property; null if not applicable.
     * @param rdfNamespace
     *            Namespace of the RDF property being converted.
     * @param rdfText
     *            Text from the RDF property being converted.
     * @return org.LexGrid.emf.concepts.ConceptProperty
     */
    protected Property resolveProp(RDFProperty prop, EClass emfClass, String emfID, String emfLabel, String emfDType,
            String rdfNamespace, String rdfText) {

        Property emfProp;
        String propName = prop.getName();
        if (RDFSNames.Slot.LABEL.equals(propName) || emfClass == ConceptsPackage.eINSTANCE.getPresentation())
            emfProp = CreateUtils.createPresentation(emfID, emfLabel, rdfText, null, emfSupportedMappings_);
        else if (RDFSNames.Slot.COMMENT.equals(propName) || emfClass == ConceptsPackage.eINSTANCE.getComment())
            emfProp = CreateUtils.createComment(emfID, emfLabel, rdfText, emfSupportedMappings_);
        else if (emfClass == ConceptsPackage.eINSTANCE.getDefinition())
            emfProp = CreateUtils.createDefinition(emfID, emfLabel, rdfText, null, emfSupportedMappings_);
        else
            emfProp = CreateUtils.createProperty(emfID, emfLabel, null, emfSupportedMappings_);

        // Handle imbedded XML if present ...
        Map<String, String> xmlTagsAndVals = resolveXMLTagsAndValues(rdfText);
        Set<String> xmlTags = xmlTagsAndVals.keySet();
        if (xmlTags.size() > 0 && prefManager.isProcessComplexProperties()) {

            // Designated tags may act as property text or source;
            // all other will be treated as property qualifiers.
            for (Iterator<String> tags = xmlTags.iterator(); tags.hasNext();) {
                String tag = tags.next();

                if (tag == null) {
                    messages_.info("Skipping " + emfID + ", " + emfLabel + ", " + emfDType + ", " + rdfNamespace + ", "
                            + rdfText);
                    continue;
                }

                String text = xmlTagsAndVals.get(tag);

                if (tag.matches(prefManager.getMatchPattern_xmlTextNames())) {
                    emfProp.setValue(CreateUtils.createText(text));
                } else if (tag.matches(prefManager.getMatchPattern_xmlSourceNames())) {
                    emfProp.addSource(CreateUtils.createSource(text, null, null, emfSupportedMappings_));

                    // Register the source as supported if not already
                    // defined.
                    emfSupportedMappings_.registerSupportedSource(text, rdfNamespace + text, text, null, false);
                } else if (tag.matches("language")) {
                    emfProp.setLanguage(text);

                    // Register the source as supported if not already
                    // defined.
                    emfSupportedMappings_.registerSupportedLanguage(text,
                            ProtegeOwl2EMFConstants.LANG_URI + ':' + text, text, false);
                }
                // specific to the new complex props implementation
                else if (prefManager.isComplexProps_isDbxRefSource()
                        && text.matches(ProtegeOwl2EMFConstants.MATCH_XMLSOURCE_VALUES)) {
                    String val = text;
                    String ref = null;
                    String[] sourceWithRef = text.split("(:)");
                    if (sourceWithRef.length == 2) {
                        val = sourceWithRef[0];
                        ref = sourceWithRef[1];
                    }
                    emfProp.addSource(CreateUtils.createSource(val, null, ref, emfSupportedMappings_));

                    // Register the source as supported if not already
                    // defined.
                    emfSupportedMappings_.registerSupportedSource(text, rdfNamespace + text, text, null, false);

                } else if (emfProp instanceof Presentation
                        && tag.matches(prefManager.getMatchPattern_xmlRepFormNames())) {
                    ((Presentation) emfProp).setRepresentationalForm(text);

                    // Register the source as supported if not already
                    // defined.
                    emfSupportedMappings_.registerSupportedRepresentationalForm(text, rdfNamespace + text, text, false);
                }
                // specific to the new complex props implementation
                else if (prefManager.isComplexProps_isDbxRefRepForm() && emfProp instanceof Presentation
                        && text.matches(ProtegeOwl2EMFConstants.MATCH_XMLREPFORM_VALUES)) {
                    ((Presentation) emfProp).setRepresentationalForm(text);

                    // Register the source as supported if not already
                    // defined.
                    emfSupportedMappings_.registerSupportedRepresentationalForm(text, rdfNamespace + text, text, false);
                } else {
                    emfProp.addPropertyQualifier(CreateUtils.createPropertyQualifier(tag, text, emfSupportedMappings_));

                    // Register the qualifier as supported if not already
                    // defined.
                    emfSupportedMappings_.registerSupportedPropertyQualifier(tag, rdfNamespace + tag, tag, false);
                }
            }
        } else {
            // No XML; interpret text as complete property text.
            emfProp.setValue(CreateUtils.createText(rdfText));
        }
        return emfProp;
    }

    /**
     * 
     * This method handles the resolution of owl:Anonymous classes.
     * 
     * @param owlClass
     * @return
     */
    protected String resolveAnonymousClass(OWLClass owlClass, AssociationSource assocSource) {
        String code = owlClass.getLocalName();
        // Check if this concept has already been processed. We do not want
        // duplicate concepts.
        if (isEntityCodeRegistered(code)) {
            return code;
        }

        Concept emfClass = new Concept();
        emfClass.setEntityCode(code);
        emfClass.setIsAnonymous(Boolean.TRUE);
        
        EntityDescription ed = new EntityDescription();
        ed.setContent(owlClass.getBrowserText());
        emfClass.setEntityDescription(ed);

        int emfPropNum = 0;
        int dataTypeNumber = 0;

        AssociationSource source = CreateUtils.createAssociationSource(code, null);
        // Add relationships to further break down components of the anonymous
        // node ...
        if (owlClass instanceof OWLNAryLogicalClass) {
            OWLNAryLogicalClass logicalClass = (OWLNAryLogicalClass) owlClass;
            // Add the type prop ...
            RDFProperty opProp = logicalClass.getOperandsProperty();
            Property emfProp = CreateUtils.createProperty(generatePropertyID(++emfPropNum), prefManager
                    .getPropertyName_type(), opProp.getLocalName(), emfSupportedMappings_);
            emfClass.addProperty(emfProp);

            // Evaluate the operands defined for the anonymous node to determine
            // relations.
            for (Iterator operands = logicalClass.getOperands().iterator(); operands.hasNext();) {
                Object operand = operands.next();
                if (operand instanceof OWLComplementClass) {
                    OWLComplementClass complement = (OWLComplementClass) operand;
                    String emfCode = resolveAnonymousClass((OWLClass) complement.getComplement(), assocSource);
                    AssociationTarget opTarget = CreateUtils.createAssociationTarget(emfCode, null);
                    relateAssociationSourceTarget(assocManager.getComplementOf(), source, opTarget);

                } else if (operand instanceof OWLRestriction) {
                    // Operand defines a restriction placed on the anonymous
                    // node...
                    OWLRestriction op = (OWLRestriction) operand;
                    processRestriction(op, assocSource, source);

                } else if (operand instanceof OWLNamedClass) {
                    // Operand defines a direct participant in the anonymous
                    // node...
                    OWLNamedClass op = (OWLNamedClass) operand;
                    AssociationTarget opTarget = CreateUtils.createAssociationTarget(op.getLocalName(), null);
                    relateAssociationSourceTarget(assocManager.getSubClassOf(), source, opTarget);

                } else if (operand instanceof OWLEnumeratedClass) {
                    String emfCode = resolveAnonymousClass((OWLEnumeratedClass) operand, assocSource);

                    AssociationTarget opTarget = CreateUtils.createAssociationTarget(emfCode, null);

                    relateAssociationSourceTarget(assocManager.getSubClassOf(), source, opTarget);
                } else if (operand instanceof OWLNAryLogicalClass) {
                    String emfCode = resolveAnonymousClass((OWLNAryLogicalClass) operand, assocSource);

                    AssociationTarget opTarget = CreateUtils.createAssociationTarget(emfCode, null);

                    relateAssociationSourceTarget(assocManager.getSubClassOf(), source, opTarget);
                }
            }
        }

        if (owlClass instanceof OWLEnumeratedClass) {
            Property emfProp = CreateUtils.createProperty(generatePropertyID(++emfPropNum), prefManager
                    .getPropertyName_type(), "owl:oneOf", emfSupportedMappings_);
            emfClass.addProperty(emfProp);
        }

        if (owlClass instanceof OWLRestriction) {
            OWLRestriction restriction = (OWLRestriction) owlClass;
            Property emfProperty = CreateUtils.createProperty(generatePropertyID(++emfPropNum), prefManager
                    .getPropertyName_type(), "owl:Restriction", emfSupportedMappings_);
            emfClass.addProperty(emfProperty);
            processRestriction(restriction, assocSource, source);
        }

        if (owlClass instanceof OWLComplementClass) {
            OWLComplementClass complement = (OWLComplementClass) owlClass;
            String emfCode = resolveAnonymousClass((OWLClass) complement.getComplement(), assocSource);
            AssociationTarget opTarget = CreateUtils.createAssociationTarget(emfCode, null);
            relateAssociationSourceTarget(assocManager.getComplementOf(), source, opTarget);
        }

        // Add entity description and matching preferred text presentation to
        // the browser text we get from the Protege API.
        // Note: text was derived from the browser text. Since it is unclear
        // what property it was derived from, we document as 'label'.
        Presentation pres = CreateUtils.createPresentation(generatePropertyID(++emfPropNum), "label", emfClass
                .getEntityDescription().getContent(), Boolean.TRUE, emfSupportedMappings_);
        emfClass.addPresentation(pres);
        // Add to the concept container or write to db...
        addEntity(emfClass);
        // Return the emf class name
        return emfClass.getEntityCode();
    }

    /**
     * 
     * @param restriction
     * @param assocSource
     *            - The assocSource that should be added if processing strict
     *            owl
     * @param source
     *            - The normal source of the association
     */
    protected void processRestriction(OWLRestriction restriction, AssociationSource assocSource,
            AssociationSource source) {
        // Operand defines a restriction placed on the anonymous
        // node...
        RDFProperty onProp = restriction.getOnProperty();
        if (onProp != null) {
            String assocName = getRDFResourceLocalName(onProp);
            AssociationWrapper opAssoc = assocManager.getAssociation(assocName);
            if (opAssoc != null) {
                // to be intialized later depending on the need.
                AssociationData opData = null;
                String targetCode = null;
                String targetNameSpace = null;
                RDFProperty fillerProp = restriction.getFillerProperty();

                if (restriction instanceof OWLQuantifierRestriction) {
                    OWLQuantifierRestriction rest = (OWLQuantifierRestriction) restriction;
                    RDFResource myresource = rest.getFiller();
                    if (myresource instanceof OWLNamedClass) {
                        // Set the OWL Class as the range
                        targetCode = resolveConceptID(myresource);
                        targetNameSpace = getNameSpace(myresource.getNamespace());
                    } else if (myresource instanceof OWLClass) {
                        targetCode = resolveAnonymousClass((OWLClass) myresource, assocSource);
                    }
                } else if (restriction instanceof OWLCardinalityBase) {
                    OWLCardinalityBase cardinailityBase = (OWLCardinalityBase) restriction;
                    opData = CreateUtils.createAssociationTextData("" + cardinailityBase.getCardinality());
                } else if (restriction instanceof OWLHasValue) {
                    OWLHasValue hasValue = (OWLHasValue) restriction;
                    Object hasValueObj = hasValue.getHasValue();

                    if (hasValueObj instanceof RDFResource) {
                        RDFResource hasValueResource = (RDFResource) hasValueObj;
                        targetCode = resolveConceptID(hasValueResource);
                        targetNameSpace = getNameSpace(hasValueResource.getNamespace());
                    } else if ((hasValueObj instanceof RDFSLiteral)) {
                        // Updated on 09/23/08: need to store
                        // RDFSLiteral as AssociationData
                        opData = CreateUtils.createAssociationTextData(((RDFSLiteral) hasValueObj).getBrowserText());

                    } else {
                        // Updated on 09/23/08: need to store
                        // Literal as AssociationData
                        opData = CreateUtils.createAssociationTextData(hasValueObj.toString());
                    }

                }

                if (targetCode == null) {
                    try {
                        RDFProperty rpty = restriction.getOWLModel().getRDFProperty("owl:onClass");
                        if (rpty == null)
                            rpty = restriction.getOWLModel().createRDFProperty("owl:onClass");
                        Object value = restriction.getPropertyValue(rpty);
                        if ((value != null) && (value instanceof OWLNamedClass)) {
                            targetCode = resolveConceptID((OWLNamedClass) value);
                            targetNameSpace = getNameSpace(((OWLNamedClass) value).getNamespace());
                        }
                    } catch (Exception ex1) {
                        // failed to get targetcode from onClass;
                    }
                }

                AssociationTarget opTarget = null;
                if (targetCode != null) {
                    opTarget = CreateUtils.createAssociationTarget(targetCode, targetNameSpace);
                }

                // Set the association qualifications: this
                // indicates the kind of restriction (e.g., owl:cardinality).
                if (fillerProp != null) {
                    AssociationQualification opQual = CreateUtils.createAssociationQualification(fillerProp,
                            emfSupportedMappings_);
                    if (opData != null) {
                        opData.addAssociationQualification(opQual);
                    }
                    if (opTarget != null) {
                        opTarget.addAssociationQualification(opQual);
                    }
                }

                if (opData != null) {
                    relateAssociationSourceData(opAssoc, source, opData);
                }
                if (opTarget != null) {
                    relateAssociationSourceTarget(opAssoc, source, opTarget);
                    if (!prefManager.isProcessStrictOWL() && assocSource != null)
                        relateAssociationSourceTarget(opAssoc, assocSource, opTarget);
                }
            }
        }
    }

    // /////////////////////////////////////////////
    // ////////// INITIALIZATION METHODS ///////////
    // ////////////////////////////////////////////

    /**
     * Initialize the Java model from source.
     * 
     */
    protected void initOWLModelFromSource() throws LgConvertException {
        try {
            messages_.info("Before Protege load");
            Snapshot snap = SimpleMemUsageReporter.snapshot();
            messages_.info("Read Time : " + SimpleMemUsageReporter.formatTimeDiff(snap.getTimeDelta(null))
                    + " Heap Usage: " + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsage()) + " Heap Delta:"
                    + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsageDelta(null)));

            BufferedReader r = null;
            JenaOWLModel fileModel = null;
            try {
                // Check if the memory profiling option indicates to create
                // and hold the Protege model in-memory.
                if (memoryProfile_ == ProtegeOwl2EMFConstants.MEMOPT_ALL_IN_MEMORY
                        || memoryProfile_ == ProtegeOwl2EMFConstants.MEMOPT_LEXGRID_DIRECT_DB) {
                    owlModel_ = ProtegeOWL.createJenaOWLModelFromURI(owlURI_.toString());
                    messages_.info("After Protege load into memory");
                    snap = SimpleMemUsageReporter.snapshot();
                    messages_.info("Read Time : " + SimpleMemUsageReporter.formatTimeDiff(snap.getTimeDelta(null))
                            + " Heap Usage: " + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsage())
                            + " Heap Delta:" + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsageDelta(null)));
                }

                // Check if the memory profiling option indicates to cache
                // the Protege model to database after constructing it in
                // memory.
                // This frees memory for doing EMF processing, and performs
                // significantly
                // better than if the Protege model is streamed to cache
                // (without
                // building it in memory first).
                if (memoryProfile_ == ProtegeOwl2EMFConstants.MEMOPT_NON_STREAMING_PROTEGE_DB_AND_LEXGRID_DIRECT_DB) {
                    fileModel = ProtegeOWL.createJenaOWLModelFromReader(r = new BufferedReader(new FileReader(new File(
                            owlURI_.getPath()))));

                    List errors = new ArrayList();
                    Project fileProject = fileModel.getProject();
                    OWLDatabaseKnowledgeBaseFactory factory = new OWLDatabaseKnowledgeBaseFactory();
                    PropertyList sources = PropertyList.create(fileProject.getInternalProjectKnowledgeBase());

                    DatabaseKnowledgeBaseFactory.setSources(sources, dbDriver_, dbUrl_, dbProtegeTempTable_, dbUser_,
                            dbPassword_);
                    factory.saveKnowledgeBase(fileModel, sources, errors);
                    handleProtegeErrors(errors);
                    if (!errors.isEmpty()) {
                        messages_.warn("Unable to load source ontology to database, proceeding with memory model.");
                        owlModel_ = fileModel;
                    } else {
                        Project dbProject = Project.createNewProject(factory, errors);
                        DatabaseKnowledgeBaseFactory.setSources(dbProject.getSources(), dbDriver_, dbUrl_,
                                dbProtegeTempTable_, dbUser_, dbPassword_);

                        dbProject.createDomainKnowledgeBase(factory, errors, true);
                        handleProtegeErrors(errors);
                        owlModel_ = (OWLModel) dbProject.getKnowledgeBase();
                        fileModel.dispose();
                    }

                    messages_.info("After Protege load into temp DB (NON_STREAMING)");
                    snap = SimpleMemUsageReporter.snapshot();
                    messages_.info("Read Time : " + SimpleMemUsageReporter.formatTimeDiff(snap.getTimeDelta(null))
                            + " Heap Usage: " + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsage())
                            + " Heap Delta:" + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsageDelta(null)));

                }

                // Check if the memory option indicates to stream the Protege
                // model to database as it is created. This is the most memory
                // efficient
                // profile, but results in significant performance degradation.
                if (memoryProfile_ == ProtegeOwl2EMFConstants.MEMOPT_STREAMING_PROTEGE_DB_AND_LEXGRID_DIRECT_DB) {
                    CreateOWLDatabaseFromFileProjectPlugin creator = new CreateOWLDatabaseFromFileProjectPlugin();
                    creator.setKnowledgeBaseFactory(new OWLDatabaseKnowledgeBaseFactory());
                    creator.setDriver(dbDriver_);
                    creator.setURL(dbUrl_);
                    creator.setTable(dbProtegeTempTable_);
                    creator.setUsername(dbUser_);
                    creator.setPassword(dbPassword_);
                    creator.setOntologyInputSource(owlURI_);
                    creator.setUseExistingSources(true);

                    Project dbProject = creator.createProject();
                    List errors = new ArrayList();
                    dbProject.save(errors);
                    handleProtegeErrors(errors);

                    // if you forget to do this it will be a mystery when things
                    // go wrong
                    owlModel_ = (OWLModel) dbProject.getKnowledgeBase();

                    messages_.info("After Protege load into temp DB (STREAMING)");
                    snap = SimpleMemUsageReporter.snapshot();
                    messages_.info("Read Time : " + SimpleMemUsageReporter.formatTimeDiff(snap.getTimeDelta(null))
                            + " Heap Usage: " + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsage())
                            + " Heap Delta:" + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsageDelta(null)));

                }

                messages_.info("After Protege load");
                snap = SimpleMemUsageReporter.snapshot();
                messages_.info("Read Time : " + SimpleMemUsageReporter.formatTimeDiff(snap.getTimeDelta(null))
                        + " Heap Usage: " + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsage()) + " Heap Delta:"
                        + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsageDelta(null)));

                // Cache the annotation type for later reference ...
                annotationType_ = owlModel_.getRDFResource(OWLNames.Cls.ANNOTATION_PROPERTY);

                // Report raw OWL counts ...
                int clsCount = owlModel_.getUserDefinedOWLNamedClasses().size();
                messages_.info("OWL file loaded at: " + new Date());
                messages_.info("Total OWL classes = " + clsCount);
            } finally {
                if (r != null)
                    r.close();
            }
        } catch (Exception e) {
            throw new LgConvertException("Unable to read ontology URI.", e);
        }
    }

    /**
     * Initialize tracking of supported behavior specified in LexGrid metadata.
     */
    protected void initSupportedMappings() {
        // Create a helper class used to accumulate mappings
        emfSupportedMappings_ = new EMFSupportedMappings(messages_);

        // Register mappings that will not be explicitly built
        // while processing the OWL source...
        emfSupportedMappings_.registerSupportedDataType(ProtegeOwl2EMFConstants.DATATYPE_BOOLEAN,
                ProtegeOwl2EMFConstants.DATATYPE_BOOLEAN_URI, ProtegeOwl2EMFConstants.DATATYPE_BOOLEAN, false);
        emfSupportedMappings_.registerSupportedDataType(ProtegeOwl2EMFConstants.DATATYPE_STRING,
                ProtegeOwl2EMFConstants.DATATYPE_STRING_URI, ProtegeOwl2EMFConstants.DATATYPE_STRING, false);
        emfSupportedMappings_.registerSupportedPropertyQualifierType(ProtegeOwl2EMFConstants.DATATYPE_STRING,
                ProtegeOwl2EMFConstants.DATATYPE_STRING_URI, ProtegeOwl2EMFConstants.DATATYPE_STRING, false);
        emfSupportedMappings_.registerSupportedPropertyType(ProtegeOwl2EMFConstants.DATATYPE_STRING,
                ProtegeOwl2EMFConstants.DATATYPE_STRING_URI, ProtegeOwl2EMFConstants.DATATYPE_STRING, false);
        emfSupportedMappings_.registerSupportedHierarchy(ProtegeOwl2EMFConstants.SUPP_HIERARCHY_ISA,
                ProtegeOwl2EMFConstants.SUPP_HIERARCHY_ISA_URI, ProtegeOwl2EMFConstants.SUPP_HIERARCHY_ISA,
                ProtegeOwl2EMFConstants.ROOT_CODE, Arrays
                        .asList(ProtegeOwl2EMFConstants.SUPP_HIERARCHY_ISA_ASSOCIATION_LIST), false, false);
    }

    /**
     * Initializes the EMF coding scheme and adds top-level containers for codes
     * and relations.
     * 
     */
    protected void initScheme() {
        // Basics
        emfScheme_ = new CodingScheme();

        // Create top-level container for assigned entities ...
        emfScheme_.setEntities(new Entities());

        // Create top-level containers for relations.
        emfRelationsContainer_Assoc = new Relations();
        emfRelationsContainer_Assoc.setContainerName(ProtegeOwl2EMFConstants.DC_ASSOCIATIONS);
        emfRelationsContainer_Assoc.setIsNative(Boolean.TRUE);
        emfScheme_.addRelations(emfRelationsContainer_Assoc);
        
        // Add this Container to the Supported Mappings
        emfSupportedMappings_
                .registerSupportedContainerName(ProtegeOwl2EMFConstants.DC_ASSOCIATIONS, null, null, false);

        // Create top-level "Roles" containers for relations.
        emfRelationsContainer_Roles = new Relations();
        emfRelationsContainer_Roles.setContainerName(ProtegeOwl2EMFConstants.DC_ROLES);
        emfRelationsContainer_Roles.setIsNative(Boolean.TRUE);
        emfScheme_.addRelations(emfRelationsContainer_Roles);
        // Add this Container to the Supported Mappings
        emfSupportedMappings_.registerSupportedContainerName(ProtegeOwl2EMFConstants.DC_ROLES, null, null, false);

        Mappings mappings = new Mappings();
        emfScheme_.setMappings(mappings);

        NamespaceManager nm = owlModel_.getNamespaceManager();
        for (Iterator i = nm.getPrefixes().iterator(); i.hasNext();) {
            String prefix = i.next().toString();
            if (StringUtils.isNotEmpty(prefix)) {
                emfSupportedMappings_.registerSupportedSource(prefix, nm.getNamespaceForPrefix(prefix), prefix, null,
                        false);
                emfSupportedMappings_.registerSupportedNamespace(prefix, nm.getNamespaceForPrefix(prefix), prefix, "",
                        false);
            }
        }

        // Initialize the coding scheme type.
        initSchemeMetadata();
    }

    /**
     * Initialize further metadata about the coding scheme.
     */
    protected void initSchemeMetadata() {
        String uri = owlModel_.getDefaultOWLOntology().getURI();

        if (uri != null) {
            String localName = owlModel_.getDefaultOWLOntology().getLocalName();

            // The URN w/ protocol and type removed is a second local name
            String localProtocol;
            if (uri.endsWith("#"))
                localProtocol = uri.substring(0, uri.length() - 1);
            else
                localProtocol = uri;
            if (localProtocol.endsWith("/"))
                localProtocol = localProtocol.substring(0, localProtocol.length() - 1);

            if (localProtocol.toLowerCase().startsWith("http://"))
                emfScheme_.addLocalName(localProtocol.substring("http://".length()));
            else if (localProtocol.toLowerCase().startsWith("urn:oid:"))
                emfScheme_.addLocalName(localProtocol.substring("urn:oid:".length()));
            else if (localProtocol.toLowerCase().startsWith("urn:iso:"))
                emfScheme_.addLocalName(localProtocol.substring("urn:oid:".length()));
            else if (localProtocol.toLowerCase().startsWith("urn:lsid:"))
                emfScheme_.addLocalName(localProtocol.substring("urn:lsid:".length()));
            else
                emfScheme_.addLocalName(localProtocol);

            // The rightmost part of the urn is a third local name if it doesn't
            // match the coding scheme
            String localProtocol_csname = "";
            if (localProtocol.contains("/")) {
                localProtocol_csname = localProtocol.substring(localProtocol.lastIndexOf("/") + 1);
                if (!localName.equals(localProtocol_csname))
                    emfScheme_.addLocalName(localProtocol_csname);
            }

            if (localName.trim().length() == 0) {
                localName = localProtocol_csname;
                // if the namespace contains '.owl', then remove it.
                if (localName.indexOf(".owl") != -1) {
                    localName = localName.substring(0, localName.indexOf(".owl"));
                }

            } else {
                emfScheme_.addLocalName(localName);
            }

            // Override with manifest values, if provided. Note that we only
            // need be concerned with identifying information. Other values
            // from the manifest will be applied outside of this loader code.
            String schemeName = localName;

            if (manifest_ != null) {
                CsmfCodingSchemeName codingscheme = manifest_.getCodingScheme();
                CsmfCodingSchemeURI csURI = manifest_.getCodingSchemeURI();

                if (codingscheme != null && codingscheme.getToOverride()) {
                    String mName = codingscheme.getContent();
                    String mUri = csURI != null ? csURI.getContent() : null;
                    if (mName != null)
                        schemeName = mName;
                    if (mUri != null)
                        uri = mUri;
                }
            }

            // Assign and register values.
            // Note that the coding scheme name is always one of the local names
            // and if manifest is specifying
            // any supportedCodingScheme then the default one is not included.
            CsmfMappings mappings = manifest_ != null ? manifest_.getMappings() : null;

            if (manifest_ == null || mappings == null || !mappings.getToUpdate()
                    || mappings.getSupportedCodingScheme().length <= 0)
                emfSupportedMappings_.registerSupportedCodingScheme(schemeName, uri, schemeName, false, false);
            emfScheme_.setCodingSchemeURI(uri);
            emfScheme_.setCodingSchemeName(schemeName);
            emfScheme_.setFormalName(localName);
            EntityDescription ed = new EntityDescription();
            ed.setContent(localName);
            emfScheme_.setEntityDescription(ed);
        }

        // Set the ontology version from the versionInfo tag
        String version = "";
        for (Iterator i = owlModel_.getDefaultOWLOntology().getVersionInfo().iterator(); i.hasNext();) {
            String newVersion = i.next().toString().trim();
            if (version.length() == 0
                    || (newVersion.length() > 0 && version.length() > 0 && newVersion.length() < version.length()))
                version = newVersion;
        }

        if (version.length() == 0) {
            version = "UNASSIGNED";
        }
        if (version.length() > 50) {
            version = version.substring(0, 49);
        }
        emfScheme_.setRepresentsVersion(version);

        // Set the default language
        String defaultLanguage = owlModel_.getDefaultLanguage();
        if (StringUtils.isBlank(defaultLanguage)) {
            defaultLanguage = ProtegeOwl2EMFConstants.LANG_ENGLISH;
        }
        emfScheme_.setDefaultLanguage(defaultLanguage);
        emfSupportedMappings_.registerSupportedLanguage(defaultLanguage, ProtegeOwl2EMFConstants.LANG_URI + ':'
                + defaultLanguage, defaultLanguage, false);
    }

    /**
     * This method initializes the OWL datatype properties. Note that, similar
     * to objecttype properties, we are modeling them as "associations" as well.
     * 
     */
    protected void initSupportedDatatypeProperties() {
        // Initialize OWL to EMF mapping structures ...
        owlDatatypeName2label_ = new HashMap<String, String>();
        owlDatatypeName2emfPropClass_ = new HashMap<String, EClass>();

        int dataTypePropertyCounter = 0;

        for (Iterator props = owlModel_.getRDFProperties().iterator(); props.hasNext();) {
            RDFProperty prop = (RDFProperty) props.next();
            String propertyName = getRDFResourceLocalName(prop);
            // Correlate first assigned label to the primary ID.
            String label = resolveLabel(prop);
            if (isNoopNamespace(label))
                continue;
            // For NCI we do not want the A type properties i.e Properties that
            // are both an
            // annotation property and an Object Property to be treated as a
            // property.
            // They are associations that go into the association container
            if (prop.isAnnotationProperty() && !(prop instanceof OWLObjectProperty)) {
                addToSupportedPropertyAndMap(label, propertyName, prop);
                owlDatatypeName2label_.put(propertyName, label);
            } else if (prop instanceof OWLDatatypeProperty) {

                if (prefManager.getDataTypePropertySwitch().equals("both")
                        || prefManager.getDataTypePropertySwitch().equals("association")) {
                    // Create and register a new association ...
                    AssociationWrapper aw = new AssociationWrapper();
                    
                    aw.setAssociationName(label);
                    aw.setEntityCode(propertyName);
                    
                    aw.setIsReverseFunctional(new Boolean(((OWLDatatypeProperty) prop).isInverseFunctional()));
                    aw.setIsSymmetric(Boolean.FALSE);
                    aw.setIsTransitive(Boolean.FALSE);
                    aw.setIsFunctional(new Boolean(((OWLDatatypeProperty) prop).isFunctional()));
                    String nameSpace = getNameSpace(((OWLDatatypeProperty) prop).getNamespace());
                    aw.setEntityCodeNamespace(nameSpace);
                    aw = assocManager.addAssociation(emfRelationsContainer_Roles, aw);

                    // Add to supported associations ...
                    emfSupportedMappings_.registerSupportedAssociation(propertyName,
                            prop.getNamespace() + propertyName, propertyName, false);

                    // Add the information that this is an datatype
                    // association, and not an objectType property.
                    Property assocDataProp = CreateUtils.createProperty(
                            generatePropertyID(++dataTypePropertyCounter),
                            ProtegeOwl2EMFConstants.PROPNAME_DATATYPEPROPERTY, "true", emfSupportedMappings_);
                    aw.addProperty(assocDataProp);
                    Property assocObjectProp = CreateUtils.createProperty(
                            generatePropertyID(++dataTypePropertyCounter),
                            ProtegeOwl2EMFConstants.PROPNAME_OBJECTPROPERTY, "false", emfSupportedMappings_);
                    aw.addProperty(assocObjectProp);
                    resolveAssociationProperty(aw.getAssociationEntity(), prop);
                }

                if (prefManager.getDataTypePropertySwitch().equals("both")
                        || prefManager.getDataTypePropertySwitch().equals("conceptProperty")) {
                    addToSupportedPropertyAndMap(label, propertyName, prop);
                }

                owlDatatypeName2label_.put(propertyName, label);
            }
        }

    }

    protected void addToSupportedPropertyAndMap(String label, String propertyName, RDFProperty rdfProp) {
        /*
         * This may be somewhat incorrect, because NOT all datatype properties
         * are supportedProperties for a given ontology. This needs to be fixed
         * later based on rule-based approach. Refer to email thread with Tom,
         * Deepak and Pradip on 05/01/2008.
         */
        EClass emfClass = null;
        if (prefManager.getPrioritized_presentation_names().contains(label))
            emfClass = ConceptsPackage.eINSTANCE.getPresentation();
        else if (prefManager.getPrioritized_definition_names().contains(label))
            emfClass = ConceptsPackage.eINSTANCE.getDefinition();
        else if (prefManager.getPrioritized_comment_names().contains(label))
            emfClass = ConceptsPackage.eINSTANCE.getComment();
        else
            emfClass = CommontypesPackage.eINSTANCE.getProperty();

        // Register in supported properties
        emfSupportedMappings_.registerSupportedProperty(propertyName, rdfProp.getNamespace() + propertyName,
                propertyName, false);

        // Register the ID to EMF class mapping; default to
        // generic property class if not mapped above.
        owlDatatypeName2emfPropClass_.put(propertyName, emfClass);
    }

    /**
     * This method determines the various data types that are used in the
     * ontology and stores them.
     * 
     */
    protected void initSupportedDatatypes() {
        // Initialize Datatype to EMF mapping structures.
        owlDatatypeName2emfDatatype_ = new HashMap();

        // Iterate through available datatypes and store for later reference
        for (Iterator props = owlModel_.getRDFProperties().iterator(); props.hasNext();) {
            RDFProperty prop = (RDFProperty) props.next();
            if (prop instanceof OWLDatatypeProperty) {
                String propertyName = getRDFResourceLocalName(prop);

                // See if the label is not part of the namespace
                String label = resolveLabel(prop);
                if (!isNoopNamespace(label)) {
                    // Register the data type representing this property type
                    RDFResource range = prop.getRangeDatatype();
                    if (range != null) {
                        String typeURI = range.getURI();
                        String rangeName = getRDFResourceLocalName(range);
                        if (isNoop(typeURI)) {
                            typeURI = range.getNamespace() + rangeName;
                        }
                        String emfType = null;
                        if (typeURI.indexOf("#") >= 0) {
                            emfType = typeURI.substring(typeURI.lastIndexOf("#") + 1);
                        }
                        if (isNoop(emfType)) {
                            emfType = owlDatatypeName2label_.get(propertyName);
                            typeURI = prop.getNamespace() + propertyName;
                        }
                        owlDatatypeName2emfDatatype_.put(propertyName, emfType);
                        emfSupportedMappings_.registerSupportedDataType(emfType, typeURI, emfType, false);
                    }
                }
            }
        }
    }

    /**
     * This method initializes the user defined OWL object properties in the
     * ontology.
     */
    protected void initSupportedObjectProperties() {
        int objectPropertyCounter = 0;

        // Iterate through available OWL object properties and register
        // associations for later reference ...
        for (Iterator props = owlModel_.getUserDefinedOWLObjectProperties().iterator(); props.hasNext();) {
            OWLObjectProperty owlProp = (OWLObjectProperty) props.next();
            String propertyName = getRDFResourceLocalName(owlProp);
            // Correlate all assigned labels to the primary ID.
            // String label = propertyName;
            Collection rdfLabels = owlProp.getLabels();
            if (rdfLabels.size() == 0) {
                rdfLabels = new ArrayList();
                rdfLabels.add(propertyName);
            }

            String label = rdfLabels.iterator().next().toString();
            label = getFromLastIndexOfColonOrHash(label);
            // Create and register a new association ...
            AssociationWrapper aw = new AssociationWrapper();
            
            aw.setEntityCode(propertyName);
            aw.setAssociationName(label);
            aw.setForwardName(getAssociationLabel(label, true));
            aw.setReverseName(getAssociationLabel(label, false));
            aw.setIsReverseFunctional(owlProp.isInverseFunctional());
            aw.setIsSymmetric(owlProp.isSymmetric());
            aw.setIsTransitive(owlProp.isTransitive());
            aw.setIsFunctional(owlProp.isFunctional());
            String nameSpace = getNameSpace(owlProp.getNamespace());
            aw.setEntityCodeNamespace(nameSpace);
            // Register as role or association and, if applicable,
            // create a target of owl:AnnotationProperty.
            if (owlProp.getRDFTypes().contains(annotationType_)) {
                aw = assocManager.addAssociation(emfRelationsContainer_Assoc, aw);
            } else {
                aw = assocManager.addAssociation(emfRelationsContainer_Roles, aw);
            }

            // Add to supported associations ...

            emfSupportedMappings_.registerSupportedAssociation(propertyName, owlProp.getNamespace() + propertyName,
                    label, false);

            // Update 05/13/2008: I am adding this, even though
            // mostly supportedProperties
            // only contain information about datatype properties.
            // Pradip: taking it out.....not sure why we need to add this to
            // supportedProperties
            // emfSupportedMappings_.registerSupportedProperty(propertyName,
            // prop.getNamespace() + propertyName,
            // propertyName, false);

            // Add the information that this is an object property
            // association, and not an datatype property.
            Property assocDataProp = CreateUtils.createProperty(generatePropertyID(++objectPropertyCounter),
                    ProtegeOwl2EMFConstants.PROPNAME_DATATYPEPROPERTY, "false", emfSupportedMappings_);
            aw.addProperty(assocDataProp);
            Property assocObjectProp = CreateUtils.createProperty(generatePropertyID(++objectPropertyCounter),
                    ProtegeOwl2EMFConstants.PROPNAME_OBJECTPROPERTY, "true", emfSupportedMappings_);
            aw.addProperty(assocObjectProp);
            resolveAssociationProperty(aw.getAssociationEntity(), owlProp);
        }
    }

    /**
     * Initialize and return the root node for the subclass hierarchy.
     * 
     * @return Concept
     */
    protected Concept initSubtypeRoot() {
        Concept topThing = new Concept();
        topThing.setEntityCode(ProtegeOwl2EMFConstants.ROOT_CODE);
        EntityDescription ed = new EntityDescription();
        ed.setContent(ProtegeOwl2EMFConstants.ROOT_DESCRIPTION);
        topThing.setEntityDescription(ed);
        topThing.setIsAnonymous(Boolean.TRUE);
        Presentation p = CreateUtils.createPresentation(generatePropertyID(1), ProtegeOwl2EMFConstants.ROOT_NAME,
                ProtegeOwl2EMFConstants.ROOT_DESCRIPTION, Boolean.TRUE, emfSupportedMappings_);
        topThing.addPresentation(p);
        addEntity(topThing);
        return topThing;
    }

    // /////////////////////////////////////////////
    // /////////// UTILITY METHODS /////////////////
    // /////////////////////////////////////////////

    /**
     * Indicates if the given entity code has been registered against a
     * namespace.
     * 
     * @param code
     * @return boolean
     */
    protected boolean isEntityCodeRegistered(String code) {
        return entityCode2NameSpace_.containsKey(code);
    }

    /**
     * Indicates whether the given string represents a null or empty resource.
     * 
     * @param s
     * @return boolean
     */
    protected boolean isNoop(String s) {
        return s == null || s.toLowerCase().startsWith("null") || s.trim().length() == 0;
    }

    /**
     * Indicates whether the given string defines a resource in a non-operative
     * namespace.
     * 
     * @param s
     * @return boolean
     */
    protected boolean isNoopNamespace(String s) {
        return StringUtils.isBlank(s) || s.matches(prefManager.getMatchPattern_noopNamespaces());
    }

    /**
     * Determines if a given concept is a root node or not.
     * 
     * @author
     * @param rdfsNamedClass
     * @return
     */
    protected boolean isRootNode(RDFSNamedClass rdfsNamedClass) {
        if (prefManager.getMatchRootName() != null) {
            String conceptName = resolveConceptID(rdfsNamedClass);
            return prefManager.getMatchRootName().matcher(conceptName).matches();
        } else {
            return rdfsNamedClass.getSuperclasses(false).contains(owlModel_.getOWLThingClass());
        }
    }

    /**
     * Constructs a new property id with the given integer suffix.
     * 
     * @param i
     * @return The corresponding ID.
     */
    protected String generatePropertyID(int i) {
        String idSuffix = Integer.toString(i);
        return "P0000".substring(0, 5 - idSuffix.length()) + idSuffix;
    }

    /**
     * Constructs a new datatype id with the given integer suffix.
     * 
     * @param i
     * @return The corresponding ID.
     */
    protected String generateDatatypeID(int i) {
        String idSuffix = Integer.toString(i);
        return "D0000".substring(0, 5 - idSuffix.length()) + idSuffix;
    }

    /**
     * Constructs a new datatype instance id (e.g., instance of xsd:int is "24")
     * with the given integer suffix.
     * 
     * @param i
     * @return The corresponding ID.
     */
    protected String generateDatatypeInstanceID(int i) {
        String idSuffix = Integer.toString(i);
        return "DI0000".substring(0, 5 - idSuffix.length()) + idSuffix;
    }

    /**
     * Resolve all properties encapsulated by the RDFProperty and assign
     * corresponding EMF properties to the given association.
     * 
     * @param assoc
     * @param rdfProp
     */
    protected void resolveAssociationProperty(AssociationEntity assocEntity, RDFProperty rdfProp) {
        int i = 0;
        for (Iterator itr = rdfProp.getRDFProperties().iterator(); itr.hasNext();) {
            RDFProperty property = (RDFProperty) itr.next();
            String propName = getRDFResourceLocalName(property);
            String resolvedText = resolveRDFText(rdfProp, property);
            Property pro = CreateUtils.createProperty(generatePropertyID(++i), propName, resolvedText,
                    emfSupportedMappings_);
            assocEntity.addProperty(pro);
        }
    }

    /**
     * Return the first label assigned to the given resource, or the rdf
     * resource name if no labels are assigned.
     * 
     * @param rdf
     * @return A text label for the resource.
     */
    protected String resolveLabel(RDFResource rdf) {
        Collection labels = rdf.getLabels();
        String rdfName = null;
        if (!labels.isEmpty()) {
            Object o = labels.iterator().next();
            if (o instanceof String)
                return (String) o;
            if (o instanceof RDFSLiteral) {
                rdfName = ((RDFSLiteral) o).getBrowserText();
                return getFromLastIndexOfColonOrHash(rdfName);
            }
        }
        rdfName = getRDFResourceLocalName(rdf);
        return rdfName;
    }

    /**
     * Return the text value assigned to the given rdf property.
     * 
     * @param rdfResource
     * @param rdfProperty
     * @return The text; empty if not available.
     */
    protected String resolveRDFText(RDFResource rdfResource, Object rdfProperty) {
        Object o = rdfProperty;
        if (o instanceof RDFProperty)
            o = rdfResource.getPropertyValue((RDFProperty) o);
        if (o instanceof RDFSClass)
            o = ((RDFSClass) o).getBrowserText();
        if (o instanceof RDFIndividual)
            o = ((RDFIndividual) o).getBrowserText();
        return (o == null) ? StringUtils.EMPTY : o.toString();
    }

    /**
     * Returns a map from the xml tags imbedded within the given source string
     * to associated text values.
     * <p>
     * Note: This method assumes a non-repeating single-level tag structure, and
     * bypasses formal xml parsing in favor of performance/simplicity.
     * 
     * @param src
     * @return The tag to value mapping; empty if no XML tags are found in the
     *         given string.
     */
    protected Map<String, String> resolveXMLTagsAndValues(String src) {
        Map<String, String> tags2vals = new HashMap<String, String>();
        tags2vals = bxp.parseDocument(src, messages_);
        return tags2vals;
    }

    /**
     * Return the concept identifier mapped to the given rdf resource, or the
     * null if no mapping exists.
     * 
     * @param rdfResource
     * @return java.lang.String
     */
    protected String resolveConceptID(RDFResource rdfResource) {
        String rdfLocalName = getRDFResourceLocalName(rdfResource);
        String code = owlClassName2Conceptcode_.get(rdfLocalName);
        if (code != null)
            return code;
        // Updated on 05/28/08: if the concept ID is null,
        // it means that either the concept has not been processed yet
        // OR this is an anonymous concept. So, we need to return null
        // instead of the rdf name.
        return null;
    }

    /**
     * Return the instance identifier mapped to the given rdf resource name, or
     * the rdf class name if no mapping exists.
     * 
     * @param rdfResource
     * @return java.lang.String
     */
    protected String resolveInstanceID(RDFResource rdfResource) {
        String rdfLocalName = getRDFResourceLocalName(rdfResource);
        String code = owlInstanceName2code_.get(rdfLocalName);
        if (code != null)
            return code;
        return null;
    }

    protected AssociationSource addAssocSrc2Assoc(AssociationWrapper aw, AssociationSource assocSource) {
        String qName = aw.getAssociationEntity().getEntityCode() + "::" + assocSource.getSourceEntityCode();
        AssociationSource source = null;
        if (emfAssocToAssocSrc_.containsKey(qName)) {
            source = (AssociationSource) emfAssocToAssocSrc_.get(qName);
        } else {
            emfAssocToAssocSrc_.put(qName, assocSource);
            aw.getAssociationPredicate().addSource(assocSource);
            source = assocSource;
        }
        return source;
    }

    protected String getAssociationLabel(String associationName, boolean forward) {
        // Honor mappings specified in preferences.
        // If no match, default to the association name in the
        // forward direction only (per GForge #750).
        Map<String, String> map = forward ? prefManager.getMapForwardNames() : prefManager.getMapReverseNames();
        String label = map.get(associationName);
        if (label == null && forward)
            label = associationName;
        return label;
    }

    protected String getFromLastIndexOfColonOrHash(String str) {
        if (str != null && str.lastIndexOf(":") != -1) {
            str = str.substring(str.lastIndexOf(":") + 1);
        }

        if (str != null && str.lastIndexOf("#") != -1) {
            str = str.substring(str.lastIndexOf("#") + 1);
        }
        return str;
    }

    protected String getRDFResourceLocalName(RDFResource rdfResource) {
        String rdfLocalName;
        if (StringUtils.isNotBlank(rdfResource.getLocalName())) {
            rdfLocalName = getFromLastIndexOfColonOrHash(rdfResource.getLocalName());
        } else {
            rdfLocalName = getFromLastIndexOfColonOrHash(rdfResource.getName());
        }
        return rdfLocalName;
    }

    protected String getNameSpace(String str) {
        String nameSpace = owlModel_.getNamespaceManager().getPrefix(str);
        if ((nameSpace == null) || (nameSpace.trim().length() == 0)) {
            nameSpace = emfScheme_.getCodingSchemeName();
        }
        return nameSpace;
    }

    protected void handleProtegeErrors(Collection errors) {
        for (Object o : errors) {
            if (o instanceof Throwable)
                messages_.error("Protege error encountered : ", (Throwable) o);
        }
    }

    // /////////////////////////////////////////////
    // /////////// STORAGE METHODS /////////////////
    // /////////////////////////////////////////////

    protected void addEntity(Entity emfEntity) {
        if (isEntityCodeRegistered(emfEntity.getEntityCode())) {
            messages_.info("Entity " + emfEntity.getEntityCode() + " already exists.");
            return;
        }
        if (memoryProfile_ == ProtegeOwl2EMFConstants.MEMOPT_ALL_IN_MEMORY) {
            emfScheme_.getEntities().addEntity(emfEntity);
        } else {
            try {
                writeEntity(emfEntity);
            } catch (Exception e) {
                // Exception logged by SQLReadWrite
                return;
            }
        }
        entityCode2NameSpace_.put(emfEntity.getEntityCode(), emfEntity.getEntityCodeNamespace());
        if (emfEntity instanceof Concept)
            conceptCount_++;
    }

    protected void writeEntity(Entity entity) throws Exception {
        try {
            if (tempEmfScheme_ == null) {
                tempEmfScheme_ = new CodingScheme();
                tempEmfScheme_.setCodingSchemeName(emfScheme_.getCodingSchemeName());
            }
            if (tempEmfEntityList_ == null) {
                tempEmfEntityList_ = new Entities();
                tempEmfScheme_.setEntities(tempEmfEntityList_);
            }
            tempEmfEntityList_.addEntity(entity);
            
            String uri = emfScheme_.getCodingSchemeURI();
            String version = emfScheme_.getRepresentsVersion();
            
            databaseServiceManager.getEntityService().
                insertEntity(uri, version, entity);

        } finally {
            tempEmfEntityList_.removeEntity(entity);
        }
    }

    /**
     * The RDFResource is used to compute the target. The EntityType is passed
     * into the function so that we know what kind of a lookup needs to be made
     * to get the code. For NCI, the code of the entity could be different from
     * the localName of the resource. We need to do a lookup to get the actual
     * code.
     * 
     * @param type
     * @param assoc
     * @param source
     * @param tgtResource
     */
    protected void relateAssocSourceWithRDFResourceTarget(EntityTypes type, AssociationWrapper aw,
            AssociationSource source, RDFResource tgtResource) {
        String targetID = getRDFResourceLocalName(tgtResource);
        if (type == EntityTypes.CONCEPT) {
            targetID = resolveConceptID(tgtResource);
        } else if (type == EntityTypes.INSTANCE) {
            targetID = resolveInstanceID(tgtResource);
        }

        if (StringUtils.isNotBlank(targetID)) {
            String nameSpace = getNameSpace(tgtResource.getNamespace());
            AssociationTarget target = CreateUtils.createAssociationTarget(targetID, nameSpace);
            relateAssociationSourceTarget(aw, source, target);
        }
    }

    protected void relateAssociationSourceTarget(AssociationWrapper aw, AssociationSource source, AssociationTarget target) {
        if (memoryProfile_ == ProtegeOwl2EMFConstants.MEMOPT_ALL_IN_MEMORY) {
            source = addAssocSrc2Assoc(aw, source);
            if (target != null) {
                RelationsUtil.subsume(source, target);
            }
        } else {
            writeAssociationSourceTarget(aw.getAssociationPredicate(), source, target);
        }
    }

    protected void relateAssociationSourceData(AssociationWrapper aw, AssociationSource source, AssociationData data) {
        if (memoryProfile_ == ProtegeOwl2EMFConstants.MEMOPT_ALL_IN_MEMORY) {
            source = addAssocSrc2Assoc(aw, source);
            if (data != null) {
                source.addTargetData(data);
            }
        } else {
            if (data != null)
                source.addTargetData(data);
            writeAssociationSourceTarget(aw.getAssociationPredicate(), source, null);
        }
    }

    protected void writeAssociationSourceTarget(AssociationPredicate assoc, AssociationSource source, AssociationTarget target) {
        try {
            if (StringUtils.isEmpty(source.getSourceEntityCode())) {
                messages_.warn("Unable to write sourceTarget relationship: Source entity code not assigned.");
                return;
            }

            // If writing only source with target data, the association
            // target will be null. There is also a chance that the
            // targetEntityCode is null. This is possible and is valid
            // when the sourceEntityCode is an association. For example,
            // association 'range' can have sourceEntityCode
            // 'has_database' without any targetEntityCode.
            // In this case, set the targetEntityCode as empty string
            // so that database will not complain during insertion.
            if (target != null) {
                if (StringUtils.isEmpty(target.getTargetEntityCode()))
                    target.setTargetEntityCode(" ");
                source.addTarget(target);
            }

            assoc.addSource(source);
            
            String uri = emfScheme_.getCodingSchemeURI();
            String version = emfScheme_.getRepresentsVersion();
            
            AssociationWrapper wrapper = this.assocManager.getAssociation(assoc.getAssociationName());
            databaseServiceManager.getAssociationService().
                insertAssociationSource(uri, version, 
                        wrapper.getRelationsContainerName(), assoc.getAssociationName(), source);

        } catch (Exception e) {
            this.messages_.error("Error Inserting AssociationSource.", e);
        } finally {
            assoc.removeSource(source);
            if (target != null)
                source.removeTarget(target);
        }
    }

    protected void updateApproximateConceptNumber() {
        if (memoryProfile_ == ProtegeOwl2EMFConstants.MEMOPT_ALL_IN_MEMORY) {
            emfScheme_.setApproxNumConcepts(new Long(emfScheme_.getEntities().getEntity().length));
        } else {
            attributeMap_ = new HashMap<String, Object>();
            attributeMap_.put(SQLTableConstants.TBLCOL_APPROXNUMCONCEPTS, new Integer(conceptCount_));
            whereClause_ = new StringBuffer();
            whereClause_.append(SQLTableConstants.TBLCOL_CODINGSCHEMEURI + " = ");
            whereClause_.append("\'");
            whereClause_.append(emfScheme_.getCodingSchemeURI());
            whereClause_.append("\'");
            whereClause_.append(" AND ");
            whereClause_.append(SQLTableConstants.TBLCOL_REPRESENTSVERSION + " = ");
            whereClause_.append("\'");
            whereClause_.append(emfScheme_.getRepresentsVersion());
            whereClause_.append("\'");
            try {
                sqlTableUtil_.updateRow(SQLTableConstants.TBL_CODING_SCHEME, attributeMap_, whereClause_.toString(),
                        dbType_);
            } catch (SQLException e) {
                messages_.error("Error occurred updating the concept count : ", e);
            }
        }

    }

} // end of the class