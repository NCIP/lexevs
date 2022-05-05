/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.platform.derby;

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

public class DerbyModelReader
extends JdbcModelReader {
    public DerbyModelReader(Platform platform) {
        super(platform);
    }

    @Override
    protected Column readColumn(DatabaseMetaDataWrapper metaData, Map values) throws SQLException {
        Column column = super.readColumn(metaData, values);
        String defaultValue = column.getDefaultValue();
        if (defaultValue != null) {
            if ("GENERATED_BY_DEFAULT".equals(defaultValue) || defaultValue.startsWith("AUTOINCREMENT:")) {
                column.setDefaultValue(null);
                column.setAutoIncrement(true);
            } else if (TypeMap.isTextType(column.getTypeCode())) {
                column.setDefaultValue(this.unescape(defaultValue, "'", "''"));
            }
        }
        return column;
    }

    @Override
    protected boolean isInternalForeignKeyIndex(DatabaseMetaDataWrapper metaData, Table table, ForeignKey fk, Index index) {
        return this.isInternalIndex(index);
    }

    @Override
    protected boolean isInternalPrimaryKeyIndex(DatabaseMetaDataWrapper metaData, Table table, Index index) {
        return this.isInternalIndex(index);
    }

    private boolean isInternalIndex(Index index) {
        String name = index.getName();
        if (name != null && name.startsWith("SQL")) {
            try {
                Long.parseLong(name.substring(3));
                return true;
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
        return false;
    }
}

