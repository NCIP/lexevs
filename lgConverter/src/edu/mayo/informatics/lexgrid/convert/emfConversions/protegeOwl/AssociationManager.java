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

import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationEntity;
import org.LexGrid.relations.Relations;
import org.LexGrid.emf.relations.util.RelationsUtil;

import edu.mayo.informatics.lexgrid.convert.emfConversions.EMFSupportedMappings;
import edu.stanford.smi.protegex.owl.model.OWLNames;
import edu.stanford.smi.protegex.owl.model.RDFNames;
import edu.stanford.smi.protegex.owl.model.RDFSNames;

public class AssociationManager {
    private AssociationWrapper equivalentClass = null;
    private AssociationWrapper equivalentProperty = null;
    private AssociationWrapper subClassOf = null;
    private AssociationWrapper subPropertyOf = null;
    private AssociationWrapper disjointWith = null;
    private AssociationWrapper complementOf = null;
    private AssociationWrapper inverseOf = null;
    private AssociationWrapper rdfType = null;
    private AssociationWrapper sameAs = null;
    private AssociationWrapper differentFrom = null;
    private AssociationWrapper allDifferent = null;
    private AssociationWrapper domain = null;
    private AssociationWrapper range = null;
    private AssociationWrapper datatype = null;
    private AssociationWrapper datatypeValue = null;
    private Relations emfRelationsContainer_Assoc = null;
    private Relations emfRelationsContainer_Roles = null;
    private EMFSupportedMappings emfSupportedMappings_ = null;
    // owl relation name to emf container for associations
    private Map<String, AssociationWrapper> owlRelName2emfRoles_ = new HashMap<String, AssociationWrapper>();
    // owl relation name to emf container for roles
    private Map<String, AssociationWrapper> owlRelName2emfAssoc_ = new HashMap<String, AssociationWrapper>();

    public AssociationManager(EMFSupportedMappings emfSupportedMappings_, Relations emfRelationsContainer_Assoc,
            Relations emfRelationsContainer_Roles) {
        this.emfSupportedMappings_ = emfSupportedMappings_;
        this.emfRelationsContainer_Assoc = emfRelationsContainer_Assoc;
        this.emfRelationsContainer_Roles = emfRelationsContainer_Roles;
        init();

    }

    protected AssociationWrapper addAssociation(Relations relContainer, AssociationWrapper aw) {
        String container = relContainer.getContainerName();

        if (ProtegeOwl2EMFConstants.DC_ASSOCIATIONS.equals(container)) {
            aw.setAssociationPrediate( RelationsUtil.subsume(emfRelationsContainer_Assoc, aw.getAssociationPredicate()));
            owlRelName2emfAssoc_.put(aw.getAssociationEntity().getEntityCode(), aw);
        } else if (ProtegeOwl2EMFConstants.DC_ROLES.equals(container)) {
            aw.setAssociationPrediate( RelationsUtil.subsume(emfRelationsContainer_Roles, aw.getAssociationPredicate()) );
            owlRelName2emfRoles_.put(aw.getAssociationEntity().getEntityCode(), aw);
        }

        return aw;
    }

    public AssociationWrapper getAllDifferent() {
        return allDifferent;
    }

    protected AssociationWrapper getAssociation(String assocName) {
        AssociationWrapper assoc = null;
        if (owlRelName2emfAssoc_.containsKey(assocName)) {
            assoc = (AssociationWrapper) owlRelName2emfAssoc_.get(assocName);
        } else if (owlRelName2emfRoles_.containsKey(assocName)) {
            assoc = (AssociationWrapper) owlRelName2emfRoles_.get(assocName);
        }
        return assoc;
    }

    public AssociationWrapper getComplementOf() {
        return complementOf;
    }

    public AssociationWrapper getDatatype() {
        return datatype;
    }

    public AssociationWrapper getDatatypeValue() {
        return datatypeValue;
    }

    public AssociationWrapper getDifferentFrom() {
        return differentFrom;
    }

