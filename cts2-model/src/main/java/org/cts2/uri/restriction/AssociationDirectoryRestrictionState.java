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
import java.util.Set;

import org.cts2.core.TargetExpression;
import org.cts2.service.core.EntityNameOrURI;
import org.cts2.service.core.NameOrURI;
import org.cts2.uri.AssociationDirectoryURI;

/**
 * The Class AssociationDirectoryRestrictionState.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class AssociationDirectoryRestrictionState extends
		RestrictionState<AssociationDirectoryURI> {

	/** The restrict to code system version restrictions. */
	private Set<RestrictToCodeSystemVersionRestriction> restrictToCodeSystemVersionRestrictions = new HashSet<RestrictToCodeSystemVersionRestriction>();
	
	/** The restrict to predicate restrictions. */
	private Set<RestrictToPredicateRestriction> restrictToPredicateRestrictions = new HashSet<RestrictToPredicateRestriction>();
	
	/** The restrict to source entity restrictions. */
	private Set<RestrictToSourceEntityRestriction> restrictToSourceEntityRestrictions = new HashSet<RestrictToSourceEntityRestriction>();
	
	/** The restrict to source or target entity restrictions. */
	private Set<RestrictToSourceOrTargetEntityRestriction> restrictToSourceOrTargetEntityRestrictions = new HashSet<RestrictToSourceOrTargetEntityRestriction>();
	
	/** The restrict to target entity restrictions. */
	private Set<RestrictToTargetEntityRestriction> restrictToTargetEntityRestrictions = new HashSet<RestrictToTargetEntityRestriction>();
	
	/** The restrict to target expression restrictions. */
	private Set<RestrictToTargetExpressionRestriction> restrictToTargetExpressionRestrictions = new HashSet<RestrictToTargetExpressionRestriction>();
	
	/** The restrict to target literal restriction. */
	private Set<RestrictToTargetLiteralRestriction> restrictToTargetLiteralRestriction = new HashSet<RestrictToTargetLiteralRestriction>();

	/**
	 * The Class RestrictToCodeSystemVersionRestriction.
	 *
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public static class RestrictToCodeSystemVersionRestriction {
		
		/** The code system version. */
		private NameOrURI codeSystemVersion;

		/**
		 * Sets the code system version.
		 *
		 * @param codeSystemVersion the new code system version
		 */
		public void setCodeSystemVersion(NameOrURI codeSystemVersion) {
			this.codeSystemVersion = codeSystemVersion;
		}

		/**
		 * Gets the code system version.
		 *
		 * @return the code system version
		 */
		public NameOrURI getCodeSystemVersion() {
			return codeSystemVersion;
		}
	}

	/**
	 * The Class RestrictToPredicateRestriction.
	 *
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public static class RestrictToPredicateRestriction {
		
		/** The predicate. */
		private EntityNameOrURI predicate;

		/**
		 * Sets the predicate.
		 *
		 * @param predicate the new predicate
		 */
		public void setPredicate(EntityNameOrURI predicate) {
			this.predicate = predicate;
		}

		/**
		 * Gets the predicate.
		 *
		 * @return the predicate
		 */
		public EntityNameOrURI getPredicate() {
			return predicate;
		}
	}

	/**
	 * The Class RestrictToSourceEntityRestriction.
	 *
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public static class RestrictToSourceEntityRestriction {
		
		/** The source entity. */
		private EntityNameOrURI sourceEntity;

		/**
		 * Sets the source entity.
		 *
		 * @param sourceEntity the new source entity
		 */
		public void setSourceEntity(EntityNameOrURI sourceEntity) {
			this.sourceEntity = sourceEntity;
		}

		/**
		 * Gets the source entity.
		 *
		 * @return the source entity
		 */
		public EntityNameOrURI getSourceEntity() {
			return sourceEntity;
		}
	}

	/**
	 * The Class RestrictToSourceOrTargetEntityRestriction.
	 *
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public static class RestrictToSourceOrTargetEntityRestriction {
		
		/** The entity. */
		private EntityNameOrURI entity;

		/**
		 * Sets the entity.
		 *
		 * @param entity the new entity
		 */
		public void setEntity(EntityNameOrURI entity) {
			this.entity = entity;
		}

		/**
		 * Gets the entity.
		 *
		 * @return the entity
		 */
		public EntityNameOrURI getEntity() {
			return entity;
		}
	}

	/**
	 * The Class RestrictToTargetEntityRestriction.
	 *
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public static class RestrictToTargetEntityRestriction {
		
		/** The target. */
		private EntityNameOrURI target;

		/**
		 * Sets the target.
		 *
		 * @param target the new target
		 */
		public void setTarget(EntityNameOrURI target) {
			this.target = target;
		}

		/**
		 * Gets the target.
		 *
		 * @return the target
		 */
		public EntityNameOrURI getTarget() {
			return target;
		}
	}

	/**
	 * The Class RestrictToTargetExpressionRestriction.
	 *
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public static class RestrictToTargetExpressionRestriction {
		
		/** The target. */
		private TargetExpression target;

		/**
		 * Sets the target.
		 *
		 * @param target the new target
		 */
		public void setTarget(TargetExpression target) {
			this.target = target;
		}

		/**
		 * Gets the target.
		 *
		 * @return the target
		 */
		public TargetExpression getTarget() {
			return target;
		}
	}

	/**
	 * The Class RestrictToTargetLiteralRestriction.
	 *
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public static class RestrictToTargetLiteralRestriction {
		
		/** The target. */
		private String target;

		/**
		 * Sets the target.
		 *
		 * @param target the new target
		 */
		public void setTarget(String target) {
			this.target = target;
		}

		/**
		 * Gets the target.
		 *
		 * @return the target
		 */
		public String getTarget() {
			return target;
		}
	}

	/**
	 * Gets the restrict to code system version restrictions.
	 *
	 * @return the restrict to code system version restrictions
	 */
	public Set<RestrictToCodeSystemVersionRestriction> getRestrictToCodeSystemVersionRestrictions() {
		return restrictToCodeSystemVersionRestrictions;
	}

	/**
	 * Sets the restrict to code system version restrictions.
	 *
	 * @param restrictToCodeSystemVersionRestrictions the new restrict to code system version restrictions
	 */
	public void setRestrictToCodeSystemVersionRestrictions(
			Set<RestrictToCodeSystemVersionRestriction> restrictToCodeSystemVersionRestrictions) {
		this.restrictToCodeSystemVersionRestrictions = restrictToCodeSystemVersionRestrictions;
	}

	/**
	 * Gets the restrict to predicate restrictions.
	 *
	 * @return the restrict to predicate restrictions
	 */
	public Set<RestrictToPredicateRestriction> getRestrictToPredicateRestrictions() {
		return restrictToPredicateRestrictions;
	}

	/**
	 * Sets the restrict to predicate restrictions.
	 *
	 * @param restrictToPredicateRestrictions the new restrict to predicate restrictions
	 */
	public void setRestrictToPredicateRestrictions(
			Set<RestrictToPredicateRestriction> restrictToPredicateRestrictions) {
		this.restrictToPredicateRestrictions = restrictToPredicateRestrictions;
	}

	/**
	 * Gets the restrict to source entity restrictions.
	 *
	 * @return the restrict to source entity restrictions
	 */
	public Set<RestrictToSourceEntityRestriction> getRestrictToSourceEntityRestrictions() {
		return restrictToSourceEntityRestrictions;
	}

	/**
	 * Sets the restrict to source entity restrictions.
	 *
	 * @param restrictToSourceEntityRestrictions the new restrict to source entity restrictions
	 */
	public void setRestrictToSourceEntityRestrictions(
			Set<RestrictToSourceEntityRestriction> restrictToSourceEntityRestrictions) {
		this.restrictToSourceEntityRestrictions = restrictToSourceEntityRestrictions;
	}

	/**
	 * Gets the restrict to source or target entity restrictions.
	 *
	 * @return the restrict to source or target entity restrictions
	 */
	public Set<RestrictToSourceOrTargetEntityRestriction> getRestrictToSourceOrTargetEntityRestrictions() {
		return restrictToSourceOrTargetEntityRestrictions;
	}

	/**
	 * Sets the restrict to source or target entity restrictions.
	 *
	 * @param restrictToSourceOrTargetEntityRestrictions the new restrict to source or target entity restrictions
	 */
	public void setRestrictToSourceOrTargetEntityRestrictions(
			Set<RestrictToSourceOrTargetEntityRestriction> restrictToSourceOrTargetEntityRestrictions) {
		this.restrictToSourceOrTargetEntityRestrictions = restrictToSourceOrTargetEntityRestrictions;
	}

	/**
	 * Gets the restrict to target entity restrictions.
	 *
	 * @return the restrict to target entity restrictions
	 */
	public Set<RestrictToTargetEntityRestriction> getRestrictToTargetEntityRestrictions() {
		return restrictToTargetEntityRestrictions;
	}

	/**
	 * Sets the restrict to target entity restrictions.
	 *
	 * @param restrictToTargetEntityRestrictions the new restrict to target entity restrictions
	 */
	public void setRestrictToTargetEntityRestrictions(
			Set<RestrictToTargetEntityRestriction> restrictToTargetEntityRestrictions) {
		this.restrictToTargetEntityRestrictions = restrictToTargetEntityRestrictions;
	}

	/**
	 * Gets the restrict to target expression restrictions.
	 *
	 * @return the restrict to target expression restrictions
	 */
	public Set<RestrictToTargetExpressionRestriction> getRestrictToTargetExpressionRestrictions() {
		return restrictToTargetExpressionRestrictions;
	}

	/**
	 * Sets the restrict to target expression restrictions.
	 *
	 * @param restrictToTargetExpressionRestrictions the new restrict to target expression restrictions
	 */
	public void setRestrictToTargetExpressionRestrictions(
			Set<RestrictToTargetExpressionRestriction> restrictToTargetExpressionRestrictions) {
		this.restrictToTargetExpressionRestrictions = restrictToTargetExpressionRestrictions;
	}

	/**
	 * Gets the restrict to target literal restriction.
	 *
	 * @return the restrict to target literal restriction
	 */
	public Set<RestrictToTargetLiteralRestriction> getRestrictToTargetLiteralRestriction() {
		return restrictToTargetLiteralRestriction;
	}

	/**
	 * Sets the restrict to target literal restriction.
	 *
	 * @param restrictToTargetLiteralRestriction the new restrict to target literal restriction
	 */
	public void setRestrictToTargetLiteralRestriction(
			Set<RestrictToTargetLiteralRestriction> restrictToTargetLiteralRestriction) {
		this.restrictToTargetLiteralRestriction = restrictToTargetLiteralRestriction;
	}
}