/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.tools.ant.BuildException
 */
package org.apache.ddlutils.task;

import java.io.File;
import java.io.FileWriter;
import org.apache.ddlutils.io.DatabaseIO;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.task.Command;
import org.apache.ddlutils.task.DatabaseTaskBase;
import org.apache.tools.ant.BuildException;

public class WriteSchemaToFileCommand
extends Command {
    private File _outputFile;

    public void setOutputFile(File outputFile) {
        this._outputFile = outputFile;
    }

    @Override
    public boolean isRequiringModel() {
        return true;
    }

    @Override
    public void execute(DatabaseTaskBase task, Database model) throws BuildException {
        if (this._outputFile == null) {
            throw new BuildException("No output file specified");
        }
        if (this._outputFile.exists() && !this._outputFile.canWrite()) {
            throw new BuildException("Cannot overwrite output file " + this._outputFile.getAbsolutePath());
        }
        try {
            FileWriter outputWriter = new FileWriter(this._outputFile);
            DatabaseIO dbIO = new DatabaseIO();
            dbIO.write(model, outputWriter);
            outputWriter.close();
            this._log.info((Object)("Written schema to " + this._outputFile.getAbsolutePath()));
        }
        catch (Exception ex) {
            this.handleException(ex, "Failed to write to output file " + this._outputFile.getAbsolutePath());
        }
    }
}