    public AssociationWrapper getDisjointWith() {
        return disjointWith;
    }

    public AssociationWrapper getDomain() {
        return domain;
    }

    public AssociationWrapper getEquivalentClass() {
        return equivalentClass;
    }

    public AssociationWrapper getEquivalentProperty() {
        return equivalentProperty;
    }

    public AssociationWrapper getRdfType() {
        return rdfType;
    }

    public AssociationWrapper getInverseOf() {
        return inverseOf;
    }

    public AssociationWrapper getRange() {
        return range;
    }

    public AssociationWrapper getSameAs() {
        return sameAs;
    }

    public AssociationWrapper getSubClassOf() {
        return subClassOf;
    }

    public AssociationWrapper getSubPropertyOf() {
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
    protected AssociationWrapper initAllDifferentAssociation() {
        // Build the association definition ...
        AssociationWrapper aw = new AssociationWrapper();
        String code = ProtegeOwl2EMFConstants.ASSOC_ALLDIFFERENT;
        aw.setAssociationName(code);
        aw.setEntityCode(code);
        aw.setEntityCodeNamespace(OWLNames.OWL_PREFIX);// owl:AllDifferent
        aw.setForwardName(code);
        aw.setReverseName(code);
        aw.setIsTransitive(Boolean.FALSE);
        aw.setIsSymmetric(Boolean.TRUE);
        aw.setIsNavigable(Boolean.TRUE);
        aw.setIsFunctional(Boolean.FALSE);
        aw = addAssociation(emfRelationsContainer_Assoc, aw);
        emfSupportedMappings_.registerSupportedAssociation(code, ProtegeOwl2EMFConstants.ASSOC_ALLDIFFERENT_URI, code,
                false);
        return aw;
    }

    /**
     * Initialize complementOf association.
     * 
     * @return Association
     */
    protected AssociationWrapper initComplementOfAssociation() {
        // Build the association definition ...
        AssociationWrapper aw = new AssociationWrapper();
        String code = ProtegeOwl2EMFConstants.ASSOC_COMPLEMENTOF;
        aw.setAssociationName(code);
        aw.setEntityCode(code);
        aw.setEntityCodeNamespace(OWLNames.OWL_PREFIX);// owl:complementOf
        aw.setForwardName(code);
        aw.setReverseName(code);
        aw.setIsTransitive(Boolean.FALSE);
        aw.setIsSymmetric(Boolean.TRUE);
        aw.setIsNavigable(Boolean.TRUE);
        aw.setIsFunctional(Boolean.FALSE);
        aw = addAssociation(emfRelationsContainer_Assoc, aw);
        emfSupportedMappings_.registerSupportedAssociation(code, ProtegeOwl2EMFConstants.ASSOC_COMPLEMENTOF_URI, code,
                false);
        return aw;
    }

    /**
     * Initialize rdfs:datatype association.
     * 
     * @return
     */
    protected AssociationWrapper initDatatypeAssociation() {
        AssociationWrapper aw = new AssociationWrapper();
        String code = ProtegeOwl2EMFConstants.ASSOC_DATATYPE;
        aw.setAssociationName(code);
        aw.setEntityCode(code);
        aw.setEntityCodeNamespace(RDFSNames.RDFS_PREFIX); // rdfs:domain
        aw.setForwardName(code);
        aw = addAssociation(emfRelationsContainer_Assoc, aw);
        emfSupportedMappings_.registerSupportedAssociation(code, ProtegeOwl2EMFConstants.ASSOC_DATATYPE_URI, code,
                false);
        return aw;
    }

    /**
     * Initialize datatype values.
     * 
     * 
     * @return
     */
    protected AssociationWrapper initDatatypeValueAssociation() {
        AssociationWrapper aw = new AssociationWrapper();
        String code = ProtegeOwl2EMFConstants.ASSOC_DATATYPEVALUE;
        aw.setAssociationName(code);
        aw.setEntityCode(code);
        aw.setEntityCodeNamespace(RDFSNames.RDFS_PREFIX); // rdfs:domain
        aw.setForwardName(code);
        aw = addAssociation(emfRelationsContainer_Assoc, aw);
        emfSupportedMappings_.registerSupportedAssociation(code, ProtegeOwl2EMFConstants.ASSOC_DATATYPEVALUE_URI, code,
                false);
        return aw;
    }

    /**
     * Initialize differentFrom association.
     * 
     * @return Association
     */
    protected AssociationWrapper initDifferentFromAssociation() {
        // Build the association definition ...
        AssociationWrapper aw = new AssociationWrapper();
        String code = ProtegeOwl2EMFConstants.ASSOC_DIFFERENTFROM;
        aw.setAssociationName(code);
        aw.setEntityCode(code);
        aw.setEntityCodeNamespace(OWLNames.OWL_PREFIX);// owl:differentFrom
        aw.setForwardName(code);
        aw.setReverseName(code);
        aw.setIsTransitive(Boolean.FALSE);
        aw.setIsSymmetric(Boolean.TRUE);
        aw.setIsNavigable(Boolean.TRUE);
        aw.setIsFunctional(Boolean.FALSE);
        aw = addAssociation(emfRelationsContainer_Assoc, aw);
        emfSupportedMappings_.registerSupportedAssociation(code, ProtegeOwl2EMFConstants.ASSOC_DIFFERENTFROM_URI, code,
                false);
        return aw;
    }

    /**
     * Initialize disjointWith association.
     * 
     * @return Association
     */
    protected AssociationWrapper initDisjointWithAssociation() {
        // Build the association definition ...
        AssociationWrapper aw = new AssociationWrapper();
        String code = ProtegeOwl2EMFConstants.ASSOC_DISJOINTWITH;
        aw.setAssociationName(code);
        aw.setEntityCode(code);
        aw.setEntityCodeNamespace(OWLNames.OWL_PREFIX);// owl:disjointWith
        aw.setForwardName(code);
        aw.setReverseName(code);
        aw.setIsTransitive(Boolean.FALSE);
        aw.setIsSymmetric(Boolean.TRUE);
        aw.setIsNavigable(Boolean.TRUE);
        aw.setIsFunctional(Boolean.FALSE);
        aw = addAssociation(emfRelationsContainer_Assoc, aw);
        emfSupportedMappings_.registerSupportedAssociation(code, ProtegeOwl2EMFConstants.ASSOC_DISJOINTWITH_URI, code,
                false);
        return aw;
    }

    /**
     * Intialize the rdfs:domain association.
     * 
     * @return
     */
    protected AssociationWrapper initDomainAssociation() {
        AssociationWrapper aw = new AssociationWrapper();
        String code = ProtegeOwl2EMFConstants.ASSOC_DOMAIN;
        aw.setAssociationName(code);
        aw.setEntityCode(code);
        aw.setEntityCodeNamespace(RDFSNames.RDFS_PREFIX); // rdfs:domain
        aw.setForwardName(ProtegeOwl2EMFConstants.ASSOC_DOMAIN_FWD);
        aw.setIsTransitive(Boolean.FALSE);
        aw.setIsSymmetric(Boolean.FALSE);
        aw.setIsReflexive(Boolean.FALSE);
        aw.setIsNavigable(Boolean.TRUE);
        aw = addAssociation(emfRelationsContainer_Assoc, aw);
        emfSupportedMappings_.registerSupportedAssociation(code, ProtegeOwl2EMFConstants.ASSOC_DOMAIN_URI, code, false);
        return aw;
    }

    /**
     * Initialize equivalentClass association.
     * 
     * @return Association
     */
    protected AssociationWrapper initEquivalentClassAssociation() {
        // Build the association definition ...
        AssociationWrapper aw = new AssociationWrapper();
        String code = ProtegeOwl2EMFConstants.ASSOC_EQUIVALENTCLASS;
        aw.setAssociationName(code);
        aw.setEntityCode(code);
        aw.setEntityCodeNamespace(OWLNames.OWL_PREFIX); // owl:equivalentClass
        aw.setForwardName(code);
        aw.setReverseName(code);
        aw.setIsTransitive(Boolean.TRUE);
        aw.setIsSymmetric(Boolean.TRUE);
        aw.setIsReflexive(Boolean.TRUE);
        aw.setIsNavigable(Boolean.TRUE);
        aw.setIsFunctional(Boolean.FALSE);
        aw = addAssociation(emfRelationsContainer_Assoc, aw);
        emfSupportedMappings_.registerSupportedAssociation(code, ProtegeOwl2EMFConstants.ASSOC_EQUIVALENTCLASS_URI,
                code, false);
        return aw;
    }

    /**
     * Initialize equivalentProperty Association.
     * 
     * @return Association
     */
    protected AssociationWrapper initEquivalentPropertyAssociation() {
        // Build the association definition ...
        AssociationWrapper aw =new AssociationWrapper();
        String code = ProtegeOwl2EMFConstants.ASSOC_EQUIVALENTPROPERTY;
        aw.setAssociationName(code);
        aw.setEntityCode(code);
        aw.setEntityCodeNamespace(OWLNames.OWL_PREFIX);// owl:equivalentProperty
        aw.setForwardName(code);
        aw.setReverseName(code);
        aw.setIsTransitive(Boolean.TRUE);
        aw.setIsSymmetric(Boolean.TRUE);
        aw.setIsReflexive(Boolean.TRUE);
        aw.setIsNavigable(Boolean.TRUE);
        aw.setIsFunctional(Boolean.FALSE);
        aw = addAssociation(emfRelationsContainer_Assoc, aw);
        emfSupportedMappings_.registerSupportedAssociation(code, ProtegeOwl2EMFConstants.ASSOC_EQUIVALENTPROPERTY_URI,
                code, false);
        return aw;
    }

    /**
     * Initialize the instanceOf association.
     * 
     * @return Association
     */
    protected AssociationWrapper initRdfTypeAssociation() {
        // Build the association definition ...
        AssociationWrapper aw = new AssociationWrapper();
        String code = ProtegeOwl2EMFConstants.ASSOC_TYPE;
        aw.setAssociationName(code);
        aw.setEntityCode(code);
        aw.setEntityCodeNamespace(RDFNames.RDF_PREFIX); // rdf:type
        aw.setForwardName(code);
        aw.setIsSymmetric(Boolean.FALSE);
        aw.setIsFunctional(Boolean.FALSE);
        aw = addAssociation(emfRelationsContainer_Assoc, aw);
        emfSupportedMappings_.registerSupportedAssociation(code, ProtegeOwl2EMFConstants.ASSOC_TYPE_URI, code,
                false);
        return aw;
    }

    /**
     * Initialize inverseOf association.
     * 
     * @return Association
     */
    protected AssociationWrapper initInverseOfAssociation() {
        // Build the association definition ...
        AssociationWrapper aw = new AssociationWrapper();
        String code = ProtegeOwl2EMFConstants.ASSOC_INVERSEOF;
        aw.setAssociationName(code);
        aw.setEntityCode(code);
        aw.setEntityCodeNamespace(OWLNames.OWL_PREFIX);// owl:inverseOf
        aw.setForwardName(code);
        aw.setReverseName(code);
        aw.setIsSymmetric(Boolean.TRUE);
        aw.setIsFunctional(Boolean.FALSE);
        aw = addAssociation(emfRelationsContainer_Assoc, aw);
        emfSupportedMappings_.registerSupportedAssociation(code, ProtegeOwl2EMFConstants.ASSOC_INVERSEOF_URI, code,
                false);
        return aw;
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
        rdfType = initRdfTypeAssociation();
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
    protected AssociationWrapper initRangeAssociation() {
        AssociationWrapper aw = new AssociationWrapper();
        String code = ProtegeOwl2EMFConstants.ASSOC_RANGE;
        aw.setAssociationName(code);
        aw.setEntityCode(code);
        aw.setEntityCodeNamespace(RDFSNames.RDFS_PREFIX);
        aw.setForwardName(ProtegeOwl2EMFConstants.ASSOC_RANGE_FWD);
        aw.setIsTransitive(Boolean.FALSE);
        aw.setIsReflexive(Boolean.FALSE);
        aw.setIsNavigable(Boolean.TRUE);
        aw = addAssociation(emfRelationsContainer_Assoc, aw);
        emfSupportedMappings_.registerSupportedAssociation(code, ProtegeOwl2EMFConstants.ASSOC_RANGE_URI, code, false);
        return aw;
    }

    /**
     * Initialize sameAs association.
     * 
     * @return Association
     */
    protected AssociationWrapper initSameAsAssociation() {
        // Build the association definition ...
        AssociationWrapper aw = new AssociationWrapper();
        String code = ProtegeOwl2EMFConstants.ASSOC_SAMEAS;
        aw.setAssociationName(code);
        aw.setEntityCode(code);
        aw.setEntityCodeNamespace(OWLNames.OWL_PREFIX);// owl:sameAs
        aw.setForwardName(code);
        aw.setReverseName(code);
        aw.setIsTransitive(Boolean.TRUE);
        aw.setIsSymmetric(Boolean.TRUE);
        aw.setIsReflexive(Boolean.TRUE);
        aw.setIsNavigable(Boolean.TRUE);
        aw.setIsFunctional(Boolean.FALSE);
        aw = addAssociation(emfRelationsContainer_Assoc, aw);
        emfSupportedMappings_.registerSupportedAssociation(code, ProtegeOwl2EMFConstants.ASSOC_SAMEAS_URI, code, false);
        return aw;
    }

    /**
     * Initialize the subClassOfAssociation
     * 
     * @return Association
     */
    protected AssociationWrapper initSubClassOfAssociation() {
        // Build the association definition ...
        AssociationWrapper aw = new AssociationWrapper();
        String code = ProtegeOwl2EMFConstants.ASSOC_SUBCLASSOF;
        aw.setAssociationName(code);
        aw.setEntityCode(code);
        aw.setEntityCodeNamespace(RDFSNames.RDFS_PREFIX); // rdfs:subClassOf
        aw.setForwardName(code);
        aw.setIsTransitive(Boolean.TRUE);
        aw.setIsSymmetric(Boolean.FALSE);
        aw.setIsReflexive(Boolean.TRUE);
        aw.setIsNavigable(Boolean.TRUE);
        aw.setIsFunctional(Boolean.FALSE);
        aw = addAssociation(emfRelationsContainer_Roles, aw);
        emfSupportedMappings_.registerSupportedAssociation(code, ProtegeOwl2EMFConstants.ASSOC_SUBCLASSOF_URI, code,
                false);
        return aw;
    }

    /**
     * Initialize hasSubProperty association.
     * 
     * @return Association
     */
    protected AssociationWrapper initSubPropertyOfAssociation() {
        // Build the association definition ...
        AssociationWrapper aw = new AssociationWrapper();
        String code = ProtegeOwl2EMFConstants.ASSOC_SUBPROPERTYOF;
        aw.setAssociationName(code);
        aw.setEntityCode(code);
        aw.setEntityCodeNamespace(RDFSNames.RDFS_PREFIX);// rdfs:subPropertyOf
        aw.setForwardName(code);
        aw.setIsTransitive(Boolean.TRUE);
        aw.setIsSymmetric(Boolean.FALSE);
        aw.setIsReflexive(Boolean.TRUE);
        aw.setIsFunctional(Boolean.FALSE);
        aw = addAssociation(emfRelationsContainer_Roles, aw);
        emfSupportedMappings_.registerSupportedAssociation(code, ProtegeOwl2EMFConstants.ASSOC_SUBPROPERTYOF_URI, code,
                false);
        return aw;
    }

}
