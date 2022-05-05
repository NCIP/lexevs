/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.platform.hsqldb;

import java.sql.SQLException;
import java.util.Map;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.ForeignKey;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.model.TypeMap;
import org.apache.ddlutils.platform.DatabaseMetaDataWrapper;
import org.apache.ddlutils.platform.JdbcModelReader;

public class HsqlDbModelReader
extends JdbcModelReader {
    public HsqlDbModelReader(Platform platform) {
        super(platform);
        this.setDefaultCatalogPattern(null);
        this.setDefaultSchemaPattern(null);
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
        if (TypeMap.isTextType(column.getTypeCode()) && column.getDefaultValue() != null) {
            column.setDefaultValue(this.unescape(column.getDefaultValue(), "'", "''"));
        }
        return column;
    }

    @Override
    protected boolean isInternalForeignKeyIndex(DatabaseMetaDataWrapper metaData, Table table, ForeignKey fk, Index index) {
        String name = index.getName();
        return name != null && name.startsWith("SYS_IDX_");
    }

    @Override
    protected boolean isInternalPrimaryKeyIndex(DatabaseMetaDataWrapper metaData, Table table, Index index) {
        String name = index.getName();
        return name != null && (name.startsWith("SYS_PK_") || name.startsWith("SYS_IDX_"));
    }
}

