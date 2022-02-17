
package org.LexGrid.LexBIG.Impl.testUtility;

/**
 * Holder object for helping to configure the JUnit tests.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class TestServerConfig {
    public String url;
    public String driver;
    public String username;
    public String password;
    public String prefix;
    public String param;
    public boolean singleDBMode;
    public boolean overrideSingleDBMode = false;
}