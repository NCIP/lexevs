
package edu.mayo.informatics.lexgrid.convert.utility;

/**
 * Class to hold values used in multiple places in the converter.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 5296 $ checked in on $Date: 2007-05-16
 *          21:55:43 +0000 (Wed, 16 May 2007) $
 */
public class Constants {
    public static int mySqlBatchSize = 1000;
    public static int ldapPageSize = 5000;
    public final static String version = "3.1.2_prerelease";

    public final static String[] ldapServers = new String[] { "mir04.mayo.edu", "informatics.mayo.edu" };
    public final static String[] ldapReadUserNames = new String[] { "",
            "cn=public,dc=Users,service=userValidation,dc=LexGrid,dc=org", "cn=test,service=test,dc=LexGrid,dc=org" };
    public final static String[] ldapWriteUserNames = new String[] { "cn=test,service=test,dc=LexGrid,dc=org" };
    public final static int[] ldapPorts = new int[] { 31900, 34900 };
    public final static String[] sqlDrivers = new String[] { "sun.jdbc.odbc.JdbcOdbcDriver", "org.hsqldb.jdbcDriver",
            "org.gjt.mm.mysql.Driver", "org.postgresql.Driver", "com.ibm.db2.jcc.DB2Driver",
            "com.microsoft.jdbc.sqlserver.SQLServerDriver", "oracle.jdbc.driver.OracleDriver" };
    public final static String[] sqlLiteServers = new String[] {
            "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=C:\\Temp\\LexGridLite.mdb",
            "jdbc:hsqldb:file:C:/temp/LexGrid-hsqldbs/LexGridLite;shutdown=true;hsqldb.default_table_type=cached",
            "jdbc:mysql://localhost/LexGridLite", "jdbc:postgresql://localhost/LexGridLite",
            "jdbc:db2://localhost:50000/LexGridLite",
            "jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=LexGridLite",
            "jdbc:oracle:thin:@localhost:1521:LEXGRID" };
    public final static String[] sqlServers = new String[] {
            "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=C:\\Temp\\LexGrid.mdb",
            "jdbc:hsqldb:file:C:/temp/LexGrid-hsqldbs/LexGrid;shutdown=true;hsqldb.default_table_type=cached",
            "jdbc:mysql://localhost/LexGrid", "jdbc:postgresql://localhost/LexGrid",
            "jdbc:db2://localhost:50000/LexGrid", "jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=LexGrid",
            "jdbc:oracle:thin:@localhost:1521:LEXGRID" };

    public final static String[] umlsServers = new String[] {
            "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=C:\\Temp\\UMLS2006AB.mdb",
            "jdbc:hsqldb:file:C:/temp/LexGrid-hsqldbs/UMLS2006AB;shutdown=true;hsqldb.default_table_type=cached",
            "jdbc:mysql://localhost/UMLS2006AB", "jdbc:postgresql://localhost/UMLS2006AB",
            "jdbc:db2://localhost:50000/UMLS2006AB",
            "jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=UMLS2006AB",
            "jdbc:oracle:thin:@localhost:1521:LEXGRID" };

    public final static String[] nciMTServers = new String[] {
            "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=C:\\Temp\\NCIMetaThesaurus.mdb",
            "jdbc:hsqldb:file:C:/temp/LexGrid-hsqldbs/NCIMetaThesaurus;shutdown=true;hsqldb.default_table_type=cached",
            "jdbc:mysql://localhost/NCIMetaThesaurus", "jdbc:postgresql://localhost/NCIMetaThesaurus",
            "jdbc:db2://localhost:50000/NCIMetaThesaurus",
            "jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=NCIMetaThesaurus",
            "jdbc:oracle:thin:@localhost:1521:LEXGRID" };

    public final static String[] lsiServers = new String[] {
            "jdbc:postgresql://informatics.mayo.edu/LexGridServicesIndex",
            "jdbc:postgresql://mir04.mayo.edu/LexGridServicesIndex" };
    public final static String lsiDriver = "org.postgresql.Driver";

    /**
     * Default characters to treat as whitespace (in addition to standard
     * whitespace characters). while indexing lexgrid content. This set
     * different from the default set in the indexer because it does not include
     * colon.
     * 
     * Note that this does not include the underscore - '_' or colon - ':'
     */
    public static char[] lexGridWhiteSpaceIndexSet = new char[] { '-', ';', '(', ')', '{', '}', '[', ']', '<', '>', '|' };
}