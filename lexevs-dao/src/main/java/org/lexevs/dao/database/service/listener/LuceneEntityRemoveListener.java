package org.lexevs.dao.database.service.listener;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.lexevs.dao.database.service.event.entity.EntityInsertOrRemoveEvent;
import org.lexevs.dao.index.service.IndexServiceManager;
import org.lexevs.dao.index.service.entity.EntityIndexService;
import org.lexevs.locator.LexEvsServiceLocator;

public class LuceneEntityRemoveListener extends DefaultServiceEventListener {
	
	@Override
	public boolean onPostEntityRemove(EntityInsertOrRemoveEvent event) {
		
		IndexServiceManager indexServiceManager = LexEvsServiceLocator
				.getInstance().getIndexServiceManager();
		EntityIndexService entityIndexService = indexServiceManager
				.getEntityIndexService();

		AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
		ref.setCodingSchemeURN(event.getCodingSchemeUri());
		ref.setCodingSchemeVersion(event.getVersion());

		if (entityIndexService.doesIndexExist(ref)) {
			entityIndexService.deleteEntityFromIndex(
					event.getCodingSchemeUri(), event.getVersion(), event
							.getEntity());
		}
		
		return true;
	}
}
