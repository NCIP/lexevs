/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.platform;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class MetaDataColumnDescriptor {
    private String _columnName;
    private int _jdbcType;
    private Object _defaultValue;

    public MetaDataColumnDescriptor(String columnName, int jdbcType) {
        this(columnName, jdbcType, null);
    }

    public MetaDataColumnDescriptor(String columnName, int jdbcType, Object defaultValue) {
        this._columnName = columnName.toUpperCase();
        this._jdbcType = jdbcType;
        this._defaultValue = defaultValue;
    }

    public String getName() {
        return this._columnName;
    }

    public Object getDefaultValue() {
        return this._defaultValue;
    }

    public int getJdbcType() {
        return this._jdbcType;
    }

    public Object readColumn(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int foundIdx = -1;
        int idx = 1;
        while (foundIdx < 0 && idx <= metaData.getColumnCount()) {
            if (this._columnName.equals(metaData.getColumnName(idx).toUpperCase())) {
                foundIdx = idx;
            }
            ++idx;
        }
        if (foundIdx > 0) {
            switch (this._jdbcType) {
                case -7: {
                    return new Boolean(resultSet.getBoolean(foundIdx));
                }
                case 4: {
                    return new Integer(resultSet.getInt(foundIdx));
                }
                case -6: {
                    return new Short(resultSet.getShort(foundIdx));
                }
            }
            return resultSet.getString(foundIdx);
        }
        return this._defaultValue;
    }
}

