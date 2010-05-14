package org.lexevs.dao.database.service.listener;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.PropertyLink;
import org.lexevs.dao.database.service.event.entity.EntityInsertEvent;


public class InvalidPropertyLinkListener extends DefaultServiceEventListener{

	@Override
	public boolean onPreEntityInsert(EntityInsertEvent entityInsertEvent) {
		List<Entity> entityList = entityInsertEvent.getEntityList();
		
		for(Entity entity : entityList) {
			Property[] properties = entity.getProperty();
			List<PropertyLink> propLinkList = entity.getPropertyLinkAsReference();
			List<PropertyLink> validList = new ArrayList<PropertyLink>();
			
			for(PropertyLink propLink : propLinkList) {
				boolean srcFlag = false, tgtFlag = false;
				for (Property property : properties) {
					if (srcFlag == false && property.getPropertyId().equalsIgnoreCase(propLink.getSourceProperty()))
						srcFlag = true;
					if (tgtFlag == false && property.getPropertyId().equalsIgnoreCase(propLink.getTargetProperty()))
						tgtFlag = true;
				}
				if (srcFlag == true && tgtFlag == true)
					validList.add(propLink);
			}
			
			entity.setPropertyLink(validList);
			
		}
		
		return true;
	}

}
