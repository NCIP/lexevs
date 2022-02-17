
package org.lexgrid.valuesets.admin;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Load.ResolvedValueSetDefinitionLoader;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.admin.Util;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.lexevs.system.service.LexEvsResourceManagingService;
import org.lexgrid.loader.ResolvedValueSetDefinitionLoaderImpl;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

public class LoadAllDefinitionsToResolvedValueSet {
	private LexEvsResourceManagingService service = new LexEvsResourceManagingService();
	
	public void run(String[] args){
		synchronized (service) {
        CommandLine cl = null;
        String vsdName = null;
        Options options = getCommandOptions();
        try {
            cl = new BasicParser().parse(options, args);
        } catch (Exception e) {
        	Util.displayAndLogError("Parsing of command line options failed: " + e.getMessage() , e);
            Util.displayCommandOptions(
                            "LLoadAllDefinitionsToResolvedValueSet",
                            options,
                            "\n LoadAllDefinitionsToResolvedValueSet -uri \"http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#"
                                    + "\n LoadAllDefinitionsToResolvedValueSet -uri http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#" 
                            		+ Util.getURIHelp(), e);
            return;
        }
        
		LexEVSValueSetDefinitionServices valueSetService =  LexEVSValueSetDefinitionServicesImpl.defaultInstance();
		LexBIGService lbsvc = LexBIGServiceImpl.defaultInstance();

    	int counter = 0;
		long start = System.nanoTime();	
		String codingSchemeUri = "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#";
		if(cl.getOptionValue("uri") != null){
			codingSchemeUri = cl.getOptionValue("uri");
		}
		List<String> list = valueSetService.listValueSetDefinitionURIs();

		for(String uri : list){
			try {	
				
				ResolvedValueSetDefinitionLoader loader =  new ResolvedValueSetDefinitionLoaderImpl();
				Util.displayAndLogMessage("Attempting to load value set for: " + uri + " :" +
						vsdName);
				LexBIGServiceManager lbsm = lbsvc.getServiceManager(null);
				ValueSetDefinition def = valueSetService.getValueSetDefinition(new URI(uri), null);
			    AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
			    ref.setCodingSchemeURN(codingSchemeUri);			
			    ref.setCodingSchemeVersion(lbsvc.resolveCodingScheme(codingSchemeUri, null).getRepresentsVersion());
			    AbsoluteCodingSchemeVersionReferenceList refList = new AbsoluteCodingSchemeVersionReferenceList();
			    refList.addAbsoluteCodingSchemeVersionReference(ref);
			    vsdName = def.getValueSetDefinitionName();
				loader.load(new URI(uri), null, refList, ref.getCodingSchemeVersion(), null);
				Util.displayLoaderStatus(loader);
				while(loader.getStatus().getEndTime() == null){
					Thread.sleep(2000);
				}				
				if(!loader.getStatus().getErrorsLogged()){
				lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);
				lbsm.setVersionTag(loader.getCodingSchemeReferences()[0],"PRODUCTION");
				Util.displayAndLogMessage("Loaded and activiated resolved value set for: " + uri + " :" +
				vsdName);
				}
				else{
				Util.displayAndLogMessage("Error loading value set: " + uri + " :" +
						vsdName);	
				}
				counter++;
				if(counter % 100 == 0){
					Util.displayAndLogMessage("Resolved and loaded " + counter + " value sets");
					Util.displayAndLogMessage("Total time expended: " + TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - start) + " seconds");
				}
			} catch (LBException e) {
				Util.displayAndLogError("Error getting value set definition: " + uri + " :" +
						vsdName, e);
				throw new RuntimeException("Error getting value set definition: " + uri + " :" +
						vsdName, e);
			} catch (URISyntaxException e) {
				Util.displayAndLogError("Malformed URI.  Check ValueSet Definition", e);
				throw new RuntimeException("Malformed URI.  Check ValueSet Definition", e);
			} catch (Exception e) {
				Util.displayAndLogError("Value Set failed to resolve/load: ", e);
				throw new RuntimeException("Value Set failed to resolve/load: ", e);
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

        o = new Option("uri", "uri", true, "URI of the code system to resolve to.");
        options.addOption(o);
        
        return options;
    }


	public static void main(String[] args) {
		new LoadAllDefinitionsToResolvedValueSet().run(args);
	}
}