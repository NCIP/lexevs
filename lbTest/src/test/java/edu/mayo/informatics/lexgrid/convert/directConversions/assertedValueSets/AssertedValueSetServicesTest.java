package edu.mayo.informatics.lexgrid.convert.directConversions.assertedValueSets;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.junit.Before;
import org.junit.Test;


public class AssertedValueSetServicesTest {
	Entity entityHasPubValue;
	Entity entityHasBrowserDef;
	Entity entityHasNoBrowserDef;
	
	@Before
	public void setUP(){
		entityHasPubValue = new Entity();
		entityHasPubValue.setProperty(new ArrayList<Property>());
		Property prop = new Property();
		prop.setPropertyName(AssertedValueSetServices.DEFAULT_DO_PUBLISH_NAME);
		prop.setValue(Constructors.createText(AssertedValueSetServices.DEFAULT_DO_PUBLISH_VALUE));
		entityHasPubValue.setProperty(new Property[]{prop});
		entityHasBrowserDef = new Entity();
		Property prop1 = new Property();
		prop1.setPropertyName(AssertedValueSetServices.BROWSER_VS_DEFINITION);
		prop1.setValue(Constructors.createText("&ltp&gt browser def &ltp&gt"));
		Property prop2 = new Property();
		prop2.setPropertyName("DEFINITION");
		prop2.setValue(Constructors.createText("default definition"));
		Property prop3 = new Property();
		prop3.setPropertyName("ALT_DEF");
		prop3.setValue(Constructors.createText("alternate definition"));
		entityHasBrowserDef.setProperty(new Property[]{prop1, prop2, prop3});
		entityHasNoBrowserDef = new Entity();
		entityHasNoBrowserDef.setProperty(new Property[]{prop2, prop3});		
	}
	
	@Test
	public void testIsPublishableValueSet(){
		assertTrue(AssertedValueSetServices.isPublishableValueSet(entityHasPubValue));
	}

	@Test
	public void testGetValueSetDefinition() {
		assertEquals(AssertedValueSetServices.getValueSetDefinition(entityHasBrowserDef), 
				"&ltp&gt browser def &ltp&gt");
		assertEquals(AssertedValueSetServices.getValueSetDefinition(entityHasNoBrowserDef), 
				"default definition");
	}

}
