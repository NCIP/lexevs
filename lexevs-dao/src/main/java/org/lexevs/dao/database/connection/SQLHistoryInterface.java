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
import java.sql.SQLException;

import org.LexGrid.util.sql.lgTables.SQLTableUtilities;

/**
 * Code to access the specific sql tables for a terminology. Multiple instances
 * of this class may use the same SQLInterfaceBase (in SINGLE_DB_MODE)
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class SQLHistoryInterface {
    private SQLTableUtilities sqlTableUtilities;
    private SQLInterfaceBase sib_;
    private String tablePrefix_;

    public SQLHistoryInterface(SQLInterfaceBase sib, String tablePrefix) {
        try {
            sib_ = sib;
            tablePrefix_ = tablePrefix;
            sqlTableUtilities = sib_.getSQLTableUtilities(tablePrefix_);
            sib_.useCount++;
        } catch (Exception e) {
            throw new RuntimeException("Problem stting up the SQLInterface", e);
        }
    }

    public boolean isAccess() {
        return sib_.isAccess();
    }

    public String getTableName(String tableKey) {
        return sqlTableUtilities.getSQLTableConstants().getTableName(tableKey);
    }

    public PreparedStatement checkOutPreparedStatement(String sql) throws SQLException {
        return sib_.getArbitraryStatement(sql);
    }

    public void checkInPreparedStatement(PreparedStatement statement) {
        sib_.checkInPreparedStatement(statement);
    }

    public String getKey() {
        return sib_.getKey() + ":" + tablePrefix_;
    }

    public String getConnectionKey() {
        return sib_.getKey();
    }

    public void dropTables() throws SQLException {
        sqlTableUtilities.dropTables();
    }

    /**
     * Close this interface - if no other interfaces are using the underlying db
     * connection, close that too. return true if we closed the underlying db
     * connection. False if not.
     * 
     * @return
     */
    public boolean close() {
        boolean closedDBConnection = false;
        sib_.useCount--;
        if (sib_.useCount == 0) {
            sib_.close();
            closedDBConnection = true;
        }

        tablePrefix_ = null;
        sqlTableUtilities = null;
        sib_ = null;
        return closedDBConnection;
    }

    /**
     * @return the sqlTableUtilities
     */
    public SQLTableUtilities getSQLTableUtilities() {
        return sqlTableUtilities;
    }

}