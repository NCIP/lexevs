/*
* Copyright: (c) 2004-2013 Mayo Foundation for Medical Education and
* Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
* triple-shield Mayo logo are trademarks and service marks of MFMER.
*
* Except as contained in the copyright notice above, or as used to identify
* MFMER as the author of this software, the trade names, trademarks, service
* marks, or product names of the copyright holder shall not be used in
* advertising, promotion or otherwise in connection with this software without
* prior written authorization of the copyright holder.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.LexGrid.LexBIG.Extensions.Generic;

import java.util.Set;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;

/**
 * A simplified Search Extension.
 * 
 * Query syntax is described by the
 * {@link http://lucene.apache.org/core/old_versioned_docs/versions/2_9_1/queryparsersyntax.html Lucene Query Syntax}
 */
public interface SearchExtension extends GenericExtension {
	
	public enum MatchAlgorithm {
		PRESENTATION_EXACT, 
		PRESENTATION_CONTAINS,
		PROPERTY_EXACT,
		PROPERTY_CONTAINS,
		CODE_EXACT,		
		LUCENE
	}
	
	/**
	 * Search based on a given text string over all coding schemes.
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
     * Search based on a given text string over given coding schemes.
	 *
	 * @param text
	 * 			The search text
	 * @param codingSchemes
	 * 			The coding schemes to include in the search
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
			Set<CodingSchemeReference> codingSchemes,
			MatchAlgorithm matchAlgorithm) throws LBParameterException;
	
	/**
	 * Search based on a given text string over given coding schemes, excluding
	 * the listed.
	 * 
	 * NOTE: If a coding scheme appears in both codingSchemesToInclude 
	 * and codingSchemesToExclude, the exclude will be given priority.
	 *
	 * @param text
	 * 			The search text
	 * @param codingSchemesToInclude
	 * 			The coding schemes to include in the search
	 * @param codingSchemesToExclude
	 * 			The coding schemes to include in the search
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
			Set<CodingSchemeReference> codingSchemesToInclude,
			Set<CodingSchemeReference> codingSchemesToExclude,
			MatchAlgorithm matchAlgorithm) throws LBParameterException;
	
	/**
	 * Search based on a given text string over given coding schemes, excluding
	 * the listed.
	 * 
	 * NOTE: If a coding scheme appears in both codingSchemesToInclude 
	 * and codingSchemesToExclude, the exclude will be given priority.
	 *
	 * @param text
	 * 			The search text
	 * @param codingSchemesToInclude
	 * 			The coding schemes to include in the search
	 * @param codingSchemesToExclude
	 * 			The coding schemes to include in the search
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
			Set<CodingSchemeReference> codingSchemesToInclude,
			Set<CodingSchemeReference> codingSchemesToExclude,
			MatchAlgorithm matchAlgorithm,
			boolean includeAnonymous) throws LBParameterException;
	
	
	/**
	 * Search based on a given text string over given coding schemes, excluding
	 * the listed.
	 * 
	 * NOTE: If a coding scheme appears in both codingSchemesToInclude 
	 * and codingSchemesToExclude, the exclude will be given priority.
	 *
	 * @param text
	 * 			The search text
	 * @param codingSchemesToInclude
	 * 			The coding schemes to include in the search
	 * @param codingSchemesToExclude
	 * 			The coding schemes to include in the search
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
			Set<CodingSchemeReference> codingSchemesToInclude,
			Set<CodingSchemeReference> codingSchemesToExclude,
			MatchAlgorithm matchAlgorithm,
			boolean includeAnonymous,
			boolean includeInactive) throws LBParameterException;
}
