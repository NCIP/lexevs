
package org.LexGrid.LexBIG.Impl.pagedgraph.query;

import java.io.Serializable;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery;

/**
 * The Interface GraphQueryBuilder.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface GraphQueryBuilder extends Serializable {
    
    /**
     * Gets the query.
     * 
     * @return the query
     */
    public GraphQuery getQuery();
    
    /**
     * Restrict the graph to the nodes that participate as a source or target of
     * the named association and, if supplied, the named association qualifiers.
     * 
     * @param association List of associations used to restrict the graph.  The name and
     * value for each item in the list will be compared against the
     * id and URI of supported associations for participating
     * coding schemes.
     * @param associationQualifiers If supplied, restriction only applies to associations that are
     * qualified by one or more of the supplied qualifiers.  The name
     * and value for each item in the list will be compared against
     * the id and URI of supported association qualifiers for
     * participating coding schemes.
     * 
     * @return A new CodedNodeGraph representing the filtered result.
     * 
     * @throws LBInvocationException,LBParameterException      * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     * @throws LBInvocationException the LB invocation exception
     */
    void restrictToAssociations(
            NameAndValueList association,
            NameAndValueList associationQualifiers)
            throws LBInvocationException,LBParameterException;

    /**
     * Restrict the graph to the nodes that participate as a source or target of
     * an association whose directional name matches the one provided and, if
     * supplied, the named association qualifiers. A directional name is
     * considered to be either the forward or reverse label registered to an
     * association defined by the ontology. Forward and reverse names are
     * optionally assigned to each association. For example, an association
     * 'lineage' may have a forward name 'ancestorOf' and reverse name
     * 'descendantOf'.
     * 
     * @param directionalNames List of directionalNames used to restrict the graph. A
     * directional name is compared against the forward and reverse
     * names for defined associations. If a given name matches more
     * than one forward or reverse label, all corresponding
     * associations are included in the restriction.
     * @param associationQualifiers If supplied, restriction only applies to associations that are
     * qualified by one or more of the supplied qualifiers. The name
     * and value for each item in the list will be compared against
     * the id and URI of supported association qualifiers for
     * participating coding schemes.
     * 
     * @return A new CodedNodeGraph representing the filtered result.
     * 
     * @throws LBInvocationException ,LBParameterException
     * @throws LBParameterException the LB parameter exception
     */
    void restrictToDirectionalNames(
            NameAndValueList directionalNames,
            NameAndValueList associationQualifiers)
            throws LBInvocationException,LBParameterException;  
    
    /**
     * Return a graph that contains only the codes that are present in the
     * supplied list, and all edges that still have a source and target code
     * remaining.
     * 
     * @param codes Codes to filter on.
     * 
     * @return A new CodedNodeGraph representing the filtered result.
     * 
     * @throws LBInvocationException,LBParameterException      * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     * @throws LBInvocationException the LB invocation exception
     */
    void restrictToCodes(CodedNodeSet codes)
            throws LBInvocationException,LBParameterException;

    /**
     * Restrict the graph to codes (source and target) that originate
     * from the supplied code system. Note: edges defined by other code systems
     * will still be resolved if associated with both source and target nodes
     * for the restricted code system.
     * 
     * @param codingScheme The local name or URI of the coding scheme to filter on.
     * 
     * @return A new CodedNodeGraph representing the filtered result.
     * 
     * @throws LBInvocationException,LBParameterException      * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     * @throws LBInvocationException the LB invocation exception
     */
    void restrictToCodeSystem(String codingScheme)
            throws LBInvocationException,LBParameterException;

    /**
     * Restrict the graph to associations that have one of the codes in the
     * supplied list as source codes.
     * 
     * @param codes Codes to filter on.
     * 
     * @return A new CodedNodeGraph representing the filtered result.
     * 
     * @throws LBInvocationException,LBParameterException      * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     * @throws LBInvocationException the LB invocation exception
     */
    void restrictToSourceCodes(CodedNodeSet codes)
            throws LBInvocationException,LBParameterException;

    /**
     * Restrict the graph to edges that have codes from the specified
     * code system as a source.
     * 
     * @param codingScheme The local name or URI of the coding scheme to filter on.
     * 
     * @return A new CodedNodeGraph representing the filtered result.
     * 
     * @throws LBInvocationException,LBParameterException      * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     * @throws LBInvocationException the LB invocation exception
     */
    void restrictToSourceCodeSystem(String codingScheme)
            throws LBInvocationException,LBParameterException;

    /**
     * Restrict the graph to associations that have one of the codes in the
     * supplied list as target codes.
     * 
     * @param codes Codes to filter on.
     * 
     * @return A new CodedNodeGraph representing the filtered result.
     * 
     * @throws LBInvocationException,LBParameterException      * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     * @throws LBInvocationException the LB invocation exception
     */
    void restrictToTargetCodes(CodedNodeSet codes)
            throws LBInvocationException,LBParameterException;

    /**
     * Restrict the graph to edges that have codes from the specified
     * code system as a target.
     * 
     * @param codingScheme The local name or URI of the coding scheme to filter on.
     * 
     * @return A new CodedNodeGraph representing the filtered result.
     * 
     * @throws LBInvocationException,LBParameterException      * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     * @throws LBInvocationException the LB invocation exception
     */
    void restrictToTargetCodeSystem(String codingScheme)
            throws LBInvocationException,LBParameterException;
    
    void restrictToEntityTypes(LocalNameList localNameList) throws LBInvocationException,
        LBParameterException;

    void restrictToAnonymous(Boolean restrictToAnonymous) throws LBInvocationException,
            LBParameterException;
}