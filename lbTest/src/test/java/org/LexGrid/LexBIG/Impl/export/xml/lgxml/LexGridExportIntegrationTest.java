package org.LexGrid.LexBIG.Impl.export.xml.lgxml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Load.LexGrid_Loader;
import org.LexGrid.LexBIG.Impl.exporters.LexGridExport;
import org.LexGrid.LexBIG.Impl.loaders.LexGridMultiLoaderImpl;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.junit.Test;

import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.constants.Constants;
import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.constants.LexGridConstants;
import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.factory.CodingSchemeFactory;
import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.factory.MockLexGridObjectFactory;
import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.formatters.XmlContentWriter;

public class LexGridExportIntegrationTest extends TestCase {

    private LexBIGService lbs;
    private LexBIGServiceManager lbsm;
    private final String XMLLOADER = "LexGridLoader";
    private final String INPUT_FILE_NAME_AUTO2 = "resources/testData/Automobiles2.xml";
    private final String OUTPUT_FILE_NAME_AUTO2 = "Automobiles2-exported.xml";
    private final String CS_AUTO2_URI = "urn:oid:11.11.0.1";
    private final String CS_AUTO2_VERSION = "1.1";
    
    private File inputFile;
    private File outputFile;
    
    private static final String SEARCH_STRING_ENTITY_CODE_AUTOMOBILE = "entityCode=\"A0001\"";
    private static final String SEARCH_STRING_ENTITY_CODE_CAR = "entityCode=\"C0001\"";
    private static final String SEARCH_STRING_ENTITY_CODE_DOMESTIC_AUTO_MAKERS = "entityCode=\"005\"";
    private static final String SEARCH_STRING_ENTITY_CODE_FORD = "entityCode=\"Ford\"";
    private static final String SEARCH_STRING_ENTITY_CODE_TRUCK = "entityCode=\"T0001\"";
    
    private static final String SEARCH_STRING_SRC_ENTITY_CODE_DOMESTIC_AUTO_MAKERS = "sourceEntityCode=\"005\"";
    private static final String SEARCH_STRING_TRG_ENTITY_CODE_FORD = "targetEntityCode=\"Ford\"";
    private static final String SEARCH_STRING_TRG_ENTITY_CODE_TIRES = "targetEntityCode=\"Tires\"";
    

    public void testLexGridExportTestExportAllContent() {
    	try {
			this.init();
	    	CodingSchemeSummary css = new CodingSchemeSummary();
	    	css.setCodingSchemeURI(this.CS_AUTO2_URI);
	    	css.setRepresentsVersion(this.CS_AUTO2_VERSION);
	    	URI destination = new URI(outputFile.toURI().toString().replace(" ", "%20"));
	    	boolean overwrite = true;
	    	
	        // Find the registered extension handling this type of export ...
	        LexGridExport exporter = (LexGridExport) lbsm.getExporter(LexGridExport.name);

	        // Perform the requested action ...
	        AbsoluteCodingSchemeVersionReference acsvr = Constructors.createAbsoluteCodingSchemeVersionReference(css);
	        
	        org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph cng = lbs.getNodeGraph(acsvr.getCodingSchemeURN(), 
	                Constructors.createCodingSchemeVersionOrTagFromVersion(acsvr.getCodingSchemeVersion()),null);
	          
	        org.LexGrid.LexBIG.LexBIGService.CodedNodeSet cns = lbs.getCodingSchemeConcepts(acsvr.getCodingSchemeURN(), 
	                Constructors.createCodingSchemeVersionOrTagFromVersion(acsvr.getCodingSchemeVersion()) );
	        
	        exporter.setCng(cng);
	        exporter.setCns(cns);
	        
	        exporter.export(Constructors.createAbsoluteCodingSchemeVersionReference(css), destination, overwrite,
	                false, false);
	        
	        boolean errorsLogged = exporter.getStatus().getErrorsLogged();
	        Assert.assertFalse(errorsLogged);
	        
	        boolean exportCompleted = exporter.getStatus().getState().equals(ProcessState.COMPLETED); 
	        Assert.assertTrue(exportCompleted);

	        boolean outFileHasContent = this.verifyOutFileHasContent(outputFile, LexGridExportIntegrationTest.SEARCH_STRING_ENTITY_CODE_AUTOMOBILE);
	        Assert.assertTrue(outFileHasContent);       
			
		} catch (LBException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (Throwable th) {
			th.printStackTrace();
		} finally {
			this.cleanUp();
		}
    }
    
    public void init() throws LBException {
    	this.inputFile = new File(this.INPUT_FILE_NAME_AUTO2);
    	this.outputFile = new File(this.OUTPUT_FILE_NAME_AUTO2);
    	
        
    	// lbsm = lbs.getServiceManager(null);
    	
    	ServiceHolder.configureForSingleConfig();
    	
    	lbs = ServiceHolder.instance().getLexBIGService();
        lbsm = lbs.getServiceManager(null);
        LexGrid_Loader loader = (LexGrid_Loader) lbsm.getLoader(LexGridMultiLoaderImpl.name);
        loader.load(this.inputFile.toURI(), true, false);
        
        AbsoluteCodingSchemeVersionReference[] refs = loader.getCodingSchemeReferences();
        for (int i = 0; i < refs.length; i++) {
            AbsoluteCodingSchemeVersionReference ref = refs[i];
            lbsm.activateCodingSchemeVersion(ref);
            System.out.println("Scheme activated>> " + ref.getCodingSchemeURN() + " Version>> "
                    + ref.getCodingSchemeVersion());
        }
        
    }
    
    public String[] createCmdLineArgs() {
    	String[] args = {
    			"-out", this.OUTPUT_FILE_NAME_AUTO2, 
    			"-u", this.CS_AUTO2_URI,
    			"-v", this.CS_AUTO2_VERSION };
    	
    	return args;
    }
    
    public void cleanUp() {
    	doCsRemove(this.CS_AUTO2_URI, this.CS_AUTO2_VERSION);
    	doOutFileRemove(outputFile);
    }
    
    private void doOutFileRemove(File oFile) {
        if(oFile != null && oFile.exists()) {
            boolean result = oFile.delete();
            System.out.println("File delete result: " + result);
            if(result == false) {
            	oFile.deleteOnExit();
            	System.out.println("File delete result was false. Set to delete on exit.");
            }
            
        }    	
    }
    
	private void doCsRemove(String csUri, String csVersion) {
		LexBIGServiceManager lbsm;
		try {
			AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(csUri, csVersion);
			lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);
			lbsm.deactivateCodingSchemeVersion(a,null);
			lbsm.removeCodingSchemeVersion(a);
		} catch (LBException e) {
			e.printStackTrace();
		}
	}

    
    private boolean verifyOutFileHasContent(File outFile, String searchTarget) {
        boolean verifyTrue = false;
        //final String searchTarget = "blah";
        Reader r = null;
        BufferedReader in = null;
        try {
            r = new FileReader(outFile);
            in = new BufferedReader(r);
            if(in != null) {
                boolean done = false;
                String line = null;
                while(!done)
                {
                    line = in.readLine();
                    if(line == null) {
                        done = true;
                    } else {
                        if(line.contains(searchTarget) == true) {
                            verifyTrue = true;
                            done = true;
                        }                        
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }  finally {
            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(r != null) {
                try {
                    r.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return verifyTrue;
    }

}
