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
 *      http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.LexGrid.LexBIG.admin;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Enumeration;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Load.MetaData_Loader;
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
import org.kohsuke.args4j.CmdLineParser;
import org.lexevs.system.ResourceManager;
import org.lexgrid.loader.meta.launch.MetaBatchLoaderLauncher;

import edu.mayo.informatics.resourcereader.core.StringUtils;

/**
 * Loads NCI Metathesaurus content, provided as a collection of RRF files in a
 * single directory.
 * 
 * <pre>
 * Example: java org.LexGrid.LexBIG.admin.LoadUmlsBatchWithMetadata
 *   -in,--input <uri> URI or path of the directory containing the NLM files.
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
 *  org.LexGrid.LexBIG.admin.LoadMetaBatchWithMetadata -in "file:///path/to/directory/"
 * -or-
 *  org.LexGrid.LexBIG.admin.LoadMetaBatchWithMetadata -in "file:///path/to/directory/" -meta &quot;file:///path/to/metadata.xml&quot; -metao
 * </pre>
 */
@LgAdminFunction
public class LoadMetaBatchWithMetadata {

    private static final String EXAMPLE_CALL =  "\n LoadMetaBatchWithMetadata -in \"file:///path/to/directory/\""
            + "\n LoadMetaBatchWithMetadata -in \"file:///path/to/directory/\" -meta \"file:///path/to/metadata.xml\" -metao";

    public static void main(String[] args) {
        try {        
            new LoadMetaBatchWithMetadata().run(args);
        } catch (LBResourceUnavailableException e) {
            Util.displayTaggedMessage(e.getMessage());
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public LoadMetaBatchWithMetadata() {
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
            int v1 = -1;
    
            try {
                cl = new BasicParser().parse(options, args);
                if (cl.hasOption("metav")){
                    v1 = Integer.parseInt(cl.getOptionValue("metav"));
                }
            } catch (ParseException e) {
                Util.displayCommandOptions("LoadMetaBatchWithMetadata",options,
                        EXAMPLE_CALL + Util.getURIHelp(), e);
                return;
            }
            
            // metatdata - input file (optional)
            String metaUriStr = cl.getOptionValue("meta");
            URI metaUri = null;
            if (!StringUtils.isNull(metaUriStr)){
               metaUri = Util.string2FileURI(metaUriStr);
            }
              
            // metatdata - validate input file (optional)
            if (v1 >= 0) {
                Util.displayTaggedMessage("VALIDATING METADATA SOURCE URI: " + metaUri.toString());
            } 
           
            // metadata force
            boolean force = cl.hasOption("metaf");
            // metadata overwrite
            boolean overwrite = cl.hasOption("metao");
            
            // launch the MetaBatch loader
            MetaBatchLoaderLauncher launcher = new MetaBatchLoaderLauncher();
            CmdLineParser parser = new CmdLineParser(launcher);
            
            String[] umlsArgs = cleanseArgs(args);
            
            parser.parseArgument(umlsArgs);    
            launcher.loadAndWait();
         
            Util.displayTaggedMessage("Metabatch load complete.");
                                    
            LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
            LexBIGServiceManager lbsm = lbs.getServiceManager(null);
                    
            CodingSchemeSummary css = null;
                                                      
            Enumeration<? extends CodingSchemeRendering> schemes = lbs.getSupportedCodingSchemes()
                    .enumerateCodingSchemeRendering();
            while (schemes.hasMoreElements() && css == null) {
                CodingSchemeSummary summary = schemes.nextElement().getCodingSchemeSummary();
                                                                 
                AbsoluteCodingSchemeVersionReference[] refs = launcher.getCodingSchemeRefs();
                for (int i = 0; i < refs.length; i++) {
                    AbsoluteCodingSchemeVersionReference ref = refs[i];
                                            
                    if (ref.getCodingSchemeURN().equalsIgnoreCase(summary.getCodingSchemeURI())
                            && ref.getCodingSchemeVersion().equalsIgnoreCase(summary.getRepresentsVersion())){
                        css = summary;
                        break;
                    }
                }
            }
                
            if (css == null){
                Util.displayTaggedMessage("Unable to apply metadata");
                return;
            }
            
            if (metaUri != null) {
                // Load the user supplied metadata
                loadMetadata(css, metaUri, v1, overwrite, force);
            }
            
            URI uri = null;
            String path = System.getProperty("user.home");
            
            // Load the auto generated NciMetadata.xml metadata
            uri = Util.string2FileURI("file://" + path + File.separator + "NciMetadata.xml");
            
            loadMetadata(css, uri, -1, false, true);
            
            uri = Util.string2FileURI("file://" + path + File.separator + "NciMrdocMetadata.xml");
            
            // Load the auto generated NciMrdocMetadata.xml metadata
            loadMetadata(css, uri, -1, false, true);
            
        }
    }

    private void loadMetadata(CodingSchemeSummary css, URI metaUri, int v1, boolean overwrite, boolean force) throws LBException, IOException{

        LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
        LexBIGServiceManager lbsm = lbs.getServiceManager(null);
        
        // Find the registered extension handling this type of load ...               
        MetaData_Loader metadataLoader = (MetaData_Loader) lbsm.getLoader("MetaDataLoader");
                
        if (v1 >=0 ){
            Util.displayTaggedMessage("Validating Metadata for " + metaUri);
            metadataLoader.validateAuxiliaryData(metaUri, Constructors.createAbsoluteCodingSchemeVersionReference(css), v1);
            Util.displayTaggedMessage("METADATA VALIDATION SUCCESSFUL");
        }
        else{
            boolean confirmed = true;
            if (overwrite && !force) {
                Util.displayMessage("OVERWRITE EXISTING METADATA? ('Y' to confirm, any other key to cancel)");
                char choice = Util.getConsoleCharacter();
                confirmed = choice == 'Y' || choice == 'y';
            }
            if (confirmed) {
                Util.displayTaggedMessage("Loading Metadata for " + metaUri);
                metadataLoader.loadAuxiliaryData(metaUri, Constructors.createAbsoluteCodingSchemeVersionReference(css),
                        overwrite, false, true);
                Util.displayLoaderStatus(metadataLoader);
            }
        }
        
        metadataLoader = null;
    }
    
    
    private String[] cleanseArgs(String[] args) {
        String[] umlsArgs = null;
        ArrayList<String> argList = new ArrayList<>();
        
        for (int i = 0; i < args.length; i++) {
            // don't add -meta options
            if (!args[i].startsWith("-meta")){
                argList.add(args[i]);
            }
            else{
                i++;
                // if -meta was found above, don't add its value, if there is one.
                while (i < args.length && args[i].startsWith("-meta")){
                    i++;
                }
                if (i < args.length && args[i].startsWith("-")){
                    argList.add(args[i]);
                }
            }
        }
        umlsArgs = (String[])argList.toArray(new String[argList.size()]);
        return umlsArgs;
    }

    /**
     * Return supported command options.
     * 
     * @return org.apache.commons.cli.Options
     */
    private Options getCommandOptions() {
        Options options = new Options();
        Option o;

        o = new Option("in", "input", true, "URI or path of the directory containing the NLM files. Path string must be preceded by \"file:\"");
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