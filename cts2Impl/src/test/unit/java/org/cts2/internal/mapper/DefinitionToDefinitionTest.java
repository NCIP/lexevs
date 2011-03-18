package org.cts2.internal.mapper;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.LexGrid.commonTypes.Text;
import org.cts2.core.types.DefinitionRole;
import org.cts2.internal.lexevs.identity.DefaultLexEvsIdentityConverter;
import org.cts2.internal.lexevs.identity.LexEvsIdentityConverter;
import org.cts2.internal.mapper.converter.DefinitionPreferredToDefinitionRoleConverter;
import org.cts2.internal.mapper.converter.PresentationPreferredToDesignationRoleConverter;
import org.junit.Before;
import org.junit.Test;

public class DefinitionToDefinitionTest extends BaseDozerBeanMapperTest{
	@Resource 
	private DefinitionPreferredToDefinitionRoleConverter converter;
	
	private org.LexGrid.concepts.Definition lgDef;
	private org.cts2.core.Definition ctsDef;
	
	@Before
	public void initialize() {
		lgDef = new org.LexGrid.concepts.Definition();
		lgDef.setPropertyId("test propertyid");
		lgDef.setLanguage("test language");
		Text t = new Text();
		t.setDataType("string");
		t.setContent("content");
		lgDef.setValue(t);
		
		LexEvsIdentityConverter lexEvsIdentityConverter = new DefaultLexEvsIdentityConverter();
		this.converter.setLexEvsIdentityConverter(lexEvsIdentityConverter);
		
		ctsDef = baseDozerBeanMapper.map(lgDef, org.cts2.core.Definition.class);
//		ctsDef.setAssertedByCodeSystemVersion(assertedByCodeSystemVersion)	n/a
//		ctsDef.setAssertedInCodeSystemVersion(assertedInCodeSystemVersion)	n/a
//		ctsDef.setCorrespondingStatement(correspondingStatement) 	n/a
//		ctsDef.setDefinitionRole(definitionRole) done
//		ctsDef.setExternalIdentifier(externalIdentifier) 	done
//		ctsDef.setFormat(format)		done
//		ctsDef.setLanguage(language) 	done
//		ctsDef.setSchema(schema)		n/a
//		ctsDef.setUsageContext(usageContext)
//		ctsDef.setValue(value)  	done
	}
	
	@Test
	public void testGetPropertyId(){
		assertEquals("test propertyid", ctsDef.getExternalIdentifier());
	}
	
	@Test
	public void testGetLanguage() {
		assertEquals("test language", ctsDef.getLanguage().getContent());
	}
	
	@Test
	public void testGetValue() {
		assertEquals("content", ctsDef.getValue());
	}
	
	@Test
	public void testGetDataType() {
		assertEquals("string", ctsDef.getFormat().getContent());
	}
	
	@Test
	public void testGetIsPrefferred(){
		assertEquals(DefinitionRole.INFORMATIVE, ctsDef.getDefinitionRole());
	}

}
