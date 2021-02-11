
package org.lexgrid.lexevs.ndfrt.test;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.LexGrid.codingSchemes.CodingScheme;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lexgrid.lexevs.ndfrt.NdfrtXMLProcessor;
import org.lexgrid.lexevs.ndfrt.data.AssociationDef;
import org.lexgrid.lexevs.ndfrt.data.KindDef;
import org.lexgrid.lexevs.ndfrt.data.PropertyDef;
import org.lexgrid.lexevs.ndfrt.data.QualifierDef;
import org.lexgrid.lexevs.ndfrt.data.RoleDef;


public class TestXMLProcessor {
	BufferedReader in = null;
	XMLStreamReader xmlStreamReader;
	CodingScheme cs = new CodingScheme();
	String codingSchemeName = new String();
    List<String> localNameList = new ArrayList<String>();
    URI uri;
    String version;
    NdfrtXMLProcessor processor;
	
	@Before
	public void setUp() throws Exception {
		processor = new NdfrtXMLProcessor();
		uri = new File("test/resources/NDFRT_Public_2012.03.05_TDE.xml").toURI();
        version = processor.processPathForVersion(uri);
        cs = processor.getCodingScheme(uri, null, true, null);
        cs.setRepresentsVersion(version);
	}

	@After
	public void tearDown() throws Exception {
	}

	
	
	@Test
	public void testProcessPathForVersion(){
	 assertNotNull(version);
	 assertEquals(version, "2012.03.05");
	}
	
	@Test
	public void testProcessCodingScheme(){
	assertNotNull(cs);
	assertEquals(cs.getCodingSchemeName(), "NDF-RT2 Public");
	}
	
	
	@Test
	public void testKindDef() throws MalformedURLException, IOException, XMLStreamException, FactoryConfigurationError {
		List<KindDef> kd = processor.getKindDefList(uri, null, true);
		assertNotNull(kd);
		assertTrue(kd.size() > 0);
		assertTrue(kd.get(0).name.equals("DOSE_FORM_KIND"));
		assertTrue(kd.get(8).name.equals("THERAPEUTIC_CATEGORY_KIND"));
	}
	
	@Test
	public void testRoleDef() throws MalformedURLException, IOException, XMLStreamException, FactoryConfigurationError{
		List<RoleDef> kd = processor.getRoleDefList(uri, null, true);
		assertNotNull(kd);
		assertTrue(kd.size() > 0);
		assertTrue(kd.get(0).name.equals("has_DoseForm"));
		assertTrue(kd.get(18).name.equals("has_TC"));
	}

	@Test
	public void testPropDef() throws MalformedURLException, IOException, XMLStreamException, FactoryConfigurationError{
		List<PropertyDef> kd = processor.getPropertyDefList(uri, null, true);
		assertNotNull(kd);
		assertTrue(kd.size() > 0);
		assertTrue(kd.get(0).name.equals("Display_Name"));
		assertTrue(kd.get(23).name.equals("MeSH_CUI"));
	}
	
	@Test
	public void testAssocDef() throws MalformedURLException, IOException, XMLStreamException, FactoryConfigurationError{
		List<AssociationDef> kd = processor.getAssociationDefList(uri, null, true);
		assertNotNull(kd);
		assertTrue(kd.size() > 0);
		assertTrue(kd.get(0).name.equals("Ingredient_1"));
		assertTrue(kd.get(3).name.equals("Heading_Mapped_To"));
	}
	
	@Test
	public void testQualDef() throws MalformedURLException, IOException, XMLStreamException, FactoryConfigurationError{
		List<QualifierDef> kd = processor.getQualifierDefList(uri, null, true);
		assertNotNull(kd);
		assertTrue(kd.size() > 0);
		assertTrue(kd.get(0).name.equals("Source"));
		assertTrue(kd.get(8).name.equals("VUID"));
	}
}