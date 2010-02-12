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
import java.util.StringTokenizer;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Load.UMLS_Loader;
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
 * Loads UMLS content, provided as a collection of RRF files in a single
 * directory. Files may comprise the entire UMLS distribution or pruned via the
 * MetamorphoSys tool. A complete list of source vocabularies is available
 * online at http://www.nlm.nih.gov/research/umls/metaa1.html.
 * 
 * <pre>
 * Options:
 *   -in,--input &lt;uri&gt; Location of the source database. Typically this is
 *          specified in the form of a URL that indicates the database
 *          server, port, name, and optional properties.
 *   -u,--uid User ID for authenticated access, if required and not
 *          specified as part of the input URL.
 *   -p,--pwd Password for authenticated access, if required and not
 *          specified as part of the input URL.
 *   -d,--driver Name of the JDBC driver to use when accessing the database.
 *   -s,--sources Comma-delimited list of source vocabularies to load.
 *          If absent, all available vocabularies are loaded.
 *   -v, --validate &lt;int&gt; Perform validation of the candidate resource
 *          without loading data.  If specified, the '-ef', -a' and '-t'
 *          options are ignored. Supported levels of validation include:
 *          0 = Verify the existence of each required file
 *   -a, --activate ActivateScheme on successful load; if unspecified the
 *          vocabulary is loaded but not activated.
 *   -t, --tag &lt;id&gt; An optional tag ID (e.g. 'PRODUCTION' or 'TEST')
 *         to assign.
 * Example: LoadUMLSDatabase -in &quot;jdbc:postgresql://localhost:5432/lexgrid&quot;
 *          -d &quot;org.postgresql.Driver&quot; -u &quot;myDatabaseUser&quot; -p &quot;myPassword&quot;
 *          -s &quot;ICD9CM_2005,ICD9CM_2006&quot; -a
 *        LoadUMLSDatabase -in &quot;jdbc:postgresql://localhost:5432/lexgrid&quot;
 *          -d &quot;org.postgresql.Driver&quot; -u &quot;myDatabaseUser&quot; -p &quot;myPassword&quot;
 *          -v 0
 * </pre>
 * 
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 */
@LgAdminFunction
public class LoadUMLSDatabase {

    public static void main(String[] args) {
        try {
            new LoadUMLSDatabase().run(args);
        } catch (LBResourceUnavailableException e) {
            Util.displayTaggedMessage(e.getMessage());
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public LoadUMLSDatabase() {
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
                Util.displayCommandOptions("LoadUMLSDatabase", options,
                        "\n LoadUMLSDatabase -in \"jdbc:postgresql://localhost:5432/lexgrid\""
                                + "\n\t -d \"org.postgresql.Driver\" -u \"myDatabaseUser\" -p \"myPassword\""
                                + "\n\t -s \"ICD9CM_2005,ICD9CM_2006\" -a"
                                + "\n LoadUMLSDatabase -in \"jdbc:postgresql://localhost:5432/lexgrid\""
                                + "\n\t -d \"org.postgresql.Driver\" -u \"myDatabaseUser\" -p \"myPassword\""
                                + "\n\t -v 0" + Util.getURIHelp(), e);
                return;
            }

            // Interpret provided values ...
            URI source = new URI(cl.getOptionValue("in"));
            String driver = cl.getOptionValue("d");
            String userid = cl.hasOption("u") ? cl.getOptionValue("u") : null;
            String passwd = cl.hasOption("p") ? cl.getOptionValue("p") : null;
            boolean activate = vl < 0 && cl.hasOption("a");

            LocalNameList vocabularies = new LocalNameList();
            String srcVocab = cl.getOptionValue("s");
            if (srcVocab != null && (srcVocab = srcVocab.trim()).length() > 0) {
                StringTokenizer st = new StringTokenizer(srcVocab, ", ");
                while (st.hasMoreTokens())
                    vocabularies.addEntry(st.nextToken());
            }

            // Initial message to the user ...
            if (vl >= 0) {
                Util.displayTaggedMessage("VALIDATING SOURCE URI: " + source.toString());
            } else {
                Util.displayTaggedMessage("LOADING FROM DB: " + source.toString());
                Util.displayTaggedMessage("LOADING VOCABULARIES: "
                        + ((vocabularies.getEntryCount() > 0) ? srcVocab : "ALL"));
                Util.displayTaggedMessage(activate ? "ACTIVATE ON SUCCESS" : "NO ACTIVATION");
            }

            // Find the registered extension handling this type of load ...
            LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
            LexBIGServiceManager lbsm = lbs.getServiceManager(null);
            UMLS_Loader loader = (UMLS_Loader) lbsm.getLoader(org.LexGrid.LexBIG.Impl.loaders.UMLSLoaderImpl.name);

            // Perform the requested load or validate action ...
            if (vl >= 0) {
                loader.validate(source, userid, passwd, driver, vocabularies, 1, vl);
                Util.displayTaggedMessage("VALIDATION SUCCESSFUL");
            } else {
                loader.load(source, userid, passwd, driver, vocabularies, 1, false, true);
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

        o = new Option("in", "input", true, "Location of the source database. Typically this is specified "
                + "in the form of a URL that indicates the database server, port, " + "name, and optional properties.");
        o.setArgName("uri");
        o.setRequired(true);
        options.addOption(o);

        o = new Option("u", "uid", true, "User ID for authenticated access, if required and not "
                + "specified as part of the input URL.");
        o.setArgName("uid");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("p", "pwd", true, "Password for authenticated access, if required and not "
                + "specified as part of the input URL.");
        o.setArgName("pwd");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("d", "driver", true, "Name of the JDBC driver to use when accessing the database.");
        o.setArgName("drv");
        o.setRequired(true);
        options.addOption(o);

        o = new Option("s", "sources", true, "Comma-delimited list of source vocabularies to load.  "
                + "If absent, all available vocabularies are loaded.");
        o.setArgName("src");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("v", "validate", true, "Validation only; no load. If specified, 'a' and 't' "
                + "are ignored. 0 to verify files exist.");
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