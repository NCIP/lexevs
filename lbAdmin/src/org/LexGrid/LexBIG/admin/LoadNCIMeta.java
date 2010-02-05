/*
 * Copyright: (c) 2004-2007 Mayo Foundation for Medical Education and 
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
import org.LexGrid.LexBIG.Extensions.Load.NCI_MetaThesaurusLoader;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.dataAccess.ResourceManager;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.annotations.LgAdminFunction;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import edu.mayo.informatics.resourcereader.core.StringUtils;

/**
 * Imports the NCI MetaThesaurus, provided as a collection of RRF files.
 * 
 * <pre>
 * Example: java org.LexGrid.LexBIG.admin.LoadNCIMeta
 *   -in,--input &lt;uri&gt; URI or path of the directory containing the RRF files
 *   -mf,--manifest &lt;uri&gt; URI or path specifying location of the manifest file
 *   -lp,--loadPrefs &lt;uri&gt; URI or path specifying location Loader Preferences file
 *   -v, --validate &lt;int&gt; Perform validation of the candidate
 *         resource without loading data.  If specified, the '-rt', '-a' and
 *         '-t' options are ignored.  Supported levels of validation include:
 *         0 = Verify existence and format (up to 100 lines) of each required file
 *   -rr,--rootRecalc If specified, indicates that only root nodes are to be
 *         reevaluated for an already loaded source (if present).
 *   -a, --activate ActivateScheme on successful load; if unspecified the vocabulary is loaded but not activated
 *   -t, --tag &lt;id&gt; An optional tag ID (e.g. 'PRODUCTION' or 'TEST') to assign.
 *   
 * Example: java -Xmx512m -cp lgRuntime.jar
 *  org.LexGrid.LexBIG.admin.LoadNCIMeta -in &quot;file:///path/to/directory/&quot; -a
 * -or-
 *  org.LexGrid.LexBIG.admin.LoadNCIMeta -in &quot;file:///path/to/directory/&quot; -v 0
 * </pre>
 * 
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 */
@LgAdminFunction
public class LoadNCIMeta {

    public static void main(String[] args) {
        try {
            new LoadNCIMeta().run(args);
        } catch (LBResourceUnavailableException e) {
            Util.displayTaggedMessage(e.getMessage());
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public LoadNCIMeta() {
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
                Util.displayCommandOptions("LoadNCIMeta", options,
                        "\n LoadNCIMeta -in \"file:///path/to/directory/\" -a"
                                + "\n LoadNCIMeta -in \"file:///path/to/directory/\" -v 0" + Util.getURIHelp(), e);
                return;
            }

            // Interpret provided values ...
            boolean rootRecalc = cl.hasOption("rr");
            String path = cl.getOptionValue("in");
            if (path.charAt(path.length() - 1) != '/')
                path += '/';
            URI source = Util.string2FileURI(path);
            boolean activate = vl < 0 && !rootRecalc && cl.hasOption("a");
            if (vl >= 0) {
                Util.displayTaggedMessage("VALIDATING SOURCE URI: " + source.toString());
            } else {
                Util.displayTaggedMessage("LOADING FROM URI: " + source.toString());
                Util.displayTaggedMessage(activate ? "ACTIVATE ON SUCCESS" : "NO ACTIVATION");
            }

            String manUriStr = cl.getOptionValue("mf");
            URI manifest = null;

            if (!StringUtils.isNull(manUriStr))
                manifest = Util.string2FileURI(manUriStr);

            // Find the registered extension handling this type of load ...
            LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
            LexBIGServiceManager lbsm = lbs.getServiceManager(null);
            NCI_MetaThesaurusLoader loader = (NCI_MetaThesaurusLoader) lbsm
                    .getLoader(org.LexGrid.LexBIG.Impl.loaders.NCIMetaThesaurusLoaderImpl.name);

            // set the manifest
            loader.setCodingSchemeManifestURI(manifest);

            // get Loader Preferences
            String loaderPrefsUriStr = cl.getOptionValue("lp");
            URI loaderPrefs = null;

            // set Loader Preferences
            if (!StringUtils.isNull(loaderPrefsUriStr)) {
                loaderPrefs = Util.string2FileURI(loaderPrefsUriStr);
                loader.setLoaderPreferences(loaderPrefs);
            }

            // Perform the requested load or validate action ...
            if (vl >= 0) {
                loader.validate(source, vl);
                Util.displayTaggedMessage("VALIDATION SUCCESSFUL");
            } else if (rootRecalc) {
                Util.displayTaggedMessage("RECALCULATING ROOT NODES");
                loader.recalcRootNodes(source, true);
                Util.displayLoaderStatus(loader);
            } else {
                loader.load(source, false, true);
                Util.displayLoaderStatus(loader);
            }

            // If specified, set the associated tag on the newly loaded
            // scheme(s) ...
            if (vl < 0 && !rootRecalc && cl.hasOption("t")) {
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

        o = new Option("in", "input", true, "URI or path specifying the directory containing the RRF files.");
        o.setArgName("uri");
        o.setRequired(true);
        options.addOption(o);

        o = new Option("mf", "manifest", true, "URI or path specifying location of the manifest file.");
        o.setArgName("uri");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("lp", "loadPrefs", true, "URI or path specifying location of the Loader Preferences file.");
        o.setArgName("uri");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("v", "validate", true, "Validation only; no load. If specified, 'a' and 't' "
                + "are ignored. 0 to verify files exist and format of initial entries.");
        o.setArgName("int");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("rr", "rootRecalc", false, "If specified, indicates that only root nodes are to be "
                + "reevaluated for an already loaded source (if present).");
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