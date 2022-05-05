/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.exception.NestableRuntimeException
 */
package org.apache.ddlutils.io;

import org.apache.commons.lang.exception.NestableRuntimeException;

public class DataSinkException
extends NestableRuntimeException {
    private static final long serialVersionUID = 6790909409839782437L;

    public DataSinkException() {
    }

    public DataSinkException(String message) {
        super(message);
    }

    public DataSinkException(Throwable baseEx) {
        super(baseEx);
    }

    public DataSinkException(String message, Throwable baseEx) {
        super(message, baseEx);
    }
}

