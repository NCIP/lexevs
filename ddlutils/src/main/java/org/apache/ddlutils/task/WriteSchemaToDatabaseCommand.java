/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.tools.ant.BuildException
 */
package org.apache.ddlutils.task;

import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.platform.CreationParameters;
import org.apache.ddlutils.task.DatabaseCommandWithCreationParameters;
import org.apache.ddlutils.task.DatabaseTaskBase;
import org.apache.tools.ant.BuildException;

public class WriteSchemaToDatabaseCommand
extends DatabaseCommandWithCreationParameters {
    private boolean _alterDb = true;
    private boolean _doDrops = true;

    protected boolean isAlterDatabase() {
        return this._alterDb;
    }

    public void setAlterDatabase(boolean alterTheDb) {
        this._alterDb = alterTheDb;
    }

    protected boolean isDoDrops() {
        return this._doDrops;
    }

    public void setDoDrops(boolean doDrops) {
        this._doDrops = doDrops;
    }

    @Override
    public void execute(DatabaseTaskBase task, Database model) throws BuildException {
        if (this.getDataSource() == null) {
            throw new BuildException("No database specified.");
        }
        Platform platform = this.getPlatform();
        boolean isCaseSensitive = platform.isDelimitedIdentifierModeOn();
        CreationParameters params = this.getFilteredParameters(model, platform.getName(), isCaseSensitive);
        platform.setScriptModeOn(false);
        platform.setSqlCommentsOn(false);
        try {
            if (this.isAlterDatabase()) {
                if (this.getCatalogPattern() != null || this.getSchemaPattern() != null) {
                    platform.alterTables(this.getCatalogPattern(), this.getSchemaPattern(), null, model, params, true);
                } else {
                    platform.alterTables(model, params, true);
                }
            } else {
                platform.createTables(model, params, this._doDrops, true);
            }
            this._log.info((Object)"Written schema to database");
        }
        catch (Exception ex) {
            this.handleException(ex, ex.getMessage());
        }
    }
}

