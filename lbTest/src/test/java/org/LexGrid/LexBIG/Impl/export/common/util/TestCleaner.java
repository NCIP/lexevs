
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