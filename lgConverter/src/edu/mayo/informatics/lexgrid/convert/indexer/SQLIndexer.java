/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package edu.mayo.informatics.lexgrid.convert.indexer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.commonTypes.types.EntityTypes;
import org.LexGrid.util.sql.DBUtility;
import org.LexGrid.util.sql.GenericSQLModifier;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.util.sql.lgTables.SQLTableUtilities;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;

import edu.mayo.informatics.lexgrid.convert.utility.Constants;

/**
 * Code for building Lucene index from LexGrid SQL tables.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 */
public class SQLIndexer extends LuceneLoaderCode {
    protected SQLTableConstants stc_ = null;
    protected GenericSQLModifier gsm_ = null;
    private int batchSize_ = 5000;
    private String defaultLanguage_ = null;
    private String codingSchemeId_ = null;
    private String getEntityStmtText_ = null;
    protected LgMessageDirectorIF messageDirector_ = null;

    /**
     * Build a lucene index from the LexGrid SQL tables supplied in the
     * parameters.
     * 
     * @param indexName
     *            The name to use for the Index.
     * @param indexLocation
     *            The full path to the index location on the system.
     * @param codingSchemes
     *            The coding schemes to index from the sql tables.
     * @throws Exception
     */
    public SQLIndexer(String indexName, String indexLocation, String sqlUserName, String sqlPassword, String sqlServer,
            String sqlDriver, String tablePrefix, String[] codingSchemes, LgMessageDirectorIF messageDirector,
            boolean addNormFields, boolean addDoubleMetaphoneFields, boolean addStemFields, boolean useCompoundFile)
            throws Exception {
        normEnabled_ = addNormFields;
        doubleMetaphoneEnabled_ = addDoubleMetaphoneFields;
        stemmingEnabled_ = addStemFields;
        messageDirector_ = messageDirector;
        useCompoundFile_ = useCompoundFile;
        batchSize_ = Constants.mySqlBatchSize;
        index(indexName, indexLocation, sqlUserName, sqlPassword, sqlServer, sqlDriver, tablePrefix, codingSchemes);
    }
    
    public SQLIndexer() {
        
    }

