/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
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