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
package org.lexgrid.loader.processor.support;

/**
 * The Interface AssociationResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface AssociationResolver<I> {

	/**
	 * Gets the entity code.
	 * 
	 * @param item the item
	 * 
	 * @return the entity code
	 */
	public String getEntityCode(I item);
	
	/**
	 * Gets the entity code namespace.
	 * 
	 * @param item the item
	 * 
	 * @return the entity code namespace
	 */
	public String getEntityCodeNamespace(I item);
	
	/**
	 * Gets the container name.
	 * 
	 * @param item the item
	 * 
	 * @return the container name
	 */
	public String getContainerName(I item);
	
	/**
	 * Gets the association name.
	 * 
	 * @param item the item
	 * 
	 * @return the association name
	 */
	public String getAssociationName(I item);
	
	/**
	 * Gets the forward name.
	 * 
	 * @param item the item
	 * 
	 * @return the forward name
	 */
	public String getForwardName(I item);
	
	/**
	 * Gets the reverse name.
	 * 
	 * @param item the item
	 * 
	 * @return the reverse name
	 */
	public String getReverseName(I item);	
	
	/**
	 * Gets the inverse id.
	 * 
	 * @param item the item
	 * 
	 * @return the inverse id
	 */
	public String getInverseId(I item);;	
	
	/**
	 * Gets the checks if is navigable.
	 * 
	 * @param item the item
	 * 
	 * @return the checks if is navigable
	 */
	public boolean getIsNavigable(I item);	
	
	/**
	 * Gets the checks if is transitive.
	 * 
	 * @param item the item
	 * 
	 * @return the checks if is transitive
	 */
	public boolean getIsTransitive(I item);	
	
	/**
	 * Gets the checks if is anti transitive.
	 * 
	 * @param item the item
	 * 
	 * @return the checks if is anti transitive
	 */
	public boolean getIsAntiTransitive(I item);
	
	/**
	 * Gets the checks if is symmetric.
	 * 
	 * @param item the item
	 * 
	 * @return the checks if is symmetric
	 */
	public boolean getIsSymmetric(I item);
	
	/**
	 * Gets the checks if is anti symmetric.
	 * 
	 * @param item the item
	 * 
	 * @return the checks if is anti symmetric
	 */
	public boolean getIsAntiSymmetric(I item);
	
	/**
	 * Gets the checks if is reflexive.
	 * 
	 * @param item the item
	 * 
	 * @return the checks if is reflexive
	 */
	public boolean getIsReflexive(I item);
	
	/**
	 * Gets the checks if is anti reflexive.
	 * 
	 * @param item the item
	 * 
	 * @return the checks if is anti reflexive
	 */
	public boolean getIsAntiReflexive(I item);
	
	/**
	 * Gets the checks if is functional.
	 * 
	 * @param item the item
	 * 
	 * @return the checks if is functional
	 */
	public boolean getIsFunctional(I item);
	
	/**
	 * Gets the checks if is reverse functional.
	 * 
	 * @param item the item
	 * 
	 * @return the checks if is reverse functional
	 */
	public boolean getIsReverseFunctional(I item);	
	
	/**
	 * Gets the entity description.
	 * 
	 * @param item the item
	 * 
	 * @return the entity description
	 */
	public String getEntityDescription(I item);
}