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
package org.lexgrid.loader.data.association;

import org.LexGrid.persistence.model.EntityAssnsToEntity;
import org.lexgrid.loader.data.association.AbstractReproducibleKeyResolver;

/**
 * The Class EntityAssnsToEntityReproducibleKeyResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityAssnsToEntityReproducibleKeyResolver extends AbstractReproducibleKeyResolver<EntityAssnsToEntity> {

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.data.association.AbstractReproducibleKeyResolver#resolveMultiAttributesKey(java.lang.Object)
	 */
	@Override
	public String resolveMultiAttributesKey(EntityAssnsToEntity key) {
		return super.generateKey(
		key.getEntityCode(),
		key.getContainerName(),
		key.getEntityCodeNamespace(),
		key.getSourceEntityCode(),
		key.getSourceEntityCodeNamespace(),
		key.getTargetEntityCode(),
		key.getTargetEntityCodeNamespace());	
	}
}
