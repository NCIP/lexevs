/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.LexGrid.util.sql.sqlReconnect;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class in combination with the WrappedPreparedStatement give you an
 * automatically reconnecting sql connection. If a failure occurs (due to
 * timeout) when you execute a prepared statement, it will create a new
 * connection, a new prepared statement, and reexecute the query. </pre>
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 */
@Deprecated
public class WrappedConnection implements Connection {
    protected Connection connection_;
    private String userName_;
    private String password_;
    private String driver_;
    private String server_;
    private boolean useUTF8_ = false;

    private static ClassLoader driverClassLoader_;

    private int maxFailCount_ = 3;

    public final static Logger logger = LogManager
            .getLogger("org.LexGrid.util.sql.sqlReconnect.WrappedConnection");

    public WrappedConnection(String userName, String password, String driver, String server)
            throws ClassNotFoundException, SQLException {
        logger.debug("Creating a new reconnectable SQL connection to " + server);
        userName_ = userName;
        password_ = password;
        driver_ = driver;
        server_ = server;
        useUTF8_ = false;

        if (userName_ == null) {
            userName_ = "";
        }
        if (password_ == null) {
            password_ = "";
        }
        connect();
    }

    public WrappedConnection(String userName, String password, String driver, String server, boolean useUTF8)
            throws ClassNotFoundException, SQLException {
        logger.debug("Creating a new reconnectable SQL connection to " + server);
        userName_ = userName;
        password_ = password;
        driver_ = driver;
        server_ = server;
        useUTF8_ = useUTF8;

        if (userName_ == null) {
            userName_ = "";
        }
        if (password_ == null) {
            password_ = "";
        }
        connect();
    }

    public void setSchema(String schema) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public String getSchema() throws SQLException {
        throw new UnsupportedOperationException();
    }

    public void abort(Executor executor) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public int getNetworkTimeout() throws SQLException {
        throw new UnsupportedOperationException();
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        logger.debug("Creating reconnectable prepared statement: \"" + sql + "\"");
        return new WrappedPreparedStatement(this, sql);
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
            throws SQLException {
        logger.debug("Creating reconnectable prepared statement: \"" + sql + "\"");
        return new WrappedPreparedStatement(this, sql, resultSetType, resultSetConcurrency);
    }

    public int getHoldability() throws SQLException {
        return connection_.getHoldability();
    }

    public int getTransactionIsolation() throws SQLException {
        return connection_.getTransactionIsolation();
    }

    public void clearWarnings() throws SQLException {
        connection_.clearWarnings();
    }

    public void close() throws SQLException {
        connection_.close();
    }

    public void commit() throws SQLException {
        connection_.commit();
    }

    public void rollback() throws SQLException {
        connection_.rollback();
    }

    public boolean getAutoCommit() throws SQLException {
        return connection_.getAutoCommit();
    }

    public boolean isClosed() throws SQLException {
        return connection_.isClosed();
    }

    public boolean isReadOnly() throws SQLException {
        return connection_.isReadOnly();
    }

    public String getCatalog() throws SQLException {
        return connection_.getCatalog();
    }

    public DatabaseMetaData getMetaData() throws SQLException {
        return connection_.getMetaData();
    }

    public SQLWarning getWarnings() throws SQLException {
        return connection_.getWarnings();
    }

    public void rollback(Savepoint savepoint) throws SQLException {
        connection_.rollback(savepoint);
    }

    private Integer holdability_;

    public void setHoldability(int holdability) throws SQLException {
        connection_.setHoldability(holdability);
        holdability_ = new Integer(holdability);
    }

    private Integer transactionIsoloation_;

    public void setTransactionIsolation(int level) throws SQLException {
        connection_.setTransactionIsolation(level);
        transactionIsoloation_ = new Integer(level);
    }

    private Boolean autoCommit_;

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        connection_.setAutoCommit(autoCommit);
        autoCommit_ = new Boolean(autoCommit);
    }

    private Boolean readOnly_;

    public void setReadOnly(boolean readOnly) throws SQLException {
        connection_.setReadOnly(readOnly);
        readOnly_ = new Boolean(readOnly);
    }

    private String catalog_;

    public void setCatalog(String catalog) throws SQLException {
        connection_.setCatalog(catalog);
        catalog_ = catalog;
    }

