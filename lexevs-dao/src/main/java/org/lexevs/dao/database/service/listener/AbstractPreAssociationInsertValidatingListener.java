package org.lexevs.dao.database.service.listener;

import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.Relations;
import org.lexevs.dao.database.service.event.association.AssociationBatchInsertEvent;

public abstract class AbstractPreAssociationInsertValidatingListener extends
		DefaultServiceEventListener {

	protected abstract boolean doValidateNullNamespace(String uri, String version,
			Relations relation, AssociationSource source);

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
