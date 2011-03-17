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

import org.apache.commons.lang.StringUtils;

/**
 * The Class LevenshteinDistanceMatcher.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LevenshteinDistanceMatcher extends AbstractMatcher {

	/* (non-Javadoc)
	 * @see org.cts2.internal.match.AbstractMatcher#doIsMatch(java.lang.String, java.lang.String)
	 */
	@Override
	protected float doMatchScore(String matchText, String cadidate) {
		int longestString = 
			Math.max(matchText.length(), cadidate.length());
		
		int score = StringUtils.getLevenshteinDistance(matchText, cadidate);
		return this.normalize(score, longestString);
	}
	
	/**
	 * Normalize.
	 *
	 * @param score the score
	 * @param longestString the longest string
	 * @return the float
	 */
	protected float normalize(float score, int longestString){
		return 1f - (score / longestString);
	}
}
