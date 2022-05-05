/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.beanutils.ConversionException
 *  org.apache.commons.beanutils.ConvertUtils
 */
package org.apache.ddlutils.platform;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.ddlutils.model.TypeMap;
import org.apache.ddlutils.util.Jdbc3Utils;

public class DefaultValueHelper {
    public String convert(String defaultValue, int originalTypeCode, int targetTypeCode) {
        String result = defaultValue;
        if (defaultValue != null) {
            switch (originalTypeCode) {
                case -7: {
                    result = this.convertBoolean(defaultValue, targetTypeCode).toString();
                    break;
                }
                case 91: {
                    if (targetTypeCode != 93) break;
                    try {
                        Date date = Date.valueOf(result);
                        return new Timestamp(date.getTime()).toString();
                    }
                    catch (IllegalArgumentException date) {
                        break;
                    }
                }
                case 92: {
                    if (targetTypeCode != 93) break;
                    try {
                        Time time = Time.valueOf(result);
                        return new Timestamp(time.getTime()).toString();
                    }
                    catch (IllegalArgumentException illegalArgumentException) {
                        break;
                    }
                }
                default: {
                    if (!Jdbc3Utils.supportsJava14JdbcTypes() || originalTypeCode != Jdbc3Utils.determineBooleanTypeCode()) break;
                    result = this.convertBoolean(defaultValue, targetTypeCode).toString();
                }
            }
        }
        return result;
    }

    private Object convertBoolean(String defaultValue, int targetTypeCode) {
        Boolean value = null;
        Object result = null;
        try {
            value = (Boolean)ConvertUtils.convert((String)defaultValue, Boolean.class);
        }
        catch (ConversionException ex) {
            return defaultValue;
        }
        result = targetTypeCode == -7 || Jdbc3Utils.supportsJava14JdbcTypes() && targetTypeCode == Jdbc3Utils.determineBooleanTypeCode() ? value : (TypeMap.isNumericType(targetTypeCode) ? (value != false ? new Integer(1) : new Integer(0)) : value.toString());
        return result;
    }
}

