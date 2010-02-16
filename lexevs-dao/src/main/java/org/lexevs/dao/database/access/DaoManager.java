package org.lexevs.dao.database.access;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.access.entity.EntityDao;
import org.lexevs.dao.database.access.property.PropertyDao;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.system.ResourceManager;

public class DaoManager {

	private List<CodingSchemeDao> codingSchemeDaos;
	
	private List<EntityDao> entityDaos;
	
	private List<PropertyDao> propertyDaos;
	
	private List<VersionsDao> versionsDaos;
	
	private ResourceManager resourceManager;
	
	public VersionsDao getVersionsDao(String codingSchemeUri, String version){
		return this.doGetDao(codingSchemeUri, version, this.getVersionsDaos());
	}
	
	public EntityDao getEntityDao(String codingSchemeUri, String version){
		return this.doGetDao(codingSchemeUri, version, this.getEntityDaos());
	}
	
	public CodingSchemeDao getCodingSchemeDao(String codingSchemeUri, String version){
		return this.doGetDao(codingSchemeUri, version, this.getCodingSchemeDaos());
	}
	
	public PropertyDao getPropertyDao(String codingSchemeUri, String version){
		return this.doGetDao(codingSchemeUri, version, this.getPropertyDaos());
	}
	
	protected <T extends LexGridSchemaVersionAwareDao> T doGetDao(String codingSchemeUri, String version, List<T> daos){
		return getCorrectDaoForSchemaVersion(daos, 
				getLexGridSchemaVersion(codingSchemeUri, version));
	}
	
	protected LexGridSchemaVersion getLexGridSchemaVersion(String uri, String version){
		AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
		ref.setCodingSchemeURN(uri);
		ref.setCodingSchemeVersion(version);
		
		try {
			return resourceManager.getRegistry().getSupportedLexGridSchemaVersion(ref);
		} catch (LBInvocationException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected <T extends LexGridSchemaVersionAwareDao> T getCorrectDaoForSchemaVersion(List<T> possibleDaos, LexGridSchemaVersion schemaVersion) {
		List<T> foundDaos = new ArrayList<T>();
		
		for(T dao : possibleDaos){
			if(dao.supportsLgSchemaVersion(schemaVersion)){
				foundDaos.add(dao);
			}
		}
		
		Assert.assertTrue("No matching DAO for Database Version: " +
				schemaVersion, foundDaos.size() > 0);	
		
		Assert.assertTrue("More than one matching DAO for: " +
				foundDaos.get(0).getClass().getName(), foundDaos.size() < 2);
		
		return foundDaos.get(0);
	}

	public List<CodingSchemeDao> getCodingSchemeDaos() {
		return codingSchemeDaos;
	}

	public void setCodingSchemeDaos(List<CodingSchemeDao> codingSchemeDaos) {
		this.codingSchemeDaos = codingSchemeDaos;
	}

	public List<EntityDao> getEntityDaos() {
		return entityDaos;
	}

	public void setEntityDaos(List<EntityDao> entityDaos) {
		this.entityDaos = entityDaos;
	}

	public List<PropertyDao> getPropertyDaos() {
		return propertyDaos;
	}

	public void setPropertyDaos(List<PropertyDao> propertyDaos) {
		this.propertyDaos = propertyDaos;
	}

	public List<VersionsDao> getVersionsDaos() {
		return versionsDaos;
	}

	public void setVersionsDaos(List<VersionsDao> versionsDaos) {
		this.versionsDaos = versionsDaos;
	}

	public ResourceManager getResourceManager() {
		return resourceManager;
	}

	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

}
