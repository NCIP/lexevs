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

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.LexGrid.messaging.impl.CommandLineMessageDirector;
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

import edu.mayo.informatics.lexgrid.convert.formats.ConversionLauncher;
import edu.mayo.informatics.lexgrid.convert.formats.OptionHolder;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.LexGridSQL;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.IndexLexGridDatabase;

/**
 * Tool to build indexes over a previously populated LexGrid database
 * repository.
 * 
 * @author johnson.thomas@mayo.edu
 */
public class IndexSQL {
    /**
     * Command line parser.
     * 
     * <pre>
     * usage: java -Xmx300 -cp lgRuntime.jar IndexSQL
     * -SS SqlServer -SD SqlDriver  [-SU SqlUsername] [-SP SqlPassword] 
     * -FN IndexName -FP IndexLocation -SS SqlServer -SD SqlDriver  [-SU SqlUsername] [-SP SqlPassword] 
     *     
     * -IN,--IndexName &lt;FileName&gt;        The name of the index to (re)create.
     * -IP,--IndexPath &lt;FilePath&gt;        The location for the (re)created index.
     * -CS,--CodeSchemes &lt;CodeSchemes&gt;   The comma-delimited set of local names for the coding schemes to index.
     * -SD,--SqlDriver &lt;SqlDriver&gt;       The driver class to use for the sql connection (driver must be on the path)
     * -SP,--SqlPassword &lt;SqlPassword&gt;   The password to use on the sql server.
     * -SS,--SqlServer &lt;SqlServer&gt;       The address of the SQL server.
     * -SU,--SqlUsername &lt;SqlUsername&gt;   The username to use on the sql server
     * -NoFail,--NoFailOnError           Specify if you don't want it to fail on minor errors.
     * 
     * 
     * Example: java -Xmx300 -cp lgRuntime.jar cl.IndexSQL
     * -SD sun.jdbc.odbc.JdbcOdbcDriver -SS &quot;jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=C:\Temp\LexGrid.mdb&quot;
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

        CommandLineMessageDirector director = new CommandLineMessageDirector("cl.IndexSQL");
        System.out.println("*** " + options.getOptionValue("IN"));

        LexGridSQL in = new LexGridSQL(options.getOptionValue("SU"), options.getOptionValue("SP"), options
                .getOptionValue("SS"), options.getOptionValue("SD"), null);

        IndexLexGridDatabase out = new IndexLexGridDatabase(options.getOptionValue("IL"), options.getOptionValue("IN"),
                false, null);

        OptionHolder convertOptions = new OptionHolder();
        convertOptions.add(new edu.mayo.informatics.lexgrid.convert.formats.Option(
                edu.mayo.informatics.lexgrid.convert.formats.Option.SQL_FETCH_SIZE, "2048"));
        convertOptions.add(new edu.mayo.informatics.lexgrid.convert.formats.Option(
                edu.mayo.informatics.lexgrid.convert.formats.Option.BUILD_DB_METAPHONE, new Boolean(true)));
        convertOptions.add(new edu.mayo.informatics.lexgrid.convert.formats.Option(
                edu.mayo.informatics.lexgrid.convert.formats.Option.USE_COMPOUND_FMT, new Boolean(true)));
        convertOptions.add(new edu.mayo.informatics.lexgrid.convert.formats.Option(
                edu.mayo.informatics.lexgrid.convert.formats.Option.FAIL_ON_ERROR, new Boolean(!options
                        .hasOption("NoFail"))));

        ArrayList schemes = new ArrayList();
        StringTokenizer st = new StringTokenizer(options.getOptionValue("CS"), ",");
        while (st.hasMoreTokens())
            schemes.add(st.nextToken().trim());
        ConversionLauncher.startConversion(in, out, (String[]) schemes.toArray(new String[schemes.size()]),
                convertOptions, director);

        System.out.println("");
        System.out.println("Done");
    }

    private static CommandLine parseCommandLineOptions(String[] args) {
        // create the command line parser
        CommandLineParser parser = new BasicParser();

        // create the Options
        Options options = new Options();

        Option temp = new Option("IN", "FileName", true, "The index to (re)create");
        temp.setArgName("IndexName");
        options.addOption(temp);

        temp = new Option("IL", "FileLocation", true, "Directory to contain the index file");
        temp.setArgName("IndexLocation");
        options.addOption(temp);

        temp = new Option("CS", "CodeSchemes", true, "Directory to contain the index file");
        temp.setArgName("FileLocation");
        options.addOption(temp);

        temp = new Option("SS", "SqlServer", true, "The address of the SQL server.");
        temp.setRequired(true);
        temp.setArgName("SqlServer");
        options.addOption(temp);

        temp = new Option("SD", "SqlDriver", true,
                "The driver class to use for the sql lite connection (driver must be on the path)");
        temp.setArgName("SqlDriver");
        temp.setRequired(true);
        options.addOption(temp);

        temp = new Option("SU", "SqlUsername", true, "The username to use on the sql lite server");
        temp.setArgName("SqlUsername");
        options.addOption(temp);

        temp = new Option("SP", "SqlPassword", true, "The password to use on the sql lite server.");
        temp.setArgName("SqlPassword");
        options.addOption(temp);

        temp = new Option("NoFail", "NoFailOnError", false, "Set flag to Not Fail on minor errors.");
        temp.setArgName("No Fail On Error");
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
            formatter.printHelp(300, "java -Xmx300 -cp converter.jar cl.IndexSQL", "", options, "", true);
            System.out.println("");
            System.out
                    .println("Example: java -Xmx300 -cp converter.jar cl.IndexSQL -SD sun.jdbc.odbc.JdbcOdbcDriver -SS \"jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=C:\\Temp\\LexGrid.mdb\\");
            System.exit(0);
        }
        return result;
    }
}