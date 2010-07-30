package org.LexGrid.LexBIG.Impl.export.owlrdf;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.export.common.util.CngFactory;
import org.LexGrid.LexBIG.Impl.export.common.util.CnsFactory;
import org.LexGrid.LexBIG.Impl.export.common.util.CodingSchemeChecker;
import org.LexGrid.LexBIG.Impl.export.common.util.ExportDataVerifier;
import org.LexGrid.LexBIG.Impl.export.common.util.Logger;
import org.LexGrid.LexBIG.Impl.export.common.util.AbstractOutputDir;
import org.LexGrid.LexBIG.Impl.export.common.util.TestCleaner;
import org.LexGrid.LexBIG.Impl.export.owlrdf.util.ExportHelper;
import org.LexGrid.LexBIG.Impl.export.owlrdf.util.ImportHelper;
import org.LexGrid.LexBIG.Impl.export.owlrdf.util.OwlRdfExporterTestOutputDir;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;

public class ExportSampleOwl extends TestCase {

    private final String INPUT_FILE_NAME = "resources/testData/sample.owl";
    private final String CS_URI = "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl";
    private final String CS_VERSION = "05.09.bvt";
    
    AbstractOutputDir outputDir;
    
    private String text1 = "05.09.bvt</owl:versionInfo";
    
    public void testOwlRdfExportSampleOwl() {
    	Logger.log("ExportSampleOwl: testOwlRdfExportSampleOwl: entry");
    	boolean rv = false;
		this.init();
		try {
			
			// check if coding scheme already exists
			rv = CodingSchemeChecker.exists(this.CS_URI, this.CS_VERSION);
			assertFalse("coding scheme " + this.CS_URI + "/" + this.CS_VERSION + " should not exist", rv);
			
			// import coding scheme
			rv = ImportHelper.importOwl(this.INPUT_FILE_NAME);
			Assert.assertTrue("loaded LexGrid data from " + this.INPUT_FILE_NAME, rv);
			
			// create CNS and CNG objects
			CodedNodeGraph cng = CngFactory.createCngExportAll(this.CS_URI, this.CS_VERSION);
			CodedNodeSet cns = CnsFactory.createCnsExportAll(this.CS_URI, this.CS_VERSION);
			
			// export 
			rv = ExportHelper.export(
					this.CS_URI, 
					this.CS_VERSION, 
					this.outputDir.getOutputDirAsString(), 
					true, 
					cns, 
					cng);
			Assert.assertTrue("exported data to " + this.outputDir.getOutputDirAsString(), rv);
			
			//-----------------------------------------------------------------
			// verify verify verify verify verify verify verify verify
			//-----------------------------------------------------------------
			String fullyQualifiedOutputFile = ExportHelper.getExportedFileName(this.outputDir.getOutputDirAsString());
			
			rv = ExportDataVerifier.verifyOutFileHasContent(fullyQualifiedOutputFile, this.text1);
			Assert.assertTrue("search string assoc1 should exist in file", rv);
			
			// cleanup 
//			TestCleaner.cleanUp(this.outputDir.getOutputDirAsString(), this.CS_URI, this.CS_VERSION);
			this.outputDir.deleteOutputDir();
			
		} catch (LBException e) {
			e.printStackTrace();
			Assert.fail("ExportSampleOwl: testOwlRdfExportSampleOwl: caught exception: " + e.getMessage());
		}
		Logger.log("ExportSampleOwl: testOwlRdfExportSampleOwl: exit");
    }
        
    public void init() {
    	Logger.log("ExportSampleOwl: init: entry");
    	
    	outputDir = new OwlRdfExporterTestOutputDir();
    	
		// cleanup 
    	Logger.log("ExportSampleOwl: init: clean up any failed test artifacts");
    	this.outputDir = new OwlRdfExporterTestOutputDir();
		TestCleaner.cleanUp(this.outputDir.getOutputDirAsString(), this.CS_URI, this.CS_VERSION);
    	
    	Logger.log("ExportSampleOwl: init: exit");
    }    
}
