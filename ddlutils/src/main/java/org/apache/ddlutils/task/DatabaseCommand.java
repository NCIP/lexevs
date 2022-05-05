/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.dbcp.BasicDataSource
 *  org.apache.tools.ant.BuildException
 */
package org.apache.ddlutils.task;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.task.Command;
import org.apache.ddlutils.task.PlatformConfiguration;
import org.apache.tools.ant.BuildException;

public abstract class DatabaseCommand
extends Command {
    private PlatformConfiguration _platformConf = new PlatformConfiguration();

    protected String getDatabaseType() {
        return this._platformConf.getDatabaseType();
    }

    protected BasicDataSource getDataSource() {
        return this._platformConf.getDataSource();
    }

    public String getCatalogPattern() {
        return this._platformConf.getCatalogPattern();
    }

    public String getSchemaPattern() {
        return this._platformConf.getSchemaPattern();
    }

    protected void setPlatformConfiguration(PlatformConfiguration platformConf) {
        this._platformConf = platformConf;
    }

    protected Platform getPlatform() throws BuildException {
        return this._platformConf.getPlatform();
    }

    @Override
    public boolean isRequiringModel() {
        return true;
    }
}

