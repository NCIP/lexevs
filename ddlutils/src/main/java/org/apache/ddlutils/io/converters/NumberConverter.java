/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.beanutils.ConvertUtils
 */
package org.apache.ddlutils.io.converters;

import java.math.BigDecimal;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.ddlutils.io.converters.ConversionException;
import org.apache.ddlutils.io.converters.SqlTypeConverter;
import org.apache.ddlutils.util.Jdbc3Utils;

public class NumberConverter
implements SqlTypeConverter {

    public Object convertFromString(String textRep, int sqlTypeCode) throws ConversionException {
        if (textRep == null) {
            return null;
        }
        Class targetClass = null;
        switch (sqlTypeCode) {
            case -5: {
                targetClass = Long.class;
                break;
            }
            case -7: {
                targetClass = Boolean.class;
                break;
            }
            case 2: 
            case 3: {
                targetClass = BigDecimal.class;
                break;
            }
            case 6: 
            case 8: {
                targetClass = Double.class;
                break;
            }
            case 4: {
                targetClass = Integer.class;
                break;
            }
            case 7: {
                targetClass = Float.class;
                break;
            }
            case -6: 
            case 5: {
                targetClass = Short.class;
                break;
            }
            default: {
                if (!Jdbc3Utils.supportsJava14JdbcTypes() || sqlTypeCode != Jdbc3Utils.determineBooleanTypeCode()) break;
                targetClass = Boolean.class;
            }
        }
        return targetClass == null ? textRep : ConvertUtils.convert((String)textRep, targetClass);
    }


    public String convertToString(Object obj, int sqlTypeCode) throws ConversionException {
        if (obj == null) {
            return null;
        }
        if (sqlTypeCode == -7) {
            return (Boolean)obj != false ? "1" : "0";
        }
        return obj.toString();
    }
}

