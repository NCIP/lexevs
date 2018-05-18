package org.lexgrid.resolvedvalueset;

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
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;


public interface LexEVSResolvedValueSetService extends Serializable {
      
	/**
	 * This parameter set is required for asserted value set services
	 * 
	 * @param params - extensive asserted value set source parameter defintion
	 */
	public void initParams(AssertedValueSetParameters params);
	
   	/**
   	 * Returns a list of coding scheme representations of value sets.  Includes
   	 * entities for value sets.  
   	 * 
   	 * @return List<CodingScheme> - coding scheme representations of value set list
   	 * @throws LBException
   	 */
   	public List<CodingScheme> listAllResolvedValueSets() throws LBException;
   	
   	/**
   	 * Returns a minimal (i.e. no entities included) coding scheme object list
   	 * 
   	 * @return List<CodingScheme> - coding scheme representation of value set list
   	 * @throws LBException
   	 */
   	public List<CodingScheme> getMinimalResolvedValueSetSchemes() throws LBException;
	
   	/**
   	 * Returns a list of Coding Scheme representations of value sets for an entity represented
   	 * as a ConceptReference
   	 * 
   	 * @param ConceptReference - Requires a coding scheme reference and unique entity id
   	 * @return List<CodingScheme> List of resolved value sets as coding schemes
   	 */
   	public List<CodingScheme> getResolvedValueSetsForConceptReference(ConceptReference ref);

   	/**
	 * Gets the coding scheme representation of a value set for the unique uri of the value set
	 * 
	 * @param URI - requires Uri to be constructed as canonical Java URI.  
	 * @return CodingScheme - Coding scheme representation of a value set
	 */
	public CodingScheme getResolvedValueSetForValueSetURI(URI uri);

	/**
	 * Returns a list of ResolvedConceptReference representations of
	 * the entities belonging to the CodingScheme representation of 
	 * this value set identified by its uri designation as a String
	 * 
	 * @param String uri - String representation of coding scheme uri
	 * @return ResolvedConceptReferenceList - Concept reference list of value set members
	 */
	public ResolvedConceptReferenceList getValueSetEntitiesForURI(String uri);

	/**
	 * Returns an iterator over ResolvedConceptReference representations of
	 * the entities belonging to the CodingScheme representation of 
	 * this value set identified by its uri designation as a String
	 * 
	 * @param String uri - unique coding scheme identifier for value set representation
	 * @return ResolvedConceptReferencesIterator - iterator over value set members
	 */
	public ResolvedConceptReferencesIterator getValueSetIteratorForURI(String uri);

	/**
	 * Returns a list of very minimal references to a coding scheme representation
	 * of a value sets containing entities with this entity code.
	 * 
	 * Searches on exact match of an entity code
	 * @param String matchCode - unique identifier for entity code match of value set member
	 * @return List<AbsoluteCodingSchemeVersionReference> - list of minimal coding scheme references
	 * @throws LBException
	 */
	public List<AbsoluteCodingSchemeVersionReference> getResolvedValueSetsforEntityCode(String matchCode) throws LBException;

	/**
	 * Returns a list of very minimal references to a coding scheme representation
	 * of a value set containing an entity with a similar or exact textual representation
	 * of the matchText parameter.
	 * Searches on text using contains or exact match for a variety of property types
	 * See MatchAlgorthm class for reference. 
	 * 
	 * @param String matchText - Text to match against designated property/alorithm
	 * @param MatchAlgorithm matchType - Property and algorithm match type
	 * @return List<AbsoluteCodingSchemeVersionReference> - list of minimal coding scheme references
	 * @throws LBException
	 */
	public List<AbsoluteCodingSchemeVersionReference> getResolvedValueSetsforTextSearch(String matchText, MatchAlgorithm matchType) throws LBException;
	
	/**
	 * Return a list of AbsoluteCodingSchemeVersionReferences used for resolving the resolvedValueSet
	 * 
	 * @param CodingScheme codingScheme - The resolvedValueSet CodingScheme
	 * @return AbsoluteCodingSchemeVersionReferenceList - list of codingScheme and
	 *         version pairs used for the resolution of the resolvedValueSet
	 * @throws LBException
	 */
	public AbsoluteCodingSchemeVersionReferenceList getListOfCodingSchemeVersionsUsedInResolution(CodingScheme cs);

	/**
	 * Use a parameter set to determine whether this terminology service contains the source asserted
	 * value set source terminology defined in the parameter set. Null or a AssertedValueSetParameters 
	 * build with no arguments or changes returns default NCI Thesaurus defined values
	 * 
	 * @param AssertedValueSetParameters params - Large asserted value set parameter set with well established defaults
	 * @return Boolean - flag indicating presence of Asserted Value Set Terminology
	 */
	public Boolean doesServiceContainAssertedValueSetTerminology(AssertedValueSetParameters params);
		
	
}
