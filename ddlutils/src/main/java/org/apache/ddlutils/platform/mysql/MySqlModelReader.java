/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.platform.mysql;

import java.sql.SQLException;
import java.util.Map;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.ForeignKey;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.DatabaseMetaDataWrapper;
import org.apache.ddlutils.platform.JdbcModelReader;

public class MySqlModelReader
extends JdbcModelReader {
    public MySqlModelReader(Platform platform) {
        super(platform);
        this.setDefaultCatalogPattern(null);
        this.setDefaultSchemaPattern(null);
        this.setDefaultTablePattern(null);
    }

    @Override
    protected Table readTable(DatabaseMetaDataWrapper metaData, Map values) throws SQLException {
        Table table = super.readTable(metaData, values);
        if (table != null) {
            this.determineAutoIncrementFromResultSetMetaData(table, table.getPrimaryKeyColumns());
        }
        return table;
    }

    @Override
    protected Column readColumn(DatabaseMetaDataWrapper metaData, Map values) throws SQLException {
        Column column = super.readColumn(metaData, values);
        if (column.getTypeCode() == 93 && "0000-00-00 00:00:00".equals(column.getDefaultValue())) {
            column.setDefaultValue(null);
        }
        return column;
    }

    @Override
    protected boolean isInternalPrimaryKeyIndex(DatabaseMetaDataWrapper metaData, Table table, Index index) {
        return "PRIMARY".equals(index.getName());
    }

    @Override
    protected boolean isInternalForeignKeyIndex(DatabaseMetaDataWrapper metaData, Table table, ForeignKey fk, Index index) {
        return this.getPlatform().getSqlBuilder().getForeignKeyName(table, fk).equals(index.getName());
    }
}

