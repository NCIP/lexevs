
package org.lexevs.dao.database.connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.util.sql.GenericSQLModifier;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.util.sql.lgTables.SQLTableUtilities;
import org.lexevs.dao.database.type.DatabaseType;
import org.lexevs.locator.LexEvsServiceLocator;
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
    
    /** The stu_. */
    private SQLTableUtilities stu_;
    
    /** The data source. */
    private DataSource dataSource;
    
    /** The table prefix_. */
    private String tablePrefix_;
    
    /** The g sql mod_. */
    private GenericSQLModifier gSQLMod_;
    
    /**
     * Gets the logger.
     * 
     * @return the logger
     */
    protected LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }

    /**
     * Instantiates a new sQL interface.
     * 
     * @param dataSource the data source
     * @param databaseType the database type
     * @param tablePrefix the table prefix
     */
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
    
    public SQLInterface(String codingSchemeUri, String version) {
    	this(
    			LexEvsServiceLocator.getInstance().getLexEvsDatabaseOperations().getDataSource(), 
    			LexEvsServiceLocator.getInstance().getLexEvsDatabaseOperations().getDatabaseType(), 
    			LexEvsServiceLocator.getInstance().getLexEvsDatabaseOperations().
    			getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUri, version));
     }
    
    /**
     * Gets the sQL table utilities.
     * 
     * @return the sQL table utilities
     */
    @Deprecated
    public SQLTableUtilities getSQLTableUtilities() {
    	return stu_;
    }

    /**
     * Supports2009 model.
     * 
     * @return true, if successful
     */
    public boolean supports2009Model() {
        return stu_.getSQLTableConstants().supports2009Model();
    }

    /**
     * Gets the sQL table constants.
     * 
     * @return the sQL table constants
     */
    public SQLTableConstants getSQLTableConstants() {
        return stu_.getSQLTableConstants();
    }

    /**
     * Gets the table name.
     * 
     * @param tableKey the table key
     * 
     * @return the table name
     */
    public String getTableName(String tableKey) {
        return stu_.getSQLTableConstants().getTableName(tableKey);
    }
    
   /**
     * Modify and check out prepared statement.
     * 
     * @param sql the sql
     * 
     * @return the prepared statement
     * 
     * @throws SQLException the SQL exception
     */
    public PreparedStatement modifyAndCheckOutPreparedStatement(String sql) throws SQLException {
        return dataSource.getConnection().prepareStatement(gSQLMod_.modifySQL(sql));
    }

    /**
     * Check out prepared statement.
     * 
     * @param sql the sql
     * 
     * @return the prepared statement
     * 
     * @throws SQLException the SQL exception
     */
    public PreparedStatement checkOutPreparedStatement(String sql) throws SQLException {
        return dataSource.getConnection().prepareStatement(
        		sql,
        		ResultSet.TYPE_FORWARD_ONLY,
    			ResultSet.CONCUR_READ_ONLY);
    }

    /**
     * Check in prepared statement.
     * 
     * @param statement the statement
     */
    public void checkInPreparedStatement(PreparedStatement statement) {
    	
    	try {
    		if(statement.getConnection() != null && (!statement.getConnection().isClosed()) ){
    			statement.getConnection().close();
    		} if(statement != null && (!statement.isClosed()) ) {
    			statement.close();
    		}
		} catch (Exception e) {
			try {
				if(statement != null && (!statement.isClosed())){
					statement.close();
	    		} if(statement.getConnection() != null && (!statement.getConnection().isClosed()) ) {
	    			statement.getConnection().close();
	    		}
			} catch (Exception e1) {
				//
			}
		}
    }

    /**
     * Drop tables.
     * 
     * @throws SQLException the SQL exception
     */
    public void dropTables() throws SQLException {
        stu_.dropTables();
    }

    /**
     * Return the table prefix that this SQL Interface is accessing.
     * 
     * @return the table prefix
     */
    public String getTablePrefix() {
        return tablePrefix_;
    }

	/**
	 * Gets the key.
	 * 
	 * @return the key
	 */
	public String getKey() {
		return tablePrefix_;
	}
}