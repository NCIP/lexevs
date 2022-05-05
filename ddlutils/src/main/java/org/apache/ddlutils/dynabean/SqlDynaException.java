/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.dynabean;

import org.apache.ddlutils.DdlUtilsException;

public class SqlDynaException
extends DdlUtilsException {
    private static final long serialVersionUID = 7904337501884384392L;

    public SqlDynaException() {
    }

    public SqlDynaException(String msg) {
        super(msg);
    }

    public SqlDynaException(Throwable baseEx) {
        super(baseEx);
    }

    public SqlDynaException(String msg, Throwable baseEx) {
        super(msg, baseEx);
    }
}

