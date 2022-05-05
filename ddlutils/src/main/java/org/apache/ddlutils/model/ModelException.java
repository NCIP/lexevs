/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.model;

import org.apache.ddlutils.DdlUtilsException;

public class ModelException
extends DdlUtilsException {
    private static final long serialVersionUID = -694578915559780711L;

    public ModelException() {
    }

    public ModelException(String msg) {
        super(msg);
    }

    public ModelException(Throwable baseEx) {
        super(baseEx);
    }

    public ModelException(String msg, Throwable baseEx) {
        super(msg, baseEx);
    }
}

