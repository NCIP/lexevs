/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.collections.map.ListOrderedMap
 *  org.apache.commons.lang.StringUtils
 *  org.apache.commons.logging.Log
 *  org.apache.commons.logging.LogFactory
 */
package org.apache.ddlutils.platform;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.ForeignKey;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.IndexColumn;
import org.apache.ddlutils.model.NonUniqueIndex;
import org.apache.ddlutils.model.Reference;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.model.UniqueIndex;
import org.apache.ddlutils.platform.DatabaseMetaDataWrapper;
import org.apache.ddlutils.platform.MetaDataColumnDescriptor;

public class JdbcModelReader {
    private final Log _log = LogFactory.getLog(JdbcModelReader.class);
    private final List _columnsForTable;
    private final List _columnsForColumn;
    private final List _columnsForPK;
    private final List _columnsForFK;
    private final List _columnsForIndex;
    private Platform _platform;
    private HashMap _defaultSizes = new HashMap();
    private String _defaultCatalogPattern = "%";
    private String _defaultSchemaPattern = "%";
    private String _defaultTablePattern = "%";
    private String _defaultColumnPattern;
    private String[] _defaultTableTypes = new String[]{"TABLE"};
    private Connection _connection;

    public JdbcModelReader(Platform platform) {
        this._platform = platform;
        this._defaultSizes.put(new Integer(1), "254");
        this._defaultSizes.put(new Integer(12), "254");
        this._defaultSizes.put(new Integer(-1), "254");
        this._defaultSizes.put(new Integer(-2), "254");
        this._defaultSizes.put(new Integer(-3), "254");
        this._defaultSizes.put(new Integer(-4), "254");
        this._defaultSizes.put(new Integer(4), "32");
        this._defaultSizes.put(new Integer(-5), "64");
        this._defaultSizes.put(new Integer(7), "7,0");
        this._defaultSizes.put(new Integer(6), "15,0");
        this._defaultSizes.put(new Integer(8), "15,0");
        this._defaultSizes.put(new Integer(3), "15,15");
        this._defaultSizes.put(new Integer(2), "15,15");
        this._columnsForTable = this.initColumnsForTable();
        this._columnsForColumn = this.initColumnsForColumn();
        this._columnsForPK = this.initColumnsForPK();
        this._columnsForFK = this.initColumnsForFK();
        this._columnsForIndex = this.initColumnsForIndex();
    }

    public Platform getPlatform() {
        return this._platform;
    }

    public PlatformInfo getPlatformInfo() {
        return this._platform.getPlatformInfo();
    }

    protected List initColumnsForTable() {
        ArrayList<MetaDataColumnDescriptor> result = new ArrayList<MetaDataColumnDescriptor>();
        result.add(new MetaDataColumnDescriptor("TABLE_NAME", 12));
        result.add(new MetaDataColumnDescriptor("TABLE_TYPE", 12, "UNKNOWN"));
        result.add(new MetaDataColumnDescriptor("TABLE_CAT", 12));
        result.add(new MetaDataColumnDescriptor("TABLE_SCHEM", 12));
        result.add(new MetaDataColumnDescriptor("REMARKS", 12));
        return result;
    }

    protected List initColumnsForColumn() {
        ArrayList<MetaDataColumnDescriptor> result = new ArrayList<MetaDataColumnDescriptor>();
        result.add(new MetaDataColumnDescriptor("COLUMN_DEF", 12));
        result.add(new MetaDataColumnDescriptor("TABLE_NAME", 12));
        result.add(new MetaDataColumnDescriptor("COLUMN_NAME", 12));
        result.add(new MetaDataColumnDescriptor("DATA_TYPE", 4, new Integer(1111)));
        result.add(new MetaDataColumnDescriptor("NUM_PREC_RADIX", 4, new Integer(10)));
        result.add(new MetaDataColumnDescriptor("DECIMAL_DIGITS", 4, new Integer(0)));
        result.add(new MetaDataColumnDescriptor("COLUMN_SIZE", 12));
        result.add(new MetaDataColumnDescriptor("IS_NULLABLE", 12, "YES"));
        result.add(new MetaDataColumnDescriptor("REMARKS", 12));
        return result;
    }

