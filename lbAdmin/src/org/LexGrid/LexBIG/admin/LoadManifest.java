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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Enumeration;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Load.MetaData_Loader;
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

/**
 * Load manifest data onto the codingscheme based on unique URN and version.
 * 
 * <pre>
 * Example: java org.LexGrid.LexBIG.admin.LoadManifest
 *  -u,--urn &amp;lturn&amp;gt; URN uniquely identifying the code system.
 *  -v,--version &amp;ltversionId&amp;gt; Version identifier.
 *  -mf,--manifest &amp;ltmanifest&amp;gt; location of manifest xml file.
 * 
 * Note: If either the URN or the version values are unspecified, a
 * list of available coding schemes will be presented for
 * user selection.
 * 
 * Example: java -Xmx512m -cp lgRuntime.jar
 * org.LexGrid.LexBIG.admin.LoadManifest
 *   -u &quot;urn:oid:2.16.840.1.113883.3.26.1.1&quot; -v &quot;05.09e&quot; -mf &quot;c:/manifest.xml&quot;
 * </pre>
 * 
 * @author <A HREF="mailto:rao.ramachandra@mayo.edu">Rao</A>
 */
@LgAdminFunction
public class LoadManifest {

    final static String METADATALOADER = "MetaDataLoader";

    public static void main(String[] args) {
        try {
            new LoadManifest().run(args);
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public LoadManifest() {
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
                Util.displayCommandOptions("LoadManifest", options,
                        "\n LoadManifest -u \"urn:oid:2.16.840.1.113883.3.26.1.1\" -v \"05.09e\" -mf \"file://path//to//manifest.xml\""
                                + "\n LoadManifest -mf \"file://path//to//manifest.xml\"", e);
                Util.displayMessage(Util.getPromptForSchemeHelp());
                return;
            }

            // Interpret provided values ...
            String urn = cl.getOptionValue("u");
            String ver = cl.getOptionValue("v");

            String manifest = cl.getOptionValue("mf");
            manifest = manifest.trim();

            try {
                getReader(new URI(manifest));
            } catch (Exception e) {
                Util.displayMessage("Supplied manifest file is invalid or do not exist.");
                return;
            }

            CodingSchemeSummary css = null;

            // Find in list of registered vocabularies ...
            if (urn != null && ver != null) {
                urn = urn.trim();
                ver = ver.trim();

                LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
                Enumeration<? extends CodingSchemeRendering> schemes = lbs.getSupportedCodingSchemes()
                        .enumerateCodingSchemeRendering();
                while (schemes.hasMoreElements() && css == null) {
                    CodingSchemeSummary summary = schemes.nextElement().getCodingSchemeSummary();
                    if (urn.equalsIgnoreCase(summary.getCodingSchemeURI())
                            && ver.equalsIgnoreCase(summary.getRepresentsVersion()))
                        css = summary;
                }
            }

            // Found it? If not, prompt...
            if (css == null) {
                if (urn != null || ver != null) {
                    Util.displayMessage("No matching coding scheme was found for the given URN or version.");
                    Util.displayMessage("");
                }
                css = Util.promptForCodeSystem();
                if (css == null)
                    return;
            }

            LexBIGServiceManager lbsm = LexBIGServiceImpl.defaultInstance().getServiceManager(new Object());

            MetaData_Loader mdLoader = (MetaData_Loader) lbsm.getLoader(METADATALOADER);
            AbsoluteCodingSchemeVersionReference codingSchemeURNVersion = new AbsoluteCodingSchemeVersionReference();
            codingSchemeURNVersion.setCodingSchemeURN(css.getCodingSchemeURI());
            codingSchemeURNVersion.setCodingSchemeVersion(css.getRepresentsVersion());

            try {
                mdLoader.loadLexGridManifest(new URI(manifest), codingSchemeURNVersion, false, true);
                Util.displayMessage("Manifest data applied successfully on the codingscheme.");
            } catch (LBException e) {
                Util.displayMessage("Load failed: " + e.getMessage());
                e.printStackTrace();
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

        o = new Option("u", "urn", true, "URN uniquely identifying the code system.");
        o.setArgName("name");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("v", "version", true, "Version identifier.");
        o.setArgName("id");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("mf", "manifest", true, "manifest file URI.");
        o.setArgName("file:///path/to/manifest.xml");
        o.setRequired(true);
        options.addOption(o);

        return options;
    }

    private static BufferedReader getReader(URI filePath) throws MalformedURLException, IOException {
        BufferedReader reader = null;
        if (filePath.getScheme().equals("file")) {

            reader = new BufferedReader(new FileReader(new File(filePath)));
        } else {

            reader = new BufferedReader(new InputStreamReader(filePath.toURL().openConnection().getInputStream()));
        }
        return reader;
    }

}