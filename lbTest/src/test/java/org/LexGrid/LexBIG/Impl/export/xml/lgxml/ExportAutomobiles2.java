package org.LexGrid.LexBIG.Impl.export.xml.lgxml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Load.LexGrid_Loader;
import org.LexGrid.LexBIG.Impl.export.xml.lgxml.util.CngFactory;
import org.LexGrid.LexBIG.Impl.export.xml.lgxml.util.CnsFactory;
import org.LexGrid.LexBIG.Impl.export.xml.lgxml.util.CodingSchemeChecker;
import org.LexGrid.LexBIG.Impl.export.xml.lgxml.util.ExportDataVerifier;
import org.LexGrid.LexBIG.Impl.export.xml.lgxml.util.ExportHelper;
import org.LexGrid.LexBIG.Impl.export.xml.lgxml.util.ImportHelper;
import org.LexGrid.LexBIG.Impl.export.xml.lgxml.util.Logger;
import org.LexGrid.LexBIG.Impl.export.xml.lgxml.util.OutputDir;
import org.LexGrid.LexBIG.Impl.export.xml.lgxml.util.TestCleaner;
import org.LexGrid.LexBIG.Impl.exporters.LexGridExport;
import org.LexGrid.LexBIG.Impl.loaders.LexGridMultiLoaderImpl;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;

public class ExportAutomobiles2 extends TestCase {

    private final String INPUT_FILE_NAME_AUTO2 = "resources/testData/Automobiles2.xml";
    
    private final String CS_AUTO2_URI = "urn:oid:11.11.0.1";
    private final String CS_AUTO2_VERSION = "1.1";
    
    private OutputDir outputDir;
    
    private final String SEARCH_STRING_ENTITY_CODE_AUTOMOBILE = "entityCode=\"A0001\"";
    private final String SEARCH_STRING_ENTITY_CODE_CAR = "entityCode=\"C0001\"";
    private final String SEARCH_STRING_ENTITY_CODE_DOMESTIC_AUTO_MAKERS = "entityCode=\"005\"";
    private final String SEARCH_STRING_ENTITY_CODE_FORD = "entityCode=\"Ford\"";
    private final String SEARCH_STRING_ENTITY_CODE_TRUCK = "entityCode=\"T0001\"";
    
    private final String SEARCH_STRING_SRC_ENTITY_CODE_DOMESTIC_AUTO_MAKERS = "sourceEntityCode=\"005\"";
    private final String SEARCH_STRING_TRG_ENTITY_CODE_FORD = "targetEntityCode=\"Ford\"";
    private final String SEARCH_STRING_TRG_ENTITY_CODE_TIRES = "targetEntityCode=\"Tires\"";
    

    private final String[] assoc1 = {"<lgRel:source",
                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"",
                "xsi:schemaLocation=\"http://LexGrid.org/schema/2010/01/LexGrid/codingSchemes  http://LexGrid.org/schema/2010/01/LexGrid/codingSchemes.xsd\"",
                "sourceEntityCodeNamespace=\"Automobiles\" sourceEntityCode=\"A0001\">",
                "<lgRel:target targetEntityCode=\"Tires\" targetEntityCodeNamespace=\"ExpendableParts\"/>",
            "</lgRel:source>"};
    
    
    public void testLexGridExportAutomibiles2() {
    	Logger.log("ExportAutomobiles2: testLexGridExportAutomibiles2: entry");
    	boolean rv = false;
		this.init();
		try {
			
			// check if coding scheme already exists
			rv = CodingSchemeChecker.exists(this.CS_AUTO2_URI, this.CS_AUTO2_VERSION);
			assertFalse("coding scheme " + this.CS_AUTO2_URI + "/" + this.CS_AUTO2_VERSION + " should not exist", rv);
			
			// import coding scheme
			rv = ImportHelper.importLgXml(this.INPUT_FILE_NAME_AUTO2);
			Assert.assertTrue("loaded LexGrid data from " + this.INPUT_FILE_NAME_AUTO2, rv);
			
			// create CNS and CNG objects
			CodedNodeGraph cng = CngFactory.createCngExportAll(this.CS_AUTO2_URI, this.CS_AUTO2_VERSION);
			CodedNodeSet cns = CnsFactory.createCnsExportAll(this.CS_AUTO2_URI, this.CS_AUTO2_VERSION);
			
			// export 
			rv = ExportHelper.export(
					this.CS_AUTO2_URI, 
					this.CS_AUTO2_VERSION, 
					this.outputDir.getOutputDirAsString(), 
					true, 
					cns, 
					cng);
			Assert.assertTrue("loaded LexGrid data from " + this.INPUT_FILE_NAME_AUTO2, rv);
			
			//-----------------------------------------------------------------
			// verify verify verify verify verify verify verify verify
			//-----------------------------------------------------------------
			String fullyQualifiedOutputFile = ExportHelper.getExportedFileName(this.outputDir.getOutputDirAsString());
			
			rv = ExportDataVerifier.verifyOutFileHasContent(fullyQualifiedOutputFile, this.SEARCH_STRING_ENTITY_CODE_AUTOMOBILE);
			Assert.assertTrue("search string " + this.SEARCH_STRING_ENTITY_CODE_AUTOMOBILE + " should exist in file", rv);
			
			rv = ExportDataVerifier.verifyOutFileHasContent(fullyQualifiedOutputFile, this.assoc1);
			Assert.assertTrue("search string assoc1 should exist in file", rv);
			
			
			// cleanup 
			TestCleaner.cleanUp(this.outputDir.getOutputDirAsString(), this.CS_AUTO2_URI, this.CS_AUTO2_VERSION);
			this.outputDir.deleteOutputDir();
			
		} catch (LBException e) {
			e.printStackTrace();
			Assert.fail("ExportAutomobiles2: testLexGridExportAutomibiles2: caught exception: " + e.getMessage());
		}
		Logger.log("ExportAutomobiles2: testLexGridExportAutomibiles2: exit");
    }
    
    public void init() {
    	Logger.log("ExportAutomobiles2: init: entry");
    	
		// cleanup 
    	Logger.log("ExportAutomobiles2: init: clean up any failed test artifacts");
    	this.outputDir = new OutputDir();
		TestCleaner.cleanUp(this.outputDir.getOutputDirAsString(), this.CS_AUTO2_URI, this.CS_AUTO2_VERSION);
    	
    	Logger.log("ExportAutomobiles2: init: exit");
    }    
}
