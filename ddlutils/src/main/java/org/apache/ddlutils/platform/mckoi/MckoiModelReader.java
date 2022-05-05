/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.collections.map.ListOrderedMap
 */
package org.apache.ddlutils.platform.mckoi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.model.TypeMap;
import org.apache.ddlutils.platform.DatabaseMetaDataWrapper;
import org.apache.ddlutils.platform.JdbcModelReader;

public class MckoiModelReader
extends JdbcModelReader {
    public MckoiModelReader(Platform platform) {
        super(platform);
        this.setDefaultCatalogPattern(null);
        this.setDefaultSchemaPattern(null);
    }

    @Override
    protected Table readTable(DatabaseMetaDataWrapper metaData, Map values) throws SQLException {
        Table table = super.readTable(metaData, values);
        if (table != null) {
            StringBuffer query = new StringBuffer();
            query.append("SELECT uniqueColumns.column, uniqueColumns.seq_no, uniqueInfo.name");
            query.append(" FROM SYS_INFO.sUSRUniqueColumns uniqueColumns, SYS_INFO.sUSRUniqueInfo uniqueInfo");
            query.append(" WHERE uniqueColumns.un_id = uniqueInfo.id AND uniqueInfo.table = '");
            query.append(table.getName());
            if (table.getSchema() != null) {
                query.append("' AND uniqueInfo.schema = '");
                query.append(table.getSchema());
            }
            query.append("'");
            Statement stmt = this.getConnection().createStatement();
            ResultSet resultSet = stmt.executeQuery(query.toString());
            ListOrderedMap indices = new ListOrderedMap();
            HashMap<String, Object> indexValues = new HashMap<String, Object>();
            indexValues.put("NON_UNIQUE", Boolean.FALSE);
            while (resultSet.next()) {
                indexValues.put("COLUMN_NAME", resultSet.getString(1));
                indexValues.put("ORDINAL_POSITION", new Short(resultSet.getShort(2)));
                indexValues.put("INDEX_NAME", resultSet.getString(3));
                this.readIndex(metaData, indexValues, (Map)indices);
            }
            resultSet.close();
            table.addIndices(indices.values());
        }
        return table;
    }

    @Override
    protected Column readColumn(DatabaseMetaDataWrapper metaData, Map values) throws SQLException {
        String defaultValue;
        Column column = super.readColumn(metaData, values);
        if (column.getSize() != null && column.getSizeAsInt() <= 0) {
            column.setSize(null);
        }
        if ((defaultValue = column.getDefaultValue()) != null) {
            if (defaultValue.toLowerCase().startsWith("nextval('") || defaultValue.toLowerCase().startsWith("uniquekey('")) {
                column.setDefaultValue(null);
                column.setAutoIncrement(true);
            } else if (TypeMap.isTextType(column.getTypeCode())) {
                column.setDefaultValue(this.unescape(column.getDefaultValue(), "'", "\\'"));
            }
        }
        return column;
    }
}

