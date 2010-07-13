package org.LexGrid.LexBIG.Impl.export.xml.lgxml.util;

import java.net.URI;
import java.util.Enumeration;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExportStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.exporters.LexGridExport;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;

public class ExportHelper {
    
    /*
     * runs the LG XML exporter
     */
    public static boolean export(
    		String codingSchemeUri, 
    		String version, 
    		String exportDir,
    		boolean overwrite,
    		CodedNodeSet cns, 
    		CodedNodeGraph cng ) 
    throws LBException {
    	Logger.log("ExportHelper: export: entry");
    	Logger.log("ExportHelper: export: codingSchemeUri: " + codingSchemeUri);
    	Logger.log("ExportHelper: export: version: " + version);
    	Logger.log("ExportHelper: export: exportDir: " + exportDir);
    	Logger.log("ExportHelper: export: overwrite: " + overwrite);
        LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
        LexBIGServiceManager lbsm = lbs.getServiceManager(null);

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
        

        URI destination = StringToFileUriConverter.convert(exportDir);

        // Find the registered extension handling this type of export ...
        LexGridExport exporter = (LexGridExport) lbsm.getExporter(LexGridExport.name);

        // Perform the requested action ...
        exporter.setCng(cng);
        exporter.setCns(cns);
        
        exporter.export(
        		Constructors.createAbsoluteCodingSchemeVersionReference(css), 
        		destination, 
        		overwrite,
                false, false);
        
        ExportStatus status = exporter.getStatus();
        String s = status.getMessage();
        
        Logger.log("ExportHelper: export: export status message: " + s);
        
        boolean rv = false;
        if(status.getErrorsLogged() == false && status.getState().equals(ProcessState.COMPLETED) == true) {
        	rv = true;
        } else {
        	Logger.log("ExportHelper: export: errors were logged or export status was not complete.");
        }
        Logger.log("ExportHelper: export: exit");
        
        return rv;


    }

}
