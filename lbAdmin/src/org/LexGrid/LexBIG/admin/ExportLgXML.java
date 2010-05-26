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
import java.util.Enumeration;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Export.LexGrid_Exporter;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.exporters.LexGridExport;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.lexevs.system.ResourceManager;

/**
 * Exports content from the repository to a file in the LexGrid cononical XML
 * format.
 * 
 * <pre>
 * Example: java org.LexGrid.LexBIG.admin.ExportLgXML
 *   -out,--output &lt;uri&gt; URI or path of the directory to contain the
 *        resulting XML file.  The file name will be automatically derived
 *        from the coding scheme name.   
 *   -u,--urn &lt;name&gt; URN or local name of the coding scheme to export.
 *   -v,--version &lt;id&gt; The assigned tag/label or absolute version
 *        identifier of the coding scheme.
 *   -f,--force If specified, allows the destination file to be
 *        overwritten if present.
 * 
 * Note: If the URN and version values are unspecified, a
 * list of available coding schemes will be presented for
 * user selection.
 * 
 * Example: java -Xmx512m -cp lgRuntime.jar
 *   org.LexGrid.LexBIG.admin.ExportLgXML -out &quot;file:///path/to/dir&quot;
 *      -u &quot;FBbt&quot; -v &quot;PRODUCTION&quot; -f
 * </pre>
 * 
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 */
public class ExportLgXML {

    public static void main(String[] args) {
        try {
            new ExportLgXML().run(args);
        } catch (LBResourceUnavailableException e) {
            Util.displayTaggedMessage(e.getMessage());
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public ExportLgXML() {
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
                Util.displayCommandOptions("ExportLgXML", options,
                        "\n ExportLgXML -out \"file:///path/to/dir\" -u \"name\" -v \"PRODUCTION\" -f"
                                + Util.getURIHelp(), e);
                Util.displayMessage(Util.getPromptForSchemeHelp());
                return;
            }

            // Interpret provided values ...
            URI destination = Util.string2FileURI(cl.getOptionValue("out"));
            String urn = cl.getOptionValue("u");
            String ver = cl.getOptionValue("v");
            boolean overwrite = cl.hasOption("f");
            Util.displayTaggedMessage("WRITING TO: " + destination.toString());

            LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
            LexBIGServiceManager lbsm = lbs.getServiceManager(null);

            // Find in list of registered vocabularies ...
            CodingSchemeSummary css = null;
            if (urn != null && ver != null) {
                urn = urn.trim();
                ver = ver.trim();
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

            // Find the registered extension handling this type of export ...
            LexGridExport exporter = (LexGridExport) lbsm.getExporter(LexGridExport.name);

            // Perform the requested action ...
            AbsoluteCodingSchemeVersionReference acsvr = Constructors.createAbsoluteCodingSchemeVersionReference(css);
            
            org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph cng = lbs.getNodeGraph(acsvr.getCodingSchemeURN(), 
                    Constructors.createCodingSchemeVersionOrTagFromVersion(acsvr.getCodingSchemeVersion()),null);
              
            org.LexGrid.LexBIG.LexBIGService.CodedNodeSet cns = lbs.getCodingSchemeConcepts(acsvr.getCodingSchemeURN(), 
                    Constructors.createCodingSchemeVersionOrTagFromVersion(acsvr.getCodingSchemeVersion()) );
            
            exporter.setCng(cng);
            exporter.setCns(cns);
            
            exporter.export(Constructors.createAbsoluteCodingSchemeVersionReference(css), destination, overwrite,
                    false, true);
            Util.displayExporterStatus(exporter);
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

        o = new Option("out", "output", true, "URI or path of the directory to contain the "
                + "resulting XML file.  The file name will be automatically " + "derived from the coding scheme name.");
        o.setArgName("uri");
        o.setRequired(true);
        options.addOption(o);

        o = new Option("u", "urn", true, "URN or local name of the coding scheme to export.");
        o.setArgName("name");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("v", "version", true,
                "The assigned tag/label or absolute version identifier of the coding scheme.");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("f", "force", false, "If specified, allows the destination file to be overwritten "
                + "if present.");
        o.setRequired(false);
        options.addOption(o);

        return options;
    }

}