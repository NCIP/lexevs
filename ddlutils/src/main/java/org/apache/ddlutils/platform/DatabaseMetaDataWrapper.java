/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.platform;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseMetaDataWrapper {
    private DatabaseMetaData _metaData;
    private String _catalog;
    private String _schemaPattern;
    private String[] _tableTypes;

    public DatabaseMetaData getMetaData() {
        return this._metaData;
    }

    public void setMetaData(DatabaseMetaData metaData) {
        this._metaData = metaData;
    }

    public String getCatalog() {
        return this._catalog;
    }

    public void setCatalog(String catalog) {
        this._catalog = catalog;
    }

    public String getSchemaPattern() {
        return this._schemaPattern;
    }

    public void setSchemaPattern(String schema) {
        this._schemaPattern = schema;
    }

    public String[] getTableTypes() {
        return this._tableTypes;
    }

    public void setTableTypes(String[] types) {
        this._tableTypes = types;
    }

    public ResultSet getTables(String tableNamePattern) throws SQLException {
        return this.getMetaData().getTables(this.getCatalog(), this.getSchemaPattern(), tableNamePattern, this.getTableTypes());
    }

    public ResultSet getColumns(String tableNamePattern, String columnNamePattern) throws SQLException {
        return this.getMetaData().getColumns(this.getCatalog(), this.getSchemaPattern(), tableNamePattern, columnNamePattern);
    }

    public ResultSet getPrimaryKeys(String tableNamePattern) throws SQLException {
        return this.getMetaData().getPrimaryKeys(this.getCatalog(), this.getSchemaPattern(), tableNamePattern);
    }

    public ResultSet getForeignKeys(String tableNamePattern) throws SQLException {
        return this.getMetaData().getImportedKeys(this.getCatalog(), this.getSchemaPattern(), tableNamePattern);
    }

    public ResultSet getIndices(String tableNamePattern, boolean unique, boolean approximate) throws SQLException {
        return this.getMetaData().getIndexInfo(this.getCatalog(), this.getSchemaPattern(), tableNamePattern, unique, approximate);
    }
}

