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
import org.LexGrid.managedobj.jdbc.JDBCConnectionFactory;
import org.LexGrid.managedobj.jdbc.JDBCConnectionPool;
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
public class SQLInterfaceBase {
    private GenericSQLModifier gSQLMod_;
    protected int useCount = 0;
    protected JDBCConnectionDescriptor _dbDesc;
    private JDBCConnectionPoolPolicy _connPolicy = null;
    private JDBCConnectionPool _connPool = null;

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
            pol.maxActive = 5;
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
           
        } catch (Exception e) {
            throw new RuntimeException("There was a problem initializing the connection to " + url, e);
        }
    }

    protected PreparedStatement getArbitraryStatement(String sql) throws SQLException {
    	Connection c = null;
		try {
			c = (Connection) getConnectionPool().borrowObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				getConnectionPool().returnObject(c);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
    	return c.prepareStatement(sql);
    }


    public String modifySQL(String query) {
        return gSQLMod_.modifySQL(query);
    }
    
	public JDBCConnectionDescriptor getConnectionDescriptor() {
		if (_dbDesc == null)
			_dbDesc = new JDBCConnectionDescriptor();
		return _dbDesc;
	}
	
	/**
	 * Returns the policy used to manage the connection pool.
	 * @return JDBCConnectionPoolPolicy
	 */
	public JDBCConnectionPoolPolicy getConnectionPoolPolicy() {
		if (_connPolicy == null)
			_connPolicy = new JDBCConnectionPoolPolicy();
		return _connPolicy;
	}
	
	/**
	 * Returns an object pool used to manage JDBC connections.
	 * @return JDBCConnectionPool
	 */
	protected JDBCConnectionPool getConnectionPool() {
		if (_connPool == null)
			_connPool =
				new JDBCConnectionPool(
					new JDBCConnectionFactory(getConnectionDescriptor()),
					getConnectionPoolPolicy());
		return _connPool;
	}
}