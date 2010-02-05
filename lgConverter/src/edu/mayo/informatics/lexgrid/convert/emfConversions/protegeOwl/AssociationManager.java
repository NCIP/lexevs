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

import java.util.HashMap;
import java.util.Map;

import org.LexGrid.emf.relations.Association;
import org.LexGrid.emf.relations.Relations;
import org.LexGrid.emf.relations.RelationsFactory;
import org.LexGrid.emf.relations.util.RelationsUtil;

import edu.mayo.informatics.lexgrid.convert.emfConversions.EMFSupportedMappings;
import edu.stanford.smi.protegex.owl.model.OWLNames;
import edu.stanford.smi.protegex.owl.model.RDFNames;
import edu.stanford.smi.protegex.owl.model.RDFSNames;

public class AssociationManager {
    private Association equivalentClass = null;
    private Association equivalentProperty = null;
    private Association subClassOf = null;
    private Association subPropertyOf = null;
    private Association disjointWith = null;
    private Association complementOf = null;
    private Association inverseOf = null;
    private Association instanceOf = null;
    private Association sameAs = null;
    private Association differentFrom = null;
    private Association allDifferent = null;
    private Association domain = null;
    private Association range = null;
    private Association datatype = null;
    private Association datatypeValue = null;
    private Relations emfRelationsContainer_Assoc = null;
    private Relations emfRelationsContainer_Roles = null;
    private EMFSupportedMappings emfSupportedMappings_ = null;
    // owl relation name to emf container for associations
    private Map<String, Association> owlRelName2emfRoles_ = new HashMap<String, Association>();
    // owl relation name to emf container for roles
    private Map<String, Association> owlRelName2emfAssoc_ = new HashMap<String, Association>();

    public AssociationManager(EMFSupportedMappings emfSupportedMappings_, Relations emfRelationsContainer_Assoc,
            Relations emfRelationsContainer_Roles) {
        this.emfSupportedMappings_ = emfSupportedMappings_;
        this.emfRelationsContainer_Assoc = emfRelationsContainer_Assoc;
        this.emfRelationsContainer_Roles = emfRelationsContainer_Roles;
        init();

    }

    protected Association addAssociation(Relations relContainer, Association assoc) {
        String container = relContainer.getContainerName();

        if (ProtegeOwl2EMFConstants.DC_ASSOCIATIONS.equals(container)) {
            assoc = RelationsUtil.subsume(emfRelationsContainer_Assoc, assoc);
            owlRelName2emfAssoc_.put(assoc.getEntityCode(), assoc);
        } else if (ProtegeOwl2EMFConstants.DC_ROLES.equals(container)) {
            assoc = RelationsUtil.subsume(emfRelationsContainer_Roles, assoc);
            owlRelName2emfRoles_.put(assoc.getEntityCode(), assoc);
        }

        return assoc;
    }

    public Association getAllDifferent() {
        return allDifferent;
    }

    protected Association getAssociation(String assocName) {
        Association assoc = null;
        if (owlRelName2emfAssoc_.containsKey(assocName)) {
            assoc = (Association) owlRelName2emfAssoc_.get(assocName);
        } else if (owlRelName2emfRoles_.containsKey(assocName)) {
            assoc = (Association) owlRelName2emfRoles_.get(assocName);
        }
        return assoc;
    }

    public Association getComplementOf() {
        return complementOf;
    }

    public Association getDatatype() {
        return datatype;
    }

    public Association getDatatypeValue() {
        return datatypeValue;
    }

    public Association getDifferentFrom() {
        return differentFrom;
    }

    public Association getDisjointWith() {
        return disjointWith;
    }

    public Association getDomain() {
        return domain;
    }

    public Association getEquivalentClass() {
        return equivalentClass;
    }

    public Association getEquivalentProperty() {
        return equivalentProperty;
    }

    public Association getInstanceOf() {
        return instanceOf;
    }

    public Association getInverseOf() {
        return inverseOf;
    }

    public Association getRange() {
        return range;
    }

    public Association getSameAs() {
        return sameAs;
    }

    public Association getSubClassOf() {
        return subClassOf;
    }

    public Association getSubPropertyOf() {
        return subPropertyOf;
    }

