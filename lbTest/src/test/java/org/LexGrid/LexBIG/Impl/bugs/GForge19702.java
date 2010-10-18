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

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;

/**
 * This class should be used as a place to write JUnit tests which show a bug,
 * and pass when the bug is fixed.
 * 
 * @author <A HREF="mailto:kevin.peterson@mayo.edu">Kevin Peterson</A>
 */
public class GForge19702 extends LexBIGServiceTestCase {
    final static String testID = "GForge19702";

    private LexBIGService lbs;
    
    public void setUp(){
        lbs = ServiceHolder.instance().getLexBIGService();   
    }
    
    @Override
    protected String getTestID() {
        return testID;
    }

    /*
     * LexBIG Bug #19702 -
     * https://gforge.nci.nih.gov/tracker/?func=detail&aid=19702&group_id=491&atid=1850
     */
    public void testGetConceptProperties() throws LBException {
        CodingScheme cs = lbs.resolveCodingScheme(LexBIGServiceTestCase.AUTO_SCHEME, null);
        Properties csProps = cs.getProperties();
        Property[] props = csProps.getProperty();
        assertEquals(1,props.length);
        
        Property csProperty = props[0];
        assertTrue(csProperty.getValue().getContent().equals("Property Text"));
    }
    
    public void testGetConceptPropertiesQualifiers() throws LBException {
        CodingScheme cs = lbs.resolveCodingScheme(LexBIGServiceTestCase.AUTO_SCHEME, null);
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
    
    public void testGetConceptPropertiesSource() throws LBException {
        CodingScheme cs = lbs.resolveCodingScheme(LexBIGServiceTestCase.AUTO_SCHEME, null);
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
        assertEquals("lexgrid.org", source.getContent());  
    }
    
    public void testGetConceptPropertiesUsageContext() throws LBException {
        CodingScheme cs = lbs.resolveCodingScheme(LexBIGServiceTestCase.AUTO_SCHEME, null);
        Properties csProps = cs.getProperties();
        Property[] props = csProps.getProperty();
        assertTrue(props.length == 1);
        
        Property csProperty = props[0];
        assertTrue(csProperty.getValue().getContent().equals("Property Text"));
        
        String[] usageContexts = csProperty.getUsageContext();
        assertTrue(usageContexts.length == 1);
        
        String usageContext = usageContexts[0];
        assertEquals("sampleUsageContext",usageContext);
    }  
}