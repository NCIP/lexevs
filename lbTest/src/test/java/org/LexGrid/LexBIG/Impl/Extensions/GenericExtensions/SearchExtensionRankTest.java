package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
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
	
		long time = System.currentTimeMillis();
		ResolvedConceptReferencesIterator results = 
			se.search("mouse melanoma", MatchAlgorithm.PRESENTATION_EXACT);
		
		System.out.println(results.numberRemaining());
		
		for(ResolvedConceptReference r : results.next(50).getResolvedConceptReference()){
			PrintUtility.print(r);
		}
		System.out.println(System.currentTimeMillis() - time);
	}
}
