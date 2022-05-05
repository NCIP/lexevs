/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.ddlutils.DatabaseOperationException;

public class PlatformUtils {
    public static final String JDBC_DRIVER_DATADIRECT_DB2 = "com.ddtek.jdbc.db2.DB2Driver";
    public static final String JDBC_DRIVER_DATADIRECT_SQLSERVER = "com.ddtek.jdbc.sqlserver.SQLServerDriver";
    public static final String JDBC_DRIVER_DATADIRECT_ORACLE = "com.ddtek.jdbc.oracle.OracleDriver";
    public static final String JDBC_DRIVER_DATADIRECT_SYBASE = "com.ddtek.jdbc.sybase.SybaseDriver";
    public static final String JDBC_DRIVER_INET_DB2 = "com.inet.drda.DRDADriver";
    public static final String JDBC_DRIVER_INET_ORACLE = "com.inet.ora.OraDriver";
    public static final String JDBC_DRIVER_INET_SQLSERVER = "com.inet.tds.TdsDriver";
    public static final String JDBC_DRIVER_INET_SYBASE = "com.inet.syb.SybDriver";
    public static final String JDBC_DRIVER_INET_POOLED = "com.inet.pool.PoolDriver";
    public static final String JDBC_DRIVER_JSQLCONNECT_SQLSERVER = "com.jnetdirect.jsql.JSQLDriver";
    public static final String JDBC_DRIVER_JTDS = "net.sourceforge.jtds.jdbc.Driver";
    public static final String JDBC_SUBPROTOCOL_DATADIRECT_DB2 = "datadirect:db2";
    public static final String JDBC_SUBPROTOCOL_DATADIRECT_SQLSERVER = "datadirect:sqlserver";
    public static final String JDBC_SUBPROTOCOL_DATADIRECT_ORACLE = "datadirect:oracle";
    public static final String JDBC_SUBPROTOCOL_DATADIRECT_SYBASE = "datadirect:sybase";
    public static final String JDBC_SUBPROTOCOL_INET_DB2 = "inetdb2";
    public static final String JDBC_SUBPROTOCOL_INET_ORACLE = "inetora";
    public static final String JDBC_SUBPROTOCOL_INET_SQLSERVER = "inetdae";
    public static final String JDBC_SUBPROTOCOL_INET_SQLSERVER6 = "inetdae6";
    public static final String JDBC_SUBPROTOCOL_INET_SQLSERVER7 = "inetdae7";
    public static final String JDBC_SUBPROTOCOL_INET_SQLSERVER7A = "inetdae7a";
    public static final String JDBC_SUBPROTOCOL_INET_SQLSERVER_POOLED_1 = "inetpool:inetdae";
    public static final String JDBC_SUBPROTOCOL_INET_SQLSERVER6_POOLED_1 = "inetpool:inetdae6";
    public static final String JDBC_SUBPROTOCOL_INET_SQLSERVER7_POOLED_1 = "inetpool:inetdae7";
    public static final String JDBC_SUBPROTOCOL_INET_SQLSERVER7A_POOLED_1 = "inetpool:inetdae7a";
    public static final String JDBC_SUBPROTOCOL_INET_SQLSERVER_POOLED_2 = "inetpool:jdbc:inetdae";
    public static final String JDBC_SUBPROTOCOL_INET_SQLSERVER6_POOLED_2 = "inetpool:jdbc:inetdae6";
    public static final String JDBC_SUBPROTOCOL_INET_SQLSERVER7_POOLED_2 = "inetpool:jdbc:inetdae7";
    public static final String JDBC_SUBPROTOCOL_INET_SQLSERVER7A_POOLED_2 = "inetpool:jdbc:inetdae7a";
    public static final String JDBC_SUBPROTOCOL_INET_SYBASE = "inetsyb";
    public static final String JDBC_SUBPROTOCOL_INET_SYBASE_POOLED_1 = "inetpool:inetsyb";
    public static final String JDBC_SUBPROTOCOL_INET_SYBASE_POOLED_2 = "inetpool:jdbc:inetsyb";
    public static final String JDBC_SUBPROTOCOL_JSQLCONNECT_SQLSERVER = "JSQLConnect";
    public static final String JDBC_SUBPROTOCOL_JTDS_SQLSERVER = "jtds:sqlserver";
    public static final String JDBC_SUBPROTOCOL_JTDS_SYBASE = "jtds:sybase";
    private HashMap jdbcSubProtocolToPlatform = new HashMap();
    private HashMap jdbcDriverToPlatform = new HashMap();

