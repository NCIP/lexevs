/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.platform.interbase;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ddlutils.DdlUtilsException;
import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.platform.PlatformImplBase;
import org.apache.ddlutils.platform.interbase.InterbaseBuilder;
import org.apache.ddlutils.platform.interbase.InterbaseModelReader;

public class InterbasePlatform
extends PlatformImplBase {
    public static final String DATABASENAME = "Interbase";
    public static final String JDBC_DRIVER = "interbase.interclient.Driver";
    public static final String JDBC_SUBPROTOCOL = "interbase";

    public InterbasePlatform() {
        PlatformInfo info = this.getPlatformInfo();
        info.setMaxIdentifierLength(31);
        info.setCommentPrefix("/*");
        info.setCommentSuffix("*/");
        info.setSystemForeignKeyIndicesAlwaysNonUnique(true);
        info.addNativeTypeMapping(2003, "BLOB", -4);
        info.addNativeTypeMapping(-5, "NUMERIC(18,0)");
        info.addNativeTypeMapping(-2, "BLOB", -4);
        info.addNativeTypeMapping(-7, "SMALLINT", 5);
        info.addNativeTypeMapping(2004, "BLOB", -4);
        info.addNativeTypeMapping(2005, "BLOB SUB_TYPE TEXT");
        info.addNativeTypeMapping(2001, "BLOB", -4);
        info.addNativeTypeMapping(8, "DOUBLE PRECISION");
        info.addNativeTypeMapping(6, "DOUBLE PRECISION", 8);
        info.addNativeTypeMapping(2000, "BLOB", -4);
        info.addNativeTypeMapping(-4, "BLOB", -4);
        info.addNativeTypeMapping(-1, "BLOB SUB_TYPE TEXT", 2005);
        info.addNativeTypeMapping(0, "BLOB", -4);
        info.addNativeTypeMapping(1111, "BLOB", -4);
        info.addNativeTypeMapping(7, "FLOAT");
        info.addNativeTypeMapping(2006, "BLOB", -4);
        info.addNativeTypeMapping(2002, "BLOB", -4);
        info.addNativeTypeMapping(-6, "SMALLINT", 5);
        info.addNativeTypeMapping(-3, "BLOB", -4);
        info.addNativeTypeMapping("BOOLEAN", "SMALLINT", "SMALLINT");
        info.addNativeTypeMapping("DATALINK", "BLOB", "LONGVARBINARY");
        info.setDefaultSize(1, 254);
        info.setDefaultSize(12, 254);
        info.setHasSize(-2, false);
        info.setHasSize(-3, false);
        this.setSqlBuilder(new InterbaseBuilder(this));
        this.setModelReader(new InterbaseModelReader(this));
    }


    public String getName() {
        return DATABASENAME;
    }

    @Override
    protected void setStatementParameterValue(PreparedStatement statement, int sqlIndex, int typeCode, Object value) throws SQLException {
        if (value != null) {
            if (value instanceof byte[] && (typeCode == -2 || typeCode == -3 || typeCode == 2004)) {
                byte[] bytes = (byte[])value;
                ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
                statement.setBinaryStream(sqlIndex, (InputStream)stream, bytes.length);
                return;
            }
            if (value instanceof String && (typeCode == 2005 || typeCode == -1)) {
                statement.setString(sqlIndex, (String)value);
                return;
            }
        }
        super.setStatementParameterValue(statement, sqlIndex, typeCode, value);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected Object extractColumnValue(ResultSet resultSet, String columnName, int columnIdx, int jdbcType) throws SQLException {
        boolean useIdx = columnName == null;
        switch (jdbcType) {
            case -3: 
            case -2: 
            case 2004: {
                try {
                    BufferedInputStream input = new BufferedInputStream(useIdx ? resultSet.getBinaryStream(columnIdx) : resultSet.getBinaryStream(columnName));
                    if (resultSet.wasNull()) {
                        return null;
                    }
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream(1024);
                    byte[] data = new byte[1024];
                    while (true) {
                        int numRead;
                        if ((numRead = input.read(data, 0, data.length)) == -1) {
                            input.close();
                            return buffer.toByteArray();
                        }
                        buffer.write(data, 0, numRead);
                    }
                }
                catch (IOException ex) {
                    throw new DdlUtilsException(ex);
                }
            }
            case -1: 
            case 2005: {
                String value;
                String string = value = useIdx ? resultSet.getString(columnIdx) : resultSet.getString(columnName);
                if (resultSet.wasNull()) {
                    return null;
                }
                String string2 = value;
                return string2;
            }
        }
        return super.extractColumnValue(resultSet, columnName, columnIdx, jdbcType);
    }
}

