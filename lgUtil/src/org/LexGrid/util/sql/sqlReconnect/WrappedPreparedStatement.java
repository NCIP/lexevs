
package org.LexGrid.util.sql.sqlReconnect;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.log4j.Logger;

/**
 * An automatically reconnecting prepared statement. See description in the
 * WrappedConnection class.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 */
@Deprecated
public class WrappedPreparedStatement implements PreparedStatement {
    private PreparedStatement statement_;
    private WrappedConnection wrappedConnection_;

    private Hashtable setVariables_;
    private String sql_;
    private Integer fetchDirection_, fetchSize_, maxFieldSize_, maxRows_, queryTimeout_;

    private Integer resultSetType_, resultSetConcurrency_;

    public final static org.apache.log4j.Logger logger = Logger
            .getLogger("org.LexGrid.util.sql.sqlReconnect.WrappedPreparedStatement");
    
    public WrappedPreparedStatement(WrappedConnection connection, String sql) throws SQLException {
        sql_ = sql;
        setVariables_ = new Hashtable();
        wrappedConnection_ = connection;
        statement_ = wrappedConnection_.connection_.prepareStatement(sql_);
    }

    public WrappedPreparedStatement(WrappedConnection connection, String sql, int resultSetType,
            int resultSetConcurrency) throws SQLException {
        sql_ = sql;
        resultSetType_ = new Integer(resultSetType);
        resultSetConcurrency_ = new Integer(resultSetConcurrency);
        setVariables_ = new Hashtable();
        wrappedConnection_ = connection;
        statement_ = wrappedConnection_.connection_.prepareStatement(sql_, resultSetType, resultSetConcurrency);
    }

    public void closeOnCompletion() throws SQLException {
        throw new UnsupportedOperationException();
    }

    public boolean isCloseOnCompletion() throws SQLException {
        throw new UnsupportedOperationException();
    }

