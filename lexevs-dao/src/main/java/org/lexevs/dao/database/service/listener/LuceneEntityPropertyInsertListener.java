package org.lexevs.dao.database.service.listener;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.lexevs.dao.database.service.event.property.PropertyUpdateEvent;
import org.lexevs.dao.index.service.IndexServiceManager;
import org.lexevs.dao.index.service.entity.EntityIndexService;
import org.lexevs.locator.LexEvsServiceLocator;

public class LuceneEntityPropertyInsertListener extends
		DefaultServiceEventListener {

	@Override
	public boolean onPostPropertyInsert(PropertyUpdateEvent event) {

		if (event != null && event.getEntity() != null) {

			IndexServiceManager indexServiceManager = LexEvsServiceLocator
					.getInstance().getIndexServiceManager();
			EntityIndexService entityIndexService = indexServiceManager
					.getEntityIndexService();

			AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
			ref.setCodingSchemeURN(event.getCodingSchemeUri());
			ref.setCodingSchemeVersion(event.getCodingSchemeVersion());

			if (entityIndexService.doesIndexExist(ref)) {
				entityIndexService.updateIndexForEntity(event
						.getCodingSchemeUri(), event.getCodingSchemeVersion(),
						event.getEntity());
			}
		}

		return true;
	}

}
