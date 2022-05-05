/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.apache.ddlutils.model.JdbcTypeCategoryEnum;
import org.apache.ddlutils.util.Jdbc3Utils;

public abstract class TypeMap {
    public static final String ARRAY = "ARRAY";
    public static final String BIGINT = "BIGINT";
    public static final String BINARY = "BINARY";
    public static final String BIT = "BIT";
    public static final String BLOB = "BLOB";
    public static final String BOOLEAN = "BOOLEAN";
    public static final String CHAR = "CHAR";
    public static final String CLOB = "CLOB";
    public static final String DATALINK = "DATALINK";
    public static final String DATE = "DATE";
    public static final String DECIMAL = "DECIMAL";
    public static final String DISTINCT = "DISTINCT";
    public static final String DOUBLE = "DOUBLE";
    public static final String FLOAT = "FLOAT";
    public static final String INTEGER = "INTEGER";
    public static final String JAVA_OBJECT = "JAVA_OBJECT";
    public static final String LONGVARBINARY = "LONGVARBINARY";
    public static final String LONGVARCHAR = "LONGVARCHAR";
    public static final String NULL = "NULL";
    public static final String NUMERIC = "NUMERIC";
    public static final String OTHER = "OTHER";
    public static final String REAL = "REAL";
    public static final String REF = "REF";
    public static final String SMALLINT = "SMALLINT";
    public static final String STRUCT = "STRUCT";
    public static final String TIME = "TIME";
    public static final String TIMESTAMP = "TIMESTAMP";
    public static final String TINYINT = "TINYINT";
    public static final String VARBINARY = "VARBINARY";
    public static final String VARCHAR = "VARCHAR";
    private static HashMap _typeNameToTypeCode = new HashMap();
    private static HashMap _typeCodeToTypeName = new HashMap();
    private static HashMap _typesPerCategory = new HashMap();

    static {
        TypeMap.registerJdbcType(2003, ARRAY, JdbcTypeCategoryEnum.SPECIAL);
        TypeMap.registerJdbcType(-5, BIGINT, JdbcTypeCategoryEnum.NUMERIC);
        TypeMap.registerJdbcType(-2, BINARY, JdbcTypeCategoryEnum.BINARY);
        TypeMap.registerJdbcType(-7, BIT, JdbcTypeCategoryEnum.NUMERIC);
        TypeMap.registerJdbcType(2004, BLOB, JdbcTypeCategoryEnum.BINARY);
        TypeMap.registerJdbcType(1, CHAR, JdbcTypeCategoryEnum.TEXTUAL);
        TypeMap.registerJdbcType(2005, CLOB, JdbcTypeCategoryEnum.TEXTUAL);
        TypeMap.registerJdbcType(91, DATE, JdbcTypeCategoryEnum.DATETIME);
        TypeMap.registerJdbcType(3, DECIMAL, JdbcTypeCategoryEnum.NUMERIC);
        TypeMap.registerJdbcType(2001, DISTINCT, JdbcTypeCategoryEnum.SPECIAL);
        TypeMap.registerJdbcType(8, DOUBLE, JdbcTypeCategoryEnum.NUMERIC);
        TypeMap.registerJdbcType(6, FLOAT, JdbcTypeCategoryEnum.NUMERIC);
        TypeMap.registerJdbcType(4, INTEGER, JdbcTypeCategoryEnum.NUMERIC);
        TypeMap.registerJdbcType(2000, JAVA_OBJECT, JdbcTypeCategoryEnum.SPECIAL);
        TypeMap.registerJdbcType(-4, LONGVARBINARY, JdbcTypeCategoryEnum.BINARY);
        TypeMap.registerJdbcType(-1, LONGVARCHAR, JdbcTypeCategoryEnum.TEXTUAL);
        TypeMap.registerJdbcType(0, NULL, JdbcTypeCategoryEnum.SPECIAL);
        TypeMap.registerJdbcType(2, NUMERIC, JdbcTypeCategoryEnum.NUMERIC);
        TypeMap.registerJdbcType(1111, OTHER, JdbcTypeCategoryEnum.SPECIAL);
        TypeMap.registerJdbcType(7, REAL, JdbcTypeCategoryEnum.NUMERIC);
        TypeMap.registerJdbcType(2006, REF, JdbcTypeCategoryEnum.SPECIAL);
        TypeMap.registerJdbcType(5, SMALLINT, JdbcTypeCategoryEnum.NUMERIC);
        TypeMap.registerJdbcType(2002, STRUCT, JdbcTypeCategoryEnum.SPECIAL);
        TypeMap.registerJdbcType(92, TIME, JdbcTypeCategoryEnum.DATETIME);
        TypeMap.registerJdbcType(93, TIMESTAMP, JdbcTypeCategoryEnum.DATETIME);
        TypeMap.registerJdbcType(-6, TINYINT, JdbcTypeCategoryEnum.NUMERIC);
        TypeMap.registerJdbcType(-3, VARBINARY, JdbcTypeCategoryEnum.BINARY);
        TypeMap.registerJdbcType(12, VARCHAR, JdbcTypeCategoryEnum.TEXTUAL);
        if (Jdbc3Utils.supportsJava14JdbcTypes()) {
            TypeMap.registerJdbcType(Jdbc3Utils.determineBooleanTypeCode(), BOOLEAN, JdbcTypeCategoryEnum.NUMERIC);
            TypeMap.registerJdbcType(Jdbc3Utils.determineDatalinkTypeCode(), DATALINK, JdbcTypeCategoryEnum.SPECIAL);
        }
        _typeNameToTypeCode.put("BOOLEANINT", new Integer(-6));
        _typeNameToTypeCode.put("BOOLEANCHAR", new Integer(1));
    }

