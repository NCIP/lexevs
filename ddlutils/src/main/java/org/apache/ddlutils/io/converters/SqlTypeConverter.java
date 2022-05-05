/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.io.converters;

import org.apache.ddlutils.io.converters.ConversionException;

public interface SqlTypeConverter {
    public Object convertFromString(String var1, int var2) throws ConversionException;

    public String convertToString(Object var1, int var2) throws ConversionException;
}

