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

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Load.MetaData_Loader;
import org.LexGrid.LexBIG.Extensions.Load.OWL2_Loader;
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

import edu.mayo.informatics.resourcereader.core.StringUtils;

/**
 * Imports from an OWL-based XML file to the LexBIG repository.
 * 
 * <pre>
 * Example: java org.LexGrid.LexBIG.admin.LoadOWL
 *   -in,--input &lt;uri&gt; URI or path specifying location of the source file
 *   -mf,--manifest &lt;uri&gt; URI or path specifying location of the manifest file
 *   -v, --validate &lt;int&gt; Perform validation of the candidate
 *         resource without loading data.  If specified, the '-a' and '-t'
 *         options are ignored.  Supported levels of validation include:
 *         0 = Verify document is well-formed
 *         1 = Verify document is valid
 *   -a, --activate ActivateScheme on successful load; if unspecified the vocabulary is loaded but not activated
 *   -t, --tag &lt;id&gt; An optional tag ID (e.g. 'PRODUCTION' or 'TEST') to assign. 
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
 *  org.LexGrid.LexBIG.admin.LoadOWL2 -in &quot;file:///path/to/somefile.owl&quot; -a
 * -or-
 *  org.LexGrid.LexBIG.admin.LoadOWL2 -in &quot;file:///path/to/somefile.owl&quot; -v 0
 *  -or-
 *  org.LexGrid.LexBIG.admin.LoadOWL2 -in &quot;file:///path/to/somefile.owl&quot; -a -meta &quot;file:///path/to/metadata.xml&quot; -metao
 * </pre>
 * 
 * @author <A HREF="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala</A>
 */
@LgAdminFunction
public class LoadOWL2 {

    private static final String EXAMPLE_CALL =  "\n LoadOWL2 -in \"file:///path/to/somefile.owl\" -a"
            + "\n LoadOWL -in \"file:///path/to/somefile.owl\" -v 0"
            + "\n LoadOWL -in \"file:///path/to/somefile.owl\" -mf \"file:///path/to/myCodingScheme-manifest.xml\" -a -meta \"file:///path/to/metadata.xml\" -metav 0 -metao";
    
    public static void main(String[] args) {
        try {
            new LoadOWL2().run(args);
        } catch (LBResourceUnavailableException e) {
            Util.displayTaggedMessage(e.getMessage());
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public LoadOWL2() {
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
            int v2 = -1;
            try {
                cl = new BasicParser().parse(options, args);
                if (cl.hasOption("v")){
                    vl = Integer.parseInt(cl.getOptionValue("v"));
                }
                if (cl.hasOption("metav")) {
                    v2 = Integer.parseInt(cl.getOptionValue("metav"));
                }
            } catch (ParseException e) {
                Util.displayCommandOptions("LoadOWL2", options,
                        EXAMPLE_CALL + Util.getURIHelp(), e);
                return;                
                
            } catch (NumberFormatException nfe) {
                Util.displayCommandOptions("LoadOWL2", options,
                        EXAMPLE_CALL + Util.getURIHelp(), nfe);
                return;
            }

            // Interpret provided values ...
            URI source = Util.string2FileURI(cl.getOptionValue("in"));

            String manUriStr = cl.getOptionValue("mf");
            URI manifest = null;
            if (!StringUtils.isNull(manUriStr))
                manifest = Util.string2FileURI(manUriStr);

            String lpUriStr = cl.getOptionValue("lp");
            URI loaderPrefs = null;
            if (!StringUtils.isNull(lpUriStr))
                loaderPrefs = Util.string2FileURI(lpUriStr);
            
            String memorySafe = cl.getOptionValue("ms");
            int memSafe = 1;
            if (!StringUtils.isNull(memorySafe)) {
                try {
                    memSafe = Integer.parseInt(memorySafe);
                } catch (NumberFormatException e) {
                    Util.displayAndLogError("Unrecognized memory setting specified.", e);
                }
            }
            
            // metatdata - input file (optional)
            String metaUriStr = cl.getOptionValue("meta");
            URI metaUri = null;
            if (!StringUtils.isNull(metaUriStr)){
               metaUri = Util.string2FileURI(metaUriStr);
            }
              
            // metatdata - validate input file (optional)
            if (v2 >= 0) {
                Util.displayTaggedMessage("VALIDATING METADATA SOURCE URI: " + source.toString());
            } 
           
            // metadata force
            boolean force = cl.hasOption("metaf");
            // metadata overwrite
            boolean overwrite = cl.hasOption("metao");
            
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
            OWL2_Loader loader = (OWL2_Loader) lbsm.getLoader(org.LexGrid.LexBIG.Impl.loaders.OWL2LoaderImpl.name);

            // Perform the requested load or validate action ...
            if (vl >= 0) {
                loader.validate(source, manifest, vl);
                Util.displayTaggedMessage("VALIDATION SUCCESSFUL");
            } else {
                if (loaderPrefs != null)
                    loader.setLoaderPreferences(loaderPrefs);
                loader.load(source, manifest, memSafe, false,  true);
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
                                   
            // If there is a metadata URI passed in, then load it.
            if (metaUri != null) {
                CodingSchemeSummary css = null;
                
                // Find the registered extension handling this type of load ...               
                MetaData_Loader metadataLoader = (MetaData_Loader) lbsm.getLoader("MetaDataLoader");
                                           
                Enumeration<? extends CodingSchemeRendering> schemes = lbs.getSupportedCodingSchemes()
                        .enumerateCodingSchemeRendering();
                while (schemes.hasMoreElements() && css == null) {
                    CodingSchemeSummary summary = schemes.nextElement().getCodingSchemeSummary();
                    
                    AbsoluteCodingSchemeVersionReference[] refs = loader.getCodingSchemeReferences();
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
                     
                loader = null;
                
                if (v2 >=0 ){
                    Util.displayTaggedMessage("Validating Metadata");
                    metadataLoader.validateAuxiliaryData(metaUri, Constructors.createAbsoluteCodingSchemeVersionReference(css), v2);
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
                        Util.displayTaggedMessage("Loading Metadata");
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
                + "are ignored. 0 to verify well-formed xml; 1 to check validity.");
        o.setArgName("int");
        o.setRequired(false);
        options.addOption(o);
        
        o = new Option("ms", "memory setting", true, "If specified, indicates the profile "
                + "used for memory/performance tradeoffs. " + "0 for faster/more memory (holds OWL in memory); "
                + "1 for slower/less memory (stream content to LexEVS DB)");
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