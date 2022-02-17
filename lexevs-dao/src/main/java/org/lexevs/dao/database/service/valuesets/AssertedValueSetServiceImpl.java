
package org.lexevs.dao.database.service.valuesets;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetServices;
import org.lexevs.dao.database.access.association.model.DefinedNode;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.lexevs.locator.LexEvsServiceLocator;

public class AssertedValueSetServiceImpl extends AbstractDatabaseService implements AssertedValueSetService {

	private static final long serialVersionUID = -4003462128156213331L;
	private AssertedValueSetParameters params;
	
	public void init(AssertedValueSetParameters params) {
		this.params = params;
		
		String version = this.params.getCodingSchemeVersion();
		if (version == null || version.equals("")) {
			this.params.setCodingSchemeVersion(getCodingSchemeVersionFromTag());
		}	
	}

	@Override
	public List<CodingScheme> getSourceAssertedValueSetforTopNodeEntityCode(String matchCode) throws LBException {
		if (matchCode == null) {
			throw new RuntimeException("Entity code cannot be null!");
		}
		String csUID = getCsUid();
		List<Entity> entities = getDaoManager().getCurrentAssertedValueSetDao()
				.getSourceAssertedValueSetEntitiesForEntityCode(matchCode, params.getAssertedValueSetRelation(),
						getPredUid(csUID), csUID);
		List<Entity> entity = getDaoManager().getCurrentAssertedValueSetDao()
				.getSourceAssertedValueSetTopNodeForEntityCode(matchCode, csUID);
		List<CodingScheme> transcheme = transformToCodingScheme(entity, entities);
		if(transcheme == null || transcheme.size() == 0){return null;}
		List<CodingScheme> schemes = new ArrayList<CodingScheme>();
		schemes.addAll(transcheme);
		return schemes;
	}
	
	@Override
	public CodingScheme getSourceAssertedValueSetforDescription(String description) throws LBException {
		String csUID = getCsUid();
		List<Entity> entity = getDaoManager().getCurrentAssertedValueSetDao()
				.getSourceAssertedValueSetTopNodeDescription(description, csUID);
		
		if (entity != null && entity.size() > 0) {
			String entityCode = entity.get(0).getEntityCode();
			
			List<Entity> entities = getDaoManager().getCurrentAssertedValueSetDao()
				.getSourceAssertedValueSetEntitiesForEntityCode(entityCode, 
						params.getAssertedValueSetRelation(), getPredUid(csUID), csUID);
			
			List<CodingScheme> transcheme = transformToCodingScheme(entity, entities);
			if (transcheme != null && transcheme.size() > 0) {
				return transcheme.get(0);
			}
		}
		return null;
	}
	
	@Override
	public Entity getEntityforTopNodeEntityCode(String matchCode) throws LBException {
		if (matchCode == null) {
			throw new RuntimeException("Entity code cannot be null!");
		}
		String csUID = getCsUid();
		List<Entity> entities =  getDaoManager().getCurrentAssertedValueSetDao()
				.getSourceAssertedValueSetTopNodeForEntityCode(matchCode, csUID);
		if(entities != null && entities.size() > 0){
			return entities.get(0);
		}
		else{ return null;}
	}
	
	@Override
	public List<CodingScheme> getSourceAssertedValueSetforMemberEntityCode(String matchCode) {
		if(matchCode == null){throw new RuntimeException("Entity code cannot be null!");}
		String csUID = getCsUid();
		List<Entity> entities = getDaoManager().getCurrentAssertedValueSetDao().getSourceAssertedValueSetsForVSMemberEntityCode(matchCode,
				params.getAssertedValueSetRelation(), getPredUid(csUID), csUID);
		List<CodingScheme> schemes = new ArrayList<CodingScheme>();
		for(Entity entity: entities) { 
			List<Entity> vsEntities = getDaoManager().getCurrentAssertedValueSetDao().getSourceAssertedValueSetEntitiesForEntityCode(entity.getEntityCode(),
					params.getAssertedValueSetRelation(), getPredUid(csUID), csUID);
			
			List<Entity> topNodeListOfOne = new ArrayList<Entity>();
			topNodeListOfOne.add(entity);
					try {
						schemes.addAll(transformToCodingScheme(topNodeListOfOne, vsEntities));
					} catch (LBException e) {
						throw new RuntimeException("Failed to retrieve value set for: " + entity.getEntityCode(), e);
					}
		}
		return schemes;
	}
	
	@Override
	public List<String> getAllValidValueSetTopNodeCodes(){
		List<DefinedNode> nodes = getDaoManager().getCurrentAssertedValueSetDao().getAllValidValueSetTopNodeCodes(
				params.getPublishName(), params.getPublishValue(), getPredUid(getCsUid()), getCsUid());
		return nodes.stream().map(node -> node.getEntityCode()).collect(Collectors.toList());
	}

