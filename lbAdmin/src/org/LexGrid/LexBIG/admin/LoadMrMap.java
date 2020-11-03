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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Load.MetaData_Loader;
import org.LexGrid.LexBIG.Extensions.Load.MrMap_Loader;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.relations.Relations;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.lexevs.system.ResourceManager;

import edu.mayo.informatics.lexgrid.convert.directConversions.mrmap.MappingRelationsUtil;
import edu.mayo.informatics.resourcereader.core.StringUtils;

/**
 * Loads a mappings file(s), provided in UMLS RRF format.
 * 
 * <pre>
 * Example: java org.LexGrid.LexBIG.admin.LoadMrMap
 * 
 *   -inMap,--input <uri> URI or path specifying location of the MRMAP source file.
 *   -inSat,--input <uri> URI or path specifying location of the MRSAT source file. 
 *   -meta --meatadata input &lt;uri&gt; URI or path specifying location of the metadata source file.  
 *        metadata is applied to the code system and code system version being loaded.
 *   -metav  --validate metadata &lt;int&gt; Perform validation of the metadata source file
 *         without loading data. Supported levels of validation include:
 *         0 = Verify document is valid.
 *         metadata is validated against the code system and code system version being loaded.
 *   -metao, --overwrite If specified, existing metadata for the code system
 *         will be erased. Otherwise, new metadata will be appended to
 *         existing metadata (if present).  
 *   -metaf,--force Force overwrite (no confirmation).
 *
 * Example: java -Xmx512m -cp lgRuntime.jar
 *  org.LexGrid.LexBIG.admin.LoadMrMap -inMap "file:///path/to/MRMAP.RRF -inSat "file:///path/to/MRSAT.RRF"
 *  -or-
 *  org.LexGrid.LexBIG.admin.LoadMrMap -inMap "file:///path/to/MRMAP.RRF -inSat "file:///path/to/MRSAT.RRF" -meta &quot;file:///path/to/metadata.xml&quot; -metao
 * </pre>
 * 
 */
public class LoadMrMap {

    private static final String EXAMPLE_CALL =  "\n LoadMrMap -inMap \"file:///path/to/MRMAP.RRF -inSat \"file:///path/to/MRSAT.RRF\""
            + "\n LoadMrMap -inMap \"file:///path/to/MRMAP.RRF -inSat \"file:///path/to/MRSAT.RRF\" -meta \"file:///path/to/metadata.xml\" -metav 0 -metao";

