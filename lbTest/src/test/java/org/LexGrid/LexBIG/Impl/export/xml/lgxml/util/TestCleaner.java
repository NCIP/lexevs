package org.LexGrid.LexBIG.Impl.export.xml.lgxml.util;

import java.io.File;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
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
    	TestCleaner.deleteExportFile(outFile);
    	
    	// remove imported coding scheme
    	TestCleaner.removeCodingScheme(csUri, csVersion);
    	Logger.log("TestCleaner: cleanUp: exit");
    }
    
    private static void deleteExportFile(String fileName) {
    	Logger.log("TestCleaner: deleteExportFile: entry");
    	Logger.log("TestCleaner: deleteExportFile: fileName: " + fileName);
    	File exportFile = new File(fileName);
    	boolean deleteResult = exportFile.delete();
        if(deleteResult == false) {
        	
        	Logger.log("TestCleaner: deleteExportFile: WARNING: file: " + exportFile.getName() + " could not be deleted.");
        	Logger.log("TestCleaner: deleteExportFile: Please delete " + exportFile.getName() + " manually.");
        }    	
        Logger.log("TestCleaner: deleteExportFile: exit");
    }
    
    
	private static boolean removeCodingScheme(String csUri, String csVersion) {
		Logger.log("TestCleaner: removeCodingScheme: entry");
		Logger.log("TestCleaner: removeCodingScheme: csUri: " + csUri);
		Logger.log("TestCleaner: removeCodingScheme: csVersion: " + csVersion);
		boolean returnValue;		
		LexBIGServiceManager lbsm;
		try {
			AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(csUri, csVersion);
			lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);
			lbsm.deactivateCodingSchemeVersion(a,null);
			lbsm.removeCodingSchemeVersion(a);
			returnValue = true;
		} catch (LBException e) {
			returnValue = false;
			e.printStackTrace();
		}
		Logger.log("TestCleaner: removeCodingScheme: exit");
		return returnValue;
	}
    

}
