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
package org.lexevs.cts2.admin.export;

import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lexevs.cts2.LexEvsCTS2Impl;
import org.lexevs.cts2.admin.load.CodeSystemLoadOperation;
import org.lexevs.cts2.admin.load.ValueSetLoadOperation;
import org.lexevs.cts2.author.ValueSetAuthoringOperation;
import org.lexevs.cts2.core.update.RevisionInfo;
import org.lexevs.cts2.query.ValueSetQueryOperation;
import org.lexevs.dao.database.service.version.AuthoringService;
import org.lexevs.locator.LexEvsServiceLocator;

public class ValueSetExportOperationImplTest {

	private ValueSetExportOperation vsExportOp_ = LexEvsCTS2Impl.defaultInstance().getAdminOperation().getValueSetExportOperation();
	private static File exportFileV1, exportFileV2 ;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		exportFileV1 = new File(System.getProperty("java.io.tmpdir") + File.separator + 
				"Automobiles" + "_" + "1.0" + ".xml");
		exportFileV2 = new File(System.getProperty("java.io.tmpdir") + File.separator + 
				"Property Reference Test 1_null.xml");
		
		ValueSetLoadOperation vsLoadOp = LexEvsCTS2Impl.defaultInstance().getAdminOperation().getValueSetLoadOperation();
		try {
			vsLoadOp.load(new File("resources/testData/cts2/valueSets/VSDOnlyTest.xml").toURI(), null, "LexGrid_Loader", true);
		} catch (LBException e) {
			e.printStackTrace();
		}		
		
		CodeSystemLoadOperation csLoadOp = LexEvsCTS2Impl.defaultInstance().getAdminOperation().getCodeSystemLoadOperation();
		
		try {
			csLoadOp.load(new File("resources/testData/cts2/valueSets/Automobiles.xml").toURI(), null, null, "LexGrid_Loader", true, true, true, "DEV", true);
		} catch (LBException e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		ValueSetQueryOperation vsQueryOp = LexEvsCTS2Impl.defaultInstance().getQueryOperation().getValueSetQueryOperation();
		ValueSetAuthoringOperation vsAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getValueSetAuthoringOperation();
		String revId = UUID.randomUUID().toString();
		RevisionInfo rev = new RevisionInfo();
		rev.setRevisionId(revId);
		
		List<String> uris = vsQueryOp.listAllValueSets(null);
		assertTrue(uris.size() > 0);
		
		for (String uri : uris)
		{
			if (uri.startsWith("SRITEST:"))
				vsAuthOp.removeValueSet(new URI(uri), rev);
		}		
		
		AuthoringService authServ = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAuthoringService();
		assertTrue(authServ.removeRevisionRecordbyId(revId));
		
		assertTrue(exportFileV1.delete());
		assertTrue(exportFileV2.delete());
		
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		AbsoluteCodingSchemeVersionReference ref = 
			Constructors.createAbsoluteCodingSchemeVersionReference("urn:oid:11.11.0.1", "1.0");
		
		lbs.getServiceManager(null).deactivateCodingSchemeVersion(ref, null);
		
		lbs.getServiceManager(null).removeCodingSchemeVersion(ref);
	}

	@Test
	public void testExportValueSetDefinition() throws LBException, URISyntaxException, MarshalException, ValidationException, FileNotFoundException {
		vsExportOp_.exportValueSetDefinition(new URI("SRITEST:AUTO:PropertyRefTest1-VSDONLY"), null, exportFileV1.toString(), true, true);
		
		ValueSetDefinition vsd = ValueSetDefinition.unmarshalValueSetDefinition(new FileReader(exportFileV1));
		
		assertTrue(vsd.getValueSetDefinitionURI().equals("SRITEST:AUTO:PropertyRefTest1-VSDONLY"));
		assertTrue(vsd.getDefinitionEntryCount() == 1);
	}

	@Test
	public void testExportValueSetContents() throws LBException, URISyntaxException, MarshalException, ValidationException, FileNotFoundException {
		AbsoluteCodingSchemeVersionReferenceList csVerList = new AbsoluteCodingSchemeVersionReferenceList();
		csVerList.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference("urn:oid:11.11.0.1", "1.0"));
		
		vsExportOp_.exportValueSetContents(new URI("SRITEST:AUTO:PropertyRefTest1-VSDONLY"), null,
				new File(System.getProperty("java.io.tmpdir")).toURI(), csVerList, null, true, true);
	}
}