    protected List initColumnsForPK() {
        ArrayList<MetaDataColumnDescriptor> result = new ArrayList<MetaDataColumnDescriptor>();
        result.add(new MetaDataColumnDescriptor("COLUMN_NAME", 12));
        result.add(new MetaDataColumnDescriptor("TABLE_NAME", 12));
        result.add(new MetaDataColumnDescriptor("PK_NAME", 12));
        return result;
    }

    protected List initColumnsForFK() {
        ArrayList<MetaDataColumnDescriptor> result = new ArrayList<MetaDataColumnDescriptor>();
        result.add(new MetaDataColumnDescriptor("PKTABLE_NAME", 12));
        result.add(new MetaDataColumnDescriptor("FKTABLE_NAME", 12));
        result.add(new MetaDataColumnDescriptor("KEY_SEQ", -6, new Short("0")));
        result.add(new MetaDataColumnDescriptor("FK_NAME", 12));
        result.add(new MetaDataColumnDescriptor("PKCOLUMN_NAME", 12));
        result.add(new MetaDataColumnDescriptor("FKCOLUMN_NAME", 12));
        return result;
    }

    protected List initColumnsForIndex() {
        ArrayList<MetaDataColumnDescriptor> result = new ArrayList<MetaDataColumnDescriptor>();
        result.add(new MetaDataColumnDescriptor("INDEX_NAME", 12));
        result.add(new MetaDataColumnDescriptor("TABLE_NAME", 12));
        result.add(new MetaDataColumnDescriptor("NON_UNIQUE", -7, Boolean.TRUE));
        result.add(new MetaDataColumnDescriptor("ORDINAL_POSITION", -6, new Short("0")));
        result.add(new MetaDataColumnDescriptor("COLUMN_NAME", 12));
        result.add(new MetaDataColumnDescriptor("TYPE", -6));
        return result;
    }

    public String getDefaultCatalogPattern() {
        return this._defaultCatalogPattern;
    }

    public void setDefaultCatalogPattern(String catalogPattern) {
        this._defaultCatalogPattern = catalogPattern;
    }

    public String getDefaultSchemaPattern() {
        return this._defaultSchemaPattern;
    }

    public void setDefaultSchemaPattern(String schemaPattern) {
        this._defaultSchemaPattern = schemaPattern;
    }

    public String getDefaultTablePattern() {
        return this._defaultTablePattern;
    }

    public void setDefaultTablePattern(String tablePattern) {
        this._defaultTablePattern = tablePattern;
    }

    public String getDefaultColumnPattern() {
        return this._defaultColumnPattern;
    }

    public void setDefaultColumnPattern(String columnPattern) {
        this._defaultColumnPattern = columnPattern;
    }

    public String[] getDefaultTableTypes() {
        return this._defaultTableTypes;
    }

    public void setDefaultTableTypes(String[] types) {
        this._defaultTableTypes = types;
    }

    protected List getColumnsForTable() {
        return this._columnsForTable;
    }

    protected List getColumnsForColumn() {
        return this._columnsForColumn;
    }

    protected List getColumnsForPK() {
        return this._columnsForPK;
    }

    protected List getColumnsForFK() {
        return this._columnsForFK;
    }

    protected List getColumnsForIndex() {
        return this._columnsForIndex;
    }

    protected Connection getConnection() {
        return this._connection;
    }

    public Database getDatabase(Connection connection, String name) throws SQLException {
        return this.getDatabase(connection, name, null, null, null);
    }

