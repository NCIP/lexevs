/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.platform.mysql;

import java.sql.SQLException;
import java.util.Map;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.platform.DatabaseMetaDataWrapper;
import org.apache.ddlutils.platform.mysql.MySqlModelReader;

public class MySql50ModelReader
extends MySqlModelReader {
    public MySql50ModelReader(Platform platform) {
        super(platform);
    }

    @Override
    protected Column readColumn(DatabaseMetaDataWrapper metaData, Map values) throws SQLException {
        Column column = super.readColumn(metaData, values);
        if ("".equals(column.getDefaultValue())) {
            column.setDefaultValue(null);
        }
        return column;
    }
}

