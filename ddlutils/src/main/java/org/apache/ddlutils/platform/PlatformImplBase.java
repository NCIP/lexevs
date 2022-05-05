/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.beanutils.DynaBean
 *  org.apache.commons.collections.CollectionUtils
 *  org.apache.commons.collections.Predicate
 *  org.apache.commons.logging.Log
 *  org.apache.commons.logging.LogFactory
 */
package org.apache.ddlutils.platform;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ddlutils.DatabaseOperationException;
import org.apache.ddlutils.DdlUtilsException;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.dynabean.SqlDynaClass;
import org.apache.ddlutils.dynabean.SqlDynaProperty;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.model.TypeMap;
import org.apache.ddlutils.platform.CreationParameters;
import org.apache.ddlutils.platform.JdbcModelReader;
import org.apache.ddlutils.platform.ModelBasedResultSetIterator;
import org.apache.ddlutils.platform.SqlBuilder;
import org.apache.ddlutils.util.Jdbc3Utils;
import org.apache.ddlutils.util.JdbcSupport;
import org.apache.ddlutils.util.SqlTokenizer;
import org.dbunit.dataset.datatype.StringIgnoreCaseDataType;

public abstract class PlatformImplBase
extends JdbcSupport
implements Platform {
    protected static final String MODEL_DEFAULT_NAME = "default";
    private final Log _log = LogFactory.getLog(this.getClass());
    private PlatformInfo _info = new PlatformInfo();
    private SqlBuilder _builder;
    private JdbcModelReader _modelReader;
    private boolean _scriptModeOn;
    private boolean _sqlCommentsOn = true;
    private boolean _delimitedIdentifierModeOn;
    private boolean _identityOverrideOn;
    private boolean _foreignKeysSorted;


    public SqlBuilder getSqlBuilder() {
        return this._builder;
    }

    protected void setSqlBuilder(SqlBuilder builder) {
        this._builder = builder;
    }


    public JdbcModelReader getModelReader() {
        if (this._modelReader == null) {
            this._modelReader = new JdbcModelReader(this);
        }
        return this._modelReader;
    }

    protected void setModelReader(JdbcModelReader modelReader) {
        this._modelReader = modelReader;
    }


    public PlatformInfo getPlatformInfo() {
        return this._info;
    }


    public boolean isScriptModeOn() {
        return this._scriptModeOn;
    }


    public void setScriptModeOn(boolean scriptModeOn) {
        this._scriptModeOn = scriptModeOn;
    }


    public boolean isSqlCommentsOn() {
        return this._sqlCommentsOn;
    }


    public void setSqlCommentsOn(boolean sqlCommentsOn) {
        if (!this.getPlatformInfo().isSqlCommentsSupported() && sqlCommentsOn) {
            throw new DdlUtilsException("Platform " + this.getName() + " does not support SQL comments");
        }
        this._sqlCommentsOn = sqlCommentsOn;
    }


    public boolean isDelimitedIdentifierModeOn() {
        return this._delimitedIdentifierModeOn;
    }


    public void setDelimitedIdentifierModeOn(boolean delimitedIdentifierModeOn) {
        if (!this.getPlatformInfo().isDelimitedIdentifiersSupported() && delimitedIdentifierModeOn) {
            throw new DdlUtilsException("Platform " + this.getName() + " does not support delimited identifier");
        }
        this._delimitedIdentifierModeOn = delimitedIdentifierModeOn;
    }


    public boolean isIdentityOverrideOn() {
        return this._identityOverrideOn;
    }


    public void setIdentityOverrideOn(boolean identityOverrideOn) {
        this._identityOverrideOn = identityOverrideOn;
    }


    public boolean isForeignKeysSorted() {
        return this._foreignKeysSorted;
    }


    public void setForeignKeysSorted(boolean foreignKeysSorted) {
        this._foreignKeysSorted = foreignKeysSorted;
    }

    protected Log getLog() {
        return this._log;
    }

    protected void logWarnings(Connection connection) throws SQLException {
        SQLWarning warning = connection.getWarnings();
        while (warning != null) {
            this.getLog().warn((Object)warning.getLocalizedMessage(), warning.getCause());
            warning = warning.getNextWarning();
        }
    }


    public int evaluateBatch(String sql, boolean continueOnError) throws DatabaseOperationException {
        Connection connection = this.borrowConnection();
        try {
            int n = this.evaluateBatch(connection, sql, continueOnError);
            return n;
        }
        finally {
            this.returnConnection(connection);
        }
    }


    public int evaluateBatch(Connection connection, String sql, boolean continueOnError) throws DatabaseOperationException {
        Statement statement = null;
        int errors = 0;
        int commandCount = 0;
        try {
            try {
                statement = connection.createStatement();
                SqlTokenizer tokenizer = new SqlTokenizer(sql);
                while (tokenizer.hasMoreStatements()) {
                    String command = tokenizer.getNextStatement();
                    if ((command = command.trim()).length() == 0) continue;
                    ++commandCount;
                    if (this._log.isDebugEnabled()) {
                        this._log.debug((Object)("About to execute SQL " + command));
                    }
                    try {
                        int results = statement.executeUpdate(command);
                        if (this._log.isDebugEnabled()) {
                            this._log.debug((Object)("After execution, " + results + " row(s) have been changed"));
                        }
                    }
                    catch (SQLException ex) {
                        if (continueOnError) {
                            this._log.warn((Object)("SQL Command " + command + " failed with: " + ex.getMessage()));
                            if (this._log.isDebugEnabled()) {
                                this._log.debug((Object)ex);
                            }
                            ++errors;
                        }
                        throw new DatabaseOperationException("Error while executing SQL " + command, ex);
                    }
                    SQLWarning warning = connection.getWarnings();
                    while (warning != null) {
                        this._log.warn((Object)warning.toString());
                        warning = warning.getNextWarning();
                    }
                    connection.clearWarnings();
                }
                this._log.info((Object)("Executed " + commandCount + " SQL command(s) with " + errors + " error(s)"));
            }
            catch (SQLException ex) {
                throw new DatabaseOperationException("Error while executing SQL", ex);
            }
        }
        finally {
            this.closeStatement(statement);
        }
        return errors;
    }


    public void shutdownDatabase() throws DatabaseOperationException {
        Connection connection = this.borrowConnection();
        try {
            this.shutdownDatabase(connection);
        }
        finally {
            this.returnConnection(connection);
        }
    }


    public void shutdownDatabase(Connection connection) throws DatabaseOperationException {
    }


    public void createDatabase(String jdbcDriverClassName, String connectionUrl, String username, String password, Map<String,String> parameters) throws DatabaseOperationException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Database creation is not supported for the database platform " + this.getName());
    }


    public void dropDatabase(String jdbcDriverClassName, String connectionUrl, String username, String password) throws DatabaseOperationException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Database deletion is not supported for the database platform " + this.getName());
    }


    public void createTables(Database model, boolean dropTablesFirst, boolean continueOnError) throws DatabaseOperationException {
        Connection connection = this.borrowConnection();
        try {
            this.createTables(connection, model, dropTablesFirst, continueOnError);
        }
        finally {
            this.returnConnection(connection);
        }
    }


    public void createTables(Connection connection, Database model, boolean dropTablesFirst, boolean continueOnError) throws DatabaseOperationException {
        String sql = this.getCreateTablesSql(model, dropTablesFirst, continueOnError);
        this.evaluateBatch(connection, sql, continueOnError);
    }


    public String getCreateTablesSql(Database model, boolean dropTablesFirst, boolean continueOnError) {
        String sql = null;
        try {
            StringWriter buffer = new StringWriter();
            this.getSqlBuilder().setWriter(buffer);
            this.getSqlBuilder().createTables(model, dropTablesFirst);
            sql = buffer.toString();
        }
        catch (IOException iOException) {
            // empty catch block
        }
        return sql;
    }


    public void createTables(Database model, CreationParameters params, boolean dropTablesFirst, boolean continueOnError) throws DatabaseOperationException {
        Connection connection = this.borrowConnection();
        try {
            this.createTables(connection, model, params, dropTablesFirst, continueOnError);
        }
        finally {
            this.returnConnection(connection);
        }
    }


    public void createTables(Connection connection, Database model, CreationParameters params, boolean dropTablesFirst, boolean continueOnError) throws DatabaseOperationException {
        String sql = this.getCreateTablesSql(model, params, dropTablesFirst, continueOnError);
        this.evaluateBatch(connection, sql, continueOnError);
    }


    public String getCreateTablesSql(Database model, CreationParameters params, boolean dropTablesFirst, boolean continueOnError) {
        String sql = null;
        try {
            StringWriter buffer = new StringWriter();
            this.getSqlBuilder().setWriter(buffer);
            this.getSqlBuilder().createTables(model, params, dropTablesFirst);
            sql = buffer.toString();
        }
        catch (IOException iOException) {
            // empty catch block
        }
        return sql;
    }


    public void alterTables(Database desiredDb, boolean continueOnError) throws DatabaseOperationException {
        Connection connection = this.borrowConnection();
        try {
            this.alterTables(connection, desiredDb, continueOnError);
        }
        finally {
            this.returnConnection(connection);
        }
    }


    public String getAlterTablesSql(Database desiredDb) throws DatabaseOperationException {
        Connection connection = this.borrowConnection();
        try {
            String string = this.getAlterTablesSql(connection, desiredDb);
            return string;
        }
        finally {
            this.returnConnection(connection);
        }
    }


    public void alterTables(Database desiredDb, CreationParameters params, boolean continueOnError) throws DatabaseOperationException {
        Connection connection = this.borrowConnection();
        try {
            this.alterTables(connection, desiredDb, params, continueOnError);
        }
        finally {
            this.returnConnection(connection);
        }
    }


    public String getAlterTablesSql(Database desiredDb, CreationParameters params) throws DatabaseOperationException {
        Connection connection = this.borrowConnection();
        try {
            String string = this.getAlterTablesSql(connection, desiredDb, params);
            return string;
        }
        finally {
            this.returnConnection(connection);
        }
    }


    public void alterTables(Connection connection, Database desiredModel, boolean continueOnError) throws DatabaseOperationException {
        String sql = this.getAlterTablesSql(connection, desiredModel);
        this.evaluateBatch(connection, sql, continueOnError);
    }


    public String getAlterTablesSql(Connection connection, Database desiredModel) throws DatabaseOperationException {
        String sql = null;
        Database currentModel = this.readModelFromDatabase(connection, desiredModel.getName());
        try {
            StringWriter buffer = new StringWriter();
            this.getSqlBuilder().setWriter(buffer);
            this.getSqlBuilder().alterDatabase(currentModel, desiredModel, null);
            sql = buffer.toString();
        }
        catch (IOException iOException) {
            // empty catch block
        }
        return sql;
    }


    public void alterTables(Connection connection, Database desiredModel, CreationParameters params, boolean continueOnError) throws DatabaseOperationException {
        String sql = this.getAlterTablesSql(connection, desiredModel, params);
        this.evaluateBatch(connection, sql, continueOnError);
    }


    public String getAlterTablesSql(Connection connection, Database desiredModel, CreationParameters params) throws DatabaseOperationException {
        String sql = null;
        Database currentModel = this.readModelFromDatabase(connection, desiredModel.getName());
        try {
            StringWriter buffer = new StringWriter();
            this.getSqlBuilder().setWriter(buffer);
            this.getSqlBuilder().alterDatabase(currentModel, desiredModel, params);
            sql = buffer.toString();
        }
        catch (IOException iOException) {
            // empty catch block
        }
        return sql;
    }


    public void alterTables(String catalog, String schema, String[] tableTypes, Database desiredModel, boolean continueOnError) throws DatabaseOperationException {
        Connection connection = this.borrowConnection();
        try {
            this.alterTables(connection, catalog, schema, tableTypes, desiredModel, continueOnError);
        }
        finally {
            this.returnConnection(connection);
        }
    }


    public String getAlterTablesSql(String catalog, String schema, String[] tableTypes, Database desiredModel) throws DatabaseOperationException {
        Connection connection = this.borrowConnection();
        try {
            String string = this.getAlterTablesSql(connection, catalog, schema, tableTypes, desiredModel);
            return string;
        }
        finally {
            this.returnConnection(connection);
        }
    }


    public void alterTables(String catalog, String schema, String[] tableTypes, Database desiredModel, CreationParameters params, boolean continueOnError) throws DatabaseOperationException {
        Connection connection = this.borrowConnection();
        try {
            this.alterTables(connection, catalog, schema, tableTypes, desiredModel, params, continueOnError);
        }
        finally {
            this.returnConnection(connection);
        }
    }


    public String getAlterTablesSql(String catalog, String schema, String[] tableTypes, Database desiredModel, CreationParameters params) throws DatabaseOperationException {
        Connection connection = this.borrowConnection();
        try {
            String string = this.getAlterTablesSql(connection, catalog, schema, tableTypes, desiredModel, params);
            return string;
        }
        finally {
            this.returnConnection(connection);
        }
    }


    public void alterTables(Connection connection, String catalog, String schema, String[] tableTypes, Database desiredModel, boolean continueOnError) throws DatabaseOperationException {
        String sql = this.getAlterTablesSql(connection, catalog, schema, tableTypes, desiredModel);
        this.evaluateBatch(connection, sql, continueOnError);
    }


    public String getAlterTablesSql(Connection connection, String catalog, String schema, String[] tableTypes, Database desiredModel) throws DatabaseOperationException {
        String sql = null;
        Database currentModel = this.readModelFromDatabase(connection, desiredModel.getName(), catalog, schema, tableTypes);
        try {
            StringWriter buffer = new StringWriter();
            this.getSqlBuilder().setWriter(buffer);
            this.getSqlBuilder().alterDatabase(currentModel, desiredModel, null);
            sql = buffer.toString();
        }
        catch (IOException iOException) {
            // empty catch block
        }
        return sql;
    }


    public void alterTables(Connection connection, String catalog, String schema, String[] tableTypes, Database desiredModel, CreationParameters params, boolean continueOnError) throws DatabaseOperationException {
        String sql = this.getAlterTablesSql(connection, catalog, schema, tableTypes, desiredModel, params);
        this.evaluateBatch(connection, sql, continueOnError);
    }


    public String getAlterTablesSql(Connection connection, String catalog, String schema, String[] tableTypes, Database desiredModel, CreationParameters params) throws DatabaseOperationException {
        String sql = null;
        Database currentModel = this.readModelFromDatabase(connection, desiredModel.getName(), catalog, schema, tableTypes);
        try {
            StringWriter buffer = new StringWriter();
            this.getSqlBuilder().setWriter(buffer);
            this.getSqlBuilder().alterDatabase(currentModel, desiredModel, params);
            sql = buffer.toString();
        }
        catch (IOException iOException) {
            // empty catch block
        }
        return sql;
    }


    public void dropTable(Connection connection, Database model, Table table, boolean continueOnError) throws DatabaseOperationException {
        String sql = this.getDropTableSql(model, table, continueOnError);
        this.evaluateBatch(connection, sql, continueOnError);
    }


    public void dropTable(Database model, Table table, boolean continueOnError) throws DatabaseOperationException {
        Connection connection = this.borrowConnection();
        try {
            this.dropTable(connection, model, table, continueOnError);
        }
        finally {
            this.returnConnection(connection);
        }
    }


    public String getDropTableSql(Database model, Table table, boolean continueOnError) {
        String sql = null;
        try {
            StringWriter buffer = new StringWriter();
            this.getSqlBuilder().setWriter(buffer);
            this.getSqlBuilder().dropTable(model, table);
            sql = buffer.toString();
        }
        catch (IOException iOException) {
            // empty catch block
        }
        return sql;
    }


    public void dropTables(Database model, boolean continueOnError) throws DatabaseOperationException {
        Connection connection = this.borrowConnection();
        try {
            this.dropTables(connection, model, continueOnError);
        }
        finally {
            this.returnConnection(connection);
        }
    }


    public void dropTables(Connection connection, Database model, boolean continueOnError) throws DatabaseOperationException {
        String sql = this.getDropTablesSql(model, continueOnError);
        this.evaluateBatch(connection, sql, continueOnError);
    }


    public String getDropTablesSql(Database model, boolean continueOnError) {
        String sql = null;
        try {
            StringWriter buffer = new StringWriter();
            this.getSqlBuilder().setWriter(buffer);
            this.getSqlBuilder().dropTables(model);
            sql = buffer.toString();
        }
        catch (IOException iOException) {
            // empty catch block
        }
        return sql;
    }


    public Iterator query(Database model, String sql) throws DatabaseOperationException {
        return this.query(model, sql, (Table[])null);
    }


    public Iterator query(Database model, String sql, Collection parameters) throws DatabaseOperationException {
        return this.query(model, sql, parameters, null);
    }


    public Iterator query(Database model, String sql, Table[] queryHints) throws DatabaseOperationException {
        Connection connection = this.borrowConnection();
        Statement statement = null;
        ResultSet resultSet = null;
        ModelBasedResultSetIterator answer = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            ModelBasedResultSetIterator modelBasedResultSetIterator = answer = this.createResultSetIterator(model, resultSet, queryHints);
            return modelBasedResultSetIterator;
        }
        catch (SQLException ex) {
            throw new DatabaseOperationException("Error while performing a query", ex);
        }
        finally {
            if (answer == null) {
                this.closeStatement(statement);
                this.returnConnection(connection);
            }
        }
    }


    public Iterator query(Database model, String sql, Collection parameters, Table[] queryHints) throws DatabaseOperationException {
        Connection connection = this.borrowConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ModelBasedResultSetIterator answer = null;
        try {
            statement = connection.prepareStatement(sql);
            int paramIdx = 1;
            for (Object arg : parameters) {
                if (arg instanceof BigDecimal) {
                    statement.setBigDecimal(paramIdx, (BigDecimal)arg);
                } else {
                    statement.setObject(paramIdx, arg);
                }
                ++paramIdx;
            }
            resultSet = statement.executeQuery();
            ModelBasedResultSetIterator modelBasedResultSetIterator = answer = this.createResultSetIterator(model, resultSet, queryHints);
            return modelBasedResultSetIterator;
        }
        catch (SQLException ex) {
            throw new DatabaseOperationException("Error while performing a query", ex);
        }
        finally {
            if (answer == null) {
                this.closeStatement(statement);
                this.returnConnection(connection);
            }
        }
    }


    public List fetch(Database model, String sql) throws DatabaseOperationException {
        return this.fetch(model, sql, (Table[])null, 0, -1);
    }


    public List fetch(Database model, String sql, Table[] queryHints) throws DatabaseOperationException {
        return this.fetch(model, sql, queryHints, 0, -1);
    }


    public List fetch(Database model, String sql, int start, int end) throws DatabaseOperationException {
        return this.fetch(model, sql, (Table[])null, start, end);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */

    public List fetch(Database model, String sql, Table[] queryHints, int start, int end) throws DatabaseOperationException {
        Connection connection = this.borrowConnection();
        Statement statement = null;
        ResultSet resultSet = null;
        ArrayList<Object> result = new ArrayList<Object>();
        try {
            try {
                statement = connection.createStatement();
                resultSet = statement.executeQuery(sql);
                int rowIdx = 0;
                ModelBasedResultSetIterator it = this.createResultSetIterator(model, resultSet, queryHints);
                while (true) {
                    if (end >= 0) {
                        if (rowIdx > end) return result;
                    }
                    if (!it.hasNext()) {
                        return result;
                    }
                    if (rowIdx >= start) {
                        result.add(it.next());
                    } else {
                        it.advance();
                    }
                    ++rowIdx;
                }
            }
            catch (SQLException ex) {
                throw new DatabaseOperationException("Error while fetching data from the database", ex);
            }
        }
        finally {
            this.closeStatement(statement);
            this.returnConnection(connection);
        }
    }


    public List fetch(Database model, String sql, Collection parameters) throws DatabaseOperationException {
        return this.fetch(model, sql, parameters, null, 0, -1);
    }


    public List fetch(Database model, String sql, Collection parameters, int start, int end) throws DatabaseOperationException {
        return this.fetch(model, sql, parameters, null, start, end);
    }


    public List fetch(Database model, String sql, Collection parameters, Table[] queryHints) throws DatabaseOperationException {
        return this.fetch(model, sql, parameters, queryHints, 0, -1);
    }


    public List fetch(Database model, String sql, Collection parameters, Table[] queryHints, int start, int end) throws DatabaseOperationException {
        Connection connection = this.borrowConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ArrayList<Object> result = new ArrayList<Object>();
        try {
            statement = connection.prepareStatement(sql);
            int paramIdx = 1;
            for (Object arg : parameters) {
                if (arg instanceof BigDecimal) {
                    statement.setBigDecimal(paramIdx, (BigDecimal)arg);
                } else {
                    statement.setObject(paramIdx, arg);
                }
                ++paramIdx;
            }
            resultSet = statement.executeQuery();
            int rowIdx = 0;
            ModelBasedResultSetIterator it = this.createResultSetIterator(model, resultSet, queryHints);
            while ((end < 0 || rowIdx <= end) && it.hasNext()) {
                if (rowIdx >= start) {
                    result.add(it.next());
                } else {
                    it.advance();
                }
                ++rowIdx;
            }
        }
        catch (SQLException ex) {
            this.closeStatement(statement);
            this.returnConnection(connection);
            throw new DatabaseOperationException("Error while fetching data from the database", ex);
        }
        return result;
    }

    protected String createInsertSql(Database model, SqlDynaClass dynaClass, SqlDynaProperty[] properties, DynaBean bean) {
        Table table = model.findTable(dynaClass.getTableName());
        HashMap columnValues = this.toColumnValues(properties, bean);
        return this._builder.getInsertSql(table, columnValues, bean == null);
    }

    protected String createSelectLastInsertIdSql(Database model, SqlDynaClass dynaClass) {
        Table table = model.findTable(dynaClass.getTableName());
        return this._builder.getSelectLastIdentityValues(table);
    }


    public String getInsertSql(Database model, DynaBean dynaBean) {
        SqlDynaClass dynaClass = model.getDynaClassFor(dynaBean);
        SqlDynaProperty[] properties = dynaClass.getSqlDynaProperties();
        if (properties.length == 0) {
            this._log.info((Object)("Cannot insert instances of type " + (Object)((Object)dynaClass) + " because it has no properties"));
            return null;
        }
        return this.createInsertSql(model, dynaClass, properties, dynaBean);
    }

    private SqlDynaProperty[] getPropertiesForInsertion(Database model, SqlDynaClass dynaClass, final DynaBean bean) {
        SqlDynaProperty[] properties = dynaClass.getSqlDynaProperties();
        Collection result = CollectionUtils.select(Arrays.asList(properties), (Predicate)new Predicate(){

            public boolean evaluate(Object input) {
                SqlDynaProperty prop = (SqlDynaProperty)((Object)input);
                if (bean.get(prop.getName()) != null) {
                    return !prop.getColumn().isAutoIncrement() || PlatformImplBase.this.isIdentityOverrideOn() && PlatformImplBase.this.getPlatformInfo().isIdentityOverrideAllowed();
                }
                return !prop.getColumn().isAutoIncrement() && prop.getColumn().getDefaultValue() == null;
            }
        });
//        return result.toArray(new SqlDynaProperty[result.size()]);
        return new SqlDynaProperty[result.size()];
    }

    private Column[] getRelevantIdentityColumns(Database model, SqlDynaClass dynaClass, final DynaBean bean) {
        SqlDynaProperty[] properties = dynaClass.getSqlDynaProperties();
        Collection relevantProperties = CollectionUtils.select(Arrays.asList(properties), (Predicate)new Predicate(){

            public boolean evaluate(Object input) {
                SqlDynaProperty prop = (SqlDynaProperty)((Object)input);
                return prop.getColumn().isAutoIncrement() && (!PlatformImplBase.this.isIdentityOverrideOn() || !PlatformImplBase.this.getPlatformInfo().isIdentityOverrideAllowed() || bean.get(prop.getName()) == null);
            }
        });
        Column[] columns = new Column[relevantProperties.size()];
        int idx = 0;
        Iterator propIt = relevantProperties.iterator();
        while (propIt.hasNext()) {
            columns[idx] = ((SqlDynaProperty)((Object)propIt.next())).getColumn();
            ++idx;
        }
        return columns;
    }

    /*
     * Exception decompiling
     */

    public void insert(Connection connection, Database model, DynaBean dynaBean) throws DatabaseOperationException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }


    public void insert(Database model, DynaBean dynaBean) throws DatabaseOperationException {
        Connection connection = this.borrowConnection();
        try {
            this.insert(connection, model, dynaBean);
        }
        finally {
            this.returnConnection(connection);
        }
    }


    public void insert(Connection connection, Database model, Collection<DynaBean> dynaBeans) throws DatabaseOperationException {
        SqlDynaClass dynaClass = null;
        SqlDynaProperty[] properties = null;
        PreparedStatement statement = null;
        int addedStmts = 0;
        boolean identityWarningPrinted = false;
        for (DynaBean dynaBean : dynaBeans) {
            SqlDynaClass curDynaClass = model.getDynaClassFor(dynaBean);
            if (curDynaClass != dynaClass) {
                if (dynaClass != null) {
                    this.executeBatch(statement, addedStmts, dynaClass.getTable());
                    addedStmts = 0;
                }
                dynaClass = curDynaClass;
                properties = this.getPropertiesForInsertion(model, curDynaClass, dynaBean);
                if (properties.length == 0) {
                    this._log.warn((Object)("Cannot insert instances of type " + (Object)((Object)dynaClass) + " because it has no usable properties"));
                    continue;
                }
                if (!identityWarningPrinted && this.getRelevantIdentityColumns(model, curDynaClass, dynaBean).length > 0) {
                    this._log.warn((Object)"Updating the bean properties corresponding to auto-increment columns is not supported in batch mode");
                    identityWarningPrinted = true;
                }
                String insertSql = this.createInsertSql(model, dynaClass, properties, null);
                if (this._log.isDebugEnabled()) {
                    this._log.debug((Object)("Starting new batch with SQL: " + insertSql));
                }
                try {
                    statement = connection.prepareStatement(insertSql);
                }
                catch (SQLException ex) {
                    throw new DatabaseOperationException("Error while preparing insert statement", ex);
                }
            }
            try {
                int idx = 0;
                while (idx < properties.length) {
                    this.setObject(statement, idx + 1, dynaBean, properties[idx]);
                    ++idx;
                }
                statement.addBatch();
                ++addedStmts;
            }
            catch (SQLException ex) {
                throw new DatabaseOperationException("Error while adding batch insert", ex);
            }
        }
        if (dynaClass != null) {
            this.executeBatch(statement, addedStmts, dynaClass.getTable());
        }
    }

    private void executeBatch(PreparedStatement statement, int numRows, Table table) throws DatabaseOperationException {
        if (statement != null) {
            try {
                Connection connection = statement.getConnection();
                this.beforeInsert(connection, table);
                int[] results = statement.executeBatch();
                this.closeStatement(statement);
                this.afterInsert(connection, table);
                boolean hasSum = true;
                int sum = 0;
                int idx = 0;
                while (results != null && idx < results.length) {
                    if (results[idx] < 0) {
                        String msg;
                        hasSum = false;
                        if (Jdbc3Utils.supportsJava14BatchResultCodes() && (msg = Jdbc3Utils.getBatchResultMessage(table.getName(), idx, results[idx])) != null) {
                            this._log.warn((Object)msg);
                        }
                    } else {
                        sum += results[idx];
                    }
                    ++idx;
                }
                if (hasSum && sum != numRows) {
                    this._log.warn((Object)("Attempted to insert " + numRows + " rows into table " + table.getName() + " but changed " + sum + " rows"));
                }
            }
            catch (SQLException ex) {
                throw new DatabaseOperationException("SQLException error while inserting into the database", ex);
            }
            catch (NoSuchFieldException ex) {
                throw new DatabaseOperationException("NoSuchField error while inserting into the database", ex);
            }
            catch (IllegalAccessException ex) {
                throw new DatabaseOperationException("IllegalAccess error while inserting into the database", ex);
            }
        }
    }


    public void insert(Database model, Collection dynaBeans) throws DatabaseOperationException {
        Connection connection = this.borrowConnection();
        try {
            this.insert(connection, model, dynaBeans);
        }
        finally {
            this.returnConnection(connection);
        }
    }

    protected void beforeInsert(Connection connection, Table table) throws SQLException {
    }

    protected void afterInsert(Connection connection, Table table) throws SQLException {
    }

    protected String createUpdateSql(Database model, SqlDynaClass dynaClass, SqlDynaProperty[] primaryKeys, SqlDynaProperty[] properties, DynaBean bean) {
        Table table = model.findTable(dynaClass.getTableName());
        HashMap columnValues = this.toColumnValues(properties, bean);
        columnValues.putAll(this.toColumnValues(primaryKeys, bean));
        return this._builder.getUpdateSql(table, columnValues, bean == null);
    }


    public String getUpdateSql(Database model, DynaBean dynaBean) {
        SqlDynaClass dynaClass = model.getDynaClassFor(dynaBean);
        SqlDynaProperty[] primaryKeys = dynaClass.getPrimaryKeyProperties();
        if (primaryKeys.length == 0) {
            this._log.info((Object)("Cannot update instances of type " + (Object)((Object)dynaClass) + " because it has no primary keys"));
            return null;
        }
        return this.createUpdateSql(model, dynaClass, primaryKeys, dynaClass.getNonPrimaryKeyProperties(), dynaBean);
    }


    public void update(Connection connection, Database model, DynaBean dynaBean) {
        SqlDynaClass dynaClass = model.getDynaClassFor(dynaBean);
        SqlDynaProperty[] primaryKeys = dynaClass.getPrimaryKeyProperties();
        if (primaryKeys.length == 0) {
            this._log.info((Object)("Cannot update instances of type " + (Object)((Object)dynaClass) + " because it has no primary keys"));
            return;
        }
        SqlDynaProperty[] properties = dynaClass.getNonPrimaryKeyProperties();
        String sql = this.createUpdateSql(model, dynaClass, primaryKeys, properties, null);
        PreparedStatement statement = null;
        if (this._log.isDebugEnabled()) {
            this._log.debug((Object)("About to execute SQL: " + sql));
        }
        try {
            try {
                this.beforeUpdate(connection, dynaClass.getTable());
                statement = connection.prepareStatement(sql);
                int sqlIndex = 1;
                int idx = 0;
                while (idx < properties.length) {
                    this.setObject(statement, sqlIndex++, dynaBean, properties[idx]);
                    ++idx;
                }
                idx = 0;
                while (idx < primaryKeys.length) {
                    this.setObject(statement, sqlIndex++, dynaBean, primaryKeys[idx]);
                    ++idx;
                }
                int count = statement.executeUpdate();
                this.afterUpdate(connection, dynaClass.getTable());
                if (count != 1) {
                    this._log.warn((Object)("Attempted to insert a single row " + dynaBean + " into table " + dynaClass.getTableName() + " but changed " + count + " row(s)"));
                }
            }
            catch (SQLException ex) {
                throw new DatabaseOperationException("Error while updating in the database", ex);
            }
        }
        catch (DatabaseOperationException e) {
            throw e;
        }

        this.closeStatement(statement);
    }


    public void update(Database model, DynaBean dynaBean) throws DatabaseOperationException {
        Connection connection = this.borrowConnection();
        try {
            this.update(connection, model, dynaBean);
        }
        finally {
            this.returnConnection(connection);
        }
    }

    protected void beforeUpdate(Connection connection, Table table) throws SQLException {
    }

    protected void afterUpdate(Connection connection, Table table) throws SQLException {
    }

    protected boolean exists(Connection connection, DynaBean dynaBean) {
        return false;
    }


    public void store(Database model, DynaBean dynaBean) throws DatabaseOperationException {
        Connection connection = this.borrowConnection();
        try {
            if (this.exists(connection, dynaBean)) {
                this.update(connection, model, dynaBean);
            } else {
                this.insert(connection, model, dynaBean);
            }
        }
        finally {
            this.returnConnection(connection);
        }
    }

    protected String createDeleteSql(Database model, SqlDynaClass dynaClass, SqlDynaProperty[] primaryKeys, DynaBean bean) {
        Table table = model.findTable(dynaClass.getTableName());
        HashMap<String,Object> pkValues = this.toColumnValues(primaryKeys, bean);
        return this._builder.getDeleteSql(table, pkValues, bean == null);
    }


    public String getDeleteSql(Database model, DynaBean dynaBean) {
        SqlDynaClass dynaClass = model.getDynaClassFor(dynaBean);
        SqlDynaProperty[] primaryKeys = dynaClass.getPrimaryKeyProperties();
        if (primaryKeys.length == 0) {
            this._log.warn((Object)("Cannot delete instances of type " + (Object)((Object)dynaClass) + " because it has no primary keys"));
            return null;
        }
        return this.createDeleteSql(model, dynaClass, primaryKeys, dynaBean);
    }


    public void delete(Database model, DynaBean dynaBean) throws DatabaseOperationException {
        Connection connection = this.borrowConnection();
        try {
            this.delete(connection, model, dynaBean);
        }
        finally {
            this.returnConnection(connection);
        }
    }


    public void delete(Connection connection, Database model, DynaBean dynaBean)  {
        SqlDynaProperty[] primaryKeys;
        SqlDynaClass dynaClass;
        PreparedStatement statement;
        block8: {
            statement = null;
            dynaClass = model.getDynaClassFor(dynaBean);
            primaryKeys = dynaClass.getPrimaryKeyProperties();
            if (primaryKeys.length != 0) break block8;
            this._log.warn((Object)("Cannot delete instances of type " + (Object)((Object)dynaClass) + " because it has no primary keys"));
            this.closeStatement(statement);
            return;
        }
        try {
            try {
                String sql = this.createDeleteSql(model, dynaClass, primaryKeys, null);
                if (this._log.isDebugEnabled()) {
                    this._log.debug((Object)("About to execute SQL " + sql));
                }
                statement = connection.prepareStatement(sql);
                int idx = 0;
                while (idx < primaryKeys.length) {
                    this.setObject(statement, idx + 1, dynaBean, primaryKeys[idx]);
                    ++idx;
                }
                int count = statement.executeUpdate();
                if (count != 1) {
                    this._log.warn((Object)("Attempted to delete a single row " + dynaBean + " in table " + dynaClass.getTableName() + " but changed " + count + " row(s)."));
                }
            }
            catch (SQLException ex) {
                throw new DatabaseOperationException("Error while deleting from the database", ex);
            }
        }
        catch (Throwable throwable) {
            this.closeStatement(statement);
            throw throwable;
        }
        this.closeStatement(statement);
    }


    public Database readModelFromDatabase(String name) throws DatabaseOperationException {
        Connection connection = this.borrowConnection();
        try {
            Database database = this.readModelFromDatabase(connection, name);
            return database;
        }
        finally {
            this.returnConnection(connection);
        }
    }


    public Database readModelFromDatabase(Connection connection, String name) throws DatabaseOperationException {
        try {
            Database model = this.getModelReader().getDatabase(connection, name);
            this.postprocessModelFromDatabase(model);
            return model;
        }
        catch (SQLException ex) {
            throw new DatabaseOperationException(ex);
        }
    }


    public Database readModelFromDatabase(String name, String catalog, String schema, String[] tableTypes) throws DatabaseOperationException {
        Connection connection = this.borrowConnection();
        try {
            Database database = this.readModelFromDatabase(connection, name, catalog, schema, tableTypes);
            return database;
        }
        finally {
            this.returnConnection(connection);
        }
    }


    public Database readModelFromDatabase(Connection connection, String name, String catalog, String schema, String[] tableTypes) throws DatabaseOperationException {
        try {
            JdbcModelReader reader = this.getModelReader();
            Database model = reader.getDatabase(connection, name, catalog, schema, tableTypes);
            this.postprocessModelFromDatabase(model);
            if (model.getName() == null || model.getName().length() == 0) {
                model.setName(MODEL_DEFAULT_NAME);
            }
            return model;
        }
        catch (SQLException ex) {
            throw new DatabaseOperationException(ex);
        }
    }

    protected void postprocessModelFromDatabase(Database model) {
        int tableIdx = 0;
        while (tableIdx < model.getTableCount()) {
            Table table = model.getTable(tableIdx);
            int columnIdx = 0;
            while (columnIdx < table.getColumnCount()) {
                String defaultValue;
                Column column = table.getColumn(columnIdx);
                if ((TypeMap.isTextType(column.getTypeCode()) || TypeMap.isDateTimeType(column.getTypeCode())) && (defaultValue = column.getDefaultValue()) != null && defaultValue.length() >= 2 && defaultValue.startsWith("'") && defaultValue.endsWith("'")) {
                    defaultValue = defaultValue.substring(1, defaultValue.length() - 1);
                    column.setDefaultValue(defaultValue);
                }
                ++columnIdx;
            }
            ++tableIdx;
        }
    }

    protected HashMap<String,Object> toColumnValues(SqlDynaProperty[] properties, DynaBean bean) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        int idx = 0;
        while (idx < properties.length) {
            result.put(properties[idx].getName(), bean == null ? null : bean.get(properties[idx].getName()));
            ++idx;
        }
        return result;
    }

    protected void setObject(PreparedStatement statement, int sqlIndex, DynaBean dynaBean, SqlDynaProperty property) throws SQLException {
        int typeCode = property.getColumn().getTypeCode();
        Object value = dynaBean.get(property.getName());
        this.setStatementParameterValue(statement, sqlIndex, typeCode, value);
    }

    protected void setStatementParameterValue(PreparedStatement statement, int sqlIndex, int typeCode, Object value) throws SQLException {
        if (value == null) {
            statement.setNull(sqlIndex, typeCode);
        } else if (value instanceof String) {
            statement.setString(sqlIndex, (String)value);
        } else if (value instanceof byte[]) {
            statement.setBytes(sqlIndex, (byte[])value);
        } else if (value instanceof Boolean) {
            statement.setBoolean(sqlIndex, (Boolean)value);
        } else if (value instanceof Byte) {
            statement.setByte(sqlIndex, (Byte)value);
        } else if (value instanceof Short) {
            statement.setShort(sqlIndex, (Short)value);
        } else if (value instanceof Integer) {
            statement.setInt(sqlIndex, (Integer)value);
        } else if (value instanceof Long) {
            statement.setLong(sqlIndex, (Long)value);
        } else if (value instanceof BigDecimal) {
            statement.setBigDecimal(sqlIndex, (BigDecimal)value);
        } else if (value instanceof Float) {
            statement.setFloat(sqlIndex, ((Float)value).floatValue());
        } else if (value instanceof Double) {
            statement.setDouble(sqlIndex, (Double)value);
        } else {
            statement.setObject(sqlIndex, value, typeCode);
        }
    }

    protected Object getObjectFromResultSet(ResultSet resultSet, String columnName, Table table) throws SQLException {
        Column column = table == null ? null : table.findColumn(columnName, this.isDelimitedIdentifierModeOn());
        Object value = null;
        if (column != null) {
            int originalJdbcType = column.getTypeCode();
            int targetJdbcType = this.getPlatformInfo().getTargetJdbcType(originalJdbcType);
            int jdbcType = originalJdbcType;
            if (originalJdbcType == 2004 && targetJdbcType != 2004) {
                jdbcType = targetJdbcType;
            }
            if (originalJdbcType == 2005 && targetJdbcType != 2005) {
                jdbcType = targetJdbcType;
            }
            value = this.extractColumnValue(resultSet, columnName, 0, jdbcType);
        } else {
            value = resultSet.getObject(columnName);
        }
        return resultSet.wasNull() ? null : value;
    }

    protected Object getObjectFromResultSet(ResultSet resultSet, Column column, int idx) throws SQLException {
        int originalJdbcType = column.getTypeCode();
        int targetJdbcType = this.getPlatformInfo().getTargetJdbcType(originalJdbcType);
        int jdbcType = originalJdbcType;
        Object value = null;
        if (originalJdbcType == 2004 && targetJdbcType != 2004) {
            jdbcType = targetJdbcType;
        }
        if (originalJdbcType == 2005 && targetJdbcType != 2005) {
            jdbcType = targetJdbcType;
        }
        value = this.extractColumnValue(resultSet, null, idx, jdbcType);
        return resultSet.wasNull() ? null : value;
    }

    protected Object extractColumnValue(ResultSet resultSet, String columnName, int columnIdx, int jdbcType) throws SQLException {
        Object value;
        boolean useIdx = columnName == null;
        switch (jdbcType) {
            case -1: 
            case 1: 
            case 12: {
                value = useIdx ? resultSet.getString(columnIdx) : resultSet.getString(columnName);
                break;
            }
            case 2: 
            case 3: {
                value = useIdx ? resultSet.getBigDecimal(columnIdx) : resultSet.getBigDecimal(columnName);
                break;
            }
            case -7: {
                value = new Boolean(useIdx ? resultSet.getBoolean(columnIdx) : resultSet.getBoolean(columnName));
                break;
            }
            case -6: 
            case 4: 
            case 5: {
                value = new Integer(useIdx ? resultSet.getInt(columnIdx) : resultSet.getInt(columnName));
                break;
            }
            case -5: {
                value = new Long(useIdx ? resultSet.getLong(columnIdx) : resultSet.getLong(columnName));
                break;
            }
            case 7: {
                value = new Float(useIdx ? resultSet.getFloat(columnIdx) : resultSet.getFloat(columnName));
                break;
            }
            case 6: 
            case 8: {
                value = new Double(useIdx ? resultSet.getDouble(columnIdx) : resultSet.getDouble(columnName));
                break;
            }
            case -4: 
            case -3: 
            case -2: {
                value = useIdx ? resultSet.getBytes(columnIdx) : resultSet.getBytes(columnName);
                break;
            }
            case 91: {
                value = useIdx ? resultSet.getDate(columnIdx) : resultSet.getDate(columnName);
                break;
            }
            case 92: {
                value = useIdx ? resultSet.getTime(columnIdx) : resultSet.getTime(columnName);
                break;
            }
            case 93: {
                value = useIdx ? resultSet.getTimestamp(columnIdx) : resultSet.getTimestamp(columnName);
                break;
            }
            case 2005: {
                Clob clob;
                Clob clob2 = clob = useIdx ? resultSet.getClob(columnIdx) : resultSet.getClob(columnName);
                if (clob == null) {
                    value = null;
                    break;
                }
                long length = clob.length();
                if (length > Integer.MAX_VALUE) {
                    value = clob;
                    break;
                }
                if (length == 0L) {
                    value = "";
                    break;
                }
                value = clob.getSubString(1L, (int)length);
                break;
            }
            case 2004: {
                Blob blob;
                Blob blob2 = blob = useIdx ? resultSet.getBlob(columnIdx) : resultSet.getBlob(columnName);
                if (blob == null) {
                    value = null;
                    break;
                }
                long length = blob.length();
                if (length > Integer.MAX_VALUE) {
                    value = blob;
                    break;
                }
                if (length == 0L) {
                    value = new byte[0];
                    break;
                }
                value = blob.getBytes(1L, (int)length);
                break;
            }
            case 2003: {
                value = useIdx ? resultSet.getArray(columnIdx) : resultSet.getArray(columnName);
                break;
            }
            case 2006: {
                value = useIdx ? resultSet.getRef(columnIdx) : resultSet.getRef(columnName);
                break;
            }
            default: {
                value = Jdbc3Utils.supportsJava14JdbcTypes() && jdbcType == Jdbc3Utils.determineBooleanTypeCode() ? new Boolean(useIdx ? resultSet.getBoolean(columnIdx) : resultSet.getBoolean(columnName)) : (useIdx ? resultSet.getObject(columnIdx) : resultSet.getObject(columnName));
            }
        }
        return resultSet.wasNull() ? null : value;
    }

    protected ModelBasedResultSetIterator createResultSetIterator(Database model, ResultSet resultSet, Table[] queryHints) {
        return new ModelBasedResultSetIterator(this, model, resultSet, queryHints, true);
    }
}

