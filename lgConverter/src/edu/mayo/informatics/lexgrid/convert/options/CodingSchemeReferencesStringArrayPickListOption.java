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
package edu.mayo.informatics.lexgrid.convert.options;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;

/**
 * The Class StringOption.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CodingSchemeReferencesStringArrayPickListOption extends StringOption{
 
    /**
     * Instantiates a new string option.
     * 
     * @param optionName the option name
     */
    public CodingSchemeReferencesStringArrayPickListOption(String optionName, CodingSchemeRenderingList schemes) {
        super(optionName);
        this.setPickList(buildPickList(schemes));
 
    }
    
    private static List<String> buildPickList(CodingSchemeRenderingList schemes){
        List<String> returnList = new ArrayList<String>();
        for(CodingSchemeRendering csr : schemes.getCodingSchemeRendering()) {
            returnList.add(
                    buildOptionValue(
                            csr.getCodingSchemeSummary().getCodingSchemeURI(),
                            csr.getCodingSchemeSummary().getRepresentsVersion()));
        }
        
        return returnList;
    }
    
    public static String buildOptionValue(String uri, String version){  
        return uri + " - " + version;
    }
    
    public static AbsoluteCodingSchemeVersionReference getAbsoluteCodingSchemeVersionReference(String value, CodingSchemeRenderingList schemes) throws LBParameterException{
        List<String> possibilities = buildPickList(schemes);
        
        for(int i=0;i<possibilities.size();i++) {
            if(possibilities.get(i).equals(value)) {
                AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
                ref.setCodingSchemeURN(schemes.getCodingSchemeRendering(i).getCodingSchemeSummary().getCodingSchemeURI());
                ref.setCodingSchemeVersion(schemes.getCodingSchemeRendering(i).getCodingSchemeSummary().getRepresentsVersion());
            
                return ref;
            }
        }
        
        throw new LBParameterException("Could not find a coding scheme for value: ", value);
    }
}