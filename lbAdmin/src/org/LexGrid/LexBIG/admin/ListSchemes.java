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

import java.util.Formatter;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ObjectToString;
import org.LexGrid.codingSchemes.CodingScheme;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.lexevs.system.ResourceManager;

/**
 * List all currently registered vocabularies.
 * 
 * <pre>
 *  Example: java org.LexGrid.LexBIG.admin.ListSchemes
 *    -b,--brief List only coding scheme name, version, urn, and tags (default).
 *    -f,--full  List full detail for each scheme.
 *  
 *  Example: java -Xmx512m -cp lgRuntime.jar
 *   org.LexGrid.LexBIG.admin.ListSchemes
 * </pre>
 * 
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 */
public class ListSchemes {
    static final String Dash25 = "-------------------------";
    static final String Dash30 = "------------------------------";
    static final String Dash55 = "-------------------------------------------------------";

    public static void main(String[] args) {
        try {
            new ListSchemes().run(args);
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public ListSchemes() {
        super();
    }

    /**
     * Primary entry point for the program.
     * 
     * @throws Exception
     */

    public void run(String[] args) throws Exception {
        synchronized (ResourceManager.instance()) {
            LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
            CodingSchemeRenderingList schemes = lbs.getSupportedCodingSchemes();

            // Parse the command line ...
            CommandLine cl = null;
            Options options = getCommandOptions();
            try {
                cl = new BasicParser().parse(options, args);
            } catch (ParseException e) {
                Util.displayCommandOptions("ListExtensions", options, "ListExtensions -a", e);
                return;
            }

            if (schemes.getCodingSchemeRenderingCount() == 0)
                Util.displayMessage("No coding schemes found.");
            else {
                boolean fullRequested = cl.hasOption('f');
                if (fullRequested) {
                    for (CodingSchemeRendering csr : schemes.getCodingSchemeRendering()) {
                        // Separator ...
                        Util.displayMessage("=============================");
                        CodingSchemeSummary css = csr.getCodingSchemeSummary();
                        CodingScheme cs = lbs.resolveCodingScheme(css.getCodingSchemeURI(), Constructors
                                .createCodingSchemeVersionOrTagFromVersion(css.getRepresentsVersion()));
                        Util.displayMessage(ObjectToString.toString(cs, "", 80));
                    }
                } else {
                    Formatter f = new Formatter();

                    String format = "%-30.30s|%-25.25s|%-55.55s|%-25.25s\n";
                    Object[] hSep = new Object[] { Dash30, Dash25, Dash55, Dash25 };
                    f.format(format, hSep);
                    f.format(format, new Object[] { "Local Name", "Version", "URN", "Tag" });
                    f.format(format, hSep);
                    for (CodingSchemeRendering csr : schemes.getCodingSchemeRendering()) {
                        CodingSchemeSummary css = csr.getCodingSchemeSummary();
                        // Evaluate local name
                        String localName = css.getLocalName();
                        if (localName != null && localName.length() > 30)
                            localName = localName.substring(0, 28) + ">>";
                        // Evaluate version
                        String version = css.getRepresentsVersion();
                        if (version != null && version.length() > 25)
                            version = version.substring(0, 23) + ">>";
                        // Evaluate urn
                        String urn = css.getCodingSchemeURI();
                        if (urn != null && urn.length() > 55)
                            urn = urn.substring(0, 53) + ">>";
                        // Evaluate tag(s)
                        String[] tags = csr.getRenderingDetail().getVersionTags().getTag();
                        String tag = tags.length > 0 ? tags[0] : "";
                        if (tag != null && tag.length() > 25)
                            tag = tag.substring(0, 23) + ">>";
                        // Output the first line
                        f.format(format, new Object[] { localName, version, urn, tag });
                        // Output additional tags
                        for (int i = 1; i < tags.length; i++) {
                            tag = tags[i];
                            if (tag != null && tag.length() > 25)
                                tag = tag.substring(0, 23) + ">>";
                            f.format(format, "", "", "", tag);
                        }
                        // Output separator
                        f.format(format, hSep);
                    }
                    Util.displayMessage(f.out().toString());
                    Util.displayMessage("");
                    Util.displayMessage("NOTE: >> indicates column value exceeds the available width.");
                    Util.displayMessage("      Specify the '-f' option for additional detail.");
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

        o = new Option("b", "brief", false, "List only coding scheme name, version, urn, and tag (default).");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("f", "full", false, "List full detail for each scheme.");
        o.setRequired(false);
        options.addOption(o);

        return options;
    }

}