    private String getEntityStmtText(List<String> entityCodeList) {
        
        StringBuffer getEntityStmtText = new StringBuffer();
        
        int entityCount = (entityCodeList != null) ? entityCodeList.size() : -1;
        
        getEntityStmtText.append("SELECT "
                + "a."+ SQLTableConstants.TBLCOL_ISACTIVE
                + ", a."+ stc_.entityCodeOrEntityId
                + (stc_.supports2009Model()
                    ? ", d." + SQLTableConstants.TBLCOL_STATUS + 
                      ", a." + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE
                    : ", a." + SQLTableConstants.TBLCOL_CONCEPTSTATUS)
                + ", a."+ SQLTableConstants.TBLCOL_ISANONYMOUS
                + ", a."+ SQLTableConstants.TBLCOL_ENTITYDESCRIPTION
                + ", b."+ SQLTableConstants.TBLCOL_PROPERTYID
                + ", b."+ SQLTableConstants.TBLCOL_PROPERTYTYPE
                + ", b."+ SQLTableConstants.TBLCOL_PROPERTYNAME
                + ", b."+ SQLTableConstants.TBLCOL_LANGUAGE
                + ", b."+ SQLTableConstants.TBLCOL_FORMAT
                + ", b."+ SQLTableConstants.TBLCOL_ISPREFERRED
                + ", b."+ SQLTableConstants.TBLCOL_DEGREEOFFIDELITY
                + ", b."+ SQLTableConstants.TBLCOL_MATCHIFNOCONTEXT
                + ", b."+ SQLTableConstants.TBLCOL_REPRESENTATIONALFORM
                + ", b."+ SQLTableConstants.TBLCOL_ENTRYSTATEID
                + ", b."+ SQLTableConstants.TBLCOL_PROPERTYVALUE
                + ", c."+ SQLTableConstants.TBLCOL_TYPENAME
                + ", c."+ SQLTableConstants.TBLCOL_ATTRIBUTEVALUE
                + ", c."+ SQLTableConstants.TBLCOL_VAL1
                + (stc_.supports2009Model()
                    ? ", e."+ stc_.entityType
                    : "") +
            " FROM "
                // Note: access requires parenthesis, other databases do not.
                + (gsm_.getDatabaseType().equals("ACCESS") ? "( ( ( (" : "")
                    + stc_.getTableName(SQLTableConstants.ENTITY) + " {AS} a "
                    + (stc_.supports2009Model()
                        ? " left join "+ stc_.getTableName(SQLTableConstants.ENTRY_STATE) + " {AS} d " +
                              " on a." + SQLTableConstants.TBLCOL_ENTRYSTATEID + " = d." + SQLTableConstants.TBLCOL_ENTRYSTATEID +
                              (gsm_.getDatabaseType().equals("ACCESS") ? ")" : "") +
                          " left join "+ stc_.getTableName(SQLTableConstants.ENTITY_TYPE) + " {AS} e " +
                              " on a." + SQLTableConstants.TBLCOL_ENTITYCODE + " = e." + SQLTableConstants.TBLCOL_ENTITYCODE +
                             " AND a." + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + " = e." + SQLTableConstants.TBLCOL_CODINGSCHEMENAME +
                             " AND a." + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + " = e." + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + 
                             (gsm_.getDatabaseType().equals("ACCESS") ? ")" : "")
                        : "")
                + " inner join " + stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY) + " {AS} b " +
                      " on a." + stc_.entityCodeOrId + " = b." + stc_.entityCodeOrEntityId +
                     " AND a." + stc_.codingSchemeNameOrId + " = b." + stc_.codingSchemeNameOrId +
                  (stc_.supports2009Model()
                   ? " AND a." + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + " = b." + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE  
                           + (gsm_.getDatabaseType().equals("ACCESS") ? ")" : "")
                   : "" )
                + " left join " + stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY_MULTI_ATTRIBUTES) + " {AS} c " +
                       " on b." + stc_.entityCodeOrId + " = c." + stc_.entityCodeOrEntityId +
                      " AND b." + SQLTableConstants.TBLCOL_PROPERTYID + " = c." + SQLTableConstants.TBLCOL_PROPERTYID +
                      " AND b." + stc_.codingSchemeNameOrId + " = c." + stc_.codingSchemeNameOrId +
                  (stc_.supports2009Model()
                    ? " AND b." + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + " = c." + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE 
                    : "" )
                + (gsm_.getDatabaseType().equals("ACCESS") ? ")" : "") +
            " WHERE" 
                + " a." + stc_.codingSchemeNameOrId + " = ?");
        
        if (entityCount != -1 && entityCount != 0) {
            getEntityStmtText.append(" AND ( ");

            for (int i = 0; i < entityCount; i++) {
                getEntityStmtText.append(" a." + stc_.entityCodeOrEntityId + " = ?");

                getEntityStmtText.append((i != entityCount - 1) ? " OR" : "");
            }
            
            getEntityStmtText.append(" )");
        }

        getEntityStmtText.append((gsm_.getDatabaseType().equals("ACCESS") ? "" : " ORDER BY" + " a."
                + stc_.entityCodeOrId)); 

        getEntityStmtText_ = gsm_.modifySQL(getEntityStmtText.toString());
        
        return getEntityStmtText_;
    }

    private void index(String indexName, String indexLocation, String sqlUserName, String sqlPassword,
            String sqlServer, String sqlDriver, String tablePrefix, String[] codingSchemes) throws Exception {

        SimpleDateFormat temp = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");   
        initIndexes();
        createIndex(indexName);
        Connection sqlConnection = DBUtility.connectToDatabase(sqlServer, sqlDriver, sqlUserName, sqlPassword);
        try {
            gsm_ = new GenericSQLModifier(sqlConnection.getMetaData().getDatabaseProductName(), false);
            stc_ = new SQLTableUtilities(sqlConnection, tablePrefix).getSQLTableConstants();
            openIndexesClearExisting(indexName, codingSchemes);
            
            for (int j = 0; j < codingSchemes.length; j++) {
                logger.info("Now indexing " + codingSchemes[j] + " " + temp.format(new Date(System.currentTimeMillis())));
                messageDirector_.info("Now indexing " + codingSchemes[j] + " "
                        + temp.format(new Date(System.currentTimeMillis())));
                Date start = new Date(System.currentTimeMillis());
                String version = loadEntityLuceneIndexes(indexName, codingSchemes[j], null, sqlConnection);
                indexerService_.getMetaData().setIndexMetaDataValue(codingSchemes[j] + "[:]" + version, indexName);
                indexerService_.getMetaData().setIndexMetaDataValue(indexName, "codingScheme", codingSchemes[j]);
                indexerService_.getMetaData().setIndexMetaDataValue(indexName, "version", version);

                // create model version meta data
                if (stc_.supports2009Model())
                    indexerService_.getMetaData().setIndexMetaDataValue(indexName, "lgModel", "2009");
                indexerService_.getMetaData().setIndexMetaDataValue(indexName, "has 'Norm' fields", normEnabled_ + "");
                indexerService_.getMetaData().setIndexMetaDataValue(indexName, "has 'Double Metaphone' fields",
                        doubleMetaphoneEnabled_ + "");
                indexerService_.getMetaData().setIndexMetaDataValue(indexName, "indexing started", temp.format(start));
                indexerService_.getMetaData().setIndexMetaDataValue(indexName, "indexing finished",
                        temp.format(new Date(System.currentTimeMillis())));
            }
        } finally {
            try {
                sqlConnection.close();
            } finally {
                messageDirector_.info("Closing Indexes " + temp.format(new Date(System.currentTimeMillis())));
                closeIndexes(indexName);
                messageDirector_.info("Closed Indexes " + temp.format(new Date(System.currentTimeMillis())));
            }
        }
    }

    /*
     * Returns the version of the code system
     */
    protected String loadEntityLuceneIndexes(String indexName, String codingSchemeName, List<String> entityCodeList, 
            Connection sqlConnection) throws Exception {
        logger.debug("loadCodedEntries called for " + codingSchemeName);
        
        // Fetch scheme, language, and version info...
        String version = null;
        ResultSet results = null;
        PreparedStatement getDefaultLanguage = null;
        try {
            getDefaultLanguage = sqlConnection.prepareStatement("SELECT "
                    + SQLTableConstants.TBLCOL_DEFAULTLANGUAGE + ", " + stc_.registeredNameOrCSURI + ", "
                    + SQLTableConstants.TBLCOL_REPRESENTSVERSION + " FROM "
                    + stc_.getTableName(SQLTableConstants.CODING_SCHEME) + " WHERE "
                    + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + " = ?");
            getDefaultLanguage.setString(1, codingSchemeName);
            results = getDefaultLanguage.executeQuery();
            if (!results.next()) {
                throw new Exception("No row could be found in the coding scheme table for '" + codingSchemeName + "'");
            }
            defaultLanguage_ = results.getString(SQLTableConstants.TBLCOL_DEFAULTLANGUAGE);
            codingSchemeId_ = results.getString(stc_.registeredNameOrCSURI);
            version = results.getString(SQLTableConstants.TBLCOL_REPRESENTSVERSION);
        } catch (Exception e) {
            messageDirector_.fatalAndThrowException("Problem fetching metadata for index operation.", e);
        } finally {
            if (results != null)
                try { results.close(); } catch(Exception e) {};
            if (getDefaultLanguage != null)
                try { getDefaultLanguage.close(); } catch(Exception e) {};
        }

        // Load and index coded entities ...
        int start = 0;
        int codeCount = 0;
        PreparedStatement getEntities = null;
        try {
            getEntities = sqlConnection.prepareStatement(getEntityStmtText(entityCodeList), 
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            if (gsm_.getDatabaseType().equals("PostgreSQL")){
                sqlConnection.setAutoCommit(false);
            }
            Boolean isActive = null;
            Boolean isAnonymous = null;
            Boolean isPreferred = null;
            Boolean matchIfNoContext = null;
            String degreeOfFidelity = null;
            String entityStatus = null;
            String format = null;
            String language = null;
            String lastCode = null;
            String lastNamespace = null;
            String lastCodeType = null;
            String lastED = null;    
            String lastPropertyId = null;
            String newED = null;
            String propertyName = null;
            String propertyType = null;
            String propertyValue = null;
            String representationalForm = null;
            HashSet sources = null;
            HashSet usageContexts = null;
            Hashtable qualifiers = null;

            logger.debug("Getting results from sql.");
            messageDirector_.info("Getting results from sql.");

            int i = 1;
            getEntities.setString(i++, codingSchemeName);

            if (entityCodeList == null) {
                try {
                    //This tells MySQL drivers to stream results. No idea why
                    //Integer.MIN_VALUE is necessary -- but it is.
                    //http://dev.mysql.com/doc/refman/5.0/en/connector-j-reference-implementation-notes.html
                    if(gsm_.getDatabaseType().equals("MySQL")) {
                        getEntities.setFetchSize(Integer.MIN_VALUE);
                    } else {
                        getEntities.setFetchSize(batchSize_);
                    }
                } catch (SQLException e) {
                    logger.warn("Error setting batch size -- reverting to default."); 
                }
            } else {
                for (int j = 0; j < entityCodeList.size(); j++) {
                    getEntities.setString(i++, entityCodeList.get(j));
                }
            }

            results = getEntities.executeQuery();
            logger.debug("query finished - processing results");

            // Since we left joined all of these tables together, its a bit
            // tricky to interpret results of the query.
            try {
                while (results.next()) {
                    String newCode = results.getString(stc_.entityCodeOrEntityId);
                    String newCodeType = stc_.supports2009Model()
                    ? results.getString(SQLTableConstants.TBLCOL_ENTITYTYPE)
                            : EntityTypes.CONCEPT.value();

                    newED = results.getString(SQLTableConstants.TBLCOL_ENTITYDESCRIPTION);

                    String newNamespace = stc_.supports2009Model()
                    ? results.getString(SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE)
                            : codingSchemeName;

                    String newId = results.getString(SQLTableConstants.TBLCOL_PROPERTYID);
                    if (lastPropertyId == null || !lastPropertyId.equals(newId) || lastCode == null
                            || !lastCode.equals(newCode) || !lastNamespace.equals(newNamespace))
                    {
                        // If the propertyId or code is different - index the last item.
                        if (lastPropertyId != null
                                && (isAnonymous == null || !isAnonymous.booleanValue())
                                && (StringUtils.isNotBlank(propertyValue)))
                        {
                            addEntity(codingSchemeName, codingSchemeId_, null, lastCode, lastNamespace, lastCodeType, lastED,
                                    propertyType, propertyName, propertyValue, isActive, format, language,
                                    isPreferred, entityStatus, lastPropertyId, degreeOfFidelity, matchIfNoContext,
                                    representationalForm,
                                    (String[]) sources.toArray(new String[sources.size()]),
                                    (String[]) usageContexts.toArray(new String[usageContexts.size()]),
                                    (Qualifier[]) qualifiers.values().toArray(new Qualifier[qualifiers.size()]),
                                    indexName);
                        }

                        sources = new HashSet();
                        usageContexts = new HashSet();
                        qualifiers = new Hashtable();
                        lastPropertyId = newId;

                        if (lastCode == null || !lastCode.equals(newCode) || !lastNamespace.equals(newNamespace)) {
                            // only read these properties when we see a different
                            // code (no need to read them
                            // other times, because they will be the same as before)
                            lastCode = newCode;
                            lastNamespace = newNamespace;
                            lastED = newED;
                            lastCodeType = newCodeType;
                            isAnonymous = DBUtility.getBooleanFromResultSet(results, SQLTableConstants.TBLCOL_ISANONYMOUS);
                            if (isAnonymous != null && isAnonymous.booleanValue()) {
                                continue;
                                // skip this one - no need to read any other properties
                            }
                            isActive = DBUtility.getBooleanFromResultSet(results, SQLTableConstants.TBLCOL_ISACTIVE);
                            if (stc_.supports2009Model())
                                entityStatus = results.getString(SQLTableConstants.TBLCOL_STATUS);
                            else
                                entityStatus = results.getString(SQLTableConstants.TBLCOL_CONCEPTSTATUS);

                            if (codeCount % 5000 == 0) {
                                messageDirector_.info("Indexed " + codeCount + " entities.");
                            }
                            if (codeCount++ % 50 == 0) {
                                messageDirector_.busy();
                            }
                        }

                        propertyName = results.getString(stc_.propertyOrPropertyName);
                        format = results.getString(stc_.formatOrPresentationFormat);

                        propertyType = results.getString(SQLTableConstants.TBLCOL_PROPERTYTYPE);
                        language = results.getString(SQLTableConstants.TBLCOL_LANGUAGE);

                        if (language == null || language.length() == 0) {
                            language = defaultLanguage_;
                        }

                        isPreferred = DBUtility.getBooleanFromResultSet(results, SQLTableConstants.TBLCOL_ISPREFERRED);
                        degreeOfFidelity = results.getString(SQLTableConstants.TBLCOL_DEGREEOFFIDELITY);
                        matchIfNoContext = DBUtility.getBooleanFromResultSet(results, SQLTableConstants.TBLCOL_MATCHIFNOCONTEXT);
                        representationalForm = results.getString(SQLTableConstants.TBLCOL_REPRESENTATIONALFORM);
                        propertyValue = results.getString(SQLTableConstants.TBLCOL_PROPERTYVALUE);
                    }

                    // these values always have the potential to be unique. Always
                    // read these ...
                    String type = results.getString(SQLTableConstants.TBLCOL_TYPENAME);
                    String value = results.getString(SQLTableConstants.TBLCOL_ATTRIBUTEVALUE);
                    String val1 = results.getString(SQLTableConstants.TBLCOL_VAL1);

                    // tables don't allow duplicates, but the join creates some.
                    // We hashes to get rid of duplicates in the index.
                    if (type != null) {
                        if (type.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SOURCE)) {
                            sources.add(value);
                        } else if (type.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_USAGECONTEXT)) {
                            usageContexts.add(value);
                        } else if (type.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_QUALIFIER)) {
                            qualifiers.put(value + ":" + val1, new Qualifier(value, val1));
                        } else {
                            messageDirector_.error("Unexpected value in conceptPropertyMultiAttribs table" + type + ":"
                                    + value, null);
                        }
                    }
                }
            } finally {
                try { results.close(); } catch(Exception e) {};
            }

            // load the last row (if there was one)
            if ((lastCode != null && (isAnonymous == null || !isAnonymous.booleanValue()))
                    && StringUtils.isNotBlank(propertyValue))
            {
                addEntity(codingSchemeName, codingSchemeId_, null, lastCode, lastNamespace, lastCodeType, lastED, propertyType,
                    propertyName, propertyValue, isActive, format, language, isPreferred, entityStatus,
                    lastPropertyId, degreeOfFidelity, matchIfNoContext, representationalForm,
                   (String[]) sources.toArray(new String[sources.size()]),
                   (String[]) usageContexts.toArray(new String[usageContexts.size()]),
                   (Qualifier[]) qualifiers.values().toArray(new Qualifier[qualifiers.size()]),
                    indexName);
            }
        } catch (Exception e) {
            messageDirector_.fatalAndThrowException("Problem indexing entity", e);
        } finally {
            if (getEntities != null)
                try { getEntities.close(); } catch (Exception e) {};
            if (gsm_.getDatabaseType().equals("PostgreSQL"))
                try { sqlConnection.setAutoCommit(true); } catch (Exception e) {};
        }
        logger.info("loaded " + codeCount + " entities");
        messageDirector_.info("loaded " + codeCount + " entities");
        return version;
    }
}