
package org.LexGrid.LexBIG.Impl.helpers.graph;

import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Query.Filter;
import org.LexGrid.LexBIG.Impl.CodedNodeSetImpl;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;
import org.LexGrid.LexBIG.Impl.dataAccess.SQLImplementedMethods;
import org.LexGrid.LexBIG.Impl.helpers.AdditiveCodeHolder;
import org.LexGrid.LexBIG.Impl.helpers.CodeToReturn;
import org.LexGrid.LexBIG.Impl.helpers.DefaultCodeHolder;
import org.LexGrid.LexBIG.Impl.helpers.KnownConceptReference;
import org.LexGrid.LexBIG.Impl.helpers.comparator.ResultComparator;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.annotations.LgClientSideSafe;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.concepts.Entity;
import org.apache.commons.lang.StringUtils;
import org.lexevs.exceptions.MissingResourceException;
import org.lexevs.exceptions.UnexpectedInternalError;
import org.lexevs.system.ResourceManager;

/**
 * Class to help me get from the SQL database to the graph object.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class GHolder {
    // Track nodes and edges being maintained by the graph
    private Hashtable<String, GAssociationInfo> allAssociations_ = null;
    private Hashtable<String, GNode> allNodes_ = null;

    // Relation root
    private GNode focusNode_ = null;
    private ConceptReference focusConceptReference_ = null;

    // Identifies the code system for resolved info
    private String internalCodingSchemeName_ = null;
    private String internalVersionString_ = null;

    // Indicates direction and nature of resolution
    private boolean resolveForward_ = false;
    private boolean resolveReverse_ = false;
    private boolean resultsSkipped_ = false;

    /**
     * Constructs a new container to hold concept graph representations.
     * 
     * @param internalCodingSchemeName
     *            The coding scheme name for associated nodes; not null.
     * @param internalVersionString
     *            The coding scheme version for associated nodes; not null.
     * @param focusNode
     *            The starting point for resolved relations; null for absolute
     *            root.
     * @param resolveForward
     *            Whether relations are to be resolved in the forward direction.
     * @param resolveReverse
     *            Whether relations are to be resolved in the reverse direction.
     */
    public GHolder(String internalCodingSchemeName, String internalVersionString, ConceptReference focusNode,
            boolean resolveForward, boolean resolveReverse) {
        internalCodingSchemeName_ = internalCodingSchemeName;
        internalVersionString_ = internalVersionString;
        focusConceptReference_ = focusNode;
        resolveForward_ = resolveForward;
        resolveReverse_ = resolveReverse;

        allNodes_ = new Hashtable<String, GNode>();
        allAssociations_ = new Hashtable<String, GAssociationInfo>();

        if (focusConceptReference_ != null)
            focusConceptReference_.setCodingSchemeName(StringUtils.defaultIfEmpty(focusConceptReference_
                    .getCodingSchemeName(), internalCodingSchemeName));
    }

    /**
     * Add an edge to the graph.
     * <p>
     * If the association is new, return a reference to the concept defined by
     * the association endpoint based on specified direction (if forward, this
     * is the target concept; if reverse, it is the source concept). If the
     * association is already defined, returns null.
     * 
     * @param sourceCodeSystem
     * @param sourceCodeNamespace
     * @param sourceCode
     * @param sourceCodeTypes
     * @param relationName
     * @param associationName
     * @param targetCodeSystem
     * @param targetCodeNamespace
     * @param targetCode
     * @param targetCodeTypes
     * @param qualifiers
     * @param forward
     * @param internalCodeSystemName
     * @param internalVersionString
     * @return org.LexGrid.LexBIG.DataModel.Core.ConceptReference
     * @throws MissingResourceException
     * @throws UnexpectedInternalError
     * @throws LBParameterException
     */
    @LgClientSideSafe
    public ConceptReference addAssociation(String sourceCodeSystem, String sourceCodeNamespace, String sourceCode,
            String[] sourceCodeTypes, String sourceCodeEntityDescription, 
            String relationName, String associationName, String targetCodeSystem,
            String targetCodeNamespace, String targetCode, String[] targetCodeTypes, 
            String targetCodeEntityDescription, 
            NameAndValueList qualifiers,
            boolean forward, String internalCodeSystemName, String internalVersionString)
            throws MissingResourceException, UnexpectedInternalError, LBParameterException {

        // Get pre-resolved node for source and target, or resolve now ...
        GNode source = getNode(sourceCodeSystem, sourceCodeNamespace, sourceCode, sourceCodeTypes,
                sourceCodeEntityDescription, forward, !forward,
                internalCodeSystemName, internalVersionString,
                associationName);
        GNode target = getNode(targetCodeSystem, targetCodeNamespace, targetCode, targetCodeTypes,
                targetCodeEntityDescription, !forward, forward,
                internalCodeSystemName, internalVersionString,
                associationName);

        // What I am doing here is marking the concept codes that have already
        // been resolved -
        // so that I don't end up chasing down an infinite loop on the DB side.
        // I only return a ConceptReference to the *new* code that was found if
        // it hasn't
        // already been queried before.
        if (forward)
            source.setNodeHasBeenPrinted(true);
        else
            target.setNodeHasBeenPrinted(true);

        // If focus reference is set but the associated node is not yet
        // resolved, check to
        // see if we have now found it. If so, remember it.
        if (focusConceptReference_ != null && focusNode_ == null) {
            if (focusConceptReference_.getConceptCode().equals(sourceCode)
                    && focusConceptReference_.getCodingSchemeName().equals(sourceCodeSystem))
                focusNode_ = source;
            else if (focusConceptReference_.getConceptCode().equals(targetCode)
                    && focusConceptReference_.getCodingSchemeName().equals(targetCodeSystem))
                focusNode_ = target;
        }

        // Get existing association info, or create now, then remember for start
        // and endpoint...
        GAssociationInfo gai = getAssociationInfo(internalCodeSystemName, internalVersionString, associationName,
                relationName);
        GAssociation targetAssociations = source.getTargetAssociation(gai);
        if (!targetAssociations.hasChild(target))
            targetAssociations.addChild(target, qualifiers);

        // Associations may not be reverse-navigable; check and only remember if
        // appropriate...
        GAssociation sourceAssociations = target.getSourceAssociation(gai);
        Boolean navigable = sourceAssociations.getAssociationInfo().getIsNavigable();
        if (navigable != null && navigable.booleanValue() && !sourceAssociations.hasChild(target))
            sourceAssociations.addChild(source, qualifiers);

        // Return any new node serving as endpoint based on the indicated
        // direction
        if (forward && !target.isNodeHasBeenPrinted())
            return new KnownConceptReference(target.getCodeSystem(), target.getCode(), target.getCodeNamespace());
        if (!forward && !source.isNodeHasBeenPrinted())
            return new KnownConceptReference(source.getCodeSystem(), source.getCode(), source.getCodeNamespace());
        return null;
    }

    /**
     * Add an edge to the graph.
     * <p>
     * If the association is new, return a reference to the concept defined by
     * the association endpoint based on specified direction (if forward, this
     * is the target concept; if reverse, it is the source concept). If the
     * association is already defined, returns null.
     * 
     * @param sourceCodeSystem
     * @param sourceCodeNamespace
     * @param sourceCode
     * @param sourceCodeTypes
     * @param relationName
     * @param associationName
     * @param targetCodeSystem
     * @param targetCodeNamespace
     * @param targetCode
     * @param targetCodeTypes
     * @param qualifiers
     * @param forward
     * @param internalCodeSystemName
     * @param internalVersionString
     * @return org.LexGrid.LexBIG.DataModel.Core.ConceptReference
     * @throws MissingResourceException
     * @throws UnexpectedInternalError
     * @throws LBParameterException
     */
    @LgClientSideSafe
    public void addAssociationInfo(String sourceCodeSystem, String sourceCodeNamespace, String sourceCode,
            String[] sourceCodeTypes, String sourceCodeEntityDescription, 
            String relationName, String associationName, String targetCodeSystem,
            String targetCodeNamespace, String targetCode, String[] targetCodeTypes, 
            String targetCodeEntityDescription, NameAndValueList qualifiers,
            boolean forward, String internalCodeSystemName, String internalVersionString)
            throws MissingResourceException, UnexpectedInternalError, LBParameterException {

        // Get pre-resolved node for source and target, or resolve now ...
        GNode source = getNode(sourceCodeSystem, sourceCodeNamespace, sourceCode, sourceCodeTypes,
                sourceCodeEntityDescription, forward, !forward,
                internalCodeSystemName, internalVersionString,
                associationName);
        GNode target = getNode(targetCodeSystem, targetCodeNamespace, targetCode, targetCodeTypes,
                targetCodeEntityDescription, !forward, forward,
                internalCodeSystemName, internalVersionString,
                associationName);

        // Get existing association info, or create now, then remember for start
        // and endpoint...
        GAssociationInfo gai = getAssociationInfo(internalCodeSystemName, internalVersionString, associationName,
                relationName);
        GAssociation targetAssociations = source.getTargetAssociation(gai);
        targetAssociations.setUnaddedChildrenPresent(true);

        // Associations may not be reverse-navigable; check and only remember if
        // appropriate...
        GAssociation sourceAssociations = target.getSourceAssociation(gai);
        Boolean navigable = sourceAssociations.getAssociationInfo().getIsNavigable();
        if (navigable != null && navigable.booleanValue() && !sourceAssociations.hasChild(target))
            sourceAssociations.setUnaddedChildrenPresent(true);
    }

    /**
     * Copy child nodes from the old parent/association to the new.
     * 
     * @param oldParent
     * @param oldAssoc
     * @param newParent
     * @param newAssoc
     */
    protected void addAssociationChildren(GNode oldParent, GAssociation oldAssoc, GNode newParent, GAssociation newAssoc) {
        // If unadded children are indicated in the old assoc,
        // preserve in the new association.
        newAssoc.setUnaddedChildrenPresent(
            newAssoc.isUnaddedChildrenPresent() || oldAssoc.isUnaddedChildrenPresent());
        
        // Copy added children from old to new ...
        for (Iterator<GNode> nodeIterator = oldAssoc.getChildren().iterator(); nodeIterator.hasNext();) {
            GNode currentNode = (GNode) nodeIterator.next();

            // if not already defined as a child, copy it (but not the
            // sub-nodes).
            boolean hasChild = newAssoc.hasChild(currentNode);
            if (!hasChild) {
                // created this node yet?
                GNode child = allNodes_.get(currentNode.getKey());
                if (child == null) {
                    // nope, create it
                    child = new GNode(currentNode);
                    addNode(child);
                }
                // add it now to the association
                newAssoc.addChild(child, oldAssoc.getQualifier(currentNode));
            } else {
                // child was already known for this association; make sure all
                // qualifiers are carried over
                newAssoc.addQualifiers(newAssoc.getChild(currentNode), oldAssoc.getQualifier(currentNode));
            }
        }
    }

    /**
     * Basic addition of a node to those tracked by the graph.
     * 
     * @param node
     */
    protected void addNode(GNode node) {
        allNodes_.put(node.getKey(), node);
    }

    /**
     * Returns a graph node based on the given information; a new node is added
     * to the graph if necessary.
     * 
     * @param codeSystem
     * @param codeNamespace
     * @param code
     * @param codeTypes
     * @param internalCodeSystemName
     * @param internalVersion
     * @param forward
     * @return A GNode based on the provided info; not null.
     * @throws MissingResourceException
     * @throws UnexpectedInternalError
     */
    @LgClientSideSafe
    public ConceptReference addNode(String codeSystem, String codeNamespace, String code, String[] codeTypes,
            String entityDescription, String internalCodeSystemName, String internalVersion, boolean forward)
            throws MissingResourceException, UnexpectedInternalError {
        getNode(codeSystem, codeNamespace, code, codeTypes, entityDescription,
                forward, !forward,
                internalCodeSystemName, internalVersion,
                null);
        return new KnownConceptReference(codeSystem, code, codeNamespace);
    }

    /**
     * Create and return the list of associations for the given node, resolved
     * to the specified depth and modified according to parameter values. The
     * method will recurse as necessary to achieve the requested depth.
     * 
     * @param node
     *            Focus node.
     * @param forward
     *            Indicates whether the we are resolving source or target
     *            relations for the node.
     * @param currentDepth
     *            Tracks depth for purposes of recursion.
     * @param resolveCodedEntryDepth
     *            Depth in the graph to resolve coded entries. -1 means don't
     *            resolve anything - just return the concept references, 0 means
     *            resolve just the root nodes, 1 means resolve 1 deep, etc.
     * @param resolveAssociationDepth
     *            Number of hops to resolve associations. 0 means leave all
     *            associations unresolved, 1 means immediate neighbors, etc. -1
     *            means follow the entire closure of the graph.
     * @param propertyNames
     *            Local names of properties to resolve. If not empty and not
     *            null, only properties matching the given names are included
     *            for resolved concepts.
     * @param propertyTypes
     *            Indicates whether to resolve only specific property
     *            categories, regardless of the assigned name. Any of the
     *            enumerated PropertyType values can be specified. If not empty
     *            and not null, only properties matching the given types are
     *            included for resolved concepts.
     * @param sortOptions
     *            List of sort options to apply during resolution. If supplied,
     *            the sort algorithms will be applied in the order provided. Any
     *            algorithms not valid to be applied in context of node set
     *            iteration, as specified in the sort extension description,
     *            will result in a parameter exception. Available algorithms can
     *            be retrieved through the LexBIGService getSortExtensions()
     *            method after being defined to the LexBIGServiceManager
     *            extension registry.
     * @param filters
     * @return org.LexGrid.LexBIG.DataModel.Collections.AssociationList
     * @throws LBParameterException
     * @throws UnexpectedInternalError
     * @throws MissingResourceException
     */
    protected AssociationList buildAssociationList(GNode node, boolean forward, int currentDepth,
            int resolveCodedEntryDepth, int resolveAssociationDepth, LocalNameList restrictToProperties,
            PropertyType[] restrictToPropertyTypes, SortOptionList sortBy, Filter[] filters,
            boolean keepLastAssociationLevelUnresolved) throws LBParameterException, UnexpectedInternalError,
            MissingResourceException {

        // Most of the time, the depth will already be properly limited here.
        // However, in certain cases, if
        // they ask for depth 0, or a depth 1 and items are linked forward and
        // backward, I will have
        // more than that. Don't add them.
        if (resolveAssociationDepth >= 0 && currentDepth > resolveAssociationDepth)
            return null;

        AssociationList al = new AssociationList();
        ResourceManager rm = ResourceManager.instance();

        // Fetch the next level of associations
        Enumeration<GAssociation> GAssociations;
        GAssociations = forward ? node.getTargetAssociations(sortBy) : node.getSourceAssociations(sortBy);

        // Process each ...
        while (GAssociations.hasMoreElements()) {
            GAssociation gCurrentAssociation = (GAssociation) GAssociations.nextElement();

            // Assign identifying name and directional label
            Association assn = new Association();
            assn.setAssociationName(gCurrentAssociation.getAssociationInfo().getName());
            assn.setDirectionalName(forward ? gCurrentAssociation.getAssociationInfo().getForwardName()
                    : gCurrentAssociation.getAssociationInfo().getReverseName());

            // If the association is grounded in a concept, remember it
            assn.setAssociationReference(gCurrentAssociation.getAssociationInfo().getAssociationReference());

            // Create a holder for associated concepts, then process children
            // in order according to sort criteria...
            assn.setAssociatedConcepts(new AssociatedConceptList());
            for (Iterator<GNode> iter = gCurrentAssociation.getChildren(sortBy).iterator(); iter.hasNext();) {
                GNode gCurrentChild = (GNode) iter.next();

                // If not already 'printed', mark it here to avoid recursion
                // later
                boolean nodePreviouslyPrinted = gCurrentChild.isNodeHasBeenPrinted();
                gCurrentChild.setNodeHasBeenPrinted(true);

                // Create a concept to track to the association
                AssociatedConcept ac = new AssociatedConcept();

                // Transfer available qualifiers to the associated concept
                NameAndValueList qualifiers = gCurrentAssociation.getQualifier(gCurrentChild);
                if (qualifiers != null && qualifiers.getNameAndValueCount() > 0) {
                    NameAndValueList quals = new NameAndValueList();
                    for (int i = 0; i < qualifiers.getNameAndValueCount(); i++) {
                        NameAndValue nv = new NameAndValue();
                        nv.setName(qualifiers.getNameAndValue(i).getName());
                        nv.setContent(qualifiers.getNameAndValue(i).getContent());
                        quals.addNameAndValue(nv);
                    }
                    ac.setAssociationQualifiers(quals);
                }

                // Assign basic identifying information to the node reference ...
                ac.setCodingSchemeName(rm.getExternalCodingSchemeNameForUserCodingSchemeNameOrId(gCurrentChild
                        .getDefiningCodeSystemURN(), gCurrentChild.getDefiningCodeSystemVersion()));
                ac.setCodingSchemeURI(gCurrentChild.getDefiningCodeSystemURN());
                ac.setCodingSchemeVersion(gCurrentChild.getDefiningCodeSystemVersion());
                ac.setCode(gCurrentChild.getCode());
                ac.setCodeNamespace(gCurrentChild.getCodeNamespace());
                ac.setIsNavigable(gCurrentAssociation.getAssociationInfo().getIsNavigable());

                String version = gCurrentChild.getDefiningCodeSystemVersion();
                boolean detailsUnavailable = false;
                if (version == null || version.length() == 0) {
                    // try to get a version
                    try {
                        version = ResourceManager.instance().getInternalVersionStringForTag(
                                gCurrentChild.getDefiningCodeSystemURN(), null);
                    } catch (LBParameterException e) {
                        // no version of this code system is available, we won't
                        // be able to get further details.
                        detailsUnavailable = true;
                    }
                }

                // if there is no version, we won't be able to resolve anything.
                if (currentDepth <= resolveCodedEntryDepth && !detailsUnavailable) {
                    Entity ent =
                        SQLImplementedMethods.buildCodedEntry(
                            rm.getInternalCodingSchemeNameForUserCodingSchemeName(
                                gCurrentChild.getDefiningCodeSystemURN(), version),
                            version,
                            gCurrentChild.getCode(),
                            gCurrentChild.getCodeNamespace(),
                            restrictToProperties, restrictToPropertyTypes);
                    ac.setEntityDescription(ent.getEntityDescription());
                    ac.setEntity(ent);

                } else if (!detailsUnavailable) {
                    ac.setEntity(null);         
                    EntityDescription ed = new EntityDescription();
                    ed.setContent(gCurrentChild.getEntityDescription());
                    ac.setEntityDescription(ed);       
                } else {
                    ac.setEntity(null);
                    ac.setEntityDescription(null);
                }

                // Honor any filters that may have been applied
                boolean add = true;
                if (filters != null && filters.length > 0) {
                    for (int j = 0; j < filters.length; j++) {
                        if (!filters[j].match(ac)) {
                            add = false;
                            break;
                        }
                    }
                }
                // If filtered or already rendered, ignore the node.
                if (add) {
                    if (!nodePreviouslyPrinted) {
                        if (forward) {
                            ac.setSourceOf(buildAssociationList(gCurrentChild, forward, currentDepth + 1,
                                    resolveCodedEntryDepth, resolveAssociationDepth, restrictToProperties,
                                    restrictToPropertyTypes, sortBy, filters, keepLastAssociationLevelUnresolved));
                        } else {
                            ac.setTargetOf(buildAssociationList(gCurrentChild, forward, currentDepth + 1,
                                    resolveCodedEntryDepth, resolveAssociationDepth, restrictToProperties,
                                    restrictToPropertyTypes, sortBy, filters, keepLastAssociationLevelUnresolved));
                        }
                    }
                    assn.getAssociatedConcepts().addAssociatedConcept(ac);
                }
            }
            if (assn.getAssociatedConcepts().getAssociatedConceptCount() > 0
                    || (keepLastAssociationLevelUnresolved && gCurrentAssociation.isUnaddedChildrenPresent())) {
                al.addAssociation(assn);
            }
        }
        if (al.getAssociationCount() == 0) {
            return null;
        }
        return al;
    }

    /*
     * returns null if a filter rejects the item
     */
    protected ResolvedConceptReference buildResolvedConceptReference(GNode currentNode, int resolveCodedEntryDepth,
            int resolveAssociationDepth, LocalNameList restrictToProperties, PropertyType[] restrictToPropertyTypes,
            SortOptionList sortByProperty, Filter[] filters, boolean keepLastAssociationLevelUnresolved)
            throws UnexpectedInternalError, MissingResourceException, LBParameterException {
        
        // Assign basic identifying information to the node reference ...
        ResourceManager rm = ResourceManager.instance();
        ResolvedConceptReference rcr = new ResolvedConceptReference();
        rcr.setCodingSchemeName(
            rm.getExternalCodingSchemeNameForUserCodingSchemeNameOrId(
                currentNode.getDefiningCodeSystemURN(),
                currentNode.getDefiningCodeSystemVersion()));
        rcr.setCodingSchemeURI(currentNode.getDefiningCodeSystemURN());
        rcr.setCodingSchemeVersion(currentNode.getDefiningCodeSystemVersion());
        rcr.setCode(currentNode.getCode());
        rcr.setCodeNamespace(currentNode.getCodeNamespace());
        rcr.setEntityType(currentNode.getCodeTypes());
        try {
            if (resolveCodedEntryDepth >= 0) {
                Entity ent = SQLImplementedMethods.buildCodedEntry(
                    rm.getInternalCodingSchemeNameForUserCodingSchemeName(
                        currentNode.getDefiningCodeSystemURN(),
                        currentNode.getDefiningCodeSystemVersion()),
                        currentNode.getDefiningCodeSystemVersion(),
                        currentNode.getCode(),
                        currentNode.getCodeNamespace(),
                        restrictToProperties,
                        restrictToPropertyTypes);
                rcr.setEntityDescription(ent.getEntityDescription());
                rcr.setEntity(ent);
            } else {
                rcr.setEntity(null);
               
                EntityDescription entityDescription = new EntityDescription();
                entityDescription.setContent(currentNode.getEntityDescription());
                rcr.setEntityDescription(entityDescription);   
            }
        } catch (Exception e) {
            // if there was a problem resolving the code (reference to a code
            // system that isn't available, for
            // example - don't fail, just return what is available.
            rcr.setEntity(null);
            rcr.setEntityDescription(null);
        }

        currentNode.setNodeHasBeenPrinted(true);

        boolean add = true;
        if (filters != null && filters.length > 0) {
            for (int j = 0; j < filters.length; j++) {
                if (!filters[j].match(rcr)) {
                    add = false;
                    break;
                }
            }
        }
        if (add) {
            rcr.setSourceOf(buildAssociationList(currentNode, true, 1, resolveCodedEntryDepth, resolveAssociationDepth,
                    restrictToProperties, restrictToPropertyTypes, sortByProperty, filters,
                    keepLastAssociationLevelUnresolved));

            rcr.setTargetOf(buildAssociationList(currentNode, false, 1, resolveCodedEntryDepth,
                    resolveAssociationDepth, restrictToProperties, restrictToPropertyTypes, sortByProperty, filters,
                    keepLastAssociationLevelUnresolved));
            return rcr;
        } else {
            // filter rejected this item
            return null;
        }
    }

    /**
     * Returns a shared structure containing identifying information for the
     * given association; a new information object is automatically created if
     * necessary.
     * 
     * @param internalCodeSystemName
     * @param internalVersionString
     * @param associationName
     * @param relationName
     * @throws LBParameterException
     * @throws UnexpectedInternalError
     * @throws MissingResourceException
     */
    protected GAssociationInfo getAssociationInfo(String internalCodeSystemName, String internalVersionString,
            String associationName, String relationName) throws LBParameterException, UnexpectedInternalError,
            MissingResourceException {
        GAssociationInfo gai = allAssociations_.get(GAssociationInfo.getKey(internalCodeSystemName, associationName));
        if (gai == null) {
            gai = SQLImplementedMethods.getAssociationInfo(internalCodeSystemName, internalVersionString,
                    associationName, relationName);
            String assocKey = gai.getKey();
            allAssociations_.put(assocKey, gai);
        }
        return gai;
    }

    /**
     * Returns all nodes maintained by the graph that are target but not source
     * of any relationship. If none exists, then return nodes that are a target
     * but not a source of any relationship in which it participates, sorted
     * according to the given options.
     * 
     * @param sortBy
     */
    protected Iterator<GNode> getChildlessNodes(SortOptionList sortBy) {
        Set<GNode> childless = new HashSet<GNode>();
        for (Iterator<GNode> gNodes = allNodes_.values().iterator(); gNodes.hasNext();) {
            GNode gNode = gNodes.next();
            if (gNode.isChildless()) {
                childless.add(gNode);
            }
        }

        if (childless.isEmpty()) {
            for (Iterator<GNode> gNodes = allNodes_.values().iterator(); gNodes.hasNext();) {
                GNode gNode = gNodes.next();
                for (Enumeration<GAssociation> gAssociations = gNode.getSourceAssociations(); gAssociations
                        .hasMoreElements();) {
                    GAssociation gAssociation = gAssociations.nextElement();
                    if (gNode.getTargetAssociation(gAssociation.getAssociationInfo()).getChildCount() == 0) {
                        childless.add(gNode);
                        break;
                    }
                }
            }
        }
        return getSortedNodeIterator(sortBy, childless);
    }

    /**
     * Locates and returns a graph node based on the given information; a new
     * node is added to the graph if necessary.
     * 
     * @param codeSystem
     * @param codeNamespace
     * @param code
     * @param codeTypes
     * @param flagParentlessIfNew
     * @param flagChildlessIfNew
     * @param internalCodeSystemName
     * @param internalVersionString
     * @param associationName
     * @return A GNode based on the provided info; not null.
     * @throws MissingResourceException
     * @throws UnexpectedInternalError
     */
    protected GNode getNode(String codeSystem, String codeNamespace, 
            String code, String[] codeTypes, String entityDescription, 
            boolean flagParentlessIfNew, boolean flagChildlessIfNew, String internalCodeSystemName,
            String internalVersionString, String associationName)
            throws MissingResourceException, UnexpectedInternalError {
        GNode node = allNodes_.get(GNode.getKey(codeSystem, codeNamespace, code));
        if (node == null)
            addNode(node =
                new GNode(codeSystem, codeNamespace, code, codeTypes,
                    entityDescription, internalCodeSystemName, internalVersionString));
        return node;
    }

    /**
     * Returns the total number of nodes tracked by the graph.
     * 
     * @return int
     */
    @LgClientSideSafe
    public int getNodeCount() {
        return allNodes_.size();
    }

    @LgClientSideSafe
    public CodedNodeSet getNodeList() throws LBInvocationException {
        AdditiveCodeHolder ch = new DefaultCodeHolder();

        Enumeration<GNode> allNodes = allNodes_.elements();

        while (allNodes.hasMoreElements()) {
            GNode current = allNodes.nextElement();

            ch.add(
               new CodeToReturn(
                   current.getCode(),
                   current.getEntityDescription(),
                   current.getDefiningCodeSystemURN(),
                   current.getDefiningCodeSystemVersion(),
                   0,
                   current.getCodeNamespace(),
                   current.getCodeTypes()));
        }

        return new CodedNodeSetImpl(ch, internalCodingSchemeName_, internalVersionString_);
    }

    /**
     * Returns all nodes maintained by the graph that are source but not target
     * of any relationship. If none exists, return nodes that are a source not
     * not a target for at least one relationship in which it participates,
     * sorted according to the given options.
     * 
     * @param sortBy
     */
    protected Iterator<GNode> getParentlessNodes(SortOptionList sortBy) {
        Set<GNode> parentless = new HashSet<GNode>();
        for (Iterator<GNode> gNodes = allNodes_.values().iterator(); gNodes.hasNext();) {
            GNode gNode = gNodes.next();
            if (gNode.getIncomingLinkCount() == 0) {
                parentless.add(gNode);
            }
        }
        if (parentless.isEmpty()) {
            for (Iterator<GNode> gNodes = allNodes_.values().iterator(); gNodes.hasNext();) {
                GNode gNode = gNodes.next();
                for (Enumeration<GAssociation> gAssociations = gNode.getTargetAssociations(); gAssociations
                        .hasMoreElements();) {
                    GAssociation gAssociation = gAssociations.nextElement();
                    if (gNode.getSourceAssociation(gAssociation.getAssociationInfo()).getChildCount() == 0) {
                        parentless.add(gNode);
                        break;
                    }
                }
            }

        }
        return getSortedNodeIterator(sortBy, parentless);
    }

    /**
     * Returns the fully resolved contents of the graph.
     * 
     * @param resolveCodedEntryDepth
     * @param resolveAssociationDepth
     * @param restrictToProperties
     * @param restrictToPropertyTypes
     * @param sortByProperty
     * @param filterOptions
     * @throws MissingResourceException
     * @throws UnexpectedInternalError
     * @throws LBParameterException
     */
    @LgClientSideSafe
    public ResolvedConceptReferenceList getResolvedConceptReferenceList(int resolveCodedEntryDepth,
            int resolveAssociationDepth, LocalNameList restrictToProperties, PropertyType[] restrictToPropertyTypes,
            SortOptionList sortByProperty, LocalNameList filterOptions, boolean keepLastAssociationLevelUnresolved)
            throws MissingResourceException, UnexpectedInternalError, LBParameterException {
        resetNodePrintedFlags();
        ResolvedConceptReferenceList list = new ResolvedConceptReferenceList();
        if (isResultsSkipped()) {
            list.setIncomplete(new Boolean("true"));
        }

        Filter[] filters = validateFilters(filterOptions);

        if (focusNode_ == null) {
            // no focus node. If they asked for forward, our top node will be
            // all of the nodes that
            // aren't linked by anything else. If they asked for reverse, the
            // start nodes will be
            // the child nodes ( no outgoing links). Asking for forward and
            // reverse nodes, but having
            // a null focusNode_ is impossible.

            Iterator<GNode> nodes = (resolveForward_ ? getParentlessNodes(sortByProperty)
                    : getChildlessNodes(sortByProperty));
            while (nodes.hasNext()) {
                GNode currentNode = nodes.next();
                listHelper(list, currentNode, resolveCodedEntryDepth, resolveAssociationDepth, restrictToProperties,
                        restrictToPropertyTypes, sortByProperty, filters, keepLastAssociationLevelUnresolved);
            }
        } else {
            // have a focus, start there.
            GNode currentNode = focusNode_;
            listHelper(list, currentNode, resolveCodedEntryDepth, resolveAssociationDepth, restrictToProperties,
                    restrictToPropertyTypes, sortByProperty, filters, keepLastAssociationLevelUnresolved);
        }
        
        //if a focus ConceptReference is provided, but no associations are found, that
        //focus ConceptReference is a graph all by itself. Resolve the focus ConceptReference and return it.
        if(list.getResolvedConceptReferenceCount() == 0 && focusConceptReference_ != null){
            ResolvedConceptReference rcr = SQLImplementedMethods.resolveConceptReference(focusConceptReference_, internalVersionString_);
            if (rcr != null) {
                list.addResolvedConceptReference(rcr);
            }
        }
        return list;
    }

    /**
     * Returns an iterator that will sort the given nodes according to the
     * specified sort options.
     * 
     * @param sortBy
     * @param nodes
     */
    protected Iterator<GNode> getSortedNodeIterator(SortOptionList sortBy, Set<GNode> nodes) {
        GNode[] temp = nodes.toArray(new GNode[nodes.size()]);
        if(ResultComparator.isSortOptionListValid(sortBy)){
            ResultComparator<GNode> compare = new ResultComparator<GNode>(sortBy, GNode.class);
            Arrays.sort(temp, compare);
        } 

        return Arrays.asList(temp).iterator();
    }

    /**
     * Performs a boolean intersection of this graph holder with another. Upon
     * completion, only nodes common to both are preserved.
     * 
     * @param holder
     */
    @LgClientSideSafe
    public void intersection(GHolder holder) {
        // need to remove all nodes from my graph that do not occur in the other
        // graph. remove associations
        // as necessary
         
        Enumeration<String> myKeys = allNodes_.keys();
        while (myKeys.hasMoreElements()) {
            String myCurrentKey = (String) myKeys.nextElement();
           
            // does the other graph have this node?
            GNode other = holder.allNodes_.get(myCurrentKey);

            // If not, remove it altogether. Otherwise, maintain
            // only the associations common to both graphs.
            if (other == null)
                removeNode(myCurrentKey);
            else {
                GNode myNode = allNodes_.get(myCurrentKey);
                myNode.intersectLinks(other);
                
                if (myNode.isChildless() && myNode.getIncomingLinkCount() == 0)
                    allNodes_.remove(myCurrentKey);
            }
        }
    }

    /**
     * @return the resultsSkipped
     */
    protected boolean isResultsSkipped() {
        return this.resultsSkipped_;
    }

    protected void listHelper(ResolvedConceptReferenceList list, GNode currentNode, int resolveCodedEntryDepth,
            int resolveAssociationDepth, LocalNameList restrictToProperties, PropertyType[] restrictToPropertyTypes,
            SortOptionList sortByProperty, Filter[] filters, boolean keepLastAssociationLevelUnresolved)
            throws UnexpectedInternalError, MissingResourceException, LBParameterException {
        if (currentNode != null) {
            ResolvedConceptReference rcr = buildResolvedConceptReference(currentNode, resolveCodedEntryDepth,
                    resolveAssociationDepth, restrictToProperties, restrictToPropertyTypes, sortByProperty, filters,
                    keepLastAssociationLevelUnresolved);
            // rcr will be null if the result didn't pass the filter.
            if (rcr != null) {
                list.addResolvedConceptReference(rcr);
            }
        }
    }

    /**
     * Removes the node identified by the given key from those tracked by the
     * graph.
     * 
     * @param nodeKey
     */
    protected void removeNode(String nodeKey) {
        GNode nodeToRemove = allNodes_.get(nodeKey);

        // remove it from the root node (if it is there)
        if (focusNode_ != null && focusNode_.equals(nodeToRemove)) {
            focusNode_ = null;
        }

        // need to find out which nodes point to this node - and remove those
        // links.
        Enumeration<GAssociation> sources = nodeToRemove.getSourceAssociations();
        while (sources.hasMoreElements()) {
            GAssociation currentAssociation = sources.nextElement();
            Collection<GNode> nodesThatPointToIt = currentAssociation.getChildren();
            for (Iterator<GNode> iter = nodesThatPointToIt.iterator(); iter.hasNext();) {
                GNode currentNode = (GNode) iter.next();
                currentNode.removeLinkTo(nodeToRemove, currentAssociation);
            }
        }

        // Ok, now nobody points to this node. Need to also remove any nodes
        // that are only reachable
        // from this node.
        Enumeration<GAssociation> targets = nodeToRemove.getTargetAssociations();
        while (targets.hasMoreElements()) {
            GAssociation currentAssociation = targets.nextElement();
            Collection<GNode> nodesItPointsTo = currentAssociation.getChildren();
            for (Iterator<GNode> iter = nodesItPointsTo.iterator(); iter.hasNext();) {
                GNode currentNode = (GNode) iter.next();
                currentNode.removeLinkFrom(nodeToRemove, currentAssociation);

                int incomingCount = currentNode.getIncomingLinkCount();
                if (incomingCount == 0) {
                    // If nobody points to this node any more, need to remove it
                    // as well.
                    // this is recursive
                    removeNode(currentNode.getKey());
                }
            }
        }

        // Finally, pull it from the maintained list of concepts ...
        allNodes_.remove(nodeKey);
    }

    /**
     * For each node that is tracked, this class maintains an indication of
     * whether or not the node has been included in the results in order to
     * prevent recursion. This method clears all flags.
     */
    protected void resetNodePrintedFlags() {
        Enumeration<GNode> e = allNodes_.elements();
        while (e.hasMoreElements()) {
            GNode node = (GNode) e.nextElement();
            node.setNodeHasBeenPrinted(false);
        }
    }

    /**
     * @param resultsSkipped
     *            the resultsSkipped to set
     */
    @LgClientSideSafe
    public void setResultsSkipped(boolean resultsSkipped) {
        this.resultsSkipped_ = resultsSkipped;
    }

    /**
     * Performs a boolean union of this graph holder with another. Upon
     * completion, nodes for both are tracked.
     * 
     * @param holder
     */
    @LgClientSideSafe
    public void union(GHolder holder) {
        holder.resetNodePrintedFlags();

        for (Iterator<GNode> iter = holder.allNodes_.values().iterator(); iter.hasNext();) {
            // check if this node has already been processed ...
            GNode currentOtherNode = (GNode) iter.next();
            if (currentOtherNode.isNodeHasBeenPrinted()) {
                continue;
            }
            currentOtherNode.setNodeHasBeenPrinted(true);

            // find or add the basic node definition ...
            GNode myNode = allNodes_.get(currentOtherNode.getKey());
            if (myNode == null) {
                // create a new GNode that is almost identical to the otherNode
                // doesn't carry along the relationships.
                myNode = new GNode(currentOtherNode);
                addNode(myNode);
            }

            // now, deal with the relationships for the node being processed
            // sources first; go over all of the associations.
            Enumeration<GAssociation> sourceAssociations = currentOtherNode.getSourceAssociations();
            while (sourceAssociations.hasMoreElements()) {
                GAssociation currentOtherSourceAssociation = sourceAssociations.nextElement();
                GAssociation myAssociation = myNode.getSourceAssociation(currentOtherSourceAssociation
                        .getAssociationInfo());
                addAssociationChildren(currentOtherNode, currentOtherSourceAssociation, myNode, myAssociation);
            }

            // now targets
            Enumeration<GAssociation> targetAssociations = currentOtherNode.getTargetAssociations();
            while (targetAssociations.hasMoreElements()) {
                GAssociation currentOtherTargetAssociation = targetAssociations.nextElement();
                GAssociation myAssociation = myNode.getTargetAssociation(currentOtherTargetAssociation
                        .getAssociationInfo());
                addAssociationChildren(currentOtherNode, currentOtherTargetAssociation, myNode, myAssociation);
            }

            // Merge focus ...
            if (focusNode_ == null && currentOtherNode.equals(holder.focusNode_))
                focusNode_ = myNode;
        }

        // Merge directionality ...
        resolveForward_ = resolveForward_ || holder.resolveForward_;
        resolveReverse_ = resolveReverse_ || holder.resolveReverse_;
        resultsSkipped_ = resultsSkipped_ || holder.resultsSkipped_;

    }

    protected Filter[] validateFilters(LocalNameList filterOptions) throws LBParameterException {
        if (filterOptions != null && filterOptions.getEntryCount() > 0) {
            Filter[] temp = new Filter[filterOptions.getEntryCount()];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = ExtensionRegistryImpl.instance().getFilter(filterOptions.getEntry(i));
            }
            return temp;
        } else {
            return null;
        }
    }

    public String toString() {
        String str = "internalCodingSchemeName_ = " + internalCodingSchemeName_ + "\n" + "internalVersionString_ = "
                + internalVersionString_ + "\n" + "resolveForward_ = " + resolveForward_ + "\n" + "resolveReverse_ = "
                + resolveReverse_ + "\n" + "resultsSkipped_ = " + resultsSkipped_ + "\n" + "focusNode_ = " + focusNode_
                + "\n" + "allAssociations_ = " + allAssociations_ + "\n" + "allNodes_ = " + allNodes_ + "\n"
                + "focusConceptReference_ = " + focusConceptReference_ + "\n";
        return str;

    }

}