package org.lexevs.dao.database.service.ncihistory;

import java.net.URI;
import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeVersionList;
import org.LexGrid.LexBIG.DataModel.Collections.NCIChangeEventList;
import org.LexGrid.LexBIG.DataModel.Collections.SystemReleaseList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.SystemReleaseDetail;
import org.LexGrid.LexBIG.DataModel.NCIHistory.NCIChangeEvent;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.versions.CodingSchemeVersion;
import org.LexGrid.versions.EntityVersion;
import org.LexGrid.versions.SystemRelease;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.access.ncihistory.NciHistoryDao;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

public class VersionableEventNciHistoryService extends AbstractDatabaseService implements NciHistoryService {

	@Override
	@Transactional
	public void insertSystemRelease(String codingSchemeUri,
			SystemRelease systemRelease) {

		this.getDaoManager().getNciHistoryDao(
				codingSchemeUri).
				insertSystemRelease(codingSchemeUri, systemRelease);
	}

	@Override
	public void insertNCIChangeEvent(String codingSchemeUri,
			NCIChangeEvent changeEvent) {
		Assert.notNull(changeEvent);
		Assert.notNull(changeEvent.getEditDate());

		NciHistoryDao historyDao = this.getDaoManager().getNciHistoryDao(
				codingSchemeUri);

		String systemReleaseUid = historyDao.getSystemReleaseUidForDate(codingSchemeUri, changeEvent.getEditDate());

		historyDao.insertNciChangeEvent(systemReleaseUid, changeEvent);	
	}	

	public SystemReleaseList getBaseLines(String uri, Date releasedAfter, Date releasedBefore) {		
		List<SystemRelease> list = this.getDaoManager().getNciHistoryDao(
				uri).getBaseLines(uri, releasedAfter, releasedBefore);
		
		return this.buildSystemReleaseList(list);
	}

	public SystemRelease getEarliestBaseLine(String uri) {
		return this.getDaoManager().getNciHistoryDao(
				uri).getEarliestBaseLine(uri);
		
	}

	public SystemRelease getLatestBaseLine(String uri) {
		return this.getDaoManager().getNciHistoryDao(
				uri).getLatestBaseLine(uri);
	}


	public SystemReleaseDetail getSystemRelease(String uri, URI releaseURN) {
		SystemReleaseDetail result = new SystemReleaseDetail();
		
		SystemRelease sr = this.getDaoManager().getNciHistoryDao(
				uri).getSystemReleaseForReleaseUri(uri, releaseURN.toString());
		
		List<NCIChangeEvent> list = this.getDaoManager().getNciHistoryDao(
				uri).getEditActionList(uri, null, releaseURN.toString());
		
		for(NCIChangeEvent event : list) {
			result.addEntityVersions(this.buildEntityVersion(sr, event));
		}
		
		return result;
	}
	
	private EntityVersion buildEntityVersion(SystemRelease systemRelease, NCIChangeEvent changeEvent) {
		EntityVersion ev = new EntityVersion();
        ev.setIsComplete(new Boolean(false));
        ev.setEntityDescription(systemRelease.getEntityDescription());
        ev.setReleaseURN(systemRelease.getReleaseURI());

        ev.setVersion(NciHistoryService.dateFormat.format(changeEvent.getEditDate()).toUpperCase());
        ev.setVersionDate(changeEvent.getEditDate());
        
        return ev;
		
	}

	public NCIChangeEventList getEditActionList(String uri, ConceptReference conceptReference,
			Date date) {
		List<NCIChangeEvent> list = this.getDaoManager().getNciHistoryDao(
				uri).getEditActionList(uri, getConceptReferenceCode(conceptReference), date);
		
		return buildNCIChangeEventList(list);
	}

	public NCIChangeEventList getEditActionList(String uri, ConceptReference conceptReference, Date beginDate,
			Date endDate) {
		List<NCIChangeEvent> list = this.getDaoManager().getNciHistoryDao(
				uri).getEditActionList(uri, getConceptReferenceCode(conceptReference), beginDate, endDate);
		
		return buildNCIChangeEventList(list);
	}

	public NCIChangeEventList getEditActionList(String uri, ConceptReference conceptReference, URI releaseURN) {
		List<NCIChangeEvent> list = this.getDaoManager().getNciHistoryDao(
				uri).getEditActionList(uri, getConceptReferenceCode(conceptReference), releaseURN.toString());
		
		return buildNCIChangeEventList(list);
	}

	public CodingSchemeVersion getConceptCreationVersion(String uri, ConceptReference conceptReference) {
		return this.getDaoManager().getNciHistoryDao(
				uri).getConceptCreateVersion(uri, conceptReference.getCode());
	}

	public CodingSchemeVersionList getConceptChangeVersions(String uri, ConceptReference conceptReference,
			Date beginDate, Date endDate){
		List<CodingSchemeVersion> list = this.getDaoManager().getNciHistoryDao(
				uri).getConceptChangeVersions(uri, conceptReference.getCode(), beginDate, endDate);
		
		return buildCodingSchemeVersionList(list);
	}

	public NCIChangeEventList getDescendants(String uri, ConceptReference conceptReference) {
		List<NCIChangeEvent> list = this.getDaoManager().getNciHistoryDao(
				uri).getDescendants(uri, getConceptReferenceCode(conceptReference));
		
		return buildNCIChangeEventList(list);
	}

	public NCIChangeEventList getAncestors(String uri, ConceptReference conceptReference){
		List<NCIChangeEvent> list = this.getDaoManager().getNciHistoryDao(
				uri).getAncestors(uri, getConceptReferenceCode(conceptReference));
		
		return buildNCIChangeEventList(list);
	}
	
	private SystemReleaseList buildSystemReleaseList(List<SystemRelease> list) {
		SystemReleaseList returnList = new SystemReleaseList();
		
		if(list != null) {
			returnList.setSystemRelease(list.toArray(new SystemRelease[list.size()]));
		}
		
		return returnList;
	}

	private CodingSchemeVersionList buildCodingSchemeVersionList(List<CodingSchemeVersion> list) {
		CodingSchemeVersionList returnList = new CodingSchemeVersionList();
		
		if(list != null) {
			returnList.setEntry(list.toArray(new CodingSchemeVersion[list.size()]));
		}
		
		return returnList;
	}
	
	private NCIChangeEventList buildNCIChangeEventList(List<NCIChangeEvent> list) {
		NCIChangeEventList returnList = new NCIChangeEventList();
		
		if(list != null) {
			returnList.setEntry(list.toArray(new NCIChangeEvent[list.size()]));
		}
		
		return returnList;
	}
	
	private String getConceptReferenceCode(ConceptReference ref) {
		if(ref == null || StringUtils.isBlank(ref.getCode())) {
			return null;
		} else {
			return ref.getCode();
		}
	}
}
