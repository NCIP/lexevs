
package org.lexevs.dao.database.service.listener;

import org.lexevs.dao.database.service.event.entity.EntityUpdateEvent;

/**
 * The listener interface for receiving historyTableReplicating events.
 * The class that is interested in processing a historyTableReplicating
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addHistoryTableReplicatingListener<code> method. When
 * the historyTableReplicating event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see HistoryTableReplicatingEvent
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class HistoryTableReplicatingListener extends DefaultServiceEventListener {
	
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.listener.DefaultServiceEventListener#onCodingSchemeUpdate(org.lexevs.dao.database.service.event.codingscheme.CodingSchemeUpdateEvent)
	 */
	@Override
	public boolean onEntityUpdate(final EntityUpdateEvent event) {
		// Do this explicitly in the Services
		return true;
	}
}