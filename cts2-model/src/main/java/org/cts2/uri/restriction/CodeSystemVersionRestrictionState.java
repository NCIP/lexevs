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
import org.cts2.service.core.types.ActiveOrAll;
import org.cts2.service.core.types.RestrictionType;
import org.cts2.uri.CodeSystemVersionDirectoryURI;

/**
 * The Class CodeSystemVersionRestrictionState.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CodeSystemVersionRestrictionState extends
		RestrictionState<CodeSystemVersionDirectoryURI> {

	/** The restrict to entities restrictions. */
	private Set<RestrictToEntitiesRestriction> restrictToEntitiesRestrictions = new HashSet<RestrictToEntitiesRestriction>();

	/**
	 * Sets the restrict to entities restriction.
	 *
	 * @param restrictToEntitiesRestrictions the new restrict to entities restriction
	 */
	public void setRestrictToEntitiesRestriction(
			Set<RestrictToEntitiesRestriction> restrictToEntitiesRestrictions) {
		this.restrictToEntitiesRestrictions = restrictToEntitiesRestrictions;
	}

	/**
	 * Gets the restrict to entities restrictions.
	 *
	 * @return the restrict to entities restrictions
	 */
	public Set<RestrictToEntitiesRestriction> getRestrictToEntitiesRestrictions() {
		return restrictToEntitiesRestrictions;
	}

	/**
	 * The Class RestrictToEntitiesRestriction.
	 *
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public static class RestrictToEntitiesRestriction {

		/** The entities. */
		private List<EntityReference> entities;

		/** The all or some. */
		private RestrictionType allOrSome;

		/** The active. */
		private ActiveOrAll active;

		/**
		 * Gets the entities.
		 *
		 * @return the entities
		 */
		public List<EntityReference> getEntities() {
			return entities;
		}

		/**
		 * Sets the entities.
		 *
		 * @param entities the new entities
		 */
		public void setEntities(List<EntityReference> entities) {
			this.entities = entities;
		}

		/**
		 * Gets the all or some.
		 *
		 * @return the all or some
		 */
		public RestrictionType getAllOrSome() {
			return allOrSome;
		}

		/**
		 * Sets the all or some.
		 *
		 * @param allOrSome the new all or some
		 */
		public void setAllOrSome(RestrictionType allOrSome) {
			this.allOrSome = allOrSome;
		}

		/**
		 * Gets the active.
		 *
		 * @return the active
		 */
		public ActiveOrAll getActive() {
			return active;
		}

		/**
		 * Sets the active.
		 *
		 * @param active the new active
		 */
		public void setActive(ActiveOrAll active) {
			this.active = active;
		}

	}
}