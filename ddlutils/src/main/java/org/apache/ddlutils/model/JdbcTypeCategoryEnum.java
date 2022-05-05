/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.enums.ValuedEnum
 */
package org.apache.ddlutils.model;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.enums.ValuedEnum;

public class JdbcTypeCategoryEnum
extends ValuedEnum {
    public static final int VALUE_NUMERIC = 1;
    public static final int VALUE_DATETIME = 2;
    public static final int VALUE_TEXTUAL = 3;
    public static final int VALUE_BINARY = 4;
    public static final int VALUE_SPECIAL = 5;
    public static final int VALUE_OTHER = 6;
    public static final JdbcTypeCategoryEnum NUMERIC = new JdbcTypeCategoryEnum("numeric", 1);
    public static final JdbcTypeCategoryEnum DATETIME = new JdbcTypeCategoryEnum("datetime", 2);
    public static final JdbcTypeCategoryEnum TEXTUAL = new JdbcTypeCategoryEnum("textual", 3);
    public static final JdbcTypeCategoryEnum BINARY = new JdbcTypeCategoryEnum("binary", 4);
    public static final JdbcTypeCategoryEnum SPECIAL = new JdbcTypeCategoryEnum("special", 5);
    public static final JdbcTypeCategoryEnum OTHER = new JdbcTypeCategoryEnum("other", 6);
    private static final long serialVersionUID = -2695615907467866410L;

    private JdbcTypeCategoryEnum(String defaultTextRep, int value) {
        super(defaultTextRep, value);
    }

    public static JdbcTypeCategoryEnum getEnum(String defaultTextRep) {
        return (JdbcTypeCategoryEnum)JdbcTypeCategoryEnum.getEnum(JdbcTypeCategoryEnum.class, (String)defaultTextRep);
    }

    public static JdbcTypeCategoryEnum getEnum(int intValue) {
        return (JdbcTypeCategoryEnum)JdbcTypeCategoryEnum.getEnum(JdbcTypeCategoryEnum.class, (int)intValue);
    }

    public static Map getEnumMap() {
        return JdbcTypeCategoryEnum.getEnumMap(JdbcTypeCategoryEnum.class);
    }

    public static List getEnumList() {
        return JdbcTypeCategoryEnum.getEnumList(JdbcTypeCategoryEnum.class);
    }

    public static Iterator iterator() {
        return JdbcTypeCategoryEnum.iterator(JdbcTypeCategoryEnum.class);
    }
}

