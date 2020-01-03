package org.LexGrid.LexBIG.Extensions.Generic;

import java.util.Iterator;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Generic.NodeGraphResolutionExtension.Direction;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;

/**
 * @author bauerhs
 *
 */
/**
 * @author bauerhs
 *
 */
public interface NodeGraphResolutionExtension extends GenericExtension {
	
	/**
	 * @author bauerhs
	 *CONTAINS: Contains searches will be restricted to trailing edge wild card queries only 
	 * and may be resolved against a set of individual tokenized strings
	 * in an index that will not necessarily return contained substring
	 */
	public enum AlgorithmMatch {
		EXACT_MATCH("exactMatch"), 
		CONTAINS("contains"),
		STARTS_WITH("startsWith"),		
		LUCENE("LuceneQuery");
		
		private String match;
		
		private AlgorithmMatch(String match){
			this.match = match;
		}
		

		
		public String getMatch(){
			return match;
		}
	}
	
	/**
	 * @author bauerhs
	 *
	 */
	public enum ModelMatch {	
		NAME,
		CODE,
		PROPERTY
	}
	
	/**
	 * @author bauerhs
	 *
	 */
	public enum Direction {
	    TARGET_OF("getOutbound"),
	    SOURCE_OF("getInbound");

		private String direction;
		
		private Direction(String direction){
			this.direction = direction;
		}
		

		
		public String getDirection(){
			return direction;
		}
	}

	public static final String GET_INBOUND = "getInbound";
	public static final String GET_OUTBOUND = "getOutbound";
	
	/**
	 * @param url service url for REST service
	 * 
	 * This must be called after constructor to initialize service
	 */
	public void init(String url);
	
	
	/**
	 * @param reference The minimal reference to the coding scheme
	 * @param associationName The relation declaration for this query
	 * @param textMatch Text for Lucene match
	 * @param alg AlgorithmMatch type declaration
	 * @param model ModelMatch type declaration
	 * @param url Service Url for Graph Service
	 * @return Iterator<ConceptReference> This must be implemented as the GraphNodeContentAwareIterator to
	 * take advantage of the ability to get the remaining number of values
	 * 
	 * This 'source of' text based search and resolve hybrid works from a subset of text search results programmatically restricted to 
	 * 10 values.  Result accuracy will require some knowledge of the terminology and search algorithms to get meaningful results.
	 * Model matches against names and codes should return more accurate and meaningful results than matches against properties. 
	 * The resulting iterator does no active paging to the database and may return as many as hundreds of thousands of values at once.  
	 * SourceOf is idiomatic to LexEVS and as such implies incoming edges since for LexEVS the direction of edges is leaf to root. 
	 * Reference, textMatch, alg, and url cannot be null. Null associationNames will trigger an all associations resolution which 
	 * may return many results that are not meaningful to the intent of the user.  Association resolutions are only done so in 
	 * an unbroken chain -- no other association valid for a given matching vertex can be substituted during that resolution.
	 * 
	 */
	public Iterator<ConceptReference> getConceptReferencesForTextSearchAndAssociationSourceOf(
			AbsoluteCodingSchemeVersionReference reference, 
			String associationName, String textMatch, 
			AlgorithmMatch alg, ModelMatch model);

