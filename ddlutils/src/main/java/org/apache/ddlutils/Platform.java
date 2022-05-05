/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.beanutils.DynaBean
 */
package org.apache.ddlutils;

import java.sql.Connection;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.beanutils.DynaBean;
import org.apache.ddlutils.DatabaseOperationException;
import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.CreationParameters;
import org.apache.ddlutils.platform.JdbcModelReader;
import org.apache.ddlutils.platform.SqlBuilder;

public interface Platform {
    public String getName();

    public PlatformInfo getPlatformInfo();

    public SqlBuilder getSqlBuilder();

    public JdbcModelReader getModelReader();

    public DataSource getDataSource();

    public void setDataSource(DataSource var1);

    public String getUsername();

    public void setUsername(String var1);

    public String getPassword();

    public void setPassword(String var1);

    public boolean isScriptModeOn();

    public void setScriptModeOn(boolean var1);

    public boolean isDelimitedIdentifierModeOn();

    public void setDelimitedIdentifierModeOn(boolean var1);

    public boolean isSqlCommentsOn();

    public void setSqlCommentsOn(boolean var1);

    public boolean isIdentityOverrideOn();

    public void setIdentityOverrideOn(boolean var1);

    public boolean isForeignKeysSorted();

    public void setForeignKeysSorted(boolean var1);

    public Connection borrowConnection() throws DatabaseOperationException;

    public void returnConnection(Connection var1);

    public int evaluateBatch(String var1, boolean var2) throws DatabaseOperationException;

    public int evaluateBatch(Connection var1, String var2, boolean var3) throws DatabaseOperationException;

    public void shutdownDatabase() throws DatabaseOperationException;

    public void shutdownDatabase(Connection var1) throws DatabaseOperationException;

    public void createDatabase(String var1, String var2, String var3, String var4, Map<String,String> var5) throws DatabaseOperationException, UnsupportedOperationException;

    public void dropDatabase(String var1, String var2, String var3, String var4) throws DatabaseOperationException, UnsupportedOperationException;

    public void createTables(Database var1, boolean var2, boolean var3) throws DatabaseOperationException;

    public void createTables(Connection var1, Database var2, boolean var3, boolean var4) throws DatabaseOperationException;

    public String getCreateTablesSql(Database var1, boolean var2, boolean var3);

    public void createTables(Database var1, CreationParameters var2, boolean var3, boolean var4) throws DatabaseOperationException;

    public void createTables(Connection var1, Database var2, CreationParameters var3, boolean var4, boolean var5) throws DatabaseOperationException;

    public String getCreateTablesSql(Database var1, CreationParameters var2, boolean var3, boolean var4);

    public void alterTables(Database var1, boolean var2) throws DatabaseOperationException;

    public String getAlterTablesSql(Database var1) throws DatabaseOperationException;

    public void alterTables(Database var1, CreationParameters var2, boolean var3) throws DatabaseOperationException;

    public String getAlterTablesSql(Database var1, CreationParameters var2) throws DatabaseOperationException;

    public void alterTables(String var1, String var2, String[] var3, Database var4, boolean var5) throws DatabaseOperationException;

    public String getAlterTablesSql(String var1, String var2, String[] var3, Database var4) throws DatabaseOperationException;

    public void alterTables(String var1, String var2, String[] var3, Database var4, CreationParameters var5,
                            boolean var6) throws DatabaseOperationException;

    public String getAlterTablesSql(String var1, String var2, String[] var3, Database var4, CreationParameters var5) throws DatabaseOperationException;

    public void alterTables(Connection var1, Database var2, boolean var3) throws DatabaseOperationException;

    public String getAlterTablesSql(Connection var1, Database var2) throws DatabaseOperationException;

    public void alterTables(Connection var1, Database var2, CreationParameters var3, boolean var4) throws DatabaseOperationException;

    public String getAlterTablesSql(Connection var1, Database var2, CreationParameters var3) throws DatabaseOperationException;

    public void alterTables(Connection var1, String var2, String var3, String[] var4, Database var5, boolean var6) throws DatabaseOperationException;

    public String getAlterTablesSql(Connection var1, String var2, String var3, String[] var4, Database var5) throws DatabaseOperationException;

    public void alterTables(Connection var1, String var2, String var3, String[] var4, Database var5, CreationParameters var6, boolean var7) throws DatabaseOperationException;

    public String getAlterTablesSql(Connection var1, String var2, String var3, String[] var4, Database var5, CreationParameters var6) throws DatabaseOperationException;

    public void dropTable(Database var1, Table var2, boolean var3) throws DatabaseOperationException;

    public String getDropTableSql(Database var1, Table var2, boolean var3);

    public void dropTable(Connection var1, Database var2, Table var3, boolean var4) throws DatabaseOperationException;

    public void dropTables(Database var1, boolean var2) throws DatabaseOperationException;

    public String getDropTablesSql(Database var1, boolean var2);

    public void dropTables(Connection var1, Database var2, boolean var3) throws DatabaseOperationException;

    public Iterator query(Database var1, String var2) throws DatabaseOperationException;

    public Iterator query(Database var1, String var2, Collection var3) throws DatabaseOperationException;

    public Iterator query(Database var1, String var2, Table[] var3) throws DatabaseOperationException;

    public Iterator query(Database var1, String var2, Collection var3, Table[] var4) throws DatabaseOperationException;

    public List fetch(Database var1, String var2) throws DatabaseOperationException;

    public List fetch(Database var1, String var2, Collection var3) throws DatabaseOperationException;

    public List fetch(Database var1, String var2, Table[] var3) throws DatabaseOperationException;

    public List fetch(Database var1, String var2, Collection var3, Table[] var4) throws DatabaseOperationException;

    public List fetch(Database var1, String var2, int var3, int var4) throws DatabaseOperationException;

    public List fetch(Database var1, String var2, Collection var3, int var4, int var5) throws DatabaseOperationException;

    public List fetch(Database var1, String var2, Table[] var3, int var4, int var5) throws DatabaseOperationException;

    public List fetch(Database var1, String var2, Collection var3, Table[] var4, int var5, int var6) throws DatabaseOperationException;

    public void store(Database var1, DynaBean var2) throws DatabaseOperationException;

    public String getInsertSql(Database var1, DynaBean var2);

    public void insert(Database var1, DynaBean var2) throws DatabaseOperationException;

    public void insert(Connection var1, Database var2, DynaBean var3) throws DatabaseOperationException;

    public void insert(Database var1, Collection var2) throws DatabaseOperationException;

    public void insert(Connection var1, Database var2, Collection<DynaBean> var3) throws DatabaseOperationException;

    public String getUpdateSql(Database var1, DynaBean var2);

    public void update(Database var1, DynaBean var2) throws DatabaseOperationException;

    public void update(Connection var1, Database var2, DynaBean var3) throws DatabaseOperationException;

    public String getDeleteSql(Database var1, DynaBean var2);

    public void delete(Database var1, DynaBean var2) throws DatabaseOperationException;

    public void delete(Connection var1, Database var2, DynaBean var3) throws DatabaseOperationException;

    public Database readModelFromDatabase(String var1) throws DatabaseOperationException;

    public Database readModelFromDatabase(String var1, String var2, String var3, String[] var4) throws DatabaseOperationException;

    public Database readModelFromDatabase(Connection var1, String var2) throws DatabaseOperationException;

    public Database readModelFromDatabase(Connection var1, String var2, String var3, String var4, String[] var5) throws DatabaseOperationException;
}

