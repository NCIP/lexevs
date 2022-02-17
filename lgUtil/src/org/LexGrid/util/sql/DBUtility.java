
package org.LexGrid.util.sql;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.LexGrid.util.sql.sqlReconnect.WrappedConnection;
import org.apache.commons.lang.StringUtils;

/**
 * This class holds many utility type methods for common DB related tasks -
 * getting booleans out of a SQL server, escaping things for LDAP, etc.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 */
public class DBUtility {
    public static Connection connectToDatabase(String server, String driver, String user, String password)
            throws Exception {

        return new WrappedConnection(user, password, driver, server, true);
    }

    /**
     * Checks the validity of the Local Name
     * 
     * @param name
     *            Local Name to check
     * @return true if valid, false otherwise
     */
    public static boolean validLocalName(String name) {
        char[] temp = name.toCharArray();
        for (int i = 0; i < temp.length; i++) {
            if (temp[i] != '1' && temp[i] != '2' && temp[i] != '3' && temp[i] != '4' && temp[i] != '5'
                    && temp[i] != '6' && temp[i] != '7' && temp[i] != '8' && temp[i] != '9' && temp[i] != '0'
                    && temp[i] != '.') {
                return false;
            }
        }
        return true;
    }

    public static String escapeLdapDN(String string, boolean escapeCommas) {
        StringBuffer temp = new StringBuffer(string);
        for (int i = 0; i < temp.length(); i++) {
            if (temp.charAt(i) == '<') {
                temp.insert(i, '\\');
                i++;
            }

            if (temp.charAt(i) == '>') {
                temp.insert(i, '\\');
                i++;
            }
            if (temp.charAt(i) == '/') {
                temp.insert(i, '\\');
                i++;
            }
            if (escapeCommas && temp.charAt(i) == ',') {
                temp.insert(i, '\\');
                i++;
            }

        }
        return temp.toString();
    }

    public static String escapeLdapDN(String string) {
        return escapeLdapDN(string, false);
    }

    public static String escapeLdapCode(String string) {
        return escapeLdapDN(string, true);
    }

    /**
     * simplified call to setBooleanOnPreparedStatement. Passes in false and
     * null for the last two parameters.
     * 
     * @param statement
     * @param colNumber
     * @param value
     * @throws SQLException
     */
    public static void setBooleanOnPreparedStatment(PreparedStatement statement, int colNumber, Boolean value)
            throws SQLException {
        setBooleanOnPreparedStatment(statement, colNumber, value, false, null);
    }

    /**
     * simplified call to setBooleanOnPreparedStatement. Passes in null for
     * databaseType
     * 
     * @param statement
     * @param colNumber
     * @param value
     * @throws SQLException
     */
    public static void setBooleanOnPreparedStatment(PreparedStatement statement, int colNumber, Boolean value,
            boolean isSqlLite) throws SQLException {
        setBooleanOnPreparedStatment(statement, colNumber, value, isSqlLite, null);
    }

    /**
     * simplified call to setBooleanOnPreparedStatement. Passes in false for
     * "isSqlLite"
     * 
     * @param statement
     * @param colNumber
     * @param value
     * @throws SQLException
     */
    public static void setBooleanOnPreparedStatment(PreparedStatement statement, int colNumber, Boolean value,
            String databaseType) throws SQLException {
        setBooleanOnPreparedStatment(statement, colNumber, value, false, databaseType);
    }

