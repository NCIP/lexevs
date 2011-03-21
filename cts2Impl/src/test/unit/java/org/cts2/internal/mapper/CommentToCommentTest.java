package org.cts2.internal.mapper;

import static org.junit.Assert.assertEquals;

import org.LexGrid.commonTypes.Text;
import org.junit.Before;
import org.junit.Test;

public class CommentToCommentTest extends BaseDozerBeanMapperTest{
	private org.LexGrid.concepts.Comment lgComm;
	private org.cts2.core.Comment ctsComm;
	@Before
	public void initialize() {
		lgComm = new org.LexGrid.concepts.Comment();
		lgComm.setLanguage("testLanguage");
		Text t = new Text();
		t.setContent("content");
		t.setDataType("string");
		lgComm.setValue(t);
		lgComm.setPropertyId("propertyId");
		
		ctsComm = baseDozerBeanMapper.map(lgComm, org.cts2.core.Comment.class);
//		ctsComm.setExternalIdentifier(externalIdentifier)					done
//		ctsComm.setAssertedByCodeSystemVersion(assertedByCodeSystemVersion)	TODO
//		ctsComm.setAssertedInCodeSystemVersion(assertedInCodeSystemVersion)		TODO
//		ctsComm.setCorrespondingStatement(correspondingStatement)	TODO
//		ctsComm.setFormat(format)		done
//		ctsComm.setLanguage(language)	done
//		ctsComm.setSchema(schema)		TODO
//		ctsComm.setValue(value)			done
	}
	
	@Test
	public void testGetValue() {
		assertEquals("content", ctsComm.getValue());
	}
	
	@Test
	public void testGetLanugange() {
		assertEquals("testLanguage", ctsComm.getLanguage().getContent());
	}
	
	@Test
	public void testGetDataType() {
		assertEquals("string", ctsComm.getFormat().getContent());
	}
	
	@Test
	public void testGetPropertyId(){
		assertEquals("propertyId", ctsComm.getExternalIdentifier());
	}

}
