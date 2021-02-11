
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