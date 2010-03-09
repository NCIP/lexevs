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
 * Tool for executing the TextToSQLLite conversion from the command line.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 * @version subversion $Revision: 8766 $ checked in on $Date: 2008-06-12
 *          16:38:31 +0000 (Thu, 12 Jun 2008) $
 */
public class TextToSQLLite {
    /**
     * Command line parser.
     * 
     * <pre>
     * usage: java -Xmx300 -cp convert.jar cl.TextToSQLLite 
     * -CS Coding Scheme [-SLP SqlLitePassword] -FN File Name -SLS SqlLiteServer 
     * -SLD SqlLiteDriver [-DELIM Delimiter] [-SLU SqlLiteUsername]
     * 
     * 
     * -forceB,--ForceFormatB                     set to force reading format A as B.
     * -DELIM,--Delimiter &lt;Delimiter&gt;             The delimiter to use (defaults to tab).
     * -FN,--FileName &lt;File Name&gt;                 The file name to convert.
     * -SLD,--SqlLiteDriver &lt;SqlLiteDriver&gt;       The driver class to use for the sql lite connection (driver must be on the path)
     * -SLP,--SqlLitePassword &lt;SqlLitePassword&gt;   The password to use on the sql lite server.
     * -SLS,--SqlLiteServer &lt;SqlLiteServer&gt;       The address of the SQL Lite server.
     * -SLU,--SqlLiteUsername &lt;SqlLiteUsername&gt;   The username to use on the sql lite server
     * 
     * 
     * Example: java -Xmx300 -cp convert.jar cl.TextToSQLLite 
     * -SLD sun.jdbc.odbc.JdbcOdbcDriver -SLS &quot;jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=C:\Temp\LexGrid_Lite.mdb&quot; 
     * -CS myCodeSystem -FN myFile.txt
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

        CommandLineMessageDirector director = new CommandLineMessageDirector("convert.TextToSqlLite");

        new edu.mayo.informatics.lexgrid.convert.directConversions.TextToSQLLite(options.getOptionValue("FN"), options
                .getOptionValue("DELIMIT"), options.getOptionValue("SLS"), options.getOptionValue("SLD"), options
                .getOptionValue("SLU"), options.getOptionValue("SLP"), null, director, options.hasOption("forceB"));

        System.out.println("");
        System.out.println("Done");
    }

    private static CommandLine parseCommandLineOptions(String[] args) {
        // create the command line parser
        CommandLineParser parser = new BasicParser();

        // create the Options
        Options options = new Options();

        Option temp = new Option("SLS", "SqlLiteServer", true, "The address of the SQL Lite server.");
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

        temp = new Option("FN", "FileName", true, "The file name to convert.");
        temp.setArgName("File Name");
        temp.setRequired(true);
        options.addOption(temp);

        temp = new Option("DELIM", "Delimiter", true, "The delimiter to use (defaults to tab).");
        temp.setArgName("Delimiter");
        temp.setRequired(false);
        options.addOption(temp);

        temp = new Option("forceB", "ForceFormatB", false, "Set flag read A formated file as B.");
        temp.setArgName("ForceFormatB");
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
            formatter.printHelp(300, "java -Xmx300 -cp convert.jar cl.TextToSQLLite", "", options, "", true);
            System.out.println("");
            System.out
                    .println("Example: java -Xmx300 -cp convert.jar cl.TextToSQLLite -SLD sun.jdbc.odbc.JdbcOdbcDriver -SLS \"jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=C:\\Temp\\LexGrid_Lite.mdb\" -FN myFile.txt");
            System.exit(0);
        }
        return result;
    }
}