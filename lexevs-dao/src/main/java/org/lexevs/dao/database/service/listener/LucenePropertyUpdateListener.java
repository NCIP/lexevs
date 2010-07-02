package org.lexevs.dao.database.service.listener;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.dao.database.service.event.property.PropertyUpdateEvent;
import org.lexevs.dao.index.service.IndexServiceManager;
import org.lexevs.dao.index.service.entity.EntityIndexService;
import org.lexevs.locator.LexEvsServiceLocator;

public class LucenePropertyUpdateListener extends DefaultServiceEventListener {

	@Override
	public boolean onPropertyUpdate(PropertyUpdateEvent event) {
		IndexServiceManager indexServiceManager = LexEvsServiceLocator.getInstance().getIndexServiceManager();
		EntityIndexService entityIndexService = indexServiceManager.getEntityIndexService();

		EntityService entityService = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getEntityService();

		AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
		ref.setCodingSchemeURN(event.getCodingSchemeUri());
		ref.setCodingSchemeVersion(event.getCodingSchemeVersion());

		if(entityIndexService.doesIndexExist(ref)){

			Entity updatedEntity = entityService.getEntity(
					event.getCodingSchemeUri(),
					event.getCodingSchemeVersion(),
					event.getEntityCode(),
					event.getEntityCodeNamespace());

			entityIndexService.updateIndexForEntity(
					event.getCodingSchemeUri(), 
					event.getCodingSchemeVersion(), 
					updatedEntity);

		}
		return true;
	}
}
