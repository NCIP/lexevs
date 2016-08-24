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
package org.LexGrid.LexBIG.Impl.export.xml.lgxml;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.export.common.util.AbstractOutputDir;
import org.LexGrid.LexBIG.Impl.export.common.util.CngFactory;
import org.LexGrid.LexBIG.Impl.export.common.util.CnsFactory;
import org.LexGrid.LexBIG.Impl.export.common.util.CodingSchemeChecker;
import org.LexGrid.LexBIG.Impl.export.common.util.ExportDataVerifier;
import org.LexGrid.LexBIG.Impl.export.common.util.Logger;
import org.LexGrid.LexBIG.Impl.export.common.util.TestCleaner;
import org.LexGrid.LexBIG.Impl.export.xml.lgxml.util.ExportHelper;
import org.LexGrid.LexBIG.Impl.export.xml.lgxml.util.ImportHelper;
import org.LexGrid.LexBIG.Impl.export.xml.lgxml.util.XmlExporterTestOutputDir;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;

public class MultipuleRelations extends TestCase {

    private final String INPUT_FILE_NAME = "resources/testData/lgXmlExport/test_ontology_multi_relations.xml";
    
    private final String CS_URI = "1.2.3";
    private final String CS_VERSION = "1.0";
    
    private AbstractOutputDir outputDir;
    
    private final String[] block1 = {    
    		"<lgCon:entity",
    		"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"",
    		"xsi:schemaLocation=\"http://LexGrid.org/schema/2010/01/LexGrid/codingSchemes  http://LexGrid.org/schema/2010/01/LexGrid/codingSchemes.xsd\"",
    		"entityCode=\"1\" entityCodeNamespace=\"colors\"",
    		"isAnonymous=\"false\" isDefined=\"true\">",
    		"<lgCommon:entityDescription>Holder of colors</lgCommon:entityDescription>",
    		"<lgCon:entityType>concept</lgCon:entityType>",
    		"<lgCon:presentation propertyName=\"textPresentation\""
    };    
    
    private final String[] block2 = {
        "<lgRel:source",
            "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"",
            "xsi:schemaLocation=\"http://LexGrid.org/schema/2010/01/LexGrid/codingSchemes  http://LexGrid.org/schema/2010/01/LexGrid/codingSchemes.xsd\"",
            "sourceEntityCodeNamespace=\"colors\" sourceEntityCode=\"0\">",
            "<lgRel:target targetEntityCode=\"3\" targetEntityCodeNamespace=\"colors\"/>",
        "</lgRel:source>"};

    private final String[] block3 = {
                "<lgRel:source",
                    "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"",
                    "xsi:schemaLocation=\"http://LexGrid.org/schema/2010/01/LexGrid/codingSchemes  http://LexGrid.org/schema/2010/01/LexGrid/codingSchemes.xsd\"",
                    "sourceEntityCodeNamespace=\"colors\" sourceEntityCode=\"0\">",
                    "<lgRel:target targetEntityCode=\"5\" targetEntityCodeNamespace=\"colors\"/>",
                "</lgRel:source>"};
    
    private final String[] block4 = {"<lgCS:relations containerName=\"colorsRelation\">"};
    private final String[] block5 = {"<lgCS:relations containerName=\"newRelation\">"};
    
    
    public void testLexGridExportMultipuleRelations() {
    	Logger.log("MultipuleRelations: testLexGridExportMultipuleRelations: entry");
    	boolean rv = false;
		this.init();
		try {
			
			// check if coding scheme already exists
			rv = CodingSchemeChecker.exists(this.CS_URI, this.CS_VERSION);
			assertFalse("coding scheme " + this.CS_URI + "/" + this.CS_VERSION + " should not exist", rv);
			
			// import coding scheme
			rv = ImportHelper.importLgXml(this.INPUT_FILE_NAME);
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
			
			rv = ExportDataVerifier.verifyOutFileHasContent(fullyQualifiedOutputFile, this.block1);
			Assert.assertTrue("search string block1 should exist in file", rv);
			
			rv = ExportDataVerifier.verifyOutFileHasContent(fullyQualifiedOutputFile, this.block2);
			Assert.assertTrue("search string block2 should exist in file", rv);
			
			rv = ExportDataVerifier.verifyOutFileHasContent(fullyQualifiedOutputFile, this.block3);
			Assert.assertTrue("search string block3 should exist in file", rv);
			
			rv = ExportDataVerifier.verifyOutFileHasContent(fullyQualifiedOutputFile, this.block4);
			Assert.assertTrue("search string block4 should exist in file", rv);			
			
			rv = ExportDataVerifier.verifyOutFileHasContent(fullyQualifiedOutputFile, this.block5);
			Assert.assertTrue("search string block5 should exist in file", rv);			
			
			// cleanup 
			TestCleaner.cleanUp(this.outputDir.getOutputDirAsString(), this.CS_URI, this.CS_VERSION);
			this.outputDir.deleteOutputDir();
			
		} catch (LBException e) {
			e.printStackTrace();
			Assert.fail("MultipuleRelations: testLexGridExportMultipuleRelations: caught exception: " + e.getMessage());
		}
		Logger.log("MultipuleRelations: testLexGridExportMultipuleRelations: exit");
    }
    
    public void init() {
    	Logger.log("MultipuleRelations: init: entry");
    	
		// cleanup 
    	outputDir = new XmlExporterTestOutputDir();
    	Logger.log("MultipuleRelations: init: clean up any failed test artifacts");
		TestCleaner.cleanUp(this.outputDir.getOutputDirAsString(), this.CS_URI, this.CS_VERSION);
    	
    	Logger.log("MultipuleRelations: init: exit");
    }    
}