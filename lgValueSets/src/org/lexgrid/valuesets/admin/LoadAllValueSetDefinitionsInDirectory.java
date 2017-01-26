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
                	throw new RuntimeException();
                }
            } catch (Exception e) {
                Util.displayCommandOptions(
                                "LoadAllValueSetDefinitionsInDirectory",
                                options,
                                "\n LoadAllValueSetDefinitionsInDirectory -in \"/path/to/directory\""
                                        + "\n LoadAllValueSetDefinitionsInDirectory -in \"/path/to/directoryl\" " + Util.getURIHelp(), e);
                return;
            }
            
           

            LexEVSValueSetDefinitionServices vds = LexEVSValueSetDefinitionServicesImpl.defaultInstance();
            
            
            File dir = new File(cl.getOptionValue("in"));
            File[] directoryListing = dir.listFiles();
            if (directoryListing != null && directoryListing.length > 0) {
              for (File source : directoryListing) {
            	  
                try {
					vds.loadValueSetDefinition(source.toString(), false);
				} catch (LBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                Util.displayMessage("Value Set from file: " + source.toString() + " loaded");
              }
            } else {
              Util.displayMessage("No files or invalid directory");
            }

            }
            Util.displayMessage("Request to Load ValueSet Defintions Completed!");
      
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