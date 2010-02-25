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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import javax.sql.DataSource;

import org.LexGrid.managedobj.jdbc.JDBCConnectionDescriptor;
import org.LexGrid.util.sql.GenericSQLModifier;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.util.sql.lgTables.SQLTableUtilities;
import org.lexevs.dao.database.type.DatabaseType;
import org.lexevs.logging.LgLoggerIF;
import org.lexevs.logging.LoggerFactory;

/**
 * Code to access the specific sql tables for a terminology. Multiple instances
 * of this class may use the same SQLInterfaceBase (in SINGLE_DB_MODE)
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class SQLInterface {
    private SQLTableUtilities stu_;
    private DataSource dataSource;
    private String tablePrefix_;
    private GenericSQLModifier gSQLMod_;

    protected LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }

    public SQLInterface(DataSource dataSource, DatabaseType databaseType, String tablePrefix) {
        try {
        	this.dataSource = dataSource;
            tablePrefix_ = tablePrefix;
           	stu_ = new SQLTableUtilities(dataSource, tablePrefix);
            gSQLMod_ = new GenericSQLModifier(databaseType.getProductName(), false);
        } catch (Exception e) {
            throw new RuntimeException("Problem setting up the SQLInterface", e);
        }
    }

    public boolean supports2009Model() {
        return stu_.getSQLTableConstants().supports2009Model();
    }

    public SQLTableConstants getSQLTableConstants() {
        return stu_.getSQLTableConstants();
    }

    public String getTableName(String tableKey) {
        return stu_.getSQLTableConstants().getTableName(tableKey);
    }

    public PreparedStatement modifyAndCheckOutPreparedStatement(String sql) throws SQLException {
        return dataSource.getConnection().prepareStatement(gSQLMod_.modifySQL(sql));
    }

    public PreparedStatement checkOutPreparedStatement(String sql) throws SQLException {
        return dataSource.getConnection().prepareStatement(
        		sql,
        		ResultSet.TYPE_FORWARD_ONLY,
    			ResultSet.CONCUR_READ_ONLY);
    }

    public void checkInPreparedStatement(PreparedStatement statement) {
    	
    	try {
    		if(statement != null){
    			statement.close();
    			statement.getConnection().close();
    		}
		} catch (SQLException e) {
			//
		} finally {
    		//
		}
    }

    public void dropTables() throws SQLException {
        stu_.dropTables();
    }

    /**
     * Return the table prefix that this SQL Interface is accessing.
     * 
     * @return
     */
    public String getTablePrefix() {
        return tablePrefix_;
    }

	public String getKey() {
		return tablePrefix_;
	}
}