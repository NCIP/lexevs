/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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
package edu.mayo.informatics.lexgrid.convert.directConversions.owlapi;

import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.LexOnt.CodingSchemeManifest;
import org.LexGrid.LexOnt.CsmfCodingSchemeName;
import org.LexGrid.LexOnt.CsmfCodingSchemeURI;
import org.LexGrid.LexOnt.CsmfMappings;
import org.LexGrid.LexOnt.CsmfVersion;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.types.EntityTypes;
import org.LexGrid.commonTypes.types.PropertyTypes;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
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
import org.LexGrid.versions.ChangedEntry;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.types.ChangeType;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.locator.LexEvsServiceLocator;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.XMLUtils;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxPrefixNameShortFormProvider;
import org.semanticweb.owlapi.model.DataRangeType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationObjectVisitorEx;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLCardinalityRestriction;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataOneOf;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointUnionAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLHasValueRestriction;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLNamedObject;
import org.semanticweb.owlapi.model.OWLNaryBooleanClassExpression;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasSelf;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLObjectVisitorEx;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.model.OWLPropertyExpression;
import org.semanticweb.owlapi.model.OWLQuantifiedDataRestriction;
import org.semanticweb.owlapi.model.OWLQuantifiedObjectRestriction;
import org.semanticweb.owlapi.model.OWLRestriction;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import com.hp.hpl.jena.vocabulary.RDF;

import edu.mayo.informatics.lexgrid.convert.Conversions.SupportedMappings;
import edu.mayo.informatics.lexgrid.convert.exceptions.LgConvertException;
import edu.stanford.smi.protegex.owl.model.RDFSNames;
import uk.ac.manchester.cs.owl.owlapi.OWL2DatatypeImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLDatatypeImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLAnnotationPropertyRangeAxiomImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLDataOneOfImpl;

/**
 * This is the main class containing the logic for the conversion from OWL to
 * LexEVS..
 * 
 * 
 * Last modified on: May 14, 2013
 * 
 * @author Pradip Kanjamala (kanjamala.pradip@mayo.edu)
 * 
 *         For NCI, they want to see the OWL Object Properties that are also
 *         annotation properties loaded as Associations in the association
 *         container
 */
public class OwlApi2LG {
    /* Define some global variables */
    // Input & output ...
    private URI owlURI_ = null;

    private OWLOntology ontology = null;
    OWLReasoner reasoner;
    private LoaderPreferences loadPrefs_ = null;
    private CodingSchemeManifest manifest_ = null;
    private LgMessageDirectorIF messages_ = null;

    MachesterOWLSyntaxLexGridRenderer renderer;
    // Memory profile option ...
    private int memoryProfile_ = OwlApi2LGConstants.MEMOPT_LEXGRID_DIRECT_DB;

    // Generated EMF objects ...
    private CodingScheme lgScheme_ = null;
    private SupportedMappings lgSupportedMappings_ = null;

    private Relations lgRelationsContainer_Assoc = null;
    private Relations lgRelationsContainer_Roles = null;

    private CodingScheme tempEmfScheme_ = null;
    private Entities tempEmfEntityList_ = null;

    // Shared mapping information ...
    // private Map<String, Object> attributeMap_ = null;
    private Map<String, String> owlDatatypeName2label_ = new HashMap<String, String>();
    private Map<String, String> owlDatatypeName2lgPropClass_ = new HashMap<String, String>();
    private Map<String, String> owlDatatypeName2lgDatatype_ = null;
    private Map<String, String> owlClassName2Conceptcode_ = new HashMap<String, String>();
    private Set<String> registeredNameSpaceCode_ = new HashSet<String>();
    private Map<String, AssociationSource> lgAssocToAssocSrc_ = new HashMap<String, AssociationSource>();
    private Map<String, String> owlInstanceName2code_ = null;
    private Map<String, String> owlAnnotationPropertiesTocode_  = null;
    private Map<IRI, Boolean> owlIRIToIsAnyUIRDataType_  = new HashMap<IRI, Boolean>();
    
    //this Map provides us with a set of values that can be used to determine punned individuals
//    private Map<String, String> owlPunnedClassesToCode_ = new HashMap<String, String>();

    // Cached values and state
    private int conceptCount_ = 0;
    AssociationManager assocManager = null;
    PreferenceManager prefManager = null;
    PropertyComparator propertyComparator;

    // Complex property parser
    private BasicXMLParser bxp;

    private DatabaseServiceManager databaseServiceManager;

    private Map<String, OWLObjectPropertyExpression> inversePropCache;

     OWLOntologyManager manager = null;
     OWLDataFactory factory = null;
     
    /**
     * Create a new instance for conversion.
     * 
     * @param owlURI
     *            The OWL input file.
     * @param manifest
     *            The OWL manifest Object
     * @param loadPrefs
     * @param memoryMode
     * @param messages
     *            Responsible for handling display of program messages to the
     *            user.
     */
    public OwlApi2LG(URI owlURI, CodingSchemeManifest manifest, LoaderPreferences loadPrefs, int memoryMode,
            LgMessageDirectorIF messages) {
        super();
        owlURI_ = owlURI;
        messages_ = messages;
        this.manifest_ = manifest;
        this.memoryProfile_ = memoryMode;
        this.loadPrefs_ = loadPrefs;
        bxp = new BasicXMLParser();
        databaseServiceManager = LexEvsServiceLocator.getInstance().getDatabaseServiceManager();
        try{
        manager = OWLManager.createOWLOntologyManager();
        factory = manager.getOWLDataFactory();
        }catch(Throwable t){
            t.printStackTrace();
        }
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
        initOWLOntologyFromSource();

        // Create the EMF model
        try {

            initSupportedMappings();
            initScheme();
            initSupportedDatatypes();
            assocManager = new AssociationManager(lgSupportedMappings_, lgRelationsContainer_Assoc,
                    lgRelationsContainer_Roles);
            initAnnotationProperties();
            initSupportedDataProperties();
            initSupportedObjectProperties();
            initSupportedAssociationAnnotationProperties();

            try {
                // If we are streaming the LexGrid model to database, write
                // the coding scheme metadata as defined so far.
                if (memoryProfile_ != OwlApi2LGConstants.MEMOPT_ALL_IN_MEMORY) {
                    databaseServiceManager.getAuthoringService().loadRevision(lgScheme_, null, null);
                }
            } catch (Exception e) {
                throw new RuntimeException("Owl2 formatted scheme failed to load: ", e);
            }

            initAssociationEntities();

            // Populate the coding scheme from the OWL model
            initSubtypeRoot();
            processOWL();

            // Apply all supported attributes that have been registered
            // over the course of processing the OWL model ...
            if (lgSupportedMappings_.getSupportedAssociations().size() > 0) {
                String name = EntityTypes.ASSOCIATION.toString();
                lgSupportedMappings_.registerSupportedEntityType(name, null, name, false);
            }

            lgSupportedMappings_.applyToCodingScheme(lgScheme_);

            if (memoryProfile_ != OwlApi2LGConstants.MEMOPT_ALL_IN_MEMORY) {
                final String uri = lgScheme_.getCodingSchemeURI();
                final String version = lgScheme_.getRepresentsVersion();

                databaseServiceManager.getDaoCallbackService().executeInDaoLayer(new DaoCallback<Object>() {
                    public Object execute(DaoManager daoManager) {
                        String codingSchemeId = daoManager.getCodingSchemeDao(uri, version)
                                .getCodingSchemeUIdByUriAndVersion(uri, version);

                        daoManager.getCodingSchemeDao(uri, version).insertMappings(codingSchemeId,
                                lgScheme_.getMappings());

                        return null;
                    }
                });
            }

            // Register the number of concepts found and return the scheme
            updateApproximateConceptNumber();
            return lgScheme_;
        } catch (Exception e) {
            throw new LgConvertException(e);

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
        // Iterate through owl classes and generate LexEVS concepts
        // Resolve all concepts before working on relations, since we need to
        // ensure all concept codes are correctly resolved (sometimes buried
        // in class definition) before setting source and target references.

        messages_.info("Processing OWL Classes.....");
        processAllConceptsAndProperties(snap);
 //       processAllAnnotationProperties(snap);
        // Step 2: Process OWL individuals. Essentially, determine to which
        // classes these instances belong to, as well as, relations between
        // the individuals themselves (e.g., differentFrom)?

        messages_.info("Processing OWL Individuals.....");
        processAllInstanceAndProperties(snap);
        initPunnedInstanceNamesToCode();
        
        // Step 3: Process all the concept relations
        processAllConceptsRelations();

        // Step 4: Process all the instance relations
        processAllInstanceRelations();

        // Step 5: Process the OWL Object properties. Essentially, determine
        // the domain and ranges for the properties and relationships to other
        // properties.

        messages_.info("Processing OWL Object Properties.....");
        processOWLObjectProperties(snap);

        // Step 6: Process the OWL data properties. Essentially, determine
        // the domain and data ranges for the properties and relationships to
        // other properties.

        messages_.info("Processing OWL Datatype Properties.....");
        processOWLDataProperties(snap);

    }

    /**
     * This method is responsible for processing of all the OWL concepts.
     * 
     */
    protected void processAllConceptsAndProperties(Snapshot snap) {
        int count = 0;
                
        // The idea is to iterate through all the OWL classes
        messages_.info("Processing concepts: ");
        for (Object namedClass : ontology.classesInSignature().toArray()) {
            resolveConcept((OWLClass) namedClass);
            count++;
            if (count % 5000 == 0) {
                messages_.info("OWL classes processed: " + count);
            }
        }
        messages_.info("Total OWL classes processed: " + count);
        snap = SimpleMemUsageReporter.snapshot();
        messages_.info("Read Time : " + SimpleMemUsageReporter.formatTimeDiff(snap.getTimeDelta(null))
                + " Heap Usage: " + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsage()) + " Heap Delta:"
                + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsageDelta(null)));