    public static Integer getJdbcTypeCode(String typeName) {
        return (Integer)_typeNameToTypeCode.get(typeName.toUpperCase());
    }

    public static String getJdbcTypeName(int typeCode) {
        return (String)_typeCodeToTypeName.get(new Integer(typeCode));
    }

    protected static void registerJdbcType(int typeCode, String typeName, JdbcTypeCategoryEnum category) {
        Integer typeId = new Integer(typeCode);
        _typeNameToTypeCode.put(typeName.toUpperCase(), typeId);
        _typeCodeToTypeName.put(typeId, typeName.toUpperCase());
        HashSet<Integer> typesInCategory = (HashSet<Integer>)_typesPerCategory.get((Object)category);
        if (typesInCategory == null) {
            typesInCategory = new HashSet<Integer>();
            _typesPerCategory.put(category, typesInCategory);
        }
        typesInCategory.add(typeId);
    }

    public static boolean isNumericType(int jdbcTypeCode) {
        Set typesInCategory = (Set)_typesPerCategory.get((Object)JdbcTypeCategoryEnum.NUMERIC);
        return typesInCategory == null ? false : typesInCategory.contains(new Integer(jdbcTypeCode));
    }

    public static boolean isDateTimeType(int jdbcTypeCode) {
        Set typesInCategory = (Set)_typesPerCategory.get((Object)JdbcTypeCategoryEnum.DATETIME);
        return typesInCategory == null ? false : typesInCategory.contains(new Integer(jdbcTypeCode));
    }

    public static boolean isTextType(int jdbcTypeCode) {
        Set typesInCategory = (Set)_typesPerCategory.get((Object)JdbcTypeCategoryEnum.TEXTUAL);
        return typesInCategory == null ? false : typesInCategory.contains(new Integer(jdbcTypeCode));
    }

    public static boolean isBinaryType(int jdbcTypeCode) {
        Set typesInCategory = (Set)_typesPerCategory.get((Object)JdbcTypeCategoryEnum.BINARY);
        return typesInCategory == null ? false : typesInCategory.contains(new Integer(jdbcTypeCode));
    }

    public static boolean isSpecialType(int jdbcTypeCode) {
        Set typesInCategory = (Set)_typesPerCategory.get((Object)JdbcTypeCategoryEnum.SPECIAL);
        return typesInCategory == null ? false : typesInCategory.contains(new Integer(jdbcTypeCode));
    }
}

