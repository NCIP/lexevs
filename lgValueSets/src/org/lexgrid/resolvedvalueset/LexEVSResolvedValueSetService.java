package org.lexgrid.resolvedvalueset;

import java.net.URI;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.codingSchemes.CodingScheme;

public interface LexEVSResolvedValueSetService {
       
   	public List<CodingScheme> listAllResolvedValueSets() throws LBException;
	public List<CodingScheme> getResolvedValueSetsForConceptReference(ConceptReference ref);
	public CodingScheme getResolvedValueSetForValueSetURI(URI uri);
	public ResolvedConceptReferenceList getValueSetEntitiesForURI(String uri);
	
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
