/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.collections.map.ListOrderedMap
 *  org.apache.oro.text.regex.MalformedPatternException
 *  org.apache.oro.text.regex.Pattern
 *  org.apache.oro.text.regex.Perl5Compiler
 *  org.apache.oro.text.regex.Perl5Matcher
 */
package org.apache.ddlutils.platform.oracle;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.ddlutils.DdlUtilsException;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.model.TypeMap;
import org.apache.ddlutils.platform.DatabaseMetaDataWrapper;
import org.apache.ddlutils.platform.JdbcModelReader;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

public class Oracle8ModelReader
extends JdbcModelReader {
    private Pattern _oracleIsoDatePattern;
    private Pattern _oracleIsoTimePattern;
    private Pattern _oracleIsoTimestampPattern;

    public Oracle8ModelReader(Platform platform) {
        super(platform);
        this.setDefaultCatalogPattern(null);
        this.setDefaultSchemaPattern(null);
        this.setDefaultTablePattern("%");
        Perl5Compiler compiler = new Perl5Compiler();
        try {
            this._oracleIsoDatePattern = compiler.compile("TO_DATE\\('([^']*)'\\, 'YYYY\\-MM\\-DD'\\)");
            this._oracleIsoTimePattern = compiler.compile("TO_DATE\\('([^']*)'\\, 'HH24:MI:SS'\\)");
            this._oracleIsoTimestampPattern = compiler.compile("TO_DATE\\('([^']*)'\\, 'YYYY\\-MM\\-DD HH24:MI:SS'\\)");
        }
        catch (MalformedPatternException ex) {
            throw new DdlUtilsException(ex);
        }
    }

    @Override
    protected Table readTable(DatabaseMetaDataWrapper metaData, Map values) throws SQLException {
        String tableName = (String)values.get("TABLE_NAME");
        if (tableName.indexOf(36) > 0) {
            return null;
        }
        Table table = super.readTable(metaData, values);
        if (table != null) {
            this.determineAutoIncrementColumns(table);
        }
        return table;
    }

    @Override
    protected Column readColumn(DatabaseMetaDataWrapper metaData, Map values) throws SQLException {
        Column column;
        block31: {
            block30: {
                column = super.readColumn(metaData, values);
                if (column.getDefaultValue() != null) {
                    column.setDefaultValue(column.getDefaultValue().trim());
                }
                if (column.getTypeCode() != 3) break block30;
                switch (column.getSizeAsInt()) {
                    case 1: {
                        if (column.getScale() == 0) {
                            column.setTypeCode(-7);
                            break;
                        }
                        break block31;
                    }
                    case 3: {
                        if (column.getScale() == 0) {
                            column.setTypeCode(-6);
                            break;
                        }
                        break block31;
                    }
                    case 5: {
                        if (column.getScale() == 0) {
                            column.setTypeCode(5);
                            break;
                        }
                        break block31;
                    }
                    case 18: {
                        column.setTypeCode(7);
                        break;
                    }
                    case 22: {
                        if (column.getScale() == 0) {
                            column.setTypeCode(4);
                            break;
                        }
                        break block31;
                    }
                    case 38: {
                        if (column.getScale() == 0) {
                            column.setTypeCode(-5);
                            break;
                        }
                        column.setTypeCode(8);
                    }
                }
                break block31;
            }
            if (column.getTypeCode() == 6) {
                switch (column.getSizeAsInt()) {
                    case 63: {
                        column.setTypeCode(7);
                        break;
                    }
                    case 126: {
                        column.setTypeCode(8);
                    }
                }
            } else if (column.getTypeCode() == 91 || column.getTypeCode() == 93) {
                column.setTypeCode(93);
                if (column.getDefaultValue() != null) {
                    Perl5Matcher matcher = new Perl5Matcher();
                    Timestamp timestamp = null;
                    if (matcher.matches(column.getDefaultValue(), this._oracleIsoTimestampPattern)) {
                        String timestampVal = matcher.getMatch().group(1);
                        timestamp = Timestamp.valueOf(timestampVal);
                    } else if (matcher.matches(column.getDefaultValue(), this._oracleIsoDatePattern)) {
                        String dateVal = matcher.getMatch().group(1);
                        timestamp = new Timestamp(Date.valueOf(dateVal).getTime());
                    } else if (matcher.matches(column.getDefaultValue(), this._oracleIsoTimePattern)) {
                        String timeVal = matcher.getMatch().group(1);
                        timestamp = new Timestamp(Time.valueOf(timeVal).getTime());
                    }
                    if (timestamp != null) {
                        column.setDefaultValue(timestamp.toString());
                    }
                }
            } else if (TypeMap.isTextType(column.getTypeCode())) {
                column.setDefaultValue(this.unescape(column.getDefaultValue(), "'", "''"));
            }
        }
        return column;
    }

    protected void determineAutoIncrementColumns(Table table) throws SQLException {
        Column[] columns = table.getColumns();
        int idx = 0;
        while (idx < columns.length) {
            columns[idx].setAutoIncrement(this.isAutoIncrement(table, columns[idx]));
            ++idx;
        }
    }

    protected boolean isAutoIncrement(Table table, Column column) throws SQLException {
        PreparedStatement prepStmt = null;
        String triggerName = this.getPlatform().getSqlBuilder().getConstraintName("trg", table, column.getName(), null);
        String seqName = this.getPlatform().getSqlBuilder().getConstraintName("seq", table, column.getName(), null);
        if (!this.getPlatform().isDelimitedIdentifierModeOn()) {
            triggerName = triggerName.toUpperCase();
            seqName = seqName.toUpperCase();
        }
        try {
            prepStmt = this.getConnection().prepareStatement("SELECT * FROM user_triggers WHERE trigger_name = ?");
            prepStmt.setString(1, triggerName);
            ResultSet resultSet = prepStmt.executeQuery();
            if (!resultSet.next()) {
                return false;
            }
            prepStmt.close();
            prepStmt = this.getConnection().prepareStatement("SELECT * FROM user_sequences WHERE sequence_name = ?");
            prepStmt.setString(1, seqName);
            resultSet = prepStmt.executeQuery();
            boolean bl = resultSet.next();
            return bl;
        }
        finally {
            if (prepStmt != null) {
                prepStmt.close();
            }
        }
    }

    @Override
    protected Collection readIndices(DatabaseMetaDataWrapper metaData, String tableName) throws SQLException {
        StringBuffer query = new StringBuffer();
        query.append("SELECT a.INDEX_NAME, a.INDEX_TYPE, a.UNIQUENESS, b.COLUMN_NAME, b.COLUMN_POSITION FROM USER_INDEXES a, USER_IND_COLUMNS b WHERE ");
        query.append("a.TABLE_NAME=? AND a.GENERATED=? AND a.TABLE_TYPE=? AND a.TABLE_NAME=b.TABLE_NAME AND a.INDEX_NAME=b.INDEX_NAME AND ");
        query.append("a.INDEX_NAME NOT IN (SELECT DISTINCT c.CONSTRAINT_NAME FROM USER_CONSTRAINTS c WHERE c.CONSTRAINT_TYPE=? AND c.TABLE_NAME=a.TABLE_NAME");
        if (metaData.getSchemaPattern() != null) {
            query.append(" AND c.OWNER LIKE ?) AND a.TABLE_OWNER LIKE ?");
        } else {
            query.append(")");
        }
        ListOrderedMap indices = new ListOrderedMap();
        PreparedStatement stmt = null;
        try {
            stmt = this.getConnection().prepareStatement(query.toString());
            stmt.setString(1, this.getPlatform().isDelimitedIdentifierModeOn() ? tableName : tableName.toUpperCase());
            stmt.setString(2, "N");
            stmt.setString(3, "TABLE");
            stmt.setString(4, "P");
            if (metaData.getSchemaPattern() != null) {
                stmt.setString(5, metaData.getSchemaPattern().toUpperCase());
                stmt.setString(6, metaData.getSchemaPattern().toUpperCase());
            }
            ResultSet rs = stmt.executeQuery();
            HashMap<String, Object> values = new HashMap<String, Object>();
            while (rs.next()) {
                values.put("INDEX_NAME", rs.getString(1));
                values.put("INDEX_TYPE", new Short("3"));
                values.put("NON_UNIQUE", "UNIQUE".equalsIgnoreCase(rs.getString(3)) ? Boolean.FALSE : Boolean.TRUE);
                values.put("COLUMN_NAME", rs.getString(4));
                values.put("ORDINAL_POSITION", new Short(rs.getShort(5)));
                this.readIndex(metaData, values, (Map)indices);
            }
        }
        finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return indices.values();
    }
}

