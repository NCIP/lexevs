/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.tools.ant.BuildException
 *  org.apache.tools.ant.DirectoryScanner
 *  org.apache.tools.ant.Task
 *  org.apache.tools.ant.types.FileSet
 */
package org.apache.ddlutils.task;

import java.io.File;
import java.util.ArrayList;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.io.DataReader;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.task.ConvertingDatabaseCommand;
import org.apache.ddlutils.task.DatabaseTaskBase;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

public class WriteDataToDatabaseCommand
extends ConvertingDatabaseCommand {
    private File _singleDataFile = null;
    private ArrayList<FileSet> _fileSets = new ArrayList<FileSet>();
    private boolean _useExplicitIdentityValues;

    public void setUseExplicitIdentityValues(boolean useExplicitIdentityValues) {
        this._useExplicitIdentityValues = useExplicitIdentityValues;
    }

    public void addConfiguredFileset(FileSet fileset) {
        this._fileSets.add(fileset);
    }

    public void setDataFile(File dataFile) {
        this._singleDataFile = dataFile;
    }

    public void setBatchSize(int batchSize) {
        this.getDataIO().setBatchSize(new Integer(batchSize));
    }

    public void setUseBatchMode(boolean useBatchMode) {
        this.getDataIO().setUseBatchMode(useBatchMode);
    }

    public void setEnsureForeignKeyOrder(boolean ensureFKOrder) {
        this.getDataIO().setEnsureFKOrder(ensureFKOrder);
    }

    @Override
    public void execute(DatabaseTaskBase task, Database model) throws BuildException {
        if (this._singleDataFile != null && !this._fileSets.isEmpty()) {
            throw new BuildException("Please use either the datafile attribute or the sub fileset element, but not both");
        }
        Platform platform = this.getPlatform();
        DataReader dataReader = null;
        platform.setIdentityOverrideOn(this._useExplicitIdentityValues);
        try {
            try {
                dataReader = this.getDataIO().getConfiguredDataReader(platform, model);
                dataReader.getSink().start();
                if (this._singleDataFile != null) {
                    this.readSingleDataFile(task, dataReader, this._singleDataFile);
                } else {
                    for (FileSet fileSet : this._fileSets) {
                        File fileSetDir = fileSet.getDir(task.getProject());
                        DirectoryScanner scanner = fileSet.getDirectoryScanner(task.getProject());
                        String[] files = scanner.getIncludedFiles();
                        int idx = 0;
                        while (files != null && idx < files.length) {
                            this.readSingleDataFile(task, dataReader, new File(fileSetDir, files[idx]));
                            ++idx;
                        }
                    }
                }
            }
            catch (Exception ex) {
                this.handleException(ex, ex.getMessage());
                if (dataReader != null) {
                    dataReader.getSink().end();
                }
            }
        }
        finally {
            if (dataReader != null) {
                dataReader.getSink().end();
            }
        }
    }

    private void readSingleDataFile(Task task, DataReader reader, File dataFile) throws BuildException {
        if (!dataFile.exists()) {
            this._log.error((Object)("Could not find data file " + dataFile.getAbsolutePath()));
        } else if (!dataFile.isFile()) {
            this._log.error((Object)("Path " + dataFile.getAbsolutePath() + " does not denote a data file"));
        } else if (!dataFile.canRead()) {
            this._log.error((Object)("Could not read data file " + dataFile.getAbsolutePath()));
        } else {
            try {
                this.getDataIO().writeDataToDatabase(reader, dataFile.getAbsolutePath());
                this._log.info((Object)("Written data from file " + dataFile.getAbsolutePath() + " to database"));
            }
            catch (Exception ex) {
                this.handleException(ex, "Could not parse or write data file " + dataFile.getAbsolutePath());
            }
        }
    }
}

