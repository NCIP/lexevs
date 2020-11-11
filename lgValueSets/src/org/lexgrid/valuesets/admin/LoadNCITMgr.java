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
package org.lexgrid.valuesets.admin;

import java.net.URI;
import java.util.Enumeration;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Load.MetaData_Loader;
import org.LexGrid.LexBIG.Extensions.Load.NCIHistoryLoader;
import org.LexGrid.LexBIG.Extensions.Load.OWL2_Loader;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.loaders.AssertedValueSetIndexLoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.ProcessRunner;
import org.LexGrid.LexBIG.Impl.loaders.SourceAssertedValueSetBatchLoader;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.admin.Util;
import org.LexGrid.annotations.LgAdminFunction;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.lexevs.system.ResourceManager;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

import edu.mayo.informatics.resourcereader.core.StringUtils;

/**
 * 
 * Load manager script that performs the following tasks:
 * 	- Imports from an OWL-based XML file to the LexBIG repository.
 * 	- Removes All Value Set Definitions
 *  - Loads Asserted Value Set Definitions 
 * 	- Builds indexes associated with the source asserted value sets of the specified coding scheme.
 *  - Loads NCI History
 * 
 * 
 * <pre>
 * Example: org.lexgrid.valuesets.admin.LoadNCITMgr
 *   -ncit -ncitLoad Use this parameter to initiate the OWL2 load option of NCIt.
 *   -in,--input &lt;uri&gt; URI or path specifying location of the source file
 *   -mf,--manifest &lt;uri&gt; URI or path specifying location of the manifest file
 *   -lp,--loader preferences &lt;uri&gt; URI or path specifying location of the loader preference file
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
 * 
 * ------------- RemoveAllValueSetDefinitions Parameters -------------
 *   -rvsd -RemoveAllValueSetDefinitions Remove All Value Set Definitions (no confirmation). 
 *   		Use this parameter to initiate this option.
 * 
 * ------------- SourceAssertedValueSetDefinitionLoad Parameters -------------
 *   -savsdl --SourceAssertedValueSetDefinitionLoad Load Asserted Value Set Definitions 
 *   		Use this parameter to initiate this option.
 *   -savsdl_v --SourceAssertedValueSetDefinitionLoad_version
 *   
 * ------------- BuildAssertedValueSetIndex Parameters -------------  
 *   -bavsi --BuildAssertedValueSetIndex Build indexes associated with the source 
 *   		asserted value sets of the specified coding scheme. 
 *   		Note: There is no confirmation/force option in this script.
 *  		Use this parameter to initiate this option.
 *   -bavsi_u --buildAssertedValueSetIndex_urn Coding Scheme URN
 *   -bavsi_v --BuildAssertedValueSetIndex_version Coding Scheme Version
 *   
 * ------------- LoadNCIHistory Parameters -------------  
 *   -lncih --LoadNCIHistory Load NCI History
 *   		Use this parameter to initiate this option.
 *   -lncih_in --LoadNCIHistory_input &lt;uri&gt; URI or path specifying location of the history file
 *   -lncih_vf --LoadNCIHistory_versionFile &lt;uri&gt; URI or path specifying location of the file
 *         containing version identifiers for the history to be loaded     
 *   -lncih_r --LoadNCIHistory_replace replace if not specified, the provided history file will
 *         be added into the current history database; otherwise the
 *         current database will be replaced by the new content.
 * 
 * Example: java -Xmx8000m -cp lgRuntime.jar
 *  org.lexgrid.valuesets.admin.LoadNCITMgr 
 *  -ncit -ncitLoad Use this parameter to initiate the OWL2 load option of NCIt.
 *  -in &quot;file:///path/to/somefile.owl&quot; 
 *  -mf &quot;file:///path/to/Thesaurus_MF_OWL2_20.09d.xml&quot;
 *  -lp &quot;file:///path/to/Thesaurus_PF_OWL2.xml&quot;
 *  -a 
 *  -v &quot;20.09d&quot; 
 *  -t &quot;PRODUCTION&quot;
 *  -meta --meatadata input &lt;uri&gt; URI or path specifying location of the metadata source file.  
 *        metadata is applied to the code system and code system version being loaded.
 *  -rvsd
 *  -savsdl -savsdl_v &quot;20.09d&quot;
 *  -bavsi
 *  -bavsi_u &quot;http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#&quot;
 *  -bavsi_v &quot;20.09d&quot;
 *  -lncih
 *  -lncih_in &quot;file:///path/to/cumulative_history_20.09d.txt&quot;
 *  -lncih_vf &quot;file:///path/to/NCISystemReleaseHistory.txt&quot;
 * </pre>
 * 
 */
