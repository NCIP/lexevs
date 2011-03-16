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
	
		designation = baseDozerBeanMapper.map(presentation, Designation.class);
		
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
}