    /**
     * Sets booleans properly in the LexGrid world.
     * 
     * @param statement
     * @param colNumber
     * @param value
     * @param isSqlLite
     *            - using sqlLite tables? Set to true if the answer is yes.
     * @param databaseType
     *            - optional. Set to null or "" if you don't know it.
     * @throws SQLException
     */
    public static void setBooleanOnPreparedStatment(PreparedStatement statement, int colNumber, Boolean value,
            boolean isSqlLite, String databaseType) throws SQLException {
        if (databaseType == null || databaseType.length() == 0) {
            databaseType = statement.getConnection().getMetaData().getDatabaseProductName();
        }
        // This has been tested (and works correctly) on mysql (using tinyint
        // with 1 and 0), postgres (using
        // bool)
        // and Access using both a Text of (True or False) and yes/no.
        if (value == null) {
            // mysql lite (on access) uses yesno's for booleans (which don't
            // support null), while regular sql
            // doesn't.
            if (isSqlLite && databaseType.equals("ACCESS")) {
                statement.setBoolean(colNumber, false);
            }
            // the new postgres driver doesn't allow you to use the setString to
            // null trick.
            else if (databaseType.equals("PostgreSQL")) {
                statement.setNull(colNumber, java.sql.Types.BOOLEAN);
            }
            // most other databases let you set null on a string, even if it
            // isn't a string type.
            else {
                statement.setString(colNumber, null);
            }
        } else {
            // sql on format on access, and mysql both use strings instead of
            // booleans (to support null)
            if ((databaseType.equals("ACCESS") && !isSqlLite)) {
                statement.setString(colNumber, value.booleanValue() + "");
            } else if (databaseType.equals("MySQL")) {
                statement.setInt(colNumber, (value.booleanValue() ? 1 : 0));
            } else {
                statement.setBoolean(colNumber, value.booleanValue());
            }
        }
    }

    public static Boolean getBooleanFromResultSet(ResultSet results, String columnName) throws SQLException {
        // This has been tested (and works correctly) on mysql (using enum of
        // True, False or tinyint 1, 0), postgres (using
        // bool)
        // and Access using both a Text of (True or False) and yes/no.
        Object temp = results.getObject(columnName);
        if (temp == null) {
            return null;
        } else if (temp instanceof Boolean) {
            return (Boolean) temp;
        } else if (temp instanceof Integer) {
            int i = ((Integer) temp).intValue();
            if (i == 1) {
                return new Boolean(true);
            } else {
                return new Boolean(false);
            }
        } else if (temp instanceof String || temp instanceof Byte) {
            if (temp instanceof Byte) {
                temp = ((Byte) temp).toString();
            }

            String a = (String) temp;
            if (a.length() == 1) {
                try {
                    int i = Integer.parseInt(a);
                    if (i == 1) {
                        return new Boolean(true);
                    } else {
                        return new Boolean(false);
                    }
                } catch (NumberFormatException e) {
                    // not a number, treat it as a string?
                    if (a.equalsIgnoreCase("t")) {
                        return new Boolean(true);
                    } else if (a.equalsIgnoreCase("f")) {
                        return new Boolean(false);
                    }
                }

            } else {
                return new Boolean(temp.toString());
            }
        }
        return new Boolean(temp.toString());
    }

    public static boolean getbooleanFromResultSet(ResultSet results, String column) throws SQLException {
        Boolean temp = DBUtility.getBooleanFromResultSet(results, column);
        if (temp == null) {
            return false;
        } else {
            return temp.booleanValue();
        }
    }

    public static void AddUTF8PropToDBConnectionProperties(Properties props, String URL) {
        String tempURL = URL.toLowerCase();
        // access and postgres use this flag
        if (tempURL.indexOf("odbc") != -1 || tempURL.indexOf("postgresql") != -1) {
            props.setProperty("charSet", "utf-8");
        }
        // mysql uses this
        else if (tempURL.indexOf("mysql") != -1) {
            props.setProperty("characterEncoding", "UTF-8");
            props.setProperty("useUnicode", "true");
        } else {
            props.setProperty("charSet", "utf-8");
        }
    }

