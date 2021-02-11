
package org.LexGrid.LexBIG.Impl.History;

import java.net.URI;
import java.util.Date;
import java.util.List;

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
import org.lexevs.exceptions.UnexpectedInternalError;
import org.lexevs.system.ResourceManager;

/**
 * The implementation of the history service against the custom UMLS history
 * file / table.
 * 
 * @author <A>Satyabodha Rao</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class UMLSHistoryServiceImpl implements HistoryService {
    private static final long serialVersionUID = 5666320190009376793L;
    private String urn_ = null;

    /**
     * This constructor is only here for Apache Axis to work correctly. It
     * should not be used by anyone.
     */
    public UMLSHistoryServiceImpl() {

    }

    public UMLSHistoryServiceImpl(String urn) throws LBParameterException {
        ResourceManager rm = ResourceManager.instance();
        try {
            urn_ = urn;
            // make sure we have one for it
            rm.getSQLInterfaceForHistory(urn_);

        } catch (LBParameterException e) {
            throw e;
        }

    }

    public NCIChangeEventList getAncestors(ConceptReference conceptReference) throws LBParameterException,
            LBInvocationException {
        try {
            return UMLSHistorySQLQueries.getAncestors(urn_, conceptReference);
        } catch (UnexpectedInternalError e) {
            throw new LBInvocationException("There was an unexpected internal error", e.getLogId());
        }
    }

    public SystemReleaseList getBaselines(Date releasedAfter, Date releasedBefore) throws LBParameterException,
            LBInvocationException {
        try {
            return UMLSHistorySQLQueries.getBaseLines(urn_, releasedAfter, releasedBefore);
        } catch (UnexpectedInternalError e) {
            throw new LBInvocationException("There was an unexpected internal error", e.getLogId());
        }
    }

    public CodingSchemeVersionList getConceptChangeVersions(ConceptReference conceptReference, Date beginDate,
            Date endDate) throws LBParameterException, LBInvocationException {
        try {
            return UMLSHistorySQLQueries.getConceptChangeVersions(urn_, conceptReference, beginDate, endDate);
        } catch (UnexpectedInternalError e) {
            throw new LBInvocationException("There was an unexpected internal error", e.getLogId());
        }
    }

    public CodingSchemeVersion getConceptCreationVersion(ConceptReference conceptReference)
            throws LBParameterException, LBInvocationException {
        try {
            return UMLSHistorySQLQueries.getConceptCreationVersion(urn_, conceptReference);
        } catch (UnexpectedInternalError e) {
            throw new LBInvocationException("There was an unexpected internal error", e.getLogId());
        }
    }

    public NCIChangeEventList getDescendants(ConceptReference conceptReference) throws LBParameterException,
            LBInvocationException {
        try {
            return UMLSHistorySQLQueries.getDescendants(urn_, conceptReference);
        } catch (UnexpectedInternalError e) {
            throw new LBInvocationException("There was an unexpected internal error", e.getLogId());
        }
    }

    public SystemRelease getEarliestBaseline() throws LBInvocationException {
        try {
            return UMLSHistorySQLQueries.getEarliestBaseLine(urn_);
        } catch (UnexpectedInternalError e) {
            throw new LBInvocationException("There was an unexpected internal error", e.getLogId());
        }
    }

    public NCIChangeEventList getEditActionList(ConceptReference conceptReference,
            CodingSchemeVersion codingSchemeVersion) throws LBParameterException, LBInvocationException {
        try {
            return UMLSHistorySQLQueries.getEditActionList(urn_, conceptReference, codingSchemeVersion);
        } catch (UnexpectedInternalError e) {
            throw new LBInvocationException("There was an unexpected internal error", e.getLogId());
        }
    }

    public NCIChangeEventList getEditActionList(ConceptReference conceptReference, Date beginDate, Date endDate)
            throws LBParameterException, LBInvocationException {
        try {
            return UMLSHistorySQLQueries.getEditActionList(urn_, conceptReference, beginDate, endDate);
        } catch (UnexpectedInternalError e) {
            throw new LBInvocationException("There was an unexpected internal error", e.getLogId());
        }
    }

    public NCIChangeEventList getEditActionList(ConceptReference conceptReference, URI releaseURN)
            throws LBParameterException, LBInvocationException {
        try {
            return UMLSHistorySQLQueries.getEditActionList(urn_, conceptReference, releaseURN);
        } catch (UnexpectedInternalError e) {
            throw new LBInvocationException("There was an unexpected internal error", e.getLogId());
        }
    }

    public SystemRelease getLatestBaseline() throws LBInvocationException {
        try {
            return UMLSHistorySQLQueries.getLatestBaseLine(urn_);
        } catch (UnexpectedInternalError e) {
            throw new LBInvocationException("There was an unexpected internal error", e.getLogId());
        }
    }

    public SystemReleaseDetail getSystemRelease(URI releaseURN) throws LBParameterException, LBInvocationException {
        try {
            return UMLSHistorySQLQueries.getSystemRelease(urn_, releaseURN);
        } catch (UnexpectedInternalError e) {
            throw new LBInvocationException("There was an unexpected internal error", e.getLogId());
        }
    }

    @Override
    public List<String> getCodeListForVersion(String currentVersion) {
        throw new UnsupportedOperationException("Not supported for RRF based terminologies");  
    }

    @Override
    public Date getDateForVersion(String currentVersion) {
        throw new UnsupportedOperationException("Not supported for RRF based terminologies");  
    }

    @Override
    public List<String> getVersionsForDateRange(Date previousDate, Date currentDate) {
        throw new UnsupportedOperationException("Not supported for RRF based terminologies");  
    }
}