@LgAdminFunction
public class LoadNCITMgr {

    private static final String EXAMPLE_CALL =  
           "\n LoadNCITMgr "
    		+ "\n -ncit"
    		+ "\n -in \"file:///path/to/ThesaurusInf-200929-20.09d.owl-forProduction.owl\""
            + "\n -mf \"file:///path/to/Thesaurus_MF_OWL2_20.09d.xml\" "  
            + "\n -lp \"file:///path/to/Thesaurus_PF_OWL2.xml"
            + "\n -a"
            + "\n -t \"PRODUCTION\""
            + "\n -meta --meatadata input <uri> URI or path specifying location of the metadata source file. " 
            +       "metadata is applied to the code system and code system version being loaded."
            + "\n -rvsd"
            + "\n -savsdl"
            + "\n -savsdl_v \"20.09d\""
            + "\n -bavsi"
            + "\n -bavsi_u \"http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#\""
            + "\n -bavsi_v \"20.09d\""
            + "\n -lncih"
            + "\n -lncih_in  \"file:///path/to/cumulative_history_20.09d.txt\""
            + "\n -lncih_vf \"file:///path/to/NCISystemReleaseHistory.txt\"";
           
    private static final String SAVSDL_CODING_SCHEME = "NCI_Thesaurus";  //"owl2lexevs";
    private static final String SAVSDL_ASSERTED_DEFAULT_HIERARCHY_VS_RELATION = "Concept_In_Subset";
    private static final String SAVSDL_BASE_VALUESET_URI = "http://evs.nci.nih.gov/valueset/";
    private static final String SAVSDL_SOURCE_NAME = "Contributing_Source";
    
