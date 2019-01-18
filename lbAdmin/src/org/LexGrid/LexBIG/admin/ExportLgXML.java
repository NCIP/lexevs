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

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.exporters.LexGridExport;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.codingSchemes.CodingScheme;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.lexevs.system.ResourceManager;

import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.util.CnsCngPair;
import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.util.FilterParser;

/**
 * ExportLgXML
 *   Exports content from the repository to a file in the LexGrid canonical XML format.
 * 
 * <pre>
 *  usage: ExportLgXML [-xc] [-an null] [-xall] -v null -u name -out uri [-xa] [-f] 
 *  -an,--associationsName Export associations with this name. Only valid with export type 'xa' 
 *  -f,--force If specified, allows the destination file to be overwritten if present. 
 *  -out,--output <uri> URI or path of the directory to contain the resulting XML file. The file name will be automatically derived from the coding scheme name. 
 *  -u,--urn <name> URN or local name of the coding scheme to export. 
 *  -v,--version The assigned tag/label or absolute version identifier of the coding scheme. 
 *  -xa,--exportAssociationsType of export: export only associations. 
 *  -xall,--exportAll Type of export: export all content. Default behavior. 
 *  -xc,--exportConcepts Type of export: export only concepts. 
 * </pre>
 * 
 * Example: ExportLgXML -out "file:///path/to/dir" -u "NCI Thesaurus" -v "05.06e" -f 
 * 
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 */
public class ExportLgXML {

    public static void main(String[] args) {
        try {
            new ExportLgXML().run(args);
        } catch (LBResourceUnavailableException e) {
            Util.displayAndLogMessage(e.getMessage());
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
            Util.displayAndLogMessage("WRITING TO: " + destination.toString());

            LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
            LexBIGServiceManager lbsm = lbs.getServiceManager(null);
            
            CodingScheme codingScheme = null;
            CodingSchemeSummary css = null;
            
            // Find in list of registered vocabularies ...
            if (urn != null && ver != null) {
                urn = urn.trim();
                ver = ver.trim();
                 
                // Try to resolve the CodingScheme
                CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
                versionOrTag.setVersion(ver);
                
                try {
                    codingScheme = lbs.resolveCodingScheme(urn, versionOrTag);
                }
                catch (LBParameterException lbpe) {
                    codingScheme = null;
                } 
            }

            // If the CodingScheme was not found, prompt for a CodingSchemeSummary
            if (codingScheme == null) {
                if (urn != null || ver != null) {
                    Util.displayMessage("No matching coding scheme was found for the given URN or version.");
                    Util.displayMessage("");
                }
                css = Util.promptForCodeSystem();
               
                if (css == null)
                    return;
            }
            else {
               // Find the registered extension handling this type of export ...
               LexGridExport exporter = (LexGridExport) lbsm.getExporter(LexGridExport.name);
   
               // Perform the requested action ...
               CnsCngPair cngCngPair = FilterParser.parse(lbs, codingScheme.getCodingSchemeURI(), 
                       codingScheme.getRepresentsVersion(), cl);
               exporter.setCng(cngCngPair.getCng());
               exporter.setCns(cngCngPair.getCns());
               
               exporter.export(Constructors.createAbsoluteCodingSchemeVersionReference(codingScheme.getCodingSchemeURI(), 
                       codingScheme.getRepresentsVersion()), destination, overwrite,
                       false, true);
               Util.displayExporterStatus(exporter);
            }
            
            if (css != null) {
                // Find the registered extension handling this type of export ...
                LexGridExport exporter = (LexGridExport) lbsm.getExporter(LexGridExport.name);
    
                // Perform the requested action ...
                CnsCngPair cngCngPair = FilterParser.parse(lbs, css.getCodingSchemeURI(), 
                        css.getRepresentsVersion(), cl);
                exporter.setCng(cngCngPair.getCng());
                exporter.setCns(cngCngPair.getCns());
                
                exporter.export(Constructors.createAbsoluteCodingSchemeVersionReference(css), 
                        destination, overwrite,
                        false, true);
                Util.displayExporterStatus(exporter);
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

        o = new Option("out", "output", true, "URI or path of the directory to contain the "
                + "resulting XML file.  The file name will be automatically " + "derived from the coding scheme name.");
        o.setArgName("uri");
        o.setRequired(true);
        options.addOption(o);

        o = new Option("u", "urn", true, "URN or local name of the coding scheme to export.");
        o.setArgName("name");
        o.setRequired(true);
        options.addOption(o);

        o = new Option("v", "version", true,
                "The assigned tag/label or absolute version identifier of the coding scheme.");
        o.setRequired(true);
        options.addOption(o);

        o = new Option("f", "force", false, "If specified, allows the destination file to be overwritten "
                + "if present.");
        o.setRequired(false);
        options.addOption(o);
        
        o = new Option("xall", "exportAll", false, "Type of export: export all content. Default behavior." );
        o.setRequired(false);
        options.addOption(o);
        
        o = new Option("xc", "exportConcepts", false, "Type of export: export only concepts.");
        o.setRequired(false);
        options.addOption(o);
        
        o = new Option("xa", "exportAssociations", false, "Type of export:  export only associations.");
        o.setRequired(false);
        options.addOption(o);
        
        o = new Option("an", "associationsName", true, "Export associations with this name. Only valid with export type \'xa\'");
        o.setRequired(false);
        options.addOption(o);
        
        

        return options;
    }

}