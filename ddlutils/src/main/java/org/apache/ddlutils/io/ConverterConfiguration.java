/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.io;

import java.util.HashMap;
import org.apache.ddlutils.io.converters.ByteArrayBase64Converter;
import org.apache.ddlutils.io.converters.DateConverter;
import org.apache.ddlutils.io.converters.NumberConverter;
import org.apache.ddlutils.io.converters.SqlTypeConverter;
import org.apache.ddlutils.io.converters.TimeConverter;
import org.apache.ddlutils.io.converters.TimestampConverter;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.util.Jdbc3Utils;

public class ConverterConfiguration {
    private HashMap _convertersPerType = new HashMap();
    private HashMap _convertersPerPath = new HashMap();

    public ConverterConfiguration() {
        NumberConverter numberConverter = new NumberConverter();
        ByteArrayBase64Converter binaryConverter = new ByteArrayBase64Converter();
        this.registerConverter(91, new DateConverter());
        this.registerConverter(92, new TimeConverter());
        this.registerConverter(93, new TimestampConverter());
        this.registerConverter(-5, numberConverter);
        this.registerConverter(-7, numberConverter);
        this.registerConverter(3, numberConverter);
        this.registerConverter(8, numberConverter);
        this.registerConverter(6, numberConverter);
        this.registerConverter(4, numberConverter);
        this.registerConverter(2, numberConverter);
        this.registerConverter(7, numberConverter);
        this.registerConverter(5, numberConverter);
        this.registerConverter(-6, numberConverter);
        this.registerConverter(-2, binaryConverter);
        this.registerConverter(-3, binaryConverter);
        this.registerConverter(-4, binaryConverter);
        this.registerConverter(2004, binaryConverter);
        if (Jdbc3Utils.supportsJava14JdbcTypes()) {
            this.registerConverter(Jdbc3Utils.determineBooleanTypeCode(), numberConverter);
        }
    }

    public void registerConverter(int sqlTypeCode, SqlTypeConverter converter) {
        this._convertersPerType.put(new Integer(sqlTypeCode), converter);
    }

    public void registerConverter(String tableName, String columnName, SqlTypeConverter converter) {
        this._convertersPerPath.put(String.valueOf(tableName) + "/" + columnName, converter);
    }

    public SqlTypeConverter getRegisteredConverter(Table table, Column column) {
        SqlTypeConverter result = (SqlTypeConverter)this._convertersPerPath.get(String.valueOf(table.getName()) + "/" + column.getName());
        if (result == null) {
            result = (SqlTypeConverter)this._convertersPerType.get(new Integer(column.getTypeCode()));
        }
        return result;
    }
}

