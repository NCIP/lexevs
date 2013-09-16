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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;
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
import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntax;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.locator.LexEvsServiceLocator;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.XMLUtils;
import org.semanticweb.owlapi.model.DataRangeType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationObjectVisitorEx;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLCardinalityRestriction;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLHasValueRestriction;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLNamedObject;
import org.semanticweb.owlapi.model.OWLNaryBooleanClassExpression;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectHasSelf;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.model.OWLPropertyExpression;
import org.semanticweb.owlapi.model.OWLPropertyRange;
import org.semanticweb.owlapi.model.OWLQuantifiedDataRestriction;
import org.semanticweb.owlapi.model.OWLQuantifiedObjectRestriction;
import org.semanticweb.owlapi.model.OWLRestriction;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.OWLObjectVisitorExAdapter;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import uk.ac.manchester.cs.owl.owlapi.mansyntaxrenderer.ManchesterOWLSyntaxPrefixNameShortFormProvider;

import com.hp.hpl.jena.vocabulary.RDF;

import edu.mayo.informatics.lexgrid.convert.Conversions.SupportedMappings;

import edu.mayo.informatics.lexgrid.convert.exceptions.LgConvertException;
import edu.stanford.smi.protegex.owl.model.RDFSNames;

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
                // Exception logged by SQLReadWrite
                return null;
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

        // Step 2: Process OWL individuals. Essentially, determine to which
        // classes these instances belong to, as well as, relations between
        // the individuals themselves (e.g., differentFrom)?

        messages_.info("Processing OWL Individuals.....");
        processAllInstanceAndProperties(snap);

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
        for (OWLClass namedClass : ontology.getClassesInSignature()) {
            resolveConcept(namedClass);
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
                // resolveComplementOfRelations(source, namedClass);
                resolveOWLObjectPropertyRelations(source, namedClass);
                // resolveAnnotationPropertyRelations(source, namedClass);
                // resolveDatatypePropertyRelations(source, namedClass);
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
            for (OWLClassExpression domain : prop.getDomains(ontology)) {
                relateAssocSourceWithOWLClassExpressionTarget(EntityTypes.CONCEPT, assocManager.getDomain(), source,
                        domain, null);
            }

            // The idea is to create a new association called "range", whose
            // LHS will be the OWLObjectProperty and RHS will be the range.
            for (OWLClassExpression range : prop.getRanges(ontology)) {
                relateAssocSourceWithOWLClassExpressionTarget(EntityTypes.CONCEPT, assocManager.getRange(), source,
                        range, null);
            }

            // //////////////////////////////////////////////////
            // /// Process Property Hierarchy/Relationships /////
            // /////////////////////////////////////////////////

            // Step 1: process subPropertyOf: here also we create
            // an association between associations.

            for (OWLObjectPropertyExpression superProp : prop.getSuperProperties(ontology)) {
                relateAssocSourceWithRDFResourceTarget(EntityTypes.ASSOCIATION, assocManager.getSubPropertyOf(),
                        source, superProp);
            }

            // Step 2: process inverseProperties
            if (prop.getInverseProperty() != null) {
                relateAssocSourceWithRDFResourceTarget(EntityTypes.ASSOCIATION, assocManager.getInverseOf(), source,
                        prop.getInverseProperty());
            }

            // Step 3: process equivalentProperties
            for (OWLObjectPropertyExpression equivalent : prop.getEquivalentProperties(ontology)) {
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

            for (OWLClassExpression domain : prop.getDomains(ontology)) {
                relateAssocSourceWithOWLClassExpressionTarget(EntityTypes.CONCEPT, assocManager.getDomain(), source,
                        domain, null);
            }

            for (OWLDataRange range : prop.getRanges(ontology)) {
                AssociationData data = CreateUtils.createAssociationTextData(renderer.render(range));
                relateAssociationSourceData(assocManager.getDatatype(), source, data);
            }

            // //////////////////////////////////////////////////
            // /// Process Property Hierarchy/Relationships /////
            // /////////////////////////////////////////////////
            for (OWLDataPropertyExpression superProp : prop.getSuperProperties(ontology)) {
                relateAssocSourceWithRDFResourceTarget(EntityTypes.ASSOCIATION, assocManager.getSubPropertyOf(),
                        source, superProp);
            }

            for (OWLDataPropertyExpression equivalent : prop.getEquivalentProperties(ontology)) {
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
        concept.setIsDefined(owlClass.isDefined(ontology));

        // Resolve all the concept properties and add to entities.
        resolveEntityProperties(concept, owlClass);
        addEntity(concept);

        // Remember the rdf to code mapping and return.
        owlClassName2Conceptcode_.put(owlClass.getIRI().toString(), concept.getEntityCode());

        return concept;
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
        // be centrally linked to the top node for subclass traversal?
        OWLClass thing = reasoner.getTopClassNode().getEntities().iterator().next();
        if (owlClass.isTopEntity() || reasoner.getSuperClasses(owlClass, true).getFlattened().contains(thing)) {
            // always give the root node the default namespace
            AssociationTarget target = CreateUtils.createAssociationTarget(OwlApi2LGConstants.ROOT_CODE,
                    getDefaultNameSpace());
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
        reasonedSubClasses.addAll(reasoner.getSuperClasses(owlClass, true).getFlattened());
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
//        for (OWLClassExpression disjointClassExpression : owlClass.getDisjointClasses(ontology)) {
//            relateAssocSourceWithOWLClassExpressionTarget(EntityTypes.CONCEPT, assocManager.getDisjointWith(), source,
//                    disjointClassExpression);
//        }
        for (OWLDisjointClassesAxiom disjointClassAxiom : ontology.getDisjointClassesAxioms(owlClass)) {
            for (OWLClassExpression disjointClassExpression : disjointClassAxiom.getClassExpressionsMinus(owlClass)) {
                relateAssocSourceWithOWLClassExpressionTarget(EntityTypes.CONCEPT, assocManager.getEquivalentClass(),
                        source, disjointClassExpression, disjointClassAxiom);
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
                
                    opData = CreateUtils.createAssociationTextData(renderer.render(fillerProp));

                }
                if (restriction instanceof OWLCardinalityRestriction) {
                    OWLCardinalityRestriction rest = (OWLCardinalityRestriction) restriction;
                    OWLPropertyRange fillerProp = rest.getFiller();
                    opData = CreateUtils.createAssociationTextData("" + rest.getCardinality());

                }
                if (restriction instanceof OWLHasValueRestriction) {
                    OWLHasValueRestriction rest = (OWLHasValueRestriction) restriction;
                    OWLObject fillerProp = rest.getValue();
                    if (fillerProp instanceof OWLClass) {
                        targetCode = resolveConceptID((OWLClass) fillerProp);
                        targetNameSpace = getNameSpace((OWLClass) fillerProp);
                    } else if (fillerProp instanceof OWLNamedIndividual) {
                        targetCode = resolveInstanceID((OWLNamedIndividual) fillerProp);
                        targetNameSpace = getNameSpace((OWLClass) fillerProp);
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

    /**
     * Defines EMF complementOf relations based on OWL source.
     * 
     * 
     * protected void resolveComplementOfRelations(AssociationSource source,
     * OWLClass rdfsNamedClass) { if (rdfsNamedClass.getComplementNNF()
     * instanceof OWLComplementClass) {
     * relateAssocSourceWithRDFResourceTarget(EntityTypes.CONCEPT,
     * assocManager.getComplementOf(), source, rdfsNamedClass); } }
     */

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

    /**
     * Defines EMF differentFrom relations based on OWL source.
     * 
     */
    protected void resolveDifferentFromRelations(AssociationSource source, OWLNamedIndividual individual) {
        for (OWLIndividual different : individual.getDifferentIndividuals(ontology)) {
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
        for (OWLIndividual same : individual.getSameIndividuals(ontology)) {
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
            } else if (lgLabel != null && lgLabel.matches(prefManager.getMatchPattern_conceptStatus())) {
                lgEntity.setStatus(resolvedText);
                if (resolvedText.matches(prefManager.getMatchPattern_inactiveStatus()))
                    lgEntity.setIsActive(false);
            }
            // Otherwise instantiate a new EMF property and add the new
            // property to the list to eventually add to the concept.
            else {
                Property newProp = resolveProp(annotationAxiom, propClass, generatePropertyID(++i), lgLabel, lgDType,
                        getNameSpace(annotationAxiom.getProperty()), resolvedText, null);
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

        // Handle imbedded XML if present ...
        Map<String, String> xmlTagsAndVals = resolveXMLTagsAndValues(rdfText);

        if (xmlTagsAndVals.keySet().size() > 0 && prefManager.isProcessComplexProperties()) {
            processComplexXMLPropertyValue(lgProp, lgClass, lgID, lgLabel, lgDType, rdfNamespace, rdfText,
                    xmlTagsAndVals);

        } else {
            // No XML; interpret text as complete property text.
            lgProp.setValue(CreateUtils.createText(rdfText));
        }
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
        for (OWLAnnotation annotation : prop.getAnnotations()) {
            String annotationName = getLocalName(annotation.getProperty());
            String annotationValue = "";
            OWLAnnotationValue value = annotation.getValue();
            if (value instanceof OWLLiteral) {
                OWLLiteral literal = (OWLLiteral) value;
                annotationValue = literal.getLiteral();
            }
            if (StringUtils.isNotBlank(annotationName) && StringUtils.isNotBlank(annotationValue)) {
                lgProp.addPropertyQualifier(CreateUtils.createPropertyQualifier(annotationName, annotationValue,
                        lgSupportedMappings_));

                // Register the qualifier as supported if not already
                // defined.
                lgSupportedMappings_.registerSupportedPropertyQualifier(annotationName,
                        getNameSpace(annotation.getProperty()), annotationName, false);
            }
        }

    }

    private void processComplexXMLPropertyValue(Property lgProp, String lgClass, String lgID, String lgLabel,
            String lgDType, String rdfNamespace, String rdfText, Map<String, String> xmlTagsAndVals) {
        // Designated tags may act as property text or source;
        // all other will be treated as property qualifiers.
        for (String tag : xmlTagsAndVals.keySet()) {

            if (tag == null) {
                messages_.info("Skipping " + lgID + ", " + lgLabel + ", " + lgDType + ", " + rdfNamespace + ", "
                        + rdfText);
                continue;
            }

            String text = xmlTagsAndVals.get(tag);

            if (tag.matches(prefManager.getMatchPattern_xmlTextNames())) {
                lgProp.setValue(CreateUtils.createText(text));
            } else if (tag.matches(prefManager.getMatchPattern_xmlSourceNames())) {
                lgProp.addSource(CreateUtils.createSource(text, null, null, lgSupportedMappings_));

                // Register the source as supported if not already
                // defined.
                lgSupportedMappings_.registerSupportedSource(text, rdfNamespace + text, text, null, false);
            } else if (tag.matches("language")) {
                lgProp.setLanguage(text);

                // Register the source as supported if not already
                // defined.
                lgSupportedMappings_.registerSupportedLanguage(text, OwlApi2LGConstants.LANG_URI + ':' + text, text,
                        false);
            }
            // specific to the new complex props implementation
            else if (prefManager.isComplexProps_isDbxRefSource()
                    && text.matches(OwlApi2LGConstants.MATCH_XMLSOURCE_VALUES)) {
                String val = text;
                String ref = null;
                String[] sourceWithRef = text.split("(:)");
                if (sourceWithRef.length == 2) {
                    val = sourceWithRef[0];
                    ref = sourceWithRef[1];
                }
                lgProp.addSource(CreateUtils.createSource(val, null, ref, lgSupportedMappings_));

                // Register the source as supported if not already
                // defined.
                lgSupportedMappings_.registerSupportedSource(text, rdfNamespace + text, text, null, false);

            } else if (lgProp instanceof Presentation && tag.matches(prefManager.getMatchPattern_xmlRepFormNames())) {
                ((Presentation) lgProp).setRepresentationalForm(text);

                // Register the source as supported if not already
                // defined.
                lgSupportedMappings_.registerSupportedRepresentationalForm(text, rdfNamespace + text, text, false);
            }
            // specific to the new complex props implementation
            else if (prefManager.isComplexProps_isDbxRefRepForm() && lgProp instanceof Presentation
                    && text.matches(OwlApi2LGConstants.MATCH_XMLREPFORM_VALUES)) {
                ((Presentation) lgProp).setRepresentationalForm(text);

                // Register the source as supported if not already
                // defined.
                lgSupportedMappings_.registerSupportedRepresentationalForm(text, rdfNamespace + text, text, false);
            } else {
                lgProp.addPropertyQualifier(CreateUtils.createPropertyQualifier(tag, text, lgSupportedMappings_));

                // Register the qualifier as supported if not already
                // defined.
                lgSupportedMappings_.registerSupportedPropertyQualifier(tag, rdfNamespace + tag, tag, false);
            }
        }
    }

    /**
     * 
     * This method handles the resolution of owl:Anonymous classes.
     * 
     * @param owlClassExp
     * @return
     */
    protected String resolveAnonymousClass(OWLClassExpression owlClassExp, AssociationSource assocSource) {

        String code = "@" + DigestUtils.md5Hex(owlClassExp.toString());
        String nameSpace = getDefaultNameSpace();
        // Check if this concept has already been processed. We do not want
        // duplicate concepts.
        if (isEntityCodeRegistered(nameSpace, code)) {
            return code;
        }

        Entity lgClass = new Entity();
        lgClass.setEntityType(new String[] { EntityTypes.CONCEPT.toString() });
        lgClass.setEntityCode(code);
        lgClass.setIsAnonymous(Boolean.TRUE);

        lgClass.setEntityCodeNamespace(nameSpace);

        EntityDescription ed = new EntityDescription();
        ed.setContent(renderer.render(owlClassExp));
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
                } else {

                    String lgCode = resolveAnonymousClass(operand, assocSource);
                    String targetNameSpace = getDefaultNameSpace();
                    AssociationTarget opTarget = CreateUtils.createAssociationTarget(lgCode, targetNameSpace);
                    relateAssociationSourceTarget(assocManager.getSubClassOf(), source, opTarget);
                }
            }
        }

        if (owlClassExp instanceof OWLObjectComplementOf) {
            OWLObjectComplementOf complementClass = (OWLObjectComplementOf) owlClassExp;
            String lgCode = resolveAnonymousClass((OWLClass) complementClass.getOperand(), assocSource);
            String targetNameSpace = getDefaultNameSpace();

            AssociationTarget opTarget = CreateUtils.createAssociationTarget(lgCode, targetNameSpace);
            relateAssociationSourceTarget(assocManager.getComplementOf(), source, opTarget);
        }
        if (owlClassExp instanceof OWLRestriction) {
            OWLRestriction restrictionClassExp = (OWLRestriction) owlClassExp;
            processRestriction(restrictionClassExp, assocSource, source);
        }
        if (owlClassExp instanceof OWLObjectOneOf) {
            OWLObjectOneOf oneOfClassExp = (OWLObjectOneOf) owlClassExp;
        }

        // Return the lg class name
        return lgClass.getEntityCode();
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
        ManchesterOWLSyntaxPrefixNameShortFormProvider prov = renderer.getPrefixNameShortFormProvider();
        for (Iterator i = ontology.getImportsDeclarations().iterator(); i.hasNext();) {
            OWLImportsDeclaration decl = (OWLImportsDeclaration) i.next();

            decl.getURI().toString();

        }
        for (Iterator i = prov.getPrefixManager().getPrefixName2PrefixMap().keySet().iterator(); i.hasNext();) {
            String prefixName = (String) i.next();
            String prefix = prov.getPrefixManager().getPrefix(prefixName);
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
     */
    protected void initSchemeMetadata() {
        // Set the ontology version from the versionInfo tag
        String version = "";
        // PrefixManager prefixManager= renderer.getOntologyShortFormProvider();

        IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI();
        String uri = ontologyIRI.toString();
        if (ontology.getOntologyID().getVersionIRI() != null)
            version = ontology.getOntologyID().getVersionIRI().toString();

        if (StringUtils.isBlank(version)) {
            version = getVersionInfo();
        }

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
            EntityDescription ed = new EntityDescription();
            ed.setContent(localName);
            lgScheme_.setEntityDescription(ed);
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

    String getDefaultNameSpace() {
        IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI();
        String localName = renderer.getOntologyShortFormProvider().getShortForm(ontologyIRI);
        return localName;
    }

    protected void initAnnotationProperties() {
        for (OWLAnnotationProperty prop : ontology.getAnnotationPropertiesInSignature()) {
            String propertyName = getLocalName(prop);
            // Correlate first assigned label to the primary ID.
            String label = resolveLabel(prop);
            if (isNoopNamespace(label))
                continue;
            addToSupportedPropertyAndMap(label, propertyName, prop);
            owlDatatypeName2label_.put(propertyName, label);
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
        for (OWLObjectProperty prop : ontology.getObjectPropertiesInSignature()) {
            addAssociation(prop);
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
            // addAnnotationPropertyAssociations(annotationProperty);
        }
        /*
         * for (Iterator individuals = owlModel_.getOWLIndividuals().iterator();
         * individuals.hasNext();) { RDFIndividual individual = (RDFIndividual)
         * individuals.next(); addAnnotationPropertyAssociations(individual); }
         */

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
            resolveAssociationProperty(assocWrap.getAssociationEntity(), objectProp);
            assocWrap.setIsTransitive(objectProp.isTransitive(ontology));
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

    /**
     * Initialize and return the root node for the subclass hierarchy.
     * 
     * @return Concept
     */
    protected Entity initSubtypeRoot() {
        Entity topThing = new Entity();
        topThing.setEntityType(new String[] { EntityTypes.CONCEPT.toString() });
        topThing.setEntityCode(OwlApi2LGConstants.ROOT_CODE);
        topThing.setEntityCodeNamespace(getDefaultNameSpace());
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
     * 
     *         protected boolean isRootNode(RDFSNamedClass rdfsNamedClass) { if
     *         (prefManager.getMatchRootName() != null) { String conceptName =
     *         resolveConceptID(rdfsNamedClass); return
     *         prefManager.getMatchRootName().matcher(conceptName).matches(); }
     *         else { return
     *         rdfsNamedClass.getSuperclasses(false).contains(owlModel_
     *         .getOWLThingClass()); } }
     */
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

        if (property.isFunctional(ontology)) {
            characteristics.add(ManchesterOWLSyntax.FUNCTIONAL.toString());
        }
        if (property.isInverseFunctional(ontology)) {
            characteristics.add(ManchesterOWLSyntax.INVERSE_FUNCTIONAL.toString());
        }
        if (property.isSymmetric(ontology)) {
            characteristics.add(ManchesterOWLSyntax.SYMMETRIC.toString());
        }
        if (property.isTransitive(ontology)) {
            characteristics.add(ManchesterOWLSyntax.TRANSITIVE.toString());
        }
        if (property.isReflexive(ontology)) {
            characteristics.add(ManchesterOWLSyntax.REFLEXIVE.toString());
        }
        if (property.isIrreflexive(ontology)) {
            characteristics.add(ManchesterOWLSyntax.IRREFLEXIVE.toString());
        }
        if (property.isAsymmetric(ontology)) {
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
        if (property.isFunctional(ontology)) {
            characteristics.add(ManchesterOWLSyntax.FUNCTIONAL.toString());
        }
        for (String str : characteristics) {
            Property pro = CreateUtils.createProperty(generatePropertyID(++i), "type", str, lgSupportedMappings_,
                    RDF.type.getURI(), null);
            assocEntity.addProperty(pro);
        }
        addPropertiesToAssociationEntity(assocEntity, property);
    }

    class LabelExtractor extends OWLObjectVisitorExAdapter<String> implements OWLAnnotationObjectVisitorEx<String> {

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
        Set<OWLAnnotation> annotations = entity.getAnnotations(ontology);
        for (OWLAnnotation anno : annotations) {
            String result = anno.accept(le);
            if (result != null) {
                return result;
            }
        }
        return getLocalName(entity);

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
        String rdfLocalName = getLocalName(rdfResource);
        String code = owlClassName2Conceptcode_.get(rdfResource.getIRI().toString());
        if (code != null)
            return code;
        // Updated on 05/28/08: if the concept ID is null,
        // it means that either the concept has not been processed yet
        // OR this is an anonymous concept. So, we need to return null
        // instead of the rdf name.
        return null;
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
        for (OWLClassExpression item : individual.getTypes(ontology)) {
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
        if (str != null && str.lastIndexOf(":") != -1) {
            str = str.substring(str.lastIndexOf(":") + 1);
        }

        if (str != null && str.lastIndexOf("#") != -1) {
            str = str.substring(str.lastIndexOf("#") + 1);
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
        String localNameWithColon = renderer.getPrefixNameShortFormProvider().getShortForm(iri);
        return getFromLastIndexOfColonOrHash(localNameWithColon);
    }
    
    String getLocalName(OWLEntity entity) {
        String localNameWithColon = renderer.getPrefixNameShortFormProvider().getShortForm(entity);
        return getFromLastIndexOfColonOrHash(localNameWithColon);
    }

    protected String getNameSpace(OWLEntity entity) {
        return getNameSpace(entity.getIRI());
    }

    public String getNameSpace(IRI iri) {
        String prefixName = "";
        String iriString = iri.toString();
        String ns = XMLUtils.getNCNamePrefix(iriString);
        Map<String, String> prefix2NamespaceMap = renderer.getPrefixNameShortFormProvider().getPrefixManager()
                .getPrefixName2PrefixMap();
        for (Iterator i$ = prefix2NamespaceMap.keySet().iterator(); i$.hasNext();) {
            String keyName = (String) i$.next();
            String prefix = (String) prefix2NamespaceMap.get(keyName);
            if (ns.equals(prefix)) {
                prefixName = keyName;
                break;
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
                // Exception logged by SQLReadWrite
                return;
            }
        }
        registeredNameSpaceCode_.add(bindNamespaceAndCode(lgEntity.getEntityCodeNamespace(), lgEntity.getEntityCode()));
        if (lgEntity instanceof Entity)
            conceptCount_++;
    }

    protected void addEntity(AssociationEntity lgEntity) {
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
                // Exception logged by SQLReadWrite
                return;
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
            String namespace = getDefaultNameSpace();
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

            AssociationWrapper wrapper = this.assocManager.getAssociation(aw.getAssociationEntity().getEntityCode());
            databaseServiceManager.getAssociationService().insertAssociationSource(uri, version,
                    wrapper.getRelationsContainerName(), aw.getAssociationPredicate().getAssociationName(), source);

        } catch (Exception e) {
            this.messages_.warn("Error Inserting AssociationSource.", e);
        } finally {
            aw.getAssociationPredicate().removeSource(source);
            if (target != null)
                source.removeTarget(target);
        }
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
            CodingSchemeService service = LexEvsServiceLocator.getInstance().getDatabaseServiceManager()
                    .getCodingSchemeService();
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

        String label = rdfProp.getClassExpressionType().getName();
        if (label.isEmpty()) {
            label = renderer.render(rdfProp);
        }
        AssociationQualification lgQual = CreateUtils.createAssociationQualification(label, null, label,
                lgSupportedMappings_);
        return lgQual;
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
    

} // end of the class
