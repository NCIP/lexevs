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
import org.LexGrid.LexBIG.Extensions.Load.RadlexProtegeFrames_Loader;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.loaders.RadLexProtegeFramesLoaderImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.annotations.LgAdminFunction;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.lexevs.system.ResourceManager;

/**
 * Imports from an FMA database to a LexBIG repository. require that the pprj
 * file be configured with database URN, username, password for an FMA MySQL
 * based database The FMA.pprj file and MySQL dump file are available at
 * http://sig.biostr.washington.edu/projects/fm/ upon registration.
 * 
 * <pre>
 * Example: java org.LexGrid.LexBIG.admin.LoadFMA
 *   -in,--input &lt;uri&gt; URI or path specifying location of the source file
 *   -mf,--manifest &lt;uri&gt; URI or path specifying location of the manifest file
 *   -v, --validate &lt;int&gt; Perform validation of the candidate
 *         resource without loading data.  If specified, the '-a' and '-t'
 *         options are ignored.  Supported levels of validation include:
 *         0 = Verify document is well-formed
 *         1 = Verify document is valid
 *   -a, --activate ActivateScheme on successful load; if unspecified the vocabulary is loaded but not activated
 *   -t, --tag &lt;id&gt; An optional tag ID (e.g. 'PRODUCTION' or 'TEST') to assign. 
 * 
 * Example: java -Xmx800m -cp lgRuntime.jar
 *  org.LexGrid.LexBIG.admin.LoadFMA -in &quot;file:///path/to/FMA.pprj&quot; -a
 * -or-
 *  org.LexGrid.LexBIG.admin.LoadFMA -in &quot;file:///path/to/FMA.pprj&quot; -v 0
 * </pre>
 * 
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 */
@LgAdminFunction
public class LoadRadLexProtegeFrames {

    public static void main(String[] args) {
        try {
            new LoadRadLexProtegeFrames().run(args);
        } catch (LBResourceUnavailableException e) {
            Util.displayTaggedMessage(e.getMessage());
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public LoadRadLexProtegeFrames() {
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
                Util.displayCommandOptions("LoadRadLexProtegeFrames", options,
                        "\n LoadRadLexProtegeFrames -in \"file:///path/to/RadLex.pprj\" -a"
                                + "\n LoadRadLexProtegeFrames -in \"file:///path/to/RadLex.pprj\" -v 0"
                                + Util.getURIHelp(), e);
                return;
            }

            // Interpret provided values ...
            URI source = Util.string2FileURI(cl.getOptionValue("in"));
            URI manifest = null;
            if (cl.getOptionValue("mf") != null) {
                manifest = Util.string2FileURI(cl.getOptionValue("mf"));
            }
            boolean activate = vl < 0 && cl.hasOption("a");
            if (vl >= 0) {
                Util.displayTaggedMessage("VALIDATING SOURCE URI: " + source.toString());
            } else {
                Util.displayTaggedMessage("LOADING FROM URI: " + source.toString());
                if (manifest != null) {
                    Util.displayTaggedMessage("MANIFEST FILE URI: " + manifest.toString());
                }
                Util.displayTaggedMessage(activate ? "ACTIVATE ON SUCCESS" : "NO ACTIVATION");
            }

            // Find the registered extension handling this type of load ...
            LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
            LexBIGServiceManager lbsm = lbs.getServiceManager(null);
            RadlexProtegeFrames_Loader loader = (RadLexProtegeFramesLoaderImpl) lbsm
                    .getLoader(org.LexGrid.LexBIG.Impl.loaders.RadLexProtegeFramesLoaderImpl.name);

            if (manifest != null) {
                loader.setCodingSchemeManifestURI(manifest);
            }
            // Perform the requested load or validate action ...
            if (vl >= 0) {
                loader.validate(source, vl);
                Util.displayTaggedMessage("VALIDATION SUCCESSFUL");
            } else {
                loader.load(source, false, true);
                Util.displayLoaderStatus(loader);
            }

            // If specified, set the associated tag on the newly loaded
            // scheme(s) ...
            if (vl < 0 && cl.hasOption("t")) {
                String tag = cl.getOptionValue("t");
                AbsoluteCodingSchemeVersionReference[] refs = loader.getCodingSchemeReferences();
                for (int i = 0; i < refs.length; i++) {
                    AbsoluteCodingSchemeVersionReference ref = refs[i];
                    lbsm.setVersionTag(ref, tag);
                    Util.displayTaggedMessage("Tag assigned>> " + ref.getCodingSchemeURN() + " Version>> "
                            + ref.getCodingSchemeVersion());
                }
            }

            // If requested, activate the newly loaded scheme(s) ...
            if (activate) {
                AbsoluteCodingSchemeVersionReference[] refs = loader.getCodingSchemeReferences();
                for (int i = 0; i < refs.length; i++) {
                    AbsoluteCodingSchemeVersionReference ref = refs[i];
                    lbsm.activateCodingSchemeVersion(ref);
                    Util.displayTaggedMessage("Scheme activated>> " + ref.getCodingSchemeURN() + " Version>> "
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

        o = new Option("in", "input", true, "URI or path specifying location of the pprj file."
                + "\n Imports from a RadLex xml file to a LexBIG repository."
                + "\n Requires that the pprj file be configured with reference to" + "\n a RadLex xml file as follows:"
                + "\n([radlex_ProjectKB_Instance_66] of  String" + "\n(name \"source_file_name\")"
                + "\n(string_value \"radlex.xml\"))");
        o.setArgName("uri");
        o.setRequired(true);
        options.addOption(o);

        o = new Option("mf", "manifest", true, "URI or path specifying location of the manifest file.");
        o.setArgName("uri");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("v", "validate", true, "Validation only; no load. If specified, 'a' and 't' "
                + "are ignored. 0 to verify well-formed xml; 1 to check validity.");
        o.setArgName("int");
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