    public static void main(String[] args) {
        try {
            new LoadNCITMgr().run(args);
        } catch (LBResourceUnavailableException e) {
            Util.displayAndLogError("Resource Unavailable: " + e.getMessage() , e);
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public LoadNCITMgr() {
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
            
            boolean loadNcit = false;
            int vl = -1;
            int v2 = -1;
            URI source = null;
            URI manifest = null;
            URI loaderPrefs = null;
            int memSafe = 1;
            boolean activate = false;
            URI metaUri = null;
            boolean force = false;
            boolean overwrite = false;
            
            try {
            	cl = new BasicParser().parse(options, args);
            } catch (ParseException e) {
                Util.displayCommandOptions("LoadNCITMgr", options,
                        EXAMPLE_CALL + Util.getURIHelp(), e);
                return; 
            }
            
            // deterrmine if the NCIt OWL2 load should be performed.
            loadNcit = cl.hasOption("ncit");
            
            // gather all OWL2 load parameters if the option to load OWL2 is set
            if (loadNcit) {
	            try {
	                
	                if (cl.hasOption("v")){
	                    vl = Integer.parseInt(cl.getOptionValue("v"));
	                }
	                if (cl.hasOption("metav")) {
	                    v2 = Integer.parseInt(cl.getOptionValue("metav"));
	                } 
	            } catch (NumberFormatException nfe) {
	                Util.displayCommandOptions("LoadNCITMgr", options,
	                        EXAMPLE_CALL + Util.getURIHelp(), nfe);
	                return;
	            }
	
	            // Interpret provided values ...
	            
	            try {
	            	source = Util.string2FileURI(cl.getOptionValue("in"));
	            } catch (LBResourceUnavailableException e) {
	            	Util.displayCommandOptions("LoadNCITMgr", options,
	                        EXAMPLE_CALL + Util.getURIHelp(), e);
	                return;
	            }
	
	            String manUriStr = cl.getOptionValue("mf");
	            
	            if (!StringUtils.isNull(manUriStr))
	                manifest = Util.string2FileURI(manUriStr);
	
	            String lpUriStr = cl.getOptionValue("lp");
	            
	            if (!StringUtils.isNull(lpUriStr))
	                loaderPrefs = Util.string2FileURI(lpUriStr);
	            
	            String memorySafe = cl.getOptionValue("ms");
	            if (!StringUtils.isNull(memorySafe)) {
	                try {
	                    memSafe = Integer.parseInt(memorySafe);
	                } catch (NumberFormatException e) {
	                    Util.displayAndLogError("Unrecognized memory setting specified.", e);
	                }
	            }
	            
	            // metatdata - input file (optional)
	            String metaUriStr = cl.getOptionValue("meta");
	            
	            if (!StringUtils.isNull(metaUriStr)){
	               metaUri = Util.string2FileURI(metaUriStr);
	            }
	              
	            // metatdata - validate input file (optional)
	            if (v2 >= 0) {
	                Util.displayAndLogMessage("VALIDATING METADATA SOURCE URI: " + source.toString());
	            } 
	           
	            // metadata force
	            force = cl.hasOption("metaf");
	            // metadata overwrite
	            overwrite = cl.hasOption("metao");
	            
	            activate = vl < 0 && cl.hasOption("a");
	            if (vl >= 0) {
	                Util.displayAndLogMessage("VALIDATING SOURCE URI: " + source.toString());
	            } else {
	                Util.displayAndLogMessage("LOADING FROM URI: " + source.toString());
	                Util.displayAndLogMessage(activate ? "ACTIVATE ON SUCCESS" : "NO ACTIVATION");
	            }
            }
            
            boolean removeAllValueSetDefinitions = cl.hasOption("rvsd");
            
            boolean sourceAssertedValueSetDefinitionLoad = cl.hasOption("savsdl");
            String sourceAssertedValueSetDefinitionLoadVersionStr = cl.getOptionValue("savsdl_v");
            
            boolean buildAssertedValueSetIndex = cl.hasOption("bavsi");
            String buildAssertedValueSetIndexURNStr = cl.getOptionValue("bavsi_u");
            String buildAssertedValueSetIndexVersionStr = cl.getOptionValue("bavsi_v");
            
            if (buildAssertedValueSetIndexVersionStr != null) {
            	buildAssertedValueSetIndexVersionStr = buildAssertedValueSetIndexVersionStr.trim();
            }
            if (buildAssertedValueSetIndexURNStr != null){
            	buildAssertedValueSetIndexURNStr = buildAssertedValueSetIndexURNStr.trim();
            }
            
            boolean loadNCIHistory = cl.hasOption("lncih");
            String loadNCIHistoryInput = cl.getOptionValue("lncih_in");
            String loadNCIHistoryVersionFile = cl.getOptionValue("lncih_vf");
            
            URI historySource = null;
            URI historyVersions = null;
            
            // convert the parameters only if the user selected to load history
            if (loadNCIHistory) {
	            historySource = Util.string2FileURI(loadNCIHistoryInput);
	            historyVersions = Util.string2FileURI(loadNCIHistoryVersionFile);
            }
            
            boolean loadNCIHistoryReplace = cl.hasOption("lncih_r");

            // Find the registered extension handling this type of load ...
            LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
            LexBIGServiceManager lbsm = lbs.getServiceManager(null);
            
            Util.displayAndLogMessage("");
        	Util.displayAndLogMessage("****************************************");
        	Util.displayAndLogMessage("* Loading NCIt (OWL2)");
        	Util.displayAndLogMessage("****************************************");
        	Util.displayAndLogMessage("");
        	
            // if loadNcit is set, perform the OWL2 load 
            if (loadNcit) {
            	
	            OWL2_Loader loader = (OWL2_Loader) lbsm.getLoader(org.LexGrid.LexBIG.Impl.loaders.OWL2LoaderImpl.name);
	                                 
	            // Perform the requested load or validate action ...
	            if (vl >= 0) {
	                loader.validate(source, manifest, vl);
	                Util.displayAndLogMessage("VALIDATION SUCCESSFUL");
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
	                    Util.displayAndLogMessage("Tag assigned>> " + ref.getCodingSchemeURN() + " Version>> "
	                            + ref.getCodingSchemeVersion());
	                }
	            }
	
	            // If requested, activate the newly loaded scheme(s) ...
	            if (activate) {
	                AbsoluteCodingSchemeVersionReference[] refs = loader.getCodingSchemeReferences();
	                for (int i = 0; i < refs.length; i++) {
	                    AbsoluteCodingSchemeVersionReference ref = refs[i];
	                    lbsm.activateCodingSchemeVersion(ref);
	                    Util.displayAndLogMessage("Scheme activated>> " + ref.getCodingSchemeURN() + " Version>> "
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
	                    Util.displayAndLogMessage("Unable to apply metadata");
	                    return;
	                }
	                     
	                loader = null;
	                
	                if (v2 >=0 ){
	                    Util.displayAndLogMessage("Validating Metadata");
	                    metadataLoader.validateAuxiliaryData(metaUri, Constructors.createAbsoluteCodingSchemeVersionReference(css), v2);
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
            else {
            	Util.displayAndLogMessage("* Skipping Loading NCIt (OWL2)");
            }
            //*************************************************************
            // RemoveAllValueSetDefinitions
            //*************************************************************
            Util.displayAndLogMessage("");
        	Util.displayAndLogMessage("****************************************");
        	Util.displayAndLogMessage("* Removing All Valueset Definitions");
        	Util.displayAndLogMessage("****************************************");
        	Util.displayAndLogMessage("");
        	
            if (removeAllValueSetDefinitions){
            	
    			final LexEVSValueSetDefinitionServices vss = new LexEVSValueSetDefinitionServicesImpl();
    			List<String> uris = vss.listValueSetDefinitionURIs();
    			for (String urn : uris) {
    				try {
    					vss.removeValueSetDefinition(URI.create(urn));
    				} catch (LBException e) {
    					Util.displayAndLogError(e);
    					e.printStackTrace();
    				}
    				Util.displayAndLogMessage("ValueSetDefinition removed: " + urn);
    			}
    			Util.displayAndLogMessage("");
    			Util.displayAndLogMessage("Valueset Definition Removal Complete");
    		}
    		else {
            	Util.displayAndLogMessage("* Skipping Remove All ValueSet Definitions");
            }
            
            //*************************************************************
            // SourceAssertedValueSetDefinitionLoad
            //*************************************************************
            Util.displayAndLogMessage("");
        	Util.displayAndLogMessage("****************************************");
        	Util.displayAndLogMessage("* Source Asserted ValueSet Definition Load");
        	Util.displayAndLogMessage("****************************************");
        	Util.displayAndLogMessage("");
        	
        	Util.displayAndLogMessage("Parameters: ");
        	Util.displayAndLogMessage("\tversion:                            " + sourceAssertedValueSetDefinitionLoadVersionStr);
        	Util.displayAndLogMessage("\tcodingSchemeName:                   " + SAVSDL_CODING_SCHEME);
        	Util.displayAndLogMessage("\tassertedDefaultHierarchyVSRelation: " + SAVSDL_ASSERTED_DEFAULT_HIERARCHY_VS_RELATION);
        	Util.displayAndLogMessage("\tbaseValueSetURI:                    " + SAVSDL_BASE_VALUESET_URI);
        	Util.displayAndLogMessage("\tsourceName:                         " + SAVSDL_SOURCE_NAME);
        	Util.displayAndLogMessage("");
        	
            if (sourceAssertedValueSetDefinitionLoad) {
            	
            	AssertedValueSetParameters params = new AssertedValueSetParameters.
        			Builder(sourceAssertedValueSetDefinitionLoadVersionStr).
        			codingSchemeName(SAVSDL_CODING_SCHEME).
            		assertedDefaultHierarchyVSRelation(SAVSDL_ASSERTED_DEFAULT_HIERARCHY_VS_RELATION).
            		baseValueSetURI(SAVSDL_BASE_VALUESET_URI).
            		sourceName(SAVSDL_SOURCE_NAME).
            		build();
                new SourceAssertedValueSetBatchLoader(params,
                		"NCI", "Semantic_Type").run(params.getSourceName());
                
                Util.displayAndLogMessage("");
                Util.displayAndLogMessage("Value Set Definition Loading from " + SAVSDL_CODING_SCHEME + " Complete");
            }
            else {
                Util.displayAndLogMessage("* Skipping Source Asserted ValueSet Definition Load");
            }
            
            //*************************************************************
            // BuildAssertedValueSetIndex
            //*************************************************************
            Util.displayAndLogMessage("");
        	Util.displayAndLogMessage("****************************************");
        	Util.displayAndLogMessage("* Build Asserted ValueSet Index");
        	Util.displayAndLogMessage("****************************************");
        	Util.displayAndLogMessage("");
        	Util.displayAndLogMessage("\tParameters: ");
        	Util.displayAndLogMessage("\tbuildAssertedValueSetIndexURN:    " + buildAssertedValueSetIndexURNStr);
        	Util.displayAndLogMessage("\tbuildAssertedValueSetIndexVersion:" + buildAssertedValueSetIndexVersionStr);
        	Util.displayAndLogMessage("");
        	
        	// There is no option for confirmation/force option in this script.
            if (buildAssertedValueSetIndex) {
            	CodingSchemeSummary css = null;
            	
            	// Find in list of registered vocabularies ...
                if (buildAssertedValueSetIndexVersionStr != null && buildAssertedValueSetIndexVersionStr != null) {
                	
                	lbs = LexBIGServiceImpl.defaultInstance();
                    Enumeration<? extends CodingSchemeRendering> schemes = lbs.getSupportedCodingSchemes()
                    		.enumerateCodingSchemeRendering();
                    while (schemes.hasMoreElements() && css == null) {
                        CodingSchemeSummary summary = schemes.nextElement().getCodingSchemeSummary();
                        if (buildAssertedValueSetIndexURNStr.equalsIgnoreCase(summary.getCodingSchemeURI())
                                && buildAssertedValueSetIndexVersionStr.equalsIgnoreCase(summary.getRepresentsVersion()))
                            css = summary;
                    }
                    
                    if (css == null) {
                    	Util.displayAndLogMessage("No matching coding scheme was found for the given URN or version.");
                        Util.displayAndLogMessage("");
                    }
                    else {
                    	AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference(css);
                    	 
                    	String indexName = "URI: " + ref.getCodingSchemeURN() + " VERSION: " + 
                    	            ref.getCodingSchemeVersion();
                    	 
                    	try {
                            ProcessRunner avsil_loader = new AssertedValueSetIndexLoaderImpl();
                            Util.displayAndLogMessage("Re-creation of index extension '" + 
                                    indexName + "' in progress...");
                            Util.displayAndLogMessage("");
                            
                            Util.displayStatus(avsil_loader.runProcess(ref, null));
                            
                            Util.displayAndLogMessage("");
                            Util.displayAndLogMessage("Re-creation of index extension '" + 
                                    indexName + "' completed");
                        } catch (UnsupportedOperationException e) {
                        	Util.displayAndLogMessage("");
                            Util.displayAndLogMessage("Build index extension for '" + indexName + "' is not supported.");
                            Util.displayAndLogMessage("");
                        }
                    }
                }
            }
            else {
            	Util.displayAndLogMessage("* Skipping Build Asserted ValueSet Index");
            }
            //*************************************************************
            // LoadNCIHistory
            //*************************************************************
            Util.displayAndLogMessage("");
        	Util.displayAndLogMessage("****************************************");
        	Util.displayAndLogMessage("* Load NCI History");
        	Util.displayAndLogMessage("****************************************");
        	Util.displayAndLogMessage("");
        	Util.displayAndLogMessage("Parameters: ");
        	Util.displayAndLogMessage("\tloadNCIHistoryInputFile :  " + loadNCIHistoryInput);
        	Util.displayAndLogMessage("\tloadNCIHistoryVersionFile: " + loadNCIHistoryVersionFile);
        	Util.displayAndLogMessage("\tloadNCIHistoryReplace:     " + loadNCIHistoryReplace);
        	Util.displayAndLogMessage("");
        	
            if (loadNCIHistory) {
                // Find the registered extension handling this type of load ...
                lbs = LexBIGServiceImpl.defaultInstance();
                lbsm = lbs.getServiceManager(null);
                NCIHistoryLoader historyLoader = (NCIHistoryLoader) lbsm
                        .getLoader(org.LexGrid.LexBIG.Impl.loaders.NCIHistoryLoaderImpl.name);

                historyLoader.load(historySource, historyVersions, !loadNCIHistoryReplace, false, true);
                    Util.displayLoaderStatus(historyLoader);
            }
            else {
            	Util.displayAndLogMessage("* Skipping NCI History");
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

        o = new Option("ncit", "ncitLoad", false, "Use this parameter to initiate the OWL2 load option of NCIt.");
        o.setArgName("uri");
        o.setRequired(false);
        options.addOption(o);
        
        o = new Option("in", "input", true, "URI or path specifying location of the source file.");
        o.setArgName("uri");
        o.setRequired(false);
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
        
        //RemoveAllValueSetDefinitions
        o = new Option("rvsd", "RemoveAllValueSetDefinitions", false, "Remove All Value Set Definitions");
        o.setRequired(false);
        options.addOption(o);
        
        //SourceAssertedValueSetDefinitionLoad
        o = new Option("savsdl", "SourceAssertedValueSetDefinitionLoad", false, "Source Asserted Value Set Definition Load");
        o.setRequired(false);
        options.addOption(o);
        
        //SourceAssertedValueSetDefinitionLoad version
        o = new Option("savsdl_v", "SourceAssertedValueSetDefinitionLoad_version", true, "Source Asserted Value Set Definition Load - Version of the coding scheme");
        o.setRequired(false);
        options.addOption(o);
        
        //BuildAssertedValueSetIndex 
        o = new Option("bavsi", "BuildAssertedValueSetIndex", false, "Build Asserted ValueSet Index");
        o.setRequired(false);
        options.addOption(o);
        
        //BuildAssertedValueSetIndex URN
        o = new Option("bavsi_u", "BuildAssertedValueSetIndex_urn", true, "Build Asserted ValueSet Index URN uniquely identifying the code system.");
        o.setRequired(false);
        options.addOption(o);

        //BuildAssertedValueSetIndex Version
        o = new Option("bavsi_v", "BuildAssertedValueSetIndex_version", true, "Build Asserted ValueSet Index Version identifier.");
        o.setRequired(false);
        options.addOption(o);
        
        //LoadNCIHistory 
        o = new Option("lncih", "LoadNCIHistory", false, "Load NCI History");
        o.setRequired(false);
        options.addOption(o);
        
        //LoadNCIHistory input
        o = new Option("lncih_in", "loadNCIHistory_input", true, "Load NCI History input file");
        o.setRequired(false);
        options.addOption(o);
        
        //LoadNCIHistory version file
        o = new Option("lncih_vf", "LoadNCIHistory_versionFile", true, "Load NCI History version file");
        o.setRequired(false);
        options.addOption(o);
        
        //LoadNCIHistory replace
        o = new Option("lncih_r", "LoadNCIHistory_replace", false, "If specified, the current history database is overwritten.");
        o.setRequired(false);
        options.addOption(o);
        
        return options;
    }

}