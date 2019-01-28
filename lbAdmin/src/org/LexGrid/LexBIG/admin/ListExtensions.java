/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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
package org.LexGrid.LexBIG.admin;

import java.util.Enumeration;

import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.ObjectToString;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.lexevs.system.ResourceManager;

/**
 * List registered extensions to the LexBIG runtime.
 * 
 * <pre>
 * Example: java org.LexGrid.LexBIG.admin.ListExtensions
 *   -a,--all List all extensions (default, override by specifying other options).
 *   -i,--index List index extensions.
 *   -m,--match List match algorithm extensions.
 *   -s,--sort List sort algorithm extensions.
 *   -f,--filter List filter extensions.
 *   -g,--generic List generic extensions.
 * 
 *  Example: java -Xmx512m -cp lgRuntime.jar
 *   org.LexGrid.LexBIG.admin.ListExtensions -a
 * </pre>
 * 
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 */
public class ListExtensions {

    public static void main(String[] args) {
        try {
            new ListExtensions().run(args);
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public ListExtensions() {
        super();
    }

    /**
     * Primary entry point for the program.
     * 
     * @throws Exception
     */
    public void run(String[] args) throws Exception {
        synchronized (ResourceManager.instance()) {

            // Parse the command line ...
            CommandLine cl = null;
            Options options = getCommandOptions();
            try {
                cl = new BasicParser().parse(options, args);
            } catch (ParseException e) {
                Util.displayAndLogError("Parsing command line options failed: " + e.getMessage() , e);
                Util.displayCommandOptions("ListExtensions", options, "ListExtensions -a", e);
                return;
            }

            // Interpret provided values ...
            boolean listAll = cl.hasOption("a") || cl.getOptions().length == 0;
            LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
            LexBIGServiceManager lbsm = lbs.getServiceManager(null);

            // Generic extensions ...
            if (listAll || cl.hasOption("g")) {
                list("GENERIC EXTENSIONS", lbs.getGenericExtensions().enumerateExtensionDescription());
            }
            // Index extensions ...
            if (listAll || cl.hasOption("i")) {
                list("INDEX EXTENSIONS (NOTE: DOES NOT INCLUDE BUILT-IN INDICES REQUIRED BY THE RUNTIME)", lbsm
                        .getIndexExtensions().enumerateExtensionDescription());
            }
            // Match algorithms ...
            if (listAll || cl.hasOption("m")) {
                list("MATCH ALGORITHMS", lbs.getMatchAlgorithms().enumerateModuleDescription());
            }
            // Filter algorithms ...
            if (listAll || cl.hasOption("f")) {
                list("MATCH ALGORITHMS", lbs.getFilterExtensions().enumerateExtensionDescription());
            }
            // Sort algorithms ...
            if (listAll || cl.hasOption("s")) {
                list("SORT ALGORITHMS", lbs.getSortAlgorithms(null).enumerateSortDescription());
            }
        }
    }

    /**
     * Return supported command options.
     * 
     * @return org.apache.commons.cli.Options
     */
    private Options getCommandOptions() {
        Options options = new Options();
        Option o;

        o = new Option("a", "all", false,
                "List all registered extensions (default, override by specifying other options).");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("i", "index", false, "List index extensions.");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("f", "filter", false, "List filter extensions.");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("m", "match", false, "List match algorithm extensions.");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("s", "sort", false, "List sort algorithm extensions.");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("g", "generic", false, "List generic extensions.");
        o.setRequired(false);
        options.addOption(o);

        return options;
    }

    /**
     * Print information for the given list of extensions.
     * 
     * @param label
     * @param list
     */
    private void list(String label, Enumeration extensions) {
        Util.displayAndLogMessage(label);
        while (extensions.hasMoreElements()) {
            Util.displayAndLogMessage(ObjectToString.toString(extensions.nextElement()));
            Util.displayAndLogMessage("");
        }
    }
}