package org.lexevs.dao.database.service.codingscheme;

import java.util.Arrays;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.URIMap;
import org.LexGrid.versions.EntryState;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.lexevs.dao.database.service.entity.EntityService;
import org.springframework.transaction.annotation.Transactional;

public class VersionableEventCodingSchemeService extends AbstractDatabaseService implements CodingSchemeService {

	private EntityService entityService;
	

	public CodingScheme getCodingSchemeById(String codingSchemeId) {
		return getDaoManager().getCodingSchemeDao().getCodingSchemeById(codingSchemeId);
	}
	
	@Transactional
	public void insertCodingScheme(CodingScheme scheme) {
		this.fireCodingSchemeInsertEvent(scheme);
		String codingSchemeId = getDaoManager().getCodingSchemeDao().insertCodingScheme(scheme);
		
		if(scheme.getMappings() != null){
			getDaoManager().getCodingSchemeDao().insertMappings(codingSchemeId, scheme.getMappings());
		}
		
		if(scheme.getEntities() != null){
		entityService.
			insertEntity(codingSchemeId, Arrays.asList(scheme.getEntities().getEntity()));
		}
	}
	
	public void insertURIMap(
			String codingSchemeName, 
			String codingSchemeVersion,
			URIMap uriMap){
		getDaoManager().getCodingSchemeDao().getCodingSchemeId(codingSchemeName, codingSchemeVersion);
	}
	
	public void insertURIMap(
			String codingSchemeId, 
			URIMap uriMap){
		getDaoManager().getCodingSchemeDao().insertURIMap(codingSchemeId, uriMap);
	}

	public void updateCodingScheme(
			String codingSchemeName,
			String codingSchemeVersion,
			CodingScheme codingScheme) {
		// TODO Auto-generated method stub
		
	}

	public void updateCodingSchemeEntryState(CodingScheme codingScheme,
			EntryState entryState) {
		// TODO Auto-generated method stub
		
	}

	public void setEntityService(EntityService entityService) {
		this.entityService = entityService;
	}

	public EntityService getEntityService() {
		return entityService;
	}


}
