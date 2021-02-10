
package org.lexevs.dao.database.service.listener;

import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.Relations;
import org.lexevs.dao.database.service.event.association.AssociationBatchInsertEvent;

/**
 * The listener interface for receiving abstractPreAssociationInsertValidating events.
 * The class that is interested in processing a abstractPreAssociationInsertValidating
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addAbstractPreAssociationInsertValidatingListener<code> method. When
 * the abstractPreAssociationInsertValidating event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see AbstractPreAssociationInsertValidatingEvent
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractPreAssociationInsertValidatingListener extends
		DefaultServiceEventListener {

	/**
	 * Do validate null namespace.
	 * 
	 * @param uri the uri
	 * @param version the version
	 * @param relation the relation
	 * @param source the source
	 * 
	 * @return true, if successful
	 */
	protected abstract boolean doValidateNullNamespace(String uri, String version,
			Relations relation, AssociationSource source);

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.listener.DefaultServiceEventListener#onPreBatchAssociationInsert(org.lexevs.dao.database.service.event.association.AssociationBatchInsertEvent)
	 */
	@Override
	public boolean onPreBatchAssociationInsert(AssociationBatchInsertEvent event) {
		if (event == null || event.getRelation() == null
				|| event.getSources() == null) {
			return true;
		}

		for (AssociationSource source : event.getSources()) {
			this.doValidateNullNamespace(event.getCodingSchemeUri(), event.getVersion(),
					event.getRelation(), source);
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.listener.DefaultServiceEventListener#onPreAssociationInsert(org.lexevs.dao.database.service.event.association.AssociationBatchInsertEvent)
	 */
	@Override
	public boolean onPreAssociationInsert(AssociationBatchInsertEvent event) {
		if (event == null || event.getRelation() == null
				|| event.getSources() == null) {
			return true;
		}

		return this.doValidateNullNamespace(event.getCodingSchemeUri(), event.getVersion(),
				event.getRelation(), event.getSource());
	}
}