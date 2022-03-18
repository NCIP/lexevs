
package org.LexGrid.LexBIG.Impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.Intersection;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.RestrictToAssociations;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.RestrictToCodeSystem;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.RestrictToCodes;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.RestrictToDirectionalNames;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.RestrictToSourceCodeSystem;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.RestrictToSourceCodes;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.RestrictToTargetCodeSystem;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.RestrictToTargetCodes;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.Union;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.interfaces.Operation;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.interfaces.Restriction;
import org.LexGrid.LexBIG.Impl.dataAccess.SQLImplementedMethods;
import org.LexGrid.LexBIG.Impl.helpers.graph.GHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.annotations.LgClientSideSafe;
import org.lexevs.logging.LoggerFactory;
import org.lexevs.system.ResourceManager;

/**
 * This class implements the CodedNodeGraph interface.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 * @author <A HREF="mailto:rokickik@mail.nih.gov">Konrad Rokicki</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class CodedNodeGraphImpl implements CodedNodeGraph, Cloneable {

    private static final long serialVersionUID = -8179477301788162092L;
    protected String internalCodingSchemeName;
    protected String internalVersion;
    protected String[] relationContainerNames_;
    protected ArrayList<Operation> pendingOperations_ = new ArrayList<Operation>();

    protected LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }

    /**
     * This constructor is only here for Apache Axis to work correctly. It
     * should not be used by anyone.
     */
    public CodedNodeGraphImpl() {
        getLogger().logMethod(new Object[] {});
    }

    public CodedNodeGraphImpl(String codingScheme, String version, String relationsName) throws LBParameterException,
            LBInvocationException, LBResourceUnavailableException {
        getLogger().logMethod(new Object[] { codingScheme, version, relationsName });
        try {
            ResourceManager rm = ResourceManager.instance();
            internalCodingSchemeName = rm.getInternalCodingSchemeNameForUserCodingSchemeName(codingScheme, version);
            internalVersion = version;

            // make sure that it is active.
            String urn = rm.getURNForInternalCodingSchemeName(internalCodingSchemeName, version);
            if (!rm.getRegistry().isActive(urn, version)) {
                throw new LBResourceUnavailableException("The requested coding scheme is not currently active");
            }

            if (relationsName == null || relationsName.length() == 0) {
                relationContainerNames_ = SQLImplementedMethods.getNativeRelations(internalCodingSchemeName,
                        internalVersion);
            } else {
                relationContainerNames_ = new String[] { relationsName };
            }
        } catch (LBParameterException e) {
            throw e;
        } catch (LBResourceUnavailableException e) {
            throw e;
        } catch (Exception e) {
            String logId = getLogger().error("Unexpected Error", e);
            throw new LBInvocationException("Unexpected Internal Error", logId);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#union(org.LexGrid.LexBIG
     * .LexBIGService.CodedNodeGraph)
     */
    @LgClientSideSafe
    public CodedNodeGraph union(CodedNodeGraph graph) throws LBInvocationException {
        getLogger().logMethod(new Object[] { graph });
        try {
            pendingOperations_.add(new Union(((CodedNodeGraphImpl) graph).clone()));
            return this;
        } catch (Exception e) {
            String logId = getLogger().error("Unexpected Error", e);
            throw new LBInvocationException("Unexpected Internal Error", logId);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#intersect(org.LexGrid
     * .LexBIG.LexBIGService.CodedNodeGraph)
     */
    @LgClientSideSafe
    public CodedNodeGraph intersect(CodedNodeGraph graph) throws LBInvocationException {
        getLogger().logMethod(new Object[] { graph });
        try {
            pendingOperations_.add(new Intersection(((CodedNodeGraphImpl) graph).clone()));
            return this;
        } catch (Exception e) {
            String logId = getLogger().error("Unexpected Error", e);
            throw new LBInvocationException("Unexpected Internal Error", logId);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#restrictToCodes(org.LexGrid
     * .LexBIG.LexBIGService.CodedNodeSet)
     */
    @LgClientSideSafe
    public CodedNodeGraph restrictToCodes(CodedNodeSet codes) throws LBInvocationException, LBParameterException {
        getLogger().logMethod(new Object[] { codes });
        try {
            pendingOperations_.add(new RestrictToCodes(((CodedNodeSetImpl) codes).clone()));
            return this;
        } catch (Exception e) {
            String logId = getLogger().error("Unexpected Error", e);
            throw new LBInvocationException("Unexpected Internal Error", logId);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#restrictToAssociations
     * (org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList,
     * org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList)
     */
    @LgClientSideSafe
    public CodedNodeGraph restrictToAssociations(NameAndValueList associations, NameAndValueList associationQualifiers)
            throws LBInvocationException {
        getLogger().logMethod(new Object[] { associations, associationQualifiers });
        pendingOperations_.add(new RestrictToAssociations(associations, associationQualifiers));
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#restrictToAssociations
     * (org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList,
     * org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList)
     */
    public CodedNodeGraph restrictToDirectionalNames(NameAndValueList directionalNames,
            NameAndValueList associationQualifiers) {
        getLogger().logMethod(new Object[] { directionalNames, associationQualifiers });
        pendingOperations_.add(new RestrictToDirectionalNames(directionalNames, associationQualifiers));
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#restrictToSourceCodes
     * (org.LexGrid.LexBIG.LexBIGService.CodedNodeSet)
     */
    @LgClientSideSafe
    public CodedNodeGraph restrictToSourceCodes(CodedNodeSet sourceCodes) throws LBInvocationException {
        getLogger().logMethod(new Object[] { sourceCodes });
        try {
            pendingOperations_.add(new RestrictToSourceCodes(((CodedNodeSetImpl) sourceCodes).clone()));
            return this;
        } catch (Exception e) {
            String logId = getLogger().error("Unexpected Error", e);
            throw new LBInvocationException("Unexpected Internal Error", logId);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#restrictToTargetCodes
     * (org.LexGrid.LexBIG.LexBIGService.CodedNodeSet)
     */
    @LgClientSideSafe
    public CodedNodeGraph restrictToTargetCodes(CodedNodeSet targetCodes) throws LBInvocationException {
        getLogger().logMethod(new Object[] { targetCodes });
        try {
            pendingOperations_.add(new RestrictToTargetCodes(((CodedNodeSetImpl) targetCodes).clone()));
            return this;
        } catch (Exception e) {
            String logId = getLogger().error("Unexpected Error", e);
            throw new LBInvocationException("Unexpected Internal Error", logId);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#restrictToCodeSystem(
     * java.lang.String)
     */
    @LgClientSideSafe
    public CodedNodeGraph restrictToCodeSystem(String codeSystem) throws LBParameterException {
        getLogger().logMethod(new Object[] { codeSystem });
        String translatedName = externalCodeSystemNameToInteralRelationName(codeSystem);
        if (translatedName == null || translatedName.length() == 0) {
            translatedName = codeSystem;
        }
        pendingOperations_.add(new RestrictToCodeSystem(translatedName));

        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#restrictToSourceCodeSystem
     * (java.lang.String)
     */
    @LgClientSideSafe
    public CodedNodeGraph restrictToSourceCodeSystem(String sourceCodeSystem) throws LBParameterException {
        getLogger().logMethod(new Object[] { sourceCodeSystem });
        String translatedName = externalCodeSystemNameToInteralRelationName(sourceCodeSystem);
        if (translatedName == null || translatedName.length() == 0) {
            translatedName = sourceCodeSystem;
        }
        pendingOperations_.add(new RestrictToSourceCodeSystem(translatedName));
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#restrictToTargetCodeSystem
     * (java.lang.String)
     */
    @LgClientSideSafe
    public CodedNodeGraph restrictToTargetCodeSystem(String targetCodeSystem) throws LBParameterException {
        getLogger().logMethod(new Object[] { targetCodeSystem });
        String translatedName = externalCodeSystemNameToInteralRelationName(targetCodeSystem);
        if (translatedName == null || translatedName.length() == 0) {
            translatedName = targetCodeSystem;
        }
        pendingOperations_.add(new RestrictToTargetCodeSystem(translatedName));
        return this;
    }

    @Override
    public CodedNodeGraph restrictToAnonymous(Boolean restrictToAnonymous) throws LBInvocationException,
            LBParameterException {
        throw new UnsupportedOperationException("This feature is only available in LexEvs 6.0.");
    }

    @Override
    public CodedNodeGraph restrictToEntityTypes(LocalNameList localNameList) throws LBInvocationException,
            LBParameterException {
        throw new UnsupportedOperationException("This feature is only available in LexEvs 6.0.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#isCodeInGraph(org.LexGrid
     * .LexBIG.DataModel.Core.ConceptReference)
     */
    public Boolean isCodeInGraph(ConceptReference code) throws LBInvocationException, LBParameterException {
        getLogger().logMethod(new Object[] { code });
        try {
            optimizePendingOpsOrder();
            translateConceptReference(code);
            // Check each assigned relations container ...
            for (int i = 0; i < relationContainerNames_.length; i++) {
                if (SQLImplementedMethods.isCodeInGraph(code, pendingOperations_, internalCodingSchemeName,
                        internalVersion, relationContainerNames_[i]))
                    return true;
            }
            // Not found
            return false;
        } catch (LBInvocationException e) {
            throw e;
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            String logId = getLogger().error("Unexpected Error", e);
            throw new LBInvocationException("Unexpected Internal Error", logId);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#areCodesRelated(org.LexGrid
     * .LexBIG.DataModel.Core.ConceptReference,
     * org.LexGrid.LexBIG.DataModel.Core.ConceptReference,
     * org.LexGrid.LexBIG.DataModel.Core.ConceptReference, boolean)
     */
    public Boolean areCodesRelated(NameAndValue association, ConceptReference sourceCode, ConceptReference targetCode,
            boolean directOnly) throws LBInvocationException, LBParameterException {
        getLogger().logMethod(new Object[] { association, sourceCode, targetCode, new Boolean(directOnly) });
        try {
            optimizePendingOpsOrder();
            translateConceptReference(sourceCode);
            translateConceptReference(targetCode);
            // Check each assigned relations container ...
            for (int i = 0; i < relationContainerNames_.length; i++) {
                String relationsName = relationContainerNames_[i];
                try {
                    if (SQLImplementedMethods.areCodesRelated(association, sourceCode, targetCode, directOnly,
                            pendingOperations_, internalCodingSchemeName, internalVersion, relationsName))
                        return true;

                    // if symmetric - reverse the codes, and check again.
                    if (SQLImplementedMethods.isAssociationSymmetric(internalCodingSchemeName, internalVersion,
                            relationsName, association.getName())) {
                        if (SQLImplementedMethods.areCodesRelated(association, targetCode, sourceCode, directOnly,
                                pendingOperations_, internalCodingSchemeName, internalVersion, relationsName))
                            return true;
                    }
                } catch (LBException lbe) {
                    // do nothing
                }
            }
            // Not found
            return false;
        } catch (LBInvocationException e) {
            throw e;
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            String logId = getLogger().error("Unexpected Error", e);
            throw new LBInvocationException("Unexpected Internal Error", logId);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#listCodeRelationships
     * (org.LexGrid.LexBIG.DataModel.Core.ConceptReference,
     * org.LexGrid.LexBIG.DataModel.Core.ConceptReference, boolean)
     */
    public List<String> listCodeRelationships(ConceptReference sourceCode, ConceptReference targetCode,
            boolean directOnly) throws LBInvocationException, LBParameterException {
        getLogger().logMethod(new Object[] { sourceCode, targetCode, new Boolean(directOnly) });
        try {
            optimizePendingOpsOrder();
            translateConceptReference(sourceCode);
            translateConceptReference(targetCode);

            // Fetch results for the first relations container (typically there
            // will only be one) ...
            List<String> list = SQLImplementedMethods.listCodeRelationships(sourceCode, targetCode, directOnly,
                    pendingOperations_, internalCodingSchemeName, internalVersion, relationContainerNames_[0]);

            // Add others as assigned ...
            for (int i = 1; i < relationContainerNames_.length; i++)
                for (String assoc : SQLImplementedMethods.listCodeRelationships(sourceCode,
                        targetCode, directOnly, pendingOperations_, internalCodingSchemeName, internalVersion,
                        relationContainerNames_[i])) {
                    list.add(assoc);
                }

            return list;
            
        } catch (LBInvocationException e) {
            throw e;
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            String logId = getLogger().error("Unexpected Error", e);
            throw new LBInvocationException("Unexpected Internal Error", logId);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#toNodeList()
     */
    public CodedNodeSet toNodeList(ConceptReference graphFocus, boolean resolveForward, boolean resolveBackward,
            int resolveAssociationDepth, int maxToReturn) throws LBInvocationException, LBParameterException {
        getLogger().logMethod(
                new Object[] { graphFocus, resolveForward, resolveBackward, resolveAssociationDepth, maxToReturn });
        try {
            translateConceptReference(graphFocus);
            GHolder r = getHolder(graphFocus, resolveForward, resolveBackward, resolveAssociationDepth, maxToReturn,
                    false);

            for (int i = 0; i < pendingOperations_.size(); i++) {
                Operation current = pendingOperations_.get(i);
                if (current instanceof Union) {
                    Union union = (Union) current;
                    r.union(((CodedNodeGraphImpl) union.getGraph()).getHolder(graphFocus, resolveForward,
                            resolveBackward, -1, 0, false));

                } else if (current instanceof Intersection) {

                    Intersection intersection = (Intersection) current;
                    r.intersection(((CodedNodeGraphImpl) intersection.getGraph()).getHolder(graphFocus, resolveForward,
                            resolveBackward, -1, 0, false));
                }
            }

            return r.getNodeList();

        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            String logId = getLogger().error("Unexpected Error", e);
            throw new LBInvocationException("Unexpected Internal Error", logId);
        }
    }

    /**
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#resolveAsList(org.LexGrid.LexBIG.DataModel.Core.ConceptReference,
     *      boolean, boolean, int, int,
     *      org.LexGrid.LexBIG.DataModel.Collections.LocalNameList,
     *      org.LexGrid.LexBIG.DataModel.Collections.SortOptionList, int)
     */
    public ResolvedConceptReferenceList resolveAsList(ConceptReference graphFocus, boolean resolveForward,
            boolean resolveBackward, int resolveCodedEntryDepth, int resolveAssociationDepth,
            LocalNameList restrictToProperties, PropertyType[] restrictToPropertyTypes, SortOptionList sortOptions,
            int maxToReturn) throws LBInvocationException, LBParameterException {
        getLogger().logMethod(
                new Object[] { graphFocus, resolveForward, resolveBackward, resolveCodedEntryDepth,
                        resolveAssociationDepth, restrictToProperties, restrictToPropertyTypes, sortOptions,
                        maxToReturn });
        return resolveAsList(graphFocus, resolveForward, resolveBackward, resolveCodedEntryDepth,
                resolveAssociationDepth, restrictToProperties, restrictToPropertyTypes, sortOptions, null, maxToReturn);
    }

    /**
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#resolveAsList(org.LexGrid.LexBIG.DataModel.Core.ConceptReference,
     *      boolean, boolean, int, int,
     *      org.LexGrid.LexBIG.DataModel.Collections.LocalNameList,
     *      org.LexGrid.LexBIG.DataModel.Collections.SortOptionList,
     *      org.LexGrid.LexBIG.DataModel.Collections.LocalNameList, int)
     */
    public ResolvedConceptReferenceList resolveAsList(ConceptReference graphFocus, boolean resolveForward,
            boolean resolveBackward, int resolveCodedEntryDepth, int resolveAssociationDepth,
            LocalNameList restrictToProperties, PropertyType[] restrictToPropertyTypes, SortOptionList sortOptions,
            LocalNameList filterOptions, int maxToReturn) throws LBInvocationException, LBParameterException {
        getLogger().logMethod(
                new Object[] { graphFocus, resolveForward, resolveBackward, resolveCodedEntryDepth,
                        resolveAssociationDepth, restrictToProperties, restrictToPropertyTypes, sortOptions,
                        filterOptions, maxToReturn });
        return resolveAsList(graphFocus, resolveForward, resolveBackward, resolveCodedEntryDepth,
                resolveAssociationDepth, restrictToProperties, restrictToPropertyTypes, sortOptions, filterOptions,
                maxToReturn, false);

    }

    /**
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#resolveAsList(org.LexGrid.LexBIG.DataModel.Core.ConceptReference,
     *      boolean, boolean, int, int,
     *      org.LexGrid.LexBIG.DataModel.Collections.LocalNameList,
     *      org.LexGrid.LexBIG.DataModel.Collections.SortOptionList,
     *      org.LexGrid.LexBIG.DataModel.Collections.LocalNameList, int,
     *      boolean)
     */
    public ResolvedConceptReferenceList resolveAsList(ConceptReference graphFocus, boolean resolveForward,
            boolean resolveBackward, int resolveCodedEntryDepth, int resolveAssociationDepth,
            LocalNameList restrictToProperties, PropertyType[] restrictToPropertyTypes, SortOptionList sortOptions,
            LocalNameList filterOptions, int maxToReturn, boolean keepLastAssociationLevelUnresolved)
            throws LBInvocationException, LBParameterException {
        getLogger().logMethod(
                new Object[] { graphFocus, resolveForward, resolveBackward, resolveCodedEntryDepth,
                        resolveAssociationDepth, restrictToProperties, sortOptions, filterOptions, maxToReturn,
                        keepLastAssociationLevelUnresolved });
        try {
            translateConceptReference(graphFocus);
            GHolder r = getHolder(graphFocus, resolveForward, resolveBackward, resolveAssociationDepth, maxToReturn,
                    keepLastAssociationLevelUnresolved);

            for (int i = 0; i < pendingOperations_.size(); i++) {
                Operation current = pendingOperations_.get(i);
                if (current instanceof Union) {
                    Union union = (Union) current;
                    r.union(((CodedNodeGraphImpl) union.getGraph()).getHolder(graphFocus, resolveForward,
                            resolveBackward, resolveAssociationDepth, maxToReturn - r.getNodeCount(),
                            keepLastAssociationLevelUnresolved));

                } else if (current instanceof Intersection) {

                    Intersection intersection = (Intersection) current;
                    r.intersection(((CodedNodeGraphImpl) intersection.getGraph()).getHolder(graphFocus, resolveForward,
                            resolveBackward, resolveAssociationDepth, maxToReturn, keepLastAssociationLevelUnresolved));
                }
            }

            return r.getResolvedConceptReferenceList(resolveCodedEntryDepth, resolveAssociationDepth,
                    restrictToProperties, restrictToPropertyTypes, sortOptions, filterOptions,
                    keepLastAssociationLevelUnresolved);
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            String logId = getLogger().error("Unexpected Error", e);
            throw new LBInvocationException("Unexpected Internal Error", logId);
        }
    }

    /*
     * convert code system name to internal code system name (if possible)
     */
    private void translateConceptReference(ConceptReference cr) {
        if (cr != null && cr.getCodingSchemeName() != null && cr.getCodingSchemeName().length() > 0) {
            if (!cr.getCodingSchemeName().equals(this.internalCodingSchemeName)) {

                String result = externalCodeSystemNameToInteralRelationName(cr.getCodingSchemeName());
                if (result != null && result.length() > 0) {
                    cr.setCodingSchemeName(result);
                }
            }
        }
    }

    /*
     * graph focus could have a code system which is a local name, or a URN. If
     * it has one, I need to convert it to my internal code system name (for the
     * graph) If we are unioning or intersecting code systems, only one of the
     * code systems may have the info necessary to do this conversion.. try them
     * all till I find one that works.
     */
    private String externalCodeSystemNameToInteralRelationName(String name) {
        // first - external name to URN.
        // now, URN to relationship name

        try {
            // We first check if we can were provided a urn.

            // NOTE: This check could probably be first in this method,
            // but I want to maintain the original intent and be as
            // non-advasive as possible.
            String result = ResourceManager.instance().getRelationshipCodingSchemeNameForURNorName(name,
                    internalCodingSchemeName, true);
            if (result != null && result.length() > 0) {
                return result;
            }

        } catch (Exception e) {
            try {
                String urn = ResourceManager.instance().getURNForExternalCodingSchemeName(name);
                if (urn != null) {
                    String result = ResourceManager.instance().getRelationshipCodingSchemeNameForURNorName(urn,
                            internalCodingSchemeName, true);
                    if (result != null && result.length() > 0) {
                        return result;
                    }
                }
            } catch (Exception ex) {
                this.getLogger().debug("Unable to map: " + name + " to a CodingScheme name");
            }

        }

        // if it is null, couldn't do it in this code system. Can one of the
        // unioned or intersected
        // code systems do the translation?
        for (int i = 0; i < pendingOperations_.size(); i++) {
            Operation current = pendingOperations_.get(i);
            String result = null;
            if (current instanceof Union) {
                Union union = (Union) current;
                result = ((CodedNodeGraphImpl) union.getGraph()).externalCodeSystemNameToInteralRelationName(name);
            } else if (current instanceof Intersection) {
                Intersection intersection = (Intersection) current;
                result = ((CodedNodeGraphImpl) intersection.getGraph())
                        .externalCodeSystemNameToInteralRelationName(name);
            }
            if (result != null && result.length() > 0) {
                return result;
            }
        }
        return null;
    }

    protected GHolder getHolder(ConceptReference graphFocus, boolean resolveForward, boolean resolveBackward,
            int resolveAssociationDepth, int maxToReturn, boolean keepLastAssociationLevelUnresolved) throws Exception {
        optimizePendingOpsOrder();

        // Fetch results for the first relations container (often there will
        // only
        // be one) ...
        GHolder gh = null;
        Exception e = null;
        try {
            gh = SQLImplementedMethods.resolveRelationships(graphFocus, resolveForward, resolveBackward,
                    resolveAssociationDepth, (maxToReturn < 0 ? 0 : maxToReturn), pendingOperations_,
                    internalCodingSchemeName, internalVersion, relationContainerNames_[0],
                    keepLastAssociationLevelUnresolved);
        } catch (LBParameterException lbpe) {
            // Can occur if no top nodes are found... do not abandon yet,
            // though,
            // since top nodes may be found in other containers below.
            e = lbpe;
        }

        // Merge results for other containers as assigned ...
        for (int i = 1; i < relationContainerNames_.length; i++)
            try {
                GHolder gh2 = SQLImplementedMethods.resolveRelationships(graphFocus, resolveForward, resolveBackward,
                        resolveAssociationDepth, (maxToReturn < 0 ? 0 : maxToReturn), pendingOperations_,
                        internalCodingSchemeName, internalVersion, relationContainerNames_[i],
                        keepLastAssociationLevelUnresolved);
                if (gh == null)
                    gh = gh2;
                else
                    gh.union(gh2);
            } catch (LBParameterException lbpe) {
                // Can occur if no top nodes are found... do not abandon yet,
                // though,
                // since top nodes may be found in other containers.
                e = lbpe;
            }

        // No results and error occurred? if so, exit now.
        // Otherwise, return.
        if (e != null && (gh == null || gh.getNodeCount() == 0))
            throw e;
        return gh;
    }

    /*
     * make a clone of this CodedNodeSet - used before doing unions, joins, etc
     * since the optimize process may insert new operations.
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    @SuppressWarnings("unchecked")
    @LgClientSideSafe
    public CodedNodeGraphImpl clone() throws CloneNotSupportedException {
        CodedNodeGraphImpl cng = (CodedNodeGraphImpl) super.clone();
        cng.pendingOperations_ = (ArrayList<Operation>) this.pendingOperations_.clone();
        return cng;
    }

    /*
     * Rearrange the pending operations so that all restrictions are performed
     * before unions, intersections
     */
    protected void optimizePendingOpsOrder() {
        // Want to pull all restrictions up above any unions and intersections.
        // This lets me do cheap restrictions for a longer time - union forces a
        // brute force approach
        // that uses much more memory.
        int firstUnionPos = -1;
        for (int i = 0; i < pendingOperations_.size(); i++) {
            Operation currentOperation = pendingOperations_.get(i);

            if ((currentOperation instanceof Restriction || currentOperation instanceof Intersection)
                    && firstUnionPos >= 0) {
                // move this restriction above the firstUnionPos - shift
                // everything
                // else down.
                for (int j = i; j > firstUnionPos; j--) {
                    pendingOperations_.set(j, pendingOperations_.get(j - 1));
                    // need to put this restriction into each union or
                    // intersection
                    // that should occur before
                    // it.
                    if (pendingOperations_.get(j) instanceof Union) {
                        ((CodedNodeGraphImpl) ((Union) pendingOperations_.get(j)).getGraph())
                                .addPendingOperation(currentOperation);
                    } else if (pendingOperations_.get(j) instanceof Intersection) {
                        ((CodedNodeGraphImpl) ((Intersection) pendingOperations_.get(j)).getGraph())
                                .addPendingOperation(currentOperation);
                    }
                }

                pendingOperations_.set(firstUnionPos, currentOperation);
                firstUnionPos++;

            }
            if (firstUnionPos == -1 && (currentOperation instanceof Union || currentOperation instanceof Intersection)) {
                firstUnionPos = i;
            }
        }
    }

    /*
     * Add a new operation to the list to be executed, used by the optimize
     * process.
     */
    private void addPendingOperation(Operation operation) {
        pendingOperations_.add(operation);
    }

    public List<String> listCodeRelationships(ConceptReference sourceCode, ConceptReference targetCode,
            int distance) throws LBInvocationException, LBParameterException {
        getLogger().logMethod(new Object[] { sourceCode, targetCode, distance });
        try {
            List<String> returnList = new ArrayList<String>();

            if (distance <= 1)
                return listCodeRelationships(sourceCode, targetCode, true);

            int newDistance = distance - 1;
            ResolvedConceptReferenceList rList = resolveAsList(sourceCode, true, false, 1, 1, null, null, null, null,
                    0, false);
            for (Iterator<? extends ResolvedConceptReference> rListI = rList.iterateResolvedConceptReference(); rListI.hasNext();) {
                for (Iterator<? extends Association> assocI = rListI.next().getSourceOf().iterateAssociation(); assocI.hasNext();) {
                    for (Iterator<? extends AssociatedConcept> assConI = assocI.next().getAssociatedConcepts()
                            .iterateAssociatedConcept(); assConI.hasNext();) {
                        AssociatedConcept assCon = assConI.next();
                        ConceptReference src = Constructors.createConceptReference(assCon.getConceptCode(), assCon
                                .getCodingSchemeName());
                        for (String association : listCodeRelationships(src, targetCode, newDistance)) {
                            returnList.add(association);
                        }
                    }
                }
            }

            return returnList;
        } catch (LBInvocationException e) {
            throw e;
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            String logId = getLogger().error("Unexpected Error", e);
            throw new LBInvocationException("Unexpected Internal Error", logId);
        }
    }
}