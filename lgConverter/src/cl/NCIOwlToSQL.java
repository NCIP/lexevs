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

import java.net.URI;

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

import edu.mayo.informatics.lexgrid.convert.formats.ConversionLauncher;
import edu.mayo.informatics.lexgrid.convert.formats.OptionHolder;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.NCIOwl;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridSQLOut;
import edu.mayo.informatics.resourcereader.core.StringUtils;

/**
 * Tool for executing the NCIOwlToSQL conversion from the command line. This
 * tool loads an NCI OWL representation to a specified relational database.
 * 
 * @author johnson.thomas@mayo.edu
 */
public class NCIOwlToSQL {
    /**
     * Command line parser.
     * 
     * <pre>
     * usage: java -Xmx300 -cp lgRuntime.jar cl.NCIOwlToSQL 
     *  [-SP SqlPassword] -FN File Name -MF manifestFileURI -SS SqlServer 
     * -SD SqlDriver  [-SU SqlUsername]
     * 
     * -FN,--FileName &lt;File Name&gt;        The file name to convert.
     * -MF,--Manifest File URI &lt;URI&gt; 	 The URI of manifest file (xml)
     * -SD,--SqlDriver &lt;SqlDriver&gt;       The driver class to use for the sql connection (driver must be on the path)
     * -SP,--SqlPassword &lt;SqlPassword&gt;   The password to use on the sql server.
     * -SS,--SqlServer &lt;SqlServer&gt;       The address of the SQL server.
     * -SU,--SqlUsername &lt;SqlUsername&gt;   The username to use on the sql server
     * -NoFail,--NoFailOnError           Specify if you don't want it to fail on minor errors.
     * 
     * Example: java -Xmx300 -cp lgRuntime.jar cl.LgXMLToSQL 
     * -SD sun.jdbc.odbc.JdbcOdbcDriver -SS &quot;jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=C:\Temp\LexGrid.mdb&quot; 
     * -FN myFile.xml -MF &quot;file:///path/to/my-manifest.xml&quot;
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

        CommandLineMessageDirector director = new CommandLineMessageDirector("convert.cl.NCIOwlToSQL");
        System.out.println("*** " + options.getOptionValue("FN"));

        String manUriStr = options.getOptionValue("MF");
        URI manifest = null;

        if (!StringUtils.isNull(manUriStr.trim()))
            manifest = URI.create(manUriStr);

        NCIOwl in = new NCIOwl(options.getOptionValue("FN"), manifest);

        LexGridSQLOut out = new LexGridSQLOut(options.getOptionValue("SU"), options.getOptionValue("SP"), options
                .getOptionValue("SS"), options.getOptionValue("SD"), null);

        OptionHolder convertOptions = new OptionHolder();

        convertOptions.add(new edu.mayo.informatics.lexgrid.convert.formats.Option(
                edu.mayo.informatics.lexgrid.convert.formats.Option.FAIL_ON_ERROR, new Boolean(!options
                        .hasOption("NoFail"))));

        ConversionLauncher.startConversion(in, out, null, convertOptions, director);

        System.out.println("");
        System.out.println("Done");
    }

    private static CommandLine parseCommandLineOptions(String[] args) {
        // create the command line parser
        CommandLineParser parser = new BasicParser();

        // create the Options
        Options options = new Options();

        Option temp = new Option("SS", "SqlServer", true, "The address of the SQL server.");
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

        temp = new Option("FN", "FileName", true, "The file name to convert.");
        temp.setArgName("File Name");
        temp.setRequired(true);
        options.addOption(temp);

        temp = new Option("MF", "ManifestFile", true, "URI of the Manifest (xml) file");
        temp.setRequired(false);
        temp.setArgName("MF");
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
            formatter.printHelp(300, "java -Xmx300 -cp converter.jar cl.NCIOwlToSQL", "", options, "", true);
            System.out.println("");
            System.out
                    .println("Example: java -Xmx300 -cp converter.jar cl.NCIOwlToSQL -SD sun.jdbc.odbc.JdbcOdbcDriver -SS \"jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=C:\\Temp\\LexGrid.mdb\" -FN myFile.xml");
            System.out.println("");
            System.out
                    .println("Example: java -Xmx300 -cp converter.jar cl.NCIOwlToSQL -SD sun.jdbc.odbc.JdbcOdbcDriver -SS \"jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=C:\\Temp\\LexGrid.mdb\" -FN myFile.xml -MF \"file:///path/to/my-manifest.xml\"");
            System.exit(0);
        }
        return result;
    }
}