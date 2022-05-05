/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.oro.text.regex.MalformedPatternException
 *  org.apache.oro.text.regex.Pattern
 *  org.apache.oro.text.regex.Perl5Compiler
 *  org.apache.oro.text.regex.Perl5Matcher
 */
package org.apache.ddlutils.platform.sybase;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import org.apache.ddlutils.DdlUtilsException;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.ForeignKey;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.Reference;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.model.TypeMap;
import org.apache.ddlutils.platform.DatabaseMetaDataWrapper;
import org.apache.ddlutils.platform.JdbcModelReader;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

public class SybaseModelReader
extends JdbcModelReader {
    private Pattern _isoDatePattern;
    private Pattern _isoTimePattern;

    public SybaseModelReader(Platform platform) {
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
        Table table = super.readTable(metaData, values);
        if (table != null) {
            this.determineAutoIncrementFromResultSetMetaData(table, table.getColumns());
        }
        return table;
    }

    @Override
    protected Column readColumn(DatabaseMetaDataWrapper metaData, Map values) throws SQLException {
        Column column = super.readColumn(metaData, values);
        if (column.getTypeCode() == 3 && column.getSizeAsInt() == 19 && column.getScale() == 0) {
            column.setTypeCode(-5);
        } else if (column.getDefaultValue() != null) {
            if (column.getTypeCode() == 93) {
                Perl5Matcher matcher = new Perl5Matcher();
                Timestamp timestamp = null;
                if (matcher.matches(column.getDefaultValue(), this._isoDatePattern)) {
                    timestamp = new Timestamp(Date.valueOf(matcher.getMatch().group(1)).getTime());
                } else if (matcher.matches(column.getDefaultValue(), this._isoTimePattern)) {
                    timestamp = new Timestamp(Time.valueOf(matcher.getMatch().group(1)).getTime());
                }
                if (timestamp != null) {
                    column.setDefaultValue(timestamp.toString());
                }
            } else if (TypeMap.isTextType(column.getTypeCode())) {
                column.setDefaultValue(this.unescape(column.getDefaultValue(), "'", "''"));
            }
        }
        return column;
    }

    @Override
    protected void readIndex(DatabaseMetaDataWrapper metaData, Map values, Map knownIndices) throws SQLException {
        String indexName;
        if (this.getPlatform().isDelimitedIdentifierModeOn() && (indexName = (String)values.get("INDEX_NAME")) != null) {
            String delimiter = this.getPlatformInfo().getDelimiterToken();
            if (indexName != null && indexName.startsWith(delimiter) && indexName.endsWith(delimiter)) {
                indexName = indexName.substring(delimiter.length(), indexName.length() - delimiter.length());
                values.put("INDEX_NAME", indexName);
            }
        }
        super.readIndex(metaData, values, knownIndices);
    }

    @Override
    protected Collection readForeignKeys(DatabaseMetaDataWrapper metaData, String tableName) throws SQLException {
        StringBuffer query = new StringBuffer();
        query.append("SELECT refobjs.name, localtables.id, remotetables.name, remotetables.id");
        int idx = 1;
        while (idx <= 16) {
            query.append(", refs.fokey");
            query.append(idx);
            query.append(", refs.refkey");
            query.append(idx);
            ++idx;
        }
        query.append(" FROM sysreferences refs, sysobjects refobjs, sysobjects localtables, sysobjects remotetables");
        query.append(" WHERE refobjs.type = 'RI' AND refs.constrid = refobjs.id AND");
        query.append(" localtables.type = 'U' AND refs.tableid = localtables.id AND localtables.name = '");
        query.append(tableName);
        query.append("' AND remotetables.type = 'U' AND refs.reftabid = remotetables.id");
        Statement stmt = this.getConnection().createStatement();
        PreparedStatement prepStmt = this.getConnection().prepareStatement("SELECT name FROM syscolumns WHERE id = ? AND colid = ?");
        ArrayList<ForeignKey> result = new ArrayList<ForeignKey>();
        try {
            ResultSet fkRs = stmt.executeQuery(query.toString());
            while (fkRs.next()) {
                ForeignKey fk = new ForeignKey(fkRs.getString(1));
                int localTableId = fkRs.getInt(2);
                int remoteTableId = fkRs.getInt(4);
                fk.setForeignTableName(fkRs.getString(3));
                int idx2 = 0;
                while (idx2 < 16) {
                    short fkColIdx = fkRs.getShort(5 + idx2 + idx2);
                    short pkColIdx = fkRs.getShort(6 + idx2 + idx2);
                    Reference ref = new Reference();
                    if (fkColIdx == 0) break;
                    prepStmt.setInt(1, localTableId);
                    prepStmt.setShort(2, fkColIdx);
                    ResultSet colRs = prepStmt.executeQuery();
                    if (colRs.next()) {
                        ref.setLocalColumnName(colRs.getString(1));
                    }
                    colRs.close();
                    prepStmt.setInt(1, remoteTableId);
                    prepStmt.setShort(2, pkColIdx);
                    colRs = prepStmt.executeQuery();
                    if (colRs.next()) {
                        ref.setForeignColumnName(colRs.getString(1));
                    }
                    colRs.close();
                    fk.addReference(ref);
                    ++idx2;
                }
                result.add(fk);
            }
            fkRs.close();
        }
        finally {
            stmt.close();
            prepStmt.close();
        }
        return result;
    }

    @Override
    protected boolean isInternalPrimaryKeyIndex(DatabaseMetaDataWrapper metaData, Table table, Index index) throws SQLException {
        StringBuffer query = new StringBuffer();
        query.append("SELECT name = sysindexes.name FROM sysindexes, sysobjects WHERE sysobjects.name = '");
        query.append(table.getName());
        query.append("' AND sysindexes.name = '");
        query.append(index.getName());
        query.append("' AND sysobjects.id = sysindexes.id AND (sysindexes.status & 2048) > 0");
        Statement stmt = this.getConnection().createStatement();
        try {
            ResultSet rs = stmt.executeQuery(query.toString());
            boolean result = rs.next();
            rs.close();
            boolean bl = result;
            return bl;
        }
        finally {
            stmt.close();
        }
    }
}

