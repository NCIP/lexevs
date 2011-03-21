package org.cts2.internal.mapper;

import static org.junit.Assert.assertEquals;

import org.LexGrid.commonTypes.Text;
import org.junit.Before;
import org.junit.Test;

public class PropertyToPropertyTest extends BaseDozerBeanMapperTest {
	private org.cts2.core.Property ctsProp;
	private org.LexGrid.commonTypes.Property lgProp;

	@Before
	public void initialize() {
		lgProp = new org.LexGrid.commonTypes.Property();
		lgProp.setPropertyId("propertyId");
		Text t = new Text();
		t.setContent("content");
		t.setDataType("string");
		lgProp.setValue(t);
		lgProp.setLanguage("test lang");
		
		ctsProp = baseDozerBeanMapper.map(lgProp, org.cts2.core.Property.class);
//		ctsProp.setCorrespondingStatement(correspondingStatement)
//		ctsProp.setExternalIdentifier(externalIdentifier)
//		ctsProp.setSourceCodingSchemeVersion(sourceCodingSchemeVersion)
//		ctsProp.setTag(tag)
//		ctsProp.setValue(value)
		
	}

	 @Test
	 public void testGetPropertyId() {
		 assertEquals("propertyId", ctsProp.getExternalIdentifier());
	 }
	 
	 @Test
	 public void testGetValue() {
		 assertEquals("content", ctsProp.getValue().getValue());
		 assertEquals("string", ctsProp.getValue().getFormat().getContent());
	 }
	 
	 @Test
	 public void testGetLanguange() {
		 assertEquals("test lang", ctsProp.getValue().getLanguage().getContent());
	 }

}