    // TODO - this method has not been tested on DB2 or Oracle, or MSSQLServer
    public static void createDatabase(String DBUrl, String driver, String dbName, String username, String password,
            boolean useLexGridCharSet) throws Exception {
        if (driver.indexOf("hsqldb") != -1 || DBUrl.indexOf("Microsoft Access Driver") != -1) {
            // Dont need to do anything, hypersonic and access creates a db
            // automatically when
            // you connect to a location.
            return;
        }
        String url = DBUrl;
        String name = dbName;
        if (driver.indexOf("postgresql") != -1) {
            // need to connect to the 'template1' database
            url += "template1";
            name = "\"" + dbName + "\"";
        }
        Connection c = connectToDatabase(url, driver, username, password);
        GenericSQLModifier gsm = new GenericSQLModifier(c);
        String charSetString = "";
        if (useLexGridCharSet) {
            charSetString = " {lgCharSet}";
        }

        PreparedStatement temp = c.prepareStatement(gsm.modifySQL("Create Database " + name + charSetString));

        temp.executeUpdate();
        temp.close();
        c.close();
    }

    public static boolean doesDBExist(String server, String driver, String dbName, String parameters, String username,
            String password) {
        if (!server.endsWith("/")) {
            server += "/";
        }
        if (driver.indexOf("hsqldb") != -1) {
            String basePath = server.substring("jdbc:hsqldb:file:".length());
            int pos = basePath.indexOf(';');
            if (pos != -1) {
                basePath = basePath.substring(0, pos);
            }
            basePath += dbName;

            File temp = new File(basePath + ".log");
            if (!temp.exists()) {
                temp = new File(basePath + ".data");
                if (!temp.exists()) {
                    return false;
                }
            }
        } else if (server.indexOf("Microsoft Access Driver") != -1) {
            String basePath = server.substring("jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=".length())
                    + dbName;
            File temp = new File(basePath + parameters);

            if (!temp.exists()) {
                return false;
            }
        } else {
            server += dbName;
            try {
                Connection temp = DBUtility.connectToDatabase(server + parameters, driver, username, password);
                temp.close();
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    // TODO - this method has not been tested on DB2 or Oracle, or MSSQLServer
    public static void dropDatabase(String DBUrl, String driver, String dbName, String username, String password)
            throws Exception {
        String server = DBUrl;
        if (!server.endsWith("/")) {
            server += "/";
        }

        if (driver.indexOf("hsqldb") != -1) {
            String basePath = server.substring("jdbc:hsqldb:file:".length());
            int pos = basePath.indexOf(';');
            if (pos != -1) {
                basePath = basePath.substring(0, pos);
            }
            basePath += dbName;

            File temp = new File(basePath + ".lck");
            temp.delete();
            temp = new File(basePath + ".log");
            temp.delete();
            temp = new File(basePath + ".properties");
            temp.delete();
            temp = new File(basePath + ".backup");
            temp.delete();
            temp = new File(basePath + ".data");
            temp.delete();
            temp = new File(basePath + ".script");
            temp.delete();
        } else if (server.indexOf("Microsoft Access Driver") != -1) {
            String basePath = server.substring("jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=".length())
                    + dbName;
            File temp = new File(basePath);
            temp.delete();
            temp = new File(basePath.substring(0, basePath.length() - 4) + ".ldb");
            temp.delete();
        } else {
            String url = server;
            String name = dbName;
            if (driver.indexOf("postgresql") != -1) {
                // need to connect to the 'template1' database
                url += "template1";
                name = "\"" + dbName + "\"";

                /*
                 * sometimes on bmidev, we have issues deleting a db because
                 * postgres doesn't allow you to drop a db that someone is
                 * using. There seems to be a timing issue where our connections
                 * are not closed down quickly enough. this is an attempt to
                 * eliminate that problem.
                 */
                System.gc();
                // a bit of time to let the postgres server catch up.
                Thread.currentThread().sleep(250);
            }
            Connection c = connectToDatabase(url, driver, username, password);
            GenericSQLModifier gsm = new GenericSQLModifier(c);

            PreparedStatement temp = c.prepareStatement(gsm.modifySQL("Drop Database " + name));

            temp.executeUpdate();
            temp.close();
            c.close();
        }
    }

    /**
     * Construct the next identifier to use after the given identifier. Expects
     * (and returns) a 2 character string - using the characters a-z in the
     * first position and the characters a-z and 0-9 in the second position.
     * Case insensitive. Wraps if it gets to zz. Starts with a0 (if no
     * identifier is provided)
     * 
     * @param currentIdentifier
     * @return
     * @throws Exception
     *             if it doesn't understand the identifier.
     */
    public static String computeNextIdentifier(String currentIdentifier) throws Exception {
        String temp;
        if (currentIdentifier == null || currentIdentifier.length() == 0) {
            temp = "a0";
        } else if (StringUtils.isNumeric(currentIdentifier)) {
            temp = "a0";
        } else if (currentIdentifier.length() != 2) {
            throw new Exception("Invalid identifer passed in.  Must be a 2 character string.");
        } else {
            temp = currentIdentifier;
        }

        char a = temp.toLowerCase().charAt(0);
        char b = temp.toLowerCase().charAt(1);

        int ai = Character.getNumericValue(a);
        int bi = Character.getNumericValue(b);

        if (ai < 10 || ai > 35) {
            throw new Exception("Invalid identifer passed in.  First character must be a letter.");
        }

        if (bi < 0 || bi > 35) {
            throw new Exception("Invalid identifer passed in.  Second character must be a letter or a number.");
        }

        if (bi < 35) {
            bi++;
        } else {
            bi = 0;
            ai++;
        }

        if (ai > 35) {
            ai = 10;
        }

        return new String(Character.forDigit(ai, 36) + "" + Character.forDigit(bi, 36));
    }

    private static String convertEncodedSpaceToSpace(String in) {
        int loc = in.indexOf("%20");
        while (loc != -1) {
            in = in.substring(0, loc) + " " + in.substring(loc + 3);
            loc = in.indexOf("%20");
        }
        return in;
    }

    // public static void main(String[] args) throws Exception
    // {
    // String temp = computeNextIdentifier("a0");
    // System.out.println(temp);
    // while (!temp.equals("a0"))
    // {
    // temp = computeNextIdentifier(temp);
    // System.out.println(temp);
    // }
    //
    // Connection foo = new WrappedConnection("", "",
    // "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=C:\\temp\\LexGridLite.mdb",
    // "sun.jdbc.odbc.JdbcOdbcDriver");
    //        
    // Connection foo = new WrappedConnection("mirpub", "mirpub",
    // "jdbc:mysql://mir04/LexGrid",
    // "org.gjt.mm.mysql.Driver");
    //        
    // Connection foo = new WrappedConnection("mirpub", "mirpub",
    // "jdbc:postgresql://mir04/LexGrid",
    // "org.postgresql.Driver");+
    //                
    // Connection foo = new WrappedConnection("", "",
    // "com.ibm.db2.jcc.DB2Driver",
    // "jdbc:db2://localhost:50000/LexGrid"
    // );
    //        
    // PreparedStatement temp =
    // foo.prepareStatement("Select * from conceptProperty");
    //        
    // ResultSet results = temp.executeQuery();
    //        
    // while (results.next())
    // {
    // Boolean b = getBooleanFromResultSet(results, "isPreferred");
    // System.out.println(b == null ? "null"
    // : b.booleanValue() + "");
    // }
    //        
    // PreparedStatement temp2 =
    // foo.prepareStatement("Insert into test (test) values (?)");
    //        
    // setBooleanOnPreparedStatment(temp2, 1, null, false);
    //        
    // temp2.execute();
    //        
    // setBooleanOnPreparedStatment(temp2, 1, new Boolean(true), false);
    //        
    // temp2.execute();
    //        
    // setBooleanOnPreparedStatment(temp2, 1, new Boolean(false), false);
    //        
    // temp2.execute();
    //        
    // temp2.close();
    // foo.close();
    //
    // createBlankAccessDB("C:\\temp\\test.mdb");
    // }

}