
package org.LexGrid.LexBIG.LexBIGService;

import java.io.Serializable;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;

/**
 * A virtual graph where the edges represent associations and the nodes
 * represent coded entries. A CodedNodeGraph describes a graph that can be
 * combined with other graphs, queried or resolved into an actual graph
 * rendering.
 */
public interface CodedNodeGraph extends Serializable {

	/**
	 * Determine whether there is an directed edge (or transitive closure of an
	 * edge) from the source code to the target code in this graph. The last
	 * parameter determines whether only direct associations are considered or
	 * whether the transitive closure of the edge is used.
	 * 
	 * @param association
	 *            Identifies the association to be tested. The name and value
	 *            will be compared against the id and URI of supported
	 *            associations for participating coding schemes.
	 * @param sourceCode
	 *            Source code system/code to be tested.
	 * @param targetCode
	 *            Target code system/code to be tested.
	 * @param directOnly
	 *            True means only asserted association instances are tested.
	 *            False means that, if the association is defined as transitive,
	 *            the transitive closure of the association instances are
	 *            tested.
	 * @return True if a directed edge exists; otherwise False.
	 * @throws LBInvocationException,LBParameterException
	 */
	Boolean areCodesRelated(NameAndValue association,
			ConceptReference sourceCode, ConceptReference targetCode,
			boolean directOnly) throws LBInvocationException,
			LBParameterException;

	/**
	 * Return the set of nodes and associations that are present in both
	 * graphs.
	 * 
	 * @param graph
	 *            Identifies the CodedNodeGraph to be intersected with.
	 * @return A new CodedNodeGraph representing the intersection result.
	 * @throws LBInvocationException,LBParameterException
	 */
	CodedNodeGraph intersect(CodedNodeGraph graph)
			throws LBInvocationException,LBParameterException;

	/**
	 * Determine whether the supplied code is in the graph.
	 * 
	 * @param code
	 *            Identifies the coding scheme and code to test.
	 * @return True if the code is present; otherwise False.
	 * @throws LBInvocationException,LBParameterException
	 */
	Boolean isCodeInGraph(ConceptReference code)
			throws LBInvocationException,LBParameterException;

	/**
	 * Return a list of all of the associations in the graph that have the
	 * supplied source and target codes or, if directOnly is false, all
	 * associations whose transitive closure has the supplied associations.
	 * 
	 * @param sourceCode
	 *            Source end of the association. If null, all sources for the
	 *            specified target are included.
	 * @param targetCode
	 *            Target end of the association. If null, all targets for the
	 *            specified source are included.
	 * @param directOnly
	 *            True means only direct associations are tested. False means
	 *            that the transitive closure of transitive (and undefined)
	 *            associations are tested for membership.
	 *            <p>
	 *            Note that while the class of the returned value appears
	 *            to imply concepts only, each contained reference inherits
	 *            from the more general CodedNodeReference and is capable
	 *            of representing any type of node contained by the graph.
	 * @return The list of matching associations.
	 * @throws LBInvocationException,LBParameterException
	 */
	List<String> listCodeRelationships(
			ConceptReference sourceCode, ConceptReference targetCode,
			boolean directOnly) throws LBInvocationException,
			LBParameterException;

	/**
	 * Return a list of all of the associations in the graph that have the
	 * supplied source and target codes based on distance between them.
	 * Distance (or the No. of edges) for a direct association between a 
	 * source and target codes is 1. Values if distance should be equal or 
	 * greater than 1, otherwise exception is thrown. Resulting list is not
	 * based on associations source & target have, but on distance only.
	 * 
	 * @param sourceCode
	 *            Source end of the association. If null, all sources for the
	 *            specified target are included.
	 * @param targetCode
	 *            Target end of the association. If null, all targets for the
	 *            specified source are included.
	 * @param distance
	 *            Distance (# of edges) source and target codes must have in 
	 *            between. Must be positive & greater than zero. 
	 *
	 * @return The list of matching associations.
	 *            
	 * @throws LBInvocationException,LBParameterException
	 */
	List<String> listCodeRelationships(
			ConceptReference sourceCode, ConceptReference targetCode,
			int distance) throws LBInvocationException,
			LBParameterException;

