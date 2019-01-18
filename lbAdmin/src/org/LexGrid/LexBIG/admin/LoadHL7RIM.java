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
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Load.HL7_Loader;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.annotations.LgAdminFunction;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.lexevs.system.ResourceManager;

import edu.mayo.informatics.resourcereader.core.StringUtils;

/**
 * Converts an HL7 RIM MS Access database to a LexGrid database
 * 
 * <pre>
 * Example: java org.LexGrid.LexBIG.admin.LoadHL7RIM
 *   -in,--input &lt;uri&gt; URI or path specifying location of the source file
 *   -mf,--manifest &lt;uri&gt; URI or path specifying location of the manifest file
 *   -lp,--load preferences &lt;uri&gt; URI or path specifying location of the load preferences file
 *   -v, --validate &lt;int&gt; Perform validation of the candidate
 *         resource without loading data.  If specified, the '-a' and '-t'
 *         options are ignored.  Supported levels of validation include:
 *         0 = Verify document is valid
 *   -a, --activate ActivateScheme on successful load; if unspecified the vocabulary is loaded but not activated
 *   -t, --tag &lt;id&gt; An optional tag ID (e.g. 'PRODUCTION' or 'TEST') to assign. 
 * 
 * Example: java -Xmx512m -cp lgRuntime.jar
 *  org.LexGrid.LexBIG.admin.LoadHL7RIM -in &quot;file:///path/to/file.mdb&quot; -a
 * -or-
 *  org.LexGrid.LexBIG.admin.LoadHL7RIM -in &quot;file:///path/to/file.mdb&quot; -v 0
 * </pre>
 * 
 * @author <A HREF="mailto:bauer.scott@mayo.edu">Scott Bauer</A>
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 * @author <A HREF="mailto:stancl.craig@mayo.edu">Craig Stancl</A>
 */
@LgAdminFunction
public class LoadHL7RIM {

    public static void main(String[] args) {
        try {
            new LoadHL7RIM().run(args);
        } catch (LBResourceUnavailableException e) {
            Util.displayAndLogMessage(e.getMessage());
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public LoadHL7RIM() {
        super();
    }

    /**
     * Primary entry point for the program.
     * @throws Exception 
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
                Util
                        .displayCommandOptions(
                                "LoadHL7RIM",
                                options,
                                "\n LoadHL7RIM -in \"file:///path/to/file.mdb\" -a"
                                        + "\n LoadHL7RIM -in \"file:///path/to/file.mdb\"  -mf \"file:///path/to/myCodingScheme-manifest.xml\"-a"
                                        + "\n LoadHL7RIM -in \"file:///path/to/file.mdb\" -v 0" + Util.getURIHelp(), e);
                return;
            }

            // Interpret provided values ...

            String manUriStr = cl.getOptionValue("mf");
            URI manifest = null;

            if (!StringUtils.isNull(manUriStr))
                manifest = Util.string2FileURI(manUriStr);

            String lpUriStr = cl.getOptionValue("lp");
            URI loaderPrefs = null;

            if (!StringUtils.isNull(lpUriStr))
                loaderPrefs = Util.string2FileURI(lpUriStr);

            String dbPath = cl.getOptionValue("in");
            boolean activate = vl < 0 && cl.hasOption("a");
            if (vl >= 0) {
                Util.displayAndLogMessage("VALIDATING MSACCESS: " + dbPath);
            } else {
                Util.displayAndLogMessage("LOADING FROM MSACCESS DB: " + dbPath);
                Util.displayAndLogMessage(activate ? "ACTIVATE ON SUCCESS" : "NO ACTIVATION");
            }

            // Find the registered extension handling this type of load ...
            LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
            LexBIGServiceManager lbsm = lbs.getServiceManager(null);
            HL7_Loader loader = (HL7_Loader) lbsm.getLoader(org.LexGrid.LexBIG.Impl.loaders.HL7LoaderImpl.name);

            // Perform the requested load or validate action ...
            if (vl >= 0) {
                Util.displayAndLogMessage("VALIDATION SUCCESSFUL");
            } else {

                // may want to hard code to false the loading thread.

                // updated to include manifest
                loader.setCodingSchemeManifestURI(manifest);

                // updated to include loader preferences
                if (loaderPrefs != null) {
                    loader.setLoaderPreferences(loaderPrefs);
                }

                try {
                    loader.load(dbPath, false, true);
                } catch (LBException e) {
                    Util
                    .displayCommandOptions(
                            "LoadHL7RIM",
                            options,"Check source uri and try again" +
                            "Path requires a forward slash as designated below" +
                            "\n LoadHL7RIM -in \"file:///path/to/file.mdb\" -a"
                                    + "\n LoadHL7RIM -in \"file:///path/to/file.mdb\"  -mf \"file:///path/to/myCodingScheme-manifest.xml\"-a"
                                    + "\n LoadHL7RIM -in \"c:///path/to/file.mdb\" -v 0" + Util.getURIHelp(), e);
                }
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

        o = new Option("in", "input", true, "URI or path specifying location of the source file.");
        o.setArgName("uri");
        o.setRequired(true);
        options.addOption(o);

        o = new Option("mf", "manifest", true, "URI or path specifying location of the manifest file.");
        o.setArgName("uri");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("lp", "loaderPrefs", true, "URI or path specifying location of the loader preference file.");
        o.setArgName("uri");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("v", "validate", true, "Validation only; no load. If specified, 'a' and 't' "
                + "are ignored. 0 to verify the file conforms to the HL7RIM format.");
        o.setArgName("int");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("d", "driver", true, "Name of the JDBC driver to use when accessing the database.");
        o.setArgName("drv");
        o.setRequired(false);

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