    public PlatformUtils() {
        this.jdbcSubProtocolToPlatform.put("axiondb", "Axion");
        this.jdbcSubProtocolToPlatform.put("db2j:net", "Cloudscape");
        this.jdbcSubProtocolToPlatform.put("cloudscape:net", "Cloudscape");
        this.jdbcSubProtocolToPlatform.put("db2", "DB2");
        this.jdbcSubProtocolToPlatform.put("db2os390", "DB2");
        this.jdbcSubProtocolToPlatform.put("db2os390sqlj", "DB2");
        this.jdbcSubProtocolToPlatform.put("as400", "DB2");
        this.jdbcSubProtocolToPlatform.put(JDBC_SUBPROTOCOL_DATADIRECT_DB2, "DB2");
        this.jdbcSubProtocolToPlatform.put(JDBC_SUBPROTOCOL_INET_DB2, "DB2");
        this.jdbcSubProtocolToPlatform.put("derby", "Derby");
        this.jdbcSubProtocolToPlatform.put("firebirdsql", "Firebird");
        this.jdbcSubProtocolToPlatform.put("hsqldb", "HsqlDb");
        this.jdbcSubProtocolToPlatform.put("interbase", "Interbase");
        this.jdbcSubProtocolToPlatform.put("sapdb", "SapDB");
        this.jdbcSubProtocolToPlatform.put("mckoi", "McKoi");
        this.jdbcSubProtocolToPlatform.put("microsoft:sqlserver", "MsSql");
        this.jdbcSubProtocolToPlatform.put("sqlserver", "MsSql");
        this.jdbcSubProtocolToPlatform.put("sqljdbc", "MsSql");
        this.jdbcSubProtocolToPlatform.put(JDBC_SUBPROTOCOL_DATADIRECT_SQLSERVER, "MsSql");
        this.jdbcSubProtocolToPlatform.put(JDBC_SUBPROTOCOL_INET_SQLSERVER, "MsSql");
        this.jdbcSubProtocolToPlatform.put(JDBC_SUBPROTOCOL_INET_SQLSERVER6, "MsSql");
        this.jdbcSubProtocolToPlatform.put(JDBC_SUBPROTOCOL_INET_SQLSERVER7, "MsSql");
        this.jdbcSubProtocolToPlatform.put(JDBC_SUBPROTOCOL_INET_SQLSERVER7A, "MsSql");
        this.jdbcSubProtocolToPlatform.put(JDBC_SUBPROTOCOL_INET_SQLSERVER_POOLED_1, "MsSql");
        this.jdbcSubProtocolToPlatform.put(JDBC_SUBPROTOCOL_INET_SQLSERVER6_POOLED_1, "MsSql");
        this.jdbcSubProtocolToPlatform.put(JDBC_SUBPROTOCOL_INET_SQLSERVER7_POOLED_1, "MsSql");
        this.jdbcSubProtocolToPlatform.put(JDBC_SUBPROTOCOL_INET_SQLSERVER7A_POOLED_1, "MsSql");
        this.jdbcSubProtocolToPlatform.put(JDBC_SUBPROTOCOL_INET_SQLSERVER_POOLED_2, "MsSql");
        this.jdbcSubProtocolToPlatform.put(JDBC_SUBPROTOCOL_INET_SQLSERVER6_POOLED_2, "MsSql");
        this.jdbcSubProtocolToPlatform.put(JDBC_SUBPROTOCOL_INET_SQLSERVER7_POOLED_2, "MsSql");
        this.jdbcSubProtocolToPlatform.put(JDBC_SUBPROTOCOL_INET_SQLSERVER7A_POOLED_2, "MsSql");
        this.jdbcSubProtocolToPlatform.put(JDBC_SUBPROTOCOL_JSQLCONNECT_SQLSERVER, "MsSql");
        this.jdbcSubProtocolToPlatform.put(JDBC_SUBPROTOCOL_JTDS_SQLSERVER, "MsSql");
        this.jdbcSubProtocolToPlatform.put("mysql", "MySQL");
        this.jdbcSubProtocolToPlatform.put("oracle:thin", "Oracle");
        this.jdbcSubProtocolToPlatform.put("oracle:oci8", "Oracle");
        this.jdbcSubProtocolToPlatform.put("oracle:dnldthin", "Oracle");
        this.jdbcSubProtocolToPlatform.put(JDBC_SUBPROTOCOL_DATADIRECT_ORACLE, "Oracle");
        this.jdbcSubProtocolToPlatform.put(JDBC_SUBPROTOCOL_INET_ORACLE, "Oracle");
        this.jdbcSubProtocolToPlatform.put("postgresql", "PostgreSql");
        this.jdbcSubProtocolToPlatform.put("sybase:Tds", "Sybase");
        this.jdbcSubProtocolToPlatform.put(JDBC_SUBPROTOCOL_DATADIRECT_SYBASE, "Sybase");
        this.jdbcSubProtocolToPlatform.put(JDBC_SUBPROTOCOL_INET_SYBASE, "Sybase");
        this.jdbcSubProtocolToPlatform.put(JDBC_SUBPROTOCOL_INET_SYBASE_POOLED_1, "Sybase");
        this.jdbcSubProtocolToPlatform.put(JDBC_SUBPROTOCOL_INET_SYBASE_POOLED_2, "Sybase");
        this.jdbcSubProtocolToPlatform.put(JDBC_SUBPROTOCOL_JTDS_SYBASE, "Sybase");
        this.jdbcDriverToPlatform.put("org.axiondb.jdbc.AxionDriver", "Axion");
        this.jdbcDriverToPlatform.put("com.ibm.db2.jcc.DB2Driver", "DB2");
        this.jdbcDriverToPlatform.put("COM.ibm.db2.jdbc.app.DB2Driver", "DB2");
        this.jdbcDriverToPlatform.put("COM.ibm.db2os390.sqlj.jdbc.DB2SQLJDriver", "DB2");
        this.jdbcDriverToPlatform.put("com.ibm.as400.access.AS400JDBCDriver", "DB2");
        this.jdbcDriverToPlatform.put(JDBC_DRIVER_DATADIRECT_DB2, "DB2");
        this.jdbcDriverToPlatform.put(JDBC_DRIVER_INET_DB2, "DB2");
        this.jdbcDriverToPlatform.put("org.apache.derby.jdbc.EmbeddedDriver", "Derby");
        this.jdbcDriverToPlatform.put("org.apache.derby.jdbc.ClientDriver", "Derby");
        this.jdbcDriverToPlatform.put("org.firebirdsql.jdbc.FBDriver", "Firebird");
        this.jdbcDriverToPlatform.put("org.hsqldb.jdbcDriver", "HsqlDb");
        this.jdbcDriverToPlatform.put("interbase.interclient.Driver", "Interbase");
        this.jdbcDriverToPlatform.put("com.sap.dbtech.jdbc.DriverSapDB", "SapDB");
        this.jdbcDriverToPlatform.put("com.mckoi.JDBCDriver", "McKoi");
        this.jdbcDriverToPlatform.put("com.microsoft.jdbc.sqlserver.SQLServerDriver", "MsSql");
        this.jdbcDriverToPlatform.put("com.microsoft.sqlserver.jdbc.SQLServerDriver", "MsSql");
        this.jdbcDriverToPlatform.put(JDBC_DRIVER_DATADIRECT_SQLSERVER, "MsSql");
        this.jdbcDriverToPlatform.put(JDBC_DRIVER_INET_SQLSERVER, "MsSql");
        this.jdbcDriverToPlatform.put(JDBC_DRIVER_JSQLCONNECT_SQLSERVER, "MsSql");
        this.jdbcDriverToPlatform.put("com.mysql.jdbc.Driver", "MySQL");
        this.jdbcDriverToPlatform.put("org.gjt.mm.mysql.Driver", "MySQL");
        this.jdbcDriverToPlatform.put("oracle.jdbc.driver.OracleDriver", "Oracle");
        this.jdbcDriverToPlatform.put("oracle.jdbc.dnlddriver.OracleDriver", "Oracle");
        this.jdbcDriverToPlatform.put(JDBC_DRIVER_DATADIRECT_ORACLE, "Oracle");
        this.jdbcDriverToPlatform.put(JDBC_DRIVER_INET_ORACLE, "Oracle");
        this.jdbcDriverToPlatform.put("org.postgresql.Driver", "PostgreSql");
        this.jdbcDriverToPlatform.put("com.sybase.jdbc2.jdbc.SybDriver", "Sybase");
        this.jdbcDriverToPlatform.put("com.sybase.jdbc.SybDriver", "Sybase");
        this.jdbcDriverToPlatform.put(JDBC_DRIVER_DATADIRECT_SYBASE, "Sybase");
        this.jdbcDriverToPlatform.put(JDBC_DRIVER_INET_SYBASE, "Sybase");
    }

