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