/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
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
package org.cts2.uri.restriction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cts2.core.EntityReference;
import org.cts2.uri.ValueSetDefinitionDirectoryURI;

/**
 * The Class CodeSystemVersionRestrictionState.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ValueSetDefinitionRestrictionState extends
		RestrictionState<ValueSetDefinitionDirectoryURI> {

	/** The restrict to code system versions restrictions. */
	private Set<RestrictToEntitiesRestriction> restrictToEntitiesRestriction = new HashSet<RestrictToEntitiesRestriction>();

	/**
	 * The Class RestrictToEntitiesRestriction.
	 *
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public static class RestrictToEntitiesRestriction {
		
		/** The code system versions. */
		private List<EntityReference> entityReferences;

		/**
		 * Sets the entity references.
		 *
		 * @param entityReferences the new entity references
		 */
		public void setEntityReferences(List<EntityReference> entityReferences) {
			this.entityReferences = entityReferences;
		}

		/**
		 * Gets the entity references.
		 *
		 * @return the entity references
		 */
		public List<EntityReference> getEntityReferences() {
			return entityReferences;
		}

		
	}

	/**
	 * Sets the restrict to entities restriction.
	 *
	 * @param restrictToEntitiesRestriction the new restrict to entities restriction
	 */
	public void setRestrictToEntitiesRestriction(
			Set<RestrictToEntitiesRestriction> restrictToEntitiesRestriction) {
		this.restrictToEntitiesRestriction = restrictToEntitiesRestriction;
	}

	/**
	 * Gets the restrict to entities restriction.
	 *
	 * @return the restrict to entities restriction
	 */
	public Set<RestrictToEntitiesRestriction> getRestrictToEntitiesRestriction() {
		return restrictToEntitiesRestriction;
	}

}