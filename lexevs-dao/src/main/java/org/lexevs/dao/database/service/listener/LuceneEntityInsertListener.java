
package org.lexevs.dao.database.service.listener;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.lexevs.dao.database.service.event.entity.EntityInsertOrRemoveEvent;
import org.lexevs.dao.index.service.IndexServiceManager;
import org.lexevs.dao.index.service.entity.EntityIndexService;
import org.lexevs.locator.LexEvsServiceLocator;

/**
 * The listener interface for receiving luceneEntityInsert events.
 * The class that is interested in processing a luceneEntityInsert
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addLuceneEntityInsertListener<code> method. When
 * the luceneEntityInsert event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see LuceneEntityInsertEvent
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LuceneEntityInsertListener extends DefaultServiceEventListener {
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.listener.DefaultServiceEventListener#onPostEntityInsert(org.lexevs.dao.database.service.event.entity.EntityInsertOrRemoveEvent)
	 */
	@Override
	public boolean onPostEntityInsert(EntityInsertOrRemoveEvent event) {
		
		IndexServiceManager indexServiceManager = LexEvsServiceLocator.getInstance().getIndexServiceManager();
		EntityIndexService entityIndexService = indexServiceManager.getEntityIndexService();

		AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
		ref.setCodingSchemeURN(event.getCodingSchemeUri());
		ref.setCodingSchemeVersion(event.getVersion());

		if (entityIndexService.doesIndexExist(ref)) {
			entityIndexService.addEntityToIndex(event.getCodingSchemeUri(),
					event.getVersion(), event.getEntity());
		}
		
		return true;
	}
}