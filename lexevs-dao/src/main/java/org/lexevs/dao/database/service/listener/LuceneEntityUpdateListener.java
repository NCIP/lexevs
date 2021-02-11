
package org.lexevs.dao.database.service.listener;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.dao.database.service.event.entity.EntityUpdateEvent;
import org.lexevs.dao.index.service.IndexServiceManager;
import org.lexevs.dao.index.service.entity.EntityIndexService;
import org.lexevs.dao.index.service.search.SourceAssertedValueSetSearchIndexService;
import org.lexevs.locator.LexEvsServiceLocator;

/**
 * The listener interface for receiving luceneEntityUpdate events.
 * The class that is interested in processing a luceneEntityUpdate
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addLuceneEntityUpdateListener<code> method. When
 * the luceneEntityUpdate event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see LuceneEntityUpdateEvent
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LuceneEntityUpdateListener extends DefaultServiceEventListener {

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.listener.DefaultServiceEventListener#onEntityUpdate(org.lexevs.dao.database.service.event.entity.EntityUpdateEvent)
	 */
	@Override
	public boolean onEntityUpdate(EntityUpdateEvent event) {
		IndexServiceManager indexServiceManager = LexEvsServiceLocator.getInstance().getIndexServiceManager();
		EntityIndexService entityIndexService = indexServiceManager.getEntityIndexService();
		SourceAssertedValueSetSearchIndexService vsIndexSvc = indexServiceManager.
				getAssertedValueSetIndexService();
		
		AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
		ref.setCodingSchemeURN(event.getCodingSchemeUri());
		ref.setCodingSchemeVersion(event.getCodingSchemeVersion());

		if (entityIndexService.doesIndexExist(ref)) {
			EntityService entityService = LexEvsServiceLocator.getInstance()
					.getDatabaseServiceManager().getEntityService();
			
			Entity updatedEntity = entityService.getEntity(event
					.getCodingSchemeUri(), event.getCodingSchemeVersion(),
					event.getOriginalEntity().getEntityCode(), event
							.getOriginalEntity().getEntityCodeNamespace());

			entityIndexService.updateIndexForEntity(event.getCodingSchemeUri(),
					event.getCodingSchemeVersion(), updatedEntity);
			
		}
		
		
		return true;
	}
}