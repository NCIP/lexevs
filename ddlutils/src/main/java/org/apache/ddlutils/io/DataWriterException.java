/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.exception.NestableRuntimeException
 */
package org.apache.ddlutils.io;

import org.apache.commons.lang.exception.NestableRuntimeException;

public class DataWriterException
extends NestableRuntimeException {
    private static final long serialVersionUID = 6254759931565130848L;

    public DataWriterException() {
    }

    public DataWriterException(String message) {
        super(message);
    }

    public DataWriterException(Throwable baseEx) {
        super(baseEx);
    }

    public DataWriterException(String message, Throwable baseEx) {
        super(message, baseEx);
    }
}

