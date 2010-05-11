/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package org.lexevs.dao.database.service.association;

import org.LexGrid.relations.AssociationSource;

/**
 * The Interface AssociationService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface AssociationService {

	/**
	 * Insert association source.
	 * 
	 * @param codingSchemeUri
	 *            the coding scheme uri
	 * @param version
	 *            the version
	 * @param relationContainerName
	 *            the relation container name
	 * @param associationPredicateName
	 *            the association predicate name
	 * @param source
	 *            the source
	 */
	public void insertAssociationSource(String codingSchemeUri, String version,
			String relationContainerName, String associationPredicateName,
			AssociationSource source);
}