        // If we found at least one, register the supported entity type.
        if (count > 0) {
            String name = EntityTypes.CONCEPT.toString();
            lgSupportedMappings_.registerSupportedEntityType(name, null, name, false);
        }

    }

    protected void processAllConceptsRelations() {
        messages_.info("Processing concept relationships ...");
        for (OWLClass namedClass : ontology.getClassesInSignature()) {
            String lgConceptCode = resolveConceptID(namedClass);
            String namespace = getNameSpace(namedClass);
            if (lgConceptCode != null) {
                AssociationSource source = CreateUtils.createAssociationSource(lgConceptCode, namespace);
                resolveEquivalentClassRelations(source, namedClass);
                resolveSubClassOfRelations(source, namedClass);
                resolveDisjointWithRelations(source, namedClass);
                resolveDisjointUnionRelations(source, namedClass);
//                resolveComplementOfRelations(source, namedClass);
                resolveOWLObjectPropertyRelations(source, namedClass);
                resolveAnnotationPropertyRelations(source, namedClass);
                // resolveDatatypePropertyRelations(source, namedClass);
            }

        }

    }

    private void resolveAnnotationPropertyRelations(AssociationSource source, OWLClass owlClass) {

        for (OWLAnnotationAssertionAxiom annotationAxiom : ontology.getAnnotationAssertionAxioms(owlClass.getIRI())) {
            String propName = getLocalName(annotationAxiom.getProperty());
                                   
            Boolean isAnyURIDataType = owlIRIToIsAnyUIRDataType_.get(annotationAxiom.getProperty().getIRI());   
            if (isAnyURIDataType) {
                AssociationWrapper lgAssoc = assocManager.getAssociation(propName);
                if (lgAssoc == null) {
                    return;
                }
                OWLAnnotation anno = annotationAxiom.getAnnotation();
                String prefix = owlClass.getIRI().getShortForm();
                relateAssocSourceWithAnnotationTarget(EntityTypes.CONCEPT,  lgAssoc,
                       source, anno, annotationAxiom, prefix);
            }
            
//            for( OWLClassAxiom classAx : ontology.getAxioms(owlClass)){
//                //TODO
//            }
            
            Iterator<OWLAnnotation> itr = EntitySearcher.getAnnotations(owlClass,ontology).iterator();
            while (itr.hasNext()) {
                OWLAnnotation annot = itr.next();
                if (annot.getValue() instanceof IRI) {
                    if (ontology.containsIndividualInSignature((IRI) annot.getValue())) {
                        if (ontology.getEntitiesInSignature((IRI) annot.getValue()).iterator().next() instanceof OWLNamedIndividual) {
                            OWLAnnotation anno = annotationAxiom.getAnnotation();
                            String prefix = owlClass.getIRI().getNamespace();
                            AssociationWrapper lgAssoc = assocManager.getAssociation(propName);
                            if (lgAssoc == null) {
                                lgAssoc = addAssociation(anno);
                                AssociationPredicate pred = lgAssoc.getAssociationPredicate();
                                storeAssociationPredicateIfNeeded(lgScheme_.getCodingSchemeURI(), lgScheme_.getRepresentsVersion(), 
                                        lgAssoc.getRelationsContainerName(), pred);
                                addAnnotationPropertyAssociations(anno.getProperty());
                            }
                            relateAssocSourceWithAnnotationTarget(EntityTypes.INSTANCE, lgAssoc, source, anno,
                                    annotationAxiom, prefix);
                        }
                    }

                    // No current use case for this, but it's there if needed.
//                    if (ontology.containsClassInSignature((IRI) annot.getValue())) {
//                        if (ontology.getEntitiesInSignature((IRI) annot.getValue()).iterator().next() instanceof OWLClass) {
//                            AssociationWrapper lgAssoc = assocManager.getAssociation(propName);
//                            if (lgAssoc == null) {
//                                return;
//                            }
//                            OWLAnnotation anno = annotationAxiom.getAnnotation();
//                            String prefix = owlClass.getIRI().getNamespace();
//                            relateAssocSourceWithAnnotationTarget(EntityTypes.INSTANCE, lgAssoc, source, anno,
//                                    annotationAxiom, prefix);
//                        }
//                    }
            }
            }

        }

    }

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

        for (OWLObjectProperty prop : ontology.getObjectPropertiesInSignature()) {

            // ///////////////////////////////////
            // /// Process Domain and Ranges /////
            // //////////////////////////////////
            
            // Get the appropriate association name initialized earlier
            // (initSupportedObjectProperties)
            String propertyName = getLocalName(prop);
            AssociationWrapper lgAssoc = assocManager.getAssociation(propertyName);
            String nameSpace = getNameSpace(prop);
            AssociationSource source = CreateUtils.createAssociationSource(lgAssoc.getAssociationEntity()
                    .getEntityCode(), nameSpace);

            // The idea is to create a new association called "domain", whose
            // LHS will be the OWLObjectProperty and RHS will be the domain.
            for (OWLClassExpression domain : EntitySearcher.getDomains(prop,ontology).collect(Collectors.toList())) {
                relateAssocSourceWithOWLClassExpressionTarget(EntityTypes.CONCEPT, assocManager.getDomain(), source,
                        domain, null);
            }

            // The idea is to create a new association called "range", whose
            // LHS will be the OWLObjectProperty and RHS will be the range.
            for (OWLClassExpression range : EntitySearcher.getRanges(prop, ontology).collect(Collectors.toList())) {
                relateAssocSourceWithOWLClassExpressionTarget(EntityTypes.CONCEPT, assocManager.getRange(), source,
                        range, null);
            }

            // //////////////////////////////////////////////////
            // /// Process Property Hierarchy/Relationships /////
            // /////////////////////////////////////////////////

            // Step 1: process subPropertyOf: here also we create
            // an association between associations.

            for (OWLObjectPropertyExpression superProp : EntitySearcher.getSuperProperties(prop,ontology).collect(Collectors.toList())) {
                relateAssocSourceWithRDFResourceTarget(EntityTypes.ASSOCIATION, assocManager.getSubPropertyOf(),
                        source, superProp);
            }

            // Step 2: process inverseProperties
            if (prop.getInverseProperty() != null) {
                relateAssocSourceWithRDFResourceTarget(EntityTypes.ASSOCIATION, assocManager.getInverseOf(), source,
                        prop.getInverseProperty());
            }

            // Step 3: process equivalentProperties
            for (OWLObjectPropertyExpression equivalent : EntitySearcher.getEquivalentProperties(prop, ontology).collect(Collectors.toList())) {
                relateAssocSourceWithRDFResourceTarget(EntityTypes.ASSOCIATION, assocManager.getEquivalentProperty(),
                        source, equivalent);
            }

            // Step 4: process functional, inverse functional and transitive
            // properties
            // for (Iterator iter = prop.getRDFTypes().iterator();
            // iter.hasNext();) {
            // RDFSClass rdfType = (RDFSClass) iter.next();
            // relateAssocSourceWithRDFResourceTarget(EntityTypes.ASSOCIATION,
            // assocManager.getRdfType(), source,
            // rdfType);
            // }
        } // end of for.
    }

    /**
     * This method determines the domain and ranges for the OWL DataProperties.
     * It also processes different relationships between the properties.
     * 
     */
    protected void processOWLDataProperties(Snapshot snap) {
        snap = SimpleMemUsageReporter.snapshot();
        messages_.info("Read Time : " + SimpleMemUsageReporter.formatTimeDiff(snap.getTimeDelta(null))
                + " Heap Usage: " + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsage()) + " Heap Delta:"
                + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsageDelta(null)));
        messages_.info("Processing OWL Datatype Properties ...");
        //We are doing datatypes as entities.  We'd better register them as supported
        if(!ontology.getDataPropertiesInSignature().isEmpty()){
            String name = "datatype";
            lgSupportedMappings_.registerSupportedEntityType(name, null, name, false);
        }
        for (OWLDataProperty prop : ontology.getDataPropertiesInSignature()) {

            // Check if the data type property is an annotation property. We do
            // not treat annotation properties as associations.
            // if (isAnnotationProperty(prop)) {
            // continue;
            // }
            // resolveAssociation(prop);

            // ///////////////////////////////////
            // /// Process Domain and Ranges /////
            // //////////////////////////////////

            // Get the appropriate association name initialized earlier
            // (initSupportedObjectProperties)
            String propertyName = getLocalName(prop);
            AssociationWrapper lgAssoc = assocManager.getAssociation(propertyName);
            String nameSpace = getNameSpace(prop);
            AssociationSource source = CreateUtils.createAssociationSource(lgAssoc.getAssociationEntity()
                    .getEntityCode(), nameSpace);

            // The idea is to create a new association called "hasDomain", whose
            // LHS will be the OWLDatatyeProperty and RHS will be the
            // RDFSDatatype.

            for (OWLClassExpression domain : EntitySearcher.getDomains(prop,ontology).collect(Collectors.toList())) {
                relateAssocSourceWithOWLClassExpressionTarget(EntityTypes.CONCEPT, assocManager.getDomain(), source,
                        domain, null);
            }

            for (OWLDataRange range : EntitySearcher.getRanges(prop,ontology).collect(Collectors.toList())) {
                AssociationData data = CreateUtils.createAssociationTextData(renderer.render(range));
                relateAssociationSourceData(assocManager.getDatatype(), source, data);
            }

            // //////////////////////////////////////////////////
            // /// Process Property Hierarchy/Relationships /////
            // /////////////////////////////////////////////////
            for (OWLDataPropertyExpression superProp : EntitySearcher.getSuperProperties(prop,ontology).collect(Collectors.toList())) {
                relateAssocSourceWithRDFResourceTarget(EntityTypes.ASSOCIATION, assocManager.getSubPropertyOf(),
                        source, superProp);
            }

            for (OWLDataPropertyExpression equivalent : EntitySearcher.getEquivalentProperties(prop,ontology).collect(Collectors.toList())){
                relateAssocSourceWithRDFResourceTarget(EntityTypes.ASSOCIATION, assocManager.getEquivalentProperty(),
                        source, equivalent);
            }

        } // end of for
    }

    /**
     * Defines an EMF concept and properties based on the given rdf source.
     * 
     * @param owlClass
     *            The resource to evaluate.
     * @return The resolved concept; null if a new concept was not generated.
     */
    protected Entity resolveConcept(OWLClass owlClass) {
        String rdfName = getLocalName(owlClass);

        if (isNoopNamespace(rdfName))
            return null;

        if (owlClassName2Conceptcode_.containsKey(owlClass.getIRI().toString()))
            return null;
        
        if(isNoopTopOrBottomOWLEntity(owlClass)){
            return null;
        }

        String label = resolveLabel(owlClass);

        // Create the concept and assign label as initial description,
        // which may be overridden later by preferred text.
        Entity concept = new Entity();
        concept.setEntityType(new String[] { EntityTypes.CONCEPT.toString() });
        EntityDescription ed = new EntityDescription();
        ed.setContent(label);
        concept.setEntityDescription(ed);
        concept.setEntityCode(rdfName);
        concept.setIsAnonymous(Boolean.FALSE);

        String nameSpace = getNameSpace(owlClass);
        concept.setEntityCodeNamespace(nameSpace);

        // Is deprecated? If so, mark as inactive.
        /*
         * if (rdfResource instanceof OWLNamedClass &&
         * OWLNames.Cls.DEPRECATED_CLASS
         * .equals(rdfResource.getRDFType().getName()))
         * concept.setIsActive(Boolean.FALSE);
         */
        // Set the 'isDefined' property.
        concept.setIsDefined( EntitySearcher.isDefined(owlClass, ontology));

        // Resolve all the concept properties and add to entities.
        resolveEntityProperties(concept, owlClass);     
        addEntity(concept);

        // Remember the rdf to code mapping and return.
        owlClassName2Conceptcode_.put(owlClass.getIRI().toString(), concept.getEntityCode());

        return concept;
    }

    private boolean isNoopTopOrBottomOWLEntity(OWLClass owlClass) {
        if(owlClass.isTopEntity() || owlClass.isBottomEntity()){
            return true;
        }
        else return false;
    }

    /**
     * Defines an EMF concept and properties based on the given rdf source.
     * 
     * @param owlProp
     *            The resource to evaluate.
     * @return The resolved concept; null if a new concept was not generated.
     */
    protected Entity addPropertiesToAssociationEntity(Entity lgEntity, OWLEntity owlProp) {

        String rdfName = getLocalName(owlProp);
        if (isNoopNamespace(rdfName))
            return null;

        if (owlClassName2Conceptcode_.containsKey(owlProp.getIRI().toString()))
            return null;

        String label = resolveLabel(owlProp);

        // Create the raw EMF concept and assign label as initial description,
        // which may be overridden later by preferred text.
        EntityDescription ed = new EntityDescription();
        ed.setContent(label);
        lgEntity.setEntityDescription(ed);
        lgEntity.setEntityCode(rdfName);
        lgEntity.setIsAnonymous(Boolean.FALSE);

        String nameSpace = getNameSpace(owlProp);
        lgEntity.setEntityCodeNamespace(nameSpace);

        // Resolve all the concept properties and add to entities.
        resolveEntityProperties(lgEntity, owlProp);

        // Remember the rdf to code mapping and return.
        owlClassName2Conceptcode_.put(owlProp.getIRI().toString(), lgEntity.getEntityCode());

        return lgEntity;
    }

    protected void resolveSubClassOfRelations(AssociationSource source, OWLClass owlClass) {
        // Process parent-child (rdfs:subClassOf) relationships
        // Does this concept represent the root of a concept branch that should
        // be centrally linked to the top node for subclass traversal?;
        if (isRootNode(owlClass)) {
            // always give the root node the default namespace
            
            AssociationTarget target = CreateUtils.createAssociationTarget(OwlApi2LGConstants.ROOT_CODE,
                    getUserSetNamespace());
            relateAssociationSourceTarget(assocManager.getSubClassOf(), source, target);

        }
        

        
        
        Set<OWLClass> statedSubClasses= new HashSet<OWLClass>();
        for (OWLSubClassOfAxiom ax : ontology.getSubClassAxiomsForSubClass(owlClass)) {
                relateAssocSourceWithOWLClassExpressionTarget(EntityTypes.CONCEPT, assocManager.getSubClassOf(),
                        source, ax.getSuperClass(), ax);
                if (!ax.getSuperClass().isAnonymous()) {
                   statedSubClasses.add(ax.getSuperClass().asOWLClass());
                }
        }
        
        //The reasoner.getSuperClasses doesn't return the anonymous classes. The ontology.getSubClassAxiomsForSubClass
        //method doesn't have information that can be found using the reasoner, so we add in the reasoned expressions.
        Set<OWLClass> reasonedSubClasses= new HashSet<OWLClass>();
        Iterator<Node<OWLClass>> itr = reasoner.getSuperClasses(owlClass, true).getNodes().iterator();
        while(itr.hasNext()){
            reasonedSubClasses.addAll(itr.next().getEntities());
        }
//        reasonedSubClasses.addAll(reasoner.getSuperClasses(owlClass, true).getNodes().iterator().next().getEntities());
        reasonedSubClasses.removeAll(statedSubClasses);
        for (OWLClassExpression superClass : reasonedSubClasses) {
            relateAssocSourceWithOWLClassExpressionTarget(EntityTypes.CONCEPT, assocManager.getSubClassOf(), source,
                    superClass, null);
        }
        
        //The reasoner.getSuperClasses doesn't return the anonymous classes, so we process them separately.
//        for (OWLClassExpression superClass : owlClass.getSuperClasses(ontology)) {
//            if (superClass.isAnonymous()) {
//                relateAssocSourceWithOWLClassExpressionTarget(EntityTypes.CONCEPT, assocManager.getSubClassOf(),
//                        source, superClass);
//              
//            }
//
//        }

    }

    /**
     * 
     * Defines EMF equivalentClass relations based on OWL source.
     * 
     */
    protected void resolveEquivalentClassRelations(AssociationSource source, OWLClass owlClass) {
        for (OWLEquivalentClassesAxiom equivClassAxiom : ontology.getEquivalentClassesAxioms(owlClass)) {
            for (OWLClassExpression equivClassExpression : equivClassAxiom.getClassExpressionsMinus(owlClass)) {
                relateAssocSourceWithOWLClassExpressionTarget(EntityTypes.CONCEPT, assocManager.getEquivalentClass(),
                        source, equivClassExpression, equivClassAxiom);
            }
        }
    }

    /**
     * Defines EMF DisjointWith relations based on OWL source.
     * 
     */
    protected void resolveDisjointWithRelations(AssociationSource source, OWLClass owlClass) {
        for (OWLDisjointClassesAxiom disjointClassAxiom : ontology.getDisjointClassesAxioms(owlClass)) {
            for (OWLClassExpression disjointClassExpression : disjointClassAxiom.getClassExpressionsMinus(owlClass)) {
                relateAssocSourceWithOWLClassExpressionTarget(EntityTypes.CONCEPT, assocManager.getDisjointWith(),
                        source, disjointClassExpression, disjointClassAxiom);
            }
        }

    }
    
    
    private void resolveDisjointUnionRelations(AssociationSource source, OWLClass namedClass) {
        for (OWLDisjointUnionAxiom disjointUnionClassAxiom : ontology.getDisjointUnionAxioms(namedClass)) {
            for (OWLClassExpression disjointClassExpression : disjointUnionClassAxiom.getClassExpressions()) {
                relateAssocSourceWithOWLClassExpressionTarget(EntityTypes.CONCEPT, assocManager.getDisjointUnion(),
                        source, disjointClassExpression, disjointUnionClassAxiom);
            }
        }
        
    }

    /**
     * 
     * @param restriction
     * @param assocParentSource
     *            - The assocSource that should be added if not processing strict
     *            owl
     * @param source
     *            - The normal source of the association
     */
    protected void processRestriction(OWLRestriction restriction, AssociationSource assocParentSource,
            AssociationSource source) {
        // Operand defines a restriction placed on the anonymous
        // node...
        OWLEntity onProp = getEntity(restriction.getProperty());
        if (onProp != null) {
            String assocName = getLocalName(onProp);
            AssociationWrapper opAssoc = assocManager.getAssociation(assocName);
            if (opAssoc != null) {
                // to be intialized later depending on the need.
                AssociationData opData = null;
                String targetCode = null;
                String targetNameSpace = null;
                
                if (restriction instanceof OWLQuantifiedObjectRestriction) {
                    OWLQuantifiedObjectRestriction rest = (OWLQuantifiedObjectRestriction) restriction;
                    OWLClassExpression fillerProp = rest.getFiller();
                    if (fillerProp.isAnonymous()) {
                        targetCode = resolveAnonymousClass(fillerProp, assocParentSource);
                    } else { // Set the OWL Class as the range
                        OWLClass namedClass = fillerProp.asOWLClass();
                        targetCode = resolveConceptID(namedClass);
                        targetNameSpace = getNameSpace(namedClass);

                    }
                }
                if (restriction instanceof OWLQuantifiedDataRestriction) {
                    OWLQuantifiedDataRestriction rest = (OWLQuantifiedDataRestriction) restriction;
                    OWLDataRange fillerProp = rest.getFiller();
                    if(rest.getFiller() instanceof OWL2DatatypeImpl){
                    targetNameSpace = getNameSpace(((OWL2DatatypeImpl)rest.getFiller()).getIRI());
                    }
                    else{
                        targetNameSpace = getNameSpace(((OWLDatatypeImpl)rest.getFiller()).getIRI());
                    }
                    opData = CreateUtils.createAssociationTextData(renderer.render(fillerProp));
                    targetCode =  buildDataTypeEntity(fillerProp, targetNameSpace);
                }
                if (restriction instanceof OWLCardinalityRestriction) {
                    OWLCardinalityRestriction rest = (OWLCardinalityRestriction) restriction;
                    //OWLPropertyRange fillerProp = rest.getFiller();
                    opData = CreateUtils.createAssociationTextData("" + rest.getCardinality());
                    targetNameSpace = getUserSetNamespace();
                    //Defining the use case OWL data or data min exact.  otherwise defaults to 
                    //OWL object exact
                    if(restriction instanceof OWLDataExactCardinality  || 
                            restriction instanceof OWLDataMinCardinality  ||
                            restriction instanceof OWLDataMaxCardinality  ||
                            restriction instanceof OWLObjectMaxCardinality ||
                            restriction instanceof OWLObjectMinCardinality){
                       //TODO define the use case of this operation.  Doesn't fit well into the idea of a relationship
                       //in LexEVS
                        return;
                    }
                    targetCode =  buildCardinalityTypeEntity(restriction, targetNameSpace);
                    if(targetCode == null || targetCode.equals("Thing")){return;}
                }
                if (restriction instanceof OWLHasValueRestriction) {
                    OWLHasValueRestriction rest = (OWLHasValueRestriction) restriction;
                    OWLObject fillerProp = rest.getValue();
                    if (fillerProp instanceof OWLClass) {
                        targetCode = resolveConceptID((OWLClass) fillerProp);
                        targetNameSpace = getNameSpace((OWLClass) fillerProp);
                    } else if (fillerProp instanceof OWLNamedIndividual) {
                        targetCode = resolveInstanceID((OWLNamedIndividual) fillerProp);
                        targetNameSpace = getNameSpace((OWLNamedIndividual) fillerProp);
                    } else if (fillerProp instanceof OWLLiteral){
                        //  targetCode = ((OWLLiteral) fillerProp).getDatatype().getIRI().getFragment();
                          targetNameSpace = ((OWLLiteral) fillerProp).getDatatype().getIRI().getNamespace();
                          opData = CreateUtils.createAssociationTextData(renderer.render(fillerProp));
                          targetCode =  buildDataTypeEntity(fillerProp, targetNameSpace);
                    } else {                        
                        opData = CreateUtils.createAssociationTextData(renderer.render(fillerProp));  
                    }

                }
                if (restriction instanceof OWLObjectHasSelf) {
                    OWLObjectHasSelf rest = (OWLObjectHasSelf) restriction;

                }

                AssociationTarget opTarget = null;
                if (targetCode != null) {
                    opTarget = CreateUtils.createAssociationTarget(targetCode, targetNameSpace);
                }

                // Set the association qualifications: this
                // indicates the kind of restriction (e.g., owl:cardinality).

                if (restriction instanceof OWLQuantifiedObjectRestriction) {
                    AssociationQualification opQual = createAssociationQualification(restriction, lgSupportedMappings_);
                    if (opData != null) {
                        opData.addAssociationQualification(opQual);
                    }
                    if (opTarget != null) {
                        opTarget.addAssociationQualification(opQual);
                    }
                }
                
                if(restriction instanceof OWLDataHasValue){
                    String label = ((OWLDataHasValue) restriction).getValue().getDatatype().getIRI().getShortForm();
                    if (label.isEmpty()) {
                        label = renderer.render(restriction);
                    }
                    String value = ((OWLDataHasValue) restriction).getValue().getLiteral();
                    AssociationQualification opQual = CreateUtils.createAssociationQualification(label, null, value != null? value:label,
                            lgSupportedMappings_);
                    if (opData != null) {
                        opData.addAssociationQualification(opQual);
                    }
                    if (opTarget != null) {
                        opTarget.addAssociationQualification(opQual);
                    }
                }
                
                if(restriction instanceof OWLObjectExactCardinality){
                    String label = null;
                    
                    label = parseQualifierNameFromManchesterRender(renderer.render(((OWLObjectExactCardinality) restriction)));
                    if(label == null || label.length() < 1){
                    label = ((OWLObjectExactCardinality) restriction).getClassExpressionType().getName();
                    }
                    String value = String.valueOf(((OWLObjectExactCardinality) restriction).getCardinality());
                    AssociationQualification opQual = CreateUtils.createAssociationQualification(label, null, value != null? value:label,
                            lgSupportedMappings_);
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
                    if (!prefManager.isProcessStrictOWL() && assocParentSource != null)
                        relateAssociationSourceTarget(opAssoc, assocParentSource, opTarget);
                }

            }
        }
    }

    private String buildCardinalityTypeEntity(OWLRestriction restriction, String targetNameSpace) {
        String code = null;
        String nameSpace = null;
        EntityDescription ed = null;

        OWLClass node = restriction.getClassesInSignature().iterator().next();
        if(node.isOWLThing()){
            return resolveConceptID(node);
        }
        else{
        code = restriction.getClassesInSignature().iterator().next().getIRI().getFragment();
        }
        nameSpace = targetNameSpace;
        ed = new EntityDescription();
        ed.setContent(resolveLabel(restriction.getClassesInSignature().iterator().next()));
        
        // Check if this concept has already been processed. 
        if (isEntityCodeRegistered(nameSpace, code)) {
            return code;
        }

        Entity lgClass = new Entity();
        lgClass.setEntityType(new String[] { EntityTypes.CONCEPT.toString() });
        lgClass.setEntityCode(code);
        lgClass.setIsAnonymous(Boolean.FALSE);

        lgClass.setEntityCodeNamespace(nameSpace);
        lgClass.setEntityDescription(ed);

        int lgPropNum = 0;

        // Add entity description and matching preferred text presentation to
        // the browser text we get from the Protege API.
        // Note: text was derived from the browser text. Since it is unclear
        // what property it was derived from, we document as 'label'.
        Presentation pres = CreateUtils.createPresentation(generatePropertyID(++lgPropNum), "label", lgClass
                .getEntityDescription().getContent(), Boolean.TRUE, lgSupportedMappings_, null, null);
        lgClass.addPresentation(pres);
        // Add to the concept container or write to db...
        addEntity(lgClass);
        return code;
    }

    private String buildLiteralEntity(String targetCode, String targetNameSpace) {
        String code = targetCode;
        String nameSpace = targetNameSpace;
        // Check if this concept has already been processed. We do not want
        // duplicate concepts.
        if (isEntityCodeRegistered(nameSpace, code)) {
            return code;
        }

        Entity lgClass = new Entity();
        lgClass.setEntityType(new String[] { EntityTypes.CONCEPT.toString() });
        lgClass.setEntityCode(code);
        lgClass.setIsAnonymous(Boolean.FALSE);

        lgClass.setEntityCodeNamespace(nameSpace);

        EntityDescription ed = new EntityDescription();
        ed.setContent(targetCode);
        lgClass.setEntityDescription(ed);

        int lgPropNum = 0;

        // Add entity description and matching preferred text presentation to
        // the browser text we get from the Protege API.
        // Note: text was derived from the browser text. Since it is unclear
        // what property it was derived from, we document as 'label'.
        Presentation pres = CreateUtils.createPresentation(generatePropertyID(++lgPropNum), "label", lgClass
                .getEntityDescription().getContent(), Boolean.TRUE, lgSupportedMappings_, null, null);
        lgClass.addPresentation(pres);
        // Add to the concept container or write to db...
        addEntity(lgClass);
        return code;
        
    }

    private String buildDataTypeEntity(OWLObject fillerProp, String targetNameSpace) {
        String code = null;
        String nameSpace = null;
        EntityDescription ed = null;
        if(fillerProp instanceof OWLDataRange){
        code = fillerProp.toString();
        nameSpace = targetNameSpace;
        ed = new EntityDescription();
        ed.setContent(renderer.render(fillerProp));
        }
        
        if(fillerProp instanceof OWLLiteral){
            code = ((OWLLiteral) fillerProp).getDatatype().getBuiltInDatatype().getShortForm();
            nameSpace = ((OWLLiteral) fillerProp).getDatatype().getIRI().getNamespace();
            ed = new EntityDescription();
            ed.setContent(code);
        }
        
        // Check if this concept has already been processed. 
        if (isEntityCodeRegistered(nameSpace, code)) {
            return code;
        }

        Entity lgClass = new Entity();
        lgClass.setEntityType(new String[] { OwlApi2LGConstants.DATA_TYPE_ENTITY_TYPE });
        lgClass.setEntityCode(code);
        lgClass.setIsAnonymous(Boolean.FALSE);

        lgClass.setEntityCodeNamespace(nameSpace);
        lgClass.setEntityDescription(ed);

        int lgPropNum = 0;

        // Add entity description and matching preferred text presentation to
        // the browser text we get from the Protege API.
        // Note: text was derived from the browser text. Since it is unclear
        // what property it was derived from, we document as 'label'.
        Presentation pres = CreateUtils.createPresentation(generatePropertyID(++lgPropNum), "label", lgClass
                .getEntityDescription().getContent(), Boolean.TRUE, lgSupportedMappings_, null, null);
        lgClass.addPresentation(pres);
        // Add to the concept container or write to db...
        addEntity(lgClass);
        return code;
    }

    /**
     * Defines EMF complementOf relations based on OWL source.
     * 
     */
    protected void resolveComplementOfRelations(AssociationSource source, OWLClass rdfsNamedClass) {
        if (rdfsNamedClass.getComplementNNF() instanceof OWLObjectComplementOf) {
        
            relateAssocSourceWithOWLClassExpressionTarget(EntityTypes.CONCEPT, assocManager.getComplementOf(), source,
                    rdfsNamedClass.getComplementNNF(), null);
        }
    }
    

    /**
     * Defines EMF class RDF properties.
     * 
     */
    protected void resolveOWLObjectPropertyRelations(AssociationSource source, OWLEntity owlClass) {
        
        for (OWLAnnotationAssertionAxiom annotationAxiom : ontology.getAnnotationAssertionAxioms(owlClass.getIRI())) {
            String propName = getLocalName(annotationAxiom.getProperty());
            if (ontology.containsObjectPropertyInSignature(annotationAxiom.getProperty().getIRI())) {
                AssociationWrapper lgAssoc = assocManager.getAssociation(propName);

                if (lgAssoc == null)
                    return;
                OWLAnnotationValue value = annotationAxiom.getValue();
                if (value instanceof IRI) {
                    IRI iri_v = (IRI) value;
                    relateAssocSourceWithIriTarget(EntityTypes.CONCEPT,
                            lgAssoc, source, iri_v, annotationAxiom);
                         
                } 
            }
     
        }
            
    }

    private boolean isAnyURIDatatype(OWLAnnotationAssertionAxiom annotationAxiom) {
        //Do we declare this as anyURI locally?
        Iterator<OWLDatatype> itr = annotationAxiom.getDatatypesInSignature().iterator();
        while(itr.hasNext()){
            OWLDatatype dt = itr.next();

            if(dt.isBuiltIn() && dt.getBuiltInDatatype().equals(OWL2Datatype.XSD_ANY_URI)){
                return true;
            }
        }
       
        //If not, then check the declaration of the annotation property to see if it is declared there
        Set<OWLAxiom> annotationAxioms = EntitySearcher.getReferencingAxioms(annotationAxiom.getProperty(), ontology).collect(Collectors.toSet());
        for(OWLAxiom ax : annotationAxioms){
            if(ax instanceof OWLAnnotationPropertyRangeAxiomImpl){
                if(((OWLAnnotationPropertyRangeAxiomImpl) ax).getRange().equals(OWL2Datatype.XSD_ANY_URI.getIRI())){
                    return true;
                }
            }
        }
        //If we get here this does not have the range or data type of anyURI.
        return false;
    }

    /**
     * Defines EMF differentFrom relations based on OWL source.
     * 
     */
    protected void resolveDifferentFromRelations(AssociationSource source, OWLNamedIndividual individual) {
        for (OWLIndividual different : EntitySearcher.getDifferentIndividuals(individual, ontology).collect(Collectors.toList())) {
            if (different.isNamed()) {
                relateAssocSourceWithRDFResourceTarget(EntityTypes.INSTANCE, assocManager.getDifferentFrom(), source,
                        different.asOWLNamedIndividual());
            }
        }

    }

    /**
     * Defines sameAs relations based on OWL source.
     * 
     */
    protected void resolveSameAsRelations(AssociationSource source, OWLNamedIndividual individual) {
        for (OWLIndividual same : EntitySearcher.getSameIndividuals(individual, ontology).collect(Collectors.toList())) {
            if (same.isNamed())
                relateAssocSourceWithRDFResourceTarget(EntityTypes.INSTANCE, assocManager.getSameAs(), source,
                        same.asOWLNamedIndividual());
        }
    }

    protected void resolveAnonymousProperties(Entity lgEntity, OWLClassExpression owlClass) {
//        for (OWLAnnotationAssertionAxiom prop : ontology.getAnnotationAssertionAxioms(owlClass.getIRI())) {
//
//            String propName = getLocalName(prop.getProperty());
//            // Do we care about this rdf property?
//            if (isNoop(propName) || isNoopNamespace(propName))
//                continue;
//        ontology.getAnnotationAssertionAxioms(owlClass);
//       // ontology.getAnnotationAssertionAxioms(owlClass.)
//       // owlClass.getAnnotations()
    }
    
    
    /**
     * Resolve and assign all property information contained by the given RDF
     * resource to the EMF Entity.
     * 
     * @param lgEntity
     * @param owlClass
     * 
     *            Last updated: 05/28/2008
     */
    protected void resolveEntityProperties(Entity lgEntity, OWLEntity owlClass) {
        String rdfName = getLocalName(owlClass);

        // Temporary container for EMF properties.
        // Note: The EMF object does not enforce order. However, the XSD models
        // have required properties to occur in the XML in a specific order.
        // The comparator ensures a compatible order is maintained.
        SortedSet sortedProps = new TreeSet(propertyComparator);
        // Counters to use in property ID assignment to keep things unique
        // and track assigned presentation count.
        int i = 0;
        int presentationCount = 0;

        for (OWLAnnotationAssertionAxiom annotationAxiom : ontology.getAnnotationAssertionAxioms(owlClass.getIRI())) {

            String propName = getLocalName(annotationAxiom.getProperty());
            // Do we care about this rdf property?
            if (isNoop(propName) || isNoopNamespace(propName))
                continue;

            // Do we have an class match?
            String propClass = owlDatatypeName2lgPropClass_.get(propName);
            if (isNoop(propClass))
                continue;
            
            
            String lang = null;
            if(annotationAxiom.getValue().asLiteral().isPresent()) {
                lang = annotationAxiom.getValue().asLiteral().get().getLang();
            }
            // if the IRI is not in the cache, call isAnyURIDataType() method, get the result, and add it to the cache
            Boolean isAnyURIDataType;
                 
            if (!owlIRIToIsAnyUIRDataType_.containsKey(annotationAxiom.getProperty().getIRI())){
                isAnyURIDataType = isAnyURIDatatype(annotationAxiom);
                owlIRIToIsAnyUIRDataType_.put(annotationAxiom.getProperty().getIRI(), new Boolean(isAnyURIDataType)); 
            }
            else {
                isAnyURIDataType = owlIRIToIsAnyUIRDataType_.get(annotationAxiom.getProperty().getIRI());             
            }
            
            if (isAnyURIDataType.booleanValue()) {
                continue;
            }

            // Determine the property name and datatype ...
            String lgDType = owlDatatypeName2lgDatatype_.get(propName);
            String lgLabel = owlDatatypeName2label_.get(propName);
            OWLAnnotationValue value = annotationAxiom.getValue();
            String resolvedText = "";
            if (value instanceof OWLLiteral) {
                OWLLiteral literal = (OWLLiteral) value;
                resolvedText = literal.getLiteral();
            } else if (value instanceof IRI) {
                IRI iri_v = (IRI) value;
                resolvedText = iri_v.toString();
            } else {
                resolvedText = renderer.render(value);
            }

            // Interpret RDF property value(s) ...
            // Special case for handling concept code and status, which are
            // set directly as attributes on the LexGrid concept.
            if (propName.matches(prefManager.getMatchPattern_conceptCode())) {
                lgEntity.setEntityCode(resolvedText);
            } else if ((lgLabel != null && lgLabel.matches(prefManager.getMatchPattern_conceptStatus())) ||
                       (propName.matches(prefManager.getMatchPattern_conceptStatus())))  {
                // trim to 50, if needed.
                if (resolvedText.length() > 50){
                    System.out.println("Trimming Concept_Status to 50 characters: " + resolvedText);
                    resolvedText = resolvedText.substring(0, 49);
                }
                else {
                    lgEntity.setStatus(resolvedText);
                }            
                if (resolvedText.matches(prefManager.getMatchPattern_inactiveStatus()))
                    lgEntity.setIsActive(false);
            }
            // Otherwise instantiate a new EMF property and add the new
            // property to the list to eventually add to the concept.
            else {
                Property newProp = resolveProp(annotationAxiom, propClass, generatePropertyID(++i), lgLabel, lgDType,
                        getNameSpace(annotationAxiom.getProperty()), resolvedText, lang != null?lang: null);
                if (newProp.getValue() != null) {
                    sortedProps.add(newProp);
                    if (newProp instanceof Presentation)
                        presentationCount++;
                }
            }

        }

        // The LexGrid model requires a matching presentation for the entity
        // description. If no presentations exist, manufacture a default to
        // satisfy the requirement. If created, the name of the new property
        // is set to indicate where the value was found. Also support
        // explicit requests for "rdfs:label" as the preferred property.
        boolean generatePreferred = prefManager.getPrioritized_presentation_names().size() == 0
                || OwlApi2LGConstants.PROPNAME_RDFS_LABEL.equalsIgnoreCase(prefManager
                        .getPrioritized_presentation_names().get(0)) || presentationCount == 0;
        if (generatePreferred) {
            String entityDesc = lgEntity.getEntityDescription().getContent();
            sortedProps.add(CreateUtils.createPresentation(generatePropertyID(++i),
                    rdfName.equals(entityDesc) ? OwlApi2LGConstants.PROPNAME_RDF_ID
                            : OwlApi2LGConstants.PROPNAME_RDFS_LABEL, entityDesc, true, lgSupportedMappings_, null,
                    null));
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
                //We don't want a property with an empty value to populate the 
                //entity description
                if(StringUtils.isEmpty(pres.getValue().getContent())){
                    continue;
                }
                pres.setIsPreferred(Boolean.TRUE);
                
                // Entity description on concept should match preferred
                // presentation.
                EntityDescription ed = new EntityDescription();
                ed.setContent(((Presentation) prop).getValue().getContent());
                lgEntity.setEntityDescription(ed);

                // Remember that a preferred presentation was assigned ...
                assignedPreferredPres = true;
            }
            if (!assignedPreferredDefn && (prop instanceof Definition)) {
                // Tag the definition
                ((Definition) prop).setIsPreferred(Boolean.TRUE);

                // Remember that a preferred definition was assigned ...
                assignedPreferredDefn = true;
            }
            else if (prop instanceof Presentation){
                // default the presentation property isPreferred value to false, if it wasn't set.
                if (((Presentation) prop).getIsPreferred() == null) {
                    ((Presentation) prop).setIsPreferred(Boolean.FALSE);
                }
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

        for (OWLDatatype prop : owlClass.getDatatypesInSignature()) {
            String propertyName = getLocalName(prop);
            DataRangeType range = prop.getDataRangeType();
            if (range != null) {
                String propertyRangeName = range.getName();
                Property lgProp = CreateUtils.createProperty(generatePropertyID(++i), propertyName, propertyRangeName,
                        lgSupportedMappings_, prop.getIRI().toString(), null);
                sortedProps.add(lgProp);
            }
        }
        
        //Giving the One of data properties full definition status.
        for (OWLDataProperty prop : owlClass.getDataPropertiesInSignature()) {
            String propertyName = prop.getIRI().getFragment();
            Set<OWLDataRange> ranges = EntitySearcher.getRanges(prop, ontology).collect(Collectors.toSet());
            if (!ranges.isEmpty()) {
                OWLDataRange range = EntitySearcher.getRanges(prop, ontology).collect(Collectors.toSet()).iterator().next();
                    if (range instanceof OWLDataOneOf) {
                    OWLDataOneOfImpl oneOf = (OWLDataOneOfImpl) range;
                    for (OWLLiteral lit : oneOf.getValues()) {
                        String literal = lit.getLiteral();
                        Property lgProp = CreateUtils.createDefinition(generatePropertyID(++i), propertyName, literal,false,
                                lgSupportedMappings_, prop.getIRI().toString(), "");
                        sortedProps.add(lgProp);
                    }
                }
            }
        }

        
        // Now add all the sorted properties to the concept.
        for (Iterator<? extends Property> lgProps = sortedProps.iterator(); lgProps.hasNext();) {
            Property lgProp = lgProps.next();
            lgEntity.addAnyProperty(lgProp);
        }

    }

    /**
     * Instantiate and return a new EMF property based on the provided values.
     * 
     * @param lgClass
     *            The EMF property class to instantiate (presentation, comment,
     *            etc).
     * @param lgID
     *            The identifier to assign to the new property; not null.
     * @param lgLabel
     *            The name or label for the new property; not null.
     * @param lgDType
     *            The data type of the new property; null if not applicable.
     * @param rdfNamespace
     *            Namespace of the RDF property being converted.
     * @param rdfText
     *            Text from the RDF property being converted.
     * @return org.LexGrid.lg.concepts.ConceptProperty
     */
    protected Property resolveProp(OWLAnnotationAssertionAxiom prop, String lgClass, String lgID, String lgLabel,
            String lgDType, String rdfNamespace, String rdfText, String lang) {

        Property lgProp;
        String propName = getLocalName(prop.getProperty());
        if (RDFSNames.Slot.LABEL.equals(propName) || lgClass == PropertyTypes.PRESENTATION.toString())
            lgProp = CreateUtils.createPresentation(lgID, lgLabel, rdfText, null, lgSupportedMappings_, prop
                    .getProperty().getIRI().toString(), lang);
        else if (RDFSNames.Slot.COMMENT.equals(propName) || lgClass == PropertyTypes.COMMENT.toString())
            lgProp = CreateUtils.createComment(lgID, lgLabel, rdfText, lgSupportedMappings_, prop.getProperty()
                    .getIRI().toString(), lang);
        else if (lgClass == PropertyTypes.DEFINITION.toString())
            lgProp = CreateUtils.createDefinition(lgID, lgLabel, rdfText, null, lgSupportedMappings_, prop
                    .getProperty().getIRI().toString(), lang);
        else {
            lgProp = CreateUtils.createProperty(lgID, lgLabel, null, lgSupportedMappings_, prop.getProperty().getIRI()
                    .toString(), lang);
        }

        lgProp.setValue(CreateUtils.createText(rdfText));
        
        processAnnotationsOfAnnotationAssertionAxiom(prop, lgProp);
        return lgProp;
    }

    /**
     * Process annotations of AnnotationAssertionAxiom as property qualifiers on
     * the property
     * 
     * @param prop
     * @param lgProp
     */
    private void processAnnotationsOfAnnotationAssertionAxiom(OWLAnnotationAssertionAxiom prop, Property lgProp) {
        List<Source> sources = new ArrayList<Source>();
        for (OWLAnnotation annotation : prop.getAnnotations()) {
            String annotationName = getLocalName(annotation.getProperty());
            String annotationPropertyCode = annotation.getProperty().getIRI().getFragment();
            if(owlDatatypeName2label_.get(annotationName) != null){
                annotationName = owlDatatypeName2label_.get(annotationName);
            }
            else{
                annotationName = resolveLabel(annotation.getProperty());
            }
 //           Iterator<OWLAnnotation> itr = annotation.getProperty().asOWLAnnotationProperty().getAnnotations(ontology).iterator();
//            if(itr.hasNext()){
//                while(itr.hasNext()){
//                    OWLAnnotation annot = itr.next();
//                    if(annot.getValue() instanceof OWLLiteral){
//                    annotationName = ((OWLLiteral)annot.getValue()).getLiteral();
//                    break;
//                    }
//                }
//            }

            String annotationValue = "";
            OWLAnnotationValue value = annotation.getValue();
            if (value instanceof OWLLiteral) {
                OWLLiteral literal = (OWLLiteral) value;
                annotationValue = literal.getLiteral();
            }
            if(value instanceof IRI){
                IRI iri = (IRI)value;
                if(!ontology.getEntitiesInSignature(iri).isEmpty()){
                  OWLEntity entity = ontology.getEntitiesInSignature(iri).iterator().next();
                  annotationValue =  entity.toString();
                }
                else{
                annotationValue = iri.toString();
                }
            }
            
            //If this is a presentation we are going to try to populate the source
            //or representational form
            if(lgProp instanceof Presentation){
                if(isRepresentationalForm(annotationName) || isRepresentationalForm(annotationPropertyCode)){
                    ((Presentation) lgProp).setRepresentationalForm(annotationValue);
                    lgSupportedMappings_.registerSupportedRepresentationalForm(annotationValue, 
                            getNameSpace(annotation.getProperty()), annotationValue, false);
                    continue;
                }
                else if(isSource(annotationName) || isSource(annotationPropertyCode)){
                     Source source = new Source();
                     source.setContent(annotationValue);
                     sources.add(source);
                     
                     lgSupportedMappings_.registerSupportedSource(annotationValue, 
                             getNameSpace(annotation.getProperty()), annotationValue, null, false);
                     continue;
                }
            }
            
            //Do the same for Definition sources
            if(lgProp instanceof Definition){
                if(isSource(annotationName) || isSource(annotationPropertyCode)) {
                    Source source = new Source();
                    source.setContent(annotationValue);
                    sources.add(source);
                    
                    lgSupportedMappings_.registerSupportedSource(annotationValue, 
                            getNameSpace(annotation.getProperty()), annotationValue, null, false);
                    continue;
                }             
            }
                
            if (StringUtils.isNotBlank(annotationName) && StringUtils.isNotBlank(annotationValue)) {

                if(isSource(annotationName) || isSource(annotationPropertyCode)) {
                        Source source = new Source();
                        source.setContent(annotationValue);
                        sources.add(source);
                        
                        lgSupportedMappings_.registerSupportedSource(annotationValue, 
                                getNameSpace(annotation.getProperty()), annotationValue, null, false);
                        continue;
                }
                lgProp.addPropertyQualifier(CreateUtils.createPropertyQualifier(annotationName, annotationValue,
                        lgSupportedMappings_));

                // Register the qualifier as supported if not already
                // defined.
                lgSupportedMappings_.registerSupportedPropertyQualifier(annotationName,
                        getNameSpace(annotation.getProperty()), annotationName, false);
            }
        }
        Source[] sourceArray = new Source[sources.size()];
        int i = 0;
        for(Source s: sources){
            sourceArray[i] = s;
            i++;
        }
        lgProp.setSource(sourceArray);
    }


    private boolean isSource(String annotationValue) {
        return annotationValue.matches(prefManager.getMatchPattern_xmlSourceNames());                
    }

    private boolean isRepresentationalForm(String annotationValue) {
        return annotationValue.matches(prefManager.getMatchPattern_xmlRepFormNames());
    }

//    private void processComplexXMLPropertyValue(Property lgProp, String lgClass, String lgID, String lgLabel,
//            String lgDType, String rdfNamespace, String rdfText, Map<String, String> xmlTagsAndVals) {
//        // Designated tags may act as property text or source;
//        // all other will be treated as property qualifiers.
//        
//        for (String tag : xmlTagsAndVals.keySet()) {
//
//            if (tag == null) {
//                messages_.info("Skipping " + lgID + ", " + lgLabel + ", " + lgDType + ", " + rdfNamespace + ", "
//                        + rdfText);
//                continue;
//            }
//
//            String text = xmlTagsAndVals.get(tag);
//
//            if (tag.matches(prefManager.getMatchPattern_xmlTextNames())) {
//                lgProp.setValue(CreateUtils.createText(text));
//            } else if (tag.matches(prefManager.getMatchPattern_xmlSourceNames())) {
//                lgProp.addSource(CreateUtils.createSource(text, null, null, lgSupportedMappings_));
//
//                // Register the source as supported if not already
//                // defined.
//                lgSupportedMappings_.registerSupportedSource(text, rdfNamespace + text, text, null, false);
//            } else if (tag.matches("language")) {
//                lgProp.setLanguage(text);
//
//                // Register the source as supported if not already
//                // defined.
//                lgSupportedMappings_.registerSupportedLanguage(text, OwlApi2LGConstants.LANG_URI + ':' + text, text,
//                        false);
//            }
//            // specific to the new complex props implementation
//            else if (prefManager.isComplexProps_isDbxRefSource()
//                    && text.matches(OwlApi2LGConstants.MATCH_XMLSOURCE_VALUES)) {
//                String val = text;
//                String ref = null;
//                String[] sourceWithRef = text.split("(:)");
//                if (sourceWithRef.length == 2) {
//                    val = sourceWithRef[0];
//                    ref = sourceWithRef[1];
//                }
//                lgProp.addSource(CreateUtils.createSource(val, null, ref, lgSupportedMappings_));
//
//                // Register the source as supported if not already
//                // defined.
//                lgSupportedMappings_.registerSupportedSource(text, rdfNamespace + text, text, null, false);
//
//            } else if (lgProp instanceof Presentation && tag.matches(prefManager.getMatchPattern_xmlRepFormNames())) {
//                ((Presentation) lgProp).setRepresentationalForm(text);
//
//                // Register the source as supported if not already
//                // defined.
//                lgSupportedMappings_.registerSupportedRepresentationalForm(text, rdfNamespace + text, text, false);
//            }
//            // specific to the new complex props implementation
//            else if (prefManager.isComplexProps_isDbxRefRepForm() && lgProp instanceof Presentation
//                    && text.matches(OwlApi2LGConstants.MATCH_XMLREPFORM_VALUES)) {
//                ((Presentation) lgProp).setRepresentationalForm(text);
//
//                // Register the source as supported if not already
//                // defined.
//                lgSupportedMappings_.registerSupportedRepresentationalForm(text, rdfNamespace + text, text, false);
//            } else {
//                lgProp.addPropertyQualifier(CreateUtils.createPropertyQualifier(tag, text, lgSupportedMappings_));
//
//                // Register the qualifier as supported if not already
//                // defined.
//                lgSupportedMappings_.registerSupportedPropertyQualifier(tag, rdfNamespace + tag, tag, false);
//            }
//        }
//    }

    /**
     * 
     * This method handles the resolution of owl:Anonymous classes.
     * 
     * @param owlClassExp
     * @return
     */
    protected String resolveAnonymousClass(OWLClassExpression owlClassExp, AssociationSource assocSource) {

        String code = "@" + DigestUtils.md5Hex(owlClassExp.toString());

        String nameSpace = getUserSetNamespace();
        // Check if this concept has already been processed. We do not want
        // duplicate concepts.
        if (!isEntityCodeRegistered(nameSpace, code)) {
//            return code;
//        }

        Entity lgClass = new Entity();
        lgClass.setEntityType(new String[] { EntityTypes.CONCEPT.toString() });
        lgClass.setEntityCode(code);
        lgClass.setIsAnonymous(Boolean.TRUE);

        lgClass.setEntityCodeNamespace(nameSpace);

        EntityDescription ed = new EntityDescription();
        String desc =  renderer.render(owlClassExp);
              
        String cleanedDesc = processEquivalentClassExp(desc);
        ed.setContent(cleanedDesc);
        lgClass.setEntityDescription(ed);

        int lgPropNum = 0;

        // Add entity description and matching preferred text presentation to
        // the browser text we get from the Protege API.
        // Note: text was derived from the browser text. Since it is unclear
        // what property it was derived from, we document as 'label'.
        Presentation pres = CreateUtils.createPresentation(generatePropertyID(++lgPropNum), "label", lgClass
                .getEntityDescription().getContent(), Boolean.TRUE, lgSupportedMappings_, null, null);
        lgClass.addPresentation(pres);
        // Add to the concept container or write to db...
        addEntity(lgClass);
    }
        AssociationSource source = CreateUtils.createAssociationSource(code, nameSpace);

        if (owlClassExp instanceof OWLNaryBooleanClassExpression) {
            OWLNaryBooleanClassExpression logicalClass = (OWLNaryBooleanClassExpression) owlClassExp;
            for (OWLClassExpression operand : logicalClass.getOperands()) {
                if (!operand.isAnonymous()) {
                    OWLClass op = operand.asOWLClass();
                    String targetNameSpace = getNameSpace(op);
                    AssociationTarget opTarget = CreateUtils.createAssociationTarget(getLocalName(op), targetNameSpace);
                    relateAssociationSourceTarget(assocManager.getSubClassOf(), source, opTarget);
                } else if  (operand instanceof OWLRestriction) {
                    // Operand defines a restriction placed on the anonymous
                    // node...
                    OWLRestriction op = (OWLRestriction) operand;
                    processRestriction(op, assocSource, source);
                } else if (operand instanceof OWLNaryBooleanClassExpression || operand instanceof OWLObjectComplementOf){
                    //Still has some classes to process that are intersections or unions of or complements of.
                    processInnerNAryExpression(operand, assocSource, source);
              }
                    
                else {
                  
                    String lgCode = resolveAnonymousClass(operand, assocSource);
                    String targetNameSpace = getUserSetNamespace();
                    AssociationTarget opTarget = CreateUtils.createAssociationTarget(lgCode, targetNameSpace);
                    relateAssociationSourceTarget(assocManager.getSubClassOf(), source, opTarget);
                }
            }
        }

        if (owlClassExp instanceof OWLObjectComplementOf) {
            OWLObjectComplementOf complementClass = (OWLObjectComplementOf) owlClassExp;
            String lgCode = resolveAnonymousClass((OWLClassExpression) complementClass.getOperand(), assocSource);
            String targetNameSpace = getUserSetNamespace();
          
            AssociationTarget opTarget = CreateUtils.createAssociationTarget(lgCode, targetNameSpace);
            relateAssociationSourceTarget(assocManager.getComplementOf(), source, opTarget);
            //We need to make sure there are no other inner NAry elements.
            processInnerNAryExpression(complementClass, assocSource, source);
        }
        if (owlClassExp instanceof OWLRestriction) {
            OWLRestriction restrictionClassExp = (OWLRestriction) owlClassExp;
            processRestriction(restrictionClassExp, assocSource, source);
        }
        if (owlClassExp instanceof OWLObjectOneOf) {
            OWLObjectOneOf oneOfClassExp = (OWLObjectOneOf) owlClassExp;
        }

        // Return the lg class name
        return code;
    }

    protected String processEquivalentClassExp(String formattedExpression) {
       return formattedExpression.
               replaceAll("\n"," ").
               replaceAll("\r"," ").
               replaceAll(" +", " ");
    }

    //Resolve the complement of restriction as an association
    private void resolveComplementOfAsLgAssociation(OWLClassExpression owlClass, AssociationSource asscSource, AssociationSource source) {
       String targetNameSpace = getNameSpace((OWLEntity) owlClass);
       String targetId        = resolveConceptID((OWLEntity) owlClass);
       AssociationTarget opTarget = null;
       if (targetId != null) {
           opTarget = CreateUtils.createAssociationTarget(targetId, targetNameSpace);
       } 
       
       if (opTarget != null) {
           relateAssociationSourceTarget(assocManager.getComplementOf(), source, opTarget);
           if (!prefManager.isProcessStrictOWL() && asscSource != null)
               relateAssociationSourceTarget(assocManager.getComplementOf(), asscSource, opTarget);
       }
    }

    //Recurse through any NAry expressions or complement of until all have been parsed and loaded as
    //associations
    private void processInnerNAryExpression(OWLClassExpression operand, AssociationSource assocSource,
            AssociationSource source) {
        if (operand instanceof OWLObjectIntersectionOf) {
            for (OWLClassExpression innerOperand : ((OWLNaryBooleanClassExpression) operand).getOperands()) {
                if (innerOperand instanceof OWLRestriction) {
                    OWLRestriction op = (OWLRestriction) innerOperand;
                    processRestriction(op, assocSource, source);
                } else {
                    processInnerNAryExpression(innerOperand, assocSource, source);
                }
            }
        } else if (operand instanceof OWLObjectUnionOf) {
            for (OWLClassExpression innerOperand : ((OWLNaryBooleanClassExpression) operand).getOperands()) {
                if (innerOperand instanceof OWLRestriction) {
                    OWLRestriction op = (OWLRestriction) innerOperand;
                    processRestriction(op, assocSource, source);
                } else {
                    processInnerNAryExpression(innerOperand, assocSource, source);
                }
            }
        }else if (operand instanceof OWLObjectComplementOf){
          OWLClassExpression innerOperand =  ((OWLObjectComplementOf) operand).getOperand();
                if (innerOperand instanceof OWLRestriction) {
                    OWLRestriction op = (OWLRestriction) innerOperand;
                    processRestriction(op, assocSource, source);
                } 
                else if (innerOperand instanceof OWLNaryBooleanClassExpression) {
                    processInnerNAryExpression(innerOperand, assocSource, source);
                }
                else {
                    resolveComplementOfAsLgAssociation(innerOperand, assocSource, source);
                }
            }
    }

    /**
     * Initialize the Java model from source.
     * 
     */
    protected void initOWLOntologyFromSource() throws LgConvertException {
        try {
            messages_.info("Before Protege load");
            Snapshot snap = SimpleMemUsageReporter.snapshot();
            messages_.info("Read Time : " + SimpleMemUsageReporter.formatTimeDiff(snap.getTimeDelta(null))
                    + " Heap Usage: " + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsage()) + " Heap Delta:"
                    + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsageDelta(null)));
            IRI owl_IRI = IRI.create(owlURI_);
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            ontology = manager.loadOntologyFromOntologyDocument(owl_IRI);
            System.out.println("Loaded ontology: " + ontology.getOntologyID());
            // We need a reasoner to do our query answering
            reasoner = createReasoner(ontology);
            // Entities are named using IRIs. These are usually too long for use
            // in user interfaces. To solve this problem, we'll just use a
            // simple short form
            // provider that generates short froms from IRI fragments.
            ShortFormProvider shortFormProvider = new SimpleShortFormProvider();

            renderer = new MachesterOWLSyntaxLexGridRenderer(ontology,
                    new ManchesterOWLSyntaxPrefixNameShortFormProvider(ontology.getOWLOntologyManager()
                            .getOntologyFormat(ontology)));

            messages_.info("After OWL API load into memory");
            snap = SimpleMemUsageReporter.snapshot();
            messages_.info("Read Time : " + SimpleMemUsageReporter.formatTimeDiff(snap.getTimeDelta(null))
                    + " Heap Usage: " + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsage()) + " Heap Delta:"
                    + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsageDelta(null)));

            // Report raw OWL counts ...
            int clsCount = ontology.getClassesInSignature().size();
            messages_.info("OWL file loaded at: " + new Date());
            messages_.info("Total OWL classes = " + clsCount);

        } catch (Exception e) {
            throw new LgConvertException("\nAn error was encountered loading OWL file from " + owlURI_.toString()
                    + "\n" + "\nPlease Consider running the file through an RDF or OWL validation service such as:\n"
                    + "  - RDF Validator: http://www.w3.org/RDF/Validator\n"
                    + "  - OWL Validator: http://phoebus.cs.man.ac.uk:9999/OWL/Validator\n", e);
        }
    }

    private static OWLReasoner createReasoner(OWLOntology rootOntology) {
        // We need to create an instance of OWLReasoner. An OWLReasoner provides
        // the basic query functionality that we need, for example the ability
        // obtain the subclasses of a class etc. To do this we use a reasoner
        // factory.
        // Create a reasoner factory.
        OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
        //reasonerFactory = new Reasoner.ReasonerFactory();
        return reasonerFactory.createReasoner(rootOntology);
    }

    /**
     * Initialize tracking of supported behavior specified in LexGrid metadata.
     */
    protected void initSupportedMappings() {
        // Create a helper class used to accumulate mappings
        lgSupportedMappings_ = new SupportedMappings(messages_);

        // Register mappings that will not be explicitly built
        // while processing the OWL source...
        lgSupportedMappings_.registerSupportedDataType(OwlApi2LGConstants.DATATYPE_BOOLEAN,
                OwlApi2LGConstants.DATATYPE_BOOLEAN_URI, OwlApi2LGConstants.DATATYPE_BOOLEAN, false);
        lgSupportedMappings_.registerSupportedDataType(OwlApi2LGConstants.DATATYPE_STRING,
                OwlApi2LGConstants.DATATYPE_STRING_URI, OwlApi2LGConstants.DATATYPE_STRING, false);
        lgSupportedMappings_.registerSupportedPropertyQualifierType(OwlApi2LGConstants.DATATYPE_STRING,
                OwlApi2LGConstants.DATATYPE_STRING_URI, OwlApi2LGConstants.DATATYPE_STRING, false);
        lgSupportedMappings_.registerSupportedPropertyType(OwlApi2LGConstants.DATATYPE_STRING,
                OwlApi2LGConstants.DATATYPE_STRING_URI, OwlApi2LGConstants.DATATYPE_STRING, false);
        lgSupportedMappings_.registerSupportedHierarchy(OwlApi2LGConstants.SUPP_HIERARCHY_ISA,
                OwlApi2LGConstants.SUPP_HIERARCHY_ISA_URI, OwlApi2LGConstants.SUPP_HIERARCHY_ISA,
                OwlApi2LGConstants.ROOT_CODE, Arrays.asList(OwlApi2LGConstants.SUPP_HIERARCHY_ISA_ASSOCIATION_LIST),
                false, false);
    }

    /**
     * Initializes the EMF coding scheme and adds top-level containers for codes
     * and relations.
     * 
     */
    protected void initScheme() {
        // Basics
        lgScheme_ = new CodingScheme();

        // Create top-level container for assigned entities ...
        lgScheme_.setEntities(new Entities());

        // Create top-level containers for relations.
        lgRelationsContainer_Assoc = new Relations();
        lgRelationsContainer_Assoc.setContainerName(OwlApi2LGConstants.DC_ASSOCIATIONS);
        lgScheme_.addRelations(lgRelationsContainer_Assoc);

        // Add this Container to the Supported Mappings
        lgSupportedMappings_.registerSupportedContainerName(OwlApi2LGConstants.DC_ASSOCIATIONS, null, null, false);

        // Create top-level "Roles" containers for relations.
        lgRelationsContainer_Roles = new Relations();
        lgRelationsContainer_Roles.setContainerName(OwlApi2LGConstants.DC_ROLES);
        lgScheme_.addRelations(lgRelationsContainer_Roles);
        // Add this Container to the Supported Mappings
        lgSupportedMappings_.registerSupportedContainerName(OwlApi2LGConstants.DC_ROLES, null, null, false);

        Mappings mappings = new Mappings();
        lgScheme_.setMappings(mappings);
        ManchesterOWLSyntaxPrefixNameShortFormProvider prov = new ManchesterOWLSyntaxPrefixNameShortFormProvider(ontology);

        for (Iterator i = prov.getPrefixName2PrefixMap().keySet().iterator(); i.hasNext();) {
            String prefixName = (String) i.next();
            
            String prefix = prov.getPrefixName2PrefixMap().get(prefixName);
            prefixName = stripLastColon(prefixName);
            if (StringUtils.isNotEmpty(prefixName)) {
                // lgSupportedMappings_.registerSupportedSource(prefix,
                // nm.getNamespaceForPrefix(prefix), prefix, null, false);
                lgSupportedMappings_.registerSupportedNamespace(prefixName, prefix, prefixName, null, false);
            }

        }

        // Initialize the coding scheme type.
        initSchemeMetadata();
    }

    /**
     * Initialize further metadata about the coding scheme.
     *
     */
    protected void initSchemeMetadata(){
        // Set the ontology version from the versionInfo tag
        String version = "";
        IRI ontologyIRI = null;
        String uri = null;
        
        if(ontology.getOntologyID().getOntologyIRI().isPresent()){
           ontologyIRI = ontology.getOntologyID().getOntologyIRI().get();
           uri = ontologyIRI.toString();
        }
            version = getVersionInfo();

        if (ontologyIRI != null) {
            String localName = renderer.getOntologyShortFormProvider().getShortForm(ontologyIRI);

            // The URN w/ protocol and type removed is a second local name
            String localProtocol;
            if (uri.endsWith("#"))
                localProtocol = uri.substring(0, uri.length() - 1);
            else
                localProtocol = uri;
            if (localProtocol.endsWith("/"))
                localProtocol = localProtocol.substring(0, localProtocol.length() - 1);

            if (localProtocol.toLowerCase().startsWith("http://"))
                lgScheme_.addLocalName(localProtocol.substring("http://".length()));
            else
                lgScheme_.addLocalName(localProtocol);

            // The rightmost part of the urn is a third local name if it doesn't
            // match the coding scheme
            String localProtocol_csname = "";
            if (localProtocol.contains("/")) {
                localProtocol_csname = localProtocol.substring(localProtocol.lastIndexOf("/") + 1);
                if (!localName.equals(localProtocol_csname))
                    lgScheme_.addLocalName(localProtocol_csname);
            }

            if (StringUtils.isEmpty(localName)) {
                localName = localProtocol_csname;
                // if the namespace contains '.owl', then remove it.
                if (localName.indexOf(".owl") != -1) {
                    localName = localName.substring(0, localName.indexOf(".owl"));
                }

            } else {
                lgScheme_.addLocalName(localName);
            }
            
            //Use Ontology Annotations to provide more ontology metadata
            Set<OWLAnnotation> annotations = ontology.getAnnotations();
            for(OWLAnnotation owl: annotations){
                if(owl.getProperty().getIRI().getFragment().equals("date")){

                  
                    Date date = null;
                    try {
                        String textToParse = getAnnotationValue(owl);
                        textToParse = stripQuotes(textToParse);
                        date = parseEffectiveDate(textToParse);
                    } catch (ParseException e) {
                        System.out.println("Unable to parse effective date to date format.  Continuing load despite error");
                        e.printStackTrace();
                    }
                    lgScheme_.setEffectiveDate(date);
                }
                if(owl.getProperty().getIRI().getFragment().equals("note")){
                    String formattedString = stripQuotes(owl.getValue().toString());
                    formattedString = stripAtLanguageSuffix(formattedString);
                    lgScheme_.setEntityDescription(Constructors.createEntityDescription(formattedString));
                }
                if(owl.getProperty().getIRI().getFragment().equals("source")){
                    Source source = new Source();
                    String value = owl.getValue().isLiteral()? owl.getValue().asLiteral().get().getLiteral(): owl.getValue().toString();
                    source.setContent(value);
                    lgSupportedMappings_.registerSupportedSource(resolveLabel(owl.getProperty()), 
                             getNameSpace(owl.getProperty()), value, null, false);            
                    lgScheme_.getSourceAsReference().add(source);
                }
            }
            
            IRI versionIRI = null;
            if(ontology.getOntologyID().getVersionIRI().isPresent()){
                versionIRI = ontology.getOntologyID().getVersionIRI().get();
                Properties props = new Properties();
                Property prop = new Property();
                prop.setPropertyName("versionIRI");
                prop.setValue(Constructors.createText(versionIRI.toString()));
                props.addProperty(prop);                
                lgScheme_.setProperties(props);

            }
            // Override with manifest values, if provided. Note that we only
            // need be concerned with identifying information. Other values
            // from the manifest will be applied outside of this loader code.
            String schemeName = localName;

            if (manifest_ != null) {
                CsmfCodingSchemeName codingscheme = manifest_.getCodingScheme();
                CsmfCodingSchemeURI csURI = manifest_.getCodingSchemeURI();
                CsmfVersion mfVersion = manifest_.getRepresentsVersion();

                if (codingscheme != null && codingscheme.getToOverride()) {
                    String mName = codingscheme.getContent();
                    if (mName != null) {
                        schemeName = mName;
                    }
                }
                if (csURI != null && csURI.getToOverride()) {
                    String mURI = csURI.getContent();
                    if (mURI != null) {
                        uri = mURI;
                    }
                }

                if (mfVersion != null && mfVersion.getToOverride()) {
                    String mfVersionContent = mfVersion.getContent();
                    if (mfVersionContent != null) {
                        version = mfVersionContent;
                    }
                }
            }

            // Assign and register values.
            // Note that the coding scheme name is always one of the local names
            // and if manifest is specifying
            // any supportedCodingScheme then the default one is not included.
            CsmfMappings mappings = manifest_ != null ? manifest_.getMappings() : null;

            if (manifest_ == null || mappings == null || !mappings.getToUpdate()
                    || mappings.getSupportedCodingScheme().length <= 0)
                lgSupportedMappings_.registerSupportedCodingScheme(schemeName, uri, schemeName, false, false);
            lgScheme_.setCodingSchemeURI(uri);
            lgScheme_.setCodingSchemeName(schemeName);
            lgScheme_.setFormalName(localName);
        }

        if (version.length() == 0) {
            version = "UNASSIGNED";
        }
        if (version.length() > 50) {
            version = version.substring(0, 49);
        }
        lgScheme_.setRepresentsVersion(version);

        // Set the default language
        String defaultLanguage = OwlApi2LGConstants.LANG_ENGLISH;

        lgScheme_.setDefaultLanguage(defaultLanguage);
        lgSupportedMappings_.registerSupportedLanguage(defaultLanguage, OwlApi2LGConstants.LANG_URI + ':'
                + defaultLanguage, defaultLanguage, false);
        
 
    }

    private String stripAtLanguageSuffix(String str) {
        if (str != null && str.lastIndexOf("@") != -1) {
            str = str.substring(0, str.lastIndexOf("@"));
        }
        return str;
    }

    protected String stripQuotes(String textToParse) {
        textToParse = textToParse.replace("\"", "");
        return textToParse;
    }

    protected Date parseEffectiveDate(String dateText) throws ParseException {
        Date date = DateUtils.parseDate(dateText, OwlApi2LGConstants.DATEFORMATS);
//        SimpleDateFormat formatDate = new SimpleDateFormat("MMMM dd, yyyy");
//        formatDate.setLenient(true);
//        date = formatDate.parse(dateText);
        return date;
    }

    String getDefaultNameSpace() {
        IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI().isPresent()? 
                ontology.getOntologyID().getOntologyIRI().get(): null;
       return ontologyIRI.getFragment();
    }

    protected void initAnnotationProperties() {
        
        outer:for (OWLAnnotationProperty prop : ontology.getAnnotationPropertiesInSignature()) {
            String propertyName = getLocalName(prop);
            Boolean isAnyDataType;
                        
            Set <OWLAnnotationAssertionAxiom> annotationAxioms = 
                    EntitySearcher.getAnnotationAssertionAxioms(prop, ontology).collect(Collectors.toSet());
            if (annotationAxioms != null && annotationAxioms.size() > 0) {
                for(OWLAnnotationAssertionAxiom ax : annotationAxioms){
                    isAnyDataType = isAnyURIDatatype(ax);
                    owlIRIToIsAnyUIRDataType_.put(ax.getProperty().getIRI(), isAnyDataType);
                }
            }
            
            // Check all for multiple labels for this property
            String label = null;
            List<String> labels = resolveLabels(prop);
            //Page through to see if it is a no op or a prioritized preference
            //Noop continues the outer loop, a prioritized preference defines the label
            //We try to capture whether the property is a label by calling to string on it
            for(String s: labels){
            if (isNoopNamespace(s))
                continue outer;
            if(prefManager.getPrioritized_presentation_names().contains(s) ||
                    prefManager.getPrioritized_presentation_names().contains(prop.toString()))
                label = s;
            }
            //We didn't find any prioritized preferences so we'll just use the first label
            if(label == null){
                label = resolveLabel(prop);
            }
            addToSupportedPropertyAndMap(label, propertyName, prop);
            if(!owlDatatypeName2label_.containsKey(propertyName)){
                owlDatatypeName2label_.put(propertyName, label);
            }
        }
    }

    /**
     * This method initializes the OWL data properties. Note that, similar to
     * objecttype properties, we are modeling them as "associations" as well.
     * 
     */
    protected void initSupportedDataProperties() {
        for (OWLDataProperty prop : ontology.getDataPropertiesInSignature()) {
            addAssociation(prop);
        }
    }

    protected void addToSupportedPropertyAndMap(String label, String propertyName, OWLNamedObject rdfProp) {
        /*
         * This may be somewhat incorrect, because NOT all datatype properties
         * are supportedProperties for a given ontology. This needs to be fixed
         * later based on rule-based approach. Refer to email thread with Tom,
         * Deepak and Pradip on 05/01/2008.
         */
        PropertyTypes lgClass = null;
        if (prefManager.getPrioritized_presentation_names().contains(label))
            lgClass = PropertyTypes.PRESENTATION;
        else if (prefManager.getPrioritized_definition_names().contains(label))
            lgClass = PropertyTypes.DEFINITION;
        else if (prefManager.getPrioritized_comment_names().contains(label))
            lgClass = PropertyTypes.COMMENT;
        else
            lgClass = PropertyTypes.PROPERTY;

        // Register in supported properties
        lgSupportedMappings_.registerSupportedProperty(propertyName, rdfProp.getIRI().toString(), propertyName,
                lgClass, false);

        // Register the ID to EMF class mapping; default to
        // generic property class if not mapped above.
        owlDatatypeName2lgPropClass_.put(propertyName, lgClass.value());
    }

    protected void initAssociationEntities() {
        Map<String, AssociationWrapper> associations = this.assocManager.getAllAssociations();
        for (Entry<String, AssociationWrapper> association : associations.entrySet()) {
            AssociationEntity associationEntity = association.getValue().getAssociationEntity();
            if (associationEntity != null) {
                this.addEntity(association.getValue().getAssociationEntity());
            }
        }
    }

    /**
     * This method determines the various data types that are used in the
     * ontology and stores them.
     */

    protected void initSupportedDatatypes() {
        // Initialize Datatype to EMF mapping structures.
        owlDatatypeName2lgDatatype_ = new HashMap();
        for (OWLDatatype property : ontology.getDatatypesInSignature()) {
            String propertyName = getLocalName(property);
            // See if the label is not part of the namespace
            String label = resolveLabel(property);
            if (!isNoopNamespace(label)) {
                String lgType = label;
                if (isNoop(lgType)) {
                    lgType = property.getDataRangeType().toString();
                }
                owlDatatypeName2lgDatatype_.put(propertyName, lgType);
                lgSupportedMappings_.registerSupportedDataType(propertyName, property.getIRI().toString(), lgType,
                        false);

            }
        }

    }

    /**
     * This method initializes the user defined OWL object properties in the
     * ontology.
     */
    protected void initSupportedObjectProperties() {
        inversePropCache = new HashMap<String, OWLObjectPropertyExpression>();
        for (OWLObjectProperty prop : ontology.getObjectPropertiesInSignature()) {
            addAssociation(prop);
        }
            for (OWLObjectPropertyExpression propExp : inversePropCache.values()) {
                addInverseHierarchyAssociation(propExp);
        }
    }

    boolean isAnnotationProperty(OWLNamedObject owlProp) {
        return ontology.containsAnnotationPropertyInSignature(owlProp.getIRI());
    }

    /**
     * We need to find which annotation properties have a OWLNamedClass or
     * OWLIndividual as the RHS and treat them as an association. This is being
     * added as per Harold's request to deal with Cecil's bug
     */
    protected void initSupportedAssociationAnnotationProperties() {
        for (OWLAnnotationProperty annotationProperty : ontology.getAnnotationPropertiesInSignature()) {
            Iterator<OWLAnnotationPropertyRangeAxiom> itr = ontology.getAnnotationPropertyRangeAxioms(annotationProperty).iterator();
            while(itr.hasNext()){
                OWLAnnotationPropertyRangeAxiom OwlAx = itr.next();

                if(OwlAx.getRange().getFragment().equals(OWL2Datatype.XSD_ANY_URI.getShortForm())){
                    addAnnotationPropertyAssociations(annotationProperty);
                }
                }

      }
        
        
        
        /*
         * for (Iterator individuals = owlModel_.getOWLIndividuals().iterator();
         * individuals.hasNext();) { RDFIndividual individual = (RDFIndividual)
         * individuals.next(); addAnnotationPropertyAssociations(individual); }
         */

    }

    
    

    private void addAnnotationPropertyAssociations(OWLAnnotationProperty annotationProperty) {
       addAnnotationAsAssociation(annotationProperty);
        
    }

    private AssociationWrapper addAnnotationAsAssociation(OWLAnnotationProperty owlProp) {
        Set<OWLAnnotationAssertionAxiom> assertions = ontology.getAnnotationAssertionAxioms(owlProp.getIRI());
        AssociationWrapper assocWrap = new AssociationWrapper();
        if(!assertions.isEmpty()){
            for(OWLAnnotationAssertionAxiom ax : assertions){
                Property prop = new Property();
                prop.setPropertyName(ax.getProperty().getIRI().getFragment());
                //If not a literal -- don't try to add it as a property.
                if(ax.getValue() instanceof IRI){
                    continue;
                }
                OWLLiteral literal = (OWLLiteral) ax.getValue();
                prop.setValue(Constructors.createText(literal.getLiteral()));
                assocWrap.addProperty(prop);
            }
        }
        String propertyName = getLocalName(owlProp);
        assocWrap.setEntityCode(propertyName);
        String label = resolveLabel(owlProp);
        assocWrap.setAssociationName(label);
        
        String forwardName = getAssociationLabel(label, true);
        assocWrap.setForwardName(forwardName);
        
        // set the description to be the same as the forward name
        assocWrap.setEntityDescription(forwardName);
        
        String nameSpace = getNameSpace(owlProp);
        assocWrap.setEntityCodeNamespace(nameSpace);
        assocWrap = assocManager.addAssociation(lgRelationsContainer_Assoc, assocWrap);
        owlDatatypeName2label_.put(propertyName, label);
        lgSupportedMappings_.registerSupportedAssociation(label, owlProp.getIRI().toString(), label, propertyName,
                nameSpace, true);
        return assocWrap;
    }

    protected AssociationWrapper addAssociation(OWLProperty owlProp) {
        AssociationWrapper assocWrap = new AssociationWrapper();
        String propertyName = getLocalName(owlProp);
        assocWrap.setEntityCode(propertyName);
        String label = resolveLabel(owlProp);
        assocWrap.setAssociationName(label);
        assocWrap.setForwardName(getAssociationLabel(label, true));
        String nameSpace = getNameSpace(owlProp);
        assocWrap.setEntityCodeNamespace(nameSpace);
        
        if (isAnnotationProperty(owlProp)) {
            assocWrap = assocManager.addAssociation(lgRelationsContainer_Assoc, assocWrap);
        } else {
            assocWrap = assocManager.addAssociation(lgRelationsContainer_Roles, assocWrap);
        }
        if (owlProp instanceof OWLObjectProperty) {
            OWLObjectProperty objectProp = (OWLObjectProperty) owlProp;
            boolean isTransitive = EntitySearcher.isTransitive( objectProp, ontology);
            resolveAssociationProperty(assocWrap.getAssociationEntity(), objectProp);
            assocWrap.setIsTransitive(isTransitive);
            List<String> list = new ArrayList<String>();
            list.add(label);
            if (isTransitive) {
                if (prefManager.isDoManageInverseAndTransitiveDesignation()) {
                    if (isThisObjectPropertyAManagedInverse(objectProp)) {
                        processObjectPropertyAsInverse(objectProp);
                    }
                } else {
                    processObjectPropertyInverses(objectProp);
                }
                lgSupportedMappings_.registerSupportedHierarchy(label, 
                        owlProp.getIRI().toString(), label, "@@", list, false, true);
            }
        } else if (owlProp instanceof OWLDataProperty) {
            OWLDataProperty dataProp = (OWLDataProperty) owlProp;
            resolveAssociationProperty(assocWrap.getAssociationEntity(), dataProp);
            assocWrap.setIsTransitive(Boolean.FALSE);
            if (prefManager.getDataTypePropertySwitch().equals("both")
                    || prefManager.getDataTypePropertySwitch().equals("conceptProperty")) {
                addToSupportedPropertyAndMap(label, propertyName, dataProp);
            }

            owlDatatypeName2label_.put(propertyName, label);
        }
        // Add to supported associations ...

        lgSupportedMappings_.registerSupportedAssociation(label, owlProp.getIRI().toString(), label, propertyName,
                nameSpace, true);
        
        return assocWrap;

    }
    
    private boolean isThisObjectPropertyAManagedInverse(OWLObjectProperty objectProp) {
        return Arrays.asList(prefManager.getTransitiveInverseAssociationNames().getName()).
        stream().anyMatch(x -> x.equals(resolveLabel(objectProp)));
    }

    private void processObjectPropertyInverses(OWLObjectProperty objectProp) {
        
        Set<OWLObjectPropertyExpression> propExps = 
                EntitySearcher.getInverses(objectProp, ontology) != null? 
                        EntitySearcher.getInverses(objectProp, ontology).collect(Collectors.toSet()): null;
        Iterator<OWLObjectPropertyExpression> itr = propExps.iterator();
        while(itr.hasNext()) {
        OWLObjectPropertyExpression propExp  = itr.next();
        if(inversePropCache.get(
                propExp.getNamedProperty().getIRI().toString()) == null && 
                 inversePropCache.get(objectProp.getNamedProperty().getIRI().toString()) == null){
            inversePropCache.put(propExp.getNamedProperty().getIRI().toString(), propExp);
        }
        }
        
    }
    
    private void processObjectPropertyAsInverse(OWLObjectProperty objectProp) {

        if(inversePropCache.get(objectProp.getNamedProperty().getIRI().toString()) == null){
            inversePropCache.put(objectProp.getNamedProperty().getIRI().toString(), objectProp);
        }
    }

    protected AssociationWrapper addAssociation(OWLAnnotation owlProp) {
        AssociationWrapper assocWrap = new AssociationWrapper();
        String propertyName = getLocalName(owlProp.getProperty());
        assocWrap.setEntityCode(propertyName);
        String label = resolveLabel(owlProp.getProperty());
        assocWrap.setAssociationName(label);
        assocWrap.setForwardName(getAssociationLabel(label, true));
        String nameSpace = getNameSpace(owlProp.getProperty());
        assocWrap.setEntityCodeNamespace(nameSpace);
        

            assocWrap = assocManager.addAssociation(lgRelationsContainer_Assoc, assocWrap);

        if (owlProp instanceof OWLObjectProperty) {
            OWLObjectProperty objectProp = (OWLObjectProperty) owlProp;
            resolveAssociationProperty(assocWrap.getAssociationEntity(), objectProp);
            assocWrap.setIsTransitive(EntitySearcher.isTransitive(objectProp, ontology));
        } else if (owlProp instanceof OWLDataProperty) {
            OWLDataProperty dataProp = (OWLDataProperty) owlProp;
            resolveAssociationProperty(assocWrap.getAssociationEntity(), dataProp);
            assocWrap.setIsTransitive(Boolean.FALSE);
            if (prefManager.getDataTypePropertySwitch().equals("both")
                    || prefManager.getDataTypePropertySwitch().equals("conceptProperty")) {
                addToSupportedPropertyAndMap(label, propertyName, dataProp);
            }

            owlDatatypeName2label_.put(propertyName, label);
        }
        // Add to supported associations ...

        lgSupportedMappings_.registerSupportedAssociation(label, owlProp.getProperty().getIRI().toString(), label, propertyName,
                nameSpace, true);
        return assocWrap;

    }
    
    protected AssociationWrapper addInverseHierarchyAssociation(OWLObjectPropertyExpression propExp) {

        AssociationWrapper assocWrap = new AssociationWrapper();
        String propertyName = getLocalName(propExp.getNamedProperty());
        assocWrap.setEntityCode(propertyName);
        String label = resolveLabel(propExp.getNamedProperty());
        assocWrap.setAssociationName(label);
        assocWrap.setForwardName(getAssociationLabel(label, true));
        String nameSpace = getNameSpace(propExp.getNamedProperty());
        assocWrap.setEntityCodeNamespace(nameSpace);
        assocWrap = assocManager.addAssociation(lgRelationsContainer_Assoc, assocWrap);
        boolean isTransitive = EntitySearcher.isTransitive(propExp, ontology);
        resolveAssociationProperty(assocWrap.getAssociationEntity(), propExp.getNamedProperty());
        assocWrap.setIsTransitive(isTransitive);
        assocWrap.setInverseTransitive(true);
        List<String> list = new ArrayList<String>();
        list.add(label);
        lgSupportedMappings_.registerSupportedHierarchy(label, 
                propExp.getNamedProperty().getIRI().toString(), label, "@", list, true, true);

        lgSupportedMappings_.registerSupportedAssociation(label, propExp.getNamedProperty().getIRI().toString(), label, propertyName,
                nameSpace, true);
        return assocWrap;
        
    }

    /**
     * Initialize and return the root node for the subclass hierarchy.
     * 
     * @return Concept
     */
    protected Entity initSubtypeRoot() {
        Entity topThing = new Entity();
        topThing.setEntityType(new String[] { EntityTypes.CONCEPT.toString() });
        topThing.setEntityCode(OwlApi2LGConstants.ROOT_CODE);
        topThing.setEntityCodeNamespace(getUserSetNamespace());
        EntityDescription ed = new EntityDescription();
        ed.setContent(OwlApi2LGConstants.ROOT_DESCRIPTION);
        topThing.setEntityDescription(ed);
        topThing.setIsAnonymous(Boolean.TRUE);
        Presentation p = CreateUtils.createPresentation(generatePropertyID(1), OwlApi2LGConstants.ROOT_NAME,
                OwlApi2LGConstants.ROOT_DESCRIPTION, Boolean.TRUE, lgSupportedMappings_, null, null);
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
    protected boolean isEntityCodeRegistered(String namespace, String code) {
        return registeredNameSpaceCode_.contains(bindNamespaceAndCode(namespace, code));
    }

    private String bindNamespaceAndCode(String namespace, String code) {
        return namespace + "#" + code;
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
    protected boolean isRootNode(OWLClass owlClass) {
        OWLClass thing = reasoner.getTopClassNode().getEntities().iterator().next();
        if (prefManager.getMatchRootName() != null) {
            String conceptName = resolveConceptID(owlClass);
            return prefManager.getMatchRootName().matcher(conceptName).matches();
//        } else if (owlClass.isTopEntity()) {
//            return true;
        }else 
        { 
            return reasoner.getSuperClasses(owlClass, true).getFlattened().contains(thing); 
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

    boolean isTransitive(OWLObjectPropertyExpression property) {
        for (OWLAxiom ax : ontology.getSymmetricObjectPropertyAxioms(property)) {
            if (ax != null) {
                return true;
            }
        }
        return false;
    }

    protected void resolveAssociationProperty(AssociationEntity assocEntity, OWLObjectProperty property) {
        int i = 0;
        HashSet<String> characteristics = new HashSet<String>();

        if (EntitySearcher.isFunctional(property, ontology)) {
            characteristics.add(ManchesterOWLSyntax.FUNCTIONAL.toString());
        }
        if (EntitySearcher.isInverseFunctional(property, ontology)) {
            characteristics.add(ManchesterOWLSyntax.INVERSE_FUNCTIONAL.toString());
        }
        if (EntitySearcher.isSymmetric(property, ontology)) {
            characteristics.add(ManchesterOWLSyntax.SYMMETRIC.toString());
        }
        if (EntitySearcher.isTransitive(property, ontology)) {
            characteristics.add(ManchesterOWLSyntax.TRANSITIVE.toString());
        }
        if (EntitySearcher.isReflexive(property, ontology)) {
            characteristics.add(ManchesterOWLSyntax.REFLEXIVE.toString());
        }
        if (EntitySearcher.isIrreflexive(property, ontology)) {
            characteristics.add(ManchesterOWLSyntax.IRREFLEXIVE.toString());
        }
        if (EntitySearcher.isAsymmetric(property, ontology)) {
            characteristics.add(ManchesterOWLSyntax.ASYMMETRIC.toString());
        }

        for (String str : characteristics) {

            Property pro = CreateUtils.createProperty(generatePropertyID(++i), "type", str, lgSupportedMappings_,
                    RDF.type.getURI(), null);
            assocEntity.addProperty(pro);
        }
        addPropertiesToAssociationEntity(assocEntity, property);
    }

    protected void resolveAssociationProperty(AssociationEntity assocEntity, OWLDataProperty property) {
        int i = 0;
        HashSet<String> characteristics = new HashSet<String>();
        if (EntitySearcher.isFunctional(property, ontology)) {
            characteristics.add(ManchesterOWLSyntax.FUNCTIONAL.toString());
        }
        for (String str : characteristics) {
            Property pro = CreateUtils.createProperty(generatePropertyID(++i), "type", str, lgSupportedMappings_,
                    RDF.type.getURI(), null);
            assocEntity.addProperty(pro);
        }
        addPropertiesToAssociationEntity(assocEntity, property);
    }

    class LabelExtractor implements OWLObjectVisitorEx<String>, OWLAnnotationObjectVisitorEx<String> {


        @Override
        public String visit(OWLAnnotation annotation) {
            if (annotation.getProperty().isLabel()) {
                OWLLiteral c = (OWLLiteral) annotation.getValue();
                return c.getLiteral();
            }
            return null;
        }
    }

    /**
     * Return the first label assigned to the given resource, or the rdf
     * resource name if no labels are assigned.
     * 
     * @param rdf
     * @return A text label for the resource.
     */
    protected String resolveLabel(OWLEntity entity) {
        LabelExtractor le = new LabelExtractor();
        Set<OWLAnnotation> annotations = EntitySearcher.getAnnotations(entity, ontology).collect(Collectors.toSet());
        for (OWLAnnotation anno : annotations) {
            String result = anno.accept(le);
            if (result != null) {
                return result;
            }
        }
        return getLocalName(entity);

    }
    
    protected List<String> resolveLabels(OWLEntity entity){
        List<String> list = new ArrayList<String>();
        LabelExtractor le = new LabelExtractor();
        Set<OWLAnnotation> annotations = EntitySearcher.getAnnotations(entity, ontology).collect(Collectors.toSet());
        for (OWLAnnotation anno : annotations) {
            String result = anno.accept(le);
            if(result != null || StringUtils.isNotBlank(result)){
                list.add(result);
            }
        }
        return list;
    }

    protected Map<String, String> resolveXMLTagsAndValues(String src) {
        Map<String, String> tags2vals = new HashMap<String, String>();
        if (StringUtils.isNotBlank(src)) {
            tags2vals = bxp.parseDocument(src, messages_);
        }
        return tags2vals;
    }

    /**
     * Return the concept identifier mapped to the given rdf resource, or the
     * null if no mapping exists.
     * 
     * @param rdfResource
     * @return java.lang.String
     */
    protected String resolveConceptID(OWLEntity rdfResource) {
        String code = owlClassName2Conceptcode_.get(rdfResource.getIRI().toString());
        if (code != null)
            return code;
        // Updated on 05/28/08: if the concept ID is null,
        // it means that either the concept has not been processed yet
        // OR this is an anonymous concept. So, we need to return null
        // instead of the rdf name.
        return null;
    }

    protected String resolveConceptIDfromIRI(IRI iri){
       return owlClassName2Conceptcode_.get(iri.toString());
    }
    
    protected String resolveInstanceIDfromIRI(IRI iri){
        return owlInstanceName2code_.get(iri.toString());
     }
    
    
    protected void processAllAnnotationProperties(Snapshot snap) {
        snap = SimpleMemUsageReporter.snapshot();
        messages_.info("Read Time : " + SimpleMemUsageReporter.formatTimeDiff(snap.getTimeDelta(null))
                + " Heap Usage: " + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsage()) + " Heap Delta:"
                + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsageDelta(null)));
        messages_.info("Processing OWL Individuals ...");

        int count = 0;
        owlAnnotationPropertiesTocode_ = new HashMap();

        // The idea is to iterate through all the OWL individuals, and register
        // them as well as find out additional associations (e.g,. From)
        for (OWLAnnotationProperty aProp : ontology.getAnnotationPropertiesInSignature()) {
           // aProp.getReferencingAxioms(ontology)
            Entity lgProp = resolveAnnotationProperty(aProp);
            if (lgProp != null) {
                addEntity(lgProp);
            }
            count++;
            if (count % 1000 == 0) {
                messages_.info("OWL individuals processed: " + count);
            }

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

        // If we found at least one, register the supported entity type.
        if (!owlAnnotationPropertiesTocode_.isEmpty()) {
            String name = EntityTypes.CONCEPT.toString();
            lgSupportedMappings_.registerSupportedEntityType(name, null, name, false);
        }
    } // end
    
    private Entity resolveAnnotationProperty(OWLAnnotationProperty aProp) {
        String propName = getLocalName(aProp);

        if (isNoopNamespace(propName))
            return null;

        String label = resolveLabel(aProp);

        // Create the raw EMF individual and assign label as initial
        // description,
        // which may be overridden later by preferred text.
        Entity lgInstance = new Entity();
        lgInstance.setEntityType(new String[] { EntityTypes.CONCEPT.toString() });
        EntityDescription ed = new EntityDescription();
        ed.setContent(label);
        lgInstance.setEntityDescription(ed);
        lgInstance.setEntityCode(propName);
        String nameSpace = getNameSpace(aProp);
        lgInstance.setEntityCodeNamespace(nameSpace);

        resolveEntityProperties(lgInstance, aProp);

       owlAnnotationPropertiesTocode_.put(aProp.getIRI().toString(), lgInstance.getEntityCode());
        return lgInstance;
    }

    /**
     * Process the instance information in the ontology.
     * 
     */
    protected void processAllInstanceAndProperties(Snapshot snap) {
        snap = SimpleMemUsageReporter.snapshot();
        messages_.info("Read Time : " + SimpleMemUsageReporter.formatTimeDiff(snap.getTimeDelta(null))
                + " Heap Usage: " + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsage()) + " Heap Delta:"
                + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsageDelta(null)));
        messages_.info("Processing OWL Individuals ...");

        int count = 0;
        owlInstanceName2code_ = new HashMap();
        //We need to initialize a record of punned instances to avoid duplicate associations
        // The idea is to iterate through all the OWL individuals, and register
        // them as well as find out additional associations (e.g,. From)
        for (OWLNamedIndividual individual : ontology.getIndividualsInSignature()) {

            Entity lgInstance = resolveIndividual(individual);
            if (lgInstance != null) {
                addEntity(lgInstance);
            }
            count++;
            if (count % 1000 == 0) {
                messages_.info("OWL individuals processed: " + count);
            }

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
        
        // If we found at least one, register the supported entity type.
        if (!owlInstanceName2code_.isEmpty()) {
            String name = EntityTypes.INSTANCE.toString();
            lgSupportedMappings_.registerSupportedEntityType(name, null, name, false);
        }
    } // en

    
    //OWL2 provides for punning of some OWL classes to individuals allowing the sharing of unique identifiers.
    //Since this causes issues in LexEVS we need to identify these punned instances before they become
    //duplicate relationships and provide them with unique identifiers. 
    private void initPunnedInstanceNamesToCode() {
        int count = 0;
        for (OWLAnnotationProperty ap : ontology
                .getAnnotationPropertiesInSignature()) {
            IRI iri = ap.getIRI();
            if (ontology.containsDataPropertyInSignature(iri)) {

                OWLDataProperty dp = factory.getOWLDataProperty(iri);
                for(OWLAxiom axiom: ontology.getReferencingAxioms(dp)){
                    if(axiom instanceof OWLDataPropertyAssertionAxiom
                            && !((OWLDataPropertyAssertionAxiom) axiom).getSubject()
                            .isAnonymous()){
                        
                        OWLDataPropertyAssertionAxiom assertion = (OWLDataPropertyAssertionAxiom) axiom;
                        OWLNamedIndividual subject = assertion.getSubject()
                                .asOWLNamedIndividual();
                        Entity lgInstance =  resolvePunnedIndividual(subject);
                        if (lgInstance != null) {
                            addEntity(lgInstance);
                        }
                        count++;
                        if (count % 1000 == 0) {
                            messages_.info("OWL individuals processed: " + count);
                        }
                     
                    }
                }
            }
            if (ontology.containsObjectPropertyInSignature(iri)) {
                OWLObjectProperty op = factory.getOWLObjectProperty(iri);
                for (OWLAxiom axiom : ontology.getReferencingAxioms(op)) {
                    if (axiom instanceof OWLObjectPropertyAssertionAxiom
                            && !((OWLObjectPropertyAssertionAxiom) axiom).getSubject()
                                    .isAnonymous()) {
                        OWLObjectPropertyAssertionAxiom assertion = (OWLObjectPropertyAssertionAxiom) axiom;
                        if (!(assertion.getObject().isAnonymous())) {
                            OWLNamedIndividual subject = assertion.getSubject()
                                    .asOWLNamedIndividual();                          
                            Entity lgInstance = resolvePunnedIndividual(subject);
                            if (lgInstance != null) {
                                addEntity(lgInstance);
                            }
                            count++;
                            if (count % 1000 == 0) {
                                messages_.info("OWL individuals processed: " + count);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Defines an EMF instance.
     */
    protected Entity resolveIndividual(OWLNamedIndividual owlIndividual) {
        String individualName = getLocalName(owlIndividual);

        if (isNoopNamespace(individualName))
            return null;

        String label = resolveLabel(owlIndividual);
       
        // Create the raw EMF individual and assign label as initial
        // description,
        // which may be overridden later by preferred text.
        Entity lgInstance = new Entity();
        lgInstance.setEntityType(new String[] { EntityTypes.INSTANCE.toString() });
        EntityDescription ed = new EntityDescription();
        ed.setContent(label);
        lgInstance.setEntityDescription(ed);
        lgInstance.setEntityCode(individualName);
        String nameSpace = getNameSpace(owlIndividual);
        lgInstance.setEntityCodeNamespace(nameSpace);

        // Is deprecated? If so, mark as inactive.
        // if (rdfResource instanceof OWLIndividual
        // &&
        // OWLNames.Cls.DEPRECATED_CLASS.equals(rdfResource.getRDFType().getName()))
        // lgInstance.setIsActive(Boolean.FALSE);

        // Set the 'isDefined' property.
        // if (rdfResource instanceof OWLNamedClass) {
        // OWLNamedClass owlNamedClass = (OWLNamedClass) rdfResource;
        // lgInstance.setIsDefined(owlNamedClass.isDefinedClass());
        // }

        // Updated 05/28/2008: handle the individual OWLObjectProperties
        // and OWLDatatypeProperties. Essentially, both are represented
        // as instanceProperties.
        resolveEntityProperties(lgInstance, owlIndividual);

        // Remember the rdf to code mapping and return.
        owlInstanceName2code_.put(owlIndividual.getIRI().toString(), lgInstance.getEntityCode());
        return lgInstance;
    }
    
    //Create an instance entity from an owl individual and mark it with the instance entity type
    protected Entity resolvePunnedIndividual(OWLNamedIndividual owlIndividual) {
        StringBuilder builder = new StringBuilder();
        //applying a suffix to the OWL identifier to distinguish it in LexEVS
        builder.append(getLocalName(owlIndividual)).append(OwlApi2LGConstants.INSTANCE_SUFFIX);
        String individualName = builder.toString();

        if (isNoopNamespace(individualName))
            return null;

        String label = resolveLabel(owlIndividual);
       
        // Create the raw EMF individual and assign label as initial
        // description,
        // which may be overridden later by preferred text.
        Entity lgInstance = new Entity();
        lgInstance.setEntityType(new String[] { EntityTypes.INSTANCE.toString() });
        EntityDescription ed = new EntityDescription();
        ed.setContent(label);
        lgInstance.setEntityDescription(ed);
        lgInstance.setEntityCode(individualName);
        String nameSpace = getNameSpace(owlIndividual);
        lgInstance.setEntityCodeNamespace(nameSpace);

        resolveEntityProperties(lgInstance, owlIndividual);

        // Remember the rdf to code mapping and return.
        owlInstanceName2code_.put(owlIndividual.getIRI().toString(), lgInstance.getEntityCode());
        return lgInstance;
    }

    protected void processAllInstanceRelations() {

        // Process the associations (e.g.,
        // rdf:type, DifferentFrom, SameAs, ObjectProperties)
        for (OWLNamedIndividual individual : ontology.getIndividualsInSignature()) {

            String nameSpace = getNameSpace(individual);
            String lgCode = resolveInstanceID(individual);
            if (lgCode != null) {
                AssociationSource source = CreateUtils.createAssociationSource(lgCode, nameSpace);
                resolveRdfTypeRelations(source, individual);
                resolveDifferentFromRelations(source, individual);
                resolveSameAsRelations(source, individual);
                resolveOWLObjectPropertyRelations(source, individual);
                // resolveAnnotationPropertyRelations(source, individual);
                // resolveDatatypePropertyRelations(source, individual);
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
        // for (Iterator diffIndi = ontology.getOWLAllDifferents().iterator();
        // diffIndi.hasNext();) {
        // OWLAllDifferent allDifferent = (OWLAllDifferent) diffIndi.next();
        // String nameSpace = getNameSpace(allDifferent.getNamespace());
        // AssociationSource source =
        // CreateUtils.createAssociationSource(allDifferent.getBrowserText(),
        // nameSpace);
        // String nameSpaceTarget = getNameSpace(allDifferent.getNamespace());
        // AssociationTarget target =
        // CreateUtils.createAssociationTarget(allDifferent.getBrowserText(),
        // nameSpaceTarget);
        // relateAssociationSourceTarget(assocManager.getAllDifferent(), source,
        // target);
        // }

    }

    /**
     * Defines EMF RDFType relations based on OWL source.
     * 
     */
    protected void resolveRdfTypeRelations(AssociationSource source, OWLNamedIndividual individual) {
        for (OWLClassExpression item : EntitySearcher.getTypes(individual, ontology).collect(Collectors.toList())) {
            if (!item.isAnonymous()) {
                relateAssocSourceWithOWLClassExpressionTarget(EntityTypes.CONCEPT, assocManager.getRdfType(), source,
                        item, null);
            }
        }
    }

    /**
     * Return the instance identifier mapped to the given rdf resource name, or
     * the rdf class name if no mapping exists.
     * 
     * @param owlIndividual
     * @return java.lang.String
     */
    protected String resolveInstanceID(OWLNamedIndividual owlIndividual) {
        String rdfLocalName = getLocalName(owlIndividual);
        String code = owlInstanceName2code_.get(owlIndividual.getIRI().toString());
        if (code != null)
            return code;
        return null;
    }

    protected AssociationSource addAssocSrc2Assoc(AssociationWrapper aw, AssociationSource assocSource) {
        String qName = aw.getAssociationEntity().getEntityCode() + "::" + assocSource.getSourceEntityCode();
        AssociationSource source = null;
        if (lgAssocToAssocSrc_.containsKey(qName)) {
            source = (AssociationSource) lgAssocToAssocSrc_.get(qName);
        } else {
            lgAssocToAssocSrc_.put(qName, assocSource);
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

    protected String stripLastColon(String str) {
        if (str != null && str.lastIndexOf(":") != -1) {
            str = str.substring(0, str.lastIndexOf(":"));
        }
        return str;
    }

    protected String getFromLastIndexOfColonOrHash(String str) {
//        if (str != null && str.lastIndexOf(":") != -1) {
//            str = str.substring(str.lastIndexOf(":") + 1);
//        }

        if (str != null && str.lastIndexOf("#") != -1) {
            str = str.substring(str.lastIndexOf("#") + 1);
        }
        
        if (str != null && str.endsWith(">")) {
            str = str.substring(0, str.length() - 1);
        }
        
        if (str != null && str.startsWith("<")){
            str = str.substring(1, str.length());
        }
        return str;
    }

    
    OWLEntity getEntity(OWLPropertyExpression tgtProp) {
        OWLEntity propEntity = null;
        if (!tgtProp.isAnonymous() && tgtProp instanceof OWLObjectPropertyExpression) {
            propEntity = ((OWLObjectPropertyExpression) tgtProp).asOWLObjectProperty();
        }
        if (!tgtProp.isAnonymous() && tgtProp instanceof OWLDataPropertyExpression) {
            propEntity = ((OWLDataPropertyExpression) tgtProp).asOWLDataProperty();
        }    
        return propEntity;
    }
    
    String getLocalName(IRI iri) {
        if(!StringUtils.isBlank(iri.getFragment())){ return iri.getFragment();}
        ManchesterOWLSyntaxPrefixNameShortFormProvider prov = new ManchesterOWLSyntaxPrefixNameShortFormProvider(ontology);
        // find the base IRI string 
        Map<String, String> prefix2NamespaceMapp = prov
                .getPrefixName2PrefixMap();
        String prefix = prefix2NamespaceMapp.values().stream().filter(x -> iri.getIRIString().contains(x)).findFirst().get();
        String fragment = StringUtils.remove(iri.getIRIString(), prefix);
        return fragment;
    }
    
    String getLocalName(OWLEntity entity) {
        if(!StringUtils.isBlank(entity.getIRI().getFragment())){ return entity.getIRI().getFragment();}
        ManchesterOWLSyntaxPrefixNameShortFormProvider prov = new ManchesterOWLSyntaxPrefixNameShortFormProvider(ontology);
        // find the base IRI string 
        Map<String, String> prefix2NamespaceMapp = prov
                .getPrefixName2PrefixMap();
        String prefix = prefix2NamespaceMapp.values().stream().filter(x -> entity.getIRI().getIRIString().contains(x)).findFirst().get();
        String fragment = StringUtils.remove(entity.getIRI().getIRIString(), prefix);
        return fragment;
    }

    protected String getNameSpace(OWLEntity entity) {
        return getNameSpace(entity.getIRI());
    }

    public String getNameSpace(IRI iri) {
        String iriString = iri.toString();
        String ns = XMLUtils.getNCNamePrefix(iriString);
        
        return getIRIfromIRIString(ns);
    }
        
    private String getUserSetNamespace() {
        String defaultPrefix = ":";
        String iriString = "";
        ManchesterOWLSyntaxPrefixNameShortFormProvider prov = new ManchesterOWLSyntaxPrefixNameShortFormProvider(ontology);
        // find the base IRI string 
        Map<String, String> prefix2NamespaceMap = prov
                .getPrefixName2PrefixMap();
        for (Iterator<String> i$ = prefix2NamespaceMap.keySet().iterator(); i$.hasNext();) {
            String keyName = (String) i$.next();
            String prefix = (String) prefix2NamespaceMap.get(keyName);
            if (defaultPrefix.equals(keyName)) {
               
                iriString = prefix;
                break;
            }
        }       
        return getIRIfromIRIString(iriString);
    }
    
    private String getIRIfromIRIString(String iriString) {
        String prefixName = "";
        ManchesterOWLSyntaxPrefixNameShortFormProvider prov = new ManchesterOWLSyntaxPrefixNameShortFormProvider(ontology);
        // check if there is a default one that has bee set.  If not, find the default one below
        if (!iriString.isEmpty()){
            Map<String, String> prefix2NamespaceMap = prov
                    .getPrefixName2PrefixMap();
            for (Iterator i$ = prefix2NamespaceMap.keySet().iterator(); i$.hasNext();) {
                String keyName = (String) i$.next();
                String prefix = (String) prefix2NamespaceMap.get(keyName);
                if (iriString.equals(prefix)) {
                    prefixName = keyName;
                    // check for additional namespaces, if the current one found is empty (the default one)
                    if (!prefixName.equals(":")) {
                        break;
                    }
                }
            }
        }
        
        if (StringUtils.isNotEmpty(prefixName)) {
            if (prefixName.endsWith(":")) {
                prefixName = prefixName.substring(0, prefixName.length() - 1);
            }
        }
        if (StringUtils.isEmpty(prefixName)) {
            return getDefaultNameSpace();
        } else {
            return prefixName;
        }
    }

    // /////////////////////////////////////////////
    // /////////// STORAGE METHODS /////////////////
    // /////////////////////////////////////////////

    protected void addEntity(Entity lgEntity) {
        if (isEntityCodeRegistered(lgEntity.getEntityCodeNamespace(), lgEntity.getEntityCode())) {
            messages_.info("Entity " + lgEntity.getEntityCode() + " already exists.");
            return;
        }
        if (memoryProfile_ == OwlApi2LGConstants.MEMOPT_ALL_IN_MEMORY) {
            lgScheme_.getEntities().addEntity(lgEntity);
        } else {
            try {
                writeEntity(lgEntity);
            } catch (Exception e) {
               System.out.println("Error on Entity Insertion for Code: " + lgEntity.getEntityCode() + " *** SQL Stack Trace:  " + e.toString());
               messages_.error("Error on Entity Insertion for Code: " + lgEntity.getEntityCode() + " *** SQL Stack Trace:  " + e.toString());
                return;
            }
        }
        registeredNameSpaceCode_.add(bindNamespaceAndCode(lgEntity.getEntityCodeNamespace(), lgEntity.getEntityCode()));
        if (lgEntity instanceof Entity)
            conceptCount_++;
    }

    protected void addEntity(AssociationEntity lgEntity) {
        String trace = null;
        if (isEntityCodeRegistered(lgEntity.getEntityCodeNamespace(), lgEntity.getEntityCode())) {
            messages_.info("Entity " + lgEntity.getEntityCode() + " already exists.");
            return;
        }
        if (memoryProfile_ == OwlApi2LGConstants.MEMOPT_ALL_IN_MEMORY) {
            lgScheme_.getEntities().addAssociationEntity(lgEntity);
        } else {
            try {
                writeEntity(lgEntity);
            } catch (Exception e) {
                trace = e.getStackTrace().toString();
                messages_.warn("Entity failed to load for Entity Code: " + lgEntity.getEntityCode() 
                + " *** SQL Exception logged: " + trace);
                
                return;
            }
            finally{
                if(trace != null){
                    System.out.println(trace);
                }
            }
        }
        registeredNameSpaceCode_.add(bindNamespaceAndCode(lgEntity.getEntityCodeNamespace(), lgEntity.getEntityCode()));
        if (lgEntity instanceof Entity)
            conceptCount_++;
    }

    protected void writeEntity(Entity entity) throws Exception {
        try {
            if (tempEmfScheme_ == null) {
                tempEmfScheme_ = new CodingScheme();
                tempEmfScheme_.setCodingSchemeName(lgScheme_.getCodingSchemeName());
            }
            if (tempEmfEntityList_ == null) {
                tempEmfEntityList_ = new Entities();
                tempEmfScheme_.setEntities(tempEmfEntityList_);
            }
            tempEmfEntityList_.addEntity(entity);

            String uri = lgScheme_.getCodingSchemeURI();
            String version = lgScheme_.getRepresentsVersion();

            databaseServiceManager.getEntityService().insertEntity(uri, version, entity);

        } finally {
            tempEmfEntityList_.removeEntity(entity);
        }
    }

    private void relateAssocSourceWithRDFResourceTarget(EntityTypes type, AssociationWrapper aw,
            AssociationSource source, OWLPropertyExpression tgtProp) {
        OWLEntity propEntity = getEntity(tgtProp);
        if (propEntity != null) {
            String targetID = getLocalName(propEntity);

            if (type == EntityTypes.CONCEPT) {
                targetID = resolveConceptID(propEntity);
            }

            if (StringUtils.isNotBlank(targetID)) {
                String nameSpace = getNameSpace(propEntity);
                AssociationTarget target = CreateUtils.createAssociationTarget(targetID, nameSpace);
                relateAssociationSourceTarget(aw, source, target);
            }
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
            AssociationSource source, OWLNamedIndividual tgtResource) {
        String targetID = getLocalName(tgtResource);

        if (type == EntityTypes.CONCEPT) {
            targetID = resolveConceptID(tgtResource);
        } else if (type == EntityTypes.INSTANCE) {
            targetID = resolveInstanceID(tgtResource);
        }

        if (StringUtils.isNotBlank(targetID)) {
            String nameSpace = getNameSpace(tgtResource);
            AssociationTarget target = CreateUtils.createAssociationTarget(targetID, nameSpace);
            relateAssociationSourceTarget(aw, source, target);
        }
    }

    /**
     * The OWLClassExpression is used to compute the target. The EntityType is
     * passed into the function so that we know what kind of a lookup needs to
     * be made to get the code. For NCI, the code of the entity could be
     * different from the localName of the resource. We need to do a lookup to
     * get the actual code.
     * 
     * @param type
     * @param assoc
     * @param source
     * @param tgtResource
     */
    protected void relateAssocSourceWithOWLClassExpressionTarget(EntityTypes type, AssociationWrapper aw,
            AssociationSource source, OWLClassExpression tgtResource, OWLAxiom ax) {
        if (tgtResource.isAnonymous()) {
            String lgCode = this.resolveAnonymousClass(tgtResource, source);
            String namespace = getUserSetNamespace();
            AssociationTarget target = CreateUtils.createAssociationTarget(lgCode, namespace);
            processAnnotationsOfOWLAxiom(ax, target);
            relateAssociationSourceTarget(aw, source, target);
            
        } else {
            relateAssocSourceWithOWLClassTarget(EntityTypes.CONCEPT, aw, source, tgtResource.asOWLClass(), ax);
        }
    }
    
    
    protected void relateAssocSourceWithIriTarget(EntityTypes type, AssociationWrapper aw,
            AssociationSource source, IRI tgtResource, OWLAxiom ax) {
            String lgCode = getLocalName(tgtResource);
            String namespace = getNameSpace(tgtResource);
            AssociationTarget target = CreateUtils.createAssociationTarget(lgCode, namespace);
            processAnnotationsOfOWLAxiom(ax, target);
            relateAssociationSourceTarget(aw, source, target);
                   
    }
    
    /**
     * The OWLClass is used to compute the target. The EntityType is passed into
     * the function so that we know what kind of a lookup needs to be made to
     * get the code. For NCI, the code of the entity could be different from the
     * localName of the resource. We need to do a lookup to get the actual code.
     * 
     * @param type
     * @param assoc
     * @param source
     * @param tgtResource
     */
    protected void relateAssocSourceWithOWLClassTarget(EntityTypes type, AssociationWrapper aw,
            AssociationSource source, OWLClass tgtResource, OWLAxiom ax) {
        String targetID = getLocalName(tgtResource);

        if (type == EntityTypes.CONCEPT) {
            targetID = resolveConceptID(tgtResource);
        }

        if (StringUtils.isNotBlank(targetID)) {
            String nameSpace = getNameSpace(tgtResource);
            AssociationTarget target = CreateUtils.createAssociationTarget(targetID, nameSpace);
            processAnnotationsOfOWLAxiom(ax, target);
            relateAssociationSourceTarget(aw, source, target);
           
        }
    }
    
    protected void relateAssocSourceWithAnnotationTarget(EntityTypes type, AssociationWrapper aw,
            AssociationSource source, OWLAnnotation tgtResource, OWLAxiom ax, String prefix) {

        String targetID = null;
        IRI targetIri = null;
        if(tgtResource.getValue() instanceof OWLLiteral){
        OWLLiteral val = (OWLLiteral) tgtResource.getValue();
        targetID = val.getLiteral();
        targetIri = IRI.create(targetID);
        }else if (tgtResource.getValue() instanceof IRI){
           targetIri = (IRI)tgtResource.getValue();
        }
        
        if (type == EntityTypes.CONCEPT ) {
            targetID = resolveConceptIDfromIRI(targetIri);
        }else if (type == EntityTypes.INSTANCE){
            targetID = resolveInstanceIDfromIRI(targetIri);
        }

        if (StringUtils.isNotBlank(targetID)) {
            String nameSpace = getNameSpace(targetIri);
            AssociationTarget target = CreateUtils.createAssociationTarget(targetID, nameSpace);
            processAnnotationsOfOWLAxiom(ax, target);
            relateAssociationSourceTarget(aw, source, target);
           
        }
    }
    
    protected void relateAssocSourceWithAnnotationIRITarget(EntityTypes type, AssociationWrapper aw,
            AssociationSource source, OWLAnnotation tgtResource, OWLAxiom ax, String prefix) {
        String targetID = null;
        IRI targetIri = null;
        if(tgtResource.getValue() instanceof OWLLiteral){
        OWLLiteral val = (OWLLiteral) tgtResource.getValue();
        targetID = val.getLiteral();
        targetIri = IRI.create(targetID);
        }else if (tgtResource.getValue() instanceof IRI){
           targetIri = (IRI)tgtResource.getValue();
        }
        
        if (type == EntityTypes.CONCEPT ) {
            targetID = resolveConceptIDfromIRI(targetIri);
        }else if (type == EntityTypes.INSTANCE){
            targetID = resolveInstanceIDfromIRI(targetIri);
        }

        if (StringUtils.isNotBlank(targetID)) {
            String nameSpace = getNameSpace(targetIri);
            AssociationTarget target = CreateUtils.createAssociationTarget(targetID, nameSpace);
            processAnnotationsOfOWLAxiom(ax, target);
            relateAssociationSourceTarget(aw, source, target);
           
        }
    }

    
    protected String stripDataType(String shortForm){
        
        String substring = null;
        String prefix = null;
        if(shortForm.indexOf("^") > -1){
        substring = shortForm.substring(shortForm.indexOf("^"));
        prefix = shortForm.replace(substring,"");
        prefix = prefix.replace("\"", "");
        return prefix;
        }
        return shortForm;
    }

    protected void relateAssociationSourceTarget(AssociationWrapper aw, AssociationSource source,
            AssociationTarget target) {

        if (memoryProfile_ == OwlApi2LGConstants.MEMOPT_ALL_IN_MEMORY) {
            source = addAssocSrc2Assoc(aw, source);
            if (target != null) {
                RelationsUtil.subsume(source, target);
            }
        } else {
            // clear the AssociationSource of left-over targets.
            source.setTarget(new AssociationTarget[0]);
            writeAssociationSourceTarget(aw, source, target);
        }
    }

    protected void relateAssociationSourceData(AssociationWrapper aw, AssociationSource source, AssociationData data) {
        if (memoryProfile_ == OwlApi2LGConstants.MEMOPT_ALL_IN_MEMORY) {
            source = addAssocSrc2Assoc(aw, source);
            if (data != null) {
                source.addTargetData(data);
            }
        } else {
            if (data != null)
                source.addTargetData(data);
            writeAssociationSourceTarget(aw, source, null);
        }
    }

    protected void writeAssociationSourceTarget(AssociationWrapper aw, AssociationSource source,
            AssociationTarget target) {
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

            aw.getAssociationPredicate().addSource(source);

            String uri = lgScheme_.getCodingSchemeURI();
            String version = lgScheme_.getRepresentsVersion();

            databaseServiceManager.getAssociationService().insertAssociationSource(uri, version,
                    aw.getRelationsContainerName(), aw.getAssociationPredicate().getAssociationName(), source);

        } catch (Exception e) {
            this.messages_.warn("Error Inserting AssociationSource.", e);
        } finally {
            aw.getAssociationPredicate().removeSource(source);
            if (target != null)
                source.removeTarget(target);
        }
    }

    public void storeAssociationPredicateIfNeeded(final String uri, final String version, final String relationsName,
            final AssociationPredicate predicate) {
        DaoCallbackService daoCallbackService = databaseServiceManager.getDaoCallbackService();
        daoCallbackService.executeInDaoLayer(new DaoCallback<Object>() {

            public Object execute(DaoManager daoManager) {
                String codingSchemeId = daoManager.getCurrentCodingSchemeDao().getCodingSchemeUIdByUriAndVersion(uri,
                        version);
                String relationsId = daoManager.getCurrentAssociationDao()
                        .getRelationUId(codingSchemeId, relationsName);
                //Adding this step since predicates are not versionable and may be changed in revisions.
               String predicateUId = daoManager.getCurrentAssociationDao().getAssociationPredicateUIdByContainerName(codingSchemeId, relationsName, predicate.getAssociationName());
                if(predicateUId == null)
               daoManager.getCurrentAssociationDao().insertAssociationPredicate(codingSchemeId, relationsId,
                        predicate, true);
                return null;
            }
        });
    }
    
    String getVersionInfo() {
        String version = "";
        for (OWLAnnotation annotation : ontology.getAnnotations()) {
            String propName = getLocalName(annotation.getProperty());
            if (propName.contains("versionInfo")) {
                return getAnnotationValue(annotation);
            }
        }
        return version;
    }

    String getAnnotationValue(OWLAnnotation annotation) {
        OWLAnnotationValue value = annotation.getValue();
        String annotationValue = "";
        if (value instanceof OWLLiteral) {
            OWLLiteral literal = (OWLLiteral) value;
            annotationValue = literal.getLiteral();
        }
        return annotationValue;
    }

    protected void updateApproximateConceptNumber() {
        if (memoryProfile_ == OwlApi2LGConstants.MEMOPT_ALL_IN_MEMORY) {
            lgScheme_.setApproxNumConcepts(new Long(lgScheme_.getEntities().getEntity().length));
        } else {
        	
            lgScheme_.setApproxNumConcepts(new Long(conceptCount_));

            String revisionId = UUID.randomUUID().toString();

            CodingScheme csToUpdate = DaoUtility.deepClone(lgScheme_);
            csToUpdate.setEntities(new Entities());
            csToUpdate.setProperties(new Properties());
            csToUpdate.setRelations(new ArrayList<Relations>());
            EntryState es = new EntryState();
            es.setChangeType(ChangeType.MODIFY);
            es.setContainingRevision(revisionId);
            csToUpdate.setEntryState(es);

            Revision revision = new Revision();
            revision.setRevisionId(revisionId);
            ChangedEntry ce = new ChangedEntry();
            ce.setChangedCodingSchemeEntry(csToUpdate);
            revision.addChangedEntry(ce);

            try {
                LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAuthoringService()
                        .loadRevision(revision, null, false);
            } catch (Exception e) {
                this.messages_.warn("Failed to update the Approximate Number of Concepts.", e);
            }
        }
    }

    AssociationQualification createAssociationQualification(OWLRestriction rdfProp,
            SupportedMappings lgSupportedMappings_) {
        String label;
        label = parseQualifierNameFromManchesterRender(renderer.render(rdfProp));
            if (label.isEmpty()) {
                label = rdfProp.getClassExpressionType().getName();
            }
         String value = parseQualifierValueFromManchesterRender(renderer.render(rdfProp)); 
         //in case we've added a namespace prefix
        value = value != null? value.substring(value.lastIndexOf(':') +  1, value.length()): value;
        AssociationQualification lgQual = CreateUtils.createAssociationQualification(label, null, value == null? "":value,
                lgSupportedMappings_);
        return lgQual;
    }
    
    private String parseQualifierValueFromManchesterRender(String rendered) {
        String[] tokens = rendered.split(" ");
        String[] qualifierTokens = Arrays.copyOfRange(tokens, 2, tokens.length);
      StringBuilder builder = new StringBuilder();
      for(int i = 0; i < qualifierTokens.length; i++){  

          builder.append(qualifierTokens[i].trim()); 

              builder.append(" ");
      }
      return builder.toString().trim();
    }

    private String parseQualifierNameFromManchesterRender(String rendered){
        String[] tokens = rendered.split(" ");
        return tokens[1];
    }
    
    /**
     * Process annotations of OWLAxiom as association qualifiers 
     * 
     * @param axiom
     * @param opTarget
     */
    private void processAnnotationsOfOWLAxiom(OWLAxiom axiom, AssociationTarget opTarget) {
        if (axiom != null) {
            for (OWLAnnotation annotation : axiom.getAnnotations()) {
                String annotationName = getLocalName(annotation.getProperty());
                String annotationValue = "";
                OWLAnnotationValue value = annotation.getValue();
                if (value instanceof OWLLiteral) {
                    OWLLiteral literal = (OWLLiteral) value;
                    annotationValue = literal.getLiteral();
                }
                if (StringUtils.isNotBlank(annotationName) && StringUtils.isNotBlank(annotationValue)) {
                    AssociationQualification acQual = CreateUtils.createAssociationQualification(annotationName, null,
                            annotationValue, lgSupportedMappings_);
                    if (opTarget != null) {
                        opTarget.addAssociationQualification(acQual);
                    }

                }
            }
    }
    }

    public OWLOntology getOntology() {
        return ontology;
    }

    public void setOntology(OWLOntology ontology) {
        this.ontology = ontology;
    }

} // end of the class
