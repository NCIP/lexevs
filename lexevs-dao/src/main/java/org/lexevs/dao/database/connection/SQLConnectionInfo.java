
package org.lexevs.dao.database.connection;

import org.LexGrid.annotations.LgClientSideSafe;

/**
 * Holder class for sql connection information.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class SQLConnectionInfo {
    
    /** The username. */
    public String username;
    
    /** The password. */
    public String password;
    
    /** The server. */
    public String server;
    
    /** The driver. */
    public String driver;
    
    /** The db name. */
    public String dbName;
    
    /** The prefix. */
    public String prefix;

    /** The urn. */
    public String urn;
    
    /** The version. */
    public String version;

    /**
     * Gets the key.
     * 
     * @return the key
     */
    @LgClientSideSafe
    public String getKey() {
        return server;
    }
}