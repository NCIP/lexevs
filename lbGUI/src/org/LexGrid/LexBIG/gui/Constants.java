
package org.LexGrid.LexBIG.gui;

/**
 * Constants for the LexBIG GUI (like version number, etc).
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
// TODO - why won't webstart read jar files with my custom classloader?
public class Constants {
	public static String version = "Console";

	public static String[] singleDBModeServers = new String[] {
			"jdbc:mysql://bmidev/LexBIG", "jdbc:postgresql://bmidev/LexBIG",
			"jdbc:hsqldb:file:C:/temp/LexBIG",
			"jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=C:/temp/LexBIG.mdb" };
	public static String[] multiDBModeServers = new String[] {
			"jdbc:mysql://bmidev/", "jdbc:postgresql://bmidev/",
			"jdbc:hsqldb:file:C:/temp/",
			"jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=C:/temp/" };
	public static String[] drivers = new String[] { "org.gjt.mm.mysql.Driver",
			"org.postgresql.Driver", "org.hsqldb.jdbcDriver",
			"sun.jdbc.odbc.JdbcOdbcDriver" };

}