    void init() {
        initOWLSpecificAssociations();
        domain = initDomainAssociation();
        range = initRangeAssociation();
        datatype = initDatatypeAssociation();
        datatypeValue = initDatatypeValueAssociation();
    }

    /**
     * Init the AllDifferent association
     * 
     * @return Association
     */
    protected Association initAllDifferentAssociation() {
        // Build the association definition ...
        Association assoc = RelationsFactory.eINSTANCE.createAssociation();
        String code = ProtegeOwl2EMFConstants.ASSOC_ALLDIFFERENT;
        assoc.setAssociationName(code);
        assoc.setEntityCode(code);
        assoc.setEntityCodeNamespace(OWLNames.OWL_PREFIX);// owl:AllDifferent
        assoc.setForwardName(code);
        assoc.setReverseName(code);
        assoc.setIsTransitive(Boolean.FALSE);
        assoc.setIsSymmetric(Boolean.TRUE);
        assoc.setIsNavigable(Boolean.TRUE);
        assoc.setIsFunctional(Boolean.FALSE);
        assoc = addAssociation(emfRelationsContainer_Assoc, assoc);
        emfSupportedMappings_.registerSupportedAssociation(code, ProtegeOwl2EMFConstants.ASSOC_ALLDIFFERENT_URI, code,
                false);
        return assoc;
    }

    /**
     * Initialize complementOf association.
     * 
     * @return Association
     */
    protected Association initComplementOfAssociation() {
        // Build the association definition ...
        Association assoc = RelationsFactory.eINSTANCE.createAssociation();
        String code = ProtegeOwl2EMFConstants.ASSOC_COMPLEMENTOF;
        assoc.setAssociationName(code);
        assoc.setEntityCode(code);
        assoc.setEntityCodeNamespace(OWLNames.OWL_PREFIX);// owl:complementOf
        assoc.setForwardName(code);
        assoc.setReverseName(code);
        assoc.setIsTransitive(Boolean.FALSE);
        assoc.setIsSymmetric(Boolean.TRUE);
        assoc.setIsNavigable(Boolean.TRUE);
        assoc.setIsFunctional(Boolean.FALSE);
        assoc = addAssociation(emfRelationsContainer_Assoc, assoc);
        emfSupportedMappings_.registerSupportedAssociation(code, ProtegeOwl2EMFConstants.ASSOC_COMPLEMENTOF_URI, code,
                false);
        return assoc;
    }

    /**
     * Initialize rdfs:datatype association.
     * 
     * @return
     */
    protected Association initDatatypeAssociation() {
        Association assoc = RelationsFactory.eINSTANCE.createAssociation();
        String code = ProtegeOwl2EMFConstants.ASSOC_DATATYPE;
        assoc.setAssociationName(code);
        assoc.setEntityCode(code);
        assoc.setEntityCodeNamespace(RDFSNames.RDFS_PREFIX); // rdfs:domain
        assoc.setForwardName(code);
        assoc = addAssociation(emfRelationsContainer_Assoc, assoc);
        emfSupportedMappings_.registerSupportedAssociation(code, ProtegeOwl2EMFConstants.ASSOC_DATATYPE_URI, code,
                false);
        return assoc;
    }

    /**
     * Initialize datatype values.
     * 
     * 
     * @return
     */
    protected Association initDatatypeValueAssociation() {
        Association assoc = RelationsFactory.eINSTANCE.createAssociation();
        String code = ProtegeOwl2EMFConstants.ASSOC_DATATYPEVALUE;
        assoc.setAssociationName(code);
        assoc.setEntityCode(code);
        assoc.setEntityCodeNamespace(RDFSNames.RDFS_PREFIX); // rdfs:domain
        assoc.setForwardName(code);
        assoc = addAssociation(emfRelationsContainer_Assoc, assoc);
        emfSupportedMappings_.registerSupportedAssociation(code, ProtegeOwl2EMFConstants.ASSOC_DATATYPEVALUE_URI, code,
                false);
        return assoc;
    }

