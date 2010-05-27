package org.lexevs.dao.database.service.listener;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;

public class DuplicatePropertyIdListener extends AbstractPreEntityInsertValidatingListener {
	@Override
	protected boolean doValidate(String uri, String version, Entity entity) {
		Property[] props = entity.getProperty();
		List<Property> validList = new ArrayList<Property>();
		List<String> propIdList = new ArrayList<String>();

		for (Property prop : props) {
			if (!propIdList.contains(prop.getPropertyId().toLowerCase())) {
				validList.add(prop);
				propIdList.add(prop.getPropertyId().toLowerCase());
			}
		}
		entity.setProperty(validList);

		return true;
	}
}
