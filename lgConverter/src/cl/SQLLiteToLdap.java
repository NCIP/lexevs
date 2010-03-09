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
 * A Class to collect command line parameters, and execute the SQLLiteToLDAP
 * converter.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 * @version subversion $Revision: 7188 $ checked in on $Date: 2008-02-15
 *          17:22:02 +0000 (Fri, 15 Feb 2008) $
 */
public class SQLLiteToLdap {
    /**
     * Command Line Runner.
     * 
     * <pre>
     * usage: java -Xmx300 -cp convert.jar cl.SQLLiteToLdap [-SU SQLUserName] 
     * -SD Driver -CS Coding Scheme [-LP LdapPassword] [-SP SQLPassword] 
     * -LA LdapAddress [-IE] [-LU LdapUserName] -SS SQLServer -LS LdapService
     * 
     * 
     * -CS,--CodingScheme &lt;Coding Scheme&gt;   The coding scheme to convert
     * -IE,--IgnoreErrors                   Set flag to Ignore (not fail) on minor Errors.
     * -LA,--LdapAddress &lt;LdapAddress&gt;      The address of the Ldap server.
     * -LP,--LdapPassword &lt;LdapPassword&gt;    The password to use on the ldap server.
     * -LS,--LdapService &lt;LdapService&gt;      The service to connect to on the ldap server.
     * -LU,--LdapUserName &lt;LdapUserName&gt;    The username to use on the ldap server.
     * -SD,--SqlDriver &lt;Driver&gt;             The driver class to use for the sql connection (driver must be on the path)
     * -SP,--SqlPassword &lt;SQLPassword&gt;      The password to use on the sql server.
     * -SS,--SqlServer &lt;SQLServer&gt;          The address of the SQL server.
     * -SU,--SqlUserName &lt;SQLUserName&gt;      The username to use on the sql server.
     * 
     * 
     * Example: java -Xmx300 -cp convert.jar cl.SQLLiteToLdap -SU user 
     * -SD sun.jdbc.odbc.JdbcOdbcDriver -SS &quot;jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=C:\Temp\LexGrid_Lite.mdb&quot; 
     * -LA ldap://mir04.mayo.edu:31900 -LU cn=HL7User,dc=Users,service=userValidation,dc=LexGrid,dc=org 
     * -LP terminology -LS service=CTS,dc=LexGrid,dc=org -CS ActClass
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

        new edu.mayo.informatics.lexgrid.convert.directConversions.SQLLiteToLdap(options.getOptionValue("SS"), options
                .getOptionValue("SD"), options.getOptionValue("SU"), options.getOptionValue("SP"), options
                .getOptionValue("LU"), options.getOptionValue("LP"), options.getOptionValue("LA"), options
                .getOptionValue("LS"), options.getOptionValue("CS"), !options.hasOption("IE"), director);

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

        temp = new Option("LS", "LdapService", true, "The service to connect to on the ldap server.");
        temp.setRequired(true);
        temp.setArgName("LdapService");
        options.addOption(temp);

        temp = new Option("LA", "LdapAddress", true, "The address of the Ldap server.");
        temp.setArgName("LdapAddress");
        temp.setRequired(true);
        options.addOption(temp);

        temp = new Option("LU", "LdapUserName", true, "The username to use on the ldap server.");
        temp.setArgName("LdapUserName");
        options.addOption(temp);

        temp = new Option("LP", "LdapPassword", true, "The password to use on the ldap server.");
        temp.setArgName("LdapPassword");
        options.addOption(temp);

        temp = new Option("CS", "CodingScheme", true, "The coding scheme to convert");
        temp.setArgName("Coding Scheme");
        temp.setRequired(true);
        options.addOption(temp);

        temp = new Option("IE", "IgnoreErrors", false, "Set flag to Ignore (not fail) on minor Errors.");
        temp.setArgName("Ignore Errors");
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
            formatter.printHelp(300, "java -Xmx300 -cp convert.jar cl.SQLLiteToLdap", "", options, "", true);
            System.out.println("");
            System.out
                    .println("Example: java -Xmx300 -cp convert.jar cl.SQLLiteToLdap -SU user -SD sun.jdbc.odbc.JdbcOdbcDriver -SS \"jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=C:\\Temp\\LexGrid_Lite.mdb\" -LA ldap://mir04.mayo.edu:31900 -LU cn=HL7User,dc=Users,service=userValidation,dc=LexGrid,dc=org -LP terminology -LS service=CTS,dc=LexGrid,dc=org -CS ActClass");
            System.exit(0);
        }
        return result;
    }
}