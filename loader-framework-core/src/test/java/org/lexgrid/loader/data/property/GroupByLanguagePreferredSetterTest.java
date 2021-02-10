
package org.lexgrid.loader.data.property;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Presentation;
import org.junit.Test;
import org.lexgrid.loader.data.DataUtils;

/**
 * The Class GroupByLanguagePreferredSetterTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class GroupByLanguagePreferredSetterTest {

	
	/**
	 * Test set preferred.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testSetPreferred() throws Exception {
		Presentation prop1 = new Presentation();
		prop1.setLanguage("en");
		prop1.setPropertyType("test");
		prop1.setPropertyName("test");
		prop1.setIsPreferred(false);

		
		Presentation prop2 = DataUtils.deepCloneProperty(prop1);
		
		Presentation prop3 = DataUtils.deepCloneProperty(prop1);
		prop3.setLanguage("FR");
		
		Presentation prop4 = DataUtils.deepCloneProperty(prop1);
		prop4.setLanguage("SP");
		
		
		
		List<Presentation> props = new ArrayList<Presentation>();
		
		props.add(prop1);
		props.add(prop2);
		props.add(prop3);
		props.add(prop4);
		
		GroupByLanguagePreferredSetter languagePreferredSetter = new GroupByLanguagePreferredSetter();
		
		
		languagePreferredSetter.setPreferred(props);
		

		assertTrue(props.get(0).getIsPreferred() == true);
		assertTrue(props.get(1).getIsPreferred() == false);
		assertTrue(props.get(2).getIsPreferred() == true);
		assertTrue(props.get(3).getIsPreferred() == true);		
	}
}