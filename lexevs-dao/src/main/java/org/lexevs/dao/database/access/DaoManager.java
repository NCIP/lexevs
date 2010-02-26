package org.lexevs.dao.database.access;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.lexevs.dao.database.access.association.AssociationDao;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.access.entity.EntityDao;
import org.lexevs.dao.database.access.property.PropertyDao;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.registry.service.Registry;

public class DaoManager {
	
	public static LexGridSchemaVersion CURRENT_VERSION = LexGridSchemaVersion.parseStringToVersion("2.0");

	private List<CodingSchemeDao> codingSchemeDaos;
	
	private List<EntityDao> entityDaos;
	
	private List<PropertyDao> propertyDaos;
	
	private List<AssociationDao> associationDaos;
	
	private List<VersionsDao> versionsDaos;
	
	private Registry registry;
	
	public VersionsDao getVersionsDao(String codingSchemeUri, String version){
		return this.doGetDao(codingSchemeUri, version, this.getVersionsDaos());
	}
	
	public EntityDao getEntityDao(String codingSchemeUri, String version){
		return this.doGetDao(codingSchemeUri, version, this.getEntityDaos());
	}
	
	public CodingSchemeDao getCodingSchemeDao(String codingSchemeUri, String version){
		return this.doGetDao(codingSchemeUri, version, this.getCodingSchemeDaos());
	}
	
	public CodingSchemeDao getCurrentCodingSchemeDao(){
		return this.getCorrectDaoForSchemaVersion(this.codingSchemeDaos, CURRENT_VERSION);
	}
	
	public PropertyDao getPropertyDao(String codingSchemeUri, String version){
		return this.doGetDao(codingSchemeUri, version, this.getPropertyDaos());
	}
	
	public AssociationDao getAssociationDao(String codingSchemeUri, String version){
		return this.doGetDao(codingSchemeUri, version, this.getAssociationDaos());
	}
	
	protected <T extends LexGridSchemaVersionAwareDao> T doGetDao(String codingSchemeUri, String version, List<T> daos){
		Assert.assertNotNull("No DAOs have been registered for the requested type.", daos);	
		return getCorrectDaoForSchemaVersion(daos, 
				getLexGridSchemaVersion(codingSchemeUri, version));
	}
	
	protected LexGridSchemaVersion getLexGridSchemaVersion(String uri, String version){
		AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
		ref.setCodingSchemeURN(uri);
		ref.setCodingSchemeVersion(version);

		try {
			return LexGridSchemaVersion.parseStringToVersion(
					registry.getCodingSchemeEntry(ref).getDbSchemaVersion()
			);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected LexGridSchemaVersion getLexGridSchemaVersion(String uri){
		try {
			return LexGridSchemaVersion.parseStringToVersion(
					registry.getNonCodingSchemeEntry(uri).getDbSchemaVersion()
			);
		} catch (Exception e) {
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

	public Registry getRegistry() {
		return registry;
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	public void setAssociationDaos(List<AssociationDao> associationDaos) {
		this.associationDaos = associationDaos;
	}

	public List<AssociationDao> getAssociationDaos() {
		return associationDaos;
	}
}
