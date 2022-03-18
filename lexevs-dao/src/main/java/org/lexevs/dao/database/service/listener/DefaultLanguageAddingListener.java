
package org.lexevs.dao.database.service.listener;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.service.event.entity.EntityBatchInsertEvent;
import org.lexevs.dao.database.service.event.entity.EntityInsertOrRemoveEvent;
import org.lexevs.locator.LexEvsServiceLocator;

/**
 * The listener interface for receiving defaultLanguageAdding events.
 * The class that is interested in processing a defaultLanguageAdding
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addDefaultLanguageAddingListener<code> method. When
 * the defaultLanguageAdding event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see DefaultLanguageAddingEvent
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultLanguageAddingListener extends DefaultServiceEventListener {
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.listener.DefaultServiceEventListener#onPreBatchEntityInsert(org.lexevs.dao.database.service.event.entity.EntityBatchInsertEvent)
	 */
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

	/**
	 * Gets the coding scheme.
	 * 
	 * @param uri the uri
	 * @param version the version
	 * 
	 * @return the coding scheme
	 */
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

	/**
	 * Adds the default language.
	 * 
	 * @param cs the cs
	 * @param entity the entity
	 * 
	 * @return true, if successful
	 */
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

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.listener.DefaultServiceEventListener#onPreEntityInsert(org.lexevs.dao.database.service.event.entity.EntityInsertOrRemoveEvent)
	 */
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