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

import junit.framework.Assert;

import org.LexGrid.relations.AssociationSource;
import org.lexgrid.loader.wrappers.ParentIdHolder;

/**
 * The Class EntityAssnsToEntityReproducibleKeyResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ReproducibleAssociationInstanceIdResolver extends AbstractReproducibleIdResolver<ParentIdHolder<AssociationSource>> {

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.data.association.AbstractReproducibleKeyResolver#resolveMultiAttributesKey(java.lang.Object)
	 */
	public String resolveAssociationInstanceId(
			ParentIdHolder<AssociationSource> key) {
		Assert.assertEquals("Only one (1) AssocationSource and one (1) AssociationTarget may be processed at a time." , 
				key.getItem().getTargetCount() == 1);
		
		return super.generateKey(
			key.getParentId(),
			key.getItem().getSourceEntityCode(),
			key.getItem().getSourceEntityCodeNamespace(),
			key.getItem().getTarget()[0].getTargetEntityCode(),
			key.getItem().getTarget()[0].getTargetEntityCodeNamespace());
	}
}
