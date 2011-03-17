package org.cts2.internal.mapper;

import static org.junit.Assert.assertEquals;

import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Presentation;
import org.cts2.core.DesignationFidelityReference;
import org.cts2.core.LanguageReference;
import org.cts2.entity.Designation;
import org.junit.Before;
import org.junit.Test;

public class PresentationToDesignationTest extends BaseDozerBeanMapperTest{

	private Presentation presentation;
	private Designation designation;
	
	
	@Before
	public void initialize() {
		presentation = new Presentation();
		presentation.setDegreeOfFidelity("testFed");
		presentation.setPropertyId("testPropertyID");
		presentation.setLanguage("en");
		Text value = new Text();
		value.setContent("testValue");
		presentation.setValue(value);
		
		presentation.addUsageContext("test usage context 1");
		presentation.addUsageContext("test usage context 2");
		presentation.addUsageContext("test usage context 3");
	
		designation = baseDozerBeanMapper.map(presentation, Designation.class);
		
//		// renderURI
//		designation.setCorrespondingStatement(correspondingStatement);
//		// enum: PREFERRED, ALTERNATIVE, HIDDEN
//		designation.setDesignationRole(designationRole); 
//		designation.setDesignationType(designationType);
//		designation.setFormat(format);
//		designation.setSchema(schema);
//		designation.setUsageContext(vUsageContextArray);
	}
	
	@Test
	public void testGetDegreeOfFidelity() {
		assertEquals("testFed", designation.getDegreeOfFidelity().getContent());
	}
	
	@Test  
	public void testGetPropertyId() {
		assertEquals("testPropertyID", designation.getExternalIdentifier());
	}
	
	@Test 
	public void testGetValue() {
		assertEquals("testValue", designation.getValue());
	}
	
	@Test 
	public void testGetLanguange() {
		assertEquals("en", designation.getLanguage().getContent());
	}
	
	@Test
	public void testGetUsageContext() {
		assertEquals("test usage context 1", designation.getUsageContext(0).getContent());
		assertEquals("test usage context 2", designation.getUsageContext(1).getContent());
		assertEquals("test usage context 3", designation.getUsageContext(2).getContent());
	}
}
