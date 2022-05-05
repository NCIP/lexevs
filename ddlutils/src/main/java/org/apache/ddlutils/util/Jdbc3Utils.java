/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.util;

import java.sql.Statement;
import java.sql.Types;

public abstract class Jdbc3Utils {
    public static boolean supportsJava14JdbcTypes() {
        try {
            return Types.class.getField("BOOLEAN") != null && Types.class.getField("DATALINK") != null;
        }
        catch (Exception ex) {
            return false;
        }
    }

    public static int determineBooleanTypeCode() throws UnsupportedOperationException {
        try {
            return Types.class.getField("BOOLEAN").getInt(null);
        }
        catch (Exception ex) {
            throw new UnsupportedOperationException("The jdbc type BOOLEAN is not supported");
        }
    }

    public static int determineDatalinkTypeCode() throws UnsupportedOperationException {
        try {
            return Types.class.getField("DATALINK").getInt(null);
        }
        catch (Exception ex) {
            throw new UnsupportedOperationException("The jdbc type DATALINK is not supported");
        }
    }

    public static boolean supportsJava14BatchResultCodes() {
        try {
            return Statement.class.getField("SUCCESS_NO_INFO") != null && Statement.class.getField("EXECUTE_FAILED") != null;
        }
        catch (Exception ex) {
            return false;
        }
    }

    public static String getBatchResultMessage(String tableName, int rowIdx, int resultCode) throws NoSuchFieldException, IllegalAccessException {
        if (resultCode < 0) {
            block5: {
                try {
                    if (resultCode != Statement.class.getField("SUCCESS_NO_INFO").getInt(null)) break block5;
                    return null;
                }
                catch (Exception ex) {
                    throw new UnsupportedOperationException("The batch result codes are not supported");
                }
            }
            if (resultCode == Statement.class.getField("EXECUTE_FAILED").getInt(null)) {
                return "The batch insertion of row " + rowIdx + " into table " + tableName + " failed but the driver is able to continue processing";
            }
            return "The batch insertion of row " + rowIdx + " into table " + tableName + " returned an undefined status value " + resultCode;
        }
        return null;
    }
}

