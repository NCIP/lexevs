
package edu.mayo.informatics.lexgrid.convert.directConversions.protegeOwl;

import java.util.HashMap;
import java.util.Map;

import org.LexGrid.custom.relations.RelationsUtil;
import org.LexGrid.relations.Relations;

import edu.mayo.informatics.lexgrid.convert.Conversions.SupportedMappings;
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
    private Relations lgRelationsContainer_Assoc = null;
    private Relations lgRelationsContainer_Roles = null;
    private SupportedMappings lgSupportedMappings_ = null;
    // owl relation name to lg container for associations
    private Map<String, AssociationWrapper> owlRelName2lgRoles_ = new HashMap<String, AssociationWrapper>();
    // owl relation name to lg container for roles
    private Map<String, AssociationWrapper> owlRelName2lgAssoc_ = new HashMap<String, AssociationWrapper>();

    public AssociationManager(SupportedMappings lgSupportedMappings_, Relations lgRelationsContainer_Assoc,
            Relations lgRelationsContainer_Roles) {
        this.lgSupportedMappings_ = lgSupportedMappings_;
        this.lgRelationsContainer_Assoc = lgRelationsContainer_Assoc;
        this.lgRelationsContainer_Roles = lgRelationsContainer_Roles;
        init();

    }

    protected AssociationWrapper addAssociation(Relations relContainer, AssociationWrapper aw) {
        String container = relContainer.getContainerName();

        if (ProtegeOwl2LGConstants.DC_ASSOCIATIONS.equals(container)) {
            aw.setAssociationPrediate( RelationsUtil.subsume(lgRelationsContainer_Assoc, aw.getAssociationPredicate()));
            owlRelName2lgAssoc_.put(aw.getAssociationEntity().getEntityCode(), aw);
        } else if (ProtegeOwl2LGConstants.DC_ROLES.equals(container)) {
            aw.setAssociationPrediate( RelationsUtil.subsume(lgRelationsContainer_Roles, aw.getAssociationPredicate()) );
            owlRelName2lgRoles_.put(aw.getAssociationEntity().getEntityCode(), aw);
        }
        aw.setRelationsContainerName(relContainer.getContainerName());

        return aw;
    }

    public AssociationWrapper getAllDifferent() {
        return allDifferent;
    }
    
    public Map<String,AssociationWrapper> getAllAssociations() {
        Map<String,AssociationWrapper> returnMap = new HashMap<String,AssociationWrapper>();
        returnMap.putAll(owlRelName2lgAssoc_);
        returnMap.putAll(owlRelName2lgRoles_);
        
        return returnMap;
    }

    protected AssociationWrapper getAssociation(String assocName) {
        AssociationWrapper assoc = null;
        if (owlRelName2lgAssoc_.containsKey(assocName)) {
            assoc = owlRelName2lgAssoc_.get(assocName);
        } else if (owlRelName2lgRoles_.containsKey(assocName)) {
            assoc = owlRelName2lgRoles_.get(assocName);
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
        String code = ProtegeOwl2LGConstants.ASSOC_ALLDIFFERENT;
        aw.setAssociationName(code);
        aw.setEntityCode(code);
        aw.setEntityCodeNamespace(OWLNames.OWL_PREFIX);// owl:AllDifferent
        aw.setForwardName(code);
        aw.setReverseName(code);
        aw.setIsTransitive(Boolean.FALSE);
        aw.setIsNavigable(Boolean.TRUE);
        aw = addAssociation(lgRelationsContainer_Assoc, aw);
        lgSupportedMappings_.registerSupportedAssociation(
                code, 
                ProtegeOwl2LGConstants.ASSOC_ALLDIFFERENT_URI, 
                code,
                code,
                OWLNames.OWL_PREFIX,
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
        String code = ProtegeOwl2LGConstants.ASSOC_COMPLEMENTOF;
        aw.setAssociationName(code);
        aw.setEntityCode(code);
        aw.setEntityCodeNamespace(OWLNames.OWL_PREFIX);// owl:complementOf
        aw.setForwardName(code);
        aw.setReverseName(code);
        aw.setIsTransitive(Boolean.FALSE);
        aw.setIsNavigable(Boolean.TRUE);
        aw = addAssociation(lgRelationsContainer_Assoc, aw);
        lgSupportedMappings_.registerSupportedAssociation(
                code, 
                ProtegeOwl2LGConstants.ASSOC_COMPLEMENTOF_URI, 
                code,
                code,
                OWLNames.OWL_PREFIX,
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
        String code = ProtegeOwl2LGConstants.ASSOC_DATATYPE;
        aw.setAssociationName(code);
        aw.setEntityCode(code);
        aw.setEntityCodeNamespace(RDFSNames.RDFS_PREFIX); // rdfs:domain
        aw.setForwardName(code);
        aw = addAssociation(lgRelationsContainer_Assoc, aw);
        lgSupportedMappings_.registerSupportedAssociation(
                code, 
                ProtegeOwl2LGConstants.ASSOC_DATATYPE_URI, 
                code,
                code,
                RDFSNames.RDFS_PREFIX,
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
        String code = ProtegeOwl2LGConstants.ASSOC_DATATYPEVALUE;
        aw.setAssociationName(code);
        aw.setEntityCode(code);
        aw.setEntityCodeNamespace(RDFSNames.RDFS_PREFIX); // rdfs:domain
        aw.setForwardName(code);
        aw = addAssociation(lgRelationsContainer_Assoc, aw);
        lgSupportedMappings_.registerSupportedAssociation(
                code, 
                ProtegeOwl2LGConstants.ASSOC_DATATYPEVALUE_URI, 
                code,
                code,
                RDFSNames.RDFS_PREFIX,
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
        String code = ProtegeOwl2LGConstants.ASSOC_DIFFERENTFROM;
        aw.setAssociationName(code);
        aw.setEntityCode(code);
        aw.setEntityCodeNamespace(OWLNames.OWL_PREFIX);// owl:differentFrom
        aw.setForwardName(code);
        aw.setReverseName(code);
        aw.setIsTransitive(Boolean.FALSE);
        aw.setIsNavigable(Boolean.TRUE);
        aw = addAssociation(lgRelationsContainer_Assoc, aw);
        lgSupportedMappings_.registerSupportedAssociation(
                code, 
                ProtegeOwl2LGConstants.ASSOC_DIFFERENTFROM_URI, 
                code,
                code,
                OWLNames.OWL_PREFIX,
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
        String code = ProtegeOwl2LGConstants.ASSOC_DISJOINTWITH;
        aw.setAssociationName(code);
        aw.setEntityCode(code);
        aw.setEntityCodeNamespace(OWLNames.OWL_PREFIX);// owl:disjointWith
        aw.setForwardName(code);
        aw.setReverseName(code);
        aw.setIsTransitive(Boolean.FALSE);
        aw.setIsNavigable(Boolean.TRUE);
        aw = addAssociation(lgRelationsContainer_Assoc, aw);
        lgSupportedMappings_.registerSupportedAssociation(
                code, 
                ProtegeOwl2LGConstants.ASSOC_DISJOINTWITH_URI, 
                code,
                code,
                OWLNames.OWL_PREFIX,
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
        String code = ProtegeOwl2LGConstants.ASSOC_DOMAIN;
        aw.setAssociationName(code);
        aw.setEntityCode(code);
        aw.setEntityCodeNamespace(RDFSNames.RDFS_PREFIX); // rdfs:domain
        aw.setForwardName(ProtegeOwl2LGConstants.ASSOC_DOMAIN_FWD);
        aw.setIsTransitive(Boolean.FALSE);
        aw.setIsNavigable(Boolean.TRUE);
        aw = addAssociation(lgRelationsContainer_Assoc, aw);
        lgSupportedMappings_.registerSupportedAssociation(
                code, 
                ProtegeOwl2LGConstants.ASSOC_DOMAIN_URI, 
                code, 
                code,
                RDFSNames.RDFS_PREFIX,
                false);
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
        String code = ProtegeOwl2LGConstants.ASSOC_EQUIVALENTCLASS;
        aw.setAssociationName(code);
        aw.setEntityCode(code);
        aw.setEntityCodeNamespace(OWLNames.OWL_PREFIX); // owl:equivalentClass
        aw.setForwardName(code);
        aw.setReverseName(code);
        aw.setIsTransitive(Boolean.TRUE);
        aw.setIsNavigable(Boolean.TRUE);
        aw = addAssociation(lgRelationsContainer_Assoc, aw);
        lgSupportedMappings_.registerSupportedAssociation(
                code, 
                ProtegeOwl2LGConstants.ASSOC_EQUIVALENTCLASS_URI,
                code, 
                code,
                OWLNames.OWL_PREFIX,
                false);
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
        String code = ProtegeOwl2LGConstants.ASSOC_EQUIVALENTPROPERTY;
        aw.setAssociationName(code);
        aw.setEntityCode(code);
        aw.setEntityCodeNamespace(OWLNames.OWL_PREFIX);// owl:equivalentProperty
        aw.setForwardName(code);
        aw.setReverseName(code);
        aw.setIsTransitive(Boolean.TRUE);
        aw.setIsNavigable(Boolean.TRUE);
        aw = addAssociation(lgRelationsContainer_Assoc, aw);
        lgSupportedMappings_.registerSupportedAssociation(
                code, 
                ProtegeOwl2LGConstants.ASSOC_EQUIVALENTPROPERTY_URI,
                code, 
                code,
                OWLNames.OWL_PREFIX,
                false);
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
        String code = ProtegeOwl2LGConstants.ASSOC_TYPE;
        aw.setAssociationName(code);
        aw.setEntityCode(code);
        aw.setEntityCodeNamespace(RDFNames.RDF_PREFIX); // rdf:type
        aw.setForwardName(code);
        aw = addAssociation(lgRelationsContainer_Assoc, aw);
        lgSupportedMappings_.registerSupportedAssociation(
                code, 
                ProtegeOwl2LGConstants.ASSOC_TYPE_URI, 
                code,
                code,
                RDFNames.RDF_PREFIX,
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
        String code = ProtegeOwl2LGConstants.ASSOC_INVERSEOF;
        aw.setAssociationName(code);
        aw.setEntityCode(code);
        aw.setEntityCodeNamespace(OWLNames.OWL_PREFIX);// owl:inverseOf
        aw.setForwardName(code);
        aw.setReverseName(code);
        aw = addAssociation(lgRelationsContainer_Assoc, aw);
        lgSupportedMappings_.registerSupportedAssociation(
                code, 
                ProtegeOwl2LGConstants.ASSOC_INVERSEOF_URI, 
                code,
                code,
                OWLNames.OWL_PREFIX,
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
        String code = ProtegeOwl2LGConstants.ASSOC_RANGE;
        aw.setAssociationName(code);
        aw.setEntityCode(code);
        aw.setEntityCodeNamespace(RDFSNames.RDFS_PREFIX);
        aw.setForwardName(ProtegeOwl2LGConstants.ASSOC_RANGE_FWD);
        aw.setIsTransitive(Boolean.FALSE);
        aw.setIsNavigable(Boolean.TRUE);
        aw = addAssociation(lgRelationsContainer_Assoc, aw);
        lgSupportedMappings_.registerSupportedAssociation(
                code, 
                ProtegeOwl2LGConstants.ASSOC_RANGE_URI, 
                code, 
                code,
                RDFSNames.RDFS_PREFIX,
                false);
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
        String code = ProtegeOwl2LGConstants.ASSOC_SAMEAS;
        aw.setAssociationName(code);
        aw.setEntityCode(code);
        aw.setEntityCodeNamespace(OWLNames.OWL_PREFIX);// owl:sameAs
        aw.setForwardName(code);
        aw.setReverseName(code);
        aw.setIsTransitive(Boolean.TRUE);
        aw.setIsNavigable(Boolean.TRUE);
        aw = addAssociation(lgRelationsContainer_Assoc, aw);
        lgSupportedMappings_.registerSupportedAssociation(
                code, 
                ProtegeOwl2LGConstants.ASSOC_SAMEAS_URI, 
                code, 
                code,
                OWLNames.OWL_PREFIX,
                false);
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
        String code = ProtegeOwl2LGConstants.ASSOC_SUBCLASSOF;
        aw.setAssociationName(code);
        aw.setEntityCode(code);
        aw.setEntityCodeNamespace(RDFSNames.RDFS_PREFIX); // rdfs:subClassOf
        aw.setForwardName(code);
        aw.setIsTransitive(Boolean.TRUE);
        aw.setIsNavigable(Boolean.TRUE);
        aw = addAssociation(lgRelationsContainer_Roles, aw);
        lgSupportedMappings_.registerSupportedAssociation(
                code, 
                ProtegeOwl2LGConstants.ASSOC_SUBCLASSOF_URI, 
                code,
                code,
                RDFSNames.RDFS_PREFIX,
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
        String code = ProtegeOwl2LGConstants.ASSOC_SUBPROPERTYOF;
        aw.setAssociationName(code);
        aw.setEntityCode(code);
        aw.setEntityCodeNamespace(RDFSNames.RDFS_PREFIX);// rdfs:subPropertyOf
        aw.setForwardName(code);
        aw.setIsTransitive(Boolean.TRUE);
        aw = addAssociation(lgRelationsContainer_Roles, aw);
        lgSupportedMappings_.registerSupportedAssociation(
                code, 
                ProtegeOwl2LGConstants.ASSOC_SUBPROPERTYOF_URI, 
                code,
                code,
                RDFSNames.RDFS_PREFIX,
                false);
        return aw;
    }

}