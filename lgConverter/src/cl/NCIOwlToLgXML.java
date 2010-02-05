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

import org.LexGrid.messaging.impl.CommandLineMessageDirector;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.varia.LevelRangeFilter;

import edu.mayo.informatics.lexgrid.convert.formats.ConversionLauncher;
import edu.mayo.informatics.lexgrid.convert.formats.OptionHolder;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.NCIOwl;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridXMLOut;

/**
 * Tool for executing the NCIOwlToLgXML conversion from the command line. This
 * tool converts an NCI OWL representation to LexGrid canonical XML format.
 * 
 * @author johnson.thomas@mayo.edu
 */
public class NCIOwlToLgXML {
    /**
     * Command line parser.
     * 
     * <pre>
     * usage: java -Xmx300 -cp lgRuntime.jar cl.NCIOwlToLgXML 
     * -IN OWLFilePath -MF OWLManifestFileURI -OUT OutputDirectory 
     * 
     * Example: java -Xmx300 -cp lgRuntime.jar cl.NCIOwlToLgXML 
     * -IN &quot;in.owl&quot; -OUT &quot;.&quot; 
     * 
     * java -Xmx300 -cp lgRuntime.jar cl.NCIOwlToLgXML 
     * -IN &quot;in.owl&quot; -MF &quot;file:///path/to/in-manifest.xml&quot; -OUT &quot;.&quot;
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

        CommandLineMessageDirector director = new CommandLineMessageDirector("convert.NCIOwlToLgXML");
        System.out.println("*** FROM FILEPATH: " + options.getOptionValue("IN"));
        System.out.println("***  TO DIRECTORY: " + options.getOptionValue("OUT"));

        String manUriStr = options.getOptionValue("MF");
        URI manifest = null;

        if (!StringUtils.isEmpty(manUriStr))
            manifest = URI.create(manUriStr.trim());

        NCIOwl in = new NCIOwl(options.getOptionValue("IN"), manifest);
        LexGridXMLOut out = new LexGridXMLOut(options.getOptionValue("OUT"));

        OptionHolder convertOptions = new OptionHolder();

        convertOptions.add(new edu.mayo.informatics.lexgrid.convert.formats.Option(
                edu.mayo.informatics.lexgrid.convert.formats.Option.FAIL_ON_ERROR, new Boolean(!options
                        .hasOption("NoFail"))));
        convertOptions.add(new edu.mayo.informatics.lexgrid.convert.formats.Option(
                edu.mayo.informatics.lexgrid.convert.formats.Option.OVERWRITE, Boolean.TRUE));

        ConversionLauncher.startConversion(in, out, null, convertOptions, director);

        System.out.println("");
        System.out.println("Done");
    }

    private static CommandLine parseCommandLineOptions(String[] args) {
        // create the command line parser
        CommandLineParser parser = new BasicParser();

        // create the Options
        Options options = new Options();

        Option temp = new Option("IN", "InFile", true, "Qualified path of the input file");
        temp.setRequired(true);
        temp.setArgName("IN");
        options.addOption(temp);

        temp = new Option("MF", "ManifestFile", true, "URI of the Manifest (xml) file");
        temp.setRequired(false);
        temp.setArgName("MF");
        options.addOption(temp);

        temp = new Option("OUT", "OutPath", true, "Path of the output directory");
        temp.setRequired(true);
        temp.setArgName("OUT");
        options.addOption(temp);

        CommandLine result = null;
        try {
            result = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println("");
            System.out.println(e.toString());
            System.out.println("");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(300, "java -Xmx300 -cp converter.jar cl.NCIOwlToLgXML", "", options, "", true);
            System.out.println("");
            System.out.println("Example: java -Xmx300 -cp converter.jar cl.NCIOwlToLgXML -IN in.owl -OUT .");
            System.out.println("");
            System.out
                    .println("Example: java -Xmx300 -cp converter.jar cl.NCIOwlToLgXML -IN in.owl -MF \"file:///path/to/in-manifest.xml\" -OUT .");
            System.exit(0);
        }
        return result;
    }
}