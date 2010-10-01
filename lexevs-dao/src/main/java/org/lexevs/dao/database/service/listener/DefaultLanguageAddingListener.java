package org.lexevs.dao.database.service.listener;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.service.event.entity.EntityBatchInsertEvent;
import org.lexevs.dao.database.service.event.entity.EntityInsertOrRemoveEvent;
import org.lexevs.locator.LexEvsServiceLocator;

public class DefaultLanguageAddingListener extends DefaultServiceEventListener {
	
	@Override
	public boolean onPreBatchEntityInsert(EntityBatchInsertEvent event) {
		if(event == null || event.getEntities() == null) {return true;}
		
		CodingScheme cs = getCodingScheme(
				event.getCodingSchemeUri(), 
				event.getVersion());
		
		for(Entity entity : event.getEntities()) {
			this.addDefaultLanguage(
					cs,
					entity);
		}
		
		return true;
	}

	private CodingScheme getCodingScheme(String uri, String version) {
		CodingScheme cs = 
			LexEvsServiceLocator.getInstance().
				getDatabaseServiceManager().
					getCodingSchemeService().
						getCodingSchemeByUriAndVersion(
								uri, 
								version);
		return cs;
	}

	private boolean addDefaultLanguage(CodingScheme cs, Entity entity) {
		String defaultLanguage = cs.getDefaultLanguage();
		if(StringUtils.isNotBlank(defaultLanguage)) {
			for(Property prop : entity.getAllProperties()) {
				if(StringUtils.isBlank(prop.getLanguage())) {
					prop.setLanguage(defaultLanguage);
				}
			}
		}
		return true;
	}

	@Override
	public boolean onPreEntityInsert(EntityInsertOrRemoveEvent event) {
		if(event == null || event.getEntity() == null) {return true;}
	
		CodingScheme cs = getCodingScheme(
				event.getCodingSchemeUri(), 
				event.getVersion());
		
		return this.addDefaultLanguage(
				cs,
				event.getEntity());
	}
}
