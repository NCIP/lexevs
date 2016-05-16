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
package org.LexGrid.LexBIG.Impl.bugs;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Export.LexGrid_Exporter;
import org.LexGrid.LexBIG.Impl.exporters.LexGridExport;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.loaders.LexGridMultiLoaderImpl;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexBIG.Utility.LBConstants;
import org.LexGrid.LexBIG.Utility.OrderingTestRunner;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.annotation.Order;

import java.io.File;

/**
 * This class should be used as a place to write JUnit tests which show a bug,
 * and pass when the bug is fixed.
 * 
 * @author <A HREF="mailto:kevin.peterson@mayo.edu">Kevin Peterson</A>
 */
@RunWith(OrderingTestRunner.class)
public class GForge19650 extends LexBIGServiceTestCase {
    final static String testID = "GForge19650";

    private LexBIGServiceManager lbsm;
    private LexBIGService lbs;
    
    private static String AUTO_EXPORT_FILE_DIR = "resources/testData/testExport";
    private static String AUTO_EXPORT_FILE = AUTO_EXPORT_FILE_DIR + "/Automobiles_1.0.xml";
    private static String AUTO_EXPORT_MANIFEST = AUTO_EXPORT_FILE_DIR + "/export-autos-manifest.xml";
    
    @Override
    protected String getTestID() {
        return testID;
    }

    @Before
    public void testInitialize() throws LBException {
        lbs = ServiceHolder.instance().getLexBIGService();
        lbsm = lbs.getServiceManager(null);

        assertNotNull(lbs);
        assertNotNull(lbsm);
    }

    @Test
    @Order(1)
    public void testExportAutomobiles() throws LBException{
        // Find the registered extension handling this type of export ...
        LexGrid_Exporter exporter = (LexGrid_Exporter) lbsm.getExporter(LexGridExport.name);

        AbsoluteCodingSchemeVersionReference acvr = Constructors.createAbsoluteCodingSchemeVersionReference(
                LexBIGServiceTestCase.AUTO_SCHEME, 
                LexBIGServiceTestCase.AUTO_VERSION);
        
        // Perform the requested action ...
        exporter.export(acvr, 
                new File(AUTO_EXPORT_FILE_DIR).toURI(), 
                true,
                false, 
                false);
        
        assertTrue(new File(AUTO_EXPORT_FILE).exists());      
    }

    @Test
    @Order(2)
    public void testLoadExportedAutombiles() throws LBParameterException, LBInvocationException, InterruptedException,
    LBException {
        LexGridMultiLoaderImpl loader = (LexGridMultiLoaderImpl) lbsm.getLoader("LexGrid_Loader");
        loader.setCodingSchemeManifestURI(new File(AUTO_EXPORT_MANIFEST).toURI());
        
        loader.load(new File(AUTO_EXPORT_FILE).toURI(), true, true);

        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(3000);
        }

        assertTrue(loader.getStatus().getState() == ProcessState.COMPLETED);
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

        lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], LBConstants.KnownTags.PRODUCTION.toString());
    }

    @Test
    @Order(3)
    public void testGetConceptProperties() throws LBException {
        CodingScheme cs = lbs.resolveCodingScheme(LexBIGServiceTestCase.AUTO_EXPORT_SCHEME, null);
        Properties csProps = cs.getProperties();
        Property[] props = csProps.getProperty();
        assertTrue(props.length == 1);
        
        Property csProperty = props[0];
        assertTrue(csProperty.getValue().getContent().equals("Property Text"));
    }

    @Test
    @Order(3)
    public void testGetConceptPropertiesQualifiers() throws LBException {
        CodingScheme cs = lbs.resolveCodingScheme(LexBIGServiceTestCase.AUTO_EXPORT_SCHEME, null);
        Properties csProps = cs.getProperties();
        Property[] props = csProps.getProperty();
        assertTrue(props.length == 1);
        
        Property csProperty = props[0];
        assertTrue(csProperty.getValue().getContent().equals("Property Text"));
        
        PropertyQualifier[] quals = csProperty.getPropertyQualifier();
        assertTrue(quals.length == 1);
        
        PropertyQualifier csQual = quals[0];
        assertTrue(csQual.getPropertyQualifierName().equals("samplePropertyQualifier"));
        assertTrue(csQual.getValue().getContent().equals("Property Qualifier Text"));
    }

    @Test
    @Order(3)
    public void testGetConceptPropertiesSource() throws LBException {
        CodingScheme cs = lbs.resolveCodingScheme(LexBIGServiceTestCase.AUTO_EXPORT_SCHEME, null);
        Properties csProps = cs.getProperties();
        Property[] props = csProps.getProperty();
        assertTrue(props.length == 1);
        
        Property csProperty = props[0];
        assertTrue(csProperty.getValue().getContent().equals("Property Text"));
        
        Source[] sources = csProperty.getSource();
        assertTrue(sources.length == 1);
        
        Source source = sources[0];
        assertTrue(source.getSubRef().equals("sampleSubRef"));
        assertTrue(source.getRole().equals("sampleRole"));
        assertTrue(source.getContent().equals("lexgrid.org"));  
    }

    @Test
    @Order(3)
    public void testGetConceptPropertiesUsageContext() throws LBException {
        CodingScheme cs = lbs.resolveCodingScheme(LexBIGServiceTestCase.AUTO_EXPORT_SCHEME, null);
        Properties csProps = cs.getProperties();
        Property[] props = csProps.getProperty();
        assertTrue(props.length == 1);
        
        Property csProperty = props[0];
        assertTrue(csProperty.getValue().getContent().equals("Property Text"));
        
        String[] usageContexts = csProperty.getUsageContext();
        assertTrue(usageContexts.length == 1);
        
        String usageContext = usageContexts[0];
        assertTrue(usageContext.equals("sampleUsageContext"));
    }

    @Test
    @Order(4)
    public void testRemoveExportedAutomobiles() throws Exception {
        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                AUTO_EXPORT_URI, AUTO_EXPORT_VERSION);

        lbsm.deactivateCodingSchemeVersion(a, null);
        lbsm.removeCodingSchemeVersion(a);
        assertTrue(new File(AUTO_EXPORT_FILE).delete());
    }

}