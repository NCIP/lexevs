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
package edu.mayo.informatics.lexgrid.convert.directConversions.mrmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.loaders.MrmapRRFLoader;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.LBConstants;
import org.LexGrid.relations.Relations;

import junit.framework.TestCase;

public class TestLoadMrMap2Mappings extends TestCase {
	public void testLoadOneMappingFromMRMAP() throws LBException, InterruptedException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, FileNotFoundException{

        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        MrmapRRFLoader loader = (MrmapRRFLoader) lbsm.getLoader("MrMap_Loader");
        MappingRelationsUtil map = new  MappingRelationsUtil();
   	 HashMap<String, Relations> relationsMap = map.processMrSatBean("resources/testData/mrmap_mapping/MRSAT.RRF", "resources/testData/mrmap_mapping/MRMAP.RRF");
       for(Map.Entry<String, Relations> rel: relationsMap.entrySet()){
       loader.load(new File(("resources/testData/mrmap_mapping/MRMAP.RRF")).toURI(), 
        		new File("resources/testData/mrmap_mapping/MRSAT.RRF").toURI(), 
        		null, null, null, rel, true, true);

        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(500);
        }

        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);
        lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], LBConstants.KnownTags.PRODUCTION.toString());

       }
}
    private LexBIGServiceManager getLexBIGServiceManager() throws LBParameterException, LBInvocationException{
    	return LexBIGServiceImpl.defaultInstance().getServiceManager(null);
    }
}