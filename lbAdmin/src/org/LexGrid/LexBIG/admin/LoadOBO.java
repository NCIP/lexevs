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
import org.LexGrid.LexBIG.Extensions.Load.OBO_Loader;
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
 * Imports a file specified in the Open Biomedical Ontologies (OBO) format to
 * the LexBIG repository.
 * 
 * <pre>
 * Example: java org.LexGrid.LexBIG.admin.LoadOBO
 *   -in,--input &lt;uri&gt; URI or path specifying location of the source file
 *   -v, --validate &lt;int&gt; Perform validation of the candidate
 *         resource without loading data.  If specified, the '-a' and '-t'
 *         options are ignored.  Supported levels of validation include:
 *         0 = Verify document is valid
 *   -a, --activate ActivateScheme on successful load; if unspecified the vocabulary is loaded but not activated
 *   -t, --tag &lt;id&gt; An optional tag ID (e.g. 'PRODUCTION' or 'TEST') to assign. 
 * 
 * Example: java -Xmx512m -cp lgRuntime.jar
 *  org.LexGrid.LexBIG.admin.LoadOBO -in &quot;file:///path/to/file.obo&quot; -a
 * -or-
 *  org.LexGrid.LexBIG.admin.LoadOBO -in &quot;file:///path/to/file.obo&quot; -v 0
 * </pre>
 * 
 * @author <A HREF="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala</A>
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 */
@LgAdminFunction
public class LoadOBO {

    public static void main(String[] args) {
        try {
            new LoadOBO().run(args);
        } catch (LBResourceUnavailableException e) {
            Util.displayTaggedMessage(e.getMessage());
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public LoadOBO() {
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
                Util
                        .displayCommandOptions(
                                "LoadOBO",
                                options,
                                "\n LoadOBO -in \"file:///path/to/file.obo\" -a"
                                        + "\n LoadOBO -in \"file:///path/to/file.obo\"  -mf \"file:///path/to/myCodingScheme-manifest.xml\" -a"
                                        + "\n LoadOBO -in \"file:///path/to/file.obo\" -v 0" + Util.getURIHelp(), e);
                return;
            }

            // Interpret provided values ...
            String manUriStr = cl.getOptionValue("mf");
            URI manifest = null;

            if (!StringUtils.isNull(manUriStr))
                manifest = Util.string2FileURI(manUriStr);

            URI source = Util.string2FileURI(cl.getOptionValue("in"));
            boolean activate = vl < 0 && cl.hasOption("a");
            if (vl >= 0) {
                Util.displayTaggedMessage("VALIDATING SOURCE URI: " + source.toString());
            } else {
                Util.displayTaggedMessage("LOADING FROM URI: " + source.toString());
                Util.displayTaggedMessage(activate ? "ACTIVATE ON SUCCESS" : "NO ACTIVATION");
            }

            // Find the registered extension handling this type of load ...
            LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
            LexBIGServiceManager lbsm = lbs.getServiceManager(null);
            OBO_Loader loader = (OBO_Loader) lbsm.getLoader(org.LexGrid.LexBIG.Impl.loaders.OBOLoaderImpl.name);

            // Perform the requested load or validate action ...
            if (vl >= 0) {
                loader.validate(source, null, vl);
                Util.displayTaggedMessage("VALIDATION SUCCESSFUL");
            } else {
                loader.setCodingSchemeManifestURI(manifest);
                loader.load(source, null, false, true);
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

        o = new Option("in", "input", true, "URI or path specifying location of the source file.");
        o.setArgName("uri");
        o.setRequired(true);
        options.addOption(o);

        o = new Option("mf", "manifest", true, "URI or path specifying location of the manifest file.");
        o.setArgName("uri");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("v", "validate", true, "Validation only; no load. If specified, 'a' and 't' "
                + "are ignored. 0 to verify the file conforms to the OBO format.");
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