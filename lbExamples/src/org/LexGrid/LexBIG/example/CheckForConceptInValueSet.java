/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
 *      http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.LexGrid.LexBIG.example;

import java.net.URI;
import java.net.URISyntaxException;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

/**
 * Example showing how to find if a coded concept belongs to a value set. The program
 * accepts one parameter : The concept code
 * 
 * A list of value Set Definition available in the system will be displayed. User can choose
 * a particular value set to check the concept membership.
 * 
 * A list of Code System present in the system will also be displayed. User can select which 
 * Code System Version to be used to resolve Value Set Definition.
 * 
 * Example: CheckForConceptInValueSet "C123456"
 * 
 */
public class CheckForConceptInValueSet {
    public CheckForConceptInValueSet() {
        super();
    }

    /**
     * Entry point for processing.
     * 
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Example: CheckForConceptInValueSet \"C123456\" ");
            return;
        }

        try {
            String code = args[0];
            new CheckForConceptInValueSet().run(code);
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public void run(String code) throws LBException {
        ValueSetDefinition vsd = Util.promptForValueSetDefinition();
        if (vsd != null) {
            AbsoluteCodingSchemeVersionReferenceList acsvList = null;
            CodingSchemeSummary css = Util.promptForCodeSystem();
            if (css != null)
            {
                acsvList = new AbsoluteCodingSchemeVersionReferenceList();
                acsvList.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference(css.getCodingSchemeURI(), css.getRepresentsVersion()));
            }
            
            LexEVSValueSetDefinitionServices vsdServ = LexEVSValueSetDefinitionServicesImpl.defaultInstance();
            AbsoluteCodingSchemeVersionReference memberCSV = null;
            try {
                memberCSV = vsdServ.isEntityInValueSet(code, null, new URI(vsd.getValueSetDefinitionURI()), null, acsvList, null);
            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (memberCSV != null)
            {
                Util.displayMessage("Concept code : '" + code + "' found in :" );
                Util.displayMessage("\tValue Set Definition URI....: " + vsd.getValueSetDefinitionURI());
                Util.displayMessage("\tValue Set Definition Name...: " + vsd.getValueSetDefinitionName());
                Util.displayMessage("\tCoding Scheme URI...........: " + memberCSV.getCodingSchemeURN());
                Util.displayMessage("\tCoding Scheme Version.......: " + memberCSV.getCodingSchemeVersion());
            }
            else
            {
                Util.displayMessage("Concept code : '" + code + "' NOT found in :" );
                Util.displayMessage("\tValue Set Definition URI....: " + vsd.getValueSetDefinitionURI());
                Util.displayMessage("\tValue Set Definition Name...: " + vsd.getValueSetDefinitionName());
                Util.displayMessage("\tCoding Scheme Name..........: " + css.getFormalName());
                Util.displayMessage("\tCoding Scheme URI...........: " + css.getCodingSchemeURI());
                Util.displayMessage("\tCoding Scheme Version.......: " + css.getRepresentsVersion());
            }
        }
    }    
}