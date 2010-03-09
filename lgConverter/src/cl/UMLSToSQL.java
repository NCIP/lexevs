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
 * Tool for executing the UMLSToSQL converstion from the command line.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 * @version subversion $Revision: 10732 $ checked in on $Date: 2008-09-09
 *          18:13:11 +0000 (Tue, 09 Sep 2008) $
 */
public class UMLSToSQL {
    /**
     * Command line runner.
     * 
     * <pre>
     * usage: java -Xmx300 -cp lgRuntime.jar cl.UMLSToSQL [-SU SQLUserName] 
     * -SD Driver -CS Coding Scheme [-UP UMLSPassword] [-SP SQLPassword] [-DC] 
     * -UD UMLSDriver -US UMLSServer [-UU UMLSUsername] -SS SQLServer
     * 
     * 
     * -CS,--CodingScheme &lt;Coding Scheme&gt;   The coding scheme to convert
     * -DC,--DisableConstraints             Set flag to Disable table Constraints.
     * -SD,--SqlDriver &lt;Driver&gt;             The driver class to use for the sql connection (driver must be on the path)
     * -SP,--SqlPassword &lt;SQLPassword&gt;      The password to use on the sql server.
     * -SS,--SqlServer &lt;SQLServer&gt;          The address of the SQL server.
     * -SU,--SqlUserName &lt;SQLUserName&gt;      The username to use on the sql server.
     * -TP,--TablePrefix &lt;TablePrefix&gt;      The prefix to use for the tables on the sql server.
     * -UD,--UMLSDriver &lt;UMLSDriver&gt;        The driver class to use for the UMLS server(driver must be on the path)
     * -UP,--UMLSPassword &lt;UMLSPassword&gt;    The password to use on the UMLS server.
     * -US,--UMLSServer &lt;UMLSServer&gt;        The address of the UMLS server.
     * -UU,--UMLSUsername &lt;UMLSUsername&gt;    The username to use on the UMLS server
     * 
     * 
     * Example: java -Xmx300 -cp lgRuntime.jar cl.UMLSToSQL -UU username 
     * -UP password -UD org.gjt.mm.mysql.Driver -US jdbc:mysql://servername/UMLS2004AB 
     * -SU user -SD sun.jdbc.odbc.JdbcOdbcDriver -SS &quot;jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=D:\Temp\LexGrid.mdb&quot; -CS AIR
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

        new edu.mayo.informatics.lexgrid.convert.directConversions.UMLSToSQL(options.getOptionValue("SS"), options
                .getOptionValue("SD"), options.getOptionValue("SU"), options.getOptionValue("SP"), options
                .getOptionValue("TP"), options.getOptionValue("US"), options.getOptionValue("UD"), options
                .getOptionValue("UU"), options.getOptionValue("UP"), options.getOptionValue("CS"), null, null, !options
                .hasOption("DC"), director);

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

        temp = new Option("US", "UMLSServer", true, "The address of the UMLS server.");
        temp.setRequired(true);
        temp.setArgName("UMLSServer");
        options.addOption(temp);

        temp = new Option("UD", "UMLSDriver", true,
                "The driver class to use for the UMLS server(driver must be on the path)");
        temp.setArgName("UMLSDriver");
        temp.setRequired(true);
        options.addOption(temp);

        temp = new Option("UU", "UMLSUsername", true, "The username to use on the UMLS server");
        temp.setArgName("UMLSUsername");
        options.addOption(temp);

        temp = new Option("UP", "UMLSPassword", true, "The password to use on the UMLS server.");
        temp.setArgName("UMLSPassword");
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
            formatter.printHelp(300, "java -Xmx300 -cp convert.jar cl.UMLSToSQL", "", options, "", true);
            System.out.println("");
            System.out
                    .println("Example: java -Xmx300 -cp convert.jar cl.UMLSToSQL -UU username -UP password -UD org.gjt.mm.mysql.Driver -US jdbc:mysql://servername/UMLS2004AB -SU user -SD sun.jdbc.odbc.JdbcOdbcDriver -SS \"jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=D:\\Temp\\LexGrid.mdb\" -CS AIR");
            System.exit(0);
        }
        return result;
    }
}