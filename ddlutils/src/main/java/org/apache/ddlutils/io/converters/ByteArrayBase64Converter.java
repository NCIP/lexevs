/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.codec.binary.Base64
 */
package org.apache.ddlutils.io.converters;

import org.apache.commons.codec.binary.Base64;
import org.apache.ddlutils.io.converters.ConversionException;
import org.apache.ddlutils.io.converters.SqlTypeConverter;

public class ByteArrayBase64Converter
implements SqlTypeConverter {

    public Object convertFromString(String textRep, int sqlTypeCode) throws ConversionException {
        try {
            return textRep == null ? null : Base64.decodeBase64((byte[])textRep.getBytes());
        }
        catch (Exception ex) {
            throw new ConversionException(ex);
        }
    }


    public String convertToString(Object obj, int sqlTypeCode) throws ConversionException {
        try {
            return obj == null ? null : new String(Base64.encodeBase64((byte[])((byte[])obj)));
        }
        catch (Exception ex) {
            throw new ConversionException(ex);
        }
    }
}