    public Database getDatabase(Connection connection, String name, String catalog, String schema, String[] tableTypes) throws SQLException {
        Database db = new Database();
        if (name == null) {
            try {
                db.setName(connection.getCatalog());
                if (catalog == null) {
                    catalog = db.getName();
                }
            }
            catch (Exception ex) {
                this._log.info((Object)"Cannot determine the catalog name from connection.", (Throwable)ex);
            }
        } else {
            db.setName(name);
        }
        try {
            this._connection = connection;
            db.addTables(this.readTables(catalog, schema, tableTypes));
            if (this.getPlatform().isForeignKeysSorted()) {
                this.sortForeignKeys(db);
            }
        }
        finally {
            this._connection = null;
        }
        db.initialize();
        return db;
    }

    protected Collection readTables(String catalog, String schemaPattern, String[] tableTypes) throws SQLException {
        ResultSet tableData = null;
        try {
            DatabaseMetaDataWrapper metaData = new DatabaseMetaDataWrapper();
            metaData.setMetaData(this._connection.getMetaData());
            metaData.setCatalog(catalog == null ? this.getDefaultCatalogPattern() : catalog);
            metaData.setSchemaPattern(schemaPattern == null ? this.getDefaultSchemaPattern() : schemaPattern);
            metaData.setTableTypes(tableTypes == null || tableTypes.length == 0 ? this.getDefaultTableTypes() : tableTypes);
            tableData = metaData.getTables(this.getDefaultTablePattern());
            ArrayList<Table> tables = new ArrayList<Table>();
            while (tableData.next()) {
                Map values = this.readColumns(tableData, this.getColumnsForTable());
                Table table = this.readTable(metaData, values);
                if (table == null) continue;
                tables.add(table);
            }
            final Collator collator = Collator.getInstance();
            Collections.sort(tables, new Comparator(){

                public int compare(Object obj1, Object obj2) {
                    return collator.compare(((Table)obj1).getName().toUpperCase(), ((Table)obj2).getName().toUpperCase());
                }
            });
            ArrayList<Table> arrayList = tables;
            return arrayList;
        }
        finally {
            if (tableData != null) {
                tableData.close();
            }
        }
    }

    protected Table readTable(DatabaseMetaDataWrapper metaData, Map values) throws SQLException {
        String tableName = (String)values.get("TABLE_NAME");
        Table table = null;
        if (tableName != null && tableName.length() > 0) {
            table = new Table();
            table.setName(tableName);
            table.setType((String)values.get("TABLE_TYPE"));
            table.setCatalog((String)values.get("TABLE_CAT"));
            table.setSchema((String)values.get("TABLE_SCHEM"));
            table.setDescription((String)values.get("REMARKS"));
            table.addColumns(this.readColumns(metaData, tableName));
            table.addForeignKeys(this.readForeignKeys(metaData, tableName));
            table.addIndices(this.readIndices(metaData, tableName));
            Collection primaryKeys = this.readPrimaryKeyNames(metaData, tableName);
            Iterator it = primaryKeys.iterator();
            while (it.hasNext()) {
                table.findColumn((String)it.next(), true).setPrimaryKey(true);
            }
            if (this.getPlatformInfo().isSystemIndicesReturned()) {
                this.removeSystemIndices(metaData, table);
            }
        }
        return table;
    }

    protected void removeSystemIndices(DatabaseMetaDataWrapper metaData, Table table) throws SQLException {
        this.removeInternalPrimaryKeyIndex(metaData, table);
        int fkIdx = 0;
        while (fkIdx < table.getForeignKeyCount()) {
            this.removeInternalForeignKeyIndex(metaData, table, table.getForeignKey(fkIdx));
            ++fkIdx;
        }
    }

    protected void removeInternalPrimaryKeyIndex(DatabaseMetaDataWrapper metaData, Table table) throws SQLException {
        Column[] pks = table.getPrimaryKeyColumns();
        ArrayList<String> columnNames = new ArrayList<String>();
        int columnIdx = 0;
        while (columnIdx < pks.length) {
            columnNames.add(pks[columnIdx].getName());
            ++columnIdx;
        }
        int indexIdx = 0;
        while (indexIdx < table.getIndexCount()) {
            Index index = table.getIndex(indexIdx);
            if (index.isUnique() && this.matches(index, columnNames) && this.isInternalPrimaryKeyIndex(metaData, table, index)) {
                table.removeIndex(indexIdx);
                continue;
            }
            ++indexIdx;
        }
    }

