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
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.task.DatabaseCommand;
import org.apache.ddlutils.task.DatabaseTaskBase;
import org.apache.tools.ant.BuildException;

public class DropDatabaseCommand
extends DatabaseCommand {
    @Override
    public boolean isRequiringModel() {
        return false;
    }

    @Override
    public void execute(DatabaseTaskBase task, Database model) throws BuildException {
        BasicDataSource dataSource = this.getDataSource();
        if (dataSource == null) {
            throw new BuildException("No database specified.");
        }
        Platform platform = this.getPlatform();
        try {
            platform.dropDatabase(dataSource.getDriverClassName(), dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword());
            this._log.info((Object)"Dropped database");
        }
        catch (UnsupportedOperationException ex) {
            this._log.error((Object)("Database platform " + platform.getName() + " does not support database dropping via JDBC"), (Throwable)ex);
        }
        catch (Exception ex) {
            this.handleException(ex, ex.getMessage());
        }
    }
}

