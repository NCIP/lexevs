package org.lexevs.dao.database.service.listener;

import java.util.List;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.service.event.entity.EntityInsertOrRemoveEvent;
import org.lexevs.locator.LexEvsServiceLocator;


public class NullEntityNamespaceListener extends DefaultServiceEventListener{

	@Override
	public boolean onPreEntityInsert(EntityInsertOrRemoveEvent entityInsertEvent) {
		String uri = entityInsertEvent.getCodingSchemeUri();
		String version = entityInsertEvent.getVersion();
		List<Entity> entityList = entityInsertEvent.getEntityList();
		
		for(Entity entity : entityList) {
			if(StringUtils.isBlank(entity.getEntityCodeNamespace())){
				CodingScheme cs = LexEvsServiceLocator.getInstance().
					getDatabaseServiceManager().
					getCodingSchemeService().getCodingSchemeByUriAndVersion(uri, version);
			
				entity.setEntityCodeNamespace(cs.getCodingSchemeName());
			}
		}
		
		return true;
	}

}
