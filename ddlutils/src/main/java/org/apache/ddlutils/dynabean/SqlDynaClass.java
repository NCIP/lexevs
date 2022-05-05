/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.beanutils.BasicDynaClass
 *  org.apache.commons.beanutils.DynaProperty
 */
package org.apache.ddlutils.dynabean;

import java.util.ArrayList;
import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.ddlutils.dynabean.SqlDynaBean;
import org.apache.ddlutils.dynabean.SqlDynaProperty;
import org.apache.ddlutils.model.Table;

public class SqlDynaClass
extends BasicDynaClass {
    private static final long serialVersionUID = -5768155698352911245L;
    private Table _table;
    private SqlDynaProperty[] _primaryKeyProperties;
    private SqlDynaProperty[] _nonPrimaryKeyProperties;

    public static SqlDynaClass newInstance(Table table) {
        ArrayList<SqlDynaProperty> properties = new ArrayList<SqlDynaProperty>();
        int idx = 0;
        while (idx < table.getColumnCount()) {
            properties.add(new SqlDynaProperty(table.getColumn(idx)));
            ++idx;
        }
        SqlDynaProperty[] array = new SqlDynaProperty[properties.size()];
        properties.toArray(array);
        return new SqlDynaClass(table, array);
    }

    public SqlDynaClass(Table table, SqlDynaProperty[] properties) {
        super(table.getName(), SqlDynaBean.class, (DynaProperty[])properties);
        this._table = table;
    }

    public Table getTable() {
        return this._table;
    }

    public String getTableName() {
        return this.getTable().getName();
    }

    public SqlDynaProperty[] getSqlDynaProperties() {
        return (SqlDynaProperty[])this.getDynaProperties();
    }

    public SqlDynaProperty[] getPrimaryKeyProperties() {
        if (this._primaryKeyProperties == null) {
            this.initPrimaryKeys();
        }
        return this._primaryKeyProperties;
    }

    public SqlDynaProperty[] getNonPrimaryKeyProperties() {
        if (this._nonPrimaryKeyProperties == null) {
            this.initPrimaryKeys();
        }
        return this._nonPrimaryKeyProperties;
    }

    protected void initPrimaryKeys() {
        ArrayList<SqlDynaProperty> pkProps = new ArrayList<SqlDynaProperty>();
        ArrayList<SqlDynaProperty> nonPkProps = new ArrayList<SqlDynaProperty>();
        DynaProperty[] properties = this.getDynaProperties();
        int idx = 0;
        while (idx < properties.length) {
            if (properties[idx] instanceof SqlDynaProperty) {
                SqlDynaProperty sqlProperty = (SqlDynaProperty)properties[idx];
                if (sqlProperty.isPrimaryKey()) {
                    pkProps.add(sqlProperty);
                } else {
                    nonPkProps.add(sqlProperty);
                }
            }
            ++idx;
        }
        this._primaryKeyProperties = pkProps.toArray(new SqlDynaProperty[pkProps.size()]);
        this._nonPrimaryKeyProperties = nonPkProps.toArray(new SqlDynaProperty[nonPkProps.size()]);
    }
}

