package org.lexevs.dao.database.service.valuesets;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetServices;
import org.lexevs.dao.database.access.association.model.DefinedNode;
import org.lexevs.dao.database.access.association.model.VSHierarchyNode;
import org.lexevs.dao.database.access.entity.EntityDao;
import org.lexevs.dao.database.access.valuesets.SourceAssertedValueSetDao;
import org.lexevs.dao.database.access.valuesets.ValueSetHierarchyDao;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.lexevs.locator.LexEvsServiceLocator;

public class AssertedValueSetServiceImpl extends AbstractDatabaseService implements AssertedValueSetService {
	
	private SourceAssertedValueSetDao ibatisAssertedValueSetDao;
	private EntityDao entityDao;
	private ValueSetHierarchyService valueSetHeirarchyService;
	
	private AssertedValueSetParameters params;

	
	public void init(AssertedValueSetParameters params){
		ibatisAssertedValueSetDao = this.getDaoManager().getCurrentAssertedValueSetDao();
		entityDao = this.getDaoManager().getCurrentEntityDao();
		valueSetHeirarchyService = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getValueSetHierarchyService();
		this.params = params;
	}

	@Override
	public List<CodingScheme> getSourceAssertedValueSetforEntityCode(String matchCode)
			throws LBException {
		if(matchCode == null){throw new RuntimeException("Entity code cannot be null!");}
		String csUID = this.getDaoManager().getCodingSchemeDao(params.getCodingSchemeURI(), params.getCodingSchemeVersion()).
				getCodingSchemeUIdByUriAndVersion(params.getCodingSchemeURI(), params.getCodingSchemeVersion());
		List<String> predUID = this.getDaoManager().getAssociationDao(params.getCodingSchemeURI(), params.getCodingSchemeVersion()).
				getAssociationPredicateUidsForAssociationName(csUID, null, params.getAssertedValueSetRelation());
		List<Entity> entities = ibatisAssertedValueSetDao.getSourceAssertedValueSetEntitiesForEntityCode(matchCode,
				params.getAssertedValueSetRelation(), predUID.get(0), csUID);
		List<Entity> entity = ibatisAssertedValueSetDao.getSourceAssertedValueSetTopNodeForEntityCode(matchCode, csUID);
		CodingScheme scheme = transformToCodingScheme(entity, entities);
		List<CodingScheme> schemes = new ArrayList<CodingScheme>();
		schemes.add(scheme);
		return schemes;
	}
	
	@Override
	public List<String> getAllValueSetTopNodeCodes(String rootCode){
		if(rootCode == null){throw new RuntimeException("Root value set code cannot be null!");}
		valueSetHeirarchyService.preprocessSourceHierarchyData(params.getCodingSchemeURI(), params.getCodingSchemeVersion()
				, params.getDefaultHierarchyVSRelation(), params.getSourceName(), params.getPublishName(), rootCode);
		List<DefinedNode> list = ((ValueSetHierarchyServiceImpl) valueSetHeirarchyService).
				getAllValueSetNodesWithoutSource(params.getDefaultHierarchyVSRelation(), params.getPublishName(), params.getPublishValue());
		return list.stream().map(x -> x.getEntityCode()).collect(Collectors.toList());
	}

	private CodingScheme transformToCodingScheme(List<Entity> entity, List<Entity> entities) throws LBException {
		Entities list = new Entities();
		list.getEntityAsReference().addAll(entities);
		Entity ent = entity.iterator().next();
		String source = null;
		if(ent.getPropertyAsReference().stream().filter(x -> x.getPropertyName().equals(params.getSourceName())).findAny().isPresent())
			{source = ent.getPropertyAsReference().stream().filter(x -> x.getPropertyName().equals(params.getSourceName()))
			.map(x -> x.getValue().getContent()).collect(Collectors.toList()).get(0);}
		return AssertedValueSetServices.transform(ent, source, null, list, params.getCodingSchemeVersion(), params.getCodingSchemeURI());
	}
	

	@Override
	public List<Entity> getSourceAssertedValueSetEntitiesForEntityCode(String rootCode) {
		String csUID = this.getDaoManager().getCodingSchemeDao(params.getCodingSchemeURI(), params.getCodingSchemeVersion()).
				getCodingSchemeUIdByUriAndVersion(params.getCodingSchemeURI(), params.getCodingSchemeVersion());
		List<String> predUID = this.getDaoManager().getAssociationDao(params.getCodingSchemeURI(), params.getCodingSchemeVersion()).
				getAssociationPredicateUidsForAssociationName(csUID, null, params.getAssertedValueSetRelation());
		return ibatisAssertedValueSetDao.getSourceAssertedValueSetEntitiesForEntityCode(rootCode == null? params.getRootConcept(): rootCode, 
				params.getAssertedValueSetRelation(), predUID.get(0), csUID);
	}
	
	@Override
	public List<String> getSourceAssertedValueSetEntityUidsforPredicateUid(int start, int pageSize) {
		String csUID = this.getDaoManager().getCodingSchemeDao(params.getCodingSchemeURI(), params.getCodingSchemeVersion()).
				getCodingSchemeUIdByUriAndVersion(params.getCodingSchemeURI(), params.getCodingSchemeVersion());
		List<String> predUID = this.getDaoManager().getAssociationDao(params.getCodingSchemeURI(), params.getCodingSchemeVersion()).
				getAssociationPredicateUidsForAssociationName(csUID, null, params.getAssertedValueSetRelation());
		return ibatisAssertedValueSetDao.getValueSetEntityUids(csUID, predUID.get(0), start, pageSize);
	}
	
	@Override
	public List<Entity> getEntitiesForUidMap(List<String> entityUids){
		String csUID = this.getDaoManager().getCodingSchemeDao(params.getCodingSchemeURI(), params.getCodingSchemeVersion()).
				getCodingSchemeUIdByUriAndVersion(params.getCodingSchemeURI(), params.getCodingSchemeVersion());
		return entityDao.getEntities(csUID, entityUids);
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
	 * @return the valueSetHeirarchyService
	 */
	public ValueSetHierarchyService getValueSetHeirarchyService() {
		return valueSetHeirarchyService;
	}

	/**
	 * @param valueSetHeirarchyService the valueSetHeirarchyService to set
	 */
	public void setValueSetHeirarchyService(ValueSetHierarchyService valueSetHeirarchyService) {
		this.valueSetHeirarchyService = valueSetHeirarchyService;
	}

	public AssertedValueSetParameters getParams() {
		return params;
	}

	public void setParams(AssertedValueSetParameters params) {
		this.params = params;
	}

	public EntityDao getEntityDao() {
		return entityDao;
	}

	public void setEntityDao(EntityDao entityDao) {
		this.entityDao = entityDao;
	}




}
