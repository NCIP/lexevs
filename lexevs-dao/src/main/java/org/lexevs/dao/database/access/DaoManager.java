package org.lexevs.dao.database.access;

import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.access.entity.EntityDao;
import org.lexevs.dao.database.access.property.PropertyDao;
import org.lexevs.dao.database.access.versions.VersionsDao;

public class DaoManager {

	private CodingSchemeDao codingSchemeDao;
	
	private EntityDao EntityDao;
	
	private PropertyDao propertyDao;
	
	private VersionsDao versionsDao;
	
	public void setCodingSchemeDao(CodingSchemeDao codingSchemeDao) {
		this.codingSchemeDao = codingSchemeDao;
	}

	public CodingSchemeDao getCodingSchemeDao() {
		return codingSchemeDao;
	}

	public void setVersionsDao(VersionsDao versionsDao) {
		this.versionsDao = versionsDao;
	}

	public VersionsDao getVersionsDao() {
		return versionsDao;
	}

	public void setEntityDao(EntityDao entityDao) {
		EntityDao = entityDao;
	}

	public EntityDao getEntityDao() {
		return EntityDao;
	}

	public void setPropertyDao(PropertyDao propertyDao) {
		this.propertyDao = propertyDao;
	}

	public PropertyDao getPropertyDao() {
		return propertyDao;
	}
}
