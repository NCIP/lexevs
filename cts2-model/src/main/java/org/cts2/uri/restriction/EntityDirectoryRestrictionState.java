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

import org.cts2.service.core.NameOrURIList;
import org.cts2.uri.EntityDirectoryURI;

/**
 * The Class CodeSystemVersionRestrictionState.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityDirectoryRestrictionState extends
		RestrictionState<EntityDirectoryURI> {

	/** The restrict to code system versions restrictions. */
	private Set<RestrictToCodeSystemVersionsRestriction> restrictToCodeSystemVersionsRestrictions = new HashSet<RestrictToCodeSystemVersionsRestriction>();

	/**
	 * Sets the restrict to code system versions restrictions.
	 *
	 * @param restrictToCodeSystemVersionsRestrictions the new restrict to code system versions restrictions
	 */
	public void setRestrictToCodeSystemVersionsRestrictions(
			Set<RestrictToCodeSystemVersionsRestriction> restrictToCodeSystemVersionsRestrictions) {
		this.restrictToCodeSystemVersionsRestrictions = restrictToCodeSystemVersionsRestrictions;
	}

	/**
	 * Gets the restrict to code system versions restrictions.
	 *
	 * @return the restrict to code system versions restrictions
	 */
	public Set<RestrictToCodeSystemVersionsRestriction> getRestrictToCodeSystemVersionsRestrictions() {
		return restrictToCodeSystemVersionsRestrictions;
	}

	/**
	 * The Class RestrictToCodeSystemVersionsRestriction.
	 *
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public static class RestrictToCodeSystemVersionsRestriction {
		
		/** The code system versions. */
		private NameOrURIList codeSystemVersions;

		/**
		 * Sets the code system versions.
		 *
		 * @param codeSystemVersions the new code system versions
		 */
		public void setCodeSystemVersions(NameOrURIList codeSystemVersions) {
			this.codeSystemVersions = codeSystemVersions;
		}

		/**
		 * Gets the code system versions.
		 *
		 * @return the code system versions
		 */
		public NameOrURIList getCodeSystemVersions() {
			return codeSystemVersions;
		}
	}

}