	private List<CodingScheme> transformToCodingScheme(List<Entity> entity, List<Entity> entities) throws LBException {
		if (entity == null || entity.size() == 0) {
			return null;
		}
		Entities list = new Entities();
		list.getEntityAsReference().addAll(entities);
		Entity ent = entity.iterator().next();
		List<String> sources = null;
		if (ent.getPropertyAsReference().stream().filter(x -> x.getPropertyName().equals(params.getSourceName()))
				.findAny().isPresent()) {
			sources = ent.getPropertyAsReference().stream()
					.filter(x -> x.getPropertyName().equals(params.getSourceName())).map(x -> x.getValue().getContent())
					.collect(Collectors.toList());
		}
		if (sources != null && sources.size() > 0) {
			return sources.stream().map(source -> {
				CodingScheme cs = null;
				try {
					cs = AssertedValueSetServices.transform(ent, source, null, list, params.getCodingSchemeVersion(),
							params.getCodingSchemeURI());
				} catch (LBException e) {
					e.printStackTrace();
				}
				return cs;
			}).collect(Collectors.toList());
		} else {
			List<CodingScheme> newList = new ArrayList<CodingScheme>();
			newList.add(AssertedValueSetServices.transform(ent, null, null, list, params.getCodingSchemeVersion(),
					params.getCodingSchemeURI()));
			return newList;
		}
	}
	

	@Override
	public List<Entity> getSourceAssertedValueSetEntitiesForEntityCode(String rootCode) {
		if (rootCode == null) {
			throw new RuntimeException("Root value set code cannot be null!");
		}
		String csUID = getCsUid();
		return getDaoManager().getCurrentAssertedValueSetDao().getSourceAssertedValueSetEntitiesForEntityCode(rootCode,
				params.getAssertedValueSetRelation(), getPredUid(csUID), csUID);
	}
	
	@Override
	public List<Entity> getPagedSourceAssertedValueSetEntities(String rootCode, int start, int pageSize) {
		if (rootCode == null) {
			throw new RuntimeException("Root value set code cannot be null!");
		}
		String csUID = getCsUid();
		return getDaoManager().getCurrentAssertedValueSetDao().getPagedValueSetEntities(rootCode, csUID,
				getPredUid(csUID), start, pageSize);
	}
	
	@Override
	public List<String> getSourceAssertedValueSetEntityUidsforPredicateUid(int start, int pageSize) {
		String csUID = getCsUid();
		return getDaoManager().getCurrentAssertedValueSetDao().getValueSetEntityUids(csUID, getPredUid(csUID), start,
				pageSize);
	}
	
	@Override
	public List<Entity> getEntitiesForUidMap(List<String> entityUids){
		if(entityUids == null || entityUids.size() == 0){throw new RuntimeException("Must have entity indentifiers to proceed!");}

		return getDaoManager().getCurrentEntityDao().getEntities(getCsUid(), entityUids);
	}
	
	@Override
	public int getVSEntityCountForTopNodeCode(String code) {
		if (code == null) {
			throw new RuntimeException("Must have entity indentifiers to proceed!");
		}
		String csUid = getCsUid();
		return getDaoManager().getCurrentAssertedValueSetDao().getValueSetEntityCount(code, csUid, getPredUid(csUid));
	}
	
	@Override
	public List<Property> getEntityProperties(String entityCode) {
		// TODO Auto-generated method stub
		return getDaoManager().getCurrentAssertedValueSetDao().getValueSetEntityProperties(entityCode, getCsUid());
	}
	
	public String getCsUid() {
		return this.getCodingSchemeUId(params.getCodingSchemeURI(), params.getCodingSchemeVersion());
	}
	
	public String getPredUid(String csUID) {
		return this.getDaoManager().getAssociationDao(params.getCodingSchemeURI(), params.getCodingSchemeVersion()).
				getAssociationPredicateUidsForAssociationName(csUID, null, params.getAssertedValueSetRelation()).get(0);
	}

//	public ValueSetHierarchyService getValueSetHeirarchyService() {
//		return LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getValueSetHierarchyService();
//	}

	public AssertedValueSetParameters getParams() {
		return params;
	}

	public void setParams(AssertedValueSetParameters params) {
		this.params = params;
	}

	/**
	 * Retrieve the coding scheme version based on the name and tag
	 * @return coding scheme version
	 */
	private String getCodingSchemeVersionFromTag() {
		String version = null;
		
		try {
			version = LexEvsServiceLocator.getInstance().getSystemResourceService().
				getInternalVersionStringForTag(
						this.params.getCodingSchemeName(), 
						this.params.getCodingSchemeTag());
		} catch (LBParameterException e) {
			// do nothing
		}
		
		if(version == null) {
			System.out.println("AssertedValueSetServiceImpl: AssertedValueSetParameters - Version was not found for coding scheme: " +
					this.params.getCodingSchemeName() + " and tag: " + this.params.getCodingSchemeTag());
		}
		
		return version;
	}

}