
package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions;
import java.util.HashSet;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Extensions.Generic.CodingSchemeReference;
import org.LexGrid.LexBIG.Extensions.Generic.SearchExtension;
import org.LexGrid.LexBIG.Extensions.Generic.SearchExtension.MatchAlgorithm;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.util.PrintUtility;

/*
 * Helper class for testing SearchExtension ranking via the NCI Thesaurus.
 * 
 * This is NOT part of the automated test suite and is only run as needed.
 */
public class SearchExtensionRankTest {

	public static void main(String[] args) throws Exception {
		SearchExtension se = (SearchExtension) LexBIGServiceImpl.defaultInstance().getGenericExtension("SearchExtension");
	
		final CodingSchemeReference ref = new CodingSchemeReference();
		ref.setCodingScheme("NCI_Thesaurus");
		
		Set<CodingSchemeReference> includes = 
			new HashSet<CodingSchemeReference>() {{ add(ref); }};
			
		long time = System.currentTimeMillis();
		ResolvedConceptReferencesIterator results = 
			se.search("cell", includes, MatchAlgorithm.PRESENTATION_CONTAINS);
		
		System.out.println(results.numberRemaining());

		for(ResolvedConceptReference r : results.next(50).getResolvedConceptReference()){
			PrintUtility.print(r);
		}
		System.out.println(System.currentTimeMillis() - time);
	}
}