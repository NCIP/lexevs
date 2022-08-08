
package org.lexevs.dao.database.ibatis.systemRelease;

import java.util.List;

import org.LexGrid.versions.SystemRelease;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.access.systemRelease.SystemReleaseDao;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.versions.parameter.InsertSystemReleaseBean;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;

public class IbatisSystemReleaseDao extends AbstractIbatisDao implements SystemReleaseDao {

/** The VERSION s_ namespace. */
public static String VERSIONS_NAMESPACE = "Versions.";
	
	/** The GE t_ syste m_ releas e_ i d_ b y_ uri. */
	public static String GET_SYSTEM_RELEASE_ID_BY_URI = VERSIONS_NAMESPACE + "getSystemReleaseGuidByUri" +
			"";

	private String INSERT_INTO_SYSTEM_RELEASE = VERSIONS_NAMESPACE + "insertSystemRelease"; 
	
	private String GET_SYSTEM_RELEASE_METADATA_BY_URI = VERSIONS_NAMESPACE + "getSystemReleaseMetaDataByUri";
	
	private String GET_SYSTEM_RELEASE_METADATA_BY_ID = VERSIONS_NAMESPACE + "getSystemReleaseMetaDataById";
	
	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getSystemReleaseUIdByUri(String systemReleaseUri) {
		
		return (String) this.getSqlSessionTemplate().selectOne(GET_SYSTEM_RELEASE_ID_BY_URI, 
			systemReleaseUri);
	}
	
	@Override
	public List<SystemRelease> getAllSystemRelease() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SystemRelease getSystemReleaseMetadataById(String systemReleaseId) {
		return (SystemRelease) this.getSqlSessionTemplate().selectOne(GET_SYSTEM_RELEASE_METADATA_BY_ID, 
				systemReleaseId);
	}

	@Override
	public SystemRelease getSystemReleaseMetadataByUri(String systemReleaseUri) {
		return (SystemRelease) this.getSqlSessionTemplate().selectOne(GET_SYSTEM_RELEASE_METADATA_BY_URI, 
				systemReleaseUri);
	}

	@Override
	public String insertSystemReleaseEntry(SystemRelease systemRelease) {
		if (systemRelease == null)
			return null;
		
		// check if this system release is already loaded.
		String releaseGuid = getSystemReleaseUIdByUri(systemRelease.getReleaseURI());
		
		if (StringUtils.isNotEmpty(releaseGuid))
		{
			return releaseGuid;
		}
		
		// Load new system release.
		releaseGuid = this.createUniqueId();
		
		InsertSystemReleaseBean insertSysRelBean = new InsertSystemReleaseBean();
		
		insertSysRelBean.setReleaseUId(releaseGuid);
		insertSysRelBean.setSystemRelease(systemRelease);
		
		this.getSqlSessionTemplate().insert(INSERT_INTO_SYSTEM_RELEASE, 
				insertSysRelBean);	
		
		return releaseGuid;
	}
}