    /**
     * Initialize differentFrom association.
     * 
     * @return Association
     */
    protected Association initDifferentFromAssociation() {
        // Build the association definition ...
        Association assoc = RelationsFactory.eINSTANCE.createAssociation();
        String code = ProtegeOwl2EMFConstants.ASSOC_DIFFERENTFROM;
        assoc.setAssociationName(code);
        assoc.setEntityCode(code);
        assoc.setEntityCodeNamespace(OWLNames.OWL_PREFIX);// owl:differentFrom
        assoc.setForwardName(code);
        assoc.setReverseName(code);
        assoc.setIsTransitive(Boolean.FALSE);
        assoc.setIsSymmetric(Boolean.TRUE);
        assoc.setIsNavigable(Boolean.TRUE);
        assoc.setIsFunctional(Boolean.FALSE);
        assoc = addAssociation(emfRelationsContainer_Assoc, assoc);
        emfSupportedMappings_.registerSupportedAssociation(code, ProtegeOwl2EMFConstants.ASSOC_DIFFERENTFROM_URI, code,
                false);
        return assoc;
    }

    /**
     * Initialize disjointWith association.
     * 
     * @return Association
     */
    protected Association initDisjointWithAssociation() {
        // Build the association definition ...
        Association assoc = RelationsFactory.eINSTANCE.createAssociation();
        String code = ProtegeOwl2EMFConstants.ASSOC_DISJOINTWITH;
        assoc.setAssociationName(code);
        assoc.setEntityCode(code);
        assoc.setEntityCodeNamespace(OWLNames.OWL_PREFIX);// owl:disjointWith
        assoc.setForwardName(code);
        assoc.setReverseName(code);
        assoc.setIsTransitive(Boolean.FALSE);
        assoc.setIsSymmetric(Boolean.TRUE);
        assoc.setIsNavigable(Boolean.TRUE);
        assoc.setIsFunctional(Boolean.FALSE);
        assoc = addAssociation(emfRelationsContainer_Assoc, assoc);
        emfSupportedMappings_.registerSupportedAssociation(code, ProtegeOwl2EMFConstants.ASSOC_DISJOINTWITH_URI, code,
                false);
        return assoc;
    }

    /**
     * Intialize the rdfs:domain association.
     * 
     * @return
     */
    protected Association initDomainAssociation() {
        Association assoc = RelationsFactory.eINSTANCE.createAssociation();
        String code = ProtegeOwl2EMFConstants.ASSOC_DOMAIN;
        assoc.setAssociationName(code);
        assoc.setEntityCode(code);
        assoc.setEntityCodeNamespace(RDFSNames.RDFS_PREFIX); // rdfs:domain
        assoc.setForwardName(ProtegeOwl2EMFConstants.ASSOC_DOMAIN_FWD);
        assoc.setIsTransitive(Boolean.FALSE);
        assoc.setIsSymmetric(Boolean.FALSE);
        assoc.setIsReflexive(Boolean.FALSE);
        assoc.setIsNavigable(Boolean.TRUE);
        assoc = addAssociation(emfRelationsContainer_Assoc, assoc);
        emfSupportedMappings_.registerSupportedAssociation(code, ProtegeOwl2EMFConstants.ASSOC_DOMAIN_URI, code, false);
        return assoc;
    }

    /**
     * Initialize equivalentClass association.
     * 
     * @return Association
     */
    protected Association initEquivalentClassAssociation() {
        // Build the association definition ...
        Association assoc = RelationsFactory.eINSTANCE.createAssociation();
        String code = ProtegeOwl2EMFConstants.ASSOC_EQUIVALENTCLASS;
        assoc.setAssociationName(code);
        assoc.setEntityCode(code);
        assoc.setEntityCodeNamespace(OWLNames.OWL_PREFIX); // owl:equivalentClass
        assoc.setForwardName(code);
        assoc.setReverseName(code);
        assoc.setIsTransitive(Boolean.TRUE);
        assoc.setIsSymmetric(Boolean.TRUE);
        assoc.setIsReflexive(Boolean.TRUE);
        assoc.setIsNavigable(Boolean.TRUE);
        assoc.setIsFunctional(Boolean.FALSE);
        assoc = addAssociation(emfRelationsContainer_Assoc, assoc);
        emfSupportedMappings_.registerSupportedAssociation(code, ProtegeOwl2EMFConstants.ASSOC_EQUIVALENTCLASS_URI,
                code, false);
        return assoc;
    }

