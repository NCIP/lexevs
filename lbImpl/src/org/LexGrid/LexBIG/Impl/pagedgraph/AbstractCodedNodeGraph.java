
package org.LexGrid.LexBIG.Impl.pagedgraph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.lexevs.logging.LoggerFactory;

/**
 * The Class AbstractCodedNodeGraph.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractCodedNodeGraph implements CodedNodeGraph {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7955494879137282711L;
    
    /** The logger. */
    protected LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }
    
    protected AbstractCodedNodeGraph() {
        super();
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#resolveAsList(org.LexGrid.LexBIG.DataModel.Core.ConceptReference, boolean, boolean, int, int, org.LexGrid.LexBIG.DataModel.Collections.LocalNameList, org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType[], org.LexGrid.LexBIG.DataModel.Collections.SortOptionList, int)
     */
    @Override
    public ResolvedConceptReferenceList resolveAsList(ConceptReference graphFocus, boolean resolveForward,
            boolean resolveBackward, int resolveCodedEntryDepth, int resolveAssociationDepth,
            LocalNameList propertyNames, PropertyType[] propertyTypes, SortOptionList sortOptions, int maxToReturn)
            throws LBInvocationException, LBParameterException {
       return this.resolveAsList(
               graphFocus, 
               resolveForward, 
               resolveBackward, 
               resolveCodedEntryDepth, 
               resolveAssociationDepth, 
               propertyNames, 
               propertyTypes, 
               sortOptions, 
               null, 
               maxToReturn);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#resolveAsList(org.LexGrid.LexBIG.DataModel.Core.ConceptReference, boolean, boolean, int, int, org.LexGrid.LexBIG.DataModel.Collections.LocalNameList, org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType[], org.LexGrid.LexBIG.DataModel.Collections.SortOptionList, org.LexGrid.LexBIG.DataModel.Collections.LocalNameList, int)
     */
    @Override
    public ResolvedConceptReferenceList resolveAsList(
            ConceptReference graphFocus, 
            boolean resolveForward,
            boolean resolveBackward, 
            int resolveCodedEntryDepth, 
            int resolveAssociationDepth,
            LocalNameList propertyNames, 
            PropertyType[] propertyTypes, 
            SortOptionList sortOptions,
            LocalNameList filterOptions, 
            int maxToReturn) throws LBInvocationException, LBParameterException {
        return this.doResolveAsList(graphFocus, resolveForward, resolveBackward, resolveCodedEntryDepth, resolveAssociationDepth, propertyNames, propertyTypes, sortOptions, filterOptions, maxToReturn, false);
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#resolveAsList(org.LexGrid.LexBIG.DataModel.Core.ConceptReference, boolean, boolean, int, int, org.LexGrid.LexBIG.DataModel.Collections.LocalNameList, org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType[], org.LexGrid.LexBIG.DataModel.Collections.SortOptionList, org.LexGrid.LexBIG.DataModel.Collections.LocalNameList, int, boolean)
     */
    public ResolvedConceptReferenceList resolveAsList(ConceptReference graphFocus, boolean resolveForward,
            boolean resolveBackward, int resolveCodedEntryDepth, int resolveAssociationDepth,
            LocalNameList propertyNames, PropertyType[] propertyTypes, SortOptionList sortOptions,
            LocalNameList filterOptions, int maxToReturn, boolean keepLastAssociationLevelUnresolved)
            throws LBInvocationException, LBParameterException{
 
        return this.doResolveAsList(graphFocus, resolveForward, resolveBackward, resolveCodedEntryDepth, resolveAssociationDepth, propertyNames, propertyTypes, sortOptions, filterOptions, maxToReturn, keepLastAssociationLevelUnresolved);   
    }

    /**
     * Do resolve as list.
     * 
     * @param graphFocus the graph focus
     * @param resolveForward the resolve forward
     * @param resolveBackward the resolve backward
     * @param resolveCodedEntryDepth the resolve coded entry depth
     * @param resolveAssociationDepth the resolve association depth
     * @param propertyNames the property names
     * @param propertyTypes the property types
     * @param sortOptions the sort options
     * @param filterOptions the filter options
     * @param maxToReturn the max to return
     * @param keepLastAssociationLevelUnresolved the keep last association level unresolved
     * 
     * @return the resolved concept reference list
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    public abstract ResolvedConceptReferenceList doResolveAsList(ConceptReference graphFocus, boolean resolveForward,
            boolean resolveBackward, int resolveCodedEntryDepth, int resolveAssociationDepth,
            LocalNameList propertyNames, PropertyType[] propertyTypes, SortOptionList sortOptions,
            LocalNameList filterOptions, int maxToReturn, boolean keepLastAssociationLevelUnresolved)
            throws LBInvocationException, LBParameterException;

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#intersect(org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph)
     */
    @Override
    public CodedNodeGraph intersect(CodedNodeGraph graph) throws LBInvocationException, LBParameterException {
       return new IntersectGraph(this, graph);
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#union(org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph)
     */
    @Override
    public CodedNodeGraph union(CodedNodeGraph graph) throws LBInvocationException, LBParameterException {
        return new UnionGraph(this, graph);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#isCodeInGraph(org.LexGrid.LexBIG.DataModel.Core.ConceptReference)
     */
    @Override
    public Boolean isCodeInGraph(ConceptReference code) throws LBInvocationException, LBParameterException {
        ResolvedConceptReferenceList list = 
            this.doResolveAsList(code, true, true, 0, 0, null, null, null, null, 1, false);
        return (list != null && list.getResolvedConceptReferenceCount() > 0);
    }

  
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#listCodeRelationships(org.LexGrid.LexBIG.DataModel.Core.ConceptReference, org.LexGrid.LexBIG.DataModel.Core.ConceptReference, int)
     */
    @Override
    public List<String> listCodeRelationships(ConceptReference sourceCode, ConceptReference targetCode,
            int distance) throws LBInvocationException, LBParameterException {
 
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
    }
}