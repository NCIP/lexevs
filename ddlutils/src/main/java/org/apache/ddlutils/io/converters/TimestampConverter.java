/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.io.converters;

import java.sql.Timestamp;
import org.apache.ddlutils.io.converters.ConversionException;
import org.apache.ddlutils.io.converters.SqlTypeConverter;

public class TimestampConverter
implements SqlTypeConverter {

    public Object convertFromString(String textRep, int sqlTypeCode) throws ConversionException {
        if (textRep == null) {
            return null;
        }
        if (sqlTypeCode == 93) {
            return Timestamp.valueOf(textRep);
        }
        return textRep;
    }


    public String convertToString(Object obj, int sqlTypeCode) throws ConversionException {
        return obj == null ? null : ((Timestamp)obj).toString();
    }
}

