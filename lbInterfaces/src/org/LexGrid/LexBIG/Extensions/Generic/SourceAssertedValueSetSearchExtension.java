
package org.LexGrid.LexBIG.Extensions.Generic;

import java.io.IOException;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Generic.SearchExtension.MatchAlgorithm;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;

public interface SourceAssertedValueSetSearchExtension extends SearchExtension {

/**
     * Search based on a given text string over a coding schemes source
     * asserted value sets.  Returns and empty iterator if none exist for
     * this scheme
	 *
	 * @param text
	 * 			The search text
	 * @param codingScheme
	 * 			The coding scheme reference of the asserted value sets
	 * 			to include in the search
	 * @param matchAlgorithm
	 * 			The match algorithm to use for matching
	 * @return 
	 * 			A ResolvedConceptReferencesIterator
	 * 
	 * @throws LBParameterException
	 * @throws IOException 
	 */
public ResolvedConceptReferencesIterator search(
			String text, 
			CodingSchemeReference  codingScheme,
			MatchAlgorithm matchAlgorithm) throws LBParameterException;
	
	/**
	 * Search that defaults to the only or first source asserted value set in the system.
	 * Order is not guaranteed.
	 *
	 * @param text
	 * 			The search text
	 * @param matchAlgorithm
	 * 			The match algorithm to use for matching
	 * @return
	 * 			A ResolvedConceptReferencesIterator
	 * 
	 * @throws LBParameterException
	 * @throws IOException 
	 */
	public ResolvedConceptReferencesIterator search(String text, MatchAlgorithm matchAlgorithm) throws LBParameterException;

	/**
     * Search based on a given text string over given resolved value sets.
     * Automatically includes any value sets indexed as source asserted.
	 *
	 * @param text
	 * 			The search text
	 * @param resovledValueSets
	 * 			The resolvedValueSets to include in the search
	 * @param matchAlgorithm
	 * 			The match algorithm to use for matching
	 * @return 
	 * 			A ResolvedConceptReferencesIterator
	 * 
	 * @throws LBParameterException
	 * @throws IOException 
	 */
	public ResolvedConceptReferencesIterator search(
			String text, 
			Set<CodingSchemeReference> resolvedValueSet,
			MatchAlgorithm matchAlgorithm) throws LBParameterException;
	
	/**
	 * Search based on a given text string over given source asserted and 
	 * resolved value sets.
	 * 
	 * NOTE: If a coding scheme appears in both codingSchemesToInclude 
	 * and codingSchemesToExclude, the exclude will be given priority.
	 *
	 * @param text
	 * 			The search text
	 * @param sourceAssertedValueSetSchemeReferences
	 * 			The coding scheme references for asserted value sets
	 *   		to include in the search
	 * @param resolvedValueSets
	 * 			The resolved value sets to include in the search
	 * @param matchAlgorithm
	 * 			The match algorithm to use for matching
	 * @param includeAnonymous
	 * 			Whether or not to include Anonymous Entities
	 * 			Default: 'false'
	 * 			NOTE: 'false' -> include "anonymous != 'true'"
	 *                'true'  -> include all
	 * @return 
	 * 			A ResolvedConceptReferencesIterator
	 * 
	 * @throws LBParameterException
	 * @throws IOException 
	 */
	public ResolvedConceptReferencesIterator search(
			String text, 
			Set<CodingSchemeReference> sourceAssertedValueSetSchemeReferences,
			Set<CodingSchemeReference> resolvedValueSets,
			MatchAlgorithm matchAlgorithm,
			boolean includeAnonymous) throws LBParameterException;
	
	
	/**
	 * Search based on a given text string over given source asserted and 
	 * resolved value sets.
	 * .
	 *
	 * @param text
	 * 			The search text
	 * @param sourceAssertedValueSetSchemeReferences
	 * 			The coding scheme references for asserted value sets
	 *   		to include in the search
	 * @param resolvedValueSets
	 * 			The resolved value sets to include in the search
	 * @param matchAlgorithm
	 * 			The match algorithm to use for matching
	 * @param includeAnonymous
	 * 			Whether or not to include Anonymous Entities
	 * 			Default: 'false'
	 * 			NOTE: 'false' -> include "anonymous != 'true'"
	 *                'true'  -> include all
	 * 
	 * @param includeInactive
	 * 			Whether or not to include Inactive Entities
	 * 			Default: 'false'
	 * 			NOTE: 'false' -> include "active != 'true'"
	 *                'true'  -> include all   
	 * @return 
	 * 			A ResolvedConceptReferencesIterator
	 * 
	 * @throws LBParameterException
	 * @throws IOException 
	 */
	public ResolvedConceptReferencesIterator search(
			String text, 
			Set<CodingSchemeReference> sourceAssertedValueSetSchemeReferences,
			Set<CodingSchemeReference> resolvedValueSets,
			MatchAlgorithm matchAlgorithm,
			boolean includeAnonymous,
			boolean includeInactive) throws LBParameterException;
}