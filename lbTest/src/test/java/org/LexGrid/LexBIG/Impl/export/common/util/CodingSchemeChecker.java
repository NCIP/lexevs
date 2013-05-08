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
package org.LexGrid.LexBIG.Impl.export.common.util;

import java.util.Enumeration;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;

public class CodingSchemeChecker {
    /*
     * checks LexGrid for the existence of a coding scheme
     */
    public static boolean exists(String codingSchemeUri, String version) throws LBException {
    	Logger.log("CodingSchemeChecker: exists: entry");
    	Logger.log("CodingSchemeChecker: exists: uri: " + codingSchemeUri + " version: " + version);
    	boolean rv = false;
    	
        LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
        
        // Find in list of registered vocabularies ...
        CodingSchemeSummary css = null;
        if (codingSchemeUri != null && version != null) {
        	codingSchemeUri = codingSchemeUri.trim();
            version = version.trim();
            Enumeration<? extends CodingSchemeRendering> schemes = lbs.getSupportedCodingSchemes()
                    .enumerateCodingSchemeRendering();
            while (schemes.hasMoreElements() && css == null) {
                CodingSchemeSummary summary = schemes.nextElement().getCodingSchemeSummary();
                if (codingSchemeUri.equalsIgnoreCase(summary.getCodingSchemeURI())
                        && version.equalsIgnoreCase(summary.getRepresentsVersion()))
                    css = summary;
            }
        }

        // Found it? If if so, return true
        if (css != null) {
        	rv = true;
        }
        
        Logger.log("CodingSchemeChecker: exists: exit");
        return rv;
    }

}