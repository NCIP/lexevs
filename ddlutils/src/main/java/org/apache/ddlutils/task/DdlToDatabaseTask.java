/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.tools.ant.BuildException
 *  org.apache.tools.ant.DirectoryScanner
 *  org.apache.tools.ant.types.FileSet
 */
package org.apache.ddlutils.task;

import java.io.File;
import java.util.ArrayList;
import org.apache.ddlutils.io.DatabaseIO;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.task.CreateDatabaseCommand;
import org.apache.ddlutils.task.DatabaseTaskBase;
import org.apache.ddlutils.task.DropDatabaseCommand;
import org.apache.ddlutils.task.WriteDataToDatabaseCommand;
import org.apache.ddlutils.task.WriteDataToFileCommand;
import org.apache.ddlutils.task.WriteDtdToFileCommand;
import org.apache.ddlutils.task.WriteSchemaSqlToFileCommand;
import org.apache.ddlutils.task.WriteSchemaToDatabaseCommand;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.FileSet;

public class DdlToDatabaseTask
extends DatabaseTaskBase {
    private File _singleSchemaFile = null;
    private ArrayList<FileSet> _fileSets = new ArrayList<FileSet>();
    private boolean _useInternalDtd = true;
    private boolean _validateXml = false;

    public void setUseInternalDtd(boolean useInternalDtd) {
        this._useInternalDtd = useInternalDtd;
    }

    public void setValidateXml(boolean validateXml) {
        this._validateXml = validateXml;
    }

    public void addConfiguredFileset(FileSet fileset) {
        this._fileSets.add(fileset);
    }

    public void setSchemaFile(File schemaFile) {
        this._singleSchemaFile = schemaFile;
    }

    public void addCreateDatabase(CreateDatabaseCommand command) {
        this.addCommand(command);
    }

    public void addDropDatabase(DropDatabaseCommand command) {
        this.addCommand(command);
    }

    public void addWriteDtdToFile(WriteDtdToFileCommand command) {
        this.addCommand(command);
    }

    public void addWriteSchemaToDatabase(WriteSchemaToDatabaseCommand command) {
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

    @Override
    protected Database readModel() {
        DatabaseIO reader = new DatabaseIO();
        Database model = null;
        reader.setValidateXml(this._validateXml);
        reader.setUseInternalDtd(this._useInternalDtd);
        if (this._singleSchemaFile != null && !this._fileSets.isEmpty()) {
            throw new BuildException("Please use either the schemafile attribute or the sub fileset element, but not both");
        }
        if (this._singleSchemaFile != null) {
            model = this.readSingleSchemaFile(reader, this._singleSchemaFile);
        } else {
            for (FileSet fileSet : this._fileSets) {
                File fileSetDir = fileSet.getDir(this.getProject());
                DirectoryScanner scanner = fileSet.getDirectoryScanner(this.getProject());
                String[] files = scanner.getIncludedFiles();
                int idx = 0;
                while (files != null && idx < files.length) {
                    Database curModel = this.readSingleSchemaFile(reader, new File(fileSetDir, files[idx]));
                    if (model == null) {
                        model = curModel;
                    } else if (curModel != null) {
                        try {
                            model.mergeWith(curModel);
                        }
                        catch (IllegalArgumentException ex) {
                            throw new BuildException("Could not merge with schema from file " + files[idx] + ": " + ex.getLocalizedMessage(), (Throwable)ex);
                        }
                    }
                    ++idx;
                }
            }
        }
        return model;
    }

    private Database readSingleSchemaFile(DatabaseIO reader, File schemaFile) {
        Database model = null;
        if (!schemaFile.isFile()) {
            this._log.error((Object)("Path " + schemaFile.getAbsolutePath() + " does not denote a file"));
        } else if (!schemaFile.canRead()) {
            this._log.error((Object)("Could not read schema file " + schemaFile.getAbsolutePath()));
        } else {
            try {
                model = reader.read(schemaFile);
                this._log.info((Object)("Read schema file " + schemaFile.getAbsolutePath()));
            }
            catch (Exception ex) {
                throw new BuildException("Could not read schema file " + schemaFile.getAbsolutePath() + ": " + ex.getLocalizedMessage(), (Throwable)ex);
            }
        }
        return model;
    }
}

