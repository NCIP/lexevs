package edu.mayo.informatics.lexgrid.convert.indexer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.LexGrid.messaging.LgMessageDirectorIF;
import org.LexGrid.util.sql.DBUtility;
import org.LexGrid.util.sql.GenericSQLModifier;
import org.LexGrid.util.sql.lgTables.SQLTableUtilities;
import org.apache.lucene.queryParser.TokenMgrError;

import edu.mayo.informatics.indexer.api.IndexerService;
import edu.mayo.informatics.indexer.api.exceptions.IndexNotFoundException;
import edu.mayo.informatics.indexer.api.exceptions.InternalErrorException;
import edu.mayo.informatics.indexer.api.exceptions.InternalIndexerErrorException;

public class SQLEntityIndexer extends SQLIndexer {

    private Connection sqlConnection_ = null;
    private String sqlServer_;
    private String sqlDriver_;
    private String sqlUserName_;
    private String sqlPassword_;
    private String tablePrefix_;

    public SQLEntityIndexer(String indexName, String indexLocation, String sqlUserName, String sqlPassword,
            String sqlServer, String sqlDriver, String tablePrefix, LgMessageDirectorIF messageDirector,
            Boolean useCompoundFile) throws InternalErrorException {

        super();
        sqlServer_ = sqlServer;
        sqlDriver_ = sqlDriver;
        sqlUserName_ = sqlUserName;
        sqlPassword_ = sqlPassword;
        tablePrefix_ = tablePrefix;
        
        IndexerService indexerService = new IndexerService(indexLocation, false);

        String norm = indexerService.getMetaData().getIndexMetaDataValue(indexName, "has 'Norm' fields");
        String doubleMetaPhone = indexerService.getMetaData().getIndexMetaDataValue(indexName,
                "has 'Double Metaphone' fields");

        normEnabled_ = new Boolean(norm);
        doubleMetaphoneEnabled_ = new Boolean(doubleMetaPhone);

        initIndexes(indexName, indexLocation);

        messageDirector_ = messageDirector;
        useCompoundFile_ = useCompoundFile;
    }

    public void addEntityIndexes(String codingSchemeName, List<String> entityCodeList) throws SQLException, Exception {

        if (entityCodeList == null || entityCodeList.size() == 0) {
            messageDirector_.info("entity list is empty");
            return;
        }

        try {
            sqlConnection_ = DBUtility.connectToDatabase(sqlServer_, sqlDriver_, sqlUserName_, sqlPassword_);
            gsm_ = new GenericSQLModifier(sqlConnection_.getMetaData().getDatabaseProductName(), false);
            stc_ = new SQLTableUtilities(sqlConnection_, tablePrefix_).getSQLTableConstants();
        } catch (Exception e) {
            messageDirector_.fatalAndThrowException("Error while initializing SQLEntityIndexer", e);
        }
        
        try {
            openIndexes();
            loadEntityLuceneIndexes(codingSchemeName, entityCodeList, sqlConnection_);
        } finally {
            closeIndexes();
            sqlConnection_.close();
        }
    }

    public void removeEntityIndexes(String codingSchemeName, List<String> entityCodes) throws Exception {

        if (entityCodes == null || entityCodes.size() == 0) {
            return;
        }

        try {

            Iterator<String> itr = entityCodes.iterator();

            indexerService_.forceUnlockIndex(simpleIndexName_);
            indexerService_.openBatchRemover(simpleIndexName_);

            while (itr.hasNext()) {
                String entityCode = itr.next();
                indexerService_.removeDocument(simpleIndexName_, "entityCode", entityCode);
                indexerService_.removeDocument(simpleIndexName_, codingSchemeName + "-" + entityCode);
            }

            indexerService_.closeBatchRemover(simpleIndexName_);

        } catch (InternalIndexerErrorException e) {
            messageDirector_.error("Exception while removing the indexes.", e);
        } catch (IndexNotFoundException e) {
            messageDirector_.error("Exception while removing the indexes.", e);
        } catch (TokenMgrError e) {
            messageDirector_.error("Exception while removing the indexes.", e);
        }
    }
}
