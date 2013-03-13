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

import java.io.IOException;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.admin.Util;
import org.LexGrid.annotations.LgAdminFunction;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

/**
 * Load the resolved ValueSet Definition 
 * 
 * <pre>
 * Example: java org.LexGrid.LexBIG.admin.LoadResolvedValueSetDefinition
 *   -u, URN uniquely identifying the ValueSet Definition
 *   -l, &lt;id&gt; List of coding scheme versions to use for the resolution
 *       in format "csuri1::version1, csuri2::version2"
 *   -csVersionTag Tag to use for resolution, eg: development, production"    
 * 
 * Note: If the URN and version values are unspecified, a
 * list of available coding schemes will be presented for
 * user selection.
 * 
 * Example: java -Xmx512m -cp lgRuntime.jar
 *  org.LexGrid.LexBIG.admin.LoadResolvedValueSetDefinition
 *    -u &quot;Automobiles:valuesetDefinitionURI&quot; -l &quot;Automobiles::version1, GM::version2&quot; -csVersionTag &quot;production&quot
 * </pre>
 * 
 * @author <A HREF="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala</A>
 */
@LgAdminFunction
public class LoadResolvedValueSetDefinition {

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
                    "LoadResolvedValueSetDefinition -u \"Automobiles:valuesetDefinitionURI\" -l \"Automobiles::version1, GM::version2\" -csVersionTag \"production\" ", e);
            Util.displayMessage(Util.getPromptForSchemeHelp());
            return;
        }

        // Interpret provided values ...
        String urn = cl.getOptionValue("u");
        String csList = cl.getOptionValue("l");
        String csVersionTag = cl.getOptionValue("csVersionTag");
        boolean force = cl.hasOption("f");
        CodingSchemeSummary css = null;

        // Find in list of valueset definitions  ...
        if (urn != null) {
            urn = urn.trim();
            LexEVSValueSetDefinitionServices valueSetService =  LexEVSValueSetDefinitionServicesImpl.defaultInstance();
            List<String> valuesetUris = valueSetService.listValueSetDefinitionURIs();
            if (!valuesetUris.contains(urn)) {
            	Util.displayMessage("No valueSet definition found for the given  URN");
                Util.displayMessage("");
            }
           
        }

        // Found it? If not, prompt...
        if (css == null) {
            if (urn != null || csList != null) {
                Util.displayMessage("No matching coding scheme was found for the given URN or version.");
                Util.displayMessage("");
            }
            css = Util.promptForCodeSystem();
            if (css == null)
                return;
        }

        AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference(css);

        rebuildTransitivityTable(ref, force);
    }

    /**
     * Rebuild a single named index extension for the specified scheme.
     * 
     * @param lbsm
     * @param ref
     * @param indexName
     * @paam force
     * @throws LBException
     */
    protected void rebuildTransitivityTable(AbsoluteCodingSchemeVersionReference ref,
            boolean force) throws LBException {
        
        String codingScheme = "URI: " + ref.getCodingSchemeURN() + " VERSION: " + 
            ref.getCodingSchemeVersion();

        // Confirm the action (if not bypassed by force option) ...
        boolean confirmed = force;
        if (!confirmed) {
            Util.displayMessage("REBUILD TRANSITIVITY TABLE FOR " + codingScheme + "? ('Y' to confirm, any other key to cancel)");
            try {
                char choice = Util.getConsoleCharacter();
                confirmed = choice == 'Y' || choice == 'y';
            } catch (IOException e) {
            }
        }

        // Action confirmed?
        if (confirmed) {
            try {
               
                Util.displayTaggedMessage("Recreation Transitivity Table '" + 
                        codingScheme + "' in progress...");
                LexEvsServiceLocator.
                    getInstance().
                        getLexEvsDatabaseOperations().
                            reComputeTransitiveTable(
                                    ref.getCodingSchemeURN(), 
                                    ref.getCodingSchemeVersion());
            } catch (UnsupportedOperationException e) {
                Util.displayTaggedMessage("Recreation Transitivity Table '" + codingScheme + "' is not supported.");
            }
        } else {
            Util.displayTaggedMessage("Recreation Transitivity Table '" + codingScheme + "' cancelled by user.");
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
        o.setRequired(false);
        options.addOption(o);

        o = new Option("l", "list", true, "List of coding schemes to use.");
        o.setArgName("id");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("csVersionTag", "csVersionTag", false, "Coding Scheme version tag to use");
        o.setRequired(false);
        options.addOption(o);

        return options;
    }

}