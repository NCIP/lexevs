package org.lexgrid.resolvedvalueset;

import java.net.URI;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.codingSchemes.CodingScheme;

public interface LexEVSResolvedValueSetService {
       
   	public List<CodingScheme> listAllResolvedValueSets() throws LBException;
	public List<CodingScheme> getResolvedValueSetsForConceptReference(ConceptReference ref);
	public CodingScheme getCodingSchemeMetaDataForValueSetURI(URI uri);
	
}
