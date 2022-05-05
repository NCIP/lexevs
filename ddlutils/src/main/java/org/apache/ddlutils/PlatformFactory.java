/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.ddlutils.DdlUtilsException;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.PlatformUtils;
import org.apache.ddlutils.platform.axion.AxionPlatform;
import org.apache.ddlutils.platform.cloudscape.CloudscapePlatform;
import org.apache.ddlutils.platform.db2.Db2Platform;
import org.apache.ddlutils.platform.db2.Db2v8Platform;
import org.apache.ddlutils.platform.derby.DerbyPlatform;
import org.apache.ddlutils.platform.firebird.FirebirdPlatform;
import org.apache.ddlutils.platform.hsqldb.HsqlDbPlatform;
import org.apache.ddlutils.platform.interbase.InterbasePlatform;
import org.apache.ddlutils.platform.maxdb.MaxDbPlatform;
import org.apache.ddlutils.platform.mckoi.MckoiPlatform;
import org.apache.ddlutils.platform.mssql.MSSqlPlatform;
import org.apache.ddlutils.platform.mysql.MySql50Platform;
import org.apache.ddlutils.platform.mysql.MySqlPlatform;
import org.apache.ddlutils.platform.oracle.Oracle10Platform;
import org.apache.ddlutils.platform.oracle.Oracle8Platform;
import org.apache.ddlutils.platform.oracle.Oracle9Platform;
import org.apache.ddlutils.platform.postgresql.PostgreSqlPlatform;
import org.apache.ddlutils.platform.sapdb.SapDbPlatform;
import org.apache.ddlutils.platform.sybase.SybaseASE15Platform;
import org.apache.ddlutils.platform.sybase.SybasePlatform;

public class PlatformFactory {
    private static Map<String,Object> _platforms = null;

    private static synchronized Map<String,Object> getPlatforms() {
        if (_platforms == null) {
            _platforms = new HashMap();
            PlatformFactory.registerPlatforms();
        }
        return _platforms;
    }

    public static synchronized Platform createNewPlatformInstance(String databaseName) throws DdlUtilsException {
        Class platformClass = (Class)PlatformFactory.getPlatforms().get(databaseName.toLowerCase());
        try {
            return platformClass != null ? (Platform)platformClass.newInstance() : null;
        }
        catch (Exception ex) {
            throw new DdlUtilsException("Could not create platform for database " + databaseName, ex);
        }
    }

    public static synchronized Platform createNewPlatformInstance(String jdbcDriver, String jdbcConnectionUrl) throws DdlUtilsException {
        return PlatformFactory.createNewPlatformInstance(new PlatformUtils().determineDatabaseType(jdbcDriver, jdbcConnectionUrl));
    }

    public static synchronized Platform createNewPlatformInstance(DataSource dataSource) throws DdlUtilsException {
        Platform platform = PlatformFactory.createNewPlatformInstance(new PlatformUtils().determineDatabaseType(dataSource));
        platform.setDataSource(dataSource);
        return platform;
    }

    public static synchronized Platform createNewPlatformInstance(DataSource dataSource, String username, String password) throws DdlUtilsException {
        Platform platform = PlatformFactory.createNewPlatformInstance(new PlatformUtils().determineDatabaseType(dataSource, username, password));
        platform.setDataSource(dataSource);
        platform.setUsername(username);
        platform.setPassword(password);
        return platform;
    }

    public static synchronized String[] getSupportedPlatforms() {
        return PlatformFactory.getPlatforms().keySet().toArray(new String[0]);
    }

    public static boolean isPlatformSupported(String platformName) {
        return PlatformFactory.getPlatforms().containsKey(platformName.toLowerCase());
    }

    public static synchronized void registerPlatform(String platformName, Class platformClass) {
        PlatformFactory.addPlatform(PlatformFactory.getPlatforms(), platformName, platformClass);
    }

    private static void registerPlatforms() {
        PlatformFactory.addPlatform(_platforms, "Axion", AxionPlatform.class);
        PlatformFactory.addPlatform(_platforms, "Cloudscape", CloudscapePlatform.class);
        PlatformFactory.addPlatform(_platforms, "DB2", Db2Platform.class);
        PlatformFactory.addPlatform(_platforms, "DB2v8", Db2v8Platform.class);
        PlatformFactory.addPlatform(_platforms, "Derby", DerbyPlatform.class);
        PlatformFactory.addPlatform(_platforms, "Firebird", FirebirdPlatform.class);
        PlatformFactory.addPlatform(_platforms, "HsqlDb", HsqlDbPlatform.class);
        PlatformFactory.addPlatform(_platforms, "Interbase", InterbasePlatform.class);
        PlatformFactory.addPlatform(_platforms, "MaxDB", MaxDbPlatform.class);
        PlatformFactory.addPlatform(_platforms, "McKoi", MckoiPlatform.class);
        PlatformFactory.addPlatform(_platforms, "MsSql", MSSqlPlatform.class);
        PlatformFactory.addPlatform(_platforms, "MySQL", MySqlPlatform.class);
        PlatformFactory.addPlatform(_platforms, "MySQL5", MySql50Platform.class);
        PlatformFactory.addPlatform(_platforms, "Oracle", Oracle8Platform.class);
        PlatformFactory.addPlatform(_platforms, "Oracle9", Oracle9Platform.class);
        PlatformFactory.addPlatform(_platforms, "Oracle10", Oracle10Platform.class);
        PlatformFactory.addPlatform(_platforms, "PostgreSql", PostgreSqlPlatform.class);
        PlatformFactory.addPlatform(_platforms, "SapDB", SapDbPlatform.class);
        PlatformFactory.addPlatform(_platforms, "Sybase", SybasePlatform.class);
        PlatformFactory.addPlatform(_platforms, "SybaseASE15", SybaseASE15Platform.class);
    }

    private static synchronized void addPlatform(Map platformMap, String platformName, Class platformClass) {
        if (!Platform.class.isAssignableFrom(platformClass)) {
            throw new IllegalArgumentException("Cannot register class " + platformClass.getName() + " because it does not implement the " + Platform.class.getName() + " interface");
        }
        platformMap.put(platformName.toLowerCase(), platformClass);
    }
}

