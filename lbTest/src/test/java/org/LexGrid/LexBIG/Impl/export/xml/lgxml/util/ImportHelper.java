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
package org.LexGrid.LexBIG.Impl.export.xml.lgxml.util;

import java.net.URI;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Load.LexGrid_Loader;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.export.common.util.Logger;
import org.LexGrid.LexBIG.Impl.export.common.util.StringToFileUriConverter;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;

public class ImportHelper {
    
    /*
     * runs the XML Importer 
     */
    public static boolean importLgXml(String fileName) throws LBException {
    	
    	boolean rv = false;
    	
    	Logger.log("ImportHelper: importLgXml: exit");
    	Logger.log("ImportHelper: importLgXml: source file: " + fileName);
    	// convert filename to URI
    	URI source = StringToFileUriConverter.convert(fileName);
    	
    	// get the importer from LexEVS
        LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
        LexBIGServiceManager lbsm = lbs.getServiceManager(null);
        LexGrid_Loader loader = (LexGrid_Loader) lbsm
                .getLoader(org.LexGrid.LexBIG.Impl.loaders.LexGridMultiLoaderImpl.name);

        // do the load
    	loader.load(source, false, false);
    	
    	// activate the coding scheme
        AbsoluteCodingSchemeVersionReference[] refs = loader.getCodingSchemeReferences();
        for (int i = 0; i < refs.length; i++) {
            AbsoluteCodingSchemeVersionReference ref = refs[i];
            lbsm.activateCodingSchemeVersion(ref);
            Logger.log("Scheme activated>> " + ref.getCodingSchemeURN() + " Version>> "
                    + ref.getCodingSchemeVersion());
        }
        Logger.log("ImportHelper: importLgXml: exit");
        rv = true;
        return rv;
    }    
}