    protected void removeInternalForeignKeyIndex(DatabaseMetaDataWrapper metaData, Table table, ForeignKey fk) throws SQLException {
        ArrayList<String> columnNames = new ArrayList<String>();
        boolean mustBeUnique = !this.getPlatformInfo().isSystemForeignKeyIndicesAlwaysNonUnique();
        int columnIdx = 0;
        while (columnIdx < fk.getReferenceCount()) {
            String name = fk.getReference(columnIdx).getLocalColumnName();
            Column localColumn = table.findColumn(name, this.getPlatform().isDelimitedIdentifierModeOn());
            if (mustBeUnique && !localColumn.isPrimaryKey()) {
                mustBeUnique = false;
            }
            columnNames.add(name);
            ++columnIdx;
        }
        int indexIdx = 0;
        while (indexIdx < table.getIndexCount()) {
            Index index = table.getIndex(indexIdx);
            if (mustBeUnique == index.isUnique() && this.matches(index, columnNames) && this.isInternalForeignKeyIndex(metaData, table, fk, index)) {
                fk.setAutoIndexPresent(true);
                table.removeIndex(indexIdx);
                continue;
            }
            ++indexIdx;
        }
    }

    protected boolean matches(Index index, List columnsToSearchFor) {
        if (index.getColumnCount() != columnsToSearchFor.size()) {
            return false;
        }
        int columnIdx = 0;
        while (columnIdx < index.getColumnCount()) {
            if (!columnsToSearchFor.get(columnIdx).equals(index.getColumn(columnIdx).getName())) {
                return false;
            }
            ++columnIdx;
        }
        return true;
    }

    protected boolean isInternalPrimaryKeyIndex(DatabaseMetaDataWrapper metaData, Table table, Index index) throws SQLException {
        return false;
    }

    protected boolean isInternalForeignKeyIndex(DatabaseMetaDataWrapper metaData, Table table, ForeignKey fk, Index index) throws SQLException {
        return false;
    }

    protected Collection readColumns(DatabaseMetaDataWrapper metaData, String tableName) throws SQLException {
        ResultSet columnData = null;
        try {
            columnData = metaData.getColumns(tableName, this.getDefaultColumnPattern());
            ArrayList<Column> columns = new ArrayList<Column>();
            while (columnData.next()) {
                Map values = this.readColumns(columnData, this.getColumnsForColumn());
                columns.add(this.readColumn(metaData, values));
            }
            ArrayList<Column> arrayList = columns;
            return arrayList;
        }
        finally {
            if (columnData != null) {
                columnData.close();
            }
        }
    }

    protected Column readColumn(DatabaseMetaDataWrapper metaData, Map values) throws SQLException {
        Column column = new Column();
        column.setName((String)values.get("COLUMN_NAME"));
        column.setDefaultValue((String)values.get("COLUMN_DEF"));
        column.setTypeCode((Integer)values.get("DATA_TYPE"));
        column.setPrecisionRadix((Integer)values.get("NUM_PREC_RADIX"));
        String size = (String)values.get("COLUMN_SIZE");
        int scale = (Integer)values.get("DECIMAL_DIGITS");
        if (size == null) {
            size = (String)this._defaultSizes.get(new Integer(column.getTypeCode()));
        }
        column.setSize(size);
        if (scale != 0) {
            column.setScale(scale);
        }
        column.setRequired("NO".equalsIgnoreCase(((String)values.get("IS_NULLABLE")).trim()));
        column.setDescription((String)values.get("REMARKS"));
        return column;
    }

