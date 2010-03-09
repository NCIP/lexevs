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
package cl;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.varia.LevelRangeFilter;
import org.lexevs.logging.messaging.impl.CommandLineMessageDirector;

/**
 * Tool for executing the SQLToSQLLite converstion from the command line.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 * @version subversion $Revision: 7188 $ checked in on $Date: 2008-02-15
 *          17:22:02 +0000 (Fri, 15 Feb 2008) $
 */
public class SQLToSQLLite {
    /**
     * Command Line Runner.
     * 
     * <pre>
     * usage: java -Xmx300 -cp convert.jar cl.SQLToSQLLite [-SU SQLUserName] 
     * -SD Driver -CS Coding Scheme [-SP SQLPassword] [-SLP SqlLitePassword] [-DC] 
     * -SLS SqlLiteServer -SLD SqlLiteDriver -SS SQLServer [-SLU SqlLiteUsername]
     * 
     * 
     * -CS,--CodingScheme &lt;Coding Scheme&gt;         The coding scheme to convert
     * -DC,--DisableConstraints                   Set flag to Disable table Constraints.
     * -SD,--SqlDriver &lt;Driver&gt;                   The driver class to use for the sql connection (driver must be on the path)
     * -SLD,--SqlLiteDriver &lt;SqlLiteDriver&gt;       The driver class to use for the sql lite connection (driver must be on the path)
     * -SLP,--SqlLitePassword &lt;SqlLitePassword&gt;   The password to use on the sql lite server.
     * -SLS,--SqlLiteServer &lt;SqlLiteServer&gt;       The address of the SQL Lite server.
     * -SLU,--SqlLiteUsername &lt;SqlLiteUsername&gt;   The username to use on the sql lite server
     * -SP,--SqlPassword &lt;SQLPassword&gt;            The password to use on the sql server.
     * -SS,--SqlServer &lt;SQLServer&gt;                The address of the SQL server.
     * -SU,--SqlUserName &lt;SQLUserName&gt;            The username to use on the sql server.
     * -TP,--TablePrefix &lt;TablePrefix&gt;      The prefix to use for the tables on the sql server.
     * 
     * 
     * Example: java -Xmx300 -cp convert.jar cl.SQLToSQLLite -SU mirpub 
     * -SP mirpub -SD org.gjt.mm.mysql.Driver -SS jdbc:mysql://mir04/LexGrid 
     * -SLU user -SLD sun.jdbc.odbc.JdbcOdbcDriver -SLS &quot;jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=C:\Temp\LexGrid_Lite.mdb&quot; 
     * -CS AI/RHEUM
     * </pre>
     */
    public static void main(String[] args) throws Exception {
        CommandLine options = parseCommandLineOptions(args);
        Appender temp = new FileAppender(
                new PatternLayout("%-5p [%t] [%c] (%F:%L): %m\t%d{dd MMM yyyy HH:mm:ss,SSS}\n"),
                "convert error log.log", false);
        LevelRangeFilter tempFilter = new LevelRangeFilter();
        tempFilter.setLevelMin(Level.WARN);
        temp.addFilter(tempFilter);

        Logger.getRootLogger().addAppender(temp);
        Logger.getRootLogger().addAppender(
                new FileAppender(new PatternLayout("%-5p [%t] [%c] (%F:%L): %m\t%d{dd MMM yyyy HH:mm:ss,SSS}\n"),
                        "convert debug log.log", false));

        CommandLineMessageDirector director = new CommandLineMessageDirector();

        new edu.mayo.informatics.lexgrid.convert.directConversions.SQLToSQLLite(options.getOptionValue("SLS"), options
                .getOptionValue("SLD"), options.getOptionValue("SLU"), options.getOptionValue("SLP"), options
                .getOptionValue("SS"), options.getOptionValue("SD"), options.getOptionValue("SU"), options
                .getOptionValue("SP"), options.getOptionValue("TP"), options.getOptionValue("CS"), director, !options
                .hasOption("DC"));

        System.out.println("");
        System.out.println("Done");
    }

    private static CommandLine parseCommandLineOptions(String[] args) {
        // create the command line parser
        CommandLineParser parser = new BasicParser();

        // create the Options
        Options options = new Options();
        Option temp = new Option("SD", "SqlDriver", true,
                "The driver class to use for the sql connection (driver must be on the path)");
        temp.setRequired(true);
        temp.setArgName("Driver");
        options.addOption(temp);

        temp = new Option("SS", "SqlServer", true, "The address of the SQL server.");
        temp.setArgName("SQLServer");
        temp.setRequired(true);
        options.addOption(temp);

        temp = new Option("SU", "SqlUserName", true, "The username to use on the sql server.");
        temp.setArgName("SQLUserName");
        options.addOption(temp);

        temp = new Option("SP", "SqlPassword", true, "The password to use on the sql server.");
        temp.setArgName("SQLPassword");
        options.addOption(temp);

        temp = new Option("TP", "TablePrefix", true, "The prefix to use on the sql tables.");
        temp.setArgName("TablePrefix");
        options.addOption(temp);

        temp = new Option("SLS", "SqlLiteServer", true, "The address of the SQL Lite server.");
        temp.setRequired(true);
        temp.setArgName("SqlLiteServer");
        options.addOption(temp);

        temp = new Option("SLD", "SqlLiteDriver", true,
                "The driver class to use for the sql lite connection (driver must be on the path)");
        temp.setArgName("SqlLiteDriver");
        temp.setRequired(true);
        options.addOption(temp);

        temp = new Option("SLU", "SqlLiteUsername", true, "The username to use on the sql lite server");
        temp.setArgName("SqlLiteUsername");
        options.addOption(temp);

        temp = new Option("SLP", "SqlLitePassword", true, "The password to use on the sql lite server.");
        temp.setArgName("SqlLitePassword");
        options.addOption(temp);

        temp = new Option("CS", "CodingScheme", true, "The coding scheme to convert");
        temp.setArgName("Coding Scheme");
        temp.setRequired(true);
        options.addOption(temp);

        temp = new Option("DC", "DisableConstraints", false, "Set flag to Disable table Constraints.");
        temp.setArgName("Disable Constraints");
        temp.setRequired(false);
        options.addOption(temp);

        CommandLine result = null;
        try {
            result = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println("");
            System.out.println(e.toString());
            System.out.println("");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(300, "java -Xmx300 -cp convert.jar cl.SQLToSQLLite", "", options, "", true);
            System.out.println("");
            System.out
                    .println("Example: java -Xmx300 -cp convert.jar cl.SQLToSQLLite -SU mirpub -SP mirpub -SD org.gjt.mm.mysql.Driver -SS jdbc:mysql://mir04/LexGrid -SLU user -SLD sun.jdbc.odbc.JdbcOdbcDriver -SLS \"jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=C:\\Temp\\LexGrid_Lite.mdb\" -CS AI/RHEUM");
            System.exit(0);
        }
        return result;
    }
}