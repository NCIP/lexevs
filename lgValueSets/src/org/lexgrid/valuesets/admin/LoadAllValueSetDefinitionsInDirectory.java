
package org.lexgrid.valuesets.admin;

import java.io.File;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.admin.Util;
import org.LexGrid.annotations.LgAdminFunction;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.lexevs.system.service.LexEvsResourceManagingService;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

@LgAdminFunction
public class LoadAllValueSetDefinitionsInDirectory {
	private LexEvsResourceManagingService service = new LexEvsResourceManagingService();
	public static void main(String[] args) {
        try {
            new LoadAllValueSetDefinitionsInDirectory().run(args);
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public LoadAllValueSetDefinitionsInDirectory() {
        super();
    }

/**
     * Primary entry point for the program.
     * 
     * @throws Exception
     */
public void run(String[] args)  {
        synchronized (service) {

            // Parse the command line ...
            CommandLine cl = null;
            Options options = getCommandOptions();
            try {
                cl = new BasicParser().parse(options, args);
                if(cl.getOptionValue("in")==null){
                	Util.displayAndLogMessage("file path not defined");
                	throw new RuntimeException();
                }
            } catch (Exception e) {
            	Util.displayAndLogError("Parsing of command line options failed: " + e.getMessage() , e);
                Util.displayCommandOptions(
                                "LoadAllValueSetDefinitionsInDirectory",
                                options,
                                "\n LoadAllValueSetDefinitionsInDirectory -in \"/path/to/directory\""
                                        + "\n LoadAllValueSetDefinitionsInDirectory -in \"/path/to/directoryl\" " + Util.getURIHelp(), e);
                return;
            }
            
            
            File dir = new File(cl.getOptionValue("in"));
            File[] directoryListing = dir.listFiles();
            if (directoryListing != null && directoryListing.length > 0) {
              for (File source : directoryListing) {

                  LexEVSValueSetDefinitionServices vds = LexEVSValueSetDefinitionServicesImpl.defaultInstance(); 
                try {
					vds.loadValueSetDefinition(source.toString(), false);
				} catch (LBException e) {
					Util.displayAndLogError("Value set " +  source.getName() + " load failed", e);
					e.printStackTrace();
				}
                Util.displayAndLogMessage("Value Set from file: " + source.toString() + " loaded");
              }
            } else {
              Util.displayAndLogMessage("No files or invalid directory");
            }

            }
            Util.displayAndLogMessage("Request to Load ValueSet Defintions Completed!");
      
    }

    /**
     * Return supported command options.
     * 
     * @return org.apache.commons.cli.Options
     */
    private Options getCommandOptions() {
        Options options = new Options();
        Option o;

        o = new Option("in", "input", true, "URI or path specifying location of the source directory.");
        options.addOption(o);
        
        return options;
    }

}