/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.collections.map.ListOrderedMap
 */
package org.apache.ddlutils.platform.interbase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.ForeignKey;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.model.TypeMap;
import org.apache.ddlutils.platform.DatabaseMetaDataWrapper;
import org.apache.ddlutils.platform.JdbcModelReader;
import org.apache.ddlutils.platform.interbase.InterbaseBuilder;

public class InterbaseModelReader
extends JdbcModelReader {
    public InterbaseModelReader(Platform platform) {
        super(platform);
        this.setDefaultCatalogPattern(null);
        this.setDefaultSchemaPattern(null);
        this.setDefaultTablePattern("%");
        this.setDefaultColumnPattern("%");
    }

    @Override
    protected Table readTable(DatabaseMetaDataWrapper metaData, Map values) throws SQLException {
        Table table = super.readTable(metaData, values);
        if (table != null) {
            this.determineExtraColumnInfo(table);
            this.determineAutoIncrementColumns(table);
            this.adjustColumns(table);
        }
        return table;
    }

    @Override
    protected Collection readColumns(DatabaseMetaDataWrapper metaData, String tableName) throws SQLException {
        ResultSet columnData = null;
        try {
            ArrayList<Column> columns = new ArrayList<Column>();
            if (this.getPlatform().isDelimitedIdentifierModeOn()) {
                columnData = metaData.getColumns(this.getDefaultTablePattern(), this.getDefaultColumnPattern());
                while (columnData.next()) {
                    Map values = this.readColumns(columnData, this.getColumnsForColumn());
                    if (!tableName.equals(values.get("TABLE_NAME"))) continue;
                    columns.add(this.readColumn(metaData, values));
                }
            } else {
                columnData = metaData.getColumns(tableName, this.getDefaultColumnPattern());
                while (columnData.next()) {
                    Map values = this.readColumns(columnData, this.getColumnsForColumn());
                    columns.add(this.readColumn(metaData, values));
                }
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

    protected void determineExtraColumnInfo(Table table) throws SQLException {
        StringBuffer query = new StringBuffer();
        query.append("SELECT a.RDB$FIELD_NAME, a.RDB$DEFAULT_SOURCE, b.RDB$FIELD_PRECISION, b.RDB$FIELD_SCALE,");
        query.append(" b.RDB$FIELD_TYPE, b.RDB$FIELD_SUB_TYPE FROM RDB$RELATION_FIELDS a, RDB$FIELDS b");
        query.append(" WHERE a.RDB$RELATION_NAME=? AND a.RDB$FIELD_SOURCE=b.RDB$FIELD_NAME");
        PreparedStatement prepStmt = this.getConnection().prepareStatement(query.toString());
        try {
            prepStmt.setString(1, this.getPlatform().isDelimitedIdentifierModeOn() ? table.getName() : table.getName().toUpperCase());
            ResultSet rs = prepStmt.executeQuery();
            while (rs.next()) {
                boolean scaleSpecified;
                String columnName = rs.getString(1).trim();
                Column column = table.findColumn(columnName, this.getPlatform().isDelimitedIdentifierModeOn());
                if (column == null) continue;
                String defaultValue = rs.getString(2);
                if (!rs.wasNull() && defaultValue != null) {
                    if ((defaultValue = defaultValue.trim()).startsWith("DEFAULT ")) {
                        defaultValue = defaultValue.substring("DEFAULT ".length());
                    }
                    column.setDefaultValue(defaultValue);
                }
                short precision = rs.getShort(3);
                boolean precisionSpecified = !rs.wasNull();
                short scale = rs.getShort(4);
                boolean bl = scaleSpecified = !rs.wasNull();
                if (precisionSpecified) {
                    column.setSizeAndScale(precision, scaleSpecified ? -scale : (short)0);
                }
                short dbType = rs.getShort(5);
                short blobSubType = rs.getShort(6);
                if (rs.wasNull() || dbType != 261 || blobSubType != 1) continue;
                column.setTypeCode(2005);
            }
            rs.close();
        }
        finally {
            prepStmt.close();
        }
    }

    protected void determineAutoIncrementColumns(Table table) throws SQLException {
        InterbaseBuilder builder = (InterbaseBuilder)this.getPlatform().getSqlBuilder();
        Column[] columns = table.getColumns();
        HashMap<String, Column> names = new HashMap<String, Column>();
        int idx = 0;
        while (idx < columns.length) {
            String name = builder.getGeneratorName(table, columns[idx]);
            if (!this.getPlatform().isDelimitedIdentifierModeOn()) {
                name = name.toUpperCase();
            }
            names.put(name, columns[idx]);
            ++idx;
        }
        Statement stmt = this.getConnection().createStatement();
        try {
            ResultSet rs = stmt.executeQuery("SELECT RDB$GENERATOR_NAME FROM RDB$GENERATORS");
            while (rs.next()) {
                String generatorName = rs.getString(1).trim();
                Column column = (Column)names.get(generatorName);
                if (column == null) continue;
                column.setAutoIncrement(true);
            }
            rs.close();
        }
        finally {
            stmt.close();
        }
    }

    protected void adjustColumns(Table table) {
        Column[] columns = table.getColumns();
        int idx = 0;
        while (idx < columns.length) {
            if (columns[idx].getTypeCode() == 6) {
                columns[idx].setTypeCode(7);
            } else if (columns[idx].getTypeCode() == 2 || columns[idx].getTypeCode() == 3) {
                if (columns[idx].getTypeCode() == 2 && columns[idx].getSizeAsInt() == 18 && columns[idx].getScale() == 0) {
                    columns[idx].setTypeCode(-5);
                }
            } else if (TypeMap.isTextType(columns[idx].getTypeCode())) {
                columns[idx].setDefaultValue(this.unescape(columns[idx].getDefaultValue(), "'", "''"));
            }
            ++idx;
        }
    }

    @Override
    protected Collection readPrimaryKeyNames(DatabaseMetaDataWrapper metaData, String tableName) throws SQLException {
        ArrayList<String> pks = new ArrayList<String>();
        ResultSet pkData = null;
        try {
            if (this.getPlatform().isDelimitedIdentifierModeOn()) {
                pkData = metaData.getPrimaryKeys(this.getDefaultTablePattern());
                while (pkData.next()) {
                    Map values = this.readColumns(pkData, this.getColumnsForPK());
                    if (!tableName.equals(values.get("TABLE_NAME"))) continue;
                    pks.add(this.readPrimaryKeyName(metaData, values));
                }
            } else {
                pkData = metaData.getPrimaryKeys(tableName);
                while (pkData.next()) {
                    Map values = this.readColumns(pkData, this.getColumnsForPK());
                    pks.add(this.readPrimaryKeyName(metaData, values));
                }
            }
        }
        finally {
            if (pkData != null) {
                pkData.close();
            }
        }
        return pks;
    }

    @Override
    protected Collection readForeignKeys(DatabaseMetaDataWrapper metaData, String tableName) throws SQLException {
        ListOrderedMap fks = new ListOrderedMap();
        ResultSet fkData = null;
        try {
            if (this.getPlatform().isDelimitedIdentifierModeOn()) {
                fkData = metaData.getForeignKeys(this.getDefaultTablePattern());
                while (fkData.next()) {
                    Map values = this.readColumns(fkData, this.getColumnsForFK());
                    if (!tableName.equals(values.get("FKTABLE_NAME"))) continue;
                    this.readForeignKey(metaData, values, (Map)fks);
                }
            } else {
                fkData = metaData.getForeignKeys(tableName);
                while (fkData.next()) {
                    Map values = this.readColumns(fkData, this.getColumnsForFK());
                    this.readForeignKey(metaData, values, (Map)fks);
                }
            }
        }
        finally {
            if (fkData != null) {
                fkData.close();
            }
        }
        return fks.values();
    }

    @Override
    protected boolean isInternalPrimaryKeyIndex(DatabaseMetaDataWrapper metaData, Table table, Index index) throws SQLException {
        String tableName = this.getPlatform().getSqlBuilder().getTableName(table);
        String indexName = this.getPlatform().getSqlBuilder().getIndexName(index);
        StringBuffer query = new StringBuffer();
        query.append("SELECT RDB$CONSTRAINT_NAME FROM RDB$RELATION_CONSTRAINTS where RDB$RELATION_NAME=? AND RDB$CONSTRAINT_TYPE=? AND RDB$INDEX_NAME=?");
        PreparedStatement stmt = this.getConnection().prepareStatement(query.toString());
        try {
            stmt.setString(1, this.getPlatform().isDelimitedIdentifierModeOn() ? tableName : tableName.toUpperCase());
            stmt.setString(2, "PRIMARY KEY");
            stmt.setString(3, indexName);
            ResultSet resultSet = stmt.executeQuery();
            boolean bl = resultSet.next();
            return bl;
        }
        finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    @Override
    protected boolean isInternalForeignKeyIndex(DatabaseMetaDataWrapper metaData, Table table, ForeignKey fk, Index index) throws SQLException {
        String tableName = this.getPlatform().getSqlBuilder().getTableName(table);
        String indexName = this.getPlatform().getSqlBuilder().getIndexName(index);
        String fkName = this.getPlatform().getSqlBuilder().getForeignKeyName(table, fk);
        StringBuffer query = new StringBuffer();
        query.append("SELECT RDB$CONSTRAINT_NAME FROM RDB$RELATION_CONSTRAINTS where RDB$RELATION_NAME=? AND RDB$CONSTRAINT_TYPE=? AND RDB$CONSTRAINT_NAME=? AND RDB$INDEX_NAME=?");
        PreparedStatement stmt = this.getConnection().prepareStatement(query.toString());
        try {
            stmt.setString(1, this.getPlatform().isDelimitedIdentifierModeOn() ? tableName : tableName.toUpperCase());
            stmt.setString(2, "FOREIGN KEY");
            stmt.setString(3, fkName);
            stmt.setString(4, indexName);
            ResultSet resultSet = stmt.executeQuery();
            boolean bl = resultSet.next();
            return bl;
        }
        finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    @Override
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
                found = true;
                columnData = this.getPlatform().isDelimitedIdentifierModeOn() ? metaData.getColumns(this.getDefaultTablePattern(), this.getDefaultColumnPattern()) : metaData.getColumns(tableName, this.getDefaultColumnPattern());
                while (found && columnData.next()) {
                    values = this.readColumns(columnData, this.getColumnsForColumn());
                    if (this.getPlatform().isDelimitedIdentifierModeOn() && !tableName.equals(values.get("TABLE_NAME")) || table.findColumn((String)values.get("COLUMN_NAME"), this.getPlatform().isDelimitedIdentifierModeOn()) != null) continue;
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

