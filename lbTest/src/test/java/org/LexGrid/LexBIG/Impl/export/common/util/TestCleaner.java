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

import java.io.File;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;

/*
 * cleans up test artifacts
 *   - deletes generated file
 *   - removes coding scheme
 */
public class TestCleaner {
    
    public static void cleanUp(String outFile, String csUri, String csVersion) {
    	Logger.log("TestCleaner: cleanUp: entry");
    	// delete generated export file
    	TestCleaner.deleteExportedData(outFile);
    	
    	// remove imported coding scheme
    	TestCleaner.removeCodingScheme(csUri, csVersion);
    	Logger.log("TestCleaner: cleanUp: exit");
    }
    
    private static void deleteExportedData(String fileName) {
    	Logger.log("TestCleaner: deleteExportFile: entry");
    	Logger.log("TestCleaner: deleteExportFile: fileName: " + fileName);
    	File exportFile = new File(fileName);
    	
    	if(exportFile.isDirectory() == true) {
    		File file = null;
    		String[] fileNames = exportFile.list();
    		boolean rv;
    		
    		for(int i=0; i<fileNames.length; ++i) {
    			file = new File(exportFile.getAbsoluteFile(), fileNames[i]);
    			rv = file.delete();
    			if(rv == false) {
    				Logger.log("TestCleaner: deleteExportFile: WARNING: file: " + file.getName() + " could not be deleted.");
    				file.deleteOnExit();
    			} else {
    				Logger.log("TestCleaner: deleteExportFile: file: " + file.getAbsolutePath() + " deleted successfully.");
    			}
    		}
    	}
    	            	
        Logger.log("TestCleaner: deleteExportFile: exit");
    }
    
    
	private static boolean removeCodingScheme(String csUri, String csVersion) {
		Logger.log("TestCleaner: removeCodingScheme: entry");
		Logger.log("TestCleaner: removeCodingScheme: csUri: " + csUri);
		Logger.log("TestCleaner: removeCodingScheme: csVersion: " + csVersion);
		boolean returnValue;		
		try {
			AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(csUri, csVersion);
	        LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
	        LexBIGServiceManager lbsm = lbs.getServiceManager(null);
			lbsm.deactivateCodingSchemeVersion(a,null);
			lbsm.removeCodingSchemeVersion(a);
			returnValue = true;
		} catch (LBException e) {
			returnValue = false;
			// e.printStackTrace();
			Logger.log("TestCleaner: removeCodingScheme: WARNING: caught exception: " + e.getMessage());
		}
		Logger.log("TestCleaner: removeCodingScheme: exit");
		return returnValue;
	}
    

}