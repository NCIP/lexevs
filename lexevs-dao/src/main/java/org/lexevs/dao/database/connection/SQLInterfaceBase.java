/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.lexevs.dao.database.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.LexGrid.managedobj.FindException;
import org.LexGrid.managedobj.ManagedObjIF;
import org.LexGrid.managedobj.jdbc.JDBCBaseService;
import org.LexGrid.managedobj.jdbc.JDBCConnectionDescriptor;
import org.LexGrid.managedobj.jdbc.JDBCConnectionPoolPolicy;
import org.LexGrid.util.sql.GenericSQLModifier;
import org.LexGrid.util.sql.lgTables.SQLTableUtilities;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.LgLoggerIF;
import org.lexevs.logging.LoggerFactory;
import org.lexevs.system.ResourceManager;

/**
 * This class manages the SQL connection pool that is used to access a database.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class SQLInterfaceBase extends JDBCBaseService {
    private GenericSQLModifier gSQLMod_;
    private boolean isAccess_;
    protected int useCount = 0;

    protected LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }

    public SQLInterfaceBase(String username, String password, String url, String driver)
            throws RuntimeException {
        getLogger().debug("Creating a SQL Interface to " + url);

        try {
            JDBCConnectionDescriptor desc = getConnectionDescriptor();
            try {
                desc.setDbDriver(driver);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("The driver for your sql connection was not found.  I tried to load "
                        + driver);
            }
            desc.setDbUid(username);
            desc.setDbPwd(password);
            desc.setAutoCommit(true);
            desc.setDbUrl(url);
            desc.setUseUTF8(true);
            desc.setAutoRetryFailedConnections(true);

            // Connection pool parameters
            JDBCConnectionPoolPolicy pol = getConnectionPoolPolicy();
            pol.maxActive = LexEvsServiceLocator.getInstance().getResourceManager().getSystemVariables().getMaxConnectionsPerDB();
            pol.maxIdle = -1;
            pol.maxWait = -1;
            pol.minEvictableIdleTimeMillis = -1;
            pol.numTestsPerEvictionRun = 1;
            pol.testOnBorrow = false;
            pol.testOnReturn = false;
            pol.testWhileIdle = false;
            pol.timeBetweenEvictionRunsMillis = -1;
            pol.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_BLOCK;
            desc.setPingSQL("Select CodingSchemeName from codingScheme where CodingSchemeName='foobar'");

            // I need to know this to generate proper queries.

            Connection conn = (Connection) getConnectionPool().borrowObject();

            String databaseName = conn.getMetaData().getDatabaseProductName();
            getConnectionPool().returnObject(conn);

            gSQLMod_ = new GenericSQLModifier(databaseName, false);
            if (gSQLMod_.getDatabaseType().equals("ACCESS")) {
                isAccess_ = true;
            } else {
                isAccess_ = false;
            }
        } catch (Exception e) {
            throw new RuntimeException("There was a problem initializing the connection to " + url, e);
        }
    }

    public PreparedStatement checkOutStatement(@SuppressWarnings("unused") String key) {
        throw new UnsupportedOperationException();
    }

    protected PreparedStatement getArbitraryStatement(String sql) throws SQLException {
        return super.checkOutPreparedStatement(sql);
    }

    public void closeUnusedConnections() {
        getConnectionPool().clear();
    }

    public String modifySQL(String query) {
        return gSQLMod_.modifySQL(query);
    }

    public boolean isAccess() {
        return isAccess_;
    }

    public String getKey() {
        return this.getConnectionDescriptor().getDbUrl();
    }

    protected SQLTableUtilities getSQLTableUtilities(String tablePrefix) throws Exception {
        SQLTableUtilities stu = new SQLTableUtilities(getConnectionPool(), tablePrefix);
        return stu;
    }

    // The following methods are all abstract, so they have to be here, but I
    // don't need them.
    @Override
    protected String getDbTableName() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected ManagedObjIF findByPrimaryKeyPrim(Object key) throws FindException {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Class getInstanceClass() {
        throw new UnsupportedOperationException();
    }

    public ManagedObjIF row2ManagedObj(ResultSet rs) throws SQLException {
        throw new UnsupportedOperationException();
    }
}