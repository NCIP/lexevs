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

/**
 * @author m029206
 *
 */
public interface LexEVSResolvedValueSetService extends Serializable {
      
	/**
	 * This parameter set is required for asserted value set services
	 * 
	 * @param params
	 */
	public void initParams(AssertedValueSetParameters params);
	
   	/**
   	 * Returns a list of coding scheme representations of value sets.  Includes
   	 * entities for value sets.  
   	 * 
   	 * @return List<CodingScheme>
   	 * @throws LBException
   	 */
   	public List<CodingScheme> listAllResolvedValueSets() throws LBException;
   	
   	/**
   	 * Returns a minimal (i.e. no entities included) coding scheme object list
   	 * 
   	 * @return List<CodingScheme>
   	 * @throws LBException
   	 */
   	public List<CodingScheme> getMinimalResolvedValueSetSchemes() throws LBException;
	
   	/**
   	 * Returns a list of Coding Scheme representations of value sets for an entity represented
   	 * as a ConceptReference
   	 * 
   	 * @param ConceptReference ref: Requires a coding scheme reference and unique entity id
   	 * @return List<CodingScheme>
   	 */
   	public List<CodingScheme> getResolvedValueSetsForConceptReference(ConceptReference ref);

   	/**
	 * Gets the coding scheme representation of a value set for the unique uri of the value set
	 * 
	 * @param URI uri
	 * @return CodingScheme
	 */
	public CodingScheme getResolvedValueSetForValueSetURI(URI uri);

	/**
	 * Returns a list of ResolvedConceptReference representations of
	 * the entities belonging to the CodingScheme representation of 
	 * this value set identified by its uri designation as a String
	 * 
	 * @param String uri
	 * @return ResolvedConceptReferenceList
	 */
	public ResolvedConceptReferenceList getValueSetEntitiesForURI(String uri);

	/**
	 * Returns an iterator over ResolvedConceptReference representations of
	 * the entities belonging to the CodingScheme representation of 
	 * this value set identified by its uri designation as a String
	 * 
	 * @param String uri
	 * @return ResolvedConceptReferencesIterator
	 */
	public ResolvedConceptReferencesIterator getValueSetIteratorForURI(String uri);

	/**
	 * Returns a list of very minimal references to a coding scheme representation
	 * of a value set containing entities with this entity code.
	 * Searches on exact match of an entity code
	 * @param String matchCode
	 * @return List<AbsoluteCodingSchemeVersionReference>
	 * @throws LBException
	 */
	public List<AbsoluteCodingSchemeVersionReference> getResolvedValueSetsforEntityCode(String matchCode) throws LBException;

	/**
	 * Returns a list of very minimal references to a coding scheme representation
	 * of a value set containing an entity with a similar or exact textual representation
	 * of the matchText parameter.
	 * Searches on text using contains or exact match for a variety of property types
	 * See MatchAlgorthm class for details. 
	 * @param String matchText
	 * @param MatchAlgorithm matchType
	 * @return List<AbsoluteCodingSchemeVersionReference>
	 * @throws LBException
	 */
	public List<AbsoluteCodingSchemeVersionReference> getResolvedValueSetsforTextSearch(String matchText, MatchAlgorithm matchType) throws LBException;
	
	/**
	 * Return a list of AbsoluteCodingSchemeVersionReferences used for resolving the resolvedValueSet
	 * 
	 * @param CodingScheme codingScheme- The resolvedValueSet CodingScheme
	 * @return AbsoluteCodingSchemeVersionReferenceList list of codingScheme and
	 *         version used for the resolution of the resolvedValueSet
	 * @throws LBException
	 */
	public AbsoluteCodingSchemeVersionReferenceList getListOfCodingSchemeVersionsUsedInResolution(CodingScheme cs);

	/**
	 * Use a parameter set to determine whether this terminology service contains the source asserted
	 * value set source terminology defined in the parameter set.
	 * @param AssertedValueSetParameters params
	 * @return Boolean
	 */
	public Boolean doesServiceContainAssertedValueSetTerminology(AssertedValueSetParameters params);
		
	
}
