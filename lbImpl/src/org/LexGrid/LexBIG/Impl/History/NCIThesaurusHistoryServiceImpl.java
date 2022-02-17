
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
 * The implementation of the history service against the custom NCI Thesaurus
 * history file / table.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class NCIThesaurusHistoryServiceImpl implements HistoryService {
    private static final long serialVersionUID = 5666320190009376793L;
    private String urn_ = null;

    /**
     * This constructor is only here for Apache Axis to work correctly. It
     * should not be used by anyone.
     */
    public NCIThesaurusHistoryServiceImpl() {

    }

    public NCIThesaurusHistoryServiceImpl(String urn) throws LBParameterException {
        ResourceManager rm = ResourceManager.instance();
        try {
            urn_ = urn;
            // make sure we have one for it
            rm.getSQLInterfaceForHistory(urn_);

        } catch (LBParameterException e) {
            throw e;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.History.HistoryService#getBaselines(java.util.Date,
     * java.util.Date)
     */
    public SystemReleaseList getBaselines(Date releasedAfter, Date releasedBefore) throws LBParameterException,
            LBInvocationException {
        try {
            return NCIThesaurusHistorySQLQueries.getBaseLines(urn_, releasedAfter, releasedBefore);
        } catch (UnexpectedInternalError e) {
            throw new LBInvocationException("There was an unexpected internal error", e.getLogId());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.History.HistoryService#getConceptCreationVersion(org
     * .LexGrid.LexBIG.DataModel.Core.ConceptReference)
     */
    public CodingSchemeVersion getConceptCreationVersion(ConceptReference conceptReference)
            throws LBParameterException, LBInvocationException {
        try {
            return NCIThesaurusHistorySQLQueries.getConceptCreationVersion(urn_, conceptReference);
        } catch (UnexpectedInternalError e) {
            throw new LBInvocationException("There was an unexpected internal error", e.getLogId());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.History.HistoryService#getSystemRelease(java.net.URI)
     */
    public SystemReleaseDetail getSystemRelease(URI releaseURN) throws LBParameterException, LBInvocationException {
        try {
            return NCIThesaurusHistorySQLQueries.getSystemRelease(urn_, releaseURN);
        } catch (UnexpectedInternalError e) {
            throw new LBInvocationException("There was an unexpected internal error", e.getLogId());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.History.HistoryService#getConceptChangeVersions(org
     * .LexGrid.LexBIG.DataModel.Core.ConceptReference, java.util.Date,
     * java.util.Date)
     */
    public CodingSchemeVersionList getConceptChangeVersions(ConceptReference conceptReference, Date beginDate,
            Date endDate) throws LBParameterException, LBInvocationException {
        try {
            return NCIThesaurusHistorySQLQueries.getConceptChangeVersions(urn_, conceptReference, beginDate, endDate);
        } catch (UnexpectedInternalError e) {
            throw new LBInvocationException("There was an unexpected internal error", e.getLogId());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.LexGrid.LexBIG.History.HistoryService#getEarliestBaseline()
     */
    public SystemRelease getEarliestBaseline() throws LBInvocationException {
        try {
            return NCIThesaurusHistorySQLQueries.getEarliestBaseLine(urn_);
        } catch (UnexpectedInternalError e) {
            throw new LBInvocationException("There was an unexpected internal error", e.getLogId());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.History.HistoryService#getEditActionList(org.LexGrid
     * .codingSchemes.CodingSchemeVersion)
     */
    public NCIChangeEventList getEditActionList(ConceptReference conceptReference,
            CodingSchemeVersion codingSchemeVersion) throws LBParameterException, LBInvocationException {
        try {
            return NCIThesaurusHistorySQLQueries.getEditActionList(urn_, conceptReference, codingSchemeVersion);
        } catch (UnexpectedInternalError e) {
            throw new LBInvocationException("There was an unexpected internal error", e.getLogId());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.LexGrid.LexBIG.History.HistoryService#getLatestBaseline()
     */
    public SystemRelease getLatestBaseline() throws LBInvocationException {
        try {
            return NCIThesaurusHistorySQLQueries.getLatestBaseLine(urn_);
        } catch (UnexpectedInternalError e) {
            throw new LBInvocationException("There was an unexpected internal error", e.getLogId());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.History.HistoryService#getEditActionList(org.LexGrid
     * .LexBIG.DataModel.Core.ConceptReference, java.util.Date, java.util.Date)
     */
    public NCIChangeEventList getEditActionList(ConceptReference conceptReference, Date beginDate, Date endDate)
            throws LBParameterException, LBInvocationException {
        try {
            return NCIThesaurusHistorySQLQueries.getEditActionList(urn_, conceptReference, beginDate, endDate);
        } catch (UnexpectedInternalError e) {
            throw new LBInvocationException("There was an unexpected internal error", e.getLogId());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.History.HistoryService#getEditActionList(org.LexGrid
     * .LexBIG.DataModel.Core.ConceptReference, java.net.URI)
     */
    public NCIChangeEventList getEditActionList(ConceptReference conceptReference, URI releaseURN)
            throws LBParameterException, LBInvocationException {
        try {
            return NCIThesaurusHistorySQLQueries.getEditActionList(urn_, conceptReference, releaseURN);
        } catch (UnexpectedInternalError e) {
            throw new LBInvocationException("There was an unexpected internal error", e.getLogId());
        }
    }

    public NCIChangeEventList getDescendants(ConceptReference conceptReference) throws LBParameterException,
            LBInvocationException {
        try {
            return NCIThesaurusHistorySQLQueries.getDescendants(urn_, conceptReference);
        } catch (UnexpectedInternalError e) {
            throw new LBInvocationException("There was an unexpected internal error", e.getLogId());
        }
    }

    public NCIChangeEventList getAncestors(ConceptReference conceptReference) throws LBParameterException,
            LBInvocationException {
        try {
            return NCIThesaurusHistorySQLQueries.getAncestors(urn_, conceptReference);
        } catch (UnexpectedInternalError e) {
            throw new LBInvocationException("There was an unexpected internal error", e.getLogId());
        }
    }

    @Override
    public List<String> getCodeListForVersion(String currentVersion) {
        throw new UnsupportedOperationException("This method is unsupported in this history service");
    }

    @Override
    public Date getDateForVersion(String currentVersion) {
        throw new UnsupportedOperationException("This method is unsupported in this history service");
    }

    @Override
    public List<String> getVersionsForDateRange(Date previousDate, Date currentDate) {
        throw new UnsupportedOperationException("This method is unsupported in this history service");
    }

}