	/**
	 * @param reference The minimal reference to the coding scheme
	 * @param associationName The relation declaration for this query
	 * @param textMatch Text for Lucene match
	 * @param alg AlgorithmMatch type declaration
	 * @param model ModelMatch type declaration
	 * @param url Service Url for Graph Service
	 * @return Iterator<ConceptReference> This must be implemented as the GraphNodeContentAwareIterator to
	 * take advantage of the ability to get the remaining number of values
	 * 
	 * This 'target of' text based search and resolve hybrid works from a subset of text search results programmatically restricted to 
	 * 10 values.  Result accuracy will require some knowledge of the terminology and search algorithms to get meaningful results.
	 * Model matches against names and codes should return more accurate and meaningful results than matches against properties. 
	 * The resulting iterator does no active paging to the database and may return as many as hundreds of thousands of values at once.  
	 * TargetOf is idiomatic to LexEVS and as such implies out going edges since for LexEVS the direction of edges is leaf to root. 
	 * Reference, textMatch, alg, and url cannot be null. Null associationNames will trigger an all associations resolution which 
	 * may return many results that are not meaningful to the intent of the user.  Association resolutions are only done so in 
	 * an unbroken chain -- no other association valid for a given matching vertex can be substituted during that resolution.
	 * 
	 */
	public Iterator<ConceptReference> getConceptReferencesForTextSearchAndAssociationTargetOf(
			AbsoluteCodingSchemeVersionReference reference, String associationName, String textMatch,
			AlgorithmMatch alg, ModelMatch model);
	
	/**
	 * @param int Depth of the graph to resolve 1 = 1 level, n = n levels
	 * @param reference The minimal reference to the coding scheme
	 * @param associationName The relation declaration for this query
	 * @param textMatch Text for Lucene match
	 * @param alg AlgorithmMatch type declaration
	 * @param model ModelMatch type declaration
	 * @param url Service Url for Graph Service
	 * @return Iterator<ConceptReference> This must be implemented as the GraphNodeContentAwareIterator to
	 * take advantage of the ability to get the remaining number of values
	 * 
	 * This 'source of' text based search and resolve hybrid works from a subset of text search results programmatically restricted to 
	 * 10 values.  Result accuracy will require some knowledge of the terminology and search algorithms to get meaningful results.
	 * Model matches against names and codes should return more accurate and meaningful results than matches against properties. 
	 * The resulting iterator does no active paging to the database and may return as many as hundreds of thousands of values at once.  
	 * SourceOf is idiomatic to LexEVS and as such implies incoming edges since for LexEVS the direction of edges is leaf to root. 
	 * Reference, textMatch, alg, and url cannot be null. Null associationNames will trigger an all associations resolution which 
	 * may return many results that are not meaningful to the intent of the user.  Association resolutions are only done so in 
	 * an unbroken chain -- no other association valid for a given matching vertex can be substituted during that resolution.
	 * 
	 */
	public Iterator<ConceptReference> getConceptReferencesForTextSearchAndAssociationSourceOf( int depth,
			AbsoluteCodingSchemeVersionReference reference, 
			String associationName, String textMatch, 
			AlgorithmMatch alg, ModelMatch model);

