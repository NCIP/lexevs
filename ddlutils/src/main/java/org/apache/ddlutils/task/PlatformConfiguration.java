/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.dbcp.BasicDataSource
 *  org.apache.tools.ant.BuildException
 */
package org.apache.ddlutils.task;

import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.PlatformFactory;
import org.apache.ddlutils.PlatformUtils;
import org.apache.tools.ant.BuildException;

public class PlatformConfiguration {
    private String _databaseType;
    private BasicDataSource _dataSource;
    private boolean _useDelimitedSqlIdentifiers;
    private boolean _sortForeignKeys;
    private boolean _shutdownDatabase;
    private String _catalogPattern;
    private String _schemaPattern;

    public String getDatabaseType() {
        return this._databaseType;
    }

    public void setDatabaseType(String type) {
        this._databaseType = type;
    }

    public BasicDataSource getDataSource() {
        return this._dataSource;
    }

    public void setDataSource(BasicDataSource dataSource) {
        this._dataSource = dataSource;
    }

    public String getCatalogPattern() {
        return this._catalogPattern;
    }

    public void setCatalogPattern(String catalogPattern) {
        this._catalogPattern = catalogPattern;
    }

    public String getSchemaPattern() {
        return this._schemaPattern;
    }

    public void setSchemaPattern(String schemaPattern) {
        this._schemaPattern = schemaPattern;
    }

    public boolean isUseDelimitedSqlIdentifiers() {
        return this._useDelimitedSqlIdentifiers;
    }

    public void setUseDelimitedSqlIdentifiers(boolean useDelimitedSqlIdentifiers) {
        this._useDelimitedSqlIdentifiers = useDelimitedSqlIdentifiers;
    }

    public boolean isSortForeignKeys() {
        return this._sortForeignKeys;
    }

    public void setSortForeignKeys(boolean sortForeignKeys) {
        this._sortForeignKeys = sortForeignKeys;
    }

    public boolean isShutdownDatabase() {
        return this._shutdownDatabase;
    }

    public void setShutdownDatabase(boolean shutdownDatabase) {
        this._shutdownDatabase = shutdownDatabase;
    }

    public Platform getPlatform() throws BuildException {
        Platform platform = null;
        if (this._databaseType == null) {
            if (this._dataSource == null) {
                throw new BuildException("No database specified.");
            }
            if (this._databaseType == null) {
                this._databaseType = new PlatformUtils().determineDatabaseType(this._dataSource.getDriverClassName(), this._dataSource.getUrl());
            }
            if (this._databaseType == null) {
                this._databaseType = new PlatformUtils().determineDatabaseType((DataSource)this._dataSource);
            }
        }
        try {
            platform = PlatformFactory.createNewPlatformInstance(this._databaseType);
        }
        catch (Exception ex) {
            throw new BuildException("Database type " + this._databaseType + " is not supported.", (Throwable)ex);
        }
        if (platform == null) {
            throw new BuildException("Database type " + this._databaseType + " is not supported.");
        }
        platform.setDataSource((DataSource)this._dataSource);
        platform.setDelimitedIdentifierModeOn(this.isUseDelimitedSqlIdentifiers());
        platform.setForeignKeysSorted(this.isSortForeignKeys());
        return platform;
    }
}

