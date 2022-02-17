
package org.lexgrid.loader.data.property;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.junit.Test;
import org.lexgrid.loader.data.DataUtils;

/**
 * The Class PrefixedSequentialIdSetterTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PrefixedSequentialIdSetterTest {

	/**
	 * Test add ids.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testAddIds() throws Exception {
		Property prop1 = new Property();
		prop1.setLanguage("en");
		prop1.setPropertyType("test");
		prop1.setPropertyName("test");

		
		Property prop2 = DataUtils.deepCloneProperty(prop1);
		
		Property prop3 = DataUtils.deepCloneProperty(prop1);
		prop3.setLanguage("FR");
		
		Property prop4 = DataUtils.deepCloneProperty(prop1);
		prop4.setLanguage("SP");
		
		List<Property> props = new ArrayList<Property>();
		
		props.add(prop1);
		props.add(prop2);
		props.add(prop3);
		props.add(prop4);
		
		PrefixedSequentialIdSetter idSetter = new PrefixedSequentialIdSetter();
		idSetter.setPrefix("TEST_PREFIX");
		idSetter.addIds(props);
		
		assertTrue(props.get(0).getPropertyId().equals("TEST_PREFIX-1"));
		assertTrue(props.get(1).getPropertyId().equals("TEST_PREFIX-2"));
		assertTrue(props.get(2).getPropertyId().equals("TEST_PREFIX-3"));
		assertTrue(props.get(3).getPropertyId().equals("TEST_PREFIX-4"));		
	}
}