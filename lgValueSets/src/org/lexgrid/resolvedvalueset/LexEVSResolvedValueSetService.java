package org.lexgrid.resolvedvalueset;

import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.codingSchemes.CodingScheme;

public interface LexEVSResolvedValueSetService {
       
   	public List<CodingScheme> listAllResolvedValueSets() throws LBException;
}
