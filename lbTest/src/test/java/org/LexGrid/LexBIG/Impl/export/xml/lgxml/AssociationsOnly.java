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

public class AssociationsOnly extends TestCase {

    private final String INPUT_FILE_NAME_AUTO2 = "resources/testData/lgXmlExport/Automobiles2.xml";
    
    private final String CS_AUTO2_URI = "urn:oid:11.11.0.1";
    private final String CS_AUTO2_VERSION = "1.1";
    
    private AbstractOutputDir outputDir;
    
    private final String[] assoc1 = {"<lgRel:source",
                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"",
                "xsi:schemaLocation=\"http://LexGrid.org/schema/2010/01/LexGrid/codingSchemes  ../master/codingSchemes.xsd\"",
                "sourceEntityCodeNamespace=\"Automobiles\" sourceEntityCode=\"A0001\">",
                "<lgRel:target targetEntityCode=\"Tires\" targetEntityCodeNamespace=\"ExpendableParts\"/>",
            "</lgRel:source>"};
    
    private final String[] entity1 = {    
    		"<lgCon:entity",
    		"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"",
    		"xsi:schemaLocation=\"http://LexGrid.org/schema/2010/01/LexGrid/codingSchemes  ../master/codingSchemes.xsd\"",
    		"isActive=\"true\" status=\"asfd\" entityCode=\"A0001\" entityCodeNamespace=\"Automobiles\">",
    		"<lgCommon:entityDescription>Automobile</lgCommon:entityDescription>",
    		"<lgCon:entityType>concept</lgCon:entityType>",
    		"<lgCon:presentation propertyName=\"textualPresentation\"",
    		"propertyId=\"t1\" propertyType=\"presentation\"", 
    		"language=\"en\" isPreferred=\"true\" matchIfNoContext=\"true\">",
    		"<lgCommon:source>A0001</lgCommon:source>",
    		"<lgCommon:value dataType=\"textplain\">Automobile</lgCommon:value>",
    		"</lgCon:presentation>",
    		"<lgCon:definition propertyName=\"definition\" propertyId=\"p1\"",
    		"propertyType=\"definition\" language=\"en\" isPreferred=\"true\">",
    		"<lgCommon:source>A0001</lgCommon:source>",
    		"<lgCommon:value dataType=\"textplain\">An automobile</lgCommon:value>",
    		"</lgCon:definition>",
    		"</lgCon:entity>"
    };
        
    public void testLexGridExportAssociationsOnly() {
    	Logger.log("AssociationsOnly: testLexGridExportAssociationsOnly: entry");
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
			CodedNodeGraph cng = CngFactory.createCngAssociationsOnly(this.CS_AUTO2_URI, this.CS_AUTO2_VERSION);
			CodedNodeSet cns = CnsFactory.createCnsAssociationsOnly(this.CS_AUTO2_URI, this.CS_AUTO2_VERSION);
			
			// export 
			rv = ExportHelper.export(
					this.CS_AUTO2_URI, 
					this.CS_AUTO2_VERSION, 
					this.outputDir.getOutputDirAsString(), 
					true, 
					cns, 
					cng);
			Assert.assertTrue("exported data to " + this.outputDir.getOutputDirAsString(), rv);
			
			//-----------------------------------------------------------------
			// verify verify verify verify verify verify verify verify
			//-----------------------------------------------------------------
			String fullyQualifiedOutputFile = ExportHelper.getExportedFileName(this.outputDir.getOutputDirAsString());
			
			rv = ExportDataVerifier.verifyOutFileHasContent(fullyQualifiedOutputFile, this.entity1);
			Assert.assertFalse("search string entity1 should not exist in file", rv);

			rv = ExportDataVerifier.verifyOutFileHasContent(fullyQualifiedOutputFile, this.assoc1);
			Assert.assertTrue("search string assoc1 should exist in file", rv);
			
			// cleanup 
			TestCleaner.cleanUp(this.outputDir.getOutputDirAsString(), this.CS_AUTO2_URI, this.CS_AUTO2_VERSION);
			this.outputDir.deleteOutputDir();
			
		} catch (LBException e) {
			e.printStackTrace();
			Assert.fail("AssociationsOnly: testLexGridExportAssociationsOnly: caught exception: " + e.getMessage());
		}
		Logger.log("AssociationsOnly: testLexGridExportAssociationsOnly: exit");
    }
        
    public void init() {
    	Logger.log("AssociationsOnly: init: entry");
    	outputDir = new XmlExporterTestOutputDir();
		// cleanup 
    	Logger.log("AssociationsOnly: init: clean up any failed test artifacts");
		TestCleaner.cleanUp(this.outputDir.getOutputDirAsString(), this.CS_AUTO2_URI, this.CS_AUTO2_VERSION);
    	
    	Logger.log("AssociationsOnly: init: exit");
    }    
}