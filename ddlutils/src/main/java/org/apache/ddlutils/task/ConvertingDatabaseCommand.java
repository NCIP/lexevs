/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.task;

import org.apache.ddlutils.io.DataConverterRegistration;
import org.apache.ddlutils.io.DatabaseDataIO;
import org.apache.ddlutils.task.DatabaseCommand;

public abstract class ConvertingDatabaseCommand
extends DatabaseCommand {
    private DatabaseDataIO _dataIO = new DatabaseDataIO();

    protected DatabaseDataIO getDataIO() {
        return this._dataIO;
    }

    public void addConfiguredConverter(DataConverterRegistration converterRegistration) {
        this._dataIO.registerConverter(converterRegistration);
    }
}

