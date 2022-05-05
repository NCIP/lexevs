/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.tools.ant.BuildException
 */
package org.apache.ddlutils.task;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.platform.CreationParameters;
import org.apache.ddlutils.task.DatabaseCommandWithCreationParameters;
import org.apache.ddlutils.task.DatabaseTaskBase;
import org.apache.tools.ant.BuildException;

public class WriteSchemaSqlToFileCommand
extends DatabaseCommandWithCreationParameters {
    private File _outputFile;
    private boolean _alterDb = true;
    private boolean _doDrops = true;

    public void setOutputFile(File outputFile) {
        this._outputFile = outputFile;
    }

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
        if (this._outputFile == null) {
            throw new BuildException("No output file specified");
        }
        if (this._outputFile.exists() && !this._outputFile.canWrite()) {
            throw new BuildException("Cannot overwrite output file " + this._outputFile.getAbsolutePath());
        }
        Platform platform = this.getPlatform();
        boolean isCaseSensitive = platform.isDelimitedIdentifierModeOn();
        CreationParameters params = this.getFilteredParameters(model, platform.getName(), isCaseSensitive);
        try {
            FileWriter writer = new FileWriter(this._outputFile);
            platform.setScriptModeOn(true);
            if (platform.getPlatformInfo().isSqlCommentsSupported()) {
                platform.setSqlCommentsOn(true);
            }
            platform.getSqlBuilder().setWriter(writer);
            boolean shouldAlter = this.isAlterDatabase();
            if (shouldAlter) {
                if (this.getDataSource() == null) {
                    shouldAlter = false;
                    this._log.warn((Object)"Cannot alter the database because no database connection was specified. SQL for database creation will be generated instead.");
                } else {
                    try {
                        Connection connection = this.getDataSource().getConnection();
                        connection.close();
                    }
                    catch (SQLException ex) {
                        shouldAlter = false;
                        this._log.warn((Object)"Could not establish a connection to the specified database, so SQL for database creation will be generated instead.", (Throwable)ex);
                    }
                }
            }
            if (shouldAlter) {
                Database currentModel = this.getCatalogPattern() != null || this.getSchemaPattern() != null ? platform.readModelFromDatabase("unnamed", this.getCatalogPattern(), this.getSchemaPattern(), null) : platform.readModelFromDatabase("unnamed");
                platform.getSqlBuilder().alterDatabase(currentModel, model, params);
            } else {
                platform.getSqlBuilder().createTables(model, params, this._doDrops);
            }
            writer.close();
            this._log.info((Object)("Written schema SQL to " + this._outputFile.getAbsolutePath()));
        }
        catch (Exception ex) {
            this.handleException(ex, ex.getMessage());
        }
    }
}

