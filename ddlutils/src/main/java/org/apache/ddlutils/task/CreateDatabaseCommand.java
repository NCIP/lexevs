/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.dbcp.BasicDataSource
 *  org.apache.tools.ant.BuildException
 */
package org.apache.ddlutils.task;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.task.DatabaseCommand;
import org.apache.ddlutils.task.DatabaseTaskBase;
import org.apache.ddlutils.task.Parameter;
import org.apache.tools.ant.BuildException;

public class CreateDatabaseCommand
extends DatabaseCommand {
    private ArrayList<Parameter> _parameters = new ArrayList<Parameter>();

    public void addConfiguredParameter(Parameter param) {
        this._parameters.add(param);
    }

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
            platform.createDatabase(dataSource.getDriverClassName(), dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword(), this.getFilteredParameters(platform.getName()));
            this._log.info((Object)"Created database");
        }
        catch (UnsupportedOperationException ex) {
            this._log.error((Object)("Database platform " + platform.getName() + " does not support database creation " + "via JDBC or there was an error while creating it."), (Throwable)ex);
        }
        catch (Exception ex) {
            this.handleException(ex, ex.getMessage());
        }
    }

    private Map<String,String> getFilteredParameters(String platformName) {
        LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();
        for (Parameter param : this._parameters) {
            if (!param.isForPlatform(platformName)) continue;
            parameters.put(param.getName(), param.getValue());
        }
        return parameters;
    }
}

