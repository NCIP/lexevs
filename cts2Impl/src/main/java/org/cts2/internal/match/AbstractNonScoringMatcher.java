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
package org.cts2.internal.match;

/**
 * The Class AbstractMatcher.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractNonScoringMatcher extends AbstractMatcher {
	
	/** The N o_ match. */
	protected static float NO_MATCH = 0f;
	
	/** The EXAC t_ match. */
	protected static float EXACT_MATCH = 1f;

	/**
	 * Boolean to score.
	 *
	 * @param isMatch the is match
	 * @return the float
	 */
	private float booleanToScore(boolean isMatch){
		if(isMatch){
			return EXACT_MATCH;
		} else {
			return NO_MATCH;
		}
	}
	
	/**
	 * Do is match.
	 *
	 * @param matchText the match text
	 * @param cadidate the cadidate
	 * @return true, if successful
	 */
	protected float doMatchScore(String matchText, String cadidate){
		return this.booleanToScore(
				this.isMatch(matchText, cadidate));
	}
	
	/**
	 * Checks if is match.
	 *
	 * @param matchText the match text
	 * @param cadidate the cadidate
	 * @return true, if is match
	 */
	protected abstract boolean isMatch(String matchText, String cadidate);
}
