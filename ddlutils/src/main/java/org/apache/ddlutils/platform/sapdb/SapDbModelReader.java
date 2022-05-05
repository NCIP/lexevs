/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.platform.sapdb;

import java.sql.SQLException;
import java.util.Map;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.platform.DatabaseMetaDataWrapper;
import org.apache.ddlutils.platform.JdbcModelReader;

public class SapDbModelReader
extends JdbcModelReader {
    public SapDbModelReader(Platform platform) {
        super(platform);
        this.setDefaultCatalogPattern(null);
        this.setDefaultSchemaPattern(null);
        this.setDefaultTablePattern("%");
    }

    @Override
    protected Column readColumn(DatabaseMetaDataWrapper metaData, Map values) throws SQLException {
        Column column = super.readColumn(metaData, values);
        if (column.getDefaultValue() != null) {
            column.setDefaultValue(column.getDefaultValue().trim());
            if (column.getDefaultValue().startsWith("DEFAULT SERIAL")) {
                column.setAutoIncrement(true);
                column.setDefaultValue(null);
            }
        }
        if (column.getTypeCode() == 3 && column.getSizeAsInt() == 38 && column.getScale() == 0) {
            column.setTypeCode(-5);
        }
        return column;
    }
}

