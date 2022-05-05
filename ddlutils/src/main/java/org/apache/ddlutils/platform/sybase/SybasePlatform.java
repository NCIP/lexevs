/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.platform.sybase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.ddlutils.DatabaseOperationException;
import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.model.TypeMap;
import org.apache.ddlutils.platform.PlatformImplBase;
import org.apache.ddlutils.platform.sybase.SybaseBuilder;
import org.apache.ddlutils.platform.sybase.SybaseModelReader;

public class SybasePlatform
extends PlatformImplBase {
    public static final String DATABASENAME = "Sybase";
    public static final String JDBC_DRIVER = "com.sybase.jdbc2.jdbc.SybDriver";
    public static final String JDBC_DRIVER_OLD = "com.sybase.jdbc.SybDriver";
    public static final String JDBC_SUBPROTOCOL = "sybase:Tds";
    public static final long MAX_TEXT_SIZE = Integer.MAX_VALUE;

    public SybasePlatform() {
        PlatformInfo info = this.getPlatformInfo();
        info.setMaxIdentifierLength(28);
        info.setNullAsDefaultValueRequired(true);
        info.setCommentPrefix("/*");
        info.setCommentSuffix("*/");
        info.addNativeTypeMapping(2003, "IMAGE");
        info.addNativeTypeMapping(-5, "DECIMAL(19,0)");
        info.addNativeTypeMapping(-7, "SMALLINT", 5);
        info.addNativeTypeMapping(2004, "IMAGE", -4);
        info.addNativeTypeMapping(2005, "TEXT", -1);
        info.addNativeTypeMapping(91, "DATETIME", 93);
        info.addNativeTypeMapping(2001, "IMAGE", -4);
        info.addNativeTypeMapping(8, "DOUBLE PRECISION");
        info.addNativeTypeMapping(6, "DOUBLE PRECISION", 8);
        info.addNativeTypeMapping(4, "INT");
        info.addNativeTypeMapping(2000, "IMAGE", -4);
        info.addNativeTypeMapping(-4, "IMAGE");
        info.addNativeTypeMapping(-1, "TEXT");
        info.addNativeTypeMapping(0, "IMAGE", -4);
        info.addNativeTypeMapping(1111, "IMAGE", -4);
        info.addNativeTypeMapping(2006, "IMAGE", -4);
        info.addNativeTypeMapping(2002, "IMAGE", -4);
        info.addNativeTypeMapping(92, "DATETIME", 93);
        info.addNativeTypeMapping(93, "DATETIME", 93);
        info.addNativeTypeMapping(-6, "SMALLINT", 5);
        info.addNativeTypeMapping("BOOLEAN", "SMALLINT", "SMALLINT");
        info.addNativeTypeMapping("DATALINK", "IMAGE", "LONGVARBINARY");
        info.setDefaultSize(-2, 254);
        info.setDefaultSize(-3, 254);
        info.setDefaultSize(1, 254);
        info.setDefaultSize(12, 254);
        this.setSqlBuilder(new SybaseBuilder(this));
        this.setModelReader(new SybaseModelReader(this));
    }


    public String getName() {
        return DATABASENAME;
    }

    private void setTextSize(long size) {
        Connection connection = this.borrowConnection();
        Statement stmt = null;
        try {
            try {
                stmt = connection.createStatement();
                stmt.execute("SET textsize " + size);
            }
            catch (SQLException ex) {
                throw new DatabaseOperationException(ex);
            }
        }
        finally {
            this.closeStatement(stmt);
            this.returnConnection(connection);
        }
    }

    @Override
    protected Object extractColumnValue(ResultSet resultSet, String columnName, int columnIdx, int jdbcType) throws DatabaseOperationException, SQLException {
        boolean useIdx;
        boolean bl = useIdx = columnName == null;
        if (jdbcType == -4 || jdbcType == 2004) {
            InputStream stream;
            InputStream inputStream = stream = useIdx ? resultSet.getBinaryStream(columnIdx) : resultSet.getBinaryStream(columnName);
            if (stream == null) {
                return null;
            }
            byte[] buf = new byte[65536];
            byte[] result = new byte[]{};
            try {
                int len;
                do {
                    if ((len = stream.read(buf)) <= 0) continue;
                    byte[] newResult = new byte[result.length + len];
                    System.arraycopy(result, 0, newResult, 0, result.length);
                    System.arraycopy(buf, 0, newResult, result.length, len);
                    result = newResult;
                } while (len > 0);
                stream.close();
                return result;
            }
            catch (IOException ex) {
                throw new DatabaseOperationException("Error while extracting the value of column " + columnName + " of type " + TypeMap.getJdbcTypeName(jdbcType) + " from a result set", ex);
            }
        }
        return super.extractColumnValue(resultSet, columnName, columnIdx, jdbcType);
    }

    @Override
    protected void setStatementParameterValue(PreparedStatement statement, int sqlIndex, int typeCode, Object value) throws SQLException {
        if (typeCode == 2004 || typeCode == -4) {
            if (value instanceof byte[]) {
                byte[] data = (byte[])value;
                statement.setBinaryStream(sqlIndex, (InputStream)new ByteArrayInputStream(data), data.length);
            } else {
                super.setStatementParameterValue(statement, sqlIndex, -4, value);
            }
        } else if (typeCode == 2005) {
            super.setStatementParameterValue(statement, sqlIndex, -1, value);
        } else {
            super.setStatementParameterValue(statement, sqlIndex, typeCode, value);
        }
    }

    @Override
    public List fetch(Database model, String sql, Collection parameters, Table[] queryHints, int start, int end) throws DatabaseOperationException {
        this.setTextSize(Integer.MAX_VALUE);
        return super.fetch(model, sql, parameters, queryHints, start, end);
    }

    @Override
    public List fetch(Database model, String sql, Table[] queryHints, int start, int end) throws DatabaseOperationException {
        this.setTextSize(Integer.MAX_VALUE);
        return super.fetch(model, sql, queryHints, start, end);
    }

    @Override
    public Iterator query(Database model, String sql, Collection parameters, Table[] queryHints) throws DatabaseOperationException {
        this.setTextSize(Integer.MAX_VALUE);
        return super.query(model, sql, parameters, queryHints);
    }

    @Override
    public Iterator query(Database model, String sql, Table[] queryHints) throws DatabaseOperationException {
        this.setTextSize(Integer.MAX_VALUE);
        return super.query(model, sql, queryHints);
    }

    private boolean useIdentityOverrideFor(Table table) {
        return this.isIdentityOverrideOn() && this.getPlatformInfo().isIdentityOverrideAllowed() && table.getAutoIncrementColumns().length > 0;
    }

    @Override
    protected void beforeInsert(Connection connection, Table table) throws SQLException {
        if (this.useIdentityOverrideFor(table)) {
            SybaseBuilder builder = (SybaseBuilder)this.getSqlBuilder();
            String quotationOn = builder.getQuotationOnStatement();
            String identityInsertOn = builder.getEnableIdentityOverrideSql(table);
            Statement stmt = connection.createStatement();
            if (quotationOn.length() > 0) {
                stmt.execute(quotationOn);
            }
            stmt.execute(identityInsertOn);
            stmt.close();
        }
    }

    @Override
    protected void afterInsert(Connection connection, Table table) throws SQLException {
        if (this.useIdentityOverrideFor(table)) {
            SybaseBuilder builder = (SybaseBuilder)this.getSqlBuilder();
            String quotationOn = builder.getQuotationOnStatement();
            String identityInsertOff = builder.getDisableIdentityOverrideSql(table);
            Statement stmt = connection.createStatement();
            if (quotationOn.length() > 0) {
                stmt.execute(quotationOn);
            }
            stmt.execute(identityInsertOff);
            stmt.close();
        }
    }

    @Override
    protected void beforeUpdate(Connection connection, Table table) throws SQLException {
        this.beforeInsert(connection, table);
    }

    @Override
    protected void afterUpdate(Connection connection, Table table) throws SQLException {
        this.afterInsert(connection, table);
    }
}

