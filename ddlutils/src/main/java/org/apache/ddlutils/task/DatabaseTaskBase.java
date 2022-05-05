/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.dbcp.BasicDataSource
 *  org.apache.commons.logging.Log
 *  org.apache.commons.logging.LogFactory
 *  org.apache.log4j.Level
 *  org.apache.log4j.LogManager
 *  org.apache.log4j.PropertyConfigurator
 *  org.apache.tools.ant.AntClassLoader
 *  org.apache.tools.ant.BuildException
 *  org.apache.tools.ant.Task
 */
package org.apache.ddlutils.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.task.Command;
import org.apache.ddlutils.task.DatabaseCommand;
import org.apache.ddlutils.task.PlatformConfiguration;
import org.apache.ddlutils.task.VerbosityLevel;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.PropertyConfigurator;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public abstract class DatabaseTaskBase
extends Task {
    protected Log _log;
    private PlatformConfiguration _platformConf = new PlatformConfiguration();
    private ArrayList _commands = new ArrayList();
    private boolean _simpleLogging = true;
    private VerbosityLevel _verbosity = null;

    public void setSimpleLogging(boolean simpleLogging) {
        this._simpleLogging = simpleLogging;
    }

    public void setVerbosity(VerbosityLevel level) {
        this._verbosity = level;
    }

    public String getDatabaseType() {
        return this._platformConf.getDatabaseType();
    }

    public void setDatabaseType(String type) {
        if (type != null && type.length() > 0) {
            this._platformConf.setDatabaseType(type);
        }
    }

    public BasicDataSource getDataSource() {
        return this._platformConf.getDataSource();
    }

    public void addConfiguredDatabase(BasicDataSource dataSource) {
        this._platformConf.setDataSource(dataSource);
    }

    public void setCatalogPattern(String catalogPattern) {
        if (catalogPattern != null && catalogPattern.length() > 0) {
            this._platformConf.setCatalogPattern(catalogPattern);
        }
    }

    public void setSchemaPattern(String schemaPattern) {
        if (schemaPattern != null && schemaPattern.length() > 0) {
            this._platformConf.setSchemaPattern(schemaPattern);
        }
    }

    public boolean isUseDelimitedSqlIdentifiers() {
        return this._platformConf.isUseDelimitedSqlIdentifiers();
    }

    public void setUseDelimitedSqlIdentifiers(boolean useDelimitedSqlIdentifiers) {
        this._platformConf.setUseDelimitedSqlIdentifiers(useDelimitedSqlIdentifiers);
    }

    public boolean isSortForeignKeys() {
        return this._platformConf.isSortForeignKeys();
    }

    public void setSortForeignKeys(boolean sortForeignKeys) {
        this._platformConf.setSortForeignKeys(sortForeignKeys);
    }

    public boolean isShutdownDatabase() {
        return this._platformConf.isShutdownDatabase();
    }

    public void setShutdownDatabase(boolean shutdownDatabase) {
        this._platformConf.setShutdownDatabase(shutdownDatabase);
    }

    protected void addCommand(Command command) {
        this._commands.add(command);
    }

    protected boolean hasCommands() {
        return !this._commands.isEmpty();
    }

    protected Iterator getCommands() {
        return this._commands.iterator();
    }

    protected PlatformConfiguration getPlatformConfiguration() {
        return this._platformConf;
    }

    protected Platform getPlatform() {
        return this._platformConf.getPlatform();
    }

    protected abstract Database readModel();

    private void initLogging() {
        Properties props = new Properties();
        String level = (this._verbosity == null ? Level.INFO.toString() : this._verbosity.getValue()).toUpperCase();
        props.setProperty("log4j.rootCategory", String.valueOf(level) + ",A");
        props.setProperty("log4j.appender.A", "org.apache.log4j.ConsoleAppender");
        props.setProperty("log4j.appender.A.layout", "org.apache.log4j.PatternLayout");
        props.setProperty("log4j.appender.A.layout.ConversionPattern", "%m%n");
        props.setProperty("log4j.logger.org.apache.commons", "WARN");
//        LogManager.resetConfiguration();
//        PropertyConfigurator.configure((Properties)props);
        this._log = LogFactory.getLog(((Object)((Object)this)).getClass());
    }

    protected void executeCommands(Database model) throws BuildException {
        Iterator it = this.getCommands();
        while (it.hasNext()) {
            Command cmd = (Command)it.next();
            if (cmd.isRequiringModel() && model == null) {
                throw new BuildException("No database model specified");
            }
            if (cmd instanceof DatabaseCommand) {
                ((DatabaseCommand)cmd).setPlatformConfiguration(this._platformConf);
            }
            cmd.execute(this, model);
        }
    }

    public void execute() throws BuildException {
        if (this._simpleLogging) {
            this.initLogging();
        }
        if (!this.hasCommands()) {
            this._log.info((Object)"No sub tasks specified, so there is nothing to do.");
            return;
        }
        ClassLoader sysClassLoader = Thread.currentThread().getContextClassLoader();
        AntClassLoader newClassLoader = new AntClassLoader(((Object)((Object)this)).getClass().getClassLoader(), true);
        Thread.currentThread().setContextClassLoader((ClassLoader)newClassLoader);
        try {
            this.executeCommands(this.readModel());
        }
        finally {
            if (this.getDataSource() != null && this.isShutdownDatabase()) {
                this.getPlatform().shutdownDatabase();
            }
            Thread.currentThread().setContextClassLoader(sysClassLoader);
        }
    }
}

