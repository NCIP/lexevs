package org.lexevs.dao.database.ibatis.revision;

import java.sql.Date;
import java.util.List;

import org.LexGrid.versions.Revision;
import org.lexevs.dao.database.access.revision.RevisionDao;
import org.lexevs.dao.database.access.systemRelease.SystemReleaseDao;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.versions.parameter.InsertRevisionBean;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;

public class IbatisRevisionDao extends AbstractIbatisDao implements RevisionDao {

	/** The VERSION s_ namespace. */
	public static String VERSIONS_NAMESPACE = "Versions.";

	private String INSERT_INTO_REVISION = VERSIONS_NAMESPACE + "insertRevision"; 
	
	private String SELECT_REVISION_GUID_BY_ID = VERSIONS_NAMESPACE + "getRevisionGuidFromId";

	/** system release dao*/
	private SystemReleaseDao systemReleaseDao = null;
	
	@Override
	public List<Revision> getAllRevisions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Revision getRevisionByGuid(String revisionGuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Revision getRevisionByUri(String revisionUri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRevisionUIdById(String revisionId) {
		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		
		if(	revisionId == null )
			return null;
		
		String revisionGuid = (String) this.getSqlMapClientTemplate()
				.queryForObject(SELECT_REVISION_GUID_BY_ID, new PrefixedParameter(prefix, revisionId));
		
		return revisionGuid;
	}

	@Override
	public String insertRevisionEntry(Revision revision, String releaseURI) {

		String prefix = this.getPrefixResolver().resolveDefaultPrefix();
		String revisionUid = this.createUniqueId();
		
		String releaseUId = systemReleaseDao.getSystemReleaseUIdByUri(releaseURI);
		
		InsertRevisionBean insertRevisionBean = new InsertRevisionBean();
		
		insertRevisionBean.setPrefix(prefix);
		insertRevisionBean.setRevisionGuid(revisionUid);
		insertRevisionBean.setReleaseGuid(releaseUId);
		insertRevisionBean.setRevAppliedDate(new Date(System.currentTimeMillis()));
		insertRevisionBean.setRevision(revision);
		
		this.getSqlMapClientTemplate().insert(INSERT_INTO_REVISION, 
				insertRevisionBean);
		
		return revisionUid;
	}

	@Override
	public <T> T executeInTransaction(IndividualDaoCallback<T> callback) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean supportsLgSchemaVersion(LexGridSchemaVersion version) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		// TODO Auto-generated method stub
		return null;
	}

	public SystemReleaseDao getSystemReleaseDao() {
		return systemReleaseDao;
	}

	public void setSystemReleaseDao(SystemReleaseDao systemReleaseDao) {
		this.systemReleaseDao = systemReleaseDao;
	}

}