    public String determineDatabaseType(DataSource dataSource) throws DatabaseOperationException {
        return this.determineDatabaseType(dataSource, null, null);
    }

    public String determineDatabaseType(DataSource dataSource, String username, String password) throws DatabaseOperationException {
        Connection connection = null;
        try {
            connection = username != null ? dataSource.getConnection(username, password) : dataSource.getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            String string = this.determineDatabaseType(metaData.getDriverName(), metaData.getURL());
            return string;
        }
        catch (SQLException ex) {
            throw new DatabaseOperationException("Error while reading the database metadata: " + ex.getMessage(), ex);
        }
        finally {
            if (connection != null) {
                try {
                    connection.close();
                }
                catch (SQLException sQLException) {}
            }
        }
    }

    public String determineDatabaseType(String driverName, String jdbcConnectionUrl) {
        if (this.jdbcDriverToPlatform.containsKey(driverName)) {
            return (String)this.jdbcDriverToPlatform.get(driverName);
        }
        if (jdbcConnectionUrl == null) {
            return null;
        }
        for (Object entryObject : this.jdbcSubProtocolToPlatform.entrySet()) {
            if(entryObject instanceof Map.Entry){
                Map.Entry entry = (Map.Entry)entryObject;
            String curSubProtocol = "jdbc:" + (String)entry.getKey() + ":";
            if (!jdbcConnectionUrl.startsWith(curSubProtocol)) continue;
            return (String)entry.getValue();
        }}
        return null;
    }
}

