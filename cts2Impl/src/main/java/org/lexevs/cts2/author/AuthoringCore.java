package org.lexevs.cts2.author;

import java.net.URI;
import java.net.URISyntaxException;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.versions.SystemRelease;
import org.apache.commons.lang.StringUtils;
import org.lexevs.cts2.core.update.SystemReleaseInfo;
import org.lexevs.dao.database.service.version.AuthoringService;
import org.lexevs.locator.LexEvsServiceLocator;

public class AuthoringCore {
	private AuthoringService authServ_ = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAuthoringService();
	public String createSystemRelease(SystemReleaseInfo systemReleaseInfo) throws LBException{
		if (systemReleaseInfo == null)
			throw new LBException("Problem : System release information is empty");
		if (StringUtils.isEmpty(systemReleaseInfo.getReleaseURI()))
			throw new LBException("Problem : System release URI is empty");
		return authServ_.insertSystemReleaseMetadata(getLexGridSystemRelease(systemReleaseInfo));
	}
	
	public SystemReleaseInfo getSystemReleaseInfoByReleaseId(String releaseId) throws LBException{
		SystemRelease sr = authServ_.getSystemReleaseMetadataById(releaseId);
		if (sr == null)
			throw new LBException("Problem : No System Release found for releaseId : " + releaseId);
		
		return getSystemReleaseInfo(sr);
	}
	
	public SystemReleaseInfo getSystemReleaseInfoByReleaseURI(URI releaseURI) throws LBException{
		if (releaseURI == null)
			throw new LBException("Problem : Release URI is empty");
		
		SystemRelease sr = authServ_.getSystemReleaseMetadataByUri(releaseURI.toString());
		if (sr == null)
			throw new LBException("Problem : No System Release found for releaseURI : " + releaseURI);
		
		return getSystemReleaseInfo(sr);
	}
	
	private SystemReleaseInfo getSystemReleaseInfo(SystemRelease lgSystemRelease){
		SystemReleaseInfo srInfo = new SystemReleaseInfo();
		srInfo.setBasedOnRelease(lgSystemRelease.getBasedOnRelease());
		srInfo.setReleaseAgency(lgSystemRelease.getReleaseAgency());
		srInfo.setReleaseDate(lgSystemRelease.getReleaseDate());
		srInfo.setReleaseId(lgSystemRelease.getReleaseId());
		srInfo.setReleaseURI(lgSystemRelease.getReleaseURI());
		if (lgSystemRelease.getEntityDescription() != null)
			srInfo.setDescription(lgSystemRelease.getEntityDescription().getContent());
		return srInfo;
	}
	
	private SystemRelease getLexGridSystemRelease(SystemReleaseInfo systemReleaseInfo){
		SystemRelease sr = new SystemRelease();
		sr.setBasedOnRelease(systemReleaseInfo.getBasedOnRelease());
		sr.setReleaseAgency(systemReleaseInfo.getReleaseAgency());
		sr.setReleaseDate(systemReleaseInfo.getReleaseDate());
		sr.setReleaseId(systemReleaseInfo.getReleaseId());
		sr.setReleaseURI(systemReleaseInfo.getReleaseURI());
		if (StringUtils.isNotEmpty(systemReleaseInfo.getDescription()))
		{
			EntityDescription ed = new EntityDescription();
			ed.setContent(systemReleaseInfo.getDescription());
			sr.setEntityDescription(ed);
		}
		return sr;
	}
}
