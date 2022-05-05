/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.logging.Log
 *  org.apache.commons.logging.LogFactory
 */
package org.apache.ddlutils.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ddlutils.DatabaseOperationException;

public abstract class JdbcSupport {
    private final Log _log = LogFactory.getLog(JdbcSupport.class);
    private DataSource _dataSource;
    private String _username;
    private String _password;
    private HashSet _openConnectionNames = new HashSet();

    public DataSource getDataSource() {
        return this._dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this._dataSource = dataSource;
    }

    public String getUsername() {
        return this._username;
    }

    public void setUsername(String username) {
        this._username = username;
    }

    public String getPassword() {
        return this._password;
    }

    public void setPassword(String password) {
        this._password = password;
    }

    public Connection borrowConnection() throws DatabaseOperationException {
        try {
            Connection connection = null;
            connection = this._username == null ? this.getDataSource().getConnection() : this.getDataSource().getConnection(this._username, this._password);
            if (this._log.isDebugEnabled()) {
                String connName = connection.toString();
                this._log.debug((Object)("Borrowed connection " + connName + " from data source"));
                this._openConnectionNames.add(connName);
            }
            return connection;
        }
        catch (SQLException ex) {
            throw new DatabaseOperationException("Could not get a connection from the datasource", ex);
        }
    }

    public void returnConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                if (this._log.isDebugEnabled()) {
                    String connName = connection.toString();
                    this._openConnectionNames.remove(connName);
                    StringBuffer logMsg = new StringBuffer();
                    logMsg.append("Returning connection ");
                    logMsg.append(connName);
                    logMsg.append(" to data source.\nRemaining connections:");
                    if (this._openConnectionNames.isEmpty()) {
                        logMsg.append(" None");
                    } else {
                        Iterator it = this._openConnectionNames.iterator();
                        while (it.hasNext()) {
                            logMsg.append("\n    ");
                            logMsg.append(it.next().toString());
                        }
                    }
                    this._log.debug((Object)logMsg.toString());
                }
                connection.close();
            }
        }
        catch (Exception e) {
            this._log.warn((Object)"Caught exception while returning connection to pool", (Throwable)e);
        }
    }

    public void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            }
            catch (Exception e) {
                this._log.debug((Object)"Ignoring exception that occurred while closing statement", (Throwable)e);
            }
        }
    }
}

