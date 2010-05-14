package org.lexevs.dao.database.service.listener;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.service.event.entity.EntityInsertEvent;

public class DuplicatePropertyIdListener extends DefaultServiceEventListener {
	@Override
	public boolean onPreEntityInsert(EntityInsertEvent entityInsertEvent) {
		List<Entity> entityList = entityInsertEvent.getEntityList();
		List<Property> validList = new ArrayList<Property>();
		for (Entity entity : entityList) {
			boolean isDuplicated = false;
			for (int i = 0; i < entity.getProperty().length - 1; i++) {
				for (int j = i + 1; i < entity.getProperty().length; j++) {
					if (isDuplicated == false && entity.getProperty()[i].getPropertyId()
							.equalsIgnoreCase(
									entity.getProperty()[j].getPropertyId()))
						isDuplicated = true;
				}
				if (isDuplicated == false)
					validList.add(entity.getProperty()[i]);
			}
			entity.setProperty(validList);
		}
		
		return true;
	}
}
