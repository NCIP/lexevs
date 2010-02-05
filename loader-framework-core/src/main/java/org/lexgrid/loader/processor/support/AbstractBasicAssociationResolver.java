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
package org.lexgrid.loader.processor.support;

/**
 * The Class AbstractBasicAssociationResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractBasicAssociationResolver<I> implements AssociationResolver<I> {
	
	/** The entity code resolver. */
	private EntityCodeResolver<I> entityCodeResolver;
	
	/** The entity namespace resolver. */
	private EntityNamespaceResolver<I> entityNamespaceResolver;
	
	/** The entity description resolver. */
	private EntityDescriptionResolver<I> entityDescriptionResolver;
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.AssociationResolver#getAssociationName(java.lang.Object)
	 */
	public String getAssociationName(I item) {
		return entityCodeResolver.getEntityCode(item);
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.AssociationResolver#getEntityCode(java.lang.Object)
	 */
	public String getEntityCode(I item) {
		return entityCodeResolver.getEntityCode(item);
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.AssociationResolver#getEntityCodeNamespace(java.lang.Object)
	 */
	public String getEntityCodeNamespace(I item) {
		return entityNamespaceResolver.getEntityNamespace(item);
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.AssociationResolver#getEntityDescription(java.lang.Object)
	 */
	public String getEntityDescription(I item) {
		return entityDescriptionResolver.getEntityDescription(item);
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.AssociationResolver#getForwardName(java.lang.Object)
	 */
	public String getForwardName(I item) {
		return entityCodeResolver.getEntityCode(item);
	}
}
