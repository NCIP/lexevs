
package edu.mayo.informatics.lexgrid.convert.directConversions;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.util.sql.lgTables.SQLTableUtilities;

import edu.mayo.informatics.lexgrid.convert.exceptions.LgConvertException;

/**
 * @author m057089
 * 
 */
public class HistoryLoadDBUtil {

    /* ---------- systemRelease ---------- */
    private Map<String, String> releaseID_ = new HashMap<String, String>();
    private Map<String, String> releaseURN_ = new HashMap<String, String>();
    private Map<String, String> basedOnRelease_ = new HashMap<String, String>();
    private Map<String, Date> releaseDate_ = new HashMap<String, Date>();
    private Map<String, String> releaseAgency_ = new HashMap<String, String>();
    private Map<String, String> entityDescription_ = new HashMap<String, String>();
    /* --------- conceptHistory ---------- */
    private Map<String, String> entityID_ = new HashMap<String, String>();
    private Map<String, String> conceptName_ = new HashMap<String, String>();
    private Map<String, String> editAction_ = new HashMap<String, String>();
    private Map<String, Date> editDate_ = new HashMap<String, Date>();
    private Map<String, String> referenceCode_ = new HashMap<String, String>();
    private Map<String, String> referenceName_ = new HashMap<String, String>();

    private Map<String, Object> attributeMap_ = new HashMap<String, Object>();
    private StringBuffer whereClause_ = new StringBuffer();
    private SQLTableUtilities tableUtility_ = null;
    private LgMessageDirectorIF message_ = null;
    private Connection sqlConnection_ = null;
    private String dbType_ = null;

    /**
     * Constructor
     * 
     * @param message_
     * 
     * @param tableUtility_
     * @throws Exception
     */
    public HistoryLoadDBUtil(Connection sqlConnection, String tablePrefix, LgMessageDirectorIF message)
            throws Exception {

        sqlConnection_ = sqlConnection;
        dbType_ = sqlConnection_.getMetaData().getDatabaseProductName();
        tableUtility_ = new SQLTableUtilities(sqlConnection, tablePrefix);
        message_ = message;
    }

    /* ---------- systemRelease ---------- */
    public void setSysRelReleaseID(String key, String releaseID) {
        this.releaseID_.put(key, releaseID);
    }

    public void setSysRelReleaseURN(String key, String releaseURN) {
        this.releaseURN_.put(key, releaseURN);
    }

    public void setSysRelBasedOnRelease(String key, String releaseBasedOnRelease) {
        this.basedOnRelease_.put(key, releaseBasedOnRelease);
    }

    public void setSysRelReleaseDate(String key, Date releaseReleaseDate) {
        this.releaseDate_.put(key, releaseReleaseDate);
    }

    public void setSysRelReleaseAgency(String key, String releaseReleaseAgency) {
        this.releaseAgency_.put(key, releaseReleaseAgency);
    }

    public void setSysRelEntityDescription(String key, String releaseEntityDescription) {
        this.entityDescription_.put(key, releaseEntityDescription);
    }

    public void loadSystemReleaseTable() throws SQLException, LgConvertException {

        Iterator<String> itr = releaseID_.keySet().iterator();
        String key = null;
        while (itr.hasNext()) {

            key = (String) itr.next();

            attributeMap_ = new HashMap<String, Object>();
            attributeMap_.put("1", "*");

            whereClause_ = new StringBuffer();
            whereClause_.append(SQLTableConstants.TBLCOL_RELEASEID + " = ");
            whereClause_.append("\'");
            whereClause_.append(releaseID_.get(key));
            whereClause_.append("\'");

            ResultSet result = tableUtility_.extractDataFromDB(SQLTableConstants.TBL_SYSTEM_RELEASE, attributeMap_,
                    whereClause_.toString(), dbType_);
            if (result.next()) {

                continue;
            } else {
                attributeMap_ = new HashMap<String, Object>();
                attributeMap_.put("1", releaseID_.get(key));
                attributeMap_.put("2", releaseURN_.get(key));
                attributeMap_.put("3", basedOnRelease_.get(key));
                attributeMap_.put("4", releaseDate_.get(key));
                attributeMap_.put("5", releaseAgency_.get(key));
                attributeMap_.put("6", entityDescription_.get(key));

                tableUtility_.insertRow(SQLTableConstants.SYSTEM_RELEASE, attributeMap_);
            }
        }
    }

    /* --------- conceptHistory ---------- */
    public void setHistEntityID(String key, String entityID) {
        this.entityID_.put(key, entityID);
    }

    public void setHistConceptName(String key, String conceptName) {
        this.conceptName_.put(key, conceptName);
    }

    public void setHistEditAction(String key, String editAction) {
        this.editAction_.put(key, editAction);
    }

    public void setHistEditDate(String key, Date editDate) {
        this.editDate_.put(key, editDate);
    }

    public void setHistReferenceCode(String key, String referenceCode) {
        this.referenceCode_.put(key, referenceCode);
    }

    public void setHistReferenceName(String key, String referenceName) {
        this.referenceName_.put(key, referenceName);
    }

    public void loadHistoryTable() throws SQLException {

        Iterator<String> itr = entityID_.keySet().iterator();
        String key = null;
        while (itr.hasNext()) {

            key = (String) itr.next();

            attributeMap_ = new HashMap<String, Object>();
            attributeMap_.put("1", "*");

            whereClause_ = new StringBuffer();
            whereClause_.append(tableUtility_.getSQLTableConstants().entityCodeOrEntityId + " = ");
            whereClause_.append("\'");
            whereClause_.append(entityID_.get(key));
            whereClause_.append("\'");

            ResultSet result = tableUtility_.extractDataFromDB(SQLTableConstants.TBL_CONCEPT_HISTORY, attributeMap_,
                    whereClause_.toString(), dbType_);
            String entityId = null;
            String editAction = null;
            Date editDate = null;
            String referenceCode = null;
            if (result.next()) {
                entityId = result.getString(tableUtility_.getSQLTableConstants().entityCodeOrEntityId);
                editAction = result.getString(SQLTableConstants.TBLCOL_EDITACTION);
                editDate = result.getDate(SQLTableConstants.TBLCOL_EDITDATE);
                referenceCode = result.getString(SQLTableConstants.TBLCOL_REFERENCECODE);

                if (entityID_.get(key).equals(entityId) && editAction_.get(key).equals(editAction)
                        && ((Date) editDate_.get(key)).getTime() == editDate.getTime()
                        && referenceCode_.get(key).equals(referenceCode)) {
                    continue;
                }
            }

            attributeMap_ = new HashMap<String, Object>();
            attributeMap_.put("1", entityID_.get(key));
            attributeMap_.put("2", conceptName_.get(key));
            attributeMap_.put("3", editAction_.get(key));
            attributeMap_.put("4", editDate_.get(key));
            attributeMap_.put("5", referenceCode_.get(key));
            attributeMap_.put("6", referenceName_.get(key));

            tableUtility_.insertRow(SQLTableConstants.CONCEPT_HISTORY, attributeMap_);
        }
    }
}