	/**
	 * @param int Depth of the graph to resolve 1 = 1 level, n = n levels
	 * @param reference The minimal reference to the coding scheme
	 * @param associationName The relation declaration for this query
	 * @param textMatch Text for Lucene match
	 * @param alg AlgorithmMatch type declaration
	 * @param model ModelMatch type declaration
	 * @param url Service Url for Graph Service
	 * @return Iterator<ConceptReference> This must be implemented as the GraphNodeContentAwareIterator to
	 * take advantage of the ability to get the remaining number of values
	 * 
	 * This 'target of' text based search and resolve hybrid works from a subset of text search results programmatically restricted to 
	 * 10 values.  Result accuracy will require some knowledge of the terminology and search algorithms to get meaningful results.
	 * Model matches against names and codes should return more accurate and meaningful results than matches against properties. 
	 * The resulting iterator does no active paging to the database and may return as many as hundreds of thousands of values at once.  
	 * TargetOf is idiomatic to LexEVS and as such implies out going edges since for LexEVS the direction of edges is leaf to root. 
	 * Reference, textMatch, alg, and url cannot be null. Null associationNames will trigger an all associations resolution which 
	 * may return many results that are not meaningful to the intent of the user.  Association resolutions are only done so in 
	 * an unbroken chain -- no other association valid for a given matching vertex can be substituted during that resolution.
	 * 
	 */
	public Iterator<ConceptReference> getConceptReferencesForTextSearchAndAssociationTargetOf(int depth,
			AbsoluteCodingSchemeVersionReference reference, String associationName, String textMatch,
			AlgorithmMatch alg, ModelMatch model);
	
	
	/**
	 * @param int Depth of the graph to resolve 1 = 1 level, n = n levels
	 * @param reference The minimal reference to the coding scheme
	 * @param associationName The relation declaration for this query
	 * @param textMatch Text for Lucene match
	 * @param alg AlgorithmMatch type declaration
	 * @param model ModelMatch type declaration
	 * @param url Service Url for Graph Service
	 * @return Iterator<ConceptReference> This must be implemented as the GraphNodeContentAwareIterator to
	 * take advantage of the ability to get the remaining number of values
	 * 
	 * This 'source of' text based search and resolve hybrid works from a subset of text search results programmatically restricted to 
	 * 10 values.  Result accuracy will require some knowledge of the terminology and search algorithms to get meaningful results.
	 * Model matches against names and codes should return more accurate and meaningful results than matches against properties. 
	 * The resulting iterator does no active paging to the database and may return as many as hundreds of thousands of values at once.  
	 * SourceOf is idiomatic to LexEVS and as such implies incoming edges since for LexEVS the direction of edges is leaf to root. 
	 * Reference, textMatch, alg, and url cannot be null. Null associationNames will trigger an all associations resolution which 
	 * may return many results that are not meaningful to the intent of the user.  Association resolutions are only done so in 
	 * an unbroken chain -- no other association valid for a given matching vertex can be substituted during that resolution.
	 * 
	 */
	public Iterator<ConceptReference> getConceptReferencesForTextSearchAndAssociationSourceOf( int depth,
			AbsoluteCodingSchemeVersionReference reference, 
			String associationName, String textMatch, 
			AlgorithmMatch alg, ModelMatch model, LocalNameList sources, NameAndValueList qualifiers);

	/**
	 * @param int Depth of the graph to resolve 1 = 1 level, n = n levels
	 * @param reference The minimal reference to the coding scheme
	 * @param associationName The relation declaration for this query
	 * @param textMatch Text for Lucene match
	 * @param alg AlgorithmMatch type declaration
	 * @param model ModelMatch type declaration
	 * @param url Service Url for Graph Service
	 * @return Iterator<ConceptReference> This must be implemented as the GraphNodeContentAwareIterator to
	 * take advantage of the ability to get the remaining number of values
	 * 
	 * This 'target of' text based search and resolve hybrid works from a subset of text search results programmatically restricted to 
	 * 10 values.  Result accuracy will require some knowledge of the terminology and search algorithms to get meaningful results.
	 * Model matches against names and codes should return more accurate and meaningful results than matches against properties. 
	 * The resulting iterator does no active paging to the database and may return as many as hundreds of thousands of values at once.  
	 * TargetOf is idiomatic to LexEVS and as such implies out going edges since for LexEVS the direction of edges is leaf to root. 
	 * Reference, textMatch, alg, and url cannot be null. Null associationNames will trigger an all associations resolution which 
	 * may return many results that are not meaningful to the intent of the user.  Association resolutions are only done so in 
	 * an unbroken chain -- no other association valid for a given matching vertex can be substituted during that resolution.
	 * 
	 */
	public Iterator<ConceptReference> getConceptReferencesForTextSearchAndAssociationTargetOf(int depth,
			AbsoluteCodingSchemeVersionReference reference, String associationName, String textMatch,
			AlgorithmMatch alg, ModelMatch model, LocalNameList sources, NameAndValueList qualifiers);
	