    /**
     * Initialize equivalentProperty Association.
     * 
     * @return Association
     */
    protected Association initEquivalentPropertyAssociation() {
        // Build the association definition ...
        Association assoc = RelationsFactory.eINSTANCE.createAssociation();
        String code = ProtegeOwl2EMFConstants.ASSOC_EQUIVALENTPROPERTY;
        assoc.setAssociationName(code);
        assoc.setEntityCode(code);
        assoc.setEntityCodeNamespace(OWLNames.OWL_PREFIX);// owl:equivalentProperty
        assoc.setForwardName(code);
        assoc.setReverseName(code);
        assoc.setIsTransitive(Boolean.TRUE);
        assoc.setIsSymmetric(Boolean.TRUE);
        assoc.setIsReflexive(Boolean.TRUE);
        assoc.setIsNavigable(Boolean.TRUE);
        assoc.setIsFunctional(Boolean.FALSE);
        assoc = addAssociation(emfRelationsContainer_Assoc, assoc);
        emfSupportedMappings_.registerSupportedAssociation(code, ProtegeOwl2EMFConstants.ASSOC_EQUIVALENTPROPERTY_URI,
                code, false);
        return assoc;
    }

    /**
     * Initialize the instanceOf association.
     * 
     * @return Association
     */
    protected Association initInstanceAssociation() {
        // Build the association definition ...
        Association assoc = RelationsFactory.eINSTANCE.createAssociation();
        String code = ProtegeOwl2EMFConstants.ASSOC_INSTANCE;
        assoc.setAssociationName(code);
        assoc.setEntityCode(code);
        assoc.setEntityCodeNamespace(RDFNames.RDF_PREFIX); // rdf:type
        assoc.setForwardName(code);
        assoc.setReverseName(ProtegeOwl2EMFConstants.ASSOC_INSTANCE_INV);
        assoc.setIsSymmetric(Boolean.FALSE);
        assoc.setIsFunctional(Boolean.FALSE);
        assoc = addAssociation(emfRelationsContainer_Assoc, assoc);
        emfSupportedMappings_.registerSupportedAssociation(code, ProtegeOwl2EMFConstants.ASSOC_INSTANCEOF_URI, code,
                false);
        return assoc;
    }

    /**
     * Initialize inverseOf association.
     * 
     * @return Association
     */
    protected Association initInverseOfAssociation() {
        // Build the association definition ...
        Association assoc = RelationsFactory.eINSTANCE.createAssociation();
        String code = ProtegeOwl2EMFConstants.ASSOC_INVERSEOF;
        assoc.setAssociationName(code);
        assoc.setEntityCode(code);
        assoc.setEntityCodeNamespace(OWLNames.OWL_PREFIX);// owl:inverseOf
        assoc.setForwardName(code);
        assoc.setReverseName(code);
        assoc.setIsSymmetric(Boolean.TRUE);
        assoc.setIsFunctional(Boolean.FALSE);
        assoc = addAssociation(emfRelationsContainer_Assoc, assoc);
        emfSupportedMappings_.registerSupportedAssociation(code, ProtegeOwl2EMFConstants.ASSOC_INVERSEOF_URI, code,
                false);
        return assoc;
    }

    /**
     * This method simply initializes a set of OWL specific "associations".
     * 
     */
    protected void initOWLSpecificAssociations() {
        // Initialize all the different types of OWL specific associations.
        // These are different from the user defined ObjectProperties.
        subClassOf = initSubClassOfAssociation();
        equivalentClass = initEquivalentClassAssociation();
        equivalentProperty = initEquivalentPropertyAssociation();
        subPropertyOf = initSubPropertyOfAssociation();
        disjointWith = initDisjointWithAssociation();
        complementOf = initComplementOfAssociation();
        inverseOf = initInverseOfAssociation();
        instanceOf = initInstanceAssociation();
        sameAs = initSameAsAssociation();
        differentFrom = initDifferentFromAssociation();
        allDifferent = initAllDifferentAssociation();
    }

