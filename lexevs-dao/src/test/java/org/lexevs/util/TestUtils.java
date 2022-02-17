
package org.lexevs.util;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.scheme.PersistenceScheme;

public class TestUtils {
	
	public static boolean containsConceptReference(List<ConceptReference> list, String code) {
		for(ConceptReference ref : list) {
			if(ref.getCode().equals(code)) {
				return true;
			}
		}
		return false;
	}

	public static boolean entityContainsPropertyWithValue(Entity entity, String propertyValue) {
		for(Property prop : entity.getAllProperties()) {
			if(prop.getValue().getContent().equals(propertyValue)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean entityContainsPropertyWithId(Entity entity, String propertyId) {
		for(Property prop : entity.getAllProperties()) {
			if(prop.getPropertyId().equals(propertyId)) {
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
	
	public static class DummyPersistenceScheme implements PersistenceScheme {

		@Override
		public void destroyScheme() {
			//
		}

		@Override
		public LexGridSchemaVersion getLexGridSchemaVersion() {
			return null;
		}

		@Override
		public void initScheme() {
			//
		}

		@Override
		public void registerDaos(DaoManager daoManager) {
			//
		}
		
	}
}