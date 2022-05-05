/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.logging.Log
 *  org.apache.commons.logging.LogFactory
 *  org.apache.tools.ant.BuildException
 */
package org.apache.ddlutils.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.task.DatabaseTaskBase;
import org.apache.tools.ant.BuildException;

public abstract class Command {
    protected final Log _log = LogFactory.getLog(this.getClass());
    private boolean _failOnError = true;

    public boolean isFailOnError() {
        return this._failOnError;
    }

    public void setFailOnError(boolean failOnError) {
        this._failOnError = failOnError;
    }

    protected void handleException(Exception ex, String msg) throws BuildException {
        if (this.isFailOnError()) {
            if (ex instanceof BuildException) {
                throw (BuildException)ex;
            }
            throw new BuildException(msg, (Throwable)ex);
        }
        this._log.error((Object)msg, (Throwable)ex);
    }

    public abstract boolean isRequiringModel();

    public abstract void execute(DatabaseTaskBase var1, Database var2) throws BuildException;
}

