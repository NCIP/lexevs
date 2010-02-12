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

import java.util.Enumeration;

import org.LexGrid.LexBIG.DataModel.Collections.ExtensionDescriptionList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Extensions.Index.Index;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.annotations.LgAdminFunction;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.lexevs.system.ResourceManager;

/**
 * Clears an optional named index associated with the specified coding scheme.
 * Note that built-in indices required by the LexBIG runtime cannot be removed.
 * 
 * <pre>
 * Example: java org.LexGrid.LexBIG.admin.RemoveIndex
 *  -u,--urn &lt;name&gt; URN uniquely identifying the code system.
 *  -v,--version &lt;id&gt; Version identifier.
 *  -i,--index &lt;name&gt; Name of the index extension to clear.
 *  -f,--force Force clear (no confirmation).
 * 
 * Note: If the URN and version values are unspecified, a
 * list of available coding schemes will be presented for
 * user selection.
 * 
 * Example: java -Xmx512m -cp lgRuntime.jar
 * org.LexGrid.LexBIG.admin.RemoveIndex
 *    -u &quot;urn:oid:2.16.840.1.113883.3.26.1.1&quot; -v &quot;05.09e&quot; -i &quot;myindex&quot;
 * </pre>
 * 
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 */
@LgAdminFunction
public class RemoveIndex {

    public static void main(String[] args) {
        try {
            new RemoveIndex().run(args);
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public RemoveIndex() {
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
                Util.displayCommandOptions("RemoveIndex", options,
                        "RemoveIndex -u \"urn:oid:2.16.840.1.113883.3.26.1.1\" -v \"05.09e\" -i \"myIndex\"", e);
                Util.displayMessage(Util.getPromptForSchemeHelp());
                return;
            }

            // Interpret provided values ...
            String urn = cl.getOptionValue("u");
            String ver = cl.getOptionValue("v");
            boolean force = cl.hasOption("f");
            CodingSchemeSummary css = null;

            // Find in list of registered vocabularies ...
            if (urn != null && ver != null) {
                urn = urn.trim();
                ver = ver.trim();
                LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
                Enumeration<CodingSchemeRendering> schemes = lbs.getSupportedCodingSchemes()
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

            // Valid index name?
            String indexName = cl.getOptionValue("i");
            LexBIGServiceManager lbsm = LexBIGServiceImpl.defaultInstance().getServiceManager(null);
            ExtensionDescriptionList indexes = lbsm.getIndexExtensions();
            Index match = null;
            Enumeration<ExtensionDescription> indexEnum = indexes.enumerateExtensionDescription();
            while (indexEnum.hasMoreElements() && match == null) {
                Index ext = (Index) indexEnum.nextElement();
                if (indexName.equalsIgnoreCase(ext.getName()))
                    match = ext;
            }

            // Nope, try again ...
            if (match == null) {
                Util.displayTaggedMessage("No matching index extension was found.");
                Util.displayTaggedMessage("Use the ListExtensions program to display a list of registered extensions.");
                return;
            }

            // Continue and confirm the action (if not bypassed by force option)
            // ...
            Util.displayTaggedMessage("A matching coding scheme and index were found ...");
            boolean confirmed = true;
            if (!force) {
                Util.displayMessage("CLEAR? ('Y' to confirm, any other key to cancel)");
                char choice = Util.getConsoleCharacter();
                confirmed = choice == 'Y' || choice == 'y';
            }
            if (confirmed) {
                match.getLoader().clear(Constructors.createAbsoluteCodingSchemeVersionReference(css), match, false);
                Util.displayTaggedMessage("Request complete");
            } else {
                Util.displayTaggedMessage("Action cancelled by user");
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

        o = new Option("i", "index", true, "Name of the index extension to clear.");
        o.setArgName("name");
        o.setRequired(true);
        options.addOption(o);

        o = new Option("f", "force", false, "Force clear (no confirmation).");
        o.setRequired(false);
        options.addOption(o);

        return options;
    }

}