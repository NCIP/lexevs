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

import java.net.URI;

import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Load.UMLSHistoryLoader;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.lexevs.system.ResourceManager;

/**
 * Loads UMLS History
 * 
 * <pre>
 * Example: java org.LexGrid.LexBIG.admin.LoadOWL
 *   -in,--input &lt;uri&gt; URI or path specifying location of the source file
 *   -v, --validate &lt;int&gt; Perform validation of the candidate
 *         resource without loading data.  If specified, the '-r'
 *         option is ignored.  Supported levels of validation include:
 *         0 = Verify document is well-formed
 *         1 = Verify document is valid
 *   -r, --replace Replace exisiting file. 
 * 
 * Example: java -Xmx512m -cp lgRuntime.jar
 *  org.LexGrid.LexBIG.admin.LoadUMLSHistory -in &quot;file:///path/to/META.folder&quot; -r
 * -or-
 *  org.LexGrid.LexBIG.admin.LoadUMLSHistory -in &quot;file:///path/to/META.folder&quot; -v 0
 * </pre>
 */
public class LoadUMLSHistory {

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            new LoadUMLSHistory().run(args);
        } catch (LBResourceUnavailableException e) {
            Util.displayAndLogError("Resource Unavailable: " + e.getMessage() , e);
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }

    }

    public LoadUMLSHistory() {
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
            int vl = -1;
            try {
                cl = new BasicParser().parse(options, args);
                if (cl.hasOption("v"))
                    vl = Integer.parseInt(cl.getOptionValue("v"));
            } catch (ParseException e) {
                Util.displayCommandOptions("LoadNCIMetaHistory", options,
                        "\n LoadUMLSHistory -in \"file:///path/to/META.folder\" -r" + Util.getURIHelp(), e);
                return;
            }

            // Interpret provided values ...
            URI metaFolder = Util.string2FileURI(cl.getOptionValue("in"));
            boolean replace = vl < 0 && cl.hasOption("r");
            if (vl >= 0) {
                Util.displayAndLogMessage("VALIDATING SOURCE URI: " + metaFolder.toString());
            } else {
                Util.displayAndLogMessage("LOADING FROM URI: " + metaFolder.toString());
                Util.displayAndLogMessage(replace ? "REPLACING THE EXISTING HISTORY DATA, IF ANY."
                        : "APPEDNING TO THE EXISTING HISTORY DATA, IF ANY.");
            }

            // Find the registered extension handling this type of load ...
            LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
            LexBIGServiceManager lbsm = lbs.getServiceManager(null);
            UMLSHistoryLoader loader = (UMLSHistoryLoader) lbsm
                    .getLoader(org.LexGrid.LexBIG.Impl.loaders.UMLSHistoryLoaderImpl.name);

            // Perform the load ...
            if (vl >= 0) {
                loader.validate(metaFolder, vl);
                Util.displayAndLogMessage("VALIDATION SUCCESSFUL");
            } else {
                loader.load(metaFolder, !replace, false, true);
                Util.displayLoaderStatus(loader);
                Util.displayAndLogMessage("Final History Loader Status: " + loader.getStatus().getState().name());
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

        o = new Option("in", "input", true, "URI or path specifying location of the source files.");
        o.setArgName("uri");
        o.setRequired(true);
        options.addOption(o);

        o = new Option("v", "validate", true, "Validation only; no load. If specified, 'r' is ignored.");
        o.setArgName("int");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("r", "replace", true, "If specified, the current history database is overwritten. "
                + "If not, history is added.");
        o.setRequired(false);
        options.addOption(o);

        return options;
    }

}