    public static void main(String[] args) {
        try {
            new LoadMrMap().run(args);
        } catch (LBResourceUnavailableException e) {
            Util.displayAndLogError("Resource Unavailable: " + e.getMessage() , e);
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public LoadMrMap() {
        super();
    }

    /**
     * Primary entry point for the program.
     * 
     * @throws Exception
     */
    public void run(String[] args) throws Exception {
        synchronized (ResourceManager.instance()) {
            String uri = null;
            String version = null;
            
            // Parse the command line ...
            CommandLine cl = null;
            Options options = getCommandOptions();
            int v1 = -1;
            try {
                cl = new BasicParser().parse(options, args);
                if (cl.hasOption("metav")){
                    v1 = Integer.parseInt(cl.getOptionValue("metav"));
                }
            } catch (Exception e) {
                Util.displayCommandOptions(
                        "LoadMrMapp", options, EXAMPLE_CALL + Util.getURIHelp(), e);
                return;
            }

            // Interpret provided values ...
            URI source = Util.string2FileURI(cl.getOptionValue("inMap"));
            URI sourceSat = Util.string2FileURI(cl.getOptionValue("inSat"));

            // metatdata - input file (optional)
            String metaUriStr = cl.getOptionValue("meta");
            URI metaUri = null;
            if (!StringUtils.isNull(metaUriStr)){
               metaUri = Util.string2FileURI(metaUriStr);
            }
              
            // metatdata - validate input file (optional)
            if (v1 >= 0) {
                Util.displayAndLogMessage("VALIDATING METADATA SOURCE URI: " + source.toString());
            } 
           
            // metadata force
            boolean force = cl.hasOption("metaf");
            // metadata overwrite
            boolean overwrite = cl.hasOption("metao");
            
            Util.displayAndLogMessage("LOADING FROM URI FOR MRMAP: " + source.toString());
            Util.displayAndLogMessage("LOADING FROM URI FOR MRSAT: " + sourceSat.toString());
            Util.displayAndLogMessage("POST LOAD AND ACTIVATION AVAILABLE ONLY ON MRMAP LOADS");

            // Find the registered extension handling this type of load ...
            LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
            LexBIGServiceManager lbsm = lbs.getServiceManager(null);
            MappingRelationsUtil mapUtil = new MappingRelationsUtil();
            HashMap<String, Relations> relations =  mapUtil.processMrSatBean(sourceSat.getPath(), source.getPath());
            
            for(Map.Entry<String, Relations> rel : relations.entrySet()){
                Relations relation = rel.getValue();
                System.out.println("relation : " + relation.getSourceCodingScheme());
                MrMap_Loader loader = (MrMap_Loader) lbsm
                    .getLoader(org.LexGrid.LexBIG.Extensions.Load.MrMap_Loader.name);

                loader.load(source, sourceSat, null, null, null, null, null, null, null, null, null, rel, false, true);
                Util.displayLoaderStatus(loader);
                
                
                AbsoluteCodingSchemeVersionReference[] refs = loader.getCodingSchemeReferences();
                for (int i = 0; i < refs.length; i++) {
                    AbsoluteCodingSchemeVersionReference ref = refs[i];
                    
                    version = ref.getCodingSchemeVersion();
                    uri = ref.getCodingSchemeURN();
                }
            }
            
            // If there is a metadata URI passed in, then load it.
            if (metaUri != null) {
                CodingSchemeSummary css = null;
                
                // Find the registered extension handling this type of load ...               
                MetaData_Loader metadataLoader = (MetaData_Loader) lbsm.getLoader("MetaDataLoader");
                                           
                Enumeration<? extends CodingSchemeRendering> schemes = lbs.getSupportedCodingSchemes()
                        .enumerateCodingSchemeRendering();
                while (schemes.hasMoreElements() && css == null) {
                    CodingSchemeSummary summary = schemes.nextElement().getCodingSchemeSummary();
                                                  
                    if (uri.equalsIgnoreCase(summary.getCodingSchemeURI())
                            && version.equalsIgnoreCase(summary.getRepresentsVersion())){
                        css = summary;
                        break;
                    }
                }
                
                if (css == null){
                    Util.displayAndLogMessage("Unable to apply metadata");
                    return;
                }
                                     
                if (v1 >=0 ){
                    Util.displayAndLogMessage("Validating Metadata");
                    metadataLoader.validateAuxiliaryData(metaUri, Constructors.createAbsoluteCodingSchemeVersionReference(css), v1);
                    Util.displayAndLogMessage("METADATA VALIDATION SUCCESSFUL");
                }
                else{
                    boolean confirmed = true;
                    if (overwrite && !force) {
                        Util.displayMessage("OVERWRITE EXISTING METADATA? ('Y' to confirm, any other key to cancel)");
                        char choice = Util.getConsoleCharacter();
                        confirmed = choice == 'Y' || choice == 'y';
                    }
                    if (confirmed) {
                        Util.displayAndLogMessage("Loading Metadata");
                        metadataLoader.loadAuxiliaryData(metaUri, Constructors.createAbsoluteCodingSchemeVersionReference(css),
                                overwrite, false, true);
                        Util.displayLoaderStatus(metadataLoader);
                    }
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

        o = new Option("inMap", "input", true, "URI or path specifying location of the source file.");
        o.setArgName("uri");
        o.setRequired(true);
        options.addOption(o);

        o = new Option("inSat", "input", true, "URI or path specifying location of the source file.");
        o.setArgName("uri");
        o.setRequired(true);
        options.addOption(o);
        
        o = new Option("meta", "metadata", true, "URI or path specifying location of the metadata file.");
        o.setArgName("uri");
        o.setRequired(false);
        options.addOption(o);
        
        o = new Option("metav", "metadata validate", true, "Validation metadata only; no load. 0 to verify well-formed xml.");
        o.setArgName("int");
        o.setRequired(false);
        options.addOption(o);
        
        o = new Option("metao", "overwrite", false, "If specified, existing metadata for the code system "
                + "will be erased. Otherwise, new metadata will be appended " + "to existing metadata (if present). ");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("metaf", "force", false, "Force overwrite (no confirmation).");
        o.setRequired(false);
        options.addOption(o);

        return options;
    }


}