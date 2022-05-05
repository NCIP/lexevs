/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.platform.postgresql;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.ForeignKey;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.model.TypeMap;
import org.apache.ddlutils.platform.DatabaseMetaDataWrapper;
import org.apache.ddlutils.platform.JdbcModelReader;

public class PostgreSqlModelReader
extends JdbcModelReader {
    public PostgreSqlModelReader(Platform platform) {
        super(platform);
        this.setDefaultCatalogPattern(null);
        this.setDefaultSchemaPattern(null);
        this.setDefaultTablePattern(null);
    }

    @Override
    protected Table readTable(DatabaseMetaDataWrapper metaData, Map values) throws SQLException {
        Table table = super.readTable(metaData, values);
        if (table != null) {
            HashMap<String, Index> uniquesByName = new HashMap<String, Index>();
            int indexIdx = 0;
            while (indexIdx < table.getIndexCount()) {
                Index index = table.getIndex(indexIdx);
                if (index.isUnique() && index.getName() != null) {
                    uniquesByName.put(index.getName(), index);
                }
                ++indexIdx;
            }
            int columnIdx = 0;
            while (columnIdx < table.getColumnCount()) {
                String indexName;
                Column column = table.getColumn(columnIdx);
                if (column.isAutoIncrement() && !column.isPrimaryKey() && uniquesByName.containsKey(indexName = String.valueOf(table.getName()) + "_" + column.getName() + "_key")) {
                    table.removeIndex((Index)uniquesByName.get(indexName));
                    uniquesByName.remove(indexName);
                }
                ++columnIdx;
            }
        }
        return table;
    }

    @Override
    protected Column readColumn(DatabaseMetaDataWrapper metaData, Map values) throws SQLException {
        String defaultValue;
        Column column = super.readColumn(metaData, values);
        if (column.getSize() != null) {
            if (column.getSizeAsInt() <= 0) {
                column.setSize(null);
                if (column.getTypeCode() == -2) {
                    column.setTypeCode(-4);
                } else if (column.getTypeCode() == 12) {
                    column.setTypeCode(-1);
                }
            } else if (column.getSizeAsInt() == Integer.MAX_VALUE) {
                column.setSize(null);
                if (column.getTypeCode() == 12) {
                    column.setTypeCode(-1);
                } else if (column.getTypeCode() == -2) {
                    column.setTypeCode(-4);
                }
            }
        }
        if ((defaultValue = column.getDefaultValue()) != null && defaultValue.length() > 0) {
            if (defaultValue.startsWith("nextval(")) {
                column.setAutoIncrement(true);
                defaultValue = null;
            } else {
                switch (column.getTypeCode()) {
                    case -5: 
                    case 2: 
                    case 3: 
                    case 4: {
                        defaultValue = this.extractUndelimitedDefaultValue(defaultValue);
                        break;
                    }
                    case -1: 
                    case 1: 
                    case 12: 
                    case 91: 
                    case 92: 
                    case 93: {
                        defaultValue = this.extractDelimitedDefaultValue(defaultValue);
                    }
                }
                if (TypeMap.isTextType(column.getTypeCode())) {
                    defaultValue = this.unescape(defaultValue, "'", "''");
                }
            }
            column.setDefaultValue(defaultValue);
        }
        return column;
    }

    private String extractDelimitedDefaultValue(String defaultValue) {
        int valueEnd;
        if (defaultValue.startsWith("'") && (valueEnd = defaultValue.indexOf("'::")) > 0) {
            return defaultValue.substring("'".length(), valueEnd);
        }
        return defaultValue;
    }

    private String extractUndelimitedDefaultValue(String defaultValue) {
        int valueEnd = defaultValue.indexOf("::");
        if (valueEnd > 0) {
            return defaultValue.substring(0, valueEnd);
        }
        return defaultValue;
    }

    @Override
    protected boolean isInternalForeignKeyIndex(DatabaseMetaDataWrapper metaData, Table table, ForeignKey fk, Index index) {
        return false;
    }

    @Override
    protected boolean isInternalPrimaryKeyIndex(DatabaseMetaDataWrapper metaData, Table table, Index index) {
        return (String.valueOf(table.getName()) + "_pkey").equals(index.getName());
    }
}

