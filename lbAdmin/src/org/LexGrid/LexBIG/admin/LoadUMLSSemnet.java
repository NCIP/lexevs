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

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Load.UMLS_Loader;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.loaders.SemNetLoaderImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Preferences.loader.SemNetLoadPreferences.SemNetLoaderPreferences;
import org.LexGrid.LexBIG.Preferences.loader.SemNetLoadPreferences.types.InheritanceLevelType;
import org.LexGrid.annotations.LgAdminFunction;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.lexevs.system.ResourceManager;

/**
 * Loads the UMLS Semantic Network, provided as a collection of files in a
 * single directory. The following files are expected to be provided from the
 * National Library of Medicine (NLM) distribution:
 * 
 * <pre>
 *   - LICENSE.txt (text from distribution terms and conditions)
 *   - SRFIL.txt (File Description)
 *   - SRFIL.txt (Field Description)
 *   - SRDEF.txt (Basic information about the Semantic Types and Relations)
 *   - SRSTR.txt (Structure of the Network)
 *   - SRSTRE1.txt (Fully inherited set of Relations (UIs))
 *   - SRSTRE2.txt (Fully inherited set of Relations (Names))
 *   - SU.txt (Unit Record)
 * These files can be downloaded from the NLM web site at
 * http://semanticnetwork.nlm.nih.gov/Download/index.html.
 * Options:
 *   -in,--input &lt;uri&gt; URI or path of the directory containing the NLM files
 *   -mf,--manifest &lt;uri&gt; URI or path specifying location of the manifest file
 *   -il,--inheritance level &lt;uri&gt; If specified, indicates the extent of inherited relationships to import.  
 *         0 = none; 1 = all; 2 = all except is_a (default).  
 *         All direct relationships are imported, regardless of option.
 *   -v, --validate &lt;int&gt; Perform validation of the candidate
 *         resource without loading data.  If specified, the '-a' and '-t'
 *         options are ignored.  Supported levels of validation include:
 *         0 = Verify the existence of each required file
 *   -a, --activate ActivateScheme on successful load; if unspecified the vocabulary is loaded but not activated
 *   -t, --tag &lt;id&gt; An optional tag ID (e.g. 'PRODUCTION' or 'TEST') to assign.
 * Example: LoadUMLSSemnet -in &quot;file:///path/to/directory/&quot; -a -il 1
 *        LoadUMLSSemnet -in &quot;file:///path/to/directory/&quot; -v 0
 * 
 * </pre>
 * 
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 */
@LgAdminFunction
public class LoadUMLSSemnet {

    public static void main(String[] args) {
        try {
            new LoadUMLSSemnet().run(args);
        } catch (LBResourceUnavailableException e) {
            Util.displayAndLogMessage(e.getMessage());
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public LoadUMLSSemnet() {
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
                Util
                        .displayCommandOptions(
                                "LoadUMLSSemnet",
                                options,
                                "\n LoadUMLSSemnet -in \"file:///path/to/directory/\" -a -il 1"
                                        + "\n LoadUMLSSemnet -in \"file:///path/to/directory/\""
                                        + "\n LoadUMLSSemnet -in \"file:///path/to/directory/\" -mf \"file:///path/to/manifest/file.xml\""
                                        + Util.getURIHelp(), e);
                return;
            }

            // Interpret provided values ...
            String path = cl.getOptionValue("in");
            if (path.charAt(path.length() - 1) != '/')
                path += '/';
            URI source = Util.string2FileURI(path);

            String inheritanceLevel = cl.getOptionValue("il");

            SemNetLoaderPreferences loaderPrefObj = new SemNetLoaderPreferences();

            // Determine the inheritance level for the loader
            if (inheritanceLevel != null) {
                if (inheritanceLevel.equals(InheritanceLevelType.VALUE_0.toString()))
                    loaderPrefObj.setInheritanceLevel(InheritanceLevelType.VALUE_0);
                else if (inheritanceLevel.equals(InheritanceLevelType.VALUE_1.toString()))
                    loaderPrefObj.setInheritanceLevel(InheritanceLevelType.VALUE_1);
                else if (inheritanceLevel.equals(InheritanceLevelType.VALUE_2.toString()))
                    loaderPrefObj.setInheritanceLevel(InheritanceLevelType.VALUE_2);
            } else {// default
                loaderPrefObj.setInheritanceLevel(InheritanceLevelType.VALUE_2);
            }

            URI manifestURI = null;
            if (cl.getOptionValue("mf") != null) {
                manifestURI = Util.string2FileURI(cl.getOptionValue("mf"));
            }

            boolean activate = cl.hasOption("a");
     
                Util.displayAndLogMessage("LOADING FROM URI: " + source.toString());
                Util.displayAndLogMessage(activate ? "ACTIVATE ON SUCCESS" : "NO ACTIVATION");
   

            // Find the registered extension handling this type of load ...
            LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
            LexBIGServiceManager lbsm = lbs.getServiceManager(null);
            SemNetLoaderImpl loader = (SemNetLoaderImpl)lbsm.getLoader(org.LexGrid.LexBIG.Impl.loaders.SemNetLoaderImpl.name);
            

            // Set the loader preference.
            if (loaderPrefObj != null)
                loader.setLoaderPreferences(loaderPrefObj);

            // Set the manifest URI.
            if (manifestURI != null)
                loader.setCodingSchemeManifestURI(manifestURI);

            // Perform the requested load or validate action ...

                loader.load(source);
                Util.displayLoaderStatus(loader);


            // If specified, set the associated tag on the newly loaded
            // scheme(s) ...
            if ( cl.hasOption("t")) {
                String tag = cl.getOptionValue("t");
                AbsoluteCodingSchemeVersionReference[] refs = loader.getCodingSchemeReferences();
                for (int i = 0; i < refs.length; i++) {
                    AbsoluteCodingSchemeVersionReference ref = refs[i];
                    lbsm.setVersionTag(ref, tag);
                    Util.displayAndLogMessage("Tag assigned>> " + ref.getCodingSchemeURN() + " Version>> "
                            + ref.getCodingSchemeVersion());
                }
            }

            // If requested, activate the newly loaded scheme(s) ...
            if (activate) {
                AbsoluteCodingSchemeVersionReference[] refs = loader.getCodingSchemeReferences();
                for (int i = 0; i < refs.length; i++) {
                    AbsoluteCodingSchemeVersionReference ref = refs[i];
                    lbsm.activateCodingSchemeVersion(ref);
                    Util.displayAndLogMessage("Scheme activated>> " + ref.getCodingSchemeURN() + " Version>> "
                            + ref.getCodingSchemeVersion());
                }
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

        o = new Option("in", "input", true, "URI or path specifying the directory containing the NLM files.");
        o.setArgName("uri");
        o.setRequired(true);
        options.addOption(o);

        o = new Option("mf", "manifest", true, "URI or path specifying location of the manifest file.");
        o.setArgName("uri");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("il", "InheritanceLevel", true,
                "If specified, indicates the extent of inherited relationships to import. "
                        + "0 = none; 1 = all; 2 = all except is_a (default). "
                        + "All direct relationships are imported, regardless of option.");
        o.setArgName("uri");
        o.setRequired(false);
        options.addOption(o);


        o = new Option("a", "activate", false, "ActivateScheme on successful load; if unspecified the "
                + "vocabulary is loaded but not activated.");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("t", "tag", true, "An optional tag ID (e.g. 'PRODUCTION' or 'TEST') to assign.");
        o.setArgName("id");
        o.setRequired(false);
        options.addOption(o);

        return options;
    }

}