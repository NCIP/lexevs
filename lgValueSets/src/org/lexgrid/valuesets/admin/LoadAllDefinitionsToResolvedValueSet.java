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
import org.LexGrid.LexBIG.admin.Util;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry.ResourceType;
import org.lexgrid.loader.ResolvedValueSetDefinitionLoaderImpl;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

public class LoadAllDefinitionsToResolvedValueSet {
	
	public void run(String[] args){

        CommandLine cl = null;
        Options options = getCommandOptions();
        try {
            cl = new BasicParser().parse(options, args);
        } catch (Exception e) {
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

    	ResolvedValueSetDefinitionLoader loader =  new ResolvedValueSetDefinitionLoaderImpl();
    	int counter = 0;
		long start = System.nanoTime();	
		String codingSchemeUri = "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#";
		if(cl.getOptionValue("uri") != null){
			codingSchemeUri = cl.getOptionValue("uri");
		}
		List<String> list = valueSetService.listValueSetDefinitionURIs();

		for(String uri : list){
			try {
				LexBIGServiceManager lbsm = lbsvc.getServiceManager(null);
		
			    AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
			    ref.setCodingSchemeURN(codingSchemeUri);			
			    ref.setCodingSchemeVersion(lbsvc.resolveCodingScheme(codingSchemeUri, null).getRepresentsVersion());
			    AbsoluteCodingSchemeVersionReferenceList refList = new AbsoluteCodingSchemeVersionReferenceList();
			    refList.addAbsoluteCodingSchemeVersionReference(ref);
				loader.load(new URI(uri), null, refList, ref.getCodingSchemeVersion(), null);
				Util.displayLoaderStatus(loader);
				while(loader.getStatus().getEndTime() == null){
					Thread.sleep(2000);
				}				
				if(!loader.getStatus().getErrorsLogged()){
				lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);
				Util.displayMessage("Loaded and activiated resolved value set for: " + uri);
				}
				else{
				Util.displayMessage("Error loading value set: " + uri);	
				}
				counter++;
				if(counter % 100 == 0){
					Util.displayMessage("Resolved and loaded " + counter + " value sets");
					Util.displayMessage("Total time expended: " + TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - start) + " seconds");
				}
			} catch (LBException e) {
				throw new RuntimeException("Error getting value set definition: " + uri, e);
			} catch (URISyntaxException e) {
				throw new RuntimeException("Malformed URI.  Check ValueSet Definition", e);
			} catch (Exception e) {
				throw new RuntimeException("Value Set failed to resolve/load: ", e);
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
