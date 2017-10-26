package org.lexevs.dao.database.service.valuesets;

import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.codingSchemes.CodingScheme;

public interface  AssertedValueSetService {
	
	public List<CodingScheme> getSourceAssertedValueSetforEntityCode(
			String matchCode, String assertedRelation, 
			String designation, String designationValue,
			String version, String codingSchemeURI
			) throws LBException;

}
