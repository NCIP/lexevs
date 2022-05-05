/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.beanutils.BasicDynaBean
 *  org.apache.commons.beanutils.BasicDynaClass
 *  org.apache.commons.beanutils.DynaBean
 *  org.apache.commons.beanutils.DynaClass
 *  org.apache.commons.beanutils.DynaProperty
 *  org.apache.commons.collections.map.ListOrderedMap
 */
package org.apache.ddlutils.platform;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import org.apache.commons.beanutils.BasicDynaBean;
import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.ddlutils.DatabaseOperationException;
import org.apache.ddlutils.dynabean.SqlDynaBean;
import org.apache.ddlutils.dynabean.SqlDynaClass;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.PlatformImplBase;

public class ModelBasedResultSetIterator
implements Iterator {
    private PlatformImplBase _platform;
    private ResultSet _resultSet;
    private DynaClass _dynaClass;
    private boolean _caseSensitive;
    private Map _preparedQueryHints;
    private Map _columnsToProperties = new ListOrderedMap();
    private boolean _needsAdvancing = true;
    private boolean _isAtEnd = false;
    private boolean _cleanUpAfterFinish;

    public ModelBasedResultSetIterator(PlatformImplBase platform, Database model, ResultSet resultSet, Table[] queryHints, boolean cleanUpAfterFinish) throws DatabaseOperationException {
        if (resultSet != null) {
            this._platform = platform;
            this._resultSet = resultSet;
            this._cleanUpAfterFinish = cleanUpAfterFinish;
            this._caseSensitive = this._platform.isDelimitedIdentifierModeOn();
            this._preparedQueryHints = this.prepareQueryHints(queryHints);
            try {
                this.initFromMetaData(model);
            }
            catch (SQLException ex) {
                this.cleanUp();
                throw new DatabaseOperationException("Could not read the metadata of the result set", ex);
            }
        } else {
            this._isAtEnd = true;
        }
    }

    private void initFromMetaData(Database model) throws SQLException {
        ResultSetMetaData metaData = this._resultSet.getMetaData();
        String tableName = null;
        boolean singleKnownTable = true;
        int idx = 1;
        while (idx <= metaData.getColumnCount()) {
            Column column;
            String columnName = metaData.getColumnName(idx);
            String tableOfColumn = metaData.getTableName(idx);
            Table table = null;
            if (tableOfColumn != null && tableOfColumn.length() > 0) {
                if (tableOfColumn.startsWith("\"") && tableOfColumn.endsWith("\"") && tableOfColumn.length() > 1) {
                    tableOfColumn = tableOfColumn.substring(1, tableOfColumn.length() - 1);
                }
                table = model.findTable(tableOfColumn, this._caseSensitive);
            } else {
                table = (Table)this._preparedQueryHints.get(this._caseSensitive ? columnName : columnName.toLowerCase());
                String string = tableOfColumn = table == null ? null : table.getName();
            }
            if (tableName == null) {
                tableName = tableOfColumn;
            } else if (!tableName.equals(tableOfColumn)) {
                singleKnownTable = false;
            }
            String propName = columnName;
            if (table != null && (column = table.findColumn(columnName, this._caseSensitive)) != null) {
                propName = column.getName();
            }
            this._columnsToProperties.put(columnName, propName);
            ++idx;
        }
        if (singleKnownTable && tableName != null) {
            this._dynaClass = model.getDynaClassFor(tableName);
        } else {
            DynaProperty[] props = new DynaProperty[this._columnsToProperties.size()];
            int idx2 = 0;
            Iterator it = this._columnsToProperties.values().iterator();
            while (it.hasNext()) {
                props[idx2] = new DynaProperty((String)it.next());
                ++idx2;
            }
            this._dynaClass = new BasicDynaClass("result", BasicDynaBean.class, props);
        }
    }

    private Map prepareQueryHints(Table[] queryHints) {
        HashMap<String, Table> result = new HashMap<String, Table>();
        int tableIdx = 0;
        while (queryHints != null && tableIdx < queryHints.length) {
            int columnIdx = 0;
            while (columnIdx < queryHints[tableIdx].getColumnCount()) {
                String columnName = queryHints[tableIdx].getColumn(columnIdx).getName();
                if (!this._caseSensitive) {
                    columnName = columnName.toLowerCase();
                }
                if (!result.containsKey(columnName)) {
                    result.put(columnName, queryHints[tableIdx]);
                }
                ++columnIdx;
            }
            ++tableIdx;
        }
        return result;
    }


    public boolean hasNext() throws DatabaseOperationException {
        this.advanceIfNecessary();
        return !this._isAtEnd;
    }

    public Object next() throws DatabaseOperationException {
        this.advanceIfNecessary();
        if (this._isAtEnd) {
            throw new NoSuchElementException("No more elements in the resultset");
        }
        try {
            DynaBean bean = this._dynaClass.newInstance();
            Table table = null;
            if (bean instanceof SqlDynaBean) {
                SqlDynaClass dynaClass = (SqlDynaClass)((SqlDynaBean)bean).getDynaClass();
                table = dynaClass.getTable();
            }
            for (Object entryObject : this._columnsToProperties.entrySet()) {
                if(entryObject instanceof Map.Entry){
                    Map.Entry entry = (Map.Entry)entryObject;
                String columnName = (String)entry.getKey();
                String propName = (String)entry.getValue();
                Table curTable = table;
                if (curTable == null) {
                    curTable = (Table)this._preparedQueryHints.get(this._caseSensitive ? columnName : columnName.toLowerCase());
                }
                Object value = this._platform.getObjectFromResultSet(this._resultSet, columnName, curTable);
                bean.set(propName, value);
            }}
            this._needsAdvancing = true;
            return bean;
        }
        catch (Exception ex) {
            this.cleanUp();
            throw new DatabaseOperationException("Exception while reading the row from the resultset", ex);
        }
    }

    public void advance() {
        this.advanceIfNecessary();
        if (this._isAtEnd) {
            throw new NoSuchElementException("No more elements in the resultset");
        }
        this._needsAdvancing = true;
    }

    private void advanceIfNecessary() throws DatabaseOperationException {
        if (this._needsAdvancing && !this._isAtEnd) {
            try {
                this._isAtEnd = !this._resultSet.next();
                this._needsAdvancing = false;
            }
            catch (SQLException ex) {
                this.cleanUp();
                throw new DatabaseOperationException("Could not retrieve next row from result set", ex);
            }
            if (this._isAtEnd) {
                this.cleanUp();
            }
        }
    }


    public void remove() throws DatabaseOperationException {
        try {
            this._resultSet.deleteRow();
        }
        catch (SQLException ex) {
            this.cleanUp();
            throw new DatabaseOperationException("Failed to delete current row", ex);
        }
    }

    public void cleanUp() {
        if (this._cleanUpAfterFinish && this._resultSet != null) {
            Connection conn = null;
            try {
                Statement stmt = this._resultSet.getStatement();
                conn = stmt.getConnection();
                this._platform.closeStatement(stmt);
            }
            catch (SQLException sQLException) {
                // empty catch block
            }
            this._platform.returnConnection(conn);
            this._resultSet = null;
        }
    }

    protected void finalize() throws Throwable {
        this.cleanUp();
    }

    public boolean isConnectionOpen() {
        if (this._resultSet == null) {
            return false;
        }
        try {
            Statement stmt = this._resultSet.getStatement();
            Connection conn = stmt.getConnection();
            return !conn.isClosed();
        }
        catch (SQLException ex) {
            return false;
        }
    }
}

