
package org.lexgrid.valuesets.sourceasserted;

import java.io.Serializable;
import java.net.URI;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.SearchExtension.MatchAlgorithm;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;

public interface SourceAssertedValueSetService extends Serializable{

/**
   	 * Returns a list of coding scheme representations of value sets.  Includes
   	 * entities for value sets.  
   	 * 
   	 * @return List<CodingScheme> - List of value sets as coding schemes
   	 * @throws LBException
	 */
public List<CodingScheme> listAllSourceAssertedValueSets() throws LBException;
	/**
   	 * Returns a fully resolved coding scheme representation of a value set.  Includes
   	 * entities for the value set.  
   	 * 
   	 * @String description - Description to match exactly.
   	 * @return CodingScheme - fully resolved coding scheme representation of value set 
   	 * @throws LBException
	 */
	public CodingScheme listResolvedValueSetForDescription(String description) throws LBException;
   	/**
   	 * Returns a minimal (i.e. no entities included) coding scheme object list
   	 * 
   	 * @return List<CodingScheme> Minimal coding scheme representation of value sets
   	 * @throws LBException
   	 */
   	public List<CodingScheme> getMinimalSourceAssertedValueSetSchemes() throws LBException;
	/**
   	 * Returns a list of Coding Scheme representations of value sets for an entity represented
   	 * as a ConceptReference
   	 * 
   	 * @param ConceptReference ref: Requires a coding scheme reference and unique entity id
   	 * @return List<CodingScheme> List of coding scheme representations of value sets
	 */
	public List<CodingScheme> getSourceAssertedValueSetsForConceptReference(ConceptReference ref);
	/**
	 * Gets the coding scheme representation of a value set for the unique uri of the value set
	 * 
	 * @param URI uri - Representation of URI coding scheme identifier
	 * @return CodingScheme - Coding Scheme representation of value set
	 */
	public CodingScheme getSourceAssertedValueSetForValueSetURI(URI uri) throws LBException;

	/**
	 * Returns a list of ResolvedConceptReference representations of
	 * the entities belonging to the CodingScheme representation of 
	 * this value set identified by its uri designation as a String
	 * 
	 * @param String uri - URI string representation of coding scheme identifier
	 * @return ResolvedConceptReferenceList - List of resolved concept references
	 */
	public ResolvedConceptReferenceList getSourceAssertedValueSetEntitiesForURI(String uri);
	/**
	 * Returns an iterator over ResolvedConceptReference representations of
	 * the entities belonging to the CodingScheme representation of 
	 * this value set identified by its uri designation as a String
	 * 
	 * @param String uri URI string representation of coding scheme identifier
	 * @return ResolvedConceptReferencesIterator - iterator over resolved concepts
	 */
	public ResolvedConceptReferencesIterator getSourceAssertedValueSetIteratorForURI(String uri);
	/**
	 * Returns a list of Coding Scheme representations of a value set
	 * with the code that has the class instance defined relationship to this value set
	 * top node
	 * @param String matchCode - value set top node reference
	 * @return List<CodingScheme> - Will rarely return a list
	 * @throws LBException
	 */
	public List<CodingScheme> getSourceAssertedValueSetforTopNodeEntityCode(String matchCode) throws LBException;
	/**
	 * Returns a list of very minimal references to a coding scheme representation
	 * of a value set containing an entity with a similar or exact textual representation
	 * of the matchText parameter.
	 * Searches on text using contains or exact match for a variety of property types
	 * See MatchAlgorthm class for details. 
	 * @param String matchText - Text to match
	 * @param MatchAlgorithm matchType - Property type and algorithm match
	 * @return List<AbsoluteCodingSchemeVersionReference> - list of codingScheme and
	 *         version used for the resolution of the resolvedValueSet
	 * @throws LBException
	 */
	public List<AbsoluteCodingSchemeVersionReference> getSourceAssertedValueSetsforTextSearch(String matchText, MatchAlgorithm matchType) throws LBException;
	
	/**
	 * Return a list of AbsoluteCodingSchemeVersionReferences that were used for resolving the resolvedValueSet
	 * 
	 * @param codingScheme - The resolvedValueSet CodingScheme
	 * @return AbsoluteCodingSchemeVersionReferenceList - list of codingScheme and
	 *         version used for the resolution of the resolvedValueSet
	 * @throws LBException
	 */
	public AbsoluteCodingSchemeVersionReferenceList getListOfCodingSchemeVersionsUsedInResolution(CodingScheme cs);
	/**
	 * Assumes a hierarchy of value sets is divided amongst a set of root nodes that have a 
	 * relationship to this rootCode parameter which is a unique entity identifier in the system.
	 * 
	 * @param rootCode - Unique identifier for the top asserted value set root
	 * @return List<String> - Entity code unique identifier list
	 */
	public List<String> getSourceAssertedValueSetTopNodesForRootCode(String rootCode);
	/**
	 * @return List<? extends Entity> - A complete list of all entities in the source asserted
	 * representation
	 */
	public List<? extends Entity> getAllSourceAssertedValueSetEntities();

	/**
	 * Returns a list of coding scheme representations
	 * of a value set containing entities with this entity code.
	 * Searches on exact match of an entity code
	 * @param String matchCode - Exact match for value set top node identifier
	 * @return List<CodingScheme> - List of coding scheme representations of value sets
	 * @throws LBException
	 */
	public List<CodingScheme> getSourceAssertedValueSetforValueSetMemberEntityCode(String matchCode) throws LBException;

	
	
		

}