    public void setString(int parameterIndex, String x) throws SQLException {
        QueryParameter temp = new QueryParameter(WrapperConstants.STRING, x);
        setType(parameterIndex, temp);
        setVariables_.put(new Integer(parameterIndex), temp);
    }

    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        QueryParameter temp = new QueryParameter(WrapperConstants.BOOLEAN, new Boolean(x));
        setType(parameterIndex, temp);
        setVariables_.put(new Integer(parameterIndex), temp);
    }

    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        QueryParameter temp = new QueryParameter(WrapperConstants.NULL, new Integer(sqlType));
        setType(parameterIndex, temp);
        setVariables_.put(new Integer(parameterIndex), temp);
    }

    public void setTime(int parameterIndex, Time x) throws SQLException {
        QueryParameter temp = new QueryParameter(WrapperConstants.TIME, x);
        setType(parameterIndex, temp);
        setVariables_.put(new Integer(parameterIndex), temp);
    }

    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        QueryParameter temp = new QueryParameter(WrapperConstants.TIMESTAMP, x);
        setType(parameterIndex, temp);
        setVariables_.put(new Integer(parameterIndex), temp);
    }

    public void setByte(int parameterIndex, byte x) throws SQLException {
        QueryParameter temp = new QueryParameter(WrapperConstants.BYTE, new Byte(x));
        setType(parameterIndex, temp);
        setVariables_.put(new Integer(parameterIndex), temp);
    }

    public void setDouble(int parameterIndex, double x) throws SQLException {
        QueryParameter temp = new QueryParameter(WrapperConstants.DOUBLE, new Double(x));
        setType(parameterIndex, temp);
        setVariables_.put(new Integer(parameterIndex), temp);
    }

    public void setFloat(int parameterIndex, float x) throws SQLException {
        QueryParameter temp = new QueryParameter(WrapperConstants.FLOAT, new Float(x));
        setType(parameterIndex, temp);
        setVariables_.put(new Integer(parameterIndex), temp);
    }

    public void setInt(int parameterIndex, int x) throws SQLException {
        QueryParameter temp = new QueryParameter(WrapperConstants.INT, new Integer(x));
        setType(parameterIndex, temp);
        setVariables_.put(new Integer(parameterIndex), temp);
    }

    public void setLong(int parameterIndex, long x) throws SQLException {
        QueryParameter temp = new QueryParameter(WrapperConstants.LONG, new Long(x));
        setType(parameterIndex, temp);
        setVariables_.put(new Integer(parameterIndex), temp);
    }

    public void setShort(int parameterIndex, short x) throws SQLException {
        QueryParameter temp = new QueryParameter(WrapperConstants.SHORT, new Short(x));
        setType(parameterIndex, temp);
        setVariables_.put(new Integer(parameterIndex), temp);
    }

    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        Byte[] temp1 = new Byte[x.length];
        for (int i = 0; i < temp1.length; i++) {
            temp1[i] = new Byte(x[i]);
        }
        QueryParameter temp = new QueryParameter(WrapperConstants.BYTES, temp1);
        setType(parameterIndex, temp);
        setVariables_.put(new Integer(parameterIndex), temp);
    }

    public void setObject(int parameterIndex, Object x) throws SQLException {
        QueryParameter temp = new QueryParameter(WrapperConstants.OBJECT, x);
        setType(parameterIndex, temp);
        setVariables_.put(new Integer(parameterIndex), temp);
    }

    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        QueryParameter temp = new QueryParameter(WrapperConstants.OBJECT, x, targetSqlType);
        setType(parameterIndex, temp);
        setVariables_.put(new Integer(parameterIndex), temp);
    }

    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        QueryParameter temp = new QueryParameter(WrapperConstants.BIGDECIMAL, x);
        setType(parameterIndex, temp);
        setVariables_.put(new Integer(parameterIndex), temp);
    }

    public void setURL(int parameterIndex, URL x) throws SQLException {
        QueryParameter temp = new QueryParameter(WrapperConstants.URL, x);
        setType(parameterIndex, temp);
        setVariables_.put(new Integer(parameterIndex), temp);
    }

    public void setArray(int parameterIndex, Array x) throws SQLException {
        QueryParameter temp = new QueryParameter(WrapperConstants.ARRAY, x);
        setType(parameterIndex, temp);
        setVariables_.put(new Integer(parameterIndex), temp);
    }

    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        QueryParameter temp = new QueryParameter(WrapperConstants.BLOB, x);
        setType(parameterIndex, temp);
        setVariables_.put(new Integer(parameterIndex), temp);
    }

    public void setClob(int parameterIndex, Clob x) throws SQLException {
        QueryParameter temp = new QueryParameter(WrapperConstants.CLOB, x);
        setType(parameterIndex, temp);
        setVariables_.put(new Integer(parameterIndex), temp);
    }

    public void setDate(int parameterIndex, Date x) throws SQLException {
        QueryParameter temp = new QueryParameter(WrapperConstants.DATE, x);
        setType(parameterIndex, temp);
        setVariables_.put(new Integer(parameterIndex), temp);
    }

    public void setRef(int parameterIndex, Ref x) throws SQLException {
        QueryParameter temp = new QueryParameter(WrapperConstants.REF, x);
        setType(parameterIndex, temp);
        setVariables_.put(new Integer(parameterIndex), temp);
    }

    private void setType(int parameterIndex, QueryParameter value) throws SQLException {
        switch (value.type) {
        case WrapperConstants.STRING: {
            statement_.setString(parameterIndex, (String) value.value);
            break;
        }
        case WrapperConstants.BOOLEAN: {
            statement_.setBoolean(parameterIndex, ((Boolean) value.value).booleanValue());
            break;
        }
        case WrapperConstants.NULL: {
            statement_.setNull(parameterIndex, ((Integer) value.value).intValue());
            break;
        }
        case WrapperConstants.TIME: {
            statement_.setTime(parameterIndex, ((Time) value.value));
            break;
        }

        case WrapperConstants.TIMESTAMP: {
            statement_.setTimestamp(parameterIndex, ((Timestamp) value.value));
            break;
        }
        case WrapperConstants.BYTE: {
            statement_.setByte(parameterIndex, ((Byte) value.value).byteValue());
            break;
        }
        case WrapperConstants.DOUBLE: {
            statement_.setDouble(parameterIndex, ((Double) value.value).doubleValue());
            break;
        }
        case WrapperConstants.FLOAT: {
            statement_.setFloat(parameterIndex, ((Float) value.value).floatValue());
            break;
        }
        case WrapperConstants.INT: {
            statement_.setInt(parameterIndex, ((Integer) value.value).intValue());
            break;
        }
        case WrapperConstants.LONG: {
            statement_.setLong(parameterIndex, ((Long) value.value).longValue());
            break;
        }
        case WrapperConstants.SHORT: {
            statement_.setShort(parameterIndex, ((Short) value.value).shortValue());
            break;
        }
        case WrapperConstants.BYTES: {
            Byte[] temp = ((Byte[]) value.value);
            byte[] temp1 = new byte[temp.length];
            for (int i = 0; i < temp1.length; i++) {
                temp1[i] = temp[i].byteValue();
            }
            statement_.setBytes(parameterIndex, temp1);
            break;
        }
        case WrapperConstants.OBJECT: {
            if (value.targetType != Integer.MIN_VALUE) {
                statement_.setObject(parameterIndex, value.value, value.targetType);
            } else {
                statement_.setObject(parameterIndex, value.value);
            }
            break;
        }
        case WrapperConstants.BIGDECIMAL: {
            statement_.setBigDecimal(parameterIndex, (BigDecimal) value.value);
            break;
        }
        case WrapperConstants.URL: {
            statement_.setURL(parameterIndex, (URL) value.value);
            break;
        }
        case WrapperConstants.ARRAY: {
            statement_.setArray(parameterIndex, (Array) value.value);
            break;
        }
        case WrapperConstants.BLOB: {
            statement_.setBlob(parameterIndex, (Blob) value.value);
            break;
        }
        case WrapperConstants.CLOB: {
            statement_.setClob(parameterIndex, (Clob) value.value);
            break;
        }
        case WrapperConstants.DATE: {
            statement_.setDate(parameterIndex, (Date) value.value);
            break;
        }
        case WrapperConstants.REF: {
            statement_.setRef(parameterIndex, (Ref) value.value);
            break;
        }
        default: {
            throw new SQLException("Unknown object type passed through WrappedPreparedStatment");

        }
        }
    }

    public void close() throws SQLException {
        statement_.close();
    }

    public void setFetchDirection(int direction) throws SQLException {
        statement_.setFetchDirection(direction);
        fetchDirection_ = new Integer(direction);
    }

    public void setFetchSize(int rows) throws SQLException {
        statement_.setFetchSize(rows);
        fetchSize_ = new Integer(rows);
    }

    public void setMaxFieldSize(int max) throws SQLException {
        statement_.setMaxFieldSize(max);
        maxFieldSize_ = new Integer(max);

    }

    public void setMaxRows(int max) throws SQLException {
        statement_.setMaxRows(max);
        maxRows_ = new Integer(max);

    }

    public void setQueryTimeout(int seconds) throws SQLException {
        statement_.setQueryTimeout(seconds);
        queryTimeout_ = new Integer(seconds);
    }

    Boolean escapeProcessing_;

    public void setEscapeProcessing(boolean enable) throws SQLException {
        statement_.setEscapeProcessing(enable);
        escapeProcessing_ = new Boolean(enable);

    }

    public Connection getConnection() throws SQLException {
        return (Connection) wrappedConnection_;
    }

    public ResultSet getGeneratedKeys() throws SQLException {
        return statement_.getGeneratedKeys();
    }

    public ResultSet getResultSet() throws SQLException {
        return statement_.getResultSet();
    }

    public SQLWarning getWarnings() throws SQLException {
        return statement_.getWarnings();
    }

    public int getResultSetConcurrency() throws SQLException {
        return statement_.getResultSetConcurrency();
    }

    public int getResultSetHoldability() throws SQLException {
        return statement_.getResultSetHoldability();
    }

    public int getResultSetType() throws SQLException {
        return statement_.getResultSetType();
    }

    public int getFetchDirection() throws SQLException {
        return statement_.getFetchDirection();
    }

    public int getFetchSize() throws SQLException {
        return statement_.getFetchSize();
    }

    public int getMaxFieldSize() throws SQLException {
        return statement_.getMaxFieldSize();
    }

    public int getMaxRows() throws SQLException {
        return statement_.getMaxRows();
    }

    public int getQueryTimeout() throws SQLException {
        return statement_.getQueryTimeout();
    }

    public int getUpdateCount() throws SQLException {
        return statement_.getUpdateCount();
    }

    public void cancel() throws SQLException {
        statement_.cancel();
    }

    public void clearBatch() throws SQLException {
        statement_.clearBatch();
    }

    public void clearWarnings() throws SQLException {
        statement_.clearWarnings();
    }

    public boolean getMoreResults() throws SQLException {
        return statement_.getMoreResults();
    }

    public void clearParameters() throws SQLException {
        setVariables_.clear();
        statement_.clearParameters();
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        return statement_.getMetaData();
    }

    public boolean getMoreResults(int current) throws SQLException {
        return statement_.getMoreResults(current);
    }

    private void setAllParameters() throws SQLException {
        logger.debug("Resetting all prepared statement parameters");
        if (fetchDirection_ != null) {
            statement_.setFetchDirection(fetchDirection_.intValue());
        }
        if (fetchSize_ != null) {
            statement_.setFetchSize(fetchSize_.intValue());
        }
        if (maxFieldSize_ != null) {
            statement_.setMaxFieldSize(maxFieldSize_.intValue());
        }
        if (maxRows_ != null) {
            statement_.setMaxRows(maxRows_.intValue());
        }
        if (queryTimeout_ != null) {
            statement_.setQueryTimeout(queryTimeout_.intValue());
        }
        if (escapeProcessing_ != null) {
            statement_.setEscapeProcessing(escapeProcessing_.booleanValue());
        }
    }

    private void setAllVariables() throws SQLException {
        logger.debug("Resetting all prepared statement variable values");
        Enumeration enumerator = setVariables_.keys();
        while (enumerator.hasMoreElements()) {
            Integer index = (Integer) enumerator.nextElement();
            setType(index.intValue(), (QueryParameter) setVariables_.get(index));
        }
    }

    private void rebuildStatement() throws SQLException {
        logger.debug("recreating the prepared statement");
        if (resultSetConcurrency_ != null && resultSetType_ != null) {
            statement_ = wrappedConnection_.connection_.prepareStatement(sql_, resultSetType_.intValue(),
                    resultSetConcurrency_.intValue());
        } else {
            statement_ = wrappedConnection_.connection_.prepareStatement(sql_);
        }
    }

    private void rebuildAll() throws SQLException {
        boolean recreatedConnection = false;
        boolean isClosed = false;

        try {
            isClosed = wrappedConnection_.isClosed();
        } catch (SQLException e) {
            isClosed = true;
        }
        if (isClosed) {
            wrappedConnection_.reconnect();
            recreatedConnection = true;
        }

        try {
            // clean up resources...
            statement_.close();
            statement_ = null;
        } catch (SQLException e1) {
        }

        // try the query again.

        try {
            rebuildStatement();
        } catch (SQLException e) {
            // if we couldn't rebuild the statement, and we didn't recreate the
            // connection, recreate the connection
            if (!recreatedConnection) {
                wrappedConnection_.reconnect();
                rebuildStatement();
            } else {
                throw e;
            }
        }
        setAllVariables();
        setAllParameters();
    }

    private String toString(String sql, boolean throwException) throws SQLException {
        if (sql == null) {
            sql = "";
        }
        StringBuffer temp = new StringBuffer("WrappedPreparedStatement - query: \"" + sql + "\"");
        int parameterIndex = 1;
        for (int i = 0; i < temp.length(); i++) {
            if (temp.charAt(i) == '?') {
                QueryParameter para = ((QueryParameter) setVariables_.get(new Integer(parameterIndex++)));
                if (para == null) {
                    if (throwException) {
                        throw new SQLException("You forgot to set parameter " + parameterIndex);
                    } else {
                        para = new QueryParameter(0, "--UNSET_PARAMETER--");
                    }
                }

                String replacementValue = "";
                if (para.type == WrapperConstants.NULL) {
                    replacementValue = "'null'";
                } else {
                    replacementValue = "'" + (para.value == null ? "null" : para.value.toString()) + "'";
                }
                temp.replace(i, i + 1, replacementValue);
                i = i + replacementValue.length();
            }
        }
        return temp.toString();
    }

    public String toString() {
        try {
            return toString(this.sql_, false);
        } catch (SQLException e) {
            // this exception actually won't be thrown because of the above
            // false param.
            return null;
        }
    }

    private void debugQuery(String sql) throws SQLException {
        if (logger.isDebugEnabled()) {
            logger.debug("Executing query: " + this.toString(sql, true));
        }
    }

    public ResultSet executeQuery() throws SQLException {
        debugQuery(sql_);
        try {
            return statement_.executeQuery();
        } catch (Exception e) {
            // try the query again.
            try {
                rebuildAll();
                return statement_.executeQuery();
            } catch (SQLException e1) {
                // if anything goes wrong in retrying the query, lets just throw
                // the origional exception
                if (e instanceof SQLException) {
                    throw (SQLException) e;
                } else {
                    throw new SQLException("Unexpected Error " + e.toString());
                }
            }
        }
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        debugQuery(sql);
        try {
            return statement_.executeQuery(sql);
        } catch (Exception e) {
            // try the query again.
            try {
                rebuildAll();
                return statement_.executeQuery(sql);
            } catch (SQLException e1) {
                // if anything goes wrong in retrying the query, lets just throw
                // the origional exception
                if (e instanceof SQLException) {
                    throw (SQLException) e;
                } else {
                    throw new SQLException("Unexpected Error " + e.toString());
                }
            }

        }
    }

    public boolean execute(String sql) throws SQLException {
        debugQuery(sql);
        try {
            return statement_.execute(sql);
        } catch (SQLException e) {
            // try the query again.
            try {
                rebuildAll();
                return statement_.execute(sql);
            } catch (SQLException e1) {
                // if anything goes wrong in retrying the query, lets just throw
                // the origional exception
                if (e instanceof SQLException) {
                    throw (SQLException) e;
                } else {
                    throw new SQLException("Unexpected Error " + e.toString());
                }
            }

        }
    }

    public int executeUpdate() throws SQLException {
        debugQuery(sql_);

        try {
            return statement_.executeUpdate();
        } catch (SQLException e) {
            // try the query again.
            try {
                rebuildAll();
                return statement_.executeUpdate();
            } catch (SQLException e1) {
                // if anything goes wrong in retrying the query, lets just throw
                // the origional exception
                if (e instanceof SQLException) {
                    throw (SQLException) e;
                } else {
                    throw new SQLException("Unexpected Error " + e.toString());
                }
            }

        }
    }

    public int executeUpdate(String sql) throws SQLException {
        debugQuery(sql);
        try {
            return statement_.executeUpdate(sql);
        } catch (SQLException e) {
            // try the query again.
            try {
                rebuildAll();
                return statement_.executeUpdate(sql);
            } catch (SQLException e1) {
                // if anything goes wrong in retrying the query, lets just throw
                // the origional exception
                if (e instanceof SQLException) {
                    throw (SQLException) e;
                } else {
                    throw new SQLException("Unexpected Error " + e.toString());
                }
            }

        }
    }

    private class QueryParameter {
        int type;
        Object value;
        int targetType;

        public QueryParameter(int type, Object value) {
            this.type = type;
            this.value = value;
            this.targetType = Integer.MIN_VALUE;
        }

        public QueryParameter(int type, Object value, int targetSqlType) {
            this.type = type;
            this.value = value;
            this.targetType = targetSqlType;
        }
    }

    public void addBatch() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method addBatch not yet implemented.");
    }

    public boolean execute() throws SQLException {
        debugQuery(sql_);
        try {
            return statement_.execute();
        } catch (SQLException e) {
            // try the query again.
            try {
                rebuildAll();
                return statement_.execute();
            } catch (SQLException e1) {
                // if anything goes wrong in retrying the query, lets just throw
                // the origional exception
                if (e instanceof SQLException) {
                    throw (SQLException) e;
                } else {
                    throw new SQLException("Unexpected Error " + e.toString());
                }
            }

        }
    }

    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setAsciiStream not yet implemented.");
    }

    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setBinaryStream not yet implemented.");
    }

    /** @deprecated */
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setUnicodeStream not yet implemented.");
    }

    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setCharacterStream not yet implemented.");
    }

    public void setObject(int parameterIndex, Object x, int targetSqlType, int scale) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setObject not yet implemented.");
    }

    public void setNull(int paramIndex, int sqlType, String typeName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setNull not yet implemented.");
    }

    public ParameterMetaData getParameterMetaData() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getParameterMetaData not yet implemented.");
    }

    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setDate not yet implemented.");
    }

    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setTime not yet implemented.");
    }

    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setTimestamp not yet implemented.");
    }

    public int[] executeBatch() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method executeBatch not yet implemented.");
    }

    public void addBatch(String sql) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method addBatch not yet implemented.");
    }

    public void setCursorName(String name) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setCursorName not yet implemented.");
    }

    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method executeUpdate not yet implemented.");
    }

    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method execute not yet implemented.");
    }

    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method executeUpdate not yet implemented.");
    }

    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method execute not yet implemented.");
    }

    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method executeUpdate not yet implemented.");
    }

    public boolean execute(String sql, String[] columnNames) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method execute not yet implemented.");
    }

    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    public void setNString(int parameterIndex, String value) throws SQLException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    public boolean isClosed() throws SQLException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    public boolean isPoolable() throws SQLException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    public void setPoolable(boolean poolable) throws SQLException {
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