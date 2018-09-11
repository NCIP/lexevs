package org.lexgrid.valuesets.admin;


import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.admin.Util;
import org.LexGrid.annotations.LgAdminFunction;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

public class RemoveAllValueSetDefinitions {

	@LgAdminFunction
	public static void main(String[] args) {
		try {
			new RemoveAllValueSetDefinitions().run(args);
		} catch (final Exception e) {
			Util.displayAndLogError("REQUEST FAILED !!!", e);
		}
	}

	public void run(String[] args)  {

		// Parse the command line ...
        CommandLine cl = null;
        Options options = getCommandOptions();
		
        try {
            cl = new BasicParser().parse(options, args);
        } catch (ParseException e) {
            Util.displayCommandOptions("RemoveAllValueSetDefinitions", options,
                    "RemoveAllValueSetDefinitions -f", e);
            return;
        }
        
        boolean force = cl.hasOption("f");
        
        System.out.println("Force option: " + force);
        char choice = 0;
        
        // if user doesn't force, then confirm
        if (!force) {
			Util.displayMessage("REMOVING ALL VALUE SET DEFINITIONS. "
					+ "DO YOU REALLY WANT TO DO THIS? ('Y' to confirm, any other key to cancel)");
			try {
				choice = Util.getConsoleCharacter();
			} catch (IOException e) {
				Util.displayMessage("Error Reading Input");
			}
        }
		if (force || choice == 'Y' || choice == 'y'){
			final LexEVSValueSetDefinitionServices vss = new LexEVSValueSetDefinitionServicesImpl();
			List<String> uris = vss.listValueSetDefinitionURIs();
			for (String urn : uris) {
				try {
					vss.removeValueSetDefinition(URI.create(urn));
				} catch (LBException e) {
					e.printStackTrace();
				}
				Util.displayMessage("ValueSetDefinition removed: " + urn);
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

        o = new Option("f", "force", false, "Force removing all value set definitions without confirmation.");
        o.setRequired(false);
        options.addOption(o);

        return options;
    }
}
