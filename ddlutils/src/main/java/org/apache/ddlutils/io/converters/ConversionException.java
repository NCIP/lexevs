/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.exception.NestableRuntimeException
 */
package org.apache.ddlutils.io.converters;

import org.apache.commons.lang.exception.NestableRuntimeException;

public class ConversionException
extends NestableRuntimeException {
    private static final long serialVersionUID = -1582788733576384843L;

    public ConversionException() {
    }

    public ConversionException(String message) {
        super(message);
    }

    public ConversionException(Throwable baseEx) {
        super(baseEx);
    }

    public ConversionException(String message, Throwable baseEx) {
        super(message, baseEx);
    }
}

