package org.cts2.internal.mapper;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Presentation;
import org.cts2.entity.Designation;
import org.cts2.entity.types.DesignationRole;
import org.cts2.internal.lexevs.identity.DefaultLexEvsIdentityConverter;
import org.cts2.internal.lexevs.identity.LexEvsIdentityConverter;
import org.cts2.internal.mapper.converter.PreferredToDesignationRoleConverter;
import org.junit.Before;
import org.junit.Test;

public class PresentationToDesignationTest extends BaseDozerBeanMapperTest{

	@Resource 
	private PreferredToDesignationRoleConverter converter;
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
		value.setDataType("string");
		presentation.setValue(value);
		presentation.setPropertyName("property name");
		
		presentation.addUsageContext("test usage context 1");
		presentation.addUsageContext("test usage context 2");
		presentation.addUsageContext("test usage context 3");
		
		LexEvsIdentityConverter lexEvsIdentityConverter = new DefaultLexEvsIdentityConverter();
		this.converter.setLexEvsIdentityConverter(lexEvsIdentityConverter);
		
		designation = baseDozerBeanMapper.map(presentation, Designation.class);
		
		//		// renderURI
//		designation.setCorrespondingStatement(correspondingStatement); n/a
//		// enum: PREFERRED, ALTERNATIVE, HIDDEN
//		designation.setDesignationRole(designationRole); 
//		designation.setDesignationType(designationType); - property name
//		designation.setFormat(format); value type
//		designation.setSchema(schema); n/a
		// qualifier, source post to wiki
		
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
	
	@Test
	public void testGetValueDataType() {
		assertEquals("string", designation.getFormat().getContent());
	}
	
	@Test
	public void testGetPropertyName(){
		assertEquals("property name", designation.getDesignationType().getContent());
	}
	
	@Test
	public void testGetIsPreferred() {
		assertEquals(DesignationRole.ALTERNATIVE, designation.getDesignationRole());
		
	}
}
