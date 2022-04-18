
package org.lexevs.dao.database.ibatis.revision;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.versions.Revision;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.access.revision.RevisionDao;
import org.lexevs.dao.database.access.systemRelease.SystemReleaseDao;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.versions.parameter.InsertRevisionBean;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;
import org.lexevs.registry.service.Registry.ResourceType;
import org.lexevs.system.service.SystemResourceService;
import org.springframework.transaction.annotation.Transactional;

public class IbatisRevisionDao extends AbstractIbatisDao implements RevisionDao {
	
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");

/** The VERSION s_ namespace. */
public static String VERSIONS_NAMESPACE = "Versions.";

	private String INSERT_INTO_REVISION = VERSIONS_NAMESPACE + "insertRevision"; 
	
	private String SELECT_REVISION_GUID_BY_ID = VERSIONS_NAMESPACE + "getRevisionGuidFromId";
	
	private String GET_REVISION_ID_BY_DATE = VERSIONS_NAMESPACE + "getRevisionIdByDate";
	
	private String CHECK_REVISION_EXISTS_IN_VS_ENTRYSTATE = VERSIONS_NAMESPACE + "checkRevisionExistsInVSEntryState";
	
	private String CHECK_REVISION_EXISTS_IN_ENTRYSTATE = VERSIONS_NAMESPACE + "checkRevisionExistsInEntryState";
	
	private String DELETE_REVISION_BY_ID = VERSIONS_NAMESPACE + "deleteRevisionById";
	
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
		if(	revisionId == null )
			return null;
		
		String revisionGuid = null;
		
		revisionGuid = (String) this.getSqlSessionTemplate()
				.selectOne(SELECT_REVISION_GUID_BY_ID, revisionId);
		
		return revisionGuid;
	}
	
	@Override
	public String insertRevisionEntry(Revision revision, String releaseURI) throws LBRevisionException {

		String revisionUId = this.createUniqueId();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		
		if (getRevisionUIdById(revision.getRevisionId()) == null) {

			String releaseUId;
			if(StringUtils.isNotBlank(releaseURI)){
				releaseUId = systemReleaseDao.getSystemReleaseUIdByUri(releaseURI);
			} else {
				releaseUId = null;
			}

			InsertRevisionBean insertRevisionBean = new InsertRevisionBean();

			insertRevisionBean.setRevisionGuid(revisionUId);
			insertRevisionBean.setReleaseGuid(releaseUId);
			insertRevisionBean.setRevAppliedDate(new Timestamp(System
					.currentTimeMillis()));
			insertRevisionBean.setRevision(revision);

			this.getSqlSessionTemplate().insert(INSERT_INTO_REVISION,
					insertRevisionBean);
		} else {
			throw new LBRevisionException("Revision '"
					+ revision.getRevisionId() + "' already exists.");
		}
		
		return revisionUId;
	}

	@Transactional
	public String getRevisionIdForDate(Timestamp dateTime) {
		return (String) this.getSqlSessionTemplate()
				.selectOne(GET_REVISION_ID_BY_DATE, dateTime);		
	}

	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return Arrays.asList(supportedDatebaseVersion);
	}

	public SystemReleaseDao getSystemReleaseDao() {
		return systemReleaseDao;
	}

	public void setSystemReleaseDao(SystemReleaseDao systemReleaseDao) {
		this.systemReleaseDao = systemReleaseDao;
	}

	@Override
	public String getNewRevisionId() {

		return this.createUniqueId();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexevs.dao.database.access.revision.RevisionDao#removeRevisionById(java.lang.String)
	 */
	@Override
	public boolean removeRevisionById(String revisionId) throws LBException{
		if (StringUtils.isEmpty(revisionId))
			throw new LBException("Revision ID can not be empty");
		
		String revisionGuid = this.getRevisionUIdById(revisionId);
		if (StringUtils.isEmpty(revisionGuid))
			throw new LBException("Revision ID " + revisionId + " does not exists in the system");
		
		SystemResourceService sysSrv = LexEvsServiceLocator.getInstance().getSystemResourceService();
		
		// before removing a revision, check if it is used in cs entry state table		
		String count = null;
		if (sysSrv.getSystemVariables().isSingleTableMode())
		{
			count = (String) this.getSqlSessionTemplate()
				.selectOne(CHECK_REVISION_EXISTS_IN_ENTRYSTATE, 
					new PrefixedParameter(this.getPrefixResolver().resolveDefaultPrefix(), revisionGuid));
	
			if (!count.equals("0"))
				throw new LBException("Revision ID " + revisionId + " can not be removed as it is being referenced by other loaded entries.");
		}
		else
		{
			Registry reg = LexEvsServiceLocator.getInstance().getRegistry();
			List<RegistryEntry> reList = reg.getAllRegistryEntriesOfType(ResourceType.CODING_SCHEME);
						
			for (RegistryEntry re : reList)
			{				
				String prefix = this.getPrefixResolver().resolveDefaultPrefix() + re.getPrefix();
				count = (String) this.getSqlSessionTemplate()
					.selectOne(CHECK_REVISION_EXISTS_IN_ENTRYSTATE, 
							new PrefixedParameter(prefix, revisionGuid));
			
				if (!count.equals("0"))
					throw new LBException("Revision ID " + revisionId + " can not be removed as it is being referenced by other loaded entries.");
			
			}
		}
		
		// now check vs entry state table
		count = (String) this.getSqlSessionTemplate()
			.selectOne(CHECK_REVISION_EXISTS_IN_VS_ENTRYSTATE, 
					new PrefixedParameter(this.getPrefixResolver().resolveDefaultPrefix(), revisionGuid));
		
		if (!count.equals("0"))
			throw new LBException("Revision ID " + revisionId + " can not be removed as it is being referenced by other loaded entries.");
		
		// if not used, delete it
		this.getSqlSessionTemplate().delete(
				DELETE_REVISION_BY_ID,
				new PrefixedParameter(this.getPrefixResolver().resolveDefaultPrefix(), revisionId));
		
		return true;
	}	
}