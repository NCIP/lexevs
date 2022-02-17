
package org.lexevs.dao.database.service.listener;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.lexevs.dao.database.service.event.property.PropertyUpdateEvent;
import org.lexevs.dao.index.service.IndexServiceManager;
import org.lexevs.dao.index.service.entity.EntityIndexService;
import org.lexevs.dao.index.service.search.SourceAssertedValueSetSearchIndexService;
import org.lexevs.locator.LexEvsServiceLocator;

/**
 * The listener interface for receiving luceneEntityPropertyUpdate events.
 * The class that is interested in processing a luceneEntityPropertyUpdate
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addLuceneEntityPropertyUpdateListener<code> method. When
 * the luceneEntityPropertyUpdate event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see LuceneEntityPropertyUpdateEvent
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LuceneEntityPropertyUpdateListener extends DefaultServiceEventListener {

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.listener.DefaultServiceEventListener#onPropertyUpdate(org.lexevs.dao.database.service.event.property.PropertyUpdateEvent)
	 */
	@Override
	public boolean onPropertyUpdate(PropertyUpdateEvent event) {
		
		if (event != null && event.getEntity() != null) {

			IndexServiceManager indexServiceManager = LexEvsServiceLocator
					.getInstance().getIndexServiceManager();
			EntityIndexService entityIndexService = indexServiceManager
					.getEntityIndexService();
			SourceAssertedValueSetSearchIndexService vsIndexSvc = indexServiceManager.
					getAssertedValueSetIndexService();

			AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
			ref.setCodingSchemeURN(event.getCodingSchemeUri());
			ref.setCodingSchemeVersion(event.getCodingSchemeVersion());

			if (entityIndexService.doesIndexExist(ref)) {
				entityIndexService.updateIndexForEntity(event
						.getCodingSchemeUri(), event.getCodingSchemeVersion(),
						event.getEntity());
			}
			if(vsIndexSvc.doesIndexExist(ref)){
				vsIndexSvc.updateIndexForEntity(event
						.getCodingSchemeUri(), event.getCodingSchemeVersion(),
						event.getEntity());
			}
		}
		
		return true;
	}
}