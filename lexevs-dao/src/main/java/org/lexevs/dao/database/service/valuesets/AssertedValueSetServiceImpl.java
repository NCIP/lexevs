package org.lexevs.dao.database.service.valuesets;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetServices;
import org.lexevs.dao.database.access.valuesets.SourceAssertedValueSetDao;
import org.lexevs.dao.database.service.AbstractDatabaseService;

public class AssertedValueSetServiceImpl extends AbstractDatabaseService implements AssertedValueSetService {
	SourceAssertedValueSetDao vsDao;

	public AssertedValueSetServiceImpl() {
		vsDao = getDaoManager().getCurrentAssertedValueSetDao();
	}

	@Override
	public List<CodingScheme> getSourceAssertedValueSetforEntityCode(String matchCode, 
			String assertedRelation, String designation, String designationValue, String version, String codingSchemeURI)
			throws LBException {
		List<Entity> entities = vsDao.getSourceAssertedValueSetEntitiesForEntityCode(matchCode, assertedRelation);
		List<Entity> entity = vsDao.getSourceAssertedValueSetTopNodeForEntityCode(matchCode, designation, designationValue);
		CodingScheme scheme = transformToCodingScheme(entity, entities, version, codingSchemeURI);
		List<CodingScheme> schemes = new ArrayList<CodingScheme>();
		schemes.add(scheme);
		return schemes;
	}

	private CodingScheme transformToCodingScheme(List<Entity> entity, List<Entity> entities, String version, String codingSchemeURI) throws LBException {
		Entities list = new Entities();
		list.getEntityAsReference().addAll(entities);
		return AssertedValueSetServices.transform(entity.iterator().next(), null, null, list, version, AssertedValueSetServices.DEFAULT_CODINGSCHEME_URI);
	}

}
