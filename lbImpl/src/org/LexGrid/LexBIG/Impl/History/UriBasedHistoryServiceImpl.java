package org.LexGrid.LexBIG.Impl.History;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeVersionList;
import org.LexGrid.LexBIG.DataModel.Collections.NCIChangeEventList;
import org.LexGrid.LexBIG.DataModel.Collections.SystemReleaseList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.SystemReleaseDetail;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.History.HistoryService;
import org.LexGrid.versions.CodingSchemeVersion;
import org.LexGrid.versions.SystemRelease;
import org.lexevs.dao.database.service.ncihistory.NciHistoryService;
import org.lexevs.locator.LexEvsServiceLocator;

public class UriBasedHistoryServiceImpl implements HistoryService {
    
    private static final long serialVersionUID = 524036401675315235L;
    
    private DateFormat dateFormat = NciHistoryService.dateFormat;

    private String uri;

    public UriBasedHistoryServiceImpl(String codingSchemeUri) throws LBParameterException {
        uri = codingSchemeUri;  
    }

    private NciHistoryService getNciHistoryService() {
        return LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getNciHistoryService();
    }
    
    @Override
    public NCIChangeEventList getAncestors(ConceptReference conceptReference) throws LBParameterException,
            LBInvocationException {
       return getNciHistoryService().getAncestors(uri, conceptReference);
    }

    @Override
    public SystemReleaseList getBaselines(Date releasedAfter, Date releasedBefore) throws LBParameterException,
            LBInvocationException {
        return this.getNciHistoryService().getBaseLines(uri, releasedAfter, releasedBefore);
    }

    @Override
    public CodingSchemeVersionList getConceptChangeVersions(ConceptReference conceptReference, Date beginDate,
            Date endDate) throws LBParameterException, LBInvocationException {
        return this.getNciHistoryService().getConceptChangeVersions(uri, conceptReference, beginDate, endDate);
    }

    @Override
    public CodingSchemeVersion getConceptCreationVersion(ConceptReference conceptReference)
            throws LBParameterException, LBInvocationException {
        CodingSchemeVersion creationVersion = this.getNciHistoryService().getConceptCreationVersion(uri, conceptReference);
        if(creationVersion == null) {
            throw new LBParameterException("No results found for parameter:", "conceptReference");
        }
        
        return creationVersion;
    }

    @Override
    public NCIChangeEventList getDescendants(ConceptReference conceptReference) throws LBParameterException,
            LBInvocationException {
        return getNciHistoryService().getDescendants(uri, conceptReference);
    }

    @Override
    public SystemRelease getEarliestBaseline() throws LBInvocationException {
        return getNciHistoryService().getEarliestBaseLine(uri);
    }

    @Override
    public NCIChangeEventList getEditActionList(ConceptReference conceptReference,
            CodingSchemeVersion codingSchemeVersion) throws LBParameterException, LBInvocationException {
        if(codingSchemeVersion.getVersion() != null) {
            try {
                return getNciHistoryService().getEditActionList(uri, conceptReference, this.dateFormat.parse(codingSchemeVersion.getVersion()));
            } catch (ParseException e) {
                throw new LBParameterException(e.getMessage());
            }
        } else {
            try {
                return getNciHistoryService().getEditActionList(uri, conceptReference, new URI(codingSchemeVersion.getReleaseURN()));
            } catch (URISyntaxException e) {
                throw new LBParameterException(e.getMessage());
            }
        }
    }

    @Override
    public NCIChangeEventList getEditActionList(ConceptReference conceptReference, Date beginDate, Date endDate)
            throws LBParameterException, LBInvocationException {
        return getNciHistoryService().getEditActionList(uri, conceptReference, beginDate, endDate);
    }

    @Override
    public NCIChangeEventList getEditActionList(ConceptReference conceptReference, URI releaseURN)
            throws LBParameterException, LBInvocationException {
        return getNciHistoryService().getEditActionList(uri, conceptReference, releaseURN);
    }

    @Override
    public SystemRelease getLatestBaseline() throws LBInvocationException {
        return getNciHistoryService().getLatestBaseLine(uri);
    }

    @Override
    public SystemReleaseDetail getSystemRelease(URI releaseURN) throws LBParameterException, LBInvocationException {
        return getNciHistoryService().getSystemRelease(uri, releaseURN);
    }

}