    private Map typeMap_;

    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        connection_.setTypeMap(map);
        typeMap_ = map;
    }

    private void setAllParameters() throws SQLException {
        logger.debug("Resetting all connection parameters");
        if (holdability_ != null) {
            connection_.setHoldability(holdability_.intValue());
        }

        if (transactionIsoloation_ != null) {
            connection_.setTransactionIsolation(transactionIsoloation_.intValue());
        }

        if (autoCommit_ != null) {
            connection_.setAutoCommit(autoCommit_.booleanValue());
        }

        if (readOnly_ != null) {
            connection_.setReadOnly(readOnly_.booleanValue());
        }

        if (catalog_ != null) {
            connection_.setCatalog(catalog_);
        }

        if (typeMap_ != null) {
            connection_.setTypeMap(typeMap_);
        }
    }

    private void connect() throws ClassNotFoundException, SQLException {
        try {
            // if a driver class loader has been provided, use it to find the
            // the drivers.
            if (driverClassLoader_ != null) {
                try {
                    driverClassLoader_.loadClass(driver_).newInstance();
                } catch (InstantiationException e) {
                    logger.error("Unexpected error trying to load the driver " + driver_
                            + " with a custom class loader.", e);
                    throw new SQLException("Unexpected error - " + e.toString());
                } catch (IllegalAccessException e) {
                    logger.error("Unexpected error trying to load the driver " + driver_
                            + " with a custom class loader.", e);
                    throw new SQLException("Unexpected error - " + e.toString());
                }
            } else {
                Class.forName(driver_);
            }
        } catch (ClassNotFoundException e) {
            logger.error("The driver for your sql connection was not found.  I tried to load " + driver_);
            throw e;
        }
        DriverManager.setLoginTimeout(5);
        Properties props = new Properties();
        props.setProperty("user", userName_);
        props.setProperty("password", password_);
        if (useUTF8_) {
            setUTFCharsetForDB(props, server_);
        }

        if (driverClassLoader_ != null) {
            try {
                // have to create the connectionHelper class with the provided
                // class loader, so that the
                // driver manager works properly. It only works if it is called
                // from a class that was
                // loaded by the same class loader as the class loader that
                // found and registered the drivers.
                connection_ = ((ConnectionHelperIF) driverClassLoader_.loadClass(
                        "org.LexGrid.util.sql.sqlReconnect.ConnectionHelper").newInstance()).getConnection(server_,
                        props);
            } catch (InstantiationException e) {
                logger.error("Unexpected error trying to load the driver " + driver_ + " with a custom class loader.",
                        e);
                throw new SQLException("Unexpected error - " + e.toString());
            } catch (IllegalAccessException e) {
                logger.error("Unexpected error trying to load the driver " + driver_ + " with a custom class loader.",
                        e);
                throw new SQLException("Unexpected error - " + e.toString());
            }
        } else {
            connection_ = DriverManager.getConnection(server_, props);
        }
    }

    protected void reconnect() throws SQLException {
        logger.debug("Reconnect called on SQL connection");
        int failCount = 0;
        while (true) {
            try {
                // try to clean up, but don't fail if we can't...
                if (connection_ != null) {
                    connection_.close();
                    connection_ = null;
                }
            } catch (SQLException e1) {
            }

            try {
                Properties props = new Properties();
                props.setProperty("user", userName_);
                props.setProperty("password", password_);
                if (useUTF8_) {
                    setUTFCharsetForDB(props, server_);
                }
                if (driverClassLoader_ != null) {
                    try {
                        // have to create the connectionHelper class with the
                        // provided class loader, so that the
                        // driver manager works properly. It only works if it is
                        // called from a class that was
                        // loaded by the same class loader as the class loader
                        // that found and registered the drivers.
                        connection_ = ((ConnectionHelperIF) driverClassLoader_.loadClass(
                                "org.LexGrid.util.sql.sqlReconnect.ConnectionHelper").newInstance()).getConnection(
                                server_, props);
                    } catch (InstantiationException e) {
                        throw new SQLException("Unexpected error - " + e.toString());
                    } catch (IllegalAccessException e) {
                        throw new SQLException("Unexpected error - " + e.toString());
                    } catch (ClassNotFoundException e) {
                        throw new SQLException("Unexpected error - " + e.toString());
                    }
                } else {
                    connection_ = DriverManager.getConnection(server_, props);
                }
                setAllParameters();
                break;

            } catch (SQLException e) {
                logger.warn("Reconnect failed on attempt " + failCount);
                failCount++;
                if (failCount > maxFailCount_) {
                    throw e;
                }
            }
        }
    }

    private static void setUTFCharsetForDB(Properties props, String URL) {
        String tempURL = URL.toLowerCase();
        // access and postgres use this flag
        if (tempURL.indexOf("odbc") != -1 || tempURL.indexOf("postgresql") != -1) {
            props.setProperty("charSet", "utf-8");
        }
        // mysql uses this
        else if (tempURL.indexOf("mysql") != -1) {
            props.setProperty("characterEncoding", "UTF-8");
            props.setProperty("useUnicode", "true");
        } else {
            props.setProperty("charSet", "utf-8");
        }
    }

    public Savepoint setSavepoint() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setSavepoint not yet implemented.");
    }

    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method releaseSavepoint not yet implemented.");
    }

    public Statement createStatement() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method createStatement not yet implemented.");
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method createStatement not yet implemented.");
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method createStatement not yet implemented.");
    }

    public Map getTypeMap() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getTypeMap not yet implemented.");
    }

    public String nativeSQL(String sql) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method nativeSQL not yet implemented.");
    }

    public CallableStatement prepareCall(String sql) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method prepareCall not yet implemented.");
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method prepareCall not yet implemented.");
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
            int resultSetHoldability) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method prepareCall not yet implemented.");
    }

    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method prepareStatement not yet implemented.");
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
            int resultSetHoldability) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method prepareStatement not yet implemented.");
    }

    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method prepareStatement not yet implemented.");
    }

    public Savepoint setSavepoint(String name) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setSavepoint not yet implemented.");
    }

    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method prepareStatement not yet implemented.");
    }

    public static ClassLoader getDriverClassLoader() {
        return driverClassLoader_;
    }

    public static void setDriverClassLoader(ClassLoader driverClassLoader) {
        WrappedConnection.driverClassLoader_ = driverClassLoader;
    }

    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    public Blob createBlob() throws SQLException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    public Clob createClob() throws SQLException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    public NClob createNClob() throws SQLException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    public SQLXML createSQLXML() throws SQLException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    public Properties getClientInfo() throws SQLException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    public String getClientInfo(String name) throws SQLException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    public boolean isValid(int timeout) throws SQLException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

}