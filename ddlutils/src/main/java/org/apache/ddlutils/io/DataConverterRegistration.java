/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.tools.ant.BuildException
 */
package org.apache.ddlutils.io;

import org.apache.ddlutils.io.converters.SqlTypeConverter;
import org.apache.ddlutils.model.TypeMap;
import org.apache.tools.ant.BuildException;

public class DataConverterRegistration {
    private SqlTypeConverter _converter;
    private int _typeCode = Integer.MIN_VALUE;
    private String _table;
    private String _column;

    public SqlTypeConverter getConverter() {
        return this._converter;
    }

    public void setClassName(String converterClassName) throws BuildException {
        try {
            this._converter = (SqlTypeConverter)this.getClass().getClassLoader().loadClass(converterClassName).newInstance();
        }
        catch (Exception ex) {
            throw new BuildException((Throwable)ex);
        }
    }

    public int getTypeCode() {
        return this._typeCode;
    }

    public void setJdbcType(String jdbcTypeName) throws BuildException {
        Integer typeCode = TypeMap.getJdbcTypeCode(jdbcTypeName);
        if (typeCode == null) {
            throw new BuildException("Unknown jdbc type " + jdbcTypeName);
        }
        this._typeCode = typeCode;
    }

    public String getColumn() {
        return this._column;
    }

    public void setColumn(String column) throws BuildException {
        if (column == null || column.length() == 0) {
            throw new BuildException("Please specify a non-empty column name");
        }
        this._column = column;
    }

    public String getTable() {
        return this._table;
    }

    public void setTable(String table) throws BuildException {
        if (table == null || table.length() == 0) {
            throw new BuildException("Please specify a non-empty table name");
        }
        this._table = table;
    }
}