    protected Collection readPrimaryKeyNames(DatabaseMetaDataWrapper metaData, String tableName) throws SQLException {
        ArrayList<String> pks = new ArrayList<String>();
        ResultSet pkData = null;
        try {
            pkData = metaData.getPrimaryKeys(tableName);
            while (pkData.next()) {
                Map values = this.readColumns(pkData, this.getColumnsForPK());
                pks.add(this.readPrimaryKeyName(metaData, values));
            }
        }
        finally {
            if (pkData != null) {
                pkData.close();
            }
        }
        return pks;
    }

    protected String readPrimaryKeyName(DatabaseMetaDataWrapper metaData, Map values) throws SQLException {
        return (String)values.get("COLUMN_NAME");
    }

    protected Collection readForeignKeys(DatabaseMetaDataWrapper metaData, String tableName) throws SQLException {
        ListOrderedMap fks = new ListOrderedMap();
        ResultSet fkData = null;
        try {
            fkData = metaData.getForeignKeys(tableName);
            while (fkData.next()) {
                Map values = this.readColumns(fkData, this.getColumnsForFK());
                this.readForeignKey(metaData, values, (Map)fks);
            }
        }
        finally {
            if (fkData != null) {
                fkData.close();
            }
        }
        return fks.values();
    }

    protected void readForeignKey(DatabaseMetaDataWrapper metaData, Map values, Map knownFks) throws SQLException {
        String fkName = (String)values.get("FK_NAME");
        ForeignKey fk = (ForeignKey)knownFks.get(fkName);
        if (fk == null) {
            fk = new ForeignKey(fkName);
            fk.setForeignTableName((String)values.get("PKTABLE_NAME"));
            knownFks.put(fkName, fk);
        }
        Reference ref = new Reference();
        ref.setForeignColumnName((String)values.get("PKCOLUMN_NAME"));
        ref.setLocalColumnName((String)values.get("FKCOLUMN_NAME"));
        if (values.containsKey("KEY_SEQ")) {
            ref.setSequenceValue(((Short)values.get("KEY_SEQ")).intValue());
        }
        fk.addReference(ref);
    }

    protected Collection readIndices(DatabaseMetaDataWrapper metaData, String tableName) throws SQLException {
        ListOrderedMap indices = new ListOrderedMap();
        ResultSet indexData = null;
        try {
            indexData = metaData.getIndices(tableName, false, false);
            while (indexData.next()) {
                Map values = this.readColumns(indexData, this.getColumnsForIndex());
                this.readIndex(metaData, values, (Map)indices);
            }
        }
        finally {
            if (indexData != null) {
                indexData.close();
            }
        }
        return indices.values();
    }

    protected void readIndex(DatabaseMetaDataWrapper metaData, Map values, Map knownIndices) throws SQLException {
        Short indexType = (Short)values.get("TYPE");
        if (indexType != null && indexType == 0) {
            return;
        }
        String indexName = (String)values.get("INDEX_NAME");
        if (indexName != null) {
            Index index = (Index)knownIndices.get(indexName);
            if (index == null) {
                index = (Boolean)values.get("NON_UNIQUE") != false ? new NonUniqueIndex() : new UniqueIndex();
                index.setName(indexName);
                knownIndices.put(indexName, index);
            }
            IndexColumn indexColumn = new IndexColumn();
            indexColumn.setName((String)values.get("COLUMN_NAME"));
            if (values.containsKey("ORDINAL_POSITION")) {
                indexColumn.setOrdinalPosition(((Short)values.get("ORDINAL_POSITION")).intValue());
            }
            index.addColumn(indexColumn);
        }
    }

    protected Map readColumns(ResultSet resultSet, List<MetaDataColumnDescriptor> columnDescriptors) throws SQLException {
        HashMap<String, Object> values = new HashMap<String, Object>();
        for (MetaDataColumnDescriptor descriptor : columnDescriptors) {
            values.put(descriptor.getName(), descriptor.readColumn(resultSet));
        }
        return values;
    }

