/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.tools.ant.BuildException
 */
package org.apache.ddlutils.task;

import java.util.ArrayList;
import java.util.StringTokenizer;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.task.DatabaseTaskBase;
import org.apache.ddlutils.task.WriteDataToDatabaseCommand;
import org.apache.ddlutils.task.WriteDataToFileCommand;
import org.apache.ddlutils.task.WriteDtdToFileCommand;
import org.apache.ddlutils.task.WriteSchemaSqlToFileCommand;
import org.apache.ddlutils.task.WriteSchemaToFileCommand;
import org.apache.tools.ant.BuildException;

public class DatabaseToDdlTask
extends DatabaseTaskBase {
    private String _tableTypes;
    private String _modelName = "unnamed";

    public void setTableTypes(String tableTypes) {
        this._tableTypes = tableTypes;
    }

    public void setModelName(String modelName) {
        this._modelName = modelName;
    }

    public void addWriteDtdToFile(WriteDtdToFileCommand command) {
        this.addCommand(command);
    }

    public void addWriteSchemaToFile(WriteSchemaToFileCommand command) {
        this.addCommand(command);
    }

    public void addWriteSchemaSqlToFile(WriteSchemaSqlToFileCommand command) {
        this.addCommand(command);
    }

    public void addWriteDataToDatabase(WriteDataToDatabaseCommand command) {
        this.addCommand(command);
    }

    public void addWriteDataToFile(WriteDataToFileCommand command) {
        this.addCommand(command);
    }

    private String[] getTableTypes() {
        if (this._tableTypes == null || this._tableTypes.length() == 0) {
            return new String[0];
        }
        StringTokenizer tokenizer = new StringTokenizer(this._tableTypes, ",");
        ArrayList<String> result = new ArrayList<String>();
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken().trim();
            if (token.length() <= 0) continue;
            result.add(token);
        }
        return result.toArray(new String[result.size()]);
    }

    @Override
    protected Database readModel() {
        if (this.getDataSource() == null) {
            throw new BuildException("No database specified.");
        }
        try {
            return this.getPlatform().readModelFromDatabase(this._modelName, this.getPlatformConfiguration().getCatalogPattern(), this.getPlatformConfiguration().getSchemaPattern(), this.getTableTypes());
        }
        catch (Exception ex) {
            throw new BuildException("Could not read the schema from the specified database: " + ex.getLocalizedMessage(), (Throwable)ex);
        }
    }
}

