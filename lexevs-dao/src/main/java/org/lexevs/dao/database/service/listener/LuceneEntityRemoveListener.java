
package org.lexevs.dao.database.service.listener;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.lexevs.dao.database.service.event.entity.EntityInsertOrRemoveEvent;
import org.lexevs.dao.index.service.IndexServiceManager;
import org.lexevs.dao.index.service.entity.EntityIndexService;
import org.lexevs.locator.LexEvsServiceLocator;

/**
 * The listener interface for receiving luceneEntityRemove events.
 * The class that is interested in processing a luceneEntityRemove
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addLuceneEntityRemoveListener<code> method. When
 * the luceneEntityRemove event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see LuceneEntityRemoveEvent
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LuceneEntityRemoveListener extends DefaultServiceEventListener {
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.listener.DefaultServiceEventListener#onPostEntityRemove(org.lexevs.dao.database.service.event.entity.EntityInsertOrRemoveEvent)
	 */
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