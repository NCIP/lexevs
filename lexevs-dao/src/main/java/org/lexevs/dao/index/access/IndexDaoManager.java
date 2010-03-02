package org.lexevs.dao.index.access;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.lexevs.dao.index.access.entity.EntityDao;
import org.lexevs.dao.index.version.LexEvsIndexFormatVersion;
import org.lexevs.system.model.LocalCodingScheme;
import org.lexevs.system.service.SystemResourceService;

import edu.mayo.informatics.indexer.api.IndexerService;

public class IndexDaoManager {

	private List<EntityDao> entityDaos;
	
	private IndexerService indexerService;
	
	private SystemResourceService systemResourceService;

	public EntityDao getEntityDao(String codingSchemeUri, String version){
		return this.doGetDao(codingSchemeUri, version, this.getEntityDaos());
	}
	
	protected <T extends LexEvsIndexFormatVersionAwareDao> T doGetDao(String codingSchemeUri, String version, List<T> daos){
		Assert.assertNotNull("No DAOs have been registered for the requested type.", daos);	
		return getCorrectDaoForIndexVersion(daos, 
				getLexGridSchemaVersion(codingSchemeUri, version));
	}
	
	protected LexEvsIndexFormatVersion getLexGridSchemaVersion(String uri, String version){
		try {
			String codingSchemeName = systemResourceService.getInternalCodingSchemeNameForUserCodingSchemeName(uri, version);
			
			LocalCodingScheme lcs = LocalCodingScheme.getLocalCodingScheme(codingSchemeName, version);
			String indexName = indexerService.getMetaData().getIndexMetaDataValue(lcs.getKey());
	
			String indexVersion = indexerService.getMetaData().getIndexMetaDataValue(indexName, "lgModel");
			
			return LexEvsIndexFormatVersion.parseStringToVersion(indexVersion);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected <T extends LexEvsIndexFormatVersionAwareDao> T getCorrectDaoForIndexVersion(List<T> possibleDaos, LexEvsIndexFormatVersion indexVersion) {
		List<T> foundDaos = new ArrayList<T>();
		
		for(T dao : possibleDaos){
			if(dao.supportsLexEvsIndexFormatVersion(indexVersion)){
				foundDaos.add(dao);
			}
		}
		
		Assert.assertTrue("No matching DAO for Index Version: " +
				indexVersion, foundDaos.size() > 0);	
		
		Assert.assertTrue("More than one matching DAO for: " +
				foundDaos.get(0).getClass().getName(), foundDaos.size() < 2);
		
		return foundDaos.get(0);
	}


	public void setEntityDaos(List<EntityDao> entityDaos) {
		this.entityDaos = entityDaos;
	}


	public List<EntityDao> getEntityDaos() {
		return entityDaos;
	}

	public void setIndexerService(IndexerService indexerService) {
		this.indexerService = indexerService;
	}

	public IndexerService getIndexerService() {
		return indexerService;
	}

	public void setSystemResourceService(SystemResourceService systemResourceService) {
		this.systemResourceService = systemResourceService;
	}

	public SystemResourceService getSystemResourceService() {
		return systemResourceService;
	}
}
