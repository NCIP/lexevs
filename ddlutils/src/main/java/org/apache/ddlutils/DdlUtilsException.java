/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.exception.NestableRuntimeException
 */
package org.apache.ddlutils;

import org.apache.commons.lang.exception.NestableRuntimeException;

public class DdlUtilsException
extends NestableRuntimeException {
    private static final long serialVersionUID = 5624776387174310551L;

    public DdlUtilsException() {
    }

    public DdlUtilsException(String msg) {
        super(msg);
    }

    public DdlUtilsException(Throwable baseEx) {
        super(baseEx);
    }

    public DdlUtilsException(String msg, Throwable baseEx) {
        super(msg, baseEx);
    }
}

