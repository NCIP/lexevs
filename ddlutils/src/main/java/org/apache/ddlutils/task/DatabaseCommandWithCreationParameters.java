/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.task;

import java.util.ArrayList;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.CreationParameters;
import org.apache.ddlutils.task.DatabaseCommand;
import org.apache.ddlutils.task.TableSpecificParameter;

public abstract class DatabaseCommandWithCreationParameters
extends DatabaseCommand {
    private ArrayList<TableSpecificParameter> _parameters = new ArrayList<TableSpecificParameter>();

    public void addConfiguredParameter(TableSpecificParameter param) {
        this._parameters.add(param);
    }

    protected CreationParameters getFilteredParameters(Database model, String platformName, boolean isCaseSensitive) {
        CreationParameters parameters = new CreationParameters();
        for (TableSpecificParameter param : this._parameters) {
            if (!param.isForPlatform(platformName)) continue;
            int idx = 0;
            while (idx < model.getTableCount()) {
                Table table = model.getTable(idx);
                if (param.isForTable(table, isCaseSensitive)) {
                    parameters.addParameter(table, param.getName(), param.getValue());
                }
                ++idx;
            }
        }
        return parameters;
    }
}

