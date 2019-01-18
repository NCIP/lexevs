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

import java.util.Enumeration;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.annotations.LgAdminFunction;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang.StringUtils;
import org.lexevs.exceptions.CodingSchemeParameterException;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;
import org.lexevs.registry.service.Registry.ResourceType;

/**
 * Utility to register, unregister, and list Coding Scheme Supplements
 * 
 * <pre>
 * usage: SupplementScheme [-parentUri parentUri] [-r] [-supplementUri
 *     supplementUri] [-parentVersion parentVersion] [-l] [-supplementVersion
 *     supplementVersion] [-u]
 * -l,--list                             List Supplements
 * -parentUri <parentUri>                Parent URI.
 * -parentVersion <parentVersion>        Parent Version.
 * -r                                    Register Coding Scheme as a Supplement
 * -supplementUri <supplementUri>        Supplement URI.
 * -supplementVersion <supplementVersion>Supplement Version.
 * -u                                    Unregister Coding Scheme as a Supplement
 *
 * Example: SupplementScheme -u -parentUri "urn:oid:2.16.840.1.113883.3.26.1.1" -parentVersion "05.09e" -supplementUri "http://supplement.ontology.org" -supplementVersion "1.0.1" 
 *
 * Note: If the URN and version values are unspecified, a list of
 * available coding schemes will be presented for user selection.
 * </pre>
 * 
 * @author <A HREF="mailto:kevin.peterson@mayo.edu">Kevin Peterson</A>
 */
@LgAdminFunction
public class SupplementScheme {

    public static void main(String[] args) {
        try {
            new SupplementScheme().run(args);
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public SupplementScheme() {
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
            Util.displayCommandOptions("SupplementScheme", options,
                    "SupplementScheme -u -parentUri \"urn:oid:2.16.840.1.113883.3.26.1.1\" -parentVersion \"05.09e\" "+
                        "-supplementUri \"http://supplement.ontology.org\" -supplementVersion \"1.0.1\" ",
                    e);
            Util.displayMessage(Util.getPromptForSchemeHelp());
            return;
        }

        boolean list = cl.hasOption("l");
        
        if(list) {
            this.listSupplements();
            return;
        }
        
        boolean register = cl.hasOption("r");
        boolean unregister = cl.hasOption("u");
        
        if(register && unregister) {
            Util.displayMessage("Cannot choose both registering (-r) and unregistering (-u) at the same time.");
            return;
        }
        
        if(! (register || unregister)) {
            Util.displayMessage("Must choose either registering (-r) or unregistering (-u).");
            return;
        }
        
        // Interpret provided values ...
        String parentUri = cl.getOptionValue("parentUri");
        String parentVersion = cl.getOptionValue("parentVersion");
        String supplementUri = cl.getOptionValue("supplementUri");
        String supplementVersion = cl.getOptionValue("supplementVersion");
       
        Util.displayMessage("======================");
        Util.displayMessage("Choose a Parent Scheme");
        Util.displayMessage("======================");
        Util.displayMessage("Enter the number of the Coding scheme to use, then <Enter> :");
        AbsoluteCodingSchemeVersionReference parent =
            this.getOntology(parentUri, parentVersion);
        
        Util.displayMessage("==========================");
        Util.displayMessage("Choose a Supplement Scheme");
        Util.displayMessage("==========================");
        Util.displayMessage("Enter the number of the Coding scheme to use, then <Enter> :");
        AbsoluteCodingSchemeVersionReference supplement =
            this.getOntology(supplementUri, supplementVersion);


        try {
            if(register) {
                LexBIGServiceImpl.defaultInstance().
                    getServiceManager(null).
                        registerCodingSchemeAsSupplement(parent, supplement);
                
                Util.displayMessage("Coding Scheme Registered as a Supplement.");
            } else {
                LexBIGServiceImpl.defaultInstance().
                getServiceManager(null).
                    unRegisterCodingSchemeAsSupplement(parent, supplement);
                
                Util.displayMessage("Coding Scheme Unregistered as a Supplement.");
            }
        } catch (CodingSchemeParameterException e) {
            Util.displayAndLogError("REQUEST FAILED !!! " , e);
            return;
        }
    }
    
    private void listSupplements() {
        Registry registry = LexEvsServiceLocator.getInstance().getRegistry();

        List<RegistryEntry> entries = registry.getAllRegistryEntriesOfType(ResourceType.CODING_SCHEME);

        boolean found = false;
        for(RegistryEntry entry : entries) {
            if(StringUtils.isNotBlank(entry.getSupplementsUri())
                    &&
                    StringUtils.isNotBlank(entry.getSupplementsVersion())){
                found = true;
                Util.displayAndLogMessage("================================================");
                Util.displayAndLogMessage("PARENT:     [URI]-" + entry.getSupplementsUri());
                Util.displayAndLogMessage("            [VERSION]-" + entry.getSupplementsVersion());
                Util.displayAndLogMessage("SUPPLEMENT: [URI]-" + entry.getResourceUri());
                Util.displayAndLogMessage("            [VERSION]-" + entry.getResourceVersion());
                Util.displayAndLogMessage("================================================");
            }
        }

        if(! found){
            Util.displayAndLogMessage("No Registered Supplements.");
        }
    }

    private AbsoluteCodingSchemeVersionReference getOntology(String uri, String version) throws LBInvocationException {
        CodingSchemeSummary css = null;

        // Find in list of registered vocabularies ...
        if (uri != null && version != null) {
            uri = uri.trim();
            version = version.trim();
            LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
            Enumeration<? extends CodingSchemeRendering> schemes = lbs.getSupportedCodingSchemes()
            .enumerateCodingSchemeRendering();
            while (schemes.hasMoreElements() && css == null) {
                CodingSchemeSummary summary = schemes.nextElement().getCodingSchemeSummary();
                if (uri.equalsIgnoreCase(summary.getCodingSchemeURI())
                        && version.equalsIgnoreCase(summary.getRepresentsVersion()))
                    css = summary;
            }
        }

        // Found it? If not, prompt...
        if (css == null) {
            if (uri != null || version != null) {
                Util.displayAndLogMessage("No matching coding scheme was found for the given URN or version.");
                Util.displayAndLogMessage("");
            }
            css = Util.promptForCodeSystem();
            if (css == null)
                return null;
        }

        return Constructors.createAbsoluteCodingSchemeVersionReference(css);
    }
    
    /**
     * Return supported command options.
     * 
     * @return org.apache.commons.cli.Options
     */
    private Options getCommandOptions() {
        Options options = new Options();
        Option o;
        
        o = new Option("l", "list", false, "List Supplements");
        o.setArgName("list");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("u", null, false, "Unregister Coding Scheme as a Supplement");
        o.setArgName("u");
        o.setRequired(false);
        options.addOption(o);
        
        o = new Option("r", null, false, "Register Coding Scheme as a Supplement");
        o.setArgName("r");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("parentUri", null, true, "Parent URI.");
        o.setArgName("parentUri");
        o.setRequired(false);
        options.addOption(o);
        
        o = new Option("parentVersion", null, true, "Parent Version.");
        o.setArgName("parentVersion");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("supplementUri", null, true, "Supplement URI.");
        o.setArgName("supplementUri");
        o.setRequired(false);
        options.addOption(o);
        
        o = new Option("supplementVersion", null, true, "Supplement Version.");
        o.setArgName("supplementVersion");
        o.setRequired(false);
        options.addOption(o);

        return options;
    }
}