	/**
	 * Resolve all of the coded nodes in the list, sorting by the supplied
	 * property (if any), resolving the supplied properties, resolving coded
	 * entries to the supplied depth and resolving associations to the supplied
	 * depth.
	 * 
	 * @param graphFocus
	 *            Set the top or "focus" node of the graph. If present, only
	 *            the nodes that are reachable via this node will be returned.
     *            If null, nodes with no incoming or outgoing associations
     *            are used as starting points for navigation of forward and
     *            reverse relationships, respectively.
	 * @param resolveForward
	 *            True means resolve in the direction of source to target.
	 * @param resolveBackward
     *            True means resolve in the direction of target to source.
	 * @param resolveCodedEntryDepth
	 *            Depth in the graph to resolve coded entries. - 1 means don't
	 *            resolve anything - just return code references, 0 means
	 *            resolve just the root nodes, 1 means resolve 1 deep, etc.
	 * @param resolveAssociationDepth
	 *            Number of hops to resolve associations. 0 means leave all
	 *            associations unresolved, 1 means immediate neighbors, etc. -1
	 *            means follow the entire closure of the graph.
	 * @param propertyNames
	 *            Local names of properties to resolve.  If not empty and not
	 *            null, only properties matching the given names are included
	 *            for resolved nodes. 
     * @param propertyTypes
	 *            Indicates whether to resolve only specific property categories,
	 *            regardless of the assigned name.  Any of the enumerated PropertyType
	 *            values can be specified.  If not empty and not null, only
	 *            properties matching the given types are included for resolved
	 *            nodes.
	 * @param sortOptions
	 *            List of sort options to apply during resolution. If supplied,
	 *            the sort algorithms will be applied in the order provided. Any
	 *            algorithms not valid to be applied in context of node set
	 *            iteration, as specified in the sort extension description,
	 *            will result in a parameter exception. Available algorithms can
	 *            be retrieved through the LexBIGService getSortExtensions()
	 *            method after being defined to the LexBIGServiceManager
	 *            extension registry.
	 * @param maxToReturn
	 *            Maximum number of entries to return; a value less than 1
	 *            indicates to return unlimited entries (to the limit specified
	 *            in the runtime configuration file).
	 * @return  A list of code references, up to the maximum
	 *            number specified.  Note that in the event that a maximum
	 *            number 'n' is specified and exactly 'n' items are returned,
	 *            there is currently no flag or notification provided to
	 *            indicate whether all available items were returned.
	 *            <p>
	 *            Each entry will include basic information for the node
	 *            along with an embedded object (e.g. concept) populated
	 *            with requested properties.  
	 *            <p>
	 *            Note that while the class of the returned value appears
	 *            to imply concepts only, each contained reference inherits
	 *            from the more general CodedNodeReference and is capable
	 *            of representing any type of node contained by the graph.
	 * @throws LBInvocationException,LBParameterException
	 */
	ResolvedConceptReferenceList resolveAsList(
			ConceptReference graphFocus, boolean resolveForward, boolean resolveBackward,
			int resolveCodedEntryDepth, int resolveAssociationDepth,
			LocalNameList propertyNames, PropertyType[] propertyTypes,
			SortOptionList sortOptions, int maxToReturn)
			throws LBInvocationException,LBParameterException;

