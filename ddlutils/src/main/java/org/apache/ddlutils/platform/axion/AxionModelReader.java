/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.platform.axion;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.DatabaseMetaDataWrapper;
import org.apache.ddlutils.platform.JdbcModelReader;

public class AxionModelReader
extends JdbcModelReader {
    public AxionModelReader(Platform platform) {
        super(platform);
        this.setDefaultCatalogPattern(null);
        this.setDefaultSchemaPattern(null);
        this.setDefaultTablePattern("%");
    }

    @Override
    protected Collection readPrimaryKeyNames(DatabaseMetaDataWrapper metaData, String tableName) throws SQLException {
        return new ArrayList();
    }

    @Override
    protected Collection readForeignKeys(DatabaseMetaDataWrapper metaData, String tableName) throws SQLException {
        return new ArrayList();
    }

    @Override
    protected void removeSystemIndices(DatabaseMetaDataWrapper metaData, Table table) throws SQLException {
        int indexIdx = 0;
        while (indexIdx < table.getIndexCount()) {
            Index index = table.getIndex(indexIdx);
            if (index.getName().startsWith("SYS_")) {
                table.removeIndex(indexIdx);
                continue;
            }
            ++indexIdx;
        }
    }
}

