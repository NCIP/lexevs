/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.tools.ant.BuildException
 */
package org.apache.ddlutils.task;

import java.io.File;
import java.io.FileOutputStream;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.task.ConvertingDatabaseCommand;
import org.apache.ddlutils.task.DatabaseTaskBase;
import org.apache.tools.ant.BuildException;

public class WriteDataToFileCommand
extends ConvertingDatabaseCommand {
    private File _outputFile;
    private String _encoding;
    private boolean _determineSchema;

    public void setOutputFile(File outputFile) {
        this._outputFile = outputFile;
    }

    public void setEncoding(String encoding) {
        this._encoding = encoding;
    }

    public void setDetermineSchema(boolean determineSchema) {
        this._determineSchema = determineSchema;
    }

    @Override
    public void execute(DatabaseTaskBase task, Database model) throws BuildException {
        try {
            this.getDataIO().setDetermineSchema(this._determineSchema);
            this.getDataIO().setSchemaPattern(task.getPlatformConfiguration().getSchemaPattern());
            this.getDataIO().writeDataToXML(this.getPlatform(), new FileOutputStream(this._outputFile), this._encoding);
            this._log.info((Object)("Written data XML to file" + this._outputFile.getAbsolutePath()));
        }
        catch (Exception ex) {
            this.handleException(ex, ex.getMessage());
        }
        catch (Throwable e) {
            e.printStackTrace();
            this.handleException((Exception) e, e.getMessage());
        }

    }
}

