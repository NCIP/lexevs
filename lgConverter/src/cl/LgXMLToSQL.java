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
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.LexGridXML;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridSQLOut;

/**
 * Tool for executing the LgXMLToSql conversion from the command line.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 * @version subversion $Revision: 8812 $ checked in on $Date: 2008-06-13
 *          16:06:47 +0000 (Fri, 13 Jun 2008) $
 */
public class LgXMLToSQL {
    /**
     * Command line parser.
     * 
     * <pre>
     * usage: java -Xmx300 -cp lgRuntime.jar cl.LgXMLToSQL 
     *  [-SP SqlPassword] -FN File Name -SS SqlServer 
     * -SD SqlDriver  [-SU SqlUsername]
     * 
     * 
     * 
     * 
     * -FN,--FileName &lt;File Name&gt;                 The file name to convert.
     * -SD,--SqlDriver &lt;SqlDriver&gt;       The driver class to use for the sql connection (driver must be on the path)
     * -SP,--SqlPassword &lt;SqlPassword&gt;   The password to use on the sql server.
     * -SS,--SqlServer &lt;SqlServer&gt;       The address of the SQL server.
     * -SU,--SqlUsername &lt;SqlUsername&gt;   The username to use on the sql server
     * -NoFail,--NoFailOnError           Specify if you don't want it to fail on minor errors.
     * 
     * 
     * Example: java -Xmx300 -cp lgRuntime.jar cl.LgXMLToSQL 
     * -SD sun.jdbc.odbc.JdbcOdbcDriver -SS &quot;jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=C:\Temp\LexGrid.mdb&quot; 
     * -FN myFile.xml
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

        CommandLineMessageDirector director = new CommandLineMessageDirector("convert.LgXMLToSQL");
        System.out.println("*** " + options.getOptionValue("FN"));

        LexGridXML in = new LexGridXML(options.getOptionValue("FN"), null);

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
            formatter.printHelp(300, "java -Xmx300 -cp converter.jar cl.LgXMLToSql", "", options, "", true);
            System.out.println("");
            System.out
                    .println("Example: java -Xmx300 -cp converter.jar cl.LgXMLToSql -SD sun.jdbc.odbc.JdbcOdbcDriver -SS \"jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=C:\\Temp\\LexGrid.mdb\" -FN myFile.xml");
            System.exit(0);
        }
        return result;
    }
}