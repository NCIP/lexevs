package org.lexevs.dao.database.service.codingscheme;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.URIMap;
import org.LexGrid.versions.EntryState;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.constants.DatabaseConstants;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;
import org.lexevs.registry.service.Registry.ResourceType;
import org.springframework.transaction.annotation.Transactional;

public class VersionableEventCodingSchemeService extends AbstractDatabaseService implements CodingSchemeService {

	private EntityService entityService;
	private Registry registry;
	
	@Transactional
	public CodingScheme getCodingSchemeByUriAndVersion(String uri,
			String version) {
		return this.getDaoManager().getCodingSchemeDao(uri, version).
			getCodingSchemeByUriAndVersion(uri, version);
	}
	
	@Transactional
	public CodingSchemeSummary getCodingSchemeSummaryByUriAndVersion(
			String uri, String version) {
		return this.getDaoManager().getCodingSchemeDao(uri, version).
			getCodingSchemeSummaryByUriAndVersion(uri, version);
	}
	
	@Transactional
	public void insertCodingScheme(CodingScheme scheme) {
		try {
			//registry.addNewItem(
			//		codingSchemeToRegistryEntry(scheme));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		this.fireCodingSchemeInsertEvent(scheme);
		
		CodingSchemeDao codingSchemeDao = 
			this.getDaoManager().getCurrentCodingSchemeDao();
		
		String codingSchemeId = codingSchemeDao.insertCodingScheme(scheme);
		
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
			getCodingSchemeIdByUriAndVersion(codingSchemeUri, codingSchemeVersion);
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

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	public Registry getRegistry() {
		return registry;
	}

	
}
