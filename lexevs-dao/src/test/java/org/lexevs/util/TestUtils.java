package org.lexevs.util;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;

public class TestUtils {

	public static boolean entityContainsPropertyWithValue(Entity entity, String propertyValue) {
		for(Property prop : entity.getAllProperties()) {
			if(prop.getValue().getContent().equals(propertyValue)) {
				return true;
			}
		}
		return false;
	}

	public static Property getPropertyWithValue(Entity entity, String propertyValue) {
		for(Property prop : entity.getAllProperties()) {
			if(prop.getValue().getContent().equals(propertyValue)) {
				return prop;
			}
		}
		throw new RuntimeException("Property not found");
	}
	
	public static Property getPropertyWithId(Entity entity, String propertyId) {
		for(Property prop : entity.getAllProperties()) {
			if(prop.getPropertyId().equals(propertyId)) {
				return prop;
			}
		}
		throw new RuntimeException("Property not found");
	}
}
