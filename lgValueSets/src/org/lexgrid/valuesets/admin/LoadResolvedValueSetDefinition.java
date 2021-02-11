
package org.lexgrid.valuesets.admin;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Extensions.Load.ResolvedValueSetDefinitionLoader;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.admin.Util;
import org.LexGrid.annotations.LgAdminFunction;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.lexgrid.loader.ResolvedValueSetDefinitionLoaderImpl;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

/**
 * Load the resolved ValueSet Definition 
 * 
 * <pre>
 * Example: java org.lexgrid.valuesets.admin.LoadResolvedValueSetDefinition
 *   -u, URN uniquely identifying the ValueSet Definition
 *   -l, &lt;id&gt; List of coding scheme versions to use for the resolution
 *       in format "csuri1::version1, csuri2::version2"
 *   -csSourceTag, Resolves against scheme bearing this tag, eg: development, production" 
 *   -vsTag, User defined Tag to apply to the resulting resolved value set scheme  
 *   -vsVersion, Optional user defined version that can be applied to the resolved value set scheme
 * 
 * Note: If the URN and version values are unspecified, a
 * list of available coding schemes will be presented for
 * user selection.
 * 
 * Example: java -Xmx512m -cp lgRuntime.jar
 *  org.lexgrid.valuesets.admin.LoadResolvedValueSetDefinition
 *    -u &quot;Automobiles:valuesetDefinitionURI&quot; -l &quot;Automobiles::version1, GM::version2&quot; -csSourceTag &quot;production&quot -vsTag &quot;PRODUCTION&quot -vsVersion &quot;MyVSVersion&quot
 * </pre>
 * 
 * @author <A HREF="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala</A>
 */
@LgAdminFunction
public class LoadResolvedValueSetDefinition {
	boolean activate;
	
    public static void main(String[] args) {
        try {
            new LoadResolvedValueSetDefinition().run(args);
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public LoadResolvedValueSetDefinition() {
        super();
    }

    /**
     * Primary entry point for the program.
     * 
     * @throws Exception
     */
    public void run(String[] args) throws Exception {

        // Parse the command line ...
        CommandLine cl = null;
        Options options = getCommandOptions();
        try {
            cl = new BasicParser().parse(options, args);
        } catch (ParseException e) {
            Util.displayCommandOptions("LoadResolvedValueSetDefinition", options,
                    "LoadResolvedValueSetDefinition -u \"Automobiles:valuesetDefinitionURI\" -l \"Automobiles::version1, GM::version2\" -csVersionTag \"production\", vsTag \"PRODUCTION\"", e);
            Util.displayMessage(Util.getPromptForSchemeHelp());
            return;
        }

        // Interpret provided values ...
        String urn = cl.getOptionValue("u");
        String csList = cl.getOptionValue("l");
        String csSourceTag = cl.getOptionValue("csSourceTag");
        String vsTag = cl.getOptionValue("vsTag");
        String vsVersion = cl.getOptionValue("vsVersion");
        activate = cl.hasOption("a");

        // Find in list of valueset definitions  ...
        if (urn != null) {
            urn = urn.trim();
            LexEVSValueSetDefinitionServices valueSetService =  LexEVSValueSetDefinitionServicesImpl.defaultInstance();
            List<String> valuesetUris = valueSetService.listValueSetDefinitionURIs();
            if (!valuesetUris.contains(urn)) {
            	Util.displayAndLogMessage("No valueSet definition found for the given  URN");
                Util.displayAndLogMessage("");
                return;
            }
           
        }
        AbsoluteCodingSchemeVersionReferenceList acsvl= getCodingSchemeVersions(csList);
       
        load(urn, acsvl, null, csSourceTag, vsTag, vsVersion);
    }

    
    AbsoluteCodingSchemeVersionReferenceList getCodingSchemeVersions(String csv_list) {
    	AbsoluteCodingSchemeVersionReferenceList acsvl= new AbsoluteCodingSchemeVersionReferenceList();
    	if (csv_list != null) {
    		List<String> codingSchemeList= Arrays.asList(csv_list.split(","));
    		for (String codingSchemeWithVersion: codingSchemeList) {
    			AbsoluteCodingSchemeVersionReference ref;
    			String[] pair= codingSchemeWithVersion.split("::");
    			if (pair.length == 2 ) {
    				ref= Constructors.createAbsoluteCodingSchemeVersionReference(pair[0], pair[1]);
    			} else {
    				ref= Constructors.createAbsoluteCodingSchemeVersionReference(codingSchemeWithVersion, null);
    			}
    			acsvl.addAbsoluteCodingSchemeVersionReference(ref);
    		}
    		
    		
    	}
    	return acsvl;
    }

    protected void load(String valueSetDefinitionURI, AbsoluteCodingSchemeVersionReferenceList csVersionList, String valueSetDefinitionRevisionId,
            String  csVersionTag, String vsTag, String vsVersion) throws Exception {
    	//ResolvedValueSetDefinitionLoader loader = (ResolvedValueSetDefinitionLoader)LexBIGServiceImpl.defaultInstance().getServiceManager(null).getLoader(ResolvedValueSetDefinitionLoader.NAME);
    	ResolvedValueSetDefinitionLoader loader =  new ResolvedValueSetDefinitionLoaderImpl();
    	loader.load(new URI(valueSetDefinitionURI), valueSetDefinitionRevisionId, csVersionList, csVersionTag, vsVersion);
    	Util.displayLoaderStatus(loader);
		while (loader.getStatus().getEndTime() == null) {
			Thread.sleep(2000);
		}
    	LexBIGServiceManager lbsm = LexBIGServiceImpl.defaultInstance().getServiceManager(null);
        if (activate) {
            AbsoluteCodingSchemeVersionReference[] refs = loader.getCodingSchemeReferences();
            for (int i = 0; i < refs.length; i++) {
                AbsoluteCodingSchemeVersionReference ref = refs[i];
                lbsm.activateCodingSchemeVersion(ref);
                Util.displayAndLogMessage("Scheme activated>> " + ref.getCodingSchemeURN() + " Version>> "
                        + ref.getCodingSchemeVersion());
            }
        }
        if(vsTag != null){
            AbsoluteCodingSchemeVersionReference[] refs = loader.getCodingSchemeReferences();
            for (int i = 0; i < refs.length; i++) {
                AbsoluteCodingSchemeVersionReference ref = refs[i];
                lbsm.setVersionTag(ref, vsTag);
                Util.displayAndLogMessage("Scheme tagged>> " + ref.getCodingSchemeURN() + " Version>> "
                        + ref.getCodingSchemeVersion() + "tag>> " + vsTag);
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
        o.setArgName("urn");
        o.setRequired(true);
        options.addOption(o);
        
        o = new Option("a", "activate", true, "Activate the code system.");
        o.setArgName("activate");
        o.setRequired(false);
        o.setOptionalArg(true);
        options.addOption(o);

        o = new Option("l", "list", true, "List of coding schemes to use.");
        o.setArgName("id");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("csSourceTag", "csSourceTag", false, "Coding Scheme tag to use");
        o.setRequired(false);
        options.addOption(o);
        
        o = new Option("vsTag", "vsTag", true, "Tag for target resolved value set scheme");
        o.setRequired(false);
        options.addOption(o);
        
        o = new Option("vsVersion", "vsVersion", true, "Tag for target resolved value set scheme");
        o.setRequired(false);
        options.addOption(o);

        return options;
    }

}