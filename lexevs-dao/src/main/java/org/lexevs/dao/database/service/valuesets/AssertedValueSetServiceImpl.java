package org.lexevs.dao.database.service.valuesets;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetServices;
import org.lexevs.dao.database.access.valuesets.SourceAssertedValueSetDao;
import org.lexevs.dao.database.access.valuesets.ValueSetHierarchyDao;
import org.lexevs.dao.database.service.AbstractDatabaseService;

public class AssertedValueSetServiceImpl extends AbstractDatabaseService implements AssertedValueSetService {
	
	private SourceAssertedValueSetDao ibatisAssertedValueSetDao;
	
	private ValueSetHierarchyDao ibatisValueSetHierarchyDao;


	
	public  AssertedValueSetService init(){
		ibatisAssertedValueSetDao = this.getDaoManager().getCurrentAssertedValueSetDao();
		ibatisValueSetHierarchyDao = this.getDaoManager().getCurrentValueSetHiearchyDao();
		return this;
	}

	@Override
	public List<CodingScheme> getSourceAssertedValueSetforEntityCode(String matchCode, 
			String assertedRelation, String designation, String designationValue, String version, String codingSchemeURI)
			throws LBException {
		String csUID = this.getDaoManager().getCodingSchemeDao(codingSchemeURI, version).getCodingSchemeUIdByUriAndVersion(codingSchemeURI, version);
		List<String> predUID = this.getDaoManager().getAssociationDao(codingSchemeURI, version).getAssociationPredicateUidsForAssociationName(csUID, null, AssertedValueSetServices.ASSERTED_VALUESET_RELATION);
		List<Entity> entities = ibatisAssertedValueSetDao.getSourceAssertedValueSetEntitiesForEntityCode(matchCode, assertedRelation, predUID.get(0), csUID);
		List<Entity> entity = ibatisAssertedValueSetDao.getSourceAssertedValueSetTopNodeForEntityCode(matchCode, csUID);
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

	/**
	 * @return the ibatisAssertedValueSetDao
	 */
	public SourceAssertedValueSetDao getIbatisAssertedValueSetDao() {
		return ibatisAssertedValueSetDao;
	}

	/**
	 * @param ibatisAssertedValueSetDao the ibatisAssertedValueSetDao to set
	 */
	public void setIbatisAssertedValueSetDao(SourceAssertedValueSetDao ibatisAssertedValueSetDao) {
		this.ibatisAssertedValueSetDao = ibatisAssertedValueSetDao;
	}

	/**
	 * @return the ibatisValueSetHierarchyDao
	 */
	public ValueSetHierarchyDao getIbatisValueSetHierarchyDao() {
		return ibatisValueSetHierarchyDao;
	}

	/**
	 * @param ibatisValueSetHierarchyDao the ibatisValueSetHierarchyDao to set
	 */
	public void setIbatisValueSetHierarchyDao(ValueSetHierarchyDao ibatisValueSetHierarchyDao) {
		this.ibatisValueSetHierarchyDao = ibatisValueSetHierarchyDao;
	}

}
