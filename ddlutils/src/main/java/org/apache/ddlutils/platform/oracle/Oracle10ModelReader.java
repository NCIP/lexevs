/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.platform.oracle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.DatabaseMetaDataWrapper;
import org.apache.ddlutils.platform.oracle.Oracle8ModelReader;

public class Oracle10ModelReader
extends Oracle8ModelReader {
    public Oracle10ModelReader(Platform platform) {
        super(platform);
    }

    @Override
    protected Table readTable(DatabaseMetaDataWrapper metaData, Map values) throws SQLException {
        PreparedStatement stmt = null;
        boolean deletedObj = false;
        try {
            stmt = this.getConnection().prepareStatement("SELECT * FROM RECYCLEBIN WHERE OBJECT_NAME=?");
            stmt.setString(1, (String)values.get("TABLE_NAME"));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                deletedObj = true;
            }
            rs.close();
        }
        finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return deletedObj ? null : super.readTable(metaData, values);
    }
}