    protected void determineAutoIncrementFromResultSetMetaData(Table table, Column[] columnsToCheck) throws SQLException {
        if (columnsToCheck == null || columnsToCheck.length == 0) {
            return;
        }
        StringBuffer query = new StringBuffer();
        query.append("SELECT ");
        int idx = 0;
        while (idx < columnsToCheck.length) {
            if (idx > 0) {
                query.append(",");
            }
            if (this.getPlatform().isDelimitedIdentifierModeOn()) {
                query.append(this.getPlatformInfo().getDelimiterToken());
            }
            query.append(columnsToCheck[idx].getName());
            if (this.getPlatform().isDelimitedIdentifierModeOn()) {
                query.append(this.getPlatformInfo().getDelimiterToken());
            }
            ++idx;
        }
        query.append(" FROM ");
        if (this.getPlatform().isDelimitedIdentifierModeOn()) {
            query.append(this.getPlatformInfo().getDelimiterToken());
        }
        query.append(table.getName());
        if (this.getPlatform().isDelimitedIdentifierModeOn()) {
            query.append(this.getPlatformInfo().getDelimiterToken());
        }
        query.append(" WHERE 1 = 0");
        Statement stmt = null;
        try {
            stmt = this.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(query.toString());
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int idx2 = 0;
            while (idx2 < columnsToCheck.length) {
                if (rsMetaData.isAutoIncrement(idx2 + 1)) {
                    columnsToCheck[idx2].setAutoIncrement(true);
                }
                ++idx2;
            }
        }
        finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    protected void sortForeignKeys(Database model) {
        int tableIdx = 0;
        while (tableIdx < model.getTableCount()) {
            model.getTable(tableIdx).sortForeignKeys(this.getPlatform().isDelimitedIdentifierModeOn());
            ++tableIdx;
        }
    }

    protected String unescape(String text, String unescaped, String escaped) {
        String result = text;
        if (result != null) {
            result = escaped.equals("''") ? (result.length() > 2 && result.startsWith("'") && result.endsWith("'") ? "'" + StringUtils.replace((String)result.substring(1, result.length() - 1), (String)escaped, (String)unescaped) + "'" : StringUtils.replace((String)result, (String)escaped, (String)unescaped)) : StringUtils.replace((String)result, (String)escaped, (String)unescaped);
        }
        return result;
    }

    public String determineSchemaOf(Connection connection, String schemaPattern, Table table) throws SQLException {
        ResultSet tableData = null;
        ResultSet columnData = null;
        try {
            DatabaseMetaDataWrapper metaData = new DatabaseMetaDataWrapper();
            metaData.setMetaData(connection.getMetaData());
            metaData.setCatalog(this.getDefaultCatalogPattern());
            metaData.setSchemaPattern(schemaPattern == null ? this.getDefaultSchemaPattern() : schemaPattern);
            metaData.setTableTypes(this.getDefaultTableTypes());
            String tablePattern = table.getName();
            if (this.getPlatform().isDelimitedIdentifierModeOn()) {
                tablePattern = tablePattern.toUpperCase();
            }
            tableData = metaData.getTables(tablePattern);
            boolean found = false;
            String schema = null;
            while (!found && tableData.next()) {
                Map values = this.readColumns(tableData, this.getColumnsForTable());
                String tableName = (String)values.get("TABLE_NAME");
                if (tableName == null || tableName.length() <= 0) continue;
                schema = (String)values.get("TABLE_SCHEM");
                columnData = metaData.getColumns(tableName, this.getDefaultColumnPattern());
                found = true;
                while (found && columnData.next()) {
                    values = this.readColumns(columnData, this.getColumnsForColumn());
                    if (table.findColumn((String)values.get("COLUMN_NAME"), this.getPlatform().isDelimitedIdentifierModeOn()) != null) continue;
                    found = false;
                }
                columnData.close();
                columnData = null;
            }
            String string = found ? schema : null;
            return string;
        }
        finally {
            if (columnData != null) {
                columnData.close();
            }
            if (tableData != null) {
                tableData.close();
            }
        }
    }
}

