package org.lexevs.dao.database.service.valuesets;

import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;

public interface  AssertedValueSetService {
	
	public List<CodingScheme> getSourceAssertedValueSetforEntityCode(String matchCode) throws LBException;

	public List<String> getAllValueSetTopNodeCodes(String rootCode);
	
	public List<Entity> getSourceAssertedValueSetEntitiesForEntityCode(String rootCode);
	
	public void init(AssertedValueSetParameters params);

}
