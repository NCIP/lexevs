
package org.LexGrid.LexBIG.Impl.History;

import java.net.URI;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeVersionList;
import org.LexGrid.LexBIG.DataModel.Collections.NCIChangeEventList;
import org.LexGrid.LexBIG.DataModel.Collections.SystemReleaseList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.SystemReleaseDetail;
import org.LexGrid.LexBIG.DataModel.NCIHistory.NCIChangeEvent;
import org.LexGrid.LexBIG.DataModel.NCIHistory.types.ChangeType;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.History.HistoryService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.versions.CodingSchemeVersion;
import org.LexGrid.versions.EntityVersion;
import org.LexGrid.versions.SystemRelease;
import org.lexevs.dao.database.connection.SQLHistoryInterface;
import org.lexevs.exceptions.UnexpectedInternalError;
import org.lexevs.logging.LoggerFactory;
import org.lexevs.system.ResourceManager;

public class UMLSHistorySQLQueries {

    protected static LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }

    public static SystemReleaseList getBaseLines(String urn, Date releasedAfter, Date releasedBefore)
            throws UnexpectedInternalError {
        SystemReleaseList result;
        try {
            PreparedStatement getBaseLines = null;
            SQLHistoryInterface si = ResourceManager.instance().getSQLInterfaceForHistory(urn);
            SQLTableConstants stc = si.getSQLTableUtilities().getSQLTableConstants();
            result = new SystemReleaseList();

            try {
                StringBuffer query = new StringBuffer();
                query.append("SELECT * FROM " + si.getTableName(SQLTableConstants.SYSTEM_RELEASE));

                if (releasedAfter != null || releasedBefore != null) {
                    query.append(" WHERE ");
                }

                if (releasedAfter != null) {
                    query.append(SQLTableConstants.TBLCOL_RELEASEDATE + " >= ? ");
                    if (releasedBefore != null) {
                        query.append(" AND ");
                    }
                }

                if (releasedBefore != null) {
                    query.append(SQLTableConstants.TBLCOL_RELEASEDATE + " <= ?");
                }
                query.append(" ORDER BY " + SQLTableConstants.TBLCOL_RELEASEDATE + " ");

                getBaseLines = si.checkOutPreparedStatement(query.toString());

                int i = 1;
                if (releasedAfter != null) {
                    getBaseLines.setTimestamp(i++, new java.sql.Timestamp(releasedAfter.getTime()));
                }
                if (releasedBefore != null) {
                    getBaseLines.setTimestamp(i++, new java.sql.Timestamp(releasedBefore.getTime()));
                }

                ResultSet results = getBaseLines.executeQuery();

                while (results.next()) {
                    result.addSystemRelease(makeSystemReleaseFromQuery(results, stc));
                }

                results.close();

            } catch (Exception e) {
                throw new UnexpectedInternalError("There was an unexpected internal error.", e);
            } finally {
                si.checkInPreparedStatement(getBaseLines);
            }
        } catch (LBParameterException e) {
            throw new UnexpectedInternalError("Problem getting the sql interface for " + urn, e);
        }

        return result;
    }

    private static SystemRelease makeSystemReleaseFromQuery(ResultSet results, SQLTableConstants stc)
            throws SQLException {
        SystemRelease temp = new SystemRelease();

        temp.setEntityDescription(Constructors.createEntityDescription(results
                .getString(SQLTableConstants.TBLCOL_ENTITYDESCRIPTION)));
        temp.setReleaseAgency(results.getString(SQLTableConstants.TBLCOL_RELEASEAGENCY));
        temp.setReleaseDate(new Date(results.getTimestamp(SQLTableConstants.TBLCOL_RELEASEDATE).getTime()));
        temp.setReleaseId(results.getString(SQLTableConstants.TBLCOL_RELEASEID));
        temp.setReleaseURI(results.getString(stc.releaseURNOrreleaseURI));
        temp.setBasedOnRelease(results.getString(SQLTableConstants.TBLCOL_BASEDONRELEASE));

        return temp;
    }

    public static SystemRelease getEarliestBaseLine(String urn) throws UnexpectedInternalError {
        SystemRelease result;
        try {
            PreparedStatement getBaseLines = null;
            SQLHistoryInterface si = ResourceManager.instance().getSQLInterfaceForHistory(urn);
            SQLTableConstants stc = si.getSQLTableUtilities().getSQLTableConstants();
            result = null;

            try {
                StringBuffer query = new StringBuffer();
                query.append("SELECT * FROM " + si.getTableName(SQLTableConstants.SYSTEM_RELEASE));
                query.append(" ORDER BY " + SQLTableConstants.TBLCOL_RELEASEDATE + " ");

                getBaseLines = si.checkOutPreparedStatement(query.toString());
                getBaseLines.setMaxRows(1);

                ResultSet results = getBaseLines.executeQuery();

                if (results.next()) {
                    result = makeSystemReleaseFromQuery(results, stc);
                } else {
                    throw new UnexpectedInternalError("The history table is empty");
                }

                results.close();

            } catch (UnexpectedInternalError e) {
                throw e;
            } catch (Exception e) {
                throw new UnexpectedInternalError("There was an unexpected internal error.", e);
            } finally {
                si.checkInPreparedStatement(getBaseLines);
            }
        } catch (LBParameterException e) {
            throw new UnexpectedInternalError("Problem getting the sql interface for " + urn, e);
        }

        return result;
    }

    public static SystemRelease getLatestBaseLine(String urn) throws UnexpectedInternalError {
        SystemRelease result;
        try {
            PreparedStatement getBaseLines = null;
            SQLHistoryInterface si = ResourceManager.instance().getSQLInterfaceForHistory(urn);
            SQLTableConstants stc = si.getSQLTableUtilities().getSQLTableConstants();
            result = null;

            try {
                StringBuffer query = new StringBuffer();
                query.append("SELECT * FROM " + si.getTableName(SQLTableConstants.SYSTEM_RELEASE));
                query.append(" ORDER BY " + SQLTableConstants.TBLCOL_RELEASEDATE + " desc");

                getBaseLines = si.checkOutPreparedStatement(query.toString());
                getBaseLines.setMaxRows(1);

                ResultSet results = getBaseLines.executeQuery();

                if (results.next()) {
                    result = makeSystemReleaseFromQuery(results, stc);
                } else {
                    throw new UnexpectedInternalError("The history table is empty");
                }

                results.close();

            } catch (UnexpectedInternalError e) {
                throw e;
            } catch (Exception e) {
                throw new UnexpectedInternalError("There was an unexpected internal error.", e);
            } finally {
                si.checkInPreparedStatement(getBaseLines);
            }
        } catch (LBParameterException e) {
            throw new UnexpectedInternalError("Problem getting the sql interface for " + urn, e);
        }

        return result;
    }

    public static SystemReleaseDetail getSystemRelease(String urn, URI releaseURN) throws UnexpectedInternalError,
            LBParameterException {
        SystemReleaseDetail result = new SystemReleaseDetail();
        PreparedStatement getDetail = null;
        SQLHistoryInterface si = null;
        SQLTableConstants stc = null;

        try {
            si = ResourceManager.instance().getSQLInterfaceForHistory(urn);
            stc = si.getSQLTableUtilities().getSQLTableConstants();
        } catch (LBParameterException e) {
            throw new UnexpectedInternalError("Problem getting the sql interface for " + urn, e);
        }

        try {
            String temp = releaseURN.toString();

            if (temp.indexOf(HistoryService.metaURN + ":") == -1) {
                throw new LBParameterException("Invalid parameter", stc.releaseURNOrreleaseURI, releaseURN.toString());
            }

            SystemRelease sr = getSystemReleaseForVersion(urn, releaseURN);

            Date startDate = getDateForPreviousRelease(urn, sr.getReleaseDate());

            StringBuffer query = new StringBuffer();
            query.append("SELECT DISTINCT " + SQLTableConstants.TBLCOL_EDITDATE + " FROM "
                    + si.getTableName(SQLTableConstants.CONCEPT_HISTORY));
            query.append(" WHERE " + SQLTableConstants.TBLCOL_EDITDATE + " <= ?");
            if (startDate != null) {
                query.append(" AND " + SQLTableConstants.TBLCOL_EDITDATE + " >= ?");
            }

            query.append(" ORDER BY " + SQLTableConstants.TBLCOL_EDITDATE + "");

            getDetail = si.checkOutPreparedStatement(query.toString());
            getDetail.setTimestamp(1, new java.sql.Timestamp(sr.getReleaseDate().getTime()));
            if (startDate != null) {
                getDetail.setTimestamp(2, new java.sql.Timestamp(startDate.getTime()));
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");

            ResultSet results = getDetail.executeQuery();

            while (results.next()) {
                EntityVersion ev = new EntityVersion();
                ev.setIsComplete(new Boolean(false));
                ev.setEntityDescription(sr.getEntityDescription());
                ev.setReleaseURN(sr.getReleaseURI());
                Date editDate = new Date(results.getTimestamp(SQLTableConstants.TBLCOL_EDITDATE).getTime());
                ev.setVersion(dateFormat.format(editDate).toUpperCase());
                ev.setVersionDate(sr.getReleaseDate());

                result.addEntityVersions(ev);
            }
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            throw new UnexpectedInternalError("There was an unexpected internal error.", e);
        } finally {
            si.checkInPreparedStatement(getDetail);
        }

        return result;
    }

    private static SystemRelease getSystemReleaseForVersion(String urn, URI releaseURN) throws UnexpectedInternalError {
        SystemRelease result;
        try {
            PreparedStatement getBaseLines = null;
            SQLHistoryInterface si = ResourceManager.instance().getSQLInterfaceForHistory(urn);
            SQLTableConstants stc = si.getSQLTableUtilities().getSQLTableConstants();
            result = null;

            try {
                StringBuffer query = new StringBuffer();
                query.append("SELECT * FROM " + si.getTableName(SQLTableConstants.SYSTEM_RELEASE));
                query.append(" WHERE " + stc.releaseURNOrreleaseURI + " = ?");

                getBaseLines = si.checkOutPreparedStatement(query.toString());
                getBaseLines.setMaxRows(1);
                getBaseLines.setString(1, releaseURN.toString());

                ResultSet results = getBaseLines.executeQuery();

                if (results.next()) {
                    result = makeSystemReleaseFromQuery(results, stc);
                } else {
                    throw new LBParameterException("No System Release could be found for ",
                            SQLTableConstants.TBLCOL_RELEASEID, releaseURN.toString());
                }

                results.close();

            } catch (LBParameterException e) {
                throw e;
            } catch (Exception e) {
                throw new UnexpectedInternalError("There was an unexpected internal error.", e);
            } finally {
                si.checkInPreparedStatement(getBaseLines);
            }
        } catch (LBParameterException e) {
            throw new UnexpectedInternalError("Problem getting the sql interface for " + urn, e);
        }

        return result;
    }

    private static Date getDateForPreviousRelease(String urn, Date previousDate) throws UnexpectedInternalError {
        try {
            PreparedStatement getPrevious = null;
            SQLHistoryInterface si = ResourceManager.instance().getSQLInterfaceForHistory(urn);

            try {
                StringBuffer query = new StringBuffer();
                query.append("SELECT " + SQLTableConstants.TBLCOL_RELEASEDATE + " FROM "
                        + si.getTableName(SQLTableConstants.SYSTEM_RELEASE));
                query.append(" WHERE " + SQLTableConstants.TBLCOL_RELEASEDATE + " < ?");
                query.append(" ORDER BY " + SQLTableConstants.TBLCOL_RELEASEDATE + " desc");

                getPrevious = si.checkOutPreparedStatement(query.toString());
                getPrevious.setMaxRows(1);
                getPrevious.setTimestamp(1, new java.sql.Timestamp(previousDate.getTime()));

                ResultSet results = getPrevious.executeQuery();

                if (results.next()) {
                    return new Date(results.getTimestamp(SQLTableConstants.TBLCOL_RELEASEDATE).getTime());
                } else {
                    return null;
                }
            } catch (Exception e) {
                throw new UnexpectedInternalError("There was an unexpected internal error.", e);
            } finally {
                si.checkInPreparedStatement(getPrevious);
            }
        } catch (LBParameterException e) {
            throw new UnexpectedInternalError("Problem getting the sql interface for " + urn, e);
        }
    }

    public static NCIChangeEventList getDescendants(String urn, ConceptReference conceptReference)
            throws LBParameterException, UnexpectedInternalError {
        NCIChangeEventList result = new NCIChangeEventList();
        SQLHistoryInterface si = null;
        SQLTableConstants stc_ = null;
        PreparedStatement getDescendants = null;

        if (conceptReference != null && conceptReference.getConceptCode() != null
                && conceptReference.getConceptCode().length() > 0) {
            validateCodingScheme(conceptReference);
        } else {
            throw new LBParameterException("The Concept Reference is required", "conceptReference");
        }

        try {
            si = ResourceManager.instance().getSQLInterfaceForHistory(urn);
            stc_ = si.getSQLTableUtilities().getSQLTableConstants();
        } catch (LBParameterException e) {
            throw new UnexpectedInternalError("Problem getting the sql interface for " + urn, e);
        }

        try {
            StringBuffer query = new StringBuffer();
            query.append("SELECT * FROM " + si.getTableName(SQLTableConstants.CONCEPT_HISTORY));
            query.append(" WHERE " + stc_.entityCodeOrEntityId + " = ? ");
            query.append(" AND (" + SQLTableConstants.TBLCOL_EDITACTION + " = ?)");

            getDescendants = si.checkOutPreparedStatement(query.toString());

            getDescendants.setString(1, conceptReference.getConceptCode());
            getDescendants.setString(2, "merge");

            ResultSet results = getDescendants.executeQuery();
            while (results.next()) {
                NCIChangeEvent ce = new NCIChangeEvent();
                ce.setConceptcode(results.getString(stc_.entityCodeOrEntityId));
                ce.setConceptName(results.getString(SQLTableConstants.TBLCOL_CONCEPTNAME));
                ce.setEditaction(getChangeType(results.getString(SQLTableConstants.TBLCOL_EDITACTION)));
                ce.setEditDate(new java.util.Date(results.getTimestamp(SQLTableConstants.TBLCOL_EDITDATE).getTime()));
                ce.setReferencecode(results.getString(SQLTableConstants.TBLCOL_REFERENCECODE));
                ce.setReferencename(results.getString(SQLTableConstants.TBLCOL_REFERENCENAME));

                result.addEntry(ce);
            }
        } catch (Exception e) {
            throw new UnexpectedInternalError("There was an unexpected internal error.", e);
        } finally {
            si.checkInPreparedStatement(getDescendants);
        }

        return result;
    }

    public static NCIChangeEventList getAncestors(String urn, ConceptReference conceptReference)
            throws LBParameterException, UnexpectedInternalError {
        NCIChangeEventList result = new NCIChangeEventList();
        SQLHistoryInterface si = null;
        SQLTableConstants stc_ = null;
        PreparedStatement getDescendants = null;

        if (conceptReference != null && conceptReference.getConceptCode() != null
                && conceptReference.getConceptCode().length() > 0) {
            validateCodingScheme(conceptReference);
        } else {
            throw new LBParameterException("The Concept Reference is required", "conceptReference");
        }

        try {
            si = ResourceManager.instance().getSQLInterfaceForHistory(urn);
            stc_ = si.getSQLTableUtilities().getSQLTableConstants();
        } catch (LBParameterException e) {
            throw new UnexpectedInternalError("Problem getting the sql interface for " + urn, e);
        }

        try {
            StringBuffer query = new StringBuffer();
            query.append("SELECT * FROM " + si.getTableName(SQLTableConstants.CONCEPT_HISTORY));
            query.append(" WHERE " + SQLTableConstants.TBLCOL_REFERENCECODE + " = ? ");
            query.append(" AND ( " + SQLTableConstants.TBLCOL_EDITACTION + " = ? )");

            getDescendants = si.checkOutPreparedStatement(query.toString());

            getDescendants.setString(1, conceptReference.getConceptCode());
            getDescendants.setString(2, "merge");

            ResultSet results = getDescendants.executeQuery();
            while (results.next()) {
                NCIChangeEvent ce = new NCIChangeEvent();
                ce.setConceptcode(results.getString(stc_.entityCodeOrEntityId));
                ce.setConceptName(results.getString(SQLTableConstants.TBLCOL_CONCEPTNAME));
                ce.setEditaction(getChangeType(results.getString(SQLTableConstants.TBLCOL_EDITACTION)));
                ce.setEditDate(new java.util.Date(results.getTimestamp(SQLTableConstants.TBLCOL_EDITDATE).getTime()));
                ce.setReferencecode(results.getString(SQLTableConstants.TBLCOL_REFERENCECODE));
                ce.setReferencename(results.getString(SQLTableConstants.TBLCOL_REFERENCENAME));

                result.addEntry(ce);
            }
        } catch (Exception e) {
            throw new UnexpectedInternalError("There was an unexpected internal error.", e);
        } finally {
            si.checkInPreparedStatement(getDescendants);
        }

        return result;
    }

    public static CodingSchemeVersion getConceptCreationVersion(String urn, ConceptReference conceptReference)
            throws UnexpectedInternalError, LBParameterException {
        CodingSchemeVersion result = new CodingSchemeVersion();
        SQLHistoryInterface si = null;
        SQLTableConstants stc_ = null;
        PreparedStatement getCreationVersion = null;
        PreparedStatement getReleaseInfo = null;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");

        if (conceptReference == null || conceptReference.getConceptCode() == null
                || conceptReference.getConceptCode().length() == 0) {
            throw new LBParameterException("The concept code parameter in the supplied conceptReference is missing");
        }

        // this throws the necessary exceptions
        validateCodingScheme(conceptReference);

        try {
            si = ResourceManager.instance().getSQLInterfaceForHistory(urn);
            stc_ = si.getSQLTableUtilities().getSQLTableConstants();
        } catch (LBParameterException e) {
            throw new UnexpectedInternalError("Problem getting the sql interface for " + urn, e);
        }

        try {
            StringBuffer query = new StringBuffer();
            query.append("SELECT " + SQLTableConstants.TBLCOL_EDITDATE + " FROM "
                    + si.getTableName(SQLTableConstants.CONCEPT_HISTORY));
            query.append(" WHERE " + stc_.entityCodeOrEntityId + " = ?");
            query.append(" AND " + SQLTableConstants.TBLCOL_EDITACTION + " = ?");

            getCreationVersion = si.checkOutPreparedStatement(query.toString());

            getCreationVersion.setString(1, conceptReference.getConceptCode());
            getCreationVersion.setString(2, "create");

            Date createDate;
            ResultSet results = getCreationVersion.executeQuery();
            if (results.next()) {
                result.setIsComplete(new Boolean(false));
                createDate = new Date(results.getTimestamp(SQLTableConstants.TBLCOL_EDITDATE).getTime());
                result.setVersion(dateFormat.format(createDate).toUpperCase());
            } else {
                throw new LBParameterException("No create date could be found for the concept.  Is it valid?",
                        stc_.entityCodeOrEntityId, conceptReference.getConceptCode());
            }

            results.close();

            // get some additional info from the release info table...

            query = new StringBuffer();
            query.append("Select " + stc_.releaseURNOrreleaseURI + ", " + SQLTableConstants.TBLCOL_RELEASEDATE + ", "
                    + SQLTableConstants.TBLCOL_ENTITYDESCRIPTION + " from "
                    + si.getTableName(SQLTableConstants.SYSTEM_RELEASE));
            query.append(" WHERE " + SQLTableConstants.TBLCOL_RELEASEDATE + " >= ? ");
            query.append(" ORDER BY " + SQLTableConstants.TBLCOL_RELEASEDATE + "");

            getReleaseInfo = si.checkOutPreparedStatement(query.toString());
            getReleaseInfo.setMaxRows(1);
            getReleaseInfo.setTimestamp(1, new java.sql.Timestamp(createDate.getTime()));

            results = getReleaseInfo.executeQuery();
            if (results.next()) {
                result.setReleaseURN(results.getString(stc_.releaseURNOrreleaseURI));
                result.setEntityDescription(Constructors.createEntityDescription(results
                        .getString(SQLTableConstants.TBLCOL_ENTITYDESCRIPTION)));
                result.setVersionDate(new Date(results.getTimestamp(SQLTableConstants.TBLCOL_RELEASEDATE).getTime()));
            }
            results.close();

        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            throw new UnexpectedInternalError("There was an unexpected internal error.", e);
        } finally {
            si.checkInPreparedStatement(getCreationVersion);
            si.checkInPreparedStatement(getReleaseInfo);
        }

        return result;
    }

    public static CodingSchemeVersionList getConceptChangeVersions(String urn, ConceptReference conceptReference,
            Date beginDate, Date endDate) throws UnexpectedInternalError, LBParameterException {
        CodingSchemeVersionList result = new CodingSchemeVersionList();
        SQLHistoryInterface si = null;
        SQLTableConstants stc_ = null;
        PreparedStatement getVersion = null;
        PreparedStatement getReleaseInfo = null;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");

        if (conceptReference == null || conceptReference.getConceptCode() == null
                || conceptReference.getConceptCode().length() == 0) {
            throw new LBParameterException("The concept code parameter in the supplied conceptReference is missing");
        }

        validateCodingScheme(conceptReference);

        try {
            si = ResourceManager.instance().getSQLInterfaceForHistory(urn);
            stc_ = si.getSQLTableUtilities().getSQLTableConstants();
        } catch (LBParameterException e) {
            throw new UnexpectedInternalError("Problem getting the sql interface for " + urn, e);
        }

        try {
            StringBuffer query = new StringBuffer();
            query.append("SELECT " + SQLTableConstants.TBLCOL_EDITDATE + " FROM "
                    + si.getTableName(SQLTableConstants.CONCEPT_HISTORY));
            query.append(" WHERE " + stc_.entityCodeOrEntityId + " = ?");

            if (beginDate != null) {
                query.append(" AND " + SQLTableConstants.TBLCOL_EDITDATE + " >= ?");
            }

            if (endDate != null) {
                query.append(" AND " + SQLTableConstants.TBLCOL_EDITDATE + " <= ?");
            }

            getVersion = si.checkOutPreparedStatement(query.toString());

            query = new StringBuffer();
            query.append("SELECT " + SQLTableConstants.TBLCOL_RELEASEID + ", " + SQLTableConstants.TBLCOL_RELEASEDATE
                    + ", " + SQLTableConstants.TBLCOL_ENTITYDESCRIPTION + " FROM "
                    + si.getTableName(SQLTableConstants.SYSTEM_RELEASE));
            query.append(" WHERE " + SQLTableConstants.TBLCOL_RELEASEDATE + " >= ? ");
            query.append(" ORDER BY " + SQLTableConstants.TBLCOL_RELEASEDATE + "");

            getReleaseInfo = si.checkOutPreparedStatement(query.toString());
            getReleaseInfo.setMaxRows(1);

            getVersion.setString(1, conceptReference.getConceptCode());
            int i = 2;
            if (beginDate != null) {
                getVersion.setTimestamp(i++, new java.sql.Timestamp(beginDate.getTime()));
            }
            if (endDate != null) {
                getVersion.setTimestamp(i++, new java.sql.Timestamp(endDate.getTime()));
            }

            ResultSet results = getVersion.executeQuery();
            while (results.next()) {
                CodingSchemeVersion csVersion = new CodingSchemeVersion();

                csVersion.setIsComplete(new Boolean(false));
                Date createDate = new Date(results.getTimestamp(SQLTableConstants.TBLCOL_EDITDATE).getTime());
                csVersion.setVersion(dateFormat.format(createDate).toUpperCase());
                // get some additional info from the release info table...

                getReleaseInfo.setTimestamp(1, new java.sql.Timestamp(createDate.getTime()));

                ResultSet innerResults = getReleaseInfo.executeQuery();
                if (innerResults.next()) {
                    csVersion.setReleaseURN(HistoryService.metaURN + ":"
                            + innerResults.getString(SQLTableConstants.TBLCOL_RELEASEID));
                    csVersion.setEntityDescription(Constructors.createEntityDescription(innerResults
                            .getString(SQLTableConstants.TBLCOL_ENTITYDESCRIPTION)));
                    csVersion.setVersionDate(new Date(innerResults.getTimestamp(SQLTableConstants.TBLCOL_RELEASEDATE)
                            .getTime()));
                }
                innerResults.close();
                result.addEntry(csVersion);
            }

            results.close();

        } catch (Exception e) {
            throw new UnexpectedInternalError("There was an unexpected internal error.", e);
        } finally {
            si.checkInPreparedStatement(getVersion);
            si.checkInPreparedStatement(getReleaseInfo);
        }

        return result;
    }

    public static NCIChangeEventList getEditActionList(String urn, ConceptReference conceptReference,
            CodingSchemeVersion codingSchemeVersion) throws LBParameterException, UnexpectedInternalError {
        NCIChangeEventList result = new NCIChangeEventList();
        SQLHistoryInterface si = null;
        SQLTableConstants stc_ = null;
        PreparedStatement getChanges = null;
        Timestamp releaseDate = null;

        // SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");

        if (codingSchemeVersion == null || codingSchemeVersion.getVersion() == null
                || codingSchemeVersion.getVersion().length() == 0) {
            throw new LBParameterException("The version parameter in the supplied codingSchemeVersion is missing");
        }

        try {
            si = ResourceManager.instance().getSQLInterfaceForHistory(urn);
            stc_ = si.getSQLTableUtilities().getSQLTableConstants();
        } catch (LBParameterException e) {
            throw new UnexpectedInternalError("Problem getting the sql interface for " + urn, e);
        }

        StringBuffer query = new StringBuffer();
        query.append("SELECT * FROM " + si.getTableName(SQLTableConstants.SYSTEM_RELEASE));
        query.append(" WHERE " + SQLTableConstants.TBLCOL_RELEASEID + " = ?");

        PreparedStatement prepStmt;
        try {
            prepStmt = si.checkOutPreparedStatement(query.toString());

            prepStmt.setString(1, codingSchemeVersion.getVersion());
            ResultSet sysRelease = prepStmt.executeQuery();

            if (sysRelease.next()) {
                releaseDate = sysRelease.getTimestamp(SQLTableConstants.TBLCOL_RELEASEDATE);
            }
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        /*
         * Date date; try { date =
         * dateFormat.parse(codingSchemeVersion.getVersion()); } catch
         * (ParseException e) { throw new LBParameterException("The version
         * parameter in the supplied codingSchemeVersion was invalid",
         * "version", codingSchemeVersion.getVersion()); }
         */

        boolean useConcept = false;
        if (conceptReference != null && conceptReference.getConceptCode() != null
                && conceptReference.getConceptCode().length() > 0) {
            useConcept = true;
            validateCodingScheme(conceptReference);
        }

        try {

            query = new StringBuffer();
            query.append("SELECT * FROM " + si.getTableName(SQLTableConstants.CONCEPT_HISTORY));
            query.append(" WHERE " + SQLTableConstants.TBLCOL_EDITDATE + " = ?");

            if (useConcept) {
                query.append(" AND (" + stc_.entityCodeOrEntityId + " = ? OR " + SQLTableConstants.TBLCOL_REFERENCECODE
                        + " = ?)");
            }

            getChanges = si.checkOutPreparedStatement(query.toString());

            getChanges.setTimestamp(1, releaseDate);

            if (useConcept) {
                getChanges.setString(2, conceptReference.getConceptCode());
                getChanges.setString(3, conceptReference.getConceptCode());
            }

            ResultSet results = getChanges.executeQuery();
            while (results.next()) {
                NCIChangeEvent ce = new NCIChangeEvent();
                ce.setConceptcode(results.getString(stc_.entityCodeOrEntityId));
                ce.setConceptName(results.getString(SQLTableConstants.TBLCOL_CONCEPTNAME));
                ce.setEditaction(getChangeType(results.getString(SQLTableConstants.TBLCOL_EDITACTION)));
                ce.setEditDate(new java.util.Date(results.getTimestamp(SQLTableConstants.TBLCOL_EDITDATE).getTime()));
                ce.setReferencecode(results.getString(SQLTableConstants.TBLCOL_REFERENCECODE));
                ce.setReferencename(results.getString(SQLTableConstants.TBLCOL_REFERENCENAME));

                result.addEntry(ce);
            }
        } catch (Exception e) {
            throw new UnexpectedInternalError("There was an unexpected internal error.", e);
        } finally {
            si.checkInPreparedStatement(getChanges);
        }

        return result;
    }

    public static NCIChangeEventList getEditActionList(String urn, ConceptReference conceptReference, Date beginDate,
            Date endDate) throws LBParameterException, UnexpectedInternalError {
        NCIChangeEventList result = new NCIChangeEventList();
        SQLHistoryInterface si = null;
        SQLTableConstants stc_ = null;
        PreparedStatement getChanges = null;

        if (conceptReference == null || conceptReference.getConceptCode() == null
                || conceptReference.getConceptCode().length() == 0) {
            throw new LBParameterException("The concept code parameter in the supplied conceptReference is missing");
        }

        validateCodingScheme(conceptReference);

        try {
            si = ResourceManager.instance().getSQLInterfaceForHistory(urn);
            stc_ = si.getSQLTableUtilities().getSQLTableConstants();
        } catch (LBParameterException e) {
            throw new UnexpectedInternalError("Problem getting the sql interface for " + urn, e);
        }

        try {
            StringBuffer query = new StringBuffer();
            query.append("SELECT * FROM " + si.getTableName(SQLTableConstants.CONCEPT_HISTORY));
            query.append(" WHERE (" + stc_.entityCodeOrEntityId + " = ? OR " + SQLTableConstants.TBLCOL_REFERENCECODE
                    + " = ?)");

            if (beginDate != null) {
                query.append(" AND " + SQLTableConstants.TBLCOL_EDITDATE + " >= ?");
            }

            if (endDate != null) {
                query.append(" AND " + SQLTableConstants.TBLCOL_EDITDATE + " <= ?");
            }

            getChanges = si.checkOutPreparedStatement(query.toString());

            getChanges.setString(1, conceptReference.getConceptCode());
            getChanges.setString(2, conceptReference.getConceptCode());
            int i = 3;
            if (beginDate != null) {
                getChanges.setTimestamp(i++, new java.sql.Timestamp(beginDate.getTime()));
            }
            if (endDate != null) {
                getChanges.setTimestamp(i++, new java.sql.Timestamp(endDate.getTime()));
            }

            ResultSet results = getChanges.executeQuery();
            while (results.next()) {
                NCIChangeEvent ce = new NCIChangeEvent();
                ce.setConceptcode(results.getString(stc_.entityCodeOrEntityId));
                ce.setConceptName(results.getString(SQLTableConstants.TBLCOL_CONCEPTNAME));
                ce.setEditaction(getChangeType(results.getString(SQLTableConstants.TBLCOL_EDITACTION)));
                ce.setEditDate(new java.util.Date(results.getTimestamp(SQLTableConstants.TBLCOL_EDITDATE).getTime()));
                ce.setReferencecode(results.getString(SQLTableConstants.TBLCOL_REFERENCECODE));
                ce.setReferencename(results.getString(SQLTableConstants.TBLCOL_REFERENCENAME));

                result.addEntry(ce);
            }
        } catch (Exception e) {
            throw new UnexpectedInternalError("There was an unexpected internal error.", e);
        } finally {
            si.checkInPreparedStatement(getChanges);
        }

        return result;
    }

    @SuppressWarnings("null")
    public static NCIChangeEventList getEditActionList(String urn, ConceptReference conceptReference, URI releaseURN)
            throws LBParameterException, UnexpectedInternalError {
        NCIChangeEventList result = new NCIChangeEventList();
        PreparedStatement getChanges = null;
        SQLHistoryInterface si = null;
        SQLTableConstants stc_ = null;

        try {
            si = ResourceManager.instance().getSQLInterfaceForHistory(urn);
            stc_ = si.getSQLTableUtilities().getSQLTableConstants();
        } catch (LBParameterException e) {
            throw new UnexpectedInternalError("Problem getting the sql interface for " + urn, e);
        }

        try {
            if (releaseURN == null) {
                throw new LBParameterException("The " + stc_.releaseURNOrreleaseURI + " is required");
            }
            String temp = releaseURN.toString();

            if (temp.indexOf(HistoryService.metaURN + ":") == -1) {
                throw new LBParameterException("Invalid parameter", stc_.releaseURNOrreleaseURI, releaseURN.toString());
            }

            SystemRelease sr = getSystemReleaseForVersion(urn, releaseURN);
            if (sr == null) {
                throw new LBParameterException("Version information not available", stc_.releaseURNOrreleaseURI,
                        releaseURN.toString());
            }

            boolean useConcept = false;
            if (conceptReference != null && conceptReference.getConceptCode() != null
                    && conceptReference.getConceptCode().length() > 0)

            {
                useConcept = true;
                validateCodingScheme(conceptReference);
            }

            String lastURN = getReleaseURNForPreviousRelease(urn, sr.getReleaseDate());
            SystemRelease lastRelease = null;
            if (lastURN != null) {
                lastRelease = getSystemReleaseForVersion(urn, new URI(lastURN));
            }

            StringBuffer query = new StringBuffer();
            query.append("SELECT * FROM " + si.getTableName(SQLTableConstants.CONCEPT_HISTORY));
            query.append(" WHERE " + SQLTableConstants.TBLCOL_EDITDATE + " <= ?");
            if (lastRelease != null) {
                query.append(" AND " + SQLTableConstants.TBLCOL_EDITDATE + " >= ?");
            }
            if (useConcept) {
                query.append(" AND (" + stc_.entityCodeOrEntityId + " = ? OR " + SQLTableConstants.TBLCOL_REFERENCECODE
                        + " = ?)");
            }

            getChanges = si.checkOutPreparedStatement(query.toString());
            int i = 1;

            getChanges.setTimestamp(i++, new java.sql.Timestamp(sr.getReleaseDate().getTime()));
            if (lastRelease != null) {
                getChanges.setTimestamp(i++, new java.sql.Timestamp(lastRelease.getReleaseDate().getTime()));
            }
            if (useConcept) {
                getChanges.setString(i++, conceptReference.getConceptCode());
                getChanges.setString(i++, conceptReference.getConceptCode());
            }

            ResultSet results = getChanges.executeQuery();
            while (results.next()) {
                NCIChangeEvent ce = new NCIChangeEvent();
                ce.setConceptcode(results.getString(stc_.entityCodeOrEntityId));
                ce.setConceptName(results.getString(SQLTableConstants.TBLCOL_CONCEPTNAME));
                ce.setEditaction(getChangeType(results.getString(SQLTableConstants.TBLCOL_EDITACTION)));
                ce.setEditDate(new java.util.Date(results.getTimestamp(SQLTableConstants.TBLCOL_EDITDATE).getTime()));
                ce.setReferencecode(results.getString(SQLTableConstants.TBLCOL_REFERENCECODE));
                ce.setReferencename(results.getString(SQLTableConstants.TBLCOL_REFERENCENAME));

                result.addEntry(ce);
            }

        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            throw new UnexpectedInternalError("There was an unexpected internal error.", e);
        } finally {
            si.checkInPreparedStatement(getChanges);
        }

        return result;
    }

    /**
     * If its not null, this makes sure that the coding scheme is set properly.
     * 
     * @param conceptReference
     * @throws LBParameterException
     */
    private static void validateCodingScheme(ConceptReference conceptReference) throws LBParameterException {
        if (conceptReference.getCodingSchemeName() != null && conceptReference.getCodingSchemeName().length() > 0) {
            String codeUrn;
            try {
                codeUrn = ResourceManager.instance().getURNForExternalCodingSchemeName(
                        conceptReference.getCodingSchemeName());
            } catch (LBParameterException e) {
                // if we couldn't map it, it still may be ok if they provided
                // the correct urn
                codeUrn = conceptReference.getCodingSchemeName();
            }
            if (!codeUrn.equals(HistoryService.metaURN)) {
                throw new LBParameterException("Unknown coding scheme - only coding schemes which map to "
                        + HistoryService.metaURN + " are supported.", "codingScheme", conceptReference
                        .getCodingSchemeName());
            }
        }
    }

    private static ChangeType getChangeType(String change) {
        if (change.equals("merge")) {
            return ChangeType.MERGE;
        } else if (change.equals("retire")) {
            return ChangeType.RETIRE;
        } else if (change.equals("modify")) {
            return ChangeType.MODIFY;
        } else if (change.equals("split")) {
            return ChangeType.SPLIT;
        } else {
            getLogger().error(
                    "The UMLS history table has an invalid entry in the '" + SQLTableConstants.TBLCOL_EDITACTION
                            + "' column: " + change);
            return null;
        }
    }

    private static String getReleaseURNForPreviousRelease(String urn, Date previousDate) throws UnexpectedInternalError {
        try {
            PreparedStatement getPrevious = null;
            SQLHistoryInterface si = ResourceManager.instance().getSQLInterfaceForHistory(urn);
            SQLTableConstants stc = si.getSQLTableUtilities().getSQLTableConstants();
            try {
                StringBuffer query = new StringBuffer();
                query.append("SELECT " + stc.releaseURNOrreleaseURI + " FROM "
                        + si.getTableName(SQLTableConstants.SYSTEM_RELEASE));
                query.append(" WHERE " + SQLTableConstants.TBLCOL_RELEASEDATE + " < ?");
                query.append(" ORDER BY " + SQLTableConstants.TBLCOL_RELEASEDATE + " desc");

                getPrevious = si.checkOutPreparedStatement(query.toString());
                getPrevious.setMaxRows(1);
                getPrevious.setTimestamp(1, new java.sql.Timestamp(previousDate.getTime()));

                ResultSet results = getPrevious.executeQuery();

                if (results.next()) {
                    return results.getString(stc.releaseURNOrreleaseURI);
                } else {
                    return null;
                }
            } catch (Exception e) {
                throw new UnexpectedInternalError("There was an unexpected internal error.", e);
            } finally {
                si.checkInPreparedStatement(getPrevious);
            }
        } catch (LBParameterException e) {
            throw new UnexpectedInternalError("Problem getting the sql interface for " + urn, e);
        }
    }
}