package org.lexevs.dao.database.service.codingscheme;

import java.util.Arrays;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.URIMap;
import org.LexGrid.versions.EntryState;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.lexevs.dao.database.service.entity.EntityService;
import org.springframework.transaction.annotation.Transactional;

public class VersionableEventCodingSchemeService extends AbstractDatabaseService implements CodingSchemeService {

	private EntityService entityService;
	
	@Transactional
	public void insertCodingScheme(CodingScheme scheme) {
		this.fireCodingSchemeInsertEvent(scheme);
		String codingSchemeId = this.getCodingSchemeId(
				scheme.getCodingSchemeURI(), scheme.getRepresentsVersion());
		
		if(scheme.getMappings() != null){
			getDaoManager().getCodingSchemeDao(
					scheme.getCodingSchemeURI(), scheme.getRepresentsVersion()).
					insertMappings(codingSchemeId, scheme.getMappings());
		}

		if(scheme.getEntities() != null){
			this.getDaoManager().getEntityDao(
					scheme.getCodingSchemeURI(), scheme.getRepresentsVersion()).insertBatchEntities(codingSchemeId, 
					Arrays.asList(scheme.getEntities().getEntity()));
		}
	}
	
	@Transactional
	public void insertURIMap(
			String codingSchemeUri, 
			String codingSchemeVersion,
			URIMap uriMap){
		CodingSchemeDao codingSchemeDao = getDaoManager().getCodingSchemeDao(codingSchemeUri, codingSchemeVersion);
		String codingSchemeId = codingSchemeDao.
			getCodingSchemeId(codingSchemeUri, codingSchemeVersion);
		getDaoManager().getCodingSchemeDao(codingSchemeUri, codingSchemeVersion).
			insertURIMap(codingSchemeId, uriMap);
	}
	

	@Transactional
	public void updateCodingScheme(
			String codingSchemeName,
			String codingSchemeVersion,
			CodingScheme codingScheme) {
		// TODO Auto-generated method stub
		
	}

	@Transactional
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
