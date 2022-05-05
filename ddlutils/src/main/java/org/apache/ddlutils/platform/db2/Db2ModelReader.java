/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.oro.text.regex.MalformedPatternException
 *  org.apache.oro.text.regex.Pattern
 *  org.apache.oro.text.regex.Perl5Compiler
 *  org.apache.oro.text.regex.Perl5Matcher
 */
package org.apache.ddlutils.platform.db2;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
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

public class Db2ModelReader
extends JdbcModelReader {
    private static final String[] KNOWN_SYSTEM_TABLES = new String[]{"STMG_DBSIZE_INFO", "HMON_ATM_INFO", "HMON_COLLECTION", "POLICY"};
    private Pattern _db2TimePattern;
    private Pattern _db2TimestampPattern;

    public Db2ModelReader(Platform platform) {
        super(platform);
        this.setDefaultCatalogPattern(null);
        this.setDefaultSchemaPattern(null);
        Perl5Compiler compiler = new Perl5Compiler();
        try {
            this._db2TimePattern = compiler.compile("'(\\d{2}).(\\d{2}).(\\d{2})'");
            this._db2TimestampPattern = compiler.compile("'(\\d{4}\\-\\d{2}\\-\\d{2})\\-(\\d{2}).(\\d{2}).(\\d{2})(\\.\\d{1,8})?'");
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
        }
        return table;
    }

    @Override
    protected Column readColumn(DatabaseMetaDataWrapper metaData, Map values) throws SQLException {
        Column column = super.readColumn(metaData, values);
        if (column.getDefaultValue() != null) {
            if (column.getTypeCode() == 92) {
                Perl5Matcher matcher = new Perl5Matcher();
                if (matcher.matches(column.getDefaultValue(), this._db2TimePattern)) {
                    StringBuffer newDefault = new StringBuffer();
                    newDefault.append("'");
                    newDefault.append(matcher.getMatch().group(1));
                    newDefault.append(":");
                    newDefault.append(matcher.getMatch().group(2));
                    newDefault.append(":");
                    newDefault.append(matcher.getMatch().group(3));
                    newDefault.append("'");
                    column.setDefaultValue(newDefault.toString());
                }
            } else if (column.getTypeCode() == 93) {
                Perl5Matcher matcher = new Perl5Matcher();
                if (matcher.matches(column.getDefaultValue(), this._db2TimestampPattern)) {
                    StringBuffer newDefault = new StringBuffer();
                    newDefault.append("'");
                    newDefault.append(matcher.getMatch().group(1));
                    newDefault.append(" ");
                    newDefault.append(matcher.getMatch().group(2));
                    newDefault.append(":");
                    newDefault.append(matcher.getMatch().group(3));
                    newDefault.append(":");
                    newDefault.append(matcher.getMatch().group(4));
                    if (matcher.getMatch().groups() > 4 && matcher.getMatch().group(4) != null) {
                        newDefault.append(matcher.getMatch().group(5));
                    }
                    newDefault.append("'");
                    column.setDefaultValue(newDefault.toString());
                }
            } else if (TypeMap.isTextType(column.getTypeCode())) {
                column.setDefaultValue(this.unescape(column.getDefaultValue(), "'", "''"));
            }
        }
        return column;
    }

    @Override
    protected boolean isInternalPrimaryKeyIndex(DatabaseMetaDataWrapper metaData, Table table, Index index) throws SQLException {
        if (index.getName().startsWith("SQL")) {
            try {
                Long.parseLong(index.getName().substring(3));
                return true;
            }
            catch (NumberFormatException numberFormatException) {
                return false;
            }
        }
        ResultSet pkData = null;
        HashSet pkNames = new HashSet();
        try {
            pkData = metaData.getPrimaryKeys(table.getName());
            while (pkData.next()) {
                Map values = this.readColumns(pkData, this.getColumnsForPK());
                pkNames.add(values.get("PK_NAME"));
            }
        }
        finally {
            if (pkData != null) {
                pkData.close();
            }
        }
        return pkNames.contains(index.getName());
    }
}