	/**
	 * @param reference The minimal reference to the coding scheme
	 * @param associationName The relation declaration for this query
	 * @param textMatch Text for Lucene match
	 * @param alg AlgorithmMatch type declaration
	 * @param model ModelMatch type declaration
	 * @param url Service Url for Graph Service
	 * @return List<ResolvedConceptReference> References with a human readable name to display to end users
	 * 
	 * This method is intended to create an end user choice list since it contains a set of human readable values.  Based on this choice 
	 * a follow up call to the REST client service in getConceptReferenceListResolvedFromGraphForEntityCode() should return a more meaningful
	 * set of results to the end user.  This is restricted to a single association name further narrowing the meaning of any intended searches.
	 * This set of results is limited to 10 values to insure the most meaningful results are returned to the end user.  Search Algorithms and model matches are 
	 * consistent with the restrictions on properties, codes, and designations which are used in the Coded Node Set interface. No null value parameters are allowed.
	 * 
	 */
	public List<ResolvedConceptReference> getCandidateConceptReferencesForTextAndAssociation(AbsoluteCodingSchemeVersionReference reference, String associationName, String textMatch,
			AlgorithmMatch alg, ModelMatch model, LocalNameList sources, NameAndValueList qualifiers);
	
	
	/**
	 * @param reference The minimal reference to the coding scheme
	 * @param associationName The relation declaration for this query
	 * @param textMatch Text for Lucene match
	 * @param alg AlgorithmMatch type declaration
	 * @param model ModelMatch type declaration
	 * @param url Service Url for Graph Service
	 * @return List<ResolvedConceptReference> References with a human readable name to display to end users
	 * 
	 * This method is intended to create an end user choice list since it contains a set of human readable values.  Based on this choice 
	 * a follow up call to the REST client service in getConceptReferenceListResolvedFromGraphForEntityCode() should return a more meaningful
	 * set of results to the end user.  This is restricted to a single association name further narrowing the meaning of any intended searches.
	 * This set of results is limited to 10 values to insure the most meaningful results are returned to the end user.  Search Algorithms and model matches are 
	 * consistent with the restrictions on properties, codes, and designations which are used in the Coded Node Set interface. No null value parameters are allowed.
	 * 
	 */
	public List<ResolvedConceptReference> getCandidateConceptReferencesForTextAndAssociation(AbsoluteCodingSchemeVersionReference reference, String associationName, String textMatch,
			AlgorithmMatch alg, ModelMatch model);
	
	/**
	 * @param reference The minimal reference to the coding scheme
	 * @param associationName The relation declaration for this query
	 * @param direction target or source indicating in or out bound edges
	 * @param entityCode Unique id used to define start vertex for graph
	 * @param url Service Url for Graph Service
	 * @return List<ConceptReference> Minimal reference representation to be used to resolve values later
	 * 
	 * This method returns values from a complete resolution of a graph based on the association name and an entity code that designations the starting vertex.
	 * The resolution will be done on incoming edges for a Direction of SOURCE_OF and out going edges for a Direction of TARGET_OF.  No parameter values can
	 * can be null.  
	 */
	public List<ConceptReference> getConceptReferenceListResolvedFromGraphForEntityCode(AbsoluteCodingSchemeVersionReference reference, String associationName, Direction direction, String entityCode);
	

	/**
	 * @param reference The minimal reference to the coding scheme
	 * @param int Depth of the graph to resolve 1 = 1 level, n = n levels
	 * @param associationName The relation declaration for this query
	 * @param direction target or source indicating in or out bound edges
	 * @param entityCode Unique id used to define start vertex for graph
	 * @param url Service Url for Graph Service
	 * @return List<ConceptReference> Minimal reference representation to be used to resolve values later
	 * 
	 * This method returns values from a complete resolution of a graph based on the association name and an entity code that designations the starting vertex.
	 * The resolution will be done on incoming edges for a Direction of SOURCE_OF and out going edges for a Direction of TARGET_OF.  No parameter values can
	 * can be null.  
	 */
	public List<ConceptReference> getConceptReferenceListResolvedFromGraphForEntityCode(
			AbsoluteCodingSchemeVersionReference reference, int depth, String associationName, Direction direction,
			String entityCode);
	

//@param reference The minimal reference to the coding scheme
//@param associationName The relation declaration for this query
//@param direction target or source indicating in or out bound edges
//@param entityCode Unique id used to define start vertex for graph
//@param url Service Url for Graph Service
//@return GraphNodeContentTrackingIterator
//<ResolvedConceptReference> This will be a minimal version containing only the entity code, namespace, and entity description
// 
//
//This method returns values from a complete resolution of a graph based on the association name and an entity code that designates the starting vertex.  The resolution will be done on incoming edges for a Direction of SOURCE_OF and out going edges for a Direction of TARGET_OF.  No parameter values can be null.  Returned resolved concept references will be minimally populated with an entity code, namespace and entity description only.
// 

