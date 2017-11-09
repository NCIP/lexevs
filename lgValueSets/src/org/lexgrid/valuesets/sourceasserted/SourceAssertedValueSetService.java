package org.lexgrid.valuesets.sourceasserted;

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

public interface SourceAssertedValueSetService {
	
			
	
	public List<CodingScheme> listAllSourceAssertedValueSets() throws LBException;
   	public List<CodingScheme> getMinimalSourceAssertedValueSetSchemes() throws LBException;
	public List<CodingScheme> getSourceAssertedValueSetsForConceptReference(ConceptReference ref);
	public CodingScheme getSourceAssertedValueSetForValueSetURI(URI uri) throws LBException;
	public ResolvedConceptReferenceList getSourceAssertedValueSetEntitiesForURI(String uri);
	public ResolvedConceptReferencesIterator getSourceAssertedValueSetIteratorForURI(String uri);
	public List<CodingScheme> getSourceAssertedValueSetforEntityCode(String matchCode, String assertedRelation) throws LBException;
	public List<AbsoluteCodingSchemeVersionReference> getSourceAssertedValueSetsforTextSearch(String matchText, MatchAlgorithm matchType) throws LBException;
	
	/**
	 * Return a list of AbsoluteCodingSchemeVersionReference that was used for resolving the resolvedValueSet
	 * 
	 * @param codingScheme- The resolvedValueSet CodingScheme
	 * @return AbsoluteCodingSchemeVersionReferenceList list of codingScheme and
	 *         version used for the resolution of the resolvedValueSet
	 * @throws LBException
	 */
	public AbsoluteCodingSchemeVersionReferenceList getListOfCodingSchemeVersionsUsedInResolution(CodingScheme cs);
		

}
