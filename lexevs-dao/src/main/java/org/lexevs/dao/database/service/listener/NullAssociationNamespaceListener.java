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

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.apache.commons.lang.StringUtils;
import org.lexevs.locator.LexEvsServiceLocator;

/**
 * The listener interface for receiving nullAssociationNamespace events.
 * The class that is interested in processing a nullAssociationNamespace
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addNullAssociationNamespaceListener<code> method. When
 * the nullAssociationNamespace event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see NullAssociationNamespaceEvent
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class NullAssociationNamespaceListener extends AbstractPreAssociationInsertValidatingListener{

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.listener.AbstractPreAssociationInsertValidatingListener#doValidateNullNamespace(java.lang.String, java.lang.String, org.LexGrid.relations.Relations, org.LexGrid.relations.AssociationSource)
	 */
	@Override
	protected boolean doValidateNullNamespace(String uri, String version,
			Relations relation, AssociationSource source) {

		if( relation == null || source == null ) {
			return true;
		}
		
		String sourceCodingSchemeName = relation.getSourceCodingScheme();

		CodingScheme cs = LexEvsServiceLocator.getInstance()
				.getDatabaseServiceManager().getCodingSchemeService()
				.getCodingSchemeByUriAndVersion(uri, version);
		String codingSchemeName = cs.getCodingSchemeName();
		
		if (StringUtils.isBlank(source.getSourceEntityCodeNamespace())) {

			if (StringUtils.isNotBlank(sourceCodingSchemeName)) {
				source.setSourceEntityCodeNamespace(sourceCodingSchemeName);
			} else {
				source.setSourceEntityCodeNamespace(codingSchemeName);
			}
		}

		for (AssociationTarget target : source.getTarget()) {

			if (target != null
					&& StringUtils.isBlank(target
							.getTargetEntityCodeNamespace())) {
				String targetCodingSchemeName = relation
						.getTargetCodingScheme();

				if (StringUtils.isNotBlank(targetCodingSchemeName)) {
					target.setTargetEntityCodeNamespace(targetCodingSchemeName);
				} else {
					target.setTargetEntityCodeNamespace(codingSchemeName);
				}
			}
		}

		return true;
	}
}