	/**
	 * Resolve all of the coded nodes in the list, sorting by the supplied
	 * property (if any), resolving the supplied properties, resolving coded
	 * entries to the supplied depth, resolving associations to the supplied
	 * depth, and allowing for additional filters to be applied against
	 * the returned items.
	 * 
	 * @param graphFocus
     *            Set the top or "focus" node of the graph. If present, only
     *            the nodes that are reachable via this node will be returned.
     *            If null, nodes with no incoming or outgoing associations
     *            are used as starting points for navigation of forward and
     *            reverse relationships, respectively.
     * @param resolveForward
     *            True means resolve in the direction of source to target.
     * @param resolveBackward
     *            True means resolve in the direction of target to source.
	 * @param resolveCodedEntryDepth
	 *            Depth in the graph to resolve coded entries. - 1 means don't
	 *            resolve anything - just return the code references, 0 means
	 *            resolve just the root nodes, 1 means resolve 1 deep, etc.
	 * @param resolveAssociationDepth
	 *            Number of hops to resolve associations. 0 means leave all
	 *            associations unresolved, 1 means immediate neighbors, etc. -1
	 *            means follow the entire closure of the graph.
	 * @param propertyNames
	 *            Local names of properties to resolve.  If not empty and not
	 *            null, only properties matching the given names are included
	 *            for resolved nodes. 
     * @param propertyTypes
	 *            Indicates whether to resolve only specific property categories,
	 *            regardless of the assigned name.  Any of the enumerated PropertyType
	 *            values can be specified.  If not empty and not null, only
	 *            properties matching the given types are included for resolved
	 *            nodes.
	 * @param sortOptions
	 *            List of sort options to apply during resolution. If supplied,
	 *            the sort algorithms will be applied in the order provided. Any
	 *            algorithms not valid to be applied in context of node set
	 *            iteration, as specified in the sort extension description,
	 *            will result in a parameter exception. Available algorithms can
	 *            be retrieved through the LexBIGService getSortExtensions()
	 *            method after being defined to the LexBIGServiceManager
	 *            extension registry.
	 * @param filterOptions
	 *            List of Filter extensions to apply during resolution. If
	 *            supplied, filters are applied in the order provided. Each name
	 *            in the list must correspond to the name of a Filter
	 *            description as registered to the associated service. Available
	 *            Filter descriptions can be retrieved through the LexBIGService
	 *            getFilterExtensions() method after being defined to the
	 *            LexBIGServiceManager extension registry.
	 * @param maxToReturn
	 *            Maximum number of entries to return; a value less than 1
	 *            indicates to return unlimited entries (to the limit specified
	 *            in the runtime configuration file).
	 * @return  A list of node references, up to the maximum
	 *            number specified.  Note that in the event that a maximum
	 *            number 'n' is specified and exactly 'n' items are returned,
	 *            there is currently no flag or notification provided to
	 *            indicate whether all available items were returned.
	 *            <p>
	 *            Note that while the class of the returned value appears
	 *            to imply concepts only, each contained reference inherits
	 *            from the more general CodedNodeReference and is capable
	 *            of representing any type of node contained by the graph.
	 * @throws LBInvocationException,LBParameterException
	 */
	ResolvedConceptReferenceList resolveAsList(
			ConceptReference graphFocus, boolean resolveForward,
			boolean resolveBackward, int resolveCodedEntryDepth, int resolveAssociationDepth,
			LocalNameList propertyNames, PropertyType[] propertyTypes,
			SortOptionList sortOptions, LocalNameList filterOptions, int maxToReturn)
			throws LBInvocationException,LBParameterException;

	/**
	 * Resolve all of the coded nodes in the list, sorting by the supplied
	 * property (if any), resolving the supplied properties, resolving coded
	 * entries to the supplied depth, resolving associations to the supplied
	 * depth, and allowing for additional filters to be applied against
	 * the returned items.
	 * 
	 * @param graphFocus
     *            Set the top or "focus" node of the graph. If present, only
     *            the nodes that are reachable via this node will be returned.
     *            If null, nodes with no incoming or outgoing associations
     *            are used as starting points for navigation of forward and
     *            reverse relationships, respectively.
     * @param resolveForward
     *            True means resolve in the direction of source to target.
     * @param resolveBackward
     *            True means resolve in the direction of target to source.
	 * @param resolveCodedEntryDepth
	 *            Depth in the graph to resolve coded entries. - 1 means don't
	 *            resolve anything - just return the code references, 0 means
	 *            resolve just the root nodes, 1 means resolve 1 deep, etc.
	 * @param resolveAssociationDepth
	 *            Number of hops to resolve associations. 0 means leave all
	 *            associations unresolved, 1 means immediate neighbors, etc. -1
	 *            means follow the entire closure of the graph.
	 * @param propertyNames
	 *            Local names of properties to resolve.  If not empty and not
	 *            null, only properties matching the given names are included
	 *            for resolved nodes. 
     * @param propertyTypes
	 *            Indicates whether to resolve only specific property categories,
	 *            regardless of the assigned name.  Any of the enumerated PropertyType
	 *            values can be specified.  If not empty and not null, only
	 *            properties matching the given types are included for resolved
	 *            nodes.
	 * @param sortOptions
	 *            List of sort options to apply during resolution. If supplied,
	 *            the sort algorithms will be applied in the order provided. Any
	 *            algorithms not valid to be applied in context of node set
	 *            iteration, as specified in the sort extension description,
	 *            will result in a parameter exception. Available algorithms can
	 *            be retrieved through the LexBIGService getSortExtensions()
	 *            method after being defined to the LexBIGServiceManager
	 *            extension registry.
	 * @param filterOptions
	 *            List of Filter extensions to apply during resolution. If
	 *            supplied, filters are applied in the order provided. Each name
	 *            in the list must correspond to the name of a Filter
	 *            description as registered to the associated service. Available
	 *            Filter descriptions can be retrieved through the LexBIGService
	 *            getFilterExtensions() method after being defined to the
	 *            LexBIGServiceManager extension registry.
	 * @param maxToReturn
	 *            Maximum number of entries to return; a value less than 1
	 *            indicates to return unlimited entries (to the limit specified
	 *            in the runtime configuration file).
	 * @param keepLastAssociationLevelUnresolved
	 *            Keep the last hop while resolving associations to the resolveAssociationDepth
	 *            unresolved. This is useful while drawing trees of an ontology and we need a
	 *            quick way to tell if the tree can be expanded further. 
	 * @return  A list of coded node references, up to the maximum
	 *            number specified.  Note that in the event that a maximum
	 *            number 'n' is specified and exactly 'n' items are returned,
	 *            there is currently no flag or notification provided to
	 *            indicate whether all available items were returned.
	 *            <p>
	 *            Note that while the class of the returned value appears
	 *            to imply concepts only, each contained reference inherits
	 *            from the more general CodedNodeReference and is capable
	 *            of representing any type of node contained by the graph.
	 * @throws LBInvocationException,LBParameterException
	 */
	ResolvedConceptReferenceList resolveAsList(
			ConceptReference graphFocus, boolean resolveForward,
			boolean resolveBackward, int resolveCodedEntryDepth, int resolveAssociationDepth,
			LocalNameList propertyNames, PropertyType[] propertyTypes,
			SortOptionList sortOptions, LocalNameList filterOptions, int maxToReturn, 
			boolean keepLastAssociationLevelUnresolved)
			throws LBInvocationException,LBParameterException;
	
	/**
	 * Restrict the graph to the nodes that participate as a source or target of
	 * the named association and, if supplied, the named association qualifiers.
	 * 
	 * @param association
	 *            List of associations used to restrict the graph.  The name and
	 *            value for each item in the list will be compared against the
	 *            id and URI of supported associations for participating
	 *            coding schemes.
	 * @param associationQualifiers
	 *            If supplied, restriction only applies to associations that are
	 *            qualified by one or more of the supplied qualifiers.  The name
	 *            and value for each item in the list will be compared against
	 *            the id and URI of supported association qualifiers for
	 *            participating coding schemes.
	 * @return A new CodedNodeGraph representing the filtered result.
	 * @throws LBInvocationException,LBParameterException
	 */
	CodedNodeGraph restrictToAssociations(
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
	 * @param directionalNames
	 *            List of directionalNames used to restrict the graph. A
	 *            directional name is compared against the forward and reverse
	 *            names for defined associations. If a given name matches more
	 *            than one forward or reverse label, all corresponding
	 *            associations are included in the restriction.
	 * @param associationQualifiers
	 *            If supplied, restriction only applies to associations that are
	 *            qualified by one or more of the supplied qualifiers. The name
	 *            and value for each item in the list will be compared against
	 *            the id and URI of supported association qualifiers for
	 *            participating coding schemes.
	 * @return A new CodedNodeGraph representing the filtered result.
	 * @throws LBInvocationException
	 *             ,LBParameterException
	 */
	CodedNodeGraph restrictToDirectionalNames(
			NameAndValueList directionalNames,
			NameAndValueList associationQualifiers)
			throws LBInvocationException,LBParameterException;	
	
	/**
	 * Return a graph that contains only the codes that are present in the
	 * supplied list, and all edges that still have a source and target code
	 * remaining.
	 * 
	 * @param codes
	 *            Codes to filter on.
	 * @return A new CodedNodeGraph representing the filtered result.
	 * @throws LBInvocationException,LBParameterException
	 */
	CodedNodeGraph restrictToCodes(CodedNodeSet codes)
			throws LBInvocationException,LBParameterException;

	/**
	 * Restrict the graph to codes (source and target) that originate
	 * from the supplied code system. Note: edges defined by other code systems
	 * will still be resolved if associated with both source and target nodes
	 * for the restricted code system.
	 * 
	 * @param codingScheme
	 *            The local name or URI of the coding scheme to filter on.
	 * @return A new CodedNodeGraph representing the filtered result.
	 * @throws LBInvocationException,LBParameterException
	 */
	CodedNodeGraph restrictToCodeSystem(String codingScheme)
			throws LBInvocationException,LBParameterException;

	/**
	 * Restrict the graph to associations that have one of the codes in the
	 * supplied list as source codes.
	 * 
	 * @param codes
	 *            Codes to filter on.
	 * @return A new CodedNodeGraph representing the filtered result.
	 * @throws LBInvocationException,LBParameterException
	 */
	CodedNodeGraph restrictToSourceCodes(CodedNodeSet codes)
			throws LBInvocationException,LBParameterException;

	/**
	 * Restrict the graph to edges that have codes from the specified
	 * code system as a source.
	 * 
	 * @param codingScheme
	 *            The local name or URI of the coding scheme to filter on.
	 * @return A new CodedNodeGraph representing the filtered result.
	 * @throws LBInvocationException,LBParameterException
	 */
	CodedNodeGraph restrictToSourceCodeSystem(String codingScheme)
			throws LBInvocationException,LBParameterException;

	/**
	 * Restrict the graph to associations that have one of the codes in the
	 * supplied list as target codes.
	 * 
	 * @param codes
	 *            Codes to filter on.
	 * @return A new CodedNodeGraph representing the filtered result.
	 * @throws LBInvocationException,LBParameterException
	 */
	CodedNodeGraph restrictToTargetCodes(CodedNodeSet codes)
			throws LBInvocationException,LBParameterException;

	/**
	 * Restrict the graph to edges that have codes from the specified
	 * code system as a target.
	 * 
	 * @param codingScheme
	 *            The local name or URI of the coding scheme to filter on.
	 * @return A new CodedNodeGraph representing the filtered result.
	 * @throws LBInvocationException,LBParameterException
	 */
	CodedNodeGraph restrictToTargetCodeSystem(String codingScheme)
			throws LBInvocationException,LBParameterException;
	
	/**
	 * Return a graph that contains only the codes that contain an Entity Type
	 * in the supplied list, and all edges that still have a source and target code
	 * remaining.
	 * 
	 * @param localNameList the local name list
	 * 
	 * @return the coded node graph
	 * 
	 * @throws LBInvocationException the LB invocation exception
	 * @throws LBParameterException the LB parameter exception
	 */
	CodedNodeGraph restrictToEntityTypes(LocalNameList localNameList)
		throws LBInvocationException,LBParameterException;
	
	/**
	 * Return a graph that contains only the codes that are either Anonymous,
	 * Non-Anonymous, or Not Specified in the supplied list, and all edges 
	 * that still have a source and target code remaining.
	 * 
	 * A True value signifies Anonymous codes only
	 * A False value signifies Non-Anonymous only
	 * A Null value signifies all codes will matched
	 * 
	 * @param localNameList the local name list
	 * 
	 * @return the coded node graph
	 * 
	 * @throws LBInvocationException the LB invocation exception
	 * @throws LBParameterException the LB parameter exception
	 */
	CodedNodeGraph restrictToAnonymous(Boolean restrictToAnonymous)
		throws LBInvocationException,LBParameterException;

	/**
	 * Transform the graph into a simple of list of code references,
	 * removing all association information.
	 * 
	 * @param graphFocus
     *            Set the top or "focus" node of the graph. If present, only
     *            the nodes that are reachable via this node will be returned.
     *            If null, nodes with no incoming or outgoing associations
     *            are used as starting points for navigation of forward and
     *            reverse relationships, respectively.
	 * @param resolveForward
	 *            True means include all "forward" (source->target)
	 *            associations.
	 * @param resolveBackward
	 *            True means render all "reverse" (target->source) associations
	 * @param resolveAssociationDepth
	 *            Number of hops to resolve associations. 0 means leave all
	 *            associations unresolved, 1 means immediate neighbors, etc. -1
	 *            means follow the entire closure of the graph.
	 * @param maxToReturn
	 *            Maximum number of entries to return; a value less than 1
	 *            indicates to return unlimited entries (to the limit specified
	 *            in the runtime configuration file).
	 * @return  A set with matching items, up to the maximum
	 *            number specified.  Note that in the event that a maximum
	 *            number 'n' is specified and exactly 'n' items are returned,
	 *            there is currently no flag or notification provided to
	 *            indicate whether all available items were returned.
	 * @throws LBInvocationException,LBParameterException
	 */
	CodedNodeSet toNodeList(ConceptReference graphFocus,
			boolean resolveForward, boolean resolveBackward,
			int resolveAssociationDepth, int maxToReturn)
			throws LBInvocationException,LBParameterException;

	/**
	 * Return the union of the two graphs. Union, in this context, means that
	 * the resulting graph contains the unique set of coded entries (String
	 * independent) that are present in one or both of the graphs, and the
	 * unique combination of edges (associations) present in one or both of the
	 * graphs.
	 * 
	 * @param graph
	 *            Identifies the CodedNodeGraph to merge with.
	 * @return A new CodedNodeGraph representing the merged result.
	 * @throws LBInvocationException,LBParameterException
	 */
	CodedNodeGraph union(CodedNodeGraph graph)
			throws LBInvocationException,LBParameterException;

}