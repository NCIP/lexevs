package org.lexevs.dao.database.service.codingscheme;

import java.util.Arrays;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.URIMap;
import org.LexGrid.versions.EntryState;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.registry.service.Registry;
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
	public void destroyCodingScheme(String uri, String version) {
		CodingSchemeDao csDao = 
			this.getDaoManager().getCodingSchemeDao(uri, version);
		String codingSchemeId = csDao.getCodingSchemeIdByUriAndVersion(uri, version);
		csDao.deleteCodingSchemeById(codingSchemeId);	
	}
	
	@Transactional
	public void insertCodingScheme(CodingScheme scheme) throws CodingSchemeAlreadyLoadedException {
		boolean exists = registry.containsCodingSchemeEntry(
				
				DaoUtility.createAbsoluteCodingSchemeVersionReference(
						scheme.getCodingSchemeURI(), 
						scheme.getRepresentsVersion())
						
				);
		
		if(exists) {
			throw new CodingSchemeAlreadyLoadedException(
					scheme.getCodingSchemeURI(), 
					scheme.getRepresentsVersion());
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
