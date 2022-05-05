/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.oro.text.regex.MalformedPatternException
 *  org.apache.oro.text.regex.Pattern
 *  org.apache.oro.text.regex.Perl5Compiler
 *  org.apache.oro.text.regex.Perl5Matcher
 */
package org.apache.ddlutils.platform.mssql;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Map;
import org.apache.ddlutils.DdlUtilsException;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.model.TypeMap;
import org.apache.ddlutils.platform.DatabaseMetaDataWrapper;
import org.apache.ddlutils.platform.JdbcModelReader;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

public class MSSqlModelReader
extends JdbcModelReader {
    private static final String[] KNOWN_SYSTEM_TABLES = new String[]{"dtproperties"};
    private Pattern _isoDatePattern;
    private Pattern _isoTimePattern;

    public MSSqlModelReader(Platform platform) {
        super(platform);
        this.setDefaultCatalogPattern(null);
        this.setDefaultSchemaPattern(null);
        this.setDefaultTablePattern("%");
        Perl5Compiler compiler = new Perl5Compiler();
        try {
            this._isoDatePattern = compiler.compile("'(\\d{4}\\-\\d{2}\\-\\d{2})'");
            this._isoTimePattern = compiler.compile("'(\\d{2}:\\d{2}:\\d{2})'");
        }
        catch (MalformedPatternException ex) {
            throw new DdlUtilsException(ex);
        }
    }

    @Override
    protected Table readTable(DatabaseMetaDataWrapper metaData, Map values) throws SQLException {
        String tableName = (String)values.get("TABLE_NAME");
        int idx = 0;
        while (idx < KNOWN_SYSTEM_TABLES.length) {
            if (KNOWN_SYSTEM_TABLES[idx].equals(tableName)) {
                return null;
            }
            ++idx;
        }
        Table table = super.readTable(metaData, values);
        if (table != null) {
            this.determineAutoIncrementFromResultSetMetaData(table, table.getColumns());
            int idx2 = 0;
            while (idx2 < table.getIndexCount()) {
                Index index = table.getIndex(idx2);
                if (index.isUnique() && this.existsPKWithName(metaData, table, index.getName())) {
                    table.removeIndex(idx2);
                    continue;
                }
                ++idx2;
            }
        }
        return table;
    }

    @Override
    protected boolean isInternalPrimaryKeyIndex(DatabaseMetaDataWrapper metaData, Table table, Index index) {
        StringBuffer pkIndexName = new StringBuffer();
        pkIndexName.append("PK__");
        pkIndexName.append(table.getName());
        pkIndexName.append("__");
        return index.getName().toUpperCase().startsWith(pkIndexName.toString().toUpperCase());
    }

    private boolean existsPKWithName(DatabaseMetaDataWrapper metaData, Table table, String name) {
        try {
            ResultSet pks = metaData.getPrimaryKeys(table.getName());
            boolean found = false;
            while (pks.next() && !found) {
                if (!name.equals(pks.getString("PK_NAME"))) continue;
                found = true;
            }
            pks.close();
            return found;
        }
        catch (SQLException ex) {
            throw new DdlUtilsException(ex);
        }
    }

    @Override
    protected Column readColumn(DatabaseMetaDataWrapper metaData, Map values) throws SQLException {
        Column column = super.readColumn(metaData, values);
        String defaultValue = column.getDefaultValue();
        if (defaultValue != null) {
            while (defaultValue.startsWith("(") && defaultValue.endsWith(")")) {
                defaultValue = defaultValue.substring(1, defaultValue.length() - 1);
            }
            if (column.getTypeCode() == 93) {
                Perl5Matcher matcher = new Perl5Matcher();
                Timestamp timestamp = null;
                if (matcher.matches(defaultValue, this._isoDatePattern)) {
                    timestamp = new Timestamp(Date.valueOf(matcher.getMatch().group(1)).getTime());
                } else if (matcher.matches(defaultValue, this._isoTimePattern)) {
                    timestamp = new Timestamp(Time.valueOf(matcher.getMatch().group(1)).getTime());
                }
                if (timestamp != null) {
                    defaultValue = timestamp.toString();
                }
            } else if (column.getTypeCode() == 3) {
                if (column.getScale() == 0 && defaultValue.endsWith(".")) {
                    defaultValue = defaultValue.substring(0, defaultValue.length() - 1);
                }
            } else if (TypeMap.isTextType(column.getTypeCode())) {
                defaultValue = this.unescape(defaultValue, "'", "''");
            }
            column.setDefaultValue(defaultValue);
        }
        if (column.getTypeCode() == 3 && column.getSizeAsInt() == 19 && column.getScale() == 0) {
            column.setTypeCode(-5);
        }
        return column;
    }
}