	public Iterator<ResolvedConceptReference> getResolvedConceptReferenceListResolvedFromGraphForEntityCode(
		AbsoluteCodingSchemeVersionReference reference, String associationName,Direction direction,
		String entityCode);
	
	/**
	 * @return a List of normalized database names in the graph service
	 */
	public List<String> getTerminologyGraphDatabaseList();
	
	/**
	 * @param name This must be a name either retrieved from the graph database list or normalized using the
	 * name normalizing function in this interface
	 * 
	 * @return A list of association names normalized as graph names for the REST service
	 * 
	 * This returns a list of graph names suitable for use in querying the graph database for the named terminology.
	 * These names should be a slightly modified version of the association name in LexEVS
	 */
	public List<String> getGraphsForCodingSchemeName(String name);	
	
	/**
	 * @param ref uri and version for a terminology in the LexEVS service
	 * @return String should reliably return a database name that has been normalized for use with arangos
	 * database naming convention.
	 */
	public String getNormalizedDbNameForTermServiceIdentifiers(AbsoluteCodingSchemeVersionReference ref);


	public List<ResolvedConceptReference> doGetResolvedConceptReferenceListResolvedFromGraphForEntityCode(
			AbsoluteCodingSchemeVersionReference reference, int depth, String associationName, Direction direction,
			String entityCode);

	
	/**
	 * @param cns This CodedNodeSet must have a set of restrictions appropriate for query building and fully ready to be resolved.
	 * @param direction incoming or outgoing edges will have to be designated by one of these Enumerations
	 * @param depth This allows depth control of the query including resolving only neighbors or a full resolution if depth is known
	 * @param association The name of the edge in the graph (will have to be a supported association the code system)
	 * @return List<ResolvedConceptReference> a list of minimally populated concept references including code, namespace, 
	 * entity description and coding scheme uri and version. These objects are the result of a graph resolution without 
	 * any indication of where they existed in the graph before the resolution.
	 * 
	 * This method requires some knowledge of building queries into the LexEVS system's CodedNodeSet API, including the capability
	 * of building a CodedNodeSet set of restrictions through restriction method calls.  Within the scope of this method, 
	 * the CodedNodeSet will be resolved to a ResolvedConceptReferenceList  using the method 
	 * resolveToList(
			SortOptionList sortOptions, LocalNameList propertyNames,
			PropertyType[] propertyTypes, int maxToReturn)
			throws LBInvocationException,LBParameterException;
	* The parameter set will be defaulted to the following: 
	* SortOptionList: null No sort options allowed
	* LocalNameList: null No restrictions on property names
	* PropertyType: null No restrictions on property types
	* int: 10 Maximum return limited to ten entities
	* 
	* Exceptions would be handled in this method and an appropriately messaged RuntimeException would be thrown on failure.
	* 
	* The ResolvedConceptReference objects returned contain only the code, name space, entityDescription, coding scheme URI, and coding scheme version.
	* It will not contain any entities or their properties or targetOf or sourceOf links to other entities.   
	*/
	public List<ResolvedConceptReference> getAssociatedConcepts(CodedNodeSet cns, Direction direction, int depth,  NameAndValueList association);


}
