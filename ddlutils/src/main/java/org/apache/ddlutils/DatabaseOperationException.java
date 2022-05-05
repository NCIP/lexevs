/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils;

import org.apache.ddlutils.DdlUtilsException;

public class DatabaseOperationException
extends DdlUtilsException {
    private static final long serialVersionUID = -3090677744278358036L;

    public DatabaseOperationException() {
    }

    public DatabaseOperationException(String msg) {
        super(msg);
    }

    public DatabaseOperationException(Throwable baseEx) {
        super(baseEx);
    }

    public DatabaseOperationException(String msg, Throwable baseEx) {
        super(msg, baseEx);
    }
}