    /**
     * Initialize the rdfs:range association.
     * 
     * @return
     * 
     */
    protected Association initRangeAssociation() {
        Association assoc = RelationsFactory.eINSTANCE.createAssociation();
        String code = ProtegeOwl2EMFConstants.ASSOC_RANGE;
        assoc.setAssociationName(code);
        assoc.setEntityCode(code);
        assoc.setEntityCodeNamespace(RDFSNames.RDFS_PREFIX);
        assoc.setForwardName(ProtegeOwl2EMFConstants.ASSOC_RANGE_FWD);
        assoc.setIsTransitive(Boolean.FALSE);
        assoc.setIsSymmetric(Boolean.FALSE);
        assoc.setIsReflexive(Boolean.FALSE);
        assoc.setIsNavigable(Boolean.TRUE);
        assoc = addAssociation(emfRelationsContainer_Assoc, assoc);
        emfSupportedMappings_.registerSupportedAssociation(code, ProtegeOwl2EMFConstants.ASSOC_RANGE_URI, code, false);
        return assoc;
    }

    /**
     * Initialize sameAs association.
     * 
     * @return Association
     */
    protected Association initSameAsAssociation() {
        // Build the association definition ...
        Association assoc = RelationsFactory.eINSTANCE.createAssociation();
        String code = ProtegeOwl2EMFConstants.ASSOC_SAMEAS;
        assoc.setAssociationName(code);
        assoc.setEntityCode(code);
        assoc.setEntityCodeNamespace(OWLNames.OWL_PREFIX);// owl:sameAs
        assoc.setForwardName(code);
        assoc.setReverseName(code);
        assoc.setIsTransitive(Boolean.TRUE);
        assoc.setIsSymmetric(Boolean.TRUE);
        assoc.setIsReflexive(Boolean.TRUE);
        assoc.setIsNavigable(Boolean.TRUE);
        assoc.setIsFunctional(Boolean.FALSE);
        assoc = addAssociation(emfRelationsContainer_Assoc, assoc);
        emfSupportedMappings_.registerSupportedAssociation(code, ProtegeOwl2EMFConstants.ASSOC_SAMEAS_URI, code, false);
        return assoc;
    }

    /**
     * Initialize the subClassOfAssociation
     * 
     * @return Association
     */
    protected Association initSubClassOfAssociation() {
        // Build the association definition ...
        Association assoc = RelationsFactory.eINSTANCE.createAssociation();
        String code = ProtegeOwl2EMFConstants.ASSOC_SUBCLASSOF;
        assoc.setAssociationName(code);
        assoc.setEntityCode(code);
        assoc.setEntityCodeNamespace(RDFSNames.RDFS_PREFIX); // rdfs:subClassOf
        assoc.setForwardName(code);
        assoc.setIsTransitive(Boolean.TRUE);
        assoc.setIsSymmetric(Boolean.FALSE);
        assoc.setIsReflexive(Boolean.TRUE);
        assoc.setIsNavigable(Boolean.TRUE);
        assoc.setIsFunctional(Boolean.FALSE);
        assoc = addAssociation(emfRelationsContainer_Roles, assoc);
        emfSupportedMappings_.registerSupportedAssociation(code, ProtegeOwl2EMFConstants.ASSOC_SUBCLASSOF_URI, code,
                false);
        return assoc;
    }

    /**
     * Initialize hasSubProperty association.
     * 
     * @return Association
     */
    protected Association initSubPropertyOfAssociation() {
        // Build the association definition ...
        Association assoc = RelationsFactory.eINSTANCE.createAssociation();
        String code = ProtegeOwl2EMFConstants.ASSOC_SUBPROPERTYOF;
        assoc.setAssociationName(code);
        assoc.setEntityCode(code);
        assoc.setEntityCodeNamespace(RDFSNames.RDFS_PREFIX);// rdfs:subPropertyOf
        assoc.setForwardName(code);
        assoc.setIsTransitive(Boolean.TRUE);
        assoc.setIsSymmetric(Boolean.FALSE);
        assoc.setIsReflexive(Boolean.TRUE);
        assoc.setIsNavigable(Boolean.TRUE);
        assoc.setIsFunctional(Boolean.FALSE);
        assoc = addAssociation(emfRelationsContainer_Roles, assoc);
        emfSupportedMappings_.registerSupportedAssociation(code, ProtegeOwl2EMFConstants.ASSOC_SUBPROPERTYOF_URI, code,
                